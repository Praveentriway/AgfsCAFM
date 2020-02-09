package com.daemon.emco_android.ui.fragments.common;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.daemon.emco_android.R;
import com.daemon.emco_android.model.response.RCDownloadImage;
import com.daemon.emco_android.utils.AppUtils;

/**
 * Created by Daemonsoft on 8/23/2017.
 */

public class ViewImage extends Fragment {
    private static final String TAG = ViewImage.class.getSimpleName();
    private AppCompatActivity mActivity;
    private Context mContext;
    private Bundle mArgs = new Bundle();
    private Toolbar mToolbar;
    private View rootView;
    private CoordinatorLayout cl_main;
    private ImageView iv_fullscreen;
    private FragmentManager mManager;
    private RCDownloadImage downloadImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mActivity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            setRetainInstance(true);
            mContext = mActivity;
            mArgs = getArguments();
            mManager = mActivity.getSupportFragmentManager();
            if (mActivity.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
            }
            if (mArgs != null && mArgs.size() > 0) {
                downloadImage = mArgs.getParcelable(AppUtils.ARGS_RCDOWNLOADIMAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        try {
            rootView = inflater.inflate(R.layout.fragment_view_image, container, false);
            initUI(rootView);
            setProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void initUI(View rootView) {
        Log.d(TAG, "initUI");
        try {
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            iv_fullscreen = (ImageView) rootView.findViewById(R.id.iv_fullscreen);
            AppUtils.closeInput(cl_main);
            setupActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        if (downloadImage != null) {
            mToolbar.setTitle(downloadImage.getDocType().equals("B") ? "Defect found image" : "Work done image");
        } else {
            mToolbar.setTitle("Defect found/Work done images");
        }
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.onBackPressed();
            }
        });
    }

    private void setProperties() {
        try {
            Log.d(TAG, "setProperties");
            if (downloadImage != null) {

                iv_fullscreen.setImageBitmap(AppUtils.getDecodedString(downloadImage.getBase64Image().get(0)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(TAG, "onPrepareOptionsMenu ");
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Log.d(TAG, "onOptionsItemSelected : home");
                //mActivity.onBackPressed();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                Fragment _fragment = new MainLandingUI();
                FragmentTransaction _transaction = mManager.beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
