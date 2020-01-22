package com.daemon.emco_android.model.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class FetchPpmScheduleDetailsRequest implements Parcelable {

  public static final Parcelable.Creator<FetchPpmScheduleDetailsRequest> CREATOR =
      new Creator<FetchPpmScheduleDetailsRequest>() {

        @SuppressWarnings({"unchecked"})
        public FetchPpmScheduleDetailsRequest createFromParcel(Parcel in) {
          FetchPpmScheduleDetailsRequest instance = new FetchPpmScheduleDetailsRequest();
          instance.compCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.workOrderNo = ((String) in.readValue((String.class.getClassLoader())));
          instance.contractNo = ((String) in.readValue((String.class.getClassLoader())));
          return instance;
        }

        public FetchPpmScheduleDetailsRequest[] newArray(int size) {
          return (new FetchPpmScheduleDetailsRequest[size]);
        }
      };
  @SerializedName("compCode")
  @Expose
  private String compCode;
  @SerializedName("workOrderNo")
  @Expose
  private String workOrderNo;
  @SerializedName("contractNo")
  @Expose
  private String contractNo;

  /** @return The compCode */
  public String getCompCode() {
    return compCode;
  }

  /** @param compCode The compCode */
  public void setCompCode(String compCode) {
    this.compCode = compCode;
  }

  /** @return The workOrderNo */
  public String getWorkOrderNo() {
    return workOrderNo;
  }

  /** @param workOrderNo The workOrderNo */
  public void setWorkOrderNo(String workOrderNo) {
    this.workOrderNo = workOrderNo;
  }

  /** @return The contractNo */
  public String getContractNo() {
    return contractNo;
  }

  /** @param contractNo The contractNo */
  public void setContractNo(String contractNo) {
    this.contractNo = contractNo;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(compCode);
    dest.writeValue(workOrderNo);
    dest.writeValue(contractNo);
  }

  public int describeContents() {
    return 0;
  }
}
