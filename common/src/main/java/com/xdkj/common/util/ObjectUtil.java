package com.xdkj.common.util;

import java.io.*;

public class ObjectUtil {
    /**
     * @return
     * @description 对象转byte[]
     * @version 1.0
     * @author 张可乐
     * @update 2017-10-26 下午2:22:21
     */
    public static byte[] objectToBytes(Object obj) {
        ByteArrayOutputStream bo = null;
        ObjectOutputStream oo = null;
        byte[] bytes = null;
        try {
            bo = new ByteArrayOutputStream();
            oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("对象转byte[]异常", e);
        } finally {
            if (bo != null) {
                try {
                    bo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("对象转byte[]异常", e);
                }
            }
            if (oo != null) {
                try {
                    oo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("对象转byte[]异常", e);
                }
            }
        }
        return bytes;
    }

    /**
     * @param bytes
     * @return
     * @description byte[]转对象
     * @version 1.0
     * @author 张可乐
     * @update 2017-10-26 下午2:24:58
     */
    public static Object bytesToObject(byte[] bytes) {
        Object obj = null;
        ByteArrayInputStream in = null;
        new ByteArrayInputStream(bytes);
        ObjectInputStream sIn = null;
        try {
            in = new ByteArrayInputStream(bytes);
            sIn = new ObjectInputStream(in);
            obj = sIn.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("byte[]转对象异常", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("byte[]转对象异常", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("byte[]转对象异常", e);
                }
            }
            if (sIn != null) {
                try {
                    sIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("byte[]转对象异常", e);
                }
            }
        }
        return obj;
    }
}
