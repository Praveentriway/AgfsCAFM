package com.daemon.emco_android.model.response;

import com.daemon.emco_android.model.common.Login;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LoginResponse {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("object")
    private Login mLogin;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("token")
    private String token;
    @SerializedName("totalNumberOfRows")
    private String totalNumberOfRows;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Login getObject() {
        return mLogin;
    }

    public void setObject(Login login) {
        mLogin = login;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTotalNumberOfRows() {
        return totalNumberOfRows;
    }

    public void setTotalNumberOfRows(String totalNumberOfRows) {
        this.totalNumberOfRows = totalNumberOfRows;
    }
}
