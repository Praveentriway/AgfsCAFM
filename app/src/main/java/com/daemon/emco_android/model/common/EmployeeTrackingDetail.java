package com.daemon.emco_android.model.common;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "employeeTrackingDetail")
public class EmployeeTrackingDetail {

    @SerializedName("employeeId")
    @ColumnInfo(name = "employeeId")
    String employeeId;

    @SerializedName("lat")
    @ColumnInfo(name = "lat")
    String lat;

    @SerializedName("lng")
    @ColumnInfo(name = "lng")
    String lng;

    @SerializedName("deviceID")
    @ColumnInfo(name = "deviceID")
    String deviceID;

    @SerializedName("deviceName")
    @ColumnInfo(name = "deviceName")
    String deviceName;

    @SerializedName("trans_date")
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "trans_date")
    String trans_date;

    @SerializedName("compCode")
    @ColumnInfo(name = "compCode")
    String compCode;

    @SerializedName("transType")
    @ColumnInfo(name = "transType")
    String transType;


    @SerializedName("refNo")
    @ColumnInfo(name = "refNo")
    String refNo;

    @SerializedName("actionType")
    @ColumnInfo(name = "actionType")
    String actionType;

    @SerializedName("createdBy")
    @ColumnInfo(name = "createdBy")
    String createdBy;

    @SerializedName("generalRefno")
    @ColumnInfo(name = "generalRefno")
    String generalRefno;

    public String getGeneralRefno() {
        return generalRefno;
    }

    public void setGeneralRefno(String generalRefno) {
        this.generalRefno = generalRefno;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getTrans_date() {
        return trans_date;
    }

    public void setTrans_date(String trans_date) {
        this.trans_date = trans_date;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
