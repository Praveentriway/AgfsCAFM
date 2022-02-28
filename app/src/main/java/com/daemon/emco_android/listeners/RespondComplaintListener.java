package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.WorkDefectEntity;
import com.daemon.emco_android.repository.db.entity.WorkDoneEntity;
import com.daemon.emco_android.repository.db.entity.WorkPendingReasonEntity;
import com.daemon.emco_android.repository.db.entity.WorkStatusEntity;
import com.daemon.emco_android.model.response.RCRespond;

import java.util.List;

/** Created by vikram on 17/7/17. */
public interface RespondComplaintListener {
  void onWorkDefectReceived(List<WorkDefectEntity> workDefectEntityList, int mode);

  void onWorkDoneReceivedSuccess(List<WorkDoneEntity> workDoneEntities, int mode);

  void onWorkStatusReceivedSuccess(List<WorkStatusEntity> workStatusEntities, int mode);

  void onWorkPendingReasonReceivedSuccess(
      List<WorkPendingReasonEntity> workPendingReasonEntities, int mode);

  void onComplaintRespondSaveReceived(RCRespond rcRespond, int mode);

  void onComplaintRespondReceivedFailure(String strErr, int mode);
}
