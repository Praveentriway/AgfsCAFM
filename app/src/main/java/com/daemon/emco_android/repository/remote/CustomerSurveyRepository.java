package com.daemon.emco_android.repository.remote;

import android.content.Context;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.db.entity.ServeyQuestionnaire;
import com.daemon.emco_android.repository.db.entity.SurveyContract;
import com.daemon.emco_android.repository.db.entity.SurveyContractResponse;
import com.daemon.emco_android.repository.db.entity.SurveyCustomer;
import com.daemon.emco_android.repository.db.entity.SurveyCustomerResponse;
import com.daemon.emco_android.repository.db.entity.SurveyLocation;
import com.daemon.emco_android.repository.db.entity.SurveyLocationResponse;
import com.daemon.emco_android.repository.db.entity.SurveyMaster;
import com.daemon.emco_android.repository.db.entity.SurveyQuestionnaireResponse;
import com.daemon.emco_android.repository.db.entity.SurveyReferenceReponse;
import com.daemon.emco_android.repository.db.entity.SurveyTransaction;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;

import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSurveyRepository {

    private static final String TAG = CustomerSurveyRepository.class.getSimpleName();
    private Context mContext;
    private Listener listener;
    private SaveListener slistener;
    private ApiInterface mInterface;

    public CustomerSurveyRepository(Context context, Listener listener) {
        this.mContext = context;
        this.listener = listener;
        mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
    }

    public CustomerSurveyRepository(Context context, SaveListener listener) {
        this.mContext = context;
        this.slistener = listener;
        mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
    }


    public void getSurveyCustomer() {

        Log.d(TAG, "getSurveyCustomer");
        Call<SurveyCustomerResponse> getcustomer = mInterface.getSurveyCustomer();

        getcustomer.enqueue(
                new Callback<SurveyCustomerResponse>() {
                    @Override
                    public void onResponse(
                            Call<SurveyCustomerResponse> call, Response<SurveyCustomerResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onReceiveSurveyCustomer(
                                        response.body().getObject(), AppUtils.MODE_SERVER
                                );
                            } else
                                listener.onReceiveFailureSurveyCustomer(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onReceiveFailureSurveyCustomer(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<SurveyCustomerResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onReceiveFailureSurveyCustomer(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }

    public void getSurveyLocation(String contractNo) {

        Log.d(TAG, "getSurveyLocation");
        Call<SurveyLocationResponse> getcustomer = mInterface.getSurveyLocation(contractNo);

        getcustomer.enqueue(
                new Callback<SurveyLocationResponse>() {
                    @Override
                    public void onResponse(
                            Call<SurveyLocationResponse> call, Response<SurveyLocationResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onReceiveSurveyLocation(response.body().getObject(), AppUtils.MODE_SERVER
                                );
                            } else
                                listener.onReceiveFailureSurveyLocation(response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onReceiveFailureSurveyLocation(response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<SurveyLocationResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onReceiveFailureSurveyLocation(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }


    public void getSurveyContract(String custCode) {

        Log.d(TAG, "getSurveyContract");
        Call<SurveyContractResponse> getSurveyContract = mInterface.getSurveyContract(custCode);

        getSurveyContract.enqueue(
                new Callback<SurveyContractResponse>() {
                    @Override
                    public void onResponse(
                            Call<SurveyContractResponse> call, Response<SurveyContractResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onReceiveSurveyContract(
                                        response.body().getObject(), AppUtils.MODE_SERVER
                                );
                            } else
                                listener.onReceiveFailureSurveyContract(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onReceiveFailureSurveyContract(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<SurveyContractResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onReceiveFailureSurveyContract(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }


    public void getSurveyRefernce(String custCode) {

        Log.d(TAG, "getSurveyReference");
        Call<SurveyReferenceReponse> getSurveyRef = mInterface.getSurveyReference(custCode);

        getSurveyRef.enqueue(
                new Callback<SurveyReferenceReponse>() {
                    @Override
                    public void onResponse(
                            Call<SurveyReferenceReponse> call, Response<SurveyReferenceReponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onReceiveSurveyRefernce(
                                        response.body().getObject(), AppUtils.MODE_SERVER
                                );
                            } else
                                listener.onReceiveFailureSurveyRefernce(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onReceiveFailureSurveyRefernce(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<SurveyReferenceReponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onReceiveFailureSurveyRefernce(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }

    public void getSurveyQuestionnaire(String opco, String custCode, String custRef,String surveyType) {

        Log.d(TAG, "getSurveyQuestionnaire");
        Call<SurveyQuestionnaireResponse> getSurveyQuestionnaire = mInterface.getSurveyQuestionnaire(opco, custCode, custRef,surveyType);

        getSurveyQuestionnaire.enqueue(
                new Callback<SurveyQuestionnaireResponse>() {
                    @Override
                    public void onResponse(
                            Call<SurveyQuestionnaireResponse> call, Response<SurveyQuestionnaireResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onReceiveSurveyQuestionnaire(
                                        response.body().getObject(), AppUtils.MODE_SERVER
                                );
                            } else
                                listener.onReceiveFailureSurveyQuestionnaire(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onReceiveFailureSurveyQuestionnaire(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<SurveyQuestionnaireResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onReceiveFailureSurveyQuestionnaire(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }

    public void saveSurvey(SurveyTransaction trans) {

        Log.d(TAG, "saveSurvey");
        Call<CommonResponse> saveSurvey = mInterface.saveSurvey(trans);
        saveSurvey.enqueue(
                new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(
                            Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                slistener.onSuccessSaveSurvey(
                                        response.body().getMessage(), AppUtils.MODE_SERVER
                                );
                            } else
                                slistener.onFailureSaveSurvey(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            slistener.onFailureSaveSurvey(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        slistener.onFailureSaveSurvey(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }

    public interface Listener {

        void onReceiveSurveyCustomer(List<SurveyCustomer> customers, int mode);

        void onReceiveFailureSurveyCustomer(String strErr, int mode);

        void onReceiveSurveyContract(List<SurveyContract> contracts, int mode);

        void onReceiveFailureSurveyContract(String strErr, int mode);

        void onReceiveSurveyRefernce(List<SurveyMaster> refernces, int mode);

        void onReceiveFailureSurveyRefernce(String strErr, int mode);

        void onReceiveSurveyQuestionnaire(List<ServeyQuestionnaire> questions, int mode);

        void onReceiveFailureSurveyQuestionnaire(String strErr, int mode);

        void onReceiveSurveyLocation(List<SurveyLocation> questions, int mode);

        void onReceiveFailureSurveyLocation(String strErr, int mode);

    }

    public interface SaveListener {

        void onSuccessSaveSurvey(String strMsg, int mode);

        void onFailureSaveSurvey(String strErr, int mode);

    }


}
