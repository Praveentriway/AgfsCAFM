package com.daemon.emco_android.repository.remote;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.CheckCustomerSignListener;
import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.daemon.emco_android.model.response.CommonResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vikram on 11/3/18.
 */

public class CheckCustomerSignRepository
{
    private static final String TAG = GetPostRateServiceService.class.getSimpleName();
    private AppCompatActivity mActivity;
    private Fragment mFragment;
    private CheckCustomerSignListener mCallback;
    private ApiInterface mInterface;

    public CheckCustomerSignRepository(AppCompatActivity mActivity, CheckCustomerSignListener listener) {
        this.mActivity = mActivity;
        this.mCallback = listener;
        mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
    }

    public void checkCustomerSign(SaveRatedServiceRequest loginRequest) {
        Log.d(TAG, "getLoginData");
        try {
            mInterface.checkCustomerSignature(loginRequest).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                            mCallback.onCheckSucess(response.body().getStatus());
                        }else{
                            mCallback.onCategoryReceivedFailure(response.body().getStatus());
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                     mCallback.onCategoryReceivedFailure(t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
