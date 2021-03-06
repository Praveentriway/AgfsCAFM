package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "MaterialMaster")
public class MaterialMasterEntity {
  @SerializedName("opco")
  @ColumnInfo(name = "opco")
  private String opco;

  @SerializedName("materialCode")
  @ColumnInfo(name = "materialCode")
  @NonNull
  @PrimaryKey
  private String materialCode = "0";

  @SerializedName("materialName")
  @ColumnInfo(name = "materialName")
  private String materialName;

  @SerializedName("units")
  @ColumnInfo(name = "units")
  private String units;

  @SerializedName("uom")
  @ColumnInfo(name = "uom")
  private String uom;

  @SerializedName("materialSize")
  @ColumnInfo(name = "materialSize")
  private String materialSize;

  @SerializedName("materialMake")
  @ColumnInfo(name = "materialMake")
  private String materialMake;

  @SerializedName("status")
  @ColumnInfo(name = "status")
  private String status;

  @SerializedName("itemQuantity")
  @ColumnInfo(name = "itemQuantity")
  private String itemQuantity;

  @SerializedName("remarks")
  @ColumnInfo(name = "remarks")
  private String remarks;

  public String getItemQuantity() {
    return itemQuantity;
  }

  public void setItemQuantity(String itemQuantity) {
    this.itemQuantity = itemQuantity;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getOpco() {
    return opco;
  }

  public void setOpco(String opco) {
    this.opco = opco;
  }

  public String getMaterialCode() {
    return materialCode;
  }

  public void setMaterialCode(String materialCode) {
    this.materialCode = materialCode;
  }

  public String getMaterialName() {
    return materialName;
  }

  public void setMaterialName(String materialName) {
    this.materialName = materialName;
  }

  public String getUnits() {
    return units;
  }

  public void setUnits(String units) {
    this.units = units;
  }

  public String getUom() {
    return uom;
  }

  public void setUom(String uom) {
    this.uom = uom;
  }

  public String getMaterialSize() {
    return materialSize;
  }

  public void setMaterialSize(String materialSize) {
    this.materialSize = materialSize;
  }

  public String getMaterialMake() {
    return materialMake;
  }

  public void setMaterialMake(String materialMake) {
    this.materialMake = materialMake;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
