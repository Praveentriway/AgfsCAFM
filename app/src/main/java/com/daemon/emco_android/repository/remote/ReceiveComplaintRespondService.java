package com.daemon.emco_android.repository.remote;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.DefectDoneImage_Listener;
import com.daemon.emco_android.listeners.RespondComplaintListener;
import com.daemon.emco_android.model.request.RCDownloadImageRequest;
import com.daemon.emco_android.model.request.WorkDefectRequest;
import com.daemon.emco_android.model.request.WorkDoneRequest;
import com.daemon.emco_android.model.response.RCImageDownloadResponse;
import com.daemon.emco_android.model.response.RCImageUploadResponse;
import com.daemon.emco_android.model.response.RCRespondResponse;
import com.daemon.emco_android.model.response.WorkDefectResponse;
import com.daemon.emco_android.model.response.WorkDoneResponse;
import com.daemon.emco_android.model.response.WorkPendingReasonResponse;
import com.daemon.emco_android.model.response.WorkStatusResponse;
import com.daemon.emco_android.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Created by vikram on 19/7/17. */
public class ReceiveComplaintRespondService {
  private static final String TAG = ReceiveComplaintRespondService.class.getSimpleName();
  private AppCompatActivity mActivity;

  private Context mContext;
  private RespondComplaintListener mCallback;
  private DefectDoneImage_Listener mCallbackImages;
  private ApiInterface mInterface;

  public ReceiveComplaintRespondService(AppCompatActivity mActivity) {
    this.mActivity = mActivity;
    this.mContext = mActivity;
    mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
  }

  public void setmCallback(RespondComplaintListener mCallback) {
    this.mCallback = mCallback;
  }

  public void getWorkStatusData() {
    Log.d(TAG, "getWorkStatusData");
    try {
      mInterface
          .getWorkStatusData()
          .enqueue(
              new Callback<WorkStatusResponse>() {
                @Override
                public void onResponse(
                    Call<WorkStatusResponse> call, Response<WorkStatusResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    WorkStatusResponse mResponse = response.body();
                    if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallback.onWorkStatusReceivedSuccess(
                          mResponse.getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallback.onComplaintRespondReceivedFailure(
                          mResponse.getMessage(), AppUtils.MODE_SERVER);
                  } else
                    mCallback.onComplaintRespondReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<WorkStatusResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure :::" + t.getMessage());
                  mCallback.onComplaintRespondReceivedFailure(
                      mContext.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getAllWorkDefectData() {
    Log.d(TAG, "getWorkDefectData");
    try {
      mInterface
          .getAllWorkDefectsData()
          .enqueue(
              new Callback<WorkDefectResponse>() {
                @Override
                public void onResponse(
                    Call<WorkDefectResponse> call, Response<WorkDefectResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    WorkDefectResponse mResponse = response.body();
                    if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallback.onWorkDefectReceived(mResponse.getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallback.onComplaintRespondReceivedFailure(
                          mResponse.getMessage(), AppUtils.MODE_SERVER);
                  } else
                    mCallback.onComplaintRespondReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<WorkDefectResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure :::" + t.getMessage());
                  mCallback.onComplaintRespondReceivedFailure(
                      mContext.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getWorkDefectData(WorkDefectRequest workDefectRequest) {
    Log.d(TAG, "getWorkDefectData");
    try {
      mInterface
          .getWorkDefectsData(workDefectRequest)
          .enqueue(
              new Callback<WorkDefectResponse>() {
                @Override
                public void onResponse(
                    Call<WorkDefectResponse> call, Response<WorkDefectResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    WorkDefectResponse mResponse = response.body();
                    if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallback.onWorkDefectReceived(mResponse.getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallback.onComplaintRespondReceivedFailure(
                          mResponse.getMessage(), AppUtils.MODE_SERVER);
                  } else
                    mCallback.onComplaintRespondReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<WorkDefectResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure :::" + t.getMessage());
                  mCallback.onComplaintRespondReceivedFailure(
                      mContext.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getAllWorkDoneData() {
    Log.d(TAG, "getWorkDoneData");
    try {
      mInterface
          .getAllWorkDoneData()
          .enqueue(
              new Callback<WorkDoneResponse>() {
                @Override
                public void onResponse(
                    Call<WorkDoneResponse> call, Response<WorkDoneResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    WorkDoneResponse mResponse = response.body();
                    if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallback.onWorkDoneReceivedSuccess(
                          mResponse.getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallback.onComplaintRespondReceivedFailure(
                          mResponse.getMessage(), AppUtils.MODE_SERVER);
                  } else
                    mCallback.onComplaintRespondReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<WorkDoneResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure :::" + t.getMessage());
                  mCallback.onComplaintRespondReceivedFailure(
                      mContext.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getWorkDoneData(WorkDoneRequest workDoneRequest) {
    Log.d(TAG, "getWorkDoneData");
    try {
      mInterface
          .getWorkDoneData(workDoneRequest)
          .enqueue(
              new Callback<WorkDoneResponse>() {
                @Override
                public void onResponse(
                    Call<WorkDoneResponse> call, Response<WorkDoneResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    WorkDoneResponse mResponse = response.body();
                    if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallback.onWorkDoneReceivedSuccess(
                          mResponse.getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallback.onComplaintRespondReceivedFailure(
                          mResponse.getMessage(), AppUtils.MODE_SERVER);
                  } else
                    mCallback.onComplaintRespondReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<WorkDoneResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure :::" + t.getMessage());
                  mCallback.onComplaintRespondReceivedFailure(
                      mContext.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getWorkPendingReasonData() {
    Log.d(TAG, "getWorkPendingReasonData");
    try {
      mInterface
          .getWorkPendingReasonData()
          .enqueue(
              new Callback<WorkPendingReasonResponse>() {
                @Override
                public void onResponse(
                    Call<WorkPendingReasonResponse> call,
                    Response<WorkPendingReasonResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    WorkPendingReasonResponse mResponse = response.body();
                    if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallback.onWorkPendingReasonReceivedSuccess(
                          mResponse.getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallback.onComplaintRespondReceivedFailure(
                          mResponse.getMessage(), AppUtils.MODE_SERVER);
                  } else
                    mCallback.onComplaintRespondReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<WorkPendingReasonResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure :::" + t.getMessage());
                  mCallback.onComplaintRespondReceivedFailure(
                      mContext.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getComplaintRespondSaveData(ReceiveComplaintViewEntity saveReceivedComplaintRequest) {
    Log.d(TAG, "getComplaintRespondSaveData");
    try {
      mInterface
          .getReceiveComplaintRespondSaveResult(saveReceivedComplaintRequest)
          .enqueue(
              new Callback<RCRespondResponse>() {
                @Override
                public void onResponse(
                    Call<RCRespondResponse> call, Response<RCRespondResponse> response) {
                  Log.d(TAG, "onResponse");
                  try {
                    if (mCallback != null) {
                      if (response.isSuccessful()) {
                        if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                          mCallback.onComplaintRespondSaveReceived(
                              response.body().getObject(), AppUtils.MODE_SERVER);
                        } else
                          mCallback.onComplaintRespondReceivedFailure(
                              response.body().getMessage(), AppUtils.MODE_SERVER);
                      } else
                        mCallback.onComplaintRespondReceivedFailure(
                            response.message(), AppUtils.MODE_SERVER);
                    }
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }

                @Override
                public void onFailure(Call<RCRespondResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure :::" + t.getMessage());
                  try {
                    if (mCallback != null)
                      mCallback.onComplaintRespondReceivedFailure(
                          mContext.getString(R.string.msg_request_error_occurred),
                          AppUtils.MODE_SERVER);
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setmCallbackImages(DefectDoneImage_Listener mCallbackImages) {
    this.mCallbackImages = mCallbackImages;
  }

  public void saveComplaintRespondImageData(DFoundWDoneImageEntity imageEntity, Context context) {
    Log.d(TAG, "saveComplaintRespondImageData getClientLongTime");
    try {
      mInterface = ApiClient.getClientLongTime(context).create(ApiInterface.class);

      mInterface
          .saveRCRespondImageResult(imageEntity)
          .enqueue(
              new Callback<RCImageUploadResponse>() {
                @Override
                public void onResponse(
                    Call<RCImageUploadResponse> call, Response<RCImageUploadResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallbackImages.onImageSaveReceivedSuccess(
                          response.body().getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallbackImages.onImageSaveReceivedFailure(
                          response.body().getMessage(), AppUtils.MODE_SERVER);
                  } else
                    mCallbackImages.onImageSaveReceivedFailure(
                        response.message(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<RCImageUploadResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure :::" + t.getMessage());
                  mCallbackImages.onImageSaveReceivedFailure(
                      mContext.getString(R.string.msg_request_error_occurred),
                      AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


    public void saveComplaintRespondImageData1(DFoundWDoneImageEntity imageEntity, Context context) {
        Log.d(TAG, "saveComplaintRespondImageData getClientLongTime");
        try {
            mInterface = ApiClient.getClientLongTime(context).create(ApiInterface.class);

            mInterface
                    .saveRCRespondImageResult1(imageEntity)
                    .enqueue(
                            new Callback<RCImageDownloadResponse>() {
                                @Override
                                public void onResponse(
                                        Call<RCImageDownloadResponse> call, Response<RCImageDownloadResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                                            mCallbackImages.onImageSaveReceivedSuccess1(
                                                    response.body().getObject(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallbackImages.onImageSaveReceivedFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else
                                        mCallbackImages.onImageSaveReceivedFailure(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<RCImageDownloadResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure :::" + t.getMessage());
                                    mCallbackImages.onImageSaveReceivedFailure(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }










    public void getRespondImage(final RCDownloadImageRequest imageRequest, Context context) {
    Log.d(TAG, "getRespondImage getClientLongTime");
    try {
      mInterface = ApiClient.getClientLongTime(context).create(ApiInterface.class);

      mInterface
          .getRespondImageResult(imageRequest)
          .enqueue(
              new Callback<RCImageDownloadResponse>() {
                @Override
                public void onResponse(
                    Call<RCImageDownloadResponse> call,
                    Response<RCImageDownloadResponse> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                      mCallbackImages.onImageReceivedSuccess(
                          response.body().getObject(), AppUtils.MODE_SERVER);
                    } else
                      mCallbackImages.onImageReceivedFailure(
                          imageRequest.getDocType(), AppUtils.MODE_SERVER);
                  } else
                    mCallbackImages.onImageReceivedFailure(
                        imageRequest.getDocType(), AppUtils.MODE_SERVER);
                }

                @Override
                public void onFailure(Call<RCImageDownloadResponse> call, Throwable t) {
                  Log.d(TAG, "onFailure :::" + t.getMessage());
                  mCallbackImages.onImageReceivedFailure(
                      imageRequest.getDocType(), AppUtils.MODE_SERVER);
                }
              });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
