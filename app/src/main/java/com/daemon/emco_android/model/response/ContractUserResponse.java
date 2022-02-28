package com.daemon.emco_android.model.response;

import com.daemon.emco_android.model.common.ContractNoUsers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContractUserResponse {
    @SerializedName("message")
    private String mMessage;

    @SerializedName("object")
    private List<ContractNoUsers> mContract;

    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<ContractNoUsers> getObject() {
        return mContract;
    }

    public void setObject(List<ContractNoUsers> contract) {
        mContract = contract;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}
