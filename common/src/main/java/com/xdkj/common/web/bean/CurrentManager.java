package com.xdkj.common.web.bean;

import com.xdkj.common.model.sysFunction.bean.SysFunction;
import com.xdkj.common.model.sysManager.bean.SysManager;
import com.xdkj.common.model.sysRole.bean.SysRole;

import java.io.Serializable;
import java.util.List;

public class CurrentManager implements Serializable {

    private static final long serialVersionUID = -4446583162943858640L;

    private SysManager sysManager;

    private String ip;

    private String loginTime;

    private String sessionId;

    private List<SysFunction> levelOneFunctions;

    private List<SysRole> roles;

    public SysManager getSysManager() {
        return sysManager;
    }

    public void setSysManager(SysManager sysManager) {
        this.sysManager = sysManager;
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

    public List<SysFunction> getLevelOneFunctions() {
        return levelOneFunctions;
    }

    public void setLevelOneFunctions(List<SysFunction> levelOneFunctions) {
        this.levelOneFunctions = levelOneFunctions;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }
}