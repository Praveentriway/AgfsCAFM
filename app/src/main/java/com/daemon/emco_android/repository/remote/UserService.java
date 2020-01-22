package com.daemon.emco_android.repository.remote;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.db.entity.UserToken;
import com.daemon.emco_android.listeners.UserListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.AddNewUser;
import com.daemon.emco_android.model.request.LoginRequest;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.LoginResponse;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vikram on 13/6/17.
 */

public class UserService {
    private static final String TAG = UserService.class.getSimpleName();

    private AppCompatActivity mActivity;
    private ApiInterface mInterface;
    private UserListener mCallback;


    public UserService(AppCompatActivity _activity, UserListener listener) {
        mActivity = _activity;
        mCallback = listener;
        try{
            mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getLoginData(LoginRequest loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.getLoginResult(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getMessage()!=null){
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                Login mResponse = response.body().getObject();
                                mCallback.onLoginDataReceivedSuccess(mResponse, response.body().getTotalNumberOfRows());
                            } else mCallback.onUserDataReceivedFailure(response.body().getMessage());
                        }
                        else{
                            mCallback.onUserDataReceivedFailure(mActivity.getString(R.string.msg_request_error_occurred));
                        }

                    } else mCallback.onUserDataReceivedFailure(response.body().getMessage());
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    t.printStackTrace();
                    Log.d(TAG, "onFailure" + t.getMessage());
                    mCallback.onUserDataReceivedFailure(mActivity.getString(R.string.msg_request_error_occurred));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getForgotPasswordResult(String _email) {
        Log.d(TAG, "getForgotPasswordResult");
        try {
            mInterface.getForgotPasswordResult(new LoginRequest(_email)).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (response.isSuccessful()) {
                        CommonResponse mResponse = response.body();
                        if (mResponse.getStatus().equals(ApiConstant.SUCCESS))
                            mCallback.onUserDataReceivedSuccess(response.body());
                        else mCallback.onUserDataReceivedFailure(mResponse.getMessage());
                    } else mCallback.onUserDataReceivedFailure(response.message());
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    mCallback.onUserDataReceivedFailure(mActivity.getString(R.string.msg_request_error_occurred));
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void addUser(AddNewUser newUser) {
        Log.d(TAG, "addUser");
        try {
            mInterface.getRegResult(newUser).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            Log.d(TAG, "onResponse success");
                            mCallback.onUserDataReceivedSuccess(response.body());
                        } else mCallback.onUserDataReceivedFailure(response.body().getMessage());
                    } else mCallback.onUserDataReceivedFailure(response.message());
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());
                    mCallback.onUserDataReceivedFailure(mActivity.getString(R.string.msg_request_error_occurred));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addToken(UserToken userToken) {
        Log.d(TAG, "add token");
        try {
            mInterface.addUserToken(userToken).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            Log.d(TAG, "onResponse success");
                            mCallback.onUserDataReceivedSuccess(response.body());
                        } else mCallback.onUserDataReceivedFailure(response.body().getMessage());
                    } else mCallback.onUserDataReceivedFailure(response.message());
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());
                    mCallback.onUserDataReceivedFailure(mActivity.getString(R.string.msg_request_error_occurred));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void deleteToken(UserToken userToken) {
        Log.d(TAG, "deleteToken");
        try {
            mInterface.deleteUserToken(userToken).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            Log.d(TAG, "onResponse success");
                            mCallback.onUserDataReceivedSuccess(response.body());
                        } else mCallback.onUserDataReceivedFailure(response.body().getMessage());
                    } else mCallback.onUserDataReceivedFailure(response.message());
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());
                    mCallback.onUserDataReceivedFailure(mActivity.getString(R.string.msg_request_error_occurred));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
