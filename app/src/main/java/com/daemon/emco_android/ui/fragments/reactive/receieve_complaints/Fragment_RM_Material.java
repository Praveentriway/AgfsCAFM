package com.daemon.emco_android.ui.fragments.reactive.receieve_complaints;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.RCMaterialListAdapter;
import com.daemon.emco_android.repository.remote.RCMaterialService;
import com.daemon.emco_android.repository.db.dbhelper.RCMaterialDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.RCSavedMaterialDbInitializer;
import com.daemon.emco_android.repository.db.entity.MaterialMasterEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.repository.db.entity.SaveMaterialEntity;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.listeners.Material_Listener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.checkInternet;

/** Created by Daemonsoft on 7/26/2017. */
public class Fragment_RM_Material extends Fragment implements Material_Listener {

  private static String TAG = Fragment_RM_Material.class.getSimpleName();
  private Font font = App.getInstance().getFontInstance();
  private AppCompatActivity mActivity;
  private FragmentManager mManager;
  private CoordinatorLayout cl_main;

  private RecyclerView recyclerView;
  private LinearLayoutManager mLayoutManager;
  private int totalnoOfRows = 0;
  private int mCurrentnoOfRows = 0;
  private boolean isLastPage = true;
  private boolean isLoading = false;
  private boolean isGetMaterial = true;
  private boolean isMaterialRequired = false;

  private TextView text_view_loading_message, tv_toolbar_title;
  private LinearLayout layout_loading_message;
  private TextView text_view_message, text_view_empty;
  private LinearLayout layout_loading;
  private RelativeLayout layout_empty;
  private RCMaterialListAdapter adapter;
  private List<MaterialMasterEntity> mList = new ArrayList<>();
  private List<SaveMaterialEntity> mgetList = new ArrayList<>();
  private Bundle mArgs;
  private SharedPreferences mPreferences;
  private RCMaterialService rcMaterial_service;
  private String mStrEmpId = null, mQuery = null, mOpco = null;
  private String mLoginData = null, mComplaintNumber = null;
  private ReceiveComplaintViewEntity receiveComplaintViewEntity;
  private SearchView sac_material_search;
  private Button btn_material_save;
  private TextView tv_itemcode, tv_description, tv_quantity, tv_remarks;
  private Toolbar mToolbar;
  private View rootView;
  private List<SaveMaterialEntity> saveMaterialRequests = new ArrayList<>();
  View.OnClickListener _OnClickListener =
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          switch (view.getId()) {
            case R.id.btn_material_save:
              postSaveMaterialData();
              break;
            case R.id.sac_material_search:
              sac_material_search.setEnabled(true);
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
      Log.d(TAG, "onCreate");

      mActivity = (AppCompatActivity) getActivity();
      setHasOptionsMenu(true);
      setRetainInstance(false);
      mManager = mActivity.getSupportFragmentManager();
      mArgs = getArguments();
      mActivity
          .getWindow()
          .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
      mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
      rcMaterial_service = new RCMaterialService(mActivity, this);
      if (mLoginData != null) {
        Gson gson = new Gson();
        Login login = gson.fromJson(mLoginData, Login.class);
        mStrEmpId = login.getEmployeeId();
      }

      if (mArgs != null && mArgs.size() > 0) {
        // Check if true material required page other wise material used page
        receiveComplaintViewEntity =
            mArgs.getParcelable(AppUtils.ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS);
        mComplaintNumber = receiveComplaintViewEntity.getComplaintNumber();
        isMaterialRequired =
            mArgs.getString(AppUtils.ARGS_MATERIAL_PAGE_STRING, AppUtils.ARGS_MATERIAL_USED_STRING)
                == AppUtils.ARGS_MATERIAL_REQUIRED_STRING;

        getReceiveComplainMaterialFromService(mCurrentnoOfRows);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_rm_rc_material, container, false);
    Log.d(TAG, "onCreateView");
    initView(rootView);
    setManager();
    setProperties();
    setupActionBar();

    if ((isGetMaterial ? mgetList : mList).isEmpty()) {
      showLoadingMaterial();
    }
    return rootView;
  }

  public void initView(View view) {
    Log.d(TAG, "initView");

    try {
      cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
      recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

      sac_material_search = (SearchView) view.findViewById(R.id.sac_material_search);

      text_view_loading_message = (TextView) view.findViewById(R.id.text_view_message);
      layout_loading_message = (LinearLayout) view.findViewById(R.id.layout_loading);

      layout_loading = (LinearLayout) view.findViewById(R.id.layout_loading);
      layout_empty = (RelativeLayout) view.findViewById(R.id.layout_empty);
      text_view_empty = (TextView) view.findViewById(R.id.text_view_empty);
      text_view_message = (TextView) view.findViewById(R.id.text_view_message);

      tv_itemcode = (TextView) view.findViewById(R.id.tv_itemcode);
      tv_description = (TextView) view.findViewById(R.id.tv_description);
      tv_quantity = (TextView) view.findViewById(R.id.tv_quantity);
      tv_remarks = (TextView) view.findViewById(R.id.tv_remarks);

      btn_material_save = (Button) view.findViewById(R.id.btn_material_save);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setProperties() {
    Log.d(TAG, "setProperties");

    try {

      sac_material_search.setQueryHint(getString(R.string.lbl_search));
      sac_material_search.setIconifiedByDefault(false);
      sac_material_search.setEnabled(false);
      sac_material_search.setOnClickListener(_OnClickListener);
      sac_material_search.setOnQueryTextListener(
          new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
              sac_material_search.setEnabled(false);
              submitQuery(query);
              return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              return false;
            }
          });

      adapter = new RCMaterialListAdapter(mActivity, mList, mgetList);
      adapter.setListener(this);
      recyclerView.setAdapter(adapter);
      // more than data con't editable so remove pagination
      recyclerView.addOnScrollListener(
          new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
              super.onScrolled(recyclerView, dx, dy);
              if ((isGetMaterial ? mgetList : mList).isEmpty()) return;
              if (isLoading) return;
              int visibleItemCount = mLayoutManager.getChildCount();
              int totalItemCount = mLayoutManager.getItemCount();
              int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
              if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                // End of list
                if (!isLastPage) {
                  Log.d(TAG, "onScrolled " + isLastPage + isLoading);
                  if (isGetMaterial) {
                    getReceiveComplainMaterialFromService(mCurrentnoOfRows);
                  } else {
                    getRCMaterialFromService(mCurrentnoOfRows);
                  }
                }
              }
            }
          });

      btn_material_save.setOnClickListener(_OnClickListener);
      AppUtils.closeInput(cl_main);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void submitQuery(String query) {
    Log.d(TAG, "submitQuery");

    try {
      AppUtils.closeInput(cl_main);
      if (TextUtils.isEmpty(query)) {
        AppUtils.showDialog(mActivity, "Search query Empty");
      } else if (receiveComplaintViewEntity == null) {
        AppUtils.showDialog(mActivity, "Opco Data unavailable");
      } else {
        isLoading = true;
        mList.clear();
        mgetList.clear();
        if (adapter != null) adapter.notifyDataSetChanged();
        isGetMaterial = false;
        mCurrentnoOfRows = 0;
        mQuery = query;
        mOpco = receiveComplaintViewEntity.getOpco();

        adapter = new RCMaterialListAdapter(mActivity, mList, mgetList);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        adapter.setGetMaterial(isGetMaterial);
        adapter.notifyDataSetChanged();

        getRCMaterialFromService(mCurrentnoOfRows);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void postSaveMaterialData() {
    Log.d(TAG, "postSaveMaterialData");
    try {
      boolean network_available =
              checkInternet(getContext());
      saveMaterialRequests = new ArrayList<>();
      if (isGetMaterial) {
        List<SaveMaterialEntity> mSavegetList = adapter.onRCMaterialUpdate();
        if (mSavegetList != null) {
          for (int i = 0; i < mSavegetList.size(); i++) {
            SaveMaterialEntity entity = mSavegetList.get(i);
            SaveMaterialEntity request = new SaveMaterialEntity();

            if (entity != null
                && entity.getItemQuantity() != null
                && !TextUtils.isEmpty(entity.getItemQuantity().trim())) {
              request.setCompanyCode(entity.getCompanyCode());
              request.setTransactionType(isMaterialRequired ? AppUtils.REQUIRED : AppUtils.USED);
              request.setDocType("R");
              request.setComplainSite(receiveComplaintViewEntity.getComplaintSite());
              request.setComplaintNumber(receiveComplaintViewEntity.getComplaintNumber());
              request.setJobNumber(receiveComplaintViewEntity.getJobNumber());
              request.setItemCode(entity.getItemCode());
              request.setItemQuantity(entity.getItemQuantity());
              request.setRemarks(entity.getRemarks());
              request.setItemDescription(entity.getItemDescription());
              request.setUser(mStrEmpId);

              request.setMode(network_available ? AppUtils.MODE_SERVER : AppUtils.MODE_LOCAL);

              saveMaterialRequests.add(request);
            } else Log.d(TAG, TAG + entity.toString());
          }
        }
      } else {
        List<MaterialMasterEntity> mSaveList = adapter.onRCMaterialInsert();
        if (mSaveList != null) {
          for (int i = 0; i < mSaveList.size(); i++) {
            MaterialMasterEntity entity = mSaveList.get(i);
            SaveMaterialEntity request = new SaveMaterialEntity();

            if (entity != null
                && entity.getItemQuantity() != null
                && !TextUtils.isEmpty(entity.getItemQuantity().trim())) {
              request.setCompanyCode(entity.getOpco());
              request.setTransactionType(isMaterialRequired ? AppUtils.REQUIRED : AppUtils.USED);
              request.setDocType("R");
              request.setComplainSite(receiveComplaintViewEntity.getComplaintSite());
              request.setComplaintNumber(receiveComplaintViewEntity.getComplaintNumber());
              request.setJobNumber(receiveComplaintViewEntity.getJobNumber());
              request.setItemCode(entity.getMaterialCode());
              request.setItemQuantity(entity.getItemQuantity());
              request.setRemarks(entity.getRemarks());
              request.setItemDescription(entity.getMaterialName());
              request.setUser(mStrEmpId);

              request.setMode(network_available ? AppUtils.MODE_SERVER : AppUtils.MODE_LOCAL);

              saveMaterialRequests.add(request);
            }
          }
        }
      }
      Log.d(TAG, " saveMaterialRequests " + saveMaterialRequests.size());

      if (saveMaterialRequests.isEmpty()) {
        AppUtils.showDialog(mActivity, "Please Enter Quantity");
      } else {

        if (network_available) {
          AppUtils.showProgressDialog(mActivity, "Saving Material...", false);
          rcMaterial_service.PostReceiveComplaintSaveMaterialData(saveMaterialRequests);
        } else {

          onRCSaveMaterialSuccess("Saved Successfully", AppUtils.MODE_LOCAL);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setupActionBar() {
    mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
    tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
    tv_toolbar_title.setText(
        isMaterialRequired ? R.string.lbl_material_reqd : R.string.lbl_material_used);
    /* mToolbar.setTitle(
    getResources()
        .getString(
            isMaterialRequired ? R.string.lbl_material_reqd : R.string.lbl_material_used));*/
    mActivity.setSupportActionBar(mToolbar);
    mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    mToolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // mActivity.onBackPressed();
            mManager.popBackStack();
          }
        });
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

  public void getRCMaterialFromService(int mCurrentnoOfRows) {
    Log.d(TAG, "getReceiveComplainFromService");

    try {
      if ((isGetMaterial ? mgetList : mList).isEmpty()) {
        showLoadingMaterial();
      }
      isLoading = true;
      if (checkInternet(getContext())) {
        rcMaterial_service.getRCMeterialRequiredListData(mOpco, mQuery, mCurrentnoOfRows + "");
      } else {
        new RCMaterialDbInitializer(mActivity, this, mQuery, mCurrentnoOfRows)
            .execute(AppUtils.MODE_GET);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getReceiveComplainMaterialFromService(int mCurrentnoOfRows) {
    Log.d(TAG, "getReceiveComplainFromService");

    try {
      if ((isGetMaterial ? mgetList : mList).isEmpty()) {
        showLoadingMaterial();
      }
      isLoading = true;
      if (checkInternet(getContext())) {
        rcMaterial_service.getRCMeterialListData(
            mComplaintNumber,
            isMaterialRequired ? AppUtils.REQUIRED : AppUtils.USED,
            mCurrentnoOfRows + "");
      } else {
        new RCSavedMaterialDbInitializer(
                mActivity,
                this,
                isMaterialRequired ? AppUtils.REQUIRED : AppUtils.USED,
                mComplaintNumber,
                receiveComplaintViewEntity.getComplaintSite(),
                receiveComplaintViewEntity.getOpco(),
                mCurrentnoOfRows)
            .execute(AppUtils.MODE_GET);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setRCMaterialList(boolean isGetMaterial) {
    Log.d(TAG, "setRCMaterialList");

    try {
      Log.w(TAG, +mgetList.size() + " mList : " + mList.size());
      if ((isGetMaterial ? mgetList.size() : mList.size()) > 0) {
        adapter.setGetMaterial(isGetMaterial);
        adapter.notifyDataSetChanged();
        showList();
      } else {
        showEmptyView(getString(R.string.msg_material_empty));
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void showList() {
    if (recyclerView != null) recyclerView.setVisibility(View.VISIBLE);
    if (layout_loading != null) layout_loading.setVisibility(View.GONE);
    if (layout_empty != null) layout_empty.setVisibility(View.GONE);
    if (btn_material_save != null) btn_material_save.setVisibility(View.VISIBLE);
  }

  public void showLoadingMaterial() {
    Log.d(TAG, "showLoadingMaterial");
    try {
      if (layout_empty != null) layout_empty.setVisibility(View.GONE);
      if (layout_loading != null) layout_loading.setVisibility(View.VISIBLE);
      if (recyclerView != null) recyclerView.setVisibility(View.GONE);
      if (btn_material_save != null) btn_material_save.setVisibility(View.GONE);
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
        Fragment _fragment = new MainLandingUI();
        FragmentTransaction _transaction = mManager.beginTransaction();
        _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        _transaction.replace(R.id.frame_container, _fragment);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Log.d(TAG, " onConfigurationChanged :");

    // Checks whether a hardware keyboard is available
    if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
      if (btn_material_save != null) {
        if ((isGetMaterial ? mgetList.size() : mList.size()) > 0) {
          btn_material_save.setVisibility(View.VISIBLE);
        }
      }
      Log.d(TAG, " mSavedInstanceState :btn_material_save");
    } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
      if (btn_material_save != null) btn_material_save.setVisibility(View.GONE);
      Log.d(TAG, " mSavedInstanceState :GONE");
    }
  }

  @Override
  public void onRCSaveMaterialSuccess(String strMsg, int from) {
    try {
      new RCSavedMaterialDbInitializer(mActivity, this, saveMaterialRequests)
          .execute(AppUtils.MODE_INSERT_SINGLE);

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
                      mManager.popBackStack();
                    }
                  });

      MaterialDialog dialog = builder.build();
      dialog.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onRCMaterialListReceived(
      List<MaterialMasterEntity> materialRequiredEntities, String noOfRows, int from) {


    try {
      isLoading = false;
      AppUtils.hideProgressDialog();
      if (mList != null) {
        mCurrentnoOfRows = mList.size();
      } else {
        mList = new ArrayList<>();
      }
      if (noOfRows != null) totalnoOfRows = Integer.valueOf(noOfRows);
      if (materialRequiredEntities.size() > 0) {
        mList.addAll(materialRequiredEntities);
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
        isGetMaterial = false;
        setRCMaterialList(isGetMaterial);
      } else Log.d(TAG, "onComplaintReceivedSuccess data Not found");
      Log.d(TAG, mCurrentnoOfRows + " onComplaintReceivedSuccess totalnoOfRows " + totalnoOfRows);
      if (!mList.isEmpty() && from == AppUtils.MODE_SERVER)
        new RCMaterialDbInitializer(mActivity, this, mList).execute(AppUtils.MODE_INSERT);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void onRCMaterialGetListReceived(
      List<SaveMaterialEntity> getMaterialEntities, String noOfRows, int from) {

    try {
      Log.d(TAG, mCurrentnoOfRows + " onRCMaterialGetListReceived totalnoOfRows " + totalnoOfRows);
      isLoading = false;
      AppUtils.hideProgressDialog();
      if (mgetList != null) {
        mCurrentnoOfRows = mgetList.size();
      } else {
        mgetList = new ArrayList<>();
      }
      if (noOfRows != null) totalnoOfRows = Integer.valueOf(noOfRows);
      if (getMaterialEntities.size() > 0) {
        mgetList.addAll(getMaterialEntities);
        mCurrentnoOfRows = mgetList.size();
        if (mCurrentnoOfRows > 1) {
          if (totalnoOfRows <= mCurrentnoOfRows) {
            isLastPage = true;
            adapter.enableFooter(false);
          } else {
            isLastPage = false;
            adapter.enableFooter(true);
          }
        }
        isGetMaterial = true;
        setRCMaterialList(isGetMaterial);

        if (!mgetList.isEmpty() && from == AppUtils.MODE_SERVER) {
          new RCSavedMaterialDbInitializer(mActivity, this, mgetList)
              .execute(AppUtils.MODE_INSERT_SINGLE);
        }
      } else Log.d(TAG, "onRCMaterialGetListReceived data Not found");

      Log.d(TAG, mCurrentnoOfRows + " onRCMaterialGetListReceived totalnoOfRows " + totalnoOfRows);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void onRCMaterialListReceivedError(String strErr, int from) {

    try {
      AppUtils.hideProgressDialog();
      showEmptyView(strErr);
      setRCMaterialList(isGetMaterial);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
