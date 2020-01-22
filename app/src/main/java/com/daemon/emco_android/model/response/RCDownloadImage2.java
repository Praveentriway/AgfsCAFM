package com.daemon.emco_android.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RCDownloadImage2 implements Parcelable {

    public static final Parcelable.Creator<RCDownloadImage> CREATOR = new Parcelable.Creator<RCDownloadImage>() {
        @Override
        public RCDownloadImage createFromParcel(Parcel source) {
            return new RCDownloadImage(source);
        }

        @Override
        public RCDownloadImage[] newArray(int size) {
            return new RCDownloadImage[size];
        }
    };
    @SerializedName("base64Image")
    private ArrayList<String> base64Image;
    @SerializedName("docType")
    private String docType;

    @SerializedName("imageCount")
    private int imageCount;


    public RCDownloadImage2(ArrayList<String> base64Image, String docType,int imageCount) {
        this.base64Image = base64Image;
        this.docType = docType;
        this.imageCount=imageCount;
    }

    protected RCDownloadImage2(Parcel in) {
         this.base64Image = in.readArrayList(String.class.getClassLoader());
       // this.base64Image =  in.readString();
        this.docType = in.readString();
        this.imageCount=in.readInt();
    }

    /**
     * @return The base64Image
     */
    public ArrayList<String> getBase64Image() {
        return base64Image;
    }

    /**
     * @param base64Image The base64Image
     */
    public void setBase64Image(ArrayList<String> base64Image) {
        this.base64Image = base64Image;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    /**
     * @return The docType
     */
    public String getDocType() {
        return docType;
    }

    /**
     * @param docType The docType
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.base64Image);
        dest.writeString(this.docType);
        dest.writeInt(this.imageCount);}
}
