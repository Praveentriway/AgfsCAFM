
package com.daemon.emco_android.model.request;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class WorkDoneRequest {

    @SerializedName("complaintCode")
    private String mComplaintCode;
    @SerializedName("defectCode")
    private String mDefectCode;
    @SerializedName("workCategory")
    private String mWorkCategory;

    public WorkDoneRequest(String mComplaintCode, String mDefectCode, String mWorkCategory) {
        this.mComplaintCode = mComplaintCode;
        this.mDefectCode = mDefectCode;
        this.mWorkCategory = mWorkCategory;
    }

    public String getComplaintCode() {
        return mComplaintCode;
    }

    public void setComplaintCode(String complaintCode) {
        mComplaintCode = complaintCode;
    }

    public String getDefectCode() {
        return mDefectCode;
    }

    public void setDefectCode(String defectCode) {
        mDefectCode = defectCode;
    }

    public String getWorkCategory() {
        return mWorkCategory;
    }

    public void setWorkCategory(String workCategory) {
        mWorkCategory = workCategory;
    }

}
