package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.MaterialMasterEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Daemonsoft on 7/28/2017.
 */
public class MaterialRequiredResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("object")
    @Expose
    private List<MaterialMasterEntity> object = null;
    @SerializedName("totalNumberOfRows")
    private String totalNumberOfRows;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MaterialMasterEntity> getObject() {
        return object;
    }

    public void setObject(List<MaterialMasterEntity> object) {
        this.object = object;
    }

    public String getTotalNumberOfRows() {
        return totalNumberOfRows;
    }

    public void setTotalNumberOfRows(String totalNumberOfRows) {
        this.totalNumberOfRows = totalNumberOfRows;
    }

}