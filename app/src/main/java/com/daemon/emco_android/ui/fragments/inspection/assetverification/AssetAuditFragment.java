package com.daemon.emco_android.ui.fragments.inspection.assetverification;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.R;
import com.daemon.emco_android.databinding.FragmentAssetAuditBinding;
import com.daemon.emco_android.listeners.DatePickerDialogListener;
import com.daemon.emco_android.listeners.DefectDoneImage_Listener;
import com.daemon.emco_android.listeners.ImagePickListener;
import com.daemon.emco_android.listeners.PPMService_Listner;
import com.daemon.emco_android.model.common.AssetInfo;
import com.daemon.emco_android.model.common.AssetInfoTrans;
import com.daemon.emco_android.model.common.DocumentType;
import com.daemon.emco_android.model.common.EmployeeList;
import com.daemon.emco_android.model.common.JobList;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.OpcoDetail;
import com.daemon.emco_android.model.request.RCDownloadImageRequest;
import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.daemon.emco_android.model.response.DefectDoneImageUploaded;
import com.daemon.emco_android.model.response.GetPpmParamValue;
import com.daemon.emco_android.model.response.ObjectMonthly;
import com.daemon.emco_android.model.response.ObjectSavedCheckListResponse;
import com.daemon.emco_android.model.response.RCDownloadImage;
import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;
import com.daemon.emco_android.repository.remote.AssetVerificationRepository;
import com.daemon.emco_android.repository.remote.GetPpmResponseService;
import com.daemon.emco_android.repository.remote.ReceiveComplaintRespondService;
import com.daemon.emco_android.repository.remote.SupportDocumentRepository;
import com.daemon.emco_android.ui.adapter.CustomRecyclerViewDataAdapter;
import com.daemon.emco_android.ui.adapter.CustomRecyclerViewItem;
import com.daemon.emco_android.ui.components.FilterableListDialog;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ImageUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import uk.co.senab.photoview.PhotoViewAttacher;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.daemon.emco_android.utils.AppUtils.ARGS_ASSETINFO;
import static com.daemon.emco_android.utils.AppUtils.ARGS_ASSET_VERIFICATION_TYPE;
import static com.daemon.emco_android.utils.AppUtils.checkInternet;
import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;
import static com.daemon.emco_android.utils.StringUtil.space;
import static com.daemon.emco_android.utils.Utils.TAG_FRAGMENT_ASSET_FINDINGS;

public class AssetAuditFragment extends Fragment implements AssetVerificationRepository.Listener,SupportDocumentRepository.SupportDocTypeI, ImagePickListener
        ,DatePickerDialogListener,CustomRecyclerViewDataAdapter.ImageListner, DefectDoneImage_Listener, PPMService_Listner {

    AssetInfo assetInfo;
    private Bundle mArgs;
    private AppCompatActivity mActivity;
    String refNo;
    private boolean isPermissionGranted = false;
    String type;
    private int imageCount=0;
    private ImageLoader imageLoader;
    private int chekNoimage;
    private int imgCount=0;
    Bitmap thumbnail = null;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 4;
    private Bitmap mImageToBeAttachedDefectFound;
    private String mImagePathToBeAttached;
    private final int REQUEST_CHOOSE_PHOTO = 2;
    private final int REQUEST_TAKE_PHOTO = 1;
    private final int THUMBNAIL_SIZE = 75;
    private CharSequence[] items;
    private ReceiveComplaintRespondService receiveComplaintRespond_service;
    List<JobList> jobLists = new ArrayList<>();
    private  List<DocumentType> docTypeList;
    List<EmployeeList> employeeList = new ArrayList<>();
    List<DocumentType> assetStatus = new ArrayList<>();
    List<DocumentType> assetCondition = new ArrayList<>();
    private boolean noImageAttached=false;
    private ArrayList<CustomRecyclerViewItem> itemList  =new ArrayList<>();
    private CustomRecyclerViewDataAdapter customRecyclerViewDataAdapter = null;
    FragmentAssetAuditBinding binding;
    AssetInfoTrans trans;

    public AssetAuditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = getArguments();
        if (getArguments() != null) {
            assetInfo = (AssetInfo) mArgs.getSerializable(ARGS_ASSETINFO);
            type=mArgs.getString(ARGS_ASSET_VERIFICATION_TYPE);
        }
        refNo=getReferenceNo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_asset_audit, container, false);

        mActivity = (AppCompatActivity) getActivity();

        initView();
        setupActionBar();
        setHasOptionsMenu(true);
        fetchAssetInfo();
        imageLoader = ImageLoader.getInstance();
        return binding.getRoot();

    }


    public void initView(){

        setUpRecyclerview();

        receiveComplaintRespond_service = new ReceiveComplaintRespondService(mActivity);
        receiveComplaintRespond_service.setmCallbackImages(this);

        getImage();


        binding.btnJobNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showJobNoDialog();

            }
        });

        binding.btnCustodian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showEmployeeNoDialog();
            }
        });

        binding.btnAssetStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showAssetStatusDialog();
            }
        });

        binding.btnWarrantyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDocTypeDialog();
            }
        });

        binding.btnAssetCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           showAssetConditionDialog();
            }
        });

        binding.btnWarrantyExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDateSelection();

            }
        });


        binding.txtRefNo .setText(assetInfo.getAssetTag()+" - "+assetInfo.getEquipmentName());
        binding.txtJobNo  .setText(assetInfo.getJobNo());
        binding.txtAssetLocation  .setText(assetInfo.getAssetLocation());
        binding.txtCustodian  .setText(assetInfo.getCustodian());
        binding.txtCustodianName .setText(assetInfo.getCustodianName());
        binding.txtRemarks .setText(assetInfo.getRemark());
        binding.txtAssetStatus .setText(assetInfo.getStatus());
        binding.txtAssetCondition .setText(assetInfo.getCondition());
        binding.txtWarrantyType .setText(assetInfo.getWarrantyType());
        binding.txtWarrantyExpiry  .setText(assetInfo.getWarrantyExpiry());
        binding.txtWarrantyRemarks  .setText(assetInfo.getWarrantyRemarks());
        binding.txtSerialNo  .setText(assetInfo.getSerialNo());

        binding.fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAsset();
            }
        });

        if(type.equalsIgnoreCase("Transfer")){

            binding.btnAssetStatus.setVisibility(View.INVISIBLE);
            binding.linearMakeModel.setVisibility(View.GONE);

        }
        else{

            binding.fabSave.setImageResource(R.drawable.arrow);

        }


    }

   public void setUpRecyclerview() {

        // Create the grid layout manager with 2 columns.
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Set layout manager.
       binding.customRefreshRecyclerView.setLayoutManager(layoutManager);

        itemList = new ArrayList<CustomRecyclerViewItem>();
        // Create car recycler view data adapter with car item list.
        customRecyclerViewDataAdapter = new CustomRecyclerViewDataAdapter(itemList,1,this,getContext(),true);
        // Set data adapter.
       binding.customRefreshRecyclerView.setAdapter(customRecyclerViewDataAdapter);

        // Scroll RecyclerView a little to make later scroll take effect.
       binding.customRefreshRecyclerView.scrollToPosition(1);

    }


    public void setupActionBar() {
        Toolbar mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        TextView tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Asset "+type);
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

    @Override
    public void onReceiveAssetInfo(List<AssetInfo> object) {

    }

    @Override
    public void onReceiveFailureAssetInfo(String toString) {

    }

    @Override
    public void onReceiveJobList(List<JobList> object) {

        jobLists=object;

    }

    @Override
    public void onReceiveFailureJobList(String toString) {

    }

    @Override
    public void onReceiveEmployeeList(List<EmployeeList> object) {
        employeeList=object;
    }

    @Override
    public void onReceiveFailureEmployeeList(String toString) {

    }

    @Override
    public void onReceiveAssetType(String type,List<DocumentType> object, int from) {

        if(type.equals("ASC")){
            assetCondition=object;
        }
        else{
            assetStatus=object;
        }

    }

    @Override
    public void onReceiveFailureJAssetType(String err, int from) {

    }

    @Override
    public void onSuccessSaveAsset(String strMsg, int mode) {

        AppUtils.hideProgressDialog();
        showDialog(mActivity,"Request created successfully.","Reference Number : "+trans.getRefNo());

    }

    @Override
    public void onFailureSaveAsset(String strErr, int mode) {

        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity,strErr,"");

    }

    @Override
    public void onSuccessLocationOpco(List<OpcoDetail> opco, int mode) {

    }

    @Override
    public void onFailureLocationOpco(String strMsg, int mode) {

    }

    public void fetchAssetInfo() {

        new AssetVerificationRepository(mActivity,this).getJobList(assetInfo.getOpco());
        new AssetVerificationRepository(mActivity,this).getEmployeeList();
        new AssetVerificationRepository(mActivity,this).getDocType("ATS");
        new AssetVerificationRepository(mActivity,this).getDocType("ASC");
        new SupportDocumentRepository(mActivity,this).getDocType("AWT");

    }

    public  void showJobNoDialog(){

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

                                    binding.txtJobNo.setText(jobLists.get(strArraySiteName.indexOf(text)).getJobNo());

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


    public  void showEmployeeNoDialog(){

        if(employeeList!=null && employeeList.size()>0){

            try {
                final  ArrayList strArraySiteName = new ArrayList();
                for (EmployeeList entity : employeeList) {
                    strArraySiteName.add(entity.getEmployeeId()+ " - "+entity.getEmployeeName());
                }

                strArraySiteName.add(space);

                FilterableListDialog.create(
                        mActivity,
                        ("Select Employee"),
                        strArraySiteName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {
                                if(!text.equals(space)){
                                    binding.txtCustodian.setText(employeeList.get(strArraySiteName.indexOf(text)).getEmployeeId());
                                    binding.txtCustodianName.setText(employeeList.get(strArraySiteName.indexOf(text)).getEmployeeName());
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


    public  void showAssetConditionDialog(){

        if(assetCondition!=null && assetCondition.size()>0){

            try {
                final  ArrayList strArraySiteName = new ArrayList();
                for (DocumentType entity : assetCondition) {
                    strArraySiteName.add(entity.getCategoryName());
                }

                strArraySiteName.add(space);

                FilterableListDialog.create(
                        mActivity,
                        ("Select Asset Condition"),
                        strArraySiteName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {
                                if(!text.equals(space)){
                                    binding.txtAssetCondition.setText(text);
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

    public  void showAssetStatusDialog(){

        if(assetStatus!=null && assetStatus.size()>0){

            try {
                final  ArrayList strArraySiteName = new ArrayList();
                for (DocumentType entity : assetStatus) {
                    strArraySiteName.add(entity.getCategoryName());
                }

                strArraySiteName.add(space);

                FilterableListDialog.create(
                        mActivity,
                        ("Select Asset Status"),
                        strArraySiteName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {
                                if(!text.equals(space)){
                                    binding.txtAssetStatus .setText(text);
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

    public  void showDocTypeDialog(){

        if(docTypeList!=null && docTypeList.size()>0){
            try {
                final ArrayList strArraySiteName = new ArrayList();
                for (DocumentType entity : docTypeList) {
                    strArraySiteName.add(entity.getCategoryName());
                }

                strArraySiteName.add(space);
                FilterableListDialog.create(
                        mActivity,
                        ("Select Warranty type"),
                        strArraySiteName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {

                                if(!text.equals(space)){
                                    binding.txtWarrantyType .setText(text);
                                }
                            }
                        })
                        .show();
            } catch (Exception ex) {
                ex.printStackTrace();
            } }
        else{
            AppUtils.showToast(mActivity,"No Data to show.");
        }
    }

    public void saveAsset(){

        trans= new AssetInfoTrans();
        trans.setOpco(assetInfo.getOpco());
        trans.setRefNo(refNo);
        trans.setTransType(type);
        trans.setAssetTag(assetInfo.getAssetTag());
        trans.setJobNo(assetInfo.getJobNo());
        trans.setAssetLocation(assetInfo.getAssetLocation());
        trans.setCustodianNo(assetInfo.getCustodian());
        trans.setCustodianName(assetInfo.getCustodianName());
        trans.setAssetStatus(assetInfo.getStatus());
        trans.setAssetCondition(assetInfo.getCondition());
        trans.setAssetRemarks(assetInfo.getRemark());
        trans.setWarrantyType(assetInfo.getWarrantyType());
        trans.setWarrantyExpiry(assetInfo.getWarrantyExpiry());
        trans.setWarrantyRemarks(assetInfo.getWarrantyRemarks());
        trans.setSerialNo(assetInfo.getSerialNo());
        trans = assignNewValue(trans);
        trans.setTransStatus("Requested"); // fixed value
        trans.setAuditBy(getUseID());
        trans.setCreatedBy(getUseID());
        trans.setModifiedBy(getUseID());
        trans.setCheckListCode(assetInfo.getCheckListCode());
        trans.setCheckListName(assetInfo.getCheckListName());

        if(type.equalsIgnoreCase("Transfer")){

            AppUtils.showProgressDialog(mActivity,"",false);
            new AssetVerificationRepository(mActivity,this).saveAsset(trans);

        }
        else {

           if(itemList.size()>1){
               fetchCheckList(trans);
           }
           else{
               AppUtils.showSnackBar(R.id.coordinatorLayout,binding.getRoot(),  "Upload image before proceeding next.");
           }

        }
    }

    private void fetchCheckList(AssetInfoTrans trans){

        showProgressDialog(mActivity, "Loading...", false);
        SaveRatedServiceRequest ratedServiceRequest = new SaveRatedServiceRequest();
        ratedServiceRequest.setCompanyCode(trans.getOpco());
        ratedServiceRequest.setChkCode(trans.getCheckListCode());
        ratedServiceRequest.setPpmNo(trans.getRefNo());
        new GetPpmResponseService(mActivity, this).getSavePpmCheckList(ratedServiceRequest);

    }

    public void loadFragment(final Fragment fragment,List<ObjectMonthly> customerRemarks ,AssetInfoTrans asset, String tag) {

        ArrayList<ObjectMonthly> obj = new ArrayList<>(customerRemarks.size());
        obj.addAll(customerRemarks);

        Bundle mdata = new Bundle();
        mdata.putSerializable(AppUtils.ARGS_ASSETINFO, asset);
        mdata.putSerializable(AppUtils.ARGS_CHECKLIST, obj);
        fragment.setArguments(mdata);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

    }

    public void loadFragment(final Fragment fragment, AssetInfoTrans asset, String tag) {


        Bundle mdata = new Bundle();
        mdata.putSerializable(AppUtils.ARGS_ASSETINFO, asset);
        fragment.setArguments(mdata);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

    }


    public String getReferenceNo(){

        return (assetInfo.getOpco()+"-"+new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()));

    }

    public AssetInfoTrans assignNewValue(AssetInfoTrans trans){

        trans.setJobNewNo(binding.txtJobNo.getText().toString());
        trans.setAssetNewLocation(binding.txtAssetLocation .getText().toString());
        trans.setCustodianNewNo(binding.txtCustodian .getText().toString());
        trans.setCustodianNewName(binding.txtCustodianName .getText().toString());
        trans.setAssetNewStatus(binding.txtAssetStatus .getText().toString());
        trans.setAssetNewCondition(binding.txtAssetCondition .getText().toString());
        trans.setAssetNewRemarks(binding.txtRemarks.getText().toString());
        trans.setWarrantyNewType(binding.txtWarrantyType .getText().toString());
        trans.setWarrantyNewExpiry(binding.txtWarrantyExpiry .getText().toString());
        trans.setWarrantyNewRemarks(binding.txtWarrantyRemarks .getText().toString());
        trans.setAssetNewSerialNo(binding.txtSerialNo.getText().toString());

        if(!type.equalsIgnoreCase("Transfer")){
            trans.setMake(binding.txtAssetMake .getText().toString());
            trans.setModel(binding.txtAssetModel.getText().toString());
        }

        return trans;
    }


    public String getUseID(){

        String mStrEmpId="";

        Gson gson = new Gson();
        SharedPreferences mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
        String  mStringJson = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
        if (mStringJson != null) {
            Login  user = gson.fromJson(mStringJson, Login.class);
             mStrEmpId = user.getEmployeeId();
        }
        return mStrEmpId;
    }

    public void showDateSelection(){
        AppUtils.datePickerDialog(mActivity, this, null, null);
    }

    @Override
    public void onDateReceivedSuccess(String strDate) {

        binding.txtWarrantyExpiry .setText(changeDateFormat(strDate));

    }

    public String changeDateFormat(String dateOrg){
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
            return sdf2.format(sdf.parse(dateOrg));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


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
                                            for (int i = 0; i < 2; ++i) {
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


    public void getImage(){

        RCDownloadImageRequest imageRequest = new RCDownloadImageRequest();
        imageRequest.setOpco(assetInfo.getOpco());
        imageRequest.setDocType(type);
        imageRequest.setRef_doc(refNo);
        imageRequest.setTransactionType("CAV");
        imageRequest.setGeneralRefNo(assetInfo.getAssetTag());
        receiveComplaintRespond_service.getRespondImage(imageRequest, getActivity());

    }

    @Override
    public void onImageClick(int pos, String base64) {
        imageCount=pos;
        displayAttachImageDialog(pos,base64);
    }


    @Override
    public void onImageSaveReceivedSuccess1(RCDownloadImage imageEntity, int mode) {
        processImage(imageEntity,true);
        AppUtils.hideProgressDialog();
    }

    @Override
    public void onImageSaveReceivedSuccess(DefectDoneImageUploaded mImageEntity, int mode) {

    }

    @Override
    public void onImageReceivedSuccess(RCDownloadImage imageEntity, int mode) {
        processImage(imageEntity,false);
    }

    @Override
    public void onImageSaveReceivedFailure(String strErr, int mode) {
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity, strErr);
    }

    @Override
    public void onImageReceivedFailure(String strErr, int mode) {

    }

    @Override
    public void onAllImagesReceived(List<DFoundWDoneImageEntity> mImageEntities, int mode) {

    }


    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToReadExternalStorage(int count,String base64) {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }
            }
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            isPermissionGranted = true;
            displayAttachImageDialog(count,base64);
        }
    }

    private void displayAttachImageDialog(final int count, final String base64) {
        if (!isPermissionGranted) {
            getPermissionToReadExternalStorage( count, base64);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        if (base64 == null) {

            items = new CharSequence[]{"Take photo", "No image"};
            builder.setTitle("Add  photo");

        }
        else if(base64.equalsIgnoreCase("noImage")){
            items = new CharSequence[]{"Take photo"};
            builder.setTitle("Add  photo");
        } else {
            items = new CharSequence[]{"Take photo", "View photo"}; // , "Delete photo"
            builder.setTitle("Update and view photo");
        }
        builder.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                dispatchTakePhotoIntent();
                                break;

                            case 1:

                                if (items[item].toString().equalsIgnoreCase("View photo")) {
                                    dispatchViewPhotoIntent(base64);
                                } else if (items[item].toString().equalsIgnoreCase("No image")) {
                                    //to check image type
                                    dialog.dismiss();
                                    showNoImageAlert(count);
                                    chekNoimage = 1;
                                }

                                break;

                            case 2:
                                dialog.dismiss();
                                showNoImageAlert(count);
                                chekNoimage = 1;
                                break;

                        }
                    }
                });
        builder.show();
    }

    private void dispatchTakePhotoIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {

                }
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg", storageDir);
        mImagePathToBeAttached = image.getAbsolutePath();
        return image;
    }

    private void dispatchViewPhotoIntent(final String base64) {


        new AsyncTask<Void, RCDownloadImage, RCDownloadImage>() {
            @Override
            protected void onPreExecute() {
                // update the UI (this is executed on UI thread)
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                super.onPreExecute();
            }

            @Override
            protected RCDownloadImage doInBackground(Void... params) {
                // your async action

                ArrayList<String> images=new ArrayList<>();
                images.add(base64);

                return  new RCDownloadImage(images, "B",0);
            }

            @Override
            protected void onPostExecute(RCDownloadImage aVoid) {
                AppUtils.hideProgressDialog();

                Display display = mActivity.getWindowManager().getDefaultDisplay();
                int width = display.getWidth();
                int height = display.getHeight();

                loadPhoto(aVoid.getBase64Image().get(0),width,height);

                super.onPostExecute(aVoid);
            }
        }.execute();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            File file = new File(mImagePathToBeAttached);
            if (file.exists()) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mImagePathToBeAttached, options);
                options.inJustDecodeBounds = false;

                mImageToBeAttachedDefectFound = BitmapFactory.decodeFile(mImagePathToBeAttached, options);
                thumbnail =
                        ThumbnailUtils.extractThumbnail(
                                mImageToBeAttachedDefectFound, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
                file.delete();
                submitImage("B");
            }
            mImagePathToBeAttached = null;
        } else if (requestCode == REQUEST_CHOOSE_PHOTO) {
            try {
                Uri uri = data.getData();
                ContentResolver resolver = mActivity.getContentResolver();

                    mImageToBeAttachedDefectFound = MediaStore.Images.Media.getBitmap(resolver, uri);


                AssetFileDescriptor asset = resolver.openAssetFileDescriptor(uri, "r");
                thumbnail =
                        ImageUtil.thumbnailFromDescriptor(
                                asset.getFileDescriptor(), THUMBNAIL_SIZE, THUMBNAIL_SIZE);


            } catch (IOException e) {

            }
        }

    }

    public void showNoImageAlert(final int count){

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Are you sure you want to add No Image?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //   noImageAvailabe(count,convertImageviewBase64());
                        noImageAvailabe(count,"");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void noImageAvailabe(int count,String base64) {
        AppUtils.showProgressDialog(
                mActivity, getString(R.string.lbl_no_image_uploading), false);
        DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity(type);
        imageEntity.setOpco(assetInfo.getOpco());
        imageEntity.setFileType("jpg");
        imageEntity.setTransactionType("CAV");
        imageEntity.setDocType(type);
        imageEntity.setRefDocNo(refNo);
        imageEntity.setCreatedBy(getUseID());
        imageEntity.setModifiedBy(getUseID());
        imageEntity.setImageCount(count);
        imageEntity.setBase64Image(base64);
        imageEntity.setGeneralRefNo(assetInfo.getAssetTag());
        postImageToServer(imageEntity);
    }

    private void postImageToServer(DFoundWDoneImageEntity saveRequest) {

        if (checkInternet(getContext())) {
            receiveComplaintRespond_service.saveComplaintRespondImageData1(saveRequest, getActivity());
        }

    }

    private void loadPhoto(String base64,int width, int height) {

        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup) binding.getRoot().findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageBitmap(AppUtils.getDecodedString(base64));
        image.getLayoutParams().height = height;
        image.getLayoutParams().width = width;
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);
        image.requestLayout();
        dialog.setContentView(layout);
        dialog.show();
    }


    @Override
    public void onSingleImagePicked(String Str_Path) {
        try {

            Str_Path = "file://" + Str_Path;
            imageLoader.loadImage(
                    Str_Path,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            if (loadedImage != null) {

                                if (AppUtils.checkImageFileSize(loadedImage)) {
                                    mImageToBeAttachedDefectFound = loadedImage;


                                } else {
                                    //AppUtils.showDialog(mActivity, getString(R.string.file_size_more_then));
                                    mImageToBeAttachedDefectFound = loadedImage;

                                }
                                submitImage("B");
                            }

                            //      AppUtils.hideProgressDialog();
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void submitImage( final String docType) {

        try {

            new AsyncTask<Void, DFoundWDoneImageEntity, DFoundWDoneImageEntity>() {
                @Override
                protected void onPreExecute() {

                    AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                    super.onPreExecute();
                }

                @Override
                protected DFoundWDoneImageEntity doInBackground(Void... params) {

                    DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity("" + docType);
                    imageEntity.setOpco(assetInfo.getOpco());
                    imageEntity.setFileType("jpg");
                    imageEntity.setTransactionType("CAV");
                    imageEntity.setDocType(type);
                    imageEntity.setRefDocNo(refNo);
                    imageEntity.setCreatedBy(getUseID());
                    imageEntity.setModifiedBy(getUseID());
                    imageEntity.setImageCount(imageCount);
                    imageEntity.setGeneralRefNo(assetInfo.getAssetTag());
                    imageEntity.setBase64Image(AppUtils.getEncodedString(mImageToBeAttachedDefectFound));
                    return imageEntity;
                }

                @Override
                protected void onPostExecute(DFoundWDoneImageEntity imageEntity) {
                    postImageToServer(imageEntity);
                    super.onPostExecute(imageEntity);
                }
            }.execute();

        } catch (Exception e) {
            AppUtils.hideProgressDialog();
            e.printStackTrace();
        }
    }

    public void processImage(final RCDownloadImage imageEntity, final boolean fromUpload) {

        try {
            itemList = new ArrayList<CustomRecyclerViewItem>();
            imgCount=imageEntity.getImageCount();

            if(imageEntity.getBase64Image().size()==0){
                noImageAttached=true;
            }
            else{
                noImageAttached=false;
            }


            if (!imageEntity.getBase64Image().isEmpty() && imageEntity.getDocType() != null) {


                new AsyncTask<Void, Boolean, Boolean>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected Boolean doInBackground(Void... params) {

                        for(int i=0;i<imageEntity.getBase64Image().size();i++){
                            mImageToBeAttachedDefectFound = AppUtils.getDecodedString(imageEntity.getBase64Image().get(i));
                            DFoundWDoneImageEntity imageEntity1 = new DFoundWDoneImageEntity("B" );
                            imageEntity1.setOpco(assetInfo.getOpco());
                            imageEntity1.setFileType("jpg");
                            imageEntity1.setTransactionType("CAV");
                            imageEntity1.setRefDocNo(refNo);
                            imageEntity1.setDocType(type);
                            imageEntity1.setCreatedBy(getUseID());
                            imageEntity1.setModifiedBy(getUseID());
                            imageEntity1.setGeneralRefNo(assetInfo.getAssetTag());
                            imageEntity1.setImageCount(i);
                            imageEntity1.setActStartDate(DateFormat.getDateTimeInstance().format(new Date()));
                            if (mImageToBeAttachedDefectFound != null) {
                                imageEntity1.setBase64Image(AppUtils.getEncodedString(mImageToBeAttachedDefectFound));
                            }
                            CustomRecyclerViewItem item = new CustomRecyclerViewItem();
                            item.setDet(imageEntity1);
                            item.setThums(ThumbnailUtils.extractThumbnail(
                                    mImageToBeAttachedDefectFound, THUMBNAIL_SIZE, THUMBNAIL_SIZE));
                            itemList.add(item);
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean aVoid) {

                        processRecycleview();

//                        img_upload_count.setVisibility(View.VISIBLE);
//                        img_upload_count.setText(imageEntity.getBase64Image().size()+"/"+imgCount+" Image uploaded");

                        if(fromUpload){
                            AppUtils.hideProgressDialog();
                            AppUtils.showSnackBar(R.id.coordinatorLayout,binding.getRoot(), "Image has been successfully Saved.");
                        }

                        super.onPostExecute(aVoid);
                    }
                }.execute();
            }

            else{

                customRecyclerViewDataAdapter = new CustomRecyclerViewDataAdapter(itemList,imgCount,this,getContext(),false);
                binding.customRefreshRecyclerView.setAdapter(customRecyclerViewDataAdapter);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processRecycleview(){
        customRecyclerViewDataAdapter = new CustomRecyclerViewDataAdapter(itemList,imgCount,this,getContext(),false);
        binding.customRefreshRecyclerView.setAdapter(customRecyclerViewDataAdapter);
    }

    @Override
    public void onDocTypeListSuccess(List<DocumentType> rx, int from) {
        docTypeList=rx;
    }

    @Override
    public void onDocTypeListFailure(String err, int from) {

    }

    @Override
    public void onReceivedPPMParameterSuccess(GetPpmParamValue customerRemarks) {

    }

    @Override
    public void onReceivedSuccess(List<ObjectMonthly> customerRemarks) {

        AppUtils.hideProgressDialog();
        loadFragment(new AssetCheckListFragment(),customerRemarks, trans, TAG_FRAGMENT_ASSET_FINDINGS);

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
        loadFragment(new AssetFindingsFragment(), trans, TAG_FRAGMENT_ASSET_FINDINGS);
    }

}