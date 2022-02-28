package com.daemon.emco_android.model.response;

import com.daemon.emco_android.model.common.EmployeeTrackingDetail;
import com.google.gson.annotations.SerializedName;

public class EmployeeGpsReponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("object")
    private EmployeeTrackingDetail object;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EmployeeTrackingDetail getObject() {
        return object;
    }

    public void setObject(EmployeeTrackingDetail object) {
        this.object = object;
    }
}
