package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.MaterialMasterEntity;
import com.daemon.emco_android.repository.db.entity.SaveMaterialEntity;

import java.util.List;

/** Created by Daemonsoft on 7/20/2017. */
public interface Material_Listener {

  void onRCSaveMaterialSuccess(String strMsg, int from);

  void onRCMaterialListReceived(
      List<MaterialMasterEntity> materialRequiredEntities, String noOfRows, int from);

  void onRCMaterialGetListReceived(
      List<SaveMaterialEntity> getMaterialEntities, String noOfRows, int from);

  void onRCMaterialListReceivedError(String strErr, int from);
}
