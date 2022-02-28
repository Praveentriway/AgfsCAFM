package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(
    tableName = "workDefectEntity",
    primaryKeys = {"defectCode", "defectDescription"})
public class WorkDefectEntity {
  @ColumnInfo(name = "complaintCode")
  @SerializedName("complaintCode")
  private String mComplaintCode;

  @NonNull
  @ColumnInfo(name = "defectCode")
  @SerializedName("defectCode")
  private String mDefectCode;

  @ColumnInfo(name = "defectDescription")
  @SerializedName("defectDescription")
  @NonNull
  private String mDefectDescription;

  @ColumnInfo(name = "workCategory")
  @SerializedName("workCategory")
  private String mWorkCategory;

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

  public String getDefectDescription() {
    return mDefectDescription;
  }

  public void setDefectDescription(String defectDescription) {
    mDefectDescription = defectDescription;
  }

  public String getWorkCategory() {
    return mWorkCategory;
  }

  public void setWorkCategory(String workCategory) {
    mWorkCategory = workCategory;
  }
}
