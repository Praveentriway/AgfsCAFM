package com.daemon.emco_android.model.common;

import java.util.ArrayList;

public class SurveyQuestionnaire {

    String opco;
    String customerCode;
    String customerName;
    String surveyReference;
    String surveyName;
    String surveyQuesId;
    String surveyQues;
    ArrayList<String> surveyQuesValue;
    String surveyQuesRate;

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

    public String getSurveyQuesId() {
        return surveyQuesId;
    }

    public void setSurveyQuesId(String surveyQuesId) {
        this.surveyQuesId = surveyQuesId;
    }

    public String getSurveyQues() {
        return surveyQues;
    }

    public void setSurveyQues(String surveyQues) {
        this.surveyQues = surveyQues;
    }

    public ArrayList<String> getSurveyQuesValue() {
        return surveyQuesValue;
    }

    public void setSurveyQuesValue(ArrayList<String> surveyQuesValue) {
        this.surveyQuesValue = surveyQuesValue;
    }

    public String getSurveyQuesRate() {
        return surveyQuesRate;
    }

    public void setSurveyQuesRate(String surveyQuesRate) {
        this.surveyQuesRate = surveyQuesRate;
    }
}
