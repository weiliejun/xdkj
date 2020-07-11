package com.xdkj.common.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;

@Component(value = "httpConnectionManager")
public class HttpConnectionManager {
    private static final String CHAR_SET = "UTF-8";

    // public HttpClient getHttpClient() {
    // return httpClient;// new DefaultHttpClient(cm, httpParams);
    // }
    // 日志
    private static final Logger logger = Logger.getLogger(HttpConnectionManager.class);
    private static Object LOCAL_LOCK = new Object();
    // private static CloseableHttpClient httpClient =
    // HttpClientBuilder.create().build();
    // /**
    // * 最大连接数
    // */
    // private final static int MAX_TOTAL_CONNECTIONS = 800;
    // /**
    // * 每个路由最大连接数
    // */
    // private final static int MAX_ROUTE_CONNECTIONS = 400;
    // /**
    // * 连接超时时间
    // */
    // private final static int CONNECT_TIMEOUT = 9000000;
    // /**
    // * 读取超时时间
    // */
    // private final static int READ_TIMEOUT = 9000000;
    //
    // static {
    // httpParams = new BasicHttpParams();
    //
    // try {
    // SSLContext ctx = SSLContext.getInstance("SSL");
    // X509TrustManager tm = new X509TrustManager() {
    // public void checkClientTrusted(X509Certificate[] xcs, String string)
    // throws CertificateException {
    // }
    // public void checkServerTrusted(X509Certificate[] xcs, String string)
    // throws CertificateException {
    // }
    // public X509Certificate[] getAcceptedIssuers() {
    // return null;
    // }
    // };
    // ctx.init(null, new TrustManager[]{tm}, null);
    // // SSLSocketFactory ssf = new SSLSocketFactory(ctx, new
    // // AllowAllHostnameVerifier());
    // //
    // // SchemeRegistry schemeRegistry = new SchemeRegistry();
    // // schemeRegistry.register(new Scheme("http", 80,
    // // PlainSocketFactory.getSocketFactory()));
    // // schemeRegistry.register(new Scheme("https", 443, ssf));
    // //
    // // cm = new ThreadSafeClientConnManager(schemeRegistry);
    // // // Increase max total connection to 200
    // // cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
    // // // Increase default max connection per route to 20
    // // cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
    // // // Increase max connections for localhost:80 to 50
    // // HttpHost localhost = new HttpHost("locahost", 80);
    // // cm.setMaxForRoute(new HttpRoute(localhost), 50);
    //
    // // 设置协议http和https对应的处理socket链接工厂的对象
    // X509HostnameVerifier hostnameVerifier =
    // SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
    // SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx,
    // hostnameVerifier);
    // Registry<ConnectionSocketFactory> socketFactoryRegistry =
    // RegistryBuilder.<ConnectionSocketFactory> create().register("http",
    // PlainConnectionSocketFactory.getSocketFactory()).register("https",
    // sslsf).build();
    // PoolingHttpClientConnectionManager cm = new
    // PoolingHttpClientConnectionManager(socketFactoryRegistry);
    // httpClient =
    // HttpClients.custom().setConnectionManager(cm).setSSLSocketFactory(sslsf).build();
    //
    // // 设置连接超时时间
    // HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
    // // 设置读取超时时间
    // HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);
    // } catch (NoSuchAlgorithmException e) {
    // e.printStackTrace();
    // } catch (KeyManagementException e) {
    // e.printStackTrace();
    // }
    // }
    /**
     * 最大连接数400
     */
    private static int MAX_CONNECTION_NUM = 800;

    /**
     * 单路由最大连接数80
     */
    private static int MAX_PER_ROUTE = 400;

    /**
     * 向服务端请求超时时间设置(单位:毫秒)
     */
    private static int SERVER_REQUEST_TIME_OUT = 9000000;

    /**
     * 服务端响应超时时间设置(单位:毫秒)
     */
    private static int SERVER_RESPONSE_TIME_OUT = 9000000;

    /**
     * 连接池管理对象
     */
    PoolingHttpClientConnectionManager cm = null;

    /**
     * 构造函数
     */
    public HttpConnectionManager() {
    }

    /**
     * 创建线程安全的HttpClient
     * <p>
     * 客户端超时设置
     *
     * @return
     */
    public CloseableHttpClient getHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SERVER_REQUEST_TIME_OUT).setConnectTimeout(SERVER_RESPONSE_TIME_OUT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(this.getPoolManager()).build();
        return httpClient;
    }

    /**
     * 功能描述: <br>
     * 初始化连接池管理对象
     *
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    private PoolingHttpClientConnectionManager getPoolManager() {
        if (null == cm) {
            synchronized (LOCAL_LOCK) {
                if (null == cm) {

                    try {
                        SSLContext ctx = SSLContext.getInstance("SSL");
                        X509TrustManager tm = new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                        };
                        ctx.init(null, new TrustManager[]{tm}, null);
                        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
                        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx.getSocketFactory(), hostnameVerifier);
                        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", socketFactory).register("http", new PlainConnectionSocketFactory()).build();
                        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                        cm.setMaxTotal(MAX_CONNECTION_NUM);
                        cm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
                    } catch (Exception e) {
                    }

                }
            }
        }
        return cm;
    }

    /**
     * @param requestURI 请求地址
     * @param method     get | post
     * @return String
     */
    public String sendHttpRequest(String requestURI, String method) {
        String respContent = "";

        HttpClient httpClient = getHttpClient();
        method = method.toLowerCase();
        HttpUriRequest httpUriRequest = null;
        if (method.equals("post"))
            httpUriRequest = new HttpPost(requestURI);
        else if (method.equals("get"))
            httpUriRequest = new HttpGet(requestURI);
        else
            new Exception("method传入错误，应为post，get").printStackTrace();
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpUriRequest);
            if (response == null) {
                throw new NullPointerException("后台调用返回response响应对象为空！");
            }
            if (response.getStatusLine().getReasonPhrase().equals("OK") && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                respContent = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return respContent;
    }
}
