package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.LogComplaintEntity;
import com.daemon.emco_android.model.request.EmployeeIdRequest;

import java.util.List;

/** Created by vikram on 18/7/17. */
public interface LogComplaint_Listener {
  void onLogComplaintDataReceivedSuccess(EmployeeIdRequest logComplaint, int mode);

  void onLogComplaintDataReceivedFailure(String strErr, int mode);

  void onAllLogComplaintData(List<LogComplaintEntity> logComplaintEntities, int mode);
}
