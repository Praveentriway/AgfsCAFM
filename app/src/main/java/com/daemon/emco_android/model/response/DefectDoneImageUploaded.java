package com.daemon.emco_android.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefectDoneImageUploaded {

    @SerializedName("fieldId")
    @Expose
    private String fieldId;
    @SerializedName("imageName")
    @Expose
    private String imageName;
    @SerializedName("complaintNo")
    @Expose
    private String complaintNo;
    @SerializedName("docType")
    @Expose
    private String docType;

    public DefectDoneImageUploaded(String complaintNo, String docType) {
        this.complaintNo = complaintNo;
        this.docType = docType;
    }

    /**
     * @return The fieldId
     */
    public String getFieldId() {
        return fieldId;
    }

    /**
     * @param fieldId The fieldId
     */
    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    /**
     * @return The imageName
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @param imageName The imageName
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
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
