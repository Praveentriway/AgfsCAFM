package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverter;
import androidx.annotation.NonNull;
import com.daemon.emco_android.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;

@Entity(
    tableName = "SaveFeedbackEntity",
    primaryKeys = {"opco", "complaintSite", "complaintNumber"})
public class SaveFeedbackEntity {
  @SerializedName("complaintNumber")
  @ColumnInfo(name = "complaintNumber")
  @NonNull
  private String complaintNumber = "";

  @SerializedName("opco")
  @ColumnInfo(name = "opco")
  @NonNull
  private String opco = "";

  @SerializedName("complaintSite")
  @ColumnInfo(name = "complaintSite")
  @NonNull
  private String complaintSite = "";

  @SerializedName("attendedBy")
  @ColumnInfo
  private List<EmployeeDetailsEntity> attendedBy;

  @SerializedName("checkedBy")
  @ColumnInfo
  private List<EmployeeDetailsEntity> checkedBy;

  @SerializedName("feedbackInformation")
  @ColumnInfo()
  private String feedbackInformation;

  @SerializedName("supRemark")
  @ColumnInfo()
  private String supRemark;

  @SerializedName("customerSignStatus")
  @ColumnInfo()
  private String customerSignStatus;

  @SerializedName("createdBy")
  @ColumnInfo()
  private String createdBy;

  @ColumnInfo(name = "mode")
  private int mode = AppUtils.MODE_SERVER;

  public SaveFeedbackEntity(@NonNull String complaintNumber) {
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
  public List<EmployeeDetailsEntity> getAttendedBy() {
    return attendedBy;
  }

  /** @param attendedBy The attendedBy */
  public void setAttendedBy(List<EmployeeDetailsEntity> attendedBy) {
    this.attendedBy = attendedBy;
  }

  /** @return The checkedBy */
  public List<EmployeeDetailsEntity> getCheckedBy() {
    return checkedBy;
  }

  /** @param checkedBy The checkedBy */
  public void setCheckedBy(List<EmployeeDetailsEntity> checkedBy) {
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

  public String getSupRemark() {
    return supRemark;
  }

  public void setSupRemark(String supRemark) {
    this.supRemark = supRemark;
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
}
