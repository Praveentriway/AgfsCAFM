package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/** Created by vikram on 23/7/17. */
@Entity(tableName = "categoryEntity")
public class CategoryEntity {
  @SerializedName("natureCode")
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "natureCode")
  private String natureCode;

  @SerializedName("opco")
  @ColumnInfo(name = "opco")
  private String opco;

  @SerializedName("jobNo")
  @ColumnInfo(name = "jobNo")
  private Long jobNo;

  @SerializedName("natureDescription")
  @ColumnInfo(name = "natureDescription")
  private String natureDescription;

  public String getNatureCode() {
    return natureCode;
  }

  public void setNatureCode(String natureCode) {
    this.natureCode = natureCode;
  }

  public String getOpco() {
    return opco;
  }

  public void setOpco(String opco) {
    this.opco = opco;
  }

  public Long getJobNo() {
    return jobNo;
  }

  public void setJobNo(Long jobNo) {
    this.jobNo = jobNo;
  }

  public String getNatureDescription() {
    return natureDescription;
  }

  public void setNatureDescription(String natureDescription) {
    this.natureDescription = natureDescription;
  }
}
