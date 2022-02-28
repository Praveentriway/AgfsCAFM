package com.daemon.emco_android.ui.fragments.inspection;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.IM_PPMListAdapter;
import com.daemon.emco_android.repository.remote.HardSoftService;
import com.daemon.emco_android.repository.remote.ReceiveComplaintListService;
import com.daemon.emco_android.repository.db.entity.ComplaintStatusEntity;
import com.daemon.emco_android.listeners.ReceivecomplaintList_Listener;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.PpmScheduleDetails;
import com.daemon.emco_android.model.request.HardSoftRequest;
import com.daemon.emco_android.model.request.ReceiveComplainCCRequest;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.checkInternet;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_VIEW;

/**
 * Created by Daemonsoft on 7/18/2017.
 */

public class Fragment_IM_PPMList extends Fragment implements ReceivecomplaintList_Listener,
        SearchView.OnQueryTextListener, HardSoftService.Listener, MenuItem.OnMenuItemClickListener {
    private static String TAG = Fragment_IM_PPMList.class.getSimpleName();
    List<ReceiveComplaintItemEntity> temp = new ArrayList<>();
    private Bundle mSavedInstanceState;
    private Bundle mArgs;
    private HardSoftRequest hardSoftRequest;
    private String mTitle;
    private Font font = App.getInstance().getFontInstance();
    private AppCompatActivity mActivity;
    private Toolbar mToolbar;
    private FragmentManager mManager;
    private CoordinatorLayout cl_main;
    private LinearLayoutManager mLayoutManager;
    private TextView text_view_loading_message;
    private LinearLayout layout_loading_message;
    private TextView text_view_message, text_view_empty;
    private Button btn_reassign_ccc;
    private LinearLayout layout_loading;
    private RelativeLayout layout_empty, footer_container;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private TextView tv_s_no, tv_building, tv_location_floor_flat, tv_location_barcode, tv_start_date, tv_end_date, tv_ppm_details;

    private CheckBox cb_checkAll;
    private int totalnoOfRows = 0;
    private int mCurrentnoOfRows = 0;
    private boolean isLastPage = true;
    private boolean isLoading = false;
    private int mScrollPosition = 0;
    private int mSelectedPosition = 0;
    private IM_PPMListAdapter adapter;
    private List<PpmScheduleDetails> mList = new ArrayList<>();
    private String mUnSignedPage = AppUtils.ARGS_RECEIVECOMPLAINT_PAGE;
    private boolean ARGS_RCLIST_F = false;
    private SharedPreferences mPreferences;
    private String mStrEmpId = null;
    private String mLoginData = null;
    private List<ReceiveComplaintItemEntity> data_checked;
    private View.OnClickListener _OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                switch (view.getId()) {
                    case R.id.btn_reassign_ccc:
                        List<ReceiveComplainCCRequest> data_ccc_requrest = new ArrayList<>();
                        if (data_checked != null) {
                            for (int i = 0; i < data_checked.size(); i++) {
                                ReceiveComplaintItemEntity entity = data_checked.get(i);

                                if (entity.isChecked()) {
                                    data_ccc_requrest.add(new ReceiveComplainCCRequest(
                                            entity.getComplaintNumber(),
                                            entity.getOpco(),
                                            entity.getSiteCode()
                                    ));
                                }
                            }
                        }
                        if (data_ccc_requrest.size() > 0) {
                            saveReceiveComplaintCCformService(data_ccc_requrest);
                        } else
                            AppUtils.showDialog(mActivity, "Please Select Data to Re-Assigning to CCC");
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            try {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading) return;
                if (mList.isEmpty()) return;
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    Log.d(TAG, "onScrolled " + isLastPage + isLoading);
                    //End of list
                    if (!isLastPage) {
                        mScrollPosition = dy;
                        getReceiveComplainFromService(mCurrentnoOfRows);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mManager = mActivity.getSupportFragmentManager();
            mSavedInstanceState = savedInstanceState;
            mArgs = getArguments();
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }

            if (mArgs != null) {
                ARGS_RCLIST_F = mArgs.getBoolean(AppUtils.ARGS_RCLIST_F, false);
                hardSoftRequest = mArgs.getParcelable(AppUtils.ARGS_REACTIVE_MAINTENANCE_DASHBOARD_REQUEST);
                mTitle = mArgs.getString(AppUtils.ARGS_IM_SERVICES_Page_TITLE);
                getReceiveComplainFromService(mCurrentnoOfRows);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_im_ppm_list, container, false);
        initView(rootView);
        setProperties();
        setupActionBar();
        return rootView;
    }

    public void initView(View view) {

        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            AppUtils.closeInput(cl_main);

            searchView = (SearchView) view.findViewById(R.id.sv_rc_search);
            searchView.setOnQueryTextListener(this);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            cb_checkAll = (CheckBox) view.findViewById(R.id.cb_checkall);
            tv_s_no = (TextView) view.findViewById(R.id.tv_s_no);
            tv_building = (TextView) view.findViewById(R.id.tv_building);
            tv_location_floor_flat = (TextView) view.findViewById(R.id.tv_location_floor_flat);
            tv_location_barcode = (TextView) view.findViewById(R.id.tv_location_barcode);
            tv_start_date = (TextView) view.findViewById(R.id.tv_start_date);
            tv_end_date = (TextView) view.findViewById(R.id.tv_end_date);
            tv_ppm_details = (TextView) view.findViewById(R.id.tv_ppm_details);
            text_view_loading_message = (TextView) view.findViewById(R.id.text_view_message);
            layout_loading_message = (LinearLayout) view.findViewById(R.id.layout_loading);
            layout_loading = (LinearLayout) view.findViewById(R.id.layout_loading);
            layout_empty = (RelativeLayout) view.findViewById(R.id.layout_empty);
            footer_container = (RelativeLayout) view.findViewById(R.id.footer_container);
            text_view_empty = (TextView) view.findViewById(R.id.text_view_empty);
            text_view_message = (TextView) view.findViewById(R.id.text_view_message);

            btn_reassign_ccc = (Button) view.findViewById(R.id.btn_reassign_ccc);
            if (mList != null && mList.isEmpty()) {
                showLoadingReceiveComplaint();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
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

    private void saveReceiveComplaintCCformService(List<ReceiveComplainCCRequest> data_ccc_requrest) {
        Log.d(TAG, "saveReceiveComplaintCCformService");
        try {
            if (checkInternet(getContext())) {
                AppUtils.showProgressDialog(mActivity, getString(R.string.re_assigning_to_cc), false);
                new ReceiveComplaintListService(mActivity, this).PostReceiveComplaintCCData(data_ccc_requrest);
            } else showEmptyView(getString(R.string.lbl_alert_network_not_available));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setProperties() {
        Log.d(TAG, "setProperties");
        try {
            setManager();
            searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(this);

            btn_reassign_ccc.setVisibility(View.GONE);

            adapter = new IM_PPMListAdapter(mActivity, mList);
            adapter.setListener(this);
            recyclerView.setAdapter(adapter);

            recyclerView.addOnScrollListener(onScrollListener);

            if (mSavedInstanceState != null) {
                mUnSignedPage = mSavedInstanceState.getString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE);
                mList = mSavedInstanceState.getParcelableArrayList(AppUtils.ARG_RC_LIST);
                mSelectedPosition = mSavedInstanceState.getInt(AppUtils.ARG_SELECTED_POSITION);
                setReceivecomplaintList();
            }

            AppUtils.closeInput(cl_main);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setManager() {

        try {
            mLayoutManager = new LinearLayoutManager(mActivity);
            recyclerView.setLayoutManager(mLayoutManager);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getReceiveComplainFromService(int startIndex) {

        try {
            if (isLoading) return;
            if (startIndex == 0) {
                mList.clear();
                mCurrentnoOfRows = 0;
                if (adapter != null) adapter.notifyDataSetChanged();
                showLoadingReceiveComplaint();
            }
            if (checkInternet(getContext())) {
                isLoading = true;
              //  new ReceiveComplaintListService(mActivity, this).GetReceiveComplaintListData(mStrEmpId, ARGS_RCLIST_F ? "F" : "A", startIndex , mUnSignedPage);
                new HardSoftService(mActivity, this,null)
                        .getHardSoftServicePPMDetails(hardSoftRequest, startIndex, mTitle);
               // new PPMScheduleDetails_Service(mActivity, this).getIMppmListData(startIndex, fetchRequest,mTitle);
            } else {
                if (mUnSignedPage == AppUtils.ARGS_RECEIVECOMPLAINT_PAGE) {
                    //receiveComplaintItemDb.populateAsync(AppDatabase.getAppDatabase(mActivity), AppUtils.MODE_GETALL);
                } else showEmptyView(getString(R.string.lbl_alert_network_not_available));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setReceivecomplaintList() {
        Log.w(TAG, "setReceivecomplaintList");
        try {
            if (mList != null && mList.size() > 0) {
                adapter.notifyDataSetChanged();
                recyclerView.setScrollY(mScrollPosition);
                showList();
            } else {
                AppUtils.showDialog(mActivity, getString(R.string.msg_receivecomplaint_empty));
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

    public void showLoadingReceiveComplaint() {
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

    @Override
    public void onReceiveComplaintListReceived(List<ReceiveComplaintItemEntity> receiveComplaintItemList, String noOfRows, int from) {
        try {
            Log.d(TAG, mCurrentnoOfRows + " onReceiveComplaintListReceived totalnoOfRows " + totalnoOfRows);
            AppUtils.hideProgressDialog();
            isLoading = false;
            if (mList != null) {
                mCurrentnoOfRows = mList.size();
            } else {
                mList = new ArrayList<>();
            }
            if (noOfRows != null)
                totalnoOfRows = Integer.valueOf(noOfRows);
            if (receiveComplaintItemList.size() > 0) {
               // mList.addAll(receiveComplaintItemList);
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
                setReceivecomplaintList();
            } else {
                if (from == AppUtils.MODE_LOCAL) {
                    showEmptyView(getString(R.string.lbl_alert_network_not_available));
                } else {
                    showEmptyView(getString(R.string.msg_request_error_occurred));
                }
            }

            Log.d(TAG, mCurrentnoOfRows + " onComplaintReceivedSuccess totalnoOfRows " + totalnoOfRows);

            if (mUnSignedPage == AppUtils.ARGS_RECEIVECOMPLAINT_PAGE) {
                if (!mList.isEmpty() && from == AppUtils.MODE_SERVER) {
                          }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onReceiveComplaintListReceivedUpdate(List<ReceiveComplaintItemEntity> receiveComplaintItemList, ComplaintStatusEntity complaintStatusEntity, int from) {

    }

    @Override
    public void onReceiveComplaintListReceivedError(String Str_Msg, int mode) {
        Log.d(TAG, "onReceivecomplaintListReceivedError");
        try {
            AppUtils.hideProgressDialog();
            if (mList != null && mList.size() > 0) {
                AppUtils.showDialog(mActivity, Str_Msg);
            } else {
                showEmptyView(Str_Msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onReceiveComplaintListItemClicked(List<ReceiveComplaintItemEntity> temp, int position) {
        Log.d(TAG, "onReceivecomplaintListItemClicked");
        try {
            this.temp = temp;
            if (temp != null) {
                if (temp.get(position).getComplaintNumber() != null) {
                    gotoFragmentReceiveComplaintView(temp, position);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onReceiveComplaintListItemChecked(List<ReceiveComplaintItemEntity> data_checked) {
        this.data_checked = data_checked;
    }

    @Override
    public void onReceiveComplaintCCReceived(String strMsg, int from) {
        try {
            AppUtils.hideProgressDialog();
            AppUtils.showDialog(mActivity, strMsg);
            //receiveComplaintItemDb.populateAsync(AppDatabase.getAppDatabase(mActivity), AppUtils.MODE_DELETE);
            getReceiveComplainFromService(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoFragmentReceiveComplaintView(List<ReceiveComplaintItemEntity> data, int position) {
        Log.d(TAG, "gotoFragmentReceiveComplaintView");
        try {
            mSavedInstanceState = getSavedState();
            Bundle mdata = new Bundle();
            mdata.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, mUnSignedPage);
            mdata.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS, data.get(position));
            mdata.putString(AppUtils.ARGS_IM_SERVICES_Page_TITLE, mTitle);
            Fragment fragment = new Fragment_IM_PPMListView();
            fragment.setArguments(mdata);
            FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_VIEW);
            fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_VIEW);
            fragmentTransaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchItem = menu.findItem(R.id.action_search).setVisible(true);
        MenuItem refreshItem = menu.findItem(R.id.action_refresh).setVisible(true);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);

        refreshItem.setOnMenuItemClickListener(this);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.lbl_rc_search));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);
        changeSearchViewTextColor(searchView);
    }


    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSavedInstanceState = getSavedStateOnPause();
    }

    public Bundle getSavedStateOnPause() {
        Bundle outState = new Bundle();
        outState.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, mUnSignedPage);
        outState.putInt(AppUtils.ARG_SELECTED_POSITION, mSelectedPosition);
        outState.putParcelableArrayList(AppUtils.ARG_RC_LIST, (ArrayList<PpmScheduleDetails>) mList);

        return outState;
    }

    public Bundle getSavedState() {
        Bundle outState = new Bundle();
        outState.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, mUnSignedPage);
        outState.putInt(AppUtils.ARG_SELECTED_POSITION, mSelectedPosition);
        outState.putParcelableArrayList(AppUtils.ARG_RC_LIST, (ArrayList<PpmScheduleDetails>) mList);
        return outState;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit");
        // filter your list from your input
       // filter(query);
        //you can use runnable postDelayed like 500 ms to delay search text
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }



    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Log.d(TAG, "onMenuItemClick");
        if (isLoading) return false;
        getReceiveComplainFromService(0);
        return false;
    }

    @Override
    public void onppmListItemClicked(List<PpmScheduleDetails> ppmDetailsList, int position) {

    }

    @Override
    public void onppmListReceived(List<PpmScheduleDetails> ppmDetailsList, String noOfRows, int from) {
        try {
            Log.d(TAG, mCurrentnoOfRows + " onReceiveComplaintListReceived totalnoOfRows " + totalnoOfRows);
            AppUtils.hideProgressDialog();
            isLoading = false;
            if (mList != null) {
                mCurrentnoOfRows = mList.size();
            } else {
                mList = new ArrayList<>();
            }
            if (noOfRows != null)
                totalnoOfRows = Integer.valueOf(noOfRows);
            if (ppmDetailsList.size() > 0) {
                mList.addAll(ppmDetailsList);
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
                setReceivecomplaintList();
            } else {
                if (from == AppUtils.MODE_LOCAL) {
                    showEmptyView(getString(R.string.lbl_alert_network_not_available));
                } else {
                    showEmptyView(getString(R.string.msg_request_error_occurred));
                }
            }

            Log.d(TAG, mCurrentnoOfRows + " onComplaintReceivedSuccess totalnoOfRows " + totalnoOfRows);

            if (mUnSignedPage == AppUtils.ARGS_RECEIVECOMPLAINT_PAGE) {
                if (!mList.isEmpty() && from == AppUtils.MODE_SERVER) {
                    //receiveComplaintItemDb.populateAsync(AppDatabase.getAppDatabase(mActivity), mList, isLastPage ? AppUtils.MODE_INSERT : AppUtils.MODE_INSERT_SINGLE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onppmListReceivedError(String errMsg, int from) {
        TAG = "onReceivecomplaintListReceivedError";
        try {
            AppUtils.hideProgressDialog();
            if (mList.size() > 0) {
                AppUtils.showDialog(mActivity, errMsg);
            } else {
                showEmptyView(errMsg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
