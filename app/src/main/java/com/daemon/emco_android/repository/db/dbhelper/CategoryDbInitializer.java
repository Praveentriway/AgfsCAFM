package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.CategoryEntity;
import com.daemon.emco_android.listeners.CategoryListener;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryDbInitializer extends AsyncTask<Integer, Void, Integer> {

  private final String TAG = CategoryDbInitializer.class.getSimpleName();
  private List<CategoryEntity> mCategoryList = new ArrayList<>();
  private CategoryListener mCallback;
  private AppDatabase db;

  public CategoryDbInitializer(
      Context context, List<CategoryEntity> categoryEntities, CategoryListener callback) {
    db = AppDatabase.getAppDatabase(context);
    mCategoryList = categoryEntities;
    mCallback = callback;
  }

  @Override
  protected Integer doInBackground(final Integer... params) {
    Log.d(TAG, "doInBackground");
    switch (params[0]) {
      case AppUtils.MODE_INSERT:
        db.categoryDao().deleteAll();
        db.categoryDao().insertAll(mCategoryList);
        break;
      case AppUtils.MODE_GETALL:
        mCategoryList = db.categoryDao().getAll();
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
        if (mCategoryList.size() > 0) {
          mCallback.onCategoryReceivedSuccess(mCategoryList, AppUtils.MODE_LOCAL);
        } else
          mCallback.onCategoryReceivedFailure(
              "No data found. Please check internet connection.", AppUtils.MODE_LOCAL);
        break;
    }
  }
}
