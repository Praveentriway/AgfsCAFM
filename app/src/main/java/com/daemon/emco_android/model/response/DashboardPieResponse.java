package com.daemon.emco_android.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DashboardPieResponse {

  @SerializedName("status")
  @Expose
  private String status;

  @SerializedName("message")
  @Expose
  private String message;

  @SerializedName("object")
  @Expose
  private ArrayList<DashboardPieData> object = new ArrayList<>();

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
  public ArrayList<DashboardPieData> getObject() {
    return object;
  }

  /** @param object The object */
  public void setObject(ArrayList<DashboardPieData> object) {
    this.object = object;
  }
}
