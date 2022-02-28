package com.daemon.emco_android.repository.db.dbhelper;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import android.util.Log;

import com.daemon.emco_android.listeners.PpeListener;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.PPENameEntity;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daemonsoft on 7/25/2017.
 */

public class PPENamesDbInitializer {

    private static final String TAG = PPENamesDbInitializer.class.getSimpleName();
    private static PopulateDbAsync mTask;
    private static int mMode;
    private static List<PPENameEntity> mPpeNameEntity = new ArrayList<>();
    private static PpeListener mCallback;

    public PPENamesDbInitializer(PpeListener callback) {
        mCallback = callback;
    }

    public static void populateAsync(@NonNull final AppDatabase db, List<PPENameEntity> ppeNameEntities, int mode) {
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        mPpeNameEntity = ppeNameEntities;
        mTask.execute();
    }

    public static void populateAsync(@NonNull final AppDatabase db, int mode) {
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        mTask.execute();
    }

    private static void insertAll(final AppDatabase db, List<PPENameEntity> ppeNameEntities) {
        Log.d(TAG, "insertAll");
        int count = db.ppeNameDao().count();
        if (count == 0 && count != ppeNameEntities.size()) {
            db.ppeNameDao().deleteAll();
            db.ppeNameDao().insertAll(ppeNameEntities);
            Log.d(TAG, "insertAll block");
        } else Log.d(TAG, "insertAll " + "data already found");
    }

    private static void getAll(final AppDatabase db) {
        Log.d(TAG, "getAll");
        try {
            int count = db.ppeNameDao().count();
            if (count > 0) {
                mPpeNameEntity = db.ppeNameDao().getAll();
            } else {
                Log.d(TAG, "getAll no data found ");
                mPpeNameEntity.clear();
            }

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
            //populateWithTestData(mDb);
            switch (mMode) {
                case AppUtils.MODE_INSERT:
                    insertAll(mDb, mPpeNameEntity);
                    break;
                case AppUtils.MODE_GET:
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
                case AppUtils.MODE_INSERT:
                    break;
                case AppUtils.MODE_GET:
                    break;
                case AppUtils.MODE_GETALL:
                    if (mPpeNameEntity.size()> 0){
                        mCallback.onPPENameListReceived(mPpeNameEntity, AppUtils.MODE_LOCAL);
                    }else mCallback.onPPENameListFailure("No data found in local database", AppUtils.MODE_LOCAL);
                    break;
            }
        }
    }
}
