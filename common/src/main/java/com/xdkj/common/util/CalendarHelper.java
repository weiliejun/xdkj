package com.xdkj.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CalendarHelper {
    //日历插件网格数量
    public static final int CAL_GRID_42 = 42;
    //日历插件网格数量
    public static final int CAL_GRID_35 = 35;

    // 判断是否为闰年
    public static boolean isRun(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            return true;
        } else {
            return false;
        }
    }

    // 判断某年的哪月多少天
    public static int calDay(int year, int mth) {
        int days = 31;
        switch (mth) {
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                if (isRun(year)) {
                    days = 29;
                } else {
                    days = 28;
                }
                break;
        }
        return days;

    }

    // 判断是某年哪月第一天是周内第几天
    public static int calFirstDay(int year, int mth) {
        // 创建 Calendar 对象
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, mth - 1, 1);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return week - 1;
    }


    // 获取日历模型的起始日期和结束日期
    public static Map<String, String> getCalendarModel(int year, int mth, int grid) {
        // 创建 Calendar 对象
        Calendar startDate = Calendar.getInstance(); // 当月的第一天
        startDate.set(year, mth - 1, 1);
        int week = startDate.get(Calendar.DAY_OF_WEEK) - 1; // 某年某月第一天是周内第几天
        int days = calDay(year, mth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int prevDayNum = 0;
        int laterNum = 0;
        switch (grid) {
            case 35:
                prevDayNum = (week - 1) >= 0 ? week - 1 : 6;// 日历显示上个月的日期数
                laterNum = grid - prevDayNum - days;// 日历显示下个月的日期数
                break;
            case 42:
                prevDayNum = week > 0 ? week % 7 : 7;// 日历显示上个月的日期数
                laterNum = grid - prevDayNum - days;// 日历显示下个月的日期数
                break;
            default:
                prevDayNum = week > 0 ? week % 7 : 7;// 日历显示上个月的日期数
                laterNum = grid - prevDayNum - days;// 日历显示下个月的日期数
                break;
        }
        startDate.add(Calendar.DATE, -prevDayNum); // 日历的第一个日期

        Calendar endDate = Calendar.getInstance(); // 当月的最后一天
        endDate.set(year, mth - 1, days);
        endDate.add(Calendar.DATE, laterNum);// 日历的最后一个日期

        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("startDate", dateFormat.format(startDate.getTime()));
        resultMap.put("endDate", dateFormat.format(endDate.getTime()));
        return resultMap;
    }

    public static void main(String[] args) {
        Map<String, String> models = getCalendarModel(2016, 12, CAL_GRID_35);
        for (Entry<String, String> entry : models.entrySet()) {
            System.out.println("------" + entry.getValue());
        }

    }

}
