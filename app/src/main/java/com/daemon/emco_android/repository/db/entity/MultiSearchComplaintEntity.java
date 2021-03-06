package com.daemon.emco_android.repository.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class MultiSearchComplaintEntity implements Parcelable {

    public final static Parcelable.Creator<MultiSearchComplaintEntity> CREATOR = new Creator<MultiSearchComplaintEntity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MultiSearchComplaintEntity createFromParcel(Parcel in) {
            MultiSearchComplaintEntity instance = new MultiSearchComplaintEntity();
            instance.ticketNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.zoneName = ((String) in.readValue((String.class.getClassLoader())));
            instance.buildingName = ((String) in.readValue((String.class.getClassLoader())));
            instance.location = ((String) in.readValue((String.class.getClassLoader())));
            instance.taskDetails = ((String) in.readValue((String.class.getClassLoader())));
            instance.statusDesc = ((String) in.readValue((String.class.getClassLoader())));
            instance.customerRefNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.taskDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.workNatureDesc = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public MultiSearchComplaintEntity[] newArray(int size) {
            return (new MultiSearchComplaintEntity[size]);
        }

    };
    @SerializedName("ticketNo")
    @Expose
    private String ticketNo;
    @SerializedName("zoneName")
    @Expose
    private String zoneName;
    @SerializedName("buildingName")
    @Expose
    private String buildingName;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("taskDetails")
    @Expose
    private String taskDetails;
    @SerializedName("statusDesc")
    @Expose
    private String statusDesc;
    @SerializedName("customerRefNo")
    @Expose
    private String customerRefNo;
    @SerializedName("taskDate")
    @Expose
    private String taskDate;
    @SerializedName("workNatureDesc")
    @Expose
    private String workNatureDesc;

    /**
     * @return The ticketNo
     */
    public String getTicketNo() {
        return ticketNo;
    }

    /**
     * @param ticketNo The ticketNo
     */
    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    /**
     * @return The zoneName
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * @param zoneName The zoneName
     */
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    /**
     * @return The buildingName
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * @param buildingName The buildingName
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    /**
     * @return The location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return The taskDetails
     */
    public String getTaskDetails() {
        return taskDetails;
    }

    /**
     * @param taskDetails The taskDetails
     */
    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }

    /**
     * @return The statusDesc
     */
    public String getStatusDesc() {
        return statusDesc;
    }

    /**
     * @param statusDesc The statusDesc
     */
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    /**
     * @return The customerRefNo
     */
    public String getCustomerRefNo() {
        return customerRefNo;
    }

    /**
     * @param customerRefNo The customerRefNo
     */
    public void setCustomerRefNo(String customerRefNo) {
        this.customerRefNo = customerRefNo;
    }

    /**
     * @return The taskDate
     */
    public String getTaskDate() {
        return taskDate;
    }

    /**
     * @param taskDate The taskDate
     */
    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    /**
     * @return The workNatureDesc
     */
    public String getWorkNatureDesc() {
        return workNatureDesc;
    }

    /**
     * @param workNatureDesc The workNatureDesc
     */
    public void setWorkNatureDesc(String workNatureDesc) {
        this.workNatureDesc = workNatureDesc;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ticketNo);
        dest.writeValue(zoneName);
        dest.writeValue(buildingName);
        dest.writeValue(location);
        dest.writeValue(taskDetails);
        dest.writeValue(statusDesc);
        dest.writeValue(customerRefNo);
        dest.writeValue(taskDate);
        dest.writeValue(workNatureDesc);
    }

    public int describeContents() {
        return 0;
    }

}
