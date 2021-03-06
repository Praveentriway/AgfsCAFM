package com.daemon.emco_android.model.request;

import com.google.gson.annotations.SerializedName;

public class ReceiveComplainCCRequest {

    @SerializedName("complaintNumber")
    private String complaintNumber;
    @SerializedName("opco")
    private String opco;
    @SerializedName("complaintSite")
    private String complaintSite;

    public ReceiveComplainCCRequest(String complaintNumber, String opco, String complaintSite) {
        this.complaintNumber = complaintNumber;
        this.opco = opco;
        this.complaintSite = complaintSite;
    }

    /**
     * @return The complaintNumber
     */
    public String getComplaintNumber() {
        return complaintNumber;
    }

    /**
     * @param complaintNumber The complaintNumber
     */
    public void setComplaintNumber(String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    /**
     * @return The opco
     */
    public String getOpco() {
        return opco;
    }

    /**
     * @param opco The opco
     */
    public void setOpco(String opco) {
        this.opco = opco;
    }

    /**
     * @return The complaintSite
     */
    public String getComplaintSite() {
        return complaintSite;
    }

    /**
     * @param complaintSite The complaintSite
     */
    public void setComplaintSite(String complaintSite) {
        this.complaintSite = complaintSite;
    }

}
