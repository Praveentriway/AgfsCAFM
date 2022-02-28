package com.daemon.emco_android.model.common;

import java.io.Serializable;

public class UtilityAssetTransaction implements Serializable {

    String opco;
    String jobno;
    String refno;
    String tagNumber;
    String uom;
    String multifactor;
    String meterActual;
    String meterEntered;
    String gpsLatitude;
    String gpsLongitude;
    String createdBy;

    public String getOpco() {
        return opco;
    }

    public void setOpco(String opco) {
        this.opco = opco;
    }

    public String getJobno() {
        return jobno;
    }

    public void setJobno(String jobno) {
        this.jobno = jobno;
    }

    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getMultifactor() {
        return multifactor;
    }

    public void setMultifactor(String multifactor) {
        this.multifactor = multifactor;
    }

    public String getMeterActual() {
        return meterActual;
    }

    public void setMeterActual(String meterActual) {
        this.meterActual = meterActual;
    }

    public String getMeterEntered() {
        return meterEntered;
    }

    public void setMeterEntered(String meterEntered) {
        this.meterEntered = meterEntered;
    }

    public String getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(String gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public String getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(String gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
