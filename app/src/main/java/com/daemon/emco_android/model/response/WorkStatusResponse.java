
package com.daemon.emco_android.model.response;

import java.util.List;

import com.daemon.emco_android.repository.db.entity.WorkStatusEntity;
import com.google.gson.annotations.SerializedName;

public class WorkStatusResponse {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("object")
    private List<WorkStatusEntity> mObject;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<WorkStatusEntity> getObject() {
        return mObject;
    }

    public void setObject(List<WorkStatusEntity> object) {
        mObject = object;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
