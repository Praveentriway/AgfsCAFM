
package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.CategoryEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class CategoryResponse {
    @SerializedName("message")
    private String mMessage;
    @SerializedName("object")
    private List<CategoryEntity> mCategory;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<CategoryEntity> getObject() {
        return mCategory;
    }

    public void setObject(List<CategoryEntity> contract) {
        mCategory = contract;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
