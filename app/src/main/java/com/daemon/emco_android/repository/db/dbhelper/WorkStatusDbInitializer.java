package com.daemon.emco_android.repository.db.dbhelper;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import android.util.Log;

import com.daemon.emco_android.listeners.RespondComplaintListener;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.WorkStatusEntity;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikram on 19/7/17.
 */

public class WorkStatusDbInitializer {

    private static final String TAG = WorkStatusDbInitializer.class.getSimpleName();
    private static PopulateDbAsync mTask;
    private static int mMode;
    private static String mStatusCode = null;
    private static List<WorkStatusEntity> mWorkStatusList = new ArrayList<>();
    private static RespondComplaintListener mCallback;

    public WorkStatusDbInitializer(RespondComplaintListener callback) {
        mCallback = callback;
    }

    public static void populateAsync(@NonNull final AppDatabase db, List<WorkStatusEntity> workStatusList, int mode) {
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        mWorkStatusList = workStatusList;
        mTask.execute();
    }

    public static void populateAsync(@NonNull final AppDatabase db, String statusCode, int mode) {
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        mStatusCode = statusCode;
        mTask.execute();
    }

    public static void populateAsync(@NonNull final AppDatabase db, int mode) {
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        mTask.execute();
    }


    private static void insertAll(final AppDatabase db, List<WorkStatusEntity> workStatusEntities) {
        Log.d(TAG, "insertAll");
        int count = db.workStatusDao().count();
        if (count == 0 && count != workStatusEntities.size()) {
            db.workStatusDao().deleteAll();
            db.workStatusDao().insertAll(workStatusEntities);
        } else Log.d(TAG, "insertAll " + "data already found");
    }


    private static void getAll(final AppDatabase db) {
        Log.d(TAG, "getAll");
        try {
            int count = db.workStatusDao().count();
            if (count > 0) mWorkStatusList = db.workStatusDao().getAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            switch (mMode) {
                case AppUtils.MODE_INSERT:
                    insertAll(mDb, mWorkStatusList);
                    break;
                case AppUtils.MODE_GETALL:
                    getAll(mDb);
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            switch (mMode) {
                case AppUtils.MODE_GETALL:
                    if (mWorkStatusList.size() > 0) {
                        mCallback.onWorkStatusReceivedSuccess(mWorkStatusList, AppUtils.MODE_LOCAL);
                    } else
                        mCallback.onComplaintRespondReceivedFailure("No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
                    break;
            }
        }
    }
}
