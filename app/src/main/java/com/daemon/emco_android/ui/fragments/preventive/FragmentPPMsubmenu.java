package com.daemon.emco_android.ui.fragments.preventive;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;


import android.os.Handler;
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

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.ui.fragments.common.document.FragmentDocumentFilter;
import com.daemon.emco_android.ui.fragments.preventive.ppmschedule.Fragment_PPM_LIST_Filter;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;


public class FragmentPPMsubmenu extends Fragment implements View.OnClickListener  {

    private AppCompatActivity mActivity;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private Handler mHandler;
    ImageView btn_ppm_schedule,btn_ppm_documents;
    private String mStringJson = null;
    private Login user;
    private CoordinatorLayout cl_main;
    private Toolbar mToolbar;
    private TextView tv_toolbar_title;
    private View rootView = null;

    public FragmentPPMsubmenu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = (View) inflater.inflate(R.layout.fragment_ppmsubmenu, container, false);
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
            btn_ppm_schedule = (ImageView) rootView.findViewById(R.id.btn_ppm_schedule);
            btn_ppm_documents = (ImageView) rootView.findViewById(R.id.btn_ppm_documents);

            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        //iv_backpress=(ImageView)mToolbar.findViewById(R.id.iv_backpress);
        tv_toolbar_title=(TextView)mToolbar.findViewById(R.id.tv_toolbar_title);
        LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);
        tv_toolbar_title.setText("Preventive Maintenance");
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

        btn_ppm_schedule.setOnClickListener(this);
        btn_ppm_documents.setOnClickListener(this);

        AppUtils.closeInput(cl_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ppm_schedule:
                loadFragment(new Fragment_PPM_LIST_Filter(), Utils.Fragment_PPM_LIST_Filter);
                break;

            case R.id.btn_ppm_documents:

                Fragment filter=new FragmentDocumentFilter();
                Bundle mdata = new Bundle();
                mdata.putString(AppUtils.ARGS_FILTERTYPE, "P");
                filter.setArguments(mdata);
                loadFragment(filter, Utils.TAG_DOCUMENT_FILTER);
                break;
        }
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:

                getActivity().getSupportFragmentManager().popBackStack();

                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
