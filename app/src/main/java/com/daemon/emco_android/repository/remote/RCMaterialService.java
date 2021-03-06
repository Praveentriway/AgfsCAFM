package com.daemon.emco_android.repository.remote;

import android.content.Context;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.db.entity.SaveMaterialEntity;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.Material_Listener;
import com.daemon.emco_android.model.request.GetMaterialRequest;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.GetMaterialResponse;
import com.daemon.emco_android.model.response.MaterialRequiredResponse;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Created by vikram on 17/7/17. */
public class RCMaterialService {
  private static final String TAG = RCMaterialService.class.getSimpleName();

  private Context mContext;
  private Material_Listener mCallback;
  private ApiInterface mInterface;

  public RCMaterialService(Context context, Material_Listener listener) {
    this.mContext = context;
    this.mCallback = listener;
    mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
  }

    public void getRCMeterialListData(String complainnumber, String transtype, String startIndex) {
        Log.d(TAG, "GetReceiveComplaintMeterialListData");
        try {
            mInterface
                    .getReceiveComplaintMaterialGetResult(
                            new GetMaterialRequest(complainnumber, transtype), startIndex, "10")
                    .enqueue(
                            new Callback<GetMaterialResponse>() {
                                @Override
                                public void onResponse(
                                        Call<GetMaterialResponse> call, Response<GetMaterialResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse success");
                                            mCallback.onRCMaterialGetListReceived(
                                                    response.body().getObject(),
                                                    response.body().getTotalNumberOfRows(),
                                                    AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onRCMaterialListReceivedError(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else
                                        mCallback.onRCMaterialListReceivedError(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<GetMaterialResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    mCallback.onRCMaterialListReceivedError(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getPPMMeterialListData(String refrenceDocNumber,String opco,
                                       String refrenceDocDate, String transtype, String startIndex) {
        Log.d(TAG, "GetReceiveComplaintMeterialListData");
        try {
            mInterface
                    .getPPMMaterialGetResult(
                            new GetMaterialRequest(opco,transtype,refrenceDocNumber,refrenceDocDate), startIndex, "10")
                    .enqueue(
                            new Callback<GetMaterialResponse>() {
                                @Override
                                public void onResponse(
                                        Call<GetMaterialResponse> call, Response<GetMaterialResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse success");
                                            mCallback.onRCMaterialGetListReceived(
                                                    response.body().getObject(),
                                                    response.body().getTotalNumberOfRows(),
                                                    AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onRCMaterialListReceivedError(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else
                                        mCallback.onRCMaterialListReceivedError(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<GetMaterialResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    mCallback.onRCMaterialListReceivedError(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getRCMeterialRequiredListData(
      String strOPCo, String strDescription, String startIndex) {
    Log.d(TAG, "GetReceiveComplaintMeterialListData");
    try {
      mInterface
          .getReceiveComplaintMaterialRequiredResult(strOPCo, strDescription, startIndex, "10")
          .enqueue(
              new Callback<MaterialRequiredResponse>() {
                @Override
                public void onResponse(
                    Call<MaterialRequiredResponse> call,
                    Response<MaterialRequiredResponse> response) {
                  if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                      Log.d(TAG, "onResponse success");
                      mCallback.onRCMaterialListReceived(
                          response.body().getObject(),
                          response.body().getTotalNumberOfRows(),
                          AppUtils.MODE_SERVER);
                    } else
                      mCallback.onRCMaterialListReceivedError(
                          response.body().getMessage(), AppUtils.MODE_SERVER);
                  } else
                    mCallback.onRCMaterialListReceivedError(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<MaterialRequiredResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure" + t.getMessage());
                  mCallback.onRCMaterialListReceivedError(
                      mContext.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

    public void PostReceiveComplaintSaveMaterialData(List<SaveMaterialEntity> saveMaterialRequests) {
        Log.d(TAG, "PostReceiveComplaintSaveMaterialData");
        try {
            mInterface
                    .getReceiveComplaintMaterialRequiredSaveResult(saveMaterialRequests)
                    .enqueue(
                            new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(
                                        Call<CommonResponse> call, Response<CommonResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse success");
                                            mCallback.onRCSaveMaterialSuccess(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onRCMaterialListReceivedError(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else
                                        mCallback.onRCMaterialListReceivedError(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<CommonResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    mCallback.onRCMaterialListReceivedError(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PostPPMSaveMaterialData(List<SaveMaterialEntity> saveMaterialRequests) {
        Log.d(TAG, "PostReceiveComplaintSaveMaterialData");
        try {
            mInterface
                    .getPPMMaterialRequiredSaveResult(saveMaterialRequests)
                    .enqueue(
                            new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(
                                        Call<CommonResponse> call, Response<CommonResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse success");
                                            mCallback.onRCSaveMaterialSuccess(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onRCMaterialListReceivedError(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else
                                        mCallback.onRCMaterialListReceivedError(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<CommonResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    mCallback.onRCMaterialListReceivedError(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
