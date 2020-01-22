package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.WorkTypeEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WorkTypeResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("object")
    @Expose
    private List<WorkTypeEntity> object = new ArrayList<WorkTypeEntity>();

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
     * @return The object
     */
    public List<WorkTypeEntity> getObject() {
        return object;
    }

    /**
     * @param object The object
     */
    public void setObject(List<WorkTypeEntity> object) {
        this.object = object;
    }

}
