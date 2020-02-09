package com.daemon.emco_android.repository.remote;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.ChangePasswordListener;
import com.daemon.emco_android.model.request.ChangePasswordRequest;
import com.daemon.emco_android.model.response.CommonResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vikram on 13/6/17.
 */

public class ChangePasswordRepository {
    private static final String TAG = ChangePasswordRepository.class.getSimpleName();

    private AppCompatActivity mActivity;
    private ApiInterface mInterface;
    private ChangePasswordListener mCallback;

    public ChangePasswordRepository(AppCompatActivity _activity, ChangePasswordListener listener) {
        mActivity = _activity;
        mCallback = listener;
        mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
    }

    public void getPasswordData(ChangePasswordRequest loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.getChangePasswordResult(loginRequest).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            Log.d(TAG, "onResponse success");
                            mCallback.onPasswordChangesSuccess(response.body().getMessage().toString());
                        }else{
                            mCallback.onPasswordFailure(response.body().getMessage().toString());
                        }
                        //else mCallback.onUserDataReceivedFailure(response.body().getMessage());
                    }
                    //else mCallback.onUserDataReceivedFailure(response.message());
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());
                   // mCallback.onUserDataReceivedFailure(mActivity.getString(R.string.msg_request_error_occurred));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }


