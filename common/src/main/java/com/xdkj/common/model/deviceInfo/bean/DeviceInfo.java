package com.xdkj.common.model.deviceInfo.bean;

import java.io.Serializable;

public class DeviceInfo implements Serializable {
    private String id;

    private String dlsId;

    private String sblb;

    private String cpdm;

    private String csms;

    private String dw;

    private String sl;

    private String znjg;

    private String wbjg;

    private String pp;

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

    public String getDlsId() {
        return dlsId;
    }

    public void setDlsId(String dlsId) {
        this.dlsId = dlsId == null ? null : dlsId.trim();
    }

    public String getSblb() {
        return sblb;
    }

    public void setSblb(String sblb) {
        this.sblb = sblb == null ? null : sblb.trim();
    }

    public String getCpdm() {
        return cpdm;
    }

    public void setCpdm(String cpdm) {
        this.cpdm = cpdm == null ? null : cpdm.trim();
    }

    public String getCsms() {
        return csms;
    }

    public void setCsms(String csms) {
        this.csms = csms == null ? null : csms.trim();
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw == null ? null : dw.trim();
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl == null ? null : sl.trim();
    }

    public String getZnjg() {
        return znjg;
    }

    public void setZnjg(String znjg) {
        this.znjg = znjg == null ? null : znjg.trim();
    }

    public String getWbjg() {
        return wbjg;
    }

    public void setWbjg(String wbjg) {
        this.wbjg = wbjg == null ? null : wbjg.trim();
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp == null ? null : pp.trim();
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