package com.daemon.emco_android.model.common;

import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntity;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class FeedbackDetailsResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("object")
    private SaveFeedbackEntity object;

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
    public SaveFeedbackEntity getObject() {
        return object;
    }

    /**
     * @param object The object
     */
    public void setObject(SaveFeedbackEntity object) {
        this.object = object;
    }

}
