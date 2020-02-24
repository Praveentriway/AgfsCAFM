package com.daemon.emco_android.ui.fragments.reactive.receieve_complaints;

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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.daemon.emco_android.ui.fragments.common.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
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
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.CustomRecyclerViewDataAdapter;
import com.daemon.emco_android.ui.adapter.CustomRecyclerViewItem;
import com.daemon.emco_android.ui.adapter.ReceivecomplaintListAdapter;
import com.daemon.emco_android.repository.remote.FeedBackRepository;
import com.daemon.emco_android.repository.remote.PpeService;
import com.daemon.emco_android.repository.remote.ReceiveComplaintRespondService;
import com.daemon.emco_android.ui.components.CustomTextInputLayout;
import com.daemon.emco_android.ui.components.FilterableListDialog;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.dbhelper.DefectDoneImageDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ReceiveComplaintViewDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.WorkDefectsDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.WorkDoneDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.WorkPendingReasonDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.WorkStatusDbInitializer;
import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;
import com.daemon.emco_android.repository.db.entity.EmployeeDetailsEntity;
import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.daemon.emco_android.repository.db.entity.PPENameEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntity;
import com.daemon.emco_android.repository.db.entity.WorkDefectEntity;
import com.daemon.emco_android.repository.db.entity.WorkDoneEntity;
import com.daemon.emco_android.repository.db.entity.WorkPendingReasonEntity;
import com.daemon.emco_android.repository.db.entity.WorkStatusEntity;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.listeners.DatePickerDialogListener;
import com.daemon.emco_android.listeners.DefectDoneImage_Listener;
import com.daemon.emco_android.listeners.FeedbackListener;
import com.daemon.emco_android.listeners.ImagePickListener;
import com.daemon.emco_android.listeners.PpeListener;
import com.daemon.emco_android.listeners.RespondComplaintListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.FetchFeedbackRequest;
import com.daemon.emco_android.model.request.RCDownloadImageRequest;
import com.daemon.emco_android.model.request.WorkDefectRequest;
import com.daemon.emco_android.model.request.WorkDoneRequest;
import com.daemon.emco_android.model.response.DefectDoneImageUploaded;
import com.daemon.emco_android.model.response.PpmEmployeeFeedResponse;
import com.daemon.emco_android.model.response.RCDownloadImage;
import com.daemon.emco_android.model.response.RCRespond;
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
import java.util.Locale;

import uk.co.senab.photoview.PhotoViewAttacher;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.daemon.emco_android.utils.Utils.TAG_RATE_AND_SHARE;
import static com.daemon.emco_android.utils.Utils.TAG_REACTIVE_MAINTENANCE;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_FEEDBACK;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_MATERIALREQUIRED;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_PPE;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_RATE__AND_FEEDBACK;

public class Fragment_RC_Respond extends Fragment
        implements DatePickerDialogListener, PpeListener,
        RespondComplaintListener,
        ImagePickListener, FeedbackListener,
        DefectDoneImage_Listener, CustomRecyclerViewDataAdapter.ImageListnerR {
    private final String TAG = Fragment_RC_Respond.class.getSimpleName();
    ReceiveComplaintViewEntity requestToServer;
    boolean asyncRunning=false;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 4;
    private final int REQUEST_READ_EXTERNAL_STORAGE = 5;
    private FeedBackRepository feedBackService;
    private final int REQUEST_TAKE_PHOTO = 1;
    private final int REQUEST_CHOOSE_PHOTO = 2;
    private final int THUMBNAIL_SIZE = 75;
    public Bundle mSavedInstanceState;
    private String mComplaintSite = null, mOpco = null;
    private String imgType;

    private int imageCount1=0,imageCount2=0;

    private int imageTotalCount=0;

    private String DefectFound="DefectFound",WorkDone="WorkDone";
    private String mNetworkInfo = null,
            mComplaintCode = null,
            mComplaintStatus = null,
            mComplaintNumber = null,
            mDefectCode = null,
            mSelectDefect = null,
            mWorkCategory = null,
            mComplaint = null;
    private String mPendingReasonCode = null,
            mWorkStatusCode = null,
            mWorkDoneCode = null,
            mSelectDate = null;
    Bitmap thumbnail = null;
    private boolean confirm = false;

    RCDownloadImage imageEntityB,imageEntityA;

    private boolean ppeList=false;
    private boolean feedBackList=false;

    private boolean checkImageLoad = false;
    private String mSelectPendingReason = null, mSelectWorkStatus = null, mSelectWorkDone = null;
    private ReceiveComplaintViewEntity mReceiveComplaintView;
    private ImageLoader imageLoader;
    private String mUnSignedPage = AppUtils.ARGS_RECEIVECOMPLAINT_PAGE;
    private Bitmap mImageToBeAttachedDefectFound;
    private Bitmap mImageToBeAttached;
    private String mImagePathToBeAttached;
    private boolean isIvDefectFound = false;
    private AppCompatActivity mActivity;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private FragmentManager mManager;
    private CoordinatorLayout cl_main;
    private CustomTextInputLayout til_defectsfound, til_workdone, til_workpending;
    private TextInputEditText tie_defectsfound, tie_workdone, tie_workpending;
    private ImageView iv_defectfound, iv_workdone;
    private ProgressBar pb_defectfound, pb_workdone;
    private LinearLayout ll_pending_reason, ll_materials_required, ll_workdone;
    private TextView tv_lbl_complaint,
            tv_toolbar_title,
            tv_lbl_defectsfound,
            tv_lbl_workdone,
            tv_lbl_workstatus,
            tv_lbl_reason,
            tv_lbl_tentative_date;
    private TextView tv_complaint,
            tv_select_defectsfound,
            tv_select_workdone,
            tv_select_workstatus,
            tv_select_reason,
            tv_select_tentative_date,img_upload_count1,img_upload_count2;
    private Button btn_done_save, btn_defect_save, btn_respond_save;
    private Button btn_ppe, btn_feedback, btn_material_reqd, btn_material_used;

    private WorkStatusDbInitializer mWorkStatusInitializer;
    private WorkPendingReasonDbInitializer mWorkPendingInitializer;
    private ReceiveComplaintRespondService receiveComplaintRespond_service;

    private List<WorkStatusEntity> mWorkStatusEntityList = new ArrayList<>();
    private List<WorkDefectEntity> mWorkDefectsEntityList = new ArrayList<>();
    private List<WorkDoneEntity> mWorkDoneEntityList = new ArrayList<>();
    private List<WorkPendingReasonEntity> mWorkPendingReasonList = new ArrayList<>();
    private Bundle mArgs = new Bundle();
    private Toolbar mToolbar;
    private int checkNoImageStatus;
    private View rootView;
    private String mLoginData = null;
    private String mStrEmpId = null;
    private ReceivecomplaintListAdapter adapter;
    private PpeService ppeService;
    private RecyclerView recyclerView;
    private int mSelectedPosition = 0;
    private int checkNormalNoImage;
    private CharSequence[] items;
    private boolean isPermissionGranted = false;

    private RecyclerView recyclerviewImage1,recyclerviewImage2 = null;
    private ArrayList<CustomRecyclerViewItem> itemList1  =new ArrayList<>();
    private ArrayList<CustomRecyclerViewItem> itemList2  =new ArrayList<>();
    private CustomRecyclerViewDataAdapter customRecyclerViewDataAdapter1,customRecyclerViewDataAdapter2 = null;

    View.OnClickListener _OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick");
                    AppUtils.closeInput(cl_main);
                    switch (v.getId()) {
                        case R.id.btn_ppe:
                            gotoPPEPage();
                            break;
                        case R.id.btn_feedback:
                            gotoFeedBackPage();
                            break;
                        case R.id.btn_material_reqd:
                            gotoMaterialRequired();
                            break;
                        case R.id.btn_material_used:
                            gotoMaterialUsed();
                            break;
                        case R.id.btn_respond_save:
                            confirm = false;
                            submitForm();
                            break;
                        case R.id.tv_select_defectsfound:
                            getDefectsFound();
                            break;
                        case R.id.tv_select_workdone:
                            getWorkDone();
                            break;
                        case R.id.tv_select_workstatus:
                            getWorkStatus();
                            break;
                        case R.id.tv_select_reason:
                            getReason();
                            break;
                        case R.id.tv_select_tentative_date:
                            AppUtils.datePickerDialog(mActivity, Fragment_RC_Respond.this, new Date(), null);
                            Log.d(TAG, "getDate ");
                            break;
                        case R.id.iv_defectfound:

                            break;
                        case R.id.iv_workdone:

                            break;
                        case R.id.btn_defect_save:

                            break;
                        case R.id.btn_done_save:

                            break;

                        default:
                            break;
                    }
                }
            };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(true);
            mContext = mActivity;
            imageLoader = ImageLoader.getInstance();
            ppeService = new PpeService(mActivity, this);
            feedBackService = new FeedBackRepository(mActivity, this);
            // initialize local db
            mWorkStatusInitializer = new WorkStatusDbInitializer(this);
            mWorkPendingInitializer = new WorkPendingReasonDbInitializer(this);

            font = App.getInstance().getFontInstance();
            mArgs = getArguments();
            mSavedInstanceState = savedInstanceState;
            receiveComplaintRespond_service = new ReceiveComplaintRespondService(mActivity);
            receiveComplaintRespond_service.setmCallback(this);
            receiveComplaintRespond_service.setmCallbackImages(this);

            mManager = mActivity.getSupportFragmentManager();
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }

            if (mActivity.getCurrentFocus() != null) {
                InputMethodManager imm =
                        (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
            }
            getParcelableData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        try {
            rootView = inflater.inflate(R.layout.fragment_receivecomplaints_respond, container, false);
            initUI(rootView);
            setProperties();
            setPropertiesValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void getPopupData() {
        try {
            gettingWorkDefects();
            gettingWorkStatus();
            if (!mComplaintStatus.equals("C")) {
                gettingWorkPending();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI(View rootView) {
        Log.d(TAG, "initUI");
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            ll_pending_reason = (LinearLayout) rootView.findViewById(R.id.ll_pending_reason);
            ll_materials_required = (LinearLayout) rootView.findViewById(R.id.ll_button_materials);
            ll_workdone = (LinearLayout) rootView.findViewById(R.id.ll_workdone);

            img_upload_count1 = (TextView) rootView.findViewById(R.id.img_upload_count);
            img_upload_count2 = (TextView) rootView.findViewById(R.id.img_upload_count2);

            tv_lbl_complaint = (TextView) rootView.findViewById(R.id.tv_lbl_complaint);
            tv_lbl_defectsfound = (TextView) rootView.findViewById(R.id.tv_lbl_defectsfound);
            tv_lbl_workdone = (TextView) rootView.findViewById(R.id.tv_lbl_workdone);
            tv_lbl_workstatus = (TextView) rootView.findViewById(R.id.tv_lbl_workstatus);
            tv_lbl_reason = (TextView) rootView.findViewById(R.id.tv_lbl_reason);
            tv_lbl_tentative_date = (TextView) rootView.findViewById(R.id.tv_lbl_tentative_date);
            tv_select_reason = (TextView) rootView.findViewById(R.id.tv_select_reason);
            tv_select_workstatus = (TextView) rootView.findViewById(R.id.tv_select_workstatus);
            tv_select_defectsfound = (TextView) rootView.findViewById(R.id.tv_select_defectsfound);
            tv_select_workdone = (TextView) rootView.findViewById(R.id.tv_select_workdone);
            tv_select_tentative_date = (TextView) rootView.findViewById(R.id.tv_select_tentative_date);
            tv_complaint = (TextView) rootView.findViewById(R.id.tv_complaint);

            til_workdone = (CustomTextInputLayout) rootView.findViewById(R.id.til_workdone);
            til_defectsfound = (CustomTextInputLayout) rootView.findViewById(R.id.til__defectsfound);
            til_workpending = (CustomTextInputLayout) rootView.findViewById(R.id.til_workpending);

            tie_defectsfound = (TextInputEditText) rootView.findViewById(R.id.tie_defectsfound);
            tie_workdone = (TextInputEditText) rootView.findViewById(R.id.tie_workdone);
            tie_workpending = (TextInputEditText) rootView.findViewById(R.id.tie_workpending);

            iv_defectfound = (ImageView) rootView.findViewById(R.id.iv_defectfound);
            iv_workdone = (ImageView) rootView.findViewById(R.id.iv_workdone);
            // iv_workdone.setEnabled(false);
            // iv_defectfound.setEnabled(false);
            pb_defectfound = (ProgressBar) rootView.findViewById(R.id.pb_defectfound);
            pb_workdone = (ProgressBar) rootView.findViewById(R.id.pb_workdone);

            btn_defect_save = (Button) rootView.findViewById(R.id.btn_defect_save);
            btn_done_save = (Button) rootView.findViewById(R.id.btn_done_save);
            btn_respond_save = (Button) rootView.findViewById(R.id.btn_respond_save);

            btn_ppe = (Button) rootView.findViewById(R.id.btn_ppe);
            btn_feedback = (Button) rootView.findViewById(R.id.btn_feedback);
            btn_material_reqd = (Button) rootView.findViewById(R.id.btn_material_reqd);
            btn_material_used = (Button) rootView.findViewById(R.id.btn_material_used);
            setupActionBar();

            setRecyclerViewForIMG();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setRecyclerViewForIMG(){
        // Create the recyclerview.
        recyclerviewImage1 = (RecyclerView)rootView.findViewById(R.id.custom_refresh_recycler_view);
        recyclerviewImage2 = (RecyclerView)rootView.findViewById(R.id.custom_refresh_recycler_view2);
        // Create the grid layout manager with 2 columns.
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        GridLayoutManager layoutManager2 = new GridLayoutManager(getContext(),1);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Set layout manager.
        recyclerviewImage1.setLayoutManager(layoutManager);
        recyclerviewImage2.setLayoutManager(layoutManager2);

        itemList1 = new ArrayList<CustomRecyclerViewItem>();
        itemList2 = new ArrayList<CustomRecyclerViewItem>();
        // Create car recycler view data adapter with car item list.
        customRecyclerViewDataAdapter1 = new CustomRecyclerViewDataAdapter(itemList1,1,this,getContext(),true,DefectFound);
        customRecyclerViewDataAdapter2 = new CustomRecyclerViewDataAdapter(itemList2,1,this,getContext(),true,WorkDone);

        // Set data adapter.
        recyclerviewImage1.setAdapter(customRecyclerViewDataAdapter1);
        recyclerviewImage2.setAdapter(customRecyclerViewDataAdapter2);

        // Scroll RecyclerView a little to make later scroll take effect.
        recyclerviewImage1.scrollToPosition(1);
        recyclerviewImage2.scrollToPosition(1);

    }

    public void setupActionBar() {
        try {
            mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
            // mToolbar.setTitle(getResources().getString(R.string.lbl_receive_complaints));
            tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
            tv_toolbar_title.setText(getString(R.string.lbl_receive_complaints));
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
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setProperties() {
        try {
            Log.d(TAG, "setProperties");
            AppUtils.closeInput(cl_main);
            // ll_materials_required.setVisibility(View.GONE);
            // ll_pending_reason.setVisibility(View.GONE);

            tv_lbl_complaint.setTypeface(font.getHelveticaRegular());
            tv_complaint.setTypeface(font.getHelveticaRegular());
            if (mComplaint != null) tv_complaint.setText(mComplaint);

            tv_lbl_defectsfound.setText(
                    Html.fromHtml(getString(R.string.lbl_defectsfound) + AppUtils.mandatory));
            tv_lbl_workdone.setText(Html.fromHtml(getString(R.string.lbl_workdone) + AppUtils.mandatory));
            tv_lbl_workstatus.setText(
                    Html.fromHtml(getString(R.string.lbl_workstatus) + AppUtils.mandatory));
            tv_lbl_reason.setText(Html.fromHtml(getString(R.string.lbl_reason) + AppUtils.mandatory));
            tv_lbl_tentative_date.setText(
                    Html.fromHtml(getString(R.string.tentative_date) + AppUtils.mandatory));

            tv_lbl_defectsfound.setTypeface(font.getHelveticaRegular());
            tv_lbl_workdone.setTypeface(font.getHelveticaRegular());
            tv_lbl_workstatus.setTypeface(font.getHelveticaRegular());
            tv_lbl_reason.setTypeface(font.getHelveticaRegular());
            tv_lbl_tentative_date.setTypeface(font.getHelveticaRegular());

            tv_select_reason.setTypeface(font.getHelveticaRegular());
            tv_select_workstatus.setTypeface(font.getHelveticaRegular());
            tv_select_defectsfound.setTypeface(font.getHelveticaRegular());
            tv_select_workdone.setTypeface(font.getHelveticaRegular());
            tv_select_tentative_date.setTypeface(font.getHelveticaRegular());

            tie_defectsfound.setTypeface(font.getHelveticaRegular());
            tie_workdone.setTypeface(font.getHelveticaRegular());

            btn_defect_save.setTypeface(font.getHelveticaRegular());
            btn_done_save.setTypeface(font.getHelveticaRegular());
            btn_respond_save.setTypeface(font.getHelveticaRegular());

            btn_ppe.setTypeface(font.getHelveticaRegular());
            btn_feedback.setTypeface(font.getHelveticaRegular());
            btn_material_reqd.setTypeface(font.getHelveticaRegular());
            btn_material_used.setTypeface(font.getHelveticaRegular());

            btn_respond_save.setOnClickListener(_OnClickListener);

            btn_ppe.setOnClickListener(_OnClickListener);
            btn_feedback.setOnClickListener(_OnClickListener);
            btn_material_reqd.setOnClickListener(_OnClickListener);
            btn_material_used.setOnClickListener(_OnClickListener);

            tv_select_workstatus.setOnClickListener(_OnClickListener);
            tv_select_reason.setOnClickListener(_OnClickListener);
            tv_select_workdone.setOnClickListener(_OnClickListener);
            tv_select_defectsfound.setOnClickListener(_OnClickListener);
            tv_select_tentative_date.setOnClickListener(_OnClickListener);

            // image picker and save options
            iv_defectfound.setOnClickListener(_OnClickListener);
            iv_workdone.setOnClickListener(_OnClickListener);
            btn_defect_save.setOnClickListener(_OnClickListener);
            btn_done_save.setOnClickListener(_OnClickListener);

            tie_defectsfound.addTextChangedListener(
                    new Fragment_RC_Respond.MyTextWatcher(tie_defectsfound, til_defectsfound));
            tie_workdone.addTextChangedListener(
                    new Fragment_RC_Respond.MyTextWatcher(tie_workdone, til_workdone));
            tie_workpending.addTextChangedListener(
                    new Fragment_RC_Respond.MyTextWatcher(tie_workpending, til_workpending));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPropertiesValue() {
        Log.d(TAG, "setPropertiesValue mComplaintStatus" + mComplaintStatus);
        try {
            AppUtils.closeInput(cl_main);
            if (mComplaintStatus != null) {
                if (mComplaintStatus.equals(AppUtils.COMPLETED)) {
                    Log.d(TAG, "mComplaintStatus" + mComplaintStatus);
                    tv_select_workstatus.setOnClickListener(null);
                    tv_select_reason.setOnClickListener(null);
                    tv_select_workdone.setOnClickListener(null);
                    tv_select_defectsfound.setOnClickListener(null);
                    tv_select_tentative_date.setOnClickListener(null);

                    btn_defect_save.setVisibility(View.GONE);
                    btn_done_save.setVisibility(View.GONE);
                }

                btn_respond_save.setText(
                        getString(
                                mComplaintStatus.equals(AppUtils.COMPLETED)
                                        ? R.string.lbl_next
                                        : R.string.lbl_save));

                if (mSavedInstanceState != null) {
                    Log.d(TAG, mWorkStatusCode + " mSavedInstanceState : " + mSavedInstanceState);
                    mReceiveComplaintView =
                            mSavedInstanceState.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);
                    mSelectedPosition =
                            mSavedInstanceState.getInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION);
                    mWorkStatusCode = mReceiveComplaintView.getWorkStatus();



                    if(mSavedInstanceState.getParcelableArrayList(AppUtils.ARGS_IMAGE_LIST_DFECT_FOUND)!=null) {
                        itemList1=new ArrayList<CustomRecyclerViewItem>();
                        itemList1=(mSavedInstanceState.getParcelableArrayList(AppUtils.ARGS_IMAGE_LIST_DFECT_FOUND));
                        img_upload_count1.setVisibility(View.VISIBLE);
                        img_upload_count1.setText((itemList1.size()) + "/" + imageTotalCount + " Image uploaded - Defect Found");
                        // load recyclerview from cache
                        customRecyclerViewDataAdapter1 = new CustomRecyclerViewDataAdapter(itemList1, imageTotalCount, this, getContext(), false,DefectFound);
                        recyclerviewImage1.setAdapter(customRecyclerViewDataAdapter1);
                    }

                    if(mSavedInstanceState.getParcelableArrayList(AppUtils.ARGS_IMAGE_LIST_WORK_DONE)!=null) {
                        itemList2=new ArrayList<CustomRecyclerViewItem>();
                        itemList2=(mSavedInstanceState.getParcelableArrayList(AppUtils.ARGS_IMAGE_LIST_WORK_DONE));
                        img_upload_count2.setVisibility(View.VISIBLE);
                        img_upload_count2.setText((itemList2.size()) + "/" + imageTotalCount + " Image uploaded - Work Done - Work Done");
                        // load recyclerview from cache
                        customRecyclerViewDataAdapter2 = new CustomRecyclerViewDataAdapter(itemList2, imageTotalCount, this, getContext(), false,WorkDone);
                        recyclerviewImage2.setAdapter(customRecyclerViewDataAdapter2);
                    }

                }
                Log.d(
                        TAG,
                        mWorkStatusCode
                                + " mSavedInstanceState :"
                                + mReceiveComplaintView.getOtherDefects()
                                + " d "
                                + mReceiveComplaintView.getOtherPendingRemarks()
                                + " d "
                                + mReceiveComplaintView.getOtherWorkDone());
                try {
                    tie_defectsfound.setText(mReceiveComplaintView.getOtherDefects());
                    tie_workpending.setText(mReceiveComplaintView.getOtherPendingRemarks());
                    tie_workdone.setText(mReceiveComplaintView.getOtherWorkDone());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mReceiveComplaintView.getTentativeDate() != null
                        && !mReceiveComplaintView.getTentativeDate().equals("null"))
                    tv_select_tentative_date.setText(
                            AppUtils.getDateTime(mReceiveComplaintView.getTentativeDate()));

                if (mSelectDefect != null) {
                    tv_select_defectsfound.setText(mSelectDefect);
                    if (mReceiveComplaintView.getOtherDefects() != null
                            && !mReceiveComplaintView.getOtherDefects().equals("null")
                            && !mReceiveComplaintView.getOtherDefects().equals("")) {
                        til_defectsfound.setVisibility(View.VISIBLE);
                        tie_defectsfound.setText(mReceiveComplaintView.getOtherDefects());
                    } /*else {
                til_defectsfound.setVisibility(View.GONE);
            }*/
                    if (mSelectDefect.equals(getString(R.string.lbl_other))) {
                        til_defectsfound.setVisibility(View.VISIBLE);
                    } else {
                        // til_defectsfound.setVisibility(View.GONE);
                    }
                }

                if (mSelectWorkDone != null) {
                    tv_select_workdone.setText(mSelectWorkDone);
                    //  mWorkDoneCode=tv_select_workdone.getText().toString();
                    if (mReceiveComplaintView.getOtherWorkDone() != null
                            && !mReceiveComplaintView.getOtherWorkDone().equals("null")
                            && !mReceiveComplaintView.getOtherWorkDone().equals("")) {
                        til_workdone.setVisibility(View.VISIBLE);
                        tie_workdone.setText(mReceiveComplaintView.getOtherWorkDone());
                    } /*else {
                til_workdone.setVisibility(View.GONE);
            }*/
                }
                if (mSelectWorkStatus != null) {
                    tv_select_workstatus.setText(mSelectWorkStatus);

                    // if (mWorkStatusCode.equals(AppUtils.PENDING)) {
                    // ll_pending_reason.setVisibility(View.VISIBLE);

                    if (mSelectPendingReason != null) {
                        tv_select_reason.setText(mSelectPendingReason);

                        if (mPendingReasonCode == null) {
                            mPendingReasonCode = mReceiveComplaintView.getPendingResponseCode();
                        }

                        if (mPendingReasonCode != null && mPendingReasonCode.equals(AppUtils.OTHERS)) {
                            til_workpending.setVisibility(View.VISIBLE);
                            if (mReceiveComplaintView.getOtherPendingRemarks() != null
                                    && !mReceiveComplaintView.getOtherPendingRemarks().equals("null")
                                    && !mReceiveComplaintView.getOtherPendingRemarks().equals("")) {
                                tie_workpending.setText(mReceiveComplaintView.getOtherPendingRemarks());
                            }
                        } else {
                            til_workpending.setVisibility(View.GONE);
                        }
                    }


                }
                if (mSelectDate != null)
                    tv_select_tentative_date.setText(mSelectDate);

                if (mUnSignedPage != AppUtils.ARGS_RECEIVECOMPLAINT_PAGE) {
                    btn_material_reqd.setVisibility(View.GONE);
                    btn_material_used.setVisibility(View.GONE);
                    btn_feedback.setVisibility(View.GONE);
                    btn_ppe.setVisibility(View.GONE);
                }

                mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
                if (mNetworkInfo != null && mNetworkInfo.length() > 0) {

                    if (mImageToBeAttachedDefectFound == null) {
                        if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                            // Download defect found image
                            RCDownloadImageRequest imageRequest = new RCDownloadImageRequest();
                            imageRequest.setOpco(mOpco);
                            imageRequest.setComplaintSite(mComplaintSite);
                            imageRequest.setComplaintNo(mComplaintNumber);
                            imageRequest.setDocType("B");
                            pb_defectfound.setVisibility(View.VISIBLE);
                            receiveComplaintRespond_service.getRespondImage(imageRequest, mContext);
                        } else {
                            new DefectDoneImageDbInitializer(
                                    mActivity, new DFoundWDoneImageEntity(mComplaintNumber + "B"), this)
                                    .execute(AppUtils.MODE_GET);
                        }
                    } else {
                        iv_defectfound.setImageBitmap(
                                ThumbnailUtils.extractThumbnail(
                                        mImageToBeAttachedDefectFound, THUMBNAIL_SIZE, THUMBNAIL_SIZE));
                    }
                    if (mImageToBeAttached == null) {
                        if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                            // Download work done image
                            RCDownloadImageRequest doneimageRequest = new RCDownloadImageRequest();
                            doneimageRequest.setOpco(mOpco);
                            doneimageRequest.setComplaintSite(mComplaintSite);
                            doneimageRequest.setComplaintNo(mComplaintNumber);
                            doneimageRequest.setDocType("A");
                            pb_workdone.setVisibility(View.VISIBLE);
                            receiveComplaintRespond_service.getRespondImage(doneimageRequest, mContext);
                        } else {
                            new DefectDoneImageDbInitializer(
                                    mActivity, new DFoundWDoneImageEntity(mComplaintNumber + "A"), this)
                                    .execute(AppUtils.MODE_GET);
                        }
                    } else {
                        iv_workdone.setImageBitmap(
                                ThumbnailUtils.extractThumbnail(
                                        mImageToBeAttached, THUMBNAIL_SIZE, THUMBNAIL_SIZE));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageClick(int pos,String base64,String type){

        imgType=type;

        if(type.equalsIgnoreCase(DefectFound)){
            imageCount1=pos;
            displayAttachImageDialog(pos,base64);
        }
        else{
            imageCount2=pos;
            displayAttachImageDialog(pos,base64);

        }


    }

    private void getParcelableData() {
        Log.d(TAG, "getParcelableData");
        if (mArgs != null && mArgs.size() > 0) {
            try {
                mSavedInstanceState = null;
                mSelectedPosition = mArgs.getInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION);
                mReceiveComplaintView = mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);
                mWorkCategory = mReceiveComplaintView.getWorkCategory();
                mOpco = mReceiveComplaintView.getOpco();
                mComplaintStatus = mReceiveComplaintView.getComplaintStatus();
                mComplaintCode = mReceiveComplaintView.getComplaintCode();
                mComplaintNumber = mReceiveComplaintView.getComplaintNumber();
                mComplaint = mReceiveComplaintView.getComplaintDetails();
                mComplaintSite = mReceiveComplaintView.getComplaintSite();
                mUnSignedPage = mArgs.getString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE);
                getPopupData();
                Log.d(TAG, " category ::" + mReceiveComplaintView.toString() + " :comp code : " + mComplaintCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } }


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

                    DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity(mComplaintNumber + docType);
                    imageEntity.setOpco(mOpco);
                    imageEntity.setComplaintSite(mComplaintSite);
                    imageEntity.setComplaintNo(mComplaintNumber);
                    imageEntity.setFileType("jpg");
                    imageEntity.setTransactionType("R");
                    imageEntity.setDocType(docType);
                    imageEntity.setCreatedBy(mStrEmpId);
                    imageEntity.setModifiedBy(mStrEmpId);

                    if(docType.equalsIgnoreCase("B")){
                        imageEntity.setImageCount(imageCount1);
                    }
                    else{
                        imageEntity.setImageCount(imageCount2);
                    }

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




    private void gotoPPEPage() {
        mSavedInstanceState = getSavedState();
        try {
            if (mArgs != null && mArgs.size() > 0) {
                Fragment fragment = new Fragment_RM_PPE();
                fragment.setArguments(getSavedState());
                FragmentTransaction fragmentTransaction = mManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_PPE);
                fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_PPE);
                fragmentTransaction.commit();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } }

    private void gotoFeedBackPage() {
        mSavedInstanceState = getSavedState();
        try {
            if (mArgs != null && mArgs.size() > 0) {
                Fragment fragment = new Fragment_RC_Feedback();
                fragment.setArguments(getSavedState());
                FragmentTransaction fragmentTransaction = mManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_FEEDBACK);
                fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_FEEDBACK);
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } }

    private void gotoMaterialRequired() {
        mSavedInstanceState = getSavedState();
        try {
            if (mArgs != null && mArgs.size() > 0) {
                Bundle data = new Bundle();
                data.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, mReceiveComplaintView);
                data.putString(AppUtils.ARGS_MATERIAL_PAGE_STRING, AppUtils.ARGS_MATERIAL_REQUIRED_STRING);
                Fragment fragment = new Fragment_RM_Material();
                fragment.setArguments(data);
                FragmentTransaction fragmentTransaction = mManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(
                        R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_MATERIALREQUIRED);
                fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_MATERIALREQUIRED);
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } }

    private void gotoMaterialUsed() {
        mSavedInstanceState = getSavedState();
        try {
            if (mArgs != null && mArgs.size() > 0) {
                Bundle data = new Bundle();
                data.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, mReceiveComplaintView);
                data.putString(AppUtils.ARGS_MATERIAL_PAGE_STRING, AppUtils.ARGS_MATERIAL_USED_STRING);
                Fragment fragment = new Fragment_RM_Material();
                fragment.setArguments(data);
                FragmentTransaction fragmentTransaction = mManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(
                        R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_MATERIALREQUIRED);
                fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_MATERIALREQUIRED);
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoFragment_Rate_And_Share() {
        mSavedInstanceState = getSavedState();
        Log.d(TAG, "gotoFragment_Rate_And_Share");
        try {
            if (mComplaintStatus.equals("C")) {
                mSavedInstanceState = getSavedState();
                Bundle mdata = new Bundle();
                mdata.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, mUnSignedPage);
                mdata.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, mReceiveComplaintView);
                Fragment_RM_RateService fragment = new Fragment_RM_RateService();
                fragment.setArguments(mdata);
                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(
                        R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_RATE__AND_FEEDBACK);
                fragmentTransaction.addToBackStack(TAG_RATE_AND_SHARE);
                fragmentTransaction.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void submitForm() {
        Log.d(TAG, "submitForm");
        try {
            AppUtils.closeInput(cl_main);

            if (mComplaintStatus.equals("C")) {
                gotoFragment_Rate_And_Share();
                return;
            }

            String msgErr = "";
            ReceiveComplaintViewEntity request = mReceiveComplaintView;

            request.setModifiedBy(mStrEmpId);

            if (mDefectCode != null) {
                AppUtils.setErrorBg(tv_select_defectsfound, false);
                request.setDefect(mDefectCode);
                if (tv_select_defectsfound.getText().toString().equals("Others")) {
                    if (tie_defectsfound.getText().toString().trim().isEmpty()) {
                        til_defectsfound.setError(getString(R.string.msg_empty));
                        requestFocus(tie_defectsfound);
                        msgErr = msgErr + "tv_select_defectsfound";
                    } else {
                        til_defectsfound.setErrorEnabled(false);
                        request.setOtherDefects(tie_defectsfound.getText().toString());
                    }
                } else {
                    request.setOtherDefects(tie_defectsfound.getText().toString());
                }
                // request.setOtherDefects(null);
            } else {
                AppUtils.setErrorBg(tv_select_defectsfound, true);
                msgErr = msgErr + "mDefectCode";
            }

            if (mWorkStatusCode != null) {
                AppUtils.setErrorBg(tv_select_workstatus, false);
                request.setWorkStatus(mWorkStatusCode);

                if (mPendingReasonCode != null) {
                    AppUtils.setErrorBg(tv_select_reason, false);
                    request.setPendingResponseCode(mPendingReasonCode);
                    if (tv_select_reason.getText().toString().equals("Others")) {
                        if (tie_workpending.getText().toString().trim().isEmpty()) {
                            til_workpending.setError(getString(R.string.msg_empty));
                            requestFocus(tie_workpending);
                            msgErr = msgErr + "tv_select_reason";
                        } else {
                            til_workpending.setErrorEnabled(false);
                            request.setOtherPendingRemarks(tie_workpending.getText().toString());
                        }
                    } else request.setOtherPendingRemarks(null);
                } else {
                    if (!mWorkStatusCode.equalsIgnoreCase(AppUtils.COMPLETED)) {
                        AppUtils.setErrorBg(tv_select_reason, true);
                        msgErr = msgErr + "mWorkStatusCode";
                    } else {
                        msgErr = "";
                    }
                }

                if (mWorkDoneCode != null) {
                    AppUtils.setErrorBg(tv_select_workdone, false);
                    request.setWorkDone(mWorkDoneCode);
                    if (tv_select_workdone.getText().toString().equals("Others")) {
                        if (tie_workdone.getText().toString().trim().isEmpty() &&  tv_select_workstatus.getText().toString().equalsIgnoreCase("Completed") ) {


                            til_workdone.setError(getString(R.string.msg_empty));
                            requestFocus(tie_workdone);
                            msgErr = msgErr + "tv_select_workdone";


                        } else {
                            til_workdone.setErrorEnabled(false);
                            request.setOtherWorkDone(tie_workdone.getText().toString());
                        }
                    } else {
                        request.setOtherWorkDone(tie_workdone.getText().toString());
                    }
                    // else request.setOtherWorkDone(null);
                } else {

                    if(tv_select_workstatus.getText().toString().equalsIgnoreCase("Completed")){
                        AppUtils.setErrorBg(tv_select_workdone, true);
                        msgErr = msgErr + "mWorkDoneCode";
                    }
                }

                if (mWorkStatusCode.equals(AppUtils.COMPLETED)) {
                    request.setComplaintStatus(mWorkStatusCode);
                    request.setConfirm(confirm);
                    request.setTentativeDate(
                            new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date()));
                } else {
                    request.setComplaintStatus(mComplaintStatus);
                    request.setConfirm(confirm);
                    if (TextUtils.isEmpty(tv_select_tentative_date.getText().toString()) &&
                            tv_select_workstatus.getText().toString().equalsIgnoreCase("Completed")) {
                        AppUtils.setErrorBg(tv_select_tentative_date, true);
                        msgErr = msgErr + "tv_select_tentative_date";
                    } else {
                        AppUtils.setErrorBg(tv_select_tentative_date, false);
                        request.setTentativeDate(tv_select_tentative_date.getText().toString());
                    }
                }
            } else {

                AppUtils.setErrorBg(tv_select_workstatus, true);
                msgErr = msgErr + "mWorkStatusCode";

            }

            if (msgErr != "") {
                //   AppUtils.showDialog(mActivity, "Please select values in Mandatory field");
                AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Please select values in Mandatory field");

                return;
            }

            requestToServer=new ReceiveComplaintViewEntity();

            requestToServer=request;

            ppeService.fetchPPEData(mComplaintNumber);

            Log.d(TAG, msgErr);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mActivity
                    .getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onDateReceivedSuccess(String strDate) {
        mSelectDate = strDate;
        tv_select_tentative_date.setText(strDate);
        tv_select_tentative_date.setTypeface(font.getHelveticaBold());
        AppUtils.setErrorBg(tv_select_tentative_date, false);
    }

    public void getDefectsFound() {
        Log.d(TAG, "getDefectsFound");
        try {
            ArrayList strArrayDefects = new ArrayList();
            for (WorkDefectEntity entity : mWorkDefectsEntityList) {
                strArrayDefects.add(entity.getDefectDescription());
            }

            strArrayDefects.add("\n\n");

            FilterableListDialog.create(
                    mActivity,
                    getString(R.string.lbl_select_defectsfound),
                    strArrayDefects,
                    new FilterableListDialog.OnListItemSelectedListener() {
                        @Override
                        public void onItemSelected(String text) {

                            if(!text.equals("\n\n")){
                                mSelectDefect = text.toString();
                                for (WorkDefectEntity item : mWorkDefectsEntityList) {
                                    if (mSelectDefect.equals(item.getDefectDescription())) {
                                        mDefectCode = item.getDefectCode();
                                        mWorkDoneEntityList.clear();
                                        mWorkDoneCode = null;
                                        mSelectWorkDone = null;
                                        tv_select_workdone.setText("");
                                        tv_select_workdone.setHint(getString(R.string.lbl_select_workdone));
                                        gettingWorkDone(mDefectCode);
                                    }
                                }
                                tv_select_defectsfound.setText(mSelectDefect);
                                tv_select_defectsfound.setTypeface(font.getHelveticaBold());
                                if (mSelectDefect.equals(getString(R.string.lbl_other))) {
                                    til_defectsfound.setVisibility(View.VISIBLE);
                                } else {
                                    // til_defectsfound.setVisibility(View.GONE);
                                }
                                AppUtils.setErrorBg(tv_select_defectsfound, false);
                            }


                        }
                    })
                    .show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getWorkDone() {
        Log.d(TAG, "getWorkDone");
        try {
            ArrayList strArrayWorkDone = new ArrayList();
            for (WorkDoneEntity entity : mWorkDoneEntityList) {
                strArrayWorkDone.add(entity.getWorkDoneDescription());
            }
            strArrayWorkDone.add("\n\n");

            FilterableListDialog.create(
                    mActivity,
                    getString(R.string.lbl_select_workdone),
                    strArrayWorkDone,
                    new FilterableListDialog.OnListItemSelectedListener() {
                        @Override
                        public void onItemSelected(String text) {

                            if(!text.equals("\n\n")){
                                mSelectWorkDone = text.toString();
                                tv_select_workdone.setText(mSelectWorkDone);
                                tv_select_workdone.setTypeface(font.getHelveticaBold());

                                for (WorkDoneEntity item : mWorkDoneEntityList) {
                                    if (mSelectWorkDone.equals(item.getWorkDoneDescription())) {
                                        mWorkDoneCode = item.getWorkDoneCode();
                                    }
                                }

                                if (mSelectWorkDone.equals(getString(R.string.lbl_other))) {
                                    til_workdone.setVisibility(View.VISIBLE);
                                } else {
                                    // til_workdone.setVisibility(View.GONE);
                                }
                                AppUtils.setErrorBg(tv_select_workdone, false);
                            }
                        }
                    })
                    .show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getReason() {
        Log.d(TAG, "getReason");
        try {
            ArrayList strArrayWorkPending = new ArrayList();
            for (WorkPendingReasonEntity entity : mWorkPendingReasonList) {
                strArrayWorkPending.add(entity.getReasonDescription());
            }

            strArrayWorkPending.add("\n\n");

            FilterableListDialog.create(
                    mActivity,
                    getString(R.string.lbl_select_reason),
                    strArrayWorkPending,
                    new FilterableListDialog.OnListItemSelectedListener() {
                        @Override
                        public void onItemSelected(String text) {

                            if(!text.equals("\n\n")){
                                mSelectPendingReason = text.toString();
                                tv_select_reason.setText(mSelectPendingReason);
                                tv_select_reason.setTypeface(font.getHelveticaBold());

                                for (WorkPendingReasonEntity item : mWorkPendingReasonList) {
                                    if (mSelectPendingReason.equals(item.getReasonDescription()))
                                        mPendingReasonCode = item.getReasonCode();
                                }


                                if (mPendingReasonCode.equals(AppUtils.OTHERS)) {
                                    til_workpending.setVisibility(View.VISIBLE);
                                } else {
                                    til_workpending.setVisibility(View.GONE);
                                }
                                AppUtils.setErrorBg(tv_select_reason, false);
                            }
                        }
                    })
                    .show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getWorkStatus() {
        Log.d(TAG, "getWorkStatus");
        try {
            if (mWorkStatusEntityList.size() > 0) {
                int count = mWorkStatusEntityList.size();
                String[] strArray = new String[count];
                for (int i = 0; i < count; i++) {
                    strArray[i] = mWorkStatusEntityList.get(i).getDescription();
                }
                new MaterialDialog.Builder(mActivity)
                        .title(R.string.lbl_select_workstatus)
                        .items(strArray)
                        .itemsCallbackSingleChoice(
                                -1,
                                new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(
                                            MaterialDialog dialog, View view, int which, CharSequence text) {
                                        if (which >= 0) {
                                            mSelectWorkStatus = text.toString();
                                            Log.d(TAG, "text ..." + mSelectWorkStatus);
                                            tv_select_workstatus.setText(mSelectWorkStatus);
                                            tv_select_workstatus.setTypeface(font.getHelveticaBold());
                                            // mReceiveComplaintVi
                                            mReceiveComplaintView.setWorkStatus(mSelectWorkStatus);

                                            mEditor = mPreferences.edit();
                                            mEditor.putString(AppUtils.WORK_STATUS, mSelectWorkStatus);
                                            mEditor.commit();
                                            for (WorkStatusEntity item : mWorkStatusEntityList) {
                                                if (mSelectWorkStatus.equals(item.getDescription())) {
                                                    mWorkStatusCode = item.getPendingCode();
                                                }
                                            }
                                            if (mWorkStatusCode.equals(AppUtils.COMPLETED)) {
                                                ll_pending_reason.setVisibility(View.GONE);
                                            } else {
                                                ll_pending_reason.setVisibility(View.VISIBLE);
                                            }

                                            AppUtils.setErrorBg(tv_select_workstatus, false);

                                        } else {
                                            AppUtils.setErrorBg(tv_select_workstatus, true);
                                            mWorkStatusCode = null;
                                            tv_select_workstatus.setText("");
                                            tv_select_workstatus.setHint(getString(R.string.lbl_select_workstatus));
                                            AppUtils.showDialog(
                                                    mActivity, getString(R.string.no_value_has_been_selected));
                                        }
                                        AppUtils.closeInput(cl_main);
                                        return true;
                                    }
                                })
                        .positiveText(R.string.lbl_done)
                        .negativeText(R.string.lbl_close)
                        .show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(TAG, "onPrepareOptionsMenu ");
        try {
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_home).setVisible(true);
            AppUtils.closeInput(cl_main);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void onWorkDefectReceived(List<WorkDefectEntity> workDefectEntityList, int mode) {
        Log.d(TAG, "onWorkDefectReceived");
        try {
            AppUtils.hideProgressDialog();
            mWorkDefectsEntityList = workDefectEntityList;
            if (mWorkDefectsEntityList != null) {
                if (mReceiveComplaintView.getDefect() != null) {
                    for (WorkDefectEntity item : mWorkDefectsEntityList) {
                        if (mReceiveComplaintView.getDefect().equals(item.getDefectCode())) {
                            mDefectCode = item.getDefectCode();
                            mSelectDefect = item.getDefectDescription();
                            tv_select_defectsfound.setText(item.getDefectDescription());
                            if (mSelectDefect.equals(getString(R.string.lbl_other))) {
                                til_defectsfound.setVisibility(View.VISIBLE);
                            } else {
                                // til_defectsfound.setVisibility(View.GONE);
                            }
                            if (mDefectCode != null) gettingWorkDone(mDefectCode);
                        } else {

                        }
                    }
                }

                if (mode == AppUtils.MODE_SERVER)
                    new WorkDefectsDbInitializer(mActivity, this, mWorkDefectsEntityList)
                            .execute(AppUtils.MODE_INSERT_SINGLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWorkDoneReceivedSuccess(List<WorkDoneEntity> workDoneEntities, int mode) {
        Log.d(TAG, "onWorkDoneReceivedSuccess");
        AppUtils.hideProgressDialog();
        mWorkDoneEntityList = workDoneEntities;
        if (mode == AppUtils.MODE_SERVER) {
            new WorkDoneDbInitializer(mActivity, this, workDoneEntities)
                    .execute(AppUtils.MODE_INSERT_SINGLE);
        }
        if (mWorkDoneEntityList != null) {
            if (mReceiveComplaintView.getWorkDone() != null) {
                for (WorkDoneEntity item : mWorkDoneEntityList) {
                    if (mReceiveComplaintView.getWorkDone().equals(item.getWorkDoneCode())) {
                        mSelectWorkDone = item.getWorkDoneDescription();
                        tv_select_workdone.setText(item.getWorkDoneDescription());
                        mWorkDoneCode=item.getWorkDoneCode();
                        if (mSelectWorkDone.equals(getString(R.string.lbl_other))) {
                            til_workdone.setVisibility(View.VISIBLE);
                        } else {
                            // til_workdone.setVisibility(View.GONE);
                        }
                        return;
                    } else {
                        //mWorkDoneCode = null;
                        //mSelectWorkDone = null;
                        mSelectWorkDone = item.getWorkDoneDescription();
                        tv_select_workdone.setText(item.getWorkDoneDescription());
                        //  mWorkDoneCode=tv_select_workdone.getText().toString();
                        if (mSelectWorkDone.equals(getString(R.string.lbl_other))) {
                            til_workdone.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onWorkStatusReceivedSuccess(List<WorkStatusEntity> workStatusEntities, int mode) {
        Log.d(TAG, "onWorkStatusReceivedSuccess");
        try {
            AppUtils.hideProgressDialog();
            mWorkStatusEntityList = workStatusEntities;
            if (mWorkStatusEntityList != null) {
                if (mReceiveComplaintView.getWorkStatus() != null) {
                    for (WorkStatusEntity item : mWorkStatusEntityList) {
                        if (mReceiveComplaintView.getWorkStatus().equals(item.getPendingCode())) {
                            mWorkStatusCode = item.getPendingCode();
                            mSelectWorkStatus = item.getDescription();
                            tv_select_workstatus.setText(item.getDescription());

                            if (mWorkStatusCode.equals(AppUtils.COMPLETED)) {
                                ll_pending_reason.setVisibility(View.GONE);
                            } else {
                                ll_pending_reason.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                }
            }

            if (mode == AppUtils.MODE_SERVER) {
                mWorkStatusInitializer.populateAsync(
                        AppDatabase.getAppDatabase(mActivity), mWorkStatusEntityList, AppUtils.MODE_INSERT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplaintRespondReceivedFailure(String strErr, int mode) {
        Log.d(TAG, "onComplaintRespondReceivedFailure ::" + strErr);
        try {
            AppUtils.hideProgressDialog();
            Fragment main = mManager.findFragmentByTag(Utils.TAG_RECEIVE_COMPLAINT_RESPOND);
            // if (main != null && main.isVisible()) AppUtils.showDialog(mActivity, strErr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWorkPendingReasonReceivedSuccess(
            List<WorkPendingReasonEntity> workPendingReasonEntities, int mode) {
        Log.d(TAG, "onWorkPendingReasonReceivedSuccess ::");
        AppUtils.hideProgressDialog();
        mWorkPendingReasonList = workPendingReasonEntities;
        if (mWorkPendingReasonList != null) {
            if (mReceiveComplaintView.getPendingResponseCode() != null) {
                for (WorkPendingReasonEntity item : mWorkPendingReasonList) {
                    if (mReceiveComplaintView.getPendingResponseCode().equals(item.getReasonCode())) {
                        mPendingReasonCode = item.getReasonCode();
                        mSelectPendingReason = item.getReasonDescription();
                        tv_select_reason.setText(item.getReasonDescription());

                        if (mPendingReasonCode.equals(AppUtils.OTHERS)) {
                            til_workpending.setVisibility(View.VISIBLE);
                        } else {
                            til_workpending.setVisibility(View.GONE);
                        }

                    }
                }
            }
        }
        if (mode == AppUtils.MODE_SERVER) {
            mWorkPendingInitializer.populateAsync(
                    AppDatabase.getAppDatabase(mActivity), mWorkPendingReasonList, AppUtils.MODE_INSERT);
        }
    }

    @Override
    public void onComplaintRespondSaveReceived(final RCRespond rcRespond, int mode) {
        Log.d(TAG, "onComplaintRespondSaveReceived ");
        try {
            AppUtils.hideProgressDialog();
            if (rcRespond.getComplaintStatus().equals(AppUtils.COMPLETED)) {
                mComplaintStatus = rcRespond.getComplaintStatus();
                mReceiveComplaintView.setComplaintStatus(mComplaintStatus);
                if (adapter != null) adapter.deleteItem(mSelectedPosition);
                if (recyclerView != null) recyclerView.postInvalidate();
            }

            final MaterialDialog.Builder builder =
                    new MaterialDialog.Builder(mActivity)
                            .content(rcRespond.getComplaintDetails())
                            .stackingBehavior(StackingBehavior.ADAPTIVE)
                            .onPositive(
                                    new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(
                                                @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                            if (rcRespond.getComplaintStatus().equals(AppUtils.COMPLETED)) {
                                                if (mReceiveComplaintView != null)
                                                    if (!mPreferences.getString(AppUtils.WORK_STATUS, null).equals("")) {
                                                        if (mPreferences
                                                                .getString(AppUtils.WORK_STATUS, null)
                                                                .equalsIgnoreCase("completed")) {
                                                            if (mReceiveComplaintView.getCustomerSignStatus() != null
                                                                    && mReceiveComplaintView
                                                                    .getCustomerSignStatus()
                                                                    .equalsIgnoreCase(AppUtils.CLOSEWITHOUTSIGN)) {


                                                                FragmentManager fm=getFragmentManager();
                                                                for (int i = 0; i < fm.getBackStackEntryCount()-1; ++i) {
                                                                    fm.popBackStack();
                                                                }

                                                                Fragment_RC_List fragment = new Fragment_RC_List();
                                                                Bundle data = new Bundle();
                                                                data.putString(
                                                                        AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE,
                                                                        AppUtils.ARGS_RECEIVECOMPLAINT_PAGE);
                                                                FragmentManager fragmentManager =
                                                                        mActivity.getSupportFragmentManager();
                                                                fragment.setArguments(data);
                                                                FragmentTransaction fragmentTransactionch =
                                                                        fragmentManager.beginTransaction();
                                                                fragmentTransactionch.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                                                                fragmentTransactionch.replace(
                                                                        R.id.frame_container,
                                                                        fragment,
                                                                        TAG_RECEIVE_COMPLAINT_RATE__AND_FEEDBACK);
                                                                fragmentTransactionch.addToBackStack(TAG_REACTIVE_MAINTENANCE);
                                                                fragmentTransactionch.commit();

                                                            } else {
                                                                gotoFragment_Rate_And_Share();
                                                            }
                                                        }
                                                    }
                                            } else if (mWorkStatusCode.equals(AppUtils.COMPLETED)) {
                                                confirm = true;
                                                submitForm();
                                            } else if (!mWorkStatusCode.equals(AppUtils.COMPLETED)
                                                    && !rcRespond.isConfirm()) {
                                                confirm = true;
                                                submitForm();
                                            }
                                            else{
                                                getChildFragmentManager().popBackStack();
                                            }
                                        }
                                    });

            if (rcRespond.getComplaintStatus().equals(AppUtils.COMPLETED)) {
                builder.positiveText(R.string.lbl_okay);
            } else if (mWorkStatusCode.equals(AppUtils.COMPLETED)) {
                builder.negativeText(R.string.lbl_no);
                builder.positiveText(R.string.lbl_yes);
            } else if ((rcRespond.getComplaintStatus().equals(AppUtils.RESPONDED)
                    && rcRespond.isConfirm())) {
                builder.positiveText(R.string.lbl_okay);

                builder.onPositive(
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(
                                    @NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                FragmentManager fm=getFragmentManager();
                                for (int i = 0; i < fm.getBackStackEntryCount()-2; ++i) {
                                    fm.popBackStack();
                                } }
                        });
            } else if (!mWorkStatusCode.equals(AppUtils.COMPLETED)) {
                builder.negativeText(R.string.lbl_no);
                builder.positiveText(R.string.lbl_yes);
            }

            MaterialDialog dialog = builder.build();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gettingWorkPending() {
        mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
        if (mNetworkInfo != null && mNetworkInfo.length() > 0) {
            if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                receiveComplaintRespond_service.getWorkPendingReasonData();
            } else
                mWorkPendingInitializer.populateAsync(
                        AppDatabase.getAppDatabase(mActivity), AppUtils.MODE_GETALL);
        }
    }

    private void gettingWorkDone(String mDefectCode) {
        try {
            mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
            if (mNetworkInfo != null && mNetworkInfo.length() > 0) {
                WorkDoneRequest request = new WorkDoneRequest(mComplaintCode, mDefectCode, mWorkCategory);
                if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                    // AppUtils.showProgressDialog(mActivity, getString(R.string.fetching_work_done_details),
                    // false);
                    receiveComplaintRespond_service.getWorkDoneData(request);
                } else {
                    new WorkDoneDbInitializer(mActivity, this, request).execute(AppUtils.MODE_GET);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gettingWorkDefects() {
        mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
        if (mNetworkInfo != null && mNetworkInfo.length() > 0) {
            WorkDefectRequest request = new WorkDefectRequest(mComplaintCode, mWorkCategory);
            if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                receiveComplaintRespond_service.getWorkDefectData(request);
            } else
                new WorkDefectsDbInitializer(mActivity, this, request).execute(AppUtils.MODE_GET);
        }
    }

    private void gettingWorkStatus() {
        mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
        if (mNetworkInfo != null && mNetworkInfo.length() > 0) {
            if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                receiveComplaintRespond_service.getWorkStatusData();
            } else
                mWorkStatusInitializer.populateAsync(
                        AppDatabase.getAppDatabase(mActivity), AppUtils.MODE_GETALL);
        }
    }

    private void postDataToServer(ReceiveComplaintViewEntity saveRequest) {
        Log.d(TAG, "postDataToServer");

        mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
        if (mNetworkInfo.length() > 0) {
            if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                receiveComplaintRespond_service.getComplaintRespondSaveData(saveRequest);
            } else {
                saveRequest.setMode(AppUtils.MODE_LOCAL);
                new ReceiveComplaintViewDbInitializer(mActivity, null, saveRequest)
                        .execute(AppUtils.MODE_INSERT_SINGLE);
                showPopupMsg("Saved successfully");
            }
        }
    }

    private void showPopupMsg(String strMsg) {
        try {
            AppUtils.hideProgressDialog();
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
                                            if (mReceiveComplaintView.getComplaintStatus().equals(AppUtils.COMPLETED)) {
                                                if (mReceiveComplaintView
                                                        .getCustomerSignStatus()
                                                        .equalsIgnoreCase(AppUtils.CLOSEWITHSIGN)) {
                                                    Bundle mdata = new Bundle();
                                                    mdata.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, mUnSignedPage);
                                                    mdata.putParcelable(
                                                            AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, mReceiveComplaintView);
                                                    Fragment_RM_RateService fragment = new Fragment_RM_RateService();
                                                    fragment.setArguments(mdata);
                                                    FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                                                    FragmentTransaction fragmentTransaction =
                                                            fragmentManager.beginTransaction();
                                                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                                                    fragmentTransaction.replace(
                                                            R.id.frame_container,
                                                            fragment,
                                                            TAG_RECEIVE_COMPLAINT_RATE__AND_FEEDBACK);
                                                    fragmentTransaction.addToBackStack(TAG_RATE_AND_SHARE);
                                                    fragmentTransaction.commit();
                                                }
                                            }
                                        }
                                    });

            MaterialDialog dialog = builder.build();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postImageToServer(DFoundWDoneImageEntity saveRequest) {
        Log.d(TAG, "postImageToServer");
        mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
        if (mNetworkInfo.length() > 0) {
            if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                receiveComplaintRespond_service.saveComplaintRespondImageData1(saveRequest, mContext);
            } else {
                new DefectDoneImageDbInitializer(mActivity, saveRequest, this)
                        .execute(AppUtils.MODE_INSERT);
            }
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
            mReceiveComplaintView.setOtherDefects(tie_defectsfound.getText().toString());
            mReceiveComplaintView.setOtherWorkDone(tie_workdone.getText().toString());
            mReceiveComplaintView.setWorkStatus(mWorkStatusCode);
            mReceiveComplaintView.setOtherPendingRemarks(tie_workpending.getText().toString());
            outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, mReceiveComplaintView);
            outState.putInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION, mSelectedPosition);


            ArrayList<CustomRecyclerViewItem> tt1=itemList1;
            tt1.removeAll(Collections.singleton(null));
            outState.putParcelableArrayList(AppUtils.ARGS_IMAGE_LIST_DFECT_FOUND,tt1);

            ArrayList<CustomRecyclerViewItem> tt2=itemList2;
            tt2.removeAll(Collections.singleton(null));
            outState.putParcelableArrayList(AppUtils.ARGS_IMAGE_LIST_WORK_DONE,tt2);


            Log.d(TAG, "getSavedState");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outState;
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
                                }

                                break;

                            case 3:
                                dialog.dismiss();
                                showNoImageAlert(count);

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
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);
        image.requestLayout();
        dialog.setContentView(layout);
        dialog.show();
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

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true;
                //  displayAttachImageDialog();
            } else {

            }

        } else if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true;
                // displayAttachImageDialog();
            } else {

            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.d(TAG, "onActivityResult");
        if (resultCode != Activity.RESULT_OK) {
            return;
        }


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

                // Delete the temporary image file
                file.delete();
                if(imgType.equalsIgnoreCase(DefectFound)){
                    submitImage("B");
                }
                else{
                    submitImage("A");
                }
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

    private void dispatchChoosePhotoIntent() {
        ImagePicker fragment = new ImagePicker();
        fragment.SetImagePickListener(this);
        FragmentTransaction ObjTransaction = mManager.beginTransaction();
        ObjTransaction.add(android.R.id.content, fragment, AppUtils.SHARED_DIALOG_PICKER);
        ObjTransaction.addToBackStack(AppUtils.SHARED_DIALOG_PICKER);
        ObjTransaction.commit();
    }

    private void noImageAvailabe(int count,String base64) {
        AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_no_image_uploading), false);
        if (imgType.equalsIgnoreCase(DefectFound)) {
            DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity(mComplaintNumber + "B");
            imageEntity.setOpco(mOpco);
            imageEntity.setComplaintSite(mComplaintSite);
            imageEntity.setComplaintNo(mComplaintNumber);
            imageEntity.setFileType("jpg");
            imageEntity.setTransactionType("R");
            imageEntity.setDocType("B");
            imageEntity.setCreatedBy(mStrEmpId);
            imageEntity.setModifiedBy(mStrEmpId);
            imageEntity.setImageCount(count);
            imageEntity.setBase64Image(base64);
            postImageToServer(imageEntity);

        } else  {
            DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity(mComplaintNumber + "A");
            imageEntity.setOpco(mOpco);
            imageEntity.setComplaintSite(mComplaintSite);
            imageEntity.setComplaintNo(mComplaintNumber);
            imageEntity.setFileType("jpg");
            imageEntity.setTransactionType("R");
            imageEntity.setDocType("A");
            imageEntity.setCreatedBy(mStrEmpId);
            imageEntity.setModifiedBy(mStrEmpId);
            imageEntity.setImageCount(count);
            imageEntity.setBase64Image(base64);
            postImageToServer(imageEntity);

        }
    }


    @Override
    public void onSingleImagePicked(String Str_Path) {
        try {
            //  AppUtils.showProgressDialog(mActivity, "Processing image", false);
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


                                mImageToBeAttachedDefectFound = loadedImage;
                                iv_defectfound.setImageBitmap(
                                        ThumbnailUtils.extractThumbnail(
                                                loadedImage, THUMBNAIL_SIZE, THUMBNAIL_SIZE));

                                if(imgType.equalsIgnoreCase(DefectFound)){
                                    submitImage("B");
                                }
                                else{
                                    submitImage("A");
                                }

                            }
                            AppUtils.closeInput(cl_main);
                            //  AppUtils.hideProgressDialog();
                        }
                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void setAdapter(ReceivecomplaintListAdapter adapter) {
        this.adapter = adapter;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onImageSaveReceivedSuccess(DefectDoneImageUploaded mImageEntity, int mode) {
        Log.d(TAG, "onImageSaveReceivedSuccess ::" + mode);
        Fragment main = mManager.findFragmentByTag(Utils.TAG_RECEIVE_COMPLAINT_RESPOND);
        if (main != null && main.isVisible()) {
            AppUtils.hideProgressDialog();
            if (checkNoImageStatus == 1) {
                if (mImageEntity.getDocType().equals("B")) {
                    btn_defect_save.setText("Save");
                    btn_defect_save.setEnabled(false);
                } else if (mImageEntity.getDocType().equals("A")) {
                    btn_done_save.setText("Save");
                    btn_done_save.setEnabled(false);
                }
            } else {
                if (mImageEntity.getDocType().equals("B")) {
                    btn_defect_save.setText("Save");
                    btn_defect_save.setEnabled(true);
                } else if (mImageEntity.getDocType().equals("A")) {
                    btn_done_save.setText("Save");
                    btn_done_save.setEnabled(true);
                }
                //    AppUtils.showDialog(mActivity, "Data has been successfully Saved.");
                AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Image has been successfully Saved.");
            }
        }

    }

    @Override
    public void onImageReceivedSuccess(RCDownloadImage imageEntity, int mode) {
        processImage(imageEntity,false);
    }

    @Override
    public void onImageSaveReceivedFailure(String strErr, int mode) {
        Log.d(TAG, "onImageSaveReceivedFailure ::" + strErr);

        btn_defect_save.setText("Save");
        btn_defect_save.setEnabled(true);
        iv_defectfound.setEnabled(true);
        btn_done_save.setText("Save");
        btn_done_save.setEnabled(true);
        iv_workdone.setEnabled(true);
        Fragment main = mManager.findFragmentByTag(Utils.TAG_RECEIVE_COMPLAINT_RESPOND);
        if (main != null && main.isVisible()) {
            AppUtils.hideProgressDialog();
            AppUtils.showDialog(mActivity, strErr);
        }
    }

    @Override
    public void onImageReceivedFailure(String strErr, int mode) {
        iv_defectfound.setEnabled(true);
        iv_workdone.setEnabled(true);
        checkImageLoad = true;
        if (strErr != null && strErr.toUpperCase().equals("B")) {
            pb_defectfound.setVisibility(View.GONE);
        } else pb_workdone.setVisibility(View.GONE);
    }

    @Override
    public void onAllImagesReceived(List<DFoundWDoneImageEntity> mImageEntities, int mode) {
    }

    private class MyTextWatcher implements TextWatcher {

        private CustomTextInputLayout til_view;
        private TextInputEditText tie_view;

        private MyTextWatcher(TextInputEditText tie_view, CustomTextInputLayout til_view) {
            this.tie_view = tie_view;
            this.til_view = til_view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            try {
                if (tie_view.getText().toString().trim().isEmpty()) {
                    // til_view.setError(getString(R.string.msg_empty));
                } else {
                    til_view.setErrorEnabled(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onPPEFetchListSuccess(List<PPEFetchSaveEntity> ppeSaveEntities, int mode) {




        if(tv_select_workstatus.getText().toString().equalsIgnoreCase("Completed")){

            ppeList=true;
            FetchFeedbackRequest feedbackRequest =
                    new FetchFeedbackRequest(
                            mComplaintNumber,
                            mComplaintSite,
                            mOpco);
            feedBackService.fetchFeedbackDetailsData(feedbackRequest);
        }
        else{
            postDataToServer(requestToServer);

        }

    }

    @Override
    public void onPPENameListReceived(List<PPENameEntity> ppeNameEntities, int mode) {
        Log.d(TAG, "onPPENamesReceived ");
    }

    @Override
    public void onPPEFetchListFailure(String strErr, int mode) {

        FetchFeedbackRequest feedbackRequest =
                new FetchFeedbackRequest(
                        mComplaintNumber,
                        mComplaintSite,
                        mOpco);

        feedBackService.fetchFeedbackDetailsData(feedbackRequest);
    }

    @Override
    public void onPPESaveFailure(String strErr, int mode) {
        Log.d(TAG, "onPPESaveFailure ");
    }


    @Override
    public void onPPENameListFailure(String strErr, int mode) {
        Log.d(TAG, "onPPENameListFailure ");
    }

    @Override
    public void onPPESaveSuccess(String strMsg, int mode) {

    }

    @Override
    public void onPPESaveClicked(List<PPENameEntity> ppeNameEntities, List<PPEFetchSaveEntity> ppeSaveEntities, boolean isFetchdata) {
    }

    @Override
    public void onFeedbackEmployeeDetailsReceivedSuccess(List<EmployeeDetailsEntity> employeeDetailsEntities, int mode){

    }
    @Override
    public void onFeedbackDetailsReceivedSuccess(SaveFeedbackEntity saveFeedbackEntity, int mode){

        if(ppeList ){
            postDataToServer(requestToServer);
        }
        else{
            showDialog("PPE should be entered before saving the complaint.");
        }
    }

    @Override
    public void onAllFeedbackDetailsReceivedSuccess(List<SaveFeedbackEntity> saveFeedbackEntities, int mode){

    }

    @Override
    public void onFeedbackDetailsReceivedSuccessPPMAtten(PpmEmployeeFeedResponse saveFeedbackEntity){

    }

    @Override
    public  void onFeedbackEmployeeDetailsSaveSuccess (String strMsg, int mode){

    }

    @Override
    public void onFeedbackPpmStatusSucess (List<String> strMsg, int mode){

    }
    @Override
    public void onFeedbackEmployeeDetailsReceivedFailure(String strErr, int mode){

        if(ppeList){
            showDialog("FeedBack should be entered before saving the complaint.");
        }
        else{
            showDialog("PPE and FeedBack should be entered before saving the complaint.");
        }
    }
    public void showDialog(String strMsg){

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

    @Override
    public void onImageSaveReceivedSuccess1(RCDownloadImage imageEntity, int mode) {
        processImage(imageEntity,true);
    }


    public void processImage(final RCDownloadImage imageEntity, final boolean fromUpload) {


        imageTotalCount=imageEntity.getImageCount();

        if(imageEntity.getDocType().equalsIgnoreCase("B")) {
            imageEntityB=imageEntity;
        }
        else{
            imageEntityA=imageEntity;
        }
        try {

            if (imageEntityB!=null || imageEntityB.getBase64Image()!=null || !imageEntityB.getBase64Image().isEmpty() || imageEntityB.getDocType() != null) {

                itemList1 = new ArrayList<CustomRecyclerViewItem>();
                for (int i = 0; i < imageEntityB.getBase64Image().size(); i++) {
                    mImageToBeAttachedDefectFound = AppUtils.getDecodedString(imageEntityB.getBase64Image().get(i));
                    DFoundWDoneImageEntity imageEntity1 = new DFoundWDoneImageEntity("B");
                    imageEntity1.setOpco(mOpco);
                    imageEntity1.setComplaintSite(mComplaintSite);
                    imageEntity1.setComplaintNo(mComplaintNumber);
                    imageEntity1.setFileType("jpg");
                    imageEntity1.setTransactionType("R");
                    imageEntity1.setDocType("B");
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
                    itemList1.add(item);
                }
                setupRecyclerview1();
                img_upload_count1.setVisibility(View.VISIBLE);
                img_upload_count1.setText(imageEntityB.getBase64Image().size() + "/" + imageTotalCount + " Image uploaded - Defect Found");

                if (fromUpload) {
                    AppUtils.hideProgressDialog();
                    AppUtils.showSnackBar(R.id.coordinatorLayout, rootView, "Image has been successfully Saved.");
                }

            }
            else {
                customRecyclerViewDataAdapter1 = new CustomRecyclerViewDataAdapter(itemList1, imageTotalCount, this, getContext(), false, DefectFound);
                recyclerviewImage1.setAdapter(customRecyclerViewDataAdapter1);
                img_upload_count1.setVisibility(View.VISIBLE);
                img_upload_count1.setText(imageEntityB.getBase64Image().size() + "/" + imageTotalCount + " Image uploaded - Defect Found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            imageCount2=imageEntity.getImageCount();
            if (imageEntityA!=null || imageEntityA.getBase64Image()!=null || !imageEntityA.getBase64Image().isEmpty() || imageEntityA.getDocType() != null) {

                itemList2 = new ArrayList<CustomRecyclerViewItem>();
                for(int i=0;i<imageEntityA.getBase64Image().size();i++){
                    mImageToBeAttachedDefectFound = AppUtils.getDecodedString(imageEntityA.getBase64Image().get(i));
                    DFoundWDoneImageEntity imageEntity1 = new DFoundWDoneImageEntity("B" );
                    imageEntity1.setOpco(mOpco);
                    imageEntity1.setComplaintSite(mComplaintSite);
                    imageEntity1.setComplaintNo(mComplaintNumber);
                    imageEntity1.setFileType("jpg");
                    imageEntity1.setTransactionType("R");
                    imageEntity1.setDocType("A");
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
                    itemList2.add(item);
                }

                setupRecyclerview2();

                img_upload_count2.setVisibility(View.VISIBLE);
                img_upload_count2.setText(imageEntityA.getBase64Image().size()+"/"+imageTotalCount+" Image uploaded - Work Done");
                if(fromUpload){
                    AppUtils.hideProgressDialog();
                    AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Image has been successfully Saved.");
                }

            }

            else{
                customRecyclerViewDataAdapter2 = new CustomRecyclerViewDataAdapter(itemList2,imageTotalCount,this,getContext(),false,WorkDone);
                recyclerviewImage2.setAdapter(customRecyclerViewDataAdapter2);
                img_upload_count2.setVisibility(View.VISIBLE);
                img_upload_count2.setText(imageEntityA.getBase64Image().size()+"/"+imageTotalCount+" Image uploaded - Work Done");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupRecyclerview1(){

        customRecyclerViewDataAdapter1 = new CustomRecyclerViewDataAdapter(itemList1,imageTotalCount,this,getContext(),false,DefectFound);
        recyclerviewImage1.setAdapter(customRecyclerViewDataAdapter1);

    }

    public void setupRecyclerview2(){

        customRecyclerViewDataAdapter2 = new CustomRecyclerViewDataAdapter(itemList2,imageTotalCount,this,getContext(),false,WorkDone);
        recyclerviewImage2.setAdapter(customRecyclerViewDataAdapter2);

    }
}
