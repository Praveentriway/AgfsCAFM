package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.listeners.ReceivecomplaintView_Listener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/** Created by Daemonsoft on 7/25/2017. */
public class ReceiveComplaintViewDbInitializer extends AsyncTask<Integer, Void, Integer> {

  private static final String TAG = ReceiveComplaintViewDbInitializer.class.getSimpleName();
  private List<ReceiveComplaintViewEntity> mreceiveComplaintItemEntity = new ArrayList<>();
  private ReceivecomplaintView_Listener mCallback;
  private ReceiveComplaintViewEntity receiveComplaintItemEntity;
  private AppDatabase db;

  public ReceiveComplaintViewDbInitializer(
      Context context,
      ReceivecomplaintView_Listener callback,
      List<ReceiveComplaintViewEntity> complaintItemEntities) {
    db = AppDatabase.getAppDatabase(context);
    mCallback = callback;
    mreceiveComplaintItemEntity = complaintItemEntities;
  }

  public ReceiveComplaintViewDbInitializer(
      Context context,
      ReceivecomplaintView_Listener callback,
      ReceiveComplaintViewEntity complaintItemEntity) {
    db = AppDatabase.getAppDatabase(context);
    mCallback = callback;
    receiveComplaintItemEntity = complaintItemEntity;
  }

  @Override
  protected Integer doInBackground(final Integer... params) {
    switch (params[0]) {
      case AppUtils.MODE_INSERT:
        Log.d(TAG, "insertAll");
        db.receiveComplaintViewDao().deleteAll();
        db.receiveComplaintViewDao().insertAll(mreceiveComplaintItemEntity);
        Log.d(TAG, "insertAll block");
        break;
      case AppUtils.MODE_INSERT_SINGLE:
        Log.d(TAG, "b insert Single count " + db.receiveComplaintViewDao().count());
        db.receiveComplaintViewDao().insertSingle(receiveComplaintItemEntity);
        break;
      case AppUtils.MODE_GET:
        Log.d(TAG, "getSingle " + db.receiveComplaintViewDao().count());
        mreceiveComplaintItemEntity =
            db.receiveComplaintViewDao()
                .getReceiveComplaintViewEntity(
                    receiveComplaintItemEntity.getOpco(),
                    receiveComplaintItemEntity.getComplaintSite(),
                    receiveComplaintItemEntity.getComplaintNumber());
        break;
      case AppUtils.MODE_GETALL:
        Log.d(TAG, "getAll");
        mreceiveComplaintItemEntity = db.receiveComplaintViewDao().getAll(AppUtils.MODE_LOCAL);
        break;
    }
    return params[0];
  }

  @Override
  protected void onPostExecute(Integer params) {
    super.onPostExecute(params);
    switch (params) {
      case AppUtils.MODE_INSERT:
        break;
      case AppUtils.MODE_INSERT_SINGLE:
        break;
      case AppUtils.MODE_GET:
        if (mreceiveComplaintItemEntity == null || mreceiveComplaintItemEntity.isEmpty()) {
          mCallback.onReceiveComplaintViewReceivedError(
              "No data found for Complaint Number : "
                  + receiveComplaintItemEntity.getComplaintNumber(),
              AppUtils.MODE_LOCAL);
        } else {
          Log.d(TAG, "getSingle " + mreceiveComplaintItemEntity.size());
          mCallback.onReceiveComplaintViewReceived(
              mreceiveComplaintItemEntity, AppUtils.MODE_LOCAL);
        }
        break;
      case AppUtils.MODE_GETALL:
        mCallback.onReceiveComplaintViewReceived(mreceiveComplaintItemEntity, AppUtils.MODE_LOCAL);
        break;
    }
  }
}
