package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.SaveRatedServiceEntity;
import com.daemon.emco_android.listeners.RatedServiceListener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikram on 19/7/17.
 */
public class SaveRatedServiceDbInitializer extends AsyncTask<Integer, Void, Integer> {

    private final String TAG = SaveRatedServiceDbInitializer.class.getSimpleName();
    private List<SaveRatedServiceEntity> entities = new ArrayList<>();
    private SaveRatedServiceEntity entity = null;
    private RatedServiceListener mCallback;
    private AppDatabase db;

    public SaveRatedServiceDbInitializer(
            Context context,
            RatedServiceListener callback,
            List<SaveRatedServiceEntity> detailsEntities) {
        db = AppDatabase.getAppDatabase(context);
        mCallback = callback;
        entities = detailsEntities;
    }

    public SaveRatedServiceDbInitializer(
            Context context, RatedServiceListener callback, SaveRatedServiceEntity detailsEntity) {
        db = AppDatabase.getAppDatabase(context);
        mCallback = callback;
        entity = detailsEntity;
    }

    @Override
    protected Integer doInBackground(final Integer... params) {
        try {
            switch (params[0]) {
                case AppUtils.MODE_INSERT:
                    Log.d(TAG, "insertAll");
                    // db.receiveComplaintItemDao().deleteAll();
                    // db.saveFeedbackDao().insertAll(saveFeedbackEntities);
                    Log.d(TAG, db.saveFeedbackDao().count() + "MODE_INSERT");
                    break;

                case AppUtils.MODE_INSERT_SINGLE:
                    Log.d(TAG, "insert");
                    db.receiveComplaintItemDao().insert(entity);
          /*db.receiveComplaintItemDao()
              .deleteSingleComplaint(
                  entity.getOpco(), entity.getComplaintNumber(), entity.getComplaintSite());
          */
                    break;

                case AppUtils.MODE_DELETE:
                    db.receiveComplaintItemDao().delete(entity);
                    break;

                case AppUtils.MODE_GETALL:
                    entities = db.receiveComplaintItemDao().getAllSaveRatedServiceEntity();
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
            case AppUtils.MODE_INSERT_SINGLE:
                mCallback.onSaveRatedService("Saved successfully");
                break;
            case AppUtils.MODE_GET:
                if (entity != null) {
                    // mCallback.onSaveRatedService("Saved successfully");
                } /*else
          mCallback.onFeedbackEmployeeDetailsReceivedFailure(
              "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);*/
                break;
            case AppUtils.MODE_GETALL:
                if (entities.size() > 0) {
                    mCallback.onRatedServiceReceived(entities);
                } else mCallback.onSaveRatedService("No data");
                break;
        }
    }
}
