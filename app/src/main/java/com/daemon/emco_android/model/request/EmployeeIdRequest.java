package com.daemon.emco_android.model.request;

import com.google.gson.annotations.SerializedName;

public class EmployeeIdRequest {

  @SerializedName("employeeId")
  private String employeeId;

  @SerializedName("userCode")
  private String userCode;

  @SerializedName("emailId")
  private String emailId;

  @SerializedName("complainWebNumber")
  private String complainWebNumber;

  public EmployeeIdRequest(
      String employeeId, String userCode, String emailId, String complainWebNumber) {
    this.employeeId = employeeId;
    this.userCode = userCode;
    this.emailId = emailId;
    this.complainWebNumber = complainWebNumber;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
  }

  public String getUserCode() {
    return userCode;
  }

  public void setUserCode(String userCode) {
    this.userCode = userCode;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getComplainWebNumber() {
    return complainWebNumber;
  }

  public void setComplainWebNumber(String complainWebNumber) {
    this.complainWebNumber = complainWebNumber;
  }
}
