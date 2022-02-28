package com.daemon.emco_android.ui.fragments.utilityconsumption;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daemon.emco_android.R;
import com.daemon.emco_android.databinding.FragmentUtilityFilterBinding;
import com.daemon.emco_android.model.common.JobList;
import com.daemon.emco_android.model.common.OpcoDetail;
import com.daemon.emco_android.repository.remote.AssetVerificationRepository;
import com.daemon.emco_android.repository.remote.HeaderDetailRepository;
import com.daemon.emco_android.ui.components.FilterableListDialog;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.ui.fragments.inspection.assetverification.AddAssetTagFragment;
import com.daemon.emco_android.ui.fragments.survey.SurveyHeaderSecondary;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.showErrorToast;
import static com.daemon.emco_android.utils.StringUtil.space;

public class UtilityFilterFragment extends Fragment implements  HeaderDetailRepository.Listener{

    FragmentUtilityFilterBinding binding;
    private AppCompatActivity mActivity;
    List<OpcoDetail> opcoList = new ArrayList<>();
    List<JobList> jobLists = new ArrayList<>();
    String opco="";
    String jobno="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = (AppCompatActivity) getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_utility_filter, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupActionBar();
        setHasOptionsMenu(true);

        binding.tvSelectCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOpco();
            }
        });

        binding.tvSelectContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppUtils.showProgressDialog2(mActivity,"Loading..",true);
                new HeaderDetailRepository(mActivity,UtilityFilterFragment.this).getContractList(opco);

            }
        });

        binding.fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                proceedNext();

            }
        });

        new HeaderDetailRepository(mActivity,this).getOpcoList("");

    }

    public void proceedNext(){

        if(opco.equalsIgnoreCase("")){


            showErrorToast(getActivity(),"Company should not be empty.");

        }
        else if(jobno.equalsIgnoreCase("")){

            showErrorToast(getActivity(),"Contract should not be empty.");

        }
        else{
            loadFragment(new UtilityBarcodeScanningFragment(), Utils.TAG_FRAGMENT_UTILITY_SCANNING);
        }


    }


    public void showOpco(){

        if(opcoList!=null && opcoList.size()>0){

            try {
                final ArrayList strArraySiteName = new ArrayList();
                for (OpcoDetail entity : opcoList) {
                    strArraySiteName.add(entity.getOpcoCode()+ " - "+entity.getOpcoName());
                }

                strArraySiteName.add(space);

                FilterableListDialog.create(
                        mActivity,
                        ("Select OPCO"),
                        strArraySiteName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {
                                if(!text.equals(space)){

                                    binding.tvSelectCompany.setText(opcoList.get(strArraySiteName.indexOf(text)).getOpcoName());
                                    opco=opcoList.get(strArraySiteName.indexOf(text)).getOpcoCode();
                                    binding.tvSelectContract.setText("");
                                    binding.tvSelectContract.setHint("Select the Contract");
                                    jobno="";

                                }
                            }
                        })
                        .show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else{
            Log.i("","no data to show");
        }

    }

    public void showJobNo(){

        if(opcoList.isEmpty()){

            AppUtils.showErrorToast(getActivity(),"OPCO should not be empty.");
            return;

        }

        if(jobLists!=null && jobLists.size()>0){

            try {
                final ArrayList strArraySiteName = new ArrayList();
                for (JobList entity : jobLists) {
                    strArraySiteName.add(entity.getJobNo()+ " - "+entity.getJobDescription());
                }

                strArraySiteName.add(space);

                FilterableListDialog.create(
                        mActivity,
                        ("Select Job"),
                        strArraySiteName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {
                                if(!text.equals(space)){

                                    binding.tvSelectContract.setText(jobLists.get(strArraySiteName.indexOf(text)).getJobDescription());
                                    jobno=jobLists.get(strArraySiteName.indexOf(text)).getJobNo();
                                }
                            }
                        })
                        .show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else{


        }
    }

    public void setupActionBar() {
        Toolbar mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        TextView tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Utility Consumption");
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

    public void loadFragment(final Fragment fragment, final String tag) {
        Bundle mdata = new Bundle();
        mdata.putString(AppUtils.ARGS_COMPANYCODE,opco);
        mdata.putString(AppUtils.ARGS_JOBNO,jobno);
        fragment.setArguments(mdata);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_home).setVisible(true);
        menu.findItem(R.id.action_logout).setVisible(false);

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


    @Override
    public void onReceiveContractList(List<JobList> object) {
        AppUtils.hideProgressDialog();
        jobLists=object;
        showJobNo();
    }

    @Override
    public void onReceiveFailureContractList(String toString) {

    }

    @Override
    public void onSuccessOpcoList(List<OpcoDetail> opco, int mode) {
        opcoList=opco;
    }

    @Override
    public void onFailureOpcoList(String strMsg, int mode) {

    }
}