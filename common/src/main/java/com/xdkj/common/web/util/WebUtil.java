package com.xdkj.common.web.util;


import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WebUtil {
    /**
     * 是否是ajax请求
     */
    public static boolean isAjax(HttpServletRequest request) {
        String XRequested = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(XRequested)) {
            return true;
        }
        return false;
    }

    /**
     * 生成token
     */
    public synchronized static String generateToken(String sid) {
        try {
            byte id[] = sid.getBytes();
            byte now[] =
                    new Long(System.currentTimeMillis()).toString().getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(id);
            md.update(now);
            return (toHex(md.digest()));
        } catch (IllegalStateException e) {
            return (null);
        } catch (NoSuchAlgorithmException e) {
            return (null);
        }
    }

    public static String toHex(byte[] buffer) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);

        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 0x0f, 16));
        }

        return sb.toString();
    }


}