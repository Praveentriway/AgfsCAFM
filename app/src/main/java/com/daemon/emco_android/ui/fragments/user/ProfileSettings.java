package com.daemon.emco_android.ui.fragments.user;

import android.content.Context;
import android.content.Intent;
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
import com.daemon.emco_android.listeners.UserListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.repository.db.entity.UserToken;
import com.daemon.emco_android.repository.remote.UserService;
import com.daemon.emco_android.ui.activities.LoginActivity;
import com.daemon.emco_android.repository.db.dbhelper.ReceiveComplaintItemDbInitializer;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.SessionManager;
import com.daemon.emco_android.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 */
public class ProfileSettings extends Fragment implements View.OnClickListener, UserListener {

    private CoordinatorLayout cl_main;
    private AppCompatActivity mActivity;
    private Toolbar mToolbar;
    private ImageView iv_myprofile,iv_changepass,iv_logout;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private TextView tv_toolbar_title;
    private String mLoginData = null;
    private String mStrEmpId = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mLoginData == null) return;
            Gson gson = new Gson();
            Login login = gson.fromJson(mLoginData, Login.class);
            mStrEmpId = login.getEmployeeId();

            //mStringJson = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            mManager = mActivity.getSupportFragmentManager();
            font = App.getInstance().getFontInstance();
            //mArgs = getArguments().getString(ARGS_BUNDLE_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = (View) inflater.inflate(R.layout.fragment_fragment__view_profile__new, container, false);
            initUI(rootView);
           // setProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rootView;
    }



    private void initUI(View rootView) {
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            iv_myprofile = (ImageView) rootView.findViewById(R.id.iv_myprofile);
            iv_changepass = (ImageView) rootView.findViewById(R.id.iv_changepass);
            iv_logout = (ImageView) rootView.findViewById(R.id.iv_logout);
            iv_myprofile.setOnClickListener(this);
            iv_changepass.setOnClickListener(this);
            iv_logout.setOnClickListener(this);

            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title=(TextView)mToolbar.findViewById(R.id.tv_toolbar_title);
        LinearLayout  linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);
        tv_toolbar_title.setText(getString(R.string.lbl_setting));
        //mToolbar.setTitle(getResources().getString(R.string.lbl_view_profile));
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_home:
                mActivity.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadFragment(final Fragment fragment, final String tag) {
        // update the main content by replacing fragments
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_myprofile:
                loadFragment(new UserProfile(), Utils.TAG_VIEW_PROFILE);
                break;
            case R.id.iv_changepass:
                loadFragment(new ChangePassword(), Utils.TAG_CHANGE_PASS);
                break;
            case R.id.iv_logout:
                try {
                    MaterialDialog.Builder builder =
                            new MaterialDialog.Builder(mActivity)
                                    .content("Are you sure you want to logout?")
                                    .title("Logout")
                                    .positiveText(R.string.lbl_yes)
                                    .negativeText(R.string.lbl_cancel)
                                    .stackingBehavior(StackingBehavior.ADAPTIVE)
                                    .onPositive(
                                            new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(
                                                        @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    dialog.dismiss();
                                                    clearPreferences();
                                                    SessionManager.clearSession(mActivity);
                                                    clearToken();
                                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                    startActivity(intent);
                                                    getActivity().finish();

                                                }
                                            });

                    MaterialDialog dialog = builder.build();
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
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

    public void clearPreferences() {
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


    public void onLoginDataReceivedSuccess(Login login, String totalNumberOfRows){}

    public void onUserDataReceivedSuccess(CommonResponse response){}

    public void onUserDataReceivedFailure(String strErr){}


}
