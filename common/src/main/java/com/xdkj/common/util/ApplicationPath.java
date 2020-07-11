package com.xdkj.common.util;

public class ApplicationPath {
    public static String getRootPath() {
        // 因为类名为"ApplicationPath"，因此" ApplicationPath.class"一定能找到
        String result = ApplicationPath.class.getResource("ApplicationPath.class").toString();
        System.out.println(result);
        int index = result.indexOf("WEB-INF");
        if (index == -1) {
            index = result.indexOf("bin");
        }
        // maven项目中类的存在位置
        if (index == -1) {
            index = result.indexOf("target");
        }
        result = result.substring(0, index);
        if (result.startsWith("jar")) {
            // 当class文件在jar文件中时，返回"jar:file:/F:/ ..."样的路径
            result = result.substring(10);
        } else if (result.startsWith("file")) {
            // 当class文件在class文件中时，返回"file:/F:/ ..."样的路径
            result = result.substring(6);
        }
        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);// 不包含最后的"/"
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getRootPath());

    }

}
