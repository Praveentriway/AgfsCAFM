package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.listeners.RespondComplaintListener;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.WorkDefectEntity;
import com.daemon.emco_android.model.request.WorkDefectRequest;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikram on 19/7/17.
 */

public class WorkDefectsDbInitializer extends AsyncTask<Integer, Void, Integer> {

    private final String TAG = WorkDefectsDbInitializer.class.getSimpleName();
    private List<WorkDefectEntity> mWorkDefectList = new ArrayList<>();
    private RespondComplaintListener mCallback;
    private AppDatabase db;
    private WorkDefectRequest mdefectRequest;

    public WorkDefectsDbInitializer(Context context, RespondComplaintListener callback, List<WorkDefectEntity> defectEntities) {
        db = AppDatabase.getAppDatabase(context);
        mWorkDefectList = defectEntities;
        mCallback = callback;
    }

    public WorkDefectsDbInitializer(Context context, RespondComplaintListener callback, WorkDefectRequest defectRequest) {
        db = AppDatabase.getAppDatabase(context);
        mdefectRequest = defectRequest;
        mCallback = callback;
    }

    @Override
    protected Integer doInBackground(final Integer... params) {
        try {
            switch (params[0]) {
                case AppUtils.MODE_INSERT:
                    Log.d(TAG, "insertAll");
                    db.workDefectsDao().deleteAll();
                    db.workDefectsDao().insertAll(mWorkDefectList);
                    break;
                case AppUtils.MODE_INSERT_SINGLE:
                    Log.d(TAG, "insertAll");
                    db.workDefectsDao().insert(mWorkDefectList);
                    break;
                case AppUtils.MODE_GETALL:
                    Log.d(TAG, "getAll");
                    mWorkDefectList = db.workDefectsDao().getAll();
                    break;
                case AppUtils.MODE_GET:
                    Log.d(TAG, "get");
                    mWorkDefectList = db.workDefectsDao()
                            .findByWorkDefectCode(mdefectRequest.getComplaintCode(), mdefectRequest.getWorkCategory());
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
                if (mWorkDefectList.size() > 0) {
                    mCallback.onWorkDefectReceived(mWorkDefectList, AppUtils.MODE_LOCAL);
                } else
                    mCallback.onComplaintRespondReceivedFailure("No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
                break;
            case AppUtils.MODE_GETALL:
                if (mWorkDefectList.size() > 0) {
                    mCallback.onWorkDefectReceived(mWorkDefectList, AppUtils.MODE_LOCAL);
                } else
                    mCallback.onComplaintRespondReceivedFailure("No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
                break;
        }
    }
}