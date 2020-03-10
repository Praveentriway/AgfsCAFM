package com.daemon.emco_android.ui.fragments.preventive.ppmschedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.daemon.emco_android.model.common.EmployeeTrackingDetail;
import com.daemon.emco_android.service.GPSTracker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.CustomerFeedbackRepository;
import com.daemon.emco_android.repository.remote.GetPostRateServiceService;
import com.daemon.emco_android.ui.components.CustomTextInputLayout;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.listeners.CustomerFeedback;
import com.daemon.emco_android.listeners.RateAndShareListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.PPMDetails;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.checkInternet;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVED_COMPALINTS;

/**
 * Created by subbu on 25/11/16.
 */
public class Fragment_PM_RateService extends Fragment
        implements View.OnClickListener, SignaturePad.OnSignedListener, RateAndShareListener, CustomerFeedback {
    private static final String TAG = Fragment_PM_RateService.class.getSimpleName();
    private static String mCustomerRemarks = null,
            select_customer_rank = null,
            mComplaintno = null,
            mComplaintSite = null,
            mOpco = null;
    private Gson gson = new Gson();
    private Handler mHandler;
    private AppCompatActivity mActivity;
    private List<PpmScheduleDocBy> ppmScheduleDocByList;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private TextView tv_lbl_cus_satisfaction,
            tv_lbl_cus_remarks,
            tv_lbl_tech_remarks;
    private TextView tv_select_customer_rank, tv_select_signstatus;
    private SignaturePad signaturePad;
    private Button  btnClear;
    private FloatingActionButton  btnSave;
    private CustomTextInputLayout til_remarks_detail;
    private AppCompatEditText tie_customer_remarks;
    private ImageView iv_very_good, iv_good, iv_excellent, iv_Satisfactory, iv_poor;
    private LinearLayout ll_remarks_detail;
    private CoordinatorLayout cl_main;
    private ImageView img_signature;
    private Toolbar mToolbar;
    private Bundle mArgs = new Bundle();
    private String mLoginData = null;
    private String mStrEmpId = null;
    private PPMDetails receiveComplaintViewEntity;
    private List<String> listCustomerRank = null;
    private Type baseType = new TypeToken<List<String>>() {
    }.getType();
    private GetPostRateServiceService mGetPostRateService;
    private CustomerFeedbackRepository mCustomerFeedbackService;
    private String complaint_number = "", opco_number = "", complaint_site = "", checkPPmList = "";
    private PpmScheduleDocBy ppmScheduleDocBy;
    private String mUnSignedPage = AppUtils.ARGS_RECEIVECOMPLAINT_PAGE;
    private boolean feedbackUpdate=false;
    private boolean signatureUpdated=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }
            mManager = mActivity.getSupportFragmentManager();
            font = App.getInstance().getFontInstance();
            mArgs = getArguments();
            mHandler = new Handler();
            mGetPostRateService =
                    new GetPostRateServiceService(mActivity,  this);

            mCustomerFeedbackService=
                    new CustomerFeedbackRepository(mActivity,  this);
            getParcelableData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getParcelableData() {
        Log.d(TAG, "getParcelableData");
        getLocalData();
        if (mArgs != null) {
            if (mArgs.containsKey(AppUtils.ARGS_PPMDetails_List)) {
                receiveComplaintViewEntity = mArgs.getParcelable(AppUtils.ARGS_PPMDetails_List);
                mComplaintno = receiveComplaintViewEntity.getCompCode();
                mComplaintSite = receiveComplaintViewEntity.getContractNo();
                mOpco = receiveComplaintViewEntity.getContractNo();
            }
            if (mArgs != null) {
                ppmScheduleDocBy = mArgs.getParcelable(AppUtils.ARGS_PPM_FEEDBACK);
                if (mArgs.getString(AppUtils.ARGS_PPM_FEEDBACK_CHECK) != null && !mArgs.getString(AppUtils.ARGS_PPM_FEEDBACK_CHECK).isEmpty()) {
                    checkPPmList = mArgs.getString(AppUtils.ARGS_PPM_FEEDBACK_CHECK);
                }
            }
            if (mArgs.getString("complaint_number") != null
                    && mArgs.getString("opco_number") != null
                    && mArgs.getString("complaint_site") != null)
                if (!mArgs.getString("complaint_number").equals("")
                        && !mArgs.getString("opco_number").equals("")
                        && !mArgs.getString("complaint_site").equals("")) {
                    complaint_number = mArgs.getString("complaint_number");
                    opco_number = mArgs.getString("opco_number");
                    complaint_site = mArgs.getString("complaint_site");
                }


            // this condition is to load rating either from before ppm page customer signature or after ppm feed back page
            if(ppmScheduleDocBy.getPpmnoList()!=null){
                mCustomerFeedbackService.fetchCustomerFeedback(ppmScheduleDocBy.getCompanyCode(),ppmScheduleDocBy.getPpmnoList().get(0).getPpmNo(),this);

            }
            else{
                mCustomerFeedbackService.fetchCustomerFeedback(ppmScheduleDocBy.getCompanyCode(),ppmScheduleDocBy.getPpmNo(),this);

            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = null;
        try {
            rootView = (View) inflater.inflate(R.layout.fragment_rate_service, container, false);
            initUI(rootView);
            setProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    private void initUI(View rootView) {
        Log.d(TAG, "initUI");
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            ll_remarks_detail = (LinearLayout) rootView.findViewById(R.id.ll_remarks_detail);
            tv_lbl_cus_satisfaction = (TextView) rootView.findViewById(R.id.tv_lbl_cus_satisfaction);
            tv_lbl_cus_remarks = (TextView) rootView.findViewById(R.id.tv_lbl_cus_remarks);
          //  tv_lbl_cus_sign = (TextView) rootView.findViewById(R.id.tv_lbl_cus_sign);
            tv_lbl_tech_remarks = (TextView) rootView.findViewById(R.id.tv_lbl_tech_remarks);
            img_signature = (ImageView) rootView.findViewById(R.id.img_signature);
            // logo changes R id
            iv_excellent = (ImageView) rootView.findViewById(R.id.iv_excellent);
            iv_very_good = (ImageView) rootView.findViewById(R.id.iv_poor);
            tie_customer_remarks = (AppCompatEditText) rootView.findViewById(R.id.tie_customer_remarks);
            iv_good = (ImageView) rootView.findViewById(R.id.iv_very_good);
            iv_Satisfactory = (ImageView) rootView.findViewById(R.id.iv_good);
            iv_poor = (ImageView) rootView.findViewById(R.id.iv_Satisfactory);

            til_remarks_detail = (CustomTextInputLayout) rootView.findViewById(R.id.til_remarks_detail);

            tv_select_customer_rank = (TextView) rootView.findViewById(R.id.tv_select_customer_rank);
            tv_select_signstatus = (TextView) rootView.findViewById(R.id.tv_select_signstatus);
            signaturePad = (SignaturePad) rootView.findViewById(R.id.signaturePad);
            btnSave = (FloatingActionButton) rootView.findViewById(R.id.btnSave);
            btnClear = (Button) rootView.findViewById(R.id.btnClear);
            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.lbl_rate_and_share));
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

    private void setProperties() {
        Log.d(TAG, "setProperties");
        try {
            tv_lbl_cus_remarks.setText(
                    Html.fromHtml(getString(R.string.lbl_customer_remarks) + AppUtils.mandatory));
            tv_lbl_tech_remarks.setText(
                    Html.fromHtml(getString(R.string.lbl_tech_remarks) + AppUtils.mandatory));

            tv_lbl_cus_satisfaction.setTypeface(font.getHelveticaRegular());
            tv_lbl_cus_remarks.setTypeface(font.getHelveticaRegular());
            tv_lbl_tech_remarks.setTypeface(font.getHelveticaRegular());
            tv_select_customer_rank.setTypeface(font.getHelveticaRegular());
            tv_select_signstatus.setTypeface(font.getHelveticaRegular());
            til_remarks_detail.setTypeface(font.getHelveticaRegular());


            btnClear.setTypeface(font.getHelveticaRegular());

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitForm();
                }
            });
            btnClear.setOnClickListener(this);
            signaturePad.setOnSignedListener(this);

            iv_very_good.setOnClickListener(this);
            iv_good.setOnClickListener(this);
            iv_excellent.setOnClickListener(this);
            iv_Satisfactory.setOnClickListener(this);
            iv_poor.setOnClickListener(this);

            tie_customer_remarks.addTextChangedListener(new MyTextWatcher(tie_customer_remarks));

            AppUtils.closeInput(cl_main);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_customer_rank:
                // getCustomerRemarks();
                break;
            case R.id.btnSave:

                break;
            case R.id.btnClear:
                signaturePad.clear();
                if(img_signature.getVisibility()==View.VISIBLE){
                    img_signature.setVisibility(View.GONE);
                    signaturePad.setVisibility(View.VISIBLE);
                    signatureUpdated=true;
                }

                break;
            case R.id.iv_excellent:
                if (listCustomerRank != null) customerRemarkUpdate(listCustomerRank.get(0));
                break;
            case R.id.iv_very_good:
                if (listCustomerRank != null) customerRemarkUpdate(listCustomerRank.get(2));
                break;
            case R.id.iv_good:
                if (listCustomerRank != null) customerRemarkUpdate(listCustomerRank.get(3));
                break;
            case R.id.iv_Satisfactory:
                if (listCustomerRank != null) customerRemarkUpdate(listCustomerRank.get(4));
                break;
            case R.id.iv_poor:
                if (listCustomerRank != null) customerRemarkUpdate(listCustomerRank.get(1));
                break;
        }
    }

    private void getLocalData() {
        Log.d(TAG, "getLocalData");
        getCustomerRemarkData();
        // getTechRemarkData();

    }

    private void getCustomerRemarkData() {

            if (checkInternet(getContext())) {
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                mGetPostRateService.getCustomerRemarksData();
            } else {
                String strJsonCr = mPreferences.getString(AppUtils.SHARED_CUSTOMER_REMARKS, null);
                Log.d(TAG, "str json :;" + strJsonCr);
                if (strJsonCr != null && strJsonCr.length() > 0) {
                    listCustomerRank = gson.fromJson(strJsonCr, baseType);
                }
            }

    }

    private void customerRemarkUpdate(String CustomerRank) {
        select_customer_rank = CustomerRank;
        tv_select_customer_rank.setText(select_customer_rank);
        tv_select_customer_rank.setTypeface(font.getHelveticaBold());
        tv_select_customer_rank.setOnClickListener(null);
    }

    private void submitForm() {
        Log.d(TAG, "submitForm");
        AppUtils.closeInput(cl_main);
        String msgErr = "";
        if (!validateCustomerRemarks()) return;

        if (select_customer_rank == null) {
            AppUtils.setErrorBg(tv_select_customer_rank, true);
            msgErr = getString(R.string.lbl_select_customer_remarks);
        } else AppUtils.setErrorBg(tv_select_customer_rank, false);

        if (ppmScheduleDocBy != null) {
            if (signaturePad.isEmpty()) {
                AppUtils.showDialog(mActivity, "Customer signature should not be empty");
                AppUtils.closeInput(cl_main);
                return;
            }
        }

        // }

        if (msgErr != "") {
            AppUtils.showDialog(mActivity, "Please fill all the mandatory fields");
            return;
        }

        if (ppmScheduleDocBy != null && !signaturePad.isEmpty()) {
            if (checkPPmList.equalsIgnoreCase("ppmcheckList")) {
                ppmScheduleDocByList = new ArrayList<>();
                PpmScheduleDocBy ppmScheduleDocBy1 = null;
                ppmScheduleDocBy1 = new PpmScheduleDocBy();
                ppmScheduleDocBy1.setPpmNo(ppmScheduleDocBy.getPpmNo());
                ppmScheduleDocByList.add(ppmScheduleDocBy1);
                ppmScheduleDocBy.setPpmnoList(ppmScheduleDocByList);
                ppmScheduleDocBy.setOpco(ppmScheduleDocBy.getCompanyCode());
            }
            SaveRatedServiceRequest serviceRequest = new SaveRatedServiceRequest();
            AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
            serviceRequest.setCustomerFeedback(select_customer_rank);
            serviceRequest.setCustomerRemarks(mCustomerRemarks);
            serviceRequest.setPpmRefNoList(ppmScheduleDocBy.getPpmnoList());
            //serviceRequest.setPpmRefNo(ppmScheduleDocBy.getPpmNo());
            serviceRequest.setOpco(ppmScheduleDocBy.getCompanyCode());
            serviceRequest.setCreatedBy(mStrEmpId);
            serviceRequest.setTransactionType("P");

            if(feedbackUpdate){
                if(signatureUpdated){
                    serviceRequest.setCustomerSignature(
                            AppUtils.getEncodedString(signaturePad.getSignatureBitmap()));
                }
                else{
                    // dont add signature to the object
                }
            }
            else{
                serviceRequest.setCustomerSignature(
                        AppUtils.getEncodedString(signaturePad.getSignatureBitmap()));
            }


            mGetPostRateService.saveCustomerFeedback(serviceRequest);

        }

    }


    private boolean validateCustomerRemarks() {
        mCustomerRemarks = tie_customer_remarks.getText().toString().trim();
        if (mCustomerRemarks.isEmpty()) {
            AppUtils.showToast(mActivity,getString(R.string.msg_enter_remark_detail));
            requestFocus(tie_customer_remarks);
            return false;
        } else {
          //  til_customer_remarks.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mActivity
                    .getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onStartSigning() {
        Log.d(TAG, "onStartSigning");
    }

    @Override
    public void onSigned() {
        Log.d(TAG, "onSigned");
    }

    @Override
    public void onClear() {
        Log.d(TAG, "onClear");
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
                // mActivity.onBackPressed();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                Fragment _fragment = new MainLandingUI();
                FragmentTransaction _transaction = mManager.beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCustomerRemarksReceived(List<String> customerRemarks) {
        Log.d(TAG, "onCustomerRemarksReceived");
        listCustomerRank = customerRemarks;
        AppUtils.hideProgressDialog();
        String strCusRemarks = gson.toJson(customerRemarks, baseType);
        mEditor.putString(AppUtils.SHARED_CUSTOMER_REMARKS, strCusRemarks);
        mEditor.commit();
    }

    @Override
    public void onRateShareReceivedError(String strErr) {
        Log.d(TAG, "onRateShareReceivedError");
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity, strErr);
    }

    @Override
    public void onSaveRateShareReceived(String strErr) {

        EmployeeTrackingDetail emp=new EmployeeTrackingDetail();
        emp.setCompCode(ppmScheduleDocBy.getCompanyCode());
        emp.setTransType("PPM");
        emp.setRefNo(ppmScheduleDocBy.getPpmNo());
        new GPSTracker(getContext()).updateFusedLocation(emp);


        try {
            AppUtils.hideProgressDialog();
            MaterialDialog.Builder builder =
                    new MaterialDialog.Builder(mActivity)
                            .content(strErr)
                            .positiveText(R.string.lbl_okay)
                            .stackingBehavior(StackingBehavior.ADAPTIVE)
                            .onPositive(
                                    new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(
                                                @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();

                                            FragmentManager fm = getActivity().getSupportFragmentManager();
                                            for (int i = 0; i < fm.getBackStackEntryCount()-3; ++i) {
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

    public void loadFragment(final Fragment fragment, final String tag) {
        Log.d(TAG, "loadFragment");

        Runnable mPendingRunnable =
                new Runnable() {
                    @Override
                    public void run() {
                        // update the main content by replacing fragments
                        FragmentTransaction fragmentTransaction =
                                mActivity.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
                        fragmentTransaction.addToBackStack(TAG_RECEIVED_COMPALINTS);
                        fragmentTransaction.commit();
                    }
                };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

            }
        }
    }

    @Override
   public void onCustomerFeedbackReceived(SaveRatedServiceRequest customerRemarks){

        if(!customerRemarks.getCustomerFeedback().equalsIgnoreCase("null")){
            feedbackUpdate=true;
            tv_select_customer_rank.setText(customerRemarks.getCustomerFeedback());
            tv_select_customer_rank.setTypeface(font.getHelveticaBold());
            select_customer_rank=customerRemarks.getCustomerFeedback();
            tie_customer_remarks.setText(customerRemarks.getCustomerRemarks());
            signaturePad.setSignatureBitmap(convertBase64Tobitmap(customerRemarks.getCustomerSignature()));
            signaturePad.setVisibility(View.GONE);
            img_signature.setVisibility(View.VISIBLE);
            img_signature.setImageBitmap(convertBase64Tobitmap(customerRemarks.getCustomerSignature()));
        }
    }
    @Override
    public void onCustomerFeedbackReceivedError(String strErr){
    }

    public Bitmap convertBase64Tobitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}
