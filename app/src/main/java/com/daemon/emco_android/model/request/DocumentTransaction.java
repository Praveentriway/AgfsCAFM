package com.daemon.emco_android.model.request;

import android.os.Parcel;
import android.os.Parcelable;

public class DocumentTransaction implements Parcelable {

    private String	opco;
    private String  fileType;
    private String  transactionType;
    private String  docType;
    private String  createdBy;
    private String  modifiedBy;
    private String  base64Image;
    private String  createdDate;
    private String  modifiedDate;
    private int docCount;
    private String	refType;
    private String	image_name;
    private String	fieldId;
    private String 	uniqueId;
    private String 	referenceId;
    private String 	remarks;
    private String 	sdCode;
    private String 	categoryCode;

    public String getOpco() {
        return opco;
    }

    public void setOpco(String opco) {
        this.opco = opco;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getDocCount() {
        return docCount;
    }

    public void setDocCount(int docCount) {
        this.docCount = docCount;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSdCode() {
        return sdCode;
    }

    public void setSdCode(String sdCode) {
        this.sdCode = sdCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.opco);
        dest.writeString(this.fileType);
        dest.writeString(this.transactionType);
        dest.writeString(this.docType);
        dest.writeString(this.createdBy);
        dest.writeString(this.modifiedBy);
        dest.writeString(this.base64Image);
        dest.writeString(this.createdDate);
        dest.writeString(this.modifiedDate);
        dest.writeInt(this.docCount);
        dest.writeString(this.refType);
        dest.writeString(this.image_name);
        dest.writeString(this.fieldId);
        dest.writeString(this.uniqueId);
        dest.writeString(this.referenceId);
        dest.writeString(this.remarks);
        dest.writeString(this.sdCode);
        dest.writeString(this.categoryCode);
    }

    public DocumentTransaction() {
    }

    protected DocumentTransaction(Parcel in) {
        this.opco = in.readString();
        this.fileType = in.readString();
        this.transactionType = in.readString();
        this.docType = in.readString();
        this.createdBy = in.readString();
        this.modifiedBy = in.readString();
        this.base64Image = in.readString();
        this.createdDate = in.readString();
        this.modifiedDate = in.readString();
        this.docCount = in.readInt();
        this.refType = in.readString();
        this.image_name = in.readString();
        this.fieldId = in.readString();
        this.uniqueId = in.readString();
        this.referenceId = in.readString();
        this.remarks = in.readString();
        this.sdCode = in.readString();
        this.categoryCode = in.readString();
    }

    public static final Parcelable.Creator<DocumentTransaction> CREATOR = new Parcelable.Creator<DocumentTransaction>() {
        @Override
        public DocumentTransaction createFromParcel(Parcel source) {
            return new DocumentTransaction(source);
        }

        @Override
        public DocumentTransaction[] newArray(int size) {
            return new DocumentTransaction[size];
        }
    };
}
