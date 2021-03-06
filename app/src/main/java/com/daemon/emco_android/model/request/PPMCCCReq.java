
package com.daemon.emco_android.model.request;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class PPMCCCReq {

    @SerializedName("compCode")
    @Expose
    private String compCode;
    @SerializedName("contractNo")
    @Expose
    private String contractNo;
    @SerializedName("workOrderNo")
    @Expose
    private String workOrderNo;

    public PPMCCCReq(String compCode, String contractNo, String workOrderNo) {
        this.compCode = compCode;
        this.contractNo = contractNo;
        this.workOrderNo = workOrderNo;
    }

    /**
     * 
     * @return
     *     The compCode
     */
    public String getCompCode() {
        return compCode;
    }

    /**
     * 
     * @param compCode
     *     The compCode
     */
    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    /**
     * 
     * @return
     *     The contractNo
     */
    public String getContractNo() {
        return contractNo;
    }

    /**
     * 
     * @param contractNo
     *     The contractNo
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    /**
     * 
     * @return
     *     The workOrderNo
     */
    public String getWorkOrderNo() {
        return workOrderNo;
    }

    /**
     * 
     * @param workOrderNo
     *     The workOrderNo
     */
    public void setWorkOrderNo(String workOrderNo) {
        this.workOrderNo = workOrderNo;
    }

}
