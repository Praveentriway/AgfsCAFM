package com.daemon.emco_android.model.response;

import com.daemon.emco_android.model.request.LocationDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LocationResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("object")
    @Expose
    ArrayList<LocationDetail> location;

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

    public ArrayList<LocationDetail> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<LocationDetail> location) {
        this.location = location;
    }
}
