package com.xdkj.common.util;


import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
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
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientHelper {
    public static final long RELEASE_CONNECTION_WAIT_TIME = 5000;// 监控连接间隔
    private static Logger logger = Logger.getLogger(HttpClientHelper.class);
    private static String DEFAULT_CHARSET = "UTF-8";
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
            clientConnectionManager.setMaxTotal(200);
            clientConnectionManager.setDefaultMaxPerRoute(50);
        } catch (Exception e) {
            logger.warn("httpUtils init get exception:", e);
        }

    }

    public static CloseableHttpClient getHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()//  
                .setConnectTimeout(30000)//  
                .setSocketTimeout(30000)//  
                // 忽略cookie,如果不需要登陆最好去掉,否则修改策略保存cookie即可  
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)//  
                .setConnectionRequestTimeout(60000)//  
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
     * post请求,使用json格式传参
     *
     * @param url
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
            httpPost.setEntity(new StringEntity(data, ContentType.create("text/plain", "UTF-8")));
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
     * post请求,map格式传参
     *
     * @param url
     * @param params
     * @return
     */
    public static String httpPost(String url, Map<String, String> params) {
        String respContent = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        //参数处理
        UrlEncodedFormEntity formEntiry = null;
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        try {
            formEntiry = new UrlEncodedFormEntity(nvps, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("post请求,参数编码异常");
        }
        post.setEntity(formEntiry);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(post);
            if (response == null) {
                throw new NullPointerException("后台调用返回response响应对象为空！");
            }
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                respContent = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return respContent;
    }


    public static String sendHttpPost(String requestURI, String params) {
        String respContent = "";

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        if (requestURI.startsWith("https:")) {
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
                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
                PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                httpClient = HttpClients.custom().setConnectionManager(cm).setSSLSocketFactory(sslsf).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HttpPost post = new HttpPost(requestURI);
        /*
         * //设置参数 List<NameValuePair> list = new ArrayList<NameValuePair>();
         * list.add(new BasicNameValuePair("param", params)); list.add(new
         * BasicNameValuePair("appType","qiatu")); UrlEncodedFormEntity entity =
         * new UrlEncodedFormEntity(list,"UTF-8");
         */
        StringEntity entity = new StringEntity(params, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        post.setEntity(entity);
        HttpResponse response = null;
        try {
            response = httpClient.execute(post);
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

    public static String sendHttpRequest(String requestURI, MultipartEntityBuilder entity) {
        String respContent = "";

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        if (requestURI.startsWith("https:")) {
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

                // 忽略校验
                // SSLSocketFactory ssf = new SSLSocketFactory(ctx);
                // ssf.setHostnameVerifier(new AllowAllHostnameVerifier());
                // ClientConnectionManager ccm =
                // httpClient.getConnectionManager();
                // SchemeRegistry sr = ccm.getSchemeRegistry();
                // sr.register(new Scheme("https", 443, ssf));

                // 设置协议http和https对应的处理socket链接工厂的对象
                HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx.getSocketFactory(), hostnameVerifier);
                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
                PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                httpClient = HttpClients.custom().setConnectionManager(cm).setSSLSocketFactory(sslsf).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HttpPost post = new HttpPost(requestURI);
        post.setEntity(entity.build());
        HttpResponse response = null;
        try {
            response = httpClient.execute(post);
            if (response == null) {
                throw new NullPointerException("后台调用返回response响应对象为空！");
            }
            if ((response.getStatusLine().getReasonPhrase().equals("200") || response.getStatusLine().getReasonPhrase().equals("OK")) && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                respContent = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return respContent;
    }

    /**
     * @param requestURI
     * @return
     * @description 通过url获取数据流
     */
    public static HttpResponse downLoad2(String requestURI) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        if (requestURI.startsWith("https:")) {
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
                // 忽略校验
                // SSLSocketFactory ssf = new SSLSocketFactory(ctx);
                // ssf.setHostnameVerifier(new AllowAllHostnameVerifier());
                // ClientConnectionManager ccm =
                // httpClient.getConnectionManager();
                // SchemeRegistry sr = ccm.getSchemeRegistry();
                // sr.register(new Scheme("https", 443, ssf));

                // 设置协议http和https对应的处理socket链接工厂的对象
                HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx.getSocketFactory(), hostnameVerifier);
                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
                PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                httpClient = HttpClients.custom().setConnectionManager(cm).setSSLSocketFactory(sslsf).build();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HttpResponse httpResponse = null;
        try {
            HttpGet httpGet = new HttpGet(requestURI);
            httpResponse = httpClient.execute(httpGet);
            return httpResponse;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResponse;
    }

    public static OutputStream downLoad(String requestURI) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        if (requestURI.startsWith("https:")) {
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
                // 忽略校验
                // SSLSocketFactory ssf = new SSLSocketFactory(ctx);
                // ssf.setHostnameVerifier(new AllowAllHostnameVerifier());
                // ClientConnectionManager ccm =
                // httpClient.getConnectionManager();
                // SchemeRegistry sr = ccm.getSchemeRegistry();
                // sr.register(new Scheme("https", 443, ssf));

                // 设置协议http和https对应的处理socket链接工厂的对象
                HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx.getSocketFactory(), hostnameVerifier);
                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
                PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                httpClient = HttpClients.custom().setConnectionManager(cm).setSSLSocketFactory(sslsf).build();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        InputStream in = null;
        OutputStream out = null;
        ByteArrayOutputStream outArr = new ByteArrayOutputStream();
        try {
            HttpGet httpGet = new HttpGet(requestURI);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            in = entity.getContent();

            byte[] buffer = new byte[4096];
            int readLength = 0;
            while ((readLength = in.read(buffer)) > 0) {
                outArr.write(buffer, 0, readLength);
            }
            return outArr;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

    public static void main(String[] args) {
        // 15020315072310241057
        String url = "https://123.126.102.219:40446/business/timer/doUserRecommendRelationCheck";
        // 远程调用
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        try {
            entity.addPart("condition", new StringBody("true", ContentType.DEFAULT_TEXT));
            HttpClientHelper.sendHttpRequest(url, entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("-----------------");
    }

}
