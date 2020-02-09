package com.daemon.emco_android.ui.fragments.preventive.ppmschedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.NestedScrollView;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.PPMListAdapter;
import com.daemon.emco_android.repository.remote.GetSearchComplaintPreventiveService;
import com.daemon.emco_android.repository.remote.PPMDetailsService;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.listeners.SearchComplaintPreventiveListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.PPMDetails;
import com.daemon.emco_android.model.request.PPMCCCReq;
import com.daemon.emco_android.model.request.PPMFilterRequest;
import com.daemon.emco_android.model.request.SearchSingleComplaintPreventiveRequest;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Fragment_PM_PPMDetails_List extends Fragment implements PPMDetailsService.Listener,
        SearchView.OnQueryTextListener,SearchComplaintPreventiveListener
,MenuItem.OnMenuItemClickListener{
  private static String MODULE = "F_RComplaintsList";
  private static String TAG = "";
  private Bundle mSavedInstanceState;
  private Bundle mArgs;
  private Font font = App.getInstance().getFontInstance();
  private AppCompatActivity mActivity;
  private Toolbar mToolbar;
  private TextView text_view_message, text_view_empty, tv_toolbar_title;
  private ImageView img_toolbar;
  private FragmentManager mManager;
  private CoordinatorLayout cl_main;
  private LinearLayoutManager mLayoutManager;
  private TextView text_view_loading_message;
  private LinearLayout layout_loading_message;
  private Button btn_reassign_ccc;
  private List<PPMDetails> temp = new ArrayList<>();
  private CheckBox cb_checkAll;
  private LinearLayout layout_loading;
  private RelativeLayout layout_empty, footer_container;
  private RecyclerView recyclerView;
  private SearchView searchView;
  private TextView tv_building,
      tv_completed,
      tv_proposed_start_date,
      tv_proposed_end_date,
      tv_pending,
      tv_totla_ppm,
      tv_asset_name;

  int scrollDown=0;

  boolean searchEnabled=false;

  NestedScrollView scroller;
  int scrollCount=1;

  private int totalnoOfRows = 0;
  private int mCurrentnoOfRows = 0;
  private boolean isLastPage = true;
  private boolean isLoading = false;
  // this is to keep count for first time loading dialog
  private boolean isLoadingNew = false;
  private int mScrollPosition = 0;
  private int mSelectedPosition = 0;

  private PPMListAdapter adapter;
  private List<PPMDetails> mList = new ArrayList<>();
  private SharedPreferences mPreferences;
  private String mStrEmpId = null;
  private String mLoginData = null;
  private List<PPMDetails> data_checked;
  PPMFilterRequest ppmfilter;
  View.OnClickListener _OnClickListener =
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          switch (view.getId()) {
            case R.id.btn_reassign_ccc:
              List<PPMCCCReq> data_ccc_requrest = new ArrayList<>();
              if (data_checked != null) {
                for (int i = 0; i < data_checked.size(); i++) {
                  PPMDetails entity = data_checked.get(i);
                  if (entity.isChecked()) {
                    data_ccc_requrest.add(
                        new PPMCCCReq(
                            entity.getCompCode(), entity.getContractNo(), entity.getWorkOrderNo()));
                  }
                }
              }
              if (data_ccc_requrest.size() > 0) {
                saveReceiveComplaintCCformService(data_ccc_requrest);
              } else AppUtils.showDialog(mActivity, "Please Select Data to Re-Assigning to CCC");
              break;

            default:
              break;
          }
        }
      };
  private CompoundButton.OnCheckedChangeListener checkedChangeListener =
      new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
          try {
            if (mList != null && adapter != null && !mList.isEmpty()) {
              adapter.checkAll(b);
            } else Log.d(TAG, "onCheckedChanged empty");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };

  @Override
  public void onppmListItemChecked(List<PPMDetails> data_checked) {
    this.data_checked = data_checked;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      TAG = "onCreate";
      Log.d(MODULE, TAG);
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
      if(mArgs==null){

        String ppmfil = mPreferences.getString(AppUtils.SHARED_PPM_FILTER, null);
        Gson gson = new Gson();
        ppmfilter = gson.fromJson(ppmfil, PPMFilterRequest.class);

      }
      else{

        ppmfilter= (PPMFilterRequest) mArgs.getSerializable(AppUtils.ARGS_PPMFILTER);
        Gson gson = new Gson();
        String ppmfil = gson.toJson(ppmfilter);
        SharedPreferences.Editor mEditor = mPreferences.edit();
        mEditor.putString(AppUtils.SHARED_PPM_FILTER, ppmfil);
        mEditor.commit();
      }
      ArrayList<Integer> frags=new ArrayList<>();
      FragmentManager fm = getFragmentManager();

//      for(int entry = 0; entry < fm.getBackStackEntryCount(); entry++){
//        Log.i(TAG, "Found fragment: " + fm.getBackStackEntryAt(entry).getId());
//          frags.add( fm.getBackStackEntryAt(entry).getId());
//      }
//      if(frags.size()>2){
//        for(int i=0;i<frags.size()-2;i++)
//         getFragmentManager().popBackStack();
//      }

      mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
      if (mLoginData != null) {
        Gson gson = new Gson();
        Login login = gson.fromJson(mLoginData, Login.class);
        mStrEmpId = login.getEmployeeId();
      }
      getReceiveComplainFromService(mCurrentnoOfRows);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_preventive_complaint_list, container, false);
    TAG = "onCreateView";
    Log.d(MODULE, TAG);
    initView(rootView);
    setProperties();
    setupActionBar();
    return rootView;
  }

  public void initView(View view) {
    TAG = "initView";
    Log.d(MODULE, TAG);
    try {
      cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
      recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
      cb_checkAll = (CheckBox) view.findViewById(R.id.cb_checkall);
      searchView = (SearchView) view.findViewById(R.id.sv_rc_search);
      searchView.setOnQueryTextListener(this);
      tv_building = (TextView) view.findViewById(R.id.tv_building);
      tv_asset_name = (TextView) view.findViewById(R.id.tv_asset_name);
      tv_proposed_start_date = (TextView) view.findViewById(R.id.tv_start_date);
      tv_proposed_end_date = (TextView) view.findViewById(R.id.tv_end_date);
      tv_completed = (TextView) view.findViewById(R.id.tv_completed);
      tv_pending = (TextView) view.findViewById(R.id.tv_pending);
      tv_totla_ppm = (TextView) view.findViewById(R.id.tv_total_ppm);
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
    tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
    img_toolbar = (ImageView) mToolbar.findViewById(R.id.img_toolbar);
    tv_toolbar_title.setText(getString(R.string.lbl_ppm_details));
    // mToolbar.setTitle(getResources().getString(R.string.lbl_ppm_details));
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

  private void saveReceiveComplaintCCformService(List<PPMCCCReq> data_ccc_requrest) {
    TAG = "saveReceiveComplaintCCformService";
    Log.d(MODULE, TAG);
    try {
      if (mPreferences
          .getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE)
          .contains(AppUtils.NETWORK_AVAILABLE)) {
        AppUtils.showProgressDialog(mActivity, getString(R.string.re_assigning_to_cc), false);
        new PPMDetailsService(mActivity, this).postppmCCCData(data_ccc_requrest);
      } else showEmptyView(getString(R.string.lbl_alert_network_not_available));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setProperties() {
    TAG = "setProperties";
    Log.d(MODULE, TAG);
    try {
      setManager();
      searchView.setIconifiedByDefault(true);
      searchView.setOnQueryTextListener(this);
      tv_totla_ppm.setTypeface(font.getHelveticaRegular());
      tv_building.setTypeface(font.getHelveticaRegular());
      tv_asset_name.setTypeface(font.getHelveticaRegular());
      tv_proposed_start_date.setTypeface(font.getHelveticaRegular());
      tv_proposed_end_date.setTypeface(font.getHelveticaRegular());
      tv_completed.setTypeface(font.getHelveticaRegular());
      tv_pending.setTypeface(font.getHelveticaRegular());
      text_view_empty.setTypeface(font.getHelveticaRegular());
      text_view_message.setTypeface(font.getHelveticaRegular());
      btn_reassign_ccc.setTypeface(font.getHelveticaRegular());
      text_view_loading_message.setTypeface(font.getHelveticaRegular());
      cb_checkAll.setOnCheckedChangeListener(checkedChangeListener);
      btn_reassign_ccc.setOnClickListener(_OnClickListener);

      recyclerView.addOnScrollListener(
          new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
              super.onScrollStateChanged(recyclerView, newState);


              LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
              int total = lm.getItemCount();
              int firstVisibleItemCount = lm.findFirstVisibleItemPosition();
              int lastVisibleItemCount = lm.findLastVisibleItemPosition();

              //to avoid multiple calls to loadMore() method
              //maintain a boolean value (isLoading). if loadMore() task started set to true and completes set to false

                if (total > 0)
                  if ((total - 1) == lastVisibleItemCount){

                    Log.i("status","Load more");

                    if(!searchEnabled){

                        Log.d(TAG, "onScrolled " + isLastPage + isLoading);
                        if (isLoading) return;
                        if (mList.isEmpty()) return;

                        // End of list
                        if (!isLastPage) {
                          scrollCount++;

                          if (layout_empty != null) layout_empty.setVisibility(View.GONE);
                          if (footer_container != null) footer_container.setVisibility(View.GONE);
                          if (layout_loading != null) layout_loading.setVisibility(View.VISIBLE);

                          getReceiveComplainFromService(mCurrentnoOfRows);
                          scrollDown=0;
                        }
                    }

                  }else{

                  }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
              super.onScrolled(recyclerView, dx, dy);

            }
          });

      adapter = new PPMListAdapter(mActivity, mList);
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
    TAG = "setManager";
    Log.d(MODULE, TAG);
    try {
      mLayoutManager = new LinearLayoutManager(mActivity);
      recyclerView.setLayoutManager(mLayoutManager);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getReceiveComplainFromService(int startIndex) {
    TAG = "getReceiveComplainFromService";
    Log.d(MODULE, TAG);
    try {
      if (mCurrentnoOfRows == 0) {
        mList.clear();
        startIndex = 0;
        if (adapter != null) adapter.notifyDataSetChanged();
        showLoadingReceiveComplaint();
      }
      if (mPreferences
          .getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE)
          .contains(AppUtils.NETWORK_AVAILABLE)) {
        Log.d(MODULE, "getReceivecomplaintListFromServer");
        isLoading = true;

        if(startIndex==0){
         // AppUtils.showProgressDialog2(mActivity,"Loading",true);
          isLoadingNew=true;
        }

        new PPMDetailsService(mActivity, this).getppmListData(mStrEmpId, startIndex,ppmfilter);
      } else showEmptyView(getString(R.string.lbl_alert_network_not_available));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setReceivecomplaintList() {
    TAG = "setPpeNameList";
    Log.w(MODULE, TAG);
    try {
      if (mList != null && mList.size() > 0) {
        Log.w(MODULE, TAG + " mList : " + mList.size());

//        adapter = new PPMListAdapter(mActivity, mList);
//        adapter.setListener(this);
//        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
     //   recyclerView.setScrollY(mScrollPosition);
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
    if (btn_reassign_ccc != null) btn_reassign_ccc.setVisibility(View.VISIBLE);
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
      if (btn_reassign_ccc != null) btn_reassign_ccc.setVisibility(View.GONE);
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
      List<PPMDetails> receiveComplaintItemList, String noOfRows, int from) {

    if(isLoadingNew==true){
    //  AppUtils.hideProgressDialog();
      isLoadingNew=false;
    }

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
  public void onppmListItemClicked(List<PPMDetails> temp, int position) {
    TAG = "onReceivecomplaintListItemClicked";
    Log.w(MODULE, TAG);
    try {
      if (mList.get(position).getCompCode() != null) {
        gotoFragmentReceiveComplaintView(mList, position);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void onppmCCCReceived(String strMsg, int from) {
    try {
      AppUtils.hideProgressDialog();
      AppUtils.showDialog(mActivity, strMsg);
      mCurrentnoOfRows = 0;
      getReceiveComplainFromService(mCurrentnoOfRows);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void gotoFragmentReceiveComplaintView(List<PPMDetails> data, int position) {
    TAG = "gotoFragmentReceiveComplaintView";
    Log.w(MODULE, TAG);

    try {
      mSavedInstanceState = getSavedState();
      Bundle mdata = new Bundle();
      mdata.putParcelable(AppUtils.ARGS_PPMDetails_List, data.get(position));
      Fragment_PPMDetails_List fragment = new Fragment_PPMDetails_List();
      fragment.setArguments(mdata);
      FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
      fragmentTransaction.add(R.id.frame_container, fragment, Utils.TAG_PM_PPMDETAILS_VIEW);
      fragmentTransaction.hide(getFragmentManager().findFragmentByTag(Utils.TAG_PM_PPMDETAILS_LIST));
      fragmentTransaction.addToBackStack(Utils.TAG_PM_PPMDETAILS_VIEW);
      fragmentTransaction.commit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    Log.d(TAG, "onPrepareOptionsMenu ");
    menu.findItem(R.id.action_logout).setVisible(false);
    menu.findItem(R.id.action_home).setVisible(true);
    MenuItem searchItem = menu.findItem(R.id.action_search).setVisible(true);
    MenuItem refreshItem = menu.findItem(R.id.action_refresh).setVisible(true);
    searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
    searchView.setQueryHint(getString(R.string.lbl_rc_search_ppm));
    searchView.setMaxWidth(Integer.MAX_VALUE);
    searchView.setOnQueryTextListener(this);

    refreshItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {

        mCurrentnoOfRows=0;
        getReceiveComplainFromService(mCurrentnoOfRows);
        scrollCount=1;

        return false;
      }
    });

    searchView.setOnCloseListener(new SearchView.OnCloseListener() {

      @Override
      public boolean onClose() {

        img_toolbar.setVisibility(View.VISIBLE);

        if(searchEnabled){
          searchEnabled=false;
          mCurrentnoOfRows=0;
          getReceiveComplainFromService(mCurrentnoOfRows);
          scrollCount=1;
        }

        return false;
      }
    });
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
    outState.putParcelableArrayList(AppUtils.ARG_RC_LIST, (ArrayList<PPMDetails>) mList);
    Log.d(MODULE, TAG);
    return outState;
  }

  public Bundle getSavedState() {
    Bundle outState = new Bundle();
    TAG = "getSavedState";
    outState.putInt(AppUtils.ARG_SELECTED_POSITION, mSelectedPosition);
    outState.putParcelableArrayList(AppUtils.ARG_RC_LIST, (ArrayList<PPMDetails>) mList);
    Log.d(MODULE, TAG);
    return outState;
  }

  @Override
  public boolean onQueryTextSubmit(String query) {

    closeKeyboard();

    img_toolbar.setVisibility(View.GONE);

    if(query.trim().length()>2){

      searchEnabled=true;

   //   scroller.scrollTo(0, 0);

      AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
      SearchSingleComplaintPreventiveRequest searchSingleComplaintPreventiveRequest=new SearchSingleComplaintPreventiveRequest(query,ppmfilter.getContractNo());
      new GetSearchComplaintPreventiveService(mActivity, this, this)
              .getSingleSearchPreventiiveData(searchSingleComplaintPreventiveRequest,mStrEmpId);
    }
    else{

      AppUtils.showDialog(getContext(),"Search query text should be greater than 2-digit.");

    }

    return false;
  }



  @Override
  public boolean onQueryTextChange(String newText) {
    //filter(newText);
  /*  AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
    SearchSingleComplaintPreventiveRequest searchSingleComplaintPreventiveRequest=new SearchSingleComplaintPreventiveRequest(newText);
    new GetSearchComplaintPreventiveService(mActivity, this, this)
            .getSingleSearchPreventiiveData(searchSingleComplaintPreventiveRequest);*/
    return false;
  }

  void filter(String text) {
    Log.d(TAG, "filter");
    try {
      if (mList != null && !mList.isEmpty()) {
        if (temp != null) {
          temp = new ArrayList<>();
        } else {
          temp.clear();
        }
        for (PPMDetails d : mList) {
          // or use .equal(text) with you want equal match
          // use .toLowerCase() for better matches
        //  if (d.getCompCode().toLowerCase().contains(text.toLowerCase())) {
            temp.add(d);
         // }
        }
        // update recyclerview
        adapter.updateList(temp);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean onMenuItemClick(MenuItem menuItem) {
    if (isLoading) return false;
    getReceiveComplainFromService(0);
    return false;
  }

  @Override
  public void onComplaintReceivedSuccess(List<PPMDetails> searchComplaintEntityList) {

    AppUtils.hideProgressDialog();

    if (searchComplaintEntityList.size() > 0) {
      mList.clear();
      mList.addAll(searchComplaintEntityList);
      filter("");
      //showList();
      adapter.enableFooter(false);
     // adapter.enableFooter(false);
    }
  }

  @Override
  public void onComplaintReceivedFailure(String strErr) {

    AppUtils.hideProgressDialog();
    AppUtils.showDialog(mActivity,strErr);
  }


  public void closeKeyboard(){
    searchView.clearFocus();
  }



}
