package com.daemon.emco_android.repository.remote;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.LogComplaint_Listener;
import com.daemon.emco_android.repository.db.entity.LogComplaintEntity;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.model.response.LogComplaintResponse;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vikram on 18/7/17.
 */

public class PostLogComplaintService {
    private static final String TAG = PostLogComplaintService.class.getSimpleName();
    private Context mContext;
    private AppCompatActivity mActivity;
    private LogComplaint_Listener mCallback;
    private ApiInterface mInterface;
    private Call<LogComplaintResponse> postLogComplaintData;

    public PostLogComplaintService(AppCompatActivity mActivity, LogComplaint_Listener mCallback) {
        this.mActivity = mActivity;
        this.mContext = mActivity;
        this.mCallback = mCallback;
        mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
    }

    public void postLogComplaintData(final LogComplaintEntity logComplaintRequest) {
        Log.d(TAG, "postLogComplaintData");
        try {
            postLogComplaintData = mInterface.getLogComplaintResult(logComplaintRequest);
            postLogComplaintData.enqueue(new Callback<LogComplaintResponse>() {
                @Override
                public void onResponse(Call<LogComplaintResponse> call, Response<LogComplaintResponse> response) {
                    Log.d(TAG, "onResponse");
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                            mCallback.onLogComplaintDataReceivedSuccess(new EmployeeIdRequest(null,null,logComplaintRequest.getComplainWebNumber(),response.body().getWebNumber().getComplainWebNumber()), AppUtils.MODE_SERVER);
                        } else
                            mCallback.onLogComplaintDataReceivedFailure(response.body().getMessage(), AppUtils.MODE_SERVER);
                    } else mCallback.onLogComplaintDataReceivedFailure(response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<LogComplaintResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure :" + t.getMessage());
                    mCallback.onLogComplaintDataReceivedFailure(mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }
}
