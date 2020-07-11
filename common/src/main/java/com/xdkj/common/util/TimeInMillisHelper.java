package com.xdkj.common.util;

import java.util.Calendar;

public class TimeInMillisHelper {
    public static long getTimeInMillis(String orgTime) {
        Calendar c = Calendar.getInstance();
        if (null != orgTime) {
            int year = Integer.valueOf(orgTime.substring(0, 4));
            int month = Integer.valueOf(orgTime.substring(5, 7)) - 1;
            int day = Integer.valueOf(orgTime.substring(8, 10));
            int hour = 0;
            int minute = 0;
            int second = 0;
            if (orgTime.length() >= 11) {
                hour = Integer.valueOf(orgTime.substring(11, 13));
                minute = Integer.valueOf(orgTime.substring(14, 16));
                second = Integer.valueOf(orgTime.substring(17, 19));
            }
            c.set(year, month, day, hour, minute, second);
        } else {
            return 0;
        }
        return c.getTimeInMillis() / 1000;
    }
}
