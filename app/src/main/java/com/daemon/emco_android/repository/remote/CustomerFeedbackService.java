package com.daemon.emco_android.repository.remote;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.CustomerFeedback;
import com.daemon.emco_android.model.response.CustomerFeedBackResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerFeedbackService {

    private static final String TAG = GetPostRateServiceService.class.getSimpleName();
    private AppCompatActivity mActivity;
    private CustomerFeedback mCallback;
    private ApiInterface mInterface;
    private Call<CustomerFeedBackResponse> customerRemarks;

    public CustomerFeedbackService(AppCompatActivity mActivity,CustomerFeedback listener){

        this.mActivity = mActivity;
        this.mCallback = listener;
        mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);

    }

    public void fetchCustomerFeedback(final String opco,String complaintNo, final CustomerFeedback listener) {
        Log.d(TAG, "saveCustomerFeedback");
        customerRemarks = mInterface.getRatedService(opco,complaintNo);
        try {
            customerRemarks.enqueue(
                    new Callback<CustomerFeedBackResponse>() {
                        @Override
                        public void onResponse(Call<CustomerFeedBackResponse> call, Response<CustomerFeedBackResponse> response) {
                            Log.d(TAG, "onResponse");
                            if (response.isSuccessful()) {
                                CustomerFeedBackResponse mResponse = response.body();
                                if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                                    mCallback.onCustomerFeedbackReceived(mResponse.getObject());

                                } else mCallback.onCustomerFeedbackReceivedError(mResponse.getMessage());
                            } else mCallback.onCustomerFeedbackReceivedError(response.message());
                        }

                        @Override
                        public void onFailure(Call<CustomerFeedBackResponse> call, Throwable t) {
                            Log.d(TAG, "onFailure" + t.getMessage());
                            mCallback.onCustomerFeedbackReceivedError(
                                    mActivity.getString(R.string.msg_request_error_occurred));
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
