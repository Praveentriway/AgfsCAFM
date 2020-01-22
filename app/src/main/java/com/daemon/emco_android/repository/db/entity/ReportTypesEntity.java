package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ReportTypesEntity")
public class ReportTypesEntity implements Parcelable {

  public static final Parcelable.Creator<ReportTypesEntity> CREATOR =
      new Creator<ReportTypesEntity>() {

        @SuppressWarnings({"unchecked"})
        public ReportTypesEntity createFromParcel(Parcel in) {
          ReportTypesEntity instance = new ReportTypesEntity();
          instance.reportType = ((String) in.readValue((String.class.getClassLoader())));
          instance.reportSRL = ((int) in.readValue((int.class.getClassLoader())));
          return instance;
        }

        public ReportTypesEntity[] newArray(int size) {
          return (new ReportTypesEntity[size]);
        }
      };

  @SerializedName("reportType")
  @ColumnInfo
  private String reportType;

  @SerializedName("reportSRL")
  @ColumnInfo
  @NonNull
  @PrimaryKey
  private int reportSRL;

  /** @return The reportType */
  public String getReportType() {
    return reportType;
  }

  /** @param reportType The reportType */
  public void setReportType(String reportType) {
    this.reportType = reportType;
  }

  /** @return The reportSRL */
  public int getReportSRL() {
    return reportSRL;
  }

  /** @param reportSRL The reportSRL */
  public void setReportSRL(int reportSRL) {
    this.reportSRL = reportSRL;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(reportType);
    dest.writeValue(reportSRL);
  }

  public int describeContents() {
    return 0;
  }
}
