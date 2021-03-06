package com.daemon.emco_android.listeners;


import com.daemon.emco_android.model.common.ContractNoUsers;
import com.daemon.emco_android.repository.db.entity.ContractEntity;

import java.util.List;

/**
 * Created by vikram on 17/7/17.
 */

public interface JobNoListener {
    void onContractReceivedSuccess(List<ContractEntity> contractList, int mode);

    void onContractReceivedFailure(String strErr, int mode);
}
