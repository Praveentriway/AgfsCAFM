
package com.daemon.emco_android.model.request;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class SearchSingleComplaintPreventiveRequest {

    @SerializedName("ppmRefNo")
    private String ppmRefNo;

    @SerializedName("contractNo")
    private String contractNo;

    public SearchSingleComplaintPreventiveRequest(String ppmRefNo) {
        this.ppmRefNo = ppmRefNo;
    }

    public SearchSingleComplaintPreventiveRequest(String ppmRefNo,String contractNo) {
        this.ppmRefNo = ppmRefNo;
        this.contractNo = contractNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getPpmRefNo() {
        return ppmRefNo;
    }

    public void setPpmRefNo(String ppmRefNo) {
        this.ppmRefNo = ppmRefNo;
    }
}
