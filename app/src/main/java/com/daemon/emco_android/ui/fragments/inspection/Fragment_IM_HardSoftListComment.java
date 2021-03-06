package com.daemon.emco_android.ui.fragments.inspection;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.HardSoftService;
import com.daemon.emco_android.repository.remote.ReceiveComplaintRespondService;
import com.daemon.emco_android.repository.remote.ReceiveComplaintViewService;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.dbhelper.DefectDoneImageDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.RCRespondDbInitializer;
import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.repository.db.entity.ComplaintStatusEntity;
import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.ui.components.Fragments.ImagePicker;
import com.daemon.emco_android.ui.fragments.common.ViewImage;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.ui.fragments.reactive.receieve_complaints.Fragment_RC_Respond;
import com.daemon.emco_android.listeners.DefectDoneImage_Listener;
import com.daemon.emco_android.listeners.HardSoft_Listener;
import com.daemon.emco_android.listeners.ImagePickListener;
import com.daemon.emco_android.listeners.ReceivecomplaintList_Listener;
import com.daemon.emco_android.listeners.ReceivecomplaintView_Listener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.HardSoftRequest;
import com.daemon.emco_android.model.request.ReceiveComplaintViewRequest;
import com.daemon.emco_android.model.response.DefectDoneImageUploaded;
import com.daemon.emco_android.model.response.HardSoftView;
import com.daemon.emco_android.model.response.RCDownloadImage;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.SessionManager;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.checkInternet;
import static com.daemon.emco_android.utils.Utils.TAG_FRAGMENT_IM_HARDSOFTLIST;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_RESPOND;

/**
 * Created by vikram on 14/7/17.
 */

public class Fragment_IM_HardSoftListComment extends Fragment implements ReceivecomplaintView_Listener
        , ImagePickListener, DefectDoneImage_Listener, HardSoft_Listener, ReceivecomplaintList_Listener {
    private static final String TAG = Fragment_IM_HardSoftListComment.class.getSimpleName();
    private final int THUMBNAIL_SIZE = 75;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 4;
    private final int REQUEST_READ_EXTERNAL_STORAGE = 5;
    private final int REQUEST_CHOOSE_PHOTO = 2;
    private final int REQUEST_TAKE_PHOTO = 1;
    private HardSoftRequest hardSoftRequest;
    public Bundle mSavedInstanceState;
    private AppCompatActivity mActivity;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private ImageLoader imageLoader;
    private ProgressBar pb_workdone;
    private int chekNoimage;
    private Bundle mArgs;
    private String mTitle;
    private CoordinatorLayout cl_main;
    private TextView tv_lbl_comments, tv_lbl_photo;
    private EditText tv_comments;
    private ImageView iv_defectfound;
    private Button btn_save, btn_save_img;
    private Toolbar mToolbar;
    private View rootView;
    private ReceiveComplaintItemEntity receiveComplaintItemEntity;
    private int mSelectedPosition = 0;
    private ReceiveComplaintViewEntity complaintViewEntity;
    private AssetDetailsEntity assetDetailsEntity;
    View.OnClickListener _OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AppUtils.closeInput(cl_main);
            switch (v.getId()) {
                case R.id.btn_next:
                    gotoNextPage();
                    break;
                default:
                    break;
            }
        }
    };
    private String mNetworkInfo = null;
    private RCRespondDbInitializer complaintRespondDbInitializer;
    private ReceiveComplaintViewService complaintView_service;
    private List<String> remarkList = new ArrayList<>();
    private String mLoginData = null, mStrEmpId;
    private boolean isIvDefectFound = false;
    private Bitmap mImageToBeAttachedDefectFound;
    private boolean isPermissionGranted = false;
    private CharSequence[] items;
    private boolean checkImageLoad = false;
    private String mImagePathToBeAttached;
    private ReceiveComplaintRespondService receiveComplaintRespond_service;

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
            font = App.getInstance().getFontInstance();
            complaintRespondDbInitializer = new RCRespondDbInitializer(this);

            imageLoader = ImageLoader.getInstance();
            complaintView_service = new ReceiveComplaintViewService(mActivity, this);
            receiveComplaintRespond_service = new ReceiveComplaintRespondService(mActivity);
            receiveComplaintRespond_service.setmCallbackImages(this);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }

            if (mArgs != null && mArgs.size() > 0) {
                mTitle = mArgs.getString(AppUtils.ARGS_IM_SERVICES_Page_TITLE);
                hardSoftRequest = mArgs.getParcelable(AppUtils.ARGS_REACTIVE_MAINTENANCE_DASHBOARD_REQUEST);
                receiveComplaintItemEntity = mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);
                mSelectedPosition = mArgs.getInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION);
                if (receiveComplaintItemEntity != null) {
                    getReceiveComplainViewFromService(receiveComplaintItemEntity);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            rootView = inflater.inflate(R.layout.fragment_im_hardsoftlistcomment, container, false);
            initUI(rootView);
            setupActionBar();
            setProperties();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void initUI(View rootView) {

        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);

            tv_lbl_comments = (TextView) rootView.findViewById(R.id.tv_lbl_comments);
            tv_comments = (EditText) rootView.findViewById(R.id.tv_comments);
            iv_defectfound = (ImageView) rootView.findViewById(R.id.iv_defectfound);
            tv_lbl_photo = (TextView) rootView.findViewById(R.id.tv_lbl_photo);
            btn_save_img = (Button) rootView.findViewById(R.id.btn_save_img);
            btn_save = (Button) rootView.findViewById(R.id.btn_save);
            pb_workdone = (ProgressBar) rootView.findViewById(R.id.pb_defectfound);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        }  else {
            isPermissionGranted = true;
            displayAttachImageDialog();
        }
    }

    private void displayAttachImageDialog() {
        if (!isPermissionGranted) {
            getPermissionToReadExternalStorage();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        if (isIvDefectFound) {
            if (mImageToBeAttachedDefectFound != null) {
                items = new CharSequence[]{"Take photo", "Choose photo", "View photo"}; // , "Delete photo"
                builder.setTitle("Add and view photo");
            } else {
                items = new CharSequence[]{"Take photo", "Choose photo", "No image"};
                builder.setTitle("Add  photo");
            }
            builder.setItems(
                    items,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                case 0:
                                    dispatchTakePhotoIntent();
                                    btn_save.setEnabled(true);
                                    btn_save.setBackgroundColor(getResources().getColor(R.color.color_gray));
                                    break;
                                case 1:
                                    dispatchChoosePhotoIntent();
                                    btn_save.setEnabled(true);
                                    btn_save.setBackgroundColor(getResources().getColor(R.color.color_gray));
                                    break;
                                case 2:
                                    for (CharSequence charSequence : items) {
                                        if (charSequence.toString().equalsIgnoreCase("View photo")) {
                                            dispatchViewPhotoIntent();
                                        } else if (charSequence.toString().equalsIgnoreCase("No image")) {
                                            //to check image type
                                            noImageAvailabe();
                                            chekNoimage = 1;
                                        }
                                    }
                                    break;
                            }
                        }
                    });
            builder.show();
        }
    }

    private void noImageAvailabe() {
        if (mTitle.startsWith("R")) {
            AppUtils.showProgressDialog(
                    mActivity, getString(R.string.lbl_no_image_uploading), false);
            DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity("" + "B");
            imageEntity.setOpco(receiveComplaintItemEntity.getOpco());
            imageEntity.setComplaintSite(receiveComplaintItemEntity.getSiteCode());
            imageEntity.setComplaintNo(receiveComplaintItemEntity.getComplaintNumber());
            imageEntity.setFileType("jpg");
            imageEntity.setTransactionType("R");
            imageEntity.setDocType("B");
            imageEntity.setCreatedBy(mStrEmpId);
            imageEntity.setModifiedBy(mStrEmpId);
            imageEntity.setBase64Image("noImage");
            postImageToServer(imageEntity);
            iv_defectfound.setImageResource(R.drawable.noimage);
        } else {
            AppUtils.showProgressDialog(
                    mActivity, getString(R.string.lbl_no_image_uploading), false);
            DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity("" + "B");
            imageEntity.setOpco(receiveComplaintItemEntity.getOpco());
            // imageEntity.setPpmRefNo(receiveComplaintItemEntity.);
            imageEntity.setFileType("jpg");
            imageEntity.setTransactionType("P");
            imageEntity.setDocType("B");
            imageEntity.setCreatedBy(mStrEmpId);
            imageEntity.setModifiedBy(mStrEmpId);
            imageEntity.setActStartDate(DateFormat.getDateTimeInstance().format(new Date()));
            imageEntity.setActEndDate(DateFormat.getDateTimeInstance().format(new Date()));
            imageEntity.setBase64Image("noImage");
            postImageToServer(imageEntity);
            iv_defectfound.setImageResource(R.drawable.noimage);
        }

    }

    private void postImageToServer(DFoundWDoneImageEntity saveRequest) {
        Log.d(TAG, "postImageToServer" + saveRequest.getPpmRefNo());
        mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
        if (mNetworkInfo.length() > 0) {
            if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                receiveComplaintRespond_service.saveComplaintRespondImageData(saveRequest, getActivity());
            } else {
                new DefectDoneImageDbInitializer(mActivity, saveRequest, this)
                        .execute(AppUtils.MODE_INSERT);
            }
        }
    }

    private void dispatchChoosePhotoIntent() {
        ImagePicker fragment = new ImagePicker();
        fragment.SetImagePickListener(this);
        FragmentTransaction ObjTransaction = mManager.beginTransaction();
        ObjTransaction.add(android.R.id.content, fragment, AppUtils.SHARED_DIALOG_PICKER);
        ObjTransaction.addToBackStack(AppUtils.SHARED_DIALOG_PICKER);
        ObjTransaction.commit();
    }

    private void dispatchViewPhotoIntent() {
        RCDownloadImage downloadImage = null;
        if (isIvDefectFound) {
            if (mImageToBeAttachedDefectFound == null) {
                AppUtils.showDialog(mActivity, "No image found");
                return;
            } else {

            }
        }
        Bundle data = new Bundle();
        data.putParcelable(AppUtils.ARGS_RCDOWNLOADIMAGE, downloadImage);
        Fragment fragment = new ViewImage();
        fragment.setArguments(data);
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_container, fragment, Utils.TAG_RCDOWNLOADIMAGE);
        fragmentTransaction.addToBackStack(Utils.TAG_RCDOWNLOADIMAGE);
        fragmentTransaction.commit();
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

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbar.setTitle(mTitle);
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.onBackPressed();
            }
        });
    }

    private void setProperties() {

        tv_lbl_comments.setTypeface(font.getHelveticaRegular());
        tv_comments.setTypeface(font.getHelveticaRegular());
        tv_lbl_photo.setTypeface(font.getHelveticaRegular());
        btn_save.setTypeface(font.getHelveticaRegular());
        btn_save_img.setTypeface(font.getHelveticaRegular());

        if (mSavedInstanceState != null) {
            Log.d(TAG, " mSavedInstanceState : " + mSavedInstanceState);
            receiveComplaintItemEntity = mSavedInstanceState.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS);
            mSelectedPosition = mSavedInstanceState.getInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION);
            complaintViewEntity = mSavedInstanceState.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);
            assetDetailsEntity = mSavedInstanceState.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ASSET_DETAILS);
        }
        AppUtils.closeInput(cl_main);

        iv_defectfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isIvDefectFound = true;
                if (isIvDefectFound) {
                    btn_save.setEnabled(true);
                    if (mImageToBeAttachedDefectFound != null) {
                        if (checkImageLoad) {
                            displayAttachImageDialog();
                        }
                    } else {
                        //if (checkImageLoad) {
                        displayAttachImageDialog();
                        //}
                    }
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternet(getContext())) {
                    AppUtils.showProgressDialog(mActivity, "Loading...", true);
                    saveDataServer();
                }
            }
        });
        btn_save_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        if (mTitle.equalsIgnoreCase(getResources().getString(R.string.title_reactive_hard_services))) {
            if (checkInternet(getContext())) {
              //  AppUtils.showProgressDialog(mActivity, "Loading...", true);
                HardSoftRequest loginRequest = new HardSoftRequest();
                loginRequest.setOpco(receiveComplaintItemEntity.getOpco());
                loginRequest.setComplaintSite(receiveComplaintItemEntity.getSiteCode());
                loginRequest.setComplaintNumber(receiveComplaintItemEntity.getComplaintNumber());
                new HardSoftService(mActivity, this, this)
                        .fetchHardServiceReact(loginRequest);
            }
        }
        if (mTitle.equalsIgnoreCase(getResources().getString(R.string.title_reactive_soft_services))) {
            if (checkInternet(getContext())) {
              //  AppUtils.showProgressDialog(mActivity, "Loading...", true);
                HardSoftRequest loginRequest = new HardSoftRequest();
                loginRequest.setOpco(receiveComplaintItemEntity.getOpco());
                loginRequest.setComplaintSite(receiveComplaintItemEntity.getSiteCode());
                loginRequest.setComplaintNumber(receiveComplaintItemEntity.getComplaintNumber());
                new HardSoftService(mActivity, this, this)
                        .fetchSoftServiceReact(loginRequest);
            }
        }
        if (mTitle.equalsIgnoreCase(getResources().getString(R.string.title_ppm_request_verification_hard))) {
            if (checkInternet(getContext())) {
                HardSoftRequest loginRequest = new HardSoftRequest();
                loginRequest.setPpmNo(receiveComplaintItemEntity.getOpco());
                new HardSoftService(mActivity, this, this)
                        .fetchHardServicePpm(loginRequest);
            }
        }
        if (mTitle.equalsIgnoreCase(getResources().getString(R.string.title_ppm_request_verification_soft))) {
            if (checkInternet(getContext())) {
                HardSoftRequest loginRequest = new HardSoftRequest();
                loginRequest.setPpmNo(receiveComplaintItemEntity.getOpco());
                new HardSoftService(mActivity, this, this)
                        .fetchHardServicePpm(loginRequest);
            }
        }
    }

    private void saveDataServer() {

        if (mTitle.equalsIgnoreCase(getResources().getString(R.string.title_reactive_hard_services))) {
            HardSoftRequest loginRequest = new HardSoftRequest();
            loginRequest.setOpco(receiveComplaintItemEntity.getOpco());
            loginRequest.setComplaintSite(receiveComplaintItemEntity.getSiteCode());
            loginRequest.setComplaintNumber(receiveComplaintItemEntity.getComplaintNumber());
            loginRequest.setSupervisorRemark(tv_comments.getText().toString());
            new HardSoftService(mActivity, this, this)
                    .savedHardServiceReact(loginRequest);
        }
        if (mTitle.equalsIgnoreCase(getResources().getString(R.string.title_reactive_soft_services))) {
            HardSoftRequest loginRequest = new HardSoftRequest();
            loginRequest.setOpco(receiveComplaintItemEntity.getOpco());
            loginRequest.setComplaintSite(receiveComplaintItemEntity.getSiteCode());
            loginRequest.setComplaintNumber(receiveComplaintItemEntity.getComplaintNumber());
            loginRequest.setSupervisorRemark(tv_comments.getText().toString());
            new HardSoftService(mActivity, this, this)
                    .savedSoftServiceReact(loginRequest);
        }
        if (mTitle.equalsIgnoreCase(getResources().getString(R.string.title_ppm_request_verification_hard))) {
            HardSoftRequest loginRequest = new HardSoftRequest();
            loginRequest.setPpmNo(receiveComplaintItemEntity.getOpco());
            loginRequest.setRemarks(tv_comments.getText().toString());
            new HardSoftService(mActivity, this, this)
                    .savedHardServicePpm(loginRequest);
        }
        if (mTitle.equalsIgnoreCase(getResources().getString(R.string.title_ppm_request_verification_soft))) {
            HardSoftRequest loginRequest = new HardSoftRequest();
            loginRequest.setPpmNo(receiveComplaintItemEntity.getOpco());
            loginRequest.setRemarks(tv_comments.getText().toString());
            new HardSoftService(mActivity, this, this)
                    .savedSoftServicePpm(loginRequest);
        }
    }

    private void submitImage(String docType) {
        try {
            if (mTitle.startsWith("R")) {
                AppUtils.showProgressDialog(
                        mActivity, getString(R.string.lbl_no_image_uploading), false);
                DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity("" + "B");
                imageEntity.setOpco(receiveComplaintItemEntity.getOpco());
                imageEntity.setComplaintSite(receiveComplaintItemEntity.getSiteCode());
                imageEntity.setComplaintNo(receiveComplaintItemEntity.getComplaintNumber());
                imageEntity.setFileType("jpg");
                imageEntity.setTransactionType("R");
                imageEntity.setDocType("B");
                imageEntity.setCreatedBy(mStrEmpId);
                imageEntity.setModifiedBy(mStrEmpId);
                imageEntity.setBase64Image("noImage");
                if (docType.equals("B")) {
                    if (mImageToBeAttachedDefectFound != null) {
                        imageEntity.setBase64Image(AppUtils.getEncodedString(mImageToBeAttachedDefectFound));
                        postImageToServer(imageEntity);
                    } else {
                        AppUtils.hideProgressDialog();
                        AppUtils.showDialog(mActivity, "Please add before ppm image");
                    }
                }
                iv_defectfound.setImageResource(R.drawable.noimage);
            } else {
                AppUtils.showProgressDialog(
                        mActivity, getString(R.string.lbl_no_image_uploading), false);
                DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity("" + "B");
                imageEntity.setOpco(receiveComplaintItemEntity.getOpco());
                //imageEntity.setPpmRefNo(receiveComplaintItemEntity.getpPpmRefNo());
                imageEntity.setFileType("jpg");
                imageEntity.setTransactionType("P");
                imageEntity.setDocType("B");
                imageEntity.setCreatedBy(mStrEmpId);
                imageEntity.setModifiedBy(mStrEmpId);
                imageEntity.setActStartDate(DateFormat.getDateTimeInstance().format(new Date()));
                imageEntity.setActEndDate(DateFormat.getDateTimeInstance().format(new Date()));
                if (docType.equals("B")) {
                    if (mImageToBeAttachedDefectFound != null) {
                        imageEntity.setBase64Image(AppUtils.getEncodedString(mImageToBeAttachedDefectFound));
                        postImageToServer(imageEntity);
                    } else {
                        AppUtils.hideProgressDialog();
                        AppUtils.showDialog(mActivity, "Please add before ppm image");
                    }
                }
                iv_defectfound.setImageResource(R.drawable.noimage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gotoNextPage() {
        if (complaintViewEntity != null) {
            if (complaintViewEntity.getComplaintStatus().equals("F")) {
                AppUtils.showDialog(mActivity, "Complaint Status Forwarded..");
            } else {
                mSavedInstanceState = getSavedState();
                Bundle data = new Bundle();
                data.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, complaintViewEntity);
                data.putInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION, mSelectedPosition);
                Fragment_RC_Respond fragment = new Fragment_RC_Respond();
                fragment.setArguments(data);
                FragmentTransaction fragmentTransaction = mManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_RESPOND);
                fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_RESPOND);
                fragmentTransaction.commit();
            }
        } else AppUtils.showDialog(mActivity, "Please Wait Respond data getting..");
    }

    public void getReceiveComplainViewFromService(ReceiveComplaintItemEntity receiveComplaintItemEntity) {
        Log.d(TAG, "getReceiveComplainViewFromService");
        try {
            mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
            if (mNetworkInfo.length() > 0) {
                if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                    Log.d(TAG, "getReceiveComplainViewFromServer");
                    AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                    complaintView_service.GetReceiveComplaintViewData(new ReceiveComplaintViewRequest(
                            receiveComplaintItemEntity.getComplaintNumber(),
                            receiveComplaintItemEntity.getSiteCode(),
                            receiveComplaintItemEntity.getOpco()));

                    Log.d(TAG, "getReceiveComplainRemarksFromService");
                    complaintView_service.getReceiveComplaintViewRemarksData();
                } else {
                    Log.d(TAG, "getReceiveComplainViewFromLocal");
                    ReceiveComplaintViewEntity complaintViewValue = new ReceiveComplaintViewEntity();
                    complaintViewValue.setComplaintNumber(receiveComplaintItemEntity.getComplaintNumber());

                }
            } else
                AppUtils.showDialog(mActivity, getString(R.string.lbl_alert_network_not_available));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void postDataToServer(ReceiveComplaintRespondEntity respondRequest) {
        Log.d(TAG, "postDataToServer");

        mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
        if (mNetworkInfo.length() > 0) {
            if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                complaintView_service.postReceiveComplaintViewData(respondRequest);
            } else
                complaintRespondDbInitializer.populateAsync(AppDatabase.getAppDatabase(mActivity), respondRequest, AppUtils.MODE_INSERT_SINGLE);
        }
    }

    @Override
    public void onReceiveComplaintRemarksReceived(List<String> remarkList, int mode) {
        this.remarkList = remarkList;
    }

    @Override
    public void onReceiveComplaintRespondReceived(String strMsg, String complaintNumber, int mode) {
        Log.d(TAG, "onReceiveComplaintRespondReceived");

    }

    @Override
    public void onReceiveComplaintViewReceived(List<ReceiveComplaintViewEntity> receiveComplaintItemView, int from) {
        AppUtils.hideProgressDialog();
        Log.d(TAG, "onReceiveComplaintViewReceived" + receiveComplaintItemView.size());
    }

    @Override
    public void onReceiveComplaintViewAssetDetailsReceived(List<AssetDetailsEntity> assetDetailsEntities, int from) {

        Log.d(TAG, "onReceiveComplaintViewAssetDetailsReceived " + assetDetailsEntities.size());
        AppUtils.hideProgressDialog();
    }

    @Override
    public void onReceiveComplaintViewReceivedError(String strErr, int from) {
        Log.d(TAG, "onReceiveComplaintViewReceivedError");
        try {
            AppUtils.hideProgressDialog();
            Fragment main = mManager.findFragmentByTag(Utils.TAG_RECEIVE_COMPLAINT_VIEW);
            if (main != null && main.isVisible())
                AppUtils.showDialog(mActivity, strErr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveBarCodeAssetReceived(String msg, int mode) {

    }

    @Override
    public void onAllReceiveComplaintData(List<ReceiveComplaintRespondEntity> complaintRespondEntities, int modeLocal) {

    }

    @Override
    public void onReceiveComplaintBycomplaintNumber(ReceiveComplaintRespondEntity complaintRespondEntity, int modeLocal) {

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
                //mActivity.onBackPressed();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                Fragment _fragment = new MainDashboard();
                FragmentTransaction _transaction = mManager.beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        mSavedInstanceState = getSavedStateOnPause();
    }

    public Bundle getSavedStateOnPause() {
        Bundle outState = new Bundle();
        outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, complaintViewEntity);
        outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS, receiveComplaintItemEntity);
        outState.putInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION, mSelectedPosition);
        outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ASSET_DETAILS, assetDetailsEntity);
        return outState;
    }

    public Bundle getSavedState() {
        Bundle outState = new Bundle();
        outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, complaintViewEntity);
        outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS, receiveComplaintItemEntity);
        outState.putInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION, mSelectedPosition);
        outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ASSET_DETAILS, assetDetailsEntity);
        return outState;
    }

    @Override
    public void onSingleImagePicked(String Str_Path) {
        try {
            AppUtils.showProgressDialog(mActivity, "Processing image", false);
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
                                if (isIvDefectFound) {
                                    if (AppUtils.checkImageFileSize(loadedImage)) {
                                        mImageToBeAttachedDefectFound = loadedImage;
                                        iv_defectfound.setImageBitmap(
                                                ThumbnailUtils.extractThumbnail(
                                                        loadedImage, THUMBNAIL_SIZE, THUMBNAIL_SIZE));
                                    } else {
                                       // AppUtils.showDialog(mActivity, getString(R.string.file_size_more_then));
                                        mImageToBeAttachedDefectFound = loadedImage;
                                        iv_defectfound.setImageBitmap(
                                                ThumbnailUtils.extractThumbnail(
                                                        loadedImage, THUMBNAIL_SIZE, THUMBNAIL_SIZE));
                                    }
                                }
                            }
                            AppUtils.closeInput(cl_main);
                            AppUtils.hideProgressDialog();
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
        AppUtils.hideProgressDialog();
        if (chekNoimage == 1) {
            btn_save_img.setEnabled(false);
            btn_save_img.setBackgroundColor(getResources().getColor(R.color.color_gray));
        } else {
            btn_save_img.setEnabled(true);
            AppUtils.showDialog(mActivity, "Data has been successfully Saved.");
        }
    }

    @Override
    public void onImageReceivedSuccess(RCDownloadImage imageEntity, int mode) {
        try {
            checkImageLoad = true;
            if (imageEntity.getBase64Image() != null && imageEntity.getDocType() != null) {
                Log.d(TAG, "getDocType :" + imageEntity.getDocType());
                if (imageEntity.getBase64Image().get(0).equalsIgnoreCase("noImage")) {
                    iv_defectfound.setImageResource(R.drawable.noimage);
                    btn_save.setEnabled(false);
                } else {
                    btn_save.setEnabled(true);
                    if (imageEntity.getDocType().equals("B")) {
                        mImageToBeAttachedDefectFound = AppUtils.getDecodedString(imageEntity.getBase64Image().get(0));
                        iv_defectfound.setImageBitmap(
                                ThumbnailUtils.extractThumbnail(
                                        mImageToBeAttachedDefectFound, THUMBNAIL_SIZE, THUMBNAIL_SIZE));
                        pb_workdone.setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageSaveReceivedFailure(String strErr, int mode) {
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity, strErr);
    }

    @Override
    public void onImageReceivedFailure(String strErr, int mode) {
        iv_defectfound.setEnabled(true);
        checkImageLoad = true;
        if (strErr.toUpperCase().equals("B")) {
            pb_workdone.setVisibility(View.GONE);
        } else pb_workdone.setVisibility(View.GONE);
    }

    @Override
    public void onAllImagesReceived(List<DFoundWDoneImageEntity> mImageEntities, int mode) {

    }

    @Override
    public void onHardSoftReceivedSuccess(HardSoftView hardSoftView, int mode) {
        AppUtils.hideProgressDialog();
        if(hardSoftView!=null){
            hardSoftRequest = new HardSoftRequest();
            hardSoftRequest.setComplaintSite(hardSoftView.getComplaintSite());
            hardSoftRequest.setJobNumber(hardSoftView.getJobNumber());
            hardSoftRequest.setFromDate(SessionManager.getSession("fromDataSave",mActivity));
            hardSoftRequest.setToDate(SessionManager.getSession("toDataSave",mActivity));
            hardSoftRequest.setZoneCode(hardSoftView.getZoneCode());
            hardSoftRequest.setBuildingCode(hardSoftView.getBuildingCode());
            tv_comments.setText(hardSoftView.getSupervisorRemark());
        }
    }

    @Override
    public void onHardSoftReceivedFailure(String strErr, int mode) {
        AppUtils.hideProgressDialog();
    }

    @Override
    public void onHardSoftSaveSucess(String strErr) {
        AppUtils.hideProgressDialog();
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity)
                .content(strErr)
                .positiveText(R.string.lbl_okay)
                .stackingBehavior(StackingBehavior.ADAPTIVE)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        Bundle data = new Bundle();
                        data.putParcelable(AppUtils.ARGS_REACTIVE_MAINTENANCE_DASHBOARD_REQUEST, hardSoftRequest);
                        data.putString(AppUtils.ARGS_IM_SERVICES_Page_TITLE, mTitle);
                        Fragment_IM_HardSoftList fragment = new Fragment_IM_HardSoftList();
                        fragment.setArguments(data);
                        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame_container, fragment, TAG_FRAGMENT_IM_HARDSOFTLIST);
                        fragmentTransaction.addToBackStack(TAG_FRAGMENT_IM_HARDSOFTLIST);
                        fragmentTransaction.commit();
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    @Override
    public void onReceiveComplaintListItemClicked(List<ReceiveComplaintItemEntity> temp, int position) {

    }

    @Override
    public void onReceiveComplaintListItemChecked(List<ReceiveComplaintItemEntity> data) {

    }

    @Override
    public void onReceiveComplaintCCReceived(String strMsg, int from) {

    }

    @Override
    public void onReceiveComplaintListReceived(List<ReceiveComplaintItemEntity> receiveComplaintItemList, String noOfRows, int from) {

    }

    @Override
    public void onReceiveComplaintListReceivedUpdate(List<ReceiveComplaintItemEntity> receiveComplaintItemList, ComplaintStatusEntity complaintStatusEntity, int from) {

    }

    @Override
    public void onReceiveComplaintListReceivedError(String errMsg, int from) {

    }

    @Override
    public void onImageSaveReceivedSuccess1(RCDownloadImage imageEntity, int mode) {

    }
}