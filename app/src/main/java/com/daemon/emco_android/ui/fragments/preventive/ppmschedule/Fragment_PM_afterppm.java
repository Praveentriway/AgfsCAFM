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
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.activities.BarcodeCaptureActivity;
import com.daemon.emco_android.ui.adapter.CustomRecyclerViewDataAdapter;
import com.daemon.emco_android.ui.adapter.CustomRecyclerViewItem;
import com.daemon.emco_android.repository.remote.ReceiveComplaintRespondService;
import com.daemon.emco_android.repository.db.dbhelper.DefectDoneImageDbInitializer;
import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.ui.fragments.common.ImagePicker;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.listeners.DefectDoneImage_Listener;
import com.daemon.emco_android.listeners.ImagePickListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.RCDownloadImageRequest;
import com.daemon.emco_android.model.response.DefectDoneImageUploaded;
import com.daemon.emco_android.model.response.RCDownloadImage;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.ImageUtil;
import com.daemon.emco_android.utils.Utils;
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
import static com.daemon.emco_android.utils.Utils.TAG_PPM_FINDING;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_FEEDBACK;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_MATERIALREQUIRED;

public class Fragment_PM_afterppm extends Fragment implements ImagePickListener, DefectDoneImage_Listener,CustomRecyclerViewDataAdapter.ImageListner  {
    private static final String TAG = Fragment_PM_afterppm.class.getSimpleName();
    public Bundle mSavedInstanceState;
    private AppCompatActivity mActivity;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private FragmentManager mManager;
    private Bundle mArgs;
    private Button btn_next;
    private ReceiveComplaintViewEntity mReceiveComplaintView = new ReceiveComplaintViewEntity();
    private PpmScheduleDocBy ppmScheduleDocBy;
    private ImageView iv_workdone;
    private TextView tv_toolbar_title,img_upload_count;
    private boolean isIvDefectFound = false;
    private Bitmap mImageToBeAttachedDefectFound;
    //private Bitmap mImageToBeAttached;
    Bitmap thumbnail = null;
    private int imgCount=0;
    private boolean isPermissionGranted = false;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 4;
    private String mImagePathToBeAttached;
    private final int REQUEST_CHOOSE_PHOTO = 2;
    private final int REQUEST_TAKE_PHOTO = 1;
    private final int THUMBNAIL_SIZE = 75;
    boolean asyncRunning=false;
    private ImageLoader imageLoader;
    private String mNetworkInfo = null;
    private ReceiveComplaintRespondService receiveComplaintRespond_service;
    private Button btn_save;
    private int imageCount=0;
    private ProgressBar pb_workdone;
    private boolean checkImageLoad = false;
    private CharSequence[] items;
    private CoordinatorLayout cl_main;
    private TextView tv_header;
    private RecyclerView recyclerView = null;
    private ArrayList<CustomRecyclerViewItem> itemList  =new ArrayList<>();
    private CustomRecyclerViewDataAdapter customRecyclerViewDataAdapter = null;
    View.OnClickListener _OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick");
                    AppUtils.closeInput(cl_main);
                    switch (v.getId()) {
                        case R.id.btn_feedback:
                            gotoFeedBackPage();
                            break;
                        case R.id.btn_ppm_finding:
                           gotoPPMFindings();
                            break;
                        case R.id.btn_material_used:
                            gotoMaterialUsed();
                            break;
                        case R.id.btn_next:
                            gotoNextPage();
                            break;
                        case R.id.btn_respond:
                            break;
                        case R.id.tv_select_remarks:
                            break;

                        case R.id.btn_barcode_scan:
                            int requestId = AppUtils.getIdForRequestedCamera(AppUtils.CAMERA_FACING_BACK);
                            if (requestId == -1)
                                AppUtils.showDialog(mActivity, "Camera not available");
                            else {
                                Intent intent = new Intent(mActivity, BarcodeCaptureActivity.class);
                                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                            }
                            break;

                        default:
                            break;
                    }
                }
            };
    private Toolbar mToolbar;
    private View rootView;
    private String mStrEmpId = null;
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
            mManager = mActivity.getSupportFragmentManager();
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            font = App.getInstance().getFontInstance();
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            mSavedInstanceState = savedInstanceState;
            imageLoader = ImageLoader.getInstance();
            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }

            receiveComplaintRespond_service = new ReceiveComplaintRespondService(mActivity);
            // receiveComplaintRespond_service.setmCallback(this);
            receiveComplaintRespond_service.setmCallbackImages(this);
            if (mArgs != null && mArgs.size() > 0) {
                // Check if true material required page other wise material used page
                ppmScheduleDocBy =
                        mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);
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
            rootView = inflater.inflate(R.layout.fragment_after_ppm, container, false);
            initUI(rootView);
            setProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void initUI(View rootView) {
        Log.d(TAG, "initUI");
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            btn_next = (Button) rootView.findViewById(R.id.btn_feedback);
            btn_next.setOnClickListener(_OnClickListener);
            btn_next = (Button) rootView.findViewById(R.id.btn_ppm_finding);
            btn_save = (Button) rootView.findViewById(R.id.btn_save);
            btn_next.setOnClickListener(_OnClickListener);
            btn_next = (Button) rootView.findViewById(R.id.btn_material_used);
            btn_next.setOnClickListener(_OnClickListener);
            btn_next = (Button) rootView.findViewById(R.id.btn_next);
            tv_header= (TextView) rootView.findViewById(R.id.tv_header);
            btn_next.setOnClickListener(_OnClickListener);
            iv_workdone = (ImageView) rootView.findViewById(R.id.iv_workdone);
            pb_workdone = (ProgressBar) rootView.findViewById(R.id.pb_workdone);
            img_upload_count=(TextView) rootView.findViewById(R.id.img_upload_count);

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

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
       // mToolbar.setTitle(getResources().getString(R.string.lbl_afterppm));
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.lbl_afterppm));
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
        Log.d(TAG, "setProperties");

        AppUtils.closeInput(cl_main);


        tv_header.setText(ppmScheduleDocBy.getPpmNo()+" - "+ppmScheduleDocBy.getLocation());


        mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
        if (mNetworkInfo != null && mNetworkInfo.length() > 0) {
            if (mImageToBeAttachedDefectFound == null) {
                if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                    // Download defect found image
                    RCDownloadImageRequest imageRequest = new RCDownloadImageRequest();
                    imageRequest.setOpco(ppmScheduleDocBy.getCompanyCode());
                    imageRequest.setDocType("A");
                    imageRequest.setPpmRefNo(ppmScheduleDocBy.getPpmNo());
                    pb_workdone.setVisibility(View.VISIBLE);
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


        btn_save.setOnClickListener(new View.OnClickListener() {
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
                                        submitImage("A");
                                    }
                                },
                                1000);
            }
        });


        if (mSavedInstanceState != null) {


            if(mSavedInstanceState.getParcelableArrayList(AppUtils.ARGS_IMAGE_LIST)!=null) {
                itemList=new ArrayList<CustomRecyclerViewItem>();
                itemList=(mSavedInstanceState.getParcelableArrayList(AppUtils.ARGS_IMAGE_LIST));
                img_upload_count.setVisibility(View.VISIBLE);
                img_upload_count.setText((itemList.size()) + "/" + imgCount + " Image uploaded");
                // load recyclerview from cache
                customRecyclerViewDataAdapter = new CustomRecyclerViewDataAdapter(itemList, imgCount, this, getContext(), false);
                recyclerView.setAdapter(customRecyclerViewDataAdapter);

            }
        }

        }

    private void displayAttachImageDialog(final int count, final String base64) {
        if (!isPermissionGranted) {
            getPermissionToReadExternalStorage( count, base64);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        //if (isIvDefectFound) {
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
        // }
    }


    private void noImageAvailabe(int count,String base64) {
        AppUtils.showProgressDialog(
                mActivity, getString(R.string.lbl_no_image_uploading), false);
        DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity("" + "B");
        imageEntity.setOpco(ppmScheduleDocBy.getCompanyCode());
        imageEntity.setFileType("jpg");
        imageEntity.setTransactionType("P");
        imageEntity.setDocType("A");
        imageEntity.setPpmRefNo(ppmScheduleDocBy.getPpmNo());
        imageEntity.setCreatedBy(mStrEmpId);
        imageEntity.setModifiedBy(mStrEmpId);
        imageEntity.setImageCount(count);
        imageEntity.setActStartDate(DateFormat.getDateTimeInstance().format(new Date()));
        imageEntity.setBase64Image(base64);
        postImageToServer(imageEntity);
        iv_workdone.setImageResource(R.drawable.noimage);
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

                return  new RCDownloadImage(images, "A",0);
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


    private void loadPhoto(String base64,int width, int height) {

        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup) rootView.findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageBitmap(AppUtils.getDecodedString(base64));
        image.getLayoutParams().height = height;
        image.getLayoutParams().width = width;
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);
        image.requestLayout();
        dialog.setContentView(layout);
        dialog.show();
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
                Fragment _fragment = new MainLandingUI();
                FragmentTransaction _transaction = mManager.beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoNextPage() {
        Bundle mdata = new Bundle();
        mdata.putParcelable(AppUtils.ARGS_PPM_FEEDBACK, ppmScheduleDocBy);
        mdata.putString(AppUtils.ARGS_PPM_FEEDBACK_CHECK, "ppmcheckList");
        Fragment_PM_RateService fragment = new Fragment_PM_RateService();
        fragment.setArguments(mdata);
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(
                R.id.frame_container, fragment, Utils.TAG_RECEIVE_COMPLAINT_RESPOND);
        fragmentTransaction.addToBackStack(Utils.TAG_RECEIVE_COMPLAINT_RESPOND);
        fragmentTransaction.commit();
    }

    private void gotoFeedBackPage() {

        mSavedInstanceState = getSavedState();

        try {
            // if (mArgs != null && mArgs.size() > 0) {
            Bundle data = new Bundle();
            data.putParcelable(AppUtils.ARGS_FEEDBACK_VIEW_DETAILS, ppmScheduleDocBy);
            Fragment fragment = new Fragment_PPM_Feedback();
            fragment.setArguments(data);
            FragmentTransaction fragmentTransaction = mManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            fragmentTransaction.replace(R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_FEEDBACK);
            fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_FEEDBACK);
            fragmentTransaction.commit();
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void gotoPPMFindings() {
        mSavedInstanceState = getSavedState();
        Bundle data = new Bundle();
        data.putParcelable(AppUtils.ARGS_TOOL_LIST, ppmScheduleDocBy);
        Fragment_PPM_Finding fragment = new Fragment_PPM_Finding();
        fragment.setArguments(data);
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, TAG_PPM_FINDING);
        fragmentTransaction.addToBackStack(TAG_PPM_FINDING);
        fragmentTransaction.commit();
    }

    private void gotoMaterialUsed() {

        mSavedInstanceState = getSavedState();

        try {
            // if (mArgs != null && mArgs.size() > 0) {
            Bundle data = new Bundle();
            data.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, ppmScheduleDocBy);
            data.putString(AppUtils.ARGS_MATERIAL_PAGE_STRING, AppUtils.ARGS_MATERIAL_USED_STRING);
            Fragment fragment = new Fragment_PPM_Material();
            fragment.setArguments(data);
            FragmentTransaction fragmentTransaction = mManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            fragmentTransaction.replace(
                    R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_MATERIALREQUIRED);
            fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_MATERIALREQUIRED);
            fragmentTransaction.commit();
            // }
        } catch (Exception e) {
            e.printStackTrace();
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
        try {
            outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, mReceiveComplaintView);

            ArrayList<CustomRecyclerViewItem> tt=itemList;
            tt.removeAll(Collections.singleton(null));
            outState.putParcelableArrayList(AppUtils.ARGS_IMAGE_LIST,tt);

            Log.d(TAG, "getSavedState");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outState;
    }

    @Override
    public void onSingleImagePicked(String Str_Path) {
        try {
           // AppUtils.showProgressDialog(mActivity, "Processing image", false);
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
                                       // AppUtils.showDialog(mActivity, getString(R.string.file_size_more_then));
                                        mImageToBeAttachedDefectFound = loadedImage;
                                        iv_workdone.setImageBitmap(
                                                ThumbnailUtils.extractThumbnail(
                                                        loadedImage, THUMBNAIL_SIZE, THUMBNAIL_SIZE));
                                    }

                                    submitImage("A");
                            }
                            AppUtils.closeInput(cl_main);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                        }
                    });
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
                    imageEntity.setOpco(ppmScheduleDocBy.getCompanyCode());
                    imageEntity.setFileType("jpg");
                    imageEntity.setTransactionType("P");
                    imageEntity.setDocType(docType);
                    imageEntity.setPpmRefNo(ppmScheduleDocBy.getPpmNo());
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
        Log.d(TAG, "postImageToServer");
        mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
        if (mNetworkInfo.length() > 0) {
            if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                receiveComplaintRespond_service.saveComplaintRespondImageData1(saveRequest, getActivity());
            } else {
                new DefectDoneImageDbInitializer(mActivity, saveRequest, this)
                        .execute(AppUtils.MODE_INSERT);
            }
        }
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
                submitImage("A");
            }
            mImagePathToBeAttached = null;
        } else if (requestCode == REQUEST_CHOOSE_PHOTO) {
            try {
                Uri uri = data.getData();
                ContentResolver resolver = mActivity.getContentResolver();

                    mImageToBeAttachedDefectFound = MediaStore.Images.Media.getBitmap(resolver, uri);


                AssetFileDescriptor asset = resolver.openAssetFileDescriptor(uri, "r");
                thumbnail =
                        ImageUtil.thumbnailFromDescriptor(
                                asset.getFileDescriptor(), THUMBNAIL_SIZE, THUMBNAIL_SIZE);


            } catch (IOException e) {
                Log.d(TAG, "Cannot get a selected photo from the gallery.", e);
            }
        }

    }
    @Override
    public void onImageSaveReceivedSuccess(DefectDoneImageUploaded mImageEntity, int mode) {
        AppUtils.hideProgressDialog();
        if (chekNoimage == 1) {
            btn_save.setEnabled(false);
        } else {
            btn_save.setEnabled(true);
            AppUtils.showSnackBar(R.id.coordinatorLayout, rootView, "Image has been successfully Saved.");
        }
        Fragment main = mManager.findFragmentByTag(Utils.TAG_RECEIVE_COMPLAINT_RESPOND);
    }

    @Override
    public void onImageSaveReceivedSuccess1(RCDownloadImage imageEntity, int mode) {
        processImage(imageEntity,true);
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
                //pb_defectfound.setVisibility(View.GONE);
            } else pb_workdone.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAllImagesReceived(List<DFoundWDoneImageEntity> mImageEntities, int mode) {

    }


    @Override
    public void onImageClick(int pos, String base64){
        imageCount=pos;
        displayAttachImageDialog(pos,base64);
    }

    public void showNoImageAlert(final int count){

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Are you sure you want to add No Image?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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


    public String convertImageviewBase64(){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image=stream.toByteArray();
        return Base64.encodeToString(image, 0);
    }



    public void processImage(final RCDownloadImage imageEntity, final boolean fromUpload) {

        try {
            itemList = new ArrayList<CustomRecyclerViewItem>();
            imgCount=imageEntity.getImageCount();


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
                            imageEntity1.setOpco(ppmScheduleDocBy.getCompanyCode());
                            imageEntity1.setFileType("jpg");
                            imageEntity1.setTransactionType("P");
                            imageEntity1.setDocType("A");
                            imageEntity1.setPpmRefNo(ppmScheduleDocBy.getPpmNo());
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
