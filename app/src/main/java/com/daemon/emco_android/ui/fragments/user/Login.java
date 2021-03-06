package com.daemon.emco_android.ui.fragments.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.text.InputType;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.base.BaseFragment;
import com.daemon.emco_android.ui.activities.MainActivity;
import com.daemon.emco_android.repository.remote.UrlService;
import com.daemon.emco_android.repository.remote.UserService;
import com.daemon.emco_android.repository.db.entity.UserToken;
import com.daemon.emco_android.listeners.URLListener;
import com.daemon.emco_android.listeners.UserListener;
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

import es.dmoral.toasty.Toasty;

import static com.daemon.emco_android.ui.activities.LoginActivity.IP_ADDRESS;
import static com.daemon.emco_android.utils.AppUtils.checkInternet;
import static com.daemon.emco_android.utils.AppUtils.serverurls;
import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;

/**
 * Created by vikram on 14/7/17.
 */

public class Login extends BaseFragment implements View.OnClickListener, UserListener,URLListener {
    private static final String TAG = Login.class.getSimpleName();
    private AppCompatActivity mActivity;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Font font = App.getInstance().getFontInstance();
    private CoordinatorLayout cl_main;
    private EditText tie_username, tie_password, tie_serverurl;
    private TextView tv_forgot_password, tv_reg,txt_username,txt_password,txt_server_url;
    private Button btnLogin;
    private Fragment mFragment = null;
    private View rootView;
    private ImageView img_header;

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

        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initView();
        setUpActionBar();
        setProperties();
        AppUtils.closeInput(cl_main);

        return rootView;
    }

    public void initView() {

        try {

            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            tie_username = (EditText) rootView.findViewById(R.id.tie_username);
            tie_password = (EditText) rootView.findViewById(R.id.tie_password);
            tie_password.setTransformationMethod(new PasswordTransformationMethod());
            tv_forgot_password = (TextView) rootView.findViewById(R.id.tv_forgot_password);
            btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
            tv_reg = (TextView) rootView.findViewById(R.id.tv_reg);
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

         setupToolBar(mActivity,false);

//            if(SessionManager.getSessionForURL(IP_ADDRESS,mActivity) !=null && (!SessionManager.getSessionForURL("ip_address",mActivity).trim().isEmpty())  && (SessionManager.getSessionForURL("ip_address",mActivity).contains("mbm"))){
//                img_header.setImageResource(R.drawable.logo_mbm_png_no_bg);
//            }
//            else{
//                img_header.setImageResource(R.drawable.headerlogo);
//            }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                // submitFormData();
                validateUrlfromapi();
                break;
            case R.id.tv_forgot_password:
                fragmentTransition(new ForgotPassword(), Utils.TAG_FORGOT_PASSWORD);
                break;
            case R.id.tv_reg:
                fragmentTransition(new UserRegisteration(), Utils.TAG_USER_REG);
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
        if(SessionManager.getSession(IP_ADDRESS,getContext()) !=null || (!SessionManager.getSession(IP_ADDRESS,getContext()).trim().isEmpty())){
          tie_serverurl.setText(SessionManager.getSessionForURL(IP_ADDRESS,getContext()));
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
        if (!TextUtils.isEmpty(tie_serverurl.getText().toString())) {
            if (ConnectivityStatus.isConnected(getContext())) {
                showProgressDialog(mActivity, "Loading...", false);
                new UrlService(mActivity, this, tie_serverurl.getText().toString().trim()).getURLData();
            }else{
                Toasty.info(mActivity,R.string.lbl_alert_network_not_available,Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void submitFormData() {
        try {

            String loginData = mPreferences.getString(AppUtils.SHARED_LOGIN_OFFLINE, null);
            if (checkInternet(getContext())) {
                showProgressDialog(mActivity, "Loading...", false);
                LoginRequest loginRequest = new LoginRequest(tie_username.getText().toString().trim(), tie_password.getText().toString().trim());
                new UserService(mActivity, this).getLoginData(loginRequest);
            }
            else if (loginData != null && loginData.length() > 0) {
                Gson gson = new Gson();
                com.daemon.emco_android.model.common.Login login = gson.fromJson(loginData, com.daemon.emco_android.model.common.Login.class);
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
        if (tie_username.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(),getString(R.string.msg_username_empty),Toast.LENGTH_SHORT).show();
            return false;
        } else {

        }
        return true;
    }

    private boolean validatePassword() {
        if (tie_password.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(),getString(R.string.msg_password_empty),Toast.LENGTH_SHORT).show();
            requestFocus(tie_password);
            return false;
        } else {
        }
        return true;
    }

    private boolean validUrl() {
        if (tie_serverurl.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(),getString(R.string.msg_server_url),Toast.LENGTH_SHORT).show();
            requestFocus(tie_serverurl);
            return false;
        } else {

        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onLoginDataReceivedSuccess(com.daemon.emco_android.model.common.Login login, String totalNumberOfRows,String token) {
        Log.d(TAG, "onLoginDataReceivedSuccess");
        AppUtils.hideProgressDialog();
        Gson gson = new GsonBuilder().create();
        String loginJson = gson.toJson(login);
        mEditor.putString(AppUtils.SHARED_LOGIN, loginJson);
        mEditor.commit();
        Log.d(TAG, "ALLFORWARDEDCOMPLAINT" + totalNumberOfRows);
        updateToken(login.getEmployeeId());        // update fcm token for pushmsg
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
