package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.SurveyEmployeeList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SurveyFloorFlatListResponse {

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
    private List<SurveyFloorFlat> object =
            new ArrayList<SurveyFloorFlat>();


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

    public int getTotalNumberOfRows() {
        return totalNumberOfRows;
    }

    public void setTotalNumberOfRows(int totalNumberOfRows) {
        this.totalNumberOfRows = totalNumberOfRows;
    }

    public List<SurveyFloorFlat> getObject() {
        return object;
    }

    public void setObject(List<SurveyFloorFlat> object) {
        this.object = object;
    }
}
