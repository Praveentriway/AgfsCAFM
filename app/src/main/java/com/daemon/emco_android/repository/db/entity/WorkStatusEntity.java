package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "workStatusEntity")
public class WorkStatusEntity {
  @ColumnInfo(name = "description")
  @SerializedName("description")
  private String mDescription;

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "pendingCode")
  @SerializedName("pendingCode")
  private String mPendingCode;

  public String getDescription() {
    return mDescription;
  }

  public void setDescription(String description) {
    mDescription = description;
  }

  public String getPendingCode() {
    return mPendingCode;
  }

  public void setPendingCode(String pendingCode) {
    mPendingCode = pendingCode;
  }
}
