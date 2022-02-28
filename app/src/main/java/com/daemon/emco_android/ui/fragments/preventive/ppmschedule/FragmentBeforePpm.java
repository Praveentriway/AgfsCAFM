package com.daemon.emco_android.ui.fragments.preventive.ppmschedule;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.daemon.emco_android.model.common.EmployeeTrackingDetail;
import com.daemon.emco_android.service.GPSTracker;
import com.daemon.emco_android.ui.components.Fragments.ImagePicker;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.utils.ZxingScannerActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.CustomRecyclerViewDataAdapter;
import com.daemon.emco_android.ui.adapter.CustomRecyclerViewItem;
import com.daemon.emco_android.repository.remote.PPMScheduleByService;
import com.daemon.emco_android.repository.remote.ReceiveComplaintRespondService;
import com.daemon.emco_android.repository.remote.ReceiveComplaintViewService;
import com.daemon.emco_android.repository.db.dbhelper.DefectDoneImageDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.RCAssetDetailsDbInitializer;
import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.listeners.DefectDoneImage_Listener;
import com.daemon.emco_android.listeners.ImagePickListener;
import com.daemon.emco_android.listeners.ReceivecomplaintView_Listener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.AssetDetailsRequest;
import com.daemon.emco_android.model.request.PpmScheduleDocByRequest;
import com.daemon.emco_android.model.request.RCDownloadImageRequest;
import com.daemon.emco_android.model.response.DefectDoneImageUploaded;
import com.daemon.emco_android.model.response.RCDownloadImage;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ImageUtil;
import com.daemon.emco_android.utils.StringUtil;
import com.daemon.emco_android.utils.Utils;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import uk.co.senab.photoview.PhotoViewAttacher;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.daemon.emco_android.utils.AppUtils.checkInternet;

public class FragmentBeforePpm extends Fragment
        implements PPMScheduleByService.Listener, ReceivecomplaintView_Listener, ImagePickListener, DefectDoneImage_Listener, CustomRecyclerViewDataAdapter.ImageListner {
    private static final String TAG = FragmentBeforePpm.class.getSimpleName();
    private static final int RC_BARCODE_CAPTURE = 9001;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 4;
    private final int REQUEST_READ_EXTERNAL_STORAGE = 5;
    private final int REQUEST_CHOOSE_PHOTO = 2;
    private final int REQUEST_TAKE_PHOTO = 1;
    private final int THUMBNAIL_SIZE = 75;
    public Bundle mSavedInstanceState;
    private AppCompatActivity mActivity;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private Bundle mArgs;
    private CoordinatorLayout cl_main;
    private PpmScheduleDocByRequest ppmScheduleDocByRequest;
    private ImageView iv_workdone;
    private ProgressBar pb_workdone;
    private boolean isIvDefectFound = false;
    private Bitmap mImageToBeAttachedDefectFound;
    private boolean isPermissionGranted = false;
    private String mImagePathToBeAttached;
    private ImageLoader imageLoader;
    private CharSequence[] items;
    Bitmap thumbnail = null;
    private int imgCount=0;

    private boolean noImageAttached=false;
    private TextView tv_header;
    private ReceiveComplaintRespondService receiveComplaintRespond_service;
    private int imageCount=0;
    private TextView tv_lbl_job_no, tv_job_no, tv_lbl_site_name, tv_site_name, tv_lbl_location, tv_location, tv_lbl_zone_area, tv_zone_area;
    private TextView tv_lbl_nature, tv_nature, tv_lbl_startdate, tv_startdate, tv_lbl_enddate, tv_enddate, tv_lbl_assetcode;
    private TextView tv_lbl_model, tv_model, tv_lbl_make, tv_make, tv_lbl_assettype, tv_assettype,img_upload_count;
    private EditText tv_assetcode,tv_clientBarcode;
    private static final String LOG_TAG = FragmentBeforePpm.class.getSimpleName();
    private RecyclerView recyclerView = null;
    private ArrayList<CustomRecyclerViewItem> itemList  =new ArrayList<>();
    private CustomRecyclerViewDataAdapter customRecyclerViewDataAdapter = null;
    private Button btn_save, btn_barcode_scan, btn_ppm_checklist;
    private Toolbar mToolbar;
    private View rootView;
    private PpmScheduleDocBy ppmScheduleDocBy;
    private PpmScheduleDocBy PpmScheduleDocByUpdate;
    private List<AssetDetailsEntity> assetDetailsEntitiesUp;
    private AssetDetailsEntity assetDetailsEntity;
    private boolean checkImageLoad = false;
    private String mStrEmpId = null;
    View.OnClickListener _OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppUtils.closeInput(cl_main);
                    switch (v.getId()) {
                        case R.id.btn_ppm_checklist:
                            if(checkActStrt()){
                                gotoNextPage();
                            }
                            break;
                        case R.id.btn_save:

                            AppUtils.showProgressDialog(
                                    mActivity, getString(R.string.lbl_image_uploading), false);
                            new Handler()
                                    .postDelayed(
                                            new Runnable() {

                        /*
                         * Showing splash screen with a timer. This will be useful when you
                         * want to show case your app logo / company
                         */

                                                @Override
                                                public void run() {
                                                    // This method will be executed once the timer is over
                                                    submitImage("B");
                                                }
                                            },
                                            1000);
                            break;

                        case R.id.btn_barcode_scan:
                            int requestId = AppUtils.getIdForRequestedCamera(AppUtils.CAMERA_FACING_BACK);
                            if (requestId == -1)
                                AppUtils.showDialog(mActivity, "Camera not available");
                            else {
                                Intent intent = new Intent(mActivity, ZxingScannerActivity.class);
                                intent.putExtra(ZxingScannerActivity.AutoFocus, true);

                                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                            }
                            break;

                        default:
                            break;
                    }
                }
            };
    private String mLoginData = null;
    private int chekNoimage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mArgs = getArguments();
            mSavedInstanceState = savedInstanceState;
            mManager = mActivity.getSupportFragmentManager();
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            imageLoader = ImageLoader.getInstance();
            receiveComplaintRespond_service = new ReceiveComplaintRespondService(mActivity);
            receiveComplaintRespond_service.setmCallbackImages(this);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        try {
            rootView = inflater.inflate(R.layout.fragment_ppmdetails_view, container, false);
            initUI(rootView);
            setProperties();

            if (mArgs != null && mArgs.size() > 0) {
                ppmScheduleDocByRequest = mArgs.getParcelable(AppUtils.ARGS_PPMSCHEDULEDOCBYREQUEST);
                getReceiveComplainViewFromService();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isPermissionGranted) {
            getPermissionToReadExternalStorage();
            return;
        }

    }

    private void initUI(View rootView) {
        Log.d(TAG, "initUI");
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);

            tv_lbl_job_no = (TextView) rootView.findViewById(R.id.tv_lbl_job_no);
            tv_header = (TextView) rootView.findViewById(R.id.tv_header);
            tv_lbl_site_name = (TextView) rootView.findViewById(R.id.tv_lbl_site_name);
            tv_lbl_location = (TextView) rootView.findViewById(R.id.tv_lbl_location);
            tv_lbl_zone_area = (TextView) rootView.findViewById(R.id.tv_lbl_zone_area);
            tv_lbl_nature = (TextView) rootView.findViewById(R.id.tv_lbl_nature);
            tv_lbl_startdate = (TextView) rootView.findViewById(R.id.tv_lbl_startdate);
            tv_lbl_enddate = (TextView) rootView.findViewById(R.id.tv_lbl_enddate);
            tv_lbl_assetcode = (TextView) rootView.findViewById(R.id.tv_lbl_assetcode);
            tv_lbl_model = (TextView) rootView.findViewById(R.id.tv_lbl_model);
            tv_lbl_make = (TextView) rootView.findViewById(R.id.tv_lbl_make);
            tv_lbl_assettype = (TextView) rootView.findViewById(R.id.tv_lbl_assettype);
            iv_workdone = (ImageView) rootView.findViewById(R.id.iv_workdone);
            tv_job_no = (TextView) rootView.findViewById(R.id.tv_job_no);
            tv_site_name = (TextView) rootView.findViewById(R.id.tv_site_name);
            tv_location = (TextView) rootView.findViewById(R.id.tv_location);
            tv_zone_area = (TextView) rootView.findViewById(R.id.tv_zone_area);
            tv_nature = (TextView) rootView.findViewById(R.id.tv_nature);
            tv_startdate = (TextView) rootView.findViewById(R.id.tv_startdate);
            img_upload_count=(TextView) rootView.findViewById(R.id.img_upload_count);
            tv_enddate = (TextView) rootView.findViewById(R.id.tv_enddate);
            tv_assetcode = (EditText) rootView.findViewById(R.id.tv_assetcode);
            tv_clientBarcode = (EditText) rootView.findViewById(R.id.tv_clientBarcode);
            tv_model = (TextView) rootView.findViewById(R.id.tv_model);
            tv_make = (TextView) rootView.findViewById(R.id.tv_make);
            tv_assettype = (TextView) rootView.findViewById(R.id.tv_assettype);
            pb_workdone = (ProgressBar) rootView.findViewById(R.id.pb_workdone);
            btn_barcode_scan = (Button) rootView.findViewById(R.id.btn_barcode_scan);
            btn_save = (Button) rootView.findViewById(R.id.btn_save);
            btn_ppm_checklist = (Button) rootView.findViewById(R.id.btn_ppm_checklist);
            btn_barcode_scan.setOnClickListener(_OnClickListener);
            btn_ppm_checklist.setOnClickListener(_OnClickListener);
            btn_save.setOnClickListener(_OnClickListener);
            FloatingActionButton fab=(FloatingActionButton) rootView.findViewById(R.id.fab_next);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(checkActStrt()){
                        gotoNextPage();
                    }

                }
            });

            // Create the recyclerview.
             recyclerView = (RecyclerView)rootView.findViewById(R.id.custom_refresh_recycler_view);
            // Create the grid layout manager with 2 columns.
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            // Set layout manager.
            recyclerView.setLayoutManager(layoutManager);

            itemList = new ArrayList<CustomRecyclerViewItem>();
            // Create car recycler view data adapter with car item list.
            customRecyclerViewDataAdapter = new CustomRecyclerViewDataAdapter(itemList,1,this,getContext(),true);
            // Set data adapter.
            recyclerView.setAdapter(customRecyclerViewDataAdapter);

            // Scroll RecyclerView a little to make later scroll take effect.
            recyclerView.scrollToPosition(1);

            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void submitImage( final String docType) {

        try {

                    new AsyncTask<Void, DFoundWDoneImageEntity, DFoundWDoneImageEntity>() {
                        @Override
                        protected void onPreExecute() {

                            AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                            super.onPreExecute();
                        }

                        @Override
                        protected DFoundWDoneImageEntity doInBackground(Void... params) {

                            DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity("" + docType);
                            imageEntity.setOpco(PpmScheduleDocByUpdate.getCompanyCode());
                            imageEntity.setFileType("jpg");
                            imageEntity.setTransactionType("P");
                            imageEntity.setDocType(docType);
                            imageEntity.setPpmRefNo(PpmScheduleDocByUpdate.getPpmNo());
                            imageEntity.setCreatedBy(mStrEmpId);
                            imageEntity.setModifiedBy(mStrEmpId);
                            imageEntity.setImageCount(imageCount);
                            imageEntity.setActStartDate(DateFormat.getDateTimeInstance().format(new Date()));
                            imageEntity.setBase64Image(AppUtils.getEncodedString(mImageToBeAttachedDefectFound));
                            return imageEntity;
                        }

                        @Override
                        protected void onPostExecute(DFoundWDoneImageEntity imageEntity) {
                            postImageToServer(imageEntity);
                            super.onPostExecute(imageEntity);
                        }
                    }.execute();

        } catch (Exception e) {
            AppUtils.hideProgressDialog();
            e.printStackTrace();
        }
    }

    private void postImageToServer(DFoundWDoneImageEntity saveRequest) {
        Log.d(TAG, "postImageToServer" + saveRequest.getPpmRefNo());

            if (checkInternet(getContext())) {
                receiveComplaintRespond_service.saveComplaintRespondImageData1(saveRequest, getActivity());
            } else {
                new DefectDoneImageDbInitializer(mActivity, saveRequest, this)
                        .execute(AppUtils.MODE_INSERT);
            }

    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.lbl_ppm_details));
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.onBackPressed();
                    }
                });
    }

    private void setProperties() {

        btn_save.setOnClickListener(_OnClickListener);
        btn_ppm_checklist.setOnClickListener(_OnClickListener);
        btn_barcode_scan.setOnClickListener(_OnClickListener);
        tv_assetcode.addTextChangedListener(
                new FragmentBeforePpm.MyTextWatcher(tv_assetcode));
        tv_assetcode.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            AppUtils.closeInput(cl_main);
                            if (TextUtils.isEmpty(tv_assetcode.getText())) {
                                tv_assetcode.setError(getString(R.string.thisfeildisrequired));
                                tv_assetcode.requestFocus();
                            } else {
                                getBarcodeDetailsService();
                            }
                            return true;
                        }
                        return false;
                    }
                });
        iv_workdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isIvDefectFound = true;
                if (isIvDefectFound) {
                    btn_save.setEnabled(true);
                    if (mImageToBeAttachedDefectFound != null) {
                        if (checkImageLoad) {

                        }
                    } else {
                        if (checkImageLoad) {

                        } } } }});

        if (mSavedInstanceState != null) {
            Log.d(TAG, " mSavedInstanceState : " + mSavedInstanceState);
            ppmScheduleDocBy = mSavedInstanceState.getParcelable(AppUtils.ARGS_PPMSCHEDULEDOCBY);
            assetDetailsEntity =
                    mSavedInstanceState.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ASSET_DETAILS);
            setReceiveComplaintViewValue(ppmScheduleDocBy);


            if(mSavedInstanceState.getParcelableArrayList(AppUtils.ARGS_IMAGE_LIST)!=null ||  !(mSavedInstanceState.getParcelableArrayList(AppUtils.ARGS_IMAGE_LIST).size()==0) ){
                itemList=new ArrayList<CustomRecyclerViewItem>();
                itemList=(mSavedInstanceState.getParcelableArrayList(AppUtils.ARGS_IMAGE_LIST));
                img_upload_count.setVisibility(View.VISIBLE);
                img_upload_count.setText((itemList.size())+"/"+imgCount+" Image uploaded");

                // load recyclerview from cache
                customRecyclerViewDataAdapter = new CustomRecyclerViewDataAdapter(itemList,imgCount,this,getContext(),false);
                recyclerView.setAdapter(customRecyclerViewDataAdapter);

            }

            else{

                RCDownloadImageRequest imageRequest = new RCDownloadImageRequest();
                imageRequest.setOpco(ppmScheduleDocBy.getCompanyCode());
                imageRequest.setDocType("B");
                imageRequest.setPpmRefNo(ppmScheduleDocBy.getPpmNo());
                receiveComplaintRespond_service.getRespondImage(imageRequest, getActivity());
            }

        }
        AppUtils.closeInput(cl_main);
    }

    private void displayAttachImageDialog(final int count, final String base64) {
        if (!isPermissionGranted) {
            getPermissionToReadExternalStorage( count, base64);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

            if (base64 == null) {

                items = new CharSequence[]{"Take photo", "Choose photo", "No image"};
                builder.setTitle("Add  photo");

            }
            else if(base64.equalsIgnoreCase("noImage")){
                items = new CharSequence[]{"Take photo", "Choose photo"};
                builder.setTitle("Add  photo");
            } else {
                items = new CharSequence[]{"Take photo", "Choose photo", "View photo"}; // , "Delete photo"
                builder.setTitle("Update and view photo");
            }
            builder.setItems(
                    items,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                case 0:
                                    dispatchTakePhotoIntent();

                                    break;
                                case 1:
                                    dispatchChoosePhotoIntent();

                                    break;
                                case 2:

                                        if (items[item].toString().equalsIgnoreCase("View photo")) {
                                            dispatchViewPhotoIntent(base64);
                                        } else if (items[item].toString().equalsIgnoreCase("No image")) {
                                            //to check image type
                                            dialog.dismiss();
                                            showNoImageAlert(count);
                                            chekNoimage = 1;
                                        }

                                    break;

                                case 3:
                                    dialog.dismiss();
                                    showNoImageAlert(count);
                                    chekNoimage = 1;
                                    break;

                            }
                        }
                    });
            builder.show();
    }

    public void showNoImageAlert(final int count){

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Are you sure you want to add No Image?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                     //   noImageAvailabe(count,convertImageviewBase64());
                        noImageAvailabe(count,"");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    private void noImageAvailabe(int count,String base64) {
        AppUtils.showProgressDialog(
                mActivity, getString(R.string.lbl_no_image_uploading), false);
        DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity("" + "B");
        imageEntity.setOpco(PpmScheduleDocByUpdate.getCompanyCode());
        imageEntity.setFileType("jpg");
        imageEntity.setTransactionType("P");
        imageEntity.setDocType("B");
        imageEntity.setPpmRefNo(PpmScheduleDocByUpdate.getPpmNo());
        imageEntity.setCreatedBy(mStrEmpId);
        imageEntity.setModifiedBy(mStrEmpId);
        imageEntity.setImageCount(count);
        imageEntity.setActStartDate(DateFormat.getDateTimeInstance().format(new Date()));
        imageEntity.setBase64Image(base64);
        postImageToServer(imageEntity);
    }

    private void dispatchChoosePhotoIntent() {
        ImagePicker fragment = new ImagePicker();
        fragment.SetImagePickListener(this);
        FragmentTransaction ObjTransaction = mManager.beginTransaction();
        ObjTransaction.add(android.R.id.content, fragment, AppUtils.SHARED_DIALOG_PICKER);
        ObjTransaction.addToBackStack(AppUtils.SHARED_DIALOG_PICKER);
        ObjTransaction.commit();
    }

    private void dispatchTakePhotoIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    Log.d(TAG, "Cannot create a temp image file", e);
                }
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg", storageDir);
        mImagePathToBeAttached = image.getAbsolutePath();
        return image;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToReadExternalStorage() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }

                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }

            }
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            isPermissionGranted = true;
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToReadExternalStorage(int count,String base64) {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        // Show our own UI to explain to the user why we need to read the contacts
                        // before actually requesting the permission and showing the default UI
                    }
            }
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            isPermissionGranted = true;
            displayAttachImageDialog(count,base64);
        }
    }

    private void dispatchViewPhotoIntent(final String base64) {


                new AsyncTask<Void, RCDownloadImage, RCDownloadImage>() {
                    @Override
                    protected void onPreExecute() {
                        // update the UI (this is executed on UI thread)
                        AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                        super.onPreExecute();
                    }

                    @Override
                    protected RCDownloadImage doInBackground(Void... params) {
                        // your async action

                               // new RCDownloadImage(AppUtils.getEncodedString(mImageToBeAttachedDefectFound), "B");

                        ArrayList<String> images=new ArrayList<>();
                        images.add(base64);

                        return  new RCDownloadImage(images, "B",0);
                    }

                    @Override
                    protected void onPostExecute(RCDownloadImage aVoid) {
                        AppUtils.hideProgressDialog();

                        Display display = mActivity.getWindowManager().getDefaultDisplay();
                        int width = display.getWidth();
                        int height = display.getHeight();

                        loadPhoto(aVoid.getBase64Image().get(0),width,height);

                        super.onPostExecute(aVoid);
                    }
                }.execute();

    }

    private void getBarcodeDetailsService() {
        if (checkInternet(getContext())) {
            if (ppmScheduleDocBy != null) {
                AssetDetailsRequest assetDetailsRequest =
                        new AssetDetailsRequest(
                                ppmScheduleDocBy.getCompanyCode(),
                                ppmScheduleDocBy.getJobNo(),
                                ppmScheduleDocBy.getZoneCode(),
                                ppmScheduleDocBy.getBuildTag(),
                                tv_assetcode.getText().toString());
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);

                new ReceiveComplaintViewService(mActivity, this)
                        .GetReceiveComplaintViewAssetDetailsData(assetDetailsRequest);

            } else AppUtils.showDialog(mActivity, getString(R.string.lbl_alert_rc_not_available));
        } else {
            Log.d(TAG, "getReceiveComplainViewAssetDetailsFromLocal");
            AssetDetailsEntity entity = new AssetDetailsEntity();
            entity.setAssetBarCode(tv_assetcode.getText().toString());
            new RCAssetDetailsDbInitializer(mActivity, this, entity).execute(AppUtils.MODE_GET);
        }
    }

    private void setReceiveComplaintViewValue(PpmScheduleDocBy complaintViewValue) {
        Log.d(TAG, "setReceiveComplaintViewValue ");
        try {
            if (complaintViewValue != null) {
                this.ppmScheduleDocBy = complaintViewValue;
                if (!TextUtils.isEmpty(complaintViewValue.getJobNo())  && !complaintViewValue.getJobNo().equalsIgnoreCase("NULL"))
                    tv_job_no.setText(complaintViewValue.getJobNo());
                if (!TextUtils.isEmpty(complaintViewValue.getSiteName()) && !complaintViewValue.getSiteName().equalsIgnoreCase("NULL"))
                    tv_site_name.setText(complaintViewValue.getSiteName());
                if (!TextUtils.isEmpty(complaintViewValue.getLocation()) && !complaintViewValue.getLocation().equalsIgnoreCase("NULL"))
                    tv_header.setText(complaintViewValue.getPpmNo()+"  -  "+complaintViewValue.getLocation());
                if (!TextUtils.isEmpty(complaintViewValue.getZoneDescription()) && !complaintViewValue.getZoneDescription().equalsIgnoreCase("NULL"))
                    tv_zone_area.setText(complaintViewValue.getZoneDescription());
                if (!TextUtils.isEmpty(complaintViewValue.getNatureDescription()) && !complaintViewValue.getNatureDescription().equalsIgnoreCase("NULL"))
                    tv_nature.setText(complaintViewValue.getNatureDescription());
                if (!TextUtils.isEmpty(complaintViewValue.getStartDate()) && !complaintViewValue.getStartDate().equalsIgnoreCase("NULL"))
                    tv_startdate.setText(StringUtil.dateConvertion((complaintViewValue.getStartDate())));
                if (!TextUtils.isEmpty(complaintViewValue.getEndDate()) && !complaintViewValue.getEndDate().equalsIgnoreCase("NULL"))
                    tv_enddate.setText(StringUtil.dateConvertion((complaintViewValue.getEndDate())));
                if (!TextUtils.isEmpty(complaintViewValue.getAssetTypeDesc()) && !complaintViewValue.getAssetTypeDesc().equalsIgnoreCase("NULL"))
                    tv_assettype.setText(complaintViewValue.getAssetTypeDesc());
                if (!TextUtils.isEmpty(complaintViewValue.getAssetMake()) && !complaintViewValue.getAssetMake().equalsIgnoreCase("NULL"))
                    tv_make.setText(complaintViewValue.getAssetMake());
                if (!TextUtils.isEmpty(complaintViewValue.getAssetModel()) && !complaintViewValue.getAssetModel().equalsIgnoreCase("NULL"))
                    tv_model.setText(complaintViewValue.getAssetModel());
                if (!TextUtils.isEmpty(complaintViewValue.getAssetBarCode()) && !complaintViewValue.getAssetBarCode().equalsIgnoreCase("NULL"))
                    tv_assetcode.setText(complaintViewValue.getAssetBarCode());
                if (!TextUtils.isEmpty(complaintViewValue.getClientBarcode()) && !complaintViewValue.getClientBarcode().equalsIgnoreCase("NULL"))
                    tv_clientBarcode.setText(complaintViewValue.getClientBarcode());


            } else Log.d(TAG, "setReceiveComplaintViewValue null");

            PpmScheduleDocByUpdate = complaintViewValue;

            if (checkInternet(getContext())) {
                if (mImageToBeAttachedDefectFound == null) {
                    if (checkInternet(getContext())) {
                        // Download defect found image
                        RCDownloadImageRequest imageRequest = new RCDownloadImageRequest();
                        imageRequest.setOpco(complaintViewValue.getCompanyCode());
                        imageRequest.setDocType("B");
                        imageRequest.setPpmRefNo(complaintViewValue.getPpmNo());
                        receiveComplaintRespond_service.getRespondImage(imageRequest, getActivity());
                    } else {
                        new DefectDoneImageDbInitializer(
                                mActivity, new DFoundWDoneImageEntity("" + "B"), this)
                                .execute(AppUtils.MODE_GET);
                    }
                } else {
                    iv_workdone.setImageBitmap(
                            ThumbnailUtils.extractThumbnail(
                                    mImageToBeAttachedDefectFound, THUMBNAIL_SIZE, THUMBNAIL_SIZE));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setReceiveComplaintViewAssetDetailsValue(AssetDetailsEntity assetDetailsValue) {

        try {
            if (assetDetailsValue != null) {
                this.assetDetailsEntity = assetDetailsValue;
                if (!TextUtils.isEmpty(assetDetailsValue.getAssetCode())
                        && !assetDetailsValue.getAssetCode().equalsIgnoreCase("NULL"))
                    tv_assetcode.setText(assetDetailsValue.getAssetBarCode());
                if (!TextUtils.isEmpty(assetDetailsValue.getEquipmentName())
                        && !assetDetailsValue.getEquipmentName().equalsIgnoreCase("NULL"))
                    tv_assettype.setText(assetDetailsValue.getEquipmentName());
                if (!TextUtils.isEmpty(assetDetailsValue.getAssetMake())
                        && !assetDetailsValue.getAssetMake().equalsIgnoreCase("NULL"))
                    tv_make.setText(assetDetailsValue.getAssetMake());
                if (!TextUtils.isEmpty(assetDetailsValue.getAssetModel())
                        && !assetDetailsValue.getAssetModel().equalsIgnoreCase("NULL"))
                    tv_model.setText(assetDetailsValue.getAssetModel());

                if (!TextUtils.isEmpty(assetDetailsValue.getClientBarcode())
                        && !assetDetailsValue.getClientBarcode().equalsIgnoreCase("NULL"))
                    tv_clientBarcode.setText(assetDetailsValue.getClientBarcode());

            } else Log.d(TAG, "setReceiveComplaintViewAssetDetailsValue null");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gotoNextPage() {

        mSavedInstanceState = getSavedState();
        Bundle data = new Bundle();
        data.putParcelable(AppUtils.ARGS_PPMSCHEDULEDOCBY, ppmScheduleDocBy);
        Fragment_PM_PPMChecklist fragment = new Fragment_PM_PPMChecklist();
        fragment.setArguments(data);
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, Utils.TAG_PM_PPMCHECKLIST);
        fragmentTransaction.addToBackStack(Utils.TAG_PM_PPMCHECKLIST);
        fragmentTransaction.commit();
    }

    public void getReceiveComplainViewFromService() {

        try {


                if (checkInternet(getContext())) {

                    AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                    new PPMScheduleByService(mActivity, this).getppmData(ppmScheduleDocByRequest);
                }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onReceiveComplaintRemarksReceived(List<String> remarkList, int mode) {
    }

    public void onReceiveComplaintRespondReceived(String strMsg, String complaintNumber, int mode) {

        try {

            AppUtils.hideProgressDialog();

            if (!TextUtils.isEmpty(strMsg)) {
                MaterialDialog.Builder builder =
                        new MaterialDialog.Builder(mActivity)
                                .content(strMsg)
                                .positiveText(R.string.lbl_okay)
                                .stackingBehavior(StackingBehavior.ADAPTIVE)
                                .onPositive(
                                        new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(
                                                    @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();

                                            }
                                        });

                MaterialDialog dialog = builder.build();
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveComplaintViewReceived(
            List<ReceiveComplaintViewEntity> complaintViewEntities, int mode) {
    }

    @Override
    public void onReceiveComplaintViewReceivedError(String msg, int mode) {

        tv_assetcode.setError("Enter a valid barcode");

        try {
            AppUtils.hideProgressDialog();
            Fragment main = mManager.findFragmentByTag(Utils.TAG_PM_PPMDETAILS_VIEW);
            if (main != null && main.isVisible()) AppUtils.showDialog(mActivity, msg);

            if (msg.equalsIgnoreCase("false")) {
                AppUtils.showDialog(getActivity(), msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveBarCodeAssetReceived(String msg, int mode) {

    }

    @Override
    public void onAllReceiveComplaintData(
            List<ReceiveComplaintRespondEntity> complaintRespondEntities, int modeLocal) {
    }

    @Override
    public void onReceiveComplaintBycomplaintNumber(
            ReceiveComplaintRespondEntity complaintRespondEntity, int modeLocal) {
    }

    @Override
    public void onppmListReceived(List<PpmScheduleDocBy> receiveComplaintItemView, int from) {
        AppUtils.hideProgressDialog();
        Log.d(TAG, "onReceiveComplaintViewReceived" + receiveComplaintItemView.size());
        setReceiveComplaintViewValue(receiveComplaintItemView.get(0));
    }

    @Override
    public void onReceiveComplaintViewAssetDetailsReceived(
          final  List<AssetDetailsEntity> assetDetailsEntities,final int from) {

        if(assetDetailsEntities.size()>0){
            StringBuffer sb=new StringBuffer();

            if(assetDetailsEntities.get(0).getEquipmentName()!=null && !(assetDetailsEntities.get(0).getEquipmentName().equalsIgnoreCase("NULL"))){
                sb.append( "Asset Type - "+assetDetailsEntities.get(0).getEquipmentName());
            }
            else{
                sb.append( "Asset Type - ");
            }
            sb.append(System.getProperty("line.separator"));

            if(assetDetailsEntities.get(0).getAssetMake()!=null && !(assetDetailsEntities.get(0).getAssetMake().equalsIgnoreCase("NULL"))){
                sb.append( "Make - "+assetDetailsEntities.get(0).getAssetMake());
            }
            else{
                sb.append( "Make - ");
            }
            sb.append(System.getProperty("line.separator"));

            if(assetDetailsEntities.get(0).getAssetModel()!=null && !(assetDetailsEntities.get(0).getAssetModel().equalsIgnoreCase("NULL"))){
                sb.append( "Model - "+assetDetailsEntities.get(0).getAssetModel());
            }
            else{
                sb.append( "Model - ");
            }
            sb.append(System.getProperty("line.separator"));

            if(assetDetailsEntities.get(0).getAssetBarCode()!=null && !(assetDetailsEntities.get(0).getAssetBarCode().equalsIgnoreCase("NULL"))){
                sb.append( "Asset/Location Barcode - "+assetDetailsEntities.get(0).getAssetBarCode());
            }
            else{
                sb.append( "Asset/Location Barcode - ");
            }

            sb.append(System.getProperty("line.separator"));

            if(assetDetailsEntities.get(0).getClientBarcode()!=null && !(assetDetailsEntities.get(0).getClientBarcode().equalsIgnoreCase("NULL"))){
                sb.append( "Client Barcode - "+assetDetailsEntities.get(0).getClientBarcode());
            }
            else{
                sb.append( "Client Barcode - ");
            }

            try {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                builder.setTitle("Asset Detail").setMessage(sb.toString())

                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();



                                assetDetailsEntitiesUp = assetDetailsEntities;
                                if (ppmScheduleDocBy != null) {
                                    AssetDetailsRequest assetDetailsRequest =
                                            new AssetDetailsRequest(
                                                    ppmScheduleDocBy.getCompanyCode(),
                                                    ppmScheduleDocBy.getJobNo(),
                                                    ppmScheduleDocBy.getZoneCode(),
                                                    ppmScheduleDocBy.getBuildTag(), assetDetailsEntities.get(0).getAssetCode(), ppmScheduleDocBy.getPpmNo(),assetDetailsEntities.get(0).getClientBarcode(),assetDetailsEntities.get(0).getAssetBarCode());
                                    //AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);

                                    // to save the details to table
                                    new ReceiveComplaintViewService(mActivity, FragmentBeforePpm.this)
                                            .getAssestBarCodePPM(assetDetailsRequest);
                                    }

                                setReceiveComplaintViewAssetDetailsValue(assetDetailsEntities.get(0));

                                if (from == AppUtils.MODE_SERVER) {
                                    Log.d(TAG, "onReceiveComplaintViewAssetDetailsReceived");
                                    new RCAssetDetailsDbInitializer(mActivity, FragmentBeforePpm.this,assetDetailsEntity).execute(
                                            AppUtils.MODE_INSERT_SINGLE);
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                AppUtils.hideProgressDialog();
                            }
                        });
                androidx.appcompat.app.AlertDialog alert = builder.create();
                alert.show();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else{

            AppUtils.hideProgressDialog();
            AppUtils.showDialog(mActivity, "No Data found.");
        }

    }

    @Override
    public void onppmListReceivedError(String strErr, int from) {
        Log.d(TAG, "onReceiveComplaintViewReceivedError");

        try {
            AppUtils.hideProgressDialog();
            Fragment main = mManager.findFragmentByTag(Utils.TAG_RECEIVE_COMPLAINT_VIEW);
            if (main != null && main.isVisible()) AppUtils.showDialog(mActivity, strErr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(TAG, "onPrepareOptionsMenu ");
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Log.d(TAG, "onOptionsItemSelected : home");
                // mActivity.onBackPressed();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                Fragment _fragment = new MainDashboard();
                FragmentTransaction _transaction = mManager.beginTransaction();
                _transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            File file = new File(mImagePathToBeAttached);
            if (file.exists()) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mImagePathToBeAttached, options);
                options.inJustDecodeBounds = false;

                    mImageToBeAttachedDefectFound = BitmapFactory.decodeFile(mImagePathToBeAttached, options);
                    thumbnail =
                            ThumbnailUtils.extractThumbnail(
                                    mImageToBeAttachedDefectFound, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
                file.delete();
                submitImage("B");
            }
            mImagePathToBeAttached = null;
        } else if (requestCode == REQUEST_CHOOSE_PHOTO) {
            try {
                Uri uri = data.getData();
                ContentResolver resolver = mActivity.getContentResolver();
                if (isIvDefectFound) {
                    mImageToBeAttachedDefectFound = MediaStore.Images.Media.getBitmap(resolver, uri);
                }

                AssetFileDescriptor asset = resolver.openAssetFileDescriptor(uri, "r");
                thumbnail =
                        ImageUtil.thumbnailFromDescriptor(
                                asset.getFileDescriptor(), THUMBNAIL_SIZE, THUMBNAIL_SIZE);


            } catch (IOException e) {
                Log.d(TAG, "Cannot get a selected photo from the gallery.", e);
            }
        }



        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String barcode = data.getStringExtra(ZxingScannerActivity.BarcodeObject);
                    tv_assetcode.setText(barcode);
                    Log.d(TAG, "Barcode read: " + barcode);
                    if (assetDetailsEntity == null) {
                        getBarcodeDetailsService();
                    }else{

                        if(tv_assetcode.getText().toString().equalsIgnoreCase(barcode) || tv_clientBarcode.getText().toString().equalsIgnoreCase(barcode)){
                            getBarcodeDetailsService();
                        }
                        else{

                            AppUtils.showDialog(mActivity, "Please scan a valid Asset Bsrcode or Client Barcode.");

                        }

                    }
                } else {
                    tv_assetcode.setError(getString(R.string.lbl_no_barcode_captured));
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                tv_assetcode.setError(
                        String.format(
                                getString(R.string.barcode_error),
                                CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
          //  super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        mSavedInstanceState = getSavedState();
    }

    public Bundle getSavedState() {
        Bundle outState = new Bundle();
        outState.putParcelable(AppUtils.ARGS_PPMSCHEDULEDOCBY, ppmScheduleDocBy);
        outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ASSET_DETAILS, assetDetailsEntity);

        ArrayList<CustomRecyclerViewItem> tt=itemList;
        tt.removeAll(Collections.singleton(null));

        outState.putParcelableArrayList(AppUtils.ARGS_IMAGE_LIST,tt);

        return outState;
    }

    @Override
    public void onSingleImagePicked(String Str_Path) {
        try {

            Str_Path = "file://" + Str_Path;
            imageLoader.loadImage(
                    Str_Path,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            if (loadedImage != null) {

                                    if (AppUtils.checkImageFileSize(loadedImage)) {
                                        mImageToBeAttachedDefectFound = loadedImage;
                                        iv_workdone.setImageBitmap(
                                                ThumbnailUtils.extractThumbnail(
                                                        loadedImage, THUMBNAIL_SIZE, THUMBNAIL_SIZE));

                                    } else {
                                        //AppUtils.showDialog(mActivity, getString(R.string.file_size_more_then));
                                        mImageToBeAttachedDefectFound = loadedImage;
                                        iv_workdone.setImageBitmap(
                                                ThumbnailUtils.extractThumbnail(
                                                        loadedImage, THUMBNAIL_SIZE, THUMBNAIL_SIZE));
                                    }
                                submitImage("B");
                            }
                            AppUtils.closeInput(cl_main);
                      //      AppUtils.hideProgressDialog();
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onImageSaveReceivedSuccess(DefectDoneImageUploaded mImageEntity, int mode) {

    }


    @Override
    public void onImageSaveReceivedSuccess1(RCDownloadImage imageEntity, int mode) {
        processImage(imageEntity,true);

        EmployeeTrackingDetail emp=new EmployeeTrackingDetail();
        emp.setCompCode(PpmScheduleDocByUpdate.getCompanyCode());
        emp.setTransType("PPM");
        emp.setRefNo(PpmScheduleDocByUpdate.getPpmNo());
        new GPSTracker(getContext()).updateFusedLocation(emp);

    }


    @Override
    public void onImageReceivedSuccess(RCDownloadImage imageEntity, int mode) {
        processImage(imageEntity,false);
    }

    @Override
    public void onImageSaveReceivedFailure(String strErr, int mode) {
        Fragment main = mManager.findFragmentByTag(Utils.TAG_RECEIVE_COMPLAINT_RESPOND);
        if (main != null && main.isVisible()) {
            AppUtils.hideProgressDialog();
            AppUtils.showDialog(mActivity, strErr);
        } else {
            AppUtils.hideProgressDialog();
            AppUtils.showDialog(mActivity, strErr);
        }
    }

    @Override
    public void onImageReceivedFailure(String strErr, int mode) {
        iv_workdone.setEnabled(true);
        checkImageLoad=true;
        if(strErr!=null){
            if (strErr.toUpperCase().equals("B")) {
                pb_workdone.setVisibility(View.GONE);
            } else pb_workdone.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAllImagesReceived(List<DFoundWDoneImageEntity> mImageEntities, int mode) {

    }
    private class MyTextWatcher implements TextWatcher {

        private TextView tie_view;

        private MyTextWatcher(TextView tie_view) {
            this.tie_view = tie_view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            try {
                if (tie_view.getText().toString().trim().isEmpty()) {
                    tv_assetcode.setError(getString(R.string.msg_empty));
                } else {
                    tv_assetcode.setError(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkActStrt(){

        if(noImageAttached){
            AppUtils.showDialog(getContext(),"Add at least one image before navigating to PPM Checklist.");
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onImageClick(int pos, String base64){
        imageCount=pos;
        displayAttachImageDialog(pos,base64);
    }

     public String convertImageviewBase64(){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image=stream.toByteArray();
        return Base64.encodeToString(image, 0);
    }


    private void loadPhoto(String base64,int width, int height) {

        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dialog.setContentView(R.layout.custom_fullimage_dialog);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup) rootView.findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageBitmap(AppUtils.getDecodedString(base64));
        image.getLayoutParams().height = height;
        image.getLayoutParams().width = width;
        PhotoViewAttacher  mAttacher = new PhotoViewAttacher(image);
        image.requestLayout();
        dialog.setContentView(layout);
        dialog.show();
    }

    public void processImage(final RCDownloadImage imageEntity, final boolean fromUpload) {

        try {
            itemList = new ArrayList<CustomRecyclerViewItem>();
            imgCount=imageEntity.getImageCount();

            if(imageEntity.getBase64Image().size()==0){
                noImageAttached=true;
            }
            else{
                noImageAttached=false;
            }


            if (!imageEntity.getBase64Image().isEmpty() && imageEntity.getDocType() != null) {


            new AsyncTask<Void, Boolean, Boolean>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Boolean doInBackground(Void... params) {

                    for(int i=0;i<imageEntity.getBase64Image().size();i++){
                        mImageToBeAttachedDefectFound = AppUtils.getDecodedString(imageEntity.getBase64Image().get(i));
                        DFoundWDoneImageEntity imageEntity1 = new DFoundWDoneImageEntity("B" );
                        imageEntity1.setOpco(PpmScheduleDocByUpdate.getCompanyCode());
                        imageEntity1.setFileType("jpg");
                        imageEntity1.setTransactionType("P");
                        imageEntity1.setDocType("B");
                        imageEntity1.setPpmRefNo(PpmScheduleDocByUpdate.getPpmNo());
                        imageEntity1.setCreatedBy(mStrEmpId);
                        imageEntity1.setModifiedBy(mStrEmpId);
                        imageEntity1.setImageCount(i);
                        imageEntity1.setActStartDate(DateFormat.getDateTimeInstance().format(new Date()));
                        if (mImageToBeAttachedDefectFound != null) {
                            imageEntity1.setBase64Image(AppUtils.getEncodedString(mImageToBeAttachedDefectFound));
                        }
                        CustomRecyclerViewItem item = new CustomRecyclerViewItem();
                        item.setDet(imageEntity1);
                        item.setThums(ThumbnailUtils.extractThumbnail(
                                mImageToBeAttachedDefectFound, THUMBNAIL_SIZE, THUMBNAIL_SIZE));
                        itemList.add(item);
                    }
                    return true;
                }

                @Override
                protected void onPostExecute(Boolean aVoid) {

                    processRecycleview();

                    img_upload_count.setVisibility(View.VISIBLE);
                    img_upload_count.setText(imageEntity.getBase64Image().size()+"/"+imgCount+" Image uploaded");

                    if(fromUpload){
                        AppUtils.hideProgressDialog();
                        AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Image has been successfully Saved.");
                    }

                    super.onPostExecute(aVoid);
                }
            }.execute(); }

            else{

                customRecyclerViewDataAdapter = new CustomRecyclerViewDataAdapter(itemList,imgCount,this,getContext(),false);
                recyclerView.setAdapter(customRecyclerViewDataAdapter);
                img_upload_count.setVisibility(View.VISIBLE);
                img_upload_count.setText(imageEntity.getBase64Image().size()+"/"+imgCount+" Image uploaded");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   public void processRecycleview(){
        customRecyclerViewDataAdapter = new CustomRecyclerViewDataAdapter(itemList,imgCount,this,getContext(),false);
        recyclerView.setAdapter(customRecyclerViewDataAdapter);
        pb_workdone.setVisibility(View.GONE);
    }

}


