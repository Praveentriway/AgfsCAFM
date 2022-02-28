package com.daemon.emco_android.model.response;


import com.daemon.emco_android.model.common.UtilityAssetDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UtilityConsumptionResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("object")
    @Expose
    private List<UtilityAssetDetail> details = new ArrayList<UtilityAssetDetail>();

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

    public List<UtilityAssetDetail> getDetails() {
        return details;
    }

    public void setDetails(List<UtilityAssetDetail> details) {
        this.details = details;
    }
}
