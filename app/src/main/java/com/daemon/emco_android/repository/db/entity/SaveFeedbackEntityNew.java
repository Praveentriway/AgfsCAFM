package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.annotation.NonNull;

import com.daemon.emco_android.model.response.AttendedBy;
import com.daemon.emco_android.model.response.CheckedBy;
import com.daemon.emco_android.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;

@Entity(tableName = "SaveFeedbackEntity")
public class SaveFeedbackEntityNew {
  @SerializedName("complaintNumber")
  @ColumnInfo(name = "complaintNumber")
  @NonNull
  @PrimaryKey
  private String complaintNumber;

  @SerializedName("opco")
  @ColumnInfo(name = "opco")
  private String opco;

  @SerializedName("complaintSite")
  @ColumnInfo(name = "complaintSite")
  private String complaintSite;

  @SerializedName("attendedBy")
  @ColumnInfo
  private List<AttendedBy> attendedBy;

  @SerializedName("checkedBy")
  @ColumnInfo
  private List<CheckedBy> checkedBy;

  @SerializedName("feedbackInformation")
  @ColumnInfo()
  private String feedbackInformation;

  @SerializedName("ppmNo")
  @ColumnInfo()
  private String ppmNo;

  public String getPpmNo() {
    return ppmNo;
  }

  public void setPpmNo(String ppmNo) {
    this.ppmNo = ppmNo;
  }

  @SerializedName("remark")
  @ColumnInfo()
  private String remark;


  @SerializedName("pendingReason")
  @ColumnInfo()
  private String pendingReason;

  @SerializedName("newStartDate")
  @ColumnInfo()
  private String newStartDate;

  @SerializedName("newEndDate")
  @ColumnInfo()
  private String newEndDate;


  @SerializedName("customerSignStatus")
  @ColumnInfo()
  private String customerSignStatus;

  @SerializedName("createdBy")
  @ColumnInfo()
  private String createdBy;

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

  @SerializedName("actStartDate")

  @ColumnInfo()
  private String actStartDate;

  @SerializedName("ppmStatus")

  @ColumnInfo()
  private String ppmStatus;

  @SerializedName("actEndDate")
  @ColumnInfo()
  private String actEndDate;

  @ColumnInfo(name = "mode")
  private int mode = AppUtils.MODE_SERVER;

  public SaveFeedbackEntityNew(@NonNull String complaintNumber) {
    this.complaintNumber = complaintNumber;
  }

  @TypeConverter
  public static List<EmployeeDetailsEntity> stringToConditionList(String data) {
    return new Gson().fromJson(data, new TypeToken<List<EmployeeDetailsEntity>>() {}.getType());
  }

  @TypeConverter
  public static String fromConditionList(List<EmployeeDetailsEntity> data) {
    return new Gson().toJson(data);
  }

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

  /** @return The attendedBy */
  public List<AttendedBy> getAttendedBy() {
    return attendedBy;
  }

  /** @param attendedBy The attendedBy */
  public void setAttendedBy(List<AttendedBy> attendedBy) {
    this.attendedBy = attendedBy;
  }

  /** @return The checkedBy */
  public List<CheckedBy> getCheckedBy() {
    return checkedBy;
  }

  /** @param checkedBy The checkedBy */
  public void setCheckedBy(List<CheckedBy> checkedBy) {
    this.checkedBy = checkedBy;
  }

  /** @return The feedbackInformation */
  public String getFeedbackInformation() {
    return feedbackInformation;
  }

  /** @param feedbackInformation The feedbackInformation */
  public void setFeedbackInformation(String feedbackInformation) {
    this.feedbackInformation = feedbackInformation;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String Remark) {
    this.remark = Remark;
  }

  public String getCustomerSignStatus() {
    return customerSignStatus;
  }

  public void setCustomerSignStatus(String customerSignStatus) {
    this.customerSignStatus = customerSignStatus;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = mode;
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

}
