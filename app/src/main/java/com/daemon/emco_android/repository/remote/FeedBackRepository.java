package com.daemon.emco_android.repository.remote;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.db.entity.EmployeeDetailsEntity;
import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntity;
import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntityNew;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.FeedbackListener;
import com.daemon.emco_android.listeners.PendingReasonsListner;
import com.daemon.emco_android.listeners.TechRemarksListener;
import com.daemon.emco_android.model.request.FetchFeedbackRequest;
import com.daemon.emco_android.model.request.PpmfeedbackemployeeReq;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.FeedbackDetailsResponse;
import com.daemon.emco_android.model.response.FeedbackEmployeeDetailsResponse;
import com.daemon.emco_android.model.response.PendingReasonsResponse;
import com.daemon.emco_android.model.response.PpmEmployeeFeedResponse;
import com.daemon.emco_android.model.response.PpmStatusResponse;
import com.daemon.emco_android.model.response.TechnicalRemarksResponse;
import com.daemon.emco_android.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Created by vikram on 19/7/17. */
public class FeedBackRepository {
  private static final String TAG = FeedBackRepository.class.getSimpleName();
  private AppCompatActivity mActivity;
  private FeedbackListener mCallback;
  private ApiInterface mInterface;

  public FeedBackRepository(AppCompatActivity mActivity, FeedbackListener listener) {
    this.mActivity = mActivity;
    this.mCallback = listener;
    mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
  }

  public void getAllFeedbackEmployeeDetailsData() {
    Log.d(TAG, "getFeedbackEmployeeDetailsData");
    try {
      mInterface
          .getAllFeedbackEmployeeDetailsResult()
          .enqueue(
              new Callback<FeedbackEmployeeDetailsResponse>() {
                @Override
                public void onResponse(
                    Call<FeedbackEmployeeDetailsResponse> call,
                    Response<FeedbackEmployeeDetailsResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallback.onFeedbackEmployeeDetailsReceivedSuccess(
                          response.body().getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                          response.body().getMessage(), AppUtils.MODE_SERVER);

                  } else
                    mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<FeedbackEmployeeDetailsResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure");
                  mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                      mActivity.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

    public void getFeedbackEmployeeDetailsData(EmployeeDetailsEntity request) {
        Log.d(TAG, "getFeedbackEmployeeDetailsData");
        try {
            mInterface
                    .getFeedbackEmployeeDetailsResult(request)
                    .enqueue(
                            new Callback<FeedbackEmployeeDetailsResponse>() {
                                @Override
                                public void onResponse(
                                        Call<FeedbackEmployeeDetailsResponse> call,
                                        Response<FeedbackEmployeeDetailsResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                                            mCallback.onFeedbackEmployeeDetailsReceivedSuccess(
                                                    response.body().getObject(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);

                                    } else
                                        mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<FeedbackEmployeeDetailsResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure");
                                    mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                                            mActivity.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getFeedbackEmployeeDetailsDataPPM(PpmfeedbackemployeeReq request) {
        Log.d(TAG, "getFeedbackEmployeeDetailsData"+request.getTeamCode());
        try {
            mInterface
                    .getFeedbackEmployeeDetailsResult(request)
                    .enqueue(
                            new Callback<PpmEmployeeFeedResponse>() {
                                @Override
                                public void onResponse(
                                        Call<PpmEmployeeFeedResponse> call,
                                        Response<PpmEmployeeFeedResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            mCallback.onFeedbackDetailsReceivedSuccessPPMAtten(
                                                    response.body());
                                        } else{
                                            mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                                                    response.message(), AppUtils.MODE_SERVER);
                                        }
                                    }
                                   /* else
                                        mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                                                response.message(), AppUtils.MODE_SERVER);*/
                                }

                                @Override
                                public void onFailure(Call<PpmEmployeeFeedResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure");
                                    mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                                            mActivity.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

  public void postFeedbackDetailsData(SaveFeedbackEntity request) {
    Log.d(TAG, "getFeedbackEmployeeDetailsData");
    try {
      mInterface
          .getSaveFeedbackDetailsResult(request)
          .enqueue(
              new Callback<CommonResponse>() {
                @Override
                public void onResponse(
                    Call<CommonResponse> call, Response<CommonResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallback.onFeedbackEmployeeDetailsSaveSuccess(
                          response.body().getMessage(), AppUtils.MODE_SERVER);
                    } else
                      mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                          response.body().getMessage(), AppUtils.MODE_SERVER);

                  } else
                    mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure");
                  mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                      mActivity.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

    public void postFeedbackDetailsDataNew(SaveFeedbackEntityNew request) {
        Log.d(TAG, "getFeedbackEmployeeDetailsData");
        try {
            mInterface
                    .getSaveFeedbackDetailsResultNew(request)
                    .enqueue(
                            new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(
                                        Call<CommonResponse> call, Response<CommonResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                                            mCallback.onFeedbackEmployeeDetailsSaveSuccess(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);

                                    } else
                                        mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<CommonResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure");
                                    mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                                            mActivity.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getPpmWorkStatus() {
        try {
            mInterface
                    .getPpmWorkStatus()
                    .enqueue(
                            new Callback<PpmStatusResponse>() {
                                @Override
                                public void onResponse(
                                        Call<PpmStatusResponse> call, Response<PpmStatusResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                                            mCallback.onFeedbackPpmStatusSucess(response.body().getObject(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    }
                                }

                                @Override
                                public void onFailure(Call<PpmStatusResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure :::" + t.getMessage());
                                    mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                                            t.getMessage().toString(),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

  public void fetchFeedbackDetailsData(FetchFeedbackRequest request) {
    Log.d(TAG, "fetchFeedbackDetailsData");
    try {
      mInterface
          .getFatchFeedbackDetailsResult(request)
          .enqueue(
              new Callback<FeedbackDetailsResponse>() {
                @Override
                public void onResponse(
                    Call<FeedbackDetailsResponse> call,
                    Response<FeedbackDetailsResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallback.onFeedbackDetailsReceivedSuccess(
                          response.body().getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                          response.body().getMessage(), AppUtils.MODE_SERVER);

                  } else
                    mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<FeedbackDetailsResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure");
                  mCallback.onFeedbackEmployeeDetailsReceivedFailure(
                      mActivity.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getTechnicalRemarksData(final TechRemarksListener listener) {
    Log.d(TAG, "getTechnicalRemarksData");
    Call<TechnicalRemarksResponse> techRemarks = mInterface.getTechRemarksData();
    try {
      techRemarks.enqueue(
          new Callback<TechnicalRemarksResponse>() {
            @Override
            public void onResponse(
                Call<TechnicalRemarksResponse> call, Response<TechnicalRemarksResponse> response) {
              Log.d(TAG, "onResponse");
              if (response.isSuccessful()) {
                TechnicalRemarksResponse techRemarksResponse = response.body();
                if (techRemarksResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                  listener.onTechRemarksReceived(techRemarksResponse.getObject());
                } else listener.onechRemarksReceivedError(techRemarksResponse.getMessage());
              } else listener.onechRemarksReceivedError(response.message());
            }

            @Override
            public void onFailure(Call<TechnicalRemarksResponse> call, Throwable t) {
              Log.d(TAG, "onFailure" + t.getMessage());
              listener.onechRemarksReceivedError(
                  mActivity.getString(R.string.msg_request_error_occurred));
            }
          });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }



    public void getPendingReasons(final PendingReasonsListner listener) {
        Log.d(TAG, "getNatureDescription");
        Call<PendingReasonsResponse> getPendingReasons = mInterface.getPendingReasons();

        getPendingReasons.enqueue(
                new Callback<PendingReasonsResponse>() {
                    @Override
                    public void onResponse(
                            Call<PendingReasonsResponse> call, Response<PendingReasonsResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onPendingReasonsReceived(
                                        response.body().getObject()
                                );
                            } else
                                listener.onPendingReasonsReceivedError(
                                        mActivity.getString(R.string.msg_request_error_occurred));
                        } else
                            listener.onPendingReasonsReceivedError(
                                    mActivity.getString(R.string.msg_request_error_occurred));
                    }

                    @Override
                    public void onFailure(Call<PendingReasonsResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onPendingReasonsReceivedError(
                                mActivity.getString(R.string.msg_request_error_occurred));
                    }
                });
    }


}
