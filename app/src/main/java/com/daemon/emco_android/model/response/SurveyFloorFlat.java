package com.daemon.emco_android.model.response;

public class SurveyFloorFlat {

    String opco;
    String jobNo;
    String buildingCode;
    String floorCode;
    String floorText;
    String flatCode;
    String flatText;
    String floorFlat;

    public SurveyFloorFlat(String opco, String jobNo, String buildingCode, String floorCode, String floorText, String flatCode, String flatText, String floorFlat) {
        this.opco = opco;
        this.jobNo = jobNo;
        this.buildingCode = buildingCode;
        this.floorCode = floorCode;
        this.floorText = floorText;
        this.flatCode = flatCode;
        this.flatText = flatText;
        this.floorFlat = floorFlat;
    }


    public String getOpco() {
        return opco;
    }

    public void setOpco(String opco) {
        this.opco = opco;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getFloorCode() {
        return floorCode;
    }

    public void setFloorCode(String floorCode) {
        this.floorCode = floorCode;
    }

    public String getFloorText() {
        return floorText;
    }

    public void setFloorText(String floorText) {
        this.floorText = floorText;
    }

    public String getFlatCode() {
        return flatCode;
    }

    public void setFlatCode(String flatCode) {
        this.flatCode = flatCode;
    }

    public String getFlatText() {
        return flatText;
    }

    public void setFlatText(String flatText) {
        this.flatText = flatText;
    }

    public String getFloorFlat() {
        return floorFlat;
    }

    public void setFloorFlat(String floorFlat) {
        this.floorFlat = floorFlat;
    }
}
