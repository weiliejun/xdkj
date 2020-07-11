package com.xdkj.common.model.dlsInfo.bean;

import java.io.Serializable;

public class DlsInfo implements Serializable {
    private String id;

    private String dlsmc;

    private String dlsjc;

    private String dlzs;

    private String qtzz;

    private String txdz;

    private String lxdh;

    private String lxr;

    private String dataStatus;

    private String createTime;

    private String updateTime;

    private String remark;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDlsmc() {
        return dlsmc;
    }

    public void setDlsmc(String dlsmc) {
        this.dlsmc = dlsmc == null ? null : dlsmc.trim();
    }

    public String getDlsjc() {
        return dlsjc;
    }

    public void setDlsjc(String dlsjc) {
        this.dlsjc = dlsjc == null ? null : dlsjc.trim();
    }

    public String getDlzs() {
        return dlzs;
    }

    public void setDlzs(String dlzs) {
        this.dlzs = dlzs == null ? null : dlzs.trim();
    }

    public String getQtzz() {
        return qtzz;
    }

    public void setQtzz(String qtzz) {
        this.qtzz = qtzz == null ? null : qtzz.trim();
    }

    public String getTxdz() {
        return txdz;
    }

    public void setTxdz(String txdz) {
        this.txdz = txdz == null ? null : txdz.trim();
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh == null ? null : lxdh.trim();
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr == null ? null : lxr.trim();
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus == null ? null : dataStatus.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}