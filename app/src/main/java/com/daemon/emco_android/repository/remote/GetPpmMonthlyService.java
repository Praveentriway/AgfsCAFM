package com.daemon.emco_android.repository.remote;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.PPMService_Listner;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.daemon.emco_android.model.response.CheckListMonthlyResponse;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.GetPpmParamValue;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vikram on 11/3/18.
 */

public class GetPpmMonthlyService
{
    private static final String TAG = GetPostRateServiceService.class.getSimpleName();
    private AppCompatActivity mActivity;
    private Fragment mFragment;
    private PPMService_Listner mCallback;
    private ApiInterface mInterface;

    public GetPpmMonthlyService(AppCompatActivity mActivity, PPMService_Listner listener) {
        this.mActivity = mActivity;
        this.mCallback = listener;
        mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
    }
    public void savePpmParamCheckValue(List<PpmScheduleDocBy> loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.savePpmParamCheckValue(loginRequest).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            Log.d(TAG, "onResponse success");
                            mCallback.onReceivedSavedSucess(response.body().getMessage());
                        }else{
                            mCallback.onReceiveFailure(response.body().getMessage().toString());
                        }
                    }
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

    public void getSavePpmCheckList(SaveRatedServiceRequest loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.getSavePpmCheckList(loginRequest).enqueue(new Callback<CheckListMonthlyResponse>() {
                @Override
                public void onResponse(Call<CheckListMonthlyResponse> call, Response<CheckListMonthlyResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            mCallback.onReceivedSucess(response.body().getObject());
                        }else{
                            mCallback.onReceiveFailure(response.body().getMessage().toString());
                        }
                    }
                }
                @Override
                public void onFailure(Call<CheckListMonthlyResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());
                     mCallback.onReceiveFailure(t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPpmParameterValue(SaveRatedServiceRequest loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.getPpmCheckParamValue(loginRequest).enqueue(new Callback<GetPpmParamValue>() {
                @Override
                public void onResponse(Call<GetPpmParamValue> call, Response<GetPpmParamValue> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            mCallback.onReceivedPPMParameterSucess(response.body());
                        }else{
                            mCallback.onReceiveFailure(response.body().getMessage().toString());
                        }
                    }
                }
                @Override
                public void onFailure(Call<GetPpmParamValue> call, Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());
                    mCallback.onReceiveFailure(t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
