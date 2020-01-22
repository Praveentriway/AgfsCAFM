
package com.daemon.emco_android.model.common;

import javax.annotation.Generated;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class PpmScheduleDetails implements Parcelable
{

    @SerializedName("compCode")
    @Expose
    private String compCode;
    @SerializedName("workOrderNo")
    @Expose
    private String workOrderNo;
    @SerializedName("contractNo")
    @Expose
    private String contractNo;
    @SerializedName("ppmRefNo")
    @Expose
    private String ppmRefNo;
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
    @SerializedName("assetCode")
    @Expose
    private String assetCode;
    @SerializedName("assetBarCode")
    @Expose
    private String assetBarCode;
    @SerializedName("zoneCode")
    @Expose
    private String zoneCode;
    @SerializedName("building")
    @Expose
    private String building;
    @SerializedName("floorCode")
    @Expose
    private String floorCode;
    @SerializedName("flatCode")
    @Expose
    private String flatCode;
    @SerializedName("roomCode")
    @Expose
    private String roomCode;
    @SerializedName("zoneBuilding")
    @Expose
    private String zoneBuilding;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("checkListCode")
    @Expose
    private String checkListCode;
    @SerializedName("checkListName")
    @Expose
    private String checkListName;
    @SerializedName("ppmFrequency")
    @Expose
    private String ppmFrequency;
    @SerializedName("workHours")
    @Expose
    private String workHours;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("checkedBy")
    @Expose
    private String checkedBy;
    @SerializedName("attendedBy")
    @Expose
    private String attendedBy;
    @SerializedName("ppmStatus")
    @Expose
    private String ppmStatus;
    public final static Parcelable.Creator<PpmScheduleDetails> CREATOR = new Creator<PpmScheduleDetails>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PpmScheduleDetails createFromParcel(Parcel in) {
            PpmScheduleDetails instance = new PpmScheduleDetails();
            instance.compCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.workOrderNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.contractNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.ppmRefNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.natureCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.natureDesc = ((String) in.readValue((String.class.getClassLoader())));
            instance.equipmentCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.equipmentName = ((String) in.readValue((String.class.getClassLoader())));
            instance.assetCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.assetBarCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.zoneCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.building = ((String) in.readValue((String.class.getClassLoader())));
            instance.floorCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.flatCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.roomCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.zoneBuilding = ((String) in.readValue((String.class.getClassLoader())));
            instance.locationName = ((String) in.readValue((String.class.getClassLoader())));
            instance.checkListCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.checkListName = ((String) in.readValue((String.class.getClassLoader())));
            instance.ppmFrequency = ((String) in.readValue((String.class.getClassLoader())));
            instance.workHours = ((String) in.readValue((String.class.getClassLoader())));
            instance.startDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.endDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.checkedBy = ((String) in.readValue((String.class.getClassLoader())));
            instance.attendedBy = ((String) in.readValue((String.class.getClassLoader())));
            instance.ppmStatus = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public PpmScheduleDetails[] newArray(int size) {
            return (new PpmScheduleDetails[size]);
        }

    }
    ;

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
     *     The ppmRefNo
     */
    public String getPpmRefNo() {
        return ppmRefNo;
    }

    /**
     * 
     * @param ppmRefNo
     *     The ppmRefNo
     */
    public void setPpmRefNo(String ppmRefNo) {
        this.ppmRefNo = ppmRefNo;
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
     *     The assetCode
     */
    public String getAssetCode() {
        return assetCode;
    }

    /**
     * 
     * @param assetCode
     *     The assetCode
     */
    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
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
     *     The building
     */
    public String getBuilding() {
        return building;
    }

    /**
     * 
     * @param building
     *     The building
     */
    public void setBuilding(String building) {
        this.building = building;
    }

    /**
     * 
     * @return
     *     The floorCode
     */
    public String getFloorCode() {
        return floorCode;
    }

    /**
     * 
     * @param floorCode
     *     The floorCode
     */
    public void setFloorCode(String floorCode) {
        this.floorCode = floorCode;
    }

    /**
     * 
     * @return
     *     The flatCode
     */
    public String getFlatCode() {
        return flatCode;
    }

    /**
     * 
     * @param flatCode
     *     The flatCode
     */
    public void setFlatCode(String flatCode) {
        this.flatCode = flatCode;
    }

    /**
     * 
     * @return
     *     The roomCode
     */
    public String getRoomCode() {
        return roomCode;
    }

    /**
     * 
     * @param roomCode
     *     The roomCode
     */
    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
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
     *     The locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * 
     * @param locationName
     *     The locationName
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * 
     * @return
     *     The checkListCode
     */
    public String getCheckListCode() {
        return checkListCode;
    }

    /**
     * 
     * @param checkListCode
     *     The checkListCode
     */
    public void setCheckListCode(String checkListCode) {
        this.checkListCode = checkListCode;
    }

    /**
     * 
     * @return
     *     The checkListName
     */
    public String getCheckListName() {
        return checkListName;
    }

    /**
     * 
     * @param checkListName
     *     The checkListName
     */
    public void setCheckListName(String checkListName) {
        this.checkListName = checkListName;
    }

    /**
     * 
     * @return
     *     The ppmFrequency
     */
    public String getPpmFrequency() {
        return ppmFrequency;
    }

    /**
     * 
     * @param ppmFrequency
     *     The ppmFrequency
     */
    public void setPpmFrequency(String ppmFrequency) {
        this.ppmFrequency = ppmFrequency;
    }

    /**
     * 
     * @return
     *     The workHours
     */
    public String getWorkHours() {
        return workHours;
    }

    /**
     * 
     * @param workHours
     *     The workHours
     */
    public void setWorkHours(String workHours) {
        this.workHours = workHours;
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
     *     The checkedBy
     */
    public String getCheckedBy() {
        return checkedBy;
    }

    /**
     * 
     * @param checkedBy
     *     The checkedBy
     */
    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    /**
     * 
     * @return
     *     The attendedBy
     */
    public String getAttendedBy() {
        return attendedBy;
    }

    /**
     * 
     * @param attendedBy
     *     The attendedBy
     */
    public void setAttendedBy(String attendedBy) {
        this.attendedBy = attendedBy;
    }

    /**
     * 
     * @return
     *     The ppmStatus
     */
    public String getPpmStatus() {
        return ppmStatus;
    }

    /**
     * 
     * @param ppmStatus
     *     The ppmStatus
     */
    public void setPpmStatus(String ppmStatus) {
        this.ppmStatus = ppmStatus;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(compCode);
        dest.writeValue(workOrderNo);
        dest.writeValue(contractNo);
        dest.writeValue(ppmRefNo);
        dest.writeValue(natureCode);
        dest.writeValue(natureDesc);
        dest.writeValue(equipmentCode);
        dest.writeValue(equipmentName);
        dest.writeValue(assetCode);
        dest.writeValue(assetBarCode);
        dest.writeValue(zoneCode);
        dest.writeValue(building);
        dest.writeValue(floorCode);
        dest.writeValue(flatCode);
        dest.writeValue(roomCode);
        dest.writeValue(zoneBuilding);
        dest.writeValue(locationName);
        dest.writeValue(checkListCode);
        dest.writeValue(checkListName);
        dest.writeValue(ppmFrequency);
        dest.writeValue(workHours);
        dest.writeValue(startDate);
        dest.writeValue(endDate);
        dest.writeValue(checkedBy);
        dest.writeValue(attendedBy);
        dest.writeValue(ppmStatus);
    }

    public int describeContents() {
        return  0;
    }

}
