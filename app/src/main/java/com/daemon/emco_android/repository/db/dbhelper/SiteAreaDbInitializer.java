package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.SiteAreaEntity;
import com.daemon.emco_android.listeners.SiteListener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/** Created by vikram on 19/7/17. */
public class SiteAreaDbInitializer extends AsyncTask<Integer, Void, Integer> {

  private final String TAG = SiteAreaDbInitializer.class.getSimpleName();
  private List<SiteAreaEntity> siteAreaList = new ArrayList<>();
  private SiteListener mCallback;
  private AppDatabase db;

  public SiteAreaDbInitializer(
      Context context, List<SiteAreaEntity> siteAreaEntityList, SiteListener callback) {
    db = AppDatabase.getAppDatabase(context);
    siteAreaList = siteAreaEntityList;
    mCallback = callback;
  }

  @Override
  protected Integer doInBackground(final Integer... params) {
    Log.d(TAG, "doInBackground");
    switch (params[0]) {
      case AppUtils.MODE_INSERT:
        db.siteAreaDao().deleteAll();
        db.siteAreaDao().insertAllSites(siteAreaList);
        break;
      case AppUtils.MODE_GETALL:
        siteAreaList = db.siteAreaDao().getAllSites();
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
        if (siteAreaList.size() > 0) {
          mCallback.onLogComplaintSiteReceivedSuccess(siteAreaList, AppUtils.MODE_LOCAL);
        } else
          mCallback.onLogComplaintSiteReceivedFailure(
              "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
        break;
    }
  }
}
