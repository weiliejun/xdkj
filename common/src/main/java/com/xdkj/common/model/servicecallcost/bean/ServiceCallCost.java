package com.xdkj.common.model.servicecallcost.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ServiceCallCost implements Serializable {
    private Integer id;

    private String serviceCode;

    private String menuType;

    private BigDecimal singleCost;

    private String segmentCost;

    private String costType;

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

    public BigDecimal getSingleCost() {
        return singleCost;
    }

    public void setSingleCost(BigDecimal singleCost) {
        this.singleCost = singleCost;
    }

    public String getSegmentCost() {
        return segmentCost;
    }

    public void setSegmentCost(String segmentCost) {
        this.segmentCost = segmentCost == null ? null : segmentCost.trim();
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType == null ? null : costType.trim();
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