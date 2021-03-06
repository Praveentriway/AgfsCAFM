
package com.daemon.emco_android.model.response;

import javax.annotation.Generated;

import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LogComplaintResponse {


    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("object")
    private EmployeeIdRequest webNumber;

    public LogComplaintResponse(String mMessage, String mStatus, EmployeeIdRequest webNumber) {
        this.mMessage = mMessage;
        this.mStatus = mStatus;
        this.webNumber = webNumber;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public EmployeeIdRequest getWebNumber() {
        return webNumber;
    }

    public void setWebNumber(EmployeeIdRequest webNumber) {
        this.webNumber = webNumber;
    }
}
