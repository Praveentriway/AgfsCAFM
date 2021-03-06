package com.daemon.emco_android.repository.remote;

import android.content.Context;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.SearchComplaintListener;
import com.daemon.emco_android.repository.db.entity.SingleSearchComplaintEntity;
import com.daemon.emco_android.model.request.MultiSearchRequest;
import com.daemon.emco_android.model.request.SearchSingleComplaintRequest;
import com.daemon.emco_android.model.response.MultiSearchComplaintResponse;
import com.daemon.emco_android.model.response.SingleSearchComplaintResponse;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vikram on 17/7/17.
 */

public class GetSearchComplaintService {
    private static final String TAG = GetSearchComplaintService.class.getSimpleName();

    private Context mContext;
    private Fragment mFragment;
    private SearchComplaintListener mCallback;
    private SingleSearchComplaintEntity mResponse;
    private ApiInterface mInterface;
    private Call<SingleSearchComplaintResponse> getSearchData;
    private Call<MultiSearchComplaintResponse> getMultiSearchData;


    public GetSearchComplaintService(Context context, Fragment fragment, SearchComplaintListener listener) {
        this.mContext = context;
        this.mFragment = fragment;
        this.mCallback = listener;
        mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
    }

    public void getSingleSearchData(SearchSingleComplaintRequest mSingleRequest) {
        Log.d(TAG, "getSingleSearchData");
        getSearchData = mInterface.getSingleSearchComplaintData(mSingleRequest);
        getSearchData.enqueue(new Callback<SingleSearchComplaintResponse>() {
            @Override
            public void onResponse(Call<SingleSearchComplaintResponse> call, Response<SingleSearchComplaintResponse> response) {
                Log.d(TAG, "onResponse");
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                        mResponse = response.body().getObject();
                        mCallback.onSingleComplaintReceivedSuccess(mResponse);
                    } else mCallback.onComplaintReceivedFailure(response.body().getMessage());
                } else mCallback.onComplaintReceivedFailure(response.message());
            }

            @Override
            public void onFailure(Call<SingleSearchComplaintResponse> call, Throwable t) {
                Log.d(TAG, "onFailure"+t.getMessage());
                mCallback.onComplaintReceivedFailure(mContext.getString(R.string.msg_request_error_occurred));
            }
        });
    }

    public void getSingleSearchPreventiiveData(SearchSingleComplaintRequest mSingleRequest) {
        Log.d(TAG, "getSingleSearchData");
        getSearchData = mInterface.getSingleSearchComplaintData(mSingleRequest);
        getSearchData.enqueue(new Callback<SingleSearchComplaintResponse>() {
            @Override
            public void onResponse(Call<SingleSearchComplaintResponse> call, Response<SingleSearchComplaintResponse> response) {
                Log.d(TAG, "onResponse");
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                        mResponse = response.body().getObject();
                        mCallback.onSingleComplaintReceivedSuccess(mResponse);
                    } else mCallback.onComplaintReceivedFailure(response.body().getMessage());
                } else mCallback.onComplaintReceivedFailure(response.message());
            }

            @Override
            public void onFailure(Call<SingleSearchComplaintResponse> call, Throwable t) {
                Log.d(TAG, "onFailure"+t.getMessage());
                mCallback.onComplaintReceivedFailure(mContext.getString(R.string.msg_request_error_occurred));
            }
        });
    }



    public void getMultiSearchData(MultiSearchRequest request, boolean isDashboard) {
        Log.d(TAG, "getMultiSearchData");
        if (isDashboard) {
            getMultiSearchData = mInterface.getDashboardPiechartDetails(request);
        } else {
            getMultiSearchData = mInterface.getMultiSearchComplaintData(request);
        }

        getMultiSearchData.enqueue(new Callback<MultiSearchComplaintResponse>() {
            @Override
            public void onResponse(Call<MultiSearchComplaintResponse> call, Response<MultiSearchComplaintResponse> response) {
                Log.d(TAG, "onResponse");
                if (response.isSuccessful()) {
                    MultiSearchComplaintResponse mResponse = response.body();
                    Log.d(TAG, "response data ::" + mResponse.getMessage());
                    if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                        mCallback.onComplaintReceivedSuccess(mResponse.getObject(), mResponse.getTotalNumberOfRows());
                    } else mCallback.onComplaintReceivedFailure(mResponse.getMessage());
                } else mCallback.onComplaintReceivedFailure(response.message());
            }

            @Override
            public void onFailure(Call<MultiSearchComplaintResponse> call, Throwable t) {
                Log.d(TAG, "onFailure " + t.toString());
                mCallback.onComplaintReceivedFailure(mContext.getString(R.string.msg_request_error_occurred));
            }
        });
    }
}
