package com.daemon.emco_android.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjectPPM {
  @SerializedName("checkedBy")
  @Expose
  private List<CheckedBy> checkedBy = null;

  @SerializedName("attendedBy")
  @Expose
  private List<AttendedBy> attendedBy = null;

  @SerializedName("feedbackInformation")
  @Expose
  private String feedbackInformation;

  @SerializedName("customerSignStatus")
  @Expose
  private String customerSignStatus;

  @SerializedName("actStartDate")
  @Expose
  private String actStartDate;

  @SerializedName("actEndDate")
  @Expose
  private String actEndDate;

  @SerializedName("ppmStatus")
  @Expose
  private String ppmStatus;

  @SerializedName("ppmFinding")
  private String ppmFinding;

  @SerializedName("ppmRecommend")
  private String ppmRecommend;


  @SerializedName("remark")
  private String remark;

  @SerializedName("pendingReason")
  private String pendingReason;

  @SerializedName("newStartDate")
  private String newStartDate;

  @SerializedName("newEndDate")
  private String newEndDate;


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getPendingReason() {
    return pendingReason;
  }

  public void setPendingReason(String pendingReason) {
    this.pendingReason = pendingReason;
  }

  public String getNewStartDate() {
    return newStartDate;
  }

  public void setNewStartDate(String newStartDate) {
    this.newStartDate = newStartDate;
  }

  public String getNewEndDate() {
    return newEndDate;
  }

  public void setNewEndDate(String newEndDate) {
    this.newEndDate = newEndDate;
  }

  public String getPpmFinding() {
    return ppmFinding;
  }

  public void setPpmFinding(String ppmFinding) {
    this.ppmFinding = ppmFinding;
  }

  public String getPpmRecommend() {
    return ppmRecommend;
  }

  public void setPpmRecommend(String ppmRecommend) {
    this.ppmRecommend = ppmRecommend;
  }

  public List<CheckedBy> getCheckedBy() {
    return checkedBy;
  }

  public void setCheckedBy(List<CheckedBy> checkedBy) {
    this.checkedBy = checkedBy;
  }

  public List<AttendedBy> getAttendedBy() {
    return attendedBy;
  }

  public void setAttendedBy(List<AttendedBy> attendedBy) {
    this.attendedBy = attendedBy;
  }

  public String getFeedbackInformation() {
    return feedbackInformation;
  }

  public void setFeedbackInformation(String feedbackInformation) {
    this.feedbackInformation = feedbackInformation;
  }

  public String getCustomerSignStatus() {
    return customerSignStatus;
  }

  public void setCustomerSignStatus(String customerSignStatus) {
    this.customerSignStatus = customerSignStatus;
  }

  public String getActStartDate() {
    return actStartDate;
  }

  public void setActStartDate(String actStartDate) {
    this.actStartDate = actStartDate;
  }

  public String getActEndDate() {
    return actEndDate;
  }

  public void setActEndDate(String actEndDate) {
    this.actEndDate = actEndDate;
  }

  public String getPpmStatus() {
    return ppmStatus;
  }

  public void setPpmStatus(String ppmStatus) {
    this.ppmStatus = ppmStatus;
  }
}
