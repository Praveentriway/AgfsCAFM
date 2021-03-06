package com.daemon.emco_android.model.common;

public class CommonResponse {

  private String status;
  private String message;
  private String complaintNumber;
  private PPMDetails object;
  private String totalNumberOfRows;

  public PPMDetails getObject() {
    return object;
  }

  public void setObject(PPMDetails object) {
    this.object = object;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getComplaintNumber() {
    return complaintNumber;
  }

  public void setComplaintNumber(String complaintNumber) {
    this.complaintNumber = complaintNumber;
  }

  public String getTotalNumberOfRows() {
    return totalNumberOfRows;
  }

  public void setTotalNumberOfRows(String totalNumberOfRows) {
    this.totalNumberOfRows = totalNumberOfRows;
  }

}
