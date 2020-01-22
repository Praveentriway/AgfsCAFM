package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daemonsoft on 7/20/2017.
 */

public class ReceiveComplaintResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("object")
    private List<ReceiveComplaintItemEntity> receiveComplaintItem = new ArrayList<>();
    @SerializedName("totalNumberOfRows")
    private String totalNumberOfRows;
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
    public List<ReceiveComplaintItemEntity> getReceiveComplaintItem() {
        return receiveComplaintItem;
    }

    /**
     * @param receiveComplaintItem The {@link com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity}
     */
    public void setObject(List<ReceiveComplaintItemEntity> receiveComplaintItem) {
        this.receiveComplaintItem = receiveComplaintItem;
    }

    public String getTotalNumberOfRows() {
        return totalNumberOfRows;
    }

    public void setTotalNumberOfRows(String totalNumberOfRows) {
        this.totalNumberOfRows = totalNumberOfRows;
    }
}
