package com.daemon.emco_android.ui.adapter;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;

public class CustomRecyclerViewItem implements Parcelable {

    private Bitmap thums;
    private DFoundWDoneImageEntity det;

    public Bitmap getThums() {
        return thums;
    }

    public void setThums(Bitmap thums) {
        this.thums = thums;
    }

    public DFoundWDoneImageEntity getDet() {
        return det;
    }

    public void setDet(DFoundWDoneImageEntity det) {
        this.det = det;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.thums, flags);
        dest.writeParcelable(this.det, flags);
    }

    public CustomRecyclerViewItem() {
    }

    protected CustomRecyclerViewItem(Parcel in) {
        this.thums = in.readParcelable(Bitmap.class.getClassLoader());
        this.det = in.readParcelable(DFoundWDoneImageEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<CustomRecyclerViewItem> CREATOR = new Parcelable.Creator<CustomRecyclerViewItem>() {
        @Override
        public CustomRecyclerViewItem createFromParcel(Parcel source) {
            return new CustomRecyclerViewItem(source);
        }

        @Override
        public CustomRecyclerViewItem[] newArray(int size) {
            return new CustomRecyclerViewItem[size];
        }
    };
}