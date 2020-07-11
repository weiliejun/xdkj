package com.xdkj.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;

public class ClobHelper {

    public static String clobToString(Clob clob) {

        if (clob == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        char[] charArray = new char[1024];
        int i = -1;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(clob.getCharacterStream());
            while ((i = reader.read(charArray, 0, 1024)) != -1) {
                sb.append(charArray, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
