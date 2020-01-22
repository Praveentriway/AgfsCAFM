package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.ComplaintStatusEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;

import java.util.List;

/**
 * Created by Daemonsoft on 7/20/2017.
 */

public interface ReceivecomplaintList_Listener {
    void onReceiveComplaintListItemClicked(List<ReceiveComplaintItemEntity> temp, int position);
    void onReceiveComplaintListItemChecked(List<ReceiveComplaintItemEntity> data);

    void onReceiveComplaintCCReceived(String strMsg,int from);
    void onReceiveComplaintListReceived(List<ReceiveComplaintItemEntity> receiveComplaintItemList, String noOfRows, int from);
    void onReceiveComplaintListReceivedUpdate(List<ReceiveComplaintItemEntity> receiveComplaintItemList, ComplaintStatusEntity complaintStatusEntity, int from);
    void onReceiveComplaintListReceivedError(String errMsg,int from);
}
