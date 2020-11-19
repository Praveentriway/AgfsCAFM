package com.daemon.emco_android.model.request;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RCDownloadImageRequest {

    @SerializedName("opco")
    @Expose
    private String opco;
    @SerializedName("complaintSite")
    @Expose
    private String complaintSite;

    public String getPpmRefNo() {
        return ppmRefNo;
    }

    public void setPpmRefNo(String ppmRefNo) {
        this.ppmRefNo = ppmRefNo;
    }

    @SerializedName("complaintNo")

    @Expose
    private String complaintNo;
    @SerializedName("docType")
    @Expose
    private String docType;
    @SerializedName("ppmRefNo")
    @Expose
    private String ppmRefNo;

    @SerializedName("generalRefNo")
    @Expose
    private String generalRefNo;

    @SerializedName("transactionType")
    @Expose
    private String transactionType;

    @SerializedName("ref_doc")
    @Expose
    private String ref_doc;

    /**
     * @param generalRefNo The generalRefNo
     */
    public void setGeneralRefNo(String generalRefNo) {
        this.generalRefNo = generalRefNo;
    }

    /**
     * @return The generalRefNo
     */
    public String getGeneralRefNo() {
        return generalRefNo;
    }

    /**
     * @return The transactionType
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * @param transactionType The transactionType
     */
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    /**
     * @return The ref_doc
     */
    public String getRef_doc() {
        return ref_doc;
    }

    /**
     * @param ref_doc The ref_doc
     */
    public void setRef_doc(String ref_doc) {
        this.ref_doc = ref_doc;
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

    /**
     * @return The complaintNo
     */
    public String getComplaintNo() {
        return complaintNo;
    }

    /**
     * @param complaintNo The complaintNo
     */
    public void setComplaintNo(String complaintNo) {
        this.complaintNo = complaintNo;
    }

    /**
     * @return The docType
     */
    public String getDocType() {
        return docType;
    }

    /**
     * @param docType The docType
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

}
