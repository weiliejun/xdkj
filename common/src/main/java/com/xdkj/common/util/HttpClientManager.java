package com.xdkj.common.util;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientManager {
    public static final long RELEASE_CONNECTION_WAIT_TIME = 5000;// 监控连接间隔
    private static Logger logger = Logger.getLogger(HttpClientManager.class);
    private static PoolingHttpClientConnectionManager clientConnectionManager = null;
    private static RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
    private static PoolingHttpClientConnectionManager httpClientConnectionManager = null;
    private static LaxRedirectStrategy redirectStrategy = null;
    private static HttpRequestRetryHandler myRetryHandler = null;
    private static SSLConnectionSocketFactory sslConnectionSocketFactory = null;

    static {
        initHttpClient();
    }

    /**
     * httpclient连接池并初始化
     */
    public static void initHttpClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            // 设置协议http和https对应的处理socket链接工厂的对象
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx.getSocketFactory(), hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslsf)
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .build();
            clientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            clientConnectionManager.setMaxTotal(50);
            clientConnectionManager.setDefaultMaxPerRoute(25);
        } catch (Exception e) {
            logger.warn("httpUtils init get exception:", e);
        }

    }

    public static CloseableHttpClient getHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()//  
                .setConnectTimeout(3000)//  
                .setSocketTimeout(3000)//  
                // 忽略cookie,如果不需要登陆最好去掉,否则修改策略保存cookie即可  
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)//  
                .setConnectionRequestTimeout(6000)//  
                // .setProxy(ip)//设置代理ip,不设置就用本机  
                .build();
        // 连接池配置  
        CloseableHttpClient httpClient = HttpClients.custom()//  
                .setSSLSocketFactory(sslConnectionSocketFactory)//  
                .setConnectionManager(httpClientConnectionManager)//  
                .setDefaultRequestConfig(requestConfig)//  
                .setRedirectStrategy(redirectStrategy)//  
                .setRetryHandler(myRetryHandler)//  
                .build();

        return httpClient;
    }


    /**
     * get请求
     *
     * @param url
     * @param headers
     * @return
     */
    public static HttpEntity httpGet(String url, Map<String, Object> headers) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpRequest httpGet = new HttpGet(url);
        if (headers != null && !headers.isEmpty()) {
            httpGet = setHeaders(headers, httpGet);
        }
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute((HttpGet) httpGet);
            HttpEntity entity = response.getEntity();
            return entity;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * post请求,使用json格式传参
     *
     * @param url
     * @param headers
     * @param data
     * @return
     */
    public static String httpPost(String url, String data) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpRequest request = new HttpPost(url);
        CloseableHttpResponse response = null;
        String respContent = "";
        try {
            HttpPost httpPost = (HttpPost) request;
            httpPost.setEntity(new StringEntity(data, ContentType.create("application/json", "UTF-8")));
            response = httpClient.execute(httpPost);
            if (response == null) {
                throw new NullPointerException("后台调用返回response响应对象为空！");
            }
            if (response.getStatusLine().getReasonPhrase().equals("OK") && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                respContent = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
            return respContent;
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 使用表单键值对传参
     */
    public static HttpEntity PostForm(String url, Map<String, Object> headers, List<NameValuePair> data) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpRequest request = new HttpPost(url);
        if (headers != null && !headers.isEmpty()) {
            request = setHeaders(headers, request);
        }
        CloseableHttpResponse response = null;
        UrlEncodedFormEntity uefEntity;
        try {
            HttpPost httpPost = (HttpPost) request;
            uefEntity = new UrlEncodedFormEntity(data, "UTF-8");
            httpPost.setEntity(uefEntity);
            // httpPost.setEntity(new StringEntity(data, ContentType.create("application/json", "UTF-8")));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            return entity;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 设置请求头信息
     *
     * @param headers
     * @param request
     * @return
     */
    private static HttpRequest setHeaders(Map<String, Object> headers, HttpRequest request) {
        for (Map.Entry entry : headers.entrySet()) {
            if (!entry.getKey().equals("Cookie")) {
                request.addHeader((String) entry.getKey(), (String) entry.getValue());
            } else {
                Map<String, Object> Cookies = (Map<String, Object>) entry.getValue();
                for (Map.Entry entry1 : Cookies.entrySet()) {
                    request.addHeader(new BasicHeader("Cookie", (String) entry1.getValue()));
                }
            }
        }
        return request;
    }

    public static Map<String, String> getCookie(String url) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpRequest httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute((HttpGet) httpGet);
            Header[] headers = response.getAllHeaders();
            Map<String, String> cookies = new HashMap<String, String>();
            for (Header header : headers) {
                cookies.put(header.getName(), header.getValue());
            }
            return cookies;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public static void main(String[] args) throws ParseException, IOException {
        Map<String, Object> headers = null;
        HttpEntity resp = httpGet("http://blog.csdn.net/nethackatschool/article/details/51965673", headers);
        resp.getContentType();
        String respContent = EntityUtils.toString(resp, "UTF-8");
        System.out.println(respContent);
    }

}
