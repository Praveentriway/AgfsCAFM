package com.daemon.emco_android.ui.fragments.common;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.listeners.UserListener;
import com.daemon.emco_android.model.common.EmployeeList;
import com.daemon.emco_android.model.request.LoginRequest;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.repository.db.entity.UserToken;
import com.daemon.emco_android.repository.remote.EmployeeGpsRepository;
import com.daemon.emco_android.repository.remote.UserService;
import com.daemon.emco_android.service.EmployeeTrackingService;
import com.daemon.emco_android.ui.activities.LoginActivity;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.dbhelper.ReceiveComplaintItemDbInitializer;
import com.daemon.emco_android.ui.activities.MainActivity;
import com.daemon.emco_android.ui.fragments.attendance.AttendanceFragment;
import com.daemon.emco_android.ui.fragments.inspection.Fragment_InspectionModule_Landing;
import com.daemon.emco_android.ui.fragments.locationfinder.JobLocationFinder;
import com.daemon.emco_android.ui.fragments.survey.SurveyHeader;
import com.daemon.emco_android.ui.fragments.preventive.PpmDashboard;
import com.daemon.emco_android.ui.fragments.reactive.receieve_complaints.Fragment_RC_List;
import com.daemon.emco_android.ui.fragments.reactive.dashboard.Fragment_RM_Dashboard;
import com.daemon.emco_android.ui.fragments.reactive.logcomplaints.Fragment_RM_LogComplaintUser;
import com.daemon.emco_android.ui.fragments.reactive.Fragment_RM_SearchComplaint;
import com.daemon.emco_android.ui.fragments.reactive.FragmentRxSubmenu;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.SessionManager;
import com.daemon.emco_android.utils.Utils;
import com.github.florent37.expectanim.ExpectAnim;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import static android.content.Context.MODE_PRIVATE;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYTYPE;
import static com.daemon.emco_android.utils.AppUtils.MODE_REMOTE;
import static com.daemon.emco_android.utils.Utils.TAG_LOG_COMPLAINT;
import static com.github.florent37.expectanim.core.Expectations.atItsOriginalPosition;
import static com.github.florent37.expectanim.core.Expectations.invisible;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.visible;

public class MainDashboard extends Fragment implements View.OnClickListener, UserListener,EmployeeGpsRepository.EmployeeGPSListener {
    private static final String TAG = MainDashboard.class.getSimpleName();
    final Handler handler = new Handler();
    private AppCompatActivity mActivity;
    Intent  mServiceIntent;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private LinearLayout ll_main_cust, ll_main_tech, ll_main_tech2;

    private CardView  btnPreventive, btnInspection, btnSurvey,btn_location_module,btn_attendance;
    private ExpectAnim expectAnimMove;
    CardView btnReactive;
    private ImageView btnLogComplaint,
            btnReceiveComplaint,
            btnDashboard,
            btn_view_complaint,
            btnViewComplaintStatus,
            profile_action;
    private String mStringJson = null;
    private Bundle mArgs;
    private Gson gson = new Gson();
    private Login user;
    private CoordinatorLayout cl_main;
    private Toolbar mToolbar;
    private TextView tv_toolbar_title, tv_user_name;
    LinearLayout linear_toolbar;
    private View rootView = null;
    private TimerTask mTimerTask;
    private Timer t = new Timer();
    private FloatingActionButton fab_menu;
    private String mStrEmpId=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
            mEditor = mPreferences.edit();
            mStringJson = mPreferences.getString(AppUtils.SHARED_LOGIN, null);

            mManager = mActivity.getSupportFragmentManager();
            font = App.getInstance().getFontInstance();
            mArgs = getArguments();

            if (mStringJson != null) {
                user = gson.fromJson(mStringJson, Login.class);
                mStrEmpId = user.getEmployeeId();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void timeout() {
        mTimerTask =
                new TimerTask() {
                    public void run() {
                        handler.post(
                                new Runnable() {
                                    public void run() {
                                        MaterialDialog.Builder builder =
                                                new MaterialDialog.Builder(mActivity)
                                                        .content(getString(R.string.sess_exp_msg))
                                                        .title("You've timed out")
                                                        .positiveText(R.string.lbl_okay)
                                                        .stackingBehavior(StackingBehavior.ADAPTIVE)
                                                        .onPositive(
                                                                new MaterialDialog.SingleButtonCallback() {
                                                                    @Override
                                                                    public void onClick(
                                                                            @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                        dialog.dismiss();
                                                                        clearPreferences();
                                                                        Intent intent = new Intent(mActivity, LoginActivity.class);
                                                                        mActivity.startActivity(intent);
                                                                        mActivity.finish();
                                                                    }
                                                                });
                                        MaterialDialog dialog = builder.build();
                                        dialog.setCanceledOnTouchOutside(false);
                                        dialog.setCancelable(false);
                                        dialog.show();
                                    }
                                });
                    }
                };

        t.scheduleAtFixedRate(mTimerTask, AppUtils.TIMEOUT_NEW, AppUtils.TIMEOUT);
    }

    String token;

    public void clearToken(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("", "getInstanceId failed", task.getException());
                            return;
                        }
                        token = task.getResult().getToken();

                        deleteToken();
                    }
                });

    }

    public void deleteToken(){
        UserToken user=new UserToken();
        user.setUserId(mStrEmpId);
        user.setToken(token);
        new UserService(mActivity, this).deleteToken(user);
    }

    public void cleartime() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
    }

    public void clearPreferences() {
        Log.d(TAG, "clearPreferences");
        try {
            mEditor = mPreferences.edit();
            mEditor
                    .putString(
                            AppUtils.SHARED_LOGIN_OFFLINE, mPreferences.getString(AppUtils.SHARED_LOGIN, null))
                    .commit();
            mEditor.putString(AppUtils.SHARED_LOGIN, null).commit();
            new ReceiveComplaintItemDbInitializer(mActivity, null, null).execute(AppUtils.MODE_DELETE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            rootView = (View) inflater.inflate(R.layout.fragment_main, container, false);
            initUI(rootView);
            setProperties();
            validateUser();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    private void initUI(View rootView) {

        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            ll_main_tech = (LinearLayout) rootView.findViewById(R.id.ll_main_tech);
            ll_main_tech2 = (LinearLayout) rootView.findViewById(R.id.ll_main_tech2);
            btnReactive = (CardView) rootView.findViewById(R.id.btn_reactive_maintenance);
            btnPreventive = (CardView) rootView.findViewById(R.id.btn_preventive_maintenance);
            btnInspection = (CardView) rootView.findViewById(R.id.btn_inspection_module);
            btnSurvey = (CardView) rootView.findViewById(R.id.btn_survey_module);
            btn_attendance= (CardView) rootView.findViewById(R.id.btn_attendance);
            btn_location_module = (CardView) rootView.findViewById(R.id.btn_location_module);
            tv_user_name = (TextView) rootView.findViewById(R.id.tv_user_name);
            profile_action = (ImageView) rootView.findViewById(R.id.profile_action);
            ll_main_cust = (LinearLayout) rootView.findViewById(R.id.ll_main_cust);
            btnLogComplaint = (ImageView) rootView.findViewById(R.id.btn_log_complaint);
            btnReceiveComplaint = (ImageView) rootView.findViewById(R.id.btn_receive_complaint);
            btnViewComplaintStatus = (ImageView) rootView.findViewById(R.id.btn_view_complaint);
            fab_menu = (FloatingActionButton) rootView.findViewById(R.id.fab_menu);
            btnDashboard = (ImageView) rootView.findViewById(R.id.btn_dashboard);
            setupActionBar();

            new ExpectAnim()
                    .expect(ll_main_tech)
                    .toBe(
                            outOfScreen(Gravity.LEFT),
                            invisible()
                    )
                    .toAnimation()
                    .setNow();

            new ExpectAnim()

                    .expect(ll_main_tech2)
                    .toBe(
                            outOfScreen(Gravity.RIGHT),
                            invisible()
                    )
                    .toAnimation()
                    .setNow();

            this.expectAnimMove = new ExpectAnim()
                    .expect(ll_main_tech)
                    .toBe(
                            atItsOriginalPosition(),
                            visible()
                    ).expect(ll_main_tech2)
                    .toBe(
                            atItsOriginalPosition(),
                            visible()
                    )
                    .toAnimation()
                    .setDuration(800)
                    .start();

            FragmentManager fm = getActivity().getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                Log.i(TAG, "Found fragment: " + fm.getBackStackEntryAt(i).getId());
                if (fm.getBackStackEntryAt(i).getId() != 0) {
                    fm.popBackStack();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        linear_toolbar = (LinearLayout) mToolbar.findViewById(R.id.linear_profile);
        linear_toolbar.setVisibility(View.VISIBLE);
        tv_user_name = (TextView) mToolbar.findViewById(R.id.tv_user_name);
        profile_action = (ImageView) mToolbar.findViewById(R.id.profile_action);

        if (SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, mActivity) != null && (!SessionManager.getSessionForURL("ip_address", mActivity).trim().isEmpty()) && (SessionManager.getSessionForURL("ip_address", mActivity).contains("mbm"))) {
            tv_toolbar_title.setText(getString(R.string.app_name_mbm));

        } else {
            tv_toolbar_title.setText(getString(R.string.app_name));
        }
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setProperties() {

        try {
            btnReactive.setOnClickListener(this);
            btnPreventive.setOnClickListener(this);
            btnInspection.setOnClickListener(this);
            tv_user_name.setOnClickListener(this);
            profile_action.setOnClickListener(this);
            btnSurvey.setOnClickListener(this);
            btn_attendance.setOnClickListener(this);
            btn_location_module.setOnClickListener(this);
            linear_toolbar.setOnClickListener(this);

            tv_user_name.setText(user.getFirstName().replaceAll("(\\r|\\n)", "") + " " + user.getLastName());
            if (user.getUserType().equals(AppUtils.TECHNICIAN)) {
                ll_main_tech.setVisibility(View.VISIBLE);
                ll_main_cust.setVisibility(View.GONE);
                btnInspection.setEnabled(true);
            } else if (user.getUserType().equals(AppUtils.CUSTOMER)) {
                ll_main_tech.setVisibility(View.GONE);
                ll_main_cust.setVisibility(View.VISIBLE);
            }

            btnLogComplaint.setOnClickListener(this);
            btnReceiveComplaint.setOnClickListener(this);
            btnViewComplaintStatus.setOnClickListener(this);
            btnDashboard.setOnClickListener(this);
            if (user.getUserType().equals(AppUtils.CUSTOMER)) {
                btnReceiveComplaint.setVisibility(View.GONE);
                if (user.getContractNo().equals("*")) {
                    btnDashboard.setVisibility(View.VISIBLE);
                } else btnDashboard.setVisibility(View.GONE);
            } else btnReceiveComplaint.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).onDrawerOpen();
            }
        });
    }

    public void validateUser(){

        mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        String loginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
        Login login = gson.fromJson(loginData, Login.class);
        mStrEmpId = login.getEmployeeId();
        LoginRequest loginRequest = new LoginRequest(login.getEmployeeId(), login.getPassword());
        new UserService(mActivity, this).getLoginData(loginRequest);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reactive_maintenance:
                if (!(user.getUserType().equals(AppUtils.AUDITOR))) {
                    loadFragment(new FragmentRxSubmenu(), Utils.TAG_REACTIVE_MAINTENANCE);
                }
                break;
            case R.id.btn_preventive_maintenance:
                if (!(user.getUserType().equals(AppUtils.AUDITOR))) {
                    loadFragment(new PpmDashboard(), Utils.Fragment_PPMsubmenu);
                }

                break;
            case R.id.btn_inspection_module:
                 loadFragment(new Fragment_InspectionModule_Landing(), Utils.TAG_INSPECTIONMODULE);
                break;
            case R.id.tv_user_name:
                ((MainActivity) getActivity()).onDrawerOpen();
                break;
            case R.id.profile_action:
                ((MainActivity) getActivity()).onDrawerOpen();
                break;
            case R.id.btn_attendance:
                if (!(user.getUserType().equals(AppUtils.AUDITOR))) {
                    loadFragment(new AttendanceFragment(), Utils.TAG_HELP_SUPPORT);
                }

                break;

            case R.id.btn_survey_module:

                if (!(user.getUserType().equals(AppUtils.TECHNICIAN)) && !(user.getUserType().equals(AppUtils.CUSTOMER)) && !(user.getUserType().equals(AppUtils.AUDITOR))) {
                    showSurvey();
                }
                break;

            case R.id.btn_location_module:

                if (!(user.getUserType().equals(AppUtils.AUDITOR))) {
                    loadFragment(new JobLocationFinder(), Utils.TAG_F_JOBLOCATIONFINDER);
                }

                break;

            case R.id.btn_log_complaint:

                if (!(user.getUserType().equals(AppUtils.AUDITOR))) {
                    loadFragment(new Fragment_RM_LogComplaintUser(), TAG_LOG_COMPLAINT);
                }


                break;
            case R.id.btn_receive_complaint:
                Bundle data = new Bundle();
                data.putString(
                        AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, AppUtils.ARGS_RECEIVECOMPLAINT_PAGE);
                Fragment_RC_List complaintsList = new Fragment_RC_List();
                complaintsList.setArguments(data);
                loadFragment(complaintsList, Utils.TAG_RECEIVED_COMPALINTS);
                break;
            case R.id.btn_view_complaint:
                loadFragment(new Fragment_RM_SearchComplaint(), Utils.TAG_SINGLE_SEARCH_COMPLAINT);
                break;
            case R.id.btn_dashboard:
                loadFragment(new Fragment_RM_Dashboard(), Utils.TAG_DASHBOARD);
                break;
        }
        AppUtils.closeInput(cl_main);
    }

    public void loadFragment(final Fragment fragment, final String tag) {
        linear_toolbar.setVisibility(View.GONE);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public void loadFragment(final Fragment fragment, final String tag, Bundle b) {
        fragment.setArguments(b);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            List<Fragment> fragments = mManager.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                mActivity.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        PowerManager pm = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (!isScreenOn) {
            timeout();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PowerManager pm = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (isScreenOn) {
            cleartime();
        }
    }

    public void showSurvey() {
        int icon = R.drawable.ic_survey;
        if (SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, mActivity) != null && (!SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, mActivity).trim().isEmpty()) && (SessionManager.getSessionForURL(LoginActivity.IP_ADDRESS, mActivity).contains(LoginActivity.MBM))) {
            icon = R.drawable.ic_survey;
        }

        String colorval="";

        if(getString(R.string.app_name).equalsIgnoreCase("AGFS CAFM")){
            colorval="#D7164B";
        }
        else{
            colorval="#00ADEF";
        }

        new FancyAlertDialog.Builder(mActivity)
                .setTitle(getString(R.string.customer_survey))
                .setBackgroundColor(Color.parseColor("#DDDDDD"))  //Don't pass R.color.colorvalue
                .setMessage(getString(R.string.survey_msg))
                .setNegativeBtnText("Customer")
                .setPositiveBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Tenant")
                .setNegativeBtnBackground(Color.parseColor(colorval))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(icon, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Bundle mdata = new Bundle();
                        mdata.putSerializable(ARGS_SURVEYTYPE, "Tenant");
                        loadFragment(new SurveyHeader(), Utils.TAG_F_CUSTOMER_FEEDBACK_HEADER, mdata);
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Bundle mdata = new Bundle();
                        mdata.putSerializable(ARGS_SURVEYTYPE, "Customer");
                        loadFragment(new SurveyHeader(), Utils.TAG_F_CUSTOMER_FEEDBACK_HEADER, mdata);
                    }
                })
                .build();
    }

    public void onLoginDataReceivedSuccess(Login login, String totalNumberOfRows){
        Gson gson = new GsonBuilder().create();
        String loginJson = gson.toJson(login);
        mEditor.putString(AppUtils.SHARED_LOGIN, loginJson);
        mEditor.commit();
        // start tracking service
        if(login.getTrackingFlag()!=null && login.getTrackingFlag().equalsIgnoreCase("Y")){
            // setting track flag for double check the gps cache vals upload to server --
            startTracking();
        }
        else{
            stopTracking();
        }
    }

    public void onSuccessGpsUpdate(String strMsg, int mode){}

    public void onFailureGpsUpdate(String strErr, int mode){}

    @Override
    public void onReceiveEmployeeList(List<EmployeeList> object) {

    }

    @Override
    public void onReceiveFailureEmployeeList(String toString) {

    }

    public void onUserDataReceivedSuccess(CommonResponse response){}

    public void onUserDataReceivedFailure(String strErr){}

    public void stopTracking(){
        mServiceIntent = new Intent(mActivity, EmployeeTrackingService.class);
        if (isMyServiceRunning( EmployeeTrackingService.class)) {
            mActivity.stopService(mServiceIntent);
        } }

    private void startTracking() {
        EmployeeTrackingService employeeTrackingService = new EmployeeTrackingService(mActivity);
        mServiceIntent = new Intent(mActivity, employeeTrackingService.getClass());
        if (!isMyServiceRunning(employeeTrackingService.getClass())) {
            mActivity.startService(mServiceIntent);
        }
        {
            new EmployeeGpsRepository(mActivity, MainDashboard.this).updateEmployeeGps(null,MODE_REMOTE);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    Log.i ("isMyServiceRunning?", true+"");
                    return true;
                }
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }
}
