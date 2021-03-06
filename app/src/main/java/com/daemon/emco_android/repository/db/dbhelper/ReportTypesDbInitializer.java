package com.daemon.emco_android.repository.db.dbhelper;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import android.util.Log;

import com.daemon.emco_android.listeners.ReportTypesListener;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.ReportTypesEntity;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikram on 17/8/17.
 */

public class ReportTypesDbInitializer {

    private static final String TAG = ReportTypesDbInitializer.class.getSimpleName();
    private static PopulateDbAsync mTask;
    private static int mMode;
    private static String mReportSRL = null;
    private static List<ReportTypesEntity> reportTypesEntities = new ArrayList<>();
    private static ReportTypesListener mCallback;
    private static ReportTypesEntity siteArea;

    public ReportTypesDbInitializer(ReportTypesListener callback) {
        mCallback = callback;
    }

    public static void populateAsync(@NonNull final AppDatabase db, List<ReportTypesEntity> mReportTypesEntities, int mode) {
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        reportTypesEntities = mReportTypesEntities;
        mTask.execute();
    }

    public static void populateAsync(@NonNull final AppDatabase db, String reportSRL, int mode) {
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        mReportSRL = reportSRL;
        mTask.execute();
    }

    public static void populateAsync(@NonNull final AppDatabase db, int mode) {
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        mTask.execute();
    }


    private static void insertAllSites(final AppDatabase db, List<ReportTypesEntity> reportTypesEntities) {
        Log.d(TAG, "insertAllSites");
        int count = db.reportTypesDao().count();
        if (count == 0 && count != reportTypesEntities.size()) {
            db.reportTypesDao().deleteAll();
            db.reportTypesDao().insertAllReportTypes(reportTypesEntities);
        } else Log.d(TAG, "insertAllSites " + "data already found");
    }

    private static void getSiteAreaUsingSiteCode(final AppDatabase db, String siteCode) {
        Log.d(TAG, "getSiteArea");
        try {
            int count = db.reportTypesDao().countByreportSRL(siteCode);
            if (count > 0) {
                siteArea = db.reportTypesDao().findReportTypesByreportSRL(siteCode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void getAll(final AppDatabase db) {
        Log.d(TAG, "getAllSites");
        try {
            int count = db.reportTypesDao().count();
            if (count > 0) {
                reportTypesEntities = db.reportTypesDao().getAllReportTypes();
            } else {
                reportTypesEntities.clear();
                Log.d(TAG, "getAll no data found ");
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
                    insertAllSites(mDb, reportTypesEntities);
                    break;
                case AppUtils.MODE_GET:
                    getSiteAreaUsingSiteCode(mDb, mReportSRL);
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
                    if (siteArea != null) {
                        mCallback.onReportTypesDbDataSingleByreportSRL(siteArea);
                    } else
                        mCallback.onReportTypesReceivedFailure("No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
                    break;
                case AppUtils.MODE_GETALL:
                    if (reportTypesEntities.size() > 0) {
                        mCallback.onReportTypesReceivedSuccess(reportTypesEntities, AppUtils.MODE_LOCAL);
                    } else
                        mCallback.onReportTypesReceivedFailure("No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
                    break;
            }
        }
    }
}
