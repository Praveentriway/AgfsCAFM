package com.daemon.emco_android.ui.fragments.reactive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
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
import com.daemon.emco_android.ui.activities.LoginActivity;
import com.daemon.emco_android.repository.db.dbhelper.ReceiveComplaintItemDbInitializer;
import com.daemon.emco_android.ui.fragments.reactive.dashboard.Fragment_RM_Dashboard;
import com.daemon.emco_android.ui.fragments.common.document.FragmentDocumentFilter;
import com.daemon.emco_android.ui.fragments.reactive.logcomplaints.Fragment_RM_LogComplaintUser;
import com.daemon.emco_android.ui.fragments.reactive.receieve_complaints.Fragment_RC_List;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import static com.daemon.emco_android.utils.Utils.TAG_LOG_COMPLAINT;

/**
 * Created by subbu on 25/11/16.
 */

public class FragmentRxSubmenu extends Fragment implements View.OnClickListener {
    private static final String TAG = FragmentRxSubmenu.class.getSimpleName();

    private AppCompatActivity mActivity;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private Handler mHandler;

    //private Button  btnPendingSignature ;
    private ImageView btnLogComplaint,btnReceiveComplaint,btnViewComplaintStatus,btnDashboard,btn_receive_documents;
    private String mStringJson = null;
    private Login user;
    private CoordinatorLayout cl_main;
    private Toolbar mToolbar;
    private TextView tv_toolbar_title;
    private View rootView = null;

    private TimerTask mTimerTask;
    final Handler handler = new Handler();
    private Timer t = new Timer();

    public void timeout(){
        mTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        MaterialDialog.Builder builder =
                                new MaterialDialog.Builder(mActivity).content("Your session has been expired!Please login to continue!!! ")
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
            }};
        // public void schedule (TimerTask task, long delay, long period)
        t.scheduleAtFixedRate(mTimerTask,AppUtils.TIMEOUT_NEW,AppUtils.TIMEOUT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            mHandler = new Handler();
            mStringJson = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            mManager = mActivity.getSupportFragmentManager();
            font = App.getInstance().getFontInstance();
            //mArgs = getArguments().getString(ARGS_BUNDLE_MESSAGE);

            if (mStringJson != null) {
                Gson gson = new Gson();
                user = gson.fromJson(mStringJson, Login.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        try {
            rootView = (View) inflater.inflate(R.layout.fragment_reactive_landing_page, container, false);
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
            btnLogComplaint = (ImageView) rootView.findViewById(R.id.btn_log_complaint);
            btnReceiveComplaint = (ImageView) rootView.findViewById(R.id.btn_receive_complaint);
            btn_receive_documents = (ImageView) rootView.findViewById(R.id.btn_receive_documents);
            btnViewComplaintStatus = (ImageView) rootView.findViewById(R.id.btn_view_complaint);
          //  btnPendingSignature = (Button) rootView.findViewById(R.id.btn_pending_client_signature);

            btnDashboard = (ImageView) rootView.findViewById(R.id.btn_dashboard);
            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        //iv_backpress=(ImageView)mToolbar.findViewById(R.id.iv_backpress);
        tv_toolbar_title=(TextView)mToolbar.findViewById(R.id.tv_toolbar_title);
        LinearLayout  linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);
        tv_toolbar_title.setText(getString(R.string.title_reactive_maintenance));
       // mToolbar.setTitle(getResources().getString(R.string.title_reactive_maintenance));
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mActivity.onBackPressed();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void setProperties() {
        Log.d(TAG, "setProperties");
       // btnLogComplaint.setTypeface(font.getHelveticaRegular());
       // btnReceiveComplaint.setTypeface(font.getHelveticaRegular());
       // btnViewComplaintStatus.setTypeface(font.getHelveticaRegular());
       // btnPendingSignature.setTypeface(font.getHelveticaRegular());
       // btnDashboard.setTypeface(font.getHelveticaRegular());

        btnLogComplaint.setOnClickListener(this);
        btnReceiveComplaint.setOnClickListener(this);
        btn_receive_documents.setOnClickListener(this);
        btnViewComplaintStatus.setOnClickListener(this);
       // btnPendingSignature.setOnClickListener(this);
        btnDashboard.setOnClickListener(this);

        if (user.getUserType().equals(AppUtils.CUSTOMER)) {
            btnReceiveComplaint.setVisibility(View.GONE);
            if (user.getContractNo().equals("*")){
                btnDashboard.setVisibility(View.VISIBLE);
            }else btnDashboard.setVisibility(View.GONE);
            //btnReceiveComplaint.setBackground(getResources().getDrawable(R.drawable.bg_btn_light));
        } else btnReceiveComplaint.setVisibility(View.VISIBLE);

        AppUtils.closeInput(cl_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_log_complaint:
                    loadFragment(new Fragment_RM_LogComplaintUser(), TAG_LOG_COMPLAINT);
                break;
            case R.id.btn_receive_complaint:
                Bundle data = new Bundle();
                data.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, AppUtils.ARGS_RECEIVECOMPLAINT_PAGE);
                Fragment_RC_List complaintsList = new Fragment_RC_List();
                complaintsList.setArguments(data);
                loadFragment(complaintsList, Utils.TAG_RECEIVED_COMPALINTS);
                break;
            case R.id.btn_view_complaint:
                loadFragment(new Fragment_RM_SearchComplaint(), Utils.TAG_SINGLE_SEARCH_COMPLAINT);
                break;
            /*case R.id.btn_pending_client_signature:
                Bundle mdata = new Bundle();
                mdata.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, AppUtils.ARGS_RECEIVECOMPLAINTUNSIGNED_PAGE);
                Fragment_RC_List unsignedcomplaintslist = new Fragment_RC_List();
                unsignedcomplaintslist.setArguments(mdata);
                loadFragment(unsignedcomplaintslist, Utils.TAG_RECEIVED_COMPALINTS);
                break;*/
            case R.id.btn_dashboard:
                loadFragment(new Fragment_RM_Dashboard(), Utils.TAG_DASHBOARD);
                break;
            case R.id.btn_receive_documents:

                Fragment filter=new FragmentDocumentFilter();
                Bundle mdata = new Bundle();
                mdata.putString(AppUtils.ARGS_FILTERTYPE, "R");
                filter.setArguments(mdata);

                loadFragment(filter, Utils.TAG_DOCUMENT_FILTER);
                break;
        }
    }

    public void loadFragment(final Fragment fragment, final String tag) {
        Log.d(TAG, "loadFragment");

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(R.id.frame_container, fragment, tag);
                fragmentTransaction.addToBackStack(tag);
                fragmentTransaction.commit();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
    }

    @Override
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
            //    mActivity.onBackPressed();
                getActivity().getSupportFragmentManager().popBackStack();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        PowerManager pm = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if(isScreenOn){
            cleartime();
        }
    }

    public void cleartime(){
        if(mTimerTask!=null) {
            mTimerTask.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        PowerManager pm = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if(!isScreenOn){
            timeout();
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






}
