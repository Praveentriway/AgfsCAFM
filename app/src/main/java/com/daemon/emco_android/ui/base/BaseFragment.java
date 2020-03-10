package com.daemon.emco_android.ui.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.utils.AppUtils;
import com.google.gson.Gson;

public class BaseFragment extends Fragment {


    public Login getLogin(Context context){

        Login login=null;
        SharedPreferences mPreferences = context.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
        String mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
        if (mLoginData != null) {
            Gson gson = new Gson();
             login = gson.fromJson(mLoginData, Login.class);
        }

      return login;
    }


    public void setupToolBarHome(final AppCompatActivity mActivity,String title) {

        Toolbar mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbar.setVisibility(View.VISIBLE);
        TextView tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(title);
        LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.VISIBLE);
        // mToolbar.setTitle(getResources().getString(R.string.lbl_ppm_details));
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.onBackPressed();
                    }
                });

    }


    public void setupToolBar(final AppCompatActivity mActivity,String title) {

        Toolbar mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbar.setVisibility(View.VISIBLE);
        TextView tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(title);
        LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);
        // mToolbar.setTitle(getResources().getString(R.string.lbl_ppm_details));
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.onBackPressed();
                    }
                });

    }


    public void setupToolBar(final AppCompatActivity mActivity, boolean isFalse) {

        Toolbar mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbar.setVisibility(View.GONE);
        ImageView img_toolbar = (ImageView) mToolbar.findViewById(R.id.img_toolbar);
        img_toolbar.setVisibility(View.GONE);
        LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);
        mActivity.setSupportActionBar(mToolbar);

    }



    public void setupToolBarOnly(final AppCompatActivity mActivity,String title) {

        Toolbar mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbar.setVisibility(View.VISIBLE);
        ImageView img_toolbar = (ImageView) mToolbar.findViewById(R.id.img_toolbar);
        img_toolbar.setVisibility(View.GONE);
        TextView tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(title);
        LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);

        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.onBackPressed();
                    }
                });

    }



}
