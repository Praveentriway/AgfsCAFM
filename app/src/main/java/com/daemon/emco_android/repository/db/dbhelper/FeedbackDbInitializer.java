package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntity;
import com.daemon.emco_android.listeners.FeedbackListener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/** Created by vikram on 19/7/17. */
public class FeedbackDbInitializer extends AsyncTask<Integer, Void, Integer> {

  private final String TAG = FeedbackDbInitializer.class.getSimpleName();
  private List<SaveFeedbackEntity> saveFeedbackEntities = new ArrayList<>();
  private SaveFeedbackEntity saveFeedbackEntity = null;
  private FeedbackListener mCallback;
  private AppDatabase db;

  public FeedbackDbInitializer(
      Context context, FeedbackListener callback, List<SaveFeedbackEntity> detailsEntities) {
    db = AppDatabase.getAppDatabase(context);
    mCallback = callback;
    saveFeedbackEntities = detailsEntities;
  }

  public FeedbackDbInitializer(
      Context context, FeedbackListener callback, SaveFeedbackEntity detailsEntity) {
    db = AppDatabase.getAppDatabase(context);
    mCallback = callback;
    saveFeedbackEntity = detailsEntity;
  }

  public FeedbackDbInitializer(Context context, FeedbackListener callback) {
    db = AppDatabase.getAppDatabase(context);
    mCallback = callback;
  }

  @Override
  protected Integer doInBackground(final Integer... params) {
    try {
      switch (params[0]) {
        case AppUtils.MODE_INSERT:
          Log.d(TAG, "insertAll");
          db.saveFeedbackDao().deleteAll();
          //System.out.println(saveFeedbackEntities.get(0).getOpco().toString()+"aaaaaaaaaaaaa"+saveFeedbackEntities.size());
          db.saveFeedbackDao().insertAll(saveFeedbackEntities);
          Log.d(TAG, db.saveFeedbackDao().count() + "MODE_INSERT");
          break;

        case AppUtils.MODE_INSERT_SINGLE:
          Log.d(TAG, "insert");
          db.saveFeedbackDao().insert(saveFeedbackEntity);
          break;

        case AppUtils.MODE_GET:
          saveFeedbackEntity =
              db.saveFeedbackDao()
                  .findBycomplaintNumber(
                      saveFeedbackEntity.getOpco(),
                      saveFeedbackEntity.getComplaintSite(),
                      saveFeedbackEntity.getComplaintNumber());
          break;

        case AppUtils.MODE_GETALL:
          saveFeedbackEntities = db.saveFeedbackDao().getAll(AppUtils.MODE_LOCAL);
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
      case AppUtils.MODE_INSERT:
        break;
      case AppUtils.MODE_GET:
        if (saveFeedbackEntity != null) {
          mCallback.onFeedbackDetailsReceivedSuccess(saveFeedbackEntity, AppUtils.MODE_LOCAL);
        } /*else
          mCallback.onFeedbackEmployeeDetailsReceivedFailure(
              "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);*/
        break;
      case AppUtils.MODE_GETALL:
        if (saveFeedbackEntities.size() > 0) {
          mCallback.onAllFeedbackDetailsReceivedSuccess(saveFeedbackEntities, AppUtils.MODE_LOCAL);
        } else
          mCallback.onFeedbackEmployeeDetailsReceivedFailure(
              "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
        break;
    }
  }
}
