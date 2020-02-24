package com.daemon.emco_android.repository.db.entity;

public class SurveyMaster {

    String opco;
    String customerCode;
    String customerName;
    String surveyReference;
    String surveyName;
    Boolean suggestionFlag;
    String customerSurveyFlag;
    String customerScoreLimit;
    String tenantSurveyFlag;
    String tenantScoreLimit;


    public String getCustomerSurveyFlag() {
        return customerSurveyFlag;
    }

    public void setCustomerSurveyFlag(String customerSurveyFlag) {
        this.customerSurveyFlag = customerSurveyFlag;
    }

    public String getCustomerScoreLimit() {
        return customerScoreLimit;
    }

    public void setCustomerScoreLimit(String customerScoreLimit) {
        this.customerScoreLimit = customerScoreLimit;
    }

    public String getTenantSurveyFlag() {
        return tenantSurveyFlag;
    }

    public void setTenantSurveyFlag(String tenantSurveyFlag) {
        this.tenantSurveyFlag = tenantSurveyFlag;
    }

    public String getTenantScoreLimit() {
        return tenantScoreLimit;
    }

    public void setTenantScoreLimit(String tenantScoreLimit) {
        this.tenantScoreLimit = tenantScoreLimit;
    }

    public Boolean getSuggestionFlag() {
        return suggestionFlag;
    }

    public void setSuggestionFlag(Boolean suggestionFlag) {
        this.suggestionFlag = suggestionFlag;
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
