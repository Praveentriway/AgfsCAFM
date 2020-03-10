package com.daemon.emco_android.ui.fragments.reactive.receieve_complaints;

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
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.listeners.PpeListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.checkInternet;

/**
 * Created by Daemonsoft on 7/18/2017.
 */

public class Fragment_RM_PPE extends Fragment implements PpeListener {
    private static final String TAG = Fragment_RM_PPE.class.getSimpleName();
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
    private TextView text_view_message, text_view_empty, tv_toolbar_title;
    private LinearLayout layout_loading;
    private RelativeLayout layout_empty;
    private ViewComplaintPPEListAdapter adapter;
    private List<PPENameEntity> mNameList = new ArrayList<>();
    private List<PPEFetchSaveEntity> fetchlist = new ArrayList<>();
    private List<PPEFetchSaveEntity> savelist = new ArrayList<>();
    private Bundle mArgs;
    private ReceiveComplaintViewEntity receiveComplaintViewEntity;
    private SharedPreferences mPreferences;
    private PPENamesDbInitializer ppeNamesDbInitializer;
    private PPEFetchSaveDbInitializer ppeFetchSaveDbInitializer;
    private PpeService ppeService;
    private String mStrEmpId = null;
    private String mLoginData = null;
    private Toolbar mToolbar;
    private FloatingActionButton btn_save;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_receive_complaint_ppe, container, false);
        initView(rootView);
        return rootView;
    }

    private void getParcelableData() {

        if (mArgs != null) {
            if (mArgs.containsKey(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS)) {
                receiveComplaintViewEntity = mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);

                if (checkInternet(getContext())) {
                    showLoadingSearch();
                    ppeService.fetchPPEData(receiveComplaintViewEntity.getComplaintNumber());

                    ppeService.getPpeNamesData();
                } else {
                    ppeNamesDbInitializer.populateAsync(AppDatabase.getAppDatabase(mActivity), AppUtils.MODE_GETALL);
                    ppeFetchSaveDbInitializer.populateAsync(AppDatabase.getAppDatabase(mActivity), receiveComplaintViewEntity.getComplaintNumber(), AppUtils.MODE_GET);
                }
            }
        }
    }

    public void initView(View view) {

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
            btn_save=(FloatingActionButton) view.findViewById(R.id.btn_save);
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

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.lbl_ppe)+" for Reactive");
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mActivity.getSupportActionBar().setTitle(getResources().getString(R.string.lbl_ppe));
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
                request.setCompanyCode(receiveComplaintViewEntity.getOpco());
                request.setSiteCode(receiveComplaintViewEntity.getComplaintSite());
                request.setWorkType(receiveComplaintViewEntity.getWorkType());
                request.setComplainNumber(receiveComplaintViewEntity.getComplaintNumber());
                request.setJobNumber(receiveComplaintViewEntity.getJobNumber());
                request.setPpeRefrenceNumber("12345");
                request.setCreatedUser(mStrEmpId);
                request.setModifiedBy(mStrEmpId);

                request.setPpeCode(entity.getCode());
                request.setPpeName(entity.getName());
                request.setPpeUsed(entity.getPpeUsed());

                //update later
                request.setMode(AppUtils.MODE_LOCAL);
                request.setId(receiveComplaintViewEntity.getComplaintNumber() + entity.getCode());
                savelist.add(request);
            }

            if (checkInternet(getContext())) {
                AppUtils.showProgressDialog(mActivity, "Saving PPE...", false);
                ppeService.savePpeData(savelist);
            } else
                ppeFetchSaveDbInitializer.populateAsync(AppDatabase.getAppDatabase(mActivity), savelist, AppUtils.MODE_INSERT_SINGLE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updatePpeList(List<PPEFetchSaveEntity> ppeSaveEntities) {

        try {
            savelist = new ArrayList<>();
            for (int i = 0; i < ppeSaveEntities.size(); i++) {
                PPEFetchSaveEntity entity = ppeSaveEntities.get(i);

                PPEFetchSaveEntity request = new PPEFetchSaveEntity();
                request.setCompanyCode(receiveComplaintViewEntity.getOpco());
                request.setSiteCode(receiveComplaintViewEntity.getComplaintSite());
                request.setWorkType(receiveComplaintViewEntity.getWorkType());
                request.setComplainNumber(receiveComplaintViewEntity.getComplaintNumber());
                request.setJobNumber(receiveComplaintViewEntity.getJobNumber());
                request.setPpeRefrenceNumber("12345");
                request.setCreatedUser(mStrEmpId);
                request.setModifiedBy(mStrEmpId);

                request.setPpeCode(entity.getPpeCode());
                request.setPpeName(entity.getPpeName());
                request.setPpeUsed(entity.getPpeUsed());

                //update later
                request.setMode(AppUtils.MODE_LOCAL);
                request.setId(receiveComplaintViewEntity.getComplaintNumber() + entity.getPpeCode());
                savelist.add(request);
            }

            if (checkInternet(getContext())) {
                AppUtils.showProgressDialog(mActivity, "Updating PPE...", false);
                ppeService.savePpeData(savelist);
            } else
                ppeFetchSaveDbInitializer.populateAsync(AppDatabase.getAppDatabase(mActivity), savelist, AppUtils.MODE_INSERT_SINGLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPpeList(boolean isFetchData) {

        try {
            Log.w(TAG, fetchlist.size() + " mList : " + mNameList.size());
            if ((isFetchData ? fetchlist.size() : mNameList.size()) > 0) {
                adapter = new ViewComplaintPPEListAdapter(mActivity, mNameList, fetchlist);
                adapter.setListener(this);
                if (receiveComplaintViewEntity != null) {
                    if (receiveComplaintViewEntity.getComplaintStatus() != null) {
                        adapter.setCompleted(receiveComplaintViewEntity.getComplaintStatus().equals(AppUtils.COMPLETED));
                    }
                }
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

        try {
            if (layout_empty != null) layout_empty.setVisibility(View.GONE);
            if (layout_loading != null) layout_loading.setVisibility(View.VISIBLE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void showEmptyView(String Str_Msg) {
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
                Fragment _fragment = new MainLandingUI();
                FragmentTransaction _transaction = mManager.beginTransaction();
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

   public void onSubmitClicked(){

       if (adapter.isFetchdata()) {
           updatePpeList(adapter.getPPEFetchSaveEntity());
       } else {
           savePpeList(adapter.getPPENameEntity());
       }

   }

   public void onPPESaveClicked(List<PPENameEntity> ppeNameEntities, List<PPEFetchSaveEntity> ppeSaveEntities, boolean isFetchdata){

    }


    @Override
    public void onPPEFetchListSuccess(List<PPEFetchSaveEntity> ppeSaveEntities, int mode) {
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
        AppUtils.hideProgressDialog();
        if (mNameList.size() > 0) {
            setPpeList(false);
        } else showEmptyView(strErr);
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
    public void onPPEFetchListFailure2(String strErr, int mode) {}
}
