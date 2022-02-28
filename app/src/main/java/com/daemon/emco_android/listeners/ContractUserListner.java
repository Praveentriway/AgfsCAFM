package com.daemon.emco_android.listeners;

import com.daemon.emco_android.model.common.ContractNoUsers;
import com.daemon.emco_android.repository.db.entity.ContractEntity;

import java.util.List;

public interface ContractUserListner {

    void onContractUserReceivedSuccess(List<ContractNoUsers> contractList, int mode);
   // void onContractUserSiteReceivedSuccess(List<ContractNoUsers> contractList, int mode);
    void onContractReceivedFailure(String strErr, int mode);
}
