package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.PriorityEntity;

import java.util.List;

/** Created by vikram on 19/7/17. */
public interface PriorityListListener {
  void onPriorityReceivedSuccess(List<PriorityEntity> priorities, int mode);

  void onPriorityReceivedFailure(String strErr, int mode);
}
