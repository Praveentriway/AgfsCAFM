package com.daemon.emco_android.listeners;

import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.response.CommonResponse;

import retrofit2.http.Header;

public interface UserListener {
    void onLoginDataReceivedSuccess(Login login, String totalNumberOfRows,String token);

    void onUserDataReceivedSuccess(CommonResponse response);

    void onUserDataReceivedFailure(String strErr);
}
