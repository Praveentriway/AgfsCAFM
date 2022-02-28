package com.daemon.emco_android.ui.fragments.inspection.assetverification;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daemon.emco_android.R;
import com.daemon.emco_android.databinding.FragmentAssetCheckListBinding;
import com.daemon.emco_android.listeners.PPMService_Listner;
import com.daemon.emco_android.model.common.AssetInfo;
import com.daemon.emco_android.model.common.AssetInfoTrans;
import com.daemon.emco_android.model.common.EmployeeTrackingDetail;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.daemon.emco_android.model.response.GetPpmParamValue;
import com.daemon.emco_android.model.response.ObjectMonthly;
import com.daemon.emco_android.model.response.ObjectSavedCheckListResponse;
import com.daemon.emco_android.repository.db.entity.ServeyQuestionnaire;
import com.daemon.emco_android.repository.remote.GetPpmResponseService;
import com.daemon.emco_android.service.GPSTracker;
import com.daemon.emco_android.ui.adapter.PPMCheckListAdapter;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.utils.AppUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.daemon.emco_android.utils.AppUtils.ARGS_ASSETINFO;
import static com.daemon.emco_android.utils.AppUtils.ARGS_CHECKLIST;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYQUES;
import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;
import static com.daemon.emco_android.utils.Utils.TAG_FRAGMENT_ASSET_CHECKLIST;
import static com.daemon.emco_android.utils.Utils.TAG_FRAGMENT_ASSET_FINDINGS;


public class AssetCheckListFragment extends Fragment implements PPMService_Listner {

    AssetInfoTrans assetInfo;
    private AppCompatActivity mActivity;
    FragmentAssetCheckListBinding binding;
    private PPMCheckListAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private List<Object> insertData = new ArrayList<>();
    private List<PpmScheduleDocBy> fetchPpmScheduleDocBy = new ArrayList<>();
    PpmScheduleDocBy getPpmScheduleDocBy;
    List<ObjectMonthly> checkList;

    public AssetCheckListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assetInfo = (AssetInfoTrans) getArguments().getSerializable(ARGS_ASSETINFO);
            checkList = (ArrayList<ObjectMonthly>) getArguments().getSerializable(ARGS_CHECKLIST);
        }
        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding= DataBindingUtil
                .inflate(inflater, R.layout.fragment_asset_check_list, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupActionBar();
        setHasOptionsMenu(true);
        mLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveCheckList();

            }
        });
        loadCheckList(checkList);

    }


    public void setupActionBar() {
        Toolbar mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        TextView tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Asset Audit");

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
                FragmentTransaction _transaction = fm.beginTransaction();
                _transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadFragment(final Fragment fragment,List<PpmScheduleDocBy> fetchPpmScheduleDocBy,AssetInfoTrans trans, String tag) {


        ArrayList<PpmScheduleDocBy> obj = new ArrayList<>(fetchPpmScheduleDocBy.size());
        obj.addAll(fetchPpmScheduleDocBy);

        Bundle mdata = new Bundle();
        mdata.putSerializable(ARGS_CHECKLIST, obj);
        mdata.putSerializable(ARGS_ASSETINFO, trans);

        fragment.setArguments(mdata);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

    }

    private void loadCheckList(List<ObjectMonthly> checkList){

        List<ObjectMonthly> value = new ArrayList<>();
        binding.txtCheckTitle.setText(checkList.get(0).getCheckListDesc().replaceAll("(\\r|\\n)", ""));
        LinkedHashMap<String, ArrayList<ObjectMonthly>> hashMap = new LinkedHashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        for (ObjectMonthly customerRemark : checkList) {
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
        binding.recyclerView.setAdapter(adapter);


    }

    private void saveCheckList(){

        insertData = adapter.onInsertData();
        saveDataServer(true);

    }
    private void saveDataServer(boolean isSave) {
        if (insertData != null && !insertData.isEmpty()) {
            for (int i = 0; i < insertData.size(); i++) {
                if (insertData.get(i) instanceof ObjectMonthly) {
                    ObjectMonthly d = (ObjectMonthly) insertData.get(i);
                    if (TextUtils.isEmpty(d.getParamValue())) {

                        AppUtils.showSnackBar(R.id.coordinatorLayout,binding.getRoot(),  "Please fill all the mandatory fields.");
                        fetchPpmScheduleDocBy.clear();
                        return;

                    } else {

                        getPpmScheduleDocBy = new PpmScheduleDocBy();
                        getPpmScheduleDocBy.setCompanyCode(assetInfo.getOpco());
                        String[] splt=assetInfo.getRefNo().split("-");
                        getPpmScheduleDocBy.setWorkOrderNo(splt[1]);
                        getPpmScheduleDocBy.setJobNo(assetInfo.getJobNo());
                        getPpmScheduleDocBy.setPpmNo(assetInfo.getRefNo());
                        getPpmScheduleDocBy.setAssetCode(assetInfo.getAssetTag());
                        getPpmScheduleDocBy.setChkCode(assetInfo.getCheckListCode());
                        getPpmScheduleDocBy.setNatureWork(d.getNatureWork());
                        getPpmScheduleDocBy.setEquipType(d.getEquipType());
                        getPpmScheduleDocBy.setClinsCode(d.getHeaderCode());
                        getPpmScheduleDocBy.setCldetlCode(d.getDetailsCode());
                        getPpmScheduleDocBy.setParamValue(d.getParamValue());
                        getPpmScheduleDocBy.setRemarks(d.getRemarks());
                        getPpmScheduleDocBy.setModifiedBy(getUseID());
                        getPpmScheduleDocBy.setTransType("Asset Verification");
                        fetchPpmScheduleDocBy.add(getPpmScheduleDocBy);

                    }
                }
            }


            loadFragment(new AssetFindingsFragment(),fetchPpmScheduleDocBy,assetInfo, TAG_FRAGMENT_ASSET_FINDINGS);

          //  sendServer(fetchPpmScheduleDocBy);

        }
    }

    public String getUseID(){

        String mStrEmpId="";

        Gson gson = new Gson();
        SharedPreferences mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor  mEditor = mPreferences.edit();
        String  mStringJson = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
        if (mStringJson != null) {
            Login user = gson.fromJson(mStringJson, Login.class);
            mStrEmpId = user.getEmployeeId();
        }
        return mStrEmpId;
    }

    public void sendServer(List<PpmScheduleDocBy> fetchPpmScheduleDocBy) {
      //  showProgressDialog(mActivity, "Loading...", false);
       // new GetPpmResponseService(mActivity, this).savePpmParamCheckValue(fetchPpmScheduleDocBy);

    }


    @Override
    public void onReceivedPPMParameterSuccess(GetPpmParamValue customerRemarks) {

    }

    @Override
    public void onReceivedSuccess(List<ObjectMonthly> customerRemarks) {


    }

    @Override
    public void onGetSavedDataSuccess(List<ObjectSavedCheckListResponse> customerRemarks) {

    }

    @Override
    public void onReceivedSavedSuccess(String customerRemarks) {

    }

    @Override
    public void onReceiveFailure(String strErr) {
        AppUtils.hideProgressDialog();
    }
}