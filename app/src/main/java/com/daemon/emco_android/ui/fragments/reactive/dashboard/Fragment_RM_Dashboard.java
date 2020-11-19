package com.daemon.emco_android.ui.fragments.reactive.dashboard;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.GetLogComplaintPopupRepository;
import com.daemon.emco_android.repository.remote.ReactiveDashboardService;
import com.daemon.emco_android.ui.components.FilterableListDialog;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.dbhelper.ReportTypesDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.SiteAreaDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ZoneDbInitializer;
import com.daemon.emco_android.repository.db.entity.ReportTypesEntity;
import com.daemon.emco_android.repository.db.entity.SiteAreaEntity;
import com.daemon.emco_android.repository.db.entity.ZoneEntity;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.listeners.DatePickerDialogListener;
import com.daemon.emco_android.listeners.ReactiveDashboard_Listener;
import com.daemon.emco_android.listeners.ReportTypesListener;
import com.daemon.emco_android.listeners.SiteListener;
import com.daemon.emco_android.listeners.ZoneListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.model.request.MultiSearchRequest;
import com.daemon.emco_android.model.response.DashboardPieData;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ConnectivityStatus;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.daemon.emco_android.utils.AppUtils.checkInternet;

/** Created by subbu on 17/8/17. */
public class Fragment_RM_Dashboard extends Fragment
    implements ZoneListener,
        SiteListener,
        DatePickerDialogListener,
        ReportTypesListener,
        ReactiveDashboard_Listener {
  private static final String TAG = Fragment_RM_Dashboard.class.getSimpleName();

  // Offline storage
  private static ReportTypesDbInitializer mReportTypesDb;
  // root view of fragment
  View rootView = null;
  private AppCompatActivity mActivity;
  private Context mContext;
  private Font font = App.getInstance().getFontInstance();
  private SharedPreferences mPreferences;
  private SharedPreferences.Editor mEditor;
  private FragmentManager mManager;
  private CoordinatorLayout cl_main;
  private TextView tv_lbl_site, tv_lbl_zone, tv_lbl_from_date, tv_lbl_reporttypes, tv_lbl_to_date;
  private TextView tv_select_site,tv_toolbar_title,
      tv_select_reporttypes,
      tv_select_zone,
      tv_select_from_date,
      tv_select_to_date;
  private Button btnSearch;
  private Toolbar mToolbar;
  private int mModeDate;
  private MultiSearchRequest pieChartRequest;
  private GetLogComplaintPopupRepository mGetComplaintPopupService;
  /** Global variables for post log complaint data */
  private Login mUserData;

  private String mStrLoginData = null, mFromDate = null, mToDate = null;
  private String mStrEmployeeId = null;
  private String mReportTypes = null; // response data from category data
  private String mStrSiteCode = null, mContractNo = null, mContractTitle = null;
  private String mZoneCode = "ALL", mZoneName = "ALL", mOpco = null;
  private List<ZoneEntity> listZone = new ArrayList<>();
  private List<SiteAreaEntity> listSiteArea = new ArrayList<>();
  private List<ReportTypesEntity> listReportTypes = new ArrayList<>();
  View.OnClickListener _OnClickListener =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          switch (v.getId()) {
            case R.id.btnEnter:
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
            case R.id.tv_select_zone:
              getZone();
              break;
            case R.id.tv_select_reporttypes:
              getReportTypes();
              break;
            default:
              break;
          }
        }
      };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate");

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
        mStrEmployeeId =
            mStrEmployeeId != null ? mUserData.getEmployeeId() : mUserData.getUserName();
      }

      mManager = mActivity.getSupportFragmentManager();
      font = App.getInstance().getFontInstance();
      if (mActivity.getCurrentFocus() != null) {
        InputMethodManager imm =
            (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
      }

      mGetComplaintPopupService =
          new GetLogComplaintPopupRepository(
              mActivity,
              new EmployeeIdRequest(
                  mUserData.getEmployeeId(),
                  mUserData.getEmployeeId() == null ? mUserData.getUserName() : null,
                  null,
                  null));
      /** Initializing db */
      mReportTypesDb = new ReportTypesDbInitializer(this);
      getPopupDataFromServer();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    try {
      rootView = (View) inflater.inflate(R.layout.fragment_rm_dashboard, container, false);
      initUI(rootView);
      setProperties();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return rootView;
  }

  private void getPopupDataFromServer() {

    gettingSiteName();
    gettingReportTypes();
  }

  private void gettingReportTypes() {

      if (checkInternet(getContext())) {
        mGetComplaintPopupService.getAllReportTypesListData(this);
      } else
        mReportTypesDb.populateAsync(AppDatabase.getAppDatabase(mActivity), AppUtils.MODE_GETALL);

  }

  private void gettingZoneName() {

      if (checkInternet(getContext())) {
         AppUtils.showProgressDialog(mActivity,getString(R.string.lbl_loading),true);
        ZoneEntity zoneEntity = new ZoneEntity();
        zoneEntity.setOpco(mOpco);
        zoneEntity.setContractNo(mContractNo);
        mGetComplaintPopupService.getZoneListData(zoneEntity, this);
      }
      else new ZoneDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);

  }

  private void gettingSiteName() {

      if (checkInternet(getContext())) {
        AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
        mGetComplaintPopupService.getSiteAreaData(this);
      }
      else new SiteAreaDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);

  }

  private void initUI(View rootView) {
    try {
      cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
      tv_lbl_from_date = (TextView) rootView.findViewById(R.id.tv_lbl_from_date);
      tv_lbl_site = (TextView) rootView.findViewById(R.id.tv_lbl_site);
      tv_lbl_zone = (TextView) rootView.findViewById(R.id.tv_lbl_zone);
      tv_lbl_to_date = (TextView) rootView.findViewById(R.id.tv_lbl_to_date);
      tv_lbl_reporttypes = (TextView) rootView.findViewById(R.id.tv_lbl_reporttypes);

      tv_select_from_date = (TextView) rootView.findViewById(R.id.tv_select_from_date);
      tv_select_to_date = (TextView) rootView.findViewById(R.id.tv_select_to_date);
      tv_select_site = (TextView) rootView.findViewById(R.id.tv_select_site);
      tv_select_reporttypes = (TextView) rootView.findViewById(R.id.tv_select_reporttypes);
      tv_select_zone = (TextView) rootView.findViewById(R.id.tv_select_zone);

      btnSearch = (Button) rootView.findViewById(R.id.btnEnter);
      setupActionBar();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setupActionBar() {

    mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
    tv_toolbar_title=(TextView)mToolbar.findViewById(R.id.tv_toolbar_title);
    tv_toolbar_title.setText(getString(R.string.lbl_dashboard));
   // mToolbar.setTitle(getResources().getString(R.string.lbl_dashboard));
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


    tv_lbl_site.setText(Html.fromHtml(getString(R.string.lbl_site) + AppUtils.mandatory));
    tv_lbl_reporttypes.setText(
        Html.fromHtml(getString(R.string.lbl_reporttypes) + AppUtils.mandatory));
    tv_lbl_from_date.setText(Html.fromHtml(getString(R.string.lbl_from_date) + AppUtils.mandatory));
    tv_lbl_to_date.setText(Html.fromHtml(getString(R.string.lbl_to_date) + AppUtils.mandatory));


    btnSearch.setOnClickListener(_OnClickListener);

    tv_select_from_date.setOnClickListener(_OnClickListener);
    tv_select_to_date.setOnClickListener(_OnClickListener);
    tv_select_site.setOnClickListener(_OnClickListener);
    tv_select_reporttypes.setOnClickListener(_OnClickListener);
    tv_select_zone.setOnClickListener(_OnClickListener);
  }

  private void gotoFragmentPieChart(ArrayList<DashboardPieData> response) {
    Log.d(TAG, "gotoFragmentViewComplaintList");
    Bundle data = new Bundle();
    data.putString(AppUtils.ARGS_BIECHART_ZONETITLE, mContractTitle + " & " + mZoneName);
    data.putParcelableArrayList(AppUtils.ARGS_REACTIVE_MAINTENANCE_DASHBOARD_DATA, response);
    data.putParcelable(AppUtils.ARGS_REACTIVE_MAINTENANCE_DASHBOARD_REQUEST, pieChartRequest);
    Fragment _fragment = new Fragment_RM_PieChart();
    _fragment.setArguments(data);
    FragmentTransaction _transaction = mManager.beginTransaction();
    _transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
    _transaction.replace(R.id.frame_container, _fragment, Utils.TAG_DASHBOARDPIECHARTS);
    _transaction.addToBackStack(Utils.TAG_DASHBOARDPIECHARTS);
    _transaction.commit();
    clearSelectedValue();
  }

  private void clearSelectedValue() {
    mStrSiteCode = null;
    mZoneCode = "ALL";
    mReportTypes = null;
    mFromDate = null;
    mToDate = null;
    btnSearch.setEnabled(true);
  }

  private void submitForm() {
    Log.d(TAG, "submitForm");
    try {
      String msgErr = "";
      if (mStrSiteCode == null) {
        AppUtils.setErrorBg(tv_select_site, true);
        msgErr = getString(R.string.lbl_select_site);
      } else AppUtils.setErrorBg(tv_select_site, false);

      if (mZoneCode == null) {
        AppUtils.setErrorBg(tv_select_zone, true);
        msgErr = msgErr + "\n" + getString(R.string.lbl_select_zone);
      } else AppUtils.setErrorBg(tv_select_zone, false);

      if (mReportTypes == null) {
        AppUtils.setErrorBg(tv_select_reporttypes, true);
        msgErr = msgErr + "\n" + getString(R.string.lbl_building_property_detail);
      } else AppUtils.setErrorBg(tv_select_reporttypes, false);

      if (mFromDate == null) {
        AppUtils.setErrorBg(tv_select_from_date, true);
        msgErr = msgErr + "\n" + getString(R.string.lbl_select_from_date);
      } else AppUtils.setErrorBg(tv_select_from_date, false);
      if (mToDate == null) {
        AppUtils.setErrorBg(tv_select_to_date, true);
        msgErr = msgErr + "\n" + getString(R.string.lbl_select_to_date);
      } else AppUtils.setErrorBg(tv_select_to_date, false);

      if (msgErr != "") {
        AppUtils.showDialog(mActivity, "Please select values in Mandatory field");
        return;
      }


      pieChartRequest = new MultiSearchRequest();
      pieChartRequest.setOpco(mOpco);
      pieChartRequest.setSiteCode(mStrSiteCode);
      pieChartRequest.setContractNo(mContractNo);
      pieChartRequest.setFromDate(mFromDate);
      pieChartRequest.setToDate(mToDate);
      pieChartRequest.setReportType(mReportTypes);
      pieChartRequest.setZoneCode(mZoneCode);
      if(mUserData.getUserType().equalsIgnoreCase(AppUtils.CUSTOMER)){
        pieChartRequest.setUserCode(mStrEmployeeId);
      }else{
        pieChartRequest.setEmployeeId(mStrEmployeeId);
      }


      if(ConnectivityStatus.isConnected(mActivity)){
        AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
        btnSearch.setEnabled(false);
        new ReactiveDashboardService(mActivity, this, this).GetDashboardData(pieChartRequest);
      }else {
        Toast.makeText(mActivity,(R.string.msg_no_data_found_in_local_db),Toast.LENGTH_SHORT).show();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getDate(int mode) {

    try {
      mModeDate = mode;
      SimpleDateFormat originalFormat = new SimpleDateFormat("dd");
      Date date = originalFormat.parse(String.valueOf(mModeDate));
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
    Log.d(TAG, "onDateReceivedSuccess " + strDate);
    if (mModeDate == AppUtils.MODE_FROMDATE) {
      mFromDate = strDate;
      tv_select_from_date.setText(strDate);
      tv_select_from_date.setTypeface(font.getHelveticaBold());
      AppUtils.setErrorBg(tv_select_from_date, false);
    } else if (mModeDate == AppUtils.MODE_TODATE) {
      mToDate = strDate;
      tv_select_to_date.setText(strDate);
      tv_select_to_date.setTypeface(font.getHelveticaBold());
      AppUtils.setErrorBg(tv_select_to_date, false);
    }
  }

  public void getSiteName() {

    try {
      ArrayList strArraySiteName = new ArrayList();
      for (SiteAreaEntity entity : listSiteArea) {
        strArraySiteName.add(entity.getSiteDescription());
      }
      FilterableListDialog.create(
              mActivity,
              getString(R.string.lbl_select_site),
              strArraySiteName,
              new FilterableListDialog.OnListItemSelectedListener() {
                @Override
                public void onItemSelected(String text) {
                  for (SiteAreaEntity item : listSiteArea) {
                    if (text.equals(item.getSiteDescription())) {
                      mStrSiteCode = item.getSiteCode();
                      mContractNo = item.getJobNo();
                      mContractTitle = item.getJobDescription();
                      mOpco = item.getOpco();
                    }
                  }
                  gettingZoneName();
                  tv_select_site.setText(text.toString());
                  tv_select_site.setTypeface(font.getHelveticaBold());
                  AppUtils.setErrorBg(tv_select_site, false);
                }
              })
          .show();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getZone() {

    try {
      ArrayList strArrayZone = new ArrayList();
      for (int i = 0; i < listZone.size(); i++) {
        strArrayZone.add(listZone.get(i).getZoneName());
      }
      strArrayZone.add(0, "ALL");
      FilterableListDialog.create(
              mActivity,
              getString(R.string.lbl_select_zone),
              strArrayZone,
              new FilterableListDialog.OnListItemSelectedListener() {
                @Override
                public void onItemSelected(String text) {
                  AppUtils.setErrorBg(tv_select_zone, false);
                  mZoneCode = text;
                  if (text.toString().equals("ALL")) {
                    tv_select_zone.setText(mZoneCode);
                    tv_select_zone.setTypeface(font.getHelveticaBold());
                  } else {
                    tv_select_zone.setText(text.toString());
                    tv_select_zone.setTypeface(font.getHelveticaBold());
                    for (ZoneEntity item : listZone) {
                      if (mZoneCode.equals(item.getZoneName())) {
                        mZoneCode = item.getZoneCode();
                        mZoneName = item.getZoneName();
                      }
                    }
                  }
                }
              })
          .show();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getReportTypes() {

    try {
      ArrayList strArrayReportTypes = new ArrayList();
      for (int i = 0; i < listReportTypes.size(); i++) {
        strArrayReportTypes.add(listReportTypes.get(i).getReportType());
      }
      FilterableListDialog.create(
              mActivity,
              getString(R.string.lbl_select_reporttypes),
              strArrayReportTypes,
              new FilterableListDialog.OnListItemSelectedListener() {
                @Override
                public void onItemSelected(String text) {
                  AppUtils.setErrorBg(tv_select_reporttypes, false);
                  mReportTypes = text.toString();
                  tv_select_reporttypes.setText(text.toString());
                  tv_select_reporttypes.setTypeface(font.getHelveticaBold());
                }
              })
          .show();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void onLogComplaintSiteReceivedSuccess(List<SiteAreaEntity> siteAreaList, int mode) {
    Log.d(TAG, "onLogComplaintSiteReceivedSuccess");
    AppUtils.hideProgressDialog();
    listSiteArea = siteAreaList;
    if (mode == AppUtils.MODE_SERVER) {
      AppUtils.hideProgressDialog();
      new SiteAreaDbInitializer(mActivity, listSiteArea, this).execute(AppUtils.MODE_INSERT);
    }
  }

  @Override
  public void onZoneReceivedSuccess(List<ZoneEntity> zoneList, int mode) {
    Log.d(TAG, "onZoneReceivedSuccess");
    AppUtils.hideProgressDialog();
    listZone = zoneList;
    if (mode == AppUtils.MODE_SERVER) {
      new ZoneDbInitializer(mActivity, listZone, this).execute(AppUtils.MODE_INSERT);
    }
  }

  @Override
  public void onReportTypesReceivedSuccess(List<ReportTypesEntity> reportTypesEntities, int mode) {
    Log.d(TAG, "onReportTypesReceivedSuccess");
    AppUtils.hideProgressDialog();
    listReportTypes = reportTypesEntities;
    if (mode == AppUtils.MODE_SERVER) {
      AppUtils.hideProgressDialog();
      mReportTypesDb.populateAsync(
          AppDatabase.getAppDatabase(mActivity), reportTypesEntities, AppUtils.MODE_INSERT);
    }
  }

  @Override
  public void onReportTypesDbDataSingleByreportSRL(ReportTypesEntity reportTypesEntity) {}

  @Override
  public void onReportTypesReceivedFailure(String strErr, int mode) {
    Log.d(TAG, "onReportTypesReceivedFailure");
    AppUtils.hideProgressDialog();
    if (mode == AppUtils.MODE_LOCAL) {
      AppUtils.showDialog(mActivity, strErr);
    } else
      mReportTypesDb.populateAsync(AppDatabase.getAppDatabase(mActivity), AppUtils.MODE_GETALL);
  }

  @Override
  public void onLogComplaintSiteReceivedFailure(String strErr, int mode) {
    Log.d(TAG, "onLogComplaintSiteReceivedFailure");
    AppUtils.hideProgressDialog();
    if (mode == AppUtils.MODE_LOCAL) {
      AppUtils.showDialog(mActivity, strErr);
    } else new SiteAreaDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
  }

  @Override
  public void onZoneReceivedFailure(String strErr, int mode) {
    Log.d(TAG, "onZoneReceivedFailure");
    AppUtils.hideProgressDialog();
    if (mode == AppUtils.MODE_LOCAL) {
      AppUtils.showDialog(mActivity, strErr);
    }
    else new ZoneDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
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
  public void onDashboardDataReceivedSuccess(ArrayList<DashboardPieData> responses, int mode) {
    Log.d(TAG, "onDashboardDataReceivedSuccess");
    AppUtils.hideProgressDialog();
    if (responses.size() > 0) gotoFragmentPieChart(responses);
    else Log.d(TAG, "onComplaintReceivedSuccess data Not found");
  }

  @Override
  public void onDashboardDataReceivedFailure(String strErr, int mode) {
    Log.d(TAG, "onDashboardDataReceivedFailure");
    AppUtils.hideProgressDialog();
    AppUtils.showDialog(mActivity, strErr);
    btnSearch.setEnabled(true);
  }

}
