package com.xdkj.common.web;

import com.xdkj.common.model.user.bean.UserInfo;

import java.io.Serializable;

public class SessionUser implements Serializable {

    private UserInfo userInfo;

    private String ip;

    private String loginTime;

    private String sessionId;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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
}
