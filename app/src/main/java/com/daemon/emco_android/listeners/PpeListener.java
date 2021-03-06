package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.daemon.emco_android.repository.db.entity.PPENameEntity;

import java.util.List;

/**
 * Created by vikram on 17/7/17.
 */

public interface PpeListener {
    void onPPENameListReceived(List<PPENameEntity> ppeNameEntities, int mode);

    void onPPESaveSuccess(String strMsg, int mode);

    void onPPESaveClicked(List<PPENameEntity> ppeNameEntities, List<PPEFetchSaveEntity> ppeSaveEntities, boolean isFetchdata);

    void onPPEFetchListSuccess(List<PPEFetchSaveEntity> ppeSaveEntities, int mode);

    void onPPEFetchListFailure(String strErr, int mode);

    void onPPEFetchListFailure2(String strErr, int mode);

    void onPPESaveFailure(String strErr, int mode);

    void onPPENameListFailure(String strErr, int mode);
}
