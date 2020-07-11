package com.xdkj.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xdkj.common.components.exception.ParamErrorException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtil {
    private static Logger logger = Logger.getLogger(VerifyUtil.class);


    /**
     * 接口签名验证
     */
    public static Map<String, Object> verifySign(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        boolean verifySign = false;
        if (StringUtils.isBlank(param)) {
            throw new IllegalArgumentException("parameter con't be null!");
        }
        // 解密
        String decryptParam = DES3.decrypt(param);
        JSONObject paramJson = JSON.parseObject(decryptParam);
        JSONObject data = paramJson.getJSONObject("data");
        String check = paramJson.getString("check");

        if (data == null || check == null) {
            throw new IllegalArgumentException("parameter must include field data, check! ");
        }
        String md5 = MD5Util.MD5(JSON.toJSONString(data));
        if (md5.equals(check)) {
            verifySign = true;
        }
        result.put("verifySign", verifySign);
        result.put("decryptParam", decryptParam);
        result.put("data", data);
        return result;
    }

    /**
     * 接口参数校验
     *
     * @throws ParamErrorException
     */
    public static boolean validateParamInterface(JSONObject data, InterfaceName defaultverify) {
        LinkedHashMap<String, String[]> paramRule = getParamsNotNull(defaultverify);
        validateData(paramRule, data, "");
        return true;
    }

    /**
     * 获取接口必填的输入参数集合
     */
    public static LinkedHashMap<String, String[]> getParamsNotNull(InterfaceName InterfaceName) {
        LinkedHashMap<String, String[]> paramNotNull = new LinkedHashMap<String, String[]>();
        switch (InterfaceName) {
            default:
                ;
        }
        return paramNotNull;

    }

    /**
     * 注册接口参数校验
     *
     * @throws ParamErrorException
     */
    public static boolean validateParamRegister(JSONObject data) {
        LinkedHashMap<String, String[]> paramRule = new LinkedHashMap<String, String[]>();// 参数的校验规则
        paramRule.put("userTmsId", new String[]{"required"});
        paramRule.put("type", new String[]{"required", "validateCode{person,corp}"});
        paramRule.put("mobile", new String[]{"required", "validateMobile"});
        paramRule.put("password", new String[]{"required", "validatePassword"});
        paramRule.put("nickName", new String[]{"required", "validateNickName"});
        paramRule.put("idNo", new String[]{"required", "validateIdCard"});
        paramRule.put("name", new String[]{"required"});
        paramRule.put("email", new String[]{"validateEmail"});
        paramRule.put("terminal", new String[]{"required"});
        String type = data.getString("type");
        if ("corp".equals(type)) {
            paramRule.put("corpName", new String[]{"required"});
            paramRule.put("busiCode", new String[]{"required", "isValidBusiCode"});
            paramRule.put("busiAddress", new String[]{"required"});
            paramRule.put("registeredCapital", new String[]{"required", "validateBigDecimal"});
            paramRule.put("province", new String[]{"required"});
            paramRule.put("city", new String[]{"required"});
            paramRule.put("corpAddress", new String[]{"required"});
            paramRule.put("corpPhone", new String[]{"required", "validatePhone"});
            paramRule.put("corpOwnerName", new String[]{"required"});
            paramRule.put("summary", new String[]{"required"});
        }
        validateData(paramRule, data, "");
        return true;
    }

    /**
     * prefixParam:参数前缀，在异常提示语句中使用，便于指定参数的位置
     *
     * @throws ParamErrorException
     */
    public static boolean validateData(LinkedHashMap<String, String[]> paramRule, JSONObject data, String prefixParam) throws ParamErrorException {
        String paramValue = null;
        for (Map.Entry<String, String[]> entry : paramRule.entrySet()) {
            String key = entry.getKey();
            String[] validateRules = entry.getValue();
            paramValue = data.getString(key);
            if (prefixParam != null && !"".equals(prefixParam)) {
                key = prefixParam + "." + key;
            }
            validate(key, paramValue, validateRules);
        }
        return true;
    }

    /**
     * 根据规则校验参数
     *
     * @throws ParamErrorException
     */
    private static boolean validate(String paramName, Object paramValue, String[] validateRules) throws ParamErrorException {
        if (validateRules == null || validateRules.length == 0) {
            return true;
        }

        if (paramName == null || "".equals(paramName)) {
            return true;
        }
        String value = null;
        if (paramValue instanceof String) {
            value = (String) paramValue;
        }
        for (String rule : validateRules) {
            if (rule != null && !"".equals(rule)) {
                if (rule.indexOf("required") >= 0) {// 非空校验
                    if (value == null || "".equals(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "不能为空");
                    }
                }
                if (value == null || "".equals(value)) {// 如果值为空，不进行其它校验
                    continue;
                }
                if (rule.indexOf("validateNickName") >= 0) {// 昵称校验
                    if (!validateNickName(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，必须为6~25位数字、字母、下划线的组合，不能为纯数字");
                    }
                }
                if (rule.indexOf("validatePassword") >= 0) {// 密码校验
                    if (!validatePassword(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，不能有空格,长度在6-16个字符之间");
                    }
                }
                if (rule.indexOf("validateMobile") >= 0) {// 手机号校验
                    if (!validateMobile(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，必须为手机号");
                    }
                }
                if (rule.indexOf("validateIdCard") >= 0) {// 身份证号校验
                    if (!validateIdCard(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，必须为身份证号");
                    }
                }
                if (rule.indexOf("validateEmail") >= 0) {// 邮箱校验吗
                    if (!validateEmail(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，必须为邮箱");
                    }
                }
                if (rule.indexOf("validatePhone") >= 0) {// 固定电话校验
                    if (!validatePhone(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，必须为电话号");
                    }
                }

                if (rule.indexOf("validateTelPhone") >= 0) {// 固定电话号或手机号
                    if (!validateTelPhone(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，必须为固定电话号或手机号");
                    }
                }
                if (rule.indexOf("validateBigDecimal") >= 0) {// 数字校验，可以包含两位小数
                    if (!validateBigDecimal(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，必须为数字，最多包含两位小数");
                    }
                }
                if (rule.indexOf("validateInteger") >= 0) {// 零或正整数校验
                    if (!validateInteger(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，必须为整数或零");
                    }
                }
                if (rule.indexOf("validateIntegerGreatZero") >= 0) {// 正整数校验
                    if (!validateInteger(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，必须为大于零的整数");
                    }
                }
                if (rule.indexOf("validateCode") >= 0) {// 字段值是否为编码值的校验
                    String code = rule.replace("validateCode", "").replace("{", "").replace("}", "");
                    if (!validateCode(value, code)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，值必须为" + code + "其中之一");
                    }
                }
                if (rule.indexOf("isValidDateFormat") >= 0) {// 日期格式校验
                    String format = "yyyy-MM-dd";
                    if (!isValidDateFormat(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "日期格式不正确，日期格式为:" + format);
                    }
                }
                if (rule.indexOf("isValidBusiCode") >= 0) {// 营业执照编码校验
                    if (!isValidBusiCode(value)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，必须为15位数字");
                    }
                }
                if (rule.indexOf("isValidIntegerSection") >= 0) {// 整数区间校验
                    String section = rule.replace("isValidIntegerSection", "");
                    if (!isValidIntegerSection(value, section)) {
                        throw new ParamErrorException("请求参数:" + paramName + "格式不正确，区间为" + section);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 手机号校验
     */
    public static boolean validateMobile(String mobile) {
        Pattern pattern = Pattern.compile("^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(17[0-9]{1})|(18[0-9]{1})|(19[0-9]{1}))+\\d{8})$");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    /**
     * 电话号码验证
     */
    public static boolean validatePhone(String phone) {
        Pattern pattern = null;
        Matcher m = null;
        boolean b = false;
        pattern = Pattern.compile("(\\d{3,4}-)?\\d{6,8}");
        Matcher matcher = pattern.matcher(phone);
        b = matcher.matches();
        return b;
    }

    /**
     * 固定电话或手机号的校验
     */
    public static boolean validateTelPhone(String phone) {
        return validateMobile(phone) || validatePhone(phone);
    }

    /**
     * 身份证号校验
     */
    public static boolean validateIdCard(String idNo) {
        return IDCardUtil.validateCard(idNo);
    }

    /**
     * 昵称校验
     */
    public static boolean validateNickName(String nickName) {
        Pattern pattern = Pattern.compile("^(?!^\\d+$)[a-zA-Z0-9_]{6,25}$");
        Matcher matcher = pattern.matcher(nickName);
        return matcher.matches();
    }

    /**
     * 密码校验
     */
    public static boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^[\\@A-Za-z0-9\\!\\#\\$\\%\\^\\&\\*\\.\\~]{6,16}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * 邮箱校验
     */
    public static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 数字校验
     */
    public static boolean validateBigDecimal(String bigDecimal) {
        Pattern pattern = Pattern.compile("^([1-9][0-9]+(\\.[0-9]{1,2})?|[0-9]{1}(\\.[0-9]{1,2})?)$");
        Matcher matcher = pattern.matcher(bigDecimal);
        return matcher.matches();
    }

    /**
     * 正整数或零校验
     */
    public static boolean validateInteger(String integer) {
        Pattern pattern = Pattern.compile("^[1-9]\\d*|0$");
        Matcher matcher = pattern.matcher(integer);
        return matcher.matches();
    }

    /**
     * 正整数校验
     */
    public static boolean validateIntegerGreatZero(String integer) {
        Pattern pattern = Pattern.compile("^[1-9]\\d*$");
        Matcher matcher = pattern.matcher(integer);
        return matcher.matches();
    }

    /**
     * 字段值是否为编码值的校验
     */
    public static boolean validateCode(String value, String code) {
        if (code == null || "".equals(code)) {
            return true;
        }
        if (value == null || "".equals(value)) {
            return true;
        }
        if (code.indexOf(value) >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 日期格式校验
     */
    public static boolean isValidDateFormat(String value) {
        Pattern pattern = Pattern.compile("^\\d{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2]\\d)|(3[0-1]))$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * 营业执照校验
     */
    public static boolean isValidBusiCode(String busiCode) {
        Pattern pattern = Pattern.compile("^\\d{15}$");
        Matcher matcher = pattern.matcher(busiCode);
        return matcher.matches();
    }

    /**
     * 整数区间校验，区间格式为[1,10]或(1,10]等
     */
    public static boolean isValidIntegerSection(String value, String section) {
        boolean flag = false;
        try {
            Pattern pattern = Pattern.compile("^(\\(|\\[)(-?\\d+),(-?\\d+)(\\)|\\])$");
            Matcher matcher = pattern.matcher(section);
            if (!matcher.matches()) {// 区间格式不匹配，
                throw new RuntimeException("整数区间校验失败，区间格式不正确");
            }
            Integer intValue = Integer.parseInt(value);
            char firstChar = section.charAt(0);
            char lastChar = section.charAt(section.length() - 1);
            Integer firstValue = Integer.parseInt(matcher.group(2));
            Integer lastValue = Integer.parseInt(matcher.group(3));
            if (firstChar == '[') {
                flag = (intValue >= firstValue);
            } else {
                flag = (intValue > firstValue);
            }
            if (flag) {
                if (lastChar == ']') {
                    flag = (intValue <= lastValue);
                } else {
                    flag = (intValue < lastValue);
                }
            }
        } catch (NumberFormatException e) {
            flag = false;
            return flag;
        }
        return flag;
    }

    /**
     * 根据校验方法名调用校验方法 params数组中，第一个元素是方法名，后边的是校验方法的参数（有序）
     */
    public static boolean invokeValidMethod(String[] params) {
        boolean flag = true;
        if (params == null || params.length == 0) {
            return true;
        }
        Method method = null;
        try {
            switch (params.length) {
                case 1:
                    method = VerifyUtil.class.getMethod(params[0], null);
                    flag = (Boolean) method.invoke(null, null);
                    break;
                case 2:
                    method = VerifyUtil.class.getMethod(params[0], new Class[]{String.class});
                    flag = (Boolean) method.invoke(null, params[1]);
                    break;
                case 3:
                    method = VerifyUtil.class.getMethod(params[0], new Class[]{String.class, String.class});
                    flag = (Boolean) method.invoke(null, params[1], params[2]);
                    break;
                default:
                    flag = true;
                    break;
            }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("校验方法：" + params[0] + "不存在");
        } catch (SecurityException e) {
            throw new RuntimeException("调用校验方法：" + params[0] + "的权限");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("调用校验方法：" + params[0] + "权限异常");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("调用校验方法：" + params[0] + "参数异常");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("调用校验方法：" + params[0] + "发生异常");
        }
        return flag;
    }

    public static void main(String[] args) throws ParamErrorException {
        logger.info("日期2020-10-10-=== " + isValidDateFormat("2020-10-10"));
        logger.info("数字校验=== " + validateBigDecimal("13330.00"));
        logger.info("营业执照校验=== " + isValidBusiCode("1234567890你345"));

    }


    /**
     * 定义接口名枚举类型
     */
    public enum InterfaceName {
        defaultVerify // 默认不进行非空校验

    }

}
