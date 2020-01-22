package com.daemon.emco_android.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardPieData implements Parcelable {

  public static final Parcelable.Creator<DashboardPieData> CREATOR =
      new Parcelable.Creator<DashboardPieData>() {
        @Override
        public DashboardPieData createFromParcel(Parcel source) {
          return new DashboardPieData(source);
        }

        @Override
        public DashboardPieData[] newArray(int size) {
          return new DashboardPieData[size];
        }
      };
  @SerializedName("code")
  @Expose
  private String code;
  @SerializedName("description")
  @Expose
  private String description;
  @SerializedName("totalNoOfRows")
  @Expose
  private String totalNoOfRows;

  public DashboardPieData() {}

  protected DashboardPieData(Parcel in) {
    this.code = in.readString();
    this.description = in.readString();
    this.totalNoOfRows = in.readString();
  }

  /** @return The code */
  public String getCode() {
    return code;
  }

  /** @param code The code */
  public void setCode(String code) {
    this.code = code;
  }

  /** @return The description */
  public String getDescription() {
    return description;
  }

  /** @param description The description */
  public void setDescription(String description) {
    this.description = description;
  }

  /** @return The totalNoOfRows */
  public String getTotalNoOfRows() {
    return totalNoOfRows;
  }

  /** @param totalNoOfRows The totalNoOfRows */
  public void setTotalNoOfRows(String totalNoOfRows) {
    this.totalNoOfRows = totalNoOfRows;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.code);
    dest.writeString(this.description);
    dest.writeString(this.totalNoOfRows);
  }
}
