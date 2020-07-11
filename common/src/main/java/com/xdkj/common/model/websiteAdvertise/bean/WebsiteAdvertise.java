package com.xdkj.common.model.websiteAdvertise.bean;

import java.io.Serializable;
import java.util.Date;

public class WebsiteAdvertise implements Serializable {
    private Integer id;

    private String code;

    private String topic;

    private String channel;

    private Integer sequnum;

    private String type;

    private String description;

    private String targetUrl;

    private String advertisePicture;

    private String advertiseText;

    private Integer clicks;

    private String dataStatus;

    private Date createTime;

    private Date updateTime;

    private String status;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic == null ? null : topic.trim();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public Integer getSequnum() {
        return sequnum;
    }

    public void setSequnum(Integer sequnum) {
        this.sequnum = sequnum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl == null ? null : targetUrl.trim();
    }

    public String getAdvertisePicture() {
        return advertisePicture;
    }

    public void setAdvertisePicture(String advertisePicture) {
        this.advertisePicture = advertisePicture == null ? null : advertisePicture.trim();
    }

    public String getAdvertiseText() {
        return advertiseText;
    }

    public void setAdvertiseText(String advertiseText) {
        this.advertiseText = advertiseText == null ? null : advertiseText.trim();
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}