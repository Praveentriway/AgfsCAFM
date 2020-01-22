
package com.daemon.emco_android.model.request;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ReceiveComplaintViewRequest {

    @SerializedName("complaintNumber")
    @Expose
    private String complaintNumber;
    @SerializedName("complaintSite")
    @Expose
    private String complaintSite;
    @SerializedName("opco")
    @Expose
    private String opco;

    public ReceiveComplaintViewRequest(String complaintNumber, String complaintSite, String opco) {
        this.complaintNumber = complaintNumber;
        this.complaintSite = complaintSite;
        this.opco = opco;
    }

    /**
     * 
     * @return
     *     The complaintNumber
     */
    public String getComplaintNumber() {
        return complaintNumber;
    }

    /**
     * 
     * @param complaintNumber
     *     The complaintNumber
     */
    public void setComplaintNumber(String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    /**
     * 
     * @return
     *     The complaintSite
     */
    public String getComplaintSite() {
        return complaintSite;
    }

    /**
     * 
     * @param complaintSite
     *     The complaintSite
     */
    public void setComplaintSite(String complaintSite) {
        this.complaintSite = complaintSite;
    }

    /**
     * 
     * @return
     *     The opco
     */
    public String getOpco() {
        return opco;
    }

    /**
     * 
     * @param opco
     *     The opco
     */
    public void setOpco(String opco) {
        this.opco = opco;
    }

}
