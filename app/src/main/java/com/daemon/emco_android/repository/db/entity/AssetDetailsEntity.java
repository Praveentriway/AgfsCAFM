package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "AssetDetailsEntity")
public class AssetDetailsEntity implements Parcelable {

  public static final Parcelable.Creator<AssetDetailsEntity> CREATOR =
      new Creator<AssetDetailsEntity>() {

        @SuppressWarnings({"unchecked"})
        public AssetDetailsEntity createFromParcel(Parcel in) {
          AssetDetailsEntity instance = new AssetDetailsEntity();
          instance.opco = ((String) in.readValue((String.class.getClassLoader())));
          instance.assetCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.jobNo = ((String) in.readValue((String.class.getClassLoader())));
          instance.assetType = ((String) in.readValue((String.class.getClassLoader())));
          instance.regionCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.regionName = ((String) in.readValue((String.class.getClassLoader())));
          instance.zoneCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.zoneName = ((String) in.readValue((String.class.getClassLoader())));
          instance.buildingCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.buildingNAE = ((String) in.readValue((String.class.getClassLoader())));
          instance.floor = ((String) in.readValue((String.class.getClassLoader())));
          instance.floorName = ((String) in.readValue((String.class.getClassLoader())));
          instance.flat = ((String) in.readValue((String.class.getClassLoader())));
          instance.flatName = ((String) in.readValue((String.class.getClassLoader())));
          instance.room = ((String) in.readValue((String.class.getClassLoader())));
          instance.roomName = ((String) in.readValue((String.class.getClassLoader())));
          instance.assetNature = ((String) in.readValue((String.class.getClassLoader())));
          instance.natureDescription = ((String) in.readValue((String.class.getClassLoader())));
          instance.equipmentCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.equipmentName = ((String) in.readValue((String.class.getClassLoader())));
          instance.assetBarCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.assetDescription = ((String) in.readValue((String.class.getClassLoader())));
          instance.shortDescription = ((String) in.readValue((String.class.getClassLoader())));
          instance.assetMake = ((String) in.readValue((String.class.getClassLoader())));
          instance.assetModel = ((String) in.readValue((String.class.getClassLoader())));
          instance.assetSize = ((String) in.readValue((String.class.getClassLoader())));
          return instance;
        }

        public AssetDetailsEntity[] newArray(int size) {
          return (new AssetDetailsEntity[size]);
        }
      };

  @SerializedName("opco")
  @ColumnInfo(name = "opco")
  private String opco;

  @SerializedName("assetCode")
  @ColumnInfo(name = "assetCode")
  private String assetCode;

  @SerializedName("jobNo")
  @ColumnInfo(name = "jobNo")
  private String jobNo;

  @SerializedName("assetType")
  @ColumnInfo(name = "assetType")
  private String assetType;

  @ColumnInfo(name = "regionCode")
  @SerializedName("regionCode")
  private String regionCode;

  @SerializedName("regionName")
  @ColumnInfo(name = "regionName")
  private String regionName;

  @SerializedName("zoneCode")
  @ColumnInfo(name = "zoneCode")
  private String zoneCode;

  @SerializedName("zoneName")
  @ColumnInfo(name = "zoneName")
  private String zoneName;

  @SerializedName("buildingCode")
  @ColumnInfo(name = "buildingCode")
  private String buildingCode;

  @SerializedName("buildingNAE")
  @ColumnInfo(name = "buildingNAE")
  private String buildingNAE;

  @SerializedName("floor")
  @ColumnInfo(name = "floor")
  private String floor;

  @SerializedName("floorName")
  @ColumnInfo(name = "floorName")
  private String floorName;

  @SerializedName("flat")
  @ColumnInfo(name = "flat")
  private String flat;

  @SerializedName("flatName")
  @ColumnInfo(name = "flatName")
  private String flatName;

  @SerializedName("room")
  @ColumnInfo(name = "room")
  private String room;

  @SerializedName("roomName")
  @ColumnInfo(name = "roomName")
  private String roomName;

  @SerializedName("assetNature")
  @ColumnInfo(name = "assetNature")
  private String assetNature;

  @SerializedName("natureDescription")
  @ColumnInfo(name = "natureDescription")
  private String natureDescription;

  @SerializedName("equipmentCode")
  @ColumnInfo(name = "equipmentCode")
  private String equipmentCode;

  @SerializedName("equipmentName")
  @ColumnInfo(name = "equipmentName")
  private String equipmentName;

  @SerializedName("assetBarCode")
  @ColumnInfo(name = "assetBarCode")
  @NonNull
  @PrimaryKey
  private String assetBarCode;

  @SerializedName("assetDescription")
  @ColumnInfo(name = "assetDescription")
  private String assetDescription;

  @SerializedName("shortDescription")
  @ColumnInfo(name = "shortDescription")
  private String shortDescription;

  @SerializedName("assetMake")
  @ColumnInfo(name = "assetMake")
  private String assetMake;

  @SerializedName("assetModel")
  @ColumnInfo(name = "assetModel")
  private String assetModel;

  @SerializedName("assetSize")
  @ColumnInfo(name = "assetSize")
  private String assetSize;

  private String clientBarcode;


  public AssetDetailsEntity() {}

  /** @return The opco */
  public String getOpco() {
    return opco;
  }

  /** @param opco The opco */
  public void setOpco(String opco) {
    this.opco = opco;
  }

  /** @return The assetCode */
  public String getAssetCode() {
    return assetCode;
  }

  /** @param assetCode The assetCode */
  public void setAssetCode(String assetCode) {
    this.assetCode = assetCode;
  }

  /** @return The jobNo */
  public String getJobNo() {
    return jobNo;
  }

  /** @param jobNo The jobNo */
  public void setJobNo(String jobNo) {
    this.jobNo = jobNo;
  }

  /** @return The assetType */
  public String getAssetType() {
    return assetType;
  }

  /** @param assetType The assetType */
  public void setAssetType(String assetType) {
    this.assetType = assetType;
  }

  /** @return The regionCode */
  public String getRegionCode() {
    return regionCode;
  }

  public String getClientBarcode() {
    return clientBarcode;
  }

  public void setClientBarcode(String clientBarcode) {
    this.clientBarcode = clientBarcode;
  }

  /** @param regionCode The regionCode */
  public void setRegionCode(String regionCode) {
    this.regionCode = regionCode;
  }

  /** @return The regionName */
  public String getRegionName() {
    return regionName;
  }

  /** @param regionName The regionName */
  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  /** @return The zoneCode */
  public String getZoneCode() {
    return zoneCode;
  }

  /** @param zoneCode The zoneCode */
  public void setZoneCode(String zoneCode) {
    this.zoneCode = zoneCode;
  }

  /** @return The zoneName */
  public String getZoneName() {
    return zoneName;
  }

  /** @param zoneName The zoneName */
  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
  }

  /** @return The buildingCode */
  public String getBuildingCode() {
    return buildingCode;
  }

  /** @param buildingCode The buildingCode */
  public void setBuildingCode(String buildingCode) {
    this.buildingCode = buildingCode;
  }

  /** @return The buildingNAE */
  public String getBuildingNAE() {
    return buildingNAE;
  }

  /** @param buildingNAE The buildingNAE */
  public void setBuildingNAE(String buildingNAE) {
    this.buildingNAE = buildingNAE;
  }

  /** @return The floor */
  public String getFloor() {
    return floor;
  }

  /** @param floor The floor */
  public void setFloor(String floor) {
    this.floor = floor;
  }

  /** @return The floorName */
  public String getFloorName() {
    return floorName;
  }

  /** @param floorName The floorName */
  public void setFloorName(String floorName) {
    this.floorName = floorName;
  }

  /** @return The flat */
  public String getFlat() {
    return flat;
  }

  /** @param flat The flat */
  public void setFlat(String flat) {
    this.flat = flat;
  }

  /** @return The flatName */
  public String getFlatName() {
    return flatName;
  }

  /** @param flatName The flatName */
  public void setFlatName(String flatName) {
    this.flatName = flatName;
  }

  /** @return The room */
  public String getRoom() {
    return room;
  }

  /** @param room The room */
  public void setRoom(String room) {
    this.room = room;
  }

  /** @return The roomName */
  public String getRoomName() {
    return roomName;
  }

  /** @param roomName The roomName */
  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  /** @return The assetNature */
  public String getAssetNature() {
    return assetNature;
  }

  /** @param assetNature The assetNature */
  public void setAssetNature(String assetNature) {
    this.assetNature = assetNature;
  }

  /** @return The natureDescription */
  public String getNatureDescription() {
    return natureDescription;
  }

  /** @param natureDescription The natureDescription */
  public void setNatureDescription(String natureDescription) {
    this.natureDescription = natureDescription;
  }

  /** @return The equipmentCode */
  public String getEquipmentCode() {
    return equipmentCode;
  }

  /** @param equipmentCode The equipmentCode */
  public void setEquipmentCode(String equipmentCode) {
    this.equipmentCode = equipmentCode;
  }

  /** @return The equipmentName */
  public String getEquipmentName() {
    return equipmentName;
  }

  /** @param equipmentName The equipmentName */
  public void setEquipmentName(String equipmentName) {
    this.equipmentName = equipmentName;
  }

  /** @return The assetBarCode */
  public String getAssetBarCode() {
    return assetBarCode;
  }

  /** @param assetBarCode The assetBarCode */
  public void setAssetBarCode(String assetBarCode) {
    this.assetBarCode = assetBarCode;
  }

  /** @return The assetDescription */
  public String getAssetDescription() {
    return assetDescription;
  }

  /** @param assetDescription The assetDescription */
  public void setAssetDescription(String assetDescription) {
    this.assetDescription = assetDescription;
  }

  /** @return The shortDescription */
  public String getShortDescription() {
    return shortDescription;
  }

  /** @param shortDescription The shortDescription */
  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getAssetMake() {
    return assetMake;
  }

  public void setAssetMake(String assetMake) {
    this.assetMake = assetMake;
  }

  public String getAssetModel() {
    return assetModel;
  }

  public void setAssetModel(String assetModel) {
    this.assetModel = assetModel;
  }

  public String getAssetSize() {
    return assetSize;
  }

  public void setAssetSize(String assetSize) {
    this.assetSize = assetSize;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(opco);
    dest.writeValue(assetCode);
    dest.writeValue(jobNo);
    dest.writeValue(assetType);
    dest.writeValue(regionCode);
    dest.writeValue(regionName);
    dest.writeValue(zoneCode);
    dest.writeValue(zoneName);
    dest.writeValue(buildingCode);
    dest.writeValue(buildingNAE);
    dest.writeValue(floor);
    dest.writeValue(floorName);
    dest.writeValue(flat);
    dest.writeValue(flatName);
    dest.writeValue(room);
    dest.writeValue(roomName);
    dest.writeValue(assetNature);
    dest.writeValue(natureDescription);
    dest.writeValue(equipmentCode);
    dest.writeValue(equipmentName);
    dest.writeValue(assetBarCode);
    dest.writeValue(assetDescription);
    dest.writeValue(shortDescription);
    dest.writeValue(assetMake);
    dest.writeValue(assetModel);
    dest.writeValue(assetSize);
    dest.writeValue(clientBarcode);
  }

  public int describeContents() {
    return 0;
  }
}
