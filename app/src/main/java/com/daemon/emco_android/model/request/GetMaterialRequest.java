package com.daemon.emco_android.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class GetMaterialRequest {


  public String getComplaintNumber() {
    return complaintNumber;
  }

  public void setComplaintNumber(String complaintNumber) {
    this.complaintNumber = complaintNumber;
  }

  @SerializedName("transactionType")
  @Expose
  private String transactionType;

  @SerializedName("refrenceDocNumber")
  @Expose
  private String refrenceDocNumber;

  @SerializedName("refrenceDocDate")
  @Expose
  private String refrenceDocDate;

  @SerializedName("complaintNumber")
  @Expose
  private String complaintNumber;

  @SerializedName("companyCode")
  @Expose
  private String companyCode;

  public GetMaterialRequest(String complaintNumber, String transactionType) {
    this.complaintNumber = complaintNumber;
    this.transactionType = transactionType;
  }


  public String getCompanyCode() {
    return companyCode;
  }

  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  public GetMaterialRequest(
      String companyCode,
      String transactionType,
      String refrenceDocNumber,
      String refrenceDocDate) {
    this.companyCode=companyCode;
    this.transactionType = transactionType;
    this.refrenceDocNumber = refrenceDocNumber;
    this.refrenceDocDate = refrenceDocDate;

  }

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


  /** @return The transactionType */
  public String getTransactionType() {
    return transactionType;
  }

  /** @param transactionType The transactionType */
  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }
}
