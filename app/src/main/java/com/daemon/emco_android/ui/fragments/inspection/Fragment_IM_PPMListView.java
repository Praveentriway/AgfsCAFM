package com.daemon.emco_android.ui.fragments.inspection;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.ReceiveComplaintViewService;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.listeners.ReceivecomplaintView_Listener;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.dbhelper.RCRespondDbInitializer;
import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.ReceiveComplaintViewRequest;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.Utils.TAG_IM_PPM_CHECK_LIST;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_RESPOND;

/**
 * Created by vikram on 14/7/17.
 */

public class Fragment_IM_PPMListView extends Fragment implements ReceivecomplaintView_Listener {
    private static final String TAG = Fragment_IM_PPMListView.class.getSimpleName();
    public Bundle mSavedInstanceState;
    private AppCompatActivity mActivity;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private Bundle mArgs;
    private String mTitle;
    private CoordinatorLayout cl_main;

    private TextView tv_lbl_complaint_ref_no, tv_lbl_job_no, tv_lbl_nature, tv_lbl_site_name, tv_lbl_location, tv_lbl_asset_type, tv_lbl_bar_code, tv_lbl_client_remark;
    private TextView tv_complaint_ref_no, tv_job_no, tv_nature, tv_site_name, tv_location, tv_asset_type, tv_bar_code, tv_client_remark;

    private TextView tv_lbl_make, tv_lbl_model, tv_lbl_date, tv_lbl_complaint, tv_lbl_defectsfound, tv_lbl_workdone, tv_lbl_material_used, tv_lbl_tech_remarks;
    private TextView tv_make, tv_model, tv_date, tv_complaint, tv_defectsfound, tv_workdone, tv_material_used, tv_tech_remarks;

    private Button btn_next,btn_ppm_checklist;
    private Toolbar mToolbar;
    private View rootView;
    private ReceiveComplaintItemEntity receiveComplaintItemEntity;
    private int mSelectedPosition = 0;
    private ReceiveComplaintViewEntity complaintViewEntity;
    private AssetDetailsEntity assetDetailsEntity;
    View.OnClickListener _OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick");
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

            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }

            if (mArgs != null && mArgs.size() > 0) {
                mTitle = mArgs.getString(AppUtils.ARGS_IM_SERVICES_Page_TITLE);

                receiveComplaintItemEntity = mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS);
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
            rootView = inflater.inflate(R.layout.fragment_im_ppmlistview, container, false);
            initUI(rootView);
            setupActionBar();
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

            tv_lbl_complaint_ref_no = (TextView) rootView.findViewById(R.id.tv_lbl_complaint_ref_no);
            tv_lbl_job_no = (TextView) rootView.findViewById(R.id.tv_lbl_job_no);
            tv_lbl_nature = (TextView) rootView.findViewById(R.id.tv_lbl_nature);
            tv_lbl_site_name = (TextView) rootView.findViewById(R.id.tv_lbl_site_name);
            tv_lbl_location = (TextView) rootView.findViewById(R.id.tv_lbl_location);
            tv_lbl_asset_type = (TextView) rootView.findViewById(R.id.tv_lbl_asset_type);
            tv_lbl_bar_code = (TextView) rootView.findViewById(R.id.tv_lbl_bar_code);
            tv_lbl_client_remark = (TextView) rootView.findViewById(R.id.tv_lbl_client_remark);

            tv_complaint_ref_no = (TextView) rootView.findViewById(R.id.tv_complaint_ref_no);
            tv_job_no = (TextView) rootView.findViewById(R.id.tv_job_no);
            tv_nature = (TextView) rootView.findViewById(R.id.tv_nature);
            tv_site_name = (TextView) rootView.findViewById(R.id.tv_site_name);
            tv_location = (TextView) rootView.findViewById(R.id.tv_location);
            tv_asset_type = (TextView) rootView.findViewById(R.id.tv_asset_type);
            tv_bar_code = (TextView) rootView.findViewById(R.id.tv_bar_code);
            tv_client_remark = (TextView) rootView.findViewById(R.id.tv_client_remark);

            tv_lbl_make = (TextView) rootView.findViewById(R.id.tv_lbl_make);
            tv_lbl_model = (TextView) rootView.findViewById(R.id.tv_lbl_model);
            tv_lbl_date = (TextView) rootView.findViewById(R.id.tv_lbl_date);
            tv_lbl_complaint = (TextView) rootView.findViewById(R.id.tv_lbl_complaint);
            tv_lbl_defectsfound = (TextView) rootView.findViewById(R.id.tv_lbl_defectsfound);
            tv_lbl_workdone = (TextView) rootView.findViewById(R.id.tv_lbl_workdone);
            tv_lbl_material_used = (TextView) rootView.findViewById(R.id.tv_lbl_material_used);
            tv_lbl_tech_remarks = (TextView) rootView.findViewById(R.id.tv_lbl_tech_remarks);

            tv_make = (TextView) rootView.findViewById(R.id.tv_make);
            tv_model = (TextView) rootView.findViewById(R.id.tv_model);
            tv_date = (TextView) rootView.findViewById(R.id.tv_date);
            tv_complaint = (TextView) rootView.findViewById(R.id.tv_complaint);
            tv_defectsfound = (TextView) rootView.findViewById(R.id.tv_defectsfound);
            tv_workdone = (TextView) rootView.findViewById(R.id.tv_workdone);
            tv_material_used = (TextView) rootView.findViewById(R.id.tv_material_used);
            tv_tech_remarks = (TextView) rootView.findViewById(R.id.tv_tech_remarks);
            btn_next = (Button) rootView.findViewById(R.id.btn_next);
            btn_ppm_checklist= (Button) rootView.findViewById(R.id.btn_ppm_checklist);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

        btn_ppm_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCheckListPage();
            }
        });
        btn_next.setOnClickListener(_OnClickListener);

        if (mSavedInstanceState != null) {
            Log.d(TAG, " mSavedInstanceState : " + mSavedInstanceState);
            receiveComplaintItemEntity = mSavedInstanceState.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS);
            mSelectedPosition = mSavedInstanceState.getInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION);
            complaintViewEntity = mSavedInstanceState.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);
            assetDetailsEntity = mSavedInstanceState.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ASSET_DETAILS);
        }
        AppUtils.closeInput(cl_main);
    }

    private void gotoNextPage() {
        mSavedInstanceState = getSavedState();
        Bundle data = new Bundle();
        data.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, receiveComplaintItemEntity);
        data.putString(AppUtils.ARGS_IM_SERVICES_Page_TITLE, mTitle);
        data.putInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION, mSelectedPosition);
        Fragment_IM_HardSoftListComment fragment = new Fragment_IM_HardSoftListComment();
        fragment.setArguments(data);
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_RESPOND);
        fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_RESPOND);
        fragmentTransaction.commit();
    }

    private void gotoCheckListPage() {
        mSavedInstanceState = getSavedState();
        Bundle data = new Bundle();
        data.putString(AppUtils.ARGS_IM_SERVICES_Page_TITLE, mTitle);
        data.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, complaintViewEntity);
        Fragment_IM_HardsoftPPMChecklist fragment = new Fragment_IM_HardsoftPPMChecklist();
        fragment.setArguments(data);
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_container, fragment, TAG_IM_PPM_CHECK_LIST);
        fragmentTransaction.addToBackStack(TAG_IM_PPM_CHECK_LIST);
        fragmentTransaction.commit();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

}