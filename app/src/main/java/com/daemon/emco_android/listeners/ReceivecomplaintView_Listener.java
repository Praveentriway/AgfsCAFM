package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;

import java.util.List;

/**
 * Created by Daemonsoft on 7/26/2017.
 */

public interface ReceivecomplaintView_Listener {

    void onReceiveComplaintRemarksReceived(List<String> remarkList, int mode);

    void onReceiveComplaintRespondReceived(String strMsg, String complaintNumber, int mode);

    void onReceiveComplaintViewReceived(List<ReceiveComplaintViewEntity> complaintViewEntities, int mode);

    void onReceiveComplaintViewAssetDetailsReceived(List<AssetDetailsEntity> assetDetailsEntities, int mode);

    void onReceiveComplaintViewReceivedError(String msg, int mode);

    void onReceiveBarCodeAssetReceived(String msg, int mode);

    void onAllReceiveComplaintData(List<ReceiveComplaintRespondEntity> complaintRespondEntities, int modeLocal);

    void onReceiveComplaintBycomplaintNumber(ReceiveComplaintRespondEntity complaintRespondEntity, int modeLocal);
}
