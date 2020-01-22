package com.daemon.emco_android.model.common;

import android.os.Parcel;
import android.os.Parcelable;

public class DocumentType implements Parcelable {

    private String sdCode;
    private String sdName;
    private String categoryCode;
    private String categoryName;

    public String getSdCode() {
        return sdCode;
    }

    public void setSdCode(String sdCode) {
        this.sdCode = sdCode;
    }

    public String getSdName() {
        return sdName;
    }

    public void setSdName(String sdName) {
        this.sdName = sdName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sdCode);
        dest.writeString(this.sdName);
        dest.writeString(this.categoryCode);
        dest.writeString(this.categoryName);
    }

    public DocumentType() {
    }

    protected DocumentType(Parcel in) {
        this.sdCode = in.readString();
        this.sdName = in.readString();
        this.categoryCode = in.readString();
        this.categoryName = in.readString();
    }

    public static final Parcelable.Creator<DocumentType> CREATOR = new Parcelable.Creator<DocumentType>() {
        @Override
        public DocumentType createFromParcel(Parcel source) {
            return new DocumentType(source);
        }

        @Override
        public DocumentType[] newArray(int size) {
            return new DocumentType[size];
        }
    };
}
