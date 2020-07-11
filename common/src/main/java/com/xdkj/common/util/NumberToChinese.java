package com.xdkj.common.util;

public class NumberToChinese {
    public static String toChinese(int n) {
        String[] n1 = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
        String[] n2 = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿"};
        int d = 9;
        int t, j;
        String s = "";
        while (d != 0) {
            j = (int) Math.pow(10, d);
            t = n / j;
            if (t != 0) {
                s = s + n1[t] + n2[d];
                n = n - t * j;
            }
            d = d - 1;
        }
        s = s + n1[n];
        if (s.length() == 3 && s.substring(1, 2).equals("十"))
            s = s.substring(1);
        return s;
    }
}
