package com.daemon.emco_android.repository.remote;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.ContractUserListner;
import com.daemon.emco_android.listeners.SiteListener;
import com.daemon.emco_android.model.common.ContractNoUsers;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.model.response.ContractResponse;
import com.daemon.emco_android.model.response.ContractUserResponse;
import com.daemon.emco_android.model.response.SiteResponse;
import com.daemon.emco_android.repository.db.entity.ContractEntity;
import com.daemon.emco_android.repository.remote.restapi.Api;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractUserRepo {
    private static final String TAG = "CONTRACTUSER";
    private AppCompatActivity mActivity;
    private ContractUserListner mCallContractUser;
    private ApiInterface mInterface;
    private EmployeeIdRequest employeeIdRequest;
    public ContractUserRepo(
            AppCompatActivity mActivity, EmployeeIdRequest employeeIdRequest) {
        this.mActivity = mActivity;
        this.employeeIdRequest = employeeIdRequest;
        //  mInterface = ApiClient.getClient(mActivity).create(ApiInterface.class);
        mInterface = Api.dataClient().create(ApiInterface.class);
    }
    public void getContractNoContractUser(String [] data, ContractUserListner listener) {
        Log.d(TAG, "getContractListData");
        String employeeId = data[0];
        String serviceGroup = data[1];
        this.mCallContractUser = listener;
        try {
            mInterface.getContractNoResult(employeeId,serviceGroup)
                    .enqueue(
                            new Callback<ContractUserResponse>() {
                                @Override
                                public void onResponse(
                                        Call<ContractUserResponse> call, Response<ContractUserResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse"+response.body().getObject());
                                            mCallContractUser.onContractUserReceivedSuccess(
                                                    response.body().getObject(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallContractUser.onContractReceivedFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);

                                    }else
                                        mCallContractUser.onContractReceivedFailure(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<ContractUserResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure");
                                    mCallContractUser.onContractReceivedFailure(
                                            mActivity.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void getSiteContractUser(String [] data, ContractUserListner listener) {
        Log.d(TAG, "getContractListData");
        String employeeId = data[0];
        String serviceGroup = data[1];
        String opco = data[2];
        String contractNo = data[3];
        this.mCallContractUser = listener;
        try {
            mInterface.getSiteFromContractUser(employeeId,serviceGroup,opco,contractNo)
                    .enqueue(
                            new Callback<ContractUserResponse>() {
                                @Override
                                public void onResponse(
                                        Call<ContractUserResponse> call, Response<ContractUserResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse"+response.body().getObject());
                                            mCallContractUser.onContractUserReceivedSuccess(
                                                    response.body().getObject(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallContractUser.onContractReceivedFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);

                                    }else
                                        mCallContractUser.onContractReceivedFailure(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<ContractUserResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure");
                                    mCallContractUser.onContractReceivedFailure(
                                            mActivity.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public void getBuildingContractUser(String [] data, ContractUserListner listener) {
        Log.d(TAG, "getContractListData");
        String employeeId = data[0];
        String serviceGroup = data[1];
        String opco = data[2];
        String contractNo = data[3];
        String siteCode = data[4];
        String zoneCode = data[5];
        this.mCallContractUser = listener;
        try {
            mInterface.getBuildingFromContractUser(employeeId,serviceGroup,opco,contractNo,siteCode,zoneCode)
                    .enqueue(
                            new Callback<ContractUserResponse>() {
                                @Override
                                public void onResponse(
                                        Call<ContractUserResponse> call, Response<ContractUserResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equals(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse"+response.body().getObject());
                                            mCallContractUser.onContractUserReceivedSuccess(
                                                    response.body().getObject(), AppUtils.MODE_SERVER);
                                        } else
                                            mCallContractUser.onContractReceivedFailure(
                                                    response.body().getMessage(), AppUtils.MODE_SERVER);

                                    }else
                                        mCallContractUser.onContractReceivedFailure(
                                                response.message(), AppUtils.MODE_SERVER);
                                }

                                @Override
                                public void onFailure(Call<ContractUserResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure");
                                    mCallContractUser.onContractReceivedFailure(
                                            mActivity.getString(R.string.msg_request_error_occurred),
                                            AppUtils.MODE_SERVER);
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




}
