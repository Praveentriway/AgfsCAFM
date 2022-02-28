package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PpeAfterSaveResponse {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("object")
    private List<PPEFetchSaveEntity> ppeFetchSaveEntities;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<PPEFetchSaveEntity> getObject() {
        return ppeFetchSaveEntities;
    }

    public void setObject(List<PPEFetchSaveEntity> PPESaveEntity) {
        ppeFetchSaveEntities = PPESaveEntity;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
