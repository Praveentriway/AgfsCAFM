package com.daemon.emco_android.model.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HardSoftRequest implements Parcelable {

  public static final Parcelable.Creator<HardSoftRequest> CREATOR =
      new Creator<HardSoftRequest>() {

        @SuppressWarnings({"unchecked"})
        public HardSoftRequest createFromParcel(Parcel in) {
          HardSoftRequest instance = new HardSoftRequest();
          instance.jobNumber = ((String) in.readValue((String.class.getClassLoader())));
          instance.complaintSite = ((String) in.readValue((String.class.getClassLoader())));
          instance.zoneCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.buildingCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.fromDate = ((String) in.readValue((String.class.getClassLoader())));
          instance.toDate = ((String) in.readValue((String.class.getClassLoader())));
          return instance;
        }

        public HardSoftRequest[] newArray(int size) {
          return (new HardSoftRequest[size]);
        }
      };
  @SerializedName("jobNumber")
  private String jobNumber;
  @SerializedName("complaintSite")
  private String complaintSite;

  @SerializedName("zoneCode")
  private String zoneCode;

  @SerializedName("buildingCode")
  private String buildingCode;

  @SerializedName("fromDate")
  private String fromDate;
  @SerializedName("toDate")
  private String toDate;
  @SerializedName("opco")
  private String opco;
  @SerializedName("complaintNumber")
  private String complaintNumber;
  @SerializedName("supervisorRemark")
  private String supervisorRemark;
  @SerializedName("ppmNo")
  private String ppmNo;
  @SerializedName("remarks")
  private String remarks;
  @SerializedName("buildTag")
  private String buildTag;
  @SerializedName("jobNo")
  private String jobNo;
  @SerializedName("startIndex")
  private String startIndex;
  @SerializedName("limit")
  private String limit;

  @SerializedName("serviceGroup")
  private String serviceGroup;

  public String getServiceGroup() {
    return serviceGroup;
  }

  public void setServiceGroup(String serviceGroup) {
    this.serviceGroup = serviceGroup;
  }



  /** @return The jobNumber */
  public String getJobNumber() {
    return jobNumber;
  }

  public String getOpco() {
    return opco;
  }

  public void setOpco(String opco) {
    this.opco = opco;
  }

  public String getComplaintNumber() {
    return complaintNumber;
  }

  public String getPpmNo() {
    return ppmNo;
  }

  public void setPpmNo(String ppmNo) {
    this.ppmNo = ppmNo;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public void setComplaintNumber(String complaintNumber) {
    this.complaintNumber = complaintNumber;
  }

  public String getSupervisorRemark() {
    return supervisorRemark;
  }

  public void setSupervisorRemark(String supervisorRemark) {
    this.supervisorRemark = supervisorRemark;
  }

  public String getBuildTag() {
    return buildTag;
  }

  public void setBuildTag(String buildTag) {
    this.buildTag = buildTag;
  }

  public String getJobNo() {
    return jobNo;
  }

  public void setJobNo(String jobNo) {
    this.jobNo = jobNo;
  }

  public String getStartIndex() {
    return startIndex;
  }

  public void setStartIndex(String startIndex) {
    this.startIndex = startIndex;
  }

  public String getLimit() {
    return limit;
  }

  public void setLimit(String limit) {
    this.limit = limit;
  }

  /** @param jobNumber The jobNumber */
  public void setJobNumber(String jobNumber) {
    this.jobNumber = jobNumber;
  }

  /** @return The complaintSite */
  public String getComplaintSite() {
    return complaintSite;
  }

  /** @param complaintSite The complaintSite */
  public void setComplaintSite(String complaintSite) {
    this.complaintSite = complaintSite;
  }

  public String getZoneCode() {
    return zoneCode;
  }

  public void setZoneCode(String zoneCode) {
    this.zoneCode = zoneCode;
  }

  public String getBuildingCode() {
    return buildingCode;
  }

  public void setBuildingCode(String buildingCode) {
    this.buildingCode = buildingCode;
  }

  /** @return The fromDate */
  public String getFromDate() {
    return fromDate;
  }

  /** @param fromDate The fromDate */
  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  /** @return The toDate */
  public String getToDate() {
    return toDate;
  }

  /** @param toDate The toDate */
  public void setToDate(String toDate) {
    this.toDate = toDate;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(jobNumber);
    dest.writeValue(complaintSite);
    dest.writeValue(zoneCode);
    dest.writeValue(buildingCode);
    dest.writeValue(fromDate);
    dest.writeValue(toDate);
    dest.writeValue(buildTag);
    dest.writeValue(jobNo);
    dest.writeValue(startIndex);
    dest.writeValue(limit);

  }

  public int describeContents() {
    return 0;
  }
}
