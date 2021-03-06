package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.SaveMaterialEntity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetMaterialResponse {

  @SerializedName("status")
  private String status;

  @SerializedName("message")
  private String message;

  @SerializedName("object")
  private List<SaveMaterialEntity> object = new ArrayList<>();

  @SerializedName("totalNumberOfRows")
  private String totalNumberOfRows;

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
  public List<SaveMaterialEntity> getObject() {
    return object;
  }

  /** @param object The object */
  public void setObject(List<SaveMaterialEntity> object) {
    this.object = object;
  }

  public String getTotalNumberOfRows() {
    return totalNumberOfRows;
  }

  public void setTotalNumberOfRows(String totalNumberOfRows) {
    this.totalNumberOfRows = totalNumberOfRows;
  }
}
