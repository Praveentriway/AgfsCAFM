package com.daemon.emco_android.ui.fragments.reactive.viewcomplaint;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.ViewComplaintListAdapter;
import com.daemon.emco_android.repository.remote.GetSearchComplaintService;
import com.daemon.emco_android.ui.components.SimpleDividerItemDecoration;
import com.daemon.emco_android.ui.fragments.common.Fragment_Main;
import com.daemon.emco_android.listeners.SearchComplaintListener;
import com.daemon.emco_android.repository.db.entity.MultiSearchComplaintEntity;
import com.daemon.emco_android.repository.db.entity.SingleSearchComplaintEntity;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.MultiSearchRequest;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Daemonsoft on 7/18/2017.
 */

public class Fragment_RM_ViewComplaintList extends Fragment implements SearchComplaintListener {
    private static final String TAG = Fragment_RM_ViewComplaintList.class.getSimpleName();
    public Bundle mSavedInstanceState;
    private Bundle mArgs;
    private Font font = App.getInstance().getFontInstance();
    private AppCompatActivity mActivity;
    private FragmentManager mManager;
    private CoordinatorLayout cl_main;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextView text_view_loading_message;
    TextView tv_zone_area, tv_property, tv_site_location, tv_complaint_no, tv_request_details, tv_nature, tv_date,
            tv_status, tv_coutumer_ref;
    private LinearLayout layout_loading_message;
    private TextView text_view_message, text_view_empty;
    private LinearLayout layout_loading;
    private RelativeLayout layout_empty,footer_container;
    private ViewComplaintListAdapter adapter;
    private ArrayList<MultiSearchComplaintEntity> mList = new ArrayList<>();
    private MultiSearchRequest searchRequest;
    private SharedPreferences mPreferences;
    private int totalnoOfRows = 0;
    private int mCurrentnoOfRows = 0;
    private boolean isLastPage = true;
    private boolean isLoading = false;
    private int mScrollPosition;
    private String mStrEmpId = null;
    private String mLoginData = null;
    private GetSearchComplaintService mGetSearchData;
    private boolean isDashboard = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.d(TAG, "onCreate");
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mManager = mActivity.getSupportFragmentManager();
            mSavedInstanceState = savedInstanceState;
            //receiveComplaintItemDb = new ReceiveComplaintItemDbInitializer(this);
            mArgs = getArguments();
            getParcelableData();
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_complaint_list, container, false);
        Log.d(TAG, "onCreateView");
        initView(rootView);
        return rootView;
    }

    public void initView(View view) {
        Log.d(TAG, "initView");
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);

            tv_complaint_no = (TextView) view.findViewById(R.id.tv_complaint_no);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_zone_area = (TextView) view.findViewById(R.id.tv_zone_area);
            tv_site_location = (TextView) view.findViewById(R.id.tv_site_location);
            tv_request_details = (TextView) view.findViewById(R.id.tv_request_details);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_coutumer_ref = (TextView) view.findViewById(R.id.tv_coutumer_ref);
            tv_nature = (TextView) view.findViewById(R.id.tv_nature);
            tv_property = (TextView) view.findViewById(R.id.tv_property);

            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

            text_view_loading_message = (TextView) view.findViewById(R.id.text_view_message);
            layout_loading_message = (LinearLayout) view.findViewById(R.id.layout_loading);

            layout_loading = (LinearLayout) view.findViewById(R.id.layout_loading);
            layout_empty = (RelativeLayout) view.findViewById(R.id.layout_empty);
            footer_container = (RelativeLayout) view.findViewById(R.id.footer_container);
            text_view_empty = (TextView) view.findViewById(R.id.text_view_empty);
            text_view_message = (TextView) view.findViewById(R.id.text_view_message);

            setProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setProperties() {
        Log.d(TAG, "setProperties");
        try {
            setManager();
            tv_complaint_no.setTypeface(font.getHelveticaRegular());
            tv_date.setTypeface(font.getHelveticaRegular());
            tv_site_location.setTypeface(font.getHelveticaRegular());
            tv_request_details.setTypeface(font.getHelveticaRegular());
            tv_nature.setTypeface(font.getHelveticaRegular());
            tv_status.setTypeface(font.getHelveticaRegular());
            tv_coutumer_ref.setTypeface(font.getHelveticaRegular());
            tv_zone_area.setTypeface(font.getHelveticaRegular());
            tv_property.setTypeface(font.getHelveticaRegular());

            text_view_empty.setTypeface(font.getHelveticaRegular());
            text_view_message.setTypeface(font.getHelveticaRegular());

            text_view_loading_message.setTypeface(font.getHelveticaRegular());
            setSearchComplaintList();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Log.d(TAG, "onScrolled " + isLastPage + isLoading);
                    if (isLoading) return;
                    if (mList.isEmpty()) return;
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                        //End of list
                        if (!isLastPage) {
                            mScrollPosition = dy;
                            searchRequest.setStartIndex(mCurrentnoOfRows + "");
                            getMoreData();
                        }
                    }
                }
            });
            mGetSearchData = new GetSearchComplaintService(mActivity, this, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getParcelableData() {
        Log.d(TAG, "getParcelableData");
        try {
            if (mArgs != null) {
                if (mArgs.containsKey(AppUtils.ARGS_MULTISEARC_COMPLAINT_LIST)) {
                    mList = mArgs.getParcelableArrayList(AppUtils.ARGS_MULTISEARC_COMPLAINT_LIST);
                    searchRequest = mArgs.getParcelable(AppUtils.ARGS_MULTISEARC_COMPLAINT_REQUEST);
                    totalnoOfRows = Integer.valueOf(mArgs.getString(AppUtils.ARGS_MULTISEARC_COMPLAINT_TOTALPAGE, "0"));
                    isDashboard = mArgs.getBoolean(AppUtils.ARGS_MULTISEARC_COMPLAINT_REQUEST_DASHBOARD, false);
                    Log.d(TAG, "Array list count :" + mList.size());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMoreData() {
        if (mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE).equals(AppUtils.NETWORK_NOT_AVAILABLE)) {
            AppUtils.showDialog(mActivity, getString(R.string.lbl_alert_network_not_available));
            return;
        }
        if (mCurrentnoOfRows == 0) {
            mList.clear();
            showLoadingSearch();
        }
        isLoading = true;
        if (searchRequest.getCode() != null) {
            isDashboard = true;
        } else {
            isDashboard = false;
        }
        mGetSearchData.getMultiSearchData(searchRequest, isDashboard);
    }

    public void setManager() {
        Log.d(TAG, "setManager");
        try {
            mLayoutManager = new LinearLayoutManager(mActivity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(mActivity));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setSearchComplaintList() {
        Log.d(TAG, "setSearchComplaintList totalnoOfRows " + totalnoOfRows);

        try {
            Log.w(TAG, " mList : " + mList.size());
            if (mList.size() > 0) {
                mCurrentnoOfRows = mList.size();
                adapter = new ViewComplaintListAdapter(mActivity, mList);
                if (totalnoOfRows <= mCurrentnoOfRows) {
                    isLastPage = true;
                    adapter.enableFooter(false);
                } else {
                    isLastPage = false;
                    adapter.enableFooter(true);
                }
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                showList();
            } else {
                showEmptyView(getString(R.string.msg_receivecomplaint_empty));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showList() {
        try {
            if (recyclerView != null) recyclerView.setVisibility(View.VISIBLE);
            if (layout_loading != null) layout_loading.setVisibility(View.GONE);
            if (layout_empty != null) layout_empty.setVisibility(View.GONE);
            if (footer_container != null) footer_container.setVisibility(View.VISIBLE);
            AppUtils.closeInput(cl_main);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoadingSearch() {
        try {
            if (layout_empty != null) layout_empty.setVisibility(View.GONE);
            if (footer_container != null) footer_container.setVisibility(View.GONE);
            if (layout_loading != null) layout_loading.setVisibility(View.VISIBLE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showEmptyView(String Str_Msg) {
        try {
            if (text_view_empty != null) {
                text_view_empty.setText(Str_Msg);
            } else AppUtils.showDialog(mActivity, Str_Msg);
            if (layout_empty != null) layout_empty.setVisibility(View.VISIBLE);
            if (footer_container != null) footer_container.setVisibility(View.GONE);
            if (layout_loading != null) layout_loading.setVisibility(View.GONE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
        } catch (Exception ex) {
            ex.printStackTrace();
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

    @Override
    public void onComplaintItemClicked(int position) {

    }

    @Override
    public void onComplaintReceivedSuccess(ArrayList<MultiSearchComplaintEntity> searchComplaintEntityList, String noOfRows) {
        Log.d(TAG, mCurrentnoOfRows + " onComplaintReceivedSuccess totalnoOfRows " + totalnoOfRows);
        isLoading = false;
        AppUtils.hideProgressDialog();
        if (mList != null) {
            mCurrentnoOfRows = mList.size();
        } else {
            mList = new ArrayList<>();
        }

        if (noOfRows != null)
            totalnoOfRows = Integer.valueOf(noOfRows);
        if (searchComplaintEntityList.size() > 0) {
            mList.addAll(searchComplaintEntityList);
            mCurrentnoOfRows = mList.size();
            if (mCurrentnoOfRows > 1) {
                if (totalnoOfRows <= mCurrentnoOfRows) {
                    isLastPage = true;
                    adapter.enableFooter(false);
                } else {
                    isLastPage = false;
                    adapter.enableFooter(true);
                }
            }
            adapter.notifyDataSetChanged();
            recyclerView.setScrollY(mScrollPosition);
        } else Log.d(TAG, "onComplaintReceivedSuccess data Not found");

        Log.d(TAG, mCurrentnoOfRows + " onComplaintReceivedSuccess totalnoOfRows " + totalnoOfRows);
    }

    @Override
    public void onSingleComplaintReceivedSuccess(SingleSearchComplaintEntity searchComplaintEntity) {

    }

    @Override
    public void onComplaintReceivedFailure(String strErr) {
        Log.d(TAG, "onComplaintReceivedFailure");
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity, strErr);
        isLoading = false;
    }
}
