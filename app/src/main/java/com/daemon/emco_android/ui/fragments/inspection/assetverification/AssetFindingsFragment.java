package com.daemon.emco_android.ui.fragments.inspection.assetverification;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.R;
import com.daemon.emco_android.databinding.FragmentAssetFindingsBinding;
import com.daemon.emco_android.listeners.PPMService_Listner;
import com.daemon.emco_android.model.common.AssetInfo;
import com.daemon.emco_android.model.common.AssetInfoTrans;
import com.daemon.emco_android.model.common.DocumentType;
import com.daemon.emco_android.model.common.EmployeeList;
import com.daemon.emco_android.model.common.JobList;
import com.daemon.emco_android.model.common.OpcoDetail;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.response.GetPpmParamValue;
import com.daemon.emco_android.model.response.ObjectMonthly;
import com.daemon.emco_android.model.response.ObjectSavedCheckListResponse;
import com.daemon.emco_android.repository.remote.AssetVerificationRepository;
import com.daemon.emco_android.repository.remote.GetPpmResponseService;
import com.daemon.emco_android.utils.AppUtils;

import org.jsoup.helper.DataUtil;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.ARGS_ASSETINFO;
import static com.daemon.emco_android.utils.AppUtils.ARGS_CHECKLIST;
import static com.daemon.emco_android.utils.AppUtils.showErrorToast;
import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;


public class AssetFindingsFragment extends Fragment implements  AssetVerificationRepository.Listener, PPMService_Listner {

    ArrayList<PpmScheduleDocBy> checkList;
    AssetInfoTrans assetInfo;
    FragmentAssetFindingsBinding binding;
    private AppCompatActivity mActivity;

    public AssetFindingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mActivity = (AppCompatActivity) getActivity();

            if (getArguments() != null) {
                assetInfo = (AssetInfoTrans) getArguments().getSerializable(ARGS_ASSETINFO);
                checkList = (ArrayList<PpmScheduleDocBy>) getArguments().getSerializable(ARGS_CHECKLIST);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_asset_findings, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        if(binding.edtAssestfinding.getText().toString().equals(""))    {

            showErrorToast(mActivity,"Asset Findings field should not be empty.");

        }

        else if(binding.edtAssetrecommedation.getText().toString().equals("")){

            showErrorToast(mActivity,"Asset Recommendation field should not be empty.");

        }
      else{

          assetInfo.setFindings(binding.edtAssestfinding.getText().toString());
          assetInfo.setRecommendation(binding.edtAssetrecommedation.getText().toString());

            saveAssetDetails();
        }


            }
        });

    }


    private void saveAssetDetails(){

        if(checkList!=null){
            showProgressDialog(mActivity, "Loading...", false);
            new GetPpmResponseService(mActivity, this).savePpmParamCheckValue(checkList);
        }
        else{
            new AssetVerificationRepository(mActivity,this).saveAsset(assetInfo);
        }
    }


    @Override
    public void onReceiveAssetInfo(List<AssetInfo> object) {

    }

    @Override
    public void onReceiveFailureAssetInfo(String toString) {

    }

    @Override
    public void onReceiveJobList(List<JobList> object) {

    }

    @Override
    public void onReceiveFailureJobList(String toString) {

    }

    @Override
    public void onReceiveEmployeeList(List<EmployeeList> object) {

    }

    @Override
    public void onReceiveFailureEmployeeList(String toString) {

    }

    @Override
    public void onReceiveAssetType(String type, List<DocumentType> rx, int from) {

    }

    @Override
    public void onReceiveFailureJAssetType(String err, int from) {

    }

    @Override
    public void onSuccessSaveAsset(String strMsg, int mode) {

        AppUtils.hideProgressDialog();
        showDialog(mActivity,strMsg,"");

    }

    @Override
    public void onFailureSaveAsset(String strErr, int mode) {

    }

    @Override
    public void onSuccessLocationOpco(List<OpcoDetail> opco, int mode) {

    }

    @Override
    public void onFailureLocationOpco(String strMsg, int mode) {

    }

    @Override
    public void onReceivedPPMParameterSucess(GetPpmParamValue customerRemarks) {

    }

    @Override
    public void onReceivedSucess(List<ObjectMonthly> customerRemarks) {



    }

    @Override
    public void onGetSavedDataSucess(List<ObjectSavedCheckListResponse> customerRemarks) {

    }

    @Override
    public void onReceivedSavedSucess(String customerRemarks) {

        new AssetVerificationRepository(mActivity,this).saveAsset(assetInfo);

    }

    @Override
    public void onReceiveFailure(String strErr) {
        AppUtils.hideProgressDialog();
    }

    public  void showDialog(Context context, String strTitle, String StrMsg) {
        try {

            MaterialDialog.Builder builder =
                    new MaterialDialog.Builder(context)
                            .content(StrMsg)
                            .title(strTitle)
                            .positiveText(R.string.lbl_okay)
                            .stackingBehavior(StackingBehavior.ADAPTIVE)
                            .onPositive(
                                    new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(
                                                @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();

                                            FragmentManager fm = getActivity().getSupportFragmentManager();
                                            for (int i = 0; i < 4; ++i) {
                                                fm.popBackStack();
                                            }

                                        }
                                    });
            MaterialDialog dialog = builder.build();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}