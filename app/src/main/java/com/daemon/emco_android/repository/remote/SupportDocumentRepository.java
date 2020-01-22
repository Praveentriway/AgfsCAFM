package com.daemon.emco_android.repository.remote;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.DocumentType;
import com.daemon.emco_android.model.common.ReactiveSupportDoc;
import com.daemon.emco_android.model.request.PPMFilterRequest;
import com.daemon.emco_android.model.response.DocComplaintListResponse;
import com.daemon.emco_android.model.response.DocumentTypeResponse;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportDocumentRepository {

    private static final String TAG = SupportDocumentRepository.class.getSimpleName();
    private AppCompatActivity mActivity;
    private ApiInterface mInterface;
    private SupportDocI listener;
    private SupportDocTypeI listenerDT;

    public SupportDocumentRepository(AppCompatActivity _activity, SupportDocI listener) {
        mActivity = _activity;
        this.listener = listener;
        mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
    }

    public SupportDocumentRepository(AppCompatActivity _activity, SupportDocTypeI listener) {
        mActivity = _activity;
        this.listenerDT = listener;
        mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
    }


    public void getDocListData(
            String strEmpid, int startIndex, PPMFilterRequest ppmFilterRequest,String type) {

        Call<DocComplaintListResponse> getppm = mInterface.getDocCmplListResult(strEmpid, startIndex, 10, ppmFilterRequest, type);

        getppm.enqueue(
                new Callback<DocComplaintListResponse>() {
                    @Override
                    public void onResponse(
                            Call<DocComplaintListResponse> call, Response<DocComplaintListResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onListRcSuccess(
                                        response.body().getPPMDetails(),
                                        AppUtils.MODE_SERVER);
                            } else
                                listener.onListRcFailure(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onListRcFailure(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<DocComplaintListResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onListRcFailure(
                                mActivity.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }


    public void getDocType(String type) {

        Call<DocumentTypeResponse> getppm = mInterface.getDocumentType(type);

        getppm.enqueue(
                new Callback<DocumentTypeResponse>() {
                    @Override
                    public void onResponse(
                            Call<DocumentTypeResponse> call, Response<DocumentTypeResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listenerDT.onDocTypeListSuccess(
                                        response.body().getPPMDetails(),
                                        AppUtils.MODE_SERVER);
                            } else
                                listenerDT.onDocTypeListFailure(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listenerDT.onDocTypeListFailure(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<DocumentTypeResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onListRcFailure(
                                mActivity.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }


    public interface SupportDocTypeI{
        void onDocTypeListSuccess(List<DocumentType> rx, int from);
        void onDocTypeListFailure(String err, int from);
    }

    public interface SupportDocI{
        void onListRcSuccess(List<ReactiveSupportDoc> rx, int from);
        void onListRcFailure(String err, int from);
    }


}
