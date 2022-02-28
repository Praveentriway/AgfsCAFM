package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/** Created by vikram on 23/7/17. */
@Entity(tableName = "zoneEntity")
public class ZoneEntity {
  @SerializedName("contractNo")
  @ColumnInfo(name = "contractNo")
  private String contractNo;

  @SerializedName("opco")
  @ColumnInfo(name = "opco")
  private String opco;

  @PrimaryKey
  @NonNull
  @SerializedName("zoneCode")
  @ColumnInfo(name = "zoneCode")
  private String zoneCode;

  @SerializedName("zoneName")
  @ColumnInfo(name = "zoneName")
  private String zoneName;

  @SerializedName("serviceGroup")

  private String serviceGroup;

  @SerializedName("siteCode")

  private String siteCode;

  public String getSiteCode() {
    return siteCode;
  }

  public void setSiteCode(String siteCode) {
    this.siteCode = siteCode;
  }



  public String getServiceGroup() {
    return serviceGroup;
  }

  public void setServiceGroup(String serviceGroup) {
    this.serviceGroup = serviceGroup;
  }



  public String getContractNo() {
    return contractNo;
  }

  public void setContractNo(String contractNo) {
    this.contractNo = contractNo;
  }

  public String getOpco() {
    return opco;
  }

  public void setOpco(String opco) {
    this.opco = opco;
  }

  public String getZoneCode() {
    return zoneCode;
  }

  public void setZoneCode(String zoneCode) {
    this.zoneCode = zoneCode;
  }

  public String getZoneName() {
    return zoneName;
  }

  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
  }
}
