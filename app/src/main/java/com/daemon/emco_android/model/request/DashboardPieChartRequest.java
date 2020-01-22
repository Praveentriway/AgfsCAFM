package com.daemon.emco_android.model.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class DashboardPieChartRequest implements Parcelable {

    @SerializedName("opco")
    @Expose
    private String opco;
    @SerializedName("siteCode")
    @Expose
    private String siteCode;
    @SerializedName("contractNo")
    @Expose
    private String contractNo;
    @SerializedName("fromDate")
    @Expose
    private String fromDate;
    @SerializedName("toDate")
    @Expose
    private String toDate;
    @SerializedName("employeeId")
    @Expose
    private String employeeId;
    @SerializedName("reportType")
    @Expose
    private String reportType;
    @SerializedName("zoneCode")
    @Expose
    private String zoneCode;

    /**
     * @return The opco
     */
    public String getOpco() {
        return opco;
    }

    /**
     * @param opco The opco
     */
    public void setOpco(String opco) {
        this.opco = opco;
    }

    /**
     * @return The siteCode
     */
    public String getSiteCode() {
        return siteCode;
    }

    /**
     * @param siteCode The siteCode
     */
    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    /**
     * @return The contractNo
     */
    public String getContractNo() {
        return contractNo;
    }

    /**
     * @param contractNo The contractNo
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    /**
     * @return The fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate The fromDate
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return The toDate
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * @param toDate The toDate
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    /**
     * @return The employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId The employeeId
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return The reportType
     */
    public String getReportType() {
        return reportType;
    }

    /**
     * @param reportType The reportType
     */
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    /**
     * @return The zoneCode
     */
    public String getZoneCode() {
        return zoneCode;
    }

    /**
     * @param zoneCode The zoneCode
     */
    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.opco);
        dest.writeString(this.siteCode);
        dest.writeString(this.contractNo);
        dest.writeString(this.fromDate);
        dest.writeString(this.toDate);
        dest.writeString(this.employeeId);
        dest.writeString(this.reportType);
        dest.writeString(this.zoneCode);
    }

    public DashboardPieChartRequest() {
    }

    protected DashboardPieChartRequest(Parcel in) {
        this.opco = in.readString();
        this.siteCode = in.readString();
        this.contractNo = in.readString();
        this.fromDate = in.readString();
        this.toDate = in.readString();
        this.employeeId = in.readString();
        this.reportType = in.readString();
        this.zoneCode = in.readString();
    }

    public static final Parcelable.Creator<DashboardPieChartRequest> CREATOR = new Parcelable.Creator<DashboardPieChartRequest>() {
        @Override
        public DashboardPieChartRequest createFromParcel(Parcel source) {
            return new DashboardPieChartRequest(source);
        }

        @Override
        public DashboardPieChartRequest[] newArray(int size) {
            return new DashboardPieChartRequest[size];
        }
    };
}
