package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.PriorityEntity;
import com.daemon.emco_android.listeners.PriorityListListener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/** Created by vikram on 19/7/17. */
public class PriorityDbInitializer extends AsyncTask<Integer, Void, Integer> {

  private final String TAG = PriorityDbInitializer.class.getSimpleName();
  private List<PriorityEntity> priorityEntities = new ArrayList<>();
  private PriorityListListener mCallback;
  private AppDatabase db;

  public PriorityDbInitializer(
      Context context, List<PriorityEntity> mPriorityEntities, PriorityListListener callback) {
    db = AppDatabase.getAppDatabase(context);
    priorityEntities = mPriorityEntities;
    mCallback = callback;
  }

  @Override
  protected Integer doInBackground(final Integer... params) {
    Log.d(TAG, "doInBackground");
    switch (params[0]) {
      case AppUtils.MODE_INSERT:
        db.priorityDao().deleteAll();
        db.priorityDao().insertAll(priorityEntities);
        break;
      case AppUtils.MODE_GETALL:
        priorityEntities = db.priorityDao().getAll();
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
        if (priorityEntities.size() > 0) {
          mCallback.onPriorityReceivedSuccess(priorityEntities, AppUtils.MODE_LOCAL);
        } else
          mCallback.onPriorityReceivedFailure(
              "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
        break;
    }
  }
}
