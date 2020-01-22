package com.daemon.emco_android.repository.remote;

import android.content.Context;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.ReceivecomplaintView_Listener;
import com.daemon.emco_android.repository.db.entity.RCViewRemarksEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;
import com.daemon.emco_android.model.request.AssetDetailsRequest;
import com.daemon.emco_android.model.request.ReceiveComplaintViewRequest;
import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.daemon.emco_android.model.response.AssetDetailsResponse;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.ReceiveComplaintViewResponse;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vikram on 26/7/17.
 */

public class ReceiveComplaintViewService {
    private static final String TAG = ReceiveComplaintViewService.class.getSimpleName();

    private Context mContext;
    private ReceivecomplaintView_Listener mCallback;
    private ApiInterface mInterface;

    public ReceiveComplaintViewService(Context context, ReceivecomplaintView_Listener listener) {
        this.mContext = context;
        this.mCallback = listener;
        mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
    }

    /*public void GetAllReceiveComplaintViewData() {
        Log.d(TAG, "GetReceiveComplaintViewData");
        mInterface.getAllReceiveComplaintViewResult().enqueue(new Callback<ReceiveComplaintViewResponse>() {
            @Override
            public void onResponse(Call<ReceiveComplaintViewResponse> call, Response<ReceiveComplaintViewResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                        Log.d(TAG, "onResponse success");
                        mCallback.onReceiveComplaintViewReceived(response.body().getReceiveComplaintViewEntity(), AppUtils.MODE_SERVER);
                    } else
                        mCallback.onReceiveComplaintViewReceivedError(response.body().getMessage(), AppUtils.MODE_SERVER);
                } else
                    mCallback.onReceiveComplaintViewReceivedError(response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<ReceiveComplaintViewResponse> call, Throwable t) {
                Log.d(TAG, "onFailure" + t.getMessage());
                mCallback.onReceiveComplaintViewReceivedError(mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
        });
    }*/

    public void GetReceiveComplaintViewData(ReceiveComplaintViewRequest receiveComplaintViewRequest) {
        Log.d(TAG, "GetReceiveComplaintViewData");
        mInterface.getReceiveComplaintViewResult(receiveComplaintViewRequest).enqueue(new Callback<ReceiveComplaintViewResponse>() {
            @Override
            public void onResponse(Call<ReceiveComplaintViewResponse> call, Response<ReceiveComplaintViewResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                        Log.d(TAG, "onResponse success");
                        mCallback.onReceiveComplaintViewReceived(response.body().getReceiveComplaintViewEntity(), AppUtils.MODE_SERVER);
                    } else
                        mCallback.onReceiveComplaintViewReceivedError(response.body().getMessage(), AppUtils.MODE_SERVER);
                } else
                    mCallback.onReceiveComplaintViewReceivedError(response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<ReceiveComplaintViewResponse> call, Throwable t) {
                Log.d(TAG, "onFailure" + t.getMessage());
                mCallback.onReceiveComplaintViewReceivedError(mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
        });
    }

    public void postReceiveComplaintViewData(ReceiveComplaintRespondEntity respondRequest) {
        Log.d(TAG, "postReceiveComplaintViewData");
        mInterface.getReceiveComplaintRespondResult(respondRequest).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                        Log.d(TAG, "onResponse success");
                        mCallback.onReceiveComplaintRespondReceived(response.body().getMessage(), response.body().getComplaintNumber(), AppUtils.MODE_SERVER);
                    } else
                        mCallback.onReceiveComplaintViewReceivedError(response.body().getMessage(), AppUtils.MODE_SERVER);
                } else
                    mCallback.onReceiveComplaintViewReceivedError(response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Log.d(TAG, "onFailure" + t.getMessage());
                mCallback.onReceiveComplaintViewReceivedError(mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
        });
    }

    public void saveReceiveComplaintAssetCode(SaveRatedServiceRequest respondRequest) {
        Log.d(TAG, "postReceiveComplaintViewData");
        mInterface.saveAssetBarCode(respondRequest).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                        Log.d(TAG, "onResponse success");
                        mCallback.onReceiveBarCodeAssetReceived(response.body().getStatus(), AppUtils.MODE_SERVER);
                    } else
                        mCallback.onReceiveComplaintViewReceivedError(response.body().getMessage(), AppUtils.MODE_SERVER);
                } else
                    mCallback.onReceiveComplaintViewReceivedError(response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Log.d(TAG, "onFailure" + t.getMessage());
                mCallback.onReceiveComplaintViewReceivedError(mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
        });
    }

    public void GetReceiveComplaintViewAssetDetailsData(AssetDetailsRequest assetDetailsRequest) {
        Log.d(TAG, "GetReceiveComplaintViewAssetDetailsData");
        mInterface.getReceiveComplaintViewAssetDetailsResult(assetDetailsRequest).enqueue(new Callback<AssetDetailsResponse>() {
            @Override
            public void onResponse(Call<AssetDetailsResponse> call, Response<AssetDetailsResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                        mCallback.onReceiveComplaintViewAssetDetailsReceived(response.body().getAssetDetailsEntity(), AppUtils.MODE_SERVER);
                    } else
                        mCallback.onReceiveComplaintViewReceivedError(response.body().getMessage(), AppUtils.MODE_SERVER);
                } else
                    mCallback.onReceiveComplaintViewReceivedError(response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<AssetDetailsResponse> call, Throwable t) {
                Log.d(TAG, "onFailure" + t.getMessage());
                mCallback.onReceiveComplaintViewReceivedError(mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
        });
    }

    public void getAssestBarCodePPM(AssetDetailsRequest assetDetailsRequest) {
        Log.d(TAG, "GetReceiveComplaintViewAssetDetailsData");
        mInterface.getAssestBarCodePPM(assetDetailsRequest).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                        mCallback.onReceiveComplaintRespondReceived(response.body().getMessage(),"",1);
                    } else
                        mCallback.onReceiveComplaintViewReceivedError(response.body().getMessage(), AppUtils.MODE_SERVER);
                } else
                    mCallback.onReceiveComplaintViewReceivedError(response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Log.d(TAG, "onFailure" + t.getMessage());
                mCallback.onReceiveComplaintViewReceivedError(mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
        });
    }

    public void GetAllAssetBArCodeDetailsData() {
        Log.d(TAG, "GetReceiveComplaintViewAssetDetailsData");
        try {
            mInterface.getAllAssetBarcodeDetailsResult().enqueue(new Callback<AssetDetailsResponse>() {
                @Override
                public void onResponse(Call<AssetDetailsResponse> call, Response<AssetDetailsResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            mCallback.onReceiveComplaintViewAssetDetailsReceived(response.body().getAssetDetailsEntity(), AppUtils.MODE_SERVER);
                        } else
                            mCallback.onReceiveComplaintViewReceivedError(response.body().getMessage(), AppUtils.MODE_SERVER);
                    } else
                        mCallback.onReceiveComplaintViewReceivedError(response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<AssetDetailsResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());
                    mCallback.onReceiveComplaintViewReceivedError(mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getReceiveComplaintViewRemarksData() {
        Log.d(TAG, "postReceiveComplaintViewData");
        mInterface.getAllResponseDetailsResult().enqueue(new Callback<RCViewRemarksEntity>() {
            @Override
            public void onResponse(Call<RCViewRemarksEntity> call, Response<RCViewRemarksEntity> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                        Log.d(TAG, "onResponse success");
                        mCallback.onReceiveComplaintRemarksReceived(response.body().getObject(), AppUtils.MODE_SERVER);
                    } else
                        mCallback.onReceiveComplaintViewReceivedError(response.body().getMessage(), AppUtils.MODE_SERVER);
                } else
                    mCallback.onReceiveComplaintViewReceivedError(response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<RCViewRemarksEntity> call, Throwable t) {
                Log.d(TAG, "onFailure" + t.getMessage());
                mCallback.onReceiveComplaintViewReceivedError(mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
        });
    }

}
