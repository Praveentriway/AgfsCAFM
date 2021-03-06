package com.daemon.emco_android.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.daemon.emco_android.model.common.PPMDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class PPMDetailsResponse implements Parcelable {

  public static final Parcelable.Creator<PPMDetailsResponse> CREATOR =
      new Creator<PPMDetailsResponse>() {

        @SuppressWarnings({"unchecked"})
        public PPMDetailsResponse createFromParcel(Parcel in) {
          PPMDetailsResponse instance = new PPMDetailsResponse();
          instance.status = ((String) in.readValue((String.class.getClassLoader())));
          instance.message = ((String) in.readValue((String.class.getClassLoader())));
          in.readList(
              instance.PPMDetails, (com.daemon.emco_android.model.common.PPMDetails.class.getClassLoader()));
          instance.totalNumberOfRows = ((String) in.readValue((String.class.getClassLoader())));
          return instance;
        }

        public PPMDetailsResponse[] newArray(int size) {
          return (new PPMDetailsResponse[size]);
        }
      };
  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("object")
  @Expose
  private List<PPMDetails> PPMDetails = new ArrayList<PPMDetails>();
  @SerializedName("totalNumberOfRows")
  @Expose
  private String totalNumberOfRows;

  /** @return The status */
  public String getStatus() {
    return status;
  }

  /** @param status The status */
  public void setStatus(String status) {
    this.status = status;
  }

  /** @return The message */
  public String getMessage() {
    return message;
  }

  /** @param message The message */
  public void setMessage(String message) {
    this.message = message;
  }

  /** @return The PPMDetails */
  public List<PPMDetails> getPPMDetails() {
    return PPMDetails;
  }

  /** @param PPMDetails The PPMDetails */
  public void setPPMDetails(List<PPMDetails> PPMDetails) {
    this.PPMDetails = PPMDetails;
  }

  /** @return The totalNumberOfRows */
  public String getTotalNumberOfRows() {
    return totalNumberOfRows;
  }

  /** @param totalNumberOfRows The totalNumberOfRows */
  public void setTotalNumberOfRows(String totalNumberOfRows) {
    this.totalNumberOfRows = totalNumberOfRows;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(status);
    dest.writeValue(message);
    dest.writeList(PPMDetails);
    dest.writeValue(totalNumberOfRows);
  }

  public int describeContents() {
    return 0;
  }
}
