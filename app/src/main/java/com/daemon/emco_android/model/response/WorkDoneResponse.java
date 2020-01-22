package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.WorkDoneEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkDoneResponse {

  @SerializedName("message")
  private String mMessage;

  @SerializedName("object")
  private List<WorkDoneEntity> mWorkDoneEntity;

  @SerializedName("status")
  private String mStatus;

  public String getMessage() {
    return mMessage;
  }

  public void setMessage(String message) {
    mMessage = message;
  }

  public List<WorkDoneEntity> getObject() {
    return mWorkDoneEntity;
  }

  public void setObject(List<WorkDoneEntity> workDoneEntity) {
    mWorkDoneEntity = workDoneEntity;
  }

  public String getStatus() {
    return mStatus;
  }

  public void setStatus(String status) {
    mStatus = status;
  }
}
