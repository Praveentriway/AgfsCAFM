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
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.Equipment_ListAdapter;
import com.daemon.emco_android.repository.remote.RiskAssessmentService;
import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.ui.fragments.common.Fragment_Main;
import com.daemon.emco_android.listeners.RiskeAssListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.PPMFindingRequest;
import com.daemon.emco_android.model.request.SavePPMFindingRequest;
import com.daemon.emco_android.model.response.Object;
import com.daemon.emco_android.model.response.ObjectPPM;
import com.daemon.emco_android.model.response.PpmFeedBackResponse;
import com.daemon.emco_android.utils.AppUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;

/**
 * Created by vikram on 8/3/18.
 */

public class Fragment_PPM_Finding extends Fragment implements RiskeAssListener
{
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
    private RiskAssessmentService riskAssessment_service;
    private List<AssetDetailsEntity> mList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private PpmScheduleDocBy ppmScheduleDocBy;
    private Equipment_ListAdapter adapter;
    private Button btn_save,btn_next;
    private List<Object> insertDataFirst = new ArrayList<>();
    private List<Object> updateData = new ArrayList<>();
    private List<Object> loginData;
    private EditText edt_ppmfinding,edt_ppmrecommedation;

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
                ppmScheduleDocBy = mArgs.getParcelable(AppUtils.ARGS_TOOL_LIST);
                showProgressDialog(mActivity, "Loading...", false);
                PPMFindingRequest request = new PPMFindingRequest(ppmScheduleDocBy.getCompanyCode(), ppmScheduleDocBy.getJobNo(),
                        ppmScheduleDocBy.getPpmNo(), ppmScheduleDocBy.getBatchSRL());
                new RiskAssessmentService(mActivity, this).getPPMFinding(request);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ppm_finding, container, false);
        TAG = "onCreateView";
        Log.d(MODULE, TAG);
        initView(rootView);
        setupActionBar();
        return rootView;
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.ppm_finding));
        // mToolbar.setTitle(getResources().getString(R.string.lbl_ppm_details));
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

    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_next=(Button)view.findViewById(R.id.btn_next);
        edt_ppmfinding=(EditText)view.findViewById(R.id.edt_ppmfinding);
        edt_ppmrecommedation=(EditText)view.findViewById(R.id.edt_ppmrecommedation);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edt_ppmrecommedation.getText().toString())&&TextUtils.isEmpty(edt_ppmfinding.getText().toString())){
                    AppUtils.showDialog(mActivity, "Please Enter Data to Save");
                }else if(TextUtils.isEmpty(edt_ppmfinding.getText().toString())){
                    //AppUtils.showDialog(mActivity, "Are you sure want to save without entering ppmfinding");
                    String s="Are you sure you want to save without entering PPM finding";
                    showDialog(s,1);
                }else if(TextUtils.isEmpty(edt_ppmrecommedation.getText().toString())){
                    String s="Are you sure you want to save without entering PPM Recommendation";
                    showDialog(s,2);
                }else{
                    saveDataToServer(0);
                }
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
                Fragment _fragment = new Fragment_Main();
                FragmentTransaction _transaction = mManager.beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(String s,final int toCheck){
        try {
            MaterialDialog.Builder dialog = dialog = new MaterialDialog.Builder(mActivity);
            dialog.title(s);
            dialog.itemsCallbackSingleChoice(
                    -1,
                    new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(
                                MaterialDialog dialog, View view, int which, CharSequence text) {
                            //to save the data for ppm findig
                            saveDataToServer(toCheck);
                            dialog.dismiss();
                            return true;
                        }
                    });
            dialog.positiveText(R.string.lbl_yes);
            dialog.negativeText(R.string.lbl_no);
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveDataToServer(int toCheck){
        if(toCheck==1){
            showProgressDialog(mActivity, "Loading...", false);
            SavePPMFindingRequest request = new SavePPMFindingRequest(ppmScheduleDocBy.getCompanyCode(), ppmScheduleDocBy.getBatchSRL(),
                    ppmScheduleDocBy.getJobNo(),ppmScheduleDocBy.getPpmNo(),ppmScheduleDocBy.getEquipCode(),"",edt_ppmrecommedation.getText().toString(), ppmScheduleDocBy.getUser());
            new RiskAssessmentService(mActivity, this).savePPMFinding(request);
        }else if(toCheck==2){
            showProgressDialog(mActivity, "Loading...", false);
            SavePPMFindingRequest request = new SavePPMFindingRequest(ppmScheduleDocBy.getCompanyCode(), ppmScheduleDocBy.getBatchSRL(),
                    ppmScheduleDocBy.getJobNo(),ppmScheduleDocBy.getPpmNo(),ppmScheduleDocBy.getEquipCode(),edt_ppmfinding.getText().toString(),"", ppmScheduleDocBy.getUser());
            new RiskAssessmentService(mActivity, this).savePPMFinding(request);
        }else{
            showProgressDialog(mActivity, "Loading...", false);
            SavePPMFindingRequest request = new SavePPMFindingRequest(ppmScheduleDocBy.getCompanyCode(), ppmScheduleDocBy.getBatchSRL(),
                    ppmScheduleDocBy.getJobNo(),ppmScheduleDocBy.getPpmNo(),ppmScheduleDocBy.getEquipCode(),edt_ppmfinding.getText().toString(),edt_ppmrecommedation.getText().toString(), ppmScheduleDocBy.getUser());
            new RiskAssessmentService(mActivity, this).savePPMFinding(request);
        }

    }

    @Override
    public void onListAssSuccess(List<Object> login) {
        //this is check risk assesment
    }

    @Override
    public void onPPMDataSuccess(List<ObjectPPM> loginppm) {
        //this is for ppm finding
        AppUtils.hideProgressDialog();
        if(loginppm.get(0).getPpmFinding()!=null){
            edt_ppmfinding.setText(loginppm.get(0).getPpmFinding());
        }if(loginppm.get(0).getPpmRecommend()!=null){
            edt_ppmrecommedation.setText(loginppm.get(0).getPpmRecommend());
        }


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
//                        Bundle data = new Bundle();
//                        data.putParcelable(AppUtils.ARGS_PPMSCHEDULEDOCBY, ppmScheduleDocBy);
//                        Fragment_PM_PPMChecklist fragment = new Fragment_PM_PPMChecklist();
//                        fragment.setArguments(data);
//                        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
//                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//                        fragmentTransaction.replace(R.id.frame_container, fragment, TAG_PM_PPMCHECKLIST);
//                        fragmentTransaction.addToBackStack(TAG_PM_PPMCHECKLIST);
//                        fragmentTransaction.commit();
                    }
                });
        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    @Override
    public void onListAssFailure(String login) {
        AppUtils.hideProgressDialog();
    }
}
