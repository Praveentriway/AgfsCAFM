package com.daemon.emco_android.ui.fragments.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.google.gson.Gson;

/**
 * Created by subbu on 25/11/16.
 */

public class UserProfile extends Fragment {

    private static final String TAG = UserProfile.class.getSimpleName();
    private AppCompatActivity mActivity;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private TextView tv_lbl_firtname, tv_lbl_lastname, tv_lbl_emailid, tv_lbl_phoneno, tv_lbl_mobileno, tv_lbl_usertype;
    private TextView tv_firstname, tv_lastname, tv_emailid, tv_phoneno, tv_mobileno, tv_usertype;
    private String mStringJson = null;
    private CoordinatorLayout cl_main;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            mStringJson = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
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
        Log.d(TAG, "onCreateView");
        View rootView = null;
        try {
            rootView = (View) inflater.inflate(R.layout.fragment_view_profile, container, false);
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
            tv_lbl_firtname = (TextView) rootView.findViewById(R.id.tv_lbl_firtname);
            tv_lbl_lastname = (TextView) rootView.findViewById(R.id.tv_lbl_lastname);
            tv_lbl_emailid = (TextView) rootView.findViewById(R.id.tv_lbl_emailid);
            tv_lbl_phoneno = (TextView) rootView.findViewById(R.id.tv_lbl_phoneno);
            tv_lbl_mobileno = (TextView) rootView.findViewById(R.id.tv_lbl_mobileno);
            tv_lbl_usertype = (TextView) rootView.findViewById(R.id.tv_lbl_usertype);
            tv_firstname = (TextView) rootView.findViewById(R.id.tv_firstname);
            tv_lastname = (TextView) rootView.findViewById(R.id.tv_lastname);
            tv_emailid = (TextView) rootView.findViewById(R.id.tv_emailid);
            tv_phoneno = (TextView) rootView.findViewById(R.id.tv_phoneno);
            tv_mobileno = (TextView) rootView.findViewById(R.id.tv_mobileno);
            tv_usertype = (TextView) rootView.findViewById(R.id.tv_usertype);
            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {

        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        TextView tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("");
        LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);
      //  mToolbar.setTitle(getResources().getString(R.string.lbl_view_profile));
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

        if (mStringJson != null) {
            Gson gson = new Gson();
            Login user = gson.fromJson(mStringJson, Login.class);
            tv_firstname.setText(user.getFirstName().replaceAll("(\\r|\\n)", ""));
            tv_lastname.setText(user.getLastName().replaceAll("(\\r|\\n)", ""));
            tv_emailid.setText(user.getEmailId());
            tv_phoneno.setText(user.getPhoneNumber());
            tv_mobileno.setText(user.getMobileNumber());
            tv_usertype.setText(user.getUserType());
        }
    }

    public static String toCamelCase(final String init) {
        if (init == null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length() == init.length()))
                ret.append(" ");
        }
        return ret.toString();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(TAG,"onPrepareOptionsMenu ");
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_home:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                Fragment _fragment = new MainLandingUI();
                FragmentTransaction _transaction = getActivity().getSupportFragmentManager().beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
