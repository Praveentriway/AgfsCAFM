package com.daemon.emco_android.repository.db.entity;

import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(
    tableName = "ReceiveComplaintItemEntity",
    primaryKeys = {"opco", "siteCode", "complaintNumber"})
public class ReceiveComplaintItemEntity implements Parcelable {

  public static final Parcelable.Creator<ReceiveComplaintItemEntity> CREATOR =
      new Parcelable.Creator<ReceiveComplaintItemEntity>() {
        @Override
        public ReceiveComplaintItemEntity createFromParcel(Parcel source) {
          return new ReceiveComplaintItemEntity(source);
        }

        @Override
        public ReceiveComplaintItemEntity[] newArray(int size) {
          return new ReceiveComplaintItemEntity[size];
        }
      };
  public static DiffUtil.ItemCallback<ReceiveComplaintItemEntity> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<ReceiveComplaintItemEntity>() {
        @Override
        public boolean areItemsTheSame(
            @NonNull ReceiveComplaintItemEntity oldItem,
            @NonNull ReceiveComplaintItemEntity newItem) {
          return oldItem.getComplaintNumber() == newItem.getComplaintNumber();
        }

        @Override
        public boolean areContentsTheSame(
            @NonNull ReceiveComplaintItemEntity oldItem,
            @NonNull ReceiveComplaintItemEntity newItem) {
          return oldItem.equals(newItem);
        }
      };

  @SerializedName("opco")
  @NonNull
  @ColumnInfo(name = "opco")
  private String opco = "";

  @SerializedName("siteCode")
  @NonNull
  @ColumnInfo(name = "siteCode")
  private String siteCode = "";

  @SerializedName("complaintNumber")
  @ColumnInfo(name = "complaintNumber")
  @NonNull
  private String complaintNumber = "";

  @SerializedName("complainRefrenceNumber")
  @Expose
  @ColumnInfo(name = "complainRefrenceNumber")
  private String complainRefrenceNumber;

  @SerializedName("complainDate")
  @Expose
  @ColumnInfo(name = "complainDate")
  private String complainDate;

  @SerializedName("workType")
  @Expose
  @ColumnInfo(name = "workType")
  private String workType;

  @SerializedName("workTypeDescription")
  @Expose
  @ColumnInfo(name = "workTypeDescription")
  private String workTypeDescription;

  @SerializedName("priority")
  @Expose
  @ColumnInfo(name = "priority")
  private String priority;

  @SerializedName("location")
  @Expose
  @ColumnInfo(name = "location")
  private String location;

  @SerializedName("complainDetails")
  @Expose
  @ColumnInfo(name = "complainDetails")
  private String complainDetails;

  @SerializedName("customerRefrenceNumber")
  @Expose
  @ColumnInfo(name = "customerRefrenceNumber")
  private String customerRefrenceNumber;

  @SerializedName("status")
  @Expose
  @ColumnInfo(name = "status")
  private String status;

  @SerializedName("statusDesription")
  @Expose
  @ColumnInfo(name = "statusDesription")
  private String statusDesription;

  @SerializedName("forwardedToEmployee")
  @Expose
  @ColumnInfo(name = "forwardedToEmployee")
  private String forwardedToEmployee;

  @SerializedName("contractNumber")
  @Expose
  @ColumnInfo(name = "contractNumber")
  private String contractNumber;

  @SerializedName("zoneCode")
  @Expose
  @ColumnInfo(name = "zoneCode")
  private String zoneCode;

  private boolean checked = false;

  public ReceiveComplaintItemEntity() {}

  protected ReceiveComplaintItemEntity(Parcel in) {
    this.opco = in.readString();
    this.siteCode = in.readString();
    this.complaintNumber = in.readString();
    this.complainRefrenceNumber = in.readString();
    this.complainDate = in.readString();
    this.workType = in.readString();
    this.workTypeDescription = in.readString();
    this.priority = in.readString();
    this.location = in.readString();
    this.complainDetails = in.readString();
    this.customerRefrenceNumber = in.readString();
    this.status = in.readString();
    this.statusDesription = in.readString();
    this.forwardedToEmployee = in.readString();
    this.contractNumber = in.readString();
    this.zoneCode = in.readString();
  }

  public boolean isChecked() {
    return checked;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }

  /** @return The opco */
  public String getOpco() {
    return opco;
  }

  /** @param opco The opco */
  public void setOpco(String opco) {
    this.opco = opco;
  }

  /** @return The siteCode */
  public String getSiteCode() {
    return siteCode;
  }

  /** @param siteCode The siteCode */
  public void setSiteCode(String siteCode) {
    this.siteCode = siteCode;
  }

  /** @return The complaintNumber */
  public String getComplaintNumber() {
    return complaintNumber;
  }

  /** @param complaintNumber The complaintNumber */
  public void setComplaintNumber(String complaintNumber) {
    this.complaintNumber = complaintNumber;
  }

  /** @return The complainRefrenceNumber */
  public String getComplainRefrenceNumber() {
    return complainRefrenceNumber;
  }

  /** @param complainRefrenceNumber The complainRefrenceNumber */
  public void setComplainRefrenceNumber(String complainRefrenceNumber) {
    this.complainRefrenceNumber = complainRefrenceNumber;
  }

  /** @return The complainDate */
  public String getComplainDate() {
    return complainDate;
  }

  /** @param complainDate The complainDate */
  public void setComplainDate(String complainDate) {
    this.complainDate = complainDate;
  }

  /** @return The workType */
  public String getWorkType() {
    return workType;
  }

  /** @param workType The workType */
  public void setWorkType(String workType) {
    this.workType = workType;
  }

  /** @return The workTypeDescription */
  public String getWorkTypeDescription() {
    return workTypeDescription;
  }

  /** @param workTypeDescription The workTypeDescription */
  public void setWorkTypeDescription(String workTypeDescription) {
    this.workTypeDescription = workTypeDescription;
  }

  /** @return The priority */
  public String getPriority() {
    return priority;
  }

  /** @param priority The priority */
  public void setPriority(String priority) {
    this.priority = priority;
  }

  /** @return The location */
  public String getLocation() {
    return location;
  }

  /** @param location The location */
  public void setLocation(String location) {
    this.location = location;
  }

  /** @return The complainDetails */
  public String getComplainDetails() {
    return complainDetails;
  }

  /** @param complainDetails The complainDetails */
  public void setComplainDetails(String complainDetails) {
    this.complainDetails = complainDetails;
  }

  /** @return The customerRefrenceNumber */
  public String getCustomerRefrenceNumber() {
    return customerRefrenceNumber;
  }

  /** @param customerRefrenceNumber The customerRefrenceNumber */
  public void setCustomerRefrenceNumber(String customerRefrenceNumber) {
    this.customerRefrenceNumber = customerRefrenceNumber;
  }

  /** @return The status */
  public String getStatus() {
    return status;
  }

  /** @param status The status */
  public void setStatus(String status) {
    this.status = status;
  }

  /** @return The statusDesription */
  public String getStatusDesription() {
    return statusDesription;
  }

  /** @param statusDesription The statusDesription */
  public void setStatusDesription(String statusDesription) {
    this.statusDesription = statusDesription;
  }

  /** @return The forwardedToEmployee */
  public String getForwardedToEmployee() {
    return forwardedToEmployee;
  }

  /** @param forwardedToEmployee The forwardedToEmployee */
  public void setForwardedToEmployee(String forwardedToEmployee) {
    this.forwardedToEmployee = forwardedToEmployee;
  }

  /** @return The contractNumber */
  public String getContractNumber() {
    return contractNumber;
  }

  /** @param contractNumber The contractNumber */
  public void setContractNumber(String contractNumber) {
    this.contractNumber = contractNumber;
  }

  /** @return The zoneCode */
  public String getZoneCode() {
    return zoneCode;
  }

  /** @param zoneCode The zoneCode */
  public void setZoneCode(String zoneCode) {
    this.zoneCode = zoneCode;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.opco);
    dest.writeString(this.siteCode);
    dest.writeString(this.complaintNumber);
    dest.writeString(this.complainRefrenceNumber);
    dest.writeString(this.complainDate);
    dest.writeString(this.workType);
    dest.writeString(this.workTypeDescription);
    dest.writeString(this.priority);
    dest.writeString(this.location);
    dest.writeString(this.complainDetails);
    dest.writeString(this.customerRefrenceNumber);
    dest.writeString(this.status);
    dest.writeString(this.statusDesription);
    dest.writeString(this.forwardedToEmployee);
    dest.writeString(this.contractNumber);
    dest.writeString(this.zoneCode);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ReceiveComplaintItemEntity that = (ReceiveComplaintItemEntity) o;

    if (checked != that.checked) return false;
    if (opco != null ? !opco.equals(that.opco) : that.opco != null) return false;
    if (siteCode != null ? !siteCode.equals(that.siteCode) : that.siteCode != null) return false;
    if (complaintNumber != null
        ? !complaintNumber.equals(that.complaintNumber)
        : that.complaintNumber != null) return false;
    if (complainRefrenceNumber != null
        ? !complainRefrenceNumber.equals(that.complainRefrenceNumber)
        : that.complainRefrenceNumber != null) return false;
    if (complainDate != null ? !complainDate.equals(that.complainDate) : that.complainDate != null)
      return false;
    if (workType != null ? !workType.equals(that.workType) : that.workType != null) return false;
    if (workTypeDescription != null
        ? !workTypeDescription.equals(that.workTypeDescription)
        : that.workTypeDescription != null) return false;
    if (priority != null ? !priority.equals(that.priority) : that.priority != null) return false;
    if (location != null ? !location.equals(that.location) : that.location != null) return false;
    if (complainDetails != null
        ? !complainDetails.equals(that.complainDetails)
        : that.complainDetails != null) return false;
    if (customerRefrenceNumber != null
        ? !customerRefrenceNumber.equals(that.customerRefrenceNumber)
        : that.customerRefrenceNumber != null) return false;
    if (status != null ? !status.equals(that.status) : that.status != null) return false;
    if (statusDesription != null
        ? !statusDesription.equals(that.statusDesription)
        : that.statusDesription != null) return false;
    if (forwardedToEmployee != null
        ? !forwardedToEmployee.equals(that.forwardedToEmployee)
        : that.forwardedToEmployee != null) return false;
    if (contractNumber != null
        ? !contractNumber.equals(that.contractNumber)
        : that.contractNumber != null) return false;
    return zoneCode != null ? zoneCode.equals(that.zoneCode) : that.zoneCode == null;
  }

  @Override
  public int hashCode() {
    int result = opco != null ? opco.hashCode() : 0;
    result = 31 * result + (siteCode != null ? siteCode.hashCode() : 0);
    result = 31 * result + (complaintNumber != null ? complaintNumber.hashCode() : 0);
    result = 31 * result + (complainRefrenceNumber != null ? complainRefrenceNumber.hashCode() : 0);
    result = 31 * result + (complainDate != null ? complainDate.hashCode() : 0);
    result = 31 * result + (workType != null ? workType.hashCode() : 0);
    result = 31 * result + (workTypeDescription != null ? workTypeDescription.hashCode() : 0);
    result = 31 * result + (priority != null ? priority.hashCode() : 0);
    result = 31 * result + (location != null ? location.hashCode() : 0);
    result = 31 * result + (complainDetails != null ? complainDetails.hashCode() : 0);
    result = 31 * result + (customerRefrenceNumber != null ? customerRefrenceNumber.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (statusDesription != null ? statusDesription.hashCode() : 0);
    result = 31 * result + (forwardedToEmployee != null ? forwardedToEmployee.hashCode() : 0);
    result = 31 * result + (contractNumber != null ? contractNumber.hashCode() : 0);
    result = 31 * result + (zoneCode != null ? zoneCode.hashCode() : 0);
    result = 31 * result + (checked ? 1 : 0);
    return result;
  }
}
