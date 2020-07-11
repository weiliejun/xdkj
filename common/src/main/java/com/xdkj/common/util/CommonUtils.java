/**
 *
 */
package com.xdkj.common.util;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 常用方法
 *
 * @param args
 */
public class CommonUtils {
    public static final SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat formatYearMonth = new SimpleDateFormat("yyyyMM");
    public static final SimpleDateFormat formatYear_Month = new SimpleDateFormat("yyyy-MM");
    public static final SimpleDateFormat formatYear_Month_day = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 临时附件存放主键
    public static final Long filePerId = (long) -999;
    // 允许上传附件大小
    public static final int FILE_SIZE = 1024 * 10;
    // 允许上传附件类型
    public static final String[] fileType = new String[]{"txt", "rar", "zip", "jpg", "gif", "jpeg", "png", "ppt", "doc", "tsv", "csv", "wps", "html", "htm", "pdf", "xls", "xlsx", "pptx", "docx"};
    // 1KB=1024B 1M=1024KB 1G=1024M
    public static final String finacesource = "市财政";
    public static final int INT_MAX = Integer.MAX_VALUE;// INT 最大值
    private static final Logger logger = Logger.getLogger(CommonUtils.class);
    private static SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    private static SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
    private static SimpleDateFormat formatDay = new SimpleDateFormat("dd");

    public static Date getString2Date(String date) {
        try {
            return formatdate.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 返回年
     *
     * @param dt
     * @return
     */
    public static String getYear(Date dt) {
        return formatYear.format(dt);
    }

    public static String getYear(Date dt, int i) {
        String year = String.valueOf(new Integer(formatYear.format(dt)) - i);
        return year;
    }

    /**
     * 返回年月(yyyy-MM or yyyy-mm)
     *
     * @param dt
     * @return
     */
    public static String getYearMonth(Date dt, boolean flag) {
        // if(flag)
        // return formatYearMonth.format(dt);
        // else
        return formatYear_Month.format(dt);
    }

    public static String getMonth(Date dt, boolean flag) {
        // if(flag)
        // return formatYearMonth.format(dt);
        // else\
        if (dt != null)
            return formatMonth.format(dt);
        else
            return formatMonth.format(new Date());
    }

    public static String getDay(Date dt, boolean flag) {
        // if(flag)
        // return formatYearMonth.format(dt);
        // else\
        if (dt != null)
            return formatDay.format(dt);
        else
            return formatDay.format(new Date());
    }

    /**
     * 年份加减
     *
     * @param count
     * @param dt
     * @return
     */
    public static Date getAddYear(int year, Date dt) {
        Calendar newDate = Calendar.getInstance();
        newDate.setTime(dt);
        newDate.add(newDate.YEAR, year);// 加减年份
        return date2SqlDate(newDate.getTime());
    }

    /**
     * 月加减
     *
     * @param count
     * @param dt
     * @return
     */
    public static Date getAddMonth(int month, Date dt) {
        Calendar newDate = Calendar.getInstance();
        newDate.setTime(dt);
        newDate.add(newDate.MONTH, month);// 加减月
        return date2SqlDate(newDate.getTime());
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
        return newDate.getTime();
    }

    /**
     * 分钟相加
     *
     * @param day
     * @param dt
     * @return
     */
    public static Date getAddMinute(int day, Date dt) {
        Calendar newDate = Calendar.getInstance();
        newDate.setTime(dt);
        newDate.add(newDate.MINUTE, day);// 加减分钟d
        // SimpleDateFormat formatYear_Month_day2 = new SimpleDateFormat(
        // "yyyy-MM-dd HH:mm:ss",new Locale("US"));
        // logger.info(ConvertUtil.dateToString(newDate.getTime(),"yyyy-MM-dd
        // HH:mm:ss"));
        // return
        // formatYear_Month_day.parse(formatYear_Month_day.format(newDate.getTime()));
        return newDate.getTime();
    }

    /**
     * 获得往前推迟几个月、几天 month 推迟月大小 date 推迟天大小
     */
    public static String getNextMonthFirst(int month, int date, SimpleDateFormat format) {
        String str = "";
        // SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, month);// 加一个月
        lastDate.set(Calendar.DATE, date);// 把日期设置为当月第一天
        str = format.format(lastDate.getTime());
        return str;
    }

    /**
     * 比较时间大小 返回0相等 返回1firstDate》secondDate 返回-1firstDate《secondDate
     *
     * @return
     */
    public static int compareDate(Date firstDate, Date secondDate) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            calendar.setTime(firstDate);
            calendar2.setTime(secondDate);
        } catch (Exception e) {
            // ServiceException s = new ServiceException(e.getMessage(),e);
            // s.setModelMessage("时间比较出错");
            // throw s;
        }
        return calendar.compareTo(calendar2);
    }

    /**
     * 计算退休补贴年龄之差
     *
     * @param begDate
     * @param birthDate birthDate一定要比begDate 要小
     * @return
     */
    public static int getSubDate(Date begDate, Date birthDate) {
        int begYear = Integer.parseInt(formatYear.format(begDate));
        int birthYear = Integer.parseInt(formatYear.format(birthDate));
        return begYear - birthYear;

    }

    /**
     * 验证lang型不为空
     *
     * @param value
     * @return
     */
    public static Long longIsNotNull(Long value) {
        return value == null ? 0 : value;
    }

    /**
     * 验证Integer型不为空
     *
     * @param value
     * @return
     */
    public static Integer integerIsNotNull(Integer value) {
        return value == null ? 0 : value;
    }

    /**
     * 获取月份最后一天 先加一个月然后减去当前天数就是当前月最后一天
     *
     * @param dt
     * @return
     */
    public static Date getLastDayByDate(Date dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getAddMonth(1, dt));
        Date newDate2 = getAddDay(-calendar.getTime().getDate(), calendar.getTime());
        return date2SqlDate(newDate2);
    }

    /**
     * 获取月份最第一天
     *
     * @param dt
     * @return
     */
    public static Date getFirstDayByDate(Date dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        Date newDate2 = getAddDay(-calendar.getTime().getDate() + 1, calendar.getTime());
        return date2SqlDate(newDate2);
    }

    /**
     * java。util。date变成java。sql。date
     *
     * @param dt
     * @return
     * @throws ParseException
     */
    public static java.sql.Date date2SqlDate(Date dt) {
        return java.sql.Date.valueOf(formatYear_Month_day.format(dt));
    }

    /**
     * 返回2个时间(当前月的第几个月（年、年月))
     *
     * @return
     */
    public static String[] getYearAndMonthAndDate(Date currentDate, int mountCount, boolean flag) {
        Date newDate = getAddMonth(mountCount, currentDate);//
        String currentMonth = CommonUtils.getYearMonth(newDate, flag);// 发放年月
        String[] dt = new String[2];
        dt[0] = CommonUtils.getYear(newDate);// 年
        dt[1] = currentMonth;// 年月
        return dt;
    }

    public static String[] getYearAndMonthAndDate(String giveyearmonth, int mountCount, boolean flag) throws ParseException {
        Date newDate = getAddMonth(mountCount, formatdate.parse(giveyearmonth));//
        String currentMonth = CommonUtils.getYearMonth(newDate, flag);// 发放年月
        String[] dt = new String[2];
        dt[0] = CommonUtils.getYear(newDate);// 年
        dt[1] = currentMonth;// 年月
        return dt;
    }

    /**
     * @param @param  money
     * @param @return
     * @方法名：formatMoney @描述：钱转换成整型不用四舍五入有。全部+1
     * @创建时间：下午02:24:48
     */
    public static String formatMoney(double money) {
        DecimalFormat format = new DecimalFormat("#");
        money += 0.5;// 加0.4
        return format.format(money);
    }

    /**
     * 截取字符串返回数组
     * <p>
     * Title: splitString
     * </p>
     * <p>
     * Description:
     * </p>
     *
     * @param str
     * @param reg
     * @return
     * @author luoshunfeng
     */
    public static String[] splitString(String str, String reg) {
        String[] addr = null;
        if (str != null && !"".equals(str) && str.indexOf(reg) != -1) {
            addr = str.split(reg);
        }
        return addr;
    }

    // public static String[] strFormatMonth(String month,int mountCount,boolean
    // flag){
    // month = month + "-01";//补齐日
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    // Date dt = sdf.parse("2011-11-17");
    // Date dt = .parse("2011-11-17");
    //
    // }

    public static String getLastyear12Month(Date currentDate) {
        Date newDate = getAddMonth(0, currentDate);
        String data = String.valueOf(Integer.valueOf(CommonUtils.getYear(newDate)) - 1) + "-12";
        return data;
    }

    public static int getMutlDate(Date firstDate, Date secondDate) {
        int age = 0;
        if (firstDate != null && secondDate != null) {
            Short firstYear = Short.parseShort(getYear(firstDate));
            Short secondYear = Short.parseShort(getYear(secondDate));
            age = secondYear - firstYear;
            // Short firstMonth = Short.parseShort(getMonth(firstDate,true));
            // Short secondMonth = Short.parseShort(getMonth(secondDate,true));
            // Short firstDay = Short.parseShort(getDay(firstDate,true));
            // Short secondDay = Short.parseShort(getDay(secondDate,true));
            // if(secondMonth>firstMonth){//当前月大于出生月 +一岁
            // age +=1;
            // }else if(secondMonth==firstMonth){//当年月等于出生月
            // if(secondDay>firstDay){//在判断初始日
            // age +=1;
            // }
            // }
            // return age;
        }
        return age;
    }

    public static Date getDate(String date) {
        try {
            return formatYear_Month.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过long型获取date行
     *
     * @return
     */
    // public static String getDateByLongDate(long longdate){
    // Timestamp tamp = new Timestamp(longdate);
    // String dateString = ConvertUtil.dateToString(tamp,"yyyy-MM-dd HH:mm:ss");
    // return dateString;
    // }
    public static String getOrgCode(String orgId, int q) {
        String lastIds = orgId;
        if (orgId != null && !"".equals(orgId)) {
            if (orgId.indexOf("-") != -1) {
                String[] org = orgId.split("-");
                String newId = org[org.length - 1];// 新id
                // logger.info(newId+1);
                // int lastId = Integer.parseInt(newId);
                lastIds = Integer.parseInt(newId) + q + "";
                int count = newId.length() - lastIds.length();
                for (int i = 0; i < count; i++) {
                    lastIds = 0 + lastIds;
                }
                // logger.info(lastIds);
                String lastlastIds = "";
                for (int i = 0; i < org.length - 1; i++) {
                    lastlastIds += "-" + org[i];
                }
                // logger.info(lastlastIds);
                lastlastIds = lastlastIds.replaceFirst("-", "");
                // logger.info(lastlastIds);
                return lastlastIds + "-" + lastIds;
                // logger.info(lastIds);
            }
        }
        return lastIds;
    }

    public static int getOrgIntegerCode(String orgId) {
        int lastIds = 0;
        if (orgId != null && !"".equals(orgId)) {
            if (orgId.indexOf("-") != -1) {
                String[] org = orgId.split("-");
                String newId = org[org.length - 1];// 新id
                // logger.info(newId+1);
                // int lastId = Integer.parseInt(newId);
                lastIds = Integer.parseInt(newId);
                // logger.info(lastIds);
            }
        }
        return lastIds;
    }

    /**
     * private long testLong; private String testStr; private int testInt;
     *
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static String decodeStringByCode(String str, String enc) throws UnsupportedEncodingException {
        if (str == null || "".equals(str)) {
            return null;
        }
        URLDecoder decoder = new URLDecoder();
        return decoder.decode(str, enc);
    }

    /**
     * 字符串转换十六进制
     *
     * @param s
     * @return
     */
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return "0x" + str;// 0x表示十六进制
    }

    // 转换十六进制编码为字符串
    public static String toStringHex(String s) {
        if ("0x".equals(s.substring(0, 2))) {
            s = s.substring(2);
        }
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static void writeToPage(String str) throws IOException {
        File file = new File("d:/log.txt");
        // FileInputStream is = new FileInputStream(file);
        FileWriter writer = new FileWriter(file);
        char[] strbyte = str.toCharArray();
        writer.write(strbyte, 0, strbyte.length);
        writer.close();
    }

    /**
     * 解密密码
     *
     * @param pwd
     * @return
     */
    // public static String jiemiPwd(String pwd){
    // if(pwd==null && "".equals(pwd))
    // return pwd;
    // return JceUtilProxy.jiemi(pwd);//解密
    // }
    public static void main(String[] args) throws IOException {

        writeToPage("你好");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            // validateType("D:\\ZGBM_201209.zip");
            String str = ".jpg";
            // long time = 1361431955759;
            logger.info(getAddMinute(1, new Date()));
            // logger.info((new Date()).getTime());
            // logger.info(toStringHex("25504446"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 加密密码
     *
     * @param pwd
     * @return
     */
    // public static String jiamiPwd(String pwd){
    // if(pwd==null && "".equals(pwd))
    // return pwd;
    // return JceUtilProxy.jiami(pwd);//加密
    // }

    /**
     * 获取相对路径
     *
     * @return
     */
    public static String getBaseDir() {
        // return
        // request.getSession().getServletContext().getRealPath("\\WEB-INF")+"\\upload\\";
        return "/home/capinfo/upload/";

    }

    /**
     * 该方法将附件保存到指定路径，并返回保存后的文件全路径
     *
     * @return
     */
    public static String saveFile(MultipartFile fileUpload, String tempDir) {
        InputStream inputStream;
        try {
            inputStream = fileUpload.getInputStream();
            OutputStream outputStream = new FileOutputStream(tempDir);
            byte[] buffer = fileUpload.getBytes();
            int bytesum = 0;
            int byteread = 0;
            while ((byteread = inputStream.read(buffer)) != -1) {
                bytesum += byteread;
                outputStream.write(buffer, 0, byteread);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tempDir;
    }

    /**
     * 验证附件类型
     *
     * @return
     */
    public static boolean validFileType(String filename) {
        String fileType = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
        // logger.info(fileType);
        for (String allType : CommonUtils.fileType) {
            if (fileType.equals(allType)) {
                return true;
            }
        }
        return false;
    }

    public static Date longToDate(Long date) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTimeInMillis(date);
        // logger.info(lastDate.getTime());
        return lastDate.getTime();
    }

    /**
     * 日期类型转换为字符串类型
     *
     * @param d 需要转换的日期值
     * @return 日期字符串，格式为“yyyy-MM-dd HH:mm:ss”
     */
    public static String dateToString(Date d) {
        return formatYear_Month_day.format(d);
    }

    /**
     * 根据参考时间(Reference Time)，计算前N天或后N天的时间，n大于0
     *
     * @param d      参考时间
     * @param dayNum 计算的天数，如果dayNum为正整数，则计算的值为参考时间的后N天时间
     *               如果dayNum为负整数，则计算的值为参考时间的前N天时间
     * @return 日期类型的值
     * @author 贾庆超
     */
    public static Date refTimeBeforeOrAfterTime(Date d, Integer dayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.roll(Calendar.DATE, dayNum);
        return calendar.getTime();
    }

    /**
     * 获取ip
     *
     * @param request
     * @return
     * @author 罗顺锋
     * @date 2013-6-27 下午02:17:47
     * @comment
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * Timestamp类型转换date类型
     *
     * @param timestamp
     * @return
     */
    public Date timestampToDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }
}
