package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.WorkTypeEntity;
import com.daemon.emco_android.listeners.WorkTypeListListener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/** Created by vikram on 19/7/17. */
public class WorkTypeDbInitializer extends AsyncTask<Integer, Void, Integer> {

  private final String TAG = WorkTypeDbInitializer.class.getSimpleName();
  private List<WorkTypeEntity> workTypeEntities = new ArrayList<>();
  private WorkTypeListListener mCallback;
  private AppDatabase db;

  public WorkTypeDbInitializer(
      Context context, List<WorkTypeEntity> mWorkTypeEntities, WorkTypeListListener callback) {
    db = AppDatabase.getAppDatabase(context);
    workTypeEntities = mWorkTypeEntities;
    mCallback = callback;
  }

  @Override
  protected Integer doInBackground(final Integer... params) {
    Log.d(TAG, "doInBackground");
    switch (params[0]) {
      case AppUtils.MODE_INSERT:
        db.workTypeDao().deleteAll();
        db.workTypeDao().insertAll(workTypeEntities);
        break;
      case AppUtils.MODE_GETALL:
        workTypeEntities = db.workTypeDao().getAll();
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
        break;
      case AppUtils.MODE_GETALL:
        if (workTypeEntities.size() > 0) {
          mCallback.onWorkTypeReceivedSuccess(workTypeEntities, AppUtils.MODE_LOCAL);
        } else
          mCallback.onWorkTypeReceivedFailure(
              "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
        break;
    }
  }
}
