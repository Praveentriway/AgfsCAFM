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
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.github.florent37.expectanim.ExpectAnim;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import static com.github.florent37.expectanim.core.Expectations.atItsOriginalPosition;
import static com.github.florent37.expectanim.core.Expectations.invisible;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.visible;

/**
 * Created by subbu on 7/7/17.
 */
public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 1000;
    private TextView tv_login,tv_signup;
    private ExpectAnim expectAnimMove;
    private SharedPreferences mPreferences;
    private static final String TAG = SplashScreen.class.getSimpleName();
    private SharedPreferences.Editor mEditor;
    boolean updateShow=false;
    private Font font = App.getInstance().getFontInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        tv_login=(TextView)findViewById(R.id.tv_login);
        tv_signup=(TextView)findViewById(R.id.tv_signup);

        tv_login.setTypeface(font.getHelveticaRegular());

        tv_signup.setTypeface(font.getHelveticaRegular());

        mPreferences = this.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        String loginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
        if (loginData != null && loginData.length() > 0) {
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            finish();
            return;
        }
        /*else{
            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(i);
        }*/

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SplashScreen.this, RegisterActivity.class);
                startActivity(i);
            }
        });



        new ExpectAnim()

                .expect(tv_login)
                .toBe(
                        outOfScreen(Gravity.TOP),
                        invisible()
                )
                .toAnimation()
                .setNow();


        new ExpectAnim()

                .expect(tv_signup)
                .toBe(
                        outOfScreen(Gravity.BOTTOM),
                        invisible()
                )
                .toAnimation()
                .setNow();


        this.expectAnimMove = new ExpectAnim()
                .expect(tv_login)
                .toBe(
                        atItsOriginalPosition(),
                        visible()
                ).expect(tv_signup)
                .toBe(
                        atItsOriginalPosition(),
                        visible()
                )
                .toAnimation()
                .setDuration(800)
                .start();




    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");

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


    }
