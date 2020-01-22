package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.SiteAreaEntity;

import java.util.List;

/** Created by vikram on 17/7/17. */
public interface SiteListener {
  void onLogComplaintSiteReceivedSuccess(List<SiteAreaEntity> siteAreaList, int mode);

  void onLogComplaintSiteReceivedFailure(String strErr, int mode);
}
