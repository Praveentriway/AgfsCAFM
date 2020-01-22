
package com.daemon.emco_android.model.request;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class SearchSingleComplaintRequest {

    @SerializedName("complainRefrenceNumber")
    private String mComplainRefrenceNumber;
    @SerializedName("opco")
    private String mOpco;
    @SerializedName("siteCode")
    private String mSiteCode;

    public SearchSingleComplaintRequest(String mComplainRefrenceNumber, String mOpco, String mSiteCode) {
        this.mComplainRefrenceNumber = mComplainRefrenceNumber;
        this.mOpco = mOpco;
        this.mSiteCode = mSiteCode;
    }

    public String getmComplainRefrenceNumber() {
        return mComplainRefrenceNumber;
    }

    public void setmComplainRefrenceNumber(String mComplainRefrenceNumber) {
        this.mComplainRefrenceNumber = mComplainRefrenceNumber;
    }

    public String getmOpco() {
        return mOpco;
    }

    public void setmOpco(String mOpco) {
        this.mOpco = mOpco;
    }

    public String getmSiteCode() {
        return mSiteCode;
    }

    public void setmSiteCode(String mSiteCode) {
        this.mSiteCode = mSiteCode;
    }
}
