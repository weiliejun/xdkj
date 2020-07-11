package com.xdkj.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSON工具
 */

public class JsonHelper {
    /**
     * 将json数组按某个属性排序，并返回一个list
     */
    public static List<JSONObject> sort(JSONArray ja, final String field, final boolean isAsc) {
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (int i = 0; i < ja.toArray().length; i++) {
            list.add((JSONObject) ja.toArray()[i]);
        }
        Collections.sort(list, new Comparator<JSONObject>() {
            public int compare(JSONObject o1, JSONObject o2) {
                Object f1 = o1.get(field);
                Object f2 = o2.get(field);
                if (isAsc) {
                    if (f1 instanceof Number && f2 instanceof Number) {
                        return ((Number) f1).intValue() - ((Number) f2).intValue();
                    } else {
                        return f1.toString().compareTo(f2.toString());
                    }
                } else {
                    if (f1 instanceof Number && f2 instanceof Number) {
                        return ((Number) f2).intValue() - ((Number) f1).intValue();
                    } else {
                        return f2.toString().compareTo(f1.toString());
                    }
                }
            }
        });
        return list;
    }

    /**
     * @param jsonStr
     * @return
     * @description json字符串key首字母大写
     * @version 1.0
     * @author 张可乐
     * @update 2017-6-5 下午1:33:47
     */
    public static String capitalizeJsonKey(String jsonStr) {
        StringBuffer result = new StringBuffer();
        if (jsonStr == null) {
            throw new RuntimeException("格式化字符串时原json字符串为空");
        }
        Pattern pattern = Pattern.compile("\"([a-zA-Z_$]{1}[a-zA-Z_$0-9]*)\":");
        Matcher matcher = pattern.matcher(jsonStr);
        while (matcher.find()) {
            String key = matcher.group(1);
            //key首字母转大写
            char[] cs = key.toCharArray();
            if (cs[0] >= 97 && cs[0] <= 122) {
                cs[0] -= 32;
            }
            matcher.appendReplacement(result, "\"" + String.valueOf(cs) + "\":");
        }
        matcher.appendTail(result);
        if (result.length() == 0) {
            return jsonStr;
        }
        return result.toString();

    }

    /**
     * @param code
     * @param msg
     * @return
     * @description 组装接口返回的json串
     * @update 2017-8-16 下午3:26:39
     */
    public static String getResultJson(String code, String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", code);
        result.put("msg", msg);
        return JSON.toJSONString(result);
    }

    /**
     * @param code
     * @param msg
     * @param data
     * @param check
     * @return
     * @description 组装接口返回的json串
     */
    public static String getResultJson(String code, String msg, Object data, String check) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", code);
        result.put("msg", msg);
        result.put("data", data);
        result.put("check", check);
        return JSON.toJSONString(result);
    }


    /**
     * map to json
     */
    public static JSONObject map2Json(Map<String, String> map) {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json;
    }
}
