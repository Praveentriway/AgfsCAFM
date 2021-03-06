package com.daemon.emco_android.model.request;

import com.google.gson.annotations.SerializedName;

public class AddNewUser {

    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("teleNo")
    private String teleNo;
    @SerializedName("mobileNo")
    private String mobileNo;
    @SerializedName("emailId")
    private String emailId;
    @SerializedName("propertyDetail")
    private String propertyDetail;
    @SerializedName("buildingName")
    private String buildingName;
    @SerializedName("locationDetail")
    private String locationDetail;

    /**
     * @return The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName The lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return The teleNo
     */
    public String getTeleNo() {
        return teleNo;
    }

    /**
     * @param teleNo The teleNo
     */
    public void setTeleNo(String teleNo) {
        this.teleNo = teleNo;
    }

    /**
     * @return The mobileNo
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * @param mobileNo The mobileNo
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * @return The emailId
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * @param emailId The emailId
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * @return The propertyDetail
     */
    public String getPropertyDetail() {
        return propertyDetail;
    }

    /**
     * @param propertyDetail The propertyDetail
     */
    public void setPropertyDetail(String propertyDetail) {
        this.propertyDetail = propertyDetail;
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
     * @return The locationDetail
     */
    public String getLocationDetail() {
        return locationDetail;
    }

    /**
     * @param locationDetail The locationDetail
     */
    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }

}
