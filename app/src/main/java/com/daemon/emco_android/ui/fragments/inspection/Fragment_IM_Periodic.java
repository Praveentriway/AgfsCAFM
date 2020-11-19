package com.daemon.emco_android.ui.fragments.inspection;

import android.content.Context;
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

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;

public class Fragment_IM_Periodic extends Fragment implements View.OnClickListener {

    private AppCompatActivity mActivity;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();
    private LinearLayout ll_im_landing, ll_im_services, ll_im_periodic;
    private Button btn_assigned, btn_random;
    private CoordinatorLayout cl_main;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            mContext = mActivity;
            setHasOptionsMenu(true);
            setRetainInstance(false);
            font = App.getInstance().getFontInstance();

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
        mToolbar.setTitle(getResources().getString(R.string.title_periodic_inspection));
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
        ll_im_periodic.setVisibility(View.VISIBLE);
        ll_im_services.setVisibility(View.GONE);
        btn_assigned.setOnClickListener(this);
        btn_random.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    private void gotoIMServices(boolean random, String title) {

        Bundle data = new Bundle();
        Fragment_IM_Services complaintsList = new Fragment_IM_Services();
        data.putBoolean(AppUtils.ARGS_IM_RANDOM_PAGE, random);
        data.putString(AppUtils.ARGS_IM_SERVICES_Page_TITLE, title);
        complaintsList.setArguments(data);

        FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, complaintsList, Utils.TAG_IM_SERVICES);
        fragmentTransaction.addToBackStack(Utils.TAG_IM_SERVICES);
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
