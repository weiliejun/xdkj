package com.xdkj.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共工具类
 *
 * @author
 * @version 1.0
 */
public class CommonUtil {
    // 日志信息
    final static Logger log = LoggerFactory.getLogger(CommonUtil.class);

    final static Pattern PWD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(.{6,12})$");

    final static String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
            "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|like'|create|" + "table|from|grant|use|group_concat|column_name|" +
            "information_schema.columns|table_schema|union|where|order|by|" +
            "--|like|//|/|#";

    /**
     * 是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            if (((String) obj).length() == 0) {
                return true;
            }
        } else if (obj instanceof Collection) {
            if (((Collection) obj).size() == 0) {
                return true;
            }
        } else if (obj instanceof Map) {
            if (((Map) obj).size() == 0) {
                return true;
            }
        } else if (obj instanceof List) {
            if (((List) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            if (((String) obj).length() > 0) {
                return true;
            }
        } else if (obj instanceof Collection) {
            if (((Collection) obj).size() > 0) {
                return true;
            }
        } else if (obj instanceof Map) {
            if (((Map) obj).size() > 0) {
                return true;
            }
        } else if (obj instanceof List) {
            if (((List) obj).size() > 0) {
                return true;
            }
        } else if (obj != null) {
            return true;
        }
        return false;
    }

    /**
     * 获取随机数
     */
    public static String getRandomNumber(int count) {
        StringBuffer sb = new StringBuffer();
        String base = "0123456789";
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /*
     * 获取随机字符
     */
    public static String getRandomCode(int count) {
        StringBuffer sb = new StringBuffer();
        String base = "23456789abcdefghigkmnpqrstuvwxyzABCDEFGHIGKMNPQRSTUVWXYZ0";
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static Double getObjToDouble(Object obj) {
        Double d = 0D;
        if (!CommonUtil.isEmpty(CommonUtil.getObjToString(obj))) {
            d = Double.valueOf(CommonUtil.getObjToString(obj));
        }
        return d;
    }


    /**
     * 过滤 SQL 查询条件
     *
     * @param str
     * @return
     */
    public static String SqlReplace(String str) {
        if (str == null) {
            return "";
        }
        return str.replaceAll("'", "''");
    }

    /**
     * 将对象转化为字符串
     *
     * @param obj
     * @return
     */
    public static String getObjToString(Object obj) {
        if (isEmpty(obj)) {
            return "";
        }
        return obj.toString();
    }

    /**
     * 校验手机号码
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        if (isEmpty(mobile)) {
            return false;
        }
        String regEx = "[1]{1}[0-9]{10}";
        return Pattern.compile(regEx).matcher(mobile).matches();
    }

    /**
     * 获得IP地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();// IP
        try {
            if (ip.equals("127.0.0.1")) {
                InetAddress inet = InetAddress.getLocalHost();
                ip = inet.getHostAddress();
            }
        } catch (Exception e) {

            return "";
        }
        return ip;
    }

    /**
     * 是否是EMAIL格式
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        /** EMAIL 格式正则 */
        String mailRegx = "[\\w\\.\\_\\-]+@(\\w+.)+[a-zA-Z]{2,3}";
        Pattern pattern = Pattern.compile(mailRegx);
        Matcher m = pattern.matcher(email.toLowerCase());
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 分包截取集合数据
     *
     * @param list    :要分包的集合需要强制转化为list
     * @param sertNum :分包阀值
     * @return
     */
    public static Object getSubList(Object list, int sertNum) {
        List<Object> data = (List<Object>) list;
        // 最后的集合
        List<List<Object>> dataList = new ArrayList<List<Object>>();
        // 数据条数
        int dataSize = data.size();
        // 当前是第几条
        int nowIndex = 0;
        // 是否继续循环
        boolean isNext = true;
        while (isNext) {
            int nextIndex = nowIndex + sertNum;
            if (nextIndex >= dataSize) {
                nextIndex = dataSize;
                isNext = false;
            }
            // 增加到集合数据中
            dataList.add(data.subList(nowIndex, nextIndex));
            nowIndex = nextIndex;
        }
        return dataList;
    }

    /**
     * 根据列名获取拼接插入表的头部
     *
     * @param fields
     * @return
     */
    public static String getSqlHead(String tableName, String[] fields) {
        // 拼接头部
        StringBuffer sqlHead = new StringBuffer("INSERT INTO " + tableName
                + "(");
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            sqlHead.append(field);
            if (i < fields.length - 1) {
                sqlHead.append(",");
            }
        }
        sqlHead.append(") values ");
        return sqlHead.toString();
    }

    /**
     * 两个时间之间的天数 ,如果前者小于后者，那么返回0
     *
     * @param date1 :开始时间
     * @param date2 :结束时间
     * @return
     */
    public static long getComputeDays(Date date1, Date date2) {
        if (date1.compareTo(date2) >= 0) {
            return (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
        }
        return 0;
    }

    /**
     * 两位小数点
     *
     * @param value
     * @return
     */
    public static String getTwoDecimal(String value) throws NumberFormatException {
        DecimalFormat df = new DecimalFormat("#.00");
        double doubleValue = Double.parseDouble(value);
        return df.format(doubleValue);
    }

    public static String getDistanceStr(Double value) throws NumberFormatException {
        DecimalFormat df = new DecimalFormat("0.00");
        if (value.compareTo(Double.valueOf("1")) > 0) {
            return df.format(value) + "km";
        } else {
            value = value * 1000;
            return df.format(value) + "m";
        }
    }

    /**
     * 将对象转化为int
     *
     * @param obj
     * @return
     */
    public static Integer getObjToInteger(Object obj) {
        if (isEmpty(obj)) {
            return 0;
        }
        return Integer.valueOf(getObjToString(obj));
    }

    /**
     * 转为BigDecimal
     *
     * @param str
     * @return
     */
    public static BigDecimal getStringToBigDecimal(String str) {
        if (str == null) {
            return new BigDecimal("0");
        }
        BigDecimal bd = new BigDecimal(str);
        return bd;
    }

    /**
     * 将对象转化为int
     *
     * @param obj
     * @param defval 默认值
     * @return
     */
    public static Integer getObjToInt(Object obj, int defval) {
        if (isEmpty(obj)) {
            return defval;
        }
        return Integer.valueOf(getObjToString(obj));
    }

    public static boolean isStringEmpty(String str) {
        return null == str || "".equals(str.trim());
    }


    public static String getDateToStringYMD(Date date) {
        //消息体
        String dateStr = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        dateStr = year + "年" + month + "月" + day + "日";
        return dateStr;
    }

    /**
     * TextArea文本转换为Html:写入数据库时使用
     *
     * @param str
     * @return
     * @author zhengxingmiao
     */
    public static String Text2Html(String str) {
        if (str == null) {
            return "";
        } else if (str.length() == 0) {
            return "";
        }
        str = str.replaceAll("\n", "</p><p>");
        str = str.replaceAll("\r", "");
        return str;
    }

    /**
     * Html转换为TextArea文本:编辑时拿来做转换
     *
     * @param str
     * @return
     * @author zhengxingmiao
     */
    public static String Html2Text(String str) {
        if (str == null) {
            return "";
        } else if (str.length() == 0) {
            return "";
        }
        str = str.replaceAll("<br/>", "\n");
        str = str.replaceAll("<br/>", "\r");
        return str;
    }

    /**
     * @param array
     * @param no
     * @return -1不替换，0插入，否则返回替换的数字
     */
    public static int add2Array(int[] array, int no) {
        if (no < 1) {
            return -1;
        }
        int length = array.length;
        if (array[length - 1] == 0) {
            for (int i = 0; i < length; i++) {
                if (array[i] == 0) {
                    array[i] = no;
                    break;
                }
            }
            if (array[length - 1] != 0) {
                for (int i = 0; i < length - 1; i++) {
                    for (int j = i + 1; j < length; j++) {
                        int temp;
                        if (array[i] < array[j]) {
                            temp = array[j];
                            array[j] = array[i];
                            array[i] = temp;
                        }
                    }
                }
            }
            return 0;
        } else {
            if (no <= array[length - 1]) {
                return -1;
            } else {
                int replaceNo = array[length - 1];
                array[length - 1] = no;
                for (int i = 0; i < length - 1; i++) {
                    for (int j = i + 1; j < length; j++) {
                        int temp;
                        if (array[i] < array[j]) {
                            temp = array[j];
                            array[j] = array[i];
                            array[i] = temp;
                        }
                    }
                }
                return replaceNo;
            }
        }
    }

    /**
     * @param map
     * @return 按value降序排列后的map
     */
    public static Map<String, Integer> sortMapByValue(Map<String, Integer> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        List<Entry<String, Integer>> entryList = new ArrayList<Entry<String, Integer>>(map.entrySet());
        Collections.sort(entryList, new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1,
                               Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        Iterator<Entry<String, Integer>> iter = entryList.iterator();
        Entry<String, Integer> tempEntry = null;
        while (iter.hasNext()) {
            tempEntry = iter.next();
            sortedMap.put(tempEntry.getKey(), tempEntry.getValue());
        }
        return sortedMap;
    }

    /**
     * 对字符串进行sha1加密
     *
     * @param str
     * @return
     */
    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("utf-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 随机生成moer后跟7或者8位数字的名字
     */
    public static String randomName() {
        long timeMillis = System.currentTimeMillis();
        String temp = String.valueOf(timeMillis);
        String tempName = temp.substring(7);
        String name = "Moer" + tempName + new Random().nextInt(99);
        return name;
    }


    public static boolean checkObjectIsNull(Object bean) throws Exception {
        boolean flag = true;
        if (CommonUtil.isEmpty(bean)) {
            return flag;
        }
        for (Field f : bean.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            String name = f.getName();
            if ("serialVersionUID".equals(f.getName())) {
                continue;
            }
            Object beanVlaue = f.get(bean);
            if (beanVlaue != null) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                flag = false;
                break;
            }
        }

        return flag;
    }

    public static String getRate(int all, int remain) {
        DecimalFormat df = new DecimalFormat("0");
        float rate = (1 - (float) remain / all) * 100;
        return df.format(rate) + "%";
    }

    public static Map<String, String> parseStationPriceToClient(String price) {
        JSONArray priceArr = JSON.parseArray(price);
        Map<String, String> setting = new HashMap<>();
        int index = 1;
        int j = 0;
        int size = priceArr.size();//取最大的三个价格段位
        Double basePrice = null;
        for (Object item : priceArr) {
            if (j < (size - 3)) {
                j++;
                continue;
            }
            JSONObject itemObj = (JSONObject) item;
            String power = itemObj.getString("power");
            String[] powerArr = power.split("<");
            String priceDetail = itemObj.getString("price");
            String[] priceDetailArr = priceDetail.split("元");
            if (basePrice == null) {
                basePrice = new Double(priceDetailArr[0]);
            }
            String settingItem = powerArr[2].split("瓦")[0] + ":" + String.format("%.0f", basePrice * 100 / new Double(priceDetailArr[0]));
            setting.put(String.valueOf(index), settingItem);
            index++;

        }
        return setting;
    }

    public static boolean validPwd(String pwd) {
        if (PWD_PATTERN.matcher(pwd).find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Description 生成手机验证码随机字符串6位
     */
    public static int getRandomVerifyCode() {
        Random random = new Random();
        int x = random.nextInt(899999);
        x = x + 100000;
        return x;
    }
}