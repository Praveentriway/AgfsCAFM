package com.daemon.emco_android.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vikram on 17/7/17.
 */

public class ChangePasswordRequest {
    @SerializedName("userName")
    private String userName;
    @SerializedName("oldPassword")
    private String oldPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @SerializedName("newPassword")
    private String newPassword;

    public ChangePasswordRequest(String mUsername, String mPassword,String newPassword) {
        this.userName = mUsername;
        this.oldPassword = mPassword;
        this.newPassword = newPassword;
    }

    public ChangePasswordRequest(String mUsername) {
        this.userName = mUsername;
    }

    public String getmUsername() {
        return userName;
    }

    public void setmUsername(String mUsername) {
        this.userName = mUsername;
    }

    public String getmPassword() {
        return oldPassword;
    }

    public void setmPassword(String mPassword) {
        this.oldPassword = mPassword;
    }


}
