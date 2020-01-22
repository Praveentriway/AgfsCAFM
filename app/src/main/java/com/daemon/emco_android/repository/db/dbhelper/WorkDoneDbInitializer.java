package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.listeners.RespondComplaintListener;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.WorkDoneEntity;
import com.daemon.emco_android.model.request.WorkDoneRequest;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikram on 19/7/17.
 */

public class WorkDoneDbInitializer extends AsyncTask<Integer, Void, Integer> {

    private final String TAG = WorkDoneDbInitializer.class.getSimpleName();
    private List<WorkDoneEntity> mWorkDoneEntities = new ArrayList<>();
    private RespondComplaintListener mCallback;
    private AppDatabase db;
    private WorkDoneRequest mWorkDoneRequest;

    public WorkDoneDbInitializer(Context context, RespondComplaintListener callback, List<WorkDoneEntity> workDoneEntities) {
        db = AppDatabase.getAppDatabase(context);
        mWorkDoneEntities = workDoneEntities;
        mCallback = callback;
    }

    public WorkDoneDbInitializer(Context context, RespondComplaintListener callback, WorkDoneRequest workDoneRequest) {
        db = AppDatabase.getAppDatabase(context);
        mWorkDoneRequest = workDoneRequest;
        mCallback = callback;
    }

    @Override
    protected Integer doInBackground(final Integer... params) {
        Log.d(TAG, "doInBackground");
        try {
            switch (params[0]) {
                case AppUtils.MODE_INSERT:
                    Log.d(TAG, "insertAll");
                    db.workDoneDao().deleteAll();
                    db.workDoneDao().insertAll(mWorkDoneEntities);
                    break;
                case AppUtils.MODE_INSERT_SINGLE:
                    Log.d(TAG, "insertAll");
                    db.workDoneDao().insertAll(mWorkDoneEntities);
                    break;
                case AppUtils.MODE_GET:
                    System.out.println(mWorkDoneRequest.getComplaintCode()+"qqqqqq"+mWorkDoneRequest.getDefectCode()+"qqqqqq"+mWorkDoneRequest.getWorkCategory());
                    mWorkDoneEntities = db.workDoneDao().findByDefectCode(mWorkDoneRequest.getComplaintCode(),
                            mWorkDoneRequest.getDefectCode(), mWorkDoneRequest.getWorkCategory());
                    break;
                case AppUtils.MODE_GETALL:
                    mWorkDoneEntities = db.workDoneDao().getAll();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params[0];
    }

    @Override
    protected void onPostExecute(Integer aVoid) {
        super.onPostExecute(aVoid);
        Log.d(TAG, "onPostExecute");
        try {
            switch (aVoid) {
                case AppUtils.MODE_GET:
                    if (mWorkDoneEntities.size() > 0) {
                        mCallback.onWorkDoneReceivedSuccess(mWorkDoneEntities, AppUtils.MODE_LOCAL);
                    } else
                        mCallback.onComplaintRespondReceivedFailure("No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
                    break;
                case AppUtils.MODE_GETALL:
                    if (mWorkDoneEntities.size() > 0) {
                        mCallback.onWorkDoneReceivedSuccess(mWorkDoneEntities, AppUtils.MODE_LOCAL);
                    } else
                        mCallback.onComplaintRespondReceivedFailure("No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}