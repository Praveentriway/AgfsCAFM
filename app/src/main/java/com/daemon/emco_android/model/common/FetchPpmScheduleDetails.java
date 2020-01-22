
package com.daemon.emco_android.model.common;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class FetchPpmScheduleDetails implements Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("object")
    @Expose
    private List<PpmScheduleDetails> ppmScheduleDetails = new ArrayList<PpmScheduleDetails>();
    @SerializedName("totalNumberOfRows")
    @Expose
    private String totalNumberOfRows;
    public final static Parcelable.Creator<FetchPpmScheduleDetails> CREATOR = new Creator<FetchPpmScheduleDetails>() {

        @SuppressWarnings({
            "unchecked"
        })
        public FetchPpmScheduleDetails createFromParcel(Parcel in) {
            FetchPpmScheduleDetails instance = new FetchPpmScheduleDetails();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.ppmScheduleDetails, (PpmScheduleDetails.class.getClassLoader()));
            instance.totalNumberOfRows = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public FetchPpmScheduleDetails[] newArray(int size) {
            return (new FetchPpmScheduleDetails[size]);
        }

    }
    ;

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @return
     *     The ppmScheduleDetails
     */
    public List<PpmScheduleDetails> getPpmScheduleDetails() {
        return ppmScheduleDetails;
    }

    /**
     * 
     * @param ppmScheduleDetails
     *     The ppmScheduleDetails
     */
    public void setPpmScheduleDetails(List<PpmScheduleDetails> ppmScheduleDetails) {
        this.ppmScheduleDetails = ppmScheduleDetails;
    }

    /**
     * 
     * @return
     *     The totalNumberOfRows
     */
    public String getTotalNumberOfRows() {
        return totalNumberOfRows;
    }

    /**
     * 
     * @param totalNumberOfRows
     *     The totalNumberOfRows
     */
    public void setTotalNumberOfRows(String totalNumberOfRows) {
        this.totalNumberOfRows = totalNumberOfRows;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeList(ppmScheduleDetails);
        dest.writeValue(totalNumberOfRows);
    }

    public int describeContents() {
        return  0;
    }

}
