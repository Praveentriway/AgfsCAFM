package com.daemon.emco_android.listeners;

import com.daemon.emco_android.model.common.Login;

/**
 * Created by vikram on 17/7/17.
 */

public interface LoginListener {
    void onLoginDataReceivedSuccess(Login login,String totalNumberOfRows);
    void onLoginDataReceivedFailure(String strErr);
}
