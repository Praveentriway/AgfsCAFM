package com.daemon.emco_android.model.common;

import com.google.gson.annotations.SerializedName;

public class ContractNoUsers {

    @SerializedName("opco")

    private String opco;

    @SerializedName("contractNo")

    private String contractNo;
    @SerializedName("siteCode")

    private String siteCode;
    @SerializedName("siteName")

    private String siteName;

    @SerializedName("buildingCode")

    private String buildingCode;


    @SerializedName("buildingName")

    private String buildingName;


    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }



    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }



    public String getOpco() {
        return opco;
    }

    public void setOpco(String opco) {
        this.opco = opco;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
}
