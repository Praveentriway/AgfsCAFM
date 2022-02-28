package com.daemon.emco_android.repository.remote;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.EmployeeList;
import com.daemon.emco_android.model.common.EmployeeTrackingDetail;
import com.daemon.emco_android.model.response.EmployeeGpsReponse;
import com.daemon.emco_android.model.response.EmployeeListResponse;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.daemon.emco_android.utils.AppUtils.MODE_OFFLINE;
import static com.daemon.emco_android.utils.AppUtils.MODE_REMOTE;

public class EmployeeGpsRepository {

    private Context mContext;
    private ApiInterface mInterface;
    private EmployeeGPSListener mCallback;
    private AppDatabase db;

    public EmployeeGpsRepository(Context context, EmployeeGPSListener listener) {
        this.mContext = context;
        this.mCallback = listener;
        db = AppDatabase.getAppDatabase(context);
        mInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
    }


    public void updateEmployeeGps(EmployeeTrackingDetail employeeTrackingDetail,int mode) {
        Log.d(getClass().getName(), "getLocationDetail");

        if(mode==MODE_OFFLINE){

            List<EmployeeTrackingDetail> emply=new ArrayList<EmployeeTrackingDetail>();
            emply.add(employeeTrackingDetail);
            db.employeeTrackingDao().insertAll(emply);
            mCallback.onSuccessGpsUpdate(
                    "GPS data added in Room Db.", MODE_OFFLINE);

        }
        else{

            List<EmployeeTrackingDetail> emply=new ArrayList<EmployeeTrackingDetail>();

            // -- check for null value which is passed intentionally on checking offline upload from main activity --

            if(employeeTrackingDetail!=null){
                // -- adding current value to array store --
                emply.add(employeeTrackingDetail);

                // -- checking for room data availability
                if(db.employeeTrackingDao().count()>0){
                    // adding room value
                    emply.addAll(db.employeeTrackingDao().getAllEmployeeTracking());
                }

            }
            else {

                // if the method called from main activity and check for room data availability
                if(db.employeeTrackingDao().count()>0){
                    // adding room value
                    emply.addAll(db.employeeTrackingDao().getAllEmployeeTracking());
                }
                else{
                    mCallback.onSuccessGpsUpdate(
                            "No GPS data found in room table to upload.", MODE_OFFLINE);
                    return;
                }

            }

            Call<EmployeeGpsReponse> getLocationOpco = mInterface.updateEmployeeGps(emply);
            getLocationOpco.enqueue(
                    new Callback<EmployeeGpsReponse>() {
                        @Override
                        public void onResponse(
                                Call<EmployeeGpsReponse> call, Response<EmployeeGpsReponse> response) {
                            if (response.isSuccessful()) {
                                Log.d(getClass().getName(), "onResponse success " + response.body().getMessage());
                                if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                    Log.d(getClass().getName(), "onResponse success");
                                    mCallback.onSuccessGpsUpdate(
                                            response.body().getStatus(), AppUtils.MODE_SERVER
                                    );

                                    if(db.employeeTrackingDao().count()>0){
                                        db.employeeTrackingDao().deleteAll();
                                    }

                                } else
                                    mCallback.onFailureGpsUpdate(
                                            response.body().getMessage(), AppUtils.MODE_SERVER);
                            } else
                                mCallback.onFailureGpsUpdate(
                                        response.message(), AppUtils.MODE_SERVER);
                        }

                        @Override
                        public void onFailure(Call<EmployeeGpsReponse> call, Throwable t) {
                            Log.d(getClass().getName(), "onFailure" + t.getMessage());
                            mCallback.onFailureGpsUpdate(
                                    mContext.getString(R.string.msg_request_error_occurred), AppUtils.MODE_SERVER);
                        }
                    });
        }

    }




    public void getEmployeeList(String employeeID) {

        try {
            mInterface.getEmployeeDetail(employeeID).enqueue(new Callback<EmployeeListResponse>() {
                @Override
                public void onResponse(Call<EmployeeListResponse> call, Response<EmployeeListResponse> response) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {

                            mCallback.onReceiveEmployeeList(response.body().getObject());
                        }else{
                            mCallback.onReceiveFailureEmployeeList(response.body().getMessage());
                        }

                    }

                    else{
                        mCallback.onReceiveFailureEmployeeList(response.body().getMessage());
                    }

                }

                @Override
                public void onFailure(Call<EmployeeListResponse> call, Throwable t) {
                    mCallback.onReceiveFailureEmployeeList(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public interface EmployeeGPSListener {

        void onSuccessGpsUpdate(String strMsg, int mode);

        void onFailureGpsUpdate(String strErr, int mode);

        void onReceiveEmployeeList(List<EmployeeList> object);

        void onReceiveFailureEmployeeList(String toString);

    }


}
