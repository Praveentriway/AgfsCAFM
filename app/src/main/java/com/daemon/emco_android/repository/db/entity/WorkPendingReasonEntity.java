package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@Entity(tableName = "workPendingReasonEntity")
@SuppressWarnings("unused")
public class WorkPendingReasonEntity {
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "reasonCode")
  @SerializedName("reasonCode")
  private String mReasonCode;

  @ColumnInfo(name = "reasonDescription")
  @SerializedName("reasonDescription")
  private String mReasonDescription;

  public String getReasonCode() {
    return mReasonCode;
  }

  public void setReasonCode(String reasonCode) {
    mReasonCode = reasonCode;
  }

  public String getReasonDescription() {
    return mReasonDescription;
  }

  public void setReasonDescription(String reasonDescription) {
    mReasonDescription = reasonDescription;
  }
}
