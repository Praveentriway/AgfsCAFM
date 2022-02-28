package com.daemon.emco_android.repository.remote;

import android.content.Context;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.model.common.FetchPpmScheduleDetails;
import com.daemon.emco_android.model.common.PpmScheduleDetails;
import com.daemon.emco_android.model.request.FetchPpmScheduleDetailsRequest;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PPMScheduleDetailsService {
  private static final String TAG = PPMScheduleDetailsService.class.getSimpleName();

  private Context mContext;
  private Listener listener;

  private ApiInterface mInterface;

  public PPMScheduleDetailsService(Context context, Listener listener) {
    this.mContext = context;
    this.listener = listener;
    mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
  }

  public void getppmListData( int startIndex, FetchPpmScheduleDetailsRequest fetchPpmScheduleDetailsRequest) {
    Log.d(TAG, "GetReceiveComplaintListData");
    Call<FetchPpmScheduleDetails> getppm = mInterface.fetchPpmScheduleDetails(startIndex, 10,fetchPpmScheduleDetailsRequest);

    getppm.enqueue(
        new Callback<FetchPpmScheduleDetails>() {
          @Override
          public void onResponse(
              Call<FetchPpmScheduleDetails> call, Response<FetchPpmScheduleDetails> response) {
            if (response.isSuccessful()) {
              Log.d(TAG, "onResponse success " + response.body().getMessage());
              if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                Log.d(TAG, "onResponse success");
                listener.onppmListReceived(
                    response.body().getPpmScheduleDetails(),
                    response.body().getTotalNumberOfRows(),
                    AppUtils.MODE_SERVER);
              } else
                listener.onppmListReceivedError(
                    response.body().getMessage(), AppUtils.MODE_SERVER);
            } else
              listener.onppmListReceivedError(
                  response.message(), AppUtils.MODE_SERVER);
          }

          @Override
          public void onFailure(Call<FetchPpmScheduleDetails> call, Throwable t) {
            Log.d(TAG, "onFailure" + t.getMessage());
            listener.onppmListReceivedError(
                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
          }
        });
  }

    public void getIMppmListData( int startIndex, FetchPpmScheduleDetailsRequest fetchPpmScheduleDetailsRequest,String pageType) {
        Log.d(TAG, "GetReceiveComplaintListData");
        Call<FetchPpmScheduleDetails> getppm
                = mInterface.fetchPpmScheduleDetails(startIndex, 10,fetchPpmScheduleDetailsRequest);
        if (pageType.equals(mContext.getString(R.string.title_ppm_request_verification_soft))) {
           // getppm = mInterface.getSoftServicePPMDetails(fetchPpmScheduleDetailsRequest, startIndex, 10);
        } else {
           // getppm = mInterface.getHardServicePPMDetails(fetchPpmScheduleDetailsRequest, startIndex, 10);
        }
        getppm.enqueue(
                new Callback<FetchPpmScheduleDetails>() {
                    @Override
                    public void onResponse(
                            Call<FetchPpmScheduleDetails> call, Response<FetchPpmScheduleDetails> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onppmListReceived(
                                        response.body().getPpmScheduleDetails(),
                                        response.body().getTotalNumberOfRows(),
                                        AppUtils.MODE_SERVER);
                            } else
                                listener.onppmListReceivedError(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onppmListReceivedError(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<FetchPpmScheduleDetails> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onppmListReceivedError(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }

    public interface Listener {
        void onppmListItemClicked(List<PpmScheduleDetails> ppmDetailsList, int position);

        void onppmListReceived(List<PpmScheduleDetails> ppmDetailsList, String noOfRows, int from);
        void onppmListReceivedError(String errMsg, int from);
    }
}
