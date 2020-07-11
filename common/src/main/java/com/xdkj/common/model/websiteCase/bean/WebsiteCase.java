package com.xdkj.common.model.websiteCase.bean;

import java.io.Serializable;
import java.util.Date;

public class WebsiteCase implements Serializable {
    private Integer id;

    private String name;

    private String code;

    private String type;

    private String link;

    private String coverLogo;

    private String caseImg;

    private String content;

    private String status;

    private String isHomepageShow;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
    }

    public String getCoverLogo() {
        return coverLogo;
    }

    public void setCoverLogo(String coverLogo) {
        this.coverLogo = coverLogo == null ? null : coverLogo.trim();
    }

    public String getCaseImg() {
        return caseImg;
    }

    public void setCaseImg(String caseImg) {
        this.caseImg = caseImg == null ? null : caseImg.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getIsHomepageShow() {
        return isHomepageShow;
    }

    public void setIsHomepageShow(String isHomepageShow) {
        this.isHomepageShow = isHomepageShow == null ? null : isHomepageShow.trim();
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