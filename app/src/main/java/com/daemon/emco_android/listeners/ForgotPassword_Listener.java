package com.daemon.emco_android.listeners;


import com.daemon.emco_android.model.response.CommonResponse;

/**
 * Created by vikram on 13/6/17.
 */

public interface ForgotPassword_Listener {
    void onForgotPasswordReceivedSuccess(CommonResponse response);
    void onForgotPasswordReceivedError(String strErr);
}
