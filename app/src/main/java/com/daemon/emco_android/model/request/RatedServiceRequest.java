
package com.daemon.emco_android.model.request;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class RatedServiceRequest {

    @SerializedName("complaintNumber")
    private String mComplaintNumber;
    @SerializedName("complaintSite")
    private String mComplaintSite;
    @SerializedName("opco")
    private String mOpco;

    public String getComplaintNumber() {
        return mComplaintNumber;
    }

    public void setComplaintNumber(String complaintNumber) {
        mComplaintNumber = complaintNumber;
    }

    public String getComplaintSite() {
        return mComplaintSite;
    }

    public void setComplaintSite(String complaintSite) {
        mComplaintSite = complaintSite;
    }

    public String getOpco() {
        return mOpco;
    }

    public void setOpco(String opco) {
        mOpco = opco;
    }

}
