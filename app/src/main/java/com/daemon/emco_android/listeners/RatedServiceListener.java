package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.SaveRatedServiceEntity;

import java.util.List;

public interface RatedServiceListener {
  void onRatedServiceReceived(List<SaveRatedServiceEntity> entities);
  void onRatedServiceReceived(SaveRatedServiceEntity entities);

  void onSaveRatedService(String message);
}
