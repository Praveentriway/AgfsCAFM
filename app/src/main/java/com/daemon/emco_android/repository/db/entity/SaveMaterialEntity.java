package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

import com.daemon.emco_android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

@Entity(
    tableName = "SaveMaterialEntity",
    primaryKeys = {"companyCode", "complainSite", "complaintNumber", "transactionType", "itemCode"})
public class SaveMaterialEntity {

  @SerializedName("companyCode")
  @ColumnInfo
  @NonNull
  private String companyCode;

  @SerializedName("transactionType")
  @ColumnInfo
  @NonNull
  private String transactionType;

  @SerializedName("docType")
  @ColumnInfo
  private String docType;

  @SerializedName("complainSite")
  @ColumnInfo
  @NonNull
  private String complainSite;

  @SerializedName("complaintNumber")
  @ColumnInfo(name = "complaintNumber")
  @NonNull
  private String complaintNumber;

  @SerializedName("refrenceDocNumber")
  @ColumnInfo(name = "refrenceDocNumber")
  private String refrenceDocNumber;

  @SerializedName("refrenceDocDate")
  @ColumnInfo(name = "refrenceDocDate")
  private String refrenceDocDate;

  @SerializedName("jobNumber")
  @ColumnInfo
  private String jobNumber;

  @SerializedName("itemCode")
  @ColumnInfo
  @NonNull
  private String itemCode;

  @SerializedName("itemQuantity")
  @ColumnInfo
  private String itemQuantity;

  @SerializedName("remarks")
  @ColumnInfo
  private String remarks;

  @SerializedName("itemDescription")
  @ColumnInfo
  private String itemDescription;

  @SerializedName("user")
  @ColumnInfo
  private String user;

  @ColumnInfo private int mode = AppUtils.MODE_SERVER;

  public String getRefrenceDocNumber() {
    return refrenceDocNumber;
  }

  public void setRefrenceDocNumber(String refrenceDocNumber) {
    this.refrenceDocNumber = refrenceDocNumber;
  }

  public String getRefrenceDocDate() {
    return refrenceDocDate;
  }

  public void setRefrenceDocDate(String refrenceDocDate) {
    this.refrenceDocDate = refrenceDocDate;
  }

  /** @return The companyCode */
  public String getCompanyCode() {
    return companyCode;
  }

  /** @param companyCode The companyCode */
  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  /** @return The transactionType */
  public String getTransactionType() {
    return transactionType;
  }

  /** @param transactionType The transactionType */
  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  /** @return The docType */
  public String getDocType() {
    return docType;
  }

  /** @param docType The docType */
  public void setDocType(String docType) {
    this.docType = docType;
  }

  /** @return The complainSite */
  public String getComplainSite() {
    return complainSite;
  }

  /** @param complainSite The complainSite */
  public void setComplainSite(String complainSite) {
    this.complainSite = complainSite;
  }

  @NonNull
  public String getComplaintNumber() {
    return complaintNumber;
  }

  public void setComplaintNumber(@NonNull String complaintNumber) {
    this.complaintNumber = complaintNumber;
  }

  /** @return The jobNumber */
  public String getJobNumber() {
    return jobNumber;
  }

  /** @param jobNumber The jobNumber */
  public void setJobNumber(String jobNumber) {
    this.jobNumber = jobNumber;
  }

  /** @return The itemCode */
  public String getItemCode() {
    return itemCode;
  }

  /** @param itemCode The itemCode */
  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
  }

  /** @return The itemQuantity */
  public String getItemQuantity() {
    return itemQuantity;
  }

  /** @param itemQuantity The itemQuantity */
  public void setItemQuantity(String itemQuantity) {
    this.itemQuantity = itemQuantity;
  }

  /** @return The remarks */
  public String getRemarks() {
    return remarks;
  }

  /** @param remarks The remarks */
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getItemDescription() {
    return itemDescription;
  }

  public void setItemDescription(String itemDescription) {
    this.itemDescription = itemDescription;
  }

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  /** @return The user */
  public String getUser() {
    return user;
  }

  /** @param user The user */
  public void setUser(String user) {
    this.user = user;
  }
}
