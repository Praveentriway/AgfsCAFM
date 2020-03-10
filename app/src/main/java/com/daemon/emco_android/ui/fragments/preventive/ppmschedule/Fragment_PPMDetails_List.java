package com.daemon.emco_android.ui.fragments.preventive.ppmschedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.PPMDetailsListAdapter;
import com.daemon.emco_android.repository.remote.CheckCustomerSignRepository;
import com.daemon.emco_android.repository.remote.PPMScheduleDetailsService;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.listeners.CheckCustomerSignListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.PPMDetails;
import com.daemon.emco_android.model.common.PpmScheduleDetails;
import com.daemon.emco_android.model.common.PpmScheduleDocBy;
import com.daemon.emco_android.model.request.FetchPpmScheduleDetailsRequest;
import com.daemon.emco_android.model.request.PpmScheduleDocByRequest;
import com.daemon.emco_android.model.request.SaveRatedServiceRequest;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.checkInternet;
import static com.daemon.emco_android.utils.AppUtils.showProgressDialog;

public class Fragment_PPMDetails_List extends Fragment
        implements PPMScheduleDetailsService.Listener, CheckCustomerSignListener {
    private static String MODULE = "F_RComplaintsList";
    private static String TAG = "";
    private Bundle mSavedInstanceState;
    private Bundle mArgs;
    private Font font = App.getInstance().getFontInstance();
    private AppCompatActivity mActivity;
    private Toolbar mToolbar;
    private FragmentManager mManager;
    private CoordinatorLayout cl_main;
    private LinearLayoutManager mLayoutManager;
    private TextView text_view_loading_message;
    private LinearLayout layout_loading_message;
    private TextView text_view_message, text_view_empty, tv_toolbar_title;
    private Button btn_custmsign;
    private LinearLayout layout_loading;
    private RelativeLayout layout_empty, footer_container;
    private RecyclerView recyclerView;
    private TextView tv_building,
            tv_proposed_start_date,
            tv_proposed_end_date,
            tv_status,
            tv_asset_name;

    private int totalnoOfRows = 0;
    private int mCurrentnoOfRows = 0;
    private boolean isLastPage = true;
    private boolean isLoading = false;
    private int mScrollPosition = 0;
    private int mSelectedPosition = 0;

    private PPMDetailsListAdapter adapter;
    private List<PpmScheduleDetails> mList = new ArrayList<>();

    private SharedPreferences mPreferences;
    private String mStrEmpId = null;
    private String mLoginData = null;
    private FetchPpmScheduleDetailsRequest fetchRequest;
    private PPMDetails ppmDetails;
    private List<PpmScheduleDocBy> ppmScheduleDocByList;
    private PpmScheduleDocBy ppmScheduleDocBy;
    View.OnClickListener _OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_custmsign:
                            gotoRateService();
                            break;

                        default:
                            break;
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
            mActivity
                    .getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
            mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);

            if (mLoginData != null) {
                Gson gson = new Gson();
                Login login = gson.fromJson(mLoginData, Login.class);
                mStrEmpId = login.getEmployeeId();
            }

            if (mArgs != null && mArgs.size() > 0) {

                fetchRequest = new FetchPpmScheduleDetailsRequest();
                ppmDetails = mArgs.getParcelable(AppUtils.ARGS_PPMDetails_List);
                fetchRequest.setCompCode(ppmDetails.getCompCode());
                fetchRequest.setContractNo(ppmDetails.getContractNo());
                fetchRequest.setWorkOrderNo(ppmDetails.getWorkOrderNo());
                getReceiveComplainFromService(mCurrentnoOfRows);

            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ppmsdetails_list, container, false);
        initView(rootView);
        setProperties();
        setupActionBar();
        return rootView;
    }

    public void initView(View view) {
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

            tv_building = (TextView) view.findViewById(R.id.tv_building);
            tv_asset_name = (TextView) view.findViewById(R.id.tv_asset_name);
            tv_proposed_start_date = (TextView) view.findViewById(R.id.tv_start_date);
            tv_proposed_end_date = (TextView) view.findViewById(R.id.tv_end_date);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            text_view_loading_message = (TextView) view.findViewById(R.id.text_view_message);
            layout_loading_message = (LinearLayout) view.findViewById(R.id.layout_loading);
            layout_loading = (LinearLayout) view.findViewById(R.id.layout_loading);
            layout_empty = (RelativeLayout) view.findViewById(R.id.layout_empty);
            footer_container = (RelativeLayout) view.findViewById(R.id.footer_container);
            text_view_empty = (TextView) view.findViewById(R.id.text_view_empty);
            text_view_message = (TextView) view.findViewById(R.id.text_view_message);
            btn_custmsign = (Button) view.findViewById(R.id.btn_custmsign);
            if (mList != null && mList.isEmpty()) {
                showLoadingReceiveComplaint();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(getString(R.string.lbl_ppm_details));
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

    public void setProperties() {
        try {
            setManager();
            tv_building.setTypeface(font.getHelveticaRegular());
            tv_asset_name.setTypeface(font.getHelveticaRegular());
            tv_proposed_start_date.setTypeface(font.getHelveticaRegular());
            tv_proposed_end_date.setTypeface(font.getHelveticaRegular());
            tv_status.setTypeface(font.getHelveticaRegular());
            text_view_empty.setTypeface(font.getHelveticaRegular());
            text_view_message.setTypeface(font.getHelveticaRegular());
            btn_custmsign.setTypeface(font.getHelveticaRegular());
            text_view_loading_message.setTypeface(font.getHelveticaRegular());
            btn_custmsign.setOnClickListener(_OnClickListener);

            recyclerView.addOnScrollListener(
                    new RecyclerView.OnScrollListener() {
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
                                // End of list
                                if (!isLastPage) {
                                    mScrollPosition = dy;
                                    getReceiveComplainFromService(mCurrentnoOfRows);
                                }
                            }
                        }
                    });

            adapter = new PPMDetailsListAdapter(mActivity, mList);
            adapter.setListener(this);
            recyclerView.setAdapter(adapter);

            if (mSavedInstanceState != null) {
                Log.d(MODULE, TAG + " mSavedInstanceState : " + mSavedInstanceState);
                mList = mSavedInstanceState.getParcelableArrayList(AppUtils.ARG_RC_LIST);
                mSelectedPosition = mSavedInstanceState.getInt(AppUtils.ARG_SELECTED_POSITION);
                setReceivecomplaintList();
            }
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
            if (mCurrentnoOfRows == 0) {
                mList.clear();
                startIndex = 0;
                if (adapter != null) adapter.notifyDataSetChanged();
                showLoadingReceiveComplaint();
            }
            if (checkInternet(getContext())) {
                isLoading = true;
                new PPMScheduleDetailsService(mActivity, this).getppmListData(startIndex, fetchRequest);
            } else showEmptyView(getString(R.string.lbl_alert_network_not_available));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setReceivecomplaintList() {
        try {
            if (mList != null && mList.size() > 0) {
                Log.w(MODULE, TAG + " mList : " + mList.size());
                adapter.notifyDataSetChanged();
                tv_building.setText(mList.get(0).getWorkOrderNo()+" - "+mList.get(0).getZoneBuilding());
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
        if (btn_custmsign != null) btn_custmsign.setVisibility(View.VISIBLE);
        if (recyclerView != null) recyclerView.setVisibility(View.VISIBLE);
        if (layout_loading != null) layout_loading.setVisibility(View.GONE);
        if (layout_empty != null) layout_empty.setVisibility(View.GONE);
    }

    public void showLoadingReceiveComplaint() {
        try {
            if (layout_empty != null) layout_empty.setVisibility(View.GONE);
            if (footer_container != null) footer_container.setVisibility(View.GONE);
            if (layout_loading != null) layout_loading.setVisibility(View.VISIBLE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
            if (btn_custmsign != null) btn_custmsign.setVisibility(View.GONE);
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
            if (layout_loading != null) layout_loading.setVisibility(View.GONE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onppmListReceived(
            List<PpmScheduleDetails> receiveComplaintItemList, String noOfRows, int from) {
        try {
            Log.d(
                    TAG, mCurrentnoOfRows + " onReceiveComplaintListReceived totalnoOfRows " + totalnoOfRows);
            isLoading = false;
            if (mList != null) {
                mCurrentnoOfRows = mList.size();
            } else {
                mList = new ArrayList<>();
            }
            if (noOfRows != null) totalnoOfRows = Integer.valueOf(noOfRows);
            if (receiveComplaintItemList.size() > 0) {
                mList.addAll(receiveComplaintItemList);

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
                ppmScheduleDocByList =new ArrayList<>();
                PpmScheduleDocBy ppmScheduleDocBy1=null;
                for(int i=0;i<receiveComplaintItemList.size();i++){
                    ppmScheduleDocBy1=new PpmScheduleDocBy();
                    ppmScheduleDocBy1.setPpmNo(receiveComplaintItemList.get(i).getPpmRefNo());
                    ppmScheduleDocByList.add(ppmScheduleDocBy1);
                }
                ppmScheduleDocBy=new PpmScheduleDocBy();
                ppmScheduleDocBy.setCompanyCode(receiveComplaintItemList.get(0).getCompCode());
                ppmScheduleDocBy.setPpmnoList(ppmScheduleDocByList);
            } else Log.d(TAG, "onComplaintReceivedSuccess data Not found");

            Log.d(TAG, mCurrentnoOfRows + " onComplaintReceivedSuccess totalnoOfRows " + totalnoOfRows);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onppmListReceivedError(String Str_Msg, int mode) {
        TAG = "onReceivecomplaintListReceivedError";
        Log.w(MODULE, TAG);
        try {
            AppUtils.hideProgressDialog();
            if (mList.size() > 0) {
                AppUtils.showDialog(mActivity, Str_Msg);
            } else {
                showEmptyView(Str_Msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onppmListItemClicked(List<PpmScheduleDetails> ppmDetailsList, int position) {
        TAG = "onReceivecomplaintListItemClicked";
        Log.w(MODULE, TAG);
        try {
            if (ppmDetailsList.get(position) != null) {
                PpmScheduleDocByRequest request = new PpmScheduleDocByRequest();
                request.setEquipmentCode(ppmDetailsList.get(position).getAssetCode());
                request.setCompCode(ppmDetailsList.get(position).getCompCode());
                request.setWorkOrderNo(ppmDetailsList.get(position).getWorkOrderNo());
                request.setContractNo(ppmDetailsList.get(position).getContractNo());
                request.setNatureCode(ppmDetailsList.get(position).getNatureCode());
                request.setPpmRefNo(ppmDetailsList.get(position).getPpmRefNo());
                gotoFragmentReceiveComplaintView(request);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoFragmentReceiveComplaintView(PpmScheduleDocByRequest ppmScheduleDocByRequest) {
        TAG = "gotoFragmentReceiveComplaintView";
        Log.w(MODULE, TAG);
        try {
            mSavedInstanceState = getSavedState();
            Bundle mdata = new Bundle();
            mdata.putParcelable(AppUtils.ARGS_PPMSCHEDULEDOCBYREQUEST, ppmScheduleDocByRequest);
            Fragment_BeforePPM fragment = new Fragment_BeforePPM();
            fragment.setArguments(mdata);
            FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            fragmentTransaction.replace(R.id.frame_container, fragment, Utils.TAG_PM_PPMDETAILS_VIEW);
            fragmentTransaction.addToBackStack(Utils.TAG_PM_PPMDETAILS_VIEW);
            fragmentTransaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoRateService() {
        TAG = "gotoFragmentReceiveComplaintView";
        Log.w(MODULE, TAG);
        showProgressDialog(mActivity, "Loading...", false);
        SaveRatedServiceRequest fetchPpmScheduleDocBy = new SaveRatedServiceRequest();
        fetchPpmScheduleDocBy.setCompCode(ppmDetails.getCompCode());
        fetchPpmScheduleDocBy.setWorkOrderNo(ppmDetails.getWorkOrderNo());
        fetchPpmScheduleDocBy.setContractNo(ppmDetails.getContractNo());
        new CheckCustomerSignRepository(mActivity, this).checkCustomerSign(fetchPpmScheduleDocBy);
    /*try {
      mSavedInstanceState = getSavedState();
      Bundle mdata = new Bundle();
      mdata.putParcelable(AppUtils.ARGS_PPMDetails_List, ppmDetails);
      Fragment_PM_RateService fragment = new Fragment_PM_RateService();
      fragment.setArguments(mdata);
      FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.frame_container, fragment, Utils.TAG_PM_PPMDETAILS_VIEW);
      fragmentTransaction.addToBackStack(Utils.TAG_PM_PPMDETAILS_VIEW);
      fragmentTransaction.commit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }*/
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
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                Fragment _fragment = new MainLandingUI();
                FragmentTransaction _transaction = mManager.beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TAG = "onSaveInstanceState";
        Log.d(MODULE, TAG);
        mSavedInstanceState = getSavedStateOnPause();
    }

    public Bundle getSavedStateOnPause() {
        Bundle outState = new Bundle();
        TAG = "getSavedStateOnPause";
        outState.putInt(AppUtils.ARG_SELECTED_POSITION, mSelectedPosition);
        outState.putParcelableArrayList(AppUtils.ARG_RC_LIST, (ArrayList<PpmScheduleDetails>) mList);
        Log.d(MODULE, TAG);
        return outState;
    }

    public Bundle getSavedState() {
        Bundle outState = new Bundle();
        TAG = "getSavedState";
        outState.putInt(AppUtils.ARG_SELECTED_POSITION, mSelectedPosition);
        outState.putParcelableArrayList(AppUtils.ARG_RC_LIST, (ArrayList<PpmScheduleDetails>) mList);
        Log.d(MODULE, TAG);
        return outState;
    }

    @Override
    public void onCheckSucess(String s) {
        AppUtils.hideProgressDialog();
        if (s.equalsIgnoreCase("True")) {
            try {
                mSavedInstanceState = getSavedState();
                Bundle mdata = new Bundle();
                mdata.putString(AppUtils.ARGS_PPM_FEEDBACK_CHECK, "ppmcheckListdata");
                mdata.putParcelable(AppUtils.ARGS_PPM_FEEDBACK, ppmScheduleDocBy);
                Fragment_PM_RateService fragment = new Fragment_PM_RateService();
                fragment.setArguments(mdata);
                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(R.id.frame_container, fragment, Utils.TAG_PM_PPMDETAILS_VIEW);
                fragmentTransaction.addToBackStack(Utils.TAG_PM_PPMDETAILS_VIEW);
                fragmentTransaction.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onCategoryReceivedFailure(String strErr) {
        AppUtils.hideProgressDialog();
        if(strErr.equalsIgnoreCase("False")){
            AppUtils.showDialog(mActivity, "Please Complete all PPM tasks");
        }

    }
}
