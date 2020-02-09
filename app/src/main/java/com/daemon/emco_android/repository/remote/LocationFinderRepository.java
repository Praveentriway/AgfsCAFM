package com.daemon.emco_android.repository.remote;

import android.content.Context;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.OpcoDetail;
import com.daemon.emco_android.model.request.LocationDetail;
import com.daemon.emco_android.model.response.ContractDetailResponse;
import com.daemon.emco_android.model.response.ContractDetails;
import com.daemon.emco_android.model.response.LocationDetailResponse;
import com.daemon.emco_android.model.response.LocationResponse;
import com.daemon.emco_android.model.response.OpcoResponse;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationFinderRepository {

    private static final String TAG = LocationFinderRepository.class.getSimpleName();
    private Context mContext;
    private LocationFinderInterface listener;
    private ApiInterface mInterface;


    public LocationFinderRepository(Context context, LocationFinderInterface listener) {
        this.mContext = context;
        this.listener = listener;
        mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
    }

    public void getLocationOpco(String userid) {
        Log.d(TAG, "getLocationOpco");
        Call<OpcoResponse> getLocationOpco = mInterface.getLocationOpco(userid);

        getLocationOpco.enqueue(
                new Callback<OpcoResponse>() {
                    @Override
                    public void onResponse(
                            Call<OpcoResponse> call, Response<OpcoResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onSuccessLocationOpco(
                                        response.body().getOpco(), AppUtils.MODE_SERVER
                                );
                            } else
                                listener.onFailureLocationOpco(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onFailureLocationOpco(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<OpcoResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onFailureLocationOpco(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }


    public void getLocationJobNo(LocationDetail location) {

        Log.d(TAG, "getLocationJobNo");
        Call<LocationResponse> getLocationOpco = mInterface.getLocationJobNo(location);

        getLocationOpco.enqueue(
                new Callback<LocationResponse>() {
                    @Override
                    public void onResponse(
                            Call<LocationResponse> call, Response<LocationResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onSuccessLocationJobNo(
                                        response.body().getLocation(), AppUtils.MODE_SERVER
                                );
                            } else
                                listener.onFailureLocationOpco(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onFailureLocationOpco(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<LocationResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onFailureLocationJobNo(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }

    public void getLocationBuilding(LocationDetail location) {

        Log.d(TAG, "getLocationBuilding");
        Call<LocationResponse> getLocationOpco = mInterface.getLocationBuilding(location);

        getLocationOpco.enqueue(
                new Callback<LocationResponse>() {
                    @Override
                    public void onResponse(
                            Call<LocationResponse> call, Response<LocationResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onSuccessLocationBuildingName(
                                        response.body().getLocation(), AppUtils.MODE_SERVER
                                );
                            } else
                                listener.onFailureLocationOpco(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onFailureLocationOpco(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<LocationResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onFailureLocationBuildingName(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }

    public void getLocationZone(LocationDetail location) {

        Log.d(TAG, "getLocationZone");
        Call<LocationResponse> getLocationOpco = mInterface.getLocationZone(location);

        getLocationOpco.enqueue(
                new Callback<LocationResponse>() {
                    @Override
                    public void onResponse(
                            Call<LocationResponse> call, Response<LocationResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onSuccessLocationZoneName(
                                        response.body().getLocation(), AppUtils.MODE_SERVER
                                );
                            } else
                                listener.onFailureLocationOpco(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onFailureLocationOpco(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<LocationResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onFailureLocationZoneName(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }


    public void getLocationDetail(LocationDetail location) {

        Log.d(TAG, "getLocationDetail");
        Call<LocationDetailResponse> getLocationOpco = mInterface.getLocationDetail(location);

        getLocationOpco.enqueue(
                new Callback<LocationDetailResponse>() {
                    @Override
                    public void onResponse(
                            Call<LocationDetailResponse> call, Response<LocationDetailResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse success " + response.body().getMessage());
                            if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                Log.d(TAG, "onResponse success");
                                listener.onSuccessLocationDetail(
                                        response.body().getLcoationDetails(), AppUtils.MODE_SERVER
                                );
                            } else
                                listener.onFailureLocationOpco(
                                        response.body().getMessage(), AppUtils.MODE_SERVER);
                        } else
                            listener.onFailureLocationDetail(
                                    response.message(), AppUtils.MODE_SERVER);
                    }

                    @Override
                    public void onFailure(Call<LocationDetailResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure" + t.getMessage());
                        listener.onFailureLocationZoneName(
                                mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                    }
                });
    }


    public interface LocationFinderInterface{

        void onSuccessLocationOpco(List<OpcoDetail> opco, int mode);
        void onFailureLocationOpco(String strMsg, int mode);
        void onSuccessLocationBuildingName(List<LocationDetail> location, int mode);
        void onFailureLocationBuildingName(String strMsg, int mode);
        void onSuccessLocationZoneName(List<LocationDetail> location, int mode);
        void onFailureLocationZoneName(String strMsg, int mode);
        void onSuccessLocationJobNo(List<LocationDetail> location, int mode);
        void onFailureLocationJobNo(String strMsg, int mode);
        void onSuccessLocationDetail(LocationDetail location, int mode);
        void onFailureLocationDetail(String strMsg, int mode);

    }


}




