package com.xdkj.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Http请求工具类
 */
public class HttpClientUtil {
    // 默认编码
    private static final String DEFAULT_CHARSET = "UTF-8";
    //最大连接数
    private static final int DEFAULT_POOL_MAX_TOTAL = 200;
    //单路由最大连接数
    private static final int DEFAULT_POOL_MAX_PER_ROUTE = 20;
    //从池中获取连接超时时间(单位:毫秒)
    private static final int DEFAULT_CONNECT_REQUEST_TIMEOUT = 5 * 1000;
    //向服务端请求超时时间设置(单位:毫秒)
    private static final int DEFAULT_CONNECT_TIMEOUT = 5 * 1000;
    //读超时时间（等待数据超时时间）
    private static final int DEFAULT_SOCKET_TIMEOUT = 6 * 1000;
    private static final Lock lock = new ReentrantLock();
    private static Logger logger = Logger.getLogger(HttpClientUtil.class);
    //全局连接池对象
    private static PoolingHttpClientConnectionManager connManager = null;
    //建Http请求配置参数
    private static RequestConfig requestConfig = null;
    //重试策略
    private static HttpRequestRetryHandler httpRequestRetryHandler = null;
    //http客户端
    private static CloseableHttpClient httpClient = null;
    //异常链接监控线程
    private static ScheduledExecutorService monitorExecutor = null;

    /**
     * 配置连接池信息
     */
    public static void init() {
        //Http请求配置参数
        requestConfig = RequestConfig.custom()
                // 获取连接超时时间
                .setConnectionRequestTimeout(DEFAULT_CONNECT_REQUEST_TIMEOUT)
                // 请求超时时间
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                // 响应超时时间
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();

        /**
         * 测出超时重试机制为了防止超时不生效而设置
         *  如果直接放回false,不重试
         *  这里会根据情况进行判断是否重试
         */
        httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 3) {// 如果已经重试了3次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return true;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        //连接池创建
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connManager.setMaxTotal(DEFAULT_POOL_MAX_TOTAL);
        connManager.setDefaultMaxPerRoute(DEFAULT_POOL_MAX_PER_ROUTE);
        // 创建httpClient
        httpClient = HttpClients.custom()
                // 把请求相关的超时信息设置到连接客户端
                .setDefaultRequestConfig(requestConfig)
                // 把请求重试设置到连接客户端
                .setRetryHandler(httpRequestRetryHandler)
                // 配置连接池管理对象
                .setConnectionManager(connManager)
                .build();

        //开启监控线程,对异常和空闲线程进行关闭
        monitorExecutor = Executors.newScheduledThreadPool(1);
        monitorExecutor.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //关闭异常连接
                connManager.closeExpiredConnections();
                // 可选的, 关闭30秒内不活动的连接
                connManager.closeIdleConnections(30, TimeUnit.SECONDS);
            }
        }, 0, 5000, TimeUnit.MILLISECONDS);
    }


    /**
     * 获取Http客户端连接对象
     */
    public static CloseableHttpClient getHttpClient() {
        //初始化连接池
        if (httpClient == null) {
            try {
                lock.lock();
                if (httpClient == null) {
                    init();
                }
            } finally {
                lock.unlock();
            }
        }
        return httpClient;
    }

    /**
     * GET请求
     */
    public static String doGet(String url) {
        return doGet(url, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    /**
     * GET请求
     */
    public static String doGet(String url, Map<String, String> params) {
        return doGet(url, Collections.EMPTY_MAP, params);
    }

    /**
     * GET请求
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, String> params) {
        //构建GET请求头
        String apiUrl = getUrlWithParams(url, params);
        HttpGet httpGet = new HttpGet(apiUrl);
        //设置header信息
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient().execute(httpGet);
            if (response == null || response.getStatusLine() == null) {
                return null;
            }

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entityRes = response.getEntity();
                if (entityRes != null) {
                    return EntityUtils.toString(entityRes, DEFAULT_CHARSET);
                }
            }
            return null;
        } catch (IOException e) {
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("释放链接异常", e);
                }
            }
        }
        return null;
    }

    /**
     * POST请求
     */
    public static String doPost(String apiUrl, Map<String, String> params) {
        return doPost(apiUrl, Collections.EMPTY_MAP, params);
    }

    /**
     * POST请求
     */
    public static String doPost(String apiUrl, Map<String, String> headers, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(apiUrl);
        //配置请求headers
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        //配置请求参数
        if (params != null && params.size() > 0) {
            HttpEntity entityReq = getUrlEncodedFormEntity(params);
            httpPost.setEntity(entityReq);
        }
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient().execute(httpPost);
            if (response == null || response.getStatusLine() == null) {
                return null;
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entityRes = response.getEntity();
                if (entityRes != null) {
                    return EntityUtils.toString(entityRes, DEFAULT_CHARSET);
                }
            }
            return null;
        } catch (IOException e) {
            logger.info("请求路径:" + apiUrl);
            logger.info("请求参数:" + JSON.toJSONString(params));
            logger.error("post请求异常", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("释放链接异常", e);
                }
            }
        }
        return null;
    }

    private static HttpEntity getUrlEncodedFormEntity(Map<String, String> params) {
        List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                    .getValue());
            pairList.add(pair);
        }
        return new UrlEncodedFormEntity(pairList, Charset.forName(DEFAULT_CHARSET));
    }

    private static String getUrlWithParams(String url, Map<String, String> params) {
        boolean first = true;
        StringBuilder sb = new StringBuilder(url);
        for (String key : params.keySet()) {
            char ch = '&';
            if (first == true) {
                ch = '?';
                first = false;
            }
            String value = params.get(key);
            try {
                String sval = URLEncoder.encode(value, DEFAULT_CHARSET);
                sb.append(ch).append(key).append("=").append(sval);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("参数编码异常", e);
            }
        }
        return sb.toString();
    }

    /**
     * 关闭连接池
     */
    public static void closeConnectionPool() {
        try {
            httpClient.close();
            connManager.close();
            monitorExecutor.shutdown();
        } catch (IOException e) {
            logger.error("关闭连接池异常", e);
        }
    }


    public static void main(String[] args) {
        //System.out.println(doGet("http://www.baidu.com"));
        Map<String, String> params = new HashMap<String, String>();
        params.put("userName", "1370000088");
        params.put("password", "111111");
        params.put("validateCode", "44ms");
        System.out.println(doPost("https://www.vjinke.com/onlogin", params));
        closeConnectionPool();
    }

}
