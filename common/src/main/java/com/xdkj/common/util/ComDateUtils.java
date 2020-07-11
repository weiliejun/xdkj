/**
 * 文件名: ComDateUtils.java
 *
 * @Package com.phhc.sys.common.util
 * @Description: 时间工具类，包含一系列的时间操作方法
 * @author 罗顺锋
 * @date 2014-9-18 上午11:33:22
 * @version V1.0
 */
package com.xdkj.common.util;

import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 罗顺锋
 * @ClassName: ComDateUtils
 * @Description: TODO(时间工具类 : 时间各种类型转换)
 * @date 2014-9-18 上午11:33:22
 */
public class ComDateUtils {
    public static final SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger logger = Logger.getLogger(ComDateUtils.class);
    private static SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    // private static SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
    // private static SimpleDateFormat formatDay = new SimpleDateFormat("dd");

    /**
     * 将整数值转换为String值的方法
     *
     * @param i
     * @return String
     * @method_name intToString
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static String intToString(int i) {
        String s = "";
        s = Integer.toString(i);
        return s;
    }

    /**
     * 将长整数值转换为String值的方法
     *
     * @param i
     * @return String
     * @method_name intToString
     * @author luoshunfeng
     * @create_time 2007-11-15 下午01:38:15
     */
    public static String longToString(long i) {
        String s = "";
        s = Long.toString(i);
        return s;
    }

    /**
     * 将长整数值转换成整数值的方法
     *
     * @param i
     * @return int
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static int longToInt(long i) {
        int s;
        s = Integer.parseInt(longToString(i));
        return s;
    }

    /**
     * 将String类型转换为int值的方法
     *
     * @param String
     * @return int
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static int stringToInt(String s) {
        if (logger.isDebugEnabled()) {
            logger.debug("stringToInt(String) - start");
        }

        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (Exception ne) {
            logger.error("stringToInt(String)", ne);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("stringToInt(String) - end");
        }
        return i;
    }

    /**
     * 返回年
     *
     * @param Date
     * @return String
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static String getYear(Date dt) {
        return formatYear.format(dt);
    }

    /**
     * 将输入的字符串转换为Date日期格式的方法
     *
     * @param s
     * @param format 时间字符串
     * @return Date
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static Date stringToDate(String s, String format) {
        if (logger.isDebugEnabled()) {
            logger.debug("stringToDate(String) - start");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date returnDate = sdf.parse(s);
            if (logger.isDebugEnabled()) {
                logger.debug("stringToDate(String) - end");
            }
            return returnDate;
        } catch (Exception e) {
            logger.error("stringToDate(String)", e);
        }
        return null;
    }

    /**
     * 将字符串转化为java.sql.Date的方法
     *
     * @param s
     * @param format
     * @return sq.Date
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static java.sql.Date stringToSqlDate(String s, String format) {
        if (logger.isDebugEnabled()) {
            logger.debug("stringToSqlDate(String) - start");
        }
        try {
            Date date = stringToDate(s, format);
            java.sql.Date rtnDate = new java.sql.Date(date.getTime());

            if (logger.isDebugEnabled()) {
                logger.debug("stringToSqlDate(String) - end");
            }
            return rtnDate;
        } catch (Exception e) {
            logger.error("stringToSqlDate(String)", e);
        }
        return null;
    }

    /**
     * 将输入的字符串转化为Timestamp类型的操作
     *
     * @param S
     * @return Timestamp
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static Timestamp stringToTimestamp(String s) {
        if (logger.isDebugEnabled()) {
            logger.debug("stringToTimestamp(String) - start");
        }

        try {

            return Timestamp.valueOf(s);
        } catch (Exception e) {
            logger.error("stringToTimestamp(String)", e);
        }
        return null;
    }

    /**
     * date转换成想要的个字符串格式
     *
     * @param date
     * @param format
     * @return
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static String dateToString(Date date, String format) {
        if (logger.isDebugEnabled()) {
            logger.debug("dateToString(Date, String) - start");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String returnString = sdf.format(date);
            if (logger.isDebugEnabled()) {
                logger.debug("dateToString(Date, String) - end");
            }
            return returnString;
        } catch (Exception e) {
            logger.error("dateToString(Date, String)", e);

            if (logger.isDebugEnabled()) {
                logger.debug("dateToString(Date, String) - end");
            }

        }
        return null;
    }

    /**
     * 将Timestamp类型转换为需要的日期格式的方法
     *
     * @param timeStamp时间格式
     * @param format        时间转换格式
     * @return String
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static String dateToString(Timestamp timeStamp, String format) {
        return dateToString(new Date(timeStamp.getTime()), format);
    }

    /**
     * long型时间转换成字符串格式
     *
     * @param dt
     * @param format
     * @return String
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static String getYearMonth(Long dt, SimpleDateFormat format) {
        return format.format(dt);
    }

    /**
     * 将字符串转化为double值的方法
     *
     * @param s
     * @return double
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static double stringToDouble(String s) {
        if (logger.isDebugEnabled()) {
            logger.debug("stringToDouble(String) - start");
        }

        try {
            double returndouble = Double.parseDouble(s);
            if (logger.isDebugEnabled()) {
                logger.debug("stringToDouble(String) - end");
            }
            return returndouble;
        } catch (Exception e) {
            logger.error("stringToDouble(String)", e);
        }
        return 0d;
    }

    /**
     * 将object(字符串)转化为double值的方法
     *
     * @param i
     * @return int
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static double objToDouble(Object s) {
        if (logger.isDebugEnabled()) {
            logger.debug("stringToDouble(String) - start");
        }

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("stringToDouble(String) - end");
            }
            return s != null ? stringToDouble(s.toString()) : 0;
        } catch (Exception e) {
            logger.error("stringToDouble(String)", e);
        }
        return 0d;
    }

    /**
     * 将double值转化为String字符串的方法
     *
     * @param d
     * @return String
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static String doubleToString(double d) {
        return Double.toString(d);
    }

    /**
     * 比较时间大小 返回0相等 返回1firstDate》secondDate 返回-1firstDate《secondDate
     *
     * @return
     * @author luoshunfeng
     * @create_time 2014-09-18 下午14:09:15
     */
    public static int compareDate(Date firstDate, Date secondDate) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            calendar.setTime(firstDate);
            calendar2.setTime(secondDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar.compareTo(calendar2);
    }

    /**
     * @Title: differenceTime @Description: TODO(时间差，精确到天) @param firstDate
     * 开始时间 @param secondDate 结束时间 @return @return int 返回类型 @create_time
     * 2015-3-17 下午2:26:06 @throws
     */
    public static int differenceTime(Date beginDate, Date endDate) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            calendar.setTime(beginDate);
            calendar2.setTime(endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long dateLen = Math.abs((calendar2.getTimeInMillis() - calendar.getTimeInMillis()) / (24 * 60 * 60 * 1000));// 获取相减值的绝对值
        return dateLen.intValue();
    }

    /**
     * 天加减
     *
     * @param count
     * @param dt
     * @return
     */
    public static Date getAddDay(int day, Date dt) {
        Calendar newDate = Calendar.getInstance();
        newDate.setTime(dt);
        newDate.add(newDate.DATE, day);// 加减天
        return date2SqlDate(newDate.getTime());
    }

    /**
     * java。util。date变成java。sql。date
     *
     * @param dt
     * @return
     * @throws ParseException
     */
    public static java.sql.Date date2SqlDate(Date dt) {
        return java.sql.Date.valueOf(formatdate.format(dt));
    }

    public static void main(String[] args) {
        String s = "2012-02-11";
        String s2 = "2012-02-11";
        // Date dt = stringToDate(s, "yyyy-MM-dd");
        // Date dt2 = stringToDate(s2, "yyyy-MM-dd HH:mm:ss");
        //
        // logger.info(dt);
        // logger.info(dt2);
        logger.info(1467253500.889 / 1000);

        // logger.info(differenceTime(dt,dt2));

        // java.util.Calendar startData=java.util.Calendar.getInstance();
        // java.util.Calendar endData=java.util.Calendar.getInstance();
        // startData.set(2012,02,01); //年，月，日
        // endData.set(2012,02,10);
        // Long dateLen =
        // Math.abs((endData.getTimeInMillis()-startData.getTimeInMillis())/(24*60*60*1000));//获取相减值的绝对值
        // logger.info(dateLen);
    }
}
