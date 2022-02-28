package com.daemon.emco_android.repository.remote;

import android.content.Context;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.SearchComplaintPreventiveListener;
import com.daemon.emco_android.model.response.PPMDetailsResponse;
import com.daemon.emco_android.model.request.SearchSingleComplaintPreventiveRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vikram on 17/7/17.
 */

public class GetSearchComplaintPreventiveService {
    private static final String TAG = GetSearchComplaintPreventiveService.class.getSimpleName();

    private Context mContext;
    private Fragment mFragment;
    private SearchComplaintPreventiveListener mCallback;
   // private SingleSearchComplaintEntity mResponse;
    private ApiInterface mInterface;


    public GetSearchComplaintPreventiveService(Context context, Fragment fragment, SearchComplaintPreventiveListener listener) {
        this.mContext = context;
        this.mFragment = fragment;
        this.mCallback = listener;
        mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
    }


    public void getSingleSearchPreventiiveData(SearchSingleComplaintPreventiveRequest mSingleRequest,String employeeId) {
        Log.d(TAG, "getSingleSearchData");
         mInterface.getSingleSearchPreventiveData(employeeId,mSingleRequest).enqueue(new Callback<PPMDetailsResponse>() {
            @Override
            public void onResponse(Call<PPMDetailsResponse> call, Response<PPMDetailsResponse> response) {
                Log.d(TAG, "onResponse");
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                        mCallback.onComplaintReceivedSuccess(response.body().getPPMDetails());
                    }else{
                      mCallback.onComplaintReceivedFailure(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PPMDetailsResponse> call, Throwable t) {
                Log.d(TAG, "onFailure"+t.getMessage());
                mCallback.onComplaintReceivedFailure(mContext.getString(R.string.msg_request_error_occurred));
            }
        });
    }


}
