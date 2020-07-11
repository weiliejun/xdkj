package com.xdkj.common.util;

/**
 * Redis 枚举类
 */
public enum RedisEnum {

    USER_LOGIN_PASSWORDERRORCOUNT(
            RedisUtil.GLOBAL_PREFIX, "userService", "passwordErrorCount", "登录密码校验失败次数Redis缓存"),
    CONSOLE_USER_LOGIN_PASSWORDERRORCOUNT(
            RedisUtil.CONSOLE_PREFIX, "userService", "passwordErrorCount", "管理后台登录密码校验失败次数Redis缓存"),

    WECHAT_ACCESSTOKEN(
            RedisUtil.INVEST_PREFIX, "accessTokenService", "refreshAccessToken", "微信accessToken缓存"),
    WECHAT_JSAPI_TICKET(
            RedisUtil.INVEST_PREFIX, "accessTokenService", "refreshjsapiTicket", "微信jsapiTicket缓存"),
    /**
     * 文件流水号
     */
    CONSOLE_NIFA_FILE_SERIALNUMBER(
            RedisUtil.CONSOLE_PREFIX, "nifaSftpService", "getSerialNumber", "金融协会上传文件流水号Redis缓存"),
    CONSOLE_CREDIT_FILE_SERIALNUMBER(
            RedisUtil.CONSOLE_PREFIX, "CreditInfoShareService", "getSerialNumber", "信用共享上传文件流水号Redis缓存");

    /**
     * 系统标识
     */
    private String keyPrefix;
    /**
     * 模块名称
     */
    private String module;
    /**
     * 方法名称
     */
    private String func;
    /**
     * 描述
     */
    private String remark;

    RedisEnum(String keyPrefix, String module, String func, String remark) {
        this.keyPrefix = keyPrefix;
        this.module = module;
        this.func = func;
        this.remark = remark;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}