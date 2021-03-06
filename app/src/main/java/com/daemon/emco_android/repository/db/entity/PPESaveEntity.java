package com.daemon.emco_android.repository.db.entity;

import com.google.gson.annotations.SerializedName;

public class PPESaveEntity {

    @SerializedName("companyCode")
    private String mCompanyCode;
    @SerializedName("complainNumber")
    private String mComplainNumber;
    @SerializedName("createdDate")
    private String mCreatedDate;
    @SerializedName("createdUser")
    private String mCreatedUser;
    @SerializedName("jobNumber")
    private String mJobNumber;
    @SerializedName("modifiedBy")
    private String mModifiedBy;
    @SerializedName("modifiedDate")
    private String mModifiedDate;
    @SerializedName("ppeCode")
    private String mPpeCode;
    @SerializedName("ppeName")
    private String mPpeName;
    @SerializedName("ppeUsed")
    private String mPpeUsed;
    @SerializedName("siteCode")
    private String mSiteCode;
    @SerializedName("workType")
    private String mWorkType;

    public String getCompanyCode() {
        return mCompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        mCompanyCode = companyCode;
    }

    public String getComplainNumber() {
        return mComplainNumber;
    }

    public void setComplainNumber(String complainNumber) {
        mComplainNumber = complainNumber;
    }

    public String getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        mCreatedDate = createdDate;
    }

    public String getCreatedUser() {
        return mCreatedUser;
    }

    public void setCreatedUser(String createdUser) {
        mCreatedUser = createdUser;
    }

    public String getJobNumber() {
        return mJobNumber;
    }

    public void setJobNumber(String jobNumber) {
        mJobNumber = jobNumber;
    }

    public String getModifiedBy() {
        return mModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        mModifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return mModifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        mModifiedDate = modifiedDate;
    }

    public String getPpeName() {
        return mPpeName;
    }

    public void setPpeName(String ppeName) {
        mPpeName = ppeName;
    }

    public String getPpeUsed() {
        return mPpeUsed;
    }

    public void setPpeUsed(String ppeUsed) {
        mPpeUsed = ppeUsed;
    }

    public String getSiteCode() {
        return mSiteCode;
    }

    public void setSiteCode(String siteCode) {
        mSiteCode = siteCode;
    }

    public String getWorkType() {
        return mWorkType;
    }

    public void setWorkType(String workType) {
        mWorkType = workType;
    }

    public String getPpeCode() {
        return mPpeCode;
    }

    public void setPpeCode(String mPpeCode) {
        this.mPpeCode = mPpeCode;
    }
}
