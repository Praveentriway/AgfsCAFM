package com.daemon.emco_android.ui.fragments.reactive.logcomplaints;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import android.widget.Button;
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
import com.daemon.emco_android.repository.db.dbhelper.CategoryDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ContractDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.LogComplaintDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.PriorityDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.SiteAreaDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.WorkTypeDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ZoneDbInitializer;
import com.daemon.emco_android.repository.db.entity.BuildingDetailsEntity;
import com.daemon.emco_android.repository.db.entity.CategoryEntity;
import com.daemon.emco_android.repository.db.entity.ContractEntity;
import com.daemon.emco_android.repository.db.entity.LogComplaintEntity;
import com.daemon.emco_android.repository.db.entity.PriorityEntity;
import com.daemon.emco_android.repository.db.entity.SiteAreaEntity;
import com.daemon.emco_android.repository.db.entity.WorkTypeEntity;
import com.daemon.emco_android.repository.db.entity.ZoneEntity;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.listeners.BuildingDetailsListener;
import com.daemon.emco_android.listeners.CategoryListener;
import com.daemon.emco_android.listeners.JobNoListener;
import com.daemon.emco_android.listeners.LogComplaint_Listener;
import com.daemon.emco_android.listeners.PriorityListListener;
import com.daemon.emco_android.listeners.SiteListener;
import com.daemon.emco_android.listeners.WorkTypeListListener;
import com.daemon.emco_android.listeners.ZoneListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.checkInternet;

/**
 * Created by subbu on 18/7/17.
 */
public class Fragment_RM_LogComplaint extends Fragment
        implements CategoryListener,
        ZoneListener,
        JobNoListener,
        SiteListener,
        LogComplaint_Listener,
        PriorityListListener,
        WorkTypeListListener,
        BuildingDetailsListener {
    private final String TAG = Fragment_RM_LogComplaint.class.getSimpleName();
    // root view of fragment
    View rootView = null;
    private AppCompatActivity mActivity;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private CoordinatorLayout cl_main;
    private CustomTextInputLayout til_location, til_complaint, til_property;
    private TextInputEditText tie_location, tie_complaint, tie_property;
    private TextView tv_lbl_jobno, tv_toolbar_title,
            tv_lbl_site,
            tv_lbl_complaint,
            tv_lbl_zone,
            tv_lbl_location,
            tv_lbl_property,
            tv_lbl_category,
            tv_lbl_priority,
            tv_lbl_worktype;
    private TextView tv_select_jobno,
            tv_select_site,
            tv_select_zone,
            tv_select_category,
            tv_select_worktype,
            tv_select_priority,
            tv_select_property;
    private Button btnSaveComplaint;
    private Toolbar mToolbar;
    private GetLogComplaintPopupRepository mGetComplaintPopupService;
    /**
     * Global variables for post log complaint data
     */
    private String mStrLoginData = null;

    private String mStrEmployeeId = null;
    private String mStrCategoryName = null; // response data from category data
    private String mStrSiteName = null, mStrSiteCode = null,mContractNo=null;
    private String mComplaintPriority = null, mComplaintPriorityCode = null;
    private String mWorkType = null, mWorkTypeCode = null;
    private String mNatureOfWork = null; // nature description from category
    private String mCompanyCode = null,
            mComplainBy = null,
            mComplainDate = null,
            mComplainWebNumber = null,
            mComplaintDetail = null,
            mComplaintEmail = null,
            mComplainMobile = null,
            mComplainPhone = null,
            mLocation = null,
            mZoneCode = null,
            mComplaintYear = null,
            mPropertyDetails = null,
            mJobNo = null;
    private Login mUserData;
    private List<ZoneEntity> listZone = new ArrayList<>();
    private List<ContractEntity> listJobNo = new ArrayList<>();
    private List<SiteAreaEntity> listSiteArea = new ArrayList<>();
    private List<CategoryEntity> listCategory = new ArrayList<>();
    private List<PriorityEntity> listpriorities = new ArrayList<>();
    private List<WorkTypeEntity> listWorkType = new ArrayList<>();

    View.OnClickListener _OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.closeInput(cl_main);
                    switch (v.getId()) {
                        case R.id.btnSaveComplaint:
                            submitForm();
                            break;
                        case R.id.tv_select_job_no:
                            getJobNo();
                            break;
                        case R.id.tv_select_site:
                            getSiteName();
                            break;
                        case R.id.tv_select_category:
                            getCategory();
                            break;
                        case R.id.tv_select_zone:
                            gettingZoneName();
                            break;
                        case R.id.tv_select_worktype:
                            getWorkType();
                            break;
                        case R.id.tv_select_priority:
                            getPriority();
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
            mStrLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mStrLoginData != null) {
                Gson gson = new Gson();
                mUserData = gson.fromJson(mStrLoginData, Login.class);
                mStrEmployeeId = mUserData.getEmployeeId();
                mComplainBy = mUserData.getUserName();
                mComplaintEmail = mUserData.getEmailId();
                mComplainMobile = mUserData.getMobileNumber();
                mComplainPhone = mUserData.getPhoneNumber();
            }
            mComplainDate = AppUtils.getDateTime().toUpperCase();

            mComplaintYear = AppUtils.getCurrentYear();
            Log.d(
                    TAG,
                    "mComplainDate : "
                            + mComplainDate
                            + "::: mComplainWebNumber :"
                            + mComplainWebNumber
                            + ":: year :"
                            + mComplaintYear);
            mManager = mActivity.getSupportFragmentManager();
            font = App.getInstance().getFontInstance();
            // mArgs = getArguments().getString(ARGS_BUNDLE_MESSAGE);
            if (mActivity.getCurrentFocus() != null) {
                InputMethodManager imm =
                        (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
            }
            if (mStrEmployeeId != null)
                mGetComplaintPopupService =
                        new GetLogComplaintPopupRepository(
                                mActivity, new EmployeeIdRequest(mStrEmployeeId, null, null, null));

            getPopupDataFromServer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getPopupDataFromServer() {
        gettingPriority();
        gettingJobNo();
        gettingSiteName();
        gettingProperty();
        gettingWorkType();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = (View) inflater.inflate(R.layout.fragment_log_complaint, container, false);
            initUI(rootView);
            setProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    private void gettingProperty() {

            if (checkInternet(getContext())) {
                mGetComplaintPopupService.getCategoryData(this);
            } else new CategoryDbInitializer(mActivity, null, this).equals(AppUtils.MODE_GETALL);

    }

    private void gettingZoneName() {

    }


    private void gettingSiteName() {

            if (checkInternet(getContext())) {
                mGetComplaintPopupService.getSiteAreaData(this);
            }
            else new SiteAreaDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);

    }

    private void gettingJobNo() {

            if (checkInternet(getContext())) {
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                mGetComplaintPopupService.getContractListData(this);
            } else new ContractDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);

    }

    private void gettingPriority() {

            if (checkInternet(getContext())) {
                mGetComplaintPopupService.getPriorityListData(this);
            } else new PriorityDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);

    }

    private void gettingWorkType() {

            if (checkInternet(getContext())) {
                mGetComplaintPopupService.getWorkTypeListData(this);
            } else new WorkTypeDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);

    }

    @Override
    public void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    private void initUI(View rootView) {
        Log.d(TAG, "initUI");
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            tv_lbl_jobno = (TextView) rootView.findViewById(R.id.tv_lbl_job_no);
            tv_lbl_site = (TextView) rootView.findViewById(R.id.tv_lbl_site);
            tv_lbl_zone = (TextView) rootView.findViewById(R.id.tv_lbl_zone);
            tv_lbl_location = (TextView) rootView.findViewById(R.id.tv_lbl_location);
            tv_lbl_property = (TextView) rootView.findViewById(R.id.tv_lbl_property);
            tv_lbl_category = (TextView) rootView.findViewById(R.id.tv_lbl_category);
            tv_lbl_complaint = (TextView) rootView.findViewById(R.id.tv_lbl_complaint_details);

            tv_lbl_priority = (TextView) rootView.findViewById(R.id.tv_lbl_priority);
            tv_lbl_worktype = (TextView) rootView.findViewById(R.id.tv_lbl_worktype);

            tv_select_jobno = (TextView) rootView.findViewById(R.id.tv_select_job_no);
            tv_select_site = (TextView) rootView.findViewById(R.id.tv_select_site);
            tv_select_category = (TextView) rootView.findViewById(R.id.tv_select_category);
            tv_select_zone = (TextView) rootView.findViewById(R.id.tv_select_zone);
            tv_select_worktype = (TextView) rootView.findViewById(R.id.tv_select_worktype);
            tv_select_priority = (TextView) rootView.findViewById(R.id.tv_select_priority);
            tv_select_property = (TextView) rootView.findViewById(R.id.tv_select_property);

            til_location = (CustomTextInputLayout) rootView.findViewById(R.id.til_location);
            til_complaint = (CustomTextInputLayout) rootView.findViewById(R.id.til_complaint);
            til_property = (CustomTextInputLayout) rootView.findViewById(R.id.til_property);

            tie_location = (TextInputEditText) rootView.findViewById(R.id.tie_location);
            tie_property = (TextInputEditText) rootView.findViewById(R.id.tie_property);
            tie_complaint = (TextInputEditText) rootView.findViewById(R.id.tie_complaint);

            btnSaveComplaint = (Button) rootView.findViewById(R.id.btnSaveComplaint);
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

        tv_lbl_priority.setText(Html.fromHtml(getString(R.string.lbl_priority) + AppUtils.mandatory));
        tv_lbl_jobno.setText(
                Html.fromHtml(getString(R.string.lbl_contract_title) + AppUtils.mandatory));
        tv_lbl_site.setText(Html.fromHtml(getString(R.string.lbl_site) + AppUtils.mandatory));
        tv_lbl_zone.setText(Html.fromHtml(getString(R.string.lbl_zone) + AppUtils.mandatory));
        tv_lbl_category.setText(Html.fromHtml(getString(R.string.lbl_category) + AppUtils.mandatory));
        tv_lbl_worktype.setText(Html.fromHtml(getString(R.string.lbl_worktype) + AppUtils.mandatory));

        tv_lbl_property.setText(
                Html.fromHtml(getString(R.string.lbl_property_details) + AppUtils.mandatory));
        tv_lbl_location.setText(
                Html.fromHtml(getString(R.string.lbl_location_details) + AppUtils.mandatory));
        tv_lbl_complaint.setText(
                Html.fromHtml(getString(R.string.lbl_complaint_details) + AppUtils.mandatory));

        btnSaveComplaint.setOnClickListener(_OnClickListener);
        tie_location.addTextChangedListener(new MyTextWatcher(tie_location));
        tie_complaint.addTextChangedListener(new MyTextWatcher(tie_complaint));
        tie_property.addTextChangedListener(new MyTextWatcher(tie_property));

        tv_select_jobno.setOnClickListener(_OnClickListener);
        tv_select_site.setOnClickListener(_OnClickListener);
        tv_select_category.setOnClickListener(_OnClickListener);
        tv_select_zone.setOnClickListener(_OnClickListener);
        tv_select_worktype.setOnClickListener(_OnClickListener);
        tv_select_priority.setOnClickListener(_OnClickListener);
    }

    private void submitForm() {
        Log.d(TAG, "submitForm");
        try {
            String msgErr = "";
            if (mComplaintPriorityCode == null) {
                AppUtils.setErrorBg(tv_select_priority, true);
                msgErr = getString(R.string.lbl_select_priority);
            } else AppUtils.setErrorBg(tv_select_priority, false);
            if (mJobNo == null) {
                AppUtils.setErrorBg(tv_select_jobno, true);
                msgErr = msgErr + "\n" + getString(R.string.lbl_select_jobno);
            } else AppUtils.setErrorBg(tv_select_jobno, false);

            if (mStrSiteName == null) {
                AppUtils.setErrorBg(tv_select_site, true);
                msgErr = msgErr + "\n" + getString(R.string.lbl_select_site);
            } else AppUtils.setErrorBg(tv_select_site, false);

            if (mZoneCode == null) {
                AppUtils.setErrorBg(tv_select_zone, true);
                msgErr = msgErr + "\n" + getString(R.string.lbl_select_zone);
            } else AppUtils.setErrorBg(tv_select_zone, false);

            if (mStrCategoryName == null) {
                AppUtils.setErrorBg(tv_select_category, true);
                msgErr = msgErr + "\n" + getString(R.string.lbl_select_property);
            } else AppUtils.setErrorBg(tv_select_category, false);

            if (mWorkTypeCode == null) {
                AppUtils.setErrorBg(tv_select_worktype, true);
                msgErr = msgErr + "\n" + getString(R.string.lbl_select_worktype);
            } else AppUtils.setErrorBg(tv_select_worktype, false);

            validateProperty();
            validateLocation();
            validateComplaint();
            if (msgErr != "") {
                AppUtils.showDialog(mActivity, "Please select values in Mandatory field");
                return;
            }

            // validate property, location and complaint details
            if (validateProperty() && validateLocation() && validateComplaint()) {
                mComplainWebNumber = AppUtils.getUniqueLogComplaintNo();
                Log.d(TAG, " mComplainWebNumber :" + mComplainWebNumber);
                LogComplaintEntity entity = new LogComplaintEntity(mComplainWebNumber);
                entity.setCompanyCode(mCompanyCode);
                entity.setComplainBy(mComplainBy);
                entity.setComplainDate(mComplainDate);
                entity.setComplainSite(mStrSiteCode);
                entity.setComplaintDetail(mComplaintDetail);
                entity.setComplaintEmail(mComplaintEmail);
                entity.setComplaintMobile(mComplainMobile);
                entity.setComplaintPhone(mComplainPhone);
                entity.setComplaintYear(mComplaintYear);
                entity.setJobNumber(mJobNo);
                entity.setLocation(mLocation);
                entity.setNatureOfWork(mNatureOfWork);
                entity.setPropertyDetail(mPropertyDetails);
                entity.setUser(mComplainBy);
                entity.setZoneCode(mZoneCode);
                entity.setComplaintPriority(mComplaintPriorityCode);
                entity.setWorkType(mWorkTypeCode);

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

    private boolean validateProperty() {
        mPropertyDetails = tie_property.getText().toString().trim();
        if (mPropertyDetails.isEmpty()) {
            til_property.setError(getString(R.string.msg_building_name_empty));
            // requestFocus(tie_property);
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
            // requestFocus(tie_complaint);
            return false;
        } else {
            til_complaint.setErrorEnabled(false);
        }

        return true;
    }

    public void getJobNo() {
        try {
            ArrayList strArrayJobNo = new ArrayList();
            for (ContractEntity entity : listJobNo) {
                strArrayJobNo.add(entity.getJobNo() + "-" + entity.getJobDescription());
            }
            FilterableListDialog.create(
                    mActivity,
                    getString(R.string.lbl_select_jobno),
                    strArrayJobNo,
                    new FilterableListDialog.OnListItemSelectedListener() {
                        @Override
                        public void onItemSelected(String text) {
                            for (ContractEntity item : listJobNo) {
                                if (text.toString().equals(item.getJobNo() + "-" + item.getJobDescription())) {
                                    mJobNo = item.getJobNo();
                                }
                            }
                            tv_select_jobno.setText(text.toString());
                            tv_select_jobno.setTypeface(font.getHelveticaBold());
                            AppUtils.setErrorBg(tv_select_jobno, false);
                        }
                    })
                    .show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getSiteName() {

        try {
            ArrayList<String> strArraySiteName = new ArrayList<>();
            for (int i = 0; i < listSiteArea.size(); i++) {
                strArraySiteName.add(listSiteArea.get(i).getSiteDescription());
            }
            FilterableListDialog.create(
                    mActivity,
                    getString(R.string.lbl_select_site),
                    strArraySiteName,
                    new FilterableListDialog.OnListItemSelectedListener() {
                        @Override
                        public void onItemSelected(String text) {
                            mStrSiteName = text.toString();
                            tv_select_site.setText(mStrSiteName);
                            tv_select_site.setTypeface(font.getHelveticaBold());
                            for (SiteAreaEntity item : listSiteArea) {
                                if (mStrSiteName.equals(item.getSiteDescription())) {
                                    mStrSiteCode = item.getSiteCode();
                                    mContractNo=item.getJobNo();
                                    mCompanyCode = item.getOpco();
                                    System.out.println(mContractNo+"------"+mStrSiteCode);
                                }
                            }
                            AppUtils.setErrorBg(tv_select_site, false);
                        }
                    })
                    .show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getCategory() {

        try {
            String[] strArrayCategory = new String[listCategory.size()];
            for (int i = 0; i < listCategory.size(); i++) {
                strArrayCategory[i] = listCategory.get(i).getNatureDescription();
            }
            new MaterialDialog.Builder(mActivity)
                    .title(R.string.lbl_select_category)
                    .items(strArrayCategory)
                    .itemsCallbackSingleChoice(
                            -1,
                            new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(
                                        MaterialDialog dialog, View view, int which, CharSequence text) {
                                    if (which >= 0) {
                                        mStrCategoryName = text.toString();
                                        tv_select_category.setText(mStrCategoryName);
                                        tv_select_category.setTypeface(font.getHelveticaBold());
                                        for (CategoryEntity item : listCategory) {
                                            if (mStrCategoryName.equals(item.getNatureDescription())) {
                                                mNatureOfWork = item.getNatureCode();
                                            }
                                        }
                                        AppUtils.setErrorBg(tv_select_category, false);
                                    } else {
                                        mNatureOfWork = null;
                                        tv_select_category.setText("");
                                        tv_select_category.setHint(getString(R.string.lbl_select_category));
                                        AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                                        AppUtils.setErrorBg(tv_select_category, true);
                                    }
                                    AppUtils.closeInput(cl_main);
                                    return true;
                                }
                            })
                    .canceledOnTouchOutside(false)
                    .positiveText(R.string.lbl_done)
                    .negativeText(R.string.lbl_close)
                    .show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getPriority() {
        try {
            String[] strArrayPriorities = new String[listpriorities.size()];
            for (int i = 0; i < listpriorities.size(); i++) {
                strArrayPriorities[i] = listpriorities.get(i).getDescription();
            }
            new MaterialDialog.Builder(mActivity)
                    .title(R.string.lbl_select_priority)
                    .items(strArrayPriorities)
                    .itemsCallbackSingleChoice(
                            -1,
                            new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(
                                        MaterialDialog dialog, View view, int which, CharSequence text) {
                                    if (which >= 0) {
                                        mComplaintPriority = text.toString();
                                        tv_select_priority.setText(mComplaintPriority);
                                        tv_select_priority.setTypeface(font.getHelveticaBold());
                                        for (PriorityEntity item : listpriorities) {
                                            if (mComplaintPriority.equals(item.getDescription())) {
                                                mComplaintPriorityCode = item.getCode();
                                            }
                                        }
                                        AppUtils.setErrorBg(tv_select_priority, false);
                                    } else {
                                        mComplaintPriorityCode = null;
                                        tv_select_priority.setText("");
                                        tv_select_priority.setHint(getString(R.string.lbl_select_priority));
                                        AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                                        AppUtils.setErrorBg(tv_select_priority, true);
                                    }
                                    AppUtils.closeInput(cl_main);
                                    return true;
                                }
                            })
                    .canceledOnTouchOutside(false)
                    .positiveText(R.string.lbl_done)
                    .negativeText(R.string.lbl_close)
                    .show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getWorkType() {
        try {
            String[] strArrayWorkType = new String[listWorkType.size()];
            for (int i = 0; i < listWorkType.size(); i++) {
                strArrayWorkType[i] = listWorkType.get(i).getDescription();
            }
            new MaterialDialog.Builder(mActivity)
                    .title(R.string.lbl_select_worktype)
                    .items(strArrayWorkType)
                    .itemsCallbackSingleChoice(
                            -1,
                            new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(
                                        MaterialDialog dialog, View view, int which, CharSequence text) {
                                    if (which >= 0) {
                                        mWorkType = text.toString();
                                        tv_select_worktype.setText(mWorkType);
                                        tv_select_worktype.setTypeface(font.getHelveticaBold());
                                        for (WorkTypeEntity item : listWorkType) {
                                            if (mWorkType.equals(item.getDescription())) {
                                                mWorkTypeCode = item.getCode();
                                            }
                                        }
                                        AppUtils.setErrorBg(tv_select_worktype, false);
                                    } else {
                                        mWorkTypeCode = null;
                                        tv_select_worktype.setText("");
                                        tv_select_worktype.setHint(getString(R.string.lbl_select_worktype));
                                        AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                                        AppUtils.setErrorBg(tv_select_worktype, true);
                                    }
                                    AppUtils.closeInput(cl_main);
                                    return true;
                                }
                            })
                    .canceledOnTouchOutside(false)
                    .positiveText(R.string.lbl_done)
                    .negativeText(R.string.lbl_close)
                    .show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getZone() {
        try {
            ArrayList<String> strArrayZone = new ArrayList<>();
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
                            mZoneCode = text;
                            tv_select_zone.setText(text.toString());
                            tv_select_zone.setTypeface(font.getHelveticaBold());
                            for (ZoneEntity item : listZone) {
                                if (mZoneCode.equals(item.getZoneName())) {
                                    mZoneCode = item.getZoneCode();
                                }
                            }
                        }
                    })
                    .show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCategoryReceivedSuccess(List<CategoryEntity> category, int mode) {
        Log.d(TAG, "onCategoryReceivedSuccess");
        listCategory = category;
        if (mode == AppUtils.MODE_SERVER) {
            AppUtils.hideProgressDialog();
            new CategoryDbInitializer(mActivity, listCategory, this).execute(AppUtils.MODE_INSERT);
        }
    }

    @Override
    public void onCategoryReceivedFailure(String strErr, int mode) {
        Log.d(TAG, "onCategoryReceivedFailure");
        AppUtils.hideProgressDialog();
        if (mode == AppUtils.MODE_LOCAL) {
            AppUtils.showDialog(mActivity, strErr);
        } else new CategoryDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
    }

    @Override
    public void onLogComplaintSiteReceivedSuccess(List<SiteAreaEntity> siteAreaList, int mode) {
        Log.d(TAG, "onLogComplaintSiteReceivedSuccess");
        listSiteArea = siteAreaList;
        AppUtils.hideProgressDialog();
    if (mode == AppUtils.MODE_SERVER) {
      AppUtils.hideProgressDialog();
      new SiteAreaDbInitializer(mActivity, listSiteArea, this).execute(AppUtils.MODE_INSERT);
    }
    }

    @Override
    public void onZoneReceivedSuccess(List<ZoneEntity> zoneList, int mode) {
        Log.d(TAG, "onZoneReceivedSuccess");
        listZone = zoneList;
        getZone();
        if (mode == AppUtils.MODE_SERVER) {
            AppUtils.hideProgressDialog();
            new ZoneDbInitializer(mActivity, listZone, this).execute(AppUtils.MODE_INSERT);
        }
    }

    @Override
    public void onLogComplaintSiteReceivedFailure(String strErr, int mode) {
        Log.d(TAG, "onLogComplaintSiteReceivedFailure");
        AppUtils.hideProgressDialog();
    if (mode == AppUtils.MODE_LOCAL) {
      AppUtils.showDialog(mActivity, strErr);
    } else new SiteAreaDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
    }

    @Override
    public void onZoneReceivedFailure(String strErr, int mode) {
        Log.d(TAG, "onZoneReceivedFailure");
        AppUtils.hideProgressDialog();
        if (mode == AppUtils.MODE_LOCAL) {
            AppUtils.showDialog(mActivity, strErr);
        } else new ZoneDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
    }

    @Override
    public void onContractReceivedSuccess(List<ContractEntity> contractList, int mode) {
        Log.d(TAG, "onContractReceivedSuccess");

        listJobNo = contractList;
        if (mode == AppUtils.MODE_SERVER) {
            AppUtils.hideProgressDialog();
            new ContractDbInitializer(mActivity, listJobNo, this).execute(AppUtils.MODE_INSERT);
        }
    }

    @Override
    public void onContractReceivedFailure(String strErr, int mode) {
        Log.d(TAG, "onContractReceivedFailure");
        AppUtils.hideProgressDialog();
        if (mode == AppUtils.MODE_LOCAL) {
            AppUtils.showDialog(mActivity, strErr);
        } else new ContractDbInitializer(mActivity, listJobNo, this).execute(AppUtils.MODE_INSERT);
    }

    @Override
    public void onPriorityReceivedSuccess(List<PriorityEntity> priorities, int mode) {
        Log.d(TAG, "onPriorityReceivedSuccess");
        listpriorities = priorities;
        if (mode == AppUtils.MODE_SERVER) {
            AppUtils.hideProgressDialog();
            new PriorityDbInitializer(mActivity, listpriorities, this).execute(AppUtils.MODE_INSERT);
        }
    }

    @Override
    public void onPriorityReceivedFailure(String strErr, int mode) {
        Log.d(TAG, "onPriorityReceivedFailure");
        AppUtils.hideProgressDialog();
        if (mode == AppUtils.MODE_LOCAL) {
            AppUtils.showDialog(mActivity, strErr);
        } else new PriorityDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
    }

    @Override
    public void onWorkTypeReceivedSuccess(List<WorkTypeEntity> typeEntities, int mode) {
        Log.d(TAG, "onWorkTypeReceivedSuccess");
        listWorkType = typeEntities;
        if (mode == AppUtils.MODE_SERVER) {
            AppUtils.hideProgressDialog();
            new WorkTypeDbInitializer(mActivity, listWorkType, this).execute(AppUtils.MODE_INSERT);
        }
    }

    @Override
    public void onWorkTypeReceivedFailure(String strErr, int mode) {
        Log.d(TAG, "onWorkTypeReceivedFailure");
        AppUtils.hideProgressDialog();
        if (mode == AppUtils.MODE_LOCAL) {
            AppUtils.showDialog(mActivity, strErr);
        } else new WorkTypeDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
    }

    @Override
    public void onLogComplaintDataReceivedSuccess(EmployeeIdRequest logComplaint, int mode) {
        Log.d(TAG, "onLogComplaintDataReceivedSuccess");
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
        Log.d(TAG, "onLogComplaintDataReceivedFailure");
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
                Fragment _fragment = new MainDashboard();
                FragmentTransaction _transaction = mManager.beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAllLogComplaintData(List<LogComplaintEntity> logComplaintEntities, int mode) {

    }

    @Override
    public void onBuildingDetailsReceivedSuccess(
            List<BuildingDetailsEntity> buildingDetailsEntities, int mode) {

        AppUtils.hideProgressDialog();
    }

    @Override
    public void onBuildingDetailsReceivedFailure(String strErr, int mode) {
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity, strErr);
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
                case R.id.tie_complaint:
                    validateComplaint();
                    break;
                case R.id.tie_property:
                    validateProperty();
                    break;
            }
        }
    }
}
