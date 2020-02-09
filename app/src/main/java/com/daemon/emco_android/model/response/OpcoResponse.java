package com.daemon.emco_android.model.response;

import com.daemon.emco_android.model.common.OpcoDetail;
import com.daemon.emco_android.model.request.LocationDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OpcoResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("object")
    @Expose
    ArrayList<OpcoDetail> opco;

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

    public ArrayList<OpcoDetail> getOpco() {
        return opco;
    }

    public void setOpco(ArrayList<OpcoDetail> opco) {
        this.opco = opco;
    }
}

