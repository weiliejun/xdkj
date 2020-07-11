package com.xdkj.common.components.filesync;

import com.xdkj.common.util.HttpConnectionManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component(value = "fileSynchronizer")
public class FileSynchronizer {
    private final static Logger logger = LoggerFactory.getLogger(FileSynchronizer.class);
    static Properties prop = new Properties();
    //是否上传资源服务器 ：1：是，0：否
    private static String isUploadToResourceServer = "1";
    //资源服务器上传地址
    private static String ReceiveFileURL;
    //本地文件接受地址
    private static String receiverUrl;
    //本地项目ContextPath地址
    private static String receiverContextPath;

    static {
        InputStream is = FileSynchronizer.class.getResourceAsStream("/config/resource.properties");
        try {
            prop.load(is);
            isUploadToResourceServer = prop.getProperty("isUploadToResourceServer");
            ReceiveFileURL = prop.getProperty("resourceServer.ReceiveFileURL");
            receiverUrl = prop.getProperty("resourceServer.AccessURL");
//            receiverContextPath = prop.getProperty("resourceServer.ContextPath");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private HttpConnectionManager httpConnectionManager;

    public static String getIsUploadToPictureServer() {
        return isUploadToResourceServer;
    }

    public static void setIsUploadToPictureServer(String isUploadToResourceServer) {
        FileSynchronizer.isUploadToResourceServer = isUploadToResourceServer;
    }

    public static String getReceiveFileURL() {
        return ReceiveFileURL;
    }

    public static void setReceiveFileURL(String receiveFileURL) {
        ReceiveFileURL = receiveFileURL;
    }

    public static String getReceiverUrl() {
        return receiverUrl;
    }

    public static void setReceiverUrl(String receiverUrl) {
        FileSynchronizer.receiverUrl = receiverUrl;
    }

    public static String getReceiverContextPath() {
        return receiverContextPath;
    }

    public static void setReceiverContextPath(String receiverContextPath) {
        FileSynchronizer.receiverContextPath = receiverContextPath;
    }

    /**
     * 上传图片到指定资源服务器
     */
    public boolean syncFile(String picResourceUrl, File file, String filePath, String fileName) throws Exception {
        logger.info("开始处理接收文件！");
        boolean result = false;

        CloseableHttpClient httpClient = httpConnectionManager.getHttpClient();
        HttpPost post = new HttpPost(picResourceUrl);

        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        ContentType contentType = ContentType.create("text/plain", "UTF-8");
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//加上此行代码解决返回中文乱码问题
        entity.addPart("file", new FileBody(file));
        entity.addPart("filePath", new StringBody(filePath, contentType));
        entity.addPart("fileName", new StringBody(fileName, contentType));
        post.setEntity(entity.build());
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = true;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
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
        return result;
    }

    /**
     * 上传图片到默认资源服务器
     */
    public boolean syncFile(File file, String filePath, String fileName) throws Exception {
        boolean result = false;
        String picResourceUrl = null;
        // 资源服务器路径
        if (isUploadToResourceServer.toString().equalsIgnoreCase("1")) {
            picResourceUrl = ReceiveFileURL;
        } else {
            picResourceUrl = receiverUrl;
        }
        result = syncFile(picResourceUrl, file, filePath, fileName);
        return result;
    }

    /**
     * 通过输入流上传文件到指定服务器
     */
    public boolean syncFileByIs(String picResourceUrl, String filePath, String fileName, InputStream is) throws Exception {
        boolean result = false;
        System.setProperty("sun.jnu.encoding", "utf-8");
        httpConnectionManager = new HttpConnectionManager();
        CloseableHttpClient httpClient = httpConnectionManager.getHttpClient();
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(picResourceUrl);

        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
        int rc = 0;
        while ((rc = is.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        MultipartEntityBuilder entity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentType contentType = ContentType.create("text/plain", "UTF-8");
        entity.addPart("file", new ByteArrayBody(swapStream.toByteArray(), ContentType.DEFAULT_BINARY, fileName));
        entity.addPart("filePath", new StringBody(filePath, contentType));
        entity.addPart("fileName", new StringBody(fileName, contentType));
        post.setEntity(entity.build());
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = true;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 通过输入流上传文件到默认服务器
     */
    public boolean syncFileByIs(String filePath, String fileName, InputStream is) throws Exception {
        boolean result = false;
        String picResourceUrl = ReceiveFileURL;
        result = syncFileByIs(picResourceUrl, filePath, fileName, is);
        return result;
    }

    /**
     * 从目标资源服务器下载文件并上传到指定资源服务器
     *
     * @throws Exception
     */
    public boolean downloadFile(String sourceFileUrl, String targetFileUrl, String filePath, String fileName) throws Exception {
        boolean result = false;
        HttpConnectionManager httpConnectionManager = new HttpConnectionManager();
        CloseableHttpClient httpClient = httpConnectionManager.getHttpClient();
        CloseableHttpResponse response = null;
        InputStream in = null;
        try {
            HttpGet httpget = new HttpGet(sourceFileUrl);
            response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return result;
            }
            in = entity.getContent();
            result = syncFileByIs(targetFileUrl, filePath, fileName, in);
            EntityUtils.consume(entity);
            return result;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    /**
     * 删除指定资源服务器上的图片
     */
    public boolean deleteFile(String deleteFileUrl, String filePath, String fileName) throws Exception {
        logger.info("开始删除接收文件！");
        boolean result = false;
        CloseableHttpClient httpClient = httpConnectionManager.getHttpClient();
        HttpPost post = new HttpPost(deleteFileUrl);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("filePath", filePath));
        urlParameters.add(new BasicNameValuePair("fileName", fileName));
        HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);

        post.setEntity(postParams);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = true;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
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
        return result;
    }

    public String generateIdentifier() {

        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String uuid = format.format(new Date().getTime()) + new Double(Math.random() * 100000).intValue();
        while (uuid.length() < 22) {
            uuid = uuid + "0";
        }
        uuid = uuid.substring(2);

        return uuid;
    }

}
