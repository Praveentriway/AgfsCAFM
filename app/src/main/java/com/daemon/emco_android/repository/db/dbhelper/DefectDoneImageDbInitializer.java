package com.daemon.emco_android.repository.db.dbhelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;
import com.daemon.emco_android.listeners.DefectDoneImage_Listener;
import com.daemon.emco_android.model.response.DefectDoneImageUploaded;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikram on 21/8/17.
 */
public class DefectDoneImageDbInitializer extends AsyncTask<Integer, Void, Integer> {
    private String TAG = DefectDoneImageDbInitializer.class.getSimpleName();
    private boolean isInsert = false;
    private DFoundWDoneImageEntity mImageEntity;
    private List<DFoundWDoneImageEntity> mImageEntities = new ArrayList<>();
    private DefectDoneImage_Listener mCallback;
    private AppDatabase mDb;
    private String Dtype;

    public DefectDoneImageDbInitializer(
            Context context, DFoundWDoneImageEntity imageEntity, DefectDoneImage_Listener callback) {
        mDb = AppDatabase.getAppDatabase(context);
        if (mImageEntity != null) {
            mImageEntity = imageEntity;
            Dtype = mImageEntity.getDocType();
        }
        mCallback = callback;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            int count;
            switch (integers[0]) {
                case AppUtils.MODE_INSERT:
                    Log.d(TAG, "insertImageComplaint");
                    isInsert = false;
                    mDb.imageUploadDao().insertComplaintImage(mImageEntity);
                    isInsert = true;
                    break;

                case AppUtils.MODE_UPDATE:
                  //  Log.d(TAG, "updaeImageComplaint " + mImageEntity.getId());
                    count = mDb.imageUploadDao().countBycomplaintNo(mImageEntity.getId());
                    if (count > 0) mDb.imageUploadDao().updateComplaintImage(mImageEntity);
                    else Log.d(TAG, "Data not found");
                    break;

                case AppUtils.MODE_GET:
                    //    Log.d(TAG, "getComplaintByWebNo " + mImageEntity.getId());
                    if (mImageEntity != null) {
                        mImageEntity = mDb.imageUploadDao().findByWebNo(mImageEntity.getId());
                    }
                    break;

                case AppUtils.MODE_DELETE:
                    Log.d(TAG, "deleteComplaintByWebNumber");
                    count = mDb.imageUploadDao().countBycomplaintNo(mImageEntity.getId());
                    if (count > 0) {
                        mDb.imageUploadDao().deleteSingleComplaint(mImageEntity.getId());
                    } else Log.d(TAG, "deleteComplaintByWebNumber not found");

                    break;

                case AppUtils.MODE_GETALL:
                    Log.d(TAG, "getAll");
                    count = mDb.imageUploadDao().count();
                    if (count > 0) {
                        mImageEntities = mDb.imageUploadDao().getAll();
                    } else {
                        mImageEntities.clear();
                        Log.d(TAG, "getAll no data found ");
                    }
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return integers[0];
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        switch (integer) {
            case AppUtils.MODE_INSERT:
                if (isInsert) {
                    mCallback.onImageSaveReceivedSuccess(
                            new DefectDoneImageUploaded(mImageEntity.getComplaintNo(), mImageEntity.getDocType()),
                            AppUtils.MODE_LOCAL);
                } else
                    mCallback.onImageSaveReceivedFailure("Data save failed", AppUtils.MODE_LOCAL);
                break;

            case AppUtils.MODE_GET:
                if (mImageEntity != null) {
//                    mCallback.onImageReceivedSuccess(
//                            new RCDownloadImage(mImageEntity.getBase64Image(), mImageEntity.getDocType(),0),
//                            AppUtils.MODE_LOCAL);
                } else {
                    mCallback.onImageReceivedFailure(Dtype, AppUtils.MODE_LOCAL);
                }
                break;

            case AppUtils.MODE_GETALL:
                if (mImageEntities.size() > 0) {
                    mCallback.onAllImagesReceived(mImageEntities, AppUtils.MODE_LOCAL);
                }
                break;
        }
    }
}
