package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.LogComplaintEntity;
import com.daemon.emco_android.listeners.LogComplaint_Listener;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/** Created by vikram on 23/7/17. */
public class LogComplaintDbInitializer extends AsyncTask<Integer, Void, Integer> {

  private final String TAG = LogComplaintDbInitializer.class.getSimpleName();
  private LogComplaintEntity logComplaintEntity;
  private List<LogComplaintEntity> logComplaintEntities = new ArrayList<>();
  private AppDatabase db;
  private LogComplaint_Listener mCallback;
  private boolean isInsert = false;

  public LogComplaintDbInitializer(
      Context context, LogComplaintEntity mLogComplaintEntity, LogComplaint_Listener callback) {
    db = AppDatabase.getAppDatabase(context);
    logComplaintEntity = mLogComplaintEntity;
    mCallback = callback;
  }

  @Override
  protected Integer doInBackground(final Integer... params) {
    Log.d(TAG, "doInBackground");
    switch (params[0]) {
      case AppUtils.MODE_INSERT:
        int count =
            db.logComplaintDao().countByWebNumber(logComplaintEntity.getComplainWebNumber());
        if (count > 0) Log.d(TAG, "Data already found");
        else db.logComplaintDao().insertComplaint(logComplaintEntity);

        if (count
            < db.logComplaintDao().countByWebNumber(logComplaintEntity.getComplainWebNumber()))
          isInsert = true;
        else isInsert = false;
        break;
      case AppUtils.MODE_DELETE:
        db.logComplaintDao().deleteSingleComplaint(logComplaintEntity.getComplainWebNumber());
        break;
      case AppUtils.MODE_GETALL:
        logComplaintEntities = db.logComplaintDao().getAll();
        break;
    }
    return params[0];
  }

  @Override
  protected void onPostExecute(Integer aVoid) {
    super.onPostExecute(aVoid);
    Log.d(TAG, "onPostExecute");
    switch (aVoid) {
      case AppUtils.MODE_INSERT:
        if (isInsert) {
          mCallback.onLogComplaintDataReceivedSuccess(
              new EmployeeIdRequest(
                  null,
                  null,
                  logComplaintEntity.getComplainWebNumber(),
                  logComplaintEntity.getComplainWebNumber()),
              AppUtils.MODE_LOCAL);
        } else mCallback.onLogComplaintDataReceivedFailure("Data save failed", AppUtils.MODE_LOCAL);
        break;

      case AppUtils.MODE_GETALL:
        if (logComplaintEntities.size() > 0) {
          mCallback.onAllLogComplaintData(logComplaintEntities, AppUtils.MODE_LOCAL);
        } else
          // mCallback.onLogComplaintDataReceivedFailure("No data found. Please check internet
          // connection.", AppUtils.MODE_LOCAL);
          break;
    }
  }
}
