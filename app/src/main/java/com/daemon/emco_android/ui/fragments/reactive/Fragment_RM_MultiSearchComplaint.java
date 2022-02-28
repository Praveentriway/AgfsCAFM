package com.daemon.emco_android.ui.fragments.reactive;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
import com.daemon.emco_android.repository.remote.GetSearchComplaintService;
import com.daemon.emco_android.ui.components.FilterableListDialog;
import com.daemon.emco_android.repository.db.dbhelper.SiteAreaDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ZoneDbInitializer;
import com.daemon.emco_android.repository.db.entity.BuildingDetailsEntity;
import com.daemon.emco_android.repository.db.entity.MultiSearchComplaintEntity;
import com.daemon.emco_android.repository.db.entity.SingleSearchComplaintEntity;
import com.daemon.emco_android.repository.db.entity.SiteAreaEntity;
import com.daemon.emco_android.repository.db.entity.ZoneEntity;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.ui.fragments.reactive.viewcomplaint.Fragment_RM_ViewComplaintList;
import com.daemon.emco_android.listeners.BuildingDetailsListener;
import com.daemon.emco_android.listeners.DatePickerDialogListener;
import com.daemon.emco_android.listeners.SearchComplaintListener;
import com.daemon.emco_android.listeners.SiteListener;
import com.daemon.emco_android.listeners.ZoneListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.BuildingDetailsRequest;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.model.request.MultiSearchRequest;
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

/**
 * Created by subbu on 18/7/17.
 */
public class Fragment_RM_MultiSearchComplaint extends Fragment
        implements ZoneListener,
        SiteListener,
        DatePickerDialogListener,
        SearchComplaintListener,
        BuildingDetailsListener {
    private static final String TAG = Fragment_RM_MultiSearchComplaint.class.getSimpleName();
    private static String mRowsExpected = "10", mStartIndex = "0";
    // root view of fragment
    View rootView = null;
    private AppCompatActivity mActivity;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private CoordinatorLayout cl_main;
    private TextView tv_lbl_site, tv_lbl_zone, tv_lbl_from_date, tv_lbl_property, tv_lbl_to_date;
    private TextView tv_select_site,
            tv_select_property,
            tv_select_zone,
            tv_select_from_date,
            tv_select_to_date;
    private Button btnSearchComplaint;
    private Toolbar mToolbar;
    private int mModeDate;
    private GetLogComplaintPopupRepository mGetComplaintPopupService;
    private GetSearchComplaintService mGetSearchData;
    private ArrayList<MultiSearchComplaintEntity> listMultiSearch = new ArrayList<>();
    private String mStrLoginData = null, mFromDate = null, mToDate = null;
    private String mStrEmployeeId = null;
    private String mBuildingCode = null; // response data from category data
    private String mStrSiteCode = null, mContractNo = null;
    private String mZoneCode = null, mOpco = null;
    private List<ZoneEntity> listZone = new ArrayList<>();
    private List<SiteAreaEntity> listSiteArea = new ArrayList<>();
    private List<BuildingDetailsEntity> listBuildingDetails = new ArrayList<>();
    private Login mUserData;
    private MultiSearchRequest _request;
    View.OnClickListener _OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick");
                    switch (v.getId()) {
                        case R.id.btnSearchComplaint:
                            submitForm();
                            // gotoFragmentViewComplaintList();
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
                        case R.id.tv_select_property:
                            getBuildingDetails();
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

            setHasOptionsMenu(true);
            setRetainInstance(false);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            mStrLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mStrLoginData != null) {
                Gson gson = new Gson();
                mUserData = gson.fromJson(mStrLoginData, Login.class);
                mStrEmployeeId = mUserData.getEmployeeId();
                if (mStrEmployeeId == null) {
                    mStrEmployeeId = mUserData.getUserName();
                }

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
            getPopupDataFromServer();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        try {
            rootView =
                    (View) inflater.inflate(R.layout.fragment_multi_search_complaints, container, false);
            initUI(rootView);
            setProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    private void getPopupDataFromServer() {
        Log.d(TAG, "getPopupDataFromServer");
        gettingSiteName();

    }

    private void gettingBuildingDetails(BuildingDetailsRequest buildingDetailsRequest) {

            if ( checkInternet(getContext())) {
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                mGetComplaintPopupService.getBuildingDetailsData(this, buildingDetailsRequest);
            }


    }

    private void gettingZoneName() {

            if (checkInternet(getContext())) {
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), true);
                ZoneEntity zoneEntity = new ZoneEntity();
                zoneEntity.setOpco(mOpco);
                zoneEntity.setContractNo(mContractNo);
                mGetComplaintPopupService.getZoneListData(zoneEntity, this);
            } else new ZoneDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);

    }

    private void gettingSiteName() {

            if ( checkInternet(getContext())) {
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                mGetComplaintPopupService.getSiteAreaData(this);
            } else new SiteAreaDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);

    }

    @Override
    public void onDestroy() {
        // AppDatabase.destroyInstance();
        super.onDestroy();
    }

    private void initUI(View rootView) {
        Log.d(TAG, "initUI");
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            tv_lbl_from_date = (TextView) rootView.findViewById(R.id.tv_lbl_from_date);
            tv_lbl_site = (TextView) rootView.findViewById(R.id.tv_lbl_site);
            tv_lbl_zone = (TextView) rootView.findViewById(R.id.tv_lbl_zone);
            tv_lbl_to_date = (TextView) rootView.findViewById(R.id.tv_lbl_to_date);
            tv_lbl_property = (TextView) rootView.findViewById(R.id.tv_lbl_property);

            tv_select_from_date = (TextView) rootView.findViewById(R.id.tv_select_from_date);
            tv_select_to_date = (TextView) rootView.findViewById(R.id.tv_select_to_date);
            tv_select_site = (TextView) rootView.findViewById(R.id.tv_select_site);
            tv_select_property = (TextView) rootView.findViewById(R.id.tv_select_property);
            tv_select_zone = (TextView) rootView.findViewById(R.id.tv_select_zone);

            btnSearchComplaint = (Button) rootView.findViewById(R.id.btnSearchComplaint);
            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.lbl_multi_search_complaint));
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

        tv_lbl_site.setText(Html.fromHtml(getString(R.string.lbl_site) + AppUtils.mandatory));
        tv_lbl_zone.setText(Html.fromHtml(getString(R.string.lbl_zone) + AppUtils.mandatory));
        tv_lbl_property.setText(
                Html.fromHtml(getString(R.string.lbl_building_property_detail) + AppUtils.mandatory));
        tv_lbl_from_date.setText(Html.fromHtml(getString(R.string.lbl_from_date) + AppUtils.mandatory));
        tv_lbl_to_date.setText(Html.fromHtml(getString(R.string.lbl_to_date) + AppUtils.mandatory));

        btnSearchComplaint.setOnClickListener(_OnClickListener);

        tv_select_from_date.setOnClickListener(_OnClickListener);
        tv_select_to_date.setOnClickListener(_OnClickListener);
        tv_select_site.setOnClickListener(_OnClickListener);
        tv_select_property.setOnClickListener(_OnClickListener);
        tv_select_zone.setOnClickListener(_OnClickListener);
    }

    private void gotoFragmentViewComplaintList(String noOfRows) {
        Log.d(TAG, "gotoFragmentViewComplaintList");
        Bundle data = new Bundle();
        data.putParcelableArrayList(AppUtils.ARGS_MULTISEARC_COMPLAINT_LIST, listMultiSearch);
        data.putParcelable(AppUtils.ARGS_MULTISEARC_COMPLAINT_REQUEST, _request);
        data.putString(AppUtils.ARGS_MULTISEARC_COMPLAINT_TOTALPAGE, noOfRows);
        Fragment _fragment = new Fragment_RM_ViewComplaintList();
        _fragment.setArguments(data);
        FragmentTransaction _transaction = mManager.beginTransaction();
        _transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        _transaction.replace(R.id.frame_container, _fragment, Utils.TAG_VIEW_COMPLAINTLIST);
        _transaction.addToBackStack(Utils.TAG_VIEW_COMPLAINTLIST);
        _transaction.commit();

        clearSelectedValue();
    }

    private void clearSelectedValue() {
        mStrSiteCode = null;
        mZoneCode = null;
        mBuildingCode = null;
        mFromDate = null;
        mToDate = null;
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

            if (mBuildingCode == null) {
                AppUtils.setErrorBg(tv_select_property, true);
                msgErr = msgErr + "\n" + getString(R.string.lbl_building_property_detail);
            } else AppUtils.setErrorBg(tv_select_property, false);

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
            if(ConnectivityStatus.isConnected(mActivity)){
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                _request = new MultiSearchRequest();
                if (mUserData.getUserType().equalsIgnoreCase(AppUtils.CUSTOMER)) {
                    _request.setUserCode(mStrEmployeeId);
                } else {
                    _request.setEmployeeId(mStrEmployeeId);
                }
                _request.setFromDate(mFromDate);
                _request.setSiteCode(mStrSiteCode);
                _request.setToDate(mToDate);
                _request.setBuildingCode(mBuildingCode);
                _request.setContractNo(mContractNo);
                _request.setContractNo(mContractNo);
                _request.setOpco(mOpco);
                _request.setZoneCode(mZoneCode);
                _request.setStartIndex(mStartIndex);
                _request.setRowsExpected(mRowsExpected);
                mGetSearchData = new GetSearchComplaintService(mActivity, this, this);
                mGetSearchData.getMultiSearchData(_request, false);
            }
            else {
                Toast.makeText(mActivity,(R.string.msg_no_data_found_in_local_db),Toast.LENGTH_SHORT).show();
            }

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
        Log.d(TAG, "getSiteName");
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
        Log.d(TAG, "getZone");
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

                            tv_select_zone.setText(text.toString());
                            tv_select_zone.setTypeface(font.getHelveticaBold());
                            for (ZoneEntity item : listZone) {
                                if (mZoneCode.equals(item.getZoneName())) {
                                    mZoneCode = item.getZoneCode();
                                      /*  mContractNo = item.getContractNo();
                                        mOpco = item.getOpco();*/
                                    // clear listBuildingDetails
                                    listBuildingDetails.clear();
                                    mBuildingCode = null;
                                    tv_select_property.setText("");
                                    tv_select_property.setHint(
                                            getString(R.string.lbl_building_property_detail));
                                    if (mUserData.getUserType().equalsIgnoreCase(AppUtils.CUSTOMER)) {
                                        gettingBuildingDetails(
                                                new BuildingDetailsRequest(
                                                        item.getOpco(), item.getContractNo(), item.getZoneCode(), mStrEmployeeId, null));
                                    } else {
                                        gettingBuildingDetails(
                                                new BuildingDetailsRequest(
                                                        item.getOpco(), item.getContractNo(), item.getZoneCode(), null, mStrEmployeeId));
                                    }
                                } else if (mZoneCode.equalsIgnoreCase("ALL")) {
                                    listBuildingDetails.clear();
                                    mBuildingCode = null;
                                    tv_select_property.setText("");
                                    tv_select_property.setHint(
                                            getString(R.string.lbl_building_property_detail));
                                    if (mUserData.getUserType().equalsIgnoreCase(AppUtils.CUSTOMER)) {
                                        gettingBuildingDetails(
                                                new BuildingDetailsRequest(
                                                        item.getOpco(), item.getContractNo(), "ALL", mStrEmployeeId, null));
                                    } else {
                                        gettingBuildingDetails(
                                                new BuildingDetailsRequest(
                                                        item.getOpco(), item.getContractNo(), "ALL", null, mStrEmployeeId));
                                    }
                                    break;
                                }
                            }
                            //  }
                        }
                    })
                    .show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getBuildingDetails() {
        Log.d(TAG, "getBuildingDetails");
        try {

            ArrayList strArrayBuildingDetails = new ArrayList();
            for (int i = 0; i < listBuildingDetails.size(); i++) {
                strArrayBuildingDetails.add(listBuildingDetails.get(i).getBuildingName());
            }
            strArrayBuildingDetails.add(0, "ALL");
            FilterableListDialog.create(
                    mActivity,
                    getString(R.string.lbl_select_building_property_detail),
                    strArrayBuildingDetails,
                    new FilterableListDialog.OnListItemSelectedListener() {
                        @Override
                        public void onItemSelected(String text) {
                            AppUtils.setErrorBg(tv_select_property, false);
                            mBuildingCode = text.toString();
                            if (text.toString().equals("ALL")) {
                                tv_select_property.setText(mBuildingCode);
                                tv_select_property.setTypeface(font.getHelveticaBold());
                            } else {
                                for (BuildingDetailsEntity item : listBuildingDetails) {
                                    if (mBuildingCode.equals(item.getBuildingName())) {
                                        mBuildingCode = item.getBuildingCode();
                                    }
                                }
                                tv_select_property.setText(text.toString());
                                tv_select_property.setTypeface(font.getHelveticaBold());
                            }
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

        listSiteArea = siteAreaList;
        AppUtils.hideProgressDialog();
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
            AppUtils.hideProgressDialog();
            new ZoneDbInitializer(mActivity, listZone, this).execute(AppUtils.MODE_INSERT);
        }
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
        } else new ZoneDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
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
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplaintItemClicked(int position) {
    }

    @Override
    public void onComplaintReceivedSuccess(
            ArrayList<MultiSearchComplaintEntity> searchComplaintEntityList, String noOfRows) {
        Log.d(TAG, "onComplaintReceivedSuccess");
        AppUtils.hideProgressDialog();
        listMultiSearch = searchComplaintEntityList;
        if (listMultiSearch.size() > 0) gotoFragmentViewComplaintList(noOfRows);
        else Log.d(TAG, "onComplaintReceivedSuccess data Not found");
    }

    @Override
    public void onSingleComplaintReceivedSuccess(SingleSearchComplaintEntity searchComplaintEntity) {
        Log.d(TAG, "onSingleComplaintReceivedSuccess");
        AppUtils.hideProgressDialog();
    }

    @Override
    public void onComplaintReceivedFailure(String strErr) {
        Log.d(TAG, "onComplaintReceivedFailure");
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity, strErr);
    }

    @Override
    public void onBuildingDetailsReceivedSuccess(
            List<BuildingDetailsEntity> buildingDetailsEntities, int mode) {
        Log.d(TAG, "onBuildingDetailsReceivedSuccess");
        listBuildingDetails = buildingDetailsEntities;
        AppUtils.hideProgressDialog();
    }

    @Override
    public void onBuildingDetailsReceivedFailure(String strErr, int mode) {
        Log.d(TAG, "onBuildingDetailsReceivedFailure");
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity, strErr);
    }

}
