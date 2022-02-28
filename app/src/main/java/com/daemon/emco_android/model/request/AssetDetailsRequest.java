
package com.daemon.emco_android.model.request;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class AssetDetailsRequest {
    @SerializedName("companyCode")
    @Expose
    private String companyCode;
    @SerializedName("ppmNo")
    @Expose
    private String ppmNo;
    @SerializedName("equipCode")
    @Expose
    private String equipCode;
    @SerializedName("buildTag")
    @Expose
    private String buildTag;
    @SerializedName("opco")
    @Expose
    private String opco;
    @SerializedName("jobNo")
    @Expose
    private String jobNo;
    @SerializedName("zoneCode")
    @Expose
    private String zoneCode;
    @SerializedName("buildingCode")
    @Expose
    private String buildingCode;
    @SerializedName("assetBarCode")
    @Expose
    private String assetBarCode;
    @SerializedName("clientBarcode")
    @Expose
    private String clientBarcode;

    public AssetDetailsRequest(String opco, String jobNo, String zoneCode, String buildingCode, String assetBarCode) {
        this.opco = opco;
        this.jobNo = jobNo;
        this.zoneCode = zoneCode;
        this.buildingCode = buildingCode;
        this.assetBarCode = assetBarCode;
    }

    public AssetDetailsRequest(String opco, String jobNo, String zoneCode, String buildingCode,String equip,String ppmnoo,String clientBarcode,String assetBarCode) {
        this.companyCode = opco;
        this.jobNo = jobNo;
        this.zoneCode = zoneCode;
        this.buildTag = buildingCode;
        this.equipCode=equip;
        this.ppmNo=ppmnoo;
        this.clientBarcode=clientBarcode;
        this.assetBarCode=assetBarCode;
    }



    public String getClientBarcode() {
        return clientBarcode;
    }

    public void setClientBarcode(String clientBarcode) {
        this.clientBarcode = clientBarcode;
    }

    /**
     * 
     * @return
     *     The opco
     */
    public String getOpco() {
        return opco;
    }

    /**
     * 
     * @param opco
     *     The opco
     */
    public void setOpco(String opco) {
        this.opco = opco;
    }

    /**
     * 
     * @return
     *     The jobNo
     */
    public String getJobNo() {
        return jobNo;
    }

    /**
     * 
     * @param jobNo
     *     The jobNo
     */
    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    /**
     * 
     * @return
     *     The zoneCode
     */
    public String getZoneCode() {
        return zoneCode;
    }

    /**
     * 
     * @param zoneCode
     *     The zoneCode
     */
    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    /**
     * 
     * @return
     *     The buildingCode
     */
    public String getBuildingCode() {
        return buildingCode;
    }

    /**
     * 
     * @param buildingCode
     *     The buildingCode
     */
    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    /**
     * 
     * @return
     *     The assetBarCode
     */
    public String getAssetBarCode() {
        return assetBarCode;
    }

    /**
     * 
     * @param assetBarCode
     *     The assetBarCode
     */
    public void setAssetBarCode(String assetBarCode) {
        this.assetBarCode = assetBarCode;
    }

}
