package com.daemon.emco_android.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.utils.AnimateUtils;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.github.florent37.expectanim.ExpectAnim;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import static com.github.florent37.expectanim.core.Expectations.atItsOriginalPosition;
import static com.github.florent37.expectanim.core.Expectations.invisible;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.visible;

/**
 * Created by praba on 7/12/19.
 */

public class SplashScreen extends AppCompatActivity {

    private TextView tv_login,tv_signup;
    private SharedPreferences mPreferences;
    boolean updateShow=false;
    private Font font = App.getInstance().getFontInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initView();
        checkDirectLogin();
        new AnimateUtils().splashAnimate(tv_login,tv_signup);

    }

    @Override
    protected void onResume() {

        if (!updateShow) {

            new AppUpdater(this)
                    .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                    .setDisplay(Display.DIALOG)
                    .setButtonDoNotShowAgain(null).setCancelable(false).setButtonDismiss(null)
                    .showAppUpdated(false)
                    .setButtonUpdateClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

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

    public void initView(){

        tv_login=(TextView)findViewById(R.id.tv_login);
        tv_signup=(TextView)findViewById(R.id.tv_signup);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashScreen.this, RegisterActivity.class));
            }
        });

    }

    public void checkDirectLogin(){
        mPreferences = this.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
        String loginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
        if (loginData != null && loginData.length() > 0) {
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            finish();
            return;
        }
    }
    }

