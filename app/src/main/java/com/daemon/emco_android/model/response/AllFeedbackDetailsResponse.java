package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class AllFeedbackDetailsResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("object")
    private List<SaveFeedbackEntity> object;

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
    public List<SaveFeedbackEntity> getObject() {
        return object;
    }

    /**
     * @param object The object
     */
    public void setObject(List<SaveFeedbackEntity> object) {
        this.object = object;
    }

}
