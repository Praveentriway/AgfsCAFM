package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.SingleSearchComplaintEntity;
import com.google.gson.annotations.SerializedName;

public class SingleSearchComplaintResponse {

  @SerializedName("message")
  private String mMessage;

  @SerializedName("object")
  private SingleSearchComplaintEntity mSingleSearchComplaintEntity;

  @SerializedName("status")
  private String mStatus;

  public String getMessage() {
    return mMessage;
  }

  public void setMessage(String message) {
    mMessage = message;
  }

  public SingleSearchComplaintEntity getObject() {
    return mSingleSearchComplaintEntity;
  }

  public void setObject(SingleSearchComplaintEntity singleSearchComplaintEntity) {
    mSingleSearchComplaintEntity = singleSearchComplaintEntity;
  }

  public String getStatus() {
    return mStatus;
  }

  public void setStatus(String status) {
    mStatus = status;
  }
}
