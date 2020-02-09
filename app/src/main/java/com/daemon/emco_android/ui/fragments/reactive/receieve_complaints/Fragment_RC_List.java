package com.daemon.emco_android.ui.fragments.reactive.receieve_complaints;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import androidx.annotation.NonNull;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.ReceiveComplaintListService;
import com.daemon.emco_android.ui.activities.LoginActivity;
import com.daemon.emco_android.ui.adapter.ReceivecomplaintListAdapter;
import com.daemon.emco_android.repository.db.dbhelper.ReceiveComplaintItemDbInitializer;
import com.daemon.emco_android.repository.db.entity.ComplaintStatusEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.listeners.ReceivecomplaintList_Listener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.ReceiveComplainCCRequest;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ConnectivityStatus;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_VIEW;

/** Created by Daemonsoft on 7/18/2017. */
// thirumal
public class Fragment_RC_List extends Fragment
    implements ReceivecomplaintList_Listener,
        SearchView.OnQueryTextListener,
        MenuItem.OnMenuItemClickListener {
  private static String TAG = Fragment_RC_List.class.getSimpleName();
  final Handler handler = new Handler();
  List<ReceiveComplaintItemEntity> temp = new ArrayList<>();
  private Bundle mSavedInstanceState;
  private Bundle mArgs;
  private Font font = App.getInstance().getFontInstance();
  private AppCompatActivity mActivity;
  private Toolbar mToolbar;
  private FragmentManager mManager;
  private CoordinatorLayout cl_main;
  private LinearLayoutManager mLayoutManager;
  private TextView text_view_loading_message, tv_toolbar_title;
  private ImageView img_toolbar;
  private LinearLayout layout_loading_message;
  private TextView text_view_message, text_view_empty;
  private Button btn_reassign_ccc;
  private LinearLayout layout_loading;
  private RelativeLayout layout_empty, footer_container;
  private SearchView searchView;
  private RecyclerView recyclerView;
  FabSpeedDial fabSpeedDial;
  private TextView tv_complaint_no,
      tv_site_location,
      tv_complaint_details,
      tv_time,
      tv_status,
      tv_work_type,
      tv_priority,
      tv_customer_ref;
  private CheckBox cb_checkAll;
  private int totalnoOfRows = 0;
  private int mCurrentnoOfRows = 0;
  private boolean isLastPage = true;
  private boolean isLoading = false;
  private int mScrollPosition = 0;
  private ComplaintStatusEntity complaintStatusEntity;
  private TimerTask mTimerTask;
  private Timer t = new Timer();

  private int mSelectedPosition = 0;
  private SharedPreferences.Editor mEditor;
  private boolean isNavigated=false;
  private ReceiveComplaintListService receiveComplaintListService;
  private ReceivecomplaintListAdapter adapter;
  private List<ReceiveComplaintItemEntity> mList = new ArrayList<>();
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
  private String mUnSignedPage = AppUtils.ARGS_RECEIVECOMPLAINT_PAGE;
  private boolean ARGS_RCLIST_F = false;
  private boolean ARGS_RCLIST_C = false;
  private SharedPreferences mPreferences;
  private Login user;
  private String mStrEmpId = null;
  private String mLoginData = null;
  private List<ReceiveComplaintItemEntity> data_checked;

  private View.OnClickListener _OnClickListener =
      new View.OnClickListener() {
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
                      data_ccc_requrest.add(
                          new ReceiveComplainCCRequest(
                              entity.getComplaintNumber(), entity.getOpco(), entity.getSiteCode()));
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
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };
  private RecyclerView.OnScrollListener onScrollListener =
      new RecyclerView.OnScrollListener() {
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
              // End of list
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

  public void timeout() {
    mTimerTask =
        new TimerTask() {
          public void run() {
            handler.post(
                new Runnable() {
                  public void run() {
                    MaterialDialog.Builder builder =
                        new MaterialDialog.Builder(mActivity)
                            .content("Your session has been expired!Please login to continue!!! ")
                            .title("You've timed out")
                            .positiveText(R.string.lbl_okay)
                            .stackingBehavior(StackingBehavior.ADAPTIVE)
                            .onPositive(
                                new MaterialDialog.SingleButtonCallback() {
                                  @Override
                                  public void onClick(
                                      @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    clearPreferences();
                                    Intent intent = new Intent(mActivity, LoginActivity.class);
                                    mActivity.startActivity(intent);
                                    mActivity.finish();
                                  }
                                });
                    MaterialDialog dialog = builder.build();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.show();
                  }
                });
          }
        };
    // public void schedule (TimerTask task, long delay, long period)
    t.scheduleAtFixedRate(mTimerTask, AppUtils.TIMEOUT_NEW, AppUtils.TIMEOUT);
  }

  public void clearPreferences() {
    Log.d(TAG, "clearPreferences");
    try {
      mEditor = mPreferences.edit();
      mEditor
          .putString(
              AppUtils.SHARED_LOGIN_OFFLINE, mPreferences.getString(AppUtils.SHARED_LOGIN, null))
          .commit();
      mEditor.putString(AppUtils.SHARED_LOGIN, null).commit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

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
      receiveComplaintListService = new ReceiveComplaintListService(mActivity, this);
      mArgs = getArguments();
      mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
      mEditor = mPreferences.edit();
      mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
      if (mLoginData != null) {
        Gson gson = new Gson();
        user = gson.fromJson(mLoginData, Login.class);
        mStrEmpId = user.getEmployeeId();
        if (user.getUserType().equals(AppUtils.TECHNISION)
            || user.getUserType().equals(AppUtils.SUPERVISOR)) {
          showNewMsg();
        }
      }
      if (mArgs != null) {
        mUnSignedPage = mArgs.getString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE);
        ARGS_RCLIST_C = mArgs.getBoolean(AppUtils.ARGS_RCLIST_C, false);
        getReceiveComplainFromService(mCurrentnoOfRows);
      }



    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_receivecomplaint_list, container, false);
    Log.d(TAG, "onCreateView");
    initView(rootView);
    setProperties();
    setupActionBar();
    return rootView;
  }

  public void initView(View view) {
    Log.d(TAG, "initView");
    try {
      cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
      AppUtils.closeInput(cl_main);

      searchView = (SearchView) view.findViewById(R.id.sv_rc_search);


      cb_checkAll = (CheckBox) view.findViewById(R.id.cb_checkall);

      recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
      tv_complaint_no = (TextView) view.findViewById(R.id.tv_complaint_no);
      tv_time = (TextView) view.findViewById(R.id.tv_date);
      tv_work_type = (TextView) view.findViewById(R.id.tv_work_type);
      tv_priority = (TextView) view.findViewById(R.id.tv_priority);
      tv_site_location = (TextView) view.findViewById(R.id.tv_site_location);
      tv_complaint_details = (TextView) view.findViewById(R.id.tv_complaint_details);
      tv_status = (TextView) view.findViewById(R.id.tv_status);
      tv_customer_ref = (TextView) view.findViewById(R.id.tv_customer_ref);

      text_view_loading_message = (TextView) view.findViewById(R.id.text_view_message);
      layout_loading_message = (LinearLayout) view.findViewById(R.id.layout_loading);

      layout_loading = (LinearLayout) view.findViewById(R.id.layout_loading);
      layout_empty = (RelativeLayout) view.findViewById(R.id.layout_empty);
      footer_container = (RelativeLayout) view.findViewById(R.id.footer_container);
      text_view_empty = (TextView) view.findViewById(R.id.text_view_empty);
      text_view_message = (TextView) view.findViewById(R.id.text_view_message);

    //  btn_reassign_ccc = (Button) view.findViewById(R.id.btn_reassign_ccc);
       fabSpeedDial = (FabSpeedDial) view.findViewById(R.id.fab_menu);

       if (ARGS_RCLIST_C){
         fabSpeedDial.setVisibility(View.GONE);
       }

       fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
        @Override
        public boolean onMenuItemSelected(MenuItem menuItem) {
          //TODO: Start some activity
          switch(menuItem.getItemId()){

            case R.id.action_assign:

              List<ReceiveComplainCCRequest> data_ccc_requrest = new ArrayList<>();
              if (data_checked != null) {
                for (int i = 0; i < data_checked.size(); i++) {
                  ReceiveComplaintItemEntity entity = data_checked.get(i);

                  if (entity.isChecked()) {
                    data_ccc_requrest.add(
                            new ReceiveComplainCCRequest(
                                    entity.getComplaintNumber(), entity.getOpco(), entity.getSiteCode()));
                  }
                }
              }
              if (data_ccc_requrest.size() > 0) {
                saveReceiveComplaintCCformService(data_ccc_requrest);
              } else AppUtils.showDialog(mActivity, "Please Select Data to Re-Assigning to CCC");

              break;

            case R.id.action_completed:

              Bundle data = new Bundle();
              data.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, AppUtils.ARGS_RECEIVECOMPLAINT_PAGE);
              data.putBoolean(AppUtils.ARGS_RCLIST_C, true);
              Fragment_RC_List complaintsList = new Fragment_RC_List();
              complaintsList.setArguments(data);
              isNavigated=true;
              loadFragment(complaintsList, Utils.TAG_RECEIVED_COMPALINTS);

              break;
          }
          return false;
        }
      });

      if (mList != null && mList.isEmpty()) {

          showLoadingReceiveComplaint();
      }

      if(isNavigated){
        if (mList != null && mList.isEmpty()) {

          try {
            if (text_view_empty != null) {
              text_view_empty.setText("No Complaint have been assigned");
            }
            if (layout_empty != null) layout_empty.setVisibility(View.VISIBLE);
            if (footer_container != null) footer_container.setVisibility(View.GONE);
            if (layout_loading != null) layout_loading.setVisibility(View.GONE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void loadFragment(final Fragment fragment, final String tag) {
    Log.d(TAG, "loadFragment");
    // update the main content by replacing fragments
    FragmentTransaction fragmentTransaction =
            mActivity.getSupportFragmentManager().beginTransaction();
    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
    fragmentTransaction.replace(R.id.frame_container, fragment, tag);
    fragmentTransaction.addToBackStack(tag);
    fragmentTransaction.commit();
  }

  public void setupActionBar() {
    mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
    tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
    img_toolbar = (ImageView) mToolbar.findViewById(R.id.img_toolbar);
    tv_toolbar_title.setText(
        getResources()
            .getString(
                mUnSignedPage == AppUtils.ARGS_RECEIVECOMPLAINT_PAGE
                    ? R.string.lbl_receive_complaints
                    : R.string.lbl_pending_client_signature));
    /*mToolbar.setTitle(
    getResources()
        .getString(
            mUnSignedPage == AppUtils.ARGS_RECEIVECOMPLAINT_PAGE
                ? R.string.lbl_receive_complaints
                : R.string.lbl_pending_client_signature));*/
    mActivity.setSupportActionBar(mToolbar);
    mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    mToolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
         //   mActivity.onBackPressed();

            mManager.popBackStack();

          }
        });
  }

  private void saveReceiveComplaintCCformService(List<ReceiveComplainCCRequest> data_ccc_requrest) {
    Log.d(TAG, "saveReceiveComplaintCCformService");
    try {
      if (mPreferences
          .getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE)
          .contains(AppUtils.NETWORK_AVAILABLE)) {
        AppUtils.showProgressDialog(mActivity, getString(R.string.re_assigning_to_cc), false);
        receiveComplaintListService.PostReceiveComplaintCCData(data_ccc_requrest);
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

      tv_complaint_no.setTypeface(font.getHelveticaRegular());
      tv_time.setTypeface(font.getHelveticaRegular());
      tv_work_type.setTypeface(font.getHelveticaRegular());
      tv_priority.setTypeface(font.getHelveticaRegular());
      tv_site_location.setTypeface(font.getHelveticaRegular());
      tv_complaint_details.setTypeface(font.getHelveticaRegular());
      tv_status.setTypeface(font.getHelveticaRegular());
      tv_customer_ref.setTypeface(font.getHelveticaRegular());

      text_view_empty.setTypeface(font.getHelveticaRegular());
      text_view_message.setTypeface(font.getHelveticaRegular());
   //   btn_reassign_ccc.setTypeface(font.getHelveticaRegular());

      text_view_loading_message.setTypeface(font.getHelveticaRegular());

      adapter = new ReceivecomplaintListAdapter(mActivity, mList);
      adapter.setListener(this);
      adapter.setmUnSignedPage(mUnSignedPage);
      recyclerView.setAdapter(adapter);

      if (mUnSignedPage == AppUtils.ARGS_RECEIVECOMPLAINT_PAGE) {
        cb_checkAll.setVisibility(View.VISIBLE);
        cb_checkAll.setOnCheckedChangeListener(checkedChangeListener);
      } else {
        cb_checkAll.setVisibility(View.GONE);
      }
    //  btn_reassign_ccc.setOnClickListener(_OnClickListener);
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
    Log.d(TAG, "setManager");
    try {
      mLayoutManager = new LinearLayoutManager(mActivity);
      recyclerView.setLayoutManager(mLayoutManager);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getReceiveComplainFromService(int startIndex) {
    Log.d(TAG, "getReceiveComplainFromService");
    try {
      if (isLoading) return;
      if (startIndex == 0) {
        mList.clear();
        mCurrentnoOfRows = 0;
        if (adapter != null) adapter.notifyDataSetChanged();
        showLoadingReceiveComplaint();
      }
      if (mPreferences
          .getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE)
          .contains(AppUtils.NETWORK_AVAILABLE)) {
        isLoading = true;
        receiveComplaintListService.GetReceiveComplaintListData(
            mStrEmpId, ARGS_RCLIST_C ? "C" : "A", startIndex, mUnSignedPage);
      } else {
        if (mUnSignedPage == AppUtils.ARGS_RECEIVECOMPLAINT_PAGE) {
          new ReceiveComplaintItemDbInitializer(mActivity, mCurrentnoOfRows, this)
              .execute(AppUtils.MODE_GET);
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
  public void onReceiveComplaintListReceived(
      List<ReceiveComplaintItemEntity> receiveComplaintItemList, String noOfRows, int from) {
    try {

      /* if (receiveComplaintItemList != null) {
        showMsg(noOfRows);
        return;
      }*/

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
          new ReceiveComplaintItemDbInitializer(mActivity, mList, this)
              .execute(isLastPage ? AppUtils.MODE_INSERT : AppUtils.MODE_INSERT_SINGLE);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void onReceiveComplaintListReceivedUpdate(
      List<ReceiveComplaintItemEntity> receiveComplaintItemList,
      ComplaintStatusEntity complaintStatusEntityne,
      int from) {
    if(!ARGS_RCLIST_C)
    showMsg(complaintStatusEntityne.getNewComplaint(), complaintStatusEntityne.getOldComplaint());
    /* new ReceiveComplaintItemDbInitializer(mActivity, complaintStatusEntityne,this)
    .execute(AppUtils.MODE_INSERT);*/
  }

  private void showMsg(String newcomplaint, String pendingComplaint) {
    Log.d(TAG, "showMsg " + newcomplaint);
    if (newcomplaint != null && pendingComplaint != null) {
      int n1 = Integer.parseInt(newcomplaint);
      int n2 = Integer.parseInt(pendingComplaint);
      int addValues = n1 + n2;
      String StrMsg;
      if (Integer.valueOf(newcomplaint) == 0 && Integer.valueOf(pendingComplaint) != 0) {
        StrMsg =
            "Welcome "
                + user.getFirstName()
                + " "
                + user.getLastName()
                + ",\nYou have no new complaints, "
                + "\n"
                + "Pending complaints :"
                + " "
                + pendingComplaint
                + ","
                + "\nTotal complaints :"
                + " "
                + pendingComplaint;

        commonAlert(StrMsg);

      } else if (Integer.valueOf(pendingComplaint) == 0 && Integer.valueOf(newcomplaint) != 0) {
        StrMsg =
            "Welcome "
                + user.getFirstName()
                + " "
                + user.getLastName()
                + ",\nYou have "
                + newcomplaint
                + " new complaints, "
                + "\nno pending complaints "
                + ","
                + "\nTotal complaints : "
                + pendingComplaint;
        commonAlert(StrMsg);

      } else if (Integer.valueOf(newcomplaint) == 0 && Integer.valueOf(pendingComplaint) == 0) {
        StrMsg =
            "Welcome " + user.getFirstName() + " " + user.getLastName() + " You have no complaints";
        commonAlert(StrMsg);

      } else {
        if (Integer.valueOf(newcomplaint) > 1) {
          StrMsg =
              "Welcome "
                  + user.getFirstName()
                  + " "
                  + user.getLastName()
                  + ",\nYou have "
                  + newcomplaint
                  + " new complaints, "
                  + "\n"
                  + pendingComplaint
                  + " pending complaints "
                  + ","
                  + "\nTotal complaints : "
                  + pendingComplaint;

          commonAlert(StrMsg);
        } else {
          StrMsg =
              "Welcome "
                  + user.getFirstName()
                  + " "
                  + user.getLastName()
                  + ",\nYou have "
                  + newcomplaint
                  + " new complaints, "
                  + "\n"
                  + pendingComplaint
                  + " pending complaints "
                  + "."
                  + "\nTotal complaint : "
                  + pendingComplaint;

          commonAlert(StrMsg);
        }
      }
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    PowerManager pm = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
    boolean isScreenOn = pm.isScreenOn();
    if (!isScreenOn) {
      timeout();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    PowerManager pm = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
    boolean isScreenOn = pm.isScreenOn();
    if (isScreenOn) {
      cleartime();
    }
  }

  public void cleartime() {
    if (mTimerTask != null) {
      mTimerTask.cancel();
    }
  }

  private void commonAlert(String alert) {
    MaterialDialog.Builder builder =
        new MaterialDialog.Builder(mActivity)
            .content(alert)
            .positiveText(R.string.lbl_okay)
            .stackingBehavior(StackingBehavior.ADAPTIVE)
            .onPositive(
                new MaterialDialog.SingleButtonCallback() {
                  @Override
                  public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();

                  }
                });

    MaterialDialog dialog = builder.build();
    dialog.show();
  }

  @Override
  public void onReceiveComplaintListReceivedError(String Str_Msg, int mode) {
    Log.d(TAG, "onReceivecomplaintListReceivedError");
    try {
      AppUtils.hideProgressDialog();
      getReceiveComplainFromService(0);
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
  public void onReceiveComplaintListItemClicked(
      List<ReceiveComplaintItemEntity> temp, int position) {
    Log.d(TAG, "onReceivecomplaintListItemClicked");
    try {
      this.temp = temp;
      if (temp != null) {
        if (temp.get(position).getStatus().equalsIgnoreCase("c")) {

          // changed navigation based on Sulaiman request
          gotoFragmentReceiveComplaintView(temp, position);

        } else {
          if (temp.get(position).getComplaintNumber() != null) {
            gotoFragmentReceiveComplaintView(temp, position);
          }
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
      new ReceiveComplaintItemDbInitializer(mActivity, null, this).execute(AppUtils.MODE_DELETE);
      getReceiveComplainFromService(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void gotoFragmentReceiveComplaintView(
      List<ReceiveComplaintItemEntity> data, int position) {
    Log.d(TAG, "gotoFragmentReceiveComplaintView");
    try {
      mSavedInstanceState = getSavedState();
      Bundle mdata = new Bundle();
      mdata.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, mUnSignedPage);
      mdata.putParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS, data.get(position));
      mdata.putInt(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION, position);
      Fragment_RC_View fragment = new Fragment_RC_View();
      fragment.setArguments(mdata);
      fragment.setAdapter(adapter);
      fragment.setRecyclerView(recyclerView);
      FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
      fragmentTransaction.replace(R.id.frame_container, fragment, TAG_RECEIVE_COMPLAINT_VIEW);
      fragmentTransaction.addToBackStack(TAG_RECEIVE_COMPLAINT_VIEW);
      fragmentTransaction.commit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    Log.d(TAG, "onPrepareOptionsMenu ");
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

    searchView.setOnQueryTextListener(this);

    searchView.setOnCloseListener(new SearchView.OnCloseListener() {

      @Override
      public boolean onClose() {
        img_toolbar.setVisibility(View.VISIBLE);
        return false;
      }
    });

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
      //  mActivity.onBackPressed();

        for (int i = 0; i < mManager.getBackStackEntryCount(); ++i) {
          mManager.popBackStack();
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
    mSavedInstanceState = getSavedStateOnPause();
  }

  public Bundle getSavedStateOnPause() {
    Bundle outState = new Bundle();
    outState.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, mUnSignedPage);
    outState.putInt(AppUtils.ARG_SELECTED_POSITION, mSelectedPosition);
    outState.putParcelableArrayList(
        AppUtils.ARG_RC_LIST, (ArrayList<ReceiveComplaintItemEntity>) mList);
    Log.d(TAG, "getSavedStateOnPause");
    return outState;
  }

  public Bundle getSavedState() {
    Bundle outState = new Bundle();
    outState.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, mUnSignedPage);
    outState.putInt(AppUtils.ARG_SELECTED_POSITION, mSelectedPosition);
    outState.putParcelableArrayList(
        AppUtils.ARG_RC_LIST, (ArrayList<ReceiveComplaintItemEntity>) mList);
    return outState;
  }

  @Override
  public boolean onQueryTextSubmit(String query) {

    img_toolbar.setVisibility(View.GONE);

    Log.d(TAG, "onQueryTextSubmit");
    // filter your list from your input
    filter(query);
    // you can use runnable postDelayed like 500 ms to delay search text
    return false;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    Log.d(TAG, "onQueryTextChange");
    // filter your list from your input
    filter(newText);
    // you can use runnable postDelayed like 500 ms to delay search text
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
        for (ReceiveComplaintItemEntity d : mList) {
          // or use .equal(text) with you want equal match
          // use .toLowerCase() for better matches
          if (d.getComplainRefrenceNumber().toLowerCase().contains(text.toLowerCase()) || d.getComplainDetails().toLowerCase().contains(text.toLowerCase()) || d.getLocation().toLowerCase().contains(text.toLowerCase()) ) {
            temp.add(d);
          }
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
    Log.d(TAG, "onMenuItemClick");
    if (isLoading) return false;
    getReceiveComplainFromService(0);
    return false;
  }

  private void showNewMsg() {
    Log.d(TAG, "showNewMsg");
    if (mPreferences.getString("rc_day", null) != null) {
      Date dateTime = null;
      try {
        dateTime =
            new SimpleDateFormat("dd-MMM-yyyy").parse(mPreferences.getString("day", "22-JAN-2011"));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(dateTime);
      Calendar today = Calendar.getInstance();

      // if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
      //   && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
      // "Today "
      // } else {

      if (ConnectivityStatus.isConnected(mActivity)) {
        receiveComplaintListService.getallforwardedcomplaintbyemployeeid(mStrEmpId);
      } else {
        if (mUnSignedPage == AppUtils.ARGS_RECEIVECOMPLAINT_PAGE) {
          new ReceiveComplaintItemDbInitializer(mActivity, mCurrentnoOfRows, this)
              .execute(AppUtils.MODE_GETALL);
        } else showEmptyView(getString(R.string.lbl_alert_network_not_available));
      }


    } else {
      if (mPreferences
          .getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE)
          .contains(AppUtils.NETWORK_AVAILABLE)) {
        //  receiveComplaintListService.getallforwardedcomplaintbyemployeeid(mStrEmpId);
      } else {
        getReceiveComplainFromService(0);
      }
      mPreferences
          .edit()
          .putString("rc_day", new SimpleDateFormat("dd-MMM-yyyy").format(new Date()))
          .commit();
    }
  }
}
