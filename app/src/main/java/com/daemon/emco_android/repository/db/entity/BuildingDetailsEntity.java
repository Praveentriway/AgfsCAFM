
package com.daemon.emco_android.repository.db.entity;

import javax.annotation.Generated;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class BuildingDetailsEntity implements Parcelable
{

    @SerializedName("opco")
    @Expose
    private String opco;
    @SerializedName("contractNo")
    @Expose
    private String contractNo;
    @SerializedName("buildingCode")
    @Expose
    private String buildingCode;
    @SerializedName("buildingName")
    @Expose
    private String buildingName;
    @SerializedName("zoneCode")
    @Expose
    private String zoneCode;
    @SerializedName("zoneName")
    @Expose
    private String zoneName;
    @SerializedName("srl")
    @Expose
    private String srl;
    public final static Parcelable.Creator<BuildingDetailsEntity> CREATOR = new Creator<BuildingDetailsEntity>() {


        @SuppressWarnings({
            "unchecked"
        })
        public BuildingDetailsEntity createFromParcel(Parcel in) {
            BuildingDetailsEntity instance = new BuildingDetailsEntity();
            instance.opco = ((String) in.readValue((String.class.getClassLoader())));
            instance.contractNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.buildingCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.buildingName = ((String) in.readValue((String.class.getClassLoader())));
            instance.zoneCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.zoneName = ((String) in.readValue((String.class.getClassLoader())));
            instance.srl = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public BuildingDetailsEntity[] newArray(int size) {
            return (new BuildingDetailsEntity[size]);
        }

    }
    ;

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
     *     The contractNo
     */
    public String getContractNo() {
        return contractNo;
    }

    /**
     * 
     * @param contractNo
     *     The contractNo
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
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
     *     The buildingName
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * 
     * @param buildingName
     *     The buildingName
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
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
     *     The zoneName
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * 
     * @param zoneName
     *     The zoneName
     */
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    /**
     * 
     * @return
     *     The srl
     */
    public String getSrl() {
        return srl;
    }

    /**
     * 
     * @param srl
     *     The srl
     */
    public void setSrl(String srl) {
        this.srl = srl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(opco);
        dest.writeValue(contractNo);
        dest.writeValue(buildingCode);
        dest.writeValue(buildingName);
        dest.writeValue(zoneCode);
        dest.writeValue(zoneName);
        dest.writeValue(srl);
    }

    public int describeContents() {
        return  0;
    }

}
