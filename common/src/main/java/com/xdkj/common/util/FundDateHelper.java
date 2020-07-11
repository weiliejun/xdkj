package com.xdkj.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基金收益日期工具类
 *
 * @author xxx
 */
public class FundDateHelper {
    /**
     * 工作日
     */
    private final static String WORKDAY = "0";

    /**
     * 休息日
     */
    private final static String DAY_OFF = "1";

    /**
     * 节假日
     */
    private final static String HOLIDAYS = "2";


    private final static int HOUR_FLAG = 15;

    /**
     * 工资宝收益日期
     */
    public static Map<String, String> getFundDate(String createTime) {
        Map<String, String> returnMap = new HashMap<String, String>(16);

        // 预计收益日期
        String confirmDate = null;
        // 预计收益到账日期
        String grantDate = null;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(createTime));

            int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            System.out.println(week);
            System.out.println(hour);
            switch (week) {
                case 1:
                    if (hour < HOUR_FLAG) {
                        calendar.add(Calendar.DAY_OF_WEEK, 2);
                        Date confirmDateFormat = calendar.getTime();
                        confirmDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(confirmDateFormat)) + " 周三";
                        calendar.add(Calendar.DAY_OF_WEEK, 1);
                        Date grantDateFormat = calendar.getTime();
                        grantDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(grantDateFormat)) + " 周四";
                    } else {
                        return getFundDate(getLastDate(calendar));
                    }
                    break;
                case 2:
                    if (hour < HOUR_FLAG) {
                        calendar.add(Calendar.DAY_OF_WEEK, 2);
                        Date confirmDateFormat = calendar.getTime();
                        confirmDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(confirmDateFormat)) + " 周四";
                        calendar.add(Calendar.DAY_OF_WEEK, 1);
                        Date grantDateFormat = calendar.getTime();
                        grantDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(grantDateFormat)) + " 周五";
                    } else {
                        return getFundDate(getLastDate(calendar));
                    }
                    break;
                case 3:
                    if (hour < HOUR_FLAG) {
                        calendar.add(Calendar.DAY_OF_WEEK, 2);
                        Date confirmDateFormat = calendar.getTime();
                        confirmDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(confirmDateFormat)) + " 周五";
                        calendar.add(Calendar.DAY_OF_WEEK, 1);
                        Date grantDateFormat = calendar.getTime();
                        grantDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(grantDateFormat)) + " 周六";
                    } else {
                        return getFundDate(getLastDate(calendar));
                    }
                    break;
                case 4:
                    if (hour < HOUR_FLAG) {
                        calendar.add(Calendar.DAY_OF_WEEK, 3);
                        Date confirmDateFormat = calendar.getTime();
                        confirmDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(confirmDateFormat)) + " 周一";
                        calendar.add(Calendar.DAY_OF_WEEK, 1);
                        Date grantDateFormat = calendar.getTime();
                        grantDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(grantDateFormat)) + " 周二";
                    } else {
                        return getFundDate(getLastDate(calendar));
                    }
                    break;
                case 5:
                    if (hour < HOUR_FLAG) {
                        calendar.add(Calendar.DAY_OF_WEEK, 3);
                        Date confirmDateFormat = calendar.getTime();
                        confirmDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(confirmDateFormat)) + " 周二";
                        calendar.add(Calendar.DAY_OF_WEEK, 1);
                        Date grantDateFormat = calendar.getTime();
                        grantDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(grantDateFormat)) + " 周三";
                    } else {
                        return getFundDate(getLastDate(calendar));
                    }
                    break;
                case 6:
                    if (hour < HOUR_FLAG) {
                        calendar.add(Calendar.DAY_OF_WEEK, 3);
                        Date confirmDateFormat = calendar.getTime();
                        confirmDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(confirmDateFormat)) + " 周二";
                        calendar.add(Calendar.DAY_OF_WEEK, 1);
                        Date grantDateFormat = calendar.getTime();
                        grantDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(grantDateFormat)) + " 周三";
                    } else {
                        return getFundDate(getLastDate(calendar));
                    }
                    break;
                default:
                    if (hour < HOUR_FLAG) {
                        calendar.add(Calendar.DAY_OF_WEEK, 2);
                        Date confirmDateFormat = calendar.getTime();
                        confirmDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(confirmDateFormat)) + " 周二";
                        calendar.add(Calendar.DAY_OF_WEEK, 1);
                        Date grantDateFormat = calendar.getTime();
                        grantDate = DateHelper.getMDFormatDate(DateHelper.getYMDHMSFormatDate(grantDateFormat)) + " 周三";
                    } else {
                        return getFundDate(getLastDate(calendar));
                    }
                    break;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        returnMap.put("confirmDate", confirmDate);
        returnMap.put("grantDate", grantDate);
        return returnMap;
    }


    /**
     * 获取下一天0时时间
     *
     * @param calendar 日历对象
     * @return yyyy-MM-dd HH:mm:ss
     */
    private static String getLastDate(Calendar calendar) {
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR, 0);
        return DateHelper.getYMDHMSFormatDate(calendar.getTime());
    }


}
