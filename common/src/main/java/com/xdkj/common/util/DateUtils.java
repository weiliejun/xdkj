package com.xdkj.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat timeFmt = new SimpleDateFormat("HHmmss");

    public static Date addTime(int field, int addNum) {
        Calendar cal = Calendar.getInstance();
        cal.add(field, addNum);
        return cal.getTime();
    }

    public static String getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String msg = "";
        switch (dayOfWeek) {
            case 1:
                msg = "星期日";
                break;
            case 2:
                msg = "星期一";
                break;
            case 3:
                msg = "星期二";
                break;
            case 4:
                msg = "星期三";
                break;
            case 5:
                msg = "星期四";
                break;
            case 6:
                msg = "星期五";
                break;
            case 7:
                msg = "星期六";
                break;
            default:
                break;
        }
        return msg;
    }

    public static int getCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }
}
