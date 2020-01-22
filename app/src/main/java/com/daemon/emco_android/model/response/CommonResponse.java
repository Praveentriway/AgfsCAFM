package com.daemon.emco_android.model.response;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.daemon.emco_android.repository.db.entity.ComplaintStatusEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/** Created by vikram on 17/7/17. */
@Generated("org.jsonschema2pojo")
public class CommonResponse {
  private String status;
  private String message;
  private String complaintNumber;


/*  private PPMDetails object;*/

  public ComplaintStatusEntity getObject() {
    return object;
  }

  public void setObject(ComplaintStatusEntity objectUpdate) {
    this.object = objectUpdate;
  }

  @SerializedName("object")
  private ComplaintStatusEntity object;

  private String totalNumberOfRows;

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

  public String getComplaintNumber() {
    return complaintNumber;
  }

  public void setComplaintNumber(String complaintNumber) {
    this.complaintNumber = complaintNumber;
  }

  public String getTotalNumberOfRows() {
    return totalNumberOfRows;
  }

  public void setTotalNumberOfRows(String totalNumberOfRows) {
    this.totalNumberOfRows = totalNumberOfRows;
  }
  public class Object {

    @SerializedName("newComplaint")
    @Expose
    @ColumnInfo(name = "newComplaint")
    @NonNull
    @PrimaryKey
    private String newComplaint;
    @SerializedName("oldComplaint")
    @ColumnInfo(name = "oldComplaint")
    @Expose
    private String oldComplaint;

    public String getNewComplaint() {
      return newComplaint;
    }

    public void setNewComplaint(String newComplaint) {
      this.newComplaint = newComplaint;
    }

    public String getOldComplaint() {
      return oldComplaint;
    }

    public void setOldComplaint(String oldComplaint) {
      this.oldComplaint = oldComplaint;
    }

  }

}


