package com.daemon.emco_android.ui.fragments.reactive.receieve_complaints;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.EmployeeTrackingDetail;
import com.daemon.emco_android.repository.remote.FeedBackRepository;
import com.daemon.emco_android.repository.remote.ReceiveComplaintViewService;
import com.daemon.emco_android.repository.db.dbhelper.FbEmployeeDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.FeedbackDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ReceiveComplaintViewDbInitializer;
import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.repository.db.entity.EmployeeDetailsEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntity;
import com.daemon.emco_android.service.GPSTracker;
import com.daemon.emco_android.listeners.FeedbackListener;
import com.daemon.emco_android.listeners.ReceivecomplaintView_Listener;
import com.daemon.emco_android.listeners.TechRemarksListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.FetchFeedbackRequest;
import com.daemon.emco_android.model.request.ReceiveComplaintViewRequest;
import com.daemon.emco_android.model.response.PpmEmployeeFeedResponse;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.checkInternet;
import static com.daemon.emco_android.utils.Utils.TAG_RATE_AND_SHARE;
import static com.daemon.emco_android.utils.Utils.TAG_RECEIVE_COMPLAINT_RATE__AND_FEEDBACK;

/** Created by Daemonsoft on 7/25/2017. */
public class Fragment_RC_Feedback extends Fragment
    implements FeedbackListener, TechRemarksListener, ReceivecomplaintView_Listener {
  private static final String TAG = Fragment_RC_Feedback.class.getSimpleName();
  Gson gson = new Gson();
  Integer[] old_Attendedby = null;
  Integer[] old_Checkedby = null;
  private SaveFeedbackEntity postFeedbackEntity;
  private Type baseType = new TypeToken<List<String>>() {}.getType();
  private AppCompatActivity mActivity;
  private Font font = App.getInstance().getFontInstance();
  private SharedPreferences mPreferences;
  private SharedPreferences.Editor mEditor;
  private CoordinatorLayout cl_main;
  private Bundle mArgs;
  private FragmentManager mManager;
  private String mUnSignedPage = AppUtils.ARGS_RECEIVECOMPLAINT_PAGE;
  private boolean frmList=false;
  private TextView tv_lbl_checkedby,
      tv_toolbar_title,
      tv_lbl_attendedby,
      tv_lbl_tech_remarks,
      tv_lbl_technfeedback,
      tv_lbl_supervisorremarks;
  private TextView tv_select_checkedby, tv_select_attendedby, tv_select_signstatus;
  private EditText et_technfeedback, et_supervisorremarks;
  private FloatingActionButton btn_feedback_save;
  private Toolbar mToolbar;
  private View rootView;
  private ReceiveComplaintViewService complaintView_service;
  private FeedBackRepository feedBackService;
  private List<EmployeeDetailsEntity> entityList = new ArrayList<>();
  private List<ReceiveComplaintViewEntity> entityListNew = new ArrayList<>();
  private ReceiveComplaintViewEntity receiveComplaintViewEntity;
  private ReceiveComplaintItemEntity receiveComplaintItemEntity;
  private List<EmployeeDetailsEntity> checkedby_response = new ArrayList<>();
  private List<EmployeeDetailsEntity> checkedby = new ArrayList<>();
  private List<EmployeeDetailsEntity> attendedby_response = new ArrayList<>();
  private List<EmployeeDetailsEntity> attendedby = new ArrayList<>();
  private String mStrEmpId = null, select_signstatus = null, mLoginData = null;
  private List<String> listSignStatus = null;
  View.OnClickListener _OnClickListener =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Log.d(TAG, "onClick");
          AppUtils.closeInput(cl_main);
          switch (v.getId()) {

            case R.id.tv_select_signstatus:
              getTechRemarks();
              break;
            case R.id.tv_select_checkedby:
              getCheckedby();
              break;
            case R.id.tv_select_attendedby:
              getAttendedby();
              break;

            default:
              break;
          }
        }
      };

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      mActivity = (AppCompatActivity) getActivity();
      setHasOptionsMenu(true);
      setRetainInstance(false);
      mManager = mActivity.getSupportFragmentManager();

      mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
      mEditor = mPreferences.edit();
      font = App.getInstance().getFontInstance();
      mArgs = getArguments();
      if (mActivity.getCurrentFocus() != null) {
        InputMethodManager imm =
            (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
      }
      mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
      mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
      if (mLoginData != null) {
        Gson gson = new Gson();
        Login login = gson.fromJson(mLoginData, Login.class);
        mStrEmpId = login.getEmployeeId();
      }
      feedBackService = new FeedBackRepository(mActivity, this);
      complaintView_service = new ReceiveComplaintViewService(mActivity, this);
      if (mArgs != null && mArgs.size() > 0) {
        receiveComplaintViewEntity =
            mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);
        if (receiveComplaintViewEntity == null) {
          receiveComplaintViewEntity = new ReceiveComplaintViewEntity();
          receiveComplaintItemEntity =
              mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS);

          if(mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS)!=null){

            frmList=true;

          }

          if (receiveComplaintItemEntity != null) {
            receiveComplaintViewEntity.setComplaintNumber(
                receiveComplaintItemEntity.getComplaintNumber());
            receiveComplaintViewEntity.setOpco(receiveComplaintItemEntity.getOpco());
            receiveComplaintViewEntity.setContractNo(
                receiveComplaintItemEntity.getContractNumber());
            receiveComplaintViewEntity.setComplaintSite(receiveComplaintItemEntity.getSiteCode());

            if (checkInternet(getContext())) {
              AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
              complaintView_service.GetReceiveComplaintViewData(
                  new ReceiveComplaintViewRequest(
                      receiveComplaintViewEntity.getComplaintNumber(),
                      receiveComplaintViewEntity.getComplaintSite(),
                      receiveComplaintViewEntity.getOpco()));
            } else {

              new ReceiveComplaintViewDbInitializer(mActivity, this, receiveComplaintViewEntity)
                  .execute(AppUtils.MODE_GET);
            }
          }
        } else fetchFeedbackData();
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(TAG, "onCreate");
    try {
      rootView = inflater.inflate(R.layout.fragment_receivecomplaints_feedback, container, false);
      initUI(rootView);
      setupActionBar();
      setProperties();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return rootView;
  }

  private void initUI(View rootView) {
    Log.d(TAG, "initUI");
    try {
      cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);

      tv_lbl_checkedby = (TextView) rootView.findViewById(R.id.tv_lbl_checkedby);
      tv_lbl_attendedby = (TextView) rootView.findViewById(R.id.tv_lbl_attendedby);
      tv_lbl_technfeedback = (TextView) rootView.findViewById(R.id.tv_lbl_technfeedback);
      tv_lbl_supervisorremarks = (TextView) rootView.findViewById(R.id.tv_lbl_supervisorremarks);
      et_technfeedback = (EditText) rootView.findViewById(R.id.et_technfeedback);
      et_supervisorremarks = (EditText) rootView.findViewById(R.id.et_supervisorremarks);
      tv_select_signstatus = (TextView) rootView.findViewById(R.id.tv_select_signstatus);
      tv_lbl_tech_remarks = (TextView) rootView.findViewById(R.id.tv_lbl_tech_remarks);
      tv_select_checkedby = (TextView) rootView.findViewById(R.id.tv_select_checkedby);
      tv_select_attendedby = (TextView) rootView.findViewById(R.id.tv_select_attendedby);
      btn_feedback_save = (FloatingActionButton) rootView.findViewById(R.id.btn_feedback_save);

      btn_feedback_save.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          submitForm();

        }
      });

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setupActionBar() {
    mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
    // mToolbar.setTitle(getResources().getString(R.string.lbl_feedback));
    tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
    tv_toolbar_title.setText(getString(R.string.lbl_feedback));
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

    tv_lbl_checkedby.setText(Html.fromHtml(getString(R.string.lbl_checkedby) + AppUtils.mandatory));
    tv_lbl_attendedby.setText(
        Html.fromHtml(getString(R.string.lbl_attendedby) + AppUtils.mandatory));
    tv_lbl_technfeedback.setText(
        Html.fromHtml(getString(R.string.lbl_technfeedback) + AppUtils.mandatory));
    tv_lbl_supervisorremarks.setText(
        Html.fromHtml(getString(R.string.lbl_supervisorremarks) + AppUtils.mandatory));
    tv_lbl_tech_remarks.setText(
        Html.fromHtml(getString(R.string.lbl_tech_remarks) + AppUtils.mandatory));


    tv_select_checkedby.setOnClickListener(_OnClickListener);
    tv_select_attendedby.setOnClickListener(_OnClickListener);
    tv_select_signstatus.setOnClickListener(_OnClickListener);


    if (receiveComplaintViewEntity != null) {


      if (receiveComplaintViewEntity.getCustomerSignStatus() != null) {
        select_signstatus = receiveComplaintViewEntity.getCustomerSignStatus();
        tv_select_signstatus.setText(receiveComplaintViewEntity.getCustomerSignStatus());
      }
    }
  }

  private void fetchFeedbackData() {
    try {
      if (receiveComplaintViewEntity != null) {
        if (receiveComplaintViewEntity.getComplaintNumber() != null
            && receiveComplaintViewEntity.getComplaintSite() != null
            && receiveComplaintViewEntity.getOpco() != null) {
          // showProgressDialog(mActivity, "Loading...", false);
          FetchFeedbackRequest feedbackRequest =
              new FetchFeedbackRequest(
                  receiveComplaintViewEntity.getComplaintNumber(),
                  receiveComplaintViewEntity.getComplaintSite(),
                  receiveComplaintViewEntity.getOpco());

          EmployeeDetailsEntity request = new EmployeeDetailsEntity();
          request.setOpco(receiveComplaintViewEntity.getOpco());
          request.setContractNo(
              receiveComplaintViewEntity.getContractNo() != null
                  ? receiveComplaintViewEntity.getContractNo()
                  : receiveComplaintViewEntity.getJobNumber());
          request.setWorkCategory(receiveComplaintViewEntity.getWorkCategory());

          if (checkInternet(getContext())) {
            feedBackService.fetchFeedbackDetailsData(feedbackRequest);

            AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
            feedBackService.getFeedbackEmployeeDetailsData(request);

            feedBackService.getTechnicalRemarksData(this);

          } else {
            SaveFeedbackEntity saveFeedbackEntity =
                new SaveFeedbackEntity(receiveComplaintViewEntity.getComplaintNumber());
            saveFeedbackEntity.setComplaintSite(receiveComplaintViewEntity.getComplaintSite());
            saveFeedbackEntity.setOpco(receiveComplaintViewEntity.getOpco());
            new FeedbackDbInitializer(mActivity, this, saveFeedbackEntity)
                .execute(AppUtils.MODE_GET);

            new FbEmployeeDbInitializer(mActivity, this, request).execute(AppUtils.MODE_GET);

            String strJsonTr = mPreferences.getString(AppUtils.SHARED_TECH_REMARKS, null);
            Log.d(TAG, "str json :;" + strJsonTr);
            if (strJsonTr != null && strJsonTr.length() > 0) {
              listSignStatus = gson.fromJson(strJsonTr, baseType);
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void submitForm() {
    Log.d(TAG, "submitForm");
    try {
      AppUtils.closeInput(cl_main);

      String msgErr = "";
      if (checkedby.isEmpty()) {
        AppUtils.setErrorBg(tv_select_checkedby, true);
        msgErr = getString(R.string.lbl_select_checkedby);
      } else AppUtils.setErrorBg(tv_select_checkedby, false);
      if (attendedby.isEmpty()) {
        AppUtils.setErrorBg(tv_select_attendedby, true);
        msgErr = msgErr + "\n" + getString(R.string.lbl_select_attendedby);
      } else AppUtils.setErrorBg(tv_select_attendedby, false);

      if (select_signstatus == null) {
        AppUtils.setErrorBg(tv_select_signstatus, true);
        msgErr = msgErr + "\n" + getString(R.string.lbl_select_tech_remarks);
      } else AppUtils.setErrorBg(tv_select_signstatus, false);

      if (msgErr != "") {
        AppUtils.showDialog(mActivity, "Please select values in Mandatory field");
        return;
      }
      if (TextUtils.isEmpty(et_technfeedback.getText().toString())) {
        et_technfeedback.setError(getString(R.string.lbl_feedback_required));
        et_technfeedback.requestFocus();
      } /*else if (TextUtils.isEmpty(et_supervisorremarks.getText().toString())) {
            et_supervisorremarks.setError(getString(R.string.lbl_remarks_required));
            et_supervisorremarks.requestFocus();
        }*/ else {
        if (receiveComplaintViewEntity != null) {
          postFeedbackEntity =
              new SaveFeedbackEntity(receiveComplaintViewEntity.getComplaintNumber());

          postFeedbackEntity.setOpco(receiveComplaintViewEntity.getOpco());
          postFeedbackEntity.setComplaintSite(receiveComplaintViewEntity.getComplaintSite());
          postFeedbackEntity.setAttendedBy(attendedby);
          postFeedbackEntity.setCheckedBy(checkedby);
          postFeedbackEntity.setCustomerSignStatus(select_signstatus);
          postFeedbackEntity.setFeedbackInformation(et_technfeedback.getText().toString());
          postFeedbackEntity.setSupRemark(et_supervisorremarks.getText().toString());

          if (checkInternet(getContext())) {

            AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
            feedBackService.postFeedbackDetailsData(postFeedbackEntity);
          } else {
            postFeedbackEntity.setMode(AppUtils.MODE_LOCAL);
            new FeedbackDbInitializer(mActivity, this, postFeedbackEntity)
                .execute(AppUtils.MODE_INSERT_SINGLE);
            showPopupMsg("Saved successfully");
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getAttendedby() {
    Log.d(TAG, "getAttendedby");
    try {
      final String[] strArraySiteName = new String[entityList.size()];
      ArrayList<Integer> list = new ArrayList();
      for (int i = 0; i < entityList.size(); i++) {
        String s = entityList.get(i).getEmployeeId() + " - " + entityList.get(i).getEmployeeName();
        strArraySiteName[i] = s;

        // if (attendedby_response != null && old_Attendedby == null) {
        for (EmployeeDetailsEntity entity : attendedby_response) {
          if (s.equals(entity.getEmployeeId() + " - " + entity.getEmployeeName())) {
            if (!list.contains(i)) {
              list.add(i);
            }
          }
        }
        old_Attendedby = list.toArray(new Integer[list.size()]);
        //  }
      }
      if (old_Attendedby != null) {
        for (int i : old_Attendedby) {
          if (!list.contains(i)) {
            list.add(i);
          }
        }
        old_Attendedby = list.toArray(new Integer[list.size()]);
      }

      new MaterialDialog.Builder(mActivity)
          .title(R.string.lbl_select_attendedby)
          .items(strArraySiteName)
          .itemsCallbackMultiChoice(
              old_Attendedby,
              new MaterialDialog.ListCallbackMultiChoice() {
                @Override
                public boolean onSelection(
                    MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                  old_Attendedby = which;
                  if (which.length > 0) {
                    attendedby.clear();
                    String a = "";
                    for (EmployeeDetailsEntity item : entityList) {
                      Log.d(item.getEmployeeId() + " - " + item.getEmployeeName(), text.toString());
                      for (int i : which) {
                        if (strArraySiteName[i].equals(
                            item.getEmployeeId() + " - " + item.getEmployeeName())) {
                          Log.d(
                              item.getEmployeeId() + " - " + item.getEmployeeName(),
                              text.toString());
                          attendedby.add(item);
                          a += item.getEmployeeName() + ",";
                        }
                      }
                    }
                    tv_select_attendedby.setText(a);
                    tv_select_attendedby.setTypeface(font.getHelveticaBold());
                    AppUtils.setErrorBg(tv_select_attendedby, false);
                  } else {
                    attendedby.clear();
                    tv_select_attendedby.setText("");
                    tv_select_attendedby.setHint(getString(R.string.lbl_select_attendedby));
                    AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                    AppUtils.setErrorBg(tv_select_attendedby, true);
                  }
                  AppUtils.closeInput(cl_main);
                  return false;
                }
              })
          .canceledOnTouchOutside(false)
          .positiveText(R.string.lbl_done)
          .negativeText(R.string.lbl_close)
          .show();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getCheckedby() {
    Log.d(TAG, "getCheckedby");
    try {
      final String[] strArrayData = new String[entityList.size()];
      ArrayList<Integer> list = new ArrayList();
      for (int i = 0; i < entityList.size(); i++) {
        String data =
            entityList.get(i).getEmployeeId() + " - " + entityList.get(i).getEmployeeName();
        strArrayData[i] = data;

        // if (checkedby_response != null && old_Checkedby == null) {
        for (EmployeeDetailsEntity entity : checkedby_response) {
          if (data.equals(entity.getEmployeeId() + " - " + entity.getEmployeeName())) {
            if (!list.contains(i)) {
              list.add(i);
            }
          }
        }
        old_Checkedby = list.toArray(new Integer[list.size()]);
        // }
      }
      if (old_Checkedby != null) {
        for (int i : old_Checkedby) {
          if (!list.contains(i)) {
            list.add(i);
          }
        }
        old_Checkedby = list.toArray(new Integer[list.size()]);
      }

      new MaterialDialog.Builder(mActivity)
          .title(R.string.lbl_select_checkedby)
          .items(strArrayData)
          .itemsCallback(
              new MaterialDialog.ListCallback() {
                @Override
                public void onSelection(
                    MaterialDialog dialog, View itemView, int position, CharSequence text) {}
              })
          .itemsCallbackMultiChoice(
              old_Checkedby,
              new MaterialDialog.ListCallbackMultiChoice() {
                @Override
                public boolean onSelection(
                    MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                  old_Checkedby = which;
                  if (which.length > 0) {
                    checkedby.clear();
                    String c = "";
                    for (EmployeeDetailsEntity item : entityList) {
                      for (int i : which) {
                        if (strArrayData[i].equals(
                            item.getEmployeeId() + " - " + item.getEmployeeName())) {
                          checkedby.add(item);
                          c += item.getEmployeeName() + ",";
                        }
                      }
                    }
                    tv_select_checkedby.setText(c);
                    tv_select_checkedby.setTypeface(font.getHelveticaBold());
                    AppUtils.setErrorBg(tv_select_checkedby, false);
                  } else {
                    checkedby.clear();
                    tv_select_checkedby.setText("");
                    tv_select_checkedby.setHint(getString(R.string.lbl_select_checkedby));
                    AppUtils.showDialog(mActivity, getString(R.string.no_value_has_been_selected));
                    AppUtils.setErrorBg(tv_select_checkedby, true);
                  }
                  AppUtils.closeInput(cl_main);
                  return false;
                }
              })
          .canceledOnTouchOutside(false)
          .positiveText(R.string.lbl_done)
          .negativeText(R.string.lbl_close)
          .show();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getTechRemarks() {
    Log.d(TAG, "getTechRemarks");
    try {
      if (listSignStatus.size() > 0) {
        new MaterialDialog.Builder(mActivity)
            .title(R.string.lbl_select_tech_remarks)
            .items(listSignStatus)
            .itemsCallbackSingleChoice(
                -1,
                new MaterialDialog.ListCallbackSingleChoice() {
                  @Override
                  public boolean onSelection(
                      MaterialDialog dialog, View view, int which, CharSequence text) {
                    try {
                      if (which >= 0) {
                        select_signstatus = text.toString();
                        if (receiveComplaintViewEntity != null)
                          receiveComplaintViewEntity.setCustomerSignStatus(text.toString());
                        tv_select_signstatus.setText(text.toString());
                        tv_select_signstatus.setTypeface(font.getHelveticaBold());
                        AppUtils.setErrorBg(tv_select_signstatus, false);
                      } else {
                        select_signstatus = null;
                        tv_select_signstatus.setText("");
                        tv_select_signstatus.setHint(getString(R.string.lbl_select_tech_remarks));
                        AppUtils.showDialog(
                            mActivity, getString(R.string.no_value_has_been_selected));
                        AppUtils.setErrorBg(tv_select_signstatus, true);
                      }
                      AppUtils.closeInput(cl_main);
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                    return true;
                  }
                })
            .canceledOnTouchOutside(false)
            .positiveText(R.string.lbl_done)
            .negativeText(R.string.lbl_close)
            .show();
      } else AppUtils.showDialog(mActivity, getString(R.string.msg_no_data_found_in_local_db));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void onFeedbackEmployeeDetailsReceivedSuccess(
      List<EmployeeDetailsEntity> employeeDetailsEntities, int mode) {
    Log.d(TAG, "onFeedbackEmployeeDetailsReceivedSuccess");
    try {
      AppUtils.hideProgressDialog();
      entityList = employeeDetailsEntities;
      if (mode == AppUtils.MODE_SERVER)
        new FbEmployeeDbInitializer(mActivity, this, employeeDetailsEntities)
            .execute(AppUtils.MODE_INSERT_SINGLE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onFeedbackDetailsReceivedSuccess(SaveFeedbackEntity rcFeedbackEntity, int mode) {
    Log.d(TAG, "onFeedbackDetailsReceivedSuccess");
    try {
      AppUtils.hideProgressDialog();
      if (rcFeedbackEntity != null) {
        if (rcFeedbackEntity.getFeedbackInformation() != null)
          et_technfeedback.setText(rcFeedbackEntity.getFeedbackInformation());
        if (rcFeedbackEntity.getCustomerSignStatus() != null) {
          select_signstatus = rcFeedbackEntity.getCustomerSignStatus();
          tv_select_signstatus.setText(rcFeedbackEntity.getCustomerSignStatus());
        }
        if (rcFeedbackEntity.getCheckedBy() != null) {
          String c = "";
          for (EmployeeDetailsEntity item : rcFeedbackEntity.getCheckedBy()) {
            c += item.getEmployeeName() + ",";
          }
          tv_select_checkedby.setText(c);
          checkedby_response.addAll(rcFeedbackEntity.getCheckedBy());
          checkedby.addAll(rcFeedbackEntity.getCheckedBy());
          AppUtils.setErrorBg(tv_select_checkedby, false);
        }
        if (rcFeedbackEntity.getAttendedBy() != null) {
          String a = "";
          for (EmployeeDetailsEntity item : rcFeedbackEntity.getAttendedBy()) {
            a += item.getEmployeeName() + ",";
          }
          tv_select_attendedby.setText(a);
          attendedby_response.addAll(rcFeedbackEntity.getAttendedBy());
          attendedby.addAll(rcFeedbackEntity.getAttendedBy());
          AppUtils.setErrorBg(tv_select_attendedby, false);
        }

        if (mode == AppUtils.MODE_SERVER) {
          rcFeedbackEntity.setComplaintNumber(receiveComplaintViewEntity.getComplaintNumber());
          rcFeedbackEntity.setOpco(receiveComplaintViewEntity.getOpco());
          rcFeedbackEntity.setComplaintSite(receiveComplaintViewEntity.getComplaintSite());
          new FeedbackDbInitializer(mActivity, this, rcFeedbackEntity)
              .execute(AppUtils.MODE_INSERT_SINGLE);
        }

      } else AppUtils.showDialog(mActivity, "No previous Data Found Feedback");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onAllFeedbackDetailsReceivedSuccess(
      List<SaveFeedbackEntity> saveFeedbackEntities, int mode) {}

  @Override
  public void onFeedbackDetailsReceivedSuccessPPMAtten(
      PpmEmployeeFeedResponse saveFeedbackEntity) {}

  @Override
  public void onFeedbackEmployeeDetailsSaveSuccess(String strMsg, int mode) {

      EmployeeTrackingDetail emp=new EmployeeTrackingDetail();
      emp.setCompCode(postFeedbackEntity.getOpco());
      emp.setTransType("Reactive");
      emp.setRefNo(postFeedbackEntity.getComplaintNumber());
      new GPSTracker(getContext()).updateFusedLocation(emp);


    Log.d(TAG, "onFeedbackEmployeeDetailsSaveSuccess" + strMsg);
    try {
      if (mode == AppUtils.MODE_SERVER)
        new FeedbackDbInitializer(mActivity, this, postFeedbackEntity)
            .execute(AppUtils.MODE_INSERT_SINGLE);

      showPopupMsg(strMsg);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showPopupMsg(String strMsg) {
    try {
      AppUtils.hideProgressDialog();
      MaterialDialog.Builder builder =
          new MaterialDialog.Builder(mActivity)
              .content(strMsg)
              .positiveText(R.string.lbl_okay)
              .stackingBehavior(StackingBehavior.ADAPTIVE)
              .onPositive(
                  new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(
                        @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                      dialog.dismiss();
                      if (select_signstatus.equalsIgnoreCase(AppUtils.CLOSEWITHSIGN)) {
                        if (receiveComplaintViewEntity != null) {

                          if(frmList){
                          Bundle mdata = new Bundle();
                          mdata.putString(AppUtils.ARGS_RECEIVECOMPLAINT_PAGETYPE, mUnSignedPage);
                          mdata.putParcelable(
                              AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS,
                              receiveComplaintViewEntity);
                          Fragment_RM_RateService fragment = new Fragment_RM_RateService();
                          fragment.setArguments(mdata);
                          FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                          FragmentTransaction fragmentTransaction =
                              fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                          fragmentTransaction.replace(
                              R.id.frame_container,
                              fragment,
                              TAG_RECEIVE_COMPLAINT_RATE__AND_FEEDBACK);
                          fragmentTransaction.addToBackStack(TAG_RATE_AND_SHARE);
                          fragmentTransaction.commit();
                          }
                          else{
                            mManager.popBackStack();
                          }




                        } else {
                          mManager.popBackStack();
                        }
                      } else {
                        mManager.popBackStack();
                      }
                    }
                  });

      MaterialDialog dialog = builder.build();
      dialog.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onFeedbackPpmStatusSucess(List<String> strMsg, int mode) {}

  @Override
  public void onFeedbackEmployeeDetailsReceivedFailure(String strErr, int mode) {
    Log.d(TAG, "onFeedbackEmployeeDetailsReceivedFailure");
    try {
      AppUtils.hideProgressDialog();

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
    try {
      switch (item.getItemId()) {
        case R.id.action_home:
          Log.d(TAG, "onOptionsItemSelected : home");
          // mActivity.onBackPressed();
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
    } catch (Exception e) {
      e.printStackTrace();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onTechRemarksReceived(List<String> technicalRemarks) {

    Log.d(TAG, "onTechnicalRemarksReceived");
    listSignStatus = technicalRemarks;
    // AppUtils.hideProgressDialog();
    String strTechRemarks = gson.toJson(technicalRemarks, baseType);
    mEditor.putString(AppUtils.SHARED_TECH_REMARKS, strTechRemarks);
    mEditor.commit();
  }

  @Override
  public void onechRemarksReceivedError(String strErr) {
    AppUtils.hideProgressDialog();
    AppUtils.showDialog(mActivity, strErr);
  }

  @Override
  public void onReceiveComplaintRemarksReceived(List<String> remarkList, int mode) {}

  @Override
  public void onReceiveComplaintRespondReceived(String strMsg, String complaintNumber, int mode) {}

  @Override
  public void onReceiveComplaintViewReceived(
      List<ReceiveComplaintViewEntity> complaintViewEntities, int mode) {
    try {
      AppUtils.hideProgressDialog();
      if (complaintViewEntities != null) {
        receiveComplaintViewEntity = complaintViewEntities.get(0);
        fetchFeedbackData();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onReceiveComplaintViewAssetDetailsReceived(
      List<AssetDetailsEntity> assetDetailsEntities, int mode) {}

  @Override
  public void onReceiveComplaintViewReceivedError(String msg, int mode) {
    try {
      AppUtils.hideProgressDialog();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onReceiveBarCodeAssetReceived(String msg, int mode) {}

  @Override
  public void onAllReceiveComplaintData(
      List<ReceiveComplaintRespondEntity> complaintRespondEntities, int modeLocal) {}

  @Override
  public void onReceiveComplaintBycomplaintNumber(
      ReceiveComplaintRespondEntity complaintRespondEntity, int modeLocal) {}
}
