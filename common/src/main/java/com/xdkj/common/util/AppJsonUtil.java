package com.xdkj.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模拟App请求工具类
 */
public class AppJsonUtil {
    private final static Logger logger = Logger.getLogger(AppJsonUtil.class);

    /**
     * @return
     * @Author 李洪斌
     * @Description //TODO 针对Postman测试 生成模拟App请求的参数(使用Postman进行post请求时会使+变成空格" "方法中已经处理)
     * @Date 2019/6/18 18:25
     * @Param param 传递的参数封装成的Map对象
     **/
    public static String getAppRequestParamForPostman(Map<String, Object> param) {

        try {
            String dataJson = JSON.toJSONString(param);
            logger.info("=====传递的json数据=====>" + dataJson);
            String check = MD5Util.MD5(dataJson);

            Map<String, Object> newParam = new HashMap<String, Object>();

            newParam.put("data", param);
            newParam.put("check", check);

            String requestJson = JSON.toJSONString(newParam);
            logger.info("=====app请求的json数据=====>" + requestJson);

            String requestStr = DES3.encrypt(requestJson).replace("+", "%2B");

            return requestStr;
        } catch (Exception e) {
            logger.info("=====app请求参数生成失败=====>" + e.getMessage());

            e.printStackTrace();
            return null;
        }

    }

    /**
     * @return
     * @Author 李洪斌
     * @Description //TODO 工具类测试生成请求参数
     * @Date 2019/6/18 18:25
     * @Param param 传递的参数封装城的Map对象
     **/
    public static String getAppRequestParam(Map<String, Object> param) {

        try {
            String dataJson = JSON.toJSONString(param);
            logger.info("=====传递的json数据=====>" + dataJson);
            String check = MD5Util.MD5(dataJson);

            Map<String, Object> newParam = new HashMap<String, Object>();

            newParam.put("data", param);
            newParam.put("check", check);

            String requestJson = JSON.toJSONString(newParam);
            logger.info("=====app请求的json数据=====>" + requestJson);

            String requestStr = DES3.encrypt(requestJson);

            // 进行URLDecode解码
            requestStr = URLDecoder.decode(requestStr, "UTF-8");

            return requestStr;
        } catch (Exception e) {
            logger.info("=====app请求参数生成失败=====>" + e.getMessage());

            e.printStackTrace();
            return null;
        }

    }


    /**
     * @return
     * @Author 李洪斌
     * @Description //TODO Java端模拟App请求
     * @Date 2019/6/19 9:48
     * @Param param 传递的参数封装成的Map对象
     **/
    public static String httpPostApp(String apiUrl, Map<String, Object> params) {

        String requestKey = getAppRequestParam(params);
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        HttpRequest request = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String respContent = "";

        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300000).setConnectTimeout(300000).build();//设置请求和传输超时时间

        try {
            HttpPost httpPost = (HttpPost) request;

            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Resource-Type", "");

            //构建请求表单
            List<BasicNameValuePair> paramList = new ArrayList<BasicNameValuePair>();
            paramList.add(new BasicNameValuePair("requestKey", requestKey));

            httpPost.setEntity(new UrlEncodedFormEntity(paramList, "utf-8"));

            response = httpClient.execute(httpPost);
            if (response == null || response.getStatusLine() == null) {
                return null;
            }
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("===================本次请求返回状态码：" + statusCode + "=======================");
            if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                HttpEntity entityRes = response.getEntity();
                if (entityRes != null) {
                    respContent = EntityUtils.toString(entityRes, "UTF-8");
                    //解析响应数据
                    try {
                        // 进行URLDecode解码
                        respContent = URLDecoder.decode(respContent, "UTF-8");
                        return DES3.decrypt(respContent);
                    } catch (Exception e) {
                        return respContent;
                    }


                }
            }
            return null;
        } catch (IOException e) {
            logger.info("请求路径:" + apiUrl);
            logger.info("请求参数:" + requestKey);
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
