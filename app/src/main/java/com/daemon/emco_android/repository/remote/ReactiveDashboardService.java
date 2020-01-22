package com.daemon.emco_android.repository.remote;

import android.content.Context;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.ReactiveDashboard_Listener;
import com.daemon.emco_android.model.request.MultiSearchRequest;
import com.daemon.emco_android.model.response.DashboardPieResponse;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vikram on 17/7/17.
 */

public class ReactiveDashboardService {
    private static final String TAG = ReactiveDashboardService.class.getSimpleName();

    private Context mContext;
    private Fragment mFragment;
    private ReactiveDashboard_Listener mCallback;
    private DashboardPieResponse mResponse;
    private ApiInterface mInterface;

    public ReactiveDashboardService(Context context, Fragment fragment, ReactiveDashboard_Listener listener) {
        this.mContext = context;
        this.mFragment = fragment;
        this.mCallback = listener;
        mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
    }

    public void GetDashboardData(MultiSearchRequest pieChartRequest) {
        Log.d(TAG, "GetDashboardData");

        Call<DashboardPieResponse> callGetDashboardData = mInterface.GetDashboardData(pieChartRequest);

        callGetDashboardData.enqueue(new Callback<DashboardPieResponse>() {
            @Override
            public void onResponse(Call<DashboardPieResponse> call, Response<DashboardPieResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                        Log.d(TAG, "onResponse success");
                        mResponse = response.body();
                        mCallback.onDashboardDataReceivedSuccess(mResponse.getObject(), AppUtils.MODE_SERVER);

                    } else
                        mCallback.onDashboardDataReceivedFailure(response.body().getMessage(), AppUtils.MODE_SERVER);
                } else
                    mCallback.onDashboardDataReceivedFailure(response.message(), AppUtils.MODE_SERVER);
            }

            @Override
            public void onFailure(Call<DashboardPieResponse> call, Throwable t) {
                Log.d(TAG, "onFailure"+t.getMessage());
                mCallback.onDashboardDataReceivedFailure(mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
            }
        });
    }

}
