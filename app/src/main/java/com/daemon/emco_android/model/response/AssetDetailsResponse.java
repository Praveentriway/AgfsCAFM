package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class AssetDetailsResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("object")
    @Expose
    private List<AssetDetailsEntity> assetDetailsEntity = new ArrayList<AssetDetailsEntity>();

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The assetDetailsEntity
     */
    public List<AssetDetailsEntity> getAssetDetailsEntity() {
        return assetDetailsEntity;
    }

    /**
     * @param assetDetailsEntity The assetDetailsEntity
     */
    public void setAssetDetailsEntity(List<AssetDetailsEntity> assetDetailsEntity) {
        this.assetDetailsEntity = assetDetailsEntity;
    }

}
