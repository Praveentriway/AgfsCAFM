package com.daemon.emco_android.repository.db.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RCViewRemarksEntity {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("object")
    private List<String> object = new ArrayList<>();

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
    public List<String> getObject() {
        return object;
    }

    /**
     * @param object The object
     */
    public void setObject(List<String> object) {
        this.object = object;
    }

}
