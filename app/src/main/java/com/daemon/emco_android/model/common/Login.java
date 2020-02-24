package com.daemon.emco_android.model.common;

import com.google.gson.annotations.SerializedName;

import static com.daemon.emco_android.utils.StringUtil.IsNull;

public class Login {

    @SerializedName("createdUser")
    private String mCreatedUser;
    @SerializedName("creationDate")
    private String mCreationDate;
    @SerializedName("emailId")
    private String mEmailId;
    @SerializedName("employeeId")
    private String mEmployeeId;
    @SerializedName("firstName")
    private String mFirstName;
    @SerializedName("lastName")
    private String mLastName;
    @SerializedName("mobileNumber")
    private String mMobileNumber;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("phoneNumber")
    private String mPhoneNumber;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("userName")
    private String mUserName;
    @SerializedName("userType")
    private String mUserType;
    @SerializedName("customerCode")
    private String customerCode;
    @SerializedName("contractNo")
    private String contractNo;

    @SerializedName("trackingFlag")
    private String trackingFlag;

    public String getTrackingFlag() {
        return trackingFlag;
    }

    public void setTrackingFlag(String trackingFlag) {
        this.trackingFlag = trackingFlag;
    }

    public String getCreatedUser() {
        return mCreatedUser;
    }

    public void setCreatedUser(String createdUser) {
        mCreatedUser = createdUser;
    }

    public String getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(String creationDate) {
        mCreationDate = creationDate;
    }

    public String getEmailId() {
        return mEmailId;
    }

    public void setEmailId(String emailId) {
        mEmailId = emailId;
    }

    public String getEmployeeId() {
        return mEmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        mEmployeeId = employeeId;
    }

    public String getFirstName() {

        return (IsNull(mFirstName) ? "": mFirstName );

    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return (IsNull(mLastName) ? "": mLastName );
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserType() {
        return mUserType;
    }

    public void setUserType(String userType) {
        mUserType = userType;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }


}
