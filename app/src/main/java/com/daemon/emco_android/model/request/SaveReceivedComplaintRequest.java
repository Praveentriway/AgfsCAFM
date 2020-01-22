package com.daemon.emco_android.model.request;

import com.google.gson.annotations.SerializedName;

public class SaveReceivedComplaintRequest {

  @SerializedName("complaintNumber")
  private String complaintNumber;

  @SerializedName("opco")
  private String opco;

  @SerializedName("complaintSite")
  private String complaintSite;

  @SerializedName("complaintStatus")
  private String complaintStatus;

  @SerializedName("defect")
  private String defect;

  @SerializedName("otherDefects")
  private String otherDefects;

  @SerializedName("workStatus")
  private String workStatus;

  @SerializedName("pendingResponseCode")
  private String pendingResponseCode;

  @SerializedName("otherPendingRemarks")
  private String otherPendingRemarks;

  @SerializedName("tentativeDate")
  private String tentativeDate;

  @SerializedName("workDone")
  private String workDone;

  @SerializedName("otherWorkDone")
  private String otherWorkDone;

  @SerializedName("modifiedBy")
  private String modifiedBy;

  @SerializedName("confirm")
  private Boolean confirm;

  /** @return The complaintNumber */
  public String getComplaintNumber() {
    return complaintNumber;
  }

  /** @param complaintNumber The complaintNumber */
  public void setComplaintNumber(String complaintNumber) {
    this.complaintNumber = complaintNumber;
  }

  /** @return The opco */
  public String getOpco() {
    return opco;
  }

  /** @param opco The opco */
  public void setOpco(String opco) {
    this.opco = opco;
  }

  /** @return The complaintSite */
  public String getComplaintSite() {
    return complaintSite;
  }

  /** @param complaintSite The complaintSite */
  public void setComplaintSite(String complaintSite) {
    this.complaintSite = complaintSite;
  }

  /** @return The complaintStatus */
  public String getComplaintStatus() {
    return complaintStatus;
  }

  /** @param complaintStatus The complaintStatus */
  public void setComplaintStatus(String complaintStatus) {
    this.complaintStatus = complaintStatus;
  }

  /** @return The defect */
  public String getDefect() {
    return defect;
  }

  /** @param defect The defect */
  public void setDefect(String defect) {
    this.defect = defect;
  }

  /** @return The otherDefects */
  public String getOtherDefects() {
    return otherDefects;
  }

  /** @param otherDefects The otherDefects */
  public void setOtherDefects(String otherDefects) {
    this.otherDefects = otherDefects;
  }

  /** @return The workStatus */
  public String getWorkStatus() {
    return workStatus;
  }

  /** @param workStatus The workStatus */
  public void setWorkStatus(String workStatus) {
    this.workStatus = workStatus;
  }

  /** @return The pendingResponseCode */
  public String getPendingResponseCode() {
    return pendingResponseCode;
  }

  /** @param pendingResponseCode The pendingResponseCode */
  public void setPendingResponseCode(String pendingResponseCode) {
    this.pendingResponseCode = pendingResponseCode;
  }

  /** @return The otherPendingRemarks */
  public String getOtherPendingRemarks() {
    return otherPendingRemarks;
  }

  /** @param otherPendingRemarks The otherPendingRemarks */
  public void setOtherPendingRemarks(String otherPendingRemarks) {
    this.otherPendingRemarks = otherPendingRemarks;
  }

  /** @return The tentativeDate */
  public String getTentativeDate() {
    return tentativeDate;
  }

  /** @param tentativeDate The tentativeDate */
  public void setTentativeDate(String tentativeDate) {
    this.tentativeDate = tentativeDate;
  }

  /** @return The workDone */
  public String getWorkDone() {
    return workDone;
  }

  /** @param workDone The workDone */
  public void setWorkDone(String workDone) {
    this.workDone = workDone;
  }

  /** @return The otherWorkDone */
  public String getOtherWorkDone() {
    return otherWorkDone;
  }

  /** @param otherWorkDone The otherWorkDone */
  public void setOtherWorkDone(String otherWorkDone) {
    this.otherWorkDone = otherWorkDone;
  }

  /** @return The modifiedBy */
  public String getModifiedBy() {
    return modifiedBy;
  }

  /** @param modifiedBy The otherWorkDone */
  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }
}
