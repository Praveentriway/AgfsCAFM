package com.daemon.emco_android.model.response;

/** Created by vikram on 17/7/17. */
public class RCRespondResponse {
  private String status;
  private String message;
  private RCRespond object;

  public RCRespond getObject() {
    return object;
  }

  public void setObject(RCRespond object) {
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
