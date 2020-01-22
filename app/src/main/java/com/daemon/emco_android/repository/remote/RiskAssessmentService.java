package com.daemon.emco_android.repository.remote;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.RiskeAssListener;
import com.daemon.emco_android.model.request.PPMFindingRequest;
import com.daemon.emco_android.model.request.RiskAssListRequest;
import com.daemon.emco_android.model.request.SaveAssesEntity;
import com.daemon.emco_android.model.request.SavePPMFindingRequest;
import com.daemon.emco_android.model.response.GetPPMRecomResponse;
import com.daemon.emco_android.model.response.PpmFeedBackResponse;
import com.daemon.emco_android.model.response.RiskAssListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vikram on 5/3/18.
 */

public class RiskAssessmentService {
    private static final String TAG = ChangePasswordService.class.getSimpleName();

    private AppCompatActivity mActivity;
    private ApiInterface mInterface;
    private RiskeAssListener mCallback;

    public RiskAssessmentService(AppCompatActivity _activity, RiskeAssListener listener) {
        mActivity = _activity;
        mCallback = listener;
        mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
    }

    public void getRiskassesList(RiskAssListRequest loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.getRiskAssesList(loginRequest).enqueue(new Callback<RiskAssListResponse>() {
                @Override
                public void onResponse(Call<RiskAssListResponse> call, Response<RiskAssListResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            mCallback.onListAssSuccess(response.body().getObject());
                        }else{
                            mCallback.onListAssFailure(response.body().getMessage().toString());
                        }
                    }
                }
                @Override
                public void onFailure(Call<RiskAssListResponse> call, Throwable t) {
                    mCallback.onListAssFailure(t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getEquipemList(RiskAssListRequest loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.getEquipemList(loginRequest).enqueue(new Callback<RiskAssListResponse>() {
                @Override
                public void onResponse(Call<RiskAssListResponse> call, Response<RiskAssListResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            mCallback.onListAssSuccess(response.body().getObject());
                        }else{
                            mCallback.onListAssFailure(response.body().getMessage().toString());
                        }
                    }
                }
                @Override
                public void onFailure(Call<RiskAssListResponse> call, Throwable t) {
                    mCallback.onListAssFailure(t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPpmDetails(RiskAssListRequest loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.fetchFedbackPpm(loginRequest).enqueue(new Callback<PpmFeedBackResponse>() {
                @Override
                public void onResponse(Call<PpmFeedBackResponse> call, Response<PpmFeedBackResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            mCallback.onPPMDataSuccessPpmFeedBack(response.body());
                        }else{
                            mCallback.onListAssFailure(response.body().getMessage().toString());
                        }
                    }
                }
                @Override
                public void onFailure(Call<PpmFeedBackResponse> call, Throwable t) {
                    mCallback.onListAssFailure(t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveRiskList(List<SaveAssesEntity> loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.saveRiskAsses(loginRequest).enqueue(new Callback<RiskAssListResponse>() {
                @Override
                public void onResponse(Call<RiskAssListResponse> call, Response<RiskAssListResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            mCallback.onSaveDataSuccess(response.body().getMessage());
                        }else{
                            mCallback.onListAssFailure(response.body().getMessage().toString());
                        }
                    }
                }
                @Override
                public void onFailure(Call<RiskAssListResponse> call, Throwable t) {
                    mCallback.onListAssFailure(t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveEquipmenttools(List<SaveAssesEntity> loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.saveEquipmentTool(loginRequest).enqueue(new Callback<RiskAssListResponse>() {
                @Override
                public void onResponse(Call<RiskAssListResponse> call, Response<RiskAssListResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            mCallback.onSaveDataSuccess(response.body().getMessage());
                        }else{
                            mCallback.onListAssFailure(response.body().getMessage().toString());
                        }
                    }
                }
                @Override
                public void onFailure(Call<RiskAssListResponse> call, Throwable t) {
                    mCallback.onListAssFailure(t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getPPMFinding(PPMFindingRequest loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.getPPMFinding(loginRequest).enqueue(new Callback<GetPPMRecomResponse>() {
                @Override
                public void onResponse(Call<GetPPMRecomResponse> call, Response<GetPPMRecomResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            mCallback.onPPMDataSuccess(response.body().getObject());
                        }else{
                            mCallback.onListAssFailure(response.body().getMessage().toString());
                        }
                    }
                }
                @Override
                public void onFailure(Call<GetPPMRecomResponse> call, Throwable t) {
                    mCallback.onListAssFailure(t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePPMFinding(SavePPMFindingRequest loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.savePPMFinding(loginRequest).enqueue(new Callback<GetPPMRecomResponse>() {
                @Override
                public void onResponse(Call<GetPPMRecomResponse> call, Response<GetPPMRecomResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            mCallback.onSaveDataSuccess(response.body().getMessage());
                        }else{
                            mCallback.onListAssFailure(response.body().getMessage().toString());
                        }
                    }
                }
                @Override
                public void onFailure(Call<GetPPMRecomResponse> call, Throwable t) {
                    mCallback.onListAssFailure(t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
