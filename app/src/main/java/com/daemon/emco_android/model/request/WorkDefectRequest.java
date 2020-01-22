package com.daemon.emco_android.model.request;

import com.google.gson.annotations.SerializedName;

public class WorkDefectRequest {

    @SerializedName("complaintCode")
    private String mComplaintCode;
    @SerializedName("workCategory")
    private String mWorkCategory;

    public WorkDefectRequest(String mComplaintCode, String mWorkCategory) {
        this.mComplaintCode = mComplaintCode;
        this.mWorkCategory = mWorkCategory;
    }

    public String getComplaintCode() {
        return mComplaintCode;
    }

    public void setComplaintCode(String complaintCode) {
        mComplaintCode = complaintCode;
    }

    public String getWorkCategory() {
        return mWorkCategory;
    }

    public void setWorkCategory(String workCategory) {
        mWorkCategory = workCategory;
    }

}
