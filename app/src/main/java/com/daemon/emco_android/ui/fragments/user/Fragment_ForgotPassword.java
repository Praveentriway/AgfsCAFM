package com.daemon.emco_android.ui.fragments.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.UrlService;
import com.daemon.emco_android.repository.remote.UserService;
import com.daemon.emco_android.listeners.URLListener;
import com.daemon.emco_android.listeners.UserListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.SessionManager;

import static com.daemon.emco_android.utils.AppUtils.serverurls;
import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;

/**
 * Created by subbu on 25/11/16.
 */

public class Fragment_ForgotPassword extends Fragment implements UserListener,URLListener {
    private static final String TAG = Fragment_ForgotPassword.class.getSimpleName();

    private AppCompatActivity mActivity;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private TextView tv_toolbar_title;
    private CoordinatorLayout cl_main;
    private TextInputLayout til_username,til_serverurl;
    private TextInputEditText tie_username,tie_serverurl;
    View rootView = null;
    View.OnClickListener _OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick");
            switch (v.getId()) {
                case R.id.btn_send_email:
                    //submitForm();
                    validateUrlfromapi();
                    break;
                default:
                    break;
            }
        }
    };
    private Button btn_send_email;
    private Toolbar mToolbar;

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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showDialog(Context context, String title, String[] btnText,
                           DialogInterface.OnClickListener listener) {


        if (listener == null)
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    paramDialogInterface.dismiss();
                }
            };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        builder.setSingleChoiceItems(serverurls, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                    }
                });
        builder.setPositiveButton(btnText[0], listener);
        if (btnText.length != 1) {
            builder.setNegativeButton(btnText[1], listener);
        }
        builder.show();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");



        try {
            rootView = (View) inflater.inflate(R.layout.fragment_forgot_password, container, false);
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
            til_username = (TextInputLayout) rootView.findViewById(R.id.til_username);
            til_serverurl = (TextInputLayout) rootView.findViewById(R.id.til_serverurl);
            tie_serverurl = (TextInputEditText) rootView.findViewById(R.id.tie_serverurl);
            tie_username = (TextInputEditText) rootView.findViewById(R.id.tie_username);
            btn_send_email = (Button) rootView.findViewById(R.id.btn_send_email);
            tie_serverurl.setTypeface(font.getHelveticaRegular());
            tie_serverurl.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(event.getRawX() >= (tie_serverurl.getRight() - tie_serverurl.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // Creating and Building the Dialog

                            showDialog(getContext(), "Select your domain", new String[] { "Ok" },
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                                            if(selectedPosition!=-1){
                                                tie_serverurl.setText(serverurls[selectedPosition]);
                                            }

                                        }
                                    });


                            return true;
                        }
                    }
                    return false;
                }
            });


            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setTypeface(font.getHelveticaRegular());
        //tv_toolbar_title.setText(getString(R.string.lbl_forget_password));
        tv_toolbar_title=(TextView)mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.lbl_forget_password));
        //mToolbar.setTitle(getResources().getString(R.string.lbl_forget_password));
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

    public void validateUrlfromapi() {
        AppUtils.closeInput(cl_main);
        validateUsername();
        validUrl();
        if (!validateUsername()) {
            return;
        }
        if (!validUrl()) {
            return;
        }
        if (!TextUtils.isEmpty(tie_serverurl.getText().toString())){
            showProgressDialog(mActivity, "Loading...", false);
            new UrlService(mActivity, this, tie_serverurl.getText().toString().trim()).getURLData();
        }
    }

    private void setProperties() {
        Log.d(TAG, "setProperties");

        til_username.setTypeface(font.getHelveticaRegular());
        tie_username.setTypeface(font.getHelveticaRegular());
        btn_send_email.setTypeface(font.getHelveticaBold());

        btn_send_email.setOnClickListener(_OnClickListener);
        tie_username.addTextChangedListener(new MyTextWatcher(tie_username));

        if(SessionManager.getSession("ip_address",getContext()) !=null || (!SessionManager.getSession("ip_address",getContext()).trim().isEmpty())){

            tie_serverurl.setText(SessionManager.getSessionForURL("ip_address",getContext()));

        }



    }

    private boolean validUrl() {
        if (tie_serverurl.getText().toString().trim().isEmpty()) {
            til_serverurl.setError(getString(R.string.msg_server_url));
            requestFocus(tie_serverurl);
            return false;
        } else {
            til_serverurl.setErrorEnabled(false);
        }

        return true;
    }

    private void submitForm() {
        Log.d(TAG, "submitForm");
        try {
           /* AppUtils.closeInput(cl_main);

            if (!validateUsername()) {
                return;
            }*/

            if (mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE).contains(AppUtils.NETWORK_AVAILABLE)) {
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                new UserService(mActivity, this).getForgotPasswordResult(tie_username.getText().toString());
            } else
                AppUtils.showDialog(mActivity, getString(R.string.lbl_alert_network_not_available));
           // AppUtils.closeInput(cl_main);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private boolean validateUsername() {
        if (tie_username.getText().toString().trim().isEmpty()) {
            til_username.setError(getString(R.string.msg_username_empty));
            requestFocus(tie_username);
            return false;
        } else {
            til_username.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onLoginDataReceivedSuccess(Login login, String totalNumberOfRows) {

    }

    @Override
    public void onUserDataReceivedSuccess(CommonResponse response) {
        Log.d(TAG, "onUserDataReceivedSuccess");
        try {
            AppUtils.hideProgressDialog();
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity)
                    .content(response.getMessage())
                    .positiveText(R.string.lbl_okay)
                    .stackingBehavior(StackingBehavior.ADAPTIVE)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mManager.popBackStack();
                            dialog.dismiss();
                        }
                    });

            MaterialDialog dialog = builder.build();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserDataReceivedFailure(String strErr) {
        try {
            Log.d(TAG, "onForgotPasswordReceivedError");
            AppUtils.hideProgressDialog();
            AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, strErr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUrlreceivedsucess(CommonResponse response) {
        if (response.getStatus().equals("True")) {
            AppUtils.hideProgressDialog();
            submitForm();
        }
    }

    @Override
    public void onUrlreceivedFailure(String strErr) {
        try {
            AppUtils.hideProgressDialog();
            AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, strErr);
        } catch (Exception e) {
            e.printStackTrace();
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
                case R.id.tie_username:
                    validateUsername();
                    break;

            }
        }

    }
}
