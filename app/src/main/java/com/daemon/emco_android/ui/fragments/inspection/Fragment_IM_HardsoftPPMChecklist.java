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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.PPMCheckListAdapter;
import com.daemon.emco_android.repository.remote.GetPpmResponseService;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.ui.fragments.common.Fragment_Main;
import com.daemon.emco_android.ui.fragments.preventive.ppmschedule.Fragment_PM_Equip_Tool;
import com.daemon.emco_android.ui.fragments.preventive.ppmschedule.Fragment_PM_Risk_List;
import com.daemon.emco_android.ui.fragments.preventive.ppmschedule.Fragment_PM_afterppm;
import com.daemon.emco_android.ui.fragments.preventive.ppmschedule.Fragment_PPM_PPE;
import com.daemon.emco_android.listeners.PPMService_Listner;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.PpmScheduleDetails;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.common.SaveCheckListData;
import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.daemon.emco_android.model.response.GetPPMRecomResponse;
import com.daemon.emco_android.model.response.GetPpmParamValue;
import com.daemon.emco_android.model.response.ObjectMonthly;
import com.daemon.emco_android.model.response.ObjectSavedCheckListResponse;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;
import static com.daemon.emco_android.utils.Utils.TAG_EQUIP_LIST;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_PPE;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_RESPOND;
import static com.daemon.emco_android.utils.Utils.TAG_RiSK_ASS_LIST;

public class Fragment_IM_HardsoftPPMChecklist extends Fragment implements PPMService_Listner {
    private static final String TAG = Fragment_IM_HardsoftPPMChecklist.class.getSimpleName();
    public Bundle mSavedInstanceState;
    private AppCompatActivity mActivity;
    private Font font = App.getInstance().getFontInstance();
    private PpmScheduleDetails mReceiveComplaintView = new PpmScheduleDetails();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private Bundle mArgs;
    private String mTitle;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private ReceiveComplaintViewEntity complaintViewEntity;
    private CoordinatorLayout cl_main;
    private PPMCheckListAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private TextView tv_toolbar_title, txt_ppm_title;
    private Button btn_next, btn_save, btn_ppe, btn_risk, btn_equip, btn_save_new;
    private Toolbar mToolbar;
    private View rootView;
    private String mNetworkInfo = null;
    private String mLoginData = null;
    private PpmScheduleDocBy ppmScheduleDocBy;
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
                        case R.id.btn_save:
                            submitForm();
                            break;
                        default:
                            break;
                    }
                }
            };
    private GetPPMRecomResponse mGetPostRateService;
    private GetPpmResponseService getPostRateService_service;
    private List<PpmScheduleDocBy> fetchPpmScheduleDocBy = new ArrayList<>();
    private PpmScheduleDocBy getPpmScheduleDocBy = null;
    private List<SaveCheckListData> updateData = new ArrayList<>();
    private List<Object> insertData = new ArrayList<>();
    private String mStrEmpId = null;
    private List<ObjectMonthly> objectCodeValue = new ArrayList<>();

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

            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }
            getPostRateService_service = new GetPpmResponseService(mActivity, this);

            if (mArgs != null && mArgs.size() > 0) {
                // Check if true material required page other wise material used page
                mTitle = mArgs.getString(AppUtils.ARGS_IM_SERVICES_Page_TITLE);
                complaintViewEntity = mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);
                // getReceiveComplainMaterialFromService(mCurrentnoOfRows);
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
            rootView = inflater.inflate(R.layout.fragment_ppmchecklist, container, false);
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
            btn_save_new = (Button) rootView.findViewById(R.id.btn_save_new);
            btn_save_new.setVisibility(View.GONE);
            btn_ppe = (Button) rootView.findViewById(R.id.btn_ppe);
            btn_risk = (Button) rootView.findViewById(R.id.btn_risk);
            btn_equip = (Button) rootView.findViewById(R.id.btn_equip);
            btn_next = (Button) rootView.findViewById(R.id.btn_next);
            linearLayout=(LinearLayout)rootView.findViewById(R.id.ll_top_lay);
            linearLayout.setVisibility(View.INVISIBLE);
            btn_save = (Button) rootView.findViewById(R.id.btn_save);
            btn_save.setVisibility(View.GONE);
            txt_ppm_title = (TextView) rootView.findViewById(R.id.txt_ppm_title);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.lbl_ppm_checklist));
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
        btn_ppe.setTypeface(font.getHelveticaRegular());
        btn_risk.setTypeface(font.getHelveticaRegular());
        btn_equip.setTypeface(font.getHelveticaRegular());
        btn_next.setTypeface(font.getHelveticaRegular());
        btn_save.setTypeface(font.getHelveticaRegular());
        btn_ppe.setOnClickListener(_OnClickListener);
        btn_risk.setOnClickListener(_OnClickListener);
        btn_equip.setOnClickListener(_OnClickListener);
        btn_next.setOnClickListener(_OnClickListener);
        btn_save.setOnClickListener(_OnClickListener);
        AppUtils.closeInput(cl_main);
        setManager();

        btn_save_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData = adapter.onInsertData();
                saveDataServer();
            }
        });

        if(mTitle.equals(mActivity.getString(R.string.title_ppm_request_verification_hard)))
        {
            System.out.println("hard");
            showProgressDialog(mActivity, "Loading...", false);
            SaveRatedServiceRequest ratedServiceRequest = new SaveRatedServiceRequest();
            ratedServiceRequest.setCompanyCode("002");
            ratedServiceRequest.setChkCode("ACFCY001");
            ratedServiceRequest.setPpmNo("6302171204000003");
            getPostRateService_service.getIMPpmCheckListHard(ratedServiceRequest);
        }else {
            showProgressDialog(mActivity, "Loading...", false);
            SaveRatedServiceRequest ratedServiceRequest = new SaveRatedServiceRequest();
            ratedServiceRequest.setCompanyCode("002");
            ratedServiceRequest.setChkCode("ACFCY001");
            ratedServiceRequest.setPpmNo("6302171204000003");
            getPostRateService_service.getIMPpmCheckListSoft(ratedServiceRequest);
        }
    }

    private void saveDataServer() {
        if (insertData != null && !insertData.isEmpty()) {
            for (int i = 0; i < insertData.size(); i++) {
                if (insertData.get(i) instanceof ObjectMonthly) {
                    ObjectMonthly d = (ObjectMonthly) insertData.get(i);
                    if (TextUtils.isEmpty(d.getParamValue())) {
                        AppUtils.showDialog(mActivity, "All Fields Mandatory");
                        return;
                    } else {
                        getPpmScheduleDocBy = new PpmScheduleDocBy();
                        getPpmScheduleDocBy.setCompanyCode(ppmScheduleDocBy.getCompanyCode());
                        getPpmScheduleDocBy.setWorkOrderNo(ppmScheduleDocBy.getBatchSRL());
                        getPpmScheduleDocBy.setPpmNo(ppmScheduleDocBy.getPpmNo());
                        getPpmScheduleDocBy.setJobNo(ppmScheduleDocBy.getJobNo());
                        getPpmScheduleDocBy.setNatureWork(ppmScheduleDocBy.getNatureWork());
                        getPpmScheduleDocBy.setEquipType(ppmScheduleDocBy.getEquipType());
                        getPpmScheduleDocBy.setAssetCode(ppmScheduleDocBy.getEquipCode());
                        getPpmScheduleDocBy.setChkCode(ppmScheduleDocBy.getChkCode());
                        getPpmScheduleDocBy.setClinsCode(d.getHeaderCode());
                        getPpmScheduleDocBy.setCldetlCode(d.getDetailsCode());
                        getPpmScheduleDocBy.setParamValue(d.getParamValue());
                        getPpmScheduleDocBy.setRemarks(d.getRemarks());
                        getPpmScheduleDocBy.setModifiedBy(mStrEmpId);
                        fetchPpmScheduleDocBy.add(getPpmScheduleDocBy);
                    }
                }
            }
            sendServer(fetchPpmScheduleDocBy);
        }
    }

    public void sendServer(List<PpmScheduleDocBy> fetchPpmScheduleDocBy) {
        showProgressDialog(mActivity, "Loading...", false);
        new GetPpmResponseService(mActivity, this).savePpmParamCheckValue(fetchPpmScheduleDocBy);
    }

    public void setManager() {
        try {
            mLayoutManager = new LinearLayoutManager(mActivity);
            recyclerView.setLayoutManager(mLayoutManager);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void gotoNextPage() {
        mSavedInstanceState = getSavedState();
        Bundle data = new Bundle();
        data.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, ppmScheduleDocBy);
        Fragment_PM_afterppm fragment = new Fragment_PM_afterppm();
        fragment.setArguments(data);
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_RESPOND);
        fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_RESPOND);
        fragmentTransaction.commit();
    }

    private void gotoRiskPage() {
        mSavedInstanceState = getSavedState();
        Bundle data = new Bundle();
        data.putParcelable(AppUtils.ARGS_RISKASSES_LIST, ppmScheduleDocBy);
        Fragment_PM_Risk_List fragment = new Fragment_PM_Risk_List();
        fragment.setArguments(data);
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_container, fragment, TAG_RiSK_ASS_LIST);
        fragmentTransaction.addToBackStack(TAG_RiSK_ASS_LIST);
        fragmentTransaction.commit();
    }

    private void gotoEquipPage() {
        mSavedInstanceState = getSavedState();
        Bundle data = new Bundle();
        data.putParcelable(AppUtils.ARGS_TOOL_LIST, ppmScheduleDocBy);
        Fragment_PM_Equip_Tool fragment = new Fragment_PM_Equip_Tool();
        fragment.setArguments(data);
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_container, fragment, TAG_EQUIP_LIST);
        fragmentTransaction.addToBackStack(TAG_EQUIP_LIST);
        fragmentTransaction.commit();
    }


    private void submitForm() {
        Log.d(TAG, "submitForm");
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(TAG, "onPrepareOptionsMenu ");
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Log.d(TAG, "onOptionsItemSelected : home");
                    mActivity.onBackPressed();
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
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        mSavedInstanceState = getSavedState();
    }

    public Bundle getSavedState() {
        Bundle outState = new Bundle();
        try {
            outState.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS, mReceiveComplaintView);
            Log.d(TAG, "getSavedState");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outState;
    }

    private void gotoPPEPage() {
        try {
            if (mArgs != null && mArgs.size() > 0) {
                Bundle data = new Bundle();
                data.putParcelable(AppUtils.ARG_PPE_PPM, ppmScheduleDocBy);

                Fragment fragment = new Fragment_PPM_PPE();
                fragment.setArguments(data);
                FragmentTransaction fragmentTransaction = mManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_PPE);
                fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_PPE);
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onReceivedPPMParameterSucess(GetPpmParamValue customerRemarks) {
    }

    @Override
    public void onReceivedSucess(List<ObjectMonthly> customerRemarks) {
        AppUtils.hideProgressDialog();
        List<ObjectMonthly> value = new ArrayList<>();
        objectCodeValue = customerRemarks;
        txt_ppm_title.setText(customerRemarks.get(0).getCheckListDesc());
        LinkedHashMap<String, ArrayList<ObjectMonthly>> hashMap = new LinkedHashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        for (ObjectMonthly customerRemark : customerRemarks) {
            String key = customerRemark.getPpmServiceINS();
            ArrayList<ObjectMonthly> pre = hashMap.get(key);
            if (pre == null) {
                ArrayList<ObjectMonthly> newList = new ArrayList<>();
                newList.add(customerRemark);
                hashMap.put(key, newList);
            } else {
                pre.add(customerRemark);
                hashMap.put(key, pre);
            }
        }
        List<ObjectMonthly> sendData = new ArrayList<>();
        for (Map.Entry<String, ArrayList<ObjectMonthly>> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            list.add(key);
            value = entry.getValue();
            for (ObjectMonthly objectMonthly : value) {
                list.add(objectMonthly);
                sendData.add(objectMonthly);
            }
        }
        adapter = new PPMCheckListAdapter(mActivity, list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onGetSavedDataSucess(List<ObjectSavedCheckListResponse> customerRemarks) {
        AppUtils.hideProgressDialog();
    }

    @Override
    public void onReceivedSavedSucess(String customerRemarks) {
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity, customerRemarks);
    }

    @Override
    public void onReceiveFailure(String strErr) {
        AppUtils.hideProgressDialog();
    }
}
