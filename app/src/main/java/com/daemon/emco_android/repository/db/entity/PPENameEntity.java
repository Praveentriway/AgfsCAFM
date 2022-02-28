package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ppeNameEntity")
public class PPENameEntity {

  @ColumnInfo(name = "cType")
  @SerializedName("cType")
  private String mCType;

  @NonNull
  @PrimaryKey
  @SerializedName("code")
  @ColumnInfo(name = "code")
  private String mCode;

  @SerializedName("name")
  @ColumnInfo(name = "name")
  private String mName;

  private String ppeUsed;

  public String getPpeUsed() {
    return ppeUsed;
  }

  public void setPpeUsed(String ppeUsed) {
    this.ppeUsed = ppeUsed;
  }

  public String getCType() {
    return mCType;
  }

  public void setCType(String cType) {
    mCType = cType;
  }

  public String getCode() {
    return mCode;
  }

  public void setCode(String code) {
    mCode = code;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name;
  }
}
