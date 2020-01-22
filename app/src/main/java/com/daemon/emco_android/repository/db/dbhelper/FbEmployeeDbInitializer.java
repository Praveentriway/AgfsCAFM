package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.EmployeeDetailsEntity;
import com.daemon.emco_android.listeners.FeedbackListener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikram on 19/7/17.
 */

public class FbEmployeeDbInitializer extends AsyncTask<Integer, Void, Integer> {

    private final String TAG = FbEmployeeDbInitializer.class.getSimpleName();
    private List<EmployeeDetailsEntity> mEmployeeDetailsList = new ArrayList<>();
    private EmployeeDetailsEntity mDetailsEntity = null;
    private FeedbackListener mCallback;
    private AppDatabase db;

    public FbEmployeeDbInitializer(Context context, FeedbackListener callback, List<EmployeeDetailsEntity> detailsEntities) {
        db = AppDatabase.getAppDatabase(context);
        mCallback = callback;
        mEmployeeDetailsList = detailsEntities;
    }

    public FbEmployeeDbInitializer(Context context, FeedbackListener callback, EmployeeDetailsEntity detailsEntity) {
        db = AppDatabase.getAppDatabase(context);
        mCallback = callback;
        mDetailsEntity = detailsEntity;
    }

    @Override
    protected Integer doInBackground(final Integer... params) {
        try {
            switch (params[0]) {
                case AppUtils.MODE_INSERT:
                    Log.d(TAG, "insertAll");
                    db.employeeDetailsDao().deleteAll();
                    db.employeeDetailsDao().insertAll(mEmployeeDetailsList);
                    break;

                case AppUtils.MODE_INSERT_SINGLE:
                    Log.d(TAG, "insert");
                    db.employeeDetailsDao().insertAll(mEmployeeDetailsList);
                    break;

                case AppUtils.MODE_GET:
                    mEmployeeDetailsList = db.employeeDetailsDao().findByEmployeeDetailsCode(
                            mDetailsEntity.getOpco(),
                            mDetailsEntity.getContractNo(),
                            mDetailsEntity.getWorkCategory());
                    break;

                case AppUtils.MODE_GETALL:
                    mEmployeeDetailsList = db.employeeDetailsDao().getAll();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params[0];
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        switch (integer) {
            case AppUtils.MODE_INSERT:
                break;
            case AppUtils.MODE_GET:
                if (mEmployeeDetailsList.size() > 0) {
                    mCallback.onFeedbackEmployeeDetailsReceivedSuccess(mEmployeeDetailsList, AppUtils.MODE_LOCAL);
                } else
                    mCallback.onFeedbackEmployeeDetailsReceivedFailure("No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
                break;
            case AppUtils.MODE_GETALL:
                if (mEmployeeDetailsList.size() > 0) {
                    mCallback.onFeedbackEmployeeDetailsReceivedSuccess(mEmployeeDetailsList, AppUtils.MODE_LOCAL);
                } else
                    mCallback.onFeedbackEmployeeDetailsReceivedFailure("No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
                break;
        }
    }
}