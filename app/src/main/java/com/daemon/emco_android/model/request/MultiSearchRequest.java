package com.daemon.emco_android.model.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MultiSearchRequest implements Parcelable {

  public static final Parcelable.Creator<MultiSearchRequest> CREATOR =
      new Parcelable.Creator<MultiSearchRequest>() {
        @Override
        public MultiSearchRequest createFromParcel(Parcel source) {
          return new MultiSearchRequest(source);
        }

        @Override
        public MultiSearchRequest[] newArray(int size) {
          return new MultiSearchRequest[size];
        }
      };

  @SerializedName("buildingCode")
  private String buildingCode;

  @SerializedName("opco")
  private String opco;

  public String getUserCode() {
    return userCode;
  }

  public void setUserCode(String userCode) {
    this.userCode = userCode;
  }

  @SerializedName("userCode")
  private String userCode;

  @SerializedName("siteCode")
  private String siteCode;

  @SerializedName("contractNo")
  private String contractNo;

  @SerializedName("fromDate")
  private String fromDate;

  @SerializedName("toDate")
  private String toDate;

  @SerializedName("employeeId")
  private String employeeId;

  @SerializedName("reportType")
  private String reportType;

  @SerializedName("zoneCode")
  private String zoneCode;

  @SerializedName("code")
  private String code;

  @SerializedName("startIndex")
  private String startIndex;

  @SerializedName("rowsExpected")
  private String rowsExpected;

  public MultiSearchRequest() {}

  protected MultiSearchRequest(Parcel in) {
    this.buildingCode = in.readString();
    this.opco = in.readString();
    this.siteCode = in.readString();
    this.contractNo = in.readString();
    this.fromDate = in.readString();
    this.toDate = in.readString();
    this.employeeId = in.readString();
    this.reportType = in.readString();
    this.zoneCode = in.readString();
    this.code = in.readString();
    this.startIndex = in.readString();
    this.rowsExpected = in.readString();
  }

  public String getBuildingCode() {
    return buildingCode;
  }

  public void setBuildingCode(String buildingCode) {
    this.buildingCode = buildingCode;
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

  public String getContractNo() {
    return contractNo;
  }

  public void setContractNo(String contractNo) {
    this.contractNo = contractNo;
  }

  public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  public String getToDate() {
    return toDate;
  }

  public void setToDate(String toDate) {
    this.toDate = toDate;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
  }

  public String getReportType() {
    return reportType;
  }

  public void setReportType(String reportType) {
    this.reportType = reportType;
  }

  public String getZoneCode() {
    return zoneCode;
  }

  public void setZoneCode(String zoneCode) {
    this.zoneCode = zoneCode;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getStartIndex() {
    return startIndex;
  }

  public void setStartIndex(String startIndex) {
    this.startIndex = startIndex;
  }

  public String getRowsExpected() {
    return rowsExpected;
  }

  public void setRowsExpected(String rowsExpected) {
    this.rowsExpected = rowsExpected;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.buildingCode);
    dest.writeString(this.opco);
    dest.writeString(this.siteCode);
    dest.writeString(this.contractNo);
    dest.writeString(this.fromDate);
    dest.writeString(this.toDate);
    dest.writeString(this.employeeId);
    dest.writeString(this.reportType);
    dest.writeString(this.zoneCode);
    dest.writeString(this.code);
    dest.writeString(this.startIndex);
    dest.writeString(this.rowsExpected);
  }
}
