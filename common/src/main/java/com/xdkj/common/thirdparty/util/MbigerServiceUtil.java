package com.xdkj.common.thirdparty.util;

import com.alibaba.fastjson.JSON;
import com.xdkj.common.util.AESUtil;
import com.xdkj.common.util.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * Http请求工具类
 */
public class MbigerServiceUtil {
    private static Logger logger = Logger.getLogger(MbigerServiceUtil.class);

    private static final String TOKEN = "UeIG3iiNMwDj7o4p";

    private static final String KEY = "Pb5SJeJQ2lWRDaYA";


    /**
     * 实名认证发送请求
     */
    public static String httpPost(String apiUrl, Map<String, String> params) {
        String data = JSON.toJSONString(params);
        //请求参数需要加密
        data = AESUtil.encryptGBK(data, KEY);
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        apiUrl = apiUrl + "?token=" + TOKEN;
        HttpRequest request = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String respContent = "";
        try {
            HttpPost httpPost = (HttpPost) request;
            httpPost.setEntity(new StringEntity(data, ContentType.create("text/plain", "UTF-8")));
            response = httpClient.execute(httpPost);
            if (response == null || response.getStatusLine() == null) {
                return null;
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entityRes = response.getEntity();
                if (entityRes != null) {
                    respContent = EntityUtils.toString(entityRes, "UTF-8");
                    return AESUtil.decryptGBK(respContent, KEY);
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

}
