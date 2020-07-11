package com.xdkj.common.model.userInfoExt.bean;

import java.io.Serializable;
import java.util.Date;

public class UserInfoExt implements Serializable {
    private Integer userId;

    private String checkType;

    private String checkStatus;

    private String certification;

    private String certificationType;

    private String certificationFrontPic;

    private String certificationBackPic;

    private String companyName;

    private String legalPersonName;

    private String companyLocation;

    private String companyAddress;

    private String businessLicensePic;

    private String taxRegistrationPic;

    private String organizationCodePic;

    private String bankAccountPermitPic;

    private String bankCreditCodePic;

    private String remark;

    private String dataStatus;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType == null ? null : checkType.trim();
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus == null ? null : checkStatus.trim();
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification == null ? null : certification.trim();
    }

    public String getCertificationType() {
        return certificationType;
    }

    public void setCertificationType(String certificationType) {
        this.certificationType = certificationType == null ? null : certificationType.trim();
    }

    public String getCertificationFrontPic() {
        return certificationFrontPic;
    }

    public void setCertificationFrontPic(String certificationFrontPic) {
        this.certificationFrontPic = certificationFrontPic == null ? null : certificationFrontPic.trim();
    }

    public String getCertificationBackPic() {
        return certificationBackPic;
    }

    public void setCertificationBackPic(String certificationBackPic) {
        this.certificationBackPic = certificationBackPic == null ? null : certificationBackPic.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName == null ? null : legalPersonName.trim();
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation == null ? null : companyLocation.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public String getBusinessLicensePic() {
        return businessLicensePic;
    }

    public void setBusinessLicensePic(String businessLicensePic) {
        this.businessLicensePic = businessLicensePic == null ? null : businessLicensePic.trim();
    }

    public String getTaxRegistrationPic() {
        return taxRegistrationPic;
    }

    public void setTaxRegistrationPic(String taxRegistrationPic) {
        this.taxRegistrationPic = taxRegistrationPic == null ? null : taxRegistrationPic.trim();
    }

    public String getOrganizationCodePic() {
        return organizationCodePic;
    }

    public void setOrganizationCodePic(String organizationCodePic) {
        this.organizationCodePic = organizationCodePic == null ? null : organizationCodePic.trim();
    }

    public String getBankAccountPermitPic() {
        return bankAccountPermitPic;
    }

    public void setBankAccountPermitPic(String bankAccountPermitPic) {
        this.bankAccountPermitPic = bankAccountPermitPic == null ? null : bankAccountPermitPic.trim();
    }

    public String getBankCreditCodePic() {
        return bankCreditCodePic;
    }

    public void setBankCreditCodePic(String bankCreditCodePic) {
        this.bankCreditCodePic = bankCreditCodePic == null ? null : bankCreditCodePic.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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