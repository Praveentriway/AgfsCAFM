
package com.daemon.emco_android.model.request;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class BuildingDetailsRequest {

    @SerializedName("opco")
    @Expose
    private String opco;
    @SerializedName("contractNo")
    @Expose
    private String contractNo;
    @SerializedName("zoneCode")
    @Expose
    private String zoneCode;
    @SerializedName("userCode")
    @Expose
    private String userCode;
    @SerializedName("employeeId")
    @Expose
    private String employeeId;

    public BuildingDetailsRequest(String opco, String contractNo, String zoneCode,String userCode,String employeeId) {
        this.opco = opco;
        this.contractNo = contractNo;
        this.zoneCode = zoneCode;
        this.userCode=userCode;
        this.employeeId=employeeId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * 
     * @return
     *     The opco
     */
    public String getOpco() {
        return opco;
    }

    /**
     * 
     * @param opco
     *     The opco
     */
    public void setOpco(String opco) {
        this.opco = opco;
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
     *     The zoneCode
     */
    public String getZoneCode() {
        return zoneCode;
    }

    /**
     * 
     * @param zoneCode
     *     The zoneCode
     */
    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

}
