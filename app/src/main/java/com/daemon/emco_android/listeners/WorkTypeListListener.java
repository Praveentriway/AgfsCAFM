package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.WorkTypeEntity;

import java.util.List;

/** Created by vikram on 19/7/17. */
public interface WorkTypeListListener {
  void onWorkTypeReceivedSuccess(List<WorkTypeEntity> typeEntities, int mode);

  void onWorkTypeReceivedFailure(String strErr, int mode);
}
