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
import com.daemon.emco_android.ui.fragments.user.UserRegisteration;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ConnectivityStatus;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.SessionManager;

import java.lang.reflect.Field;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.daemon.emco_android.ui.activities.LoginActivity.IP_ADDRESS;
import static com.daemon.emco_android.ui.activities.LoginActivity.MBM;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Font font = App.getInstance().getFontInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            mPreferences = this.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
            mEditor = mPreferences.edit();

            String loginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
            if (loginData != null && loginData.length() > 0) {
                Log.d(TAG, "getLoginData " + loginData.toString());
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
        toolbar.setTitle(R.string.app_name);
        final ImageView imageInToolbar = (ImageView) toolbar.findViewById(R.id.img_toolbar);
        setSupportActionBar(toolbar);
        if(SessionManager.getSessionForURL(IP_ADDRESS,this) !=null
                && (!SessionManager.getSessionForURL(IP_ADDRESS,this).trim().isEmpty())
                &&  (SessionManager.getSessionForURL(IP_ADDRESS,this).contains(MBM))){
            imageInToolbar.setImageDrawable(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.logo_mbm_png));
        }
        else{
            imageInToolbar.setImageDrawable(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.ag_logo_white));
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

        // update the main content by replacing fragments
        Fragment fragment = new UserRegisteration();
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
