package com.daemon.emco_android.ui.fragments.reactive.logcomplaints;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.daemon.emco_android.utils.AnimateUtils;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.GetLogComplaintPopupRepository;
import com.daemon.emco_android.repository.remote.PostLogComplaintService;
import com.daemon.emco_android.ui.components.CustomTextInputLayout;
import com.daemon.emco_android.ui.components.FilterableListDialog;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.dbhelper.LogComplaintDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ZoneDbInitializer;
import com.daemon.emco_android.repository.db.entity.LogComplaintEntity;
import com.daemon.emco_android.repository.db.entity.ZoneEntity;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.listeners.LCUserInputListener;
import com.daemon.emco_android.listeners.LogComplaint_Listener;
import com.daemon.emco_android.listeners.ZoneListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.model.response.LCUserInput;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ConnectivityStatus;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import static com.daemon.emco_android.utils.AppUtils.checkInternet;

/**
 * Created by subbu on 18/7/17.
 */
public class Fragment_RM_LogComplaintUser extends Fragment
        implements LogComplaint_Listener, LCUserInputListener, ZoneListener {
    private final String TAG = Fragment_RM_LogComplaintUser.class.getSimpleName();
    // root view of fragment
    View rootView = null;
    private Gson gson = new Gson();
    private Type baseType = new TypeToken<List<LCUserInput>>() {
    }.getType();
    private AppCompatActivity mActivity;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private CoordinatorLayout cl_main;
    private CustomTextInputLayout til_location, til_sublocation, til_complaint, til_property;
    private TextInputEditText tie_location, tie_sublocation, tie_complaint, tie_property;
    private TextView tv_lbl_contract_title, tv_toolbar_title,
            tv_lbl_contract_no,
            tv_lbl_zone,
            tv_lbl_property,
            tv_lbl_location,
            tv_lbl_sublocation,
            tv_lbl_complaint;
    private TextView tv_contract_title, tv_contract_no, tv_select_zone;
    private FloatingActionButton btnSaveComplaint;
    private Toolbar mToolbar;
    private GetLogComplaintPopupRepository mGetComplaintPopupService;
    private String mStrEmployeeId = null,
            mCompanyCode = null,
            mComplainBy = null,
            mComplainSite = null,
            mComplainDate = null,
            mComplainWebNumber = null,
            mComplaintDetail = null,
            mComplaintEmail = null,
            mComplainMobile = null,
            mComplainPhone = null,
            mLocation = null,
            mSubLocation = null,
            mZoneCode = null,
            mComplaintYear = null,
            mPropertyDetails = null,
            mJobNo = null,mJobDesc=null;
    private Login mUserData;
    private List<ZoneEntity> listZone = new ArrayList<>();
    private List<LCUserInput> listJobNo = new ArrayList<>();


    View.OnClickListener _OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppUtils.closeInput(cl_main);
                    switch (v.getId()) {
                        case R.id.btnSaveComplaint:
                            submitForm();
                            break;
                        case R.id.tv_contract_no:
                            getJobNo();
                            break;
                        case R.id.tv_select_zone:
                           getZone();
                            break;
                        default:
                            break;
                    }
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();

            setHasOptionsMenu(true);
            setRetainInstance(false);
            // register broadcast receiver
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            String mStrLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mStrLoginData != null) {
                mUserData = gson.fromJson(mStrLoginData, Login.class);
                mStrEmployeeId =
                        mUserData.getEmployeeId() == null ? mUserData.getUserName() : mUserData.getEmployeeId();
                mComplainBy = mUserData.getUserName();
                mComplaintEmail = mUserData.getEmailId();
                mComplainMobile = mUserData.getMobileNumber();
                mComplainPhone = mUserData.getPhoneNumber();
            }
            mComplainDate = AppUtils.getDateTime().toUpperCase();

            mComplaintYear = AppUtils.getCurrentYear();

            Log.d( TAG,
                    "mComplainDate : "
                            + mComplainDate
                            + "::: mComplainWebNumber :"
                            + mComplainWebNumber
                            + ":: year :"
                            + mComplaintYear);

            mManager = mActivity.getSupportFragmentManager();
            font = App.getInstance().getFontInstance();
            if (mActivity.getCurrentFocus() != null) {
                InputMethodManager imm =
                        (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
            }
            if (mStrEmployeeId != null)
                mGetComplaintPopupService =
                        new GetLogComplaintPopupRepository(
                                mActivity, new EmployeeIdRequest(null, mStrEmployeeId, null, null));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getPopupDataFromServer() {

        gettingLCUserInput();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {

            rootView = (View) inflater.inflate(R.layout.fragment_log_complaint_user, container, false);
            initUI(rootView);
            setProperties();
            getPopupDataFromServer();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rootView;
    }

    private void gettingLCUserInput() {

            if (ConnectivityStatus.isConnected(mActivity)) {
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                mGetComplaintPopupService.getLCUserInputData(this);
            }else{
                String strJsonLc = mPreferences.getString(AppUtils.SHARED_CUSTOMER_LC, null);
                Log.d(TAG, "str json :;" + strJsonLc);
                if (strJsonLc != null && strJsonLc.length() > 0) {
                    List<LCUserInput> lcUserInput = gson.fromJson(strJsonLc, baseType);
                    if(lcUserInput.size()==1){
                        tv_contract_title.setText(lcUserInput.get(0).getJobDescription());
                        tv_contract_no.setText(lcUserInput.get(0).getJobNumber());
                        if(lcUserInput.get(0).getZoneDescription()!=null){
                            tv_select_zone.setText(lcUserInput.get(0).getZoneDescription());
                        }
                        // mZoneCode = lcUserInput.getZoneCode();
                        mJobNo = lcUserInput.get(0).getJobNumber();
                        mComplainSite = lcUserInput.get(0).getComplainSite();
                        mCompanyCode = lcUserInput.get(0).getCompanyCode();
                        gettingZoneName();
                        //checkLoad=2;
                    }else{
                        listJobNo.addAll(lcUserInput);
                        tv_contract_no.setText(mActivity.getString(R.string.lbl_select_zone));
                        tv_contract_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down_spin, 0);
                    }
                } else
                    AppUtils.showDialog(mActivity, getString(R.string.msg_no_data_found_in_local_db));
            }

    }


    @Override
    public void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    private void initUI(View rootView) {

        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            tv_lbl_contract_title = (TextView) rootView.findViewById(R.id.tv_lbl_contract_title);
            tv_lbl_contract_no = (TextView) rootView.findViewById(R.id.tv_lbl_contractno);
            tv_lbl_zone = (TextView) rootView.findViewById(R.id.tv_lbl_zone);
            tv_lbl_location = (TextView) rootView.findViewById(R.id.tv_lbl_location);
            tv_lbl_sublocation = (TextView) rootView.findViewById(R.id.tv_lbl_sublocation);
            tv_lbl_property = (TextView) rootView.findViewById(R.id.tv_lbl_property);
            tv_lbl_complaint = (TextView) rootView.findViewById(R.id.tv_lbl_complaint_details);
            tv_contract_title = (TextView) rootView.findViewById(R.id.tv_contract_title);
            tv_contract_no = (TextView) rootView.findViewById(R.id.tv_contract_no);
            tv_select_zone = (TextView) rootView.findViewById(R.id.tv_select_zone);
            til_location = (CustomTextInputLayout) rootView.findViewById(R.id.til_location);
            til_sublocation = (CustomTextInputLayout) rootView.findViewById(R.id.til_sublocation);
            til_complaint = (CustomTextInputLayout) rootView.findViewById(R.id.til_complaint);
            til_property = (CustomTextInputLayout) rootView.findViewById(R.id.til_property);
            tie_location = (TextInputEditText) rootView.findViewById(R.id.tie_location);
            tie_sublocation = (TextInputEditText) rootView.findViewById(R.id.tie_sublocation);
            tie_property = (TextInputEditText) rootView.findViewById(R.id.tie_property);
            tie_complaint = (TextInputEditText) rootView.findViewById(R.id.tie_complaint);
            btnSaveComplaint = (FloatingActionButton) rootView.findViewById(R.id.btnSaveComplaint);

            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.lbl_log_complaint));
        //mToolbar.setTitle(getResources().getString(R.string.lbl_log_complaint));
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

        tv_lbl_contract_title.setText(
                Html.fromHtml(getString(R.string.lbl_contract_title) + AppUtils.mandatory));
        tv_lbl_contract_no.setText(Html.fromHtml(getString(R.string.lbl_job_no) + AppUtils.mandatory));
        tv_lbl_zone.setText(Html.fromHtml(getString(R.string.lbl_zone) + AppUtils.mandatory));

        tv_lbl_property.setText(
                Html.fromHtml(getString(R.string.lbl_property_details) + AppUtils.mandatory));
        tv_lbl_location.setText(
                Html.fromHtml(getString(R.string.lbl_location_details) + AppUtils.mandatory));
        tv_lbl_sublocation.setText(
                Html.fromHtml(getString(R.string.lbl_sublocation) + AppUtils.mandatory));
        tv_lbl_complaint.setText(
                Html.fromHtml(getString(R.string.lbl_complaint_details) + AppUtils.mandatory));

        tv_contract_no.setOnClickListener(_OnClickListener);
        btnSaveComplaint.setOnClickListener(_OnClickListener);
        tie_sublocation.addTextChangedListener(new MyTextWatcher(tie_sublocation));
        tie_location.addTextChangedListener(new MyTextWatcher(tie_location));
        tie_complaint.addTextChangedListener(new MyTextWatcher(tie_complaint));
        tie_property.addTextChangedListener(new MyTextWatcher(tie_property));
        tv_select_zone.setOnClickListener(_OnClickListener);
        new AnimateUtils().fabAnimate(btnSaveComplaint);

    }

    private void submitForm() {

        try {
            String msgErr = "";
            if (mJobNo == null) {
                AppUtils.setErrorBg(tv_contract_title, true);
                msgErr = msgErr + "\n" + getString(R.string.lbl_select_jobno);
                AppUtils.setErrorBg(tv_contract_no, true);
                msgErr = msgErr + "\n" + getString(R.string.lbl_select_jobno_new);
            } else{
                AppUtils.setErrorBg(tv_contract_no, false);
                AppUtils.setErrorBg(tv_contract_title, false);
            }

            if (mZoneCode == null) {
                AppUtils.setErrorBg(tv_select_zone, true);
                msgErr = msgErr + "\n" + getString(R.string.lbl_select_zone);
            } else AppUtils.setErrorBg(tv_select_zone, false);

            validateProperty();
            validateLocation();
            validateSubLocation();
            validateComplaint();
            if (msgErr != "") {
                AppUtils.showDialog(mActivity, "Please fill all the mandatory fields.");
                return;
            }

            if (validateProperty() && validateLocation() && validateComplaint()) {
                mComplainWebNumber = AppUtils.getUniqueLogComplaintNo();
                Log.d(TAG, " mComplainWebNumber :" + mComplainWebNumber);

                LogComplaintEntity entity = new LogComplaintEntity(mComplainWebNumber);
                entity.setCompanyCode(mCompanyCode);
                entity.setComplainBy(mComplainBy);
                entity.setComplainDate(mComplainDate);
                entity.setComplainSite(mComplainSite);
                entity.setComplaintDetail(mComplaintDetail);
                entity.setComplaintEmail(mComplaintEmail);
                entity.setComplaintMobile(mComplainMobile);
                entity.setComplaintPhone(mComplainPhone);
                entity.setComplaintYear(mComplaintYear);
                entity.setJobNumber(mJobNo);
                entity.setLocation(mLocation);
                entity.setPropertyDetail(mPropertyDetails);
                entity.setUser(mComplainBy);
                entity.setZoneCode(mZoneCode);
                entity.setSubLocation(mSubLocation);
                postDataToServer(entity);
            }
            AppUtils.closeInput(cl_main);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void postDataToServer(LogComplaintEntity logComplaintRequest) {

        try {

                if (checkInternet(getContext())) {
                    AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                    new PostLogComplaintService(mActivity, this).postLogComplaintData(logComplaintRequest);
                } else
                    new LogComplaintDbInitializer(mActivity, logComplaintRequest, this)
                            .execute(AppUtils.MODE_INSERT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateLocation() {
        mLocation = tie_location.getText().toString().trim();
        if (mLocation.isEmpty()) {
            til_location.setError(getString(R.string.msg_enter_location));
            return false;
        } else {
            til_location.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateSubLocation() {
        mSubLocation = tie_sublocation.getText().toString().trim();
        if (mSubLocation.isEmpty()) {
            til_sublocation.setError(getString(R.string.msg_enter_location));
            // requestFocus(tie_location);
            return false;
        } else {
            til_sublocation.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateProperty() {
        mPropertyDetails = tie_property.getText().toString().trim();
        if (mPropertyDetails.isEmpty()) {
            til_property.setError(getString(R.string.msg_building_name_empty));
            return false;
        } else {
            til_property.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateComplaint() {
        mComplaintDetail = tie_complaint.getText().toString().trim();
        if (mComplaintDetail.isEmpty()) {
            til_complaint.setError(getString(R.string.msg_enter_complaint_empty));
            return false;
        } else {
            til_complaint.setErrorEnabled(false);
        }

        return true;
    }

    public void getJobNo(){
        try {
            if(listJobNo.size()>1){
                ArrayList<String> strArrayZone = new ArrayList<>();

                for (int i = 0; i < listJobNo.size(); i++) {
                    strArrayZone.add(listJobNo.get(i).getJobNumber());
                }
                FilterableListDialog.create(
                        mActivity,
                        getString(R.string.lbl_select_job_no),
                        strArrayZone,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {
                                AppUtils.setErrorBg(tv_contract_no, false);
                                mJobNo = text;
                                tv_contract_no.setText(text.toString());
                                tv_contract_no.setTypeface(font.getHelveticaBold());
                                for (LCUserInput item : listJobNo) {
                                    if (mJobNo.equalsIgnoreCase(item.getJobNumber())) {
                                        mJobNo = item.getJobNumber();
                                        mJobDesc=item.getJobDescription();
                                        mCompanyCode=item.getCompanyCode();
                                        mComplainSite=item.getComplainSite();
                                        tv_contract_title.setText(item.getJobDescription());
                                    }
                                }
                                gettingZoneName();
                            }
                        })
                        .show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } }

    private void gettingZoneName() {

        ZoneEntity zoneEntity = new ZoneEntity();
        zoneEntity.setOpco(mCompanyCode);
        zoneEntity.setContractNo(mJobNo);
        List<ZoneEntity> zoneEntityList = new ArrayList<>();
        zoneEntityList.add(zoneEntity);

            if (checkInternet(getContext())) {
                    AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                    mGetComplaintPopupService.getZoneListData(zoneEntity,this);
            } else new ZoneDbInitializer(mActivity, zoneEntityList, this).execute(AppUtils.MODE_GET);

    }

    public void getZone() {
        try {
            ArrayList<String> strArrayZone = new ArrayList<>();
            if(listZone.size()>1){
                for (int i = 0; i < listZone.size(); i++) {
                    strArrayZone.add(listZone.get(i).getZoneName());
                }
                FilterableListDialog.create(
                        mActivity,
                        getString(R.string.lbl_select_zone),
                        strArrayZone,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {
                                AppUtils.setErrorBg(tv_select_zone, false);
                                tv_select_zone.setText(text);
                                tv_select_zone.setTypeface(font.getHelveticaBold());
                                for (ZoneEntity item : listZone) {
                                    if (text.equals(item.getZoneName())) {
                                        mZoneCode = item.getZoneCode();
                                    }
                                }
                            }
                        })
                        .show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onZoneReceivedSuccess(List<ZoneEntity> zoneList, int mode) {

        AppUtils.hideProgressDialog();
        listZone = zoneList;
        if(listZone.size()!=0&&listZone!=null){
            if (listZone.size() == 1) {
                tv_select_zone.setText(listZone.get(0).getZoneName());
                tv_select_zone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tv_select_zone.setClickable(false);
                mZoneCode=listZone.get(0).getZoneCode();
            }else{
                tv_select_zone.setClickable(true);
                tv_select_zone.setText(getResources().getString(R.string.lbl_select_zone));
            }

        }
        if (mode == AppUtils.MODE_SERVER) {
            new ZoneDbInitializer(mActivity, listZone, this).execute(AppUtils.MODE_INSERT);
        }
    }

    @Override
    public void onZoneReceivedFailure(String strErr, int mode) {
        AppUtils.hideProgressDialog();
        if (mode == AppUtils.MODE_LOCAL) {
            AppUtils.showDialog(mActivity, strErr);
        } else new ZoneDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
    }

    @Override
    public void onLogComplaintDataReceivedSuccess(EmployeeIdRequest logComplaint, int mode) {

        AppUtils.hideProgressDialog();
        MaterialDialog.Builder builder =
                new MaterialDialog.Builder(mActivity)
                        .content(
                                getString(R.string.msg_thankyou_note1)
                                        + " "
                                        + logComplaint.getComplainWebNumber()
                                        + " "
                                        + getString(R.string.msg_contact_us))
                        .positiveText(R.string.lbl_okay)
                        .stackingBehavior(StackingBehavior.ADAPTIVE)
                        .onPositive(
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        mManager.popBackStack();
                                    }
                                });

        MaterialDialog dialog = builder.build();
        dialog.show();
        // clearFormData();
        if (mode == AppUtils.MODE_SERVER)
            new LogComplaintDbInitializer(
                    mActivity, new LogComplaintEntity(logComplaint.getEmailId()), this)
                    .execute(AppUtils.MODE_DELETE);
    }

    @Override
    public void onLogComplaintDataReceivedFailure(String strErr, int mode) {

        try {
            AppUtils.hideProgressDialog();
            Fragment main = mManager.findFragmentByTag(Utils.TAG_LOG_COMPLAINT);
            if (main != null && main.isVisible()) AppUtils.showDialog(mActivity, strErr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
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
                Fragment _fragment = new MainLandingUI();
                FragmentTransaction _transaction = mManager.beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAllLogComplaintData(List<LogComplaintEntity> logComplaintEntities, int mode) {
        Log.d(TAG, "onAllLogComplaintData count " + logComplaintEntities.size());
    }

    @Override
    public void onLCUserInputReceivedSuccess(List<LCUserInput> lcUserInput, int mode) {
        try {
            if (lcUserInput != null) {
                AppUtils.hideProgressDialog();
                if(lcUserInput.size()>0){
                    if(lcUserInput.size()==1){
                        tv_contract_title.setText(lcUserInput.get(0).getJobDescription());
                        tv_contract_no.setText(lcUserInput.get(0).getJobNumber());
                        if(lcUserInput.get(0).getZoneDescription()!=null){
                            tv_select_zone.setText(lcUserInput.get(0).getZoneDescription());
                        }
                        mJobNo = lcUserInput.get(0).getJobNumber();
                        mComplainSite = lcUserInput.get(0).getComplainSite();
                        mCompanyCode = lcUserInput.get(0).getCompanyCode();
                        gettingZoneName();
                    }else{
                        listJobNo.addAll(lcUserInput);
                        tv_contract_no.setText(getResources().getString(R.string.lbl_select_zone));
                        tv_contract_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down_spin, 0);
                    }
                    // save data
                    String strlc = gson.toJson(lcUserInput, baseType);
                    mEditor.putString(AppUtils.SHARED_CUSTOMER_LC, strlc);
                    mEditor.commit();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLCUserInputReceivedFailure(String strErr, int mode) {
        AppUtils.hideProgressDialog();
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
                case R.id.tie_location:
                    validateLocation();
                    break;
                case R.id.tie_sublocation:
                    validateSubLocation();
                    break;
                case R.id.tie_complaint:
                    validateComplaint();
                    break;
                case R.id.tie_property:
                    validateProperty();
                    break;
            } }
    }
}
