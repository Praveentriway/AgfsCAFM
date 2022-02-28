package com.daemon.emco_android.service;

import android.app.IntentService;
import android.app.NotificationManager;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.activities.MainActivity;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.dbhelper.FbEmployeeDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.FeedbackDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.PPEFetchSaveDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.PPENamesDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.RCAssetDetailsDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.RCMaterialDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.RCSavedMaterialDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ReceiveComplaintItemDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ReceiveComplaintViewDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.WorkDefectsDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.WorkDoneDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.WorkPendingReasonDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.WorkStatusDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ZoneDbInitializer;
import com.daemon.emco_android.repository.db.entity.RCViewRemarksEntity;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.model.common.Download;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.model.response.AllFeedbackDetailsResponse;
import com.daemon.emco_android.model.response.AssetDetailsResponse;
import com.daemon.emco_android.model.response.CustomerRemarksResponse;
import com.daemon.emco_android.model.response.FeedbackEmployeeDetailsResponse;
import com.daemon.emco_android.model.response.FetchPPENameResponse;
import com.daemon.emco_android.model.response.GetMaterialResponse;
import com.daemon.emco_android.model.response.LCUserInput;
import com.daemon.emco_android.model.response.LCUserInputResponse;
import com.daemon.emco_android.model.response.MaterialRequiredResponse;
import com.daemon.emco_android.model.response.PpeAfterSaveResponse;
import com.daemon.emco_android.model.response.ReceiveComplaintResponse;
import com.daemon.emco_android.model.response.ReceiveComplaintViewResponse;
import com.daemon.emco_android.model.response.TechnicalRemarksResponse;
import com.daemon.emco_android.model.response.WorkDefectResponse;
import com.daemon.emco_android.model.response.WorkDoneResponse;
import com.daemon.emco_android.model.response.WorkPendingReasonResponse;
import com.daemon.emco_android.model.response.WorkStatusResponse;
import com.daemon.emco_android.model.response.ZoneResponse;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadService extends IntentService {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static NotificationCompat.Builder notificationBuilder;
    public static NotificationManager notificationManager;
    public static double totalFileSize;
    private static Context mContext;
    String strOPCo = "";
    int mCurrentnoOfRows = 0;
    int mmper = 1;
    private ApiInterface mInterface;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Login mUserData;
    private Gson gson = new Gson();
    private String mStrEmployeeId;
    private int checkcount = 0;
    private int checkcountloop = 5;

    public DownloadService() {
        super("Download Service");
    }

    public static void onStartDownload() {
        try {
            ApiInterface mInterface =
                    ApiClient.getClientLongTime(5, SessionManager.getSession("baseurlfile", mContext))
                            .create(ApiInterface.class);
            Call<ResponseBody> request = mInterface.downloadFile();

            sendNotification(90);
            downloadFile(request.execute().body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNotification(int per) {
        Log.d(TAG, "sendNotification");

        notificationBuilder.setProgress(100, per, false);
        notificationBuilder.setContentText("Data Downloading " + per + " % ");
        notificationManager.notify(0, notificationBuilder.build());
    }

    public static void sendNotification(String msg, int per) {
        Log.d(TAG, "sendNotification");

        notificationBuilder.setProgress(100, per, false);
        notificationBuilder.setContentText(msg + " Data Downloading " + per + " % ");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private static void onDownloadComplete() {
        Log.d(TAG, "onDownloadComplete");

        try {
            Download download = new Download();
            download.setProgress(100);
            notificationManager.cancel(0);
            notificationBuilder.setProgress(0, 0, false);
            notificationBuilder.setContentText("Data Downloaded");
            notificationManager.notify(0, notificationBuilder.build());
            notificationManager.cancel(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeSQLscript(File file) {
        Log.d(TAG, "executeSQLscript");
        try {
            InputStream is = null;

            // is = mContext.getAssets().open("MaterialMaster.sql");
            // is = new FileInputStream(file);

            Writer writer = new StringWriter();
            char[] buffer = new char[2048];
            try {
                // Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                Reader reader =
                        new BufferedReader(
                                new InputStreamReader(mContext.getAssets().open("MaterialMasters.sql"), "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                // is.close();
            }
            SupportSQLiteDatabase db =
                    AppDatabase.getAppDatabase(mContext).getOpenHelper().getWritableDatabase();

            Log.d(TAG, "openOrCreateDatabase " + db.isOpen());
            db.beginTransaction();
            db.query(writer.toString());
            sendNotification(99);
            onDownloadComplete();
            db.endTransaction();

            Cursor cursor = db.query("SELECT COUNT(*) from MaterialMaster");
            int count;
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            } else {
                count = 0;
            }
            Log.d(TAG, "count " + count);
            // Delete file
      /*file.delete();
      if (file.exists()) {
        file.getCanonicalFile().delete();
        if (file.exists()) {
          mContext.deleteFile(file.getName());
        }
      }
      if (file.delete()) {
        Log.d(TAG, "file Deleted :" + file.getPath());
      } else {
        Log.d(TAG, "file not Deleted :" + file.getPath());
      }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadFile(ResponseBody body) {
        Log.d(TAG, "downloadFile");
        try {
            int count;
            byte data[] = new byte[1024 * 4];
            long fileSize = body.contentLength();
            InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);

            String PATH = Environment.getExternalStorageDirectory() + "/download";
            File dir = new File(PATH);
            dir.mkdirs();

            File outputFile = new File(dir, "MaterialMaster.sql");
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            Log.d(TAG, "PATH " + PATH + "\ngetAbsolutePath " + outputFile.getAbsolutePath());
            OutputStream output = new FileOutputStream(outputFile);
            long total = 0;
            long startTime = System.currentTimeMillis();
            int timeCount = 1;
            while ((count = bis.read(data)) != -1) {
                total += count;
                totalFileSize = fileSize / (Math.pow(1024, 2));
                double current = total / (Math.pow(1024, 2));

                int progress = (int) ((total * 100) / fileSize);

                long currentTime = System.currentTimeMillis() - startTime;

                Download download = new Download();
                download.setTotalFileSize(totalFileSize);

                if (currentTime > 1000 * timeCount) {

                    download.setCurrentFileSize(current);
                    download.setProgress(progress);
                    sendNotification(download);
                    timeCount++;
                }

                output.write(data, 0, count);
            }
            onDownloadComplete();
            output.flush();
            output.close();
            bis.close();
            executeSQLscript(outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNotification(Download download) {
        Log.d(TAG, "sendNotification");
        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText(
                "Downloading data "
                        + new DecimalFormat("##.##").format(download.getCurrentFileSize())
                        + "/"
                        + new DecimalFormat("##.##").format(totalFileSize)
                        + " MB");
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");

        mContext = getApplicationContext();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_download)
                        // .setContentTitle("Material data download")
                        .setContentText("Fetching Data")
                        .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        // mActivity = (AppCompatActivity) getApplicationContext();

        initDownload();
    }

    private void initDownload() {
        Log.d(TAG, "initDownload");
        try {
            mPreferences = mContext.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            String mStrLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mStrLoginData != null) {
                mUserData = gson.fromJson(mStrLoginData, Login.class);
                mStrEmployeeId =
                        mUserData.getEmployeeId() == null ? mUserData.getUserName() : mUserData.getEmployeeId();
            }
            sendNotification(10);
            mInterface =
                    ApiClient.getClientLongTime(7, SessionManager.getSession("baseurl", mContext))
                            .create(ApiInterface.class);
            GetAllZoneData();
            // onStartDownload();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved");
        notificationManager.cancel(0);
    }

    public void GetAllZoneData() {
        Log.d(TAG, "GetAllZoneData");
        try {
            mInterface
                    .getAllZoneListResult(mStrEmployeeId)
                    .enqueue(
                            new Callback<ZoneResponse>() {
                                @Override
                                public void onResponse(Call<ZoneResponse> call, Response<ZoneResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse success");
                                            if (!response.body().getObject().isEmpty()) {
                                                strOPCo = response.body().getObject().get(0).getOpco();
                                                new ZoneDbInitializer(mContext, response.body().getObject(), null)
                                                        .execute(AppUtils.MODE_INSERT);
                                            }
                                        }
                                    }
                                    sendNotification(20);
                                    GetAllReceiveComplaintListData();
                                }

                                @Override
                                public void onFailure(Call<ZoneResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    sendNotification(20);
                                    GetAllReceiveComplaintListData();
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetAllReceiveComplaintListData() {
        Log.d(TAG, "GetAllReceiveComplaintListData");
        Log.d("firstAA","SDASDA"+mStrEmployeeId);
        try {
            mInterface
                    .getReceiveComplaintListResult(mStrEmployeeId, "A", 0, 100)
                    .enqueue(
                            new Callback<ReceiveComplaintResponse>() {
                                @Override
                                public void onResponse(
                                        Call<ReceiveComplaintResponse> call,
                                        Response<ReceiveComplaintResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse success");
                                            if (response.body().getReceiveComplaintItem().isEmpty()) {
                                                return;
                                            } else {
                                                strOPCo = response.body().getReceiveComplaintItem().get(0).getOpco();
                                                new ReceiveComplaintItemDbInitializer(
                                                        mContext, response.body().getReceiveComplaintItem(), null)
                                                        .execute(AppUtils.MODE_INSERT);
                                            }
                                        }
                                    }
                                    sendNotification(30);
                                    GetAllReceiveComplaintViewData();
                                    //GetAllReceiveComplaintViewDataNew(5);
                                }

                                @Override
                                public void onFailure(Call<ReceiveComplaintResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    sendNotification(30);
                                    GetAllReceiveComplaintViewData();
                                    //GetAllReceiveComplaintViewDataNew(5);
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetAllReceiveComplaintViewData() {
        try {
            mInterface
                    .getAllReceiveComplaintView_Result(mStrEmployeeId)
                    .enqueue(
                            new Callback<ReceiveComplaintViewResponse>() {
                                @Override
                                public void onResponse(
                                        Call<ReceiveComplaintViewResponse> call,
                                        Response<ReceiveComplaintViewResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse success");
                                            if (response.body().getReceiveComplaintViewEntity().isEmpty())
                                                return;
                                            new ReceiveComplaintViewDbInitializer(
                                                    mContext, null, response.body().getReceiveComplaintViewEntity())
                                                    .execute(AppUtils.MODE_INSERT);
                                        }
                                    }
                                    sendNotification(35);
                                    GetAllAssetBArCodeDetailsData();
                                }

                                @Override
                                public void onFailure(Call<ReceiveComplaintViewResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    sendNotification(35);
                                    GetAllAssetBArCodeDetailsData();
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetAllAssetBArCodeDetailsData() {
        Log.d(TAG, "GetReceiveComplaintViewAssetDetailsData");
        try {
            mInterface
                    .getAllAssetBarcodeDetailsResult()
                    .enqueue(
                            new Callback<AssetDetailsResponse>() {
                                @Override
                                public void onResponse(
                                        Call<AssetDetailsResponse> call, Response<AssetDetailsResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            new RCAssetDetailsDbInitializer(
                                                    mContext, null, response.body().getAssetDetailsEntity())
                                                    .execute(AppUtils.MODE_INSERT);

                                            sendNotification(45);
                                            getAllFeedbackEmplyeeData();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<AssetDetailsResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    sendNotification(45);
                                    getAllFeedbackEmplyeeData();
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAllFeedbackEmplyeeData() {
        Log.d(TAG, "getAllFeedbackEmplyeeData");
        try {
            mInterface
                    .getAllFeedbackEmployeeDetailsResult()
                    .enqueue(
                            new Callback<FeedbackEmployeeDetailsResponse>() {
                                @Override
                                public void onResponse(
                                        Call<FeedbackEmployeeDetailsResponse> call,
                                        Response<FeedbackEmployeeDetailsResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        FeedbackEmployeeDetailsResponse mResponse = response.body();
                                        if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                                            new FbEmployeeDbInitializer(mContext, null, mResponse.getObject())
                                                    .execute(AppUtils.MODE_INSERT);
                                        }

                                        sendNotification(50);
                                        getAllSavedFeedbackData();
                                    }
                                }

                                @Override
                                public void onFailure(Call<FeedbackEmployeeDetailsResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure :::" + t.getMessage());
                                    sendNotification(50);
                                    getAllSavedFeedbackData();
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getAllSavedFeedbackData() {
        Log.d(TAG, "getAllSavedFeedbackData");
        try {
            mInterface
                    .getAllSavedFeedbackDetailsResult(mStrEmployeeId)
                    .enqueue(
                            new Callback<AllFeedbackDetailsResponse>() {
                                @Override
                                public void onResponse(
                                        Call<AllFeedbackDetailsResponse> call,
                                        Response<AllFeedbackDetailsResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        AllFeedbackDetailsResponse mResponse = response.body();
                                        if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                                            new FeedbackDbInitializer(mContext, null, mResponse.getObject())
                                                    .execute(AppUtils.MODE_INSERT);
                                        }
                                        sendNotification(55);
                                        getAllWorkDefectData();
                                    }
                                }

                                @Override
                                public void onFailure(Call<AllFeedbackDetailsResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure :::" + t.getMessage());
                                    sendNotification(55);
                                    getAllWorkDefectData();
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getAllWorkDefectData() {
        Log.d(TAG, "getWorkDefectData");
        try {
            mInterface
                    .getAllWorkDefectsData()
                    .enqueue(
                            new Callback<WorkDefectResponse>() {
                                @Override
                                public void onResponse(
                                        Call<WorkDefectResponse> call, Response<WorkDefectResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        WorkDefectResponse mResponse = response.body();
                                        if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                                            new WorkDefectsDbInitializer(mContext, null, mResponse.getObject())
                                                    .execute(AppUtils.MODE_INSERT);
                                        }

                                        sendNotification(60);
                                        getPpeNamesData();
                                    }
                                }

                                @Override
                                public void onFailure(Call<WorkDefectResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure :::" + t.getMessage());
                                    sendNotification(60);
                                    getPpeNamesData();
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getPpeNamesData() {
        Log.d(TAG, "getPpeNamesData");
        try {
            mInterface
                    .getPPENameData()
                    .enqueue(
                            new Callback<FetchPPENameResponse>() {
                                @Override
                                public void onResponse(
                                        Call<FetchPPENameResponse> call, Response<FetchPPENameResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        FetchPPENameResponse mResponse = response.body();
                                        if (mResponse.getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            new PPENamesDbInitializer(null)
                                                    .populateAsync(
                                                            AppDatabase.getAppDatabase(mContext),
                                                            mResponse.getObject(),
                                                            AppUtils.MODE_INSERT);
                                        }
                                    }
                                    sendNotification(63);
                                    fetchPPEData();
                                }

                                @Override
                                public void onFailure(Call<FetchPPENameResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    sendNotification(62);
                                    fetchPPEData();
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void fetchPPEData() {
        Log.d(TAG, "getPpeNamesData");
        try {
            mInterface
                    .getAfterAllSavePPEData(mStrEmployeeId)
                    .enqueue(
                            new Callback<PpeAfterSaveResponse>() {
                                @Override
                                public void onResponse(
                                        Call<PpeAfterSaveResponse> call, Response<PpeAfterSaveResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            new PPEFetchSaveDbInitializer(null)
                                                    .populateAsync(
                                                            AppDatabase.getAppDatabase(mContext),
                                                            response.body().getObject(),
                                                            AppUtils.MODE_INSERT);
                                        }
                                    }
                                    sendNotification(65);
                                    getWorkPendingReasonData();
                                }

                                @Override
                                public void onFailure(Call<PpeAfterSaveResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    sendNotification(65);
                                    getWorkPendingReasonData();
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getWorkPendingReasonData() {
        Log.d(TAG, "getWorkPendingReasonData");
        try {
            mInterface
                    .getWorkPendingReasonData()
                    .enqueue(
                            new Callback<WorkPendingReasonResponse>() {
                                @Override
                                public void onResponse(
                                        Call<WorkPendingReasonResponse> call,
                                        Response<WorkPendingReasonResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        WorkPendingReasonResponse mResponse = response.body();
                                        if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                                            new WorkPendingReasonDbInitializer(null)
                                                    .populateAsync(
                                                            AppDatabase.getAppDatabase(mContext),
                                                            mResponse.getObject(),
                                                            AppUtils.MODE_INSERT);
                                        }
                                    }
                                    sendNotification(68);
                                    getWorkStatusData();
                                }

                                @Override
                                public void onFailure(Call<WorkPendingReasonResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure :::" + t.getMessage());
                                    sendNotification(68);
                                    getWorkStatusData();
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getWorkStatusData() {
        Log.d(TAG, "getWorkStatusData");
        try {
            mInterface
                    .getWorkStatusData()
                    .enqueue(
                            new Callback<WorkStatusResponse>() {
                                @Override
                                public void onResponse(
                                        Call<WorkStatusResponse> call, Response<WorkStatusResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        WorkStatusResponse mResponse = response.body();
                                        if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                                            new WorkStatusDbInitializer(null)
                                                    .populateAsync(
                                                            AppDatabase.getAppDatabase(mContext),
                                                            mResponse.getObject(),
                                                            AppUtils.MODE_INSERT);
                                        }
                                    }
                                    sendNotification(69);
                                    getAllWorkDoneData();
                                }

                                @Override
                                public void onFailure(Call<WorkStatusResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure :::" + t.getMessage());
                                    sendNotification(69);
                                    getAllWorkDoneData();
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getAllWorkDoneData() {
        Log.d(TAG, "getWorkDoneData");
        try {
            mInterface
                    .getAllWorkDoneData()
                    .enqueue(
                            new Callback<WorkDoneResponse>() {
                                @Override
                                public void onResponse(
                                        Call<WorkDoneResponse> call, Response<WorkDoneResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        WorkDoneResponse mResponse = response.body();
                                        if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                                            new WorkDoneDbInitializer(mContext, null, mResponse.getObject())
                                                    .execute(AppUtils.MODE_INSERT);
                                        }
                                    }
                                    sendNotification(70);
                                    getLCUserInputResult();
                                }

                                @Override
                                public void onFailure(Call<WorkDoneResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure :::" + t.getMessage());
                                    sendNotification(70);
                                    getLCUserInputResult();
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getLCUserInputResult() {
        Log.d(TAG, "getWorkDoneData");
        try {
            mInterface
                    .getLCUserInputResult(new EmployeeIdRequest(null, mStrEmployeeId, null, null))
                    .enqueue(
                            new Callback<LCUserInputResponse>() {
                                @Override
                                public void onResponse(
                                        Call<LCUserInputResponse> call, Response<LCUserInputResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        LCUserInputResponse mResponse = response.body();
                                        if (mResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                                            // save data
                                            String strlc =
                                                    gson.toJson(
                                                            mResponse.getObject(),
                                                            new TypeToken<List<LCUserInput>>() {
                                                            }.getType());
                                            mEditor.putString(AppUtils.SHARED_CUSTOMER_LC, strlc);
                                            mEditor.commit();
                                        }
                                    }
                                    sendNotification(72);
                                    getTechnicalRemarksData();
                                }

                                @Override
                                public void onFailure(Call<LCUserInputResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure :::" + t.getMessage());
                                    sendNotification(72);
                                    getTechnicalRemarksData();
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getTechnicalRemarksData() {
        Log.d(TAG, "getTechnicalRemarksData");
        try {
            mInterface
                    .getTechRemarksData()
                    .enqueue(
                            new Callback<TechnicalRemarksResponse>() {
                                @Override
                                public void onResponse(
                                        Call<TechnicalRemarksResponse> call,
                                        Response<TechnicalRemarksResponse> response) {
                                    Log.d(TAG, "onResponse");
                                    if (response.isSuccessful()) {
                                        TechnicalRemarksResponse techRemarksResponse = response.body();
                                        if (techRemarksResponse.getStatus().equals(ApiConstant.SUCCESS)) {
                                            String strTechRemarks =
                                                    gson.toJson(
                                                            techRemarksResponse.getObject(),
                                                            new TypeToken<List<String>>() {
                                                            }.getType());
                                            mEditor.putString(AppUtils.SHARED_TECH_REMARKS, strTechRemarks);
                                            mEditor.commit();
                                        }
                                    }
                                    sendNotification(75);
                                    getReceiveComplaintViewRemarksData();
                                }

                                @Override
                                public void onFailure(Call<TechnicalRemarksResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    sendNotification(75);
                                    getReceiveComplaintViewRemarksData();
                                }
                            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getReceiveComplaintViewRemarksData() {
        Log.d(TAG, "postReceiveComplaintViewData");
        mInterface
                .getAllResponseDetailsResult()
                .enqueue(
                        new Callback<RCViewRemarksEntity>() {
                            @Override
                            public void onResponse(
                                    Call<RCViewRemarksEntity> call, Response<RCViewRemarksEntity> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                        Log.d(TAG, "onResponse success");
                                        String strTechRemarks =
                                                new Gson()
                                                        .toJson(
                                                                response.body().getObject(),
                                                                new TypeToken<List<String>>() {
                                                                }.getType());
                                        mEditor.putString(AppUtils.SHARED_CVIEW_REMARKS, strTechRemarks);
                                        mEditor.commit();
                                    }
                                }
                                sendNotification(77);
                                getCustomerRemarksData();
                            }

                            @Override
                            public void onFailure(Call<RCViewRemarksEntity> call, Throwable t) {
                                Log.d(TAG, "onFailure" + t.getMessage());
                                sendNotification(77);
                                getCustomerRemarksData();
                            }
                        });
    }

    public void getCustomerRemarksData() {
        Log.d(TAG, "postReceiveComplaintViewData");
        mInterface
                .getCustomerRemarksData()
                .enqueue(
                        new Callback<CustomerRemarksResponse>() {
                            @Override
                            public void onResponse(
                                    Call<CustomerRemarksResponse> call, Response<CustomerRemarksResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                        Log.d(TAG, "onResponse success");
                                        String strTechRemarks =
                                                new Gson()
                                                        .toJson(
                                                                response.body().getObject(),
                                                                new TypeToken<List<String>>() {
                                                                }.getType());
                                        mEditor.putString(AppUtils.SHARED_CUSTOMER_REMARKS, strTechRemarks);
                                        mEditor.commit();
                                    }
                                }
                                sendNotification(80);
                                getRCMeterialListData();
                            }

                            @Override
                            public void onFailure(Call<CustomerRemarksResponse> call, Throwable t) {
                                Log.d(TAG, "onFailure" + t.getMessage());
                                sendNotification(80);
                                getRCMeterialListData();
                            }
                        });
    }

    public void getRCMeterialListData() {
        Log.d(TAG, "GetReceiveComplaintMeterialListData");
        try {
            mInterface
                    .getALLRCMaterialSaved(mStrEmployeeId)
                    .enqueue(
                            new Callback<GetMaterialResponse>() {
                                @Override
                                public void onResponse(
                                        Call<GetMaterialResponse> call, Response<GetMaterialResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            new RCSavedMaterialDbInitializer(mContext, null, response.body().getObject())
                                                    .execute(AppUtils.MODE_INSERT);
                                        }
                                    }
                                    sendNotification(90);
                                    getRCMeterialRequiredListData(0);
                                }

                                @Override
                                public void onFailure(Call<GetMaterialResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                    sendNotification(90);
                                    getRCMeterialRequiredListData(0);
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void swMaterial(int total, int courrentRow) {
        mCurrentnoOfRows += courrentRow;
        if (mCurrentnoOfRows >= total) {
            onDownloadComplete();
        } else {
            getRCMeterialRequiredListData(mCurrentnoOfRows);
        }
    }

    public void getRCMeterialRequiredListData(int startIndex) {
        Log.d(TAG, "GetReceiveComplaintMeterialListData");
        try {
            sendNotification("Material Master", mmper);
            mmper += 10;

            mInterface
                    .getReceiveComplaintMaterialRequiredResult(strOPCo, "", startIndex + "", "10000")
                    .enqueue(
                            new Callback<MaterialRequiredResponse>() {
                                @Override
                                public void onResponse(
                                        Call<MaterialRequiredResponse> call,
                                        Response<MaterialRequiredResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "onResponse success " + response.body().getMessage());
                                        if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                                            Log.d(TAG, "onResponse success");
                                            if (!response.body().getObject().isEmpty())
                                                new RCMaterialDbInitializer(mContext, null, response.body().getObject())
                                                        .execute(AppUtils.MODE_INSERT);

                                            swMaterial(
                                                    response.body().getObject().size(),
                                                    Integer.valueOf(response.body().getTotalNumberOfRows()));
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MaterialRequiredResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure" + t.getMessage());
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            onDownloadComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
