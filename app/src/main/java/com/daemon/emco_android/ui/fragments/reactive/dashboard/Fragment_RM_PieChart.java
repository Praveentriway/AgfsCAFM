package com.daemon.emco_android.ui.fragments.reactive.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.GetSearchComplaintService;
import com.daemon.emco_android.repository.db.entity.MultiSearchComplaintEntity;
import com.daemon.emco_android.repository.db.entity.SingleSearchComplaintEntity;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.ui.fragments.reactive.viewcomplaint.Fragment_RM_ViewComplaintList;
import com.daemon.emco_android.listeners.SearchComplaintListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.MultiSearchRequest;
import com.daemon.emco_android.model.response.DashboardPieData;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ConnectivityStatus;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Daemonsoft on 8/17/2017.
 */
public class Fragment_RM_PieChart extends Fragment
        implements OnChartValueSelectedListener, SearchComplaintListener, IValueFormatter {
    private static final String TAG = Fragment_RM_PieChart.class.getSimpleName();
    private static String mRowsExpected = "10", mStartIndex = "0";
    public Bundle mSavedInstanceState;
    // root view of fragment
    private View rootView = null;
    private AppCompatActivity mActivity;
    private Context mContext;
    private Font font = App.getInstance().getFontInstance();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FragmentManager mManager;
    private CoordinatorLayout cl_main;
    private PieChart mChart;
    private TextView tv_lbl_chartdetails, tv_lbl_zone_area, tv_toolbar_title;
    private String mStrLoginData = null;
    private String mStrEmployeeId = null;
    private Bundle mArgs;
    private Toolbar mToolbar;
    private Login mUserData;
    private ArrayList<DashboardPieData> dashboardPieDatas;
    private MultiSearchRequest pieChartRequest;
    private String mDes = "", mZone_area = "";
    private GetSearchComplaintService mGetSearchData;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            mContext = mActivity;
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
            mStrLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mStrLoginData != null) {
                Gson gson = new Gson();
                mUserData = gson.fromJson(mStrLoginData, Login.class);
                mStrEmployeeId = mUserData.getEmployeeId();
            }
            mSavedInstanceState = savedInstanceState;
            mArgs = getArguments();
            getParcelableData();

            mManager = mActivity.getSupportFragmentManager();
            font = App.getInstance().getFontInstance();
            if (mActivity.getCurrentFocus() != null) {
                InputMethodManager imm =
                        (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getParcelableData() {
        try {
            if (mArgs != null) {
                if (mArgs.containsKey(AppUtils.ARGS_REACTIVE_MAINTENANCE_DASHBOARD_DATA)) {
                    dashboardPieDatas =
                            mArgs.getParcelableArrayList(AppUtils.ARGS_REACTIVE_MAINTENANCE_DASHBOARD_DATA);
                    pieChartRequest =
                            mArgs.getParcelable(AppUtils.ARGS_REACTIVE_MAINTENANCE_DASHBOARD_REQUEST);
                    Log.d(TAG, "ARGS_REACTIVE_MAINTENANCE_DASHBOARD_DATA  :" + dashboardPieDatas.size());
                    mDes =
                            pieChartRequest.getFromDate()
                                    + " to "
                                    + pieChartRequest.getToDate()
                                    + "\t"
                                    + pieChartRequest.getReportType();
                    mZone_area = mArgs.getString(AppUtils.ARGS_BIECHART_ZONETITLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = (View) inflater.inflate(R.layout.fragment_piecharts, container, false);
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
            mChart = (PieChart) rootView.findViewById(R.id.pieChart);
            tv_lbl_chartdetails = (TextView) rootView.findViewById(R.id.tv_lbl_chartdetails);
            tv_lbl_zone_area = (TextView) rootView.findViewById(R.id.tv_lbl_zone_area);
            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.lbl_dashboard));
        // mToolbar.setTitle(getResources().getString(R.string.lbl_dashboard));
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.onBackPressed();
                    }
                });
    }

    private void setProperties() {

        mChart.setUsePercentValues(false);
        mChart.getDescription().setText("");
        tv_lbl_chartdetails.setTypeface(font.getHelveticaBold());
        tv_lbl_zone_area.setTypeface(font.getHelveticaBold());

        // radius of the center hole in percent of maximum radius
        // Disable Hole in the Pie Chart
        mChart.setDrawHoleEnabled(false);
        // Enable rotate chart
        mChart.setRotationEnabled(true);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        if (dashboardPieDatas.size() > 5) {
            l.setTextSize(16f);
        } else l.setTextSize(18f);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        tv_lbl_chartdetails.setText(mDes);
        tv_lbl_zone_area.setText(mZone_area);
        mChart.setData(generatePieData());
        mChart.setOnChartValueSelectedListener(this);
        mChart.animateXY(1400, 1400);
        mChart.setEntryLabelColor(Color.TRANSPARENT);
    }

    protected PieData generatePieData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        try {

            for (int i = 0; i < dashboardPieDatas.size(); i++) {
                DashboardPieData pieData = dashboardPieDatas.get(i);
                entries.add(
                        new PieEntry(Float.parseFloat(pieData.getTotalNoOfRows()), pieData.getDescription()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PieDataSet ds1 = new PieDataSet(entries, "");
        ds1.setColors(AppUtils.MATERIAL_COLORS);
        ds1.setSliceSpace(1f);
        ds1.setValueTypeface(font.getHelveticaRegular());
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(16f);

        PieData d = new PieData(ds1);
        d.setValueTypeface(font.getHelveticaRegular());
        d.setValueFormatter(this);

        return d;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        try {
            if (e == null) return;
            if (ConnectivityStatus.isConnected(mActivity)) {
                DashboardPieData pieData = dashboardPieDatas.get((int) h.getX());
                Log.i("VAL SELECTED", "Value: " + pieData.getTotalNoOfRows() + ", index: " + h.getX());
                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                mGetSearchData = new GetSearchComplaintService(mActivity, this, this);
                pieChartRequest.setCode(pieData.getCode());
                pieChartRequest.setStartIndex(mStartIndex);
                pieChartRequest.setRowsExpected(mRowsExpected);
                if (mUserData.getUserType().equalsIgnoreCase(AppUtils.CUSTOMER)) {

                }
                mGetSearchData.getMultiSearchData(pieChartRequest, true);
            } else {
                Toast.makeText(mActivity,(R.string.lbl_alert_network_not_available),Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void gotoFragmentViewComplaintList(
            ArrayList<MultiSearchComplaintEntity> searchComplaintEntities, String noOfRows) {
        Bundle data = new Bundle();
        data.putParcelableArrayList(AppUtils.ARGS_MULTISEARC_COMPLAINT_LIST, searchComplaintEntities);
        data.putParcelable(AppUtils.ARGS_MULTISEARC_COMPLAINT_REQUEST, pieChartRequest);
        data.putString(AppUtils.ARGS_MULTISEARC_COMPLAINT_TOTALPAGE, noOfRows);
        Fragment _fragment = new Fragment_RM_ViewComplaintList();
        _fragment.setArguments(data);
        FragmentTransaction _transaction = mManager.beginTransaction();
        _transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        _transaction.replace(R.id.frame_container, _fragment, Utils.TAG_VIEW_COMPLAINTLIST);
        _transaction.addToBackStack(Utils.TAG_VIEW_COMPLAINTLIST);
        _transaction.commit();
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onComplaintItemClicked(int position) {
    }

    @Override
    public void onComplaintReceivedSuccess(
            ArrayList<MultiSearchComplaintEntity> searchComplaintEntities, String noOfRows) {
        AppUtils.hideProgressDialog();
        if (searchComplaintEntities.size() > 0)
            gotoFragmentViewComplaintList(searchComplaintEntities, noOfRows);
        else Log.d(TAG, "onComplaintReceivedSuccess data Not found");
    }

    @Override
    public void onSingleComplaintReceivedSuccess(SingleSearchComplaintEntity searchComplaintEntity) {
        Log.d(TAG, "onSingleComplaintReceivedSuccess");
        AppUtils.hideProgressDialog();
    }

    @Override
    public void onComplaintReceivedFailure(String strErr) {
        Log.d(TAG, "onComplaintReceivedFailure");
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity, strErr);
    }

    @Override
    public String getFormattedValue(
            float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return "" + (int) value;
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                Fragment _fragment = new MainDashboard();
                FragmentTransaction _transaction = mManager.beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
