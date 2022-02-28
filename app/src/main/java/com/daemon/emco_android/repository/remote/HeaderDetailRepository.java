package com.daemon.emco_android.repository.remote;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.JobList;
import com.daemon.emco_android.model.common.OpcoDetail;
import com.daemon.emco_android.model.response.JobListResponse;
import com.daemon.emco_android.model.response.OpcoResponse;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository class to get OPCO and contract details
 * **/

public class HeaderDetailRepository {


    private static final String TAG = HeaderDetailRepository.class.getSimpleName();

    private AppCompatActivity mActivity;
    private Listener mCallback;
    private ApiInterface mInterface;

    public HeaderDetailRepository(AppCompatActivity _activity, Listener listener) {
        mActivity = _activity;
        mCallback = listener;
        mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
    }


    public void getContractList(String opco) {

        try {
            mInterface.getJobList(opco).enqueue(new Callback<JobListResponse>() {
                @Override
                public void onResponse(Call<JobListResponse> call, Response<JobListResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            Log.d(TAG, "onResponse success");
                            mCallback.onReceiveContractList(response.body().getObject());
                        }else{
                            mCallback.onReceiveFailureContractList(response.body().getMessage());
                        }
                    }
                    else{
                        mCallback.onReceiveFailureContractList(response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<JobListResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());
                    mCallback.onReceiveFailureContractList(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getOpcoList(String userid) {
        Log.d(TAG, "getOpcoList");
        Call<OpcoResponse> getLocationOpco = mInterface.getLocationOpco(userid);

        getLocationOpco.enqueue(
                new Callback<OpcoResponse>() {
                    @Override
                    public void onResponse(
                            Call<OpcoResponse> call, Response<OpcoResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                mCallback.onSuccessOpcoList(
                                        response.body().getOpco(), AppUtils.MODE_SERVER
                                );
                            } else
                                mCallback.onFailureOpcoList(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            mCallback.onFailureOpcoList(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<OpcoResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        mCallback.onFailureOpcoList(
                                mActivity.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }



    public interface Listener {

        void onReceiveContractList(List<JobList> object);
        void onReceiveFailureContractList(String toString);
        void onSuccessOpcoList(List<OpcoDetail> opco, int mode);
        void onFailureOpcoList(String strMsg, int mode);

    }

}
