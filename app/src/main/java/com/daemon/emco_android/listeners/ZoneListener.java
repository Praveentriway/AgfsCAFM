package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.ZoneEntity;

import java.util.List;

/** Created by vikram on 17/7/17. */
public interface ZoneListener {
  void onZoneReceivedSuccess(List<ZoneEntity> zoneList, int mode);

  void onZoneReceivedFailure(String strErr, int mode);
}
