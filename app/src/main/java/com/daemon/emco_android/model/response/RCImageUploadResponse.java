package com.daemon.emco_android.model.response;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class RCImageUploadResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("object")
    private DefectDoneImageUploaded object;

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
    public DefectDoneImageUploaded getObject() {
        return object;
    }

    /**
     * @param object The object
     */
    public void setObject(DefectDoneImageUploaded object) {
        this.object = object;
    }

}
