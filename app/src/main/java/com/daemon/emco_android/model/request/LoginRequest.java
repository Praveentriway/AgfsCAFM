package com.daemon.emco_android.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vikram on 17/7/17.
 */

public class LoginRequest {
    @SerializedName("userName")
    private String mUsername;
    @SerializedName("password")
    private String mPassword;

    public LoginRequest(String mUsername, String mPassword) {
        this.mUsername = mUsername;
        this.mPassword = mPassword;
    }

    public LoginRequest(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
