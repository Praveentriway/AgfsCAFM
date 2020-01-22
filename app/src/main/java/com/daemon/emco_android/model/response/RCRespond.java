package com.daemon.emco_android.model.response;

import com.google.gson.annotations.SerializedName;

public class RCRespond {

  @SerializedName("complaintDetails")
  private String complaintDetails;

  @SerializedName("complaintStatus")
  private String complaintStatus;

  @SerializedName("confirm")
  private boolean confirm;

  /** @return The complaintDetails */
  public String getComplaintDetails() {
    return complaintDetails;
  }

  /** @param complaintDetails The complaintDetails */
  public void setComplaintDetails(String complaintDetails) {
    this.complaintDetails = complaintDetails;
  }

  /** @return The complaintStatus */
  public String getComplaintStatus() {
    return complaintStatus;
  }

  /** @param complaintStatus The complaintStatus */
  public void setComplaintStatus(String complaintStatus) {
    this.complaintStatus = complaintStatus;
  }

  /** @return The confirm */
  public boolean isConfirm() {
    return confirm;
  }

  /** @param confirm The confirm */
  public void setConfirm(boolean confirm) {
    this.confirm = confirm;
  }
}
