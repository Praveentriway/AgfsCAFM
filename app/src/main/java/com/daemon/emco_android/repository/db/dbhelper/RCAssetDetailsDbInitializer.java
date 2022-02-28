package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.listeners.ReceivecomplaintView_Listener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/** Created by Daemonsoft on 7/25/2017. */
public class RCAssetDetailsDbInitializer extends AsyncTask<Integer, Void, Integer> {

  private final String TAG = RCAssetDetailsDbInitializer.class.getSimpleName();
  private List<AssetDetailsEntity> assetDetailsEntities = new ArrayList<>();
  private ReceivecomplaintView_Listener mCallback;
  private AssetDetailsEntity assetDetailsEntity;
  private AppDatabase db;

  public RCAssetDetailsDbInitializer(
      Context context,
      ReceivecomplaintView_Listener callback,
      List<AssetDetailsEntity> massetDetailsEntities) {
    db = AppDatabase.getAppDatabase(context);
    mCallback = callback;
    assetDetailsEntities = massetDetailsEntities;
  }

  public RCAssetDetailsDbInitializer(
      Context context,
      ReceivecomplaintView_Listener callback,
      AssetDetailsEntity massetDetailsEntity) {
    db = AppDatabase.getAppDatabase(context);
    mCallback = callback;
    assetDetailsEntity = massetDetailsEntity;
  }

  @Override
  protected Integer doInBackground(final Integer... params) {
    // populateWithTestData(mDb);
    switch (params[0]) {
      case AppUtils.MODE_INSERT:
        Log.d(TAG, "insertAll");
        db.rcViewAssetDetailsDao().deleteAll();
        db.rcViewAssetDetailsDao().insertAll(assetDetailsEntities);
        Log.d(TAG, "insertAll block");
        break;
      case AppUtils.MODE_INSERT_SINGLE:
        Log.d(TAG, "insertSingle" + db.rcViewAssetDetailsDao().count());
        db.rcViewAssetDetailsDao().insertSingle(assetDetailsEntity);
        break;
      case AppUtils.MODE_GET:
        Log.d(TAG, "getSingle");
        assetDetailsEntities =
            db.rcViewAssetDetailsDao()
                .getReceiveComplaintViewAssetDetailsEntity(
                    "%" + assetDetailsEntity.getAssetBarCode() + "%",
                    assetDetailsEntity.getOpco(),
                    assetDetailsEntity.getJobNo(),
                    assetDetailsEntity.getZoneCode(),
                    assetDetailsEntity.getBuildingCode());
        break;
      case AppUtils.MODE_GETALL:
        Log.d(TAG, "getAll");
        assetDetailsEntities = db.rcViewAssetDetailsDao().getAll();
        break;
    }
    return params[0];
  }

  @Override
  protected void onPostExecute(Integer integer) {
    super.onPostExecute(integer);
    switch (integer) {
      case AppUtils.MODE_INSERT:
        break;
      case AppUtils.MODE_INSERT_SINGLE:
        break;
      case AppUtils.MODE_GET:
        if (assetDetailsEntities == null || assetDetailsEntities.isEmpty()) {
          mCallback.onReceiveComplaintViewReceivedError("No data found..", AppUtils.MODE_LOCAL);
        } else {
          mCallback.onReceiveComplaintViewAssetDetailsReceived(
              assetDetailsEntities, AppUtils.MODE_LOCAL);
          Log.d(TAG, "getSingle block " + assetDetailsEntities.size());
        }
        break;
      case AppUtils.MODE_GETALL:
        mCallback.onReceiveComplaintViewAssetDetailsReceived(
            assetDetailsEntities, AppUtils.MODE_LOCAL);
        break;
    }
  }
}
