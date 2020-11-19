package com.daemon.emco_android.ui.fragments.preventive.ppmschedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.RiskAssessmentListAdapter;
import com.daemon.emco_android.repository.remote.RiskAssessmentService;
import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.listeners.RiskeAssListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.RiskAssListRequest;
import com.daemon.emco_android.model.request.SaveAssesEntity;
import com.daemon.emco_android.model.response.Object;
import com.daemon.emco_android.model.response.ObjectPPM;
import com.daemon.emco_android.model.response.PpmFeedBackResponse;
import com.daemon.emco_android.utils.AppUtils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;

/**
 * Created by vikram on 2/3/18.
 */

public class Fragment_PM_Risk_List extends Fragment implements RiskeAssListener {
    private static String MODULE = "F_PM_Rist_list";
    private static String TAG = "";
    private AppCompatActivity mActivity;
    private FragmentManager mManager;
    private Bundle mSavedInstanceState;
    private Bundle mArgs;
    private SharedPreferences mPreferences;
    private String mStrEmpId = null;
    private String mLoginData = null;
    private Toolbar mToolbar;
    private TextView tv_toolbar_title;
    private RecyclerView recyclerView;
    private RiskAssessmentListAdapter adapter;
    private PpmScheduleDocBy ppmScheduleDocBy;
    private List<AssetDetailsEntity> mList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Object> loginList1;
    private List<Object> loginList2 = new ArrayList<>();
    private Button btn_reassign_ccc;
    private List<Object> loginData;
    private List<Object> saveData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            TAG = "onCreate";
            Log.d(MODULE, TAG);
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mManager = mActivity.getSupportFragmentManager();
            mSavedInstanceState = savedInstanceState;
            mArgs = getArguments();
            mActivity
                    .getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }
            if (mArgs != null && mArgs.size() > 0) {
                // Check if true material required page other wise material used page
                showProgressDialog(mActivity, "Loading...", false);
                ppmScheduleDocBy = mArgs.getParcelable(AppUtils.ARGS_RISKASSES_LIST);
                RiskAssListRequest request = new RiskAssListRequest(ppmScheduleDocBy.getCompanyCode(), ppmScheduleDocBy.getJobNo(),
                        ppmScheduleDocBy.getPpmNo(), ppmScheduleDocBy.getChkCode());
                new RiskAssessmentService(mActivity, this).getRiskassesList(request);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preventive_riskass_list, container, false);
        TAG = "onCreateView";
        Log.d(MODULE, TAG);
        initView(rootView);
        setProperties();
        setupActionBar();
        return rootView;
    }

    public void setManager() {
        TAG = "setManager";
        Log.d(MODULE, TAG);
        try {
            mLayoutManager = new LinearLayoutManager(mActivity);
            recyclerView.setLayoutManager(mLayoutManager);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.lbl_risk_assessment));
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

    public void setProperties() {
        setManager();
    }

    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        btn_reassign_ccc = (Button) view.findViewById(R.id.btn_reassign_ccc);
        btn_reassign_ccc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData = adapter.onInsertData();
                saveDataServer();
            }
        });
    }

    private void saveDataServer() {
        if (saveData != null && !saveData.isEmpty()) {
            List<SaveAssesEntity> saveRiskAsses = new ArrayList<>();
            for (int i = 0; i < saveData.size(); i++) {
                if (TextUtils.isEmpty(saveData.get(i).getRaetComments())) {
                    AppUtils.showDialog(mActivity, "All Fields Mandatory");
                    return;
                } else {
                    SaveAssesEntity request = new SaveAssesEntity();
                    request.setCreatedBy(mStrEmpId);
                    request.setCreatedDate(ppmScheduleDocBy.getCrDate());
                    request.setJobNo(ppmScheduleDocBy.getJobNo());
                    request.setMcompanyCode(ppmScheduleDocBy.getCompanyCode());
                    request.setPpmRefNo(ppmScheduleDocBy.getPpmNo());
                    request.setRaetCode(saveData.get(i).getRaetCode());
                    request.setRaetComments(saveData.get(i).getRaetComments());
                    request.setRaetName(saveData.get(i).getRaetName());
                    request.setWorkType("P");
                    saveRiskAsses.add(request);
                }
            }
            showProgressDialog(mActivity, "Loading...", false);
            new RiskAssessmentService(mActivity, this).saveRiskList(saveRiskAsses);
        }
    }

    @Override
    public void onListAssSuccess(List<Object> login) {
        AppUtils.hideProgressDialog();
        loginData = login;
        adapter = new RiskAssessmentListAdapter(mActivity, login);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPPMDataSuccess(List<ObjectPPM> loginppm) {
        // this method is for ppm finding
    }

    @Override
    public void onPPMDataSuccessPpmFeedBack(PpmFeedBackResponse loginppm) {

    }

    @Override
    public void onSaveDataSuccess(String commonResponse) {
        AppUtils.hideProgressDialog();
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity)
                .content(commonResponse)
                .positiveText(R.string.lbl_okay)
                .stackingBehavior(StackingBehavior.ADAPTIVE)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        mManager.popBackStack();
                    }
                });
        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    @Override
    public void onListAssFailure(String strErr) {
        AppUtils.hideProgressDialog();
        AppUtils.showDialogToFinish(getActivity(),"Risk Assessment",strErr);
    }
}
