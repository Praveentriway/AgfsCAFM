package com.daemon.emco_android.repository.db.dbhelper;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import android.util.Log;

import com.daemon.emco_android.listeners.PpeListener;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class PPEFetchSaveDbInitializer {

    private static final String TAG = PPEFetchSaveDbInitializer.class.getSimpleName();
    private static PopulateDbAsync mTask;
    private static int mMode;
    private static boolean isInsert = false;
    private static String mCNumber = null;
    private static List<PPEFetchSaveEntity> ppeFetchSaveEntities = new ArrayList<>();
    private static PpeListener mCallback;

    public PPEFetchSaveDbInitializer(PpeListener callback) {
        mCallback = callback;
    }

    public static void populateAsync(@NonNull final AppDatabase db, List<PPEFetchSaveEntity> fetchSaveEntities, int mode) {
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        ppeFetchSaveEntities = fetchSaveEntities;
        mTask.execute();
    }

    public static void populateAsync(@NonNull final AppDatabase db, String cNumber, int mode) {
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        mCNumber = cNumber;
        mTask.execute();
    }

    public static void populateAsync(@NonNull final AppDatabase db, int mode) {
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        mTask.execute();
    }


    private static void insertPPE(final AppDatabase db, List<PPEFetchSaveEntity> fetchSaveEntities) {
        Log.d(TAG, "insertPPE");
        try {
            isInsert = false;
            Log.d(TAG, "b insertPPE " + db.ppeFetchSaveDao().count());
            db.ppeFetchSaveDao().insertPPE(fetchSaveEntities);
            Log.d(TAG, " a insertPPE " + db.ppeFetchSaveDao().count());
            isInsert = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static void getPPEByCNo(final AppDatabase db, String webNumber) {
        Log.d(TAG, "getPPEByCNo");
        try {
            ppeFetchSaveEntities = db.ppeFetchSaveDao().findBycomplainNumber(webNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updatePPE(final AppDatabase db) {
        Log.d(TAG, "getPPEByCNo");
        try {
            db.ppeFetchSaveDao().updateAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deletePPEBycNumber(final AppDatabase db, String webNumber) {
        Log.d(TAG, "deletePPEBycNumber");
        try {
            int count = db.ppeFetchSaveDao().countBycomplainNumber(webNumber);
            Log.d(TAG, "deletePPEBycNumber found " + count);
            if (count > 0) {
                db.ppeFetchSaveDao().deleteSinglePPE(webNumber);
            } else Log.d(TAG, "deletePPEBycNumber not found");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void getAll(final AppDatabase db) {
        Log.d(TAG, "getAll");
        try {
            int count = db.ppeFetchSaveDao().count();
            if (count > 0) {
                ppeFetchSaveEntities = db.ppeFetchSaveDao().getAll(AppUtils.MODE_LOCAL);
            } else {
                ppeFetchSaveEntities.clear();
                Log.d(TAG, "getAll no data found ");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                    insertPPE(mDb, ppeFetchSaveEntities);
                    break;
                case AppUtils.MODE_INSERT_SINGLE:
                    insertPPE(mDb, ppeFetchSaveEntities);
                    break;
                case AppUtils.MODE_GET:
                    getPPEByCNo(mDb, mCNumber);
                    break;
                case AppUtils.MODE_DELETE:
                    deletePPEBycNumber(mDb, mCNumber);
                    break;
                case AppUtils.MODE_GETALL:
                    getAll(mDb);
                    break;
                case AppUtils.MODE_UPDATE:
                    updatePPE(mDb);
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            switch (mMode) {
                case AppUtils.MODE_INSERT_SINGLE:
                    if (isInsert) {
                        mCallback.onPPESaveSuccess("Data has been successfully Saved.", AppUtils.MODE_LOCAL);
                    } else
                        mCallback.onPPESaveFailure("Data Save failed", AppUtils.MODE_LOCAL);
                    break;
                case AppUtils.MODE_GET:
                    if (ppeFetchSaveEntities.size() > 0) {
                        mCallback.onPPEFetchListSuccess(ppeFetchSaveEntities, AppUtils.MODE_LOCAL);
                    } else
                        mCallback.onPPEFetchListFailure("No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
                    break;
                case AppUtils.MODE_DELETE:
                    break;
                case AppUtils.MODE_GETALL:
                    if (ppeFetchSaveEntities.size() > 0) {
                        mCallback.onPPEFetchListSuccess(ppeFetchSaveEntities, AppUtils.MODE_LOCAL);
                    }
                    break;
            }
        }
    }
}
