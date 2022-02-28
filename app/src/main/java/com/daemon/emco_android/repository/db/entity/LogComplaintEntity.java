package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/** Created by vikram on 23/7/17. */
@Entity(tableName = "logComplaintEntity")
public class LogComplaintEntity {
  @SerializedName("complainWebNumber")
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "complainWebNumber")
  private String complainWebNumber;

  @SerializedName("companyCode")
  @ColumnInfo(name = "companyCode")
  private String companyCode;

  @SerializedName("complainBy")
  @ColumnInfo(name = "complainBy")
  private String complainBy;

  @SerializedName("complainDate")
  @ColumnInfo(name = "complainDate")
  private String complainDate;

  @SerializedName("complainSite")
  @ColumnInfo(name = "complainSite")
  private String complainSite;

  @SerializedName("complaintDetail")
  @ColumnInfo(name = "complaintDetail")
  private String complaintDetail;

  @SerializedName("complaintEmail")
  @ColumnInfo(name = "complaintEmail")
  private String complaintEmail;

  @SerializedName("complaintMobile")
  @ColumnInfo(name = "complaintMobile")
  private String complaintMobile;

  @SerializedName("complaintPhone")
  @ColumnInfo(name = "complaintPhone")
  private String complaintPhone;

  @SerializedName("complaintYear")
  @ColumnInfo(name = "complaintYear")
  private String complaintYear;

  @SerializedName("jobNumber")
  @ColumnInfo(name = "jobNumber")
  private String jobNumber;

  @SerializedName("location")
  @ColumnInfo(name = "location")
  private String location;

  @SerializedName("subLocation")
  @ColumnInfo(name = "subLocation")
  private String subLocation;

  @SerializedName("natureOfWork")
  @ColumnInfo(name = "natureOfWork")
  private String natureOfWork;

  @SerializedName("propertyDetail")
  @ColumnInfo(name = "propertyDetail")
  private String propertyDetail;

  @SerializedName("user")
  @ColumnInfo(name = "user")
  private String user;

  @SerializedName("zoneCode")
  @ColumnInfo(name = "zoneCode")
  private String zoneCode;

  @SerializedName("complaintPriority")
  @ColumnInfo(name = "complaintPriority")
  private String complaintPriority;

  @SerializedName("workType")
  @ColumnInfo(name = "workType")
  private String workType;

  public LogComplaintEntity(String complainWebNumber) {
    this.complainWebNumber = complainWebNumber;
  }

  public String getComplaintPriority() {
    return complaintPriority;
  }

  public void setComplaintPriority(String complaintPriority) {
    this.complaintPriority = complaintPriority;
  }

  public String getWorkType() {
    return workType;
  }

  public void setWorkType(String workType) {
    this.workType = workType;
  }

  public String getComplainWebNumber() {
    return complainWebNumber;
  }

  public void setComplainWebNumber(String complainWebNumber) {
    this.complainWebNumber = complainWebNumber;
  }

  public String getCompanyCode() {
    return companyCode;
  }

  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  public String getComplainBy() {
    return complainBy;
  }

  public void setComplainBy(String complainBy) {
    this.complainBy = complainBy;
  }

  public String getComplainDate() {
    return complainDate;
  }

  public void setComplainDate(String complainDate) {
    this.complainDate = complainDate;
  }

  public String getComplainSite() {
    return complainSite;
  }

  public void setComplainSite(String complainSite) {
    this.complainSite = complainSite;
  }

  public String getComplaintDetail() {
    return complaintDetail;
  }

  public void setComplaintDetail(String complaintDetail) {
    this.complaintDetail = complaintDetail;
  }

  public String getComplaintEmail() {
    return complaintEmail;
  }

  public void setComplaintEmail(String complaintEmail) {
    this.complaintEmail = complaintEmail;
  }

  public String getComplaintMobile() {
    return complaintMobile;
  }

  public void setComplaintMobile(String complaintMobile) {
    this.complaintMobile = complaintMobile;
  }

  public String getComplaintPhone() {
    return complaintPhone;
  }

  public void setComplaintPhone(String complaintPhone) {
    this.complaintPhone = complaintPhone;
  }

  public String getComplaintYear() {
    return complaintYear;
  }

  public void setComplaintYear(String complaintYear) {
    this.complaintYear = complaintYear;
  }

  public String getJobNumber() {
    return jobNumber;
  }

  public void setJobNumber(String jobNumber) {
    this.jobNumber = jobNumber;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getNatureOfWork() {
    return natureOfWork;
  }

  public void setNatureOfWork(String natureOfWork) {
    this.natureOfWork = natureOfWork;
  }

  public String getPropertyDetail() {
    return propertyDetail;
  }

  public void setPropertyDetail(String propertyDetail) {
    this.propertyDetail = propertyDetail;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getZoneCode() {
    return zoneCode;
  }

  public void setZoneCode(String zoneCode) {
    this.zoneCode = zoneCode;
  }

  public String getSubLocation() {
    return subLocation;
  }

  public void setSubLocation(String subLocation) {
    this.subLocation = subLocation;
  }
}
