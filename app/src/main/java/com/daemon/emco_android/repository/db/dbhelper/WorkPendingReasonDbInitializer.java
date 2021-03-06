package com.daemon.emco_android.repository.db.dbhelper;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import android.util.Log;

import com.daemon.emco_android.listeners.RespondComplaintListener;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.WorkPendingReasonEntity;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikram on 19/7/17.
 */

public class WorkPendingReasonDbInitializer {

    private static final String TAG = WorkPendingReasonDbInitializer.class.getSimpleName();
    private static PopulateDbAsync mTask;
    private static int mMode,mCount=0;
    private static String mWorkPendingCode=null;
    private static List<WorkPendingReasonEntity> mWorkPendingList = new ArrayList<>();
    private static WorkPendingReasonEntity mWorkPendingReasonEntity;
    private static RespondComplaintListener mCallback;


    public WorkPendingReasonDbInitializer(RespondComplaintListener callback) {
        mCallback = callback;
    }

    public static void populateAsync(@NonNull final AppDatabase db, List<WorkPendingReasonEntity> workPendingList, int mode) {
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        mWorkPendingList = workPendingList;
        mTask.execute();
    }
    public static void populateAsync(@NonNull final AppDatabase db, String pendingCode,int mode){
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        mWorkPendingCode = pendingCode;
        mTask.execute();
    }
    public static void populateAsync(@NonNull final AppDatabase db, int mode){
        mTask = new PopulateDbAsync(db);
        mMode = mode;
        mTask.execute();
    }


    private static void insertAll(final AppDatabase db,List<WorkPendingReasonEntity> workPendingReasonEntities){
        Log.d(TAG,"insertAll");
        int count = db.workPendingReasonDao().count();
        if(count ==0 && count != workPendingReasonEntities.size()){
            db.workPendingReasonDao().deleteAll();
            db.workPendingReasonDao().insertAll(workPendingReasonEntities);
        }else Log.d(TAG,"insertAll " + "data already found");
    }

    private static int getWorkPendingByPending(final AppDatabase db,String pendingCode){
        Log.d(TAG,"getWorkPendingByPending");
        try {
        int count = db.workPendingReasonDao().countByWorkReasonCode(pendingCode);
        if(count>0){
             mWorkPendingReasonEntity = db.workPendingReasonDao().findByWorkReasonCode(pendingCode);
        }
    }catch (Exception ex){
        ex.printStackTrace();
    }
    return AppUtils.MODE_GET;
    }
    private static int getAll(final AppDatabase db){
        Log.d(TAG,"getAll");
        try {
            mCount = db.workPendingReasonDao().count();
            if(mCount>0){
                mWorkPendingList = db.workPendingReasonDao().getAll();
            }else {
                mWorkPendingList.clear();
                Log.d(TAG, "getAll no data found ");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return AppUtils.MODE_GETALL;
    }
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Integer> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Integer doInBackground(final Void... params) {
            //populateWithTestData(mDb);
            int mode = 0;
            switch (mMode){
                case AppUtils.MODE_INSERT:
                    insertAll(mDb,mWorkPendingList);
                    break;
                case AppUtils.MODE_GET:
                    mode= getWorkPendingByPending(mDb,mWorkPendingCode);
                    break;
                case AppUtils.MODE_GETALL:
                    mode = getAll(mDb);
                    break;
            }
            return mode;
        }

        @Override
        protected void onPostExecute(Integer mode) {
            Log.d(TAG,"onPostExecute");
            switch (mode){
                case AppUtils.MODE_GET:
                    break;
                case AppUtils.MODE_GETALL:
                    mCallback.onWorkPendingReasonReceivedSuccess(mWorkPendingList,AppUtils.MODE_LOCAL);
                    break;
            }
        }
    }


}
