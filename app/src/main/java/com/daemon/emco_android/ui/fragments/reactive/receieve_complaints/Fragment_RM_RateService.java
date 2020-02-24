package com.daemon.emco_android.ui.fragments.reactive.receieve_complaints;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

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
import com.daemon.emco_android.repository.remote.GetPostRateServiceService;
import com.daemon.emco_android.ui.components.CustomTextInputLayout;
import com.daemon.emco_android.repository.db.dbhelper.SaveRatedServiceDbInitializer;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.repository.db.entity.SaveRatedServiceEntity;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.listeners.RateAndShareListener;
import com.daemon.emco_android.listeners.RatedServiceListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static com.daemon.emco_android.utils.Utils.TAG_RECEIVED_COMPALINTS;

/**
 * Created by subbu on 25/11/16.
 */
public class Fragment_RM_RateService extends Fragment
        implements View.OnClickListener,
        SignaturePad.OnSignedListener,
        RateAndShareListener,
        RatedServiceListener {
    private static final String TAG = Fragment_RM_RateService.class.getSimpleName();
    private static String mNetworkInfo = null,
            mCustomerRemarks = null,
            select_customer_rank = null,
            mComplaintno = null,
            mComplaintSite = null,
            mOpco = null;
    private Gson gson = new Gson();
    private Handler mHandler;
    private AppCompatActivity mActivity;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;

    AppCompatTextView tv_lbl_signature,tv_lbl_cus_remarks;
    private TextView tv_lbl_cus_satisfaction,
            tv_lbl_tech_remarks;
    private TextView tv_select_customer_rank, tv_select_signstatus;
    private SignaturePad signaturePad;
    private Button btnClear;
    private FloatingActionButton btnSave;
    private AppCompatEditText tie_customer_remarks;
    private ImageView iv_very_good, iv_good, iv_excellent, iv_Satisfactory, iv_poor;
    private LinearLayout ll_remarks_detail;
    private CoordinatorLayout cl_main;
    private Toolbar mToolbar;
    private Bundle mArgs = new Bundle();
    private String mLoginData = null;
    private String mStrEmpId = null;
    private ReceiveComplaintViewEntity receiveComplaintViewEntity;
    private List<String> listCustomerRank = null;
    private Type baseType = new TypeToken<List<String>>() {
    }.getType();
    private GetPostRateServiceService mGetPostRateService;
    private String mUnSignedPage = AppUtils.ARGS_RECEIVECOMPLAINT_PAGE;

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
                    new GetPostRateServiceService(mActivity, this);
            getParcelableData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getParcelableData() {
        Log.d(TAG, "getParcelableData");
        try {
            if (mArgs != null && mArgs.containsKey(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS)) {
                receiveComplaintViewEntity =
                        mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);
                mComplaintno = receiveComplaintViewEntity.getComplaintNumber();
                mComplaintSite = receiveComplaintViewEntity.getComplaintSite();
                mOpco = receiveComplaintViewEntity.getOpco();
                mUnSignedPage = mArgs.getString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE);

                getCustomerRemarkData();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            tv_lbl_cus_remarks = (AppCompatTextView) rootView.findViewById(R.id.tv_lbl_cus_remarks);
            tv_lbl_signature = (AppCompatTextView) rootView.findViewById(R.id.tv_lbl_signature);
            tv_lbl_tech_remarks = (TextView) rootView.findViewById(R.id.tv_lbl_tech_remarks);

            // logo changes R id
            iv_excellent = (ImageView) rootView.findViewById(R.id.iv_excellent);
            iv_very_good = (ImageView) rootView.findViewById(R.id.iv_poor);
            tie_customer_remarks= (AppCompatEditText) rootView.findViewById(R.id.tie_customer_remarks);
            iv_good = (ImageView) rootView.findViewById(R.id.iv_very_good);
            iv_Satisfactory = (ImageView) rootView.findViewById(R.id.iv_good);
            iv_poor = (ImageView) rootView.findViewById(R.id.iv_Satisfactory);

            tv_select_customer_rank = (TextView) rootView.findViewById(R.id.tv_select_customer_rank);
            tv_select_signstatus = (TextView) rootView.findViewById(R.id.tv_select_signstatus);
            signaturePad = (SignaturePad) rootView.findViewById(R.id.signaturePad);
            btnSave = (FloatingActionButton) rootView.findViewById(R.id.btnSave);
            btnClear = (Button) rootView.findViewById(R.id.btnClear);
            setupActionBar();

            FragmentManager fm=getFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                // fm.popBackStack();
                Log.i(TAG, "Found fragment: " + fm.getBackStackEntryAt(i).getId());
            }

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

            tv_lbl_signature.setText(Html.fromHtml("Customer Signature " + AppUtils.mandatory));
            tv_lbl_cus_satisfaction.setTypeface(font.getHelveticaRegular());
            tv_lbl_cus_remarks.setTypeface(font.getHelveticaRegular());
            tv_lbl_signature.setTypeface(font.getHelveticaRegular());
            tv_lbl_tech_remarks.setTypeface(font.getHelveticaRegular());
            tv_select_customer_rank.setTypeface(font.getHelveticaRegular());
            tv_select_signstatus.setTypeface(font.getHelveticaRegular());
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

            if (receiveComplaintViewEntity != null) {

                if (receiveComplaintViewEntity.getCustomerFeedback() != null) {
                    select_customer_rank = receiveComplaintViewEntity.getCustomerFeedback();
                    tv_select_customer_rank.setText(receiveComplaintViewEntity.getCustomerFeedback());
                }
            }

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
                // Bitmap bitmap = signaturePad.getSignatureBitmap();
                submitForm();
                break;
            case R.id.btnClear:
                signaturePad.clear();
                break;
            case R.id.iv_excellent:
                customerRemarkUpdate(listCustomerRank.get(0));
                break;
            case R.id.iv_very_good:
                customerRemarkUpdate(listCustomerRank.get(2));
                break;
            case R.id.iv_good:
                customerRemarkUpdate(listCustomerRank.get(3));
                break;
            case R.id.iv_Satisfactory:
                customerRemarkUpdate(listCustomerRank.get(4));
                break;
            case R.id.iv_poor:
                customerRemarkUpdate(listCustomerRank.get(1));
                break;
        }
    }

    private void getCustomerRemarkData() {
        mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
        if (mNetworkInfo != null && mNetworkInfo.length() > 0) {
            if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                mGetPostRateService.getCustomerRemarksData();

            } else {
                String strJsonCr = mPreferences.getString(AppUtils.SHARED_CUSTOMER_REMARKS, null);
                Log.d(TAG, "str json :;" + strJsonCr);
                if (strJsonCr != null && strJsonCr.length() > 0) {
                    listCustomerRank = gson.fromJson(strJsonCr, baseType);
                } else
                    AppUtils.showDialog(mActivity, getString(R.string.msg_no_data_found_in_local_db));
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
        try {
            AppUtils.closeInput(cl_main);
            String msgErr = "";
            if (!validateCustomerRemarks()) return;

            if (select_customer_rank == null) {
                AppUtils.setErrorBg(tv_select_customer_rank, true);
                msgErr = getString(R.string.lbl_select_customer_remarks);
            } else AppUtils.setErrorBg(tv_select_customer_rank, false);

            if (receiveComplaintViewEntity != null)
                if (receiveComplaintViewEntity.getCustomerSignStatus() != null) {
                    if (receiveComplaintViewEntity.getCustomerSignStatus().equals(AppUtils.CLOSEWITHSIGN)) {
                        if (signaturePad.isEmpty()) {
                            AppUtils.showDialog(mActivity, "Customer signature empty");
                            AppUtils.closeInput(cl_main);
                            return;
                        }
                    }
                }

            if (msgErr != "") {
                AppUtils.showDialog(mActivity, "Please select values in Mandatory field");
                return;
            }

            if (signaturePad.isEmpty()) {
                AppUtils.showDialog(mActivity, "Signature Mandatory");
                return;

            } else {

                SaveRatedServiceEntity serviceRequest = new SaveRatedServiceEntity();
                serviceRequest.setComplaintNumber(mComplaintno);
                serviceRequest.setComplaintSite(mComplaintSite);
                serviceRequest.setCustomerFeedback(select_customer_rank);
                serviceRequest.setCustomerSignStatus(AppUtils.CLOSEWITHSIGN);
                serviceRequest.setCustomerRemarks(mCustomerRemarks);
                serviceRequest.setOpco(mOpco);
                serviceRequest.setCreatedBy(mStrEmpId);
                serviceRequest.setCustomerSignature(AppUtils.getEncodedString(signaturePad.getSignatureBitmap()));
                serviceRequest.setTransactionType("R");

                mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
                if (mNetworkInfo != null && mNetworkInfo.length() > 0) {
                    if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
                        AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);

                        mGetPostRateService.saveCustomerFeedbackRM(serviceRequest, this);
                    } else {
                        new SaveRatedServiceDbInitializer(mActivity, this, serviceRequest)
                                .execute(AppUtils.MODE_INSERT_SINGLE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCustomerRemarks() {
        Log.d(TAG, "getCustomerRemarks");
        try {
            if (listCustomerRank.size() > 0) {
                new MaterialDialog.Builder(mActivity)
                        .title(R.string.lbl_select_customer_remarks)
                        .items(listCustomerRank)
                        .itemsCallbackSingleChoice(
                                -1,
                                new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(
                                            MaterialDialog dialog, View view, int which, CharSequence text) {
                                        if (which >= 0) {
                                            select_customer_rank = text.toString();
                                            tv_select_customer_rank.setText(text.toString());
                                            tv_select_customer_rank.setTypeface(font.getHelveticaBold());
                                            AppUtils.setErrorBg(tv_select_customer_rank, false);
                                        } else {
                                            select_customer_rank = null;
                                            tv_select_customer_rank.setText("");
                                            tv_select_customer_rank.setHint(
                                                    getString(R.string.lbl_select_customer_remarks));
                                            AppUtils.showDialog(
                                                    mActivity, getString(R.string.no_value_has_been_selected));
                                            AppUtils.setErrorBg(tv_select_customer_rank, true);
                                        }
                                        AppUtils.closeInput(cl_main);
                                        return true;
                                    }
                                })
                        .canceledOnTouchOutside(false)
                        .positiveText(R.string.lbl_done)
                        .negativeText(R.string.lbl_close)
                        .show();
            } else
                AppUtils.showDialog(mActivity, getString(R.string.msg_no_data_found_in_local_db));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateCustomerRemarks() {
        mCustomerRemarks = tie_customer_remarks.getText().toString().trim();
        if (mCustomerRemarks.isEmpty()) {
           // til_customer_remarks.setError(getString(R.string.msg_enter_remark_detail));
            AppUtils.showToast(mActivity,getString(R.string.msg_enter_remark_detail));
            requestFocus(tie_customer_remarks);
            return false;
        } else {
           // til_customer_remarks.setErrorEnabled(false);
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
        try {
            Log.d(TAG, "onSaveRateShareReceived");
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

                                            FragmentManager fm=getFragmentManager();
                                            for (int i = 0; i < fm.getBackStackEntryCount()-1; ++i) {
                                                fm.popBackStack();
                                            }

                                            Bundle mdata = new Bundle();
                                            mdata.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, mUnSignedPage);
                                            Fragment_RC_List unsignedcomplaintslist = new Fragment_RC_List();
                                            unsignedcomplaintslist.setArguments(mdata);
                                            loadFragment(unsignedcomplaintslist, Utils.TAG_RECEIVED_COMPALINTS);
                                        }
                                    });

            MaterialDialog dialog = builder.build();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
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

    @Override
    public void onRatedServiceReceived(List<SaveRatedServiceEntity> entities) {

    }

    @Override
    public void onRatedServiceReceived(SaveRatedServiceEntity entities) {

    }

    @Override
    public void onSaveRatedService(String message) {
        onSaveRateShareReceived(message);
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

            } }}
}
