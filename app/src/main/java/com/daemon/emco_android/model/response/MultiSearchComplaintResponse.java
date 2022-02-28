package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.MultiSearchComplaintEntity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class MultiSearchComplaintResponse {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("object")
    private ArrayList<MultiSearchComplaintEntity> mMultiSearch;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("totalNumberOfRows")
    private String totalNumberOfRows;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public ArrayList<MultiSearchComplaintEntity> getObject() {
        return mMultiSearch;
    }

    public void setObject(ArrayList<MultiSearchComplaintEntity> multiSearch) {
        mMultiSearch = multiSearch;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTotalNumberOfRows() {
        return totalNumberOfRows;
    }

    public void setTotalNumberOfRows(String totalNumberOfRows) {
        this.totalNumberOfRows = totalNumberOfRows;
    }
}
