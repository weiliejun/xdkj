package com.xdkj.common.util;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class RequestHelper {

    /**
     * 将request中的参数封装成javaBean的list集合
     */
    public static <T> List<T> requestToList(HttpServletRequest request, Class<T> T) {
        List<T> list = new ArrayList<T>();
        Enumeration<String> paramNames = request.getParameterNames();
        String simpleName = T.getSimpleName();
        String paramName = "";// 属性名
        boolean flag = false;// 是否包含类型加点的属性
        while (paramNames.hasMoreElements()) {
            paramName = paramNames.nextElement();
            if (paramName.contains(simpleName + ".")) {
                flag = true;
                break;
            }
        }
        if (paramName == null || "".equals(paramName)) {
            return list;
        }
        String[] paramArr = request.getParameterValues(paramName);
        Field[] fields = T.getDeclaredFields();
        Object value = null;
        String[] values = null;
        try {
            for (int i = 0; i < paramArr.length; i++) {
                T t = (T) T.newInstance();

                for (int j = 0; j < fields.length; j++) {
                    String fieldName = fields[j].getName();
                    if (fieldName.contains("serialVersionUID")) {// serialVersionUID字段不赋值
                        continue;
                    }
                    if (flag) {
                        values = request.getParameterValues(simpleName + "." + fieldName);
                    } else {
                        values = request.getParameterValues(fieldName);
                    }
                    if (values == null || values.length == 0) {// 表单中没有这个字段名，跳过
                        continue;
                    }
                    if (values[i] == null || "".equals(values[i])) {// 这个字段值为空，跳过
                        continue;
                    }
                    // 判断类型并做类型转换
                    if (fields[j].getType() == String.class) {
                        value = values[i];
                    }
                    if (fields[j].getType() == BigDecimal.class) {
                        value = new BigDecimal(values[i]);
                    }
                    fields[j].setAccessible(true);
                    fields[j].set(t, value);
                }
                list.add(t);
            }
        } catch (InstantiationException e) {
            throw new RuntimeException("request封装对象" + T.toString() + "发生异常！", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("request封装对象" + T.toString() + "发生异常！", e);
        }
        return list;

    }

    /**
     * 将request中的参数封装成javaBean对象
     */
    public static <T> T requestToObject(HttpServletRequest request, Class<T> T) {
        Field[] fields = T.getDeclaredFields();
        Object value = null;
        T t = null;
        try {
            t = (T) T.newInstance();
            for (int j = 0; j < fields.length; j++) {
                String fieldName = fields[j].getName();
                if (fieldName.contains("serialVersionUID")) {// serialVersionUID字段不赋值
                    continue;
                }
                value = request.getParameterValues(fieldName);
                if (value == null && !"".equals(value)) {
                    continue;
                }
                // 判断类型并做类型转换
                if (fields[j].getType() == String.class) {
                    value = (String) value;
                }
                if (fields[j].getType() == BigDecimal.class) {
                    value = new BigDecimal((String) value);
                }
                fields[j].setAccessible(true);
                fields[j].set(t, value);
            }
        } catch (InstantiationException e) {
            throw new RuntimeException("request封装对象" + T.toString() + "发生异常！", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("request封装对象" + T.toString() + "发生异常！", e);
        }
        return t;

    }

    /**
     * 将request中的参数封装成javaBean对象
     */
    public static <T> T requestToObjectByClass(HttpServletRequest request, Class<T> T) {
        Field[] fields = T.getDeclaredFields();
        String simpleName = T.getSimpleName();
        Object value = null;
        T t = null;
        try {
            t = (T) T.newInstance();
            for (int j = 0; j < fields.length; j++) {
                String fieldName = fields[j].getName();
                if (fieldName.contains("serialVersionUID")) {// serialVersionUID字段不赋值
                    continue;
                }
                String result = "";
                value = request.getParameterValues(simpleName + "." + fieldName);
                if (value == null) {
                    continue;
                }
                if (value instanceof String[]) {
                    String[] arr = (String[]) value;

                    for (int i = 0; i < arr.length; i++) {
                        if (i == 0) {
                            result += arr[i];
                        } else {
                            result += "," + arr[i];
                        }
                    }
                } else {
                    result = (String) value;
                }
                fields[j].setAccessible(true);
                // 判断类型并做类型转换
                if (fields[j].getType() == String.class) {
                    fields[j].set(t, result);
                }
                if (fields[j].getType() == BigDecimal.class) {
                    fields[j].set(t, new BigDecimal((String) result));
                }
            }
        } catch (InstantiationException e) {
            throw new RuntimeException("request封装对象" + T.toString() + "发生异常！", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("request封装对象" + T.toString() + "发生异常！", e);
        }
        return t;

    }
}
