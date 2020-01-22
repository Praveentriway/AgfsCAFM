package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "PriorityEntity")
public class PriorityEntity {

  @ColumnInfo(name = "code")
  @SerializedName("code")
  @NonNull
  @PrimaryKey
  private String mCode;

  @ColumnInfo(name = "description")
  @SerializedName("description")
  private String mDescription;

  public String getCode() {
    return mCode;
  }

  public void setCode(String code) {
    mCode = code;
  }

  public String getDescription() {
    return mDescription;
  }

  public void setDescription(String description) {
    mDescription = description;
  }
}
