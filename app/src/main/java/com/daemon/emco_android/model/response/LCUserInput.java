package com.daemon.emco_android.model.response;

import com.google.gson.annotations.SerializedName;

public class LCUserInput {

  @SerializedName("jobNumber")
  private String jobNumber;

  @SerializedName("jobDescription")
  private String jobDescription;

  @SerializedName("zoneCode")
  private String zoneCode;

  @SerializedName("zoneDescription")
  private String zoneDescription;

  @SerializedName("complainSite")
  private String complainSite;

  @SerializedName("companyCode")
  private String companyCode;

  /** @return The jobNumber */
  public String getJobNumber() {
    return jobNumber;
  }

  /** @param jobNumber The jobNumber */
  public void setJobNumber(String jobNumber) {
    this.jobNumber = jobNumber;
  }

  /** @return The jobDescription */
  public String getJobDescription() {
    return jobDescription;
  }

  /** @param jobDescription The jobDescription */
  public void setJobDescription(String jobDescription) {
    this.jobDescription = jobDescription;
  }

  /** @return The zoneCode */
  public String getZoneCode() {
    return zoneCode;
  }

  /** @param zoneCode The zoneCode */
  public void setZoneCode(String zoneCode) {
    this.zoneCode = zoneCode;
  }

  public String getZoneDescription() {
    return zoneDescription;
  }

  public void setZoneDescription(String zoneDescription) {
    this.zoneDescription = zoneDescription;
  }

  public String getComplainSite() {
    return complainSite;
  }

  public void setComplainSite(String complainSite) {
    this.complainSite = complainSite;
  }

  public String getCompanyCode() {
    return companyCode;
  }

  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }
}
