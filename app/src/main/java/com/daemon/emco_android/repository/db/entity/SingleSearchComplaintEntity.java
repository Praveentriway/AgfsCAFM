package com.daemon.emco_android.repository.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class SingleSearchComplaintEntity implements Parcelable {

    public final static Parcelable.Creator<SingleSearchComplaintEntity> CREATOR = new Creator<SingleSearchComplaintEntity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SingleSearchComplaintEntity createFromParcel(Parcel in) {
            SingleSearchComplaintEntity instance = new SingleSearchComplaintEntity();
            instance.complaintNumber = ((String) in.readValue((String.class.getClassLoader())));
            instance.complainDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.location = ((String) in.readValue((String.class.getClassLoader())));
            instance.complainDetails = ((String) in.readValue((String.class.getClassLoader())));
            instance.customerRefrenceNumber = ((String) in.readValue((String.class.getClassLoader())));
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.siteName = ((String) in.readValue((String.class.getClassLoader())));
            instance.buildingName = ((String) in.readValue((String.class.getClassLoader())));
            instance.zoneName = ((String) in.readValue((String.class.getClassLoader())));
            instance.floorFlat = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public SingleSearchComplaintEntity[] newArray(int size) {
            return (new SingleSearchComplaintEntity[size]);
        }

    };
    @SerializedName("complaintNumber")
    @Expose
    private String complaintNumber;
    @SerializedName("complainDate")
    @Expose
    private String complainDate;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("complainDetails")
    @Expose
    private String complainDetails;
    @SerializedName("customerRefrenceNumber")
    @Expose
    private String customerRefrenceNumber;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("siteName")
    @Expose
    private String siteName;
    @SerializedName("buildingName")
    @Expose
    private String buildingName;
    @SerializedName("zoneName")
    @Expose
    private String zoneName;
    @SerializedName("floorFlat")
    @Expose
    private String floorFlat;

    /**
     * @return The complaintNumber
     */
    public String getComplaintNumber() {
        return complaintNumber;
    }

    /**
     * @param complaintNumber The complaintNumber
     */
    public void setComplaintNumber(String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    /**
     * @return The complainDate
     */
    public String getComplainDate() {
        return complainDate;
    }

    /**
     * @param complainDate The complainDate
     */
    public void setComplainDate(String complainDate) {
        this.complainDate = complainDate;
    }

    /**
     * @return The location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return The complainDetails
     */
    public String getComplainDetails() {
        return complainDetails;
    }

    /**
     * @param complainDetails The complainDetails
     */
    public void setComplainDetails(String complainDetails) {
        this.complainDetails = complainDetails;
    }

    /**
     * @return The customerRefrenceNumber
     */
    public String getCustomerRefrenceNumber() {
        return customerRefrenceNumber;
    }

    /**
     * @param customerRefrenceNumber The customerRefrenceNumber
     */
    public void setCustomerRefrenceNumber(String customerRefrenceNumber) {
        this.customerRefrenceNumber = customerRefrenceNumber;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The siteName
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * @param siteName The siteName
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
     * @return The buildingName
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * @param buildingName The buildingName
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    /**
     * @return The zoneName
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * @param zoneName The zoneName
     */
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    /**
     * @return The floorFlat
     */
    public String getFloorFlat() {
        return floorFlat;
    }

    /**
     * @param floorFlat The floorFlat
     */
    public void setFloorFlat(String floorFlat) {
        this.floorFlat = floorFlat;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(complaintNumber);
        dest.writeValue(complainDate);
        dest.writeValue(location);
        dest.writeValue(complainDetails);
        dest.writeValue(customerRefrenceNumber);
        dest.writeValue(status);
        dest.writeValue(siteName);
        dest.writeValue(buildingName);
        dest.writeValue(zoneName);
        dest.writeValue(floorFlat);
    }

    public int describeContents() {
        return 0;
    }

}
