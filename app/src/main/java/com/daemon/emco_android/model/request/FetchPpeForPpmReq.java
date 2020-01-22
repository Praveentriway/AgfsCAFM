
package com.daemon.emco_android.model.request;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class FetchPpeForPpmReq {

    @SerializedName("checkListCode")
    @Expose
    private String checkListCode;
    @SerializedName("ppeType")
    @Expose
    private String ppeType;

    /**
     * 
     * @return
     *     The checkListCode
     */
    public String getCheckListCode() {
        return checkListCode;
    }

    /**
     * 
     * @param checkListCode
     *     The checkListCode
     */
    public void setCheckListCode(String checkListCode) {
        this.checkListCode = checkListCode;
    }

    /**
     * 
     * @return
     *     The ppeType
     */
    public String getPpeType() {
        return ppeType;
    }

    /**
     * 
     * @param ppeType
     *     The ppeType
     */
    public void setPpeType(String ppeType) {
        this.ppeType = ppeType;
    }

}
