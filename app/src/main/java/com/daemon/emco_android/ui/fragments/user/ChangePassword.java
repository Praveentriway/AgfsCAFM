package com.daemon.emco_android.ui.fragments.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.ChangePasswordRepository;
import com.daemon.emco_android.listeners.ChangePasswordListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.ChangePasswordRequest;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.google.gson.Gson;

import static com.daemon.emco_android.utils.AppUtils.checkInternet;
import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;

/**
 * Created by subbu on 25/11/16.
 */

public class ChangePassword extends Fragment implements ChangePasswordListener {

    private static final String TAG = ChangePassword.class.getSimpleName();
    private AppCompatActivity mActivity;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Gson gson = new Gson();
    private FragmentManager mManager;
    private String mStrEmployeeId = null;
    private Login mUserData;
    private View rootView = null;
    private MaterialDialog progressDialog;
    private CoordinatorLayout cl_main;
    private TextInputLayout til_useroldpass, til_usernewpass, til_userconfirmpass;
    private TextInputEditText tv_old_pass, tv_new_password, tv_confirm_newpass;
    View.OnClickListener _OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick");
            switch (v.getId()) {
                case R.id.btn_save:
                    submitForm();
                    break;
                default:
                    break;
            }
        }
    };
    private Button btn_save;
    private Toolbar mToolbar;
    private TextView tv_toolbar_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            mManager = mActivity.getSupportFragmentManager();
            font = App.getInstance().getFontInstance();
            //mArgs = getArguments().getString(ARGS_BUNDLE_MESSAGE);

            String mStrLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mStrLoginData != null) {
                mUserData = gson.fromJson(mStrLoginData, Login.class);
                mStrEmployeeId =
                        mUserData.getEmployeeId() == null ? mUserData.getUserName() : mUserData.getEmployeeId();

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
            rootView = (View) inflater.inflate(R.layout.fragment_change_password, container, false);
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
            til_useroldpass = (TextInputLayout) rootView.findViewById(R.id.til_useroldpass);
            til_usernewpass = (TextInputLayout) rootView.findViewById(R.id.til_usernewpass);
            til_userconfirmpass = (TextInputLayout) rootView.findViewById(R.id.til_userconfirmpass);
            tv_old_pass = (TextInputEditText) rootView.findViewById(R.id.tv_old_pass);
            tv_new_password = (TextInputEditText) rootView.findViewById(R.id.tv_new_password);
            tv_confirm_newpass = (TextInputEditText) rootView.findViewById(R.id.tv_confirm_newpass);
            btn_save = (Button) rootView.findViewById(R.id.btn_save);
            setupActionBar();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {

        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setTypeface(font.getHelveticaRegular());
        LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);
        tv_toolbar_title.setText("Change Password");
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
        tv_old_pass.setTypeface(font.getHelveticaRegular());
        tv_new_password.setTypeface(font.getHelveticaBold());
        tv_confirm_newpass.setTypeface(font.getHelveticaBold());
        btn_save.setOnClickListener(_OnClickListener);

        tv_confirm_newpass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitForm();
                    return true;
                }
                return false;
            }
        });
    }

    private void submitForm() {

        Log.d(TAG, "submitForm");
        try {
            AppUtils.closeInput(cl_main);
            validateOldPassword();
            validateNewPassword();
            validateConfirmNewPassword();
            if (!validateOldPassword()) {
                return;
            }
            if (!validateNewPassword()) {
                return;
            }
            if (!validateConfirmNewPassword()) {
                return;
            }

            if (!tv_new_password.getText().toString().trim().equalsIgnoreCase(tv_confirm_newpass.getText().toString().trim())) {
                MaterialDialog.Builder builder =
                        new MaterialDialog.Builder(mActivity)
                                .content(getString(R.string.password_msg))
                                .positiveText(R.string.lbl_okay)
                                .stackingBehavior(StackingBehavior.ADAPTIVE)
                                .onPositive(
                                        new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(
                                                    @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                                if (progressDialog == null) {
                                                } else {
                                                    progressDialog.hide();
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });

                MaterialDialog dialog = builder.build();
                dialog.show();
                return;
            } else {
                if (checkInternet(getContext())) {

                    showProgressDialog(mActivity, "Loading...", true);
                    ChangePasswordRequest loginRequest = new ChangePasswordRequest(mStrEmployeeId, tv_old_pass.getText().toString().trim(), tv_new_password.getText().toString().trim());
                    new ChangePasswordRepository(mActivity, this).getPasswordData(loginRequest);

                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void showProgressDialog(
            AppCompatActivity mActivity, String Str_Msg, boolean setCancelable) {
        try {
            progressDialog =
                    new MaterialDialog.Builder(mActivity)
                            .title(Str_Msg)
                            .progress(true, 0)
                            .cancelable(setCancelable)
                            .progressIndeterminateStyle(true)
                            .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean validateOldPassword() {

        if (tv_old_pass.getText().toString().trim().isEmpty()) {
            til_useroldpass.setError(getString(R.string.msg_oldpassword_empty));
            requestFocus(tv_old_pass);
            return false;
        } else {
            til_useroldpass.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNewPassword() {

        if (tv_new_password.getText().toString().trim().isEmpty()) {
            til_usernewpass.setError(getString(R.string.msg_newpassword_empty));
            requestFocus(tv_new_password);
            return false;
        } else {
            til_usernewpass.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateConfirmNewPassword() {
        if (tv_confirm_newpass.getText().toString().trim().isEmpty()) {
            til_userconfirmpass.setError(getString(R.string.msg_confinewpassword_empty));
            requestFocus(tv_confirm_newpass);
            return false;
        } else {
            til_userconfirmpass.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {

        if (view.requestFocus()) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onPasswordChangesSuccess(String login) {
        MaterialDialog.Builder builder =
                new MaterialDialog.Builder(mActivity)
                        .content(login)
                        .positiveText(R.string.lbl_okay)
                        .stackingBehavior(StackingBehavior.ADAPTIVE)
                        .onPositive(
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(
                                            @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        if (progressDialog == null) {
                                        } else {
                                            progressDialog.hide();
                                            progressDialog.dismiss();
                                        }
                                        mActivity.onBackPressed();

                                    }
                                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    @Override
    public void onPasswordFailure(String failure) {
        MaterialDialog.Builder builder =
                new MaterialDialog.Builder(mActivity)
                        .content(failure)
                        .positiveText(R.string.lbl_okay)
                        .stackingBehavior(StackingBehavior.ADAPTIVE)
                        .onPositive(
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(
                                            @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        if (progressDialog == null) {
                                        } else {
                                            progressDialog.hide();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }
}
