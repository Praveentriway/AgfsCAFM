package com.daemon.emco_android.ui.fragments.inspection;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.activities.LoginActivity;
import com.daemon.emco_android.ui.fragments.inspection.assetverification.AssetVerificationFragment;
import com.daemon.emco_android.ui.fragments.survey.SurveyHeader;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.SessionManager;
import com.daemon.emco_android.utils.Utils;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import static com.daemon.emco_android.utils.AppUtils.ARGS_ASSETTYPE;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYTYPE;
import static com.daemon.emco_android.utils.AppUtils.CLIENT;
import static com.daemon.emco_android.utils.AppUtils.CUSTOMER;

public class Fragment_InspectionModule_Landing extends Fragment implements View.OnClickListener {
  private static final String TAG = Fragment_InspectionModule_Landing.class.getSimpleName();

  private AppCompatActivity mActivity;
  private Context mContext;
  private Font font = App.getInstance().getFontInstance();

  private CardView btn_rx_inspection, btn_ppm_inspection, btn_periodic_inspection,btn_asset_verification;

  private TextView tv_toolbar_title;
  private CoordinatorLayout cl_main;
  private Toolbar mToolbar;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    try {
      mActivity = (AppCompatActivity) getActivity();
      mContext = mActivity;
      setHasOptionsMenu(true);
      font = App.getInstance().getFontInstance();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View rootView = null;
    try {
      rootView =
          (View) inflater.inflate(R.layout.fragment_inspection_landing_page, container, false);
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
      btn_rx_inspection = (CardView) rootView.findViewById(R.id.btn_rx_inspection);
      btn_ppm_inspection = (CardView) rootView.findViewById(R.id.btn_ppm_inspection);
      btn_periodic_inspection = (CardView) rootView.findViewById(R.id.btn_periodic_inspection);
      btn_asset_verification= (CardView) rootView.findViewById(R.id.btn_asset_verification);
      setupActionBar();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setupActionBar() {
    mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
    tv_toolbar_title=(TextView)mToolbar.findViewById(R.id.tv_toolbar_title);
    LinearLayout  linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
    linear_toolbar.setVisibility(View.GONE);
    tv_toolbar_title.setText("Inspection");
    mActivity.setSupportActionBar(mToolbar);
    mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        getActivity().getSupportFragmentManager().popBackStack();
      }
    });
  }
  private void setProperties() {

    btn_rx_inspection.setOnClickListener(this);
    btn_ppm_inspection.setOnClickListener(this);
    btn_periodic_inspection.setOnClickListener(this);
    btn_asset_verification.setOnClickListener(this);

    AppUtils.closeInput(cl_main);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_rx_inspection:
     //   gotoIMServices(false, getString(R.string.title_reactive_request_verification));
        break;
      case R.id.btn_ppm_inspection:
       // gotoIMServices(true, getString(R.string.title_ppm_request_verification));
        break;
      case R.id.btn_periodic_inspection:
       // loadFragment(new Fragment_IM_Periodic(), Utils.TAG_IM_PERIODIC);
        break;
      case R.id.btn_asset_verification:
        // loadFragment(new Fragment_IM_Periodic(), Utils.TAG_IM_PERIODIC);
        loadFragment(new AssetVerificationFragment(), Utils.TAG_F_ASSET_VERIFICATION);
      //  showSurvey();

        break;
    }
  }

  private void gotoIMServices(boolean ppe, String title) {

    Bundle data = new Bundle();
    Fragment_IM_Services complaintsList = new Fragment_IM_Services();
    data.putBoolean(AppUtils.ARGS_IM_PPE_Page, ppe);
    data.putString(AppUtils.ARGS_IM_SERVICES_Page_TITLE, title);
    complaintsList.setArguments(data);
    loadFragment(complaintsList, Utils.TAG_IM_SERVICES);
  }

  public void loadFragment(final Fragment fragment, final String tag) {

    FragmentTransaction fragmentTransaction =
        mActivity.getSupportFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.frame_container, fragment, tag);
    fragmentTransaction.addToBackStack(tag);
    fragmentTransaction.commit();
  }

  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    menu.findItem(R.id.action_logout).setVisible(false);
    menu.findItem(R.id.action_home).setVisible(true);
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


}
