package com.daemon.emco_android.ui.fragments.inspection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.GetLogComplaintPopupRepository;
import com.daemon.emco_android.repository.db.dbhelper.ContractDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.SiteAreaDbInitializer;
import com.daemon.emco_android.repository.db.entity.BuildingDetailsEntity;
import com.daemon.emco_android.repository.db.entity.ContractEntity;
import com.daemon.emco_android.repository.db.entity.SiteAreaEntity;
import com.daemon.emco_android.repository.db.entity.ZoneEntity;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.listeners.BuildingDetailsListener;
import com.daemon.emco_android.listeners.DatePickerDialogListener;
import com.daemon.emco_android.listeners.JobNoListener;
import com.daemon.emco_android.listeners.SiteListener;
import com.daemon.emco_android.listeners.ZoneListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.BuildingDetailsRequest;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.model.request.HardSoftRequest;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.SessionManager;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/** Created by subbu on 17/8/17. */

public class Fragment_IM_HardSoftFilter extends Fragment
    implements JobNoListener, SiteListener, DatePickerDialogListener,BuildingDetailsListener,ZoneListener{
  private final String TAG = Fragment_IM_HardSoftFilter.class.getSimpleName();
  // root view of fragment
  View rootView = null;
  private String mNetworkInfo = null;
  private AppCompatActivity mActivity;
  private Context mContext;
  private Bundle mArgs;
  private String mTitle;
  private Font font = App.getInstance().getFontInstance();
  private SharedPreferences mPreferences;
  private SharedPreferences.Editor mEditor;
  private FragmentManager mManager;
  private CoordinatorLayout cl_main;
  private TextView tv_lbl_jobno, tv_lbl_site, tv_lbl_from_date, tv_lbl_to_date,tv_zone_area,tv_building,tv_select_building,tv_select_zone_area;
  private TextView tv_select_jobno, tv_select_site, tv_select_from_date, tv_select_to_date;
  private Button btnSearch;
  private Toolbar mToolbar;
  private int mModeDate;
  private HardSoftRequest hardSoftRequest;
  private GetLogComplaintPopupRepository mGetComplaintPopupService;
  /** Global variables for post log complaint data */
  private Login mUserData;
  private String mStrLoginData = null, mFromDate = null, mToDate = null;
  private String mStrEmployeeId = null;
  private String mReportTypes = null; // response data from category data
  private String mStrSiteCode = null, mJobNo = null,mZoneCode=null,mBuildingCode=null;
  private String mOpco = null;
  private List<SiteAreaEntity> listSiteArea = new ArrayList<>();
  private List<ContractEntity> listJobNo = new ArrayList<>();
  private List<ZoneEntity> zoneArea = new ArrayList<>();
  private List<BuildingDetailsEntity> buildingName = new ArrayList<>();
  View.OnClickListener _OnClickListener =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          switch (v.getId()) {
            case R.id.btnSearch:
              submitForm();
              break;
            case R.id.tv_select_from_date:
              getDate(AppUtils.MODE_FROMDATE);
              break;
            case R.id.tv_select_to_date:
              getDate(AppUtils.MODE_TODATE);
              break;
            case R.id.tv_select_site:
              getSiteName();
              break;
            case R.id.tv_select_job_no:
              getJobNo();
              break;
            case R.id.tv_select_building:
              getBuildingName();
              break;
            case R.id.tv_select_zone_area:
              getZoneArea();
              break;
            default:
              break;
          }
        }
      };

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    try {
      mActivity = (AppCompatActivity) getActivity();
      mContext = mActivity;
      setHasOptionsMenu(true);
      setRetainInstance(false);
      mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
      mEditor = mPreferences.edit();
      mStrLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
      if (mStrLoginData != null) {
        Gson gson = new Gson();
        mUserData = gson.fromJson(mStrLoginData, Login.class);
        mStrEmployeeId = mUserData.getEmployeeId();
      }

      mManager = mActivity.getSupportFragmentManager();
      font = App.getInstance().getFontInstance();
      mArgs = getArguments();
      if (mArgs != null) {
        if (mArgs.containsKey(AppUtils.ARGS_IM_SERVICES_Page_TITLE)) {
          mTitle = mArgs.getString(AppUtils.ARGS_IM_SERVICES_Page_TITLE);
        }
      }
      mGetComplaintPopupService =
          new GetLogComplaintPopupRepository(
              mActivity,
              new EmployeeIdRequest(
                  mUserData.getEmployeeId(),
                  mUserData.getEmployeeId() == null ? mUserData.getUserName() : null,
                  null,
                  null));
      getPopupDataFromServer();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    try {
      rootView = (View) inflater.inflate(R.layout.fragment_im_hardsoftfilter_new, container, false);
      initUI(rootView);
      setupActionBar();
      setProperties();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return rootView;
  }

  private void getPopupDataFromServer() {

    gettingJobNo();
    gettingSiteName();
  }

  private void gettingZoneName(){
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        AppUtils.showProgressDialog(mActivity,getString(R.string.lbl_loading),true);
        ZoneEntity zoneEntity = new ZoneEntity();
        zoneEntity.setOpco(mOpco);
        zoneEntity.setContractNo(mJobNo);
        mGetComplaintPopupService.getZoneListData(zoneEntity, this);
      }
    }
  }

  private void gettingBuildingName(BuildingDetailsRequest buildingDetailsRequest) {
    mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        mGetComplaintPopupService.getBuildingDetailsData(this, buildingDetailsRequest);
      } else AppUtils.showDialog(mActivity, getString(R.string.lbl_alert_network_not_available));
    }
  }

  private void gettingJobNo() {
    mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
        mGetComplaintPopupService.getContractListData(this);
      }
      else new ContractDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
    }
  }

  private void gettingSiteName() {
    mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        mGetComplaintPopupService.getSiteAreaData(this);
      }
      else new SiteAreaDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
    }
  }

  private void initUI(View rootView) {

    try {
      cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
      tv_lbl_jobno = (TextView) rootView.findViewById(R.id.tv_lbl_job_no);
      tv_lbl_site = (TextView) rootView.findViewById(R.id.tv_lbl_site);
      tv_lbl_from_date = (TextView) rootView.findViewById(R.id.tv_lbl_from_date);
      tv_lbl_to_date = (TextView) rootView.findViewById(R.id.tv_lbl_to_date);
      tv_zone_area=(TextView)rootView.findViewById(R.id.tv_zone_area);
      tv_building=(TextView)rootView.findViewById(R.id.tv_building);
      tv_select_building=(TextView)rootView.findViewById(R.id.tv_select_building);
      tv_select_zone_area=(TextView)rootView.findViewById(R.id.tv_select_zone_area);
      tv_zone_area=(TextView)rootView.findViewById(R.id.tv_zone_area);
      tv_select_jobno = (TextView) rootView.findViewById(R.id.tv_select_job_no);
      tv_select_site = (TextView) rootView.findViewById(R.id.tv_select_site);
      tv_select_from_date = (TextView) rootView.findViewById(R.id.tv_select_from_date);
      tv_select_to_date = (TextView) rootView.findViewById(R.id.tv_select_to_date);

      btnSearch = (Button) rootView.findViewById(R.id.btnSearch);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setupActionBar() {
    mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
    mToolbar.setTitle(mTitle);
    mActivity.setSupportActionBar(mToolbar);
    mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    tv_lbl_jobno.setText(Html.fromHtml(getString(R.string.lbl_job_no) + AppUtils.mandatory));
    tv_lbl_site.setText(Html.fromHtml(getString(R.string.lbl_site_name) + AppUtils.mandatory));
    tv_zone_area.setText(Html.fromHtml(getString(R.string.lbl_zone_area) + AppUtils.mandatory));
    tv_lbl_from_date.setText(Html.fromHtml(getString(R.string.lbl_from_date) + AppUtils.mandatory));
    tv_lbl_to_date.setText(Html.fromHtml(getString(R.string.lbl_to_date) + AppUtils.mandatory));
    tv_building.setText(Html.fromHtml(getString(R.string.lbl_building) + AppUtils.mandatory));

    btnSearch.setOnClickListener(_OnClickListener);
    tv_select_from_date.setOnClickListener(_OnClickListener);
    tv_select_to_date.setOnClickListener(_OnClickListener);
    tv_select_site.setOnClickListener(_OnClickListener);
    tv_select_jobno.setOnClickListener(_OnClickListener);
    tv_select_building.setOnClickListener(_OnClickListener);
    tv_select_zone_area.setOnClickListener(_OnClickListener);
  }

  private void gotoFragmentList() {
    Log.d(TAG, "gotoFragmentViewComplaintList");
    Bundle data = new Bundle();
    data.putParcelable(AppUtils.ARGS_REACTIVE_MAINTENANCE_DASHBOARD_REQUEST, hardSoftRequest);
    data.putString(AppUtils.ARGS_IM_SERVICES_Page_TITLE, mTitle);
    Fragment _fragment;
    if (mTitle.contains(getString(R.string.title_ppm_request_verification))) {
      _fragment = new Fragment_IM_PPMList();
    } else {
      _fragment = new Fragment_IM_HardSoftList();
    }

    _fragment.setArguments(data);
    FragmentTransaction _transaction = mManager.beginTransaction();
    _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
    _transaction.replace(R.id.frame_container, _fragment, Utils.TAG_FRAGMENT_IM_HARDSOFTLIST);
    _transaction.addToBackStack(Utils.TAG_FRAGMENT_IM_HARDSOFTLIST);
    _transaction.commit();
    clearSelectedValue();
  }

  private void clearSelectedValue() {
    mStrSiteCode = null;
    mReportTypes = null;
    mFromDate = null;
    mToDate = null;
    btnSearch.setEnabled(true);
  }

  private void submitForm() {

    try {
      String msgErr = "";
      if (mJobNo == null) {
        AppUtils.setErrorBg(tv_select_jobno, true);
        msgErr = msgErr + "\n" + getString(R.string.lbl_select_jobno);
      } else AppUtils.setErrorBg(tv_select_jobno, false);

      if (mStrSiteCode == null) {
        AppUtils.setErrorBg(tv_select_site, true);
        msgErr = getString(R.string.lbl_select_site);
      } else AppUtils.setErrorBg(tv_select_site, false);

      if (mZoneCode == null) {
        AppUtils.setErrorBg(tv_select_zone_area, true);
        msgErr = getString(R.string.lbl_select_zone);
      } else AppUtils.setErrorBg(tv_select_zone_area, false);

      if (mZoneCode == null) {
        AppUtils.setErrorBg(tv_select_building, true);
        msgErr = getString(R.string.lbl_select_building);
      } else AppUtils.setErrorBg(tv_select_building, false);

      if (mFromDate == null) {
        AppUtils.setErrorBg(tv_select_from_date, true);
        msgErr = msgErr + "\n" + getString(R.string.lbl_select_from_date);
      } else AppUtils.setErrorBg(tv_select_from_date, false);
      if (mToDate == null) {
        AppUtils.setErrorBg(tv_select_to_date, true);
        msgErr = msgErr + "\n" + getString(R.string.lbl_select_to_date);
      } else AppUtils.setErrorBg(tv_select_to_date, false);

      if (msgErr != "") {
        AppUtils.showDialog(mActivity, "Please fill all the mandatory fields.");
        return;
      }

      hardSoftRequest = new HardSoftRequest();
      hardSoftRequest.setComplaintSite(mStrSiteCode);
      hardSoftRequest.setJobNumber(mJobNo);
      hardSoftRequest.setFromDate(mFromDate);
      hardSoftRequest.setToDate(mToDate);
      hardSoftRequest.setZoneCode(mZoneCode);
      hardSoftRequest.setBuildingCode(mBuildingCode);

      hardSoftRequest.setBuildTag(mBuildingCode);
      hardSoftRequest.setJobNo(mJobNo);
      hardSoftRequest.setStartIndex("0");
      hardSoftRequest.setLimit("10");

      AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
      gotoFragmentList();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getDate(int mode) {
    Log.d(TAG, "getDate");
    try {
      mModeDate = mode;
      if (mModeDate == AppUtils.MODE_FROMDATE) {
        AppUtils.datePickerDialog(mActivity, this, null, new Date());
      } else {
        if (mFromDate != null) {
          AppUtils.datePickerDialog(
              mActivity,
              this,
              new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(mFromDate),
              new Date());
        } else {
          AppUtils.datePickerDialog(mActivity, this, null, new Date());
        }
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void onDateReceivedSuccess(String strDate) {
    if (mModeDate == AppUtils.MODE_FROMDATE) {
      mFromDate = strDate;
      SessionManager.saveSession("fromDataSave",mFromDate,mActivity);
      tv_select_from_date.setText(strDate);
      tv_select_from_date.setTypeface(font.getHelveticaBold());
      AppUtils.setErrorBg(tv_select_from_date, false);
    } else if (mModeDate == AppUtils.MODE_TODATE) {
      mToDate = strDate;
      SessionManager.saveSession("toDataSave",mToDate,mActivity);
      tv_select_to_date.setText(strDate);
      tv_select_to_date.setTypeface(font.getHelveticaBold());
      AppUtils.setErrorBg(tv_select_to_date, false);
    }
  }

  public void getJobNo() {
    Log.d(TAG, "getDate");
    try {
      String[] strArrayJobNo = new String[listJobNo.size()];
      for (int i = 0; i < listJobNo.size(); i++) {
        strArrayJobNo[i] = listJobNo.get(i).getJobNo();
      }

      new MaterialDialog.Builder(mActivity)
          .title(R.string.lbl_select_job_no)
          .items(strArrayJobNo)
          .itemsCallbackSingleChoice(
              -1,
              new MaterialDialog.ListCallbackSingleChoice() {
                @Override
                public boolean onSelection(
                    MaterialDialog dialog, View view, int which, CharSequence text) {
                  if (which >= 0) {
                    for (ContractEntity item : listJobNo) {
                      if (text.toString()
                          .equals(item.getJobNo() + "-" + item.getJobDescription())) {
                        mJobNo = item.getJobNo();
                      }
                    }
                    tv_select_jobno.setText(text.toString());
                    tv_select_jobno.setTypeface(font.getHelveticaBold());
                    AppUtils.setErrorBg(tv_select_jobno, false);
                  } else {
                    mJobNo = null;
                    tv_select_jobno.setText("");
                    tv_select_jobno.setHint(getString(R.string.lbl_select_jobno));
                    AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                    AppUtils.setErrorBg(tv_select_jobno, true);
                  }
                  AppUtils.closeInput(cl_main);
                  return true;
                }
              })
          .canceledOnTouchOutside(false)
          .positiveText(R.string.lbl_done)
          .negativeText(R.string.lbl_close)
          .show();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getZoneArea() {
    try {
      String[] strArrayJobNo = new String[zoneArea.size()];
      for (int i = 0; i < zoneArea.size(); i++) {
        strArrayJobNo[i] = zoneArea.get(i).getZoneName();
      }
      new MaterialDialog.Builder(mActivity)
              .title(R.string.lbl_select_zone)
              .items(strArrayJobNo)
              .itemsCallbackSingleChoice(
                      -1,
                      new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(
                                MaterialDialog dialog, View view, int which, CharSequence text) {
                          if (which >= 0) {
                            for (ZoneEntity item : zoneArea) {
                              if (text.toString()
                                      .equals(item.getZoneName())) {
                                mZoneCode = item.getZoneCode();
                                BuildingDetailsRequest buildingDetailsRequest=new BuildingDetailsRequest(item.getOpco(),
                                        item.getContractNo(),item.getZoneCode(),null,null);
                                gettingBuildingName(buildingDetailsRequest);
                              }
                            }
                            tv_select_zone_area.setText(text.toString());
                            tv_select_zone_area.setTypeface(font.getHelveticaBold());
                            AppUtils.setErrorBg(tv_select_zone_area, false);
                          } else {
                            mZoneCode = null;
                            tv_select_zone_area.setText("");
                            tv_select_zone_area.setHint(getString(R.string.lbl_select_zone));
                            AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                            AppUtils.setErrorBg(tv_select_zone_area, true);
                          }
                          AppUtils.closeInput(cl_main);
                          return true;
                        }
                      })
              .canceledOnTouchOutside(false)
              .positiveText(R.string.lbl_done)
              .negativeText(R.string.lbl_close)
              .show();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getBuildingName() {
    try {
      String[] strArrayJobNo = new String[buildingName.size()];
      for (int i = 0; i < buildingName.size(); i++) {
        strArrayJobNo[i] = buildingName.get(i).getBuildingName();
      }
      new MaterialDialog.Builder(mActivity)
              .title(R.string.lbl_select_building)
              .items(strArrayJobNo)
              .itemsCallbackSingleChoice(
                      -1,
                      new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(
                                MaterialDialog dialog, View view, int which, CharSequence text) {
                          if (which >= 0) {
                            for (BuildingDetailsEntity item : buildingName) {
                              if (text.toString()
                                      .equals(item.getBuildingName())) {
                                mBuildingCode = item.getBuildingCode();
                              }
                            }
                            tv_select_building.setText(text.toString());
                            tv_select_building.setTypeface(font.getHelveticaBold());
                            AppUtils.setErrorBg(tv_select_building, false);
                          } else {
                            mBuildingCode = null;
                            tv_select_building.setText("");
                            tv_select_building.setHint(getString(R.string.lbl_select_building));
                            AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                            AppUtils.setErrorBg(tv_select_building, true);
                          }
                          AppUtils.closeInput(cl_main);
                          return true;
                        }
                      })
              .canceledOnTouchOutside(false)
              .positiveText(R.string.lbl_done)
              .negativeText(R.string.lbl_close)
              .show();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getSiteName() {
    Log.d(TAG, "getSiteName");
    try {
      String[] strArraySiteName = new String[listSiteArea.size()];
      for (int i = 0; i < listSiteArea.size(); i++) {
        strArraySiteName[i] = listSiteArea.get(i).getSiteDescription();
      }
      new MaterialDialog.Builder(mActivity)
          .title(R.string.lbl_select_site)
          .items(strArraySiteName)
          .itemsCallbackSingleChoice(
              -1,
              new MaterialDialog.ListCallbackSingleChoice() {
                @Override
                public boolean onSelection(
                    MaterialDialog dialog, View view, int which, CharSequence text) {
                  if (which >= 0) {
                    for (SiteAreaEntity item : listSiteArea) {
                      if (text.toString().equals(item.getSiteDescription())) {
                        mStrSiteCode = item.getSiteCode();
                        mJobNo = item.getJobNo();
                        mOpco = item.getOpco();
                      }
                    }
                    tv_select_site.setText(text.toString());
                    gettingZoneName();
                    tv_select_site.setTypeface(font.getHelveticaBold());
                    AppUtils.setErrorBg(tv_select_site, false);
                  } else {
                    mStrSiteCode = null;
                    tv_select_site.setText("");
                    tv_select_site.setHint(getString(R.string.lbl_select_site));
                    AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                    AppUtils.setErrorBg(tv_select_site, true);
                  }
                  AppUtils.closeInput(cl_main);
                  return true;
                }
              })
          .canceledOnTouchOutside(false)
          .positiveText(R.string.lbl_done)
          .negativeText(R.string.lbl_close)
          .show();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    menu.findItem(R.id.action_logout).setVisible(false);
    menu.findItem(R.id.action_home).setVisible(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_home:
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
  public void onLogComplaintSiteReceivedSuccess(List<SiteAreaEntity> siteAreaList, int mode) {
    Log.d(TAG, "onLogComplaintSiteReceivedSuccess");
    AppUtils.hideProgressDialog();
    listSiteArea = siteAreaList;
    if (mode == AppUtils.MODE_SERVER)
      new SiteAreaDbInitializer(mActivity, listSiteArea, this).execute(AppUtils.MODE_INSERT);
  }

  @Override
  public void onLogComplaintSiteReceivedFailure(String strErr, int mode) {
    Log.d(TAG, "onLogComplaintSiteReceivedFailure");
    AppUtils.hideProgressDialog();
    if (mode == AppUtils.MODE_LOCAL) {
      AppUtils.showDialog(mActivity, strErr);
    }
    else new SiteAreaDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
  }

  @Override
  public void onContractReceivedSuccess(List<ContractEntity> contractList, int mode) {
    Log.d(TAG, "onContractReceivedSuccess");
    AppUtils.hideProgressDialog();
    listJobNo = contractList;
    if (mode == AppUtils.MODE_SERVER)
      new ContractDbInitializer(mActivity, listJobNo, this).execute(AppUtils.MODE_INSERT);
  }

  @Override
  public void onContractReceivedFailure(String strErr, int mode) {
    Log.d(TAG, "onContractReceivedFailure");
    AppUtils.hideProgressDialog();
    if (mode == AppUtils.MODE_LOCAL) {
      AppUtils.showDialog(mActivity, strErr);
    }
    else new ContractDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
  }

  @Override
  public void onBuildingDetailsReceivedSuccess(List<BuildingDetailsEntity> buildingDetailsEntities, int mode) {
    AppUtils.hideProgressDialog();
    buildingName=buildingDetailsEntities;
  }

  @Override
  public void onBuildingDetailsReceivedFailure(String strErr, int mode) {
    AppUtils.hideProgressDialog();
  }

  @Override
  public void onZoneReceivedSuccess(List<ZoneEntity> zoneList, int mode) {
    zoneArea=zoneList;
    AppUtils.hideProgressDialog();
  }

  @Override
  public void onZoneReceivedFailure(String strErr, int mode) {
    AppUtils.hideProgressDialog();
  }
}
