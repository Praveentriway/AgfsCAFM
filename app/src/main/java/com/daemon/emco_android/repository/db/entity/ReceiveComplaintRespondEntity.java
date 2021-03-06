package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ReceiveComplaintRespondEntity")
public class ReceiveComplaintRespondEntity {

  @SerializedName("complaintNumber")
  @NonNull
  @PrimaryKey
  @ColumnInfo
  private String complaintNumber;

  @SerializedName("opco")
  @ColumnInfo
  private String opco;

  @SerializedName("complaintSite")
  @ColumnInfo
  private String complaintSite;

  @SerializedName("responseDetails")
  @ColumnInfo
  private String responseDetails;

  @SerializedName("complaintStatus")
  @ColumnInfo
  private String complaintStatus;

  @SerializedName("responseDate")
  @ColumnInfo
  private String responseDate;

  @SerializedName("floor")
  @ColumnInfo
  private String floor;

  @SerializedName("flat")
  @ColumnInfo
  private String flat;

  @SerializedName("assetBarCode")
  @ColumnInfo
  private String assetBarCode;

  @SerializedName("assetCode")
  @ColumnInfo
  private String assetCode;

  @SerializedName("modifiedBy")
  @ColumnInfo
  private String modifiedBy;

  @SerializedName("locationCode")
  @ColumnInfo
  private String locationCode;

  public String getAssetCode() {
    return assetCode;
  }

  public void setAssetCode(String assetCode) {
    this.assetCode = assetCode;
  }

  public String getLocationCode() {
    return locationCode;
  }

  public void setLocationCode(String locationCode) {
    this.locationCode = locationCode;
  }

  /** @return The complaintNumber */
  public String getComplaintNumber() {
    return complaintNumber;
  }

  /** @param complaintNumber The complaintNumber */
  public void setComplaintNumber(String complaintNumber) {
    this.complaintNumber = complaintNumber;
  }

  /** @return The opco */
  public String getOpco() {
    return opco;
  }

  /** @param opco The opco */
  public void setOpco(String opco) {
    this.opco = opco;
  }

  /** @return The complaintSite */
  public String getComplaintSite() {
    return complaintSite;
  }

  /** @param complaintSite The complaintSite */
  public void setComplaintSite(String complaintSite) {
    this.complaintSite = complaintSite;
  }

  /** @return The responseDetails */
  public String getResponseDetails() {
    return responseDetails;
  }

  /** @param responseDetails The responseDetails */
  public void setResponseDetails(String responseDetails) {
    this.responseDetails = responseDetails;
  }

  /** @return The complaintStatus */
  public String getComplaintStatus() {
    return complaintStatus;
  }

  /** @param complaintStatus The complaintStatus */
  public void setComplaintStatus(String complaintStatus) {
    this.complaintStatus = complaintStatus;
  }

  /** @return The responseDate */
  public String getResponseDate() {
    return responseDate;
  }

  /** @param responseDate The responseDate */
  public void setResponseDate(String responseDate) {
    this.responseDate = responseDate;
  }

  /** @return The floor */
  public String getFloor() {
    return floor;
  }

  /** @param floor The floor */
  public void setFloor(String floor) {
    this.floor = floor;
  }

  /** @return The flat */
  public String getFlat() {
    return flat;
  }

  /** @param flat The flat */
  public void setFlat(String flat) {
    this.flat = flat;
  }

  /** @return The assetBarCode */
  public String getAssetBarCode() {
    return assetBarCode;
  }

  /** @param assetBarCode The assetBarCode */
  public void setAssetBarCode(String assetBarCode) {
    this.assetBarCode = assetBarCode;
  }

  /** @return The modifiedBy */
  public String getModifiedBy() {
    return modifiedBy;
  }

  /** @param modifiedBy The modifiedBy */
  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }
}
