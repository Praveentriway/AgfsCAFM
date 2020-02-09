package com.daemon.emco_android.ui.fragments.preventive.ppmschedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.activities.BarcodeCaptureActivity;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.ui.fragments.reactive.receieve_complaints.Fragment_RC_Respond;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

public class Fragment_PM_PPMDetails_ViewRequired extends Fragment {
    private static final String TAG = Fragment_PM_PPMDetails_ViewRequired.class.getSimpleName();
    private AppCompatActivity mActivity;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private FragmentManager mManager;
    private Bundle mArgs;
    private CoordinatorLayout cl_main;
    View.OnClickListener _OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick");
            AppUtils.closeInput(cl_main);
            switch (v.getId()) {
                case R.id.btn_next:
                    gotoNextPage();
                    break;
                case R.id.btn_respond:
                    break;
                case R.id.tv_select_remarks:
                    break;

                case R.id.btn_barcode_scan:
                    int requestId = AppUtils.getIdForRequestedCamera(AppUtils.CAMERA_FACING_BACK);
                    if (requestId == -1) AppUtils.showDialog(mActivity, "Camera not available");
                    else {
                        Intent intent = new Intent(mActivity, BarcodeCaptureActivity.class);
                        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                    }
                    break;

                default:
                    break;
            }
        }
    };
    private Button btn_respond, btn_barcode_scan, btn_next;
    private Toolbar mToolbar;
    private View rootView;
    private String mStrEmpId = null;
    private String mLoginData = null;
    private TextView tv_toolbar_title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mArgs = getArguments();
            mManager = mActivity.getSupportFragmentManager();
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            font = App.getInstance().getFontInstance();
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        try {
            rootView = inflater.inflate(R.layout.fragment_ppmdetails_viewrequired, container, false);
            initUI(rootView);
            setProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void initUI(View rootView) {
        Log.d(TAG, "initUI");
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);

            btn_barcode_scan = (Button) rootView.findViewById(R.id.btn_barcode_scan);
            btn_respond = (Button) rootView.findViewById(R.id.btn_respond);
            btn_next = (Button) rootView.findViewById(R.id.btn_next);

            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title=(TextView)mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.lbl_preventivemaintenance));
        //mToolbar.setTitle(getResources().getString(R.string.lbl_preventivemaintenance));
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.onBackPressed();
            }
        });
    }

    private void setProperties() {
        Log.d(TAG, "setProperties");

        // only respond after move this page
        btn_next.setEnabled(false);
        btn_respond.setTypeface(font.getHelveticaRegular());
        btn_barcode_scan.setTypeface(font.getHelveticaRegular());
        btn_next.setTypeface(font.getHelveticaRegular());

        btn_respond.setOnClickListener(_OnClickListener);
        btn_next.setOnClickListener(_OnClickListener);
        btn_barcode_scan.setOnClickListener(_OnClickListener);
        AppUtils.closeInput(cl_main);
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
                //mActivity.onBackPressed();
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

    private void gotoNextPage() {
        Fragment_RC_Respond fragment = new Fragment_RC_Respond();
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, Utils.TAG_RECEIVE_COMPLAINT_RESPOND);
        fragmentTransaction.addToBackStack(Utils.TAG_RECEIVE_COMPLAINT_RESPOND);
        fragmentTransaction.commit();
    }
}