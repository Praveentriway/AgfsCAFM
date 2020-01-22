package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "DFoundWDoneImageEntity")
public class DFoundWDoneImageEntity implements Parcelable {

  @ColumnInfo @NonNull @PrimaryKey private String id;

  @SerializedName("opco")
  @ColumnInfo
  private String opco;

  @SerializedName("complaintSite")
  @ColumnInfo
  private String complaintSite;

  @SerializedName("complaintNo")
  @ColumnInfo
  private String complaintNo;

  public String getPpmRefNo() {
    return ppmRefNo;
  }

  public void setPpmRefNo(String ppmRefNo) {
    this.ppmRefNo = ppmRefNo;
  }

  @SerializedName("ppmRefNo")
  @ColumnInfo

  private String ppmRefNo;

  @SerializedName("fileType")
  @ColumnInfo
  private String fileType;

  @SerializedName("transactionType")
  @ColumnInfo
  private String transactionType;

  @SerializedName("docType")
  @ColumnInfo
  private String docType;

  @SerializedName("createdBy")
  @ColumnInfo
  private String createdBy;

  @SerializedName("modifiedBy")
  @ColumnInfo
  private String modifiedBy;

  @SerializedName("base64Image")
  @ColumnInfo
  private String base64Image;

  @SerializedName("imageCount")
  @ColumnInfo
  private int imageCount;

  @SerializedName("fieldId")
  @ColumnInfo
  private String fieldId;

  @SerializedName("imageName")
  @ColumnInfo
  private String imageName;

  @SerializedName("actStartDate")
  @ColumnInfo
  private String actStartDate;

  @SerializedName("actEndDate")
  @ColumnInfo
  private String actEndDate;

  public DFoundWDoneImageEntity(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFieldId() {
    return fieldId;
  }

  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  public String getActStartDate() {
    return actStartDate;
  }

  public void setActStartDate(String actStartDate) {
    this.actStartDate = actStartDate;
  }

  public String getActEndDate() {
    return actEndDate;
  }

  public void setActEndDate(String actEndDate) {
    this.actEndDate = actEndDate;
  }

  public int getImageCount() {
    return imageCount;
  }

  public void setImageCount(int imageCount) {
    this.imageCount = imageCount;
  }

  /** @return The opco */
  public String getOpco() {
    return opco;
  }

  /** @param opco The opco */
  public void setOpco(String opco) {
    this.opco = opco;
  }

  /** @return The complaintSite */
  public String getComplaintSite() {
    return complaintSite;
  }

  /** @param complaintSite The complaintSite */
  public void setComplaintSite(String complaintSite) {
    this.complaintSite = complaintSite;
  }

  /** @return The complaintNo */
  public String getComplaintNo() {
    return complaintNo;
  }

  /** @param complaintNo The complaintNo */
  public void setComplaintNo(String complaintNo) {
    this.complaintNo = complaintNo;
  }

  /** @return The fileType */
  public String getFileType() {
    return fileType;
  }

  /** @param fileType The fileType */
  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  /** @return The transactionType */
  public String getTransactionType() {
    return transactionType;
  }

  /** @param transactionType The transactionType */
  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  /** @return The docType */
  public String getDocType() {
    return docType;
  }

  /** @param docType The docType */
  public void setDocType(String docType) {
    this.docType = docType;
  }

  /** @return The createdBy */
  public String getCreatedBy() {
    return createdBy;
  }

  /** @param createdBy The createdBy */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /** @return The modifiedBy */
  public String getModifiedBy() {
    return modifiedBy;
  }

  /** @param modifiedBy The modifiedBy */
  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  /** @return The base64Image */
  public String getBase64Image() {
    return base64Image;
  }

  /** @param base64Image The base64Image */
  public void setBase64Image(String base64Image) {
    this.base64Image = base64Image;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.opco);
    dest.writeString(this.complaintSite);
    dest.writeString(this.complaintNo);
    dest.writeString(this.ppmRefNo);
    dest.writeString(this.fileType);
    dest.writeString(this.transactionType);
    dest.writeString(this.docType);
    dest.writeString(this.createdBy);
    dest.writeString(this.modifiedBy);
    dest.writeString(this.base64Image);
    dest.writeInt(this.imageCount);
    dest.writeString(this.fieldId);
    dest.writeString(this.imageName);
    dest.writeString(this.actStartDate);
    dest.writeString(this.actEndDate);
  }

  protected DFoundWDoneImageEntity(Parcel in) {
    this.id = in.readString();
    this.opco = in.readString();
    this.complaintSite = in.readString();
    this.complaintNo = in.readString();
    this.ppmRefNo = in.readString();
    this.fileType = in.readString();
    this.transactionType = in.readString();
    this.docType = in.readString();
    this.createdBy = in.readString();
    this.modifiedBy = in.readString();
    this.base64Image = in.readString();
    this.imageCount = in.readInt();
    this.fieldId = in.readString();
    this.imageName = in.readString();
    this.actStartDate = in.readString();
    this.actEndDate = in.readString();
  }

  public static final Parcelable.Creator<DFoundWDoneImageEntity> CREATOR = new Parcelable.Creator<DFoundWDoneImageEntity>() {
    @Override
    public DFoundWDoneImageEntity createFromParcel(Parcel source) {
      return new DFoundWDoneImageEntity(source);
    }

    @Override
    public DFoundWDoneImageEntity[] newArray(int size) {
      return new DFoundWDoneImageEntity[size];
    }
  };
}
