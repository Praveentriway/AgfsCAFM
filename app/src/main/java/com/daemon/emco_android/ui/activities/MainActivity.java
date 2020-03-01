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

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static com.daemon.emco_android.utils.GpsUtils.isNetworkConnected;

public class MainActivity  extends AppCompatActivity
        implements LogComplaint_Listener,
        ReceivecomplaintView_Listener,
        DefectDoneImage_Listener,
        PpeListener,
        FeedbackListener,
        Material_Listener,
        RatedServiceListener,
        RateAndShareListener , UserListener , View.OnClickListener  {
  private final String TAG = MainActivity.class.getSimpleName();

  private final String WORKMANAGER_TAG = "TRACKING_MANAGER";
  private final int PERMISSION_REQUEST_CODE = 1;
  private String mNetworkInfo = null;
  private boolean isLoading = false;
  private boolean isReceiver = false;
  private Bundle mArgs;
  boolean updateShow=false;
  private TextView tv_toolbar_title;
  private ApiInterface mInterface;
  DrawerLayout drawer;
  Intent  mServiceIntent;
  private RCRespondDbInitializer complaintRespondDbInitializer;
  private PPEFetchSaveDbInitializer ppeFetchSaveDbInitializer;
  private PostLogComplaintService logComplaint_service;
  private ReceiveComplaintViewService complaintView_service;
  private ReceiveComplaintRespondService complaintRespond_service;
  private PpeService ppeService;
  private FeedBackRepository feedBackService;
  private RCMaterialService rcMaterial_service;
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
  private GetPostRateServiceService mGetPostRateService;
  ActionBarDrawerToggle mDrawerToggle;

  private static final int PERMISSION_REQUEST_CODE_LCOATION = 200;


  private BroadcastReceiver receiver =
          new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

              Log.d("BroadcastReceiver", "onReceive");
              try {
                if (!ConnectivityStatus.isConnected(MainActivity.this)) {
                  Log.d(TAG, "not connected");
                  mEditor.putString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE);
                  mEditor.commit();
                  mNetworkInfo = AppUtils.NETWORK_NOT_AVAILABLE;
                  isLoading = false;
                } else {
                  Log.d(TAG, "connected");
                  mEditor.putString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_AVAILABLE);
                  mEditor.commit();
                  mNetworkInfo = AppUtils.NETWORK_AVAILABLE;
                  Log.d(TAG, "isLoading " + isLoading);
                  if (isLoading) return;
                  isLoading = true;
                  updateLogComplaint();
                  updateReceiveComplaintRespond();
                  updateReceiveComplaintSaveImage();
                  updatePPE();
                  updateFeedback();
                  updateMaterial();
                  updateRatedService();
                  updateReceiveComplaintRespondSave();

                  if (mPreferences.getString("day", null) != null) {
                    Date dateTime = null;
                    try {
                      dateTime =
                              new SimpleDateFormat("dd-MMM-yyyy")
                                      .parse(mPreferences.getString("day", "22-JAN-2011"));
                    } catch (ParseException e) {
                      e.printStackTrace();
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateTime);
                    Calendar today = Calendar.getInstance();

                    if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                            && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                      // "Today "
                    } else {
                      //   startService();
                      mPreferences
                              .edit()
                              .putString("day", new SimpleDateFormat("dd-MMM-yyyy").format(new Date()))
                              .commit();
                    }
                  } else {
                    //  startService();
                    mPreferences
                            .edit()
                            .putString("day", new SimpleDateFormat("dd-MMM-yyyy").format(new Date()))
                            .commit();
                  }
                }

              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");
    try {
      setContentView(R.layout.activity_main2);
      mActivity = this;
      mContext = getApplicationContext();
      mPreferences = this.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
      mEditor = mPreferences.edit();
      mHandler = new Handler();
      mManager = getSupportFragmentManager();
      mArgs = getIntent().getExtras();

      logComplaint_service = new PostLogComplaintService(this, this);
      complaintView_service = new ReceiveComplaintViewService(this, this);
      complaintRespond_service = new ReceiveComplaintRespondService(this);
      complaintRespond_service.setmCallbackImages(this);
      ppeService = new PpeService(this, this);
      feedBackService = new FeedBackRepository(mActivity, this);

      frame_container = (FrameLayout) findViewById(R.id.frame_container);
      drawableLogout = getResources().getDrawable(R.drawable.ic_logout);
      drawableLogout.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

      StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

      mInterface =
              ApiClient.getClientLongTime(15, SessionManager.getSession("baseurl", mActivity))
                      .create(ApiInterface.class);

//      // only one time register connectivity register
//      if (!isReceiver) {
//        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//      } else isReceiver = false;
      setupActionbar();
      loadFragment();
      logUser();


       drawer = findViewById(R.id.drawer_layout);
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
      nav_email.setText(email);

//      // start tracking service
//      if(trackingFlag!=null && trackingFlag.equalsIgnoreCase("Y")){
//        startTracking();
//      }
      if (!checkLocationPermission()) {
        ActivityCompat.requestPermissions(this,
                new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
      }
      {
        statusCheck();
      }

      // START Worker
//      Log.i("isWorkScheduled",isWorkScheduled(WORKMANAGER_TAG)+"");

//      if (!(isWorkScheduled(WORKMANAGER_TAG))) {
//
//        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(EmployeeGPSTracking.class, 15, TimeUnit.MINUTES)
//                .addTag(WORKMANAGER_TAG)
//                .build();
//        WorkManager.getInstance().enqueueUniquePeriodicWork("Location", ExistingPeriodicWorkPolicy.REPLACE, periodicWork);
//    //    Toast.makeText(MainActivity.this, "Location Worker Started : " + periodicWork.getId(), Toast.LENGTH_SHORT).show();
//      }
//      else{
//
//      }


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void logUser() {
    Log.d(TAG, "logUser");
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
    Log.d(TAG, "onPause");
    try {
      if (receiver != null) unregisterReceiver(receiver);
    } catch (Exception e) {
      e.printStackTrace();
    }
    super.onPause();
  }

  @Override
  protected void onStop() {
    Log.d(TAG, "onStop");
    super.onStop();
  }

  protected void onDestroy() {
    mServiceIntent = new Intent(this, EmployeeTrackingService.class);
    if (isMyServiceRunning( EmployeeTrackingService.class)) {
      stopService(mServiceIntent);
    }

    Log.d(TAG, "onDestroy");
    super.onDestroy();
  }

  private void setupActionbar() {
    Log.d(TAG, "setupActionbar");
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

    final ImageView imageInToolbar = (ImageView) toolbar.findViewById(R.id.img_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    if(SessionManager.getSessionForURL("ip_address",this) !=null && (!SessionManager.getSessionForURL("ip_address",this).trim().isEmpty())  && (SessionManager.getSessionForURL("ip_address",this).contains("mbm"))){

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
    Log.d(TAG, "loadFragment");
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
                      // stop tracking service
                     // WorkManager.getInstance().cancelAllWorkByTag(WORKMANAGER_TAG);
//                      mActivity.stopService(mServiceIntent);
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
        break;

       default:
        break;
    }
  }

  private void updateLogComplaint() {
    Log.d(TAG, "updateLogComplaint");
    new LogComplaintDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
  }

  private void updateReceiveComplaintRespond() {
    Log.d(TAG, "updateReceiveComplaintRespond");
    complaintRespondDbInitializer = new RCRespondDbInitializer(this);
    complaintRespondDbInitializer.populateAsync(
            AppDatabase.getAppDatabase(mActivity), AppUtils.MODE_GETALL);
  }

  private void updateReceiveComplaintSaveImage() {
    Log.d(TAG, "updateReceiveComplaintRespond");
    new DefectDoneImageDbInitializer(mActivity, null, this).execute(AppUtils.MODE_GETALL);
  }

  private void updatePPE() {
    Log.d(TAG, "updatePPE");
    ppeFetchSaveDbInitializer = new PPEFetchSaveDbInitializer(this);
    ppeFetchSaveDbInitializer.populateAsync(
            AppDatabase.getAppDatabase(mActivity), AppUtils.MODE_GETALL);
  }

  private void updateFeedback() {
    Log.d(TAG, "updatePPE");
    feedBackService = new FeedBackRepository(mActivity, this);
    new FeedbackDbInitializer(mActivity, this).execute(AppUtils.MODE_GETALL);
  }

  private void updateMaterial() {
    Log.d(TAG, "updateMaterial");
    rcMaterial_service = new RCMaterialService(mActivity, this);
    new RCSavedMaterialDbInitializer(mActivity, this, null).execute(AppUtils.MODE_GETALL);
  }

  private void updateRatedService() {
    Log.d(TAG, "updateRatedService");
    mGetPostRateService = new GetPostRateServiceService(mActivity, this);
    new SaveRatedServiceDbInitializer(mActivity, this, new SaveRatedServiceEntity())
            .execute(AppUtils.MODE_GETALL);
  }

  private void updateReceiveComplaintRespondSave() {
    Log.d(TAG, "updateReceiveComplaintRespondSave");
    new ReceiveComplaintViewDbInitializer(mActivity, this, new ReceiveComplaintViewEntity())
            .execute(AppUtils.MODE_GETALL);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    Log.d(TAG, "onCreateOptionsMenu");
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

                                      // stop tracking service

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
        // startService();
        Log.d(TAG, "search pressed " + item.getTitle());
        break;
      case R.id.action_home:
        Log.d(TAG, "search pressed " + item.getTitle());
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


  private void startTracking() {

    EmployeeTrackingService employeeTrackingService = new EmployeeTrackingService(this);
      mServiceIntent = new Intent(this, employeeTrackingService.getClass());
    if (!isMyServiceRunning(employeeTrackingService.getClass())) {
      mActivity.startService(mServiceIntent);
    }

  }

  private boolean isMyServiceRunning(Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
    if (manager != null) {
      for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.getName().equals(service.service.getClassName())) {
          Log.i ("isMyServiceRunning?", true+"");
          return true;
        }
      }
    }
    Log.i ("isMyServiceRunning?", false+"");
    return false;
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

      if (isServiceRunning()) {
      /*  Intent intent = new Intent(this, DownloadService.class);
        stopService(intent);*/
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void onLogComplaintDataReceivedSuccess(EmployeeIdRequest logComplaint, int mode) {
    Log.d(TAG, "onLogComplaintDataReceivedSuccess");
    if (mode == AppUtils.MODE_SERVER)
      new LogComplaintDbInitializer(this, new LogComplaintEntity(logComplaint.getEmailId()), this)
              .execute(AppUtils.MODE_DELETE);
  }

  @Override
  public void onLogComplaintDataReceivedFailure(String strErr, int mode) {
    Log.d(TAG, "onLogComplaintDataReceivedFailure " + strErr);
  }

  @Override
  public void onAllLogComplaintData(List<LogComplaintEntity> logComplaintEntities, int mode) {
    try {
      Log.d(TAG, "onAllLogComplaintData " + logComplaintEntities.size());
      if (mode == AppUtils.MODE_LOCAL) {
        for (LogComplaintEntity item : logComplaintEntities) {
          Log.d(TAG, "for ... " + item.getComplainWebNumber());
          try {
            postDataToServer(item);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void postDataToServer(LogComplaintEntity logComplaintRequest) {
    Log.d(TAG, "postDataToServer");
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        logComplaint_service.postLogComplaintData(logComplaintRequest);
      } else
        new LogComplaintDbInitializer(mActivity, logComplaintRequest, this)
                .execute(AppUtils.MODE_INSERT);
    }
  }

  private void postImageToServer(DFoundWDoneImageEntity saveRequest) {
    Log.d(TAG, "postImageToServer");
    mNetworkInfo = mPreferences.getString(AppUtils.IS_NETWORK_AVAILABLE, null);
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        complaintRespond_service.saveComplaintRespondImageData(saveRequest, mActivity);
      } else {
        saveRequest.setId(saveRequest.getComplaintNo() + saveRequest.getDocType());
        new DefectDoneImageDbInitializer(mActivity, saveRequest, this)
                .execute(AppUtils.MODE_INSERT);
      }
    }
  }

  private void postRespondDataToServer(ReceiveComplaintRespondEntity complaintRespondEntity) {
    Log.d(TAG, "postDataToServer");
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        complaintView_service.postReceiveComplaintViewData(complaintRespondEntity);
      } else
        complaintRespondDbInitializer.populateAsync(
                AppDatabase.getAppDatabase(mActivity), complaintRespondEntity, AppUtils.MODE_INSERT);
    }
  }

  private void postPPEDataToServer(List<PPEFetchSaveEntity> ppeFetchSaveEntities) {
    Log.d(TAG, "postDataToServer");
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        ppeService.savePpeData(ppeFetchSaveEntities);
      } else
        ppeFetchSaveDbInitializer.populateAsync(
                AppDatabase.getAppDatabase(mActivity), ppeFetchSaveEntities, AppUtils.MODE_INSERT);
    }
  }

  private void postFeedbackDataToServer(SaveFeedbackEntity saveFeedbackEntity) {
    Log.d(TAG, "postDataToServer");
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        feedBackService.postFeedbackDetailsData(saveFeedbackEntity);
      }
    }
  }

  private void postRatedServceToServer(SaveRatedServiceEntity entity) {
    Log.d(TAG, "postDataToServer");
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        mGetPostRateService.saveCustomerFeedbackRM(entity, this);
      }
    }
  }

  private void postMaterialDataToServer(List<SaveMaterialEntity> saveMaterialRequests) {
    Log.d(TAG, "postDataToServer");
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        rcMaterial_service.PostReceiveComplaintSaveMaterialData(saveMaterialRequests);
      }
    }
  }

  private void postReceiveComplaintRespondSaveToServer(ReceiveComplaintViewEntity entity) {
    Log.d(TAG, "postDataToServer");
    if (mNetworkInfo.length() > 0) {
      if (mNetworkInfo.equals(AppUtils.NETWORK_AVAILABLE)) {
        complaintRespond_service.getComplaintRespondSaveData(entity);
      }
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
        Log.d(TAG, "main.isVisible");
        // exit your application
        MainActivity.this.finish();
        return;
      }
    }
    if (reactive_main != null) {
      if (reactive_main.isVisible()) {
        Log.d(TAG, "super.onBackPressed");
        super.onBackPressed();

        return;
      }
    }
    if (receive_complaintlist != null) {
      if (receive_complaintlist.isVisible()) {
        Log.d(TAG, "receive_complaintlist.isVisible");
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
  public void onReceiveComplaintRemarksReceived(List<String> remarkList, int mode) {}

  @Override
  public void onReceiveComplaintRespondReceived(String strMsg, String complaintNumber, int mode) {
    Log.d(TAG, "onReceiveComplaintRespondReceived");
    if (mode == AppUtils.MODE_SERVER)
      complaintRespondDbInitializer.populateAsync(
              AppDatabase.getAppDatabase(this), complaintNumber, AppUtils.MODE_DELETE);
  }

  @Override
  public void onReceiveComplaintViewReceived(
          List<ReceiveComplaintViewEntity> complaintViewEntities, int from) {
    try {
      Log.d(TAG, "onReceiveComplaintViewReceived " + complaintViewEntities.size());
      if (from == AppUtils.MODE_LOCAL) {
        for (ReceiveComplaintViewEntity item : complaintViewEntities) {
          Log.d(TAG, "for ... " + item.getComplaintNumber());
          try {
            item.setConfirm(true);
            postReceiveComplaintRespondSaveToServer(item);

            item.setMode(AppUtils.MODE_SERVER);
            new ReceiveComplaintViewDbInitializer(mActivity, this, item)
                    .execute(AppUtils.MODE_INSERT_SINGLE);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onReceiveComplaintViewAssetDetailsReceived(
          List<AssetDetailsEntity> assetDetailsEntities, int from) {}

  @Override
  public void onReceiveComplaintViewReceivedError(String msg, int from) {}

  @Override
  public void onReceiveBarCodeAssetReceived(String msg, int mode) {}

  @Override
  public void onAllReceiveComplaintData(
          List<ReceiveComplaintRespondEntity> complaintRespondEntities, int modeLocal) {
    try {
      Log.d(TAG, "onAllReceiveComplaintData " + complaintRespondEntities.size());
      if (modeLocal == AppUtils.MODE_LOCAL) {
        for (ReceiveComplaintRespondEntity item : complaintRespondEntities) {
          Log.d(TAG, "for ... " + item.getComplaintNumber());
          try {
            postRespondDataToServer(item);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onReceiveComplaintBycomplaintNumber(
          ReceiveComplaintRespondEntity complaintRespondEntity, int modeLocal) {}

  @Override
  public void onImageSaveReceivedSuccess(DefectDoneImageUploaded mImageEntity, int mode) {
    Log.d(TAG, "onImageSaveReceivedSuccess ::" + mode);
    if (mode == AppUtils.MODE_SERVER) {
      new DefectDoneImageDbInitializer(
              mActivity,
              new DFoundWDoneImageEntity(mImageEntity.getComplaintNo() + mImageEntity.getDocType()),
              this)
              .execute(AppUtils.MODE_DELETE);
    }
  }

  @Override
  public void onImageReceivedSuccess(RCDownloadImage imageEntity, int mode) {}

  @Override
  public void onImageSaveReceivedFailure(String strErr, int mode) {}

  @Override
  public void onImageReceivedFailure(String strErr, int mode) {}

  @Override
  public void onAllImagesReceived(List<DFoundWDoneImageEntity> mImageEntities, int mode) {
    try {
      Log.d(TAG, "onAllImagesReceived " + mImageEntities.size());
      if (mode == AppUtils.MODE_LOCAL) {
        for (DFoundWDoneImageEntity item : mImageEntities) {
          Log.d(TAG, "for ... " + item.getId());
          try {
            postImageToServer(item);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
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

  @Override
  public void onPPENameListReceived(List<PPENameEntity> ppeNameEntities, int mode) {}


  @Override
  public void onPPESaveSuccess(String strMsg, int mode) {
    try {
      ppeFetchSaveDbInitializer.populateAsync(
              AppDatabase.getAppDatabase(mActivity), AppUtils.MODE_UPDATE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onPPESaveClicked(
          List<PPENameEntity> ppeNameEntities,
          List<PPEFetchSaveEntity> ppeSaveEntities,
          boolean isFetchdata) {}

  @Override
  public void onPPEFetchListSuccess(List<PPEFetchSaveEntity> ppeSaveEntities, int mode) {
    try {
      Log.d(TAG, "onPPEFetchListSuccess " + ppeSaveEntities.size());
      if (mode == AppUtils.MODE_LOCAL && ppeSaveEntities.size() > 0) {
        postPPEDataToServer(ppeSaveEntities);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onPPEFetchListFailure(String strErr, int mode) {}

  @Override
  public void onPPESaveFailure(String strErr, int mode) {}

  @Override
  public void onPPENameListFailure(String strErr, int mode) {}

  public void startService() {
    if (mPreferences
            .getString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE)
            .contains(AppUtils.NETWORK_AVAILABLE)) {

      Log.d(TAG, "downloadFile ");
      if (checkPermission()) {
        // start Download service
        if (!isServiceRunning()) {
        /*  Intent intent = new Intent(this, DownloadService.class);
          startService(intent);*/
        }
      } else {
        // request Permission
        ActivityCompat.requestPermissions(
                this,
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
      }
    }
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


  private boolean isServiceRunning() {
    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service :
            manager.getRunningServices(Integer.MAX_VALUE)) {
      if ("com.daemon.emco_android.service".equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void onFeedbackEmployeeDetailsReceivedSuccess(
          List<EmployeeDetailsEntity> employeeDetailsEntities, int mode) {}

  @Override
  public void onFeedbackDetailsReceivedSuccess(SaveFeedbackEntity saveFeedbackEntity, int mode) {}

  @Override
  public void onAllFeedbackDetailsReceivedSuccess(
          List<SaveFeedbackEntity> saveFeedbackEntities, int mode) {
    try {
      Log.d(TAG, "onAllImagesReceived " + saveFeedbackEntities.size());
      if (mode == AppUtils.MODE_LOCAL) {
        for (SaveFeedbackEntity item : saveFeedbackEntities) {
          Log.d(TAG, "for ... " + item.getComplaintNumber());
          try {
            postFeedbackDataToServer(item);
            item.setMode(AppUtils.MODE_SERVER);
            new FeedbackDbInitializer(mActivity, this, item).execute(AppUtils.MODE_INSERT_SINGLE);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onFeedbackDetailsReceivedSuccessPPMAtten(
          PpmEmployeeFeedResponse saveFeedbackEntity) {}

  @Override
  public void onFeedbackEmployeeDetailsSaveSuccess(String strMsg, int mode) {}

  @Override
  public void onFeedbackPpmStatusSucess(List<String> strMsg, int mode) {}

  @Override
  public void onFeedbackEmployeeDetailsReceivedFailure(String strErr, int mode) {}

  @Override
  public void onRCSaveMaterialSuccess(String strMsg, int from) {
    try {
      new RCSavedMaterialDbInitializer(mActivity, this, null).execute(AppUtils.MODE_UPDATE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onRCMaterialListReceived(
          List<MaterialMasterEntity> materialRequiredEntities, String noOfRows, int from) {}

  @Override
  public void onRCMaterialGetListReceived(
          List<SaveMaterialEntity> getMaterialEntities, String noOfRows, int mode) {
    try {
      Log.d(TAG, "onRCMaterialGetListReceived " + getMaterialEntities.size());
      if (mode == AppUtils.MODE_LOCAL && getMaterialEntities.size() > 0) {
        postMaterialDataToServer(getMaterialEntities);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onRCMaterialListReceivedError(String strErr, int from) {}

  @Override
  public void onRatedServiceReceived(List<SaveRatedServiceEntity> entities) {
    for (SaveRatedServiceEntity item : entities) {
      Log.d(TAG, "for ... " + item.getComplaintNumber());
      try {
        postRatedServceToServer(item);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  @Override
  public void onRatedServiceReceived(SaveRatedServiceEntity entities) {
    try {
      new SaveRatedServiceDbInitializer(mActivity, this, entities).execute(AppUtils.MODE_DELETE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onSaveRatedService(String message) {}

  @Override
  public void onCustomerRemarksReceived(List<String> customerRemarks) {}

  @Override
  public void onRateShareReceivedError(String strErr) {}

  @Override
  public void onSaveRateShareReceived(String strErr) {}


  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  protected void onResume() {

    if (!isNetworkConnected(mContext)) {
      buildAlertMessageNoGps("No Internet connection. Please turn on your internet connection and allow application to run properly.");
    }


    Log.d(TAG, "onResume");

    Log.d(TAG, "onResume");
    try {
      // only one time register connectivity register
      if (isReceiver) {
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
      } else isReceiver = true;
    } catch (Exception e) {
      e.printStackTrace();
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
                  if (isMyServiceRunning( EmployeeTrackingService.class)) {
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

  @Override
  public void onImageSaveReceivedSuccess1(RCDownloadImage imageEntity, int mode) {

  }

  public void onDrawerOpen() {
   drawer.openDrawer(GravityCompat.START);
  }

  private boolean isWorkScheduled(List<WorkInfo> workInfos) {

    boolean running = false;

    if (workInfos == null || workInfos.size() == 0) return false;

    for (WorkInfo workStatus : workInfos) {
      running = workStatus.getState() == WorkInfo.State.RUNNING | workStatus.getState() == WorkInfo.State.ENQUEUED;
    }

    return running;
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
         // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
          statusCheck();
        }

        else {
         // Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

          AppUtils.showDialog(mActivity,"Permission deny may lead application malfunction. Kindly close the app and reopen to get permission window.");

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
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
              public void onClick(final DialogInterface dialog, final int id) {
                dialog.cancel();
              }
            });
    final android.app.AlertDialog alert = builder.create();
    alert.show();
  }

}
