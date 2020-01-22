package com.daemon.emco_android.ui.fragments.reactive.viewcomplaint;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
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
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.fragments.common.Fragment_Main;
import com.daemon.emco_android.repository.db.entity.SingleSearchComplaintEntity;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;

import static com.daemon.emco_android.utils.AppUtils.ARGS_SEARCH_COMPLAINT_RESULT;

/**
 * Created by subbu on 25/11/16.
 */

public class Fragment_RM_ViewComplaint extends Fragment {
    private static final String TAG = Fragment_RM_ViewComplaint.class.getSimpleName();

    private AppCompatActivity mActivity;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;

    private TextView tv_lbl_complaint_no,tv_toolbar_title, tv_lbl_site, tv_lbl_location, tv_lbl_flat_office_villa,
            tv_lbl_complaint_details, tv_lbl_complaint_status, tv_lbl_complaint_date, tv_lbl_customer_ref_no;
    private TextView tv_complaint_no, tv_site, tv_location, tv_flat_office_villa,
            tv_complaint_details, tv_complaint_status, tv_complaint_date,
            tv_customer_ref_no, tv_lbl_zone_area, tv_zone_area, tv_lbl_building, tv_building;
    private String mStringJson = null;

    private CoordinatorLayout cl_main;
    private Toolbar mToolbar;
    private Snackbar snackbar;
    private Bundle mArgs = new Bundle();
    private SingleSearchComplaintEntity mSingleSearchData;

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
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            mStringJson = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            mManager = mActivity.getSupportFragmentManager();
            font = App.getInstance().getFontInstance();
            mArgs = getArguments();

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
            rootView = (View) inflater.inflate(R.layout.fragment_view_complaint, container, false);
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
            tv_lbl_complaint_no = (TextView) rootView.findViewById(R.id.tv_lbl_complaint_no);
            tv_lbl_site = (TextView) rootView.findViewById(R.id.tv_lbl_site);
            tv_lbl_location = (TextView) rootView.findViewById(R.id.tv_lbl_location);
            tv_lbl_flat_office_villa = (TextView) rootView.findViewById(R.id.tv_lbl_flat_office_villa);
            tv_lbl_complaint_details = (TextView) rootView.findViewById(R.id.tv_lbl_complaint_details);
            tv_lbl_complaint_status = (TextView) rootView.findViewById(R.id.tv_lbl_complaint_status);
            tv_lbl_complaint_date = (TextView) rootView.findViewById(R.id.tv_lbl_complaint_date);
            tv_lbl_customer_ref_no = (TextView) rootView.findViewById(R.id.tv_lbl_customer_ref_no);

            tv_complaint_no = (TextView) rootView.findViewById(R.id.tv_complaint_no);
            tv_site = (TextView) rootView.findViewById(R.id.tv_site);
            tv_location = (TextView) rootView.findViewById(R.id.tv_location);
            tv_flat_office_villa = (TextView) rootView.findViewById(R.id.tv_flat_office_villa);
            tv_complaint_details = (TextView) rootView.findViewById(R.id.tv_complaint_details);
            tv_complaint_status = (TextView) rootView.findViewById(R.id.tv_complaint_status);
            tv_complaint_date = (TextView) rootView.findViewById(R.id.tv_complaint_date);
            tv_customer_ref_no = (TextView) rootView.findViewById(R.id.tv_customer_ref_no);

            tv_lbl_zone_area = (TextView) rootView.findViewById(R.id.tv_lbl_zone_area);
            tv_zone_area = (TextView) rootView.findViewById(R.id.tv_zone_area);
            tv_lbl_building = (TextView) rootView.findViewById(R.id.tv_lbl_building);
            tv_building = (TextView) rootView.findViewById(R.id.tv_building);
            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title=(TextView)mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.lbl_view_complaint_status));
        //mToolbar.setTitle(getResources().getString(R.string.lbl_view_complaint_status));
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
        try {
            Log.d(TAG, "setProperties");
            tv_lbl_complaint_no.setTypeface(font.getHelveticaRegular());
            tv_lbl_site.setTypeface(font.getHelveticaRegular());
            tv_lbl_location.setTypeface(font.getHelveticaRegular());
            tv_lbl_flat_office_villa.setTypeface(font.getHelveticaRegular());
            tv_lbl_complaint_details.setTypeface(font.getHelveticaRegular());
            tv_lbl_complaint_status.setTypeface(font.getHelveticaRegular());

            tv_complaint_no.setTypeface(font.getHelveticaRegular());
            tv_site.setTypeface(font.getHelveticaRegular());
            tv_location.setTypeface(font.getHelveticaRegular());
            tv_flat_office_villa.setTypeface(font.getHelveticaRegular());
            tv_complaint_details.setTypeface(font.getHelveticaRegular());
            tv_complaint_status.setTypeface(font.getHelveticaRegular());

            tv_lbl_zone_area.setTypeface(font.getHelveticaRegular());
            tv_zone_area.setTypeface(font.getHelveticaRegular());
            tv_lbl_building.setTypeface(font.getHelveticaRegular());
            tv_building.setTypeface(font.getHelveticaRegular());

            if (mArgs != null) {
                mSingleSearchData = mArgs.getParcelable(ARGS_SEARCH_COMPLAINT_RESULT);

                tv_complaint_no.setText(mSingleSearchData.getComplaintNumber());
                tv_site.setText(mSingleSearchData.getSiteName());
                tv_location.setText(mSingleSearchData.getLocation());
                tv_flat_office_villa.setText(mSingleSearchData.getFloorFlat());
                tv_complaint_details.setText(mSingleSearchData.getComplainDetails());
                tv_complaint_status.setText(mSingleSearchData.getStatus());
                tv_complaint_date.setText(mSingleSearchData.getComplainDate());
                tv_customer_ref_no.setText(mSingleSearchData.getCustomerRefrenceNumber());

                tv_zone_area.setText(mSingleSearchData.getZoneName());
                tv_building.setText(mSingleSearchData.getBuildingName());
            }
            AppUtils.closeInput(cl_main);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(TAG, "onPrepareOptionsMenu ");
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Log.d(TAG, "onOptionsItemSelected : home");
                //mActivity.onBackPressed();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                Fragment _fragment = new Fragment_Main();
                FragmentTransaction _transaction = mManager.beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
