
package com.daemon.emco_android.model.request;

import javax.annotation.Generated;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class PpmScheduleDocByRequest implements Parcelable
{

    @SerializedName("compCode")
    @Expose
    private String compCode;
    @SerializedName("workOrderNo")
    @Expose
    private String workOrderNo;
    @SerializedName("contractNo")
    @Expose
    private String contractNo;
    @SerializedName("ppmRefNo")
    @Expose
    private String ppmRefNo;
    @SerializedName("equipmentCode")
    @Expose
    private String equipmentCode;
    @SerializedName("natureCode")
    @Expose
    private String natureCode;
    public final static Parcelable.Creator<PpmScheduleDocByRequest> CREATOR = new Creator<PpmScheduleDocByRequest>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PpmScheduleDocByRequest createFromParcel(Parcel in) {
            PpmScheduleDocByRequest instance = new PpmScheduleDocByRequest();
            instance.compCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.workOrderNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.contractNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.ppmRefNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.equipmentCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.natureCode = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public PpmScheduleDocByRequest[] newArray(int size) {
            return (new PpmScheduleDocByRequest[size]);
        }

    }
    ;

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
     *     The ppmRefNo
     */
    public String getPpmRefNo() {
        return ppmRefNo;
    }

    /**
     * 
     * @param ppmRefNo
     *     The ppmRefNo
     */
    public void setPpmRefNo(String ppmRefNo) {
        this.ppmRefNo = ppmRefNo;
    }

    /**
     * 
     * @return
     *     The equipmentCode
     */
    public String getEquipmentCode() {
        return equipmentCode;
    }

    /**
     * 
     * @param equipmentCode
     *     The equipmentCode
     */
    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    /**
     * 
     * @return
     *     The natureCode
     */
    public String getNatureCode() {
        return natureCode;
    }

    /**
     * 
     * @param natureCode
     *     The natureCode
     */
    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(compCode);
        dest.writeValue(workOrderNo);
        dest.writeValue(contractNo);
        dest.writeValue(ppmRefNo);
        dest.writeValue(equipmentCode);
        dest.writeValue(natureCode);
    }

    public int describeContents() {
        return  0;
    }

}
