package com.daemon.emco_android.repository.db.dbhelper;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;
import com.daemon.emco_android.listeners.ReceivecomplaintView_Listener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/** Created by vikram on 23/7/17. */
public class RCRespondDbInitializer {

  private static final String TAG = RCRespondDbInitializer.class.getSimpleName();
  private static PopulateDbAsync mTask;
  private static int mMode;
  private static boolean isInsert = false;
  private static String mComplaintNumber = null;
  private static ReceiveComplaintRespondEntity complaintRespondEntity;
  private static List<ReceiveComplaintRespondEntity> complaintRespondEntities = new ArrayList<>();
  private static ReceivecomplaintView_Listener mCallback;

  public RCRespondDbInitializer(ReceivecomplaintView_Listener callback) {
    mCallback = callback;
  }

  public static void populateAsync(
      @NonNull final AppDatabase db,
      ReceiveComplaintRespondEntity mcomplaintRespondEntity,
      int mode) {
    mTask = new PopulateDbAsync(db);
    mMode = mode;
    complaintRespondEntity = mcomplaintRespondEntity;
    mTask.execute();
  }

  public static void populateAsync(@NonNull final AppDatabase db, String logWebNo, int mode) {
    mTask = new PopulateDbAsync(db);
    mMode = mode;
    mComplaintNumber = logWebNo;
    mTask.execute();
  }

  public static void populateAsync(@NonNull final AppDatabase db, int mode) {
    mTask = new PopulateDbAsync(db);
    mMode = mode;
    mTask.execute();
  }

  private static void insertReceiveComplaint(
      final AppDatabase db, ReceiveComplaintRespondEntity complaintRespondEntity) {
    Log.d(TAG, "insertReceiveComplaint");
    try {
      mComplaintNumber = complaintRespondEntity.getComplaintNumber();
      Log.d(TAG, "insertReceiveComplaint " + mComplaintNumber);
      int count = db.rcRespondDao().countByWebNumber(complaintRespondEntity.getComplaintNumber());
      if (count > 0) Log.d(TAG, "Data already found");
      else {
        db.rcRespondDao().insertComplaint(complaintRespondEntity);
        db.receiveComplaintItemDao()
            .updateStatus(
                complaintRespondEntity.getOpco(),
                complaintRespondEntity.getComplaintNumber(),
                complaintRespondEntity.getComplaintSite());
        db.receiveComplaintViewDao()
            .updateStatus(
                complaintRespondEntity.getOpco(),
                complaintRespondEntity.getComplaintNumber(),
                complaintRespondEntity.getComplaintSite(),
                complaintRespondEntity.getResponseDetails());
      }

      if (count < db.rcRespondDao().countByWebNumber(complaintRespondEntity.getComplaintNumber()))
        isInsert = true;
      else isInsert = false;

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static void getComplaintByWebNo(final AppDatabase db, String webNumber) {
    Log.d(TAG, "getComplaintByWebNo");
    complaintRespondEntity = db.rcRespondDao().findByWebNo(webNumber);
  }

  private static void deleteComplaintByWebNumber(final AppDatabase db, String webNumber) {
    Log.d(TAG, "deleteComplaintByWebNumber");
    int count = db.rcRespondDao().countByWebNumber(webNumber);
    Log.d(TAG, "deleteComplaintByWebNumber found " + count);
    if (count > 0) {
      db.rcRespondDao().deleteSingleComplaint(webNumber);
    } else Log.d(TAG, "deleteComplaintByWebNumber not found");
  }

  private static void getAll(final AppDatabase db) {
    Log.d(TAG, "getAll");
    int count = db.rcRespondDao().count();
    if (count > 0) {
      complaintRespondEntities = db.rcRespondDao().getAll();
    } else {
      complaintRespondEntities.clear();
      Log.d(TAG, "getAll no data found ");
    }
  }

  private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

    private final AppDatabase mDb;

    PopulateDbAsync(AppDatabase db) {
      mDb = db;
    }

    @Override
    protected Void doInBackground(final Void... params) {
      switch (mMode) {
        case AppUtils.MODE_INSERT:
          insertReceiveComplaint(mDb, complaintRespondEntity);
          break;
        case AppUtils.MODE_GET:
          getComplaintByWebNo(mDb, mComplaintNumber);
          break;
        case AppUtils.MODE_DELETE:
          deleteComplaintByWebNumber(mDb, mComplaintNumber);
          break;
        case AppUtils.MODE_GETALL:
          getAll(mDb);
          break;
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      switch (mMode) {
        case AppUtils.MODE_INSERT:
          if (isInsert) {
            mCallback.onReceiveComplaintRespondReceived(
                "Data has been successfully Saved.",
                complaintRespondEntity.getComplaintNumber(),
                AppUtils.MODE_LOCAL);
          } else
            mCallback.onReceiveComplaintViewReceivedError("Data save failed", AppUtils.MODE_LOCAL);
          break;
        case AppUtils.MODE_GET:
          if (complaintRespondEntity != null)
            mCallback.onReceiveComplaintBycomplaintNumber(
                complaintRespondEntity, AppUtils.MODE_LOCAL);
          break;
        case AppUtils.MODE_DELETE:
          break;
        case AppUtils.MODE_GETALL:
          if (complaintRespondEntities.size() > 0) {
            mCallback.onAllReceiveComplaintData(complaintRespondEntities, AppUtils.MODE_LOCAL);
          } else
            // mCallback.onReceiveComplaintViewReceivedError("No data found. Please check internet
            // connection.", AppUtils.MODE_LOCAL);
            break;
      }
    }
  }
}
