package com.daemon.emco_android.repository.remote;

import android.content.Context;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.PpeListener;
import com.daemon.emco_android.model.request.FetchPpeForPpmReq;
import com.daemon.emco_android.model.request.RiskAssListRequest;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.FetchPPENameResponse;
import com.daemon.emco_android.model.response.PpeAfterSaveResponse;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Created by vikram on 17/7/17. */
public class PpeService {
  private static final String TAG = PpeService.class.getSimpleName();

  private Context mContext;
  private PpeListener mCallback;

  private ApiInterface mInterface;

  public PpeService(Context context, PpeListener listener) {
    this.mContext = context;
    this.mCallback = listener;
    mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
  }

  public void getPpeNamesData() {
    Log.d(TAG, "getPpeNamesData");
    try {
      mInterface
          .getPPENameData()
          .enqueue(
              new Callback<FetchPPENameResponse>() {
                @Override
                public void onResponse(
                    Call<FetchPPENameResponse> call, Response<FetchPPENameResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    FetchPPENameResponse mResponse = response.body();
                    if (mResponse.getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                      mCallback.onPPENameListReceived(mResponse.getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallback.onPPENameListFailure(mResponse.getMessage(), AppUtils.MODE_SERVER);
                  } else mCallback.onPPENameListFailure(response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<FetchPPENameResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure" + t.getMessage());
                  mCallback.onPPENameListFailure(
                      mContext.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

    public void savePpeData(List<PPEFetchSaveEntity> request) {
        Log.d(TAG, "getPpeNamesData");
        try {
            mInterface
                    .savePPEData(request)
                    .enqueue(
                            new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(
                                        Call<CommonResponse> call, Response<CommonResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            mCallback.onPPESaveSuccess(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onPPESaveFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else mCallback.onPPESaveFailure(response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<CommonResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    mCallback.onPPESaveFailure(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void savePpeDataPPM(List<PPEFetchSaveEntity> request) {
        Log.d(TAG, "getPpeNamesData");
        try {
            mInterface
                    .savePPEDataPPM(request)
                    .enqueue(
                            new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(
                                        Call<CommonResponse> call, Response<CommonResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            mCallback.onPPESaveSuccess(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onPPESaveFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else mCallback.onPPESaveFailure(response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<CommonResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    mCallback.onPPESaveFailure(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void fetchPPEData(String complaintNo) {
        Log.d(TAG, "getPpeNamesData");
        try {
            mInterface
                    .getAfterSavePPEData(complaintNo)
                    .enqueue(
                            new Callback<PpeAfterSaveResponse>() {
                                @Override
                                public void onResponse(
                                        Call<PpeAfterSaveResponse> call, Response<PpeAfterSaveResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            mCallback.onPPEFetchListSuccess(
                                                    response.body().getObject(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onPPEFetchListFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else mCallback.onPPEFetchListFailure(response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<PpeAfterSaveResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    mCallback.onPPEFetchListFailure(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getSavePpePpm(RiskAssListRequest loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.savePpePPm(loginRequest).enqueue(new Callback<PpeAfterSaveResponse>() {
                @Override
                public void onResponse(Call<PpeAfterSaveResponse> call, Response<PpeAfterSaveResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            mCallback.onPPEFetchListSuccess(response.body().getObject(),AppUtils.MODE_SERVER);
                        }else{
                            mCallback.onPPEFetchListFailure(
                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                        }
                    }
                }
                @Override
                public void onFailure(Call<PpeAfterSaveResponse> call, Throwable t) {
                    mCallback.onPPEFetchListFailure(
                            mContext.getString(R.string.msg_request_error_occurred),
                            AppUtils.MODE_SERVER);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchPPEDataPPM(FetchPpeForPpmReq fetchPpeForPpmReq) {
        Log.d(TAG, "getPpeNamesData");
        try {
            mInterface
                    .getAfterSavePPEDataPPM(fetchPpeForPpmReq)
                    .enqueue(
                            new Callback<PpeAfterSaveResponse>() {
                                @Override
                                public void onResponse(
                                        Call<PpeAfterSaveResponse> call, Response<PpeAfterSaveResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            mCallback.onPPEFetchListSuccess(
                                                    response.body().getObject(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onPPEFetchListFailure2(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else mCallback.onPPEFetchListFailure2(response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<PpeAfterSaveResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    mCallback.onPPEFetchListFailure2(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
