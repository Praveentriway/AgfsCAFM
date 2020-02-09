package com.daemon.emco_android.repository.remote;

import android.content.Context;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.model.common.PPMDetails;
import com.daemon.emco_android.model.response.PPMDetailsResponse;
import com.daemon.emco_android.model.request.PPMCCCReq;
import com.daemon.emco_android.model.request.PPMFilterRequest;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PPMDetailsService {
  private static final String TAG = PPMDetailsService.class.getSimpleName();

  private Context mContext;
  private Listener listener;

  private ApiInterface mInterface;

  public PPMDetailsService(Context context, Listener listener) {
    this.mContext = context;
    this.listener = listener;
    mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
  }

  public void getppmListData(String strEmpid, int startIndex,PPMFilterRequest ppmFilterRequest) {
    Log.d(TAG, "GetReceiveComplaintListData");
    Call<PPMDetailsResponse> getppm = mInterface.getppmdetailsResult(strEmpid, startIndex, 10, ppmFilterRequest);

    getppm.enqueue(
        new Callback<PPMDetailsResponse>() {
          @Override
          public void onResponse(
              Call<PPMDetailsResponse> call, Response<PPMDetailsResponse> response) {
            if (response.isSuccessful()) {
              Log.d(TAG, "onResponse success " + response.body().getMessage());
              if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                Log.d(TAG, "onResponse success");
                listener.onppmListReceived(
                    response.body().getPPMDetails(),
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
          public void onFailure(Call<PPMDetailsResponse> call, Throwable t) {
            Log.d(TAG, "onFailure" + t.getMessage());
            listener.onppmListReceivedError(
                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
          }
        });
  }

    public void postppmCCCData(List<PPMCCCReq> receiveComplainCCRequest) {

      // to resign it been called

        Log.d(TAG, "PostReceiveComplaintCCCData");
        try {
            mInterface
                    .getPPMCCCResult(receiveComplainCCRequest)
                    .enqueue(
                            new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(
                                        Call<CommonResponse> call, Response<CommonResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse success");
                                            listener.onppmCCCReceived(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                        } else
                                            listener.onppmListReceivedError(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else
                                        listener.onppmListReceivedError(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<CommonResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    listener.onppmListReceivedError(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPPMSearchData(List<PPMCCCReq> receiveComplainCCRequest) {
        Log.d(TAG, "PostReceiveComplaintCCCData");
        try {
            mInterface
                    .getPPMCCCResult(receiveComplainCCRequest)
                    .enqueue(
                            new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(
                                        Call<CommonResponse> call, Response<CommonResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse success");
                                            listener.onppmCCCReceived(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                        } else
                                            listener.onppmListReceivedError(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else
                                        listener.onppmListReceivedError(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<CommonResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    listener.onppmListReceivedError(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public interface Listener {
        void onppmListItemClicked(List<PPMDetails> ppmDetailsList, int position);
        void onppmListItemChecked(List<PPMDetails> data);

        void onppmCCCReceived(String strMsg,int from);
        void onppmListReceived(List<PPMDetails> ppmDetailsList, String noOfRows, int from);
        void onppmListReceivedError(String errMsg,int from);
    }
}
