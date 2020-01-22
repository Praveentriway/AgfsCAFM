package com.daemon.emco_android.repository.remote;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.model.request.DocumentTransaction;
import com.daemon.emco_android.model.response.DocumentDownloadResponse;
import com.daemon.emco_android.model.response.DownloadDoc;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import  com.daemon.emco_android.model.response.CommonResponse;

import java.util.ArrayList;

public class DocumentUploadRepository {

    private static final String TAG = DocumentUploadRepository.class.getSimpleName();
    private AppCompatActivity mActivity;

    private Context mContext;
    private ImageListner mCallback;
    private ApiInterface mInterface;

    public DocumentUploadRepository(AppCompatActivity mActivity,ImageListner mCallback) {
        this.mActivity = mActivity;
        this.mContext = mActivity;
        this.mCallback=mCallback;
        mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
    }


    public void saveDocumentData(DocumentTransaction trans, Context context) {

        try {
            mInterface = ApiClient.getClientLongTime(context).create(ApiInterface.class);

            mInterface
                    .saveDocument(trans)
                    .enqueue(
                            new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(
                                        Call<CommonResponse> call, Response<CommonResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                                            mCallback.onDocSaveReceivedSuccess(
                                                    response.body(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onDocSaveReceivedFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else
                                        mCallback.onDocSaveReceivedFailure(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<CommonResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure :::" + t.getMessage());
                                    mCallback.onDocSaveReceivedFailure(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void getDocumentData(DocumentTransaction trans, Context context) {

        try {
            mInterface = ApiClient.getClientLongTime(context).create(ApiInterface.class);

            mInterface
                    .getDocumentResult(trans)
                    .enqueue(
                            new Callback<DocumentDownloadResponse>() {
                                @Override
                                public void onResponse(
                                        Call<DocumentDownloadResponse> call, Response<DocumentDownloadResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                                            mCallback.onDocReceivedSuccess(
                                                    response.body().getDocs(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallback.onDocReceivedFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);
                                    } else
                                        mCallback.onDocReceivedFailure(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<DocumentDownloadResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure :::" + t.getMessage());
                                    mCallback.onDocReceivedFailure(
                                            mContext.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public interface ImageListner{
        void onDocSaveReceivedSuccess(CommonResponse resp, int mode);
        void onDocReceivedSuccess(DownloadDoc doc, int mode);
        void onDocSaveReceivedFailure(String strErr, int mode);
        void onDocReceivedFailure(String strErr, int mode);
    }


}
