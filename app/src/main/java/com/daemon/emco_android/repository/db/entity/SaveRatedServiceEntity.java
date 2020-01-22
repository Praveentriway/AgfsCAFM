package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(
    tableName = "SaveRatedServiceEntity",
    primaryKeys = {"complaintNumber", "complaintSite", "opco"})
public class SaveRatedServiceEntity {
  @SerializedName("complaintNumber")
  @ColumnInfo
  @NonNull
  private String complaintNumber;

  @SerializedName("complaintSite")
  @ColumnInfo
  @NonNull
  private String complaintSite;

  @SerializedName("createdBy")
  @ColumnInfo
  private String createdBy;

  @SerializedName("customerFeedback")
  @ColumnInfo
  private String customerFeedback;

  @SerializedName("customerRemarks")
  @ColumnInfo
  private String customerRemarks;

  @SerializedName("customerSignStatus")
  @ColumnInfo
  private String customerSignStatus;

  @SerializedName("customerSignature")
  @ColumnInfo
  private String customerSignature;

  @SerializedName("opco")
  @ColumnInfo
  @NonNull
  private String opco;

  @SerializedName("transactionType")
  @ColumnInfo
  private String transactionType;

  public String getComplaintNumber() {
    return complaintNumber;
  }

  public void setComplaintNumber(String complaintNumber) {
    this.complaintNumber = complaintNumber;
  }

  public String getComplaintSite() {
    return complaintSite;
  }

  public void setComplaintSite(String complaintSite) {
    this.complaintSite = complaintSite;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getCustomerFeedback() {
    return customerFeedback;
  }

  public void setCustomerFeedback(String customerFeedback) {
    this.customerFeedback = customerFeedback;
  }

  public String getCustomerRemarks() {
    return customerRemarks;
  }

  public void setCustomerRemarks(String customerRemarks) {
    this.customerRemarks = customerRemarks;
  }

  public String getCustomerSignStatus() {
    return customerSignStatus;
  }

  public void setCustomerSignStatus(String customerSignStatus) {
    this.customerSignStatus = customerSignStatus;
  }

  public String getCustomerSignature() {
    return customerSignature;
  }

  public void setCustomerSignature(String customerSignature) {
    this.customerSignature = customerSignature;
  }

  public String getOpco() {
    return opco;
  }

  public void setOpco(String opco) {
    this.opco = opco;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }
}
