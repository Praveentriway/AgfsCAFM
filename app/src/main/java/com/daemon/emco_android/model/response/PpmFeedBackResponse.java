package com.daemon.emco_android.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PpmFeedBackResponse {
  @SerializedName("status")
  @Expose
  private String status;

  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("object")
  @Expose
  private ObjectPPM object;

  public ObjectPPM getObject() {
    return object;
  }

  public void setObject(ObjectPPM object) {
    this.object = object;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
