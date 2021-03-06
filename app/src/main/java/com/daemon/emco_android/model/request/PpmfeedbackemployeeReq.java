
package com.daemon.emco_android.model.request;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class PpmfeedbackemployeeReq {

    @SerializedName("companyCode")
    @Expose
    private String companyCode;
    @SerializedName("teamCode")
    @Expose
    private String teamCode;

    public PpmfeedbackemployeeReq(String companyCode, String teamCode) {
        this.companyCode = companyCode;
        this.teamCode = teamCode;
    }

    /**
     * 
     * @return
     *     The companyCode
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * 
     * @param companyCode
     *     The companyCode
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    /**
     * 
     * @return
     *     The teamCode
     */
    public String getTeamCode() {
        return teamCode;
    }

    /**
     * 
     * @param teamCode
     *     The teamCode
     */
    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

}
