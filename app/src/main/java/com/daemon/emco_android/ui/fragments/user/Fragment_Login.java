package com.daemon.emco_android.ui.fragments.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.activities.MainActivity;
import com.daemon.emco_android.repository.remote.UrlService;
import com.daemon.emco_android.repository.remote.UserService;
import com.daemon.emco_android.repository.db.entity.UserToken;
import com.daemon.emco_android.listeners.URLListener;
import com.daemon.emco_android.listeners.UserListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.LoginRequest;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ConnectivityStatus;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.SessionManager;
import com.daemon.emco_android.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Field;

import static com.daemon.emco_android.utils.AppUtils.serverurls;
import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;

/**
 * Created by vikram on 14/7/17.
 */

public class Fragment_Login extends Fragment implements View.OnClickListener, UserListener,URLListener {
    private static final String TAG = Fragment_Login.class.getSimpleName();
    private AppCompatActivity mActivity;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Font font = App.getInstance().getFontInstance();
    private CoordinatorLayout cl_main;
    private Toolbar mToolbar;
   // private TextInputLayout til_uname, til_password, til_serverurl;
    private EditText tie_username, tie_password, tie_serverurl;
    private TextView tv_forgot_password, tv_reg, tv_toolbar_title, tv_user_name,txt_username,txt_password,txt_server_url;
    private Button btnLogin;
    private Fragment mFragment = null;
    private View rootView;
    private String result="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(false);

            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            if (mActivity.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initView();
        setUpActionBar();
        setProperties();
        AppUtils.closeInput(cl_main);

        return rootView;
    }

    public void initView() {
        Log.d(TAG, "initView");
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
        //    til_uname = (TextInputLayout) rootView.findViewById(R.id.til_username);
         //   til_password = (TextInputLayout) rootView.findViewById(R.id.til_password);
            tie_username = (EditText) rootView.findViewById(R.id.tie_username);
            tie_password = (EditText) rootView.findViewById(R.id.tie_password);
            tie_password.setTransformationMethod(new PasswordTransformationMethod());
            tv_forgot_password = (TextView) rootView.findViewById(R.id.tv_forgot_password);
            btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
            tv_reg = (TextView) rootView.findViewById(R.id.tv_reg);
        //  til_serverurl = (TextInputLayout) rootView.findViewById(R.id.til_serverurl);
            tie_serverurl = (EditText) rootView.findViewById(R.id.tie_serverurl);
            txt_username= (TextView) rootView.findViewById(R.id.txt_username);
            txt_password= (TextView) rootView.findViewById(R.id.txt_password);
            txt_server_url= (TextView) rootView.findViewById(R.id.txt_server_url);
            txt_username.setTypeface(font.getHelveticaRegular());
            txt_password.setTypeface(font.getHelveticaRegular());
            txt_server_url.setTypeface(font.getHelveticaRegular());
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

            tv_reg.setOnClickListener(this);
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

    public void setUpActionBar() {
        Log.d(TAG, "setActionBar");
        try {
            mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
            tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
            LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
            linear_toolbar.setVisibility(View.GONE);
            mActivity.setSupportActionBar(mToolbar);

            if(SessionManager.getSessionForURL("ip_address",mActivity) !=null && (!SessionManager.getSessionForURL("ip_address",mActivity).trim().isEmpty())  && (SessionManager.getSessionForURL("ip_address",mActivity).contains("mbm"))){
                tv_toolbar_title.setText("Welcome to MBM CAFM");
            }
            else{
                tv_toolbar_title.setText("Welcome to "+getString(R.string.app_name));
            }
            // mActivity.getSupportActionBar().setTitle(getString(R.string.lbl_login));
            mActivity.getSupportActionBar().setHomeAsUpIndicator(null);
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            TextView titleTextView = null;
            try {
                Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
                f.setAccessible(true);
                titleTextView = (TextView) f.get(mToolbar);
                titleTextView.setTypeface(font.getHelveticaRegular());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                // submitFormData();
                validateUrlfromapi();
                break;
            case R.id.tv_forgot_password:
                fragmentTransition(new Fragment_ForgotPassword(), Utils.TAG_FORGOT_PASSWORD);
                break;
            case R.id.tv_reg:
                fragmentTransition(new Fragment_Reg(), Utils.TAG_USER_REG);
                break;
            default:
                break;
        }
    }

    private void setProperties() {

        tie_username.setTypeface(font.getHelveticaRegular());
        tie_password.setTypeface(font.getHelveticaRegular());
        btnLogin.setTypeface(font.getHelveticaRegular());
        tie_username.addTextChangedListener(new MyTextWatcher(tie_username));
        tie_password.addTextChangedListener(new MyTextWatcher(tie_password));
        btnLogin.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
        if(SessionManager.getSession("ip_address",getContext()) !=null || (!SessionManager.getSession("ip_address",getContext()).trim().isEmpty())){
          tie_serverurl.setText(SessionManager.getSessionForURL("ip_address",getContext()));
        }

        tie_serverurl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //submitFormData();
                    validateUrlfromapi();
                    return true;
                }
                return false;
            }
        });
    }

    public void validateUrlfromapi() {
        AppUtils.closeInput(cl_main);
        validateUsername();
        validatePassword();
        validUrl();
        if (!validateUsername()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        if (!validUrl()) {
            return;
        }
        if (!TextUtils.isEmpty(tie_serverurl.getText().toString())){
            if (ConnectivityStatus.isConnected(getContext())) {
                showProgressDialog(mActivity, "Loading...", false);
                new UrlService(mActivity, this, tie_serverurl.getText().toString().trim()).getURLData();
            }else{
                Toast.makeText(mActivity,R.string.lbl_alert_network_not_available,Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void submitFormData() {
        try {

            String loginData = mPreferences.getString(AppUtils.SHARED_LOGIN_OFFLINE, null);
            if (mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE).contains(AppUtils.NETWORK_AVAILABLE)) {
                showProgressDialog(mActivity, "Loading...", false);
                LoginRequest loginRequest = new LoginRequest(tie_username.getText().toString().trim(), tie_password.getText().toString().trim());
                new UserService(mActivity, this).getLoginData(loginRequest);
            }
            else if (loginData != null && loginData.length() > 0) {
                Gson gson = new Gson();
                Login login = gson.fromJson(loginData, Login.class);
                Log.d(TAG, "getLoginData " + login.getUserName());
                if (tie_username.getText().toString().equals(login.getUserName()) &&
                        tie_password.getText().toString().equals(login.getPassword())) {

                    mEditor = mPreferences.edit();
                    mEditor.putString(AppUtils.SHARED_LOGIN, mPreferences.getString(AppUtils.SHARED_LOGIN_OFFLINE, null));
                    mEditor.commit();
                    Intent mainActivity = new Intent(mActivity, MainActivity.class);
                    startActivity(mainActivity);
                    mActivity.finish();
                } else {
                    AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, getString(R.string.lbl_alert_incorrect_username_pwd));
                }
                return;
            }
            else {
                AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, getString(R.string.lbl_alert_network_not_available));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    protected void fragmentTransition(Fragment _fragment, String name) {
        this.mFragment = _fragment;
        FragmentTransaction _fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
        _fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        _fragmentTransaction.replace(R.id.frame_container, mFragment, name);
        _fragmentTransaction.addToBackStack(name);
        _fragmentTransaction.commit();
    }

    private boolean validateUsername() {
        /*if (!AppUtils.validateEmail(tie_username.getText().toString().trim()))
        {
            til_uname.setError(getString(R.string.msg_email_violation));
            requestFocus(tie_username);
            return false;
        }
        else */
        if (tie_username.getText().toString().trim().isEmpty()) {

            Toast.makeText(getContext(),getString(R.string.msg_username_empty),Toast.LENGTH_SHORT).show();

          //  til_uname.setError(getString(R.string.msg_username_empty));
            return false;
        } else {
          //  Toast.makeText(getContext(),getString(R.string.msg_username_empty),Toast.LENGTH_SHORT).show();
           // til_uname.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (tie_password.getText().toString().trim().isEmpty()) {
           // til_password.setError(getString(R.string.msg_password_empty));
            Toast.makeText(getContext(),getString(R.string.msg_password_empty),Toast.LENGTH_SHORT).show();
            requestFocus(tie_password);
            return false;
        } else {
            //til_password.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validUrl() {

        if (tie_serverurl.getText().toString().trim().isEmpty()) {

            Toast.makeText(getContext(),getString(R.string.msg_server_url),Toast.LENGTH_SHORT).show();
         //   til_serverurl.setError(getString(R.string.msg_server_url));
            requestFocus(tie_serverurl);
            return false;
        } else {
          //  til_serverurl.setErrorEnabled(false);
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
        Log.d(TAG, "onLoginDataReceivedSuccess");
        AppUtils.hideProgressDialog();
        Gson gson = new GsonBuilder().create();
        String loginJson = gson.toJson(login);
        mEditor.putString(AppUtils.SHARED_LOGIN, loginJson);
        mEditor.commit();
        Log.d(TAG, "ALLFORWARDEDCOMPLAINT" + totalNumberOfRows);

        updateToken(login.getEmployeeId());

        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.putExtra(AppUtils.ALLFORWARDEDCOMPLAINT, totalNumberOfRows);
        startActivity(intent);
        mActivity.finish();


    }
    String token;
    public void updateToken(final String userid){



        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        token = task.getResult().getToken();

                        updateT(userid);
                    }
                });


    }

    public  void updateT( String userid){
        UserToken user=new UserToken();
        user.setUserId(userid);
        user.setToken(token);
        new UserService(mActivity, this).addToken(user);
    }


    @Override
    public void onUserDataReceivedSuccess(CommonResponse response) {

    }

    @Override
    public void onUserDataReceivedFailure(String strErr) {
        try {
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
            submitFormData();
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
                    //validateUsername();
                    break;
                case R.id.tie_password:
                    //validatePassword();
                    break;
            }
        }
    }
}
