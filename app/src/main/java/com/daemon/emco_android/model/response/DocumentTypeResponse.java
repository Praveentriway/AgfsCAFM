package com.daemon.emco_android.model.response;

import com.daemon.emco_android.model.common.DocumentType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DocumentTypeResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("object")
    @Expose
    private List<DocumentType> PPMDetails = new ArrayList<DocumentType>();

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

    public List<DocumentType> getPPMDetails() {
        return PPMDetails;
    }

    public void setPPMDetails(List<DocumentType> PPMDetails) {
        this.PPMDetails = PPMDetails;
    }
}
