package com.daemon.emco_android.repository.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vikram on 5/3/18.
 */

public class RiskAssesEntity implements Parcelable {

    public final static Parcelable.Creator<RiskAssesEntity> CREATOR = new Creator<RiskAssesEntity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RiskAssesEntity createFromParcel(Parcel in) {
            RiskAssesEntity instance = new RiskAssesEntity();

            instance.ppeType = ((String) in.readValue((String.class.getClassLoader())));
            instance.refCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.refNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.desc = ((String) in.readValue((String.class.getClassLoader())));
            instance.checkListCode = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public RiskAssesEntity[] newArray(int size) {
            return (new RiskAssesEntity[size]);
        }

    };

    @Expose
    private String ppeType;
    @SerializedName("ppeType")
    @Expose
    private String refCode;
    @SerializedName("refCode")
    @Expose
    private String refNo;

    public String getPpeType() {
        return ppeType;
    }

    public void setPpeType(String ppeType) {
        this.ppeType = ppeType;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCheckListCode() {
        return checkListCode;
    }

    public void setCheckListCode(String checkListCode) {
        this.checkListCode = checkListCode;
    }

    @SerializedName("refNo")

    @Expose
    private String desc;
    @SerializedName("desc")
    private String checkListCode;
    @SerializedName("checkListCode")
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(ppeType);
        parcel.writeValue(refCode);
        parcel.writeValue(refNo);
        parcel.writeValue(desc);
        parcel.writeValue(checkListCode);}
}
