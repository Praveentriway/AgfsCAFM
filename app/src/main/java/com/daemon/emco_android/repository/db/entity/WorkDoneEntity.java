package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "workDoneEntity")
public class WorkDoneEntity {
  @ColumnInfo(name = "complaintCode")
  @SerializedName("complaintCode")
  private String mComplaintCode;

  @ColumnInfo(name = "defectCode")
  @SerializedName("defectCode")
  private String mDefectCode;

  @ColumnInfo(name = "workCategory")
  @SerializedName("workCategory")
  private String mWorkCategory;

  @ColumnInfo(name = "workDoneCode")
  @SerializedName("workDoneCode")
  private String mWorkDoneCode;

  @ColumnInfo(name = "workDoneDescription")
  @SerializedName("workDoneDescription")
  @PrimaryKey
  @NonNull
  private String mWorkDoneDescription;

  public String getComplaintCode() {
    return mComplaintCode;
  }

  public void setComplaintCode(String complaintCode) {
    mComplaintCode = complaintCode;
  }

  public String getDefectCode() {
    return mDefectCode;
  }

  public void setDefectCode(String defectCode) {
    mDefectCode = defectCode;
  }

  public String getWorkCategory() {
    return mWorkCategory;
  }

  public void setWorkCategory(String workCategory) {
    mWorkCategory = workCategory;
  }

  public String getWorkDoneCode() {
    return mWorkDoneCode;
  }

  public void setWorkDoneCode(String workDoneCode) {
    mWorkDoneCode = workDoneCode;
  }

  public String getWorkDoneDescription() {
    return mWorkDoneDescription;
  }

  public void setWorkDoneDescription(String workDoneDescription) {
    mWorkDoneDescription = workDoneDescription;
  }
}
