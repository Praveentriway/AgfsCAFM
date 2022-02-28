package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


/**
 * Created by vikram on 23/7/17.
 */
@Entity(tableName = "siteAreaEntity")
public class SiteAreaEntity {
    @SerializedName("customerCode")
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "customerCode")
    private String customerCode;
    @SerializedName("customerName")
    @ColumnInfo(name = "customerName")
    private String customerName;
    @SerializedName("jobDescription")
    @ColumnInfo(name = "jobDescription")
    private String jobDescription;
    @SerializedName("jobNo")
    @ColumnInfo(name = "jobNo")
    private String jobNo;
    @SerializedName("opco")
    @ColumnInfo(name = "opco")
    private String opco;
    @SerializedName("siteCode")
    @ColumnInfo(name = "siteCode")
    private String siteCode;
    @SerializedName("siteDescription")
    @ColumnInfo(name = "siteDescription")
    private String siteDescription;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getOpco() {
        return opco;
    }

    public void setOpco(String opco) {
        this.opco = opco;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteDescription() {
        return siteDescription;
    }

    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription;
    }
}
