package com.daemon.emco_android.repository.remote;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.db.entity.SaveRatedServiceEntity;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.RateAndShareListener;
import com.daemon.emco_android.listeners.RatedServiceListener;
import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.CustomerRemarksResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Created by vikram on 19/7/17. */
public class GetPostRateServiceService {
  private static final String TAG = GetPostRateServiceService.class.getSimpleName();
  private AppCompatActivity mActivity;
  private RateAndShareListener mCallback;
  private ApiInterface mInterface;
  private Call<CustomerRemarksResponse> customerRemarks;
  private Call<CommonResponse> saveCustomerFeedback;

  public GetPostRateServiceService(
      AppCompatActivity mActivity, RateAndShareListener listener) {
    this.mActivity = mActivity;
    this.mCallback = listener;
    mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
  }

  public void getCustomerRemarksData() {
    Log.d(TAG, "getCustomerRemarksData");
    customerRemarks = mInterface.getCustomerRemarksData();
    try {
      customerRemarks.enqueue(
          new Callback<CustomerRemarksResponse>() {
            @Override
            public void onResponse(
                Call<CustomerRemarksResponse> call, Response<CustomerRemarksResponse> response) {
              Log.d(TAG, "onResponse");
              if (response.isSuccessful()) {
                CustomerRemarksResponse customerRemarksResponse = response.body();
                if (customerRemarksResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                  mCallback.onCustomerRemarksReceived(customerRemarksResponse.getObject());
                } else mCallback.onRateShareReceivedError(customerRemarksResponse.getMessage());
              } else mCallback.onRateShareReceivedError(response.message());
            }

            @Override
            public void onFailure(Call<CustomerRemarksResponse> call, Throwable t) {
              Log.d(TAG, "onFailure" + t.getMessage());
              mCallback.onRateShareReceivedError(
                  mActivity.getString(R.string.msg_request_error_occurred));
            }
          });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void saveCustomerFeedbackRM(final SaveRatedServiceEntity request, final RatedServiceListener listener) {
    Log.d(TAG, "saveCustomerFeedback");
    saveCustomerFeedback = mInterface.saveRatedServiceRM(request);
    try {
      saveCustomerFeedback.enqueue(
          new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
              Log.d(TAG, "onResponse");
              if (response.isSuccessful()) {
                CommonResponse mResponse = response.body();
                if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                  mCallback.onSaveRateShareReceived(mResponse.getMessage());
                  listener.onRatedServiceReceived(request);
                } else mCallback.onRateShareReceivedError(mResponse.getMessage());
              } else mCallback.onRateShareReceivedError(response.message());
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
              Log.d(TAG, "onFailure" + t.getMessage());
              mCallback.onRateShareReceivedError(
                  mActivity.getString(R.string.msg_request_error_occurred));
            }
          });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void saveCustomerFeedback(final SaveRatedServiceRequest request) {
    Log.d(TAG, "saveCustomerFeedback");
    saveCustomerFeedback = mInterface.saveRatedService(request);
    try {
      saveCustomerFeedback.enqueue(
          new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
              Log.d(TAG, "onResponse");
              if (response.isSuccessful()) {
                CommonResponse mResponse = response.body();
                if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                  mCallback.onSaveRateShareReceived(mResponse.getMessage());
                } else mCallback.onRateShareReceivedError(mResponse.getMessage());
              } else mCallback.onRateShareReceivedError(response.message());
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
              Log.d(TAG, "onFailure" + t.getMessage());
              mCallback.onRateShareReceivedError(
                  mActivity.getString(R.string.msg_request_error_occurred));
            }
          });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void fetchCustomerFeedback(final SaveRatedServiceRequest request) {
    Log.d(TAG, "saveCustomerFeedback");
    saveCustomerFeedback = mInterface.fetchRatedService(request);
    try {
      saveCustomerFeedback.enqueue(
          new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
              Log.d(TAG, "onResponse");
              if (response.isSuccessful()) {
                /* CommonResponse mResponse = response.body();
                if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                    mCallback.onSaveRateShareReceived(mResponse.getMessage());
                } else mCallback.onRateShareReceivedError(mResponse.getMessage());*/
              } else mCallback.onRateShareReceivedError(response.message());
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
              Log.d(TAG, "onFailure" + t.getMessage());
              mCallback.onRateShareReceivedError(
                  mActivity.getString(R.string.msg_request_error_occurred));
            }
          });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
