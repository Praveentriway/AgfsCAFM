package com.daemon.emco_android.repository.remote;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.daemon.emco_android.repository.remote.restapi.ApiClient;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.listeners.URLListener;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.daemon.emco_android.repository.remote.restapi.ApiConstant.PREFIXURL;

/** Created by vikram on 13/6/17. */
public class UrlService {
  private static final String TAG = UrlService.class.getSimpleName();

  private AppCompatActivity mActivity;
  private ApiInterface mInterface;
  private URLListener mCallback;
  private String ip;

  public UrlService(AppCompatActivity _activity, URLListener listener, String url) {
    mActivity = _activity;
    mCallback = listener;
    try {
      ip = url;
      if (ApiClient.getURLTime(url, mActivity, listener).create(ApiInterface.class) != null) {
        mInterface = ApiClient.getURLTime(url, mActivity,listener).create(ApiInterface.class);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void getURLData() {
    Log.d(TAG, "addUser");
    try {
      mInterface
          .getURLResult()
          .enqueue(
              new Callback<CommonResponse>() {
                @Override
                public void onResponse(
                    Call<CommonResponse> call, Response<CommonResponse> response) {
                  if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse success " + response.body().getMessage());
                    if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                      mCallback.onUrlreceivedsucess(response.body());
                      SessionManager.saveSession(
                          "baseurl", "http://" + ip + "/"+PREFIXURL+"/webapi/", mActivity);
                      SessionManager.saveSession(
                          "baseurlfile", "http://" + ip + "/"+PREFIXURL+"/", mActivity);

                      SessionManager.saveSessionForURL(
                              "ip_address",  ip , mActivity);

                    }
                    /*  else{
                        mCallback.onUrlreceivedFailure("Invalid url");
                    }*/
                  }
                  /*  else{mob
                      mCallback.onUrlreceivedFailure("Invalid url");
                  }*/
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                  t.printStackTrace();
                  mCallback.onUrlreceivedFailure(
                      "You have entered an invalid URL, Please try again");
                }
              });
    } catch (Exception e) {
      e.printStackTrace();

    }
  }
}
