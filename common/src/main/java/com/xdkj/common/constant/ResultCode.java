package com.xdkj.common.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统返回状态码
 * 暂定为5位
 * 第1位，定义错误提示级别，1为服务端返回提示、2为不提示、3隐藏性卖萌提示（即改变定义的提示，向
 * 用户友好提示，比如“系统内部错误”，改为“系统繁忙，请稍后重试”等）
 * 第2~3位，定义具体的错误模块，登录相关/用户相关
 * 第4~5位，具体错误编号，自增即可，最大99，如果不够再增加2位数
 */
public enum ResultCode {

    /* 成功状态码 */
    SUCCESS("00000", "业务处理成功"),

    /* 注册相关,00:为注册模块 */
    REGISTOR_USER_IS_EXIST("10001", "该用户名已存在"),
    REGISTOR_MOBILE_IS_EXIST("10002", "手机号已注册"),
    VALIDATE_CODE_ERROR("10003", "您输入的图形验证码不正确"),
    SMS_CODE_TIMEOUT("10004", "您输入的短信验证码已过期"),
    SMS_CODE_ERROR("10005", "您输入的短信验证码不正确"),

    /* 密码相关,11:为忘记密码模块 */
    FORGOT_PASSWORD_MOBILE_NOT_EXIST("11101", "手机号未注册，请先注册"),
    FORGOT_PASSWORD_GRAPHICVALIDATE_CODE_ERR("11102", "您输入的图形验证码不正确"),
    FORGOT_PASSWORD_SMS_CODE_OUTTIME("11103", "您输入的短信验证码已过期"),
    FORGOT_PASSWORD_SMS_CODE_ERR("11104", "您输入的短信验证码不正确"),
    FORGOT_PASSWORD_SMS_CODE_NOT_EXIST("11105", "您输入的短信验证码不正确"),
    FORGOT_PASSWORD_NOT_EXIST("11106", "请输入密码"),


    /* 系统相关，99：为系统相关错误 */
    PERMISSION_NO_ACCESS("29901", "系统内部错误");

    private String code;

    private String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static String getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }

    //校验重复的code值
    public static void main(String[] args) {
        ResultCode[] ApiResultCodes = ResultCode.values();
        List<String> codeList = new ArrayList<String>();
        for (ResultCode ApiResultCode : ApiResultCodes) {
            if (codeList.contains(ApiResultCode.code)) {
                System.out.println(ApiResultCode.code);
            } else {
                codeList.add(ApiResultCode.code());
            }
        }
    }
}