package com.daemon.emco_android.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HardSoftView implements Parcelable {

    public static final Creator<HardSoftView> CREATOR =
            new Creator<HardSoftView>() {
                @Override
                public HardSoftView createFromParcel(Parcel source) {
                    return new HardSoftView(source);
                }

                @Override
                public HardSoftView[] newArray(int size) {
                    return new HardSoftView[size];
                }
            };

    @SerializedName("complaitDetail")
    @Expose
    private ComplaitDetail complaitDetail;

    @SerializedName("assetType")
    @Expose
    private String assetType;

    @SerializedName("assetDescription")
    @Expose
    private String assetDescription;

    @SerializedName("make")
    @Expose
    private String make;

    @SerializedName("model")
    @Expose
    private String model;

    @SerializedName("materialUsed")
    @Expose
    private String materialUsed;

    @SerializedName("jobNumber")
    @Expose
    private String jobNumber;

    @SerializedName("opco")
    @Expose
    private String opco;

    @SerializedName("complaintSite")
    @Expose
    private String complaintSite;

    @SerializedName("complaintNumber")
    @Expose
    private String complaintNumber;

    @SerializedName("ppmRefNo")
    @Expose
    private String ppmRefNo;

    @SerializedName("supervisorRemark")
    @Expose
    private String supervisorRemark;

    @SerializedName("actStartDate")
    @Expose
    private String actStartDate;

    @SerializedName("actEndDate")
    @Expose
    private String actEndDate;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("natureDescription")
    @Expose
    private String natureDescription;

    @SerializedName("siteDescription")
    @Expose
    private String siteDescription;

    @SerializedName("assetBarCode")
    @Expose
    private String assetBarCode;

    @SerializedName("assetMake")
    @Expose
    private String assetMake;

    @SerializedName("assetModel")
    @Expose
    private String assetModel;

    @SerializedName("defect")
    @Expose
    private String defect;

    @SerializedName("complaintDetails")
    @Expose
    private String complaintDetails;

    @SerializedName("complaintDate")
    @Expose
    private String complaintDate;

    @SerializedName("workDone")
    @Expose
    private String workDone;

    @SerializedName("customerRemarks")
    @Expose
    private String customerRemarks;

    @SerializedName("defectDescription")
    @Expose
    private String defectDescription;

    @SerializedName("workDoneDescription")
    @Expose
    private String workDoneDescription;

    @SerializedName("buildingCode")
    @Expose
    private String buildingCode;

    @SerializedName("zoneCode")
    @Expose
    private String zoneCode;

    @SerializedName("technicianRemark")
    @Expose
    private String technicianRemark;

    public HardSoftView() {
    }

    protected HardSoftView(Parcel in) {
        this.assetDescription = in.readString();
        this.opco=in.readString();
        this.ppmRefNo=in.readString();
        this.complaintNumber=in.readString();
        this.complaintSite=in.readString();
        this.actStartDate=in.readString();
        this.actEndDate=in.readString();
        this.location=in.readString();
        this.natureDescription=in.readString();
        this.siteDescription=in.readString();
        this.assetBarCode=in.readString();
        this.supervisorRemark=in.readString();
        this.zoneCode=in.readString();
        this.buildingCode=in.readString();
    }

    public String getSupervisorRemark() {
        return supervisorRemark;
    }

    public void setSupervisorRemark(String supervisorRemark) {
        this.supervisorRemark = supervisorRemark;
    }

    public String getLocation() {
        return location;
    }

    public String getAssetBarCode() {
        return assetBarCode;
    }

    public void setAssetBarCode(String assetBarCode) {
        this.assetBarCode = assetBarCode;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getNatureDescription() {
        return natureDescription;
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

    public String getDefect() {
        return defect;
    }

    public void setDefect(String defect) {
        this.defect = defect;
    }

    public String getComplaintDetails() {
        return complaintDetails;
    }

    public void setComplaintDetails(String complaintDetails) {
        this.complaintDetails = complaintDetails;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getWorkDone() {
        return workDone;
    }

    public void setWorkDone(String workDone) {
        this.workDone = workDone;
    }

    public String getCustomerRemarks() {
        return customerRemarks;
    }

    public void setCustomerRemarks(String customerRemarks) {
        this.customerRemarks = customerRemarks;
    }

    public String getDefectDescription() {
        return defectDescription;
    }

    public void setDefectDescription(String defectDescription) {
        this.defectDescription = defectDescription;
    }

    public String getWorkDoneDescription() {
        return workDoneDescription;
    }

    public void setWorkDoneDescription(String workDoneDescription) {
        this.workDoneDescription = workDoneDescription;
    }

    public String getTechnicianRemark() {
        return technicianRemark;
    }

    public void setTechnicianRemark(String technicianRemark) {
        this.technicianRemark = technicianRemark;
    }

    public void setNatureDescription(String natureDescription) {
        this.natureDescription = natureDescription;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    /**
     * @return The complaitDetail
     */
    public ComplaitDetail getComplaitDetail() {
        return complaitDetail;
    }

    /**
     * @param complaitDetail The complaitDetail
     */
    public void setComplaitDetail(ComplaitDetail complaitDetail) {
        this.complaitDetail = complaitDetail;
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

    public String getPpmRefNo() {
        return ppmRefNo;
    }

    public void setPpmRefNo(String ppmRefNo) {
        this.ppmRefNo = ppmRefNo;
    }

    public String getActStartDate() {
        return actStartDate;
    }

    public void setActStartDate(String actStartDate) {
        this.actStartDate = actStartDate;
    }

    public String getActEndDate() {
        return actEndDate;
    }

    public void setActEndDate(String actEndDate) {
        this.actEndDate = actEndDate;
    }

    /**
     * @return The assetType
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * @param assetType The assetType
     */
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    /**
     * @return The assetDescription
     */
    public String getAssetDescription() {
        return assetDescription;
    }

    /**
     * @param assetDescription The assetDescription
     */
    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    /**
     * @return The make
     */
    public String getMake() {
        return make;
    }

    /**
     * @param make The make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * @return The model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model The model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return The materialUsed
     */
    public String getMaterialUsed() {
        return materialUsed;
    }

    /**
     * @param materialUsed The materialUsed
     */
    public void setMaterialUsed(String materialUsed) {
        this.materialUsed = materialUsed;
    }

    /**
     * @return The siteDescription
     */
    public String getSiteDescription() {
        return siteDescription;
    }

    /**
     * @param siteDescription The siteDescription
     */
    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
