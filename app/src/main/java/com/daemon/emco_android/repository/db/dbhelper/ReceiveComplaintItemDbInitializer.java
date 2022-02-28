package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.daemon.emco_android.listeners.ReceivecomplaintList_Listener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/** Created by Daemonsoft on 7/25/2017. */
public class ReceiveComplaintItemDbInitializer extends AsyncTask<Integer, Void, Integer> {

  private final String TAG = ReceiveComplaintItemDbInitializer.class.getSimpleName();
  private List<ReceiveComplaintItemEntity> mreceiveComplaintItemEntity = new ArrayList<>();
  private ReceivecomplaintList_Listener mCallback;
  private AppDatabase db;
  private int countTotal = 0;
  private int startIndex = 0;
  // private ComplaintStatusEntity complaintStatusEntity_;

  public ReceiveComplaintItemDbInitializer(
      Context context,
      List<ReceiveComplaintItemEntity> complaintItemEntities,
      ReceivecomplaintList_Listener callback) {
    db = AppDatabase.getAppDatabase(context);
    mreceiveComplaintItemEntity = complaintItemEntities;
    mCallback = callback;
  }

  /* public ReceiveComplaintItemDbInitializer(
          Context context,
        ComplaintStatusEntity complaintStatusEntity,
          ReceivecomplaintList_Listener callback) {
    db = AppDatabase.getAppDatabase(context);
    mCallback = callback;
    complaintStatusEntity_=complaintStatusEntity;
  }*/

  public ReceiveComplaintItemDbInitializer(
      Context context, int startIndex, ReceivecomplaintList_Listener callback) {
    db = AppDatabase.getAppDatabase(context);
    this.startIndex = startIndex;
    mCallback = callback;
  }

  private void deleteAll() {
    Log.d(TAG, " b deleteAll " + db.receiveComplaintItemDao().count());

    /* int count = db.receiveComplaintItemDao().count();
    if (count != mreceiveComplaintItemEntity.size()) {
      deleteAll();
      db.receiveComplaintItemDao().insert(mreceiveComplaintItemEntity);
      Log.d(TAG, " a insertAll " + db.receiveComplaintItemDao().count());
    }*/

    db.receiveComplaintItemDao().deleteAll();
    db.receiveComplaintViewDao().deleteAll();
    db.rcViewAssetDetailsDao().deleteAll();
    db.rcRespondDao().deleteAll();
    db.imageUploadDao().deleteAll();
    db.ppeFetchSaveDao().deleteAll();
    db.rcMaterialDao().deleteAll();
    db.rcSavedMaterialDao().deleteAll();
    db.saveFeedbackDao().deleteAll();
    // Log.d(TAG, " a deleteAll " + db.receiveComplaintItemDao().count());
  }

  @Override
  protected Integer doInBackground(final Integer... params) {
    // populateWithTestData(mDb);
    switch (params[0]) {
      case AppUtils.MODE_INSERT:
        int count = db.receiveComplaintItemDao().count();
        if (count != mreceiveComplaintItemEntity.size()) {
          db.receiveComplaintItemDao().deleteAll();
          db.receiveComplaintItemDao().insert(mreceiveComplaintItemEntity);
          /*if(complaintStatusEntity_!=null){
            db.receiveComplaintViewDaoComplaint().insertAll(complaintStatusEntity_);
          }*/
          Log.d(TAG, " a insertAll " + db.receiveComplaintItemDao().count());
        } else {
          Log.d(TAG, " b insertAll " + db.receiveComplaintItemDao().count());
          db.receiveComplaintItemDao().insert(mreceiveComplaintItemEntity);
          Log.d(TAG, " a insertAll " + db.receiveComplaintItemDao().count());
        }

        break;
      case AppUtils.MODE_INSERT_SINGLE:
        Log.d(TAG, " b insertSingle " + db.receiveComplaintItemDao().count());
        db.receiveComplaintItemDao().insert(mreceiveComplaintItemEntity);
        break;
      case AppUtils.MODE_GET:
        countTotal = db.receiveComplaintItemDao().count();
        mreceiveComplaintItemEntity = db.receiveComplaintItemDao().getList(startIndex);
        break;
      case AppUtils.MODE_GETALL:
        Log.d(TAG, "getAll");
        countTotal = db.receiveComplaintItemDao().count();
        mreceiveComplaintItemEntity = db.receiveComplaintItemDao().getAll();
        //  complaintStatusEntity_=db.receiveComplaintViewDaoComplaint().getAll();
        break;
      case AppUtils.MODE_DELETE:
        db.clearAllTables();
        // deleteAll();
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
        if (mreceiveComplaintItemEntity.isEmpty()) {
          mCallback.onReceiveComplaintListReceivedError(
              "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
        } else {
          mCallback.onReceiveComplaintListReceived(
              mreceiveComplaintItemEntity, countTotal + "", AppUtils.MODE_LOCAL);
        }
        break;
      case AppUtils.MODE_GETALL:
        if (mreceiveComplaintItemEntity.isEmpty()) {
          mCallback.onReceiveComplaintListReceived(
              mreceiveComplaintItemEntity, countTotal + "", AppUtils.MODE_LOCAL);
        }
        break;
    }
  }
}
