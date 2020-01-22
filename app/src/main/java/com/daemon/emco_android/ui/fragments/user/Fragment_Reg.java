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
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.daemon.emco_android.model.request.AddNewUser;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.SessionManager;
import com.daemon.emco_android.utils.Utils;

import java.lang.reflect.Field;

import static com.daemon.emco_android.utils.AppUtils.serverurls;
import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;

/**
 * Created by vikram on 14/7/17.
 */

public class Fragment_Reg extends Fragment implements View.OnClickListener, UserListener,URLListener {
    private static final String TAG = Fragment_Reg.class.getSimpleName();
    private AppCompatActivity mActivity;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Font font = App.getInstance().getFontInstance();

    private CoordinatorLayout cl_main;
    private Toolbar mToolbar;
    private TextInputLayout til_fname, til_lname, til_telno, til_mobile_no, til_email,
            til_property_des, til_building_name, til_location_details,til_serverurl;
    private TextInputEditText tie_fname, tie_lname, tie_telno, tie_mobile_no, tie_email,tie_serverurl,
            tie_property_des, tie_building_name, tie_location_details;
    private TextView tv_lbl_fname, tv_lbl_lname, tv_lbl_telno, tv_lbl_mobile_no, tv_lbl_email,
            tv_lbl_property_des, tv_lbl_building_name, tv_lbl_location_details,tv_toolbar_title,tv_serverurl;
    private TextView tv_forgot_password, tv_login;
    private Button btn_Login;
    private Fragment mFragment = null;
    private View rootView;
    private Bundle mSavedInstanceState;
    private Bundle mArgs;
    private FragmentManager mManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(false);

            mSavedInstanceState = savedInstanceState;
            mManager = mActivity.getSupportFragmentManager();
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            mArgs = getArguments();
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

        rootView = inflater.inflate(R.layout.fragment_reg, container, false);
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

            til_fname = (TextInputLayout) rootView.findViewById(R.id.til_name);
            til_lname = (TextInputLayout) rootView.findViewById(R.id.til_lname);
            til_telno = (TextInputLayout) rootView.findViewById(R.id.til_telno);
            til_mobile_no = (TextInputLayout) rootView.findViewById(R.id.til_mobile_no);
            til_email = (TextInputLayout) rootView.findViewById(R.id.til_email);
            til_property_des = (TextInputLayout) rootView.findViewById(R.id.til_property_details);
            til_building_name = (TextInputLayout) rootView.findViewById(R.id.til_building_name);
            til_location_details = (TextInputLayout) rootView.findViewById(R.id.til_location_details);
            til_serverurl = (TextInputLayout) rootView.findViewById(R.id.til_serverurl);
            tie_serverurl = (TextInputEditText) rootView.findViewById(R.id.tie_serverurl);
            tie_serverurl.setTypeface(font.getHelveticaRegular());
            tie_fname = (TextInputEditText) rootView.findViewById(R.id.tie_name);
            tie_lname = (TextInputEditText) rootView.findViewById(R.id.tie_lname);
            tie_telno = (TextInputEditText) rootView.findViewById(R.id.tie_telno);
            tie_mobile_no = (TextInputEditText) rootView.findViewById(R.id.tie_mobile_no);
            tie_email = (TextInputEditText) rootView.findViewById(R.id.tie_email);
            tie_property_des = (TextInputEditText) rootView.findViewById(R.id.tie_property_details);
            tie_building_name = (TextInputEditText) rootView.findViewById(R.id.tie_building_name);
            tie_location_details = (TextInputEditText) rootView.findViewById(R.id.tie_location_details);

            tv_login = (TextView) rootView.findViewById(R.id.tv_login);
            tv_forgot_password = (TextView) rootView.findViewById(R.id.tv_forgot_password);

            tv_lbl_fname = (TextView) rootView.findViewById(R.id.tv_lbl_name);
            tv_lbl_lname = (TextView) rootView.findViewById(R.id.tv_lbl_lname);
            tv_lbl_telno = (TextView) rootView.findViewById(R.id.tv_lbl_telno);
            tv_lbl_mobile_no = (TextView) rootView.findViewById(R.id.tv_lbl_mobile_no);
            tv_lbl_email = (TextView) rootView.findViewById(R.id.tv_lbl_email);
            tv_lbl_property_des = (TextView) rootView.findViewById(R.id.tv_lbl_property_details);
            tv_lbl_building_name = (TextView) rootView.findViewById(R.id.tv_lbl_building_name);
            tv_lbl_location_details = (TextView) rootView.findViewById(R.id.tv_lbl_location_details);
            tv_serverurl=(TextView)rootView.findViewById(R.id.tv_serverurl);
            btn_Login = (Button) rootView.findViewById(R.id.btn_Login);
            tv_login.setOnClickListener(this);
            btn_Login.setOnClickListener(this);

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
            mActivity.setSupportActionBar(mToolbar);
            tv_toolbar_title=(TextView)mToolbar.findViewById(R.id.tv_toolbar_title);
            tv_toolbar_title.setText(getString(R.string.lbl_reg));
            LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
            linear_toolbar.setVisibility(View.GONE);

           // mActivity.getSupportActionBar().setTitle(getString(R.string.lbl_reg));
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
            case R.id.btn_Login:
                //submitFormData();
                validateUrlfromapi();
                break;
            case R.id.tv_forgot_password:
                fragmentTransition(new Fragment_ForgotPassword(), Utils.TAG_FORGOT_PASSWORD);
                break;
            case R.id.tv_login:
                fragmentTransition(new Fragment_Login(), Utils.TAG_USER_LOGIN);
                break;
            default:
                break;
        }
    }

    public void validateUrlfromapi() {
        AppUtils.closeInput(cl_main);
        validateFname();
        validateLname();
        validateEmail();
        validateTelno();
        validateMobile();
        validateProperty();
        validateBuilding();
        validateLocation();
        validUrl();
        if (!validateFname()) {
            return;
        }
        if (!validateLname()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validateMobile()) {
            return;
        }
        if (!validateProperty()) {
            return;
        }
        if (!validateBuilding()) {
            return;
        }
        if (!validateLocation()) {
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

        tv_lbl_fname.setText(Html.fromHtml(getString(R.string.lbl_fname) + AppUtils.mandatory));
        tv_lbl_lname.setText(Html.fromHtml(getString(R.string.lbl_lname) + AppUtils.mandatory));
        tv_lbl_telno.setText(Html.fromHtml(getString(R.string.lbl_telno) + AppUtils.mandatory));
        tv_lbl_mobile_no.setText(Html.fromHtml(getString(R.string.lbl_mobile_no) + AppUtils.mandatory));
        tv_lbl_email.setText(Html.fromHtml(getString(R.string.lbl_email_id) + AppUtils.mandatory));
        tv_lbl_property_des.setText(Html.fromHtml(getString(R.string.lbl_property_details) + AppUtils.mandatory));
        tv_lbl_building_name.setText(Html.fromHtml(getString(R.string.lbl_building_name) + AppUtils.mandatory));
        tv_serverurl.setText(Html.fromHtml(getString(R.string.lbl_server_url) + AppUtils.mandatory));
        tv_lbl_location_details.setText(Html.fromHtml(getString(R.string.lbl_location_floor_flat) + AppUtils.mandatory));

        tie_fname.setTypeface(font.getHelveticaRegular());
        tie_lname.setTypeface(font.getHelveticaRegular());
        tie_telno.setTypeface(font.getHelveticaRegular());
        tie_mobile_no.setTypeface(font.getHelveticaRegular());
        tie_email.setTypeface(font.getHelveticaRegular());
        tie_property_des.setTypeface(font.getHelveticaRegular());
        tie_building_name.setTypeface(font.getHelveticaRegular());
        tie_location_details.setTypeface(font.getHelveticaRegular());

        tv_lbl_fname.setTypeface(font.getHelveticaRegular());
        tv_lbl_lname.setTypeface(font.getHelveticaRegular());
        tv_lbl_telno.setTypeface(font.getHelveticaRegular());
        tv_lbl_mobile_no.setTypeface(font.getHelveticaRegular());
        tv_lbl_email.setTypeface(font.getHelveticaRegular());
        tv_lbl_property_des.setTypeface(font.getHelveticaRegular());
        tv_lbl_building_name.setTypeface(font.getHelveticaRegular());
        tv_lbl_location_details.setTypeface(font.getHelveticaRegular());
        tv_serverurl.setTypeface(font.getHelveticaRegular());


        btn_Login.setTypeface(font.getHelveticaRegular());

        btn_Login.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);

        tie_fname.addTextChangedListener(new Fragment_Reg.MyTextWatcher(tie_fname));
        tie_lname.addTextChangedListener(new Fragment_Reg.MyTextWatcher(tie_lname));
        tie_telno.addTextChangedListener(new Fragment_Reg.MyTextWatcher(tie_telno));
        tie_mobile_no.addTextChangedListener(new Fragment_Reg.MyTextWatcher(tie_mobile_no));
        tie_email.addTextChangedListener(new Fragment_Reg.MyTextWatcher(tie_email));
        tie_property_des.addTextChangedListener(new Fragment_Reg.MyTextWatcher(tie_property_des));
        tie_building_name.addTextChangedListener(new Fragment_Reg.MyTextWatcher(tie_building_name));
        tie_location_details.addTextChangedListener(new Fragment_Reg.MyTextWatcher(tie_location_details));


        if(SessionManager.getSession("ip_address",getContext()) !=null || (!SessionManager.getSession("ip_address",getContext()).trim().isEmpty())){

            tie_serverurl.setText(SessionManager.getSessionForURL("ip_address",getContext()));

        }

        tie_serverurl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                   // submitFormData();
                    validateUrlfromapi();
                    return true;
                }
                return false;
            }
        });
    }

    protected void submitFormData() {
        try {
          /*  AppUtils.closeInput(cl_main);
            validateFname();
            validateLname();
            validateEmail();
            validateTelno();
            validateMobile();
            validateProperty();
            validateBuilding();
            validateLocation();
            validUrl();*/
            if (validateFname() && validateLname() && validateEmail() && validateTelno()
                    && validateMobile() && validateProperty() && validateBuilding() && validateLocation()) {
                AddNewUser newUser = new AddNewUser();
                newUser.setFirstName(tie_fname.getText().toString());
                newUser.setLastName(tie_lname.getText().toString());
                newUser.setEmailId(tie_email.getText().toString());
                newUser.setTeleNo(tie_telno.getText().toString());
                newUser.setMobileNo(tie_mobile_no.getText().toString());
                newUser.setPropertyDetail(tie_property_des.getText().toString());
                newUser.setBuildingName(tie_building_name.getText().toString());
                newUser.setLocationDetail(tie_location_details.getText().toString());

                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                new UserService(mActivity, this).addUser(newUser);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void fragmentTransition(Fragment _fragment, String name) {
        this.mFragment = _fragment;
        FragmentTransaction _fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
        _fragmentTransaction.replace(R.id.frame_container, mFragment, name);
        _fragmentTransaction.addToBackStack(name);
        _fragmentTransaction.commit();
    }

    private boolean validateEmail() {
        if (!AppUtils.validateEmail(tie_email.getText().toString().trim())) {
            til_email.setError(getString(R.string.msg_email_valid));
            tie_email.requestFocus();
            return false;
        } else {
            til_email.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateFname() {
        if (tie_fname.getText().toString().trim().isEmpty()) {
            til_fname.setError(getString(R.string.msg_firstname_valid));
            return false;
        } else {
            til_fname.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLname() {
        if (tie_lname.getText().toString().trim().isEmpty()) {
            til_lname.setError(getString(R.string.msg_lastname_valid));
            return false;
        } else {
            til_lname.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateTelno() {
        if (tie_telno.getText().toString().trim().isEmpty()) {
            til_telno.setError(getString(R.string.msg_no_valid));
            return false;
        } else {
            til_telno.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateMobile() {
        if (tie_mobile_no.getText().toString().trim().isEmpty()) {
            til_mobile_no.setError(getString(R.string.msg_no_valid));
            return false;
        } else {
            til_mobile_no.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateProperty() {
        if (tie_property_des.getText().toString().trim().isEmpty()) {
            til_property_des.setError(getString(R.string.msg_property_valid));
            return false;
        } else {
            til_property_des.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateBuilding() {
        if (tie_building_name.getText().toString().trim().isEmpty()) {
            til_building_name.setError(getString(R.string.msg_building_valid));
            return false;
        } else {
            til_building_name.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLocation() {
        if (tie_location_details.getText().toString().trim().isEmpty()) {
            til_location_details.setError(getString(R.string.msg_location_valid));
            return false;
        } else {
            til_location_details.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validUrl() {

        if (tie_serverurl.getText().toString().trim().isEmpty()) {
            til_serverurl.setError(getString(R.string.msg_server_url));
            return false;
        } else {
            til_serverurl.setErrorEnabled(false);
        }

        return true;
    }


    @Override
    public void onLoginDataReceivedSuccess(Login login, String totalNumberOfRows) {
        Log.d(TAG, "onLoginDataReceivedSuccess");
    }

    @Override
    public void onUserDataReceivedSuccess(CommonResponse response) {
        try {
            Log.d(TAG, "onUserDataReceivedSuccess");
            AppUtils.hideProgressDialog();
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity)
                    .content(response.getMessage())
                    .positiveText(R.string.lbl_okay)
                    .stackingBehavior(StackingBehavior.ADAPTIVE)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            fragmentTransition(new Fragment_Login(), Utils.TAG_USER_LOGIN);
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
            Log.d(TAG, "onUserDataReceivedFailure");
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
                case R.id.tie_name:
                    validateFname();
                    break;
                case R.id.tie_lname:
                    validateLname();
                    break;
                case R.id.tie_email:
                    validateEmail();
                    break;
                case R.id.tie_telno:
                    validateTelno();
                    break;
                case R.id.tie_mobile_no:
                    validateMobile();
                    break;
                case R.id.tie_property_details:
                    validateProperty();
                    break;
                case R.id.tie_building_name:
                    validateBuilding();
                    break;
                case R.id.tie_location_details:
                    validateLocation();
                    break;
                default:
                    break;
            }
        }
    }
}
