package com.daemon.emco_android.repository.remote.restapi;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import com.daemon.emco_android.listeners.URLListener;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.SessionManager;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.daemon.emco_android.repository.remote.restapi.ApiConstant.PREFIXURL;

/**
 * Created by subbu on 17/7/17.
 */

public class Api {
    private static Retrofit retrofit = null;
    public static String BASEURL="",BASE_URL_FILE="";
    private static SharedPreferences.Editor mEditor;
    private static SharedPreferences mPreferences;
    //  public static boolean bool;
    private static String result="";




    public static Retrofit dataClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://172.28.30.98:8888/mobileapi/webapi/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }







}