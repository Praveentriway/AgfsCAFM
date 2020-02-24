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
