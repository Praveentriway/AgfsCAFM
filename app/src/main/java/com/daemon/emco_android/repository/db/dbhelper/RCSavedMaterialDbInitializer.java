package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.SaveMaterialEntity;
import com.daemon.emco_android.listeners.Material_Listener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class RCSavedMaterialDbInitializer extends AsyncTask<Integer, Void, Integer> {
  private final String TAG = RCSavedMaterialDbInitializer.class.getSimpleName();
  private List<SaveMaterialEntity> requiredEntities = new ArrayList<>();
  private int countTotal = 0;
  private int startIndex = 0;
  private Material_Listener mCallback;
  private String complainNumber, transactionType;
  private AppDatabase db;
  private String complainSite;
  private String companyCode;

  public RCSavedMaterialDbInitializer(
      Context context, Material_Listener callback, List<SaveMaterialEntity> entities) {
    db = AppDatabase.getAppDatabase(context);
    requiredEntities = entities;
    mCallback = callback;
  }

  public RCSavedMaterialDbInitializer(
      Context context,
      Material_Listener callback,
      String mTransactionType,
      String mComplainNumber,
      String complainSite,
      String companyCode,
      int mStartIndex) {
    db = AppDatabase.getAppDatabase(context);
    complainNumber = mComplainNumber;
    transactionType = mTransactionType;
    mCallback = callback;
    this.companyCode = companyCode;
    this.complainSite = complainSite;
    startIndex = mStartIndex;
  }

  @Override
  protected Integer doInBackground(final Integer... params) {
    switch (params[0]) {
      case AppUtils.MODE_INSERT:
        Log.d(TAG, "insertAll");
        db.rcSavedMaterialDao().deleteAll();
        db.rcSavedMaterialDao().insertAll(requiredEntities);
        break;
      case AppUtils.MODE_INSERT_SINGLE:
        Log.d(TAG, "insertAll");
        db.rcSavedMaterialDao().insertAll(requiredEntities);
        break;
      case AppUtils.MODE_GET:
        countTotal =
            db.rcSavedMaterialDao()
                .countBY(transactionType, complainNumber, complainSite, companyCode);
        requiredEntities =
            db.rcSavedMaterialDao()
                .getMaterialBY(
                    transactionType, complainNumber, complainSite, companyCode, startIndex);
        break;
      case AppUtils.MODE_UPDATE:
        db.rcSavedMaterialDao().updateAll(AppUtils.MODE_LOCAL, AppUtils.MODE_SERVER);
        break;
      case AppUtils.MODE_GETALL:
        Log.d(TAG, "getAll");
        requiredEntities = db.rcSavedMaterialDao().getAll(AppUtils.MODE_LOCAL);
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
          mCallback.onRCMaterialGetListReceived(
              requiredEntities, requiredEntities.size() + "", AppUtils.MODE_LOCAL);
        }
        break;
      case AppUtils.MODE_GETALL:
        if (requiredEntities.isEmpty()) {
          mCallback.onRCMaterialListReceivedError(
              "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
        } else {
          mCallback.onRCMaterialGetListReceived(
              requiredEntities, countTotal + "", AppUtils.MODE_LOCAL);
        }
        break;
    }
  }
}
