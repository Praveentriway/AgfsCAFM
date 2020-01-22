package com.daemon.emco_android.model.response;

import com.daemon.emco_android.repository.db.entity.SiteAreaEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SiteResponse {

  @SerializedName("message")
  private String mMessage;

  @SerializedName("object")
  private List<SiteAreaEntity> mSiteAread;

  @SerializedName("status")
  private String mStatus;

  public String getMessage() {
    return mMessage;
  }

  public void setMessage(String message) {
    mMessage = message;
  }

  public List<SiteAreaEntity> getmSiteAread() {
    return mSiteAread;
  }

  public void setmSiteAread(List<SiteAreaEntity> mSiteAread) {
    this.mSiteAread = mSiteAread;
  }

  public String getStatus() {
    return mStatus;
  }

  public void setStatus(String status) {
    mStatus = status;
  }
}
