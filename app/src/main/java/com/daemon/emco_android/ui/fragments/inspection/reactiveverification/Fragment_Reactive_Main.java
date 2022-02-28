package com.daemon.emco_android.ui.fragments.inspection.reactiveverification;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.fragments.inspection.Fragment_IM_HardSoftFilter;
import com.daemon.emco_android.ui.fragments.inspection.Fragment_IM_Services;
import com.daemon.emco_android.ui.fragments.inspection.Fragment_InspectionModule_Landing;
import com.daemon.emco_android.ui.fragments.inspection.assetverification.AssetScanningFragment;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;

public class Fragment_Reactive_Main  extends Fragment implements View.OnClickListener {
    private static final String TAG = "Fragment_Reactive_Main";

    private AppCompatActivity mActivity;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();
    private Bundle bundle;
    private CardView btn_rv_hardservice, btn_rv_softservice;

    private TextView tv_toolbar_title;
    private CoordinatorLayout cl_main;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            mContext = mActivity;
            setHasOptionsMenu(true);
            font = App.getInstance().getFontInstance();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = null;
        try {
            rootView =
                    (View) inflater.inflate(R.layout.fragment_reactive_main, container, false);
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
            btn_rv_hardservice = (CardView) rootView.findViewById(R.id.btn_rv_hardservice);
            btn_rv_softservice= (CardView) rootView.findViewById(R.id.btn_rv_softservice);
            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title=(TextView)mToolbar.findViewById(R.id.tv_toolbar_title);
        LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);
        tv_toolbar_title.setText("Reactive Verification");
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
    private void setProperties() {

    //    btn_rx_inspection.setOnClickListener(this);
     //   btn_ppm_inspection.setOnClickListener(this);
        btn_rv_hardservice.setOnClickListener(this);
        btn_rv_softservice.setOnClickListener(this);

        AppUtils.closeInput(cl_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_rv_hardservice:
                loadFragment(new Fragment_IM_HardSoftFilter(), Utils.TAG_F_RV_HARDSERVICE);
                break;
            case R.id.btn_rv_softservice:
                //loadFragment(new Fragment_RV_SoftService(), Utils.TAG_F_RV_HARDSERVICE);
                loadFragment(new Fragment_IM_HardSoftFilter(), Utils.TAG_F_RV_SOFTSERVICE);
                break;
        }
    }

    private void gotoIMServices(boolean ppe, String title) {

        Bundle data = new Bundle();
        Fragment_IM_Services complaintsList = new Fragment_IM_Services();
        data.putBoolean(AppUtils.ARGS_IM_PPE_Page, ppe);
        data.putString(AppUtils.ARGS_IM_SERVICES_Page_TITLE, title);
        Log.d("DAtas", String.valueOf(data));
        complaintsList.setArguments(data);
        Log.d("ComplainList", String.valueOf(complaintsList));
        loadFragment(complaintsList, Utils.TAG_IM_SERVICES);

    }

    public void loadFragment(final Fragment fragment, final String tag) {

        Log.d("Frag", String.valueOf(fragment));
        bundle = new Bundle();
        bundle.putString ("tag",tag);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();


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
                mActivity.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}

