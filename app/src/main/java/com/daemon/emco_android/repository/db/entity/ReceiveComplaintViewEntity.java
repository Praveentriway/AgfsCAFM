package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.daemon.emco_android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

@Entity(
    tableName = "ReceiveComplaintViewEntity",
    primaryKeys = {"opco", "complaintSite", "complaintNumber"})
public class ReceiveComplaintViewEntity implements Parcelable {

  public static final Creator<ReceiveComplaintViewEntity> CREATOR =
      new Creator<ReceiveComplaintViewEntity>() {
        @Override
        public ReceiveComplaintViewEntity createFromParcel(Parcel source) {
          return new ReceiveComplaintViewEntity(source);
        }

        @Override
        public ReceiveComplaintViewEntity[] newArray(int size) {
          return new ReceiveComplaintViewEntity[size];
        }
      };

  @SerializedName("opco")
  @ColumnInfo(name = "opco")
  @NonNull
  private String opco = "";

  @SerializedName("complaintSite")
  @ColumnInfo(name = "complaintSite")
  @NonNull
  private String complaintSite = "";

  @SerializedName("complaintNumber")
  @ColumnInfo(name = "complaintNumber")
  @NonNull
  private String complaintNumber = "";

  @SerializedName("complaintYear")
  @ColumnInfo(name = "complaintYear")
  private String complaintYear;

  @SerializedName("complaintDate")
  @ColumnInfo(name = "complaintDate")
  private String complaintDate;

  @SerializedName("complaintType")
  @ColumnInfo(name = "complaintType")
  private String complaintType;

  @SerializedName("region")
  @ColumnInfo(name = "region")
  private String region;

  @SerializedName("regionName")
  @ColumnInfo(name = "regionName")
  private String regionName; // Based on RegionMaster

  @SerializedName("jobType")
  @ColumnInfo(name = "jobType")
  private String jobType;

  @SerializedName("jobTypeName")
  @ColumnInfo(name = "jobTypeName")
  private String jobTypeName; // Based on JobTypeMaster

  @SerializedName("jobDescription")
  @ColumnInfo(name = "jobDescription")
  private String jobDescription;

  @SerializedName("jobNumber")
  @ColumnInfo(name = "jobNumber")
  private String jobNumber;

  @SerializedName("zoneCode")
  @ColumnInfo(name = "zoneCode")
  private String zoneCode;

  @SerializedName("zoneName")
  @ColumnInfo(name = "zoneName")
  private String zoneName; // Based on ZoneMaster

  @SerializedName("buildingCode")
  @ColumnInfo(name = "buildingCode")
  private String buildingCode;

  @SerializedName("buildingName")
  @ColumnInfo(name = "buildingName")
  private String buildingName; // Based on JobBuilding

  @SerializedName("location")
  @ColumnInfo(name = "location")
  private String location;

  @SerializedName("locationCode")
  @ColumnInfo(name = "locationCode")
  private String locationCode;

  @SerializedName("locationDescription")
  @ColumnInfo(name = "locationDescription")
  private String locationDescription;

  @SerializedName("workCategory")
  @ColumnInfo(name = "workCategory")
  private String workCategory;

  @SerializedName("vendorCode")
  @ColumnInfo(name = "vendorCode")
  private String vendorCode;

  @SerializedName("complaintCode")
  @ColumnInfo(name = "complaintCode")
  private String complaintCode;

  @SerializedName("complaintDetails")
  @ColumnInfo(name = "complaintDetails")
  private String complaintDetails;

  @SerializedName("complaintBy")
  @ColumnInfo(name = "complaintBy")
  private String complaintBy;

  @SerializedName("complaintByPhoneNumber")
  @ColumnInfo(name = "complaintByPhoneNumber")
  private String complaintByPhoneNumber;

  @SerializedName("complaintStatus")
  @ColumnInfo(name = "complaintStatus")
  private String complaintStatus;

  @SerializedName("forwardedDate")
  @ColumnInfo(name = "forwardedDate")
  private String forwardedDate;

  @SerializedName("forwardedEmployeeId")
  @ColumnInfo(name = "forwardedEmployeeId")
  private String forwardedEmployeeId;

  @SerializedName("responseDate")
  @ColumnInfo(name = "responseDate")
  private String responseDate;

  @SerializedName("responseDetails")
  @ColumnInfo(name = "responseDetails")
  private String responseDetails;

  @SerializedName("feedbackDate")
  @ColumnInfo(name = "feedbackDate")
  private String feedbackDate;

  @SerializedName("workStatus")
  @ColumnInfo(name = "workStatus")
  private String workStatus;

  @SerializedName("feedbackInformation")
  @ColumnInfo(name = "feedbackInformation")
  private String feedbackInformation;

  @SerializedName("otherInfo")
  @ColumnInfo(name = "otherInfo")
  private String otherInfo;

  @SerializedName("pendingResponseCode")
  @ColumnInfo(name = "pendingResponseCode")
  private String pendingResponseCode;

  @SerializedName("otherPendingRemarks")
  @ColumnInfo(name = "otherPendingRemarks")
  private String otherPendingRemarks;

  /*private Date PRComplaintYear;
  private long PRComplaintNo;*/
  @SerializedName("tentativeDate")
  @ColumnInfo(name = "tentativeDate")
  private String tentativeDate;

  @SerializedName("floor")
  @ColumnInfo(name = "floor")
  private String floor;

  /*private String handHeld;
  private String HICode;*/
  @SerializedName("flat")
  @ColumnInfo(name = "flat")
  private String flat;

  /*private String forwardPDA;
  private long FPDACode;
  private String FPDACode1;*/
  @SerializedName("customerReferenceNumber")
  @ColumnInfo(name = "customerReferenceNumber")
  private String customerReferenceNumber;

  @SerializedName("complaintPhoneNumber")
  @ColumnInfo(name = "complaintPhoneNumber")
  private String complaintPhoneNumber;

  @SerializedName("complaintMobileNumber")
  @ColumnInfo(name = "complaintMobileNumber")
  private String complaintMobileNumber;

  /*private String tenantFail;*/
  @SerializedName("customerFeedback")
  @ColumnInfo(name = "customerFeedback")
  private String customerFeedback;

  @SerializedName("createdBy")
  @ColumnInfo(name = "createdBy")
  private String createdBy;

  @SerializedName("createdDate")
  @ColumnInfo(name = "createdDate")
  private String createdDate;

  @SerializedName("customerRemarks")
  @ColumnInfo(name = "customerRemarks")
  private String customerRemarks;

  @SerializedName("webComplaintReferenceNo")
  @ColumnInfo(name = "webComplaintReferenceNo")
  private String webComplaintReferenceNo;

  @SerializedName("workType")
  @ColumnInfo(name = "workType")
  private String workType;

  @SerializedName("priority")
  @ColumnInfo(name = "priority")
  private String priority;

  @SerializedName("quotationJob")
  @ColumnInfo(name = "quotationJob")
  private String quotationJob;

  @SerializedName("containmentDate")
  @ColumnInfo(name = "containmentDate")
  private String containmentDate;

  @SerializedName("contractNo")
  @ColumnInfo(name = "contractNo")
  private String contractNo;

  @SerializedName("email")
  @ColumnInfo(name = "email")
  private String email;

  @SerializedName("assetBarCode")
  @ColumnInfo(name = "assetBarCode")
  private String assetBarCode;

  @SerializedName("clientBarcode")
  @ColumnInfo(name = "clientBarcode")
  private String clientBarcode;

  @SerializedName("assetCode")
  @ColumnInfo(name = "assetCode")
  private String assetCode;

  @SerializedName("supervisorRemark")
  @ColumnInfo(name = "supervisorRemark")
  private String supervisorRemark;

  @SerializedName("checkedBy")
  @ColumnInfo(name = "checkedBy")
  private String checkedBy;

  @SerializedName("attendedBy")
  @ColumnInfo(name = "attendedBy")
  private String attendedBy;

  @SerializedName("supervisor")
  @ColumnInfo(name = "supervisor")
  private String supervisor;

  @SerializedName("engineer")
  @ColumnInfo(name = "engineer")
  private String engineer;

  @SerializedName("modifiedBy")
  @ColumnInfo(name = "modifiedBy")
  private String modifiedBy;

  @SerializedName("confirm")
  private Boolean confirm;

  @SerializedName("modifiedDate")
  @ColumnInfo(name = "modifiedDate")
  private String modifiedDate;

  @SerializedName("customerSignStatus")
  @ColumnInfo(name = "customerSignStatus")
  private String customerSignStatus;

  @SerializedName("assetMake")
  @ColumnInfo(name = "assetMake")
  private String assetMake;

  @SerializedName("assetModel")
  @ColumnInfo(name = "assetModel")
  private String assetModel;

  @SerializedName("assetSize")
  @ColumnInfo(name = "assetSize")
  private String assetSize;

  @SerializedName("equipmentName")
  @ColumnInfo(name = "equipmentName")
  private String equipmentName;

  private String customerSignStatusNew;

  @ColumnInfo(name = "mode")
  private int mode = AppUtils.MODE_SERVER;

  @SerializedName("defect")
  @ColumnInfo(name = "defect")
  private String defect;

  @SerializedName("otherDefects")
  @ColumnInfo(name = "otherDefects")
  private String otherDefects;

  @SerializedName("workDone")
  @ColumnInfo(name = "workDone")
  private String workDone;

  @SerializedName("otherWorkDone")
  @ColumnInfo(name = "otherWorkDone")
  private String otherWorkDone;

  public ReceiveComplaintViewEntity() {}

  protected ReceiveComplaintViewEntity(Parcel in) {


    this.clientBarcode = in.readString();
    this.opco = in.readString();
    this.equipmentName = in.readString();
    this.assetMake = in.readString();
    this.assetModel = in.readString();
    this.assetSize = in.readString();
    this.complaintSite = in.readString();
    this.complaintNumber = in.readString();
    this.complaintYear = in.readString();
    this.complaintDate = in.readString();
    this.complaintType = in.readString();
    this.region = in.readString();
    this.regionName = in.readString();
    this.jobType = in.readString();
    this.jobTypeName = in.readString();
    this.jobDescription = in.readString();
    this.jobNumber = in.readString();
    this.zoneCode = in.readString();
    this.zoneName = in.readString();
    this.buildingCode = in.readString();
    this.buildingName = in.readString();
    this.location = in.readString();
    this.locationCode = in.readString();
    this.locationDescription = in.readString();
    this.workCategory = in.readString();
    this.vendorCode = in.readString();
    this.complaintCode = in.readString();
    this.complaintDetails = in.readString();
    this.complaintBy = in.readString();
    this.complaintByPhoneNumber = in.readString();
    this.complaintStatus = in.readString();
    this.forwardedDate = in.readString();
    this.forwardedEmployeeId = in.readString();
    this.responseDate = in.readString();
    this.responseDetails = in.readString();
    this.feedbackDate = in.readString();
    this.workStatus = in.readString();
    this.feedbackInformation = in.readString();
    this.otherInfo = in.readString();
    this.pendingResponseCode = in.readString();
    this.otherPendingRemarks = in.readString();
    this.tentativeDate = in.readString();
    this.floor = in.readString();
    this.flat = in.readString();
    this.customerReferenceNumber = in.readString();
    this.complaintPhoneNumber = in.readString();
    this.complaintMobileNumber = in.readString();
    this.customerFeedback = in.readString();
    this.createdBy = in.readString();
    this.createdDate = in.readString();
    this.customerRemarks = in.readString();
    this.webComplaintReferenceNo = in.readString();
    this.workType = in.readString();
    this.priority = in.readString();
    this.quotationJob = in.readString();
    this.containmentDate = in.readString();
    this.contractNo = in.readString();
    this.email = in.readString();
    this.assetBarCode = in.readString();
    this.assetCode = in.readString();
    this.supervisorRemark = in.readString();
    this.checkedBy = in.readString();
    this.attendedBy = in.readString();
    this.supervisor = in.readString();
    this.engineer = in.readString();
    this.modifiedBy = in.readString();
    this.modifiedDate = in.readString();
    this.customerSignStatus = in.readString();
    this.defect = in.readString();
    this.otherDefects = in.readString();
    this.workDone = in.readString();
    this.otherWorkDone = in.readString();
  }

  public String getCustomerSignStatusNew() {
    return customerSignStatusNew;
  }

  public void setCustomerSignStatusNew(String customerSignStatusNew) {
    this.customerSignStatusNew = customerSignStatusNew;
  }

  public String getClientBarcode() {
    return clientBarcode;
  }

  public void setClientBarcode(String clientBarcode) {
    this.clientBarcode = clientBarcode;
  }

  public String getOpco() {
    return opco;
  }

  public void setOpco(String opco) {
    this.opco = opco;
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

  public String getComplaintYear() {
    return complaintYear;
  }

  public void setComplaintYear(String complaintYear) {
    this.complaintYear = complaintYear;
  }

  public String getComplaintDate() {
    return complaintDate;
  }

  public void setComplaintDate(String complaintDate) {
    this.complaintDate = complaintDate;
  }

  public String getComplaintType() {
    return complaintType;
  }

  public void setComplaintType(String complaintType) {
    this.complaintType = complaintType;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public String getJobType() {
    return jobType;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
  }

  public String getJobTypeName() {
    return jobTypeName;
  }

  public void setJobTypeName(String jobTypeName) {
    this.jobTypeName = jobTypeName;
  }

  public String getJobDescription() {
    return jobDescription;
  }

  public void setJobDescription(String jobDescription) {
    this.jobDescription = jobDescription;
  }

  public String getJobNumber() {
    return jobNumber;
  }

  public void setJobNumber(String jobNumber) {
    this.jobNumber = jobNumber;
  }

  public String getZoneCode() {
    return zoneCode;
  }

  public void setZoneCode(String zoneCode) {
    this.zoneCode = zoneCode;
  }

  public String getZoneName() {
    return zoneName;
  }

  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
  }

  public String getBuildingCode() {
    return buildingCode;
  }

  public void setBuildingCode(String buildingCode) {
    this.buildingCode = buildingCode;
  }

  public String getBuildingName() {
    return buildingName;
  }

  public void setBuildingName(String buildingName) {
    this.buildingName = buildingName;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getLocationCode() {
    return locationCode;
  }

  public void setLocationCode(String locationCode) {
    this.locationCode = locationCode;
  }

  public String getLocationDescription() {
    return locationDescription;
  }

  public void setLocationDescription(String locationDescription) {
    this.locationDescription = locationDescription;
  }

  public String getWorkCategory() {
    return workCategory;
  }

  public void setWorkCategory(String workCategory) {
    this.workCategory = workCategory;
  }

  public String getVendorCode() {
    return vendorCode;
  }

  public void setVendorCode(String vendorCode) {
    this.vendorCode = vendorCode;
  }

  public String getComplaintCode() {
    return complaintCode;
  }

  public void setComplaintCode(String complaintCode) {
    this.complaintCode = complaintCode;
  }

  public String getComplaintDetails() {
    return complaintDetails;
  }

  public void setComplaintDetails(String complaintDetails) {
    this.complaintDetails = complaintDetails;
  }

  public String getComplaintBy() {
    return complaintBy;
  }

  public void setComplaintBy(String complaintBy) {
    this.complaintBy = complaintBy;
  }

  public String getComplaintByPhoneNumber() {
    return complaintByPhoneNumber;
  }

  public void setComplaintByPhoneNumber(String complaintByPhoneNumber) {
    this.complaintByPhoneNumber = complaintByPhoneNumber;
  }

  public String getComplaintStatus() {
    return complaintStatus;
  }

  public void setComplaintStatus(String complaintStatus) {
    this.complaintStatus = complaintStatus;
  }

  public String getForwardedDate() {
    return forwardedDate;
  }

  public void setForwardedDate(String forwardedDate) {
    this.forwardedDate = forwardedDate;
  }

  public String getForwardedEmployeeId() {
    return forwardedEmployeeId;
  }

  public void setForwardedEmployeeId(String forwardedEmployeeId) {
    this.forwardedEmployeeId = forwardedEmployeeId;
  }

  public String getResponseDate() {
    return responseDate;
  }

  public void setResponseDate(String responseDate) {
    this.responseDate = responseDate;
  }

  public String getResponseDetails() {
    return responseDetails;
  }

  public void setResponseDetails(String responseDetails) {
    this.responseDetails = responseDetails;
  }

  public String getFeedbackDate() {
    return feedbackDate;
  }

  public void setFeedbackDate(String feedbackDate) {
    this.feedbackDate = feedbackDate;
  }

  public String getWorkStatus() {
    return workStatus;
  }

  public void setWorkStatus(String workStatus) {
    this.workStatus = workStatus;
  }

  public String getFeedbackInformation() {
    return feedbackInformation;
  }

  public void setFeedbackInformation(String feedbackInformation) {
    this.feedbackInformation = feedbackInformation;
  }

  public String getOtherInfo() {
    return otherInfo;
  }

  public void setOtherInfo(String otherInfo) {
    this.otherInfo = otherInfo;
  }

  public String getPendingResponseCode() {
    return pendingResponseCode;
  }

  public void setPendingResponseCode(String pendingResponseCode) {
    this.pendingResponseCode = pendingResponseCode;
  }

  public String getOtherPendingRemarks() {
    return otherPendingRemarks;
  }

  public void setOtherPendingRemarks(String otherPendingRemarks) {
    this.otherPendingRemarks = otherPendingRemarks;
  }

  public String getTentativeDate() {
    return tentativeDate;
  }

  public void setTentativeDate(String tentativeDate) {
    this.tentativeDate = tentativeDate;
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

  public String getCustomerReferenceNumber() {
    return customerReferenceNumber;
  }

  public void setCustomerReferenceNumber(String customerReferenceNumber) {
    this.customerReferenceNumber = customerReferenceNumber;
  }

  public String getComplaintPhoneNumber() {
    return complaintPhoneNumber;
  }

  public void setComplaintPhoneNumber(String complaintPhoneNumber) {
    this.complaintPhoneNumber = complaintPhoneNumber;
  }

  public String getComplaintMobileNumber() {
    return complaintMobileNumber;
  }

  public void setComplaintMobileNumber(String complaintMobileNumber) {
    this.complaintMobileNumber = complaintMobileNumber;
  }

  public String getCustomerFeedback() {
    return customerFeedback;
  }

  public void setCustomerFeedback(String customerFeedback) {
    this.customerFeedback = customerFeedback;
  }

  public Boolean getConfirm() {
    return confirm;
  }

  public void setConfirm(Boolean confirm) {
    this.confirm = confirm;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  public String getCustomerRemarks() {
    return customerRemarks;
  }

  public void setCustomerRemarks(String customerRemarks) {
    this.customerRemarks = customerRemarks;
  }

  public String getWebComplaintReferenceNo() {
    return webComplaintReferenceNo;
  }

  public void setWebComplaintReferenceNo(String webComplaintReferenceNo) {
    this.webComplaintReferenceNo = webComplaintReferenceNo;
  }

  public String getAssetMake() {
    return assetMake;
  }

  public void setAssetMake(String assetMake) {
    this.assetMake = assetMake;
  }

  public String getAssetModel() {
    return assetModel;
  }

  public void setAssetModel(String assetModel) {
    this.assetModel = assetModel;
  }

  public String getAssetSize() {
    return assetSize;
  }

  public void setAssetSize(String assetSize) {
    this.assetSize = assetSize;
  }

  public String getEquipmentName() {
    return equipmentName;
  }

  public void setEquipmentName(String equipmentName) {
    this.equipmentName = equipmentName;
  }

  public String getWorkType() {
    return workType;
  }

  public void setWorkType(String workType) {
    this.workType = workType;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public String getQuotationJob() {
    return quotationJob;
  }

  public void setQuotationJob(String quotationJob) {
    this.quotationJob = quotationJob;
  }

  public String getContainmentDate() {
    return containmentDate;
  }

  public void setContainmentDate(String containmentDate) {
    this.containmentDate = containmentDate;
  }

  public String getContractNo() {
    return contractNo;
  }

  public void setContractNo(String contractNo) {
    this.contractNo = contractNo;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAssetBarCode() {
    return assetBarCode;
  }

  public void setAssetBarCode(String assetBarCode) {
    this.assetBarCode = assetBarCode;
  }

  public String getAssetCode() {
    return assetCode;
  }

  public void setAssetCode(String assetCode) {
    this.assetCode = assetCode;
  }

  public String getSupervisorRemark() {
    return supervisorRemark;
  }

  public void setSupervisorRemark(String supervisorRemark) {
    this.supervisorRemark = supervisorRemark;
  }

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  public String getCheckedBy() {
    return checkedBy;
  }

  public void setCheckedBy(String checkedBy) {
    this.checkedBy = checkedBy;
  }

  public String getAttendedBy() {
    return attendedBy;
  }

  public void setAttendedBy(String attendedBy) {
    this.attendedBy = attendedBy;
  }

  public String getSupervisor() {
    return supervisor;
  }

  public void setSupervisor(String supervisor) {
    this.supervisor = supervisor;
  }

  public String getEngineer() {
    return engineer;
  }

  public void setEngineer(String engineer) {
    this.engineer = engineer;
  }

  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public String getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(String modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public String getCustomerSignStatus() {
    return customerSignStatus;
  }

  public void setCustomerSignStatus(String customerSignStatus) {
    this.customerSignStatus = customerSignStatus;
  }

  public String getDefect() {
    return defect;
  }

  public void setDefect(String defect) {
    this.defect = defect;
  }

  public String getOtherDefects() {
    return otherDefects;
  }

  public void setOtherDefects(String otherDefects) {
    this.otherDefects = otherDefects;
  }

  public String getWorkDone() {
    return workDone;
  }

  public void setWorkDone(String workDone) {
    this.workDone = workDone;
  }

  public String getOtherWorkDone() {
    return otherWorkDone;
  }

  public void setOtherWorkDone(String otherWorkDone) {
    this.otherWorkDone = otherWorkDone;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.opco);
    dest.writeString(this.equipmentName);
    dest.writeString(this.assetMake);
    dest.writeString(this.assetModel);
    dest.writeString(this.assetSize);
    dest.writeString(this.complaintSite);
    dest.writeString(this.complaintNumber);
    dest.writeString(this.complaintYear);
    dest.writeString(this.complaintDate);
    dest.writeString(this.complaintType);
    dest.writeString(this.region);
    dest.writeString(this.regionName);
    dest.writeString(this.jobType);
    dest.writeString(this.jobTypeName);
    dest.writeString(this.jobDescription);
    dest.writeString(this.jobNumber);
    dest.writeString(this.zoneCode);
    dest.writeString(this.zoneName);
    dest.writeString(this.buildingCode);
    dest.writeString(this.buildingName);
    dest.writeString(this.location);
    dest.writeString(this.locationCode);
    dest.writeString(this.locationDescription);
    dest.writeString(this.workCategory);
    dest.writeString(this.vendorCode);
    dest.writeString(this.complaintCode);
    dest.writeString(this.complaintDetails);
    dest.writeString(this.complaintBy);
    dest.writeString(this.complaintByPhoneNumber);
    dest.writeString(this.complaintStatus);
    dest.writeString(this.forwardedDate);
    dest.writeString(this.forwardedEmployeeId);
    dest.writeString(this.responseDate);
    dest.writeString(this.responseDetails);
    dest.writeString(this.feedbackDate);
    dest.writeString(this.workStatus);
    dest.writeString(this.feedbackInformation);
    dest.writeString(this.otherInfo);
    dest.writeString(this.pendingResponseCode);
    dest.writeString(this.otherPendingRemarks);
    dest.writeString(this.tentativeDate);
    dest.writeString(this.floor);
    dest.writeString(this.flat);
    dest.writeString(this.customerReferenceNumber);
    dest.writeString(this.complaintPhoneNumber);
    dest.writeString(this.complaintMobileNumber);
    dest.writeString(this.customerFeedback);
    dest.writeString(this.createdBy);
    dest.writeString(this.createdDate);
    dest.writeString(this.customerRemarks);
    dest.writeString(this.webComplaintReferenceNo);
    dest.writeString(this.workType);
    dest.writeString(this.priority);
    dest.writeString(this.quotationJob);
    dest.writeString(this.containmentDate);
    dest.writeString(this.contractNo);
    dest.writeString(this.email);
    dest.writeString(this.assetBarCode);
    dest.writeString(this.assetCode);
    dest.writeString(this.supervisorRemark);
    dest.writeString(this.checkedBy);
    dest.writeString(this.attendedBy);
    dest.writeString(this.supervisor);
    dest.writeString(this.engineer);
    dest.writeString(this.modifiedBy);
    dest.writeString(this.modifiedDate);
    dest.writeString(this.customerSignStatus);
    dest.writeString(this.defect);
    dest.writeString(this.otherDefects);
    dest.writeString(this.workDone);
    dest.writeString(this.otherWorkDone);
    dest.writeString(this.clientBarcode);
  }
}
