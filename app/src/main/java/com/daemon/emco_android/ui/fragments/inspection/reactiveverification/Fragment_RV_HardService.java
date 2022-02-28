package com.daemon.emco_android.ui.fragments.inspection.reactiveverification;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.daemon.emco_android.ui.fragments.inspection.assetverification.AssetScanningFragment;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;

public class Fragment_RV_HardService extends Fragment implements View.OnClickListener {

    private static final String TAG = "Fragment_RV_HardService";

    private AppCompatActivity mActivity;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();

    private CardView btn_rv_hardservice, btn_rv_softservice;

    private TextView tv_toolbar_title;
    private CoordinatorLayout cl_main;
    private Toolbar mToolbar;

    private Button btnEnter;

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
            rootView = (View) inflater.inflate(R.layout.fragment_rv_hardservice, container, false);
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
            btnEnter = (Button) rootView.findViewById(R.id.btnEnter);
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
        tv_toolbar_title.setText("Reactive Verification - Hard Service");
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
        btnEnter.setOnClickListener(this);
        btn_rv_softservice.setOnClickListener(this);

        AppUtils.closeInput(cl_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEnter:
                loadFragment(new Fragment_RV_HardService_List(), Utils.TAG_F_RV_HARDSERVICE_LIST);
                break;
            case R.id.btn_asset_verification:
                loadFragment(new AssetScanningFragment(), Utils.TAG_F_ASSET_VERIFICATION);
                break;
        }
    }

    public void loadFragment(final Fragment fragment, final String tag) {
        Log.d("Frag", String.valueOf(fragment));
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
