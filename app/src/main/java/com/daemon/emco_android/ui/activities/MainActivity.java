package com.daemon.emco_android.ui.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import com.daemon.emco_android.BuildConfig;
import com.daemon.emco_android.WorkManager.EmployeeGPSTracking;
import com.daemon.emco_android.service.EmployeeTrackingService;
import com.daemon.emco_android.ui.fragments.user.ChangePassword;
import com.daemon.emco_android.ui.fragments.user.UserProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.crashlytics.android.Crashlytics;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.FeedBackRepository;
import com.daemon.emco_android.repository.remote.GetPostRateServiceService;
import com.daemon.emco_android.repository.remote.PostLogComplaintService;
import com.daemon.emco_android.repository.remote.PpeService;
import com.daemon.emco_android.repository.remote.RCMaterialService;
import com.daemon.emco_android.repository.remote.ReceiveComplaintRespondService;
import com.daemon.emco_android.repository.remote.ReceiveComplaintViewService;
import com.daemon.emco_android.repository.remote.UserService;
import com.daemon.emco_android.repository.db.database.AppDatabase;
import com.daemon.emco_android.repository.db.dbhelper.DefectDoneImageDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.FeedbackDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.LogComplaintDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.PPEFetchSaveDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.RCRespondDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.RCSavedMaterialDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ReceiveComplaintItemDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.ReceiveComplaintViewDbInitializer;
import com.daemon.emco_android.repository.db.dbhelper.SaveRatedServiceDbInitializer;
import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;
import com.daemon.emco_android.repository.db.entity.EmployeeDetailsEntity;
import com.daemon.emco_android.repository.db.entity.LogComplaintEntity;
import com.daemon.emco_android.repository.db.entity.MaterialMasterEntity;
import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.daemon.emco_android.repository.db.entity.PPENameEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntity;
import com.daemon.emco_android.repository.db.entity.SaveMaterialEntity;
import com.daemon.emco_android.repository.db.entity.SaveRatedServiceEntity;
import com.daemon.emco_android.repository.db.entity.UserToken;
import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.ui.fragments.reactive.FragmentRxSubmenu;
import com.daemon.emco_android.listeners.DefectDoneImage_Listener;
import com.daemon.emco_android.listeners.FeedbackListener;
import com.daemon.emco_android.listeners.LogComplaint_Listener;
import com.daemon.emco_android.listeners.Material_Listener;
import com.daemon.emco_android.listeners.PpeListener;
import com.daemon.emco_android.listeners.RateAndShareListener;
import com.daemon.emco_android.listeners.RatedServiceListener;
import com.daemon.emco_android.listeners.ReceivecomplaintView_Listener;
import com.daemon.emco_android.listeners.UserListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.EmployeeIdRequest;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.DefectDoneImageUploaded;
import com.daemon.emco_android.model.response.PpmEmployeeFeedResponse;
import com.daemon.emco_android.model.response.RCDownloadImage;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ConnectivityStatus;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.SessionManager;
import com.daemon.emco_android.utils.Utils;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static com.daemon.emco_android.ui.activities.LoginActivity.IP_ADDRESS;
import static com.daemon.emco_android.ui.activities.LoginActivity.MBM;
import static com.daemon.emco_android.utils.GpsUtils.isNetworkConnected;

public class MainActivity  extends AppCompatActivity
        implements  UserListener , View.OnClickListener  {
  private final String TAG = MainActivity.class.getSimpleName();

  private final int PERMISSION_REQUEST_CODE = 1;
  private ApiInterface mInterface;
  private Bundle mArgs;
  boolean updateShow=false;
  DrawerLayout drawer;
  Intent  mServiceIntent;
  private AppBarConfiguration mAppBarConfiguration;
  private Handler mHandler;
  private SharedPreferences mPreferences;
  private SharedPreferences.Editor mEditor;
  private AppCompatActivity mActivity;
  private Font font = App.getInstance().getFontInstance();
  private FragmentManager mManager;
  private Context mContext;
  private FrameLayout frame_container;
  private Drawable drawableLogout;
  private String mLoginData = null;
  private String mStrEmpId = null;
  private String trackingFlag = null;
  private String username = null;
  private String email = null;
  private String mobile = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    try {
      setContentView(R.layout.activity_main2);
      mActivity = this;
      mContext = getApplicationContext();
      mPreferences = this.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
      mEditor = mPreferences.edit();
      mHandler = new Handler();
      mManager = getSupportFragmentManager();
      mArgs = getIntent().getExtras();

      frame_container = (FrameLayout) findViewById(R.id.frame_container);
      drawableLogout = getResources().getDrawable(R.drawable.ic_logout);
      drawableLogout.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

      StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

      mInterface =
              ApiClient.getClientLongTime(15, SessionManager.getSession("baseurl", mActivity))
                      .create(ApiInterface.class);

      setupActionbar();
      loadFragment();
      logUser();

      setupDrawer();


      if (!checkLocationPermission()) {
        ActivityCompat.requestPermissions(this,
                new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
      }
      {
        statusCheck();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  private void logUser() {

    try {
      mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
      if (mLoginData == null) return;
      Gson gson = new Gson();
      Login login = gson.fromJson(mLoginData, Login.class);
      mStrEmpId = login.getEmployeeId();
      trackingFlag=login.getTrackingFlag();
      username = login.getFirstName().replace("\n", "").replace("\r", "");;
      email = login.getEmailId();
      mobile = login.getMobileNumber();
      Crashlytics.setUserIdentifier(mStrEmpId);
      Crashlytics.setUserEmail(login.getEmailId());
      Crashlytics.setUserName(login.getUserType() + login.getUserName());
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
    }
  }
  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    return NavigationUI.navigateUp(navController, mAppBarConfiguration)
            || super.onSupportNavigateUp();
  }

  protected void onPause() {

    super.onPause();
  }

  @Override
  protected void onStop() {

    super.onStop();
  }

  protected void onDestroy() {
    mServiceIntent = new Intent(this, EmployeeTrackingService.class);
    if (new AppUtils(getApplicationContext()).isMyServiceRunning( EmployeeTrackingService.class,this)) {
      stopService(mServiceIntent);
    }

    Log.d(TAG, "onDestroy");
    super.onDestroy();
  }

  private void setupActionbar() {

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

    final ImageView imageInToolbar = (ImageView) toolbar.findViewById(R.id.img_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    if(SessionManager.getSessionForURL(IP_ADDRESS,this) !=null
            && (!SessionManager.getSessionForURL(IP_ADDRESS,this).trim().isEmpty())
            && (SessionManager.getSessionForURL(IP_ADDRESS,this).contains(MBM))){

      imageInToolbar.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.logo_mbm_png));
    }
    else{
      imageInToolbar.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ag_logo_white));
    }
    TextView titleTextView = null;
    try {
      Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
      f.setAccessible(true);
      titleTextView = (TextView) f.get(toolbar);
      titleTextView = (TextView) f.get(toolbar);
      titleTextView.setTypeface(font.getHelveticaRegular());
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public void loadFragment() {

    setupActionbar();
    // update the main content by replacing fragments
    Fragment fragment = new MainLandingUI();
    fragment.setArguments(mArgs);
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
    fragmentTransaction.replace(R.id.frame_container, fragment, AppUtils.TAG_FRAGMNENT_MAIN);
    fragmentTransaction.commit();

  }

  @Override
  public void onClick(View view) {

    drawer.closeDrawer(GravityCompat.START);

    switch (view.getId()) {
      case R.id.txt_profile:
        loadFragment(new UserProfile(), Utils.TAG_VIEW_PROFILE);
        break;
      case R.id.txt_home:
      {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
          fm.popBackStack();
        }
        Fragment _fragment = new MainLandingUI();
        FragmentTransaction _transaction = mManager.beginTransaction();
        _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        _transaction.replace(R.id.frame_container, _fragment);
      }
        break;
      case R.id.txt_change_password:
         loadFragment(new ChangePassword(), Utils.TAG_CHANGE_PASS);
        break;
      case R.id.txt_logout:

        logout();

        break;

       default:
        break;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    menu.findItem(R.id.action_search).setVisible(false);
    menu.findItem(R.id.action_refresh).setVisible(false);
    menu.findItem(R.id.action_home).setVisible(false);
    menu.findItem(R.id.action_logout).setVisible(false);
    menu.findItem(R.id.action_logout).setIcon(drawableLogout);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_logout:
        Log.d(TAG, "logout pressed " + item.getTitle());
        try {
          MaterialDialog.Builder builder =
                  new MaterialDialog.Builder(mActivity)
                          .content("Are you sure you want to logout?")
                          .title("Logout")
                          .positiveText(R.string.lbl_yes)
                          .negativeText(R.string.lbl_cancel)
                          .stackingBehavior(StackingBehavior.ADAPTIVE)
                          .onPositive(
                                  new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(
                                            @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                      dialog.dismiss();
                                      clearPreferences();
                                      SessionManager.clearSession(mActivity);
                                      clearToken();

                                      Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                      startActivity(intent);
                                      finish();
                                    }
                                  });

          MaterialDialog dialog = builder.build();
          dialog.show();
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      case R.id.action_refresh:

        break;
      case R.id.action_home:

        break;
    }
    return super.onOptionsItemSelected(item);
  }

  String token;

  public void clearToken(){

    FirebaseInstanceId.getInstance().getInstanceId()
            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
              @Override
              public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                  Log.w(TAG, "getInstanceId failed", task.getException());
                  return;
                }
                token = task.getResult().getToken();

                deleteToken();
              }
            });
  }

  public void deleteToken(){
    UserToken user=new UserToken();
    user.setUserId(mStrEmpId);
    user.setToken(token);
    new UserService(MainActivity.this, this).deleteToken(user);

  }


  public void clearPreferences() {
    Log.d(TAG, "clearPreferences");
    try {
      mEditor = mPreferences.edit();
      mEditor
              .putString(
                      AppUtils.SHARED_LOGIN_OFFLINE, mPreferences.getString(AppUtils.SHARED_LOGIN, null))
              .commit();
      mEditor.putString("day", null).commit();
      mEditor.putString(AppUtils.SHARED_LOGIN, null).commit();
      mEditor.putString(AppUtils.SHARED_CUSTOMER_LC, null).commit();
      new ReceiveComplaintItemDbInitializer(mActivity, null, null).execute(AppUtils.MODE_DELETE);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    List<Fragment> fragments = mManager.getFragments();
    if (fragments != null) {
      for (Fragment fragment : fragments) {
        fragment.onActivityResult(requestCode, resultCode, data);
      }
    }
  }

  @Override
  public void onBackPressed() {
    Fragment main = mManager.findFragmentByTag(AppUtils.TAG_FRAGMNENT_MAIN);
    Fragment reactive_main = mManager.findFragmentByTag(Utils.TAG_REACTIVE_MAINTENANCE);
    Fragment receive_complaintlist = mManager.findFragmentByTag(Utils.TAG_RECEIVED_COMPALINTS);
    if (main != null) {
      if (main.isVisible()) {
        // exit your application
        MainActivity.this.finish();
        return;
      }
    }
    if (reactive_main != null) {
      if (reactive_main.isVisible()) {
        super.onBackPressed();

        return;
      }
    }
    if (receive_complaintlist != null) {
      if (receive_complaintlist.isVisible()) {
        loadFragment(new FragmentRxSubmenu(), Utils.TAG_REACTIVE_MAINTENANCE);
        return;
      }
    }

    if (getFragmentManager().getBackStackEntryCount() > 1) {
      Log.d(
              TAG,
              "getFragmentManager().getBackStackEntryCount() "
                      + getFragmentManager().getBackStackEntryCount());
      getFragmentManager().popBackStack();
    } else {
      Log.d(TAG, "super.onBackPressed");
      super.onBackPressed();
    }
  }

  public void loadFragment(final Fragment fragment, final String tag) {
    Log.d(TAG, "loadFragment");
    try {
      Runnable mPendingRunnable =
              new Runnable() {
                @Override
                public void run() {
                  // update the main content by replacing fragments
                  FragmentTransaction fragmentTransaction =
                          mActivity.getSupportFragmentManager().beginTransaction();
                  fragmentTransaction.replace(R.id.frame_container, fragment, tag);
                  fragmentTransaction.addToBackStack(tag);
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

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Log.d(TAG, " onConfigurationChanged :");
  }

  private boolean checkPermission() {
    int result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    if (result == PackageManager.PERMISSION_GRANTED) {
      return true;
    } else {
      return false;
    }
  }


  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  protected void onResume() {

    if (!isNetworkConnected(mContext)) {
      buildAlertMessageNoGps(getResources().getString(R.string.no_internet_msg));
    }


    if (!updateShow) {

      new AppUpdater(this)
              .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
              .setDisplay(Display.DIALOG)
              .setButtonDoNotShowAgain(null).setCancelable(false).setButtonDismiss(null)
              .showAppUpdated(false)
              .setButtonUpdateClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                  clearPreferences();
                  SessionManager.clearSession(mActivity);
                  clearToken();

                  mServiceIntent = new Intent(MainActivity.this, EmployeeTrackingService.class);

                  if (new AppUtils(getApplicationContext()).isMyServiceRunning( EmployeeTrackingService.class,MainActivity.this)) {
                    stopService(mServiceIntent);
                  }

                  updateShow = false;

                  final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                  try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                  } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                  }

                }

              }).start();

    }

    updateShow = true;
    super.onResume();
  }

   @Override
  public void onLoginDataReceivedSuccess(Login login, String totalNumberOfRows){
  }
  @Override
  public void onUserDataReceivedSuccess(CommonResponse response){

  }
  @Override
  public void onUserDataReceivedFailure(String strErr){

  }

  public void onDrawerOpen() {
   drawer.openDrawer(GravityCompat.START);
  }


  /**
   * All about permission
   */
  private boolean checkLocationPermission() {
    int result3 = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);
    int result4 = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
    int result5 = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE );
    return result3 == PackageManager.PERMISSION_GRANTED &&
            result4 == PackageManager.PERMISSION_GRANTED && result5== PackageManager.PERMISSION_GRANTED;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == PERMISSION_REQUEST_CODE) {
      if (grantResults.length > 0) {
        boolean coarseLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        boolean fineLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
        boolean phonestate = grantResults[2] == PackageManager.PERMISSION_GRANTED;
        if (coarseLocation && fineLocation && phonestate){
          statusCheck();
        }

        else {

          AppUtils.showDialog(this,getResources().getString(R.string.permission_msg));
        }
      }
    }
  }

  public String GPS_MSG="Your GPS seems to be disabled, Please enable it.";

  public void statusCheck() {
    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      buildAlertMessageNoGps(GPS_MSG);
    }
  }

  private void buildAlertMessageNoGps(final String msg) {
    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
    builder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
              public void onClick(final DialogInterface dialog, final int id) {

                dialog.cancel();

                if(msg==GPS_MSG){
                  startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                else{
                  startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                }

              }
            })
            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
              public void onClick(final DialogInterface dialog, final int id) {
                dialog.cancel();
              }
            });
    final android.app.AlertDialog alert = builder.create();
    alert.show();
  }


  public void logout(){

    try {

      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage("Are you sure you want to logout?")
              .setCancelable(false)
              .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  dialog.dismiss();
                  clearPreferences();
                  SessionManager.clearSession(mActivity);
                  clearToken();

                  Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                  startActivity(intent);
                  finish();
                }
              })
              .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  dialog.cancel();
                }
              });
      AlertDialog alert = builder.create();
      alert.show();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }


  public void setupDrawer(){drawer = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.nav_view);
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    mAppBarConfiguration = new AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
            R.id.nav_tools, R.id.nav_share, R.id.nav_send)
            .setDrawerLayout(drawer)
            .build();

    // View hView =  navigationView.getHeaderView(0);
    TextView nav_user = (TextView)navigationView.findViewById(R.id.txtusername);
    TextView nav_email = (TextView)navigationView.findViewById(R.id.txt_email);
    TextView txt_appversion = (TextView)navigationView.findViewById(R.id.txt_appversion);

    TextView txt_home = (TextView)navigationView.findViewById(R.id.txt_home);
    TextView txt_profile = (TextView)navigationView.findViewById(R.id.txt_profile);
    TextView txt_logout = (TextView)navigationView.findViewById(R.id.txt_logout);
    TextView txt_change_password = (TextView)navigationView.findViewById(R.id.txt_change_password);

    txt_home.setOnClickListener(this);
    txt_profile.setOnClickListener(this);
    txt_logout.setOnClickListener(this);
    txt_change_password.setOnClickListener(this);

    txt_appversion.setText(getString(R.string.app_name)+" V "+ BuildConfig.VERSION_NAME);
    nav_user.setText(username+" | "+mobile);
    nav_email.setText(email);}


}
