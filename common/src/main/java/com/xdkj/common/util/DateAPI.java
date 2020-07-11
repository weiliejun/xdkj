package com.xdkj.common.util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xsp
 * @ClassName：DateAPI
 * @Description：某一年 的节假日 查询 工具类
 * @date 2017-6-17 上午10:08:44
 */

public class DateAPI {

    /**
     * @return 返回当前年日期信息的集合
     * @throws Exception
     * @description 获取当前年的节假日
     * @version 1.0
     * @author 徐赛平
     * @update 2017年6月20日 上午10:05:59
     */
    public static List<Map<String, String>> getWorkDays() throws Exception {
        Calendar a = Calendar.getInstance();
        return getWorkDaysByYear(a.get(Calendar.YEAR));
    }

    /**
     * @param year 传入参数为年份 格式为YYYY
     * @return 返回某一年日期信息的集合
     * @throws Exception
     * @description 获取某一年的节假日
     * @version 1.0
     * @author 徐赛平
     * @update 2017年6月20日 上午10:05:59
     */
    public static List<Map<String, String>> getWorkDaysByYear(int year) throws Exception {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        // 国家法定节假日查询链接
        // String httpUrl = "http://www.easybots.cn/api/holiday.php";
        String httpUrl = "http://tool.bitefu.net/jiari/vip.php";
        String t = year + "0701";// 开始的日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();// 开始日期，并要累积加 一
        Calendar calendar2 = Calendar.getInstance();// 结束的日期
        Date time = sdf.parse(t);
        calendar.setTime(time);
        calendar2.setTime(time);
        calendar2.add(Calendar.MONTH, 3);// 加上一年的后的日期
        Date first = calendar.getTime();
        Date next = calendar2.getTime();
        while (first.getTime() < next.getTime()) { // 判断是否是节假日
            String fdate = "d=" + URLEncoder.encode(sdf.format(first.getTime()), "UTF-8");

            // http://www.easybots.cn/api/holiday.php
            /* String jsonResult = request(httpUrl, fdate);//{"20170103":"0"}
            JSONObject jsonObj = new JSONObject(jsonResult);
            String dateType = (String) jsonObj.get(sdf.format(first.getTime()));
            Map<String, String> map = new HashMap<String, String>();
            map.put("time", sdfDateFormat.format(first.getTime()));
            map.put("dateType", dateType);*/

            // http://tool.bitefu.net/jiari/vip.php
            String jsonResult = request(httpUrl, fdate);//{"status":1,"data":2}
            System.out.println("jsonResult==" + jsonResult);
            JSONObject jsonObj = new JSONObject(jsonResult);
            Object statusObj = jsonObj.get("status");
            String dateType = "";
            if (statusObj != null) {
                if ("1".equals(getJsonDate(statusObj))) {
                    Object dateTypeObj = jsonObj.get("data");
                    if (dateTypeObj != null) {
                        dateType = getJsonDate(dateTypeObj);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("time", sdfDateFormat.format(first.getTime()));
                        map.put("dateType", dateType);
                        result.add(map);
                    }
                }
            }
            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
            first = calendar.getTime(); // 这个时间就是日期往后推一天的结果
            calendar.getTime();
        }
        return result;
    }

    /**
     * @param httpUrl :请求接口
     * @param httpArg :参数
     * @return 返回结果
     */
    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg + "&apikey=123456";
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                // sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param obj
     * @return
     * @description 将json串中Object类型转为String
     * @version 1.0
     * @author 徐赛平
     * @update 2017年7月28日 下午5:35:45
     */
    private static String getJsonDate(Object obj) {
        String result = "";
        if (obj instanceof Integer) {
            Integer resultInt = (Integer) obj;
            result = String.valueOf(resultInt);
        } else if (obj instanceof String) {
            result = (String) obj;
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            List<Map<String, String>> workDays = getWorkDays();
            for (Map<String, String> map : workDays) {
                String time = map.get("time");
                System.out.println(time);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}