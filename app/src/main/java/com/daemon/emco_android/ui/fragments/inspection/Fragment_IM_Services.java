package com.daemon.emco_android.ui.fragments.inspection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

/**
 * Created by subbu on 25/11/16.
 */

public class Fragment_IM_Services extends Fragment implements View.OnClickListener {
    private static final String TAG = Fragment_IM_Services.class.getSimpleName();

    private AppCompatActivity mActivity;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private LinearLayout ll_im_landing, ll_im_services, ll_im_periodic;
    private Button btn_hard_services, btn_soft_services;
    private String mStringJson = null;
    private CoordinatorLayout cl_main;
    private Toolbar mToolbar;
    private Bundle mArgs;
    private String mTitle;
    private TextView tv_toolbar_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            mContext = mActivity;
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mStringJson = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            font = App.getInstance().getFontInstance();
            mArgs = getArguments();
            if (mArgs != null) {
                if (mArgs.containsKey(AppUtils.ARGS_IM_SERVICES_Page_TITLE)) {
                    mTitle = mArgs.getString(AppUtils.ARGS_IM_SERVICES_Page_TITLE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = null;
        try {
            rootView = (View) inflater.inflate(R.layout.fragment_inspection_landing_page, container, false);
            initUI(rootView);
            setProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rootView;
    }

    private void initUI(View rootView) {
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);


            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.title_inspection_module));
        mToolbar.setTitle(mTitle);
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.onBackPressed();
            }
        });
    }

    private void setProperties() {

        ll_im_landing.setVisibility(View.GONE);
        ll_im_periodic.setVisibility(View.GONE);
        ll_im_services.setVisibility(View.VISIBLE);

        if (mStringJson != null) {
            Gson gson = new Gson();
            Login user = gson.fromJson(mStringJson, Login.class);
        }

        btn_hard_services.setOnClickListener(this);
        btn_soft_services.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    private void gotoIMServices(boolean hard) {

        Bundle data = new Bundle();
        Fragment fragment = new Fragment_IM_HardSoftFilter();
        data.putBoolean(AppUtils.ARGS_IM_HARD_Page, hard);
        data.putString(AppUtils.ARGS_IM_SERVICES_Page_TITLE, mTitle + " - " + (hard ? getString(R.string.title_hard_services) : getString(R.string.title_soft_services)));
        fragment.setArguments(data);

        FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment, Utils.TAG_IM_HARDSOFT_FILTER);
        fragmentTransaction.addToBackStack(Utils.TAG_IM_HARDSOFT_FILTER);
        fragmentTransaction.commit();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
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
}
