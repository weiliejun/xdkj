package com.xdkj.common.constant;

/**
 * @author cyp
 * @time : 2019-01-18 17:23
 * @description 三端存在session的常量
 */

public interface ApplicationSessionKeys {
    //图形验证码key
    public static final String LOGIN_VERIFYCODE = "LOGIN_VERIFYCODE_PC";
    //手机短信验证码key
    public static final String SMS_VERIFY_CODE = "SMS_VERIFY_CODE_PC";
    //系统消息总数key
    public static final String SYS_MESSAGE_NUMS_COUNT = "SYS_MESSAGE_NUMS_COUNT_PC";
    //session存储的用户信息
    public static final String CURRENT_USER = "currentUser";

}
