package com.daemon.emco_android.repository.db.entity;

public class SurveyMaster {

    String opco;
    String customerCode;
    String customerName;
    String surveyReference;
    String surveyName;

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

    public void setSurveyReference(String surveyReference) {
        this.surveyReference = surveyReference;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }
}
