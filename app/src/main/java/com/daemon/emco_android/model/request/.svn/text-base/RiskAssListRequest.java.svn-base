package com.daemon.emco_android.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vikram on 5/3/18.
 */

public class RiskAssListRequest
{
    @SerializedName("companyCode")
    private String companyCode;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getPpmRefNo() {
        return ppmRefNo;
    }

    public void setPpmRefNo(String ppmRefNo) {
        this.ppmRefNo = ppmRefNo;
    }

    public String getCheckListCode() {
        return checkListCode;
    }

    public void setCheckListCode(String checkListCode) {
        this.checkListCode = checkListCode;
    }

    @SerializedName("jobNo")

    private String jobNo;
    @SerializedName("ppmRefNo")
    private String ppmRefNo;
    @SerializedName("checkListCode")
    private String checkListCode;
    @SerializedName("ppmNo")
    private String ppmNo;
    @SerializedName("chkCode")
    private String chkCode;

    public RiskAssListRequest( String companyCode, String jobNo,String ppmRefNo,String checkListCode) {
        this.companyCode = companyCode;
        this.jobNo = jobNo;
        this.ppmRefNo = ppmRefNo;
        this.checkListCode = checkListCode;
    }

    public RiskAssListRequest( String companyCode, String jobNo,String ppmNo,String chkCode,String s) {
        this.companyCode = companyCode;
        this.jobNo = jobNo;
        this.ppmNo = ppmNo;
        this.chkCode = chkCode;
    }
    public RiskAssListRequest( String ppmNo) {
        this.ppmNo = ppmNo;
    }

}
