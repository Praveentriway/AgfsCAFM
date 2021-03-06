package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.listeners.JobNoListener;
import com.daemon.emco_android.repository.db.entity.ContractEntity;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/** Created by vikram on 19/7/17. */
public class ContractDbInitializer extends AsyncTask<Integer, Void, Integer> {

  private final String TAG = ContractDbInitializer.class.getSimpleName();
  private List<ContractEntity> mContractList = new ArrayList<ContractEntity>();
  private JobNoListener mCallback;
  private AppDatabase db;

  public ContractDbInitializer(
          Context context, List<ContractEntity> contractEntities, JobNoListener callback) {
    db = AppDatabase.getAppDatabase(context);
    mContractList = contractEntities;
    mCallback = callback;
  }

  @Override
  protected Integer doInBackground(Integer... params) {
    Log.d(TAG, "doInBackground");
    switch (params[0]) {
      case AppUtils.MODE_INSERT:
        db.contractDao().deleteAll();
        db.contractDao().insertAll(mContractList);
        break;
      case AppUtils.MODE_GETALL:
        mContractList = db.contractDao().getAll();
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
        if (mContractList.size() > 0) {
          mCallback.onContractReceivedSuccess(mContractList, AppUtils.MODE_LOCAL);
        } else
          mCallback.onContractReceivedFailure(
              "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
        break;
    }
  }
}
