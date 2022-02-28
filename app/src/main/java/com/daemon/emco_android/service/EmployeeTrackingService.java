package com.daemon.emco_android.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.EmployeeList;
import com.daemon.emco_android.model.common.EmployeeTrackingDetail;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.repository.remote.EmployeeGpsRepository;
import com.daemon.emco_android.utils.AppUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import static com.daemon.emco_android.utils.AppUtils.MODE_OFFLINE;
import static com.daemon.emco_android.utils.AppUtils.MODE_REMOTE;
import static com.daemon.emco_android.utils.GpsUtils.getDeviceID;
import static com.daemon.emco_android.utils.GpsUtils.getSerialNumber;
import static com.daemon.emco_android.utils.GpsUtils.isLocationEnabled;
import static com.daemon.emco_android.utils.GpsUtils.isNetworkConnected;

public class EmployeeTrackingService extends Service implements EmployeeGpsRepository.EmployeeGPSListener {

    public int counter = 0;
    private static final String DEFAULT_START_TIME = "00:00";
    private static final String DEFAULT_END_TIME = "24:00";
    GPSTracker gps;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    /**
     * The current location.
     */
    private Location mLocation;
    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;
    private Context mContext;
    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;
    public int TRACKTIMER_INSECONDS = 10;
    public int TRACKTIMER_ONEMINUTES = 60;
    public int TRACKTIMER_ONEDAY = 60 * 60 * 24;
    public int TRACKTIMER_ONEHOUR = 60 * 60;
    public int TRACKTIMER_HALFHOUR = 60 * 30;
    public static String TRACK_COUNT = "TRACK_COUNT";
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public EmployeeTrackingService(Context applicationContext) {
        super();

    }

    public EmployeeTrackingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        mContext = getApplicationContext();
        if (intent.getExtras() != null && intent.getExtras() != null && intent.getExtras().getInt(TRACK_COUNT) != 0) {
            counter = intent.getExtras().getInt(TRACK_COUNT);
        }

        Log.i("START", "onStartCommand!");
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        stoptimertask();
        mPreferences = this.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        Gson gson = new Gson();
        String loginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
        Login login = gson.fromJson(loginData, Login.class);

        if (loginData != null && loginData.length() > 0 && login.getTrackingFlag().equalsIgnoreCase("Y")) {
            Intent broadcastIntent = new Intent(this, EmployeeTrackingReceiver.class);
            broadcastIntent.putExtra(TRACK_COUNT, counter);
            sendBroadcast(broadcastIntent);
        } else {

            Log.i("EmployeeTrack", "Room data has deleted on stopping service");

//            AppDatabase db = AppDatabase.getAppDatabase(mContext);
//            if(db.employeeTrackingDao().count()>0){
//                db.employeeTrackingDao().deleteAll();
//                Log.i("EmployeeTrack","Room data has deleted on stopping service");
//            }
//
        }
    }

    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                // to reset the counter when it reaches the count
                if (counter >= TRACKTIMER_ONEHOUR) {
                    counter = 0;
                }
                /**
                 *execute tracking at the initial and then the timer gets triggered.
                 */
                if (counter == 0) {
                    startTracking();
                }
                counter++;
              //  Log.i("in timer", "in timer ++++  " + counter);
            }
        };
    }

    /**
     * not needed
     */

    public void stoptimertask() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void startTracking() {
        if (isNetworkConnected(mContext)) {
            if (isLocationEnabled(mContext)) {
                // fetchLocation();
            } else {
                showTurnOnNotification("Location");
            }
        } else {
            showTurnOnNotification("Mobile data");
        }
        new GPSTracker(getApplicationContext()).updateFusedLocation(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
//        else
//            startForeground(1, new Notification());
    }

    private void fetchLocation() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        String formattedDate = dateFormat.format(date);
        try {
            Date currentDate = dateFormat.parse(formattedDate);
            Date startDate = dateFormat.parse(DEFAULT_START_TIME);
            Date endDate = dateFormat.parse(DEFAULT_END_TIME);
            if (currentDate.after(startDate) && currentDate.before(endDate)) {

               new GPSTracker(getApplicationContext()).updateFusedLocation(null);

            } else {
                Log.d(this.getClass().getName(), "Time up to get location. Your time is : " + DEFAULT_START_TIME + " to " + DEFAULT_END_TIME);
            }
        } catch (ParseException ignored) {
        }
    }

    /**
     * get gps by location manager
     * */

    public void getLocation_ByGPS() {
        gps = new GPSTracker(getApplicationContext());
        // check if GPS enabled
        if (gps.canGetLocation()) {
            double  latitude = gps.getLatitude();
            double  longitude = gps.getLongitude();
        } else {
            // can't get location
            gps.showSettingsAlert();
        }
    }

    private void startMyOwnForeground() {

        String NOTIFICATION_CHANNEL_ID = "com.daemon.emco_android";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        assert manager != null;

        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_pushn)
                .setContentTitle(getString(R.string.app_name) + " Mobile app is installed.")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(getClass().getName(), "Service Stop");
    }

    private void showTurnOnNotification(String msg) {

        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getString(R.string.app_name);
            String description = mContext.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(mContext.getString(R.string.app_name), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                mContext,
                1,
                new Intent(android.provider.Settings.ACTION_SETTINGS),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.app_name))
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setContentTitle(getString(R.string.app_name) + " Warning")
                .setContentText(msg + " is not enabled. Go to settings menu and turn on the " + msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg + " is not enabled. Go to settings menu and turn on the " + msg));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1001, builder.build());

    }

    private void showNotificationForTest(String msg) {

        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getString(R.string.app_name);
            String description = mContext.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(mContext.getString(R.string.app_name), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                mContext,
                1,
                new Intent(android.provider.Settings.ACTION_SETTINGS),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.app_name))
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setContentTitle(getString(R.string.app_name) + "location Testing")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1001, builder.build());

    }

    public void onSuccessGpsUpdate(String strMsg, int mode) {

    }

    public void onFailureGpsUpdate(String strErr, int mode) {

    }

    @Override
    public void onReceiveEmployeeList(List<EmployeeList> object) {

    }

    @Override
    public void onReceiveFailureEmployeeList(String toString) {

    }

}