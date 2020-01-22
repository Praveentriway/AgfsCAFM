package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.WorkDefectEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkDefectResponse {

  @SerializedName("message")
  private String mMessage;

  @SerializedName("object")
  private List<WorkDefectEntity> mWorkDefectEntity;

  @SerializedName("status")
  private String mStatus;

  public String getMessage() {
    return mMessage;
  }

  public void setMessage(String message) {
    mMessage = message;
  }

  public List<WorkDefectEntity> getObject() {
    return mWorkDefectEntity;
  }

  public void setObject(List<WorkDefectEntity> workDefectEntity) {
    mWorkDefectEntity = workDefectEntity;
  }

  public String getStatus() {
    return mStatus;
  }

  public void setStatus(String status) {
    mStatus = status;
  }
}
