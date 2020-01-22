package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.ContractEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContractResponse {

  @SerializedName("message")
  private String mMessage;

  @SerializedName("object")
  private List<ContractEntity> mContract;

  @SerializedName("status")
  private String mStatus;

  public String getMessage() {
    return mMessage;
  }

  public void setMessage(String message) {
    mMessage = message;
  }

  public List<ContractEntity> getObject() {
    return mContract;
  }

  public void setObject(List<ContractEntity> contract) {
    mContract = contract;
  }

  public String getStatus() {
    return mStatus;
  }

  public void setStatus(String status) {
    mStatus = status;
  }
}
