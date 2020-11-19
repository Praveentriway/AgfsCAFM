package com.daemon.emco_android.ui.fragments.preventive.ppmschedule;

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
import com.daemon.emco_android.ui.base.BaseFragment;
import com.daemon.emco_android.ui.adapter.Equipment_ListAdapter;
import com.daemon.emco_android.repository.remote.RiskAssessmentService;
import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.listeners.RiskeAssListener;
import com.daemon.emco_android.model.common.PpmScheduleDetails;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.RiskAssListRequest;
import com.daemon.emco_android.model.request.SaveAssesEntity;
import com.daemon.emco_android.model.response.Object;
import com.daemon.emco_android.model.response.ObjectPPM;
import com.daemon.emco_android.model.response.PpmFeedBackResponse;
import com.daemon.emco_android.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;
import static com.daemon.emco_android.utils.Utils.TAG_PPM_FINDING;

/**
 * Created by vikram on 6/3/18.
 */

public class Fragment_PM_Equip_Tool extends BaseFragment implements RiskeAssListener {

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
    private RiskAssessmentService riskAssessment_service;
    private List<AssetDetailsEntity> mList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private PpmScheduleDocBy ppmScheduleDocBy;
    private Equipment_ListAdapter adapter;
    private Button btn_reassign_ccc, btn_next;
    private List<Object> saveData = new ArrayList<>();
    private List<Object> loginData;
    private PpmScheduleDetails mReceiveComplaintView = new PpmScheduleDetails();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mManager = mActivity.getSupportFragmentManager();
            mSavedInstanceState = savedInstanceState;
            mArgs = getArguments();
            mActivity
                    .getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            mStrEmpId = getLogin(getContext()).getEmployeeId();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preventive_equip_list, container, false);

        initView(rootView);
        setProperties();

        setupToolBar(mActivity,getString(R.string.lbl_equipment_tools));

        if (mArgs != null && mArgs.size() > 0) {
            showProgressDialog(mActivity, "Loading...", false);
            ppmScheduleDocBy = mArgs.getParcelable(AppUtils.ARGS_TOOL_LIST);
            RiskAssListRequest request = new RiskAssListRequest(ppmScheduleDocBy.getCompanyCode(), ppmScheduleDocBy.getJobNo(),
                    ppmScheduleDocBy.getPpmNo(), ppmScheduleDocBy.getChkCode());
            new RiskAssessmentService(mActivity, this).getEquipemList(request);
        }
        return rootView;
    }

    public void setManager() {

        try {
            mLayoutManager = new LinearLayoutManager(mActivity);
            recyclerView.setLayoutManager(mLayoutManager);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setProperties() {
        setManager();
    }

    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        btn_reassign_ccc = (Button) view.findViewById(R.id.btn_reassign_ccc);
        btn_next = (Button) view.findViewById(R.id.btn_next);
        btn_reassign_ccc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData = adapter.onInsertData();
                saveDataServer();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNextPage();
            }
        });
    }

    public Bundle getSavedState() {
        Bundle outState = new Bundle();
        try {
            outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, mReceiveComplaintView);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outState;
    }

    private void gotoNextPage() {
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

    private void saveDataServer() {

        if (saveData != null && !saveData.isEmpty()) {
            List<SaveAssesEntity> saveRiskAsses = new ArrayList<>();
            for (int i = 0; i < saveData.size(); i++) {
                if (TextUtils.isEmpty(saveData.get(i).getRaetTagNo())) {
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
                    request.setRaetTagNo(saveData.get(i).getRaetTagNo());
                    request.setRaetName(saveData.get(i).getRaetName());

                    if(saveData.get(i).getRaetComments()==null){
                        request.setRaetComments("  ");
                    }
                    else{
                        request.setRaetComments(saveData.get(i).getRaetComments());
                    }

                    request.setWorkType("P");
                    saveRiskAsses.add(request);
                }
            }
            showProgressDialog(mActivity, "Loading...", false);
            new RiskAssessmentService(mActivity, this).saveEquipmenttools(saveRiskAsses);

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
    public void onListAssSuccess(List<Object> login) {
        loginData = login;
        AppUtils.hideProgressDialog();
        adapter = new Equipment_ListAdapter(mActivity, login);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPPMDataSuccess(List<ObjectPPM> loginppm) {
        //this interface for equipment tools next only
    }

    @Override
    public void onPPMDataSuccessPpmFeedBack(PpmFeedBackResponse loginppm) {

    }

    @Override
    public void onSaveDataSuccess(String commonResponse) {

        AppUtils.hideProgressDialog();
        MaterialDialog.Builder builder =
                new MaterialDialog.Builder(mActivity)
                        .content(commonResponse)
                        .positiveText(R.string.lbl_okay)
                        .stackingBehavior(StackingBehavior.ADAPTIVE)
                        .onPositive(
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(
                                            @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        FragmentManager fm = getActivity().getSupportFragmentManager();
                                        fm.popBackStack();
                                    }
                                });

        MaterialDialog dialog = builder.build();
        dialog.show();

    }

    @Override
    public void onListAssFailure(String strErr) {
        AppUtils.hideProgressDialog();
        AppUtils.showDialogToFinish(getActivity(),"Equipment Tools",strErr);
    }
}
