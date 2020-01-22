
package com.daemon.emco_android.model.response;

import java.util.ArrayList;

import javax.annotation.Generated;

import com.daemon.emco_android.repository.db.entity.PPENameEntity;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class FetchPPENameResponse {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("object")
    private ArrayList<PPENameEntity> mPPENameEntity;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public ArrayList<PPENameEntity> getObject() {
        return mPPENameEntity;
    }

    public void setObject(ArrayList<PPENameEntity> PPENameEntity) {
        mPPENameEntity = PPENameEntity;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
