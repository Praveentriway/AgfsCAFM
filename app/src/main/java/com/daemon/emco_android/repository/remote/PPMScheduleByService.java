package com.daemon.emco_android.repository.remote;

import android.content.Context;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.model.common.FetchPpmScheduleDocBy;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.PpmScheduleDocByRequest;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PPMScheduleByService {
  private static final String TAG = PPMScheduleByService.class.getSimpleName();

  private Context mContext;
  private Listener listener;

  private ApiInterface mInterface;

  public PPMScheduleByService(Context context, Listener listener) {
    this.mContext = context;
    this.listener = listener;
    mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
  }

  public void getppmData( PpmScheduleDocByRequest ppmScheduleDocByRequest) {
    Log.d(TAG, "GetReceiveComplaintListData");
    Call<FetchPpmScheduleDocBy> getppm = mInterface.fetchPpmScheduleDocByWorkOrderNoAPI(ppmScheduleDocByRequest);

    getppm.enqueue(
        new Callback<FetchPpmScheduleDocBy>() {
          @Override
          public void onResponse(
              Call<FetchPpmScheduleDocBy> call, Response<FetchPpmScheduleDocBy> response) {
            if (response.isSuccessful()) {
              Log.d(TAG, "onResponse success " + response.body().getMessage());
              if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                Log.d(TAG, "onResponse success");
                listener.onppmListReceived(
                    response.body().getPpmScheduleDocBy(),
                    AppUtils.MODE_SERVER);
              } else
                listener.onppmListReceivedError(
                    response.body().getMessage(), AppUtils.MODE_SERVER);
            } else
              listener.onppmListReceivedError(
                  response.message(), AppUtils.MODE_SERVER);
          }

          @Override
          public void onFailure(Call<FetchPpmScheduleDocBy> call, Throwable t) {
            Log.d(TAG, "onFailure" + t.getMessage());
            listener.onppmListReceivedError(
                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
          }
        });
  }

    public interface Listener {
        void onppmListReceived(List<PpmScheduleDocBy> ppmDetailsList, int from);
        void onppmListReceivedError(String errMsg, int from);
    }
}
