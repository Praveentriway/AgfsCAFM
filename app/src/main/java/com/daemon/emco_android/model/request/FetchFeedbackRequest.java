
package com.daemon.emco_android.model.request;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class FetchFeedbackRequest
{

    @SerializedName("complaintNumber")
    @Expose
    private String complaintNumber;
    @SerializedName("complaintSite")
    @Expose
    private String complaintSite;
    @SerializedName("opco")
    @Expose
    private String opco;


    public FetchFeedbackRequest(String complaintNumber, String complaintSite, String opco) {
        this.complaintNumber = complaintNumber;
        this.complaintSite = complaintSite;
        this.opco = opco;
    }

    public String getComplaintNumber() {
        return complaintNumber;
    }

    public void setComplaintNumber(String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public String getComplaintSite() {
        return complaintSite;
    }

    public void setComplaintSite(String complaintSite) {
        this.complaintSite = complaintSite;
    }

    public String getOpco() {
        return opco;
    }

    public void setOpco(String opco) {
        this.opco = opco;
    }
}
