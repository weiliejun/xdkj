package com.xdkj.common.util;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    /**
     * 组装json形式的接口结果数据
     *
     * @param code  状态代码
     * @param msg   状态信息
     * @param data  结果的数据
     * @param check 结果的md5签名
     * @return String
     */
    public static String getResultJson(String code, String msg, Object data, String check) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", code);
        result.put("msg", msg);
        result.put("data", data);
        result.put("check", check);

        return JSON.toJSONString(result);
    }

    public static String getResultJson(String code, String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", code);
        result.put("msg", msg);
        return JSON.toJSONString(result);
    }
}
