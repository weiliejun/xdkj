package com.xdkj.common.web.bean;

import com.xdkj.common.model.user.bean.UserInfo;

import java.io.Serializable;

public class CurrentUser implements Serializable {

    private static final long serialVersionUID = -4446583162943858640L;

    private UserInfo user;

    private String ip;

    private String loginTime;

    private String sessionId;
    // 出借者风险类型（1、保守型 2、谨慎性 3、稳健性 4、积极型 5、激进型）
    private String userType;
    // 风险等级（1、低风险 2、中低风险 3、中风险 4、终稿风险 5、高风险）
    private String riskRank;

    // private String remark;
    private String loginFirst;//1:首次登录初始值为1；   0:首次访问我的账户置为0;
    private String logoffTime;
    private String remark;
    private String appType;
    private String optionType;
    private String appId;
    private String channelId;

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getLogoffTime() {
        return logoffTime;
    }

    public void setLogoffTime(String logoffTime) {
        this.logoffTime = logoffTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRiskRank() {
        return riskRank;
    }

    public void setRiskRank(String riskRank) {
        this.riskRank = riskRank;
    }

    public String getLoginFirst() {
        return loginFirst;
    }

    public void setLoginFirst(String loginFirst) {
        this.loginFirst = loginFirst;
    }

}
