
package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.ZoneEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ZoneResponse {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("object")
    private List<ZoneEntity> mZone;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<ZoneEntity> getObject() {
        return mZone;
    }

    public void setObject(List<ZoneEntity> zone) {
        mZone = zone;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
