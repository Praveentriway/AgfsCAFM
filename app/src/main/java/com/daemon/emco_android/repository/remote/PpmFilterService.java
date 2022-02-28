package com.daemon.emco_android.repository.remote;

import android.content.Context;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.model.response.ContractDetailResponse;
import com.daemon.emco_android.model.response.ContractDetails;
import com.daemon.emco_android.model.response.NatureDescResponse;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PpmFilterService  {

    private static final String TAG = PpmFilterService.class.getSimpleName();

    private Context mContext;
    private Listener listener;

    private ApiInterface mInterface;

    public PpmFilterService(Context context, Listener listener) {
        this.mContext = context;
        this.listener = listener;
        mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
    }

    public void getContractDetails(
            String strEmpid,String type) {
        Log.d(TAG, "getContractDetails");
        Call<ContractDetailResponse> getcontract = mInterface.getContractDetails(strEmpid,type);

        getcontract.enqueue(
                new Callback<ContractDetailResponse>() {
                    @Override
                    public void onResponse(
                            Call<ContractDetailResponse> call, Response<ContractDetailResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onContractDetailsReceived(
                                        response.body().getObject()
                                      );
                            } else
                                listener.onContractDetailsReceivedError(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onContractDetailsReceivedError(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<ContractDetailResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onContractDetailsReceivedError(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }



    public void getNatureDescription(String opco,String jobno) {
        Log.d(TAG, "getNatureDescription");
        Call<NatureDescResponse> getNaturedesc = mInterface.getNatureDescription(opco,jobno);

        getNaturedesc.enqueue(
                new Callback<NatureDescResponse>() {
                    @Override
                    public void onResponse(
                            Call<NatureDescResponse> call, Response<NatureDescResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onNatureDescReceived(
                                        response.body().getObject()
                                );
                            } else
                                listener.onNatureDescReceivedError(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onNatureDescReceivedError(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<NatureDescResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onNatureDescReceivedError(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }


    public interface Listener {

        void onContractDetailsReceived(List<ContractDetails> contractDetails);
        void onContractDetailsReceivedError(String errMsg,int from);

        void onNatureDescReceived(List<String> natureDesc);
        void onNatureDescReceivedError(String errMsg,int from);
    }

}
