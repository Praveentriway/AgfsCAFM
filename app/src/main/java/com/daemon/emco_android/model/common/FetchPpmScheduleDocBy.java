
package com.daemon.emco_android.model.common;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class FetchPpmScheduleDocBy implements Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("object")
    @Expose
    private List<PpmScheduleDocBy> ppmScheduleDocBy = new ArrayList<PpmScheduleDocBy>();
    public final static Parcelable.Creator<FetchPpmScheduleDocBy> CREATOR = new Creator<FetchPpmScheduleDocBy>() {


        @SuppressWarnings({
            "unchecked"
        })
        public FetchPpmScheduleDocBy createFromParcel(Parcel in) {
            FetchPpmScheduleDocBy instance = new FetchPpmScheduleDocBy();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.ppmScheduleDocBy, (PpmScheduleDocBy.class.getClassLoader()));
            return instance;
        }

        public FetchPpmScheduleDocBy[] newArray(int size) {
            return (new FetchPpmScheduleDocBy[size]);
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
     *     The ppmScheduleDocBy
     */
    public List<PpmScheduleDocBy> getPpmScheduleDocBy() {
        return ppmScheduleDocBy;
    }

    /**
     * 
     * @param ppmScheduleDocBy
     *     The ppmScheduleDocBy
     */
    public void setPpmScheduleDocBy(List<PpmScheduleDocBy> ppmScheduleDocBy) {
        this.ppmScheduleDocBy = ppmScheduleDocBy;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeList(ppmScheduleDocBy);
    }

    public int describeContents() {
        return  0;
    }

}
