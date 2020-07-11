package com.xdkj.common.model.servicecalllimit.bean;

import java.io.Serializable;
import java.util.Date;

public class ServiceCallLimit implements Serializable {
    private Integer id;

    private String serviceCode;

    private String menuType;

    private Integer dayLimit;

    private Integer freeLimit;

    private Integer allLimit;

    private String status;

    private String dataStatus;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode == null ? null : serviceCode.trim();
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType == null ? null : menuType.trim();
    }

    public Integer getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }

    public Integer getFreeLimit() {
        return freeLimit;
    }

    public void setFreeLimit(Integer freeLimit) {
        this.freeLimit = freeLimit;
    }

    public Integer getAllLimit() {
        return allLimit;
    }

    public void setAllLimit(Integer allLimit) {
        this.allLimit = allLimit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus == null ? null : dataStatus.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}