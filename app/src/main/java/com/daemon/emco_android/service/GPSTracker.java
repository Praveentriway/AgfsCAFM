package com.daemon.emco_android.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.daemon.emco_android.utils.AppUtils.MODE_OFFLINE;
import static com.daemon.emco_android.utils.AppUtils.MODE_REMOTE;
import static com.daemon.emco_android.utils.GpsUtils.getDeviceID;
import static com.daemon.emco_android.utils.GpsUtils.getSerialNumber;
import static com.daemon.emco_android.utils.GpsUtils.isNetworkConnected;

public class GPSTracker implements LocationListener, EmployeeGpsRepository.EmployeeGPSListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

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

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this, Looper.getMainLooper());

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                    Log.d("Network", "Network"+latitude+" - "+longitude);

                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this, Looper.getMainLooper());

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            } }
                        Log.d("GPS Enabled", "GPS Enabled"+latitude+" - "+longitude);
                    } } }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }
    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }
    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }
    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){

    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


    public void updateFusedLocation(final EmployeeTrackingDetail emp) {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        };
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        try {
            mFusedLocationClient
                    .getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                                processLocation(mLocation.getLatitude(), mLocation.getLongitude(),emp);
                                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                            } else {
                                Log.w(this.getClass().getName(), "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(this.getClass().getName(), "Lost location permission." + unlikely);
        }
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, null);
        } catch (SecurityException unlikely) {
            Log.e(this.getClass().getName(), "Lost location permission. Could not request updates. " + unlikely);
        }
    }



    public void processLocation(double latitude, double longitude,EmployeeTrackingDetail employeeTrackingDetail) {

        String currentDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        mPreferences = mContext.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        Gson gson = new Gson();
        String loginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
        Login login = gson.fromJson(loginData, Login.class);

        EmployeeTrackingDetail emp;

        if(employeeTrackingDetail!=null){

            emp =employeeTrackingDetail;
        }

        else{
            emp = new EmployeeTrackingDetail();
        }

        emp.setEmployeeId(login.getEmployeeId());
        emp.setLat(latitude + "");
        emp.setLng(longitude + "");
        //  String currentDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        emp.setTrans_date(currentDate);
        try {
            emp.setDeviceID(getDeviceID(mContext));
            emp.setDeviceName(getSerialNumber());

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isNetworkConnected(mContext)) {
            new EmployeeGpsRepository(mContext, this).updateEmployeeGps(emp, MODE_REMOTE);
        } else {
            new EmployeeGpsRepository(mContext, this).updateEmployeeGps(emp, MODE_OFFLINE);
        }

    }

    public void onSuccessGpsUpdate(String strMsg, int mode) {

    }

    public void onFailureGpsUpdate(String strErr, int mode) {

    }

}