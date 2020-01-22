package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMaterialEntity {

    @SerializedName("companyCode")
    @ColumnInfo(name = "companyCode")
    @Expose
    private String companyCode;
    @SerializedName("transactionType")
    @ColumnInfo(name = "transactionType")
    @Expose
    private String transactionType;
    @SerializedName("docType")
    @ColumnInfo(name = "docType")
    @Expose
    private String docType;
    @SerializedName("complainSite")
    @ColumnInfo(name = "complainSite")
    @Expose
    private String complainSite;
    @SerializedName("complainNumber")
    @ColumnInfo(name = "complainNumber")
    @Expose
    private String complainNumber;
    @SerializedName("jobNumber")
    @ColumnInfo(name = "jobNumber")
    @Expose
    private String jobNumber;
    @SerializedName("itemCode")
    @ColumnInfo(name = "itemCode")
    @Expose
    private String itemCode;
    @SerializedName("itemQuantity")
    @ColumnInfo(name = "itemQuantity")
    @Expose
    private String itemQuantity;
    @SerializedName("remarks")
    @ColumnInfo(name = "remarks")
    @Expose
    private String remarks;
    @SerializedName("user")
    @ColumnInfo(name = "user")
    @Expose
    private String user;
    @SerializedName("processed")
    @ColumnInfo(name = "processed")
    @Expose
    private String processed;
    @SerializedName("refrenceDocNumber")
    @ColumnInfo(name = "refrenceDocNumber")
    @Expose
    private String refrenceDocNumber;
    @SerializedName("refrenceDocDate")
    @ColumnInfo(name = "refrenceDocDate")
    @Expose
    private String refrenceDocDate;
    @SerializedName("creationDate")
    @Expose
    private String creationDate;
    @SerializedName("itemDescription")
    @Expose
    private String itemDescription;

    /**
     * @return The companyCode
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * @param companyCode The companyCode
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    /**
     * @return The complainSite
     */
    public String getComplainSite() {
        return complainSite;
    }

    /**
     * @param complainSite The complainSite
     */
    public void setComplainSite(String complainSite) {
        this.complainSite = complainSite;
    }

    /**
     * @return The complainNumber
     */
    public String getComplainNumber() {
        return complainNumber;
    }

    /**
     * @param complainNumber The complainNumber
     */
    public void setComplainNumber(String complainNumber) {
        this.complainNumber = complainNumber;
    }

    /**
     * @return The jobNumber
     */
    public String getJobNumber() {
        return jobNumber;
    }

    /**
     * @param jobNumber The jobNumber
     */
    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    /**
     * @return The itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode The itemCode
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return The itemQuantity
     */
    public String getItemQuantity() {
        return itemQuantity;
    }

    /**
     * @param itemQuantity The itemQuantity
     */
    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    /**
     * @return The remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks The remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return The user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return The processed
     */
    public String getProcessed() {
        return processed;
    }

    /**
     * @param processed The processed
     */
    public void setProcessed(String processed) {
        this.processed = processed;
    }

    /**
     * @return The refrenceDocNumber
     */
    public String getRefrenceDocNumber() {
        return refrenceDocNumber;
    }

    /**
     * @param refrenceDocNumber The refrenceDocNumber
     */
    public void setRefrenceDocNumber(String refrenceDocNumber) {
        this.refrenceDocNumber = refrenceDocNumber;
    }

    /**
     * @return The refrenceDocDate
     */
    public String getRefrenceDocDate() {
        return refrenceDocDate;
    }

    /**
     * @param refrenceDocDate The refrenceDocDate
     */
    public void setRefrenceDocDate(String refrenceDocDate) {
        this.refrenceDocDate = refrenceDocDate;
    }

    /**
     * @return The itemDescription
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * @param itemDescription The itemDescription
     */
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    /**
     * @return The creationDate
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate The creationDate
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
