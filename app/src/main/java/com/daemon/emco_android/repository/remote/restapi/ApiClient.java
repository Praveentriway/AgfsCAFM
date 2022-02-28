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

public class ApiClient {
    private static Retrofit retrofit = null;
    public static String BASEURL="",BASE_URL_FILE="";
    private static SharedPreferences.Editor mEditor;
    private static SharedPreferences mPreferences;
  //  public static boolean bool;
    private static String result="";


    public static Retrofit getClient(Context mActivity) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        if (retrofit == null) {
             retrofit = new Retrofit.Builder()
                      //.baseUrl("http://172.28.30.98:8888/mobileapi/webapi/")
                       .baseUrl(SessionManager.getSession("baseurl",mActivity))
                       .client(okHttpClient)
                       .addConverterFactory(GsonConverterFactory.create())
                       .build();
        }

        return retrofit;
    }


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

    public static Retrofit getClientLongTime(Context context) throws GeneralSecurityException, IOException {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(SessionManager.getSession("baseurl",context))
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit getClientLongTime(int timeINminutes, String url) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(timeINminutes, TimeUnit.MINUTES)
                .connectTimeout(timeINminutes, TimeUnit.MINUTES)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }


    public static Retrofit getURLTime(String url, AppCompatActivity mActivity, URLListener listener) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();
        try {
            if(url!=null){
                result= "https://" + url + "/"+PREFIXURL+"/webapi/";
                retrofit = new Retrofit.Builder()
                        .baseUrl(result)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
         //   AppUtils.showDialog(mActivity,"You have entered an invalid URL, Please try again");
            listener.onUrlreceivedFailure(
                    "You have entered an invalid URL, Please try again");
            AppUtils.hideProgressDialog();
            return null;
        }
        /*finally {
            AppUtils.hideProgressDialog();
        }*/
        return retrofit;
    }
}