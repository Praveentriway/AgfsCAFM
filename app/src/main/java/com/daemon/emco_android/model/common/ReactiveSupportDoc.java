package com.daemon.emco_android.model.common;

import android.os.Parcel;
import android.os.Parcelable;

public class ReactiveSupportDoc implements Parcelable {

    private String opco;
    private String siteCode;
    private String siteName;
    private String complaintNo;
    private String complaintDate;
    private String workType;
    private String workDesc;
    private String taskType;
    private String taskTypeDesc;
    private String priority;
    private String regionCode;
    private String regionName;
    private String jobType;
    private String jobTypeDesc;
    private String contractNo;
    private String zoneCode;
    private String zoneName;
    private String buildingCode;
    private String buildingName;
    private String location;
    private String floor;
    private String flat;
    private String workNature;
    private String workNatureDesc;
    private String subCatergory;
    private String subCatergoryDesc;
    private String customerRefno;
    private String complaintsDetails;
    private String complaintBy;
    private String phoneNo;
    private String status;
    private String statusDesc;
    private String forwardedDate;
    private String appointmentDate;
    private String responseDate;
    private String responseDetails;
    private String containtmentDate;
    private String feedbackDate;
    private String completedDate;
    private String escalationDate  ;
    private String feedbackInfo;
    private String defects;
    private String workdone;
    private String quotationJob;
    private String closedIn;
    private String recordType;
    private String barcode;
    private String clientBarcode;
    private boolean checked = false;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getClientBarcode() {
        return clientBarcode;
    }

    public void setClientBarcode(String clientBarcode) {
        this.clientBarcode = clientBarcode;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getOpco() {
        return opco;
    }

    public void setOpco(String opco) {
        this.opco = opco;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getComplaintNo() {
        return complaintNo;
    }

    public void setComplaintNo(String complaintNo) {
        this.complaintNo = complaintNo;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkDesc() {
        return workDesc;
    }

    public void setWorkDesc(String workDesc) {
        this.workDesc = workDesc;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskTypeDesc() {
        return taskTypeDesc;
    }

    public void setTaskTypeDesc(String taskTypeDesc) {
        this.taskTypeDesc = taskTypeDesc;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
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

    public String getJobTypeDesc() {
        return jobTypeDesc;
    }

    public void setJobTypeDesc(String jobTypeDesc) {
        this.jobTypeDesc = jobTypeDesc;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
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

    public String getWorkNature() {
        return workNature;
    }

    public void setWorkNature(String workNature) {
        this.workNature = workNature;
    }

    public String getWorkNatureDesc() {
        return workNatureDesc;
    }

    public void setWorkNatureDesc(String workNatureDesc) {
        this.workNatureDesc = workNatureDesc;
    }

    public String getSubCatergory() {
        return subCatergory;
    }

    public void setSubCatergory(String subCatergory) {
        this.subCatergory = subCatergory;
    }

    public String getSubCatergoryDesc() {
        return subCatergoryDesc;
    }

    public void setSubCatergoryDesc(String subCatergoryDesc) {
        this.subCatergoryDesc = subCatergoryDesc;
    }

    public String getCustomerRefno() {
        return customerRefno;
    }

    public void setCustomerRefno(String customerRefno) {
        this.customerRefno = customerRefno;
    }

    public String getComplaintsDetails() {
        return complaintsDetails;
    }

    public void setComplaintsDetails(String complaintsDetails) {
        this.complaintsDetails = complaintsDetails;
    }

    public String getComplaintBy() {
        return complaintBy;
    }

    public void setComplaintBy(String complaintBy) {
        this.complaintBy = complaintBy;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getForwardedDate() {
        return forwardedDate;
    }

    public void setForwardedDate(String forwardedDate) {
        this.forwardedDate = forwardedDate;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
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

    public String getContaintmentDate() {
        return containtmentDate;
    }

    public void setContaintmentDate(String containtmentDate) {
        this.containtmentDate = containtmentDate;
    }

    public String getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(String feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getEscalationDate() {
        return escalationDate;
    }

    public void setEscalationDate(String escalationDate) {
        this.escalationDate = escalationDate;
    }

    public String getFeedbackInfo() {
        return feedbackInfo;
    }

    public void setFeedbackInfo(String feedbackInfo) {
        this.feedbackInfo = feedbackInfo;
    }

    public String getDefects() {
        return defects;
    }

    public void setDefects(String defects) {
        this.defects = defects;
    }

    public String getWorkdone() {
        return workdone;
    }

    public void setWorkdone(String workdone) {
        this.workdone = workdone;
    }

    public String getQuotationJob() {
        return quotationJob;
    }

    public void setQuotationJob(String quotationJob) {
        this.quotationJob = quotationJob;
    }

    public String getClosedIn() {
        return closedIn;
    }

    public void setClosedIn(String closedIn) {
        this.closedIn = closedIn;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.opco);
        dest.writeString(this.siteCode);
        dest.writeString(this.siteName);
        dest.writeString(this.complaintNo);
        dest.writeString(this.complaintDate);
        dest.writeString(this.workType);
        dest.writeString(this.workDesc);
        dest.writeString(this.taskType);
        dest.writeString(this.taskTypeDesc);
        dest.writeString(this.priority);
        dest.writeString(this.regionCode);
        dest.writeString(this.regionName);
        dest.writeString(this.jobType);
        dest.writeString(this.jobTypeDesc);
        dest.writeString(this.contractNo);
        dest.writeString(this.zoneCode);
        dest.writeString(this.zoneName);
        dest.writeString(this.buildingCode);
        dest.writeString(this.buildingName);
        dest.writeString(this.location);
        dest.writeString(this.floor);
        dest.writeString(this.flat);
        dest.writeString(this.workNature);
        dest.writeString(this.workNatureDesc);
        dest.writeString(this.subCatergory);
        dest.writeString(this.subCatergoryDesc);
        dest.writeString(this.customerRefno);
        dest.writeString(this.complaintsDetails);
        dest.writeString(this.complaintBy);
        dest.writeString(this.phoneNo);
        dest.writeString(this.status);
        dest.writeString(this.statusDesc);
        dest.writeString(this.forwardedDate);
        dest.writeString(this.appointmentDate);
        dest.writeString(this.responseDate);
        dest.writeString(this.responseDetails);
        dest.writeString(this.containtmentDate);
        dest.writeString(this.feedbackDate);
        dest.writeString(this.completedDate);
        dest.writeString(this.escalationDate);
        dest.writeString(this.feedbackInfo);
        dest.writeString(this.defects);
        dest.writeString(this.workdone);
        dest.writeString(this.quotationJob);
        dest.writeString(this.closedIn);
        dest.writeString(this.recordType);
    }

    public ReactiveSupportDoc() {
    }

    protected ReactiveSupportDoc(Parcel in) {
        this.opco = in.readString();
        this.siteCode = in.readString();
        this.siteName = in.readString();
        this.complaintNo = in.readString();
        this.complaintDate = in.readString();
        this.workType = in.readString();
        this.workDesc = in.readString();
        this.taskType = in.readString();
        this.taskTypeDesc = in.readString();
        this.priority = in.readString();
        this.regionCode = in.readString();
        this.regionName = in.readString();
        this.jobType = in.readString();
        this.jobTypeDesc = in.readString();
        this.contractNo = in.readString();
        this.zoneCode = in.readString();
        this.zoneName = in.readString();
        this.buildingCode = in.readString();
        this.buildingName = in.readString();
        this.location = in.readString();
        this.floor = in.readString();
        this.flat = in.readString();
        this.workNature = in.readString();
        this.workNatureDesc = in.readString();
        this.subCatergory = in.readString();
        this.subCatergoryDesc = in.readString();
        this.customerRefno = in.readString();
        this.complaintsDetails = in.readString();
        this.complaintBy = in.readString();
        this.phoneNo = in.readString();
        this.status = in.readString();
        this.statusDesc = in.readString();
        this.forwardedDate = in.readString();
        this.appointmentDate = in.readString();
        this.responseDate = in.readString();
        this.responseDetails = in.readString();
        this.containtmentDate = in.readString();
        this.feedbackDate = in.readString();
        this.completedDate = in.readString();
        this.escalationDate = in.readString();
        this.feedbackInfo = in.readString();
        this.defects = in.readString();
        this.workdone = in.readString();
        this.quotationJob = in.readString();
        this.closedIn = in.readString();
        this.recordType = in.readString();
    }

    public static final Parcelable.Creator<ReactiveSupportDoc> CREATOR = new Parcelable.Creator<ReactiveSupportDoc>() {
        @Override
        public ReactiveSupportDoc createFromParcel(Parcel source) {
            return new ReactiveSupportDoc(source);
        }

        @Override
        public ReactiveSupportDoc[] newArray(int size) {
            return new ReactiveSupportDoc[size];
        }
    };
}
