package com.daemon.emco_android.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LCUserInputResponse {

  @SerializedName("status")
  private String status;

  @SerializedName("message")
  private String message;

  @SerializedName("object")
  private List<LCUserInput> object;

  /** @return The status */
  public String getStatus() {
    return status;
  }

  /** @param status The status */
  public void setStatus(String status) {
    this.status = status;
  }

  /** @return The message */
  public String getMessage() {
    return message;
  }

  /** @param message The message */
  public void setMessage(String message) {
    this.message = message;
  }

  /** @return The object */
  public List<LCUserInput> getObject() {
    return object;
  }

  /** @param object The object */
  public void setObject(List<LCUserInput> object) {
    this.object = object;
  }
}
