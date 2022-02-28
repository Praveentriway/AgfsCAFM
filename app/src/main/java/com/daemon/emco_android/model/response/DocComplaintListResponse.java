package com.daemon.emco_android.model.response;

import com.daemon.emco_android.model.common.ReactiveSupportDoc;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DocComplaintListResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("object")
    @Expose
    private List<ReactiveSupportDoc> PPMDetails = new ArrayList<ReactiveSupportDoc>();

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

    public List<ReactiveSupportDoc> getPPMDetails() {
        return PPMDetails;
    }

    public void setPPMDetails(List<ReactiveSupportDoc> PPMDetails) {
        this.PPMDetails = PPMDetails;
    }
}
