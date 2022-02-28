package com.daemon.emco_android.repository.remote;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.UtilityAssetDetail;
import com.daemon.emco_android.model.common.UtilityAssetTransaction;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.UtilityConsumptionResponse;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.utils.AppUtils;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilityRepository {

    private static final String TAG = UtilityRepository.class.getSimpleName();
    private AppCompatActivity mActivity;
    private ApiInterface mInterface;
    private Listener mCallback;

    public UtilityRepository(AppCompatActivity _activity, Listener listener) {
        mActivity = _activity;
        mCallback = listener;
        mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
    }

    public void getUtilityDetail(String assetTag) {

        try {
            mInterface.getUtilityDetail(assetTag).enqueue(new Callback<UtilityConsumptionResponse>() {
                @Override
                public void onResponse(Call<UtilityConsumptionResponse> call, Response<UtilityConsumptionResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            Log.d(TAG, "onResponse success");
                            mCallback.onReceiveUtilityDetails(response.body().getDetails());
                        }else{
                            mCallback.onReceiveFailureUtilityDetails(response.body().getMessage());
                        }
                    }

                    else{
                        mCallback.onReceiveFailureUtilityDetails(response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UtilityConsumptionResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());
                    mCallback.onReceiveFailureUtilityDetails(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveUtilityTransaction(UtilityAssetTransaction trans) {

        Log.d(TAG, "saveUtilityTransaction");
        Call<CommonResponse> saveUtilityTransaction = mInterface.saveUtilityTransaction(trans);
        saveUtilityTransaction.enqueue(
                new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(
                            Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                mCallback.onSuccessSaveUtilityReading(
                                        response.body().getMessage(), AppUtils.MODE_SERVER
                                );
                            } else
                                mCallback.onFailureSaveUtilityReading(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            mCallback.onFailureSaveUtilityReading(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                        mCallback.onFailureSaveUtilityReading(
                                mActivity.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }



    public interface Listener {

        void onReceiveUtilityDetails(List<UtilityAssetDetail> object);
        void onReceiveFailureUtilityDetails(String err);

        void onSuccessSaveUtilityReading(String strMsg, int mode);
        void onFailureSaveUtilityReading(String strErr, int mode);

    }

}
