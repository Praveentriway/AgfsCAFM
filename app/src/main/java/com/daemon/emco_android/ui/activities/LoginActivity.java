package com.daemon.emco_android.ui.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.fragments.user.Login;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ConnectivityStatus;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.SessionManager;
import java.lang.reflect.Field;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private SharedPreferences mPreferences;
    private final int PERMISSION_REQUEST_CODE = 1;

    public static String IP_ADDRESS="ip_address";

    public static String MBM ="mbm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
        }
        else{
            statusCheck();
        }

        try {
            mPreferences = this.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);

            String loginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (loginData != null && loginData.length() > 0) {
                loadFragment();
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                finish();
                return;
            } else loadFragment();

            setupActionbar();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    private void setupActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        final ImageView imageInToolbar = (ImageView) toolbar.findViewById(R.id.img_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(SessionManager.getSessionForURL(IP_ADDRESS,this) !=null
                &&  (!SessionManager.getSessionForURL(IP_ADDRESS,this).trim().isEmpty())
                &&  (SessionManager.getSessionForURL(IP_ADDRESS,this).contains(MBM))) {

            imageInToolbar.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.logo_mbm_png_9));
        }
        else{
            imageInToolbar.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ag_logo_white));
        }

    }

    public void loadFragment() {
        Fragment fragment = new Login();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, Please enable it.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


}
