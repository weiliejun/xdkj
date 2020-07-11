package com.xdkj.common.util;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author huangguohu 2016-01-14
 */
public class selectDateUtil {
    private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private static Logger logger = Logger.getLogger(selectDateUtil.class);
    // 用来全局控制 上一周，本周，下一周的周数变化
    private int weeks = 0;
    private int MaxDate;// 一月最大天数
    private int MaxYear;// 一年最大天数

    // 获得本年第一天的日期
    public static String getCurrentYearFirst(String dateStr) { // dateStr 日期格式为
        // yyyy-mm-dd
        dateStr = dateStr.substring(0, 4) + "-01-01";
        return dateStr;
    }

    public static Calendar getDateOfLastMonth(Calendar date) {
        Calendar lastDate = (Calendar) date.clone();
        lastDate.add(Calendar.MONTH, -1);
        return lastDate;
    }

    // 获取上个月今天的日期
    public static String getDateOfLastMonth(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            // return getDateOfLastMonth(c);
            return sdf.format(getDateOfLastMonth(c).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format(yyyyMMdd): " + dateStr);
        }
    }

    public static Calendar getDateOfLastSixMonth(Calendar date) {
        Calendar lastDate = (Calendar) date.clone();
        lastDate.add(Calendar.MONTH, -6);
        return lastDate;
    }

    public static Calendar getDateOfLastTreeMonth(Calendar date) {
        Calendar lastDate = (Calendar) date.clone();
        lastDate.add(Calendar.MONTH, -3);
        return lastDate;
    }

    // 获取一年前今天的日期
    public static String getDateOfLastYear(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            // return getDateOfLastMonth(c);
            return sdf.format(getDateOfLastYearMonth(c).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format(yyyyMMdd): " + dateStr);
        }
    }

    public static Calendar getDateOfLastYearMonth(Calendar date) {
        Calendar lastDate = (Calendar) date.clone();
        lastDate.add(Calendar.MONTH, -12);
        return lastDate;
    }

    // 获取六个月前今天的日期
    public static String getDateOfSixMonth(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            // return getDateOfLastMonth(c);
            return sdf.format(getDateOfLastSixMonth(c).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format(yyyyMMdd): " + dateStr);
        }
    }

    // 获取三个月前今天的日期
    public static String getDateOfTreeMonth(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            // return getDateOfLastMonth(c);
            return sdf.format(getDateOfLastTreeMonth(c).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format(yyyyMMdd): " + dateStr);
        }
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    private static int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    // 两个String类型的日期比较大小
    public static boolean isLessThanNetDate(String preDate, String startDate) {
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        // 获取Calendar实例
        Calendar currentTime = Calendar.getInstance();
        Calendar compareTime = Calendar.getInstance();
        try {
            // 把字符串转成日期类型
            currentTime.setTime(df.parse(preDate));
            compareTime.setTime(df.parse(startDate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 利用Calendar的方法比较大小
        if (currentTime.compareTo(compareTime) >= 0) {
            return true;
        } else {
            return false;
        }
        // 转成数字后比较大小
        // int startTime = Integer.parseInt("201406");
        // int endTime = Integer.parseInt("201506");
        // logger.info(endTime-startTime);
    }

    // 获取指定日期几天前的日期 day代表天数
    public static String LastWeekNowDate(String StringDate, int day) {
        Date date = DateHelper.stringToDate(StringDate, "yyyy-mm-dd");
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        String da = DateHelper.getYMDFormatDate(now.getTime());
        return da;
    }

    public static void main(String[] args) throws ParseException {
        // List<String> dateList=new ArrayList<String>();
        // Calendar startDay = Calendar.getInstance();
        // Calendar endDay = Calendar.getInstance();
        // startDay.setTime(FORMATTER.parse("2010-02-01"));
        // endDay.setTime(FORMATTER.parse("2010-05-09"));
        // dateList= printDay(startDay, endDay);
        // logger.info( dateList.toString());

        LastWeekNowDate("2016-01-3", 7);

        logger.info("某日前一天的日期是" + LastWeekNowDate("2015-01-05", 1));

        logger.info("某日下一天的日期是" + NextDate("2015-01-04"));

        logger.info("一周前的日期是" + LastWeekNowDate("2016-01-3", 7));

        logger.info("获取本年的第一天日期:" + getCurrentYearFirst("2016-02-01"));

        logger.info("上个月今天的日期是" + getDateOfLastMonth("2016-03-31"));

        logger.info("三个月前今天的日期" + getDateOfTreeMonth("2016-06-31"));

        logger.info("六个月前今天的日期" + getDateOfSixMonth("2016-06-31"));

        logger.info("一年前今天的日期" + getDateOfLastYear("2016-06-31"));
        String netDate = "2015-01-01";
        do {
            netDate = selectDateUtil.NextDate(netDate);
            logger.info(netDate);
        } while (!netDate.equals("2015-01-20"));

        logger.info(isLessThanNetDate("2015-01-01", "2015-01-01"));
        ;

        logger.info(getYearFirst(2015));
        ;
    }

    // 获取指定日期几天前的日期 day代表天数
    public static String NextDate(String StringDate) {
        Date date = DateHelper.stringToDate(StringDate, "yyyy-mm-dd");
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + 1);
        String da = DateHelper.getYMDFormatDate(now.getTime());
        return da;
    }

    public static List<String> printDay(Calendar startDay, Calendar endDay) {
        List<String> dateList = new ArrayList<String>();
        // 给出的日期开始日比终了日大则不执行打印
        if (startDay.compareTo(endDay) >= 0) {
            return dateList;
        }
        // 现在打印中的日期
        Calendar currentPrintDay = startDay;
        while (true) {
            // 日期加一
            currentPrintDay.add(Calendar.DATE, 1);
            // 日期加一后判断是否达到终了日，达到则终止打印
            if (currentPrintDay.compareTo(endDay) == 0) {
                break;
            }
            // 打印日期
            logger.info(FORMATTER.format(currentPrintDay.getTime()));
            dateList.add(FORMATTER.format(currentPrintDay.getTime()));
        }
        return dateList;
    }
}