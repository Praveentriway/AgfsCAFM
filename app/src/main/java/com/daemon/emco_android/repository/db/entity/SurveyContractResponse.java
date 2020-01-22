package com.daemon.emco_android.repository.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SurveyContractResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("totalNumberOfRows")
    @Expose
    private int totalNumberOfRows;

    @SerializedName("object")
    @Expose
    private List<SurveyContract> object =
            new ArrayList<SurveyContract>();

    /** @return The status */
    public String getStatus() {
        return status;
    }

    public int getTotalNumberOfRows() {
        return totalNumberOfRows;
    }

    public void setTotalNumberOfRows(int totalNumberOfRows) {
        this.totalNumberOfRows = totalNumberOfRows;
    }

    /** @param status The status */
    public void setStatus(String status) {
        this.status = status;
    }

    /** @return The message */
    public String getMessage() {
        return message;
    }

    /** @param message The message */
    public void setMessage(String message) {
        this.message = message;
    }

    public List<SurveyContract> getObject() {
        return object;
    }

    public void setObject(List<SurveyContract> object) {
        this.object = object;
    }
}
