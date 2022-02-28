package com.daemon.emco_android.repository.remote;

import android.content.Context;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.ReceivecomplaintList_Listener;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.model.request.ReceiveComplainCCRequest;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.ReceiveComplaintResponse;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Created by vikram on 17/7/17. */
public class ReceiveComplaintListService {
  private static final String TAG = ReceiveComplaintListService.class.getSimpleName();

  private Context mContext;
  private ReceivecomplaintList_Listener mCallback;

  private ApiInterface mInterface;

  public ReceiveComplaintListService(Context context, ReceivecomplaintList_Listener listener) {
    this.mContext = context;
    this.mCallback = listener;
    mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
  }

  public void GetReceiveComplaintListData(
      String strEmpid, String status, int startIndex, String pageType) {
    Log.d(TAG, "GetReceiveComplaintListData");
    Call<ReceiveComplaintResponse> getReceivecomplaint;
    if (pageType.equals(AppUtils.ARGS_RECEIVECOMPLAINT_PAGE)) {
        Log.d("Bug","Bug_Track_2"+strEmpid + ","+status + ","+ startIndex);
      getReceivecomplaint =
          mInterface.getReceiveComplaintListResult(strEmpid, status, startIndex, 10);
    } else {
        Log.d("Bug","Bug_Track_3");
      getReceivecomplaint = mInterface.getReceiveComplaintUnSignedResult(strEmpid, startIndex, 10);
    }
    getReceivecomplaint.enqueue(
        new Callback<ReceiveComplaintResponse>() {
          @Override
          public void onResponse(
              Call<ReceiveComplaintResponse> call, Response<ReceiveComplaintResponse> response) {
            if (response.isSuccessful()) {
              Log.d(TAG, "onResponse success " + response.body().getMessage());
              if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                Log.d(TAG, "onResponse success");
                mCallback.onReceiveComplaintListReceived(
                    response.body().getReceiveComplaintItem(),
                    response.body().getTotalNumberOfRows(),
                    AppUtils.MODE_SERVER);
              } else
                mCallback.onReceiveComplaintListReceivedError(
                    response.body().getMessage(), AppUtils.MODE_SERVER);
            } else
              mCallback.onReceiveComplaintListReceivedError(
                  response.message(), AppUtils.MODE_SERVER);
          }

          @Override
          public void onFailure(Call<ReceiveComplaintResponse> call, Throwable t) {
            Log.d(TAG, "onFailure" + t.getMessage());
            mCallback.onReceiveComplaintListReceivedError(
                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
          }
        });
  }

  public void PostReceiveComplaintCCData(List<ReceiveComplainCCRequest> receiveComplainCCRequest) {
    Log.d(TAG, "PostReceiveComplaintCCCData");
    try {
      mInterface
          .getReceiveComplaintCCCResult(receiveComplainCCRequest)
          .enqueue(
              new Callback<CommonResponse>() {
                @Override
                public void onResponse(
                    Call<CommonResponse> call, Response<CommonResponse> response) {
                  if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                      Log.d(TAG, "onResponse success");
                      mCallback.onReceiveComplaintCCReceived(
                          response.body().getMessage(), AppUtils.MODE_SERVER);
                    } else
                      mCallback.onReceiveComplaintListReceivedError(
                          response.body().getMessage(), AppUtils.MODE_SERVER);
                  } else
                    mCallback.onReceiveComplaintListReceivedError(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure" + t.getMessage());
                  mCallback.onReceiveComplaintListReceivedError(
                      mContext.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void getallforwardedcomplaintbyemployeeid(String strEmpid) {
    Log.d(TAG, "GetReceiveComplaintListData");
    try {
      mInterface
          .getallforwardedcomplaintbyemployeeid(new EmployeeIdRequest(strEmpid, null, null,null))
          .enqueue(
              new Callback<CommonResponse>() {
                @Override
                public void onResponse(
                    Call<CommonResponse> call, Response<CommonResponse> response) {
                  if (response.isSuccessful()) {

                     Log.d("first", "onResponse success " + response.body().getObject().getNewComplaint());
                      Log.d("first", "onResponse success o " + response.body().getObject().getOldComplaint());
                    Log.d(TAG, "onResponse success " + response.body().toString());
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                        Log.d("firstchk", "onResponse success " + response.body().getObject());
                      // mCallback.onReceiveComplaintListReceived(null,response.body().getTotalNumberOfRows(), AppUtils.MODE_SERVER);
                      //  mCallback.onReceiveComplaintListReceivedUpdate(null,response.body().getObject().getNewComplaint(),response.body().getObject().getOldComplaint(), AppUtils.MODE_SERVER);
                        mCallback.onReceiveComplaintListReceivedUpdate(null,response.body().getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallback.onReceiveComplaintListReceivedError(
                          response.body().getMessage(), AppUtils.MODE_SERVER);
                  } else
                    mCallback.onReceiveComplaintListReceivedError(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure" + t.getMessage());
                  mCallback.onReceiveComplaintListReceivedError(
                      mContext.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
