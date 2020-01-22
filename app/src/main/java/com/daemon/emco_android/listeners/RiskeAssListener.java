package com.daemon.emco_android.listeners;

import com.daemon.emco_android.model.response.Object;
import com.daemon.emco_android.model.response.ObjectPPM;
import com.daemon.emco_android.model.response.PpmFeedBackResponse;

import java.util.List;

/**
 * Created by vikram on 5/3/18.
 */

public interface RiskeAssListener
{


    void onPPMDataSuccess(List<ObjectPPM> loginppm);

    void onPPMDataSuccessPpmFeedBack(PpmFeedBackResponse loginppm);

    void onSaveDataSuccess(String commonResponse);
    void onListAssSuccess(List<Object> login);

    void onListAssFailure(String login);
}
