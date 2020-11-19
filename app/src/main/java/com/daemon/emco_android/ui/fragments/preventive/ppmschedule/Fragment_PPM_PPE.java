package com.daemon.emco_android.ui.fragments.preventive.ppmschedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.ViewComplaintPPEListAdapter;
import com.daemon.emco_android.repository.remote.PpeService;
import com.daemon.emco_android.ui.components.SimpleDividerItemDecoration;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.dbhelper.PPEFetchSaveDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.PPENamesDbInitializer;
import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.daemon.emco_android.repository.db.entity.PPENameEntity;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.listeners.PpeListener;
import com.daemon.emco_android.listeners.RiskeAssListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.PpmScheduleDetails;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.FetchPpeForPpmReq;
import com.daemon.emco_android.model.request.RiskAssListRequest;
import com.daemon.emco_android.model.response.Object;
import com.daemon.emco_android.model.response.ObjectPPM;
import com.daemon.emco_android.model.response.PpmFeedBackResponse;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.checkInternet;
import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;

/**
 * Created by Daemonsoft on 7/18/2017.
 */

public class Fragment_PPM_PPE extends Fragment implements PpeListener,RiskeAssListener {
    private static final String TAG = Fragment_PPM_PPE.class.getSimpleName();
    public Bundle mSavedInstanceState;
    private Font font = App.getInstance().getFontInstance();
    private AppCompatActivity mActivity;
    private FragmentManager mManager;
    private CoordinatorLayout cl_main;
    private RecyclerView recyclerView;
    private TextView tv_lbl_ppe_required, tv_lbl_yes, tv_lbl_no, tv_lbl_nr;
    private LinearLayoutManager mLayoutManager;
    private TextView text_view_loading_message;
    private LinearLayout layout_loading_message;
    private TextView text_view_message, text_view_empty;
    private LinearLayout layout_loading;
    private RelativeLayout layout_empty;
    private ViewComplaintPPEListAdapter adapter;
    private List<PPENameEntity> mNameList = new ArrayList<>();
    private List<PPEFetchSaveEntity> fetchlist = new ArrayList<>();
    private List<PPEFetchSaveEntity> savelist = new ArrayList<>();
    private Bundle mArgs;
    private PpmScheduleDetails scheduleDetails;
    private SharedPreferences mPreferences;
    private PPENamesDbInitializer ppeNamesDbInitializer;
    private PPEFetchSaveDbInitializer ppeFetchSaveDbInitializer;
    private PpeService ppeService;
    private String mStrEmpId = null;
    private String mLoginData = null;
    private Toolbar mToolbar;
    private PpmScheduleDocBy ppmScheduleDocBy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.d(TAG, "onCreate");
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mManager = mActivity.getSupportFragmentManager();
            mSavedInstanceState = savedInstanceState;
            ppeNamesDbInitializer = new PPENamesDbInitializer(this);
            ppeFetchSaveDbInitializer = new PPEFetchSaveDbInitializer(this);

            mArgs = getArguments();
            ppeService = new PpeService(mActivity, this);
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);

            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }

            if (mArgs != null && mArgs.size() > 0) {
                showProgressDialog(mActivity, "Loading...", false);
                ppmScheduleDocBy = mArgs.getParcelable(AppUtils.ARG_PPE_PPM);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_receive_complaint_ppe, container, false);
        Log.d(TAG, "onCreateView");
        initView(rootView);
        return rootView;
    }

    private void getParcelableData() {
        Log.d(TAG, "getParcelableData");
        if (mArgs != null) {
           // if (mArgs.containsKey(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS)) {
                scheduleDetails = mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);
          //  showProgressDialog(mActivity, "Loading...", false);
                if (checkInternet(getContext())) {
                    showLoadingSearch();
                    RiskAssListRequest riskAssListResponse=new RiskAssListRequest(ppmScheduleDocBy.getCompanyCode()
                    ,ppmScheduleDocBy.getJobNo(),ppmScheduleDocBy.getPpmNo(),ppmScheduleDocBy.getChkCode(),"");
                    new PpeService(mActivity, this).getSavePpePpm(riskAssListResponse);
                } else {

                }
        }
    }

    public void initView(View view) {
        Log.d(TAG, "initView");
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

            tv_lbl_ppe_required = (TextView) view.findViewById(R.id.tv_lbl_ppe_required);
            tv_lbl_yes = (TextView) view.findViewById(R.id.tv_lbl_yes);
            tv_lbl_no = (TextView) view.findViewById(R.id.tv_lbl_no);
            tv_lbl_nr = (TextView) view.findViewById(R.id.tv_lbl_nr);

            text_view_loading_message = (TextView) view.findViewById(R.id.text_view_message);
            layout_loading_message = (LinearLayout) view.findViewById(R.id.layout_loading);

            layout_loading = (LinearLayout) view.findViewById(R.id.layout_loading);
            layout_empty = (RelativeLayout) view.findViewById(R.id.layout_empty);
            text_view_empty = (TextView) view.findViewById(R.id.text_view_empty);
            text_view_message = (TextView) view.findViewById(R.id.text_view_message);

            FloatingActionButton   btn_save=(FloatingActionButton) view.findViewById(R.id.btn_save);

            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onSubmitClicked();

                }
            });

            AppUtils.closeInput(cl_main);
            getParcelableData();
            setupActionBar();
            setProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onSubmitClicked(){

        if (adapter.isFetchdata()) {
            updatePpeList(adapter.getPPEFetchSaveEntity());
        } else {
            savePpeList(adapter.getPPENameEntity());
        }

    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        TextView  tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getResources().getString(R.string.lbl_ppe)+" for PPM");
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.onBackPressed();
            }
        });
    }

    public void setProperties() {

        Log.d(TAG, "setProperties");
        try {
            setManager();
            tv_lbl_ppe_required.setTypeface(font.getHelveticaRegular());
            tv_lbl_yes.setTypeface(font.getHelveticaRegular());
            tv_lbl_no.setTypeface(font.getHelveticaRegular());
            tv_lbl_nr.setTypeface(font.getHelveticaRegular());

            text_view_empty.setTypeface(font.getHelveticaRegular());
            text_view_message.setTypeface(font.getHelveticaRegular());

            text_view_loading_message.setTypeface(font.getHelveticaRegular());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setManager() {

        Log.d(TAG, "setManager");
        try {
            mLayoutManager = new LinearLayoutManager(mActivity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(mActivity));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void savePpeList(List<PPENameEntity> ppeNameEntities) {

        Log.d(TAG, "getPpeNameList");
        try {
            savelist = new ArrayList<>();
            for (int i = 0; i < ppeNameEntities.size(); i++) {
                PPENameEntity entity = ppeNameEntities.get(i);

                PPEFetchSaveEntity request = new PPEFetchSaveEntity();
                request.setCompanyCode(scheduleDetails.getCompCode());
                request.setSiteCode(scheduleDetails.getAssetCode());
                request.setWorkType(scheduleDetails.getWorkOrderNo());
                request.setComplainNumber(scheduleDetails.getNatureCode());
                request.setJobNumber(scheduleDetails.getContractNo());
                request.setPpeRefrenceNumber("12345");
                request.setCreatedUser(mStrEmpId);
                request.setModifiedBy(mStrEmpId);

                request.setPpeCode(entity.getCode());
                request.setPpeName(entity.getName());
                request.setPpeUsed(entity.getPpeUsed());

                //update later
                request.setMode(AppUtils.MODE_LOCAL);
                request.setId(scheduleDetails.getAssetBarCode() + entity.getCode());
                savelist.add(request);

            }

            if (checkInternet(getContext())) {
                AppUtils.showProgressDialog(mActivity, "Saving PPE...", false);
                ppeService.savePpeDataPPM(savelist);
            } else
                ppeFetchSaveDbInitializer.populateAsync(AppDatabase.getAppDatabase(mActivity), savelist, AppUtils.MODE_INSERT_SINGLE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updatePpeList(List<PPEFetchSaveEntity> ppeSaveEntities) {

        Log.d(TAG, "getPpeNameList");
        try {
            savelist = new ArrayList<>();
            for (int i = 0; i < ppeSaveEntities.size(); i++) {
                PPEFetchSaveEntity entity = ppeSaveEntities.get(i);

                PPEFetchSaveEntity request = new PPEFetchSaveEntity();
                request.setCompanyCode(ppmScheduleDocBy.getCompanyCode());
                //request.setSiteCode(scheduleDetails.getAssetCode());
                request.setWorkType("P");
               // request.setComplainNumber(ppmScheduleDocBy.getNatureCode());
                request.setJobNumber(ppmScheduleDocBy.getJobNo());
                request.setPpeRefrenceNumber(ppmScheduleDocBy.getPpmNo());
                request.setCreatedUser(mStrEmpId);
                request.setModifiedBy(mStrEmpId);

                request.setPpeCode(entity.getPpeCode());
                request.setPpeName(entity.getPpeName());
                request.setPpeUsed(entity.getPpeUsed());

                //update later
                request.setMode(AppUtils.MODE_LOCAL);
                request.setId(ppmScheduleDocBy.getAssetBarCode() + entity.getPpeCode());
                savelist.add(request);
            }

            if (checkInternet(getContext())) {
                AppUtils.showProgressDialog(mActivity, "Updating PPE...", false);
                ppeService.savePpeDataPPM(savelist);
            } else
                ppeFetchSaveDbInitializer.populateAsync(AppDatabase.getAppDatabase(mActivity), savelist, AppUtils.MODE_INSERT_SINGLE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setPpeList(boolean isFetchData) {

        Log.d(TAG, "setPpeList");
        try {
            Log.w(TAG, fetchlist.size() + " mList : " + mNameList.size());
            if ((isFetchData ? fetchlist.size() : mNameList.size()) > 0) {
                adapter = new ViewComplaintPPEListAdapter(mActivity, mNameList, fetchlist);
                adapter.setListener(this);
                /*if (scheduleDetails != null) {
                    if (scheduleDetails.getComplaintStatus() != null) {
                        adapter.setCompleted(scheduleDetails.getComplaintStatus().equals(AppUtils.COMPLETED));
                    }
                }*/
                adapter.setFetchData(isFetchData);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                showList();
            } else {
                showEmptyView(getString(R.string.msg_ppelist_empty));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void showList() {

        if (recyclerView != null) recyclerView.setVisibility(View.VISIBLE);
        if (layout_loading != null) layout_loading.setVisibility(View.GONE);
        if (layout_empty != null) layout_empty.setVisibility(View.GONE);

    }

    public void showLoadingSearch() {

        Log.d(TAG, "showLoadingSearch");
        try {
            if (layout_empty != null) layout_empty.setVisibility(View.GONE);
            if (layout_loading != null) layout_loading.setVisibility(View.VISIBLE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void showEmptyView(String Str_Msg) {

        Log.d(TAG, "showEmptyView");
        try {
            if (text_view_empty != null) {
                text_view_empty.setText(Str_Msg);
            } else AppUtils.showDialog(mActivity, Str_Msg);
            if (layout_empty != null) layout_empty.setVisibility(View.VISIBLE);
            if (layout_loading != null) layout_loading.setVisibility(View.GONE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
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
    public void onPPESaveSuccess(String strMsg, int mode) {
        try {
            Log.d(TAG, "onPPESaveSuccess ");
            AppUtils.hideProgressDialog();
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity)
                    .content(strMsg)
                    .positiveText(R.string.lbl_okay)
                    .stackingBehavior(StackingBehavior.ADAPTIVE)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            ppeFetchSaveDbInitializer.populateAsync(AppDatabase.getAppDatabase(mActivity), savelist, AppUtils.MODE_INSERT);
                            mManager.popBackStack();
                        }
                    });

            MaterialDialog dialog = builder.build();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPPESaveClicked(List<PPENameEntity> ppeNameEntities, List<PPEFetchSaveEntity> ppeSaveEntities, boolean isFetchdata) {
        if (isFetchdata) {
            updatePpeList(ppeSaveEntities);
        } else {
            savePpeList(ppeNameEntities);
        }
    }

    @Override
    public void onPPEFetchListSuccess(List<PPEFetchSaveEntity> ppeSaveEntities, int mode) {
        Log.d(TAG, "onPPEFetchSaveSuccess ");
        AppUtils.hideProgressDialog();
        fetchlist.clear();
        for (PPEFetchSaveEntity ppeFetchSaveEntity : ppeSaveEntities) {
            ppeFetchSaveEntity.setId(ppeFetchSaveEntity.getComplainNumber() + ppeFetchSaveEntity.getPpeCode());
            ppeFetchSaveEntity.setMode(AppUtils.MODE_SERVER);
            fetchlist.add(ppeFetchSaveEntity);
        }
        if (fetchlist.size() > 0) {
            setPpeList(true);
        } else if (mNameList.size() > 0) {
            setPpeList(false);
        } else showEmptyView(getString(R.string.msg_request_error_occurred));

        if (mode == AppUtils.MODE_SERVER) {
            ppeFetchSaveDbInitializer.populateAsync(AppDatabase.getAppDatabase(mActivity), fetchlist, AppUtils.MODE_INSERT);
        }
    }


    @Override
    public void onPPENameListReceived(List<PPENameEntity> ppeNameEntities, int mode) {
        Log.d(TAG, "onPPENamesReceived ");
        AppUtils.hideProgressDialog();
        mNameList = ppeNameEntities;
        if (fetchlist.size() > 0) {
            setPpeList(true);
        } else if (mNameList.size() > 0) {
            setPpeList(false);
        } else showEmptyView(getString(R.string.msg_request_error_occurred));
        if (mode == AppUtils.MODE_SERVER) {
            ppeNamesDbInitializer.populateAsync(AppDatabase.getAppDatabase(mActivity), mNameList, AppUtils.MODE_INSERT);
        }
    }

    @Override
    public void onPPEFetchListFailure(String strErr, int mode) {

        Log.d(TAG, "onPPEFetchListFailure ");

        FetchPpeForPpmReq fetchPpeForPpmReq = new FetchPpeForPpmReq();
            fetchPpeForPpmReq.setCheckListCode(ppmScheduleDocBy.getChkCode());
            fetchPpeForPpmReq.setPpeType("PE");
            ppeService.fetchPPEDataPPM(fetchPpeForPpmReq);

    }

    @Override
    public void onPPEFetchListFailure2(String strErr, int mode) {

        Log.d(TAG, "onPPEFetchListFailure 2");

        AppUtils.hideProgressDialog();

        AppUtils.showDialogToFinish(getActivity(),"PPE",strErr);

    }

    @Override
    public void onPPESaveFailure(String strErr, int mode) {
        Log.d(TAG, "onPPESaveFailure ");
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity, strErr);
    }

    @Override
    public void onPPENameListFailure(String strErr, int mode) {
        Log.d(TAG, "onPPENameListFailure ");
        if (fetchlist.size() < 0) showEmptyView(strErr);
    }

    @Override
    public void onListAssSuccess(List<Object> login) {
        //not use for this class
    }

    @Override
    public void onPPMDataSuccess(List<ObjectPPM> loginppm) {
        //not use for this class
    }

    @Override
    public void onPPMDataSuccessPpmFeedBack(PpmFeedBackResponse loginppm) {

    }

    @Override
    public void onSaveDataSuccess(String commonResponse) {
        //not use for this class
    }

    @Override
    public void onListAssFailure(String login) {

    }
}
