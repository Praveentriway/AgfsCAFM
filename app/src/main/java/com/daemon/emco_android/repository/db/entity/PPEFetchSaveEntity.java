package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.daemon.emco_android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "PPEFetchSaveEntity")
public class PPEFetchSaveEntity {
  @SerializedName("companyCode")
  @ColumnInfo(name = "companyCode")
  private String mCompanyCode;

  @SerializedName("complainNumber")
  @ColumnInfo(name = "complainNumber")
  private String mComplainNumber;

  @SerializedName("createdUser")
  @ColumnInfo(name = "createdUser")
  private String mCreatedUser;

  @SerializedName("jobNumber")
  @ColumnInfo(name = "jobNumber")
  private String mJobNumber;

  @SerializedName("modifiedBy")
  @ColumnInfo(name = "modifiedBy")
  private String mModifiedBy;

  @SerializedName("ppeCode")
  @ColumnInfo(name = "ppeCode")
  private String mPpeCode;

  @SerializedName("ppeName")
  @ColumnInfo(name = "ppeName")
  private String mPpeName;

  @SerializedName("ppeRefrenceNumber")
  @ColumnInfo(name = "ppeRefrenceNumber")
  private String mPpeRefrenceNumber;

  @SerializedName("ppeUsed")
  @ColumnInfo(name = "ppeUsed")
  private String mPpeUsed;

  @SerializedName("siteCode")
  @ColumnInfo(name = "siteCode")
  private String mSiteCode;

  @SerializedName("workType")
  @ColumnInfo(name = "workType")
  private String mWorkType;

  @ColumnInfo(name = "mode")
  private int mode = AppUtils.MODE_SERVER;

  @ColumnInfo @NonNull @PrimaryKey private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

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

  public String getPpeCode() {
    return mPpeCode;
  }

  public void setPpeCode(String ppeCode) {
    mPpeCode = ppeCode;
  }

  public String getPpeName() {
    return mPpeName;
  }

  public void setPpeName(String ppeName) {
    mPpeName = ppeName;
  }

  public String getPpeRefrenceNumber() {
    return mPpeRefrenceNumber;
  }

  public void setPpeRefrenceNumber(String ppeRefrenceNumber) {
    mPpeRefrenceNumber = ppeRefrenceNumber;
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

  @Override
  public String toString() {
    return "PPEFetchSaveEntity{"
        + "mCompanyCode='"
        + mCompanyCode
        + '\''
        + ", mComplainNumber='"
        + mComplainNumber
        + '\''
        + ", mCreatedUser='"
        + mCreatedUser
        + '\''
        + ", mJobNumber='"
        + mJobNumber
        + '\''
        + ", mModifiedBy='"
        + mModifiedBy
        + '\''
        + ", mPpeCode='"
        + mPpeCode
        + '\''
        + ", mPpeName='"
        + mPpeName
        + '\''
        + ", mPpeRefrenceNumber='"
        + mPpeRefrenceNumber
        + '\''
        + ", mPpeUsed='"
        + mPpeUsed
        + '\''
        + ", mSiteCode='"
        + mSiteCode
        + '\''
        + ", mWorkType='"
        + mWorkType
        + '\''
        + ", mode="
        + mode
        + ", id='"
        + id
        + '\''
        + '}';
  }
}
