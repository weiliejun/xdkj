package com.xdkj.common.web.base;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * getInputStream()和getReader()
 * 都只能读取一次，由于RequestBody是流的形式读取，那么流读了一次就没有了，所以只能被调用一次。
 * 先将RequestBody保存，然后通过Servlet自带的HttpServletRequestWrapper类覆盖getReader
 * ()和getInputStream()方法，
 * 使流从保存的body读取。然后再Filter中将ServletRequest替换为ServletRequestWrapper
 *
 * @description
 */
public class MyHttpServletRequest extends HttpServletRequestWrapper {
    private String body;
    private String requestKey;
    private HttpServletRequest _request;

    public MyHttpServletRequest(HttpServletRequest request) {
        super(request);
        _request = request;

        StringBuffer jsonStr = new StringBuffer();
        try (BufferedReader bufferedReader = _request.getReader()) {
            String line;
            while ((line = bufferedReader.readLine()) != null)
                jsonStr.append(line);
            body = jsonStr.toString();
            JSONObject json = JSONObject.parseObject(jsonStr.toString());
            requestKey = json.getString("requestKey");
        } catch (Exception e) {

        }
    }

    public String getRequestKey() {
        return requestKey;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                body.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

}
