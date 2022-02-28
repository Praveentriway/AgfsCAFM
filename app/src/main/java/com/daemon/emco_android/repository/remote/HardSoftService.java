package com.daemon.emco_android.repository.remote;

import android.content.Context;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.restapi.Api;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.HardSoft_Listener;
import com.daemon.emco_android.listeners.ReceivecomplaintList_Listener;
import com.daemon.emco_android.model.common.FetchPpmScheduleDetails;
import com.daemon.emco_android.model.common.PpmScheduleDetails;
import com.daemon.emco_android.model.request.HardSoftRequest;
import com.daemon.emco_android.model.request.ReceiveComplaintViewRequest;
import com.daemon.emco_android.model.response.HardSoftViewResponse;
import com.daemon.emco_android.model.response.ReceiveComplaintResponse;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HardSoftService {
    private static final String TAG = HardSoftService.class.getSimpleName();

    private Context mContext;
    private ReceivecomplaintList_Listener mCallback;
    private HardSoft_Listener mView_listener;
    private ApiInterface mInterface;
    private Listener listener;

    public HardSoftService(
            Context context, ReceivecomplaintList_Listener listener, HardSoft_Listener view_listener) {
        mContext = context;
        mCallback = listener;
        mView_listener = view_listener;
        mInterface = Api.dataClient().create(ApiInterface.class);
    }

    public void getHardSoftServiceComplaintDetails(
            HardSoftRequest hardSoftRequest, int startIndex, String pageType) {
        Log.d(TAG, "GetReceiveComplaintListData");
        Call<ReceiveComplaintResponse> get;
       // get = mInterface.getHardServiceComplaintDetails(hardSoftRequest, startIndex, 10);
        get = mInterface.getHardServiceInspectionDetails(hardSoftRequest, startIndex, 10);
      //  if (pageType.equals(mContext.getString(R.string.title_reactive_soft_services))) {
      //      get = mInterface.getSoftServiceComplaintDetails(hardSoftRequest, startIndex, 10);
     //   } else {  GETHARDSERVICEINSPECTIONDETAILS
      //     get = mInterface.getHardServiceComplaintDetails(hardSoftRequest, startIndex, 10);
       // }
        get.enqueue(
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

    public void getHardSoftServicePPMDetails(
            HardSoftRequest hardSoftRequest, int startIndex, String pageType) {
        Log.d(TAG, "GetReceiveComplaintListData");
        Call<FetchPpmScheduleDetails> get;
        if (pageType.equals(mContext.getString(R.string.title_ppm_request_verification_soft))) {
            get = mInterface.getSoftServicePPMDetails(hardSoftRequest, startIndex, 10);
        } else {
            get = mInterface.getHardServicePPMDetails(hardSoftRequest, startIndex, 10);
        }
        get.enqueue(
                new Callback<FetchPpmScheduleDetails>() {
                    @Override
                    public void onResponse(
                            Call<FetchPpmScheduleDetails> call, Response<FetchPpmScheduleDetails> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
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

    public void getHardSoftServiceComplaintByComplaintId(
            ReceiveComplaintViewRequest hardSoftRequest, String pageType) {
        Log.d(TAG, "GetReceiveComplaintViewData");
        Call<HardSoftViewResponse> get;
        if (pageType.equals(mContext.getString(R.string.title_reactive_soft_services))) {
            get = mInterface.getSoftServiceComplaintByComplaintId(hardSoftRequest);
        } else {
            get = mInterface.getHardServiceComplaintByComplaintId(hardSoftRequest);
        }
        get.enqueue(
                new Callback<HardSoftViewResponse>() {
                    @Override
                    public void onResponse(
                            Call<HardSoftViewResponse> call, Response<HardSoftViewResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success" + response.body().getObject());
                                mView_listener.onHardSoftReceivedSuccess(
                                        response.body().getObject(), AppUtils.MODE_SERVER);
                            } else
                                mView_listener.onHardSoftReceivedFailure(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            mView_listener.onHardSoftReceivedFailure(response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<HardSoftViewResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        mView_listener.onHardSoftReceivedFailure(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }

    public void savedHardServiceReact(HardSoftRequest hardSoftRequest) {
        Log.d(TAG, "GetReceiveComplaintViewData");
        mInterface.savedHardServiceReact(hardSoftRequest)
                .enqueue(
                        new Callback<HardSoftViewResponse>() {
                            @Override
                            public void onResponse(
                                    Call<HardSoftViewResponse> call, Response<HardSoftViewResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                        Log.d(TAG, "onResponse success" + response.body().getObject());
                                        mView_listener.onHardSoftSaveSucess(
                                                response.body().getMessage());
                                    } else
                                        mView_listener.onHardSoftReceivedFailure(
                                                response.body().getMessage(), AppUtils.MODE_SERVER);
                                } else
                                    mView_listener.onHardSoftReceivedFailure(response.message(), AppUtils.MODE_SERVER);
                            }

                            @Override
                            public void onFailure(Call<HardSoftViewResponse> call, Throwable t) {
                                Log.d(TAG, "onFailure" + t.getMessage());
                                mView_listener.onHardSoftReceivedFailure(
                                        mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                            }
                        });
    }

    public void savedSoftServiceReact(HardSoftRequest hardSoftRequest) {
        Log.d(TAG, "GetReceiveComplaintViewData");
        mInterface.savedSoftServiceReact(hardSoftRequest)
                .enqueue(
                        new Callback<HardSoftViewResponse>() {
                            @Override
                            public void onResponse(
                                    Call<HardSoftViewResponse> call, Response<HardSoftViewResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                        Log.d(TAG, "onResponse success" + response.body().getObject());
                                        mView_listener.onHardSoftSaveSucess(
                                                response.body().getMessage());
                                    } else
                                        mView_listener.onHardSoftReceivedFailure(
                                                response.body().getMessage(), AppUtils.MODE_SERVER);
                                } else
                                    mView_listener.onHardSoftReceivedFailure(response.message(), AppUtils.MODE_SERVER);
                            }

                            @Override
                            public void onFailure(Call<HardSoftViewResponse> call, Throwable t) {
                                Log.d(TAG, "onFailure" + t.getMessage());
                                mView_listener.onHardSoftReceivedFailure(
                                        mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                            }
                        });
    }

    public void fetchHardServiceReact(HardSoftRequest hardSoftRequest) {
        Log.d(TAG, "GetReceiveComplaintViewData");
        mInterface.fetchHardServiceReact(hardSoftRequest)
                .enqueue(
                        new Callback<HardSoftViewResponse>() {
                            @Override
                            public void onResponse(
                                    Call<HardSoftViewResponse> call, Response<HardSoftViewResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                        Log.d(TAG, "onResponse success" + response.body().getObject());
                                        mView_listener.onHardSoftReceivedSuccess(
                                                response.body().getObject(), AppUtils.MODE_SERVER);
                                    } else
                                        mView_listener.onHardSoftReceivedFailure(
                                                response.body().getMessage(), AppUtils.MODE_SERVER);
                                } else
                                    mView_listener.onHardSoftReceivedFailure(response.message(), AppUtils.MODE_SERVER);
                            }

                            @Override
                            public void onFailure(Call<HardSoftViewResponse> call, Throwable t) {
                                Log.d(TAG, "onFailure" + t.getMessage());
                                mView_listener.onHardSoftReceivedFailure(
                                        mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                            }
                        });
    }

    public void fetchSoftServiceReact(HardSoftRequest hardSoftRequest) {
        Log.d(TAG, "GetReceiveComplaintViewData");
        mInterface.fetchSoftServiceReact(hardSoftRequest)
                .enqueue(
                        new Callback<HardSoftViewResponse>() {
                            @Override
                            public void onResponse(
                                    Call<HardSoftViewResponse> call, Response<HardSoftViewResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                        Log.d(TAG, "onResponse success" + response.body().getObject());
                                        mView_listener.onHardSoftReceivedSuccess(
                                                response.body().getObject(), AppUtils.MODE_SERVER);
                                    } else
                                        mView_listener.onHardSoftReceivedFailure(
                                                response.body().getMessage(), AppUtils.MODE_SERVER);
                                } else
                                    mView_listener.onHardSoftReceivedFailure(response.message(), AppUtils.MODE_SERVER);
                            }

                            @Override
                            public void onFailure(Call<HardSoftViewResponse> call, Throwable t) {
                                Log.d(TAG, "onFailure" + t.getMessage());
                                mView_listener.onHardSoftReceivedFailure(
                                        mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                            }
                        });
    }

    public void savedSoftServicePpm(HardSoftRequest hardSoftRequest) {
        Log.d(TAG, "GetReceiveComplaintViewData");
        mInterface.savedSoftServicePpm(hardSoftRequest)
                .enqueue(
                        new Callback<HardSoftViewResponse>() {
                            @Override
                            public void onResponse(
                                    Call<HardSoftViewResponse> call, Response<HardSoftViewResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                        Log.d(TAG, "onResponse success" + response.body().getObject());
                                        mView_listener.onHardSoftSaveSucess(
                                                response.body().getMessage());
                                    } else
                                        mView_listener.onHardSoftReceivedFailure(
                                                response.body().getMessage(), AppUtils.MODE_SERVER);
                                } else
                                    mView_listener.onHardSoftReceivedFailure(response.message(), AppUtils.MODE_SERVER);
                            }

                            @Override
                            public void onFailure(Call<HardSoftViewResponse> call, Throwable t) {
                                Log.d(TAG, "onFailure" + t.getMessage());
                                mView_listener.onHardSoftReceivedFailure(
                                        mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                            }
                        });
    }

    public void fetchSoftServicePpm(HardSoftRequest hardSoftRequest) {
        Log.d(TAG, "GetReceiveComplaintViewData");
        mInterface.fetchSoftServicePpm(hardSoftRequest)
                .enqueue(
                        new Callback<HardSoftViewResponse>() {
                            @Override
                            public void onResponse(
                                    Call<HardSoftViewResponse> call, Response<HardSoftViewResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                        Log.d(TAG, "onResponse success" + response.body().getObject());
                                        mView_listener.onHardSoftSaveSucess(
                                                response.body().getMessage());
                                    } else
                                        mView_listener.onHardSoftReceivedFailure(
                                                response.body().getMessage(), AppUtils.MODE_SERVER);
                                } else
                                    mView_listener.onHardSoftReceivedFailure(response.message(), AppUtils.MODE_SERVER);
                            }

                            @Override
                            public void onFailure(Call<HardSoftViewResponse> call, Throwable t) {
                                Log.d(TAG, "onFailure" + t.getMessage());
                                mView_listener.onHardSoftReceivedFailure(
                                        mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                            }
                        });
    }

    public void fetchHardServicePpm(HardSoftRequest hardSoftRequest) {
        Log.d(TAG, "GetReceiveComplaintViewData");
        mInterface.fetchHardServicePpm(hardSoftRequest)
                .enqueue(
                        new Callback<HardSoftViewResponse>() {
                            @Override
                            public void onResponse(
                                    Call<HardSoftViewResponse> call, Response<HardSoftViewResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                        Log.d(TAG, "onResponse success" + response.body().getObject());
                                        mView_listener.onHardSoftSaveSucess(
                                                response.body().getMessage());
                                    } else
                                        mView_listener.onHardSoftReceivedFailure(
                                                response.body().getMessage(), AppUtils.MODE_SERVER);
                                } else
                                    mView_listener.onHardSoftReceivedFailure(response.message(), AppUtils.MODE_SERVER);
                            }

                            @Override
                            public void onFailure(Call<HardSoftViewResponse> call, Throwable t) {
                                Log.d(TAG, "onFailure" + t.getMessage());
                                mView_listener.onHardSoftReceivedFailure(
                                        mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                            }
                        });
    }

    public void savedHardServicePpm(HardSoftRequest hardSoftRequest) {
        Log.d(TAG, "GetReceiveComplaintViewData");
        mInterface.savedHardServicePpm(hardSoftRequest)
                .enqueue(
                        new Callback<HardSoftViewResponse>() {
                            @Override
                            public void onResponse(
                                    Call<HardSoftViewResponse> call, Response<HardSoftViewResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                        Log.d(TAG, "onResponse success" + response.body().getObject());
                                        mView_listener.onHardSoftSaveSucess(
                                                response.body().getMessage());
                                    } else
                                        mView_listener.onHardSoftReceivedFailure(
                                                response.body().getMessage(), AppUtils.MODE_SERVER);
                                } else
                                    mView_listener.onHardSoftReceivedFailure(response.message(), AppUtils.MODE_SERVER);
                            }

                            @Override
                            public void onFailure(Call<HardSoftViewResponse> call, Throwable t) {
                                Log.d(TAG, "onFailure" + t.getMessage());
                                mView_listener.onHardSoftReceivedFailure(
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


