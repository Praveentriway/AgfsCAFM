package com.daemon.emco_android.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.fragments.user.Fragment_Login;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ConnectivityStatus;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.SessionManager;
import java.lang.reflect.Field;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Font font = App.getInstance().getFontInstance();
    private boolean isReceiver = false;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!ConnectivityStatus.isConnected(LoginActivity.this)) {
                Log.d(TAG, "not connected");
                mEditor.putString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_NOT_AVAILABLE);
                mEditor.commit();
            } else {
                Log.d(TAG, "connected");
                mEditor.putString(AppUtils.IS_NETWORK_AVAILABLE, AppUtils.NETWORK_AVAILABLE);
                mEditor.commit();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            mPreferences = this.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
            mEditor = mPreferences.edit();

            String loginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (loginData != null && loginData.length() > 0) {
                loadFragment();
                Log.d(TAG, "getLoginData " + loginData.toString());
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                finish();
                return;
            } else loadFragment();

            registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            setupActionbar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");


        try {
            // only one time register connectivity register
            if (isReceiver) {
                registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            } else isReceiver = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
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

    private void setupActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        final ImageView imageInToolbar = (ImageView) toolbar.findViewById(R.id.img_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(SessionManager.getSessionForURL("ip_address",this) !=null && (!SessionManager.getSessionForURL("ip_address",this).trim().isEmpty())  && (SessionManager.getSessionForURL("ip_address",this).contains("mbm"))){
            imageInToolbar.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.logo_mbm_png_9));
        }
        else{
            imageInToolbar.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ag_logo_white));
        }

        TextView titleTextView = null;
        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
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
        // update the main content by replacing fragments
        Fragment fragment = new Fragment_Login();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
