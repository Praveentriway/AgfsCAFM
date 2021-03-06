package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.ReportTypesEntity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReportTypesResponse {

  @SerializedName("status")
  private String status;

  @SerializedName("message")
  private String message;

  @SerializedName("object")
  private List<ReportTypesEntity> object = new ArrayList<ReportTypesEntity>();

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
  public List<ReportTypesEntity> getObject() {
    return object;
  }

  /** @param object The object */
  public void setObject(List<ReportTypesEntity> object) {
    this.object = object;
  }
}
