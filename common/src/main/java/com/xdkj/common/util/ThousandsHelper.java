package com.xdkj.common.util;

import java.text.NumberFormat;
import java.util.Locale;

public class ThousandsHelper {
    public static String formateThousands(Object objStr, boolean addZero) {
        String str = "";
        if (objStr == null || "".equals(objStr)) {
            if (addZero) {
                str = "0.00";
            } else {
                str = "0";
            }
            return str;
        }
        str = objStr.toString();
        NumberFormat format = NumberFormat.getInstance(Locale.CHINA);
        format.setMaximumFractionDigits(2);
        if (addZero) {
            format.setMinimumFractionDigits(2);
        }
        str = format.format(Double.parseDouble(str));
        return str;
    }
}
