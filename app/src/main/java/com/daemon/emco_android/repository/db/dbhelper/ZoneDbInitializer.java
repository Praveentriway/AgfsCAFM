package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.ZoneEntity;
import com.daemon.emco_android.listeners.ZoneListener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/** Created by vikram on 19/7/17. */
public class ZoneDbInitializer extends AsyncTask<Integer, Void, Integer> {

  private final String TAG = ZoneDbInitializer.class.getSimpleName();
  private List<ZoneEntity> zoneEntityArrayList = new ArrayList<>();
  private ZoneListener mCallback;
  private AppDatabase db;

  public ZoneDbInitializer(Context context, List<ZoneEntity> zoneEntities, ZoneListener callback) {
    db = AppDatabase.getAppDatabase(context);
    zoneEntityArrayList = zoneEntities;
    mCallback = callback;
  }

  @Override
  protected Integer doInBackground(final Integer... params) {
    Log.d(TAG, "doInBackground");
    switch (params[0]) {
      case AppUtils.MODE_INSERT:
        //db.zoneDao().deleteAll();
        db.zoneDao().insertAll(zoneEntityArrayList);
        break;
        case AppUtils.MODE_GETALL:
            zoneEntityArrayList = db.zoneDao().getAllZones();
            break;
            case AppUtils.MODE_GET:
            zoneEntityArrayList = db.zoneDao().findByZoneCode(zoneEntityArrayList.get(0).getOpco(),zoneEntityArrayList.get(0).getContractNo());
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
            if (zoneEntityArrayList != null && zoneEntityArrayList.size() > 0) {
                mCallback.onZoneReceivedSuccess(zoneEntityArrayList, AppUtils.MODE_LOCAL);
            } else
                mCallback.onZoneReceivedFailure(
                        "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
            break;

            case AppUtils.MODE_GET:
            if (zoneEntityArrayList != null && zoneEntityArrayList.size() > 0) {
                mCallback.onZoneReceivedSuccess(zoneEntityArrayList, AppUtils.MODE_LOCAL);
            } else
                mCallback.onZoneReceivedFailure(
                        "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
            break;
    }
  }
}
