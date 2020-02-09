package com.daemon.emco_android.repository.db.entity;

import java.io.Serializable;
import java.util.List;

public class SurveyTransaction implements Serializable {

    String opco;
    String customerCode;
    String customerName;
    String surveyReference;
    String contractNo;
    String location;
    String clientName;
    String designation;
    String contactNo;
    String email;
    String suggestion;
    String signature;
    String surveyFrom;
    String tenantName;
    String createdBy;
    List<ServeyQuestionnaire> ques;

    public String getTenantName() { return tenantName;}

    public void setTenantName(String tenantName) { this.tenantName = tenantName;}

    public String getSurveyFrom() {
        return surveyFrom;
    }

    public void setSurveyFrom(String surveyFrom) {
        this.surveyFrom = surveyFrom;
    }

    public String getOpco() {
        return opco;
    }

    public void setOpco(String opco) {
        this.opco = opco;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSurveyReference() {
        return surveyReference;
    }

    public void setSurveyReference(String surveyReference) { this.surveyReference = surveyReference;}

    public String getContractNo() {
            return (contractNo==null ? "":contractNo);
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<ServeyQuestionnaire> getQues() {
        return ques;
    }

    public void setQues(List<ServeyQuestionnaire> ques) {
        this.ques = ques;
    }
}
