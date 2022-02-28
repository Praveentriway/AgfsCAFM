package com.daemon.emco_android.listeners;

public interface ChangePasswordListener {

    void onPasswordChangesSuccess(String login);

    void onPasswordFailure(String login);

}
