package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.WorkPendingReasonEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkPendingReasonResponse {

  @SerializedName("message")
  private String mMessage;

  @SerializedName("object")
  private List<WorkPendingReasonEntity> mWorkPendingReasonEntity;

  @SerializedName("status")
  private String mStatus;

  public String getMessage() {
    return mMessage;
  }

  public void setMessage(String message) {
    mMessage = message;
  }

  public List<WorkPendingReasonEntity> getObject() {
    return mWorkPendingReasonEntity;
  }

  public void setObject(List<WorkPendingReasonEntity> workPendingReasonEntity) {
    mWorkPendingReasonEntity = workPendingReasonEntity;
  }

  public String getStatus() {
    return mStatus;
  }

  public void setStatus(String status) {
    mStatus = status;
  }
}
