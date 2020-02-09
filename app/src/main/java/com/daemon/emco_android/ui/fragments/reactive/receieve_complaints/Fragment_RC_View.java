package com.daemon.emco_android.ui.fragments.reactive.receieve_complaints;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.activities.BarcodeCaptureActivity;
import com.daemon.emco_android.ui.activities.ZxingScannerActivity;
import com.daemon.emco_android.ui.adapter.ReceivecomplaintListAdapter;
import com.daemon.emco_android.repository.remote.ReceiveComplaintViewService;
import com.daemon.emco_android.ui.components.CustomTextInputLayout;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.dbhelper.RCAssetDetailsDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.RCRespondDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ReceiveComplaintViewDbInitializer;
import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.listeners.ReceivecomplaintView_Listener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.AssetDetailsRequest;
import com.daemon.emco_android.model.request.ReceiveComplaintViewRequest;
import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_RESPOND;

/** Created by vikram on 14/7/17. */
public class Fragment_RC_View extends Fragment implements ReceivecomplaintView_Listener {
  private static final String TAG = Fragment_RC_View.class.getSimpleName();
  private static final int RC_BARCODE_CAPTURE = 9001;
  private static final int Zxing_BARCODE_CAPTURE = 9002;

  private static final int ZXING_CAMERA_PERMISSION = 1;
  public Bundle mSavedInstanceState;
  private AppCompatActivity mActivity;
  private Font font = App.getInstance().getFontInstance();
  private SharedPreferences mPreferences;
  private SharedPreferences.Editor mEditor;
  private FragmentManager mManager;
  private Bundle mArgs;
  private CoordinatorLayout cl_main;
  private String mUnSignedPage = AppUtils.ARGS_RECEIVECOMPLAINT_PAGE;
  private int checkApiCAll;
  private CustomTextInputLayout til_region,
      til_contract_title,
      til_date,
      til_job_no,
      til_zone_area,
      til_building,
      til_location,
      til_location_description;
  private TextInputEditText tie_region,
      tie_contract_title,
      tie_job_no,
      tie_zone_area,
      tie_date,
      tie_building,
      tie_location,
      tie_location_description;
  private CustomTextInputLayout til_bar_code,
      til_asset_code,
      til_main_eqpt,
      til_size,
      til_make,
      til_model,
      til_remarks_others;
  private TextInputEditText tie_bar_code,tie_client_bar_code,
      tie_asset_code,
      tie_main_eqpt,
      tie_size,
      tie_make,
      tie_model,
      tie_remarks_others;
  private TextView tv_lbl_remarks, tv_select_remarks, tv_toolbar_title;
  private Button btn_respond, btn_barcode_scan, btn_next;
  private Toolbar mToolbar;
  private View rootView;
  private ReceiveComplaintItemEntity receiveComplaintItemEntity;
  private int mSelectedPosition = 0;
  private ReceiveComplaintViewEntity complaintViewEntity;
  private AssetDetailsEntity assetDetailsEntity;
  private String mNetworkInfo = null;
  private RCRespondDbInitializer complaintRespondDbInitializer;
  private List<AssetDetailsEntity> assetDetails;

  private ReceiveComplaintViewService complaintView_service;
  private List<String> remarkList = new ArrayList<>();
  private ReceivecomplaintListAdapter adapter;
  private RecyclerView recyclerView;
  private String mStrRemark = null;
  private String mStrEmpId = null;
  View.OnClickListener _OnClickListener =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Log.d(TAG, "onClick");
          AppUtils.closeInput(cl_main);
          switch (v.getId()) {
            case R.id.btn_next:
              gotoNextPage();
              break;
            case R.id.btn_respond:
              submitForm();
              break;
            case R.id.tv_select_remarks:
              getRemarks();
              break;

            case R.id.btn_barcode_scan:
              int requestId = AppUtils.getIdForRequestedCamera(AppUtils.CAMERA_FACING_BACK);
              if (requestId == -1) {
                AppUtils.showDialog(mActivity, "Camera not available");
              } else {
                // check that the device has play services available.
                int code =
                    GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mActivity);
                if (code != ConnectionResult.SUCCESS) {
                  if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                      != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        mActivity,
                        new String[] {Manifest.permission.CAMERA},
                        ZXING_CAMERA_PERMISSION);
                  } else {
                    Intent intent = new Intent(mActivity, ZxingScannerActivity.class);
                    intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);

                    startActivityForResult(intent, Zxing_BARCODE_CAPTURE);
                  }
                } else {
                  Intent intent = new Intent(mActivity, BarcodeCaptureActivity.class);
                  intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);

                  startActivityForResult(intent, RC_BARCODE_CAPTURE);
                }
                checkApiCAll = 1;
              }
              break;

            default:
              break;
          }
        }
      };
  private String mLoginData = null;
  private Type baseType = new TypeToken<List<String>>() {}.getType();

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

      complaintView_service = new ReceiveComplaintViewService(mActivity, this);

      //  mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS,
      // Context.MODE_PRIVATE);
      mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
      if (mLoginData != null) {
        Gson gson = new Gson();
        Login login = gson.fromJson(mLoginData, Login.class);
        mStrEmpId = login.getEmployeeId();
      }

      if (mArgs != null && mArgs.size() > 0) {
        mUnSignedPage = mArgs.getString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE);

        receiveComplaintItemEntity =
            mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS);
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
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(TAG, "onCreate");
    try {
      rootView = inflater.inflate(R.layout.fragment_receive_complaints_view, container, false);
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
      tv_lbl_remarks = (TextView) rootView.findViewById(R.id.tv_lbl_remarks);
      tv_select_remarks = (TextView) rootView.findViewById(R.id.tv_select_remarks);

      til_region = (CustomTextInputLayout) rootView.findViewById(R.id.til_region);
      til_contract_title = (CustomTextInputLayout) rootView.findViewById(R.id.til_contract_title);
      til_job_no = (CustomTextInputLayout) rootView.findViewById(R.id.til_job_no);
      til_zone_area = (CustomTextInputLayout) rootView.findViewById(R.id.til_zone_area);
      til_date = (CustomTextInputLayout) rootView.findViewById(R.id.til_date);
      til_building = (CustomTextInputLayout) rootView.findViewById(R.id.til_building);
      til_location = (CustomTextInputLayout) rootView.findViewById(R.id.til_location);
      til_location_description =
          (CustomTextInputLayout) rootView.findViewById(R.id.til_location_description);

      til_bar_code = (CustomTextInputLayout) rootView.findViewById(R.id.til_bar_code);
      til_asset_code = (CustomTextInputLayout) rootView.findViewById(R.id.til_asset_code);
      til_main_eqpt = (CustomTextInputLayout) rootView.findViewById(R.id.til_main_eqpt);
      til_size = (CustomTextInputLayout) rootView.findViewById(R.id.til_size);
      til_make = (CustomTextInputLayout) rootView.findViewById(R.id.til_make);
      til_model = (CustomTextInputLayout) rootView.findViewById(R.id.til_model);
      til_remarks_others = (CustomTextInputLayout) rootView.findViewById(R.id.til_remarks_others);

      tie_region = (TextInputEditText) rootView.findViewById(R.id.tie_region);
      tie_contract_title = (TextInputEditText) rootView.findViewById(R.id.tie_contract_title);
      tie_job_no = (TextInputEditText) rootView.findViewById(R.id.tie_job_no);
      tie_zone_area = (TextInputEditText) rootView.findViewById(R.id.tie_zone_area);
      tie_date = (TextInputEditText) rootView.findViewById(R.id.tie_date);
      tie_building = (TextInputEditText) rootView.findViewById(R.id.tie_building);
      tie_location = (TextInputEditText) rootView.findViewById(R.id.tie_location);
      tie_location_description =
          (TextInputEditText) rootView.findViewById(R.id.tie_location_description);

      tie_bar_code = (TextInputEditText) rootView.findViewById(R.id.tie_bar_code);
      tie_client_bar_code = (TextInputEditText) rootView.findViewById(R.id.tie_client_bar_code);
      tie_asset_code = (TextInputEditText) rootView.findViewById(R.id.tie_asset_code);
      tie_main_eqpt = (TextInputEditText) rootView.findViewById(R.id.tie_main_eqpt);
      tie_size = (TextInputEditText) rootView.findViewById(R.id.tie_size);
      tie_make = (TextInputEditText) rootView.findViewById(R.id.tie_make);
      tie_model = (TextInputEditText) rootView.findViewById(R.id.tie_model);
      tie_remarks_others = (TextInputEditText) rootView.findViewById(R.id.tie_remarks_others);

      btn_barcode_scan = (Button) rootView.findViewById(R.id.btn_barcode_scan);
      btn_respond = (Button) rootView.findViewById(R.id.btn_respond);
      btn_next = (Button) rootView.findViewById(R.id.btn_next);

      setupActionBar();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setupActionBar() {
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
  }

  private void setProperties() {
    Log.d(TAG, "setProperties");

    // only respond after move this page
    btn_next.setEnabled(false);

    tv_lbl_remarks.setTypeface(font.getHelveticaRegular());
    tv_select_remarks.setTypeface(font.getHelveticaRegular());
    tie_region.setTypeface(font.getHelveticaRegular());
    tie_contract_title.setTypeface(font.getHelveticaRegular());
    tie_job_no.setTypeface(font.getHelveticaRegular());
    tie_zone_area.setTypeface(font.getHelveticaRegular());
    tie_date.setTypeface(font.getHelveticaRegular());
    tie_building.setTypeface(font.getHelveticaRegular());
    tie_location.setTypeface(font.getHelveticaRegular());
    tie_location_description.setTypeface(font.getHelveticaRegular());
    tie_asset_code.setTypeface(font.getHelveticaRegular());
    tie_main_eqpt.setTypeface(font.getHelveticaRegular());
    tie_size.setTypeface(font.getHelveticaRegular());
    tie_make.setTypeface(font.getHelveticaRegular());
    tie_model.setTypeface(font.getHelveticaRegular());
    tie_remarks_others.setTypeface(font.getHelveticaRegular());
    btn_respond.setTypeface(font.getHelveticaRegular());
    btn_barcode_scan.setTypeface(font.getHelveticaRegular());
    btn_next.setTypeface(font.getHelveticaRegular());
    btn_respond.setOnClickListener(_OnClickListener);
    btn_next.setOnClickListener(_OnClickListener);
    btn_barcode_scan.setOnClickListener(_OnClickListener);
    tie_region.setKeyListener(null);
    tie_contract_title.setKeyListener(null);
    tie_job_no.setKeyListener(null);
    tie_zone_area.setKeyListener(null);
    tie_date.setKeyListener(null);
    tie_building.setKeyListener(null);
    tie_location.setKeyListener(null);
    tie_location_description.setKeyListener(null);
    tie_asset_code.setKeyListener(null);
    tie_main_eqpt.setKeyListener(null);
    tie_size.setKeyListener(null);
    tie_make.setKeyListener(null);
    tie_model.setKeyListener(null);

    tie_remarks_others.addTextChangedListener(
        new Fragment_RC_View.MyTextWatcher(tie_remarks_others, til_remarks_others));

    tv_select_remarks.setOnClickListener(_OnClickListener);

    tie_bar_code.setOnEditorActionListener(
        new TextView.OnEditorActionListener() {
          @Override
          public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
              AppUtils.closeInput(cl_main);
              if (TextUtils.isEmpty(tie_bar_code.getText())) {
                til_bar_code.setError(getString(R.string.thisfeildisrequired));
                tie_bar_code.requestFocus();
              } else {
                getBarcodeDetailsService(1);
                checkApiCAll = 1;
              }
              return true;
            }
            return false;
          }
        });

    if (mSavedInstanceState != null) {
      Log.d(TAG, " mSavedInstanceState : " + mSavedInstanceState);
      receiveComplaintItemEntity =
          mSavedInstanceState.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS);
      mSelectedPosition = mSavedInstanceState.getInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION);
      complaintViewEntity =
          mSavedInstanceState.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);
      assetDetailsEntity =
          mSavedInstanceState.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ASSET_DETAILS);
      setReceiveComplaintViewValue(complaintViewEntity);
      // setReceiveComplaintViewAssetDetailsValue(assetDetailsEntity);
    }
    AppUtils.closeInput(cl_main);
  }

  private void getBarcodeDetailsService(int check) {
    if (mPreferences
        .getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE)
        .contains(AppUtils.NETWORK_AVAILABLE)) {
      if (complaintViewEntity != null) {
        if (check == 1) {
          AssetDetailsRequest assetDetailsRequest =
              new AssetDetailsRequest(
                  complaintViewEntity.getOpco(),
                  complaintViewEntity.getJobNumber(),
                  complaintViewEntity.getZoneCode(),
                  complaintViewEntity.getBuildingCode(),
                  tie_bar_code.getText().toString());
          AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
          complaintView_service.GetReceiveComplaintViewAssetDetailsData(assetDetailsRequest);
        }
      } else AppUtils.showDialog(mActivity, getString(R.string.lbl_alert_rc_not_available));
    } else {
      Log.d(TAG, "getReceiveComplainViewAssetDetailsFromLocal");
      AssetDetailsEntity entity = new AssetDetailsEntity();
      entity.setAssetBarCode(tie_bar_code.getText().toString());
      entity.setZoneCode(complaintViewEntity.getZoneCode());
      entity.setJobNo(complaintViewEntity.getJobNumber());
      entity.setBuildingCode(complaintViewEntity.getBuildingCode());
      entity.setOpco(complaintViewEntity.getOpco());
      new RCAssetDetailsDbInitializer(mActivity, this, entity).execute(AppUtils.MODE_GET);
    }
  }

  private void setReceiveComplaintViewValue(ReceiveComplaintViewEntity complaintViewValue) {
    Log.d(TAG, "setReceiveComplaintViewValue ");
    try {
      if (complaintViewValue != null) {
        this.complaintViewEntity = complaintViewValue;
        if (!TextUtils.isEmpty(complaintViewValue.getRegionName()) &&  !complaintViewValue.getRegionName().equalsIgnoreCase("NULL"))
          tie_region.setText(complaintViewValue.getRegionName());
        if (!TextUtils.isEmpty(complaintViewValue.getJobDescription()) &&  !complaintViewValue.getJobDescription().equalsIgnoreCase("NULL"))
          tie_contract_title.setText(complaintViewValue.getJobDescription());
        if (!TextUtils.isEmpty(complaintViewValue.getJobNumber()) &&  !complaintViewValue.getJobNumber().equalsIgnoreCase("NULL"))
          tie_job_no.setText(complaintViewValue.getJobNumber());
        if (!TextUtils.isEmpty(complaintViewValue.getZoneName()) &&  !complaintViewValue.getZoneName().equalsIgnoreCase("NULL"))
          tie_zone_area.setText(complaintViewValue.getZoneName());
        if (!TextUtils.isEmpty(complaintViewValue.getBuildingName()) &&  !complaintViewValue.getBuildingName().equalsIgnoreCase("NULL"))
          tie_building.setText(complaintViewValue.getBuildingName());
        if (!TextUtils.isEmpty(complaintViewValue.getComplaintDate()) &&  !complaintViewValue.getComplaintDate().equalsIgnoreCase("NULL"))
          tie_date.setText(complaintViewEntity.getComplaintDate());
        if (!TextUtils.isEmpty(complaintViewValue.getLocation()) &&  !complaintViewValue.getLocation().equalsIgnoreCase("NULL"))
          tie_location.setText(complaintViewValue.getLocation());
        if (!TextUtils.isEmpty(complaintViewValue.getLocationDescription()) &&  !complaintViewValue.getLocationDescription().equalsIgnoreCase("NULL"))
          tie_location_description.setText(complaintViewValue.getLocationDescription());
        if (!TextUtils.isEmpty(complaintViewValue.getEquipmentName()) &&  !complaintViewValue.getEquipmentName().equalsIgnoreCase("NULL") )
          tie_main_eqpt.setText(complaintViewValue.getEquipmentName());
        if (!TextUtils.isEmpty(complaintViewValue.getAssetSize()) &&  !complaintViewValue.getAssetSize().equalsIgnoreCase("NULL"))
          tie_size.setText(complaintViewValue.getAssetSize());
        if (!TextUtils.isEmpty(complaintViewValue.getAssetMake()) &&  !complaintViewValue.getAssetMake().equalsIgnoreCase("NULL"))
          tie_make.setText(complaintViewValue.getAssetMake());
        if (!TextUtils.isEmpty(complaintViewValue.getAssetModel()) &&  !complaintViewValue.getAssetModel().equalsIgnoreCase("NULL"))
          tie_model.setText(complaintViewValue.getAssetModel());

        if (!TextUtils.isEmpty(complaintViewValue.getClientBarcode()) &&  !complaintViewValue.getClientBarcode().equalsIgnoreCase("NULL"))
          tie_client_bar_code.setText(complaintViewValue.getClientBarcode());

        if (!TextUtils.isEmpty(complaintViewValue.getAssetBarCode()) &&  !complaintViewValue.getAssetBarCode().equalsIgnoreCase("NULL"))
        {
          tie_bar_code.setText(complaintViewValue.getAssetBarCode());



          if (assetDetailsEntity == null) {
            // btn_barcode_scan.setOnClickListener(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
              // btn_barcode_scan.setBackground(getResources().getDrawable(R.drawable.bg_btn_light));
            }
            getBarcodeDetailsService(0);
            checkApiCAll = 0;
          }
        }

        if (!TextUtils.isEmpty(complaintViewValue.getAssetCode()) &&  !complaintViewValue.getAssetCode().equalsIgnoreCase("NULL"))
          tie_asset_code.setText(complaintViewValue.getAssetCode());

        if (complaintViewEntity.getComplaintStatus().equals("F")) {
          btn_respond.setEnabled(true);
          btn_next.setEnabled(false);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btn_next.setBackground(getResources().getDrawable(R.drawable.bg_btn_light));
          }
        } else {
          tv_select_remarks.setText(complaintViewValue.getResponseDetails());
          tv_select_remarks.setEnabled(true);
          btn_next.setEnabled(true);
          btn_respond.setEnabled(false);
          //  tie_bar_code.setKeyListener(null);
          // btn_barcode_scan.setOnClickListener(null);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btn_respond.setBackground(getResources().getDrawable(R.drawable.bg_btn_light));
            // btn_barcode_scan.setBackground(getResources().getDrawable(R.drawable.bg_btn_light));
          }
        }
      } else Log.d(TAG, "setReceiveComplaintViewValue null");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private void setReceiveComplaintViewAssetDetailsValue(AssetDetailsEntity assetDetailsValue) {
    Log.d(TAG, "setReceiveComplaintViewAssetDetailsValue");
    try {
      if (assetDetailsValue != null) {
        this.assetDetailsEntity = assetDetailsValue;
        if (!TextUtils.isEmpty(assetDetailsValue.getAssetBarCode())
            && !assetDetailsValue.getAssetBarCode().equalsIgnoreCase("NULL"))
          tie_bar_code.setText(assetDetailsValue.getAssetBarCode());
        if (!TextUtils.isEmpty(assetDetailsValue.getAssetCode())
            && !assetDetailsValue.getAssetCode().equalsIgnoreCase("NULL"))
          tie_asset_code.setText(assetDetailsValue.getAssetCode());
        if (!TextUtils.isEmpty(assetDetailsValue.getEquipmentName()))
          tie_main_eqpt.setText(assetDetailsValue.getEquipmentName());
        if (!TextUtils.isEmpty(assetDetailsValue.getAssetSize())
            && !assetDetailsValue.getAssetSize().equalsIgnoreCase("NULL"))
          tie_size.setText(assetDetailsValue.getAssetSize());
        if (!TextUtils.isEmpty(assetDetailsValue.getAssetMake())
            && !assetDetailsValue.getAssetMake().equalsIgnoreCase("NULL"))
          tie_make.setText(assetDetailsValue.getAssetMake());
        if (!TextUtils.isEmpty(assetDetailsValue.getAssetModel())
            && !assetDetailsValue.getAssetModel().equalsIgnoreCase("NULL"))
          tie_model.setText(assetDetailsValue.getAssetModel());

        if (!TextUtils.isEmpty(assetDetailsValue.getClientBarcode())
                && !assetDetailsValue.getClientBarcode().equalsIgnoreCase("NULL"))
          tie_client_bar_code.setText(assetDetailsValue.getClientBarcode());

      } else Log.d(TAG, "setReceiveComplaintViewAssetDetailsValue null");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private void gotoNextPage() {

       mStrRemark =tv_select_remarks.getText().toString();

    if (complaintViewEntity != null) {
      if (complaintViewEntity.getComplaintStatus().equals("F")) {
        AppUtils.showDialog(mActivity, "Complaint Status Forwarded..");
      } else {
       if (mStrRemark.equals("Others")) {
          if (TextUtils.isEmpty(tie_remarks_others.getText().toString())) {
            til_remarks_others.setError(getString(R.string.thisfeildisrequired));
            tie_remarks_others.requestFocus();
          } else {
            mStrRemark = tie_remarks_others.getText().toString();
            complaintViewEntity.setResponseDetails(mStrRemark);
            mSavedInstanceState = getSavedState();
            Bundle data = new Bundle();
            data.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, mUnSignedPage);
            data.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, complaintViewEntity);
            data.putInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION, mSelectedPosition);
            Fragment_RC_Respond fragment = new Fragment_RC_Respond();
            fragment.setArguments(data);
            fragment.setAdapter(adapter);
            fragment.setRecyclerView(recyclerView);
            FragmentTransaction fragmentTransaction = mManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            fragmentTransaction.replace(R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_RESPOND);
            fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_RESPOND);
            fragmentTransaction.commit();
          }
        } else {
          complaintViewEntity.setResponseDetails(mStrRemark);
          mSavedInstanceState = getSavedState();
          Bundle data = new Bundle();
          data.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, mUnSignedPage);
          data.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, complaintViewEntity);
          data.putInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION, mSelectedPosition);
          Fragment_RC_Respond fragment = new Fragment_RC_Respond();
          fragment.setArguments(data);
          fragment.setAdapter(adapter);
          fragment.setRecyclerView(recyclerView);
          FragmentTransaction fragmentTransaction = mManager.beginTransaction();
          fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
          fragmentTransaction.replace(R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_RESPOND);
          fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_RESPOND);
          fragmentTransaction.commit();
        }
      }
    } else AppUtils.showDialog(mActivity, "Please Wait Respond data getting..");
  }

  private void submitForm() {
    Log.d(TAG, "submitForm");
    try {
      AppUtils.closeInput(cl_main);
      if (complaintViewEntity != null) {
        ReceiveComplaintRespondEntity request = new ReceiveComplaintRespondEntity();
        request.setComplaintNumber(complaintViewEntity.getComplaintNumber());
        request.setComplaintSite(complaintViewEntity.getComplaintSite());
        request.setOpco(complaintViewEntity.getOpco());
        request.setFlat(complaintViewEntity.getFlat());
        request.setFloor(complaintViewEntity.getFloor());
        request.setLocationCode(complaintViewEntity.getLocation());
        // request.setResponseDate(AppUtils.getDateTime(complaintViewEntity.getComplaintDate()));
        request.setResponseDate(complaintViewEntity.getComplaintDate());
        request.setModifiedBy(mStrEmpId);
        request.setComplaintStatus("R");

        if (complaintViewEntity.getAssetBarCode() != null) {
          request.setAssetCode(complaintViewEntity.getAssetCode());
          request.setAssetBarCode(complaintViewEntity.getAssetBarCode());
        } else {
          request.setAssetCode(tie_asset_code.getText().toString());
          request.setAssetBarCode(tie_bar_code.getText().toString());

        }
        /* if (TextUtils.isEmpty(tie_bar_code.getText().toString())) {
            til_bar_code.setError(getString(R.string.lbl_no_barcode_found));
        } else if (TextUtils.isEmpty(tie_asset_code.getText().toString())) {
            til_asset_code.setError(getString(R.string.lbl_no_barcode_found));
            tie_asset_code.requestFocus();
        } else*/

        if (mStrRemark == null) {
          AppUtils.setErrorBg(tv_select_remarks, true);
          AppUtils.showDialog(mActivity, getString(R.string.lbl_please_select_remarks));
        } else if (mStrRemark.equals("Others")) {
          if (TextUtils.isEmpty(tie_remarks_others.getText().toString())) {
            til_remarks_others.setError(getString(R.string.thisfeildisrequired));
            tie_remarks_others.requestFocus();
          } else {
            mStrRemark = tie_remarks_others.getText().toString();
            request.setResponseDetails(mStrRemark);
            postDataToServer(request);
          }
        } else {
          request.setResponseDetails(mStrRemark);
          postDataToServer(request);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getReceiveComplainViewFromService(
      ReceiveComplaintItemEntity receiveComplaintItemEntity) {
    Log.d(TAG, "getReceiveComplainViewFromService");
    try {
      if (mPreferences
          .getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE)
          .contains(AppUtils.NETWORK_AVAILABLE)) {
        Log.d(TAG, "getReceiveComplainViewFromServer");
        AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
        complaintView_service.GetReceiveComplaintViewData(
            new ReceiveComplaintViewRequest(
                receiveComplaintItemEntity.getComplaintNumber(),
                receiveComplaintItemEntity.getSiteCode(),
                receiveComplaintItemEntity.getOpco()));

        Log.d(TAG, "getReceiveComplainRemarksFromService");
        complaintView_service.getReceiveComplaintViewRemarksData();
      } else {
        Log.d(TAG, "getReceiveComplainViewFromLocal");
        ReceiveComplaintViewEntity complaintViewValue = new ReceiveComplaintViewEntity();
        complaintViewValue.setComplaintNumber(receiveComplaintItemEntity.getComplaintNumber());
        complaintViewValue.setComplaintSite(receiveComplaintItemEntity.getSiteCode());
        complaintViewValue.setOpco(receiveComplaintItemEntity.getOpco());

        new ReceiveComplaintViewDbInitializer(mActivity, this, complaintViewValue)
            .execute(AppUtils.MODE_GET);

        String strJsonTr = mPreferences.getString(AppUtils.SHARED_CVIEW_REMARKS, null);
        Log.d(TAG, "str json :;" + strJsonTr);
        if (strJsonTr != null && strJsonTr.length() > 0) {
          remarkList = new Gson().fromJson(strJsonTr, baseType);
        }
      }
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
        complaintRespondDbInitializer.populateAsync(
            AppDatabase.getAppDatabase(mActivity), respondRequest, AppUtils.MODE_INSERT);
    }
  }

  @Override
  public void onReceiveComplaintRemarksReceived(List<String> remarkList, int mode) {
    this.remarkList = remarkList;
    String strTechRemarks = new Gson().toJson(remarkList, baseType);
    mEditor.putString(AppUtils.SHARED_CVIEW_REMARKS, strTechRemarks);
    mEditor.commit();
  }

  @Override
  public void onReceiveComplaintRespondReceived(String strMsg, String complaintNumber, int mode) {
    Log.d(TAG, "onReceiveComplaintRespondReceived");
    try {
      if (mode == AppUtils.MODE_SERVER)
        complaintRespondDbInitializer.populateAsync(
            AppDatabase.getAppDatabase(mActivity), complaintNumber, AppUtils.MODE_DELETE);

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
                        complaintViewEntity.setComplaintStatus("R");
                        complaintViewEntity.setResponseDetails(mStrRemark);
                        if (adapter != null) adapter.refreshItemtoResponded(mSelectedPosition);
                        if (recyclerView != null) recyclerView.postInvalidate();
                        gotoNextPage();
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
      List<ReceiveComplaintViewEntity> receiveComplaintItemView, int from) {
    AppUtils.hideProgressDialog();
    Log.d(TAG, "onReceiveComplaintViewReceived" + receiveComplaintItemView.size());
    setReceiveComplaintViewValue(receiveComplaintItemView.get(0));
    if (from == AppUtils.MODE_SERVER && mUnSignedPage == AppUtils.ARGS_RECEIVECOMPLAINT_PAGE) {
      new ReceiveComplaintViewDbInitializer(mActivity, this, complaintViewEntity)
          .execute(AppUtils.MODE_INSERT_SINGLE);
    }
  }

  @Override
  public void onReceiveComplaintViewAssetDetailsReceived(
      final List<AssetDetailsEntity> assetDetailsEntities,final int from) {


    if(assetDetailsEntities.size()>0){

      StringBuffer sb=new StringBuffer();
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
      sb.append(System.getProperty("line.separator"));

      if(assetDetailsEntities.get(0).getAssetCode()!=null && !(assetDetailsEntities.get(0).getAssetCode().equalsIgnoreCase("NULL"))){
        sb.append( "Asset/Location Code - "+assetDetailsEntities.get(0).getAssetCode());
      }
      else{
        sb.append( "Asset/Location Code - "+assetDetailsEntities.get(0).getAssetCode());
      }
      sb.append(System.getProperty("line.separator"));
      if(assetDetailsEntities.get(0).getEquipmentName()!=null && !(assetDetailsEntities.get(0).getEquipmentName().equalsIgnoreCase("NULL"))){
        sb.append( "Main Equipment - "+assetDetailsEntities.get(0).getEquipmentName());
      }
      else{
        sb.append( "Main Equipment - ");
      }

      sb.append(System.getProperty("line.separator"));

      if(assetDetailsEntities.get(0).getAssetSize()!=null && !(assetDetailsEntities.get(0).getAssetSize().equalsIgnoreCase("NULL"))){
        sb.append( "Size - "+assetDetailsEntities.get(0).getAssetSize());
      }
      else{
        sb.append( "Size - ");
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


      AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
      builder.setTitle("Asset Detail").setMessage(sb.toString())

              .setCancelable(false)
              .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  dialog.dismiss();
                  assetDetails = assetDetailsEntities;
                  Log.d(TAG, "onReceiveComplaintViewAssetDetailsReceived " + assetDetailsEntities.size());
                  // AppUtils.hideProgressDialog();
                  setReceiveComplaintViewAssetDetailsValue(assetDetailsEntities.get(0));
                  if (from == AppUtils.MODE_SERVER && mUnSignedPage == AppUtils.ARGS_RECEIVECOMPLAINT_PAGE) {
                    Log.d(TAG, "onReceiveComplaintViewAssetDetailsReceived");
                    new RCAssetDetailsDbInitializer(mActivity, Fragment_RC_View.this, assetDetailsEntity)
                            .execute(AppUtils.MODE_INSERT_SINGLE);
                  }
                  if (checkApiCAll == 1) {
                    if (mPreferences
                            .getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE)
                            .contains(AppUtils.NETWORK_AVAILABLE)) {
                      if (complaintViewEntity != null) {
                        SaveRatedServiceRequest assetDetailsRequest = new SaveRatedServiceRequest();
                        assetDetailsRequest.setComplaintNumber(complaintViewEntity.getComplaintNumber());
                        assetDetailsRequest.setOpco(complaintViewEntity.getOpco());
                        assetDetailsRequest.setComplaintSite(complaintViewEntity.getComplaintSite());
                        assetDetailsRequest.setBuildingCode(complaintViewEntity.getBuildingCode());
                        assetDetailsRequest.setZoneCode(complaintViewEntity.getZoneCode());
                        assetDetailsRequest.setJobNumber(complaintViewEntity.getJobNumber());
                        assetDetailsRequest.setFloor(assetDetails.get(0).getFloor());
                        assetDetailsRequest.setFlat(assetDetails.get(0).getFlat());
                        assetDetailsRequest.setLocationCode(assetDetails.get(0).getAssetCode());
                        assetDetailsRequest.setAssetBarCode(assetDetails.get(0).getAssetBarCode());
                        assetDetailsRequest.setClientBarcode(assetDetails.get(0).getClientBarcode());
                        complaintView_service.saveReceiveComplaintAssetCode(assetDetailsRequest);
                      } else AppUtils.showDialog(mActivity, getString(R.string.lbl_alert_rc_not_available));
                    }
                  } else {
                    AppUtils.hideProgressDialog();
                  }

                }
              })
              .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  dialog.cancel();
                  AppUtils.hideProgressDialog();
                }
              });
      AlertDialog alert = builder.create();
      alert.show();


    }

    else{
      AppUtils.hideProgressDialog();
      AppUtils.showDialog(mActivity, "No Data found.");
    }



  }

  @Override
  public void onReceiveComplaintViewReceivedError(String strErr, int from) {
    Log.d(TAG, "onReceiveComplaintViewReceivedError");
    tie_bar_code.setError("Enter a valid barcode");
    try {
      AppUtils.hideProgressDialog();
      Fragment main = mManager.findFragmentByTag(Utils.TAG_RECEIVE_COMPLAINT_VIEW);
      if (main != null && main.isVisible()) AppUtils.showDialog(mActivity, strErr);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onReceiveBarCodeAssetReceived(String msg, int mode) {
    AppUtils.hideProgressDialog();
  }

  @Override
  public void onAllReceiveComplaintData(
      List<ReceiveComplaintRespondEntity> complaintRespondEntities, int modeLocal) {}

  @Override
  public void onReceiveComplaintBycomplaintNumber(
      ReceiveComplaintRespondEntity complaintRespondEntity, int modeLocal) {}

  public void getRemarks() {
    Log.d(TAG, "getStatus");
    try {
      if (remarkList.isEmpty()) return;
      MaterialDialog.Builder dialog = new MaterialDialog.Builder(mActivity);
      dialog.title(R.string.lbl_select_remarks);
      if (remarkList.isEmpty()) dialog.items(R.array.string_array_remarks);
      else dialog.items(remarkList);
      dialog.itemsCallbackSingleChoice(
          -1,
          new MaterialDialog.ListCallbackSingleChoice() {
            @Override
            public boolean onSelection(
                MaterialDialog dialog, View view, int which, CharSequence text) {
              if (which >= 0) {
                mStrRemark = text.toString();
                tv_select_remarks.setText(text.toString());
                tv_select_remarks.setTypeface(font.getHelveticaBold());
                AppUtils.setErrorBg(tv_select_remarks, false);

                if (mStrRemark.equals("Others")) {
                  til_remarks_others.setVisibility(View.VISIBLE);
                } else {
                  til_remarks_others.setVisibility(View.GONE);
                }
              } else {
                AppUtils.setErrorBg(tv_select_remarks, true);
                mStrRemark = null;
                tv_select_remarks.setText("");
                tv_select_remarks.setHint(getString(R.string.lbl_select_remarks));
                AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
              }

              return true;
            }
          });
      dialog.positiveText(R.string.lbl_done);
      dialog.negativeText(R.string.lbl_close);
      dialog.show();
    } catch (Exception ex) {
      ex.printStackTrace();
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
        Fragment _fragment = new MainLandingUI();
        FragmentTransaction _transaction = mManager.beginTransaction();
        _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        _transaction.replace(R.id.frame_container, _fragment);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    try {
      if (requestCode == RC_BARCODE_CAPTURE) {
        if (resultCode == CommonStatusCodes.SUCCESS) {
          if (data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
            tie_bar_code.setText(barcode.displayValue);
            Log.d(TAG, "Barcode read: " + barcode.displayValue);
            if (assetDetailsEntity == null) {
              getBarcodeDetailsService(1);
            } else {
              if (barcode != null) {
                getBarcodeDetailsService(1);
              }
            }
          } else {
            til_bar_code.setError(getString(R.string.lbl_no_barcode_captured));
            Log.d(TAG, "No barcode captured, intent data is null");
          }
        } else {
          til_bar_code.setError(
              String.format(
                  getString(R.string.barcode_error),
                  CommonStatusCodes.getStatusCodeString(resultCode)));
        }
      } else if (requestCode == Zxing_BARCODE_CAPTURE) {
        if (resultCode == CommonStatusCodes.SUCCESS) {
          if (data != null) {
            String barcode = data.getStringExtra(BarcodeCaptureActivity.BarcodeObject);
            tie_bar_code.setText(barcode);
            Log.d(TAG, "Barcode read: " + barcode);
            if (assetDetailsEntity == null && barcode != null) {
              getBarcodeDetailsService(1);
            }
          } else {
            til_bar_code.setError(getString(R.string.lbl_no_barcode_captured));
            Log.d(TAG, "No barcode captured, intent data is null");
          }
        } else {
          til_bar_code.setError(
              String.format(
                  getString(R.string.barcode_error),
                  CommonStatusCodes.getStatusCodeString(resultCode)));
        }
      } else {
        super.onActivityResult(requestCode, resultCode, data);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      case ZXING_CAMERA_PERMISSION:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          Intent intent = new Intent(mActivity, ZxingScannerActivity.class);
          intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);

          startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
        return;
    }
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
    outState.putParcelable(
        AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS, receiveComplaintItemEntity);
    outState.putInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION, mSelectedPosition);
    outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ASSET_DETAILS, assetDetailsEntity);
    return outState;
  }

  public Bundle getSavedState() {
    Bundle outState = new Bundle();
    outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, complaintViewEntity);
    outState.putParcelable(
        AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS, receiveComplaintItemEntity);
    outState.putInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION, mSelectedPosition);
    outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ASSET_DETAILS, assetDetailsEntity);
    return outState;
  }

  public void setAdapter(ReceivecomplaintListAdapter adapter) {
    this.adapter = adapter;
  }

  public void setRecyclerView(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
  }

  private class MyTextWatcher implements TextWatcher {

    private CustomTextInputLayout til_view;
    private TextInputEditText tie_view;

    private MyTextWatcher(TextInputEditText tie_view, CustomTextInputLayout til_view) {
      this.tie_view = tie_view;
      this.til_view = til_view;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    public void afterTextChanged(Editable editable) {
      try {
        if (tie_view.getText().toString().trim().isEmpty()) {
          til_view.setError(getString(R.string.msg_empty));
        } else {
          til_view.setErrorEnabled(false);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
