package com.daemon.emco_android.ui.fragments.reactive;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.GetLogComplaintPopupService;
import com.daemon.emco_android.repository.remote.GetSearchComplaintService;
import com.daemon.emco_android.ui.components.CustomTextInputLayout;
import com.daemon.emco_android.ui.components.FilterableListDialog;
import com.daemon.emco_android.repository.db.dbhelper.SiteAreaDbInitializer;
import com.daemon.emco_android.repository.db.entity.MultiSearchComplaintEntity;
import com.daemon.emco_android.repository.db.entity.SingleSearchComplaintEntity;
import com.daemon.emco_android.repository.db.entity.SiteAreaEntity;
import com.daemon.emco_android.ui.fragments.common.Fragment_Main;
import com.daemon.emco_android.ui.fragments.reactive.viewcomplaint.Fragment_RM_ViewComplaint;
import com.daemon.emco_android.listeners.SearchComplaintListener;
import com.daemon.emco_android.listeners.SiteListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.model.request.SearchSingleComplaintRequest;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ConnectivityStatus;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/** Created by subbu on 25/11/16. */
public class Fragment_RM_SearchComplaint extends Fragment
    implements View.OnClickListener, SearchComplaintListener, SiteListener {
  private final String TAG = Fragment_RM_SearchComplaint.class.getSimpleName();
  private String mNetworkInfo = null;
  private AppCompatActivity mActivity;
  private Context mContext;
  private Font font = App.getInstance().getFontInstance();
  private SharedPreferences mPreferences;
  private GetLogComplaintPopupService mGetComplaintPopupService;
  private SharedPreferences.Editor mEditor;
  private FragmentManager mManager;
  private TextView tv_lbl_complaint_ref_no, tv_lbl_goto_multi_search, tv_toolbar_title,tv_select_site, tv_lbl_site;
  private TextInputEditText tie_complaint_ref_no;
  private AppCompatButton btnGotoMultipleComplaints, btnSearchComplaint;
  private String mComplaintRefNo = null, mEmployeeId = null, mOpco = null;
  private String mStrSiteName = null, mStrSiteCode = null;
  private List<SiteAreaEntity> listSiteArea = new ArrayList<>();
  private CoordinatorLayout cl_main;
  private Toolbar mToolbar;
  private Handler mHandler;
  private SingleSearchComplaintEntity mSingleSearchData;
private CustomTextInputLayout til_complaint_ref;
  private Login mUserData;
  private String mStrLoginData = null;
  private String mStrEmployeeId = null;

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
      mStrLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
      if (mStrLoginData != null) {
        Gson gson = new Gson();
        mUserData = gson.fromJson(mStrLoginData, Login.class);
        mStrEmployeeId = mUserData.getEmployeeId();
      }
      mManager = mActivity.getSupportFragmentManager();
      mHandler = new Handler();
      font = App.getInstance().getFontInstance();
      // mArgs = getArguments().getString(ARGS_BUNDLE_MESSAGE);
      mGetComplaintPopupService =
          new GetLogComplaintPopupService(
              mActivity,
              new EmployeeIdRequest(
                  mUserData.getEmployeeId(),
                  mUserData.getEmployeeId() == null ? mUserData.getUserName() : null,
                  null,
                  null));

      gettingSiteName();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void gettingSiteName() {
    mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
        mGetComplaintPopupService.getSiteAreaData(this);
      }
      else new SiteAreaDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
    }
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView");
    View rootView = null;
    try {
      rootView =
          (View) inflater.inflate(R.layout.fragment_single_search_complaint, container, false);
      initUI(rootView);
      setProperties();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
    if (mStrLoginData != null) {
      Login user = new Gson().fromJson(mStrLoginData, Login.class);
      mEmployeeId = user.getEmployeeId();
    }
  }

  private void initUI(View rootView) {
    Log.d(TAG, "initUI");
    try {
      cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
      tv_lbl_complaint_ref_no = (TextView) rootView.findViewById(R.id.tv_lbl_complaint_ref_no);
      tv_lbl_goto_multi_search = (TextView) rootView.findViewById(R.id.tv_lbl_goto_multi_search);
      tie_complaint_ref_no = (TextInputEditText) rootView.findViewById(R.id.tie_complaint_ref_no);
      btnSearchComplaint = (AppCompatButton) rootView.findViewById(R.id.btnSearchComplaint);
      btnGotoMultipleComplaints =
          (AppCompatButton) rootView.findViewById(R.id.btnGotoMultipleComplaints);
      til_complaint_ref=(CustomTextInputLayout)rootView.findViewById(R.id.til_complaint_ref);
      tv_select_site = (TextView) rootView.findViewById(R.id.tv_select_site);
      tv_lbl_site = (TextView) rootView.findViewById(R.id.tv_lbl_site);

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
    mToolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            mActivity.onBackPressed();
          }
        });
  }

  private void setProperties() {
    Log.d(TAG, "setProperties");
    try {
      tv_lbl_site.setText(Html.fromHtml(getString(R.string.lbl_site)));
      tv_lbl_complaint_ref_no.setText(
          Html.fromHtml(getString(R.string.lbl_complaint_ref_no) + AppUtils.mandatory));

      tv_lbl_complaint_ref_no.setTypeface(font.getHelveticaRegular());
      tv_lbl_goto_multi_search.setTypeface(font.getHelveticaRegular());
      tie_complaint_ref_no.setTypeface(font.getHelveticaRegular());
      btnSearchComplaint.setTypeface(font.getHelveticaRegular());
      btnGotoMultipleComplaints.setTypeface(font.getHelveticaRegular());
      tv_select_site.setTypeface(font.getHelveticaRegular());
      tv_lbl_site.setTypeface(font.getHelveticaRegular());

      tv_select_site.setOnClickListener(this);
      btnSearchComplaint.setOnClickListener(this);
      btnGotoMultipleComplaints.setOnClickListener(this);

      tie_complaint_ref_no.setOnEditorActionListener(
          new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
              if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getDataFromServer();
                return true;
              }
              return false;
            }
          });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean validateComplaintRefNo() {
    mComplaintRefNo = tie_complaint_ref_no.getText().toString().trim();
    if (mComplaintRefNo.isEmpty()) {
      tie_complaint_ref_no.setError(getString(R.string.msg_enter_complaint_ref_no));
      requestFocus(tie_complaint_ref_no);
      return false;
    }

    return true;
  }

  private void requestFocus(View view) {
    if (view.requestFocus()) {
      mActivity
          .getWindow()
          .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btnSearchComplaint:
        getDataFromServer();
        break;
      case R.id.tv_select_site:
        getSiteName();
        break;
      case R.id.btnGotoMultipleComplaints:
        gotoNextFragment(new Fragment_RM_MultiSearchComplaint(), Utils.TAG_MULTISEARCH_COMPLAINTS);
        break;
    }
  }

  public void getSiteName() {
    Log.d(TAG, "getSiteName");
    try {

      ArrayList strArraySiteName = new ArrayList();
      for (SiteAreaEntity entity : listSiteArea) {
        strArraySiteName.add(entity.getSiteDescription());
      }
      FilterableListDialog.create(
              mActivity,
              getString(R.string.lbl_select_site),
              strArraySiteName,
              new FilterableListDialog.OnListItemSelectedListener() {
                @Override
                public void onItemSelected(String text) {
                  for (SiteAreaEntity item : listSiteArea) {
                    if (text.equals(item.getSiteDescription())) {
                      mStrSiteCode = item.getSiteCode();
                      mOpco = item.getOpco();
                    }
                  }
                  tv_select_site.setText(text.toString());
                  tv_select_site.setTypeface(font.getHelveticaBold());
                  AppUtils.setErrorBg(tv_select_site, false);
                }
              })
          .show();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void getDataFromServer() {
    Log.d(TAG, "getDataFromServer");
    try {
     /* if (mStrSiteCode == null) {
        AppUtils.setErrorBg(tv_select_site, true);
        AppUtils.showDialog(mActivity, "Please select values in Mandatory field");
        return;
      } else {
        AppUtils.setErrorBg(tv_select_site, false);
      }*/
     /* if (mOpco == null) {
        AppUtils.showDialog(mActivity, "Opco code invalid");
        return;
      } else if (!validateComplaintRefNo()) return;*/

     if(TextUtils.isEmpty(tie_complaint_ref_no.getText().toString())){
      // AppUtils.showDialog(mActivity, "Please select values in Mandatory field");
       //AppUtils.setErrorBg(til_complaint_ref, false);
      // til_complaint_ref.setError(getString(R.string.lbl_please_select_complaint_ref));
       validateComplaintRefNo();
       return;
     }else{
       if(ConnectivityStatus.isConnected(mActivity)){
         AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
         new GetSearchComplaintService(mActivity, this, this)
                 .getSingleSearchData(
                         new SearchSingleComplaintRequest(tie_complaint_ref_no.getText().toString(), mOpco, mStrSiteCode));
       }else{
         Toast.makeText(mActivity,(R.string.msg_no_data_found_in_local_db),Toast.LENGTH_SHORT).show();
       }
     }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void onComplaintItemClicked(int position) {}

  @Override
  public void onComplaintReceivedSuccess(
      ArrayList<MultiSearchComplaintEntity> searchComplaintEntityList, String noOfRows) {}

  @Override
  public void onSingleComplaintReceivedSuccess(
      SingleSearchComplaintEntity singleSearchComplaintEntity) {
    Log.d(TAG, "onSingleComplaintReceivedSuccess");
    AppUtils.hideProgressDialog();
    mSingleSearchData = singleSearchComplaintEntity;
    gotoNextFragment(new Fragment_RM_ViewComplaint(), Utils.TAG_VIEW_COMPLAINT);
  }

  @Override
  public void onComplaintReceivedFailure(String strErr) {
    Log.d(TAG, "onComplaintReceivedFailure");
    AppUtils.hideProgressDialog();
    AppUtils.showDialog(mActivity, strErr);
  }

  private void gotoNextFragment(Fragment _fragment, final String tag) {
    try {
      Log.d(TAG, "gotoNextFragment");
      final Fragment fragment = _fragment;
      if (tag.equals(Utils.TAG_VIEW_COMPLAINT)) {
        Bundle args = new Bundle();
        args.putParcelable(AppUtils.ARGS_SEARCH_COMPLAINT_RESULT, mSingleSearchData);
        fragment.setArguments(args);
        mStrSiteCode = null;
        mComplaintRefNo = null;
        tie_complaint_ref_no.setText("");
      }
      Runnable mPendingRunnable =
          new Runnable() {
            @Override
            public void run() {
              // update the main content by replacing fragments
              FragmentTransaction fragmentTransaction =
                  mActivity.getSupportFragmentManager().beginTransaction();
              fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
              fragmentTransaction.replace(R.id.frame_container, fragment, Utils.TAG_VIEW_COMPLAINT);
              fragmentTransaction.addToBackStack(Utils.TAG_VIEW_COMPLAINT);
              fragmentTransaction.commit();
            }
          };

      if (mPendingRunnable != null) {
        mHandler.post(mPendingRunnable);
      }
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
        // mActivity.onBackPressed();
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
  public void onLogComplaintSiteReceivedSuccess(List<SiteAreaEntity> siteAreaList, int mode) {
    Log.d(TAG, "onLogComplaintSiteReceivedSuccess");
    listSiteArea = siteAreaList;
    AppUtils.hideProgressDialog();
    if (mode == AppUtils.MODE_SERVER) {
      AppUtils.hideProgressDialog();
      new SiteAreaDbInitializer(mActivity, listSiteArea, this).execute(AppUtils.MODE_INSERT);
    }
  }

  @Override
  public void onLogComplaintSiteReceivedFailure(String strErr, int mode) {
    Log.d(TAG, "onLogComplaintSiteReceivedFailure");
    AppUtils.hideProgressDialog();
    if (mode == AppUtils.MODE_LOCAL) {
      AppUtils.showDialog(mActivity, strErr);
    } else new SiteAreaDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
  }
}
