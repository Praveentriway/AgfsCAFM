package com.daemon.emco_android.repository.remote;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.AssetInfo;
import com.daemon.emco_android.model.common.AssetInfoTrans;
import com.daemon.emco_android.model.common.DocumentType;
import com.daemon.emco_android.model.common.EmployeeList;
import com.daemon.emco_android.model.common.JobList;
import com.daemon.emco_android.model.common.OpcoDetail;
import com.daemon.emco_android.model.response.AssetInfoResponse;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.DocumentTypeResponse;
import com.daemon.emco_android.model.response.EmployeeListResponse;
import com.daemon.emco_android.model.response.JobListResponse;
import com.daemon.emco_android.model.response.OpcoResponse;
import com.daemon.emco_android.repository.db.entity.SurveyTransaction;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetVerificationRepository {


    private static final String TAG = AssetVerificationRepository.class.getSimpleName();

    private AppCompatActivity mActivity;
    private ApiInterface mInterface;
    private Listener mCallback;

    public AssetVerificationRepository(AppCompatActivity _activity, Listener listener) {
        mActivity = _activity;
        mCallback = listener;
        mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
    }

    public void getAssetInfo(String assetTag) {

        try {
            mInterface.getAssetInfo(assetTag).enqueue(new Callback<AssetInfoResponse>() {
                @Override
                public void onResponse(Call<AssetInfoResponse> call, Response<AssetInfoResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            Log.d(TAG, "onResponse success");
                            mCallback.onReceiveAssetInfo(response.body().getObject());
                        }else{
                           mCallback.onReceiveFailureAssetInfo(response.body().getMessage());
                        }

                    }

                    else{
                        mCallback.onReceiveFailureAssetInfo(response.body().getMessage());
                    }

                }

                @Override
                public void onFailure(Call<AssetInfoResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());
                    mCallback.onReceiveFailureAssetInfo(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getJobList(String opco) {

        try {
            mInterface.getJobList(opco).enqueue(new Callback<JobListResponse>() {
                @Override
                public void onResponse(Call<JobListResponse> call, Response<JobListResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            Log.d(TAG, "onResponse success");
                            mCallback.onReceiveJobList(response.body().getObject());
                        }else{
                            mCallback.onReceiveFailureJobList(response.body().getMessage());
                        }
                    }
                    else{
                        mCallback.onReceiveFailureJobList(response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<JobListResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());
                    mCallback.onReceiveFailureJobList(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getEmployeeList() {

        try {
            mInterface.getEmployeeList().enqueue(new Callback<EmployeeListResponse>() {
                @Override
                public void onResponse(Call<EmployeeListResponse> call, Response<EmployeeListResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            Log.d(TAG, "onResponse success");
                            mCallback.onReceiveEmployeeList(response.body().getObject());
                        }else{
                            mCallback.onReceiveFailureEmployeeList(response.body().getMessage());
                        }

                    }

                    else{
                        mCallback.onReceiveFailureEmployeeList(response.body().getMessage());
                    }

                }

                @Override
                public void onFailure(Call<EmployeeListResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());
                    mCallback.onReceiveFailureEmployeeList(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDocType(final String type) {

        Call<DocumentTypeResponse> getAssetType= mInterface.getAssetType(type);

        getAssetType.enqueue(
                new Callback<DocumentTypeResponse>() {
                    @Override
                    public void onResponse(
                            Call<DocumentTypeResponse> call, Response<DocumentTypeResponse> response) {
                        if (response.isSuccessful()) {

                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {

                                mCallback.onReceiveAssetType(type,
                                        response.body().getPPMDetails(),
                                        AppUtils.MODE_SERVER);
                            } else
                                mCallback.onReceiveFailureJAssetType(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            mCallback.onReceiveFailureJAssetType(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<DocumentTypeResponse> call, Throwable t) {

                        mCallback.onReceiveFailureJAssetType(
                                mActivity.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }


    public void saveAsset(AssetInfoTrans trans) {

        Log.d(TAG, "saveAsset");
        Call<CommonResponse> saveSurvey = mInterface.saveAsset(trans);
        saveSurvey.enqueue(
                new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(
                            Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                mCallback.onSuccessSaveAsset(
                                        response.body().getMessage(), AppUtils.MODE_SERVER
                                );
                            } else
                                mCallback.onFailureSaveAsset(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            mCallback.onFailureSaveAsset(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                        mCallback.onFailureSaveAsset(
                                mActivity.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }


    public void getLocationOpco(String userid) {
        Log.d(TAG, "getLocationOpco");
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
                                mCallback.onSuccessLocationOpco(
                                        response.body().getOpco(), AppUtils.MODE_SERVER
                                );
                            } else
                                mCallback.onFailureLocationOpco(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            mCallback.onFailureLocationOpco(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<OpcoResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        mCallback.onFailureLocationOpco(
                                mActivity.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }



    public interface Listener {

        void onReceiveAssetInfo(List<AssetInfo> object);
        void onReceiveFailureAssetInfo(String toString);

        void onReceiveJobList(List<JobList> object);
        void onReceiveFailureJobList(String toString);


        void onReceiveEmployeeList(List<EmployeeList> object);
        void onReceiveFailureEmployeeList(String toString);

        void onReceiveAssetType(String type,List<DocumentType> rx, int from);
        void onReceiveFailureJAssetType(String err, int from);


        void onSuccessSaveAsset(String strMsg, int mode);

        void onFailureSaveAsset(String strErr, int mode);


        void onSuccessLocationOpco(List<OpcoDetail> opco, int mode);
        void onFailureLocationOpco(String strMsg, int mode);

    }


    }
