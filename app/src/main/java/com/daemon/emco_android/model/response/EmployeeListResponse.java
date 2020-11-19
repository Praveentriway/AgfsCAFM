package com.daemon.emco_android.model.response;

import com.daemon.emco_android.model.common.AssetInfo;
import com.daemon.emco_android.model.common.EmployeeList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("object")
    @Expose
    private List<EmployeeList> object =
            new ArrayList<EmployeeList>();


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

    public List<EmployeeList> getObject() {
        return object;
    }

    public void setObject(List<EmployeeList> object) {
        this.object = object;
    }
}
