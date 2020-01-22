package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "WorkTypeEntity")
public class WorkTypeEntity {

  @ColumnInfo(name = "code")
  @SerializedName("code")
  @PrimaryKey
  @NonNull
  private String code;

  @ColumnInfo(name = "description")
  @SerializedName("description")
  private String description;

  @ColumnInfo(name = "srl")
  @SerializedName("srl")
  private String srl;

  /** @return The code */
  public String getCode() {
    return code;
  }

  /** @param code The code */
  public void setCode(String code) {
    this.code = code;
  }

  /** @return The description */
  public String getDescription() {
    return description;
  }

  /** @param description The description */
  public void setDescription(String description) {
    this.description = description;
  }

  /** @return The srl */
  public String getSrl() {
    return srl;
  }

  /** @param srl The srl */
  public void setSrl(String srl) {
    this.srl = srl;
  }
}
