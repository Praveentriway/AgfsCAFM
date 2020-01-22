
package com.daemon.emco_android.model.common;

import javax.annotation.Generated;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class PPMDetails implements Parcelable
{

    @SerializedName("compCode")
    @Expose
    private String compCode;
    @SerializedName("contractNo")
    @Expose
    private String contractNo;
    @SerializedName("workOrderNo")
    @Expose
    private String workOrderNo;
    @SerializedName("natureCode")
    @Expose
    private String natureCode;
    @SerializedName("natureDesc")
    @Expose
    private String natureDesc;
    @SerializedName("equipmentCode")
    @Expose
    private String equipmentCode;
    @SerializedName("equipmentName")
    @Expose
    private String equipmentName;
    @SerializedName("zoneCode")
    @Expose
    private String zoneCode;
    @SerializedName("buildingCode")
    @Expose
    private String buildingCode;
    @SerializedName("zoneBuilding")
    @Expose
    private String zoneBuilding;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("totalEquipments")
    @Expose
    private String totalEquipments;
    @SerializedName("completed")
    @Expose
    private String completed;
    @SerializedName("pending")
    @Expose
    private String pending;
    public final static Parcelable.Creator<PPMDetails> CREATOR = new Creator<PPMDetails>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PPMDetails createFromParcel(Parcel in) {
            PPMDetails instance = new PPMDetails();
            instance.compCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.contractNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.workOrderNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.natureCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.natureDesc = ((String) in.readValue((String.class.getClassLoader())));
            instance.equipmentCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.equipmentName = ((String) in.readValue((String.class.getClassLoader())));
            instance.zoneCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.buildingCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.zoneBuilding = ((String) in.readValue((String.class.getClassLoader())));
            instance.startDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.endDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.totalEquipments = ((String) in.readValue((String.class.getClassLoader())));
            instance.completed = ((String) in.readValue((String.class.getClassLoader())));
            instance.pending = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public PPMDetails[] newArray(int size) {
            return (new PPMDetails[size]);
        }

    }
    ;
    private boolean checked;

    /**
     * 
     * @return
     *     The compCode
     */
    public String getCompCode() {
        return compCode;
    }

    /**
     * 
     * @param compCode
     *     The compCode
     */
    public void setCompCode(String compCode) {
        this.compCode = compCode;
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
     *     The workOrderNo
     */
    public String getWorkOrderNo() {
        return workOrderNo;
    }

    /**
     * 
     * @param workOrderNo
     *     The workOrderNo
     */
    public void setWorkOrderNo(String workOrderNo) {
        this.workOrderNo = workOrderNo;
    }

    /**
     * 
     * @return
     *     The natureCode
     */
    public String getNatureCode() {
        return natureCode;
    }

    /**
     * 
     * @param natureCode
     *     The natureCode
     */
    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    /**
     * 
     * @return
     *     The natureDesc
     */
    public String getNatureDesc() {
        return natureDesc;
    }

    /**
     * 
     * @param natureDesc
     *     The natureDesc
     */
    public void setNatureDesc(String natureDesc) {
        this.natureDesc = natureDesc;
    }

    /**
     * 
     * @return
     *     The equipmentCode
     */
    public String getEquipmentCode() {
        return equipmentCode;
    }

    /**
     * 
     * @param equipmentCode
     *     The equipmentCode
     */
    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    /**
     * 
     * @return
     *     The equipmentName
     */
    public String getEquipmentName() {
        return equipmentName;
    }

    /**
     * 
     * @param equipmentName
     *     The equipmentName
     */
    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
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
     *     The zoneBuilding
     */
    public String getZoneBuilding() {
        return zoneBuilding;
    }

    /**
     * 
     * @param zoneBuilding
     *     The zoneBuilding
     */
    public void setZoneBuilding(String zoneBuilding) {
        this.zoneBuilding = zoneBuilding;
    }

    /**
     * 
     * @return
     *     The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate
     *     The startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 
     * @return
     *     The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate
     *     The endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 
     * @return
     *     The totalEquipments
     */
    public String getTotalEquipments() {
        return totalEquipments;
    }

    /**
     * 
     * @param totalEquipments
     *     The totalEquipments
     */
    public void setTotalEquipments(String totalEquipments) {
        this.totalEquipments = totalEquipments;
    }

    /**
     * 
     * @return
     *     The completed
     */
    public String getCompleted() {
        return completed;
    }

    /**
     * 
     * @param completed
     *     The completed
     */
    public void setCompleted(String completed) {
        this.completed = completed;
    }

    /**
     * 
     * @return
     *     The pending
     */
    public String getPending() {
        return pending;
    }

    /**
     * 
     * @param pending
     *     The pending
     */
    public void setPending(String pending) {
        this.pending = pending;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(compCode);
        dest.writeValue(contractNo);
        dest.writeValue(workOrderNo);
        dest.writeValue(natureCode);
        dest.writeValue(natureDesc);
        dest.writeValue(equipmentCode);
        dest.writeValue(equipmentName);
        dest.writeValue(zoneCode);
        dest.writeValue(buildingCode);
        dest.writeValue(zoneBuilding);
        dest.writeValue(startDate);
        dest.writeValue(endDate);
        dest.writeValue(totalEquipments);
        dest.writeValue(completed);
        dest.writeValue(pending);
    }

    public int describeContents() {
        return  0;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
