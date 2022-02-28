package com.daemon.emco_android.model.request;

import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SaveRatedServiceRequest {

  @SerializedName("opco")
  private String opco;

  @SerializedName("transactionType")
  private String transactionType;

  @SerializedName("complaintSite")
  private String complaintSite;

  @SerializedName("complaintNumber")
  private String complaintNumber;

  @SerializedName("customerSignature")
  private String customerSignature;

  @SerializedName("createdBy")
  private String createdBy;

  @SerializedName("customerFeedback")
  private String customerFeedback;

  @SerializedName("customerRemarks")
  private String customerRemarks;

  @SerializedName("customerSignStatus")
  private String customerSignStatus;

  @SerializedName("ppmRefNo")
  private String ppmRefNo;

  private List<PpmScheduleDocBy> ppmRefNoList;

  @SerializedName("detailsCode")
  private String detailsCode;

  @SerializedName("checkListCode")
  private String checkListCode;

  @SerializedName("companyCode")
  private String companyCode;

  @SerializedName("chkCode")
  private String chkCode;

  @SerializedName("cldetlCode")
  private String cldetlCode;

  @SerializedName("clinsCode")
  private String clinsCode;


  @SerializedName("ppmNo")
  private String ppmNo;

  @SerializedName("compCode")
  private String compCode;

  @SerializedName("buildingCode")
  private String buildingCode;

  @SerializedName("zoneCode")
  private String zoneCode;

  @SerializedName("jobNumber")
  private String jobNumber;

  @SerializedName("floor")
  private String floor;

  @SerializedName("flat")
  private String flat;

  @SerializedName("locationCode")
  private String locationCode;

  @SerializedName("assetBarCode")
  private String assetBarCode;
  @SerializedName("workOrderNo")
  private String workOrderNo;
  @SerializedName("contractNo")
  private String contractNo;

  @SerializedName("clientBarcode")
  private String clientBarcode;

  public String getClientBarcode() {
    return clientBarcode;
  }

  public void setClientBarcode(String clientBarcode) {
    this.clientBarcode = clientBarcode;
  }

  public List<PpmScheduleDocBy> getPpmRefNoList() {
    return ppmRefNoList;
  }

  public void setPpmRefNoList(List<PpmScheduleDocBy> ppmRefNoList) {
    this.ppmRefNoList = ppmRefNoList;
  }

  public String getBuildingCode() {
    return buildingCode;
  }

  public void setBuildingCode(String buildingCode) {
    this.buildingCode = buildingCode;
  }

  public String getZoneCode() {
    return zoneCode;
  }

  public void setZoneCode(String zoneCode) {
    this.zoneCode = zoneCode;
  }

  public String getJobNumber() {
    return jobNumber;
  }

  public void setJobNumber(String jobNumber) {
    this.jobNumber = jobNumber;
  }

  public String getFloor() {
    return floor;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public String getFlat() {
    return flat;
  }

  public void setFlat(String flat) {
    this.flat = flat;
  }

  public String getLocationCode() {
    return locationCode;
  }

  public void setLocationCode(String locationCode) {
    this.locationCode = locationCode;
  }

  public String getAssetBarCode() {
    return assetBarCode;
  }

  public void setAssetBarCode(String assetBarCode) {
    this.assetBarCode = assetBarCode;
  }

  public String getCompCode() {
    return compCode;
  }

  public void setCompCode(String compCode) {
    this.compCode = compCode;
  }

  public String getWorkOrderNo() {
    return workOrderNo;
  }

  public void setWorkOrderNo(String workOrderNo) {
    this.workOrderNo = workOrderNo;
  }

  public String getContractNo() {
    return contractNo;
  }

  public void setContractNo(String contractNo) {
    this.contractNo = contractNo;
  }

  public String getCheckListCode() {
    return checkListCode;
  }

  public void setCheckListCode(String checkListCode) {
    this.checkListCode = checkListCode;
  }

  public String getCompanyCode() {
    return companyCode;
  }

  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  public String getChkCode() {
    return chkCode;
  }

  public void setChkCode(String chkCode) {
    this.chkCode = chkCode;
  }

  public String getCldetlCode() {
    return cldetlCode;
  }

  public String getClinsCode() {
    return clinsCode;
  }

  public void setCldetlCode(String cldetlCode) {
    this.cldetlCode = cldetlCode;
  }
  public void setClinsCode(String clinsCode) {
    this.clinsCode = clinsCode;
  }

  public String getPpmNo() {
    return ppmNo;
  }

  public void setPpmNo(String ppmNo) {
    this.ppmNo = ppmNo;
  }

  public String getDetailsCode() {
    return detailsCode;
  }

  public void setDetailsCode(String detailsCode) {
    this.detailsCode = detailsCode;
  }

  public String getPpmRefNo() {
    return ppmRefNo;
  }

  public void setPpmRefNo(String ppmRefNo) {
    this.ppmRefNo = ppmRefNo;
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

  public String getComplaintSite() {
    return complaintSite;
  }

  public void setComplaintSite(String complaintSite) {
    this.complaintSite = complaintSite;
  }

  public String getComplaintNumber() {
    return complaintNumber;
  }

  public void setComplaintNumber(String complaintNumber) {
    this.complaintNumber = complaintNumber;
  }

  public String getCustomerSignature() {
    return customerSignature;
  }

  public void setCustomerSignature(String customerSignature) {
    this.customerSignature = customerSignature;
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
}
