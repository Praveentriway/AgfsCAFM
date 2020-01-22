package com.daemon.emco_android.repository.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ServeyQuestionnaire implements Serializable {

    @SerializedName("opco")
    @Expose
    String opco;
    @SerializedName("customerCode")
    @Expose
    String customerCode;
    @SerializedName("customerName")
    @Expose
    String customerName;
    @SerializedName("surveyReference")
    @Expose
    String surveyReference;
    @SerializedName("surveyName")
    @Expose
    String surveyName;
    @SerializedName("surveyQuesId")
    @Expose
    String surveyQuesId;
    @SerializedName("surveyQues")
    @Expose
    String surveyQues;
    @SerializedName("surveyQuesValue")
    @Expose
    List<String> surveyQuesValue;
    @SerializedName("surveyQuesRate")
    @Expose
    String surveyQuesRate;

    @SerializedName("surveyQuesAns")
    @Expose
    String surveyQuesAns;

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

    public List<String> getSurveyQuesValue() {
        return surveyQuesValue;
    }

    public void setSurveyQuesValue(List<String> surveyQuesValue) {
        this.surveyQuesValue = surveyQuesValue;
    }

    public String getSurveyQuesRate() {
        return surveyQuesRate;
    }

    public void setSurveyQuesRate(String surveyQuesRate) {
        this.surveyQuesRate = surveyQuesRate;
    }

    public String getSurveyQuesAns() {
        return surveyQuesAns;
    }

    public void setSurveyQuesAns(String surveyQuesAns) {
        this.surveyQuesAns = surveyQuesAns;
    }
}
