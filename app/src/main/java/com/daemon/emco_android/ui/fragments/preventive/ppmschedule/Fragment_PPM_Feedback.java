package com.daemon.emco_android.ui.fragments.preventive.ppmschedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.FeedBackService;
import com.daemon.emco_android.repository.remote.PpeService;
import com.daemon.emco_android.repository.remote.ReceiveComplaintViewService;
import com.daemon.emco_android.repository.remote.RiskAssessmentService;
import com.daemon.emco_android.repository.db.dbhelper.FbEmployeeDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.FeedbackDbInitializer;
import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.repository.db.entity.EmployeeDetailsEntity;
import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.daemon.emco_android.repository.db.entity.PPENameEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntity;
import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntityNew;
import com.daemon.emco_android.ui.fragments.common.Fragment_Main;
import com.daemon.emco_android.listeners.DatePickerDialogListener;
import com.daemon.emco_android.listeners.FeedbackListener;
import com.daemon.emco_android.listeners.PendingReasonsListner;
import com.daemon.emco_android.listeners.PpeListener;
import com.daemon.emco_android.listeners.ReceivecomplaintView_Listener;
import com.daemon.emco_android.listeners.RiskeAssListener;
import com.daemon.emco_android.listeners.TechRemarksListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.FetchFeedbackRequest;
import com.daemon.emco_android.model.request.PpmfeedbackemployeeReq;
import com.daemon.emco_android.model.request.ReceiveComplaintViewRequest;
import com.daemon.emco_android.model.request.RiskAssListRequest;
import com.daemon.emco_android.model.response.AttendedBy;
import com.daemon.emco_android.model.response.CheckedBy;
import com.daemon.emco_android.model.response.Object;
import com.daemon.emco_android.model.response.ObjectFeedBack;
import com.daemon.emco_android.model.response.ObjectPPM;
import com.daemon.emco_android.model.response.PpmEmployeeFeedResponse;
import com.daemon.emco_android.model.response.PpmFeedBackResponse;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.daemon.emco_android.utils.Utils.TAG_RATE_AND_SHARE_PPM;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_RATE__AND_FEEDBACK;

/**
 * Created by Daemonsoft on 7/25/2017.
 */
public class Fragment_PPM_Feedback extends Fragment
        implements FeedbackListener, PpeListener, TechRemarksListener, DatePickerDialogListener, ReceivecomplaintView_Listener, RiskeAssListener, PendingReasonsListner {
    private static final String TAG = Fragment_PPM_Feedback.class.getSimpleName();
    Gson gson = new Gson();
    Integer[] old_Attendedby = null;
    Integer[] old_Checkedby = null;
    private Type baseType = new TypeToken<List<String>>() {
    }.getType();
    private AppCompatActivity mActivity;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private CoordinatorLayout cl_main;
    private Bundle mArgs;
    private FragmentManager mManager;
    private String mUnSignedPage = AppUtils.ARGS_RECEIVECOMPLAINT_PAGE;
    private TextView tv_lbl_checkedby,tv_toolbar_title,
            tv_lbl_attendedby,
            tv_lbl_tech_remarks, tv_lbl_startdate, tv_lbl_enddate, tv_lbl_status,
            tv_lbl_technfeedback,
            tv_lbl_supervisorremarks,tv_select_from_date,tv_lbl_from_date;
    private TextView tv_select_checkedby, tv_select_attendedby, edt_status,txt_pending_reasons, tv_select_signstatus,tv_lbl_to_date,tv_select_to_date;
    private EditText et_technfeedback, et_supervisorremarks;
    private Button btn_feedback_save;
    private LinearLayout linear_pending_reasons, linear_todate, linear_fromdate;
    String fromDate="",toDate="";
    boolean startDateClicked;
    private Toolbar mToolbar;
    private View rootView;
    private int mModeDate;
    private String mStrRemark = null;
    private String ppmStatusArr = "";
    private String mFromDate = null, mToDate = null;
    private List<String> dailogItems = new ArrayList<>();
    private ReceiveComplaintViewService complaintView_service;
    private FeedBackService feedBackService;
    // private List<EmployeeDetailsEntity> entityList = new ArrayList<>();
    private List<ObjectFeedBack> checkedbyList_ppm = new ArrayList<>();
    private List<ObjectFeedBack> attentedList_ppm = new ArrayList<>();
    private ObjectPPM ObjectPPM;
    private List<ReceiveComplaintViewEntity> entityListNew = new ArrayList<>();
    private PpeService ppeService;
    private List<CheckedBy> checkedby_response_ppm = new ArrayList<>();
    private List<CheckedBy> empDetailsCheckBy = new ArrayList<>();
    private List<AttendedBy> empDetailsAttenBy = new ArrayList<>();
    private String actualDate = "";
    private List<CheckedBy> checkedby_ppm = new ArrayList<>();
    //private List<EmployeeDetailsEntity> attendedby_response = new ArrayList<>();
    private List<AttendedBy> attendedby_response_ppm = new ArrayList<>();
    //private List<EmployeeDetailsEntity> attendedby = new ArrayList<>();
    private List<AttendedBy> attendedby_ppm = new ArrayList<>();
    private String mStrEmpId = null, select_signstatus = null, mLoginData = null, mNetworkInfo = null;
    private List<String> listSignStatus = null;
    private String signSignatore = "";
    private PpmFeedBackResponse editppmFeedBack;
    private PpmEmployeeFeedResponse insertPpmFeedback = null;
    private List<String> ppmFeedbackStatus = new ArrayList<>();
    private List<String> pendingReasons = new ArrayList<>();

    private PpmScheduleDocBy ppmScheduleDocBy;
    private EditText edt_start_date, edt_end_date,et_remarks;
    private String complaint_number = "", opco_number = "", complaint_site = "";

    private boolean isPPEloaded=false;
    private boolean isRiskassesment=false;


    View.OnClickListener _OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick");
                    AppUtils.closeInput(cl_main);
                    switch (v.getId()) {
                        case R.id.btn_feedback_save:
                            submitForm();
                            break;
                        case R.id.tv_select_signstatus:
                            getTechRemarks();
                            break;
                        case R.id.tv_select_checkedby:
                            getCheckedby();
                            break;
                        case R.id.tv_select_attendedby:
                            getAttendedby();
                            break;
                        case R.id.tv_select_from_date :
                        {
                            startDateClicked=true;
                            showDateSelection();
                        }
                        break;
                        case R.id.tv_select_to_date :
                        {
                            startDateClicked=false;
                            showDateSelection();
                        }
                        break;

                        default:
                            break;
                    }
                }
            };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mManager = mActivity.getSupportFragmentManager();

            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            font = App.getInstance().getFontInstance();
            mArgs = getArguments();

            ppeService = new PpeService(mActivity, this);

            if (mActivity.getCurrentFocus() != null) {
                InputMethodManager imm =
                        (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
            }
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }
            feedBackService = new FeedBackService(mActivity, this);



            complaintView_service = new ReceiveComplaintViewService(mActivity, this);
            if (mArgs != null && mArgs.size() > 0) {
                ppmScheduleDocBy =
                        mArgs.getParcelable(AppUtils.ARGS_FEEDBACK_VIEW_DETAILS);
               /* scheduleDetails =
                        mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);*/
                if (mArgs.getString("complaint_number") != null && mArgs.getString("opco_number") != null && mArgs.getString("complaint_site") != null)
                    if (!mArgs.getString("complaint_number").equals("") && !mArgs.getString("opco_number").equals("") &&
                            !mArgs.getString("complaint_site").equals("")) {
                        complaint_number = mArgs.getString("complaint_number");
                        opco_number = mArgs.getString("opco_number");
                        complaint_site = mArgs.getString("complaint_site");
                    }



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
            rootView = inflater.inflate(R.layout.fragment_ppm_feedback, container, false);
            initUI(rootView);

            setProperties();
            fetchFeedbackData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void initUI(View rootView) {
        Log.d(TAG, "initUI");
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);

            tv_lbl_checkedby = (TextView) rootView.findViewById(R.id.tv_lbl_checkedby);
            tv_lbl_attendedby = (TextView) rootView.findViewById(R.id.tv_lbl_attendedby);
            tv_lbl_technfeedback = (TextView) rootView.findViewById(R.id.tv_lbl_technfeedback);
            tv_lbl_supervisorremarks = (TextView) rootView.findViewById(R.id.tv_lbl_supervisorremarks);

            tv_select_from_date= (TextView) rootView.findViewById(R.id.tv_select_from_date);
                    tv_lbl_from_date= (TextView) rootView.findViewById(R.id.tv_lbl_from_date);
            et_technfeedback = (EditText) rootView.findViewById(R.id.et_technfeedback);
            et_supervisorremarks = (EditText) rootView.findViewById(R.id.et_supervisorremarks);
            tv_select_signstatus = (TextView) rootView.findViewById(R.id.tv_select_signstatus);
            tv_lbl_to_date= (TextView) rootView.findViewById(R.id.tv_lbl_to_date);
            tv_select_to_date= (TextView) rootView.findViewById(R.id.tv_select_to_date);
            tv_lbl_tech_remarks = (TextView) rootView.findViewById(R.id.tv_lbl_tech_remarks);
            tv_lbl_startdate = (TextView) rootView.findViewById(R.id.tv_lbl_startdate);
            tv_lbl_enddate = (TextView) rootView.findViewById(R.id.tv_lbl_enddate);
            tv_lbl_status = (TextView) rootView.findViewById(R.id.tv_lbl_status);
            tv_select_checkedby = (TextView) rootView.findViewById(R.id.tv_select_checkedby);
            tv_select_attendedby = (TextView) rootView.findViewById(R.id.tv_select_attendedby);
            btn_feedback_save = (Button) rootView.findViewById(R.id.btn_feedback_save);
            edt_start_date = (EditText) rootView.findViewById(R.id.edt_start_date);
            edt_end_date = (EditText) rootView.findViewById(R.id.edt_end_date);
            et_remarks = (EditText) rootView.findViewById(R.id.et_remarks);
            edt_status = (TextView) rootView.findViewById(R.id.edt_status);
            txt_pending_reasons = (TextView) rootView.findViewById(R.id.txt_pending_reasons);

            linear_pending_reasons = (LinearLayout) rootView.findViewById(R.id.linear_pending_reasons);
            linear_fromdate = (LinearLayout) rootView.findViewById(R.id.linear_fromdate);
            linear_todate = (LinearLayout) rootView.findViewById(R.id.linear_todate);

            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
       // mToolbar.setTitle(getResources().getString(R.string.lbl_feedback));
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.lbl_feedback));
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

        tv_lbl_checkedby.setText(Html.fromHtml(getString(R.string.lbl_checkedby) + AppUtils.mandatory));
        tv_lbl_attendedby.setText(
                Html.fromHtml(getString(R.string.lbl_attendedby) + AppUtils.mandatory));
        tv_lbl_technfeedback.setText(
                Html.fromHtml(getString(R.string.lbl_technfeedback) + AppUtils.mandatory));
        tv_lbl_supervisorremarks.setText("Remarks");
        tv_lbl_tech_remarks.setText(
                Html.fromHtml(getString(R.string.lbl_tech_remarks) + AppUtils.mandatory));

        tv_lbl_tech_remarks.setTypeface(font.getHelveticaRegular());
        tv_lbl_startdate.setTypeface(font.getHelveticaRegular());
        tv_lbl_enddate.setTypeface(font.getHelveticaRegular());
        tv_lbl_status.setTypeface(font.getHelveticaRegular());
        tv_select_signstatus.setTypeface(font.getHelveticaRegular());
        tv_lbl_to_date.setTypeface(font.getHelveticaRegular());
        tv_select_to_date.setTypeface(font.getHelveticaRegular());
        tv_lbl_checkedby.setTypeface(font.getHelveticaRegular());
        tv_lbl_attendedby.setTypeface(font.getHelveticaRegular());
        tv_lbl_technfeedback.setTypeface(font.getHelveticaRegular());
        tv_lbl_supervisorremarks.setTypeface(font.getHelveticaRegular());
        tv_select_from_date.setTypeface(font.getHelveticaRegular());
        tv_lbl_from_date.setTypeface(font.getHelveticaRegular());
        edt_start_date.setTypeface(font.getHelveticaRegular());
        et_remarks.setTypeface(font.getHelveticaRegular());
        edt_end_date.setTypeface(font.getHelveticaRegular());
        edt_status.setTypeface(font.getHelveticaRegular());
        txt_pending_reasons.setTypeface(font.getHelveticaRegular());
        tv_select_checkedby.setTypeface(font.getHelveticaRegular());
        tv_select_attendedby.setTypeface(font.getHelveticaRegular());

        et_technfeedback.setTypeface(font.getHelveticaRegular());
       // et_supervisorremarks.setTypeface(font.getHelveticaRegular());

        btn_feedback_save.setTypeface(font.getHelveticaRegular());

        tv_select_checkedby.setOnClickListener(_OnClickListener);
        tv_select_attendedby.setOnClickListener(_OnClickListener);
        btn_feedback_save.setOnClickListener(_OnClickListener);
        tv_select_signstatus.setOnClickListener(_OnClickListener);

        tv_select_from_date.setOnClickListener(_OnClickListener);
        tv_select_to_date.setOnClickListener(_OnClickListener);

        if (ppmScheduleDocBy != null) {
            if (ppmScheduleDocBy.getPpmStatus() != null) {
                if (ppmScheduleDocBy.getPpmStatus().equals(AppUtils.COMPLETED)) {
                    btn_feedback_save.setVisibility(View.GONE);
                } else {
                    btn_feedback_save.setVisibility(View.VISIBLE);
                }
            }
        }
        if (ppmScheduleDocBy != null) {
            if (ppmScheduleDocBy.getPpmStatus() != null) {
                btn_feedback_save.setVisibility(
                        ppmScheduleDocBy.getPpmStatus().equals("C") ? View.GONE : View.VISIBLE);
            }
        }

        edt_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPpmStatus(edt_status);
            }
        });
        txt_pending_reasons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showPendingReasons();
            }
        });



    }



    private void fetchFeedbackData() {
        try {
            mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
            if (mNetworkInfo != null && mNetworkInfo.length() > 0) {
                if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {

                    if (ppmScheduleDocBy != null) {
                        AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                        RiskAssListRequest request = new RiskAssListRequest(ppmScheduleDocBy.getPpmNo());
                        new RiskAssessmentService(mActivity, this).getPpmDetails(request);

                    } else {
                        AppUtils.showDialog(
                                mActivity, "Some information is missing, Please contact customer care");
                    }
                }

                if (!complaint_number.equals("") && !opco_number.equals("") && !complaint_site.equals("")) {
                    FetchFeedbackRequest feedbackRequest =
                            new FetchFeedbackRequest(
                                    complaint_number,
                                    complaint_site,
                                    opco_number);
                    // feedBackService.fetchFeedbackDetailsData(feedbackRequest);
                } else {
                    if (ppmScheduleDocBy != null)
                        if (ppmScheduleDocBy.getCompanyCode() != null
                                ) {
                            FetchFeedbackRequest feedbackRequest =
                                    new FetchFeedbackRequest(
                                            ppmScheduleDocBy.getCompanyCode(),
                                            ppmScheduleDocBy.getCompanyCode(),
                                            ppmScheduleDocBy.getBatchSRL());
                            // feedBackService.fetchFeedbackDetailsData(feedbackRequest);
                        } else {
                            AppUtils.showDialog(
                                    mActivity, "Some information is missing, Please contact customer care");
                        }
                }


            } else {
                if ((ppmScheduleDocBy.getCompanyCode() != null
                        || ppmScheduleDocBy.getJobNo() != null)
                        && ppmScheduleDocBy.getCompanyCode() != null
                        ) {
                    EmployeeDetailsEntity request = new EmployeeDetailsEntity();
                    request.setOpco(ppmScheduleDocBy.getCompanyCode());
                    request.setContractNo(
                            ppmScheduleDocBy.getCompanyCode() != null
                                    ? ppmScheduleDocBy.getJobNo()
                                    : ppmScheduleDocBy.getCompanyCode());
                    request.setWorkCategory(ppmScheduleDocBy.getCompanyCode());

                    AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);

                      new FbEmployeeDbInitializer(mActivity, this, request).execute(AppUtils.MODE_GET);
                } else {
                    AppUtils.showDialog(
                            mActivity, "Some information is missing, Please contact customer care");
                }

                if (ppmScheduleDocBy.getCompanyCode() != null)
                    new FeedbackDbInitializer(
                            mActivity,
                            this,
                            new SaveFeedbackEntity(ppmScheduleDocBy.getCompanyCode()))
                            .execute(AppUtils.MODE_GET);

                String strJsonTr = mPreferences.getString(AppUtils.SHARED_TECH_REMARKS, null);
                Log.d(TAG, "str json :;" + strJsonTr);
                if (strJsonTr != null && strJsonTr.length() > 0) {
                    listSignStatus = gson.fromJson(strJsonTr, baseType);
                } else
                    AppUtils.showDialog(mActivity, getString(R.string.msg_no_data_found_in_local_db));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean editSubmit(PpmFeedBackResponse ppmFeedBackResponse, PpmEmployeeFeedResponse ppmEmployeeFeedResponse) {
        try {
            String msgErr = "";

            // checking pending attributes condition
            if(edt_status.getText().toString().equalsIgnoreCase("Pending")){

              if(txt_pending_reasons.getText().toString().equalsIgnoreCase(getString(R.string.select_the_reason))) {
                  AppUtils.setErrorBg(txt_pending_reasons, true);
                  msgErr = getString(R.string.select_the_reason);
                  return false;
              }
              else{
                  AppUtils.setErrorBg(txt_pending_reasons, false);

              }

                if(tv_select_from_date.getText().toString().equalsIgnoreCase(getString(R.string.lbl_select_from_date))) {
                    AppUtils.setErrorBg(tv_select_from_date, true);
                    msgErr = getString(R.string.lbl_select_from_date);
                    return false;
                }
                else{
                    AppUtils.setErrorBg(tv_select_from_date, false);
                }


                if(tv_select_to_date.getText().toString().equalsIgnoreCase(getString(R.string.lbl_select_to_date))) {
                    AppUtils.setErrorBg(tv_select_to_date, true);
                    msgErr = getString(R.string.lbl_select_to_date);
                    return false;
                }

            }


            if (ppmFeedBackResponse != null) {
                AppUtils.closeInput(cl_main);
                if (attendedby_ppm.isEmpty()) {
                    AppUtils.setErrorBg(tv_select_attendedby, true);
                    msgErr = getString(R.string.lbl_select_checkedby);
                    return false;
                }

                if (checkedby_ppm.isEmpty()) {
                    AppUtils.setErrorBg(tv_select_checkedby, true);
                    msgErr = msgErr + "\n" + getString(R.string.lbl_select_attendedby);
                    return false;
                }

                if (select_signstatus == null) {
                    AppUtils.setErrorBg(tv_select_signstatus, true);
                     msgErr = msgErr + "\n" + getString(R.string.lbl_select_tech_remarks);
                    return false;
                }

                if (msgErr != "") {
                    AppUtils.showDialog(mActivity, "Please select values in Mandatory field");
                    return false;
                }
                if (TextUtils.isEmpty(et_technfeedback.getText().toString())) {
                    et_technfeedback.setError(getString(R.string.lbl_feedback_required));
                    et_technfeedback.requestFocus();
                    return false;
                }
            } else {
                if (ppmEmployeeFeedResponse != null) {
                    AppUtils.closeInput(cl_main);
                    if (attendedby_ppm.isEmpty()) {
                        AppUtils.setErrorBg(tv_select_attendedby, true);
                        msgErr = getString(R.string.lbl_select_checkedby);
                        return false;
                    }

                    if (checkedby_ppm.isEmpty()) {
                        AppUtils.setErrorBg(tv_select_checkedby, true);
                        msgErr = msgErr + "\n" + getString(R.string.lbl_select_attendedby);
                        return false;
                    }
                    if (select_signstatus == null) {
                        AppUtils.setErrorBg(tv_select_signstatus, true);
                        msgErr = msgErr + "\n" + getString(R.string.lbl_select_tech_remarks);
                        return false;
                    }
                    if (msgErr != "") {
                        AppUtils.showDialog(mActivity, "Please select values in Mandatory field");
                        return false;
                    }
                    if (TextUtils.isEmpty(et_technfeedback.getText().toString())) {
                        et_technfeedback.setError(getString(R.string.lbl_feedback_required));
                        et_technfeedback.requestFocus();
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void submitForm() {
        try {
            boolean check = false;
            check = editSubmit(editppmFeedBack, insertPpmFeedback);
            if (check) {

                if(ppmStatusArr.equalsIgnoreCase("Completed")){
                    AppUtils.showProgressDialog(mActivity, "Saving PPE...", false);
                    checkCompletedCondition();
                }

                else{
                    AppUtils.showProgressDialog(mActivity, "Saving PPE...", false);
                    saveData();
                }

            } else {
                AppUtils.showDialog(mActivity, "Please select values in Mandatory field");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveData(){
        if (ppmScheduleDocBy != null) {
            SaveFeedbackEntityNew request =
                    new SaveFeedbackEntityNew(ppmScheduleDocBy.getCompanyCode());
            if (mPreferences
                    .getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE)
                    .contains(AppUtils.NETWORK_AVAILABLE)) {
                request.setOpco(ppmScheduleDocBy.getCompanyCode());
                request.setCreatedBy(mStrEmpId);
                if (editppmFeedBack != null) {
                    if (attendedby_ppm != null && !attendedby_ppm.isEmpty()) {
                        request.setAttendedBy(attendedby_ppm);
                    } else {
                        request.setAttendedBy(editppmFeedBack.getObject().getAttendedBy());
                    }
                } else {
                    request.setAttendedBy(attendedby_ppm);
                }
                if (editppmFeedBack != null) {
                    if (checkedby_ppm != null && !checkedby_ppm.isEmpty()) {
                        request.setCheckedBy(checkedby_ppm);
                    } else {
                        request.setCheckedBy(editppmFeedBack.getObject().getCheckedBy());
                    }
                } else {
                    request.setCheckedBy(checkedby_ppm);
                }
                if (editppmFeedBack != null) {

                    request.setActStartDate(DateFormat.getDateTimeInstance().format(new Date()));

                } else {
                    request.setActStartDate(DateFormat.getDateTimeInstance().format(new Date()));
                }
                if (editppmFeedBack != null) {

                    request.setActEndDate(DateFormat.getDateTimeInstance().format(new Date()));

                } else {
                    request.setActEndDate(DateFormat.getDateTimeInstance().format(new Date()));
                }
                if (editppmFeedBack != null) {
                    if (select_signstatus != null && !select_signstatus.isEmpty()) {
                        request.setCustomerSignStatus(select_signstatus);
                    } else {
                        request.setCustomerSignStatus(select_signstatus);
                    }
                } else {
                    request.setCustomerSignStatus(select_signstatus);
                }
                if (editppmFeedBack != null) {
                    request.setFeedbackInformation(et_technfeedback.getText().toString());
                } else {
                    request.setFeedbackInformation(et_technfeedback.getText().toString());
                }
                if (editppmFeedBack != null) {
                    if (ppmStatusArr != null && !ppmStatusArr.isEmpty()) {
                        request.setPpmStatus(ppmStatusArr);
                    } else {
                        request.setPpmStatus(editppmFeedBack.getObject().getPpmStatus());
                    }
                } else {
                    request.setPpmStatus(ppmStatusArr);
                }


                if(!et_remarks.getText().toString().equalsIgnoreCase(getString(R.string.enter_the_remarks))){
                    request.setRemark(et_remarks.getText().toString());
                }


                if(edt_status.getText().toString().equalsIgnoreCase("Pending")){

                    if(!txt_pending_reasons.getText().toString().equalsIgnoreCase(getString(R.string.select_the_reason))) {

                        request.setPendingReason(txt_pending_reasons.getText().toString());
                    }


                    if(!tv_select_from_date.getText().toString().equalsIgnoreCase(getString(R.string.lbl_select_from_date))) {
                        request.setNewStartDate(tv_select_from_date.getText().toString());
                    }

                if(!tv_select_to_date.getText().toString().equalsIgnoreCase(getString(R.string.lbl_select_to_date))) {
                    request.setNewEndDate(tv_select_to_date.getText().toString());
                } }

                request.setPpmNo(ppmScheduleDocBy.getPpmNo());
                feedBackService.postFeedbackDetailsDataNew(request);

            } else {
                request.setMode(AppUtils.MODE_LOCAL);
                AppUtils.showDialog(mActivity, "Saved successfully");
            }
        } }

    public void getAttendedby() {
        try {
            final String[] strArraySiteName = new String[attentedList_ppm.size()];
            ArrayList<Integer> list = new ArrayList();
            for (int i = 0; i < attentedList_ppm.size(); i++) {
                String s = attentedList_ppm.get(i).getEmplCode() + " - " + attentedList_ppm.get(i).getEmplName();
                strArraySiteName[i] = s;
                if (attendedby_response_ppm != null && old_Attendedby == null) {
                    for (AttendedBy entity : attendedby_response_ppm) {
                        if (s.equals(entity.getEmplCode() + " - " + entity.getEmplName())) {
                            if (!list.contains(i)) {
                                list.add(i);
                            }
                        }
                    }
                    old_Attendedby = list.toArray(new Integer[list.size()]);
                }
            }
            if (old_Attendedby != null) {
                for (int i : old_Attendedby) {
                    if (!list.contains(i)) {
                        list.add(i);
                    }
                }
                old_Attendedby = list.toArray(new Integer[list.size()]);
            }
            new MaterialDialog.Builder(mActivity)
                    .title(R.string.lbl_select_checkedby)
                    .items(strArraySiteName)
                    .itemsCallback(
                            new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(
                                        MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                    if (!TextUtils.isEmpty(text)) {
                                        attendedby_ppm.clear();
                                        String a = "";
                                        for (AttendedBy item : empDetailsAttenBy) {
                                            if (strArraySiteName[position].equals(
                                                    item.getEmplCode() + " - " + item.getEmplName())) {
                                                attendedby_ppm.add(item);

                                                a = item.getEmplName();
                                            }
                                        }
                                        tv_select_attendedby.setText(a);
                                        tv_select_attendedby.setTypeface(font.getHelveticaBold());
                                        AppUtils.setErrorBg(tv_select_attendedby, false);
                                    } else {
                                        attendedby_ppm.clear();
                                        tv_select_attendedby.setText("");
                                        tv_select_attendedby.setHint(getString(R.string.lbl_select_checkedby));
                                        AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                                        AppUtils.setErrorBg(tv_select_attendedby, true);
                                    }
                                    AppUtils.closeInput(cl_main);
                                }
                            }).canceledOnTouchOutside(false)
                    .positiveText(R.string.lbl_done)
                    .negativeText(R.string.lbl_close)
                    .show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getCheckedby() {
        try {
            final String[] strArrayData = new String[checkedbyList_ppm.size()];
            ArrayList<Integer> list = new ArrayList();
            for (int i = 0; i < checkedbyList_ppm.size(); i++) {
                String data =
                        checkedbyList_ppm.get(i).getEmplCode() + " - " + checkedbyList_ppm.get(i).getEmplName();
                strArrayData[i] = data;
                if (checkedby_response_ppm != null && old_Checkedby == null) {
                    for (CheckedBy entity : checkedby_response_ppm) {
                        if (data.equals(entity.getEmplCode() + " - " + entity.getEmplName())) {
                            if (!list.contains(i)) {
                                list.add(i);
                            }
                        }
                    }
                    old_Checkedby = list.toArray(new Integer[list.size()]);
                }
            }
            if (old_Checkedby != null) {
                for (int i : old_Checkedby) {
                    if (!list.contains(i)) {
                        list.add(i);
                    }
                }
                old_Checkedby = list.toArray(new Integer[list.size()]);
            }
            new MaterialDialog.Builder(mActivity)
                    .title(R.string.lbl_select_checkedby)
                    .items(strArrayData)
                    .itemsCallback(
                            new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(
                                        MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                    if (!TextUtils.isEmpty(text)) {
                                        checkedby_ppm.clear();
                                        String a = "";
                                        for (CheckedBy item : empDetailsCheckBy) {
                                            if (strArrayData[position].equals(
                                                    item.getEmplCode() + " - " + item.getEmplName())) {
                                                checkedby_ppm.add(item);
                                                a = item.getEmplName();
                                            }
                                        }
                                        tv_select_checkedby.setText(a);
                                        tv_select_checkedby.setTypeface(font.getHelveticaBold());
                                        AppUtils.setErrorBg(tv_select_checkedby, false);
                                    } else {
                                        checkedby_ppm.clear();
                                        tv_select_checkedby.setText("");
                                        tv_select_checkedby.setHint(getString(R.string.lbl_select_checkedby));
                                        AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                                        AppUtils.setErrorBg(tv_select_checkedby, true);
                                    }
                                    AppUtils.closeInput(cl_main);
                                }
                            }).canceledOnTouchOutside(false)
                    .positiveText(R.string.lbl_done)
                    .negativeText(R.string.lbl_close)
                    .show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getTechRemarks() {
        Log.d(TAG, "getTechRemarks");
        try {
            if (listSignStatus.size() > 0) {
                new MaterialDialog.Builder(mActivity)
                        .title(R.string.lbl_select_tech_remarks)
                        .items(listSignStatus)
                        .itemsCallbackSingleChoice(
                                -1,
                                new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(
                                            MaterialDialog dialog, View view, int which, CharSequence text) {
                                        try {
                                            if (which >= 0) {
                                                select_signstatus = text.toString();
                                                if (ppmScheduleDocBy != null)
                                                    //scheduleDetails.setCustomerSignStatus(text.toString());
                                                    tv_select_signstatus.setText(text.toString());
                                                if (editppmFeedBack != null) {
                                                    editppmFeedBack.getObject().setCustomerSignStatus(text.toString());
                                                }
                                                tv_select_signstatus.setTypeface(font.getHelveticaBold());
                                                AppUtils.setErrorBg(tv_select_signstatus, false);
                                            } else {
                                                select_signstatus = null;
                                                tv_select_signstatus.setText("");
                                                tv_select_signstatus.setHint(getString(R.string.lbl_select_tech_remarks));
                                                AppUtils.showDialog(
                                                        mActivity, getString(R.string.no_value_has_been_selected));
                                                AppUtils.setErrorBg(tv_select_signstatus, true);
                                            }
                                            AppUtils.closeInput(cl_main);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
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

    @Override
    public void onFeedbackEmployeeDetailsReceivedSuccess(
            List<EmployeeDetailsEntity> employeeDetailsEntities, int mode) {
        Log.d(TAG, "onFeedbackEmployeeDetailsReceivedSuccess");

    }

    @Override
    public void onFeedbackDetailsReceivedSuccess(SaveFeedbackEntity rcFeedbackEntity, int mode) {
        Log.d(TAG, "onFeedbackDetailsReceivedSuccess");
        try {
            AppUtils.hideProgressDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAllFeedbackDetailsReceivedSuccess(
            List<SaveFeedbackEntity> saveFeedbackEntities, int mode) {
    }

    @Override
    public void onFeedbackDetailsReceivedSuccessPPMAtten(PpmEmployeeFeedResponse saveFeedbackEntity) {
        checkedbyList_ppm.addAll(saveFeedbackEntity.getObject());
        attentedList_ppm.addAll(saveFeedbackEntity.getObject());
        insertPpmFeedback = saveFeedbackEntity;
        CheckedBy checkedBy = null;
        for (ObjectFeedBack checkedBy1 : saveFeedbackEntity.getObject()) {
            checkedBy = new CheckedBy();
            checkedBy.setCompCode(checkedBy1.getCompCode());
            checkedBy.setEmplCode(checkedBy1.getEmplCode());
            checkedBy.setEmplName(checkedBy1.getEmplName());
            checkedBy.setTeamCode(checkedBy1.getTeamCode());
            checkedby_response_ppm.add(checkedBy);
            empDetailsCheckBy.add(checkedBy);
        }
        AttendedBy attendedBy = null;
        for (ObjectFeedBack attenedBy1 : saveFeedbackEntity.getObject()) {
            attendedBy = new AttendedBy();
            attendedBy.setCompCode(attenedBy1.getCompCode());
            attendedBy.setEmplCode(attenedBy1.getEmplCode());
            attendedBy.setEmplName(attenedBy1.getEmplName());
            attendedBy.setTeamCode(attenedBy1.getTeamCode());
            attendedby_response_ppm.add(attendedBy);
            empDetailsAttenBy.add(attendedBy);
        }
    }

    @Override
    public void onFeedbackEmployeeDetailsSaveSuccess(String strMsg, int mode) {
        Log.d(TAG, "onFeedbackEmployeeDetailsSaveSuccess" + strMsg);
        try {
            AppUtils.hideProgressDialog();
            MaterialDialog.Builder builder =
                    new MaterialDialog.Builder(mActivity)
                            .content(strMsg)
                            .positiveText(R.string.lbl_okay)
                            .stackingBehavior(StackingBehavior.ADAPTIVE)
                            .onPositive(
                                    new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(
                                                @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                            if (editppmFeedBack != null) {
                                                if (ppmStatusArr.equalsIgnoreCase("Completed") &&
                                                        select_signstatus.equalsIgnoreCase(AppUtils.CLOSEWITHSIGN)) {
                                                    Fragment_PM_RateService fragment = new Fragment_PM_RateService();
                                                    Bundle mdata = new Bundle();
                                                    mdata.putParcelable(AppUtils.ARGS_PPM_FEEDBACK, ppmScheduleDocBy);
                                                    mdata.putString(AppUtils.ARGS_PPM_FEEDBACK_CHECK, "ppmcheckList");
                                                    fragment.setArguments(mdata);
                                                    FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                                                    fragmentTransaction.replace(
                                                            R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_RATE__AND_FEEDBACK);
                                                    fragmentTransaction.addToBackStack(TAG_RATE_AND_SHARE_PPM);
                                                    fragmentTransaction.commit();
                                                } else {
                                                    mManager.popBackStack();
                                                }
                                            } else {
                                                if (ppmStatusArr.equalsIgnoreCase("Completed") && select_signstatus.equalsIgnoreCase(AppUtils.CLOSEWITHSIGN)) {
                                                    Fragment_PM_RateService fragment = new Fragment_PM_RateService();
                                                    Bundle mdata = new Bundle();
                                                    mdata.putParcelable(AppUtils.ARGS_PPM_FEEDBACK, ppmScheduleDocBy);
                                                    mdata.putString(AppUtils.ARGS_PPM_FEEDBACK_CHECK, "ppmcheckList");
                                                    fragment.setArguments(mdata);
                                                    FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                                                    fragmentTransaction.replace(
                                                            R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_RATE__AND_FEEDBACK);
                                                    fragmentTransaction.addToBackStack(TAG_RATE_AND_SHARE_PPM);
                                                    fragmentTransaction.commit();
                                                } else {
                                                    mManager.popBackStack();
                                                }

                                            }
                                        }
                                    });

            MaterialDialog dialog = builder.build();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFeedbackPpmStatusSucess(List<String> strMsg, int mode) {
        ppmFeedbackStatus.addAll(strMsg);
    }

    public void getPpmStatus(final TextView tv_select_remarks) {
        try {
            if (ppmFeedbackStatus.isEmpty()) return;
            MaterialDialog.Builder dialog = new MaterialDialog.Builder(mActivity);
            dialog.title(R.string.lbl_select_remarks);
            if (ppmFeedbackStatus.isEmpty()) dialog.items(R.array.string_array_remarks);
            else dialog.items(ppmFeedbackStatus);
            dialog.itemsCallbackSingleChoice(
                    -1,
                    new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(
                                MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which >= 0) {
                                ppmStatusArr = text.toString();
                                if (editppmFeedBack != null) {
                                    editppmFeedBack.getObject().setPpmStatus(editppmFeedBack.getObject().getPpmStatus());
                                }
                                tv_select_remarks.setText(text.toString());
                                tv_select_remarks.setTypeface(font.getHelveticaBold());
                                AppUtils.setErrorBg(tv_select_remarks, false);

                                if(text.toString().equalsIgnoreCase("Pending")){
                                    linear_pending_reasons.setVisibility(View.VISIBLE);
                                    linear_todate.setVisibility(View.VISIBLE);
                                    linear_fromdate.setVisibility(View.VISIBLE);
                                }
                                else{
                                    linear_pending_reasons.setVisibility(View.GONE);
                                    linear_todate.setVisibility(View.GONE);
                                    linear_fromdate.setVisibility(View.GONE);

                                    txt_pending_reasons.setTypeface(font.getHelveticaRegular());
                                    tv_select_from_date.setTypeface(font.getHelveticaRegular());
                                    tv_select_to_date.setTypeface(font.getHelveticaRegular());

                                    txt_pending_reasons.setText(getString(R.string.select_the_reason));
                                    tv_select_from_date.setText(getString(R.string.lbl_select_from_date));
                                    tv_select_to_date.setText(getString(R.string.lbl_select_to_date));



                                }

                            } else {
                                AppUtils.setErrorBg(tv_select_remarks, true);
                                mStrRemark = null;
                                tv_select_remarks.setText("");
                                tv_select_remarks.setHint(getString(R.string.lbl_select_remarks));
                                AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                            }

                            return true;
                        }
                    });
            dialog.positiveText(R.string.lbl_done);
            dialog.negativeText(R.string.lbl_close);
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onFeedbackEmployeeDetailsReceivedFailure(String strErr, int mode) {
        Log.d(TAG, "onFeedbackEmployeeDetailsReceivedFailure");
        try {
            AppUtils.hideProgressDialog();
            if (complaint_number != null && opco_number != null && complaint_site != null)
                if (!complaint_number.equals("") && !opco_number.equals("") && !complaint_site.equals("")) {
                    AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                    complaintView_service.GetReceiveComplaintViewData(
                            new ReceiveComplaintViewRequest(
                                    complaint_number,
                                    complaint_site,
                                    opco_number));
                    //  select_signstatus = null;
                    tv_select_signstatus.setText("");
                    tv_select_signstatus.setHint(getString(R.string.lbl_select_tech_remarks));
                   /* AppUtils.showDialog(
                            mActivity, getString(R.string.no_value_has_been_selected));
                    AppUtils.setErrorBg(tv_select_signstatus, true);*/
                } else {
                    // select_signstatus = null;
                    tv_select_signstatus.setText("");
                    tv_select_signstatus.setHint(getString(R.string.lbl_select_tech_remarks));
                    if (strErr.contains("Data not found"))
                        return;
                    AppUtils.showDialog(mActivity, strErr);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    // mActivity.onBackPressed();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTechRemarksReceived(List<String> technicalRemarks) {

        Log.d(TAG, "onTechnicalRemarksReceived");
        listSignStatus = technicalRemarks;
        AppUtils.hideProgressDialog();
        String strTechRemarks = gson.toJson(technicalRemarks, baseType);
        mEditor.putString(AppUtils.SHARED_TECH_REMARKS, strTechRemarks);
        mEditor.commit();
    }

    @Override
    public void onechRemarksReceivedError(String strErr) {
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity, strErr);
    }

    @Override
    public void onReceiveComplaintRemarksReceived(List<String> remarkList, int mode) {

    }

    @Override
    public void onReceiveComplaintRespondReceived(String strMsg, String complaintNumber, int mode) {

    }

    @Override
    public void onReceiveComplaintViewReceived(List<ReceiveComplaintViewEntity> complaintViewEntities, int mode) {

    }

    @Override
    public void onReceiveComplaintViewAssetDetailsReceived(List<AssetDetailsEntity> assetDetailsEntities, int mode) {

    }

    @Override
    public void onReceiveComplaintViewReceivedError(String msg, int mode) {

    }

    @Override
    public void onReceiveBarCodeAssetReceived(String msg, int mode) {

    }

    @Override
    public void onAllReceiveComplaintData(List<ReceiveComplaintRespondEntity> complaintRespondEntities, int modeLocal) {

    }

    @Override
    public void onReceiveComplaintBycomplaintNumber(ReceiveComplaintRespondEntity complaintRespondEntity, int modeLocal) {

    }


    @Override
    public void onListAssSuccess(List<Object> login) {

        if(isRiskassesment)
        {
            isRiskassesment=false;
            if(login.get(0).getRaetComments()==null){

                AppUtils.hideProgressDialog();
                AppUtils.showDialog(getContext(),"Risk Assessment should not be empty before completing the PPM");
            }
            else{

                RiskAssListRequest request = new RiskAssListRequest(ppmScheduleDocBy.getCompanyCode(), ppmScheduleDocBy.getJobNo(),
                        ppmScheduleDocBy.getPpmNo(), ppmScheduleDocBy.getChkCode());
                new RiskAssessmentService(mActivity, this).getEquipemList(request);
            }
        }
        else{

         if(login.get(0).getRaetTagNo()==null){

             AppUtils.hideProgressDialog();
             AppUtils.showDialog(getContext(),"Equipment tool should not be empty before completing the PPM");

         }
          else{
             saveData();
           }
        }

    }

    @Override
    public void onPPMDataSuccess(List<com.daemon.emco_android.model.response.ObjectPPM> loginppm) {

    }

    @Override
    public void onPPMDataSuccessPpmFeedBack(PpmFeedBackResponse loginppm) {
        try {
            AppUtils.hideProgressDialog();
            if (loginppm.getObject().getCheckedBy() != null && loginppm.getObject().getAttendedBy() != null) {
                editppmFeedBack = loginppm;
                tv_select_checkedby.setText(loginppm.getObject().getCheckedBy().get(0).getEmplName());
                tv_select_attendedby.setText(loginppm.getObject().getAttendedBy().get(0).getEmplName());
                tv_select_signstatus.setText(loginppm.getObject().getCustomerSignStatus());
                et_technfeedback.setText(loginppm.getObject().getFeedbackInformation());
                et_remarks.setText(loginppm.getObject().getRemark());


                if(loginppm.getObject().getPpmStatus().equalsIgnoreCase("Pending")){

                    if(checkEmpty(loginppm.getObject().getPendingReason()))
                    txt_pending_reasons.setText(loginppm.getObject().getPendingReason());

                    if(checkEmpty(loginppm.getObject().getNewStartDate()))
                    tv_select_from_date.setText(changeStringFormat(loginppm.getObject().getNewStartDate()));

                    if(checkEmpty(loginppm.getObject().getNewEndDate()))
                    tv_select_to_date.setText(changeStringFormat(loginppm.getObject().getNewEndDate()));
                }



                checkedby_ppm.addAll(loginppm.getObject().getCheckedBy());
                attendedby_ppm.addAll(loginppm.getObject().getAttendedBy());
                if(loginppm.getObject().getPpmStatus()!=null){
                    edt_status.setText(loginppm.getObject().getPpmStatus());
                    if(loginppm.getObject().getPpmStatus().equalsIgnoreCase("Completed")){
                        edt_status.setClickable(false);
                    }else{
                        edt_status.setClickable(true);
                    }
                }
                if (loginppm.getObject().getCustomerSignStatus() != null) {
                    select_signstatus = loginppm.getObject().getCustomerSignStatus();
                }
                if (loginppm.getObject().getPpmStatus() != null) {
                    ppmStatusArr = loginppm.getObject().getPpmStatus();
                }
                if (loginppm.getObject().getActStartDate() != null && !loginppm.getObject().getActStartDate().equalsIgnoreCase("null")) {
                    edt_start_date.setText(changeStringFormat2(loginppm.getObject().getActStartDate()));
                }
                else{
                    edt_start_date.setText("");
                }
                if (loginppm.getObject().getActEndDate() != null && !loginppm.getObject().getActEndDate().equalsIgnoreCase("null")) {
                    edt_end_date.setText(changeStringFormat2(loginppm.getObject().getActEndDate()));
                }
                else{
                    edt_end_date.setText("");
                }
                feedBackService.getFeedbackEmployeeDetailsDataPPM(new PpmfeedbackemployeeReq(ppmScheduleDocBy.getCompanyCode(), ppmScheduleDocBy.getTeamCode()));
                feedBackService.getTechnicalRemarksData(this);
                feedBackService.getPpmWorkStatus();
            } else {
                feedBackService.getFeedbackEmployeeDetailsDataPPM(new PpmfeedbackemployeeReq(ppmScheduleDocBy.getCompanyCode(), ppmScheduleDocBy.getTeamCode()));
                feedBackService.getTechnicalRemarksData(this);
                feedBackService.getPpmWorkStatus();
                if (loginppm.getObject().getActStartDate() != null) {
                    edt_start_date.setText(loginppm.getObject().getActStartDate());
                }
                if (loginppm.getObject().getActEndDate() != null) {
                    edt_end_date.setText(loginppm.getObject().getActEndDate());
                }
            }

            if(loginppm.getObject().getPpmStatus()!=null){
                if(loginppm.getObject().getPpmStatus().equalsIgnoreCase("Pending")){
                    linear_pending_reasons.setVisibility(View.VISIBLE);
                    linear_todate.setVisibility(View.VISIBLE);
                    linear_fromdate.setVisibility(View.VISIBLE);
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveDataSuccess(String commonResponse) {

    }

    @Override
    public void onListAssFailure(String login) {
        feedBackService.getFeedbackEmployeeDetailsDataPPM(new PpmfeedbackemployeeReq(ppmScheduleDocBy.getCompanyCode(), ppmScheduleDocBy.getTeamCode()));
        feedBackService.getTechnicalRemarksData(this);
        feedBackService.getPpmWorkStatus();
    }


    public void checkCompletedCondition(){

        RiskAssListRequest riskAssListResponse=new RiskAssListRequest(ppmScheduleDocBy.getCompanyCode()
                ,ppmScheduleDocBy.getJobNo(),ppmScheduleDocBy.getPpmNo(),ppmScheduleDocBy.getChkCode(),"");
        ppeService.getSavePpePpm(riskAssListResponse);

    }

    @Override
    public void onPPENameListReceived(List<PPENameEntity> ppeNameEntities, int mode){


    }

    @Override
    public void onPPESaveSuccess(String strMsg, int mode){}

    @Override
    public void onPPESaveClicked(List<PPENameEntity> ppeNameEntities, List<PPEFetchSaveEntity> ppeSaveEntities, boolean isFetchdata){}

    @Override
    public void onPPEFetchListSuccess(List<PPEFetchSaveEntity> ppeSaveEntities, int mode){


           if(ppeSaveEntities.get(0).getPpeUsed()==null){
               isPPEloaded=false;
            }
           else{
               isPPEloaded=true;
           }

        isRiskassesment=true;
        RiskAssListRequest request = new RiskAssListRequest(ppmScheduleDocBy.getCompanyCode(), ppmScheduleDocBy.getJobNo(),
                ppmScheduleDocBy.getPpmNo(), ppmScheduleDocBy.getChkCode());
        new RiskAssessmentService(mActivity, this).getRiskassesList(request);




    }

    @Override
    public void onPPEFetchListFailure(String strErr, int mode){

        AppUtils.hideProgressDialog();
        AppUtils.showDialog(getContext(),"PPE should not be empty before completing the PPM");

    }

    @Override
    public void onPPESaveFailure(String strErr, int mode){}
    @Override
    public void onPPENameListFailure(String strErr, int mode){}

    @Override
    public void onPendingReasonsReceived(List<String> pending){

     //   AppUtils.hideProgressDialog();
        pendingReasons=pending;
        showPendingReasons();
    }

    @Override
    public void onPendingReasonsReceivedError(String strErr){

    }

    public void showDateSelection(){
        AppUtils.datePickerDialog(mActivity, this, null, null);
    }


    @Override
    public void onDateReceivedSuccess(String strDate) {
        Log.d(TAG, "onDateReceivedSuccess " + strDate);


        if(startDateClicked){

            fromDate=(strDate);
            tv_select_from_date.setText(strDate);
            tv_select_from_date.setTypeface(font.getHelveticaBold());

        }
        else{

            toDate=strDate;
            tv_select_to_date.setText(strDate);
            tv_select_to_date.setTypeface(font.getHelveticaBold());

        }
    }

    public void showPendingReasons(){


        if(pendingReasons.size()!=0){
            MaterialDialog.Builder dialog = new MaterialDialog.Builder(mActivity);
            dialog.title(R.string.lbl_select_pending);
           dialog.items(pendingReasons);
            dialog.itemsCallbackSingleChoice(
                    -1,
                    new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(
                                MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which >= 0) {

                                txt_pending_reasons.setText(text.toString());
                                txt_pending_reasons.setTypeface(font.getHelveticaBold());
                                AppUtils.setErrorBg(txt_pending_reasons, false);
                            } else {
                                AppUtils.setErrorBg(txt_pending_reasons, true);

                            //    txt_pending_reasons.setText("");
                                txt_pending_reasons.setHint(getString(R.string.lbl_select_remarks));
                                AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                            }

                            return true;
                        }
                    });
            dialog.positiveText(R.string.lbl_done);
            dialog.negativeText(R.string.lbl_close);
            dialog.show();

        }


        else{

            feedBackService.getPendingReasons(this);

        }


    }


    public  String changeStringFormat(String dt){

       try
       {
           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
           Date newDate = format.parse(dt);

           format = new SimpleDateFormat("dd-MMM-yyyy");
           return   format.format(newDate);
       }
       catch (Exception e){
           return  dt;
       }

    }

    public boolean checkEmpty(String ss){

        if(ss==null || ss.equalsIgnoreCase("null") || ss.equalsIgnoreCase("")){
           return false;
        }
        else{
            return true;
        }

    }


    public  String changeStringFormat2(String dt){

        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
            Date newDate = format.parse(dt);

            format = new SimpleDateFormat("dd-MMM-yyyy");
            return   format.format(newDate);
        }
        catch (Exception e){
            return  dt;
        }

    }


}
