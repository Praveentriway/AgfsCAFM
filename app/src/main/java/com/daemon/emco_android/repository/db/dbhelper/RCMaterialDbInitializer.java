package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.MaterialMasterEntity;
import com.daemon.emco_android.listeners.Material_Listener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class RCMaterialDbInitializer extends AsyncTask<Integer, Void, Integer> {
  private final String TAG = RCMaterialDbInitializer.class.getSimpleName();
  private List<MaterialMasterEntity> requiredEntities = new ArrayList<>();
  private int countTotal = 0;
  private int startIndex = 0;
  private Material_Listener mCallback;
  private String searchByName;
  private AppDatabase db;

  public RCMaterialDbInitializer(
      Context context, Material_Listener callback, List<MaterialMasterEntity> entities) {
    db = AppDatabase.getAppDatabase(context);
    requiredEntities = entities;
    mCallback = callback;
  }

  public RCMaterialDbInitializer(
      Context context, Material_Listener callback, String materialName, int startIndex) {
    db = AppDatabase.getAppDatabase(context);
    searchByName = materialName;
    mCallback = callback;
    this.startIndex = startIndex;
  }

  @Override
  protected Integer doInBackground(final Integer... params) {
    switch (params[0]) {
      case AppUtils.MODE_INSERT:
        Log.d(TAG, "insertAll");
        db.rcMaterialDao().insertAll(requiredEntities);
        break;
      case AppUtils.MODE_GET:
        countTotal = db.rcMaterialDao().count("%" + searchByName + "%");
        Log.d(TAG, "'%" + searchByName + "%'" + " MODE_GET " + countTotal);

        requiredEntities =
            db.rcMaterialDao().getByMaterialName("%" + searchByName + "%", startIndex);
        // requiredEntities = db.rcMaterialDao().getByMaterialName(searchByName,startIndex);
        break;
      case AppUtils.MODE_GETALL:
        Log.d(TAG, "getAll");
        // requiredEntities = db.rcMaterialDao().getAll();
        break;
    }
    return params[0];
  }

  @Override
  protected void onPostExecute(Integer aVoid) {
    super.onPostExecute(aVoid);
    switch (aVoid) {
      case AppUtils.MODE_INSERT:
        break;
      case AppUtils.MODE_GET:
        if (requiredEntities.isEmpty()) {
          mCallback.onRCMaterialListReceivedError(
              "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
        } else {
          mCallback.onRCMaterialListReceived(
              requiredEntities, countTotal + "", AppUtils.MODE_LOCAL);
        }
        break;
      case AppUtils.MODE_GETALL:
        if (requiredEntities.isEmpty()) {
          mCallback.onRCMaterialListReceivedError(
              "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
        } else {
          mCallback.onRCMaterialListReceived(
              requiredEntities, countTotal + "", AppUtils.MODE_LOCAL);
        }
        break;
    }
  }
}
