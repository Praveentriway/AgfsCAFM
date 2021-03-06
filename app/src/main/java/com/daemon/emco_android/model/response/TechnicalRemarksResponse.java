package com.daemon.emco_android.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TechnicalRemarksResponse {

  @SerializedName("message")
  private String mMessage;

  @SerializedName("object")
  private List<String> mObject;

  @SerializedName("status")
  private String mStatus;

  public String getMessage() {
    return mMessage;
  }

  public void setMessage(String message) {
    mMessage = message;
  }

  public List<String> getObject() {
    return mObject;
  }

  public void setObject(List<String> object) {
    mObject = object;
  }

  public String getStatus() {
    return mStatus;
  }

  public void setStatus(String status) {
    mStatus = status;
  }
}
