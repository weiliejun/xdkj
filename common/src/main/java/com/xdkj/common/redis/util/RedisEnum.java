package com.xdkj.common.redis.util;

/**
 * Redis 枚举类
 */
public enum RedisEnum {
    //密码错误次数
    USER_LOGIN_SESSION(
            RedisUtil.PC_PREFIX, "portalService", "saveSession", "session信息Redis缓存"),
    //密码错误次数
    USER_LOGIN_PASSWORDERRORCOUNT(
            RedisUtil.PC_PREFIX, "portalService", "passwordErrorCount", "登录密码校验失败次数Redis缓存");

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