package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.PriorityEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PriorityResponse {

  @SerializedName("message")
  private String mMessage;

  @SerializedName("object")
  private List<PriorityEntity> mPriority;

  @SerializedName("status")
  private String mStatus;

  public String getMessage() {
    return mMessage;
  }

  public void setMessage(String message) {
    mMessage = message;
  }

  public List<PriorityEntity> getObject() {
    return mPriority;
  }

  public void setObject(List<PriorityEntity> priority) {
    mPriority = priority;
  }

  public String getStatus() {
    return mStatus;
  }

  public void setStatus(String status) {
    mStatus = status;
  }
}
