package com.daemon.emco_android.model.response;

import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerFeedBackResponse {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("object")
    private SaveRatedServiceRequest saveRatedServiceRequest;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public SaveRatedServiceRequest getObject() {
        return saveRatedServiceRequest;
    }

    public void setObject(SaveRatedServiceRequest saveRatedServiceRequest) {
        this.saveRatedServiceRequest = saveRatedServiceRequest;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
