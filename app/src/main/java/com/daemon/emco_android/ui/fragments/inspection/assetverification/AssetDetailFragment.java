package com.daemon.emco_android.ui.fragments.inspection.assetverification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.daemon.emco_android.R;
import com.daemon.emco_android.databinding.FragmentAssetDetailBinding;
import com.daemon.emco_android.model.common.AssetInfo;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.ui.components.fab.Fab;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.ui.fragments.preventive.PpmDashboard;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import static android.content.Context.MODE_PRIVATE;
import static com.daemon.emco_android.utils.AppUtils.ARGS_ASSETINFO;
import static com.daemon.emco_android.utils.Utils.ASSET_AUDIT;
import static com.daemon.emco_android.utils.Utils.ASSET_TRANSFER;
import static com.daemon.emco_android.utils.Utils.TAG_FRAGMENT_ASSET_AUDIT;


public class AssetDetailFragment extends Fragment {

    AssetInfo assetInfo;
    private Bundle mArgs;
    private AppCompatActivity mActivity;
    View view;
    FragmentAssetDetailBinding binding;
    private MaterialSheetFab materialSheetFab;
    private String mStringJson = null;
    private Gson gson = new Gson();
    private Login user;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private int statusBarColor;

    public AssetDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = getArguments();
        if (getArguments() != null) {
            assetInfo = (AssetInfo) mArgs.getSerializable(ARGS_ASSETINFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = (AppCompatActivity) getActivity();
        mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mStringJson = mPreferences.getString(AppUtils.SHARED_LOGIN, null);

        if (mStringJson != null) {
            user = gson.fromJson(mStringJson, Login.class);
        }

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_asset_detail, container, false);
        view=binding.getRoot();

        initView();
        setHasOptionsMenu(true);
        setupActionBar();
        return binding.getRoot();

    }

    public void initView() {


        setupFab();

        binding.txtOpco.setText(assetInfo.getOpco());
        binding.txtAssetTag.setText(assetInfo.getAssetTag() + " - " + assetInfo.getEquipmentName());
        binding.txtAssetDescription.setText(assetInfo.getAssetDescription());
        binding.txtPo.setText(assetInfo.getPoNo());
        binding.txtPoDate.setText((assetInfo.getPurchaseDate()));
        binding.txtSn.setText(assetInfo.getSerialNo());
        binding.txtPrice.setText(assetInfo.getPrice());
        binding.txtJobNo.setText(assetInfo.getJobNo());
        binding.txtAssetLocation.setText(assetInfo.getAssetLocation());
        binding.txtCustodian.setText(assetInfo.getCustodian());
        binding.txtCustodianName.setText(assetInfo.getCustodianName());
        binding.txtRemarks.setText(assetInfo.getRemark());
        binding.txtAssetStatus.setText(assetInfo.getStatus());
        binding.txtAssetCondition.setText(assetInfo.getCondition());
        binding.txtWarrantyType.setText(assetInfo.getWarrantyType());
        binding.txtWarrantyExpiry.setText((assetInfo.getWarrantyExpiry()));
        binding.txtWarrantyRemarks.setText(assetInfo.getWarrantyRemarks());


    }


    public void setupActionBar() {
        Toolbar mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        TextView tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Asset Detail");
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

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:

                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                Fragment _fragment = new MainDashboard();
                FragmentTransaction _transaction = fm.beginTransaction();
                _transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                _transaction.replace(R.id.frame_container, _fragment);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void loadFragment(final Fragment fragment, AssetInfo asset, String tag, String verificationType) {

        Bundle mdata = new Bundle();
        mdata.putSerializable(AppUtils.ARGS_ASSETINFO, asset);
        mdata.putString(AppUtils.ARGS_ASSET_VERIFICATION_TYPE, verificationType);
        fragment.setArguments(mdata);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

    }


    private void setupFab() {

        // hide asset transfer to audit user
        if ((user.getUserType().equals(AppUtils.AUDITOR))) {
            binding.assetTransfer.setVisibility(View.GONE);
        }

        Fab fab = (Fab) view.findViewById(R.id.fab);
        View sheetView = view.findViewById(R.id.fab_sheet);

        View overlay = view.findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.colorPrimary);

        // Create material sheet FAB
         materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet event listener
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.color_gray));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);
            }
        });

        // Set material sheet item click listeners
        view.findViewById(R.id.asset_audit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AssetAuditFragment(), assetInfo, TAG_FRAGMENT_ASSET_AUDIT, ASSET_AUDIT);
            }
        });
        view.findViewById(R.id.asset_transfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AssetAuditFragment(), assetInfo, TAG_FRAGMENT_ASSET_AUDIT, ASSET_TRANSFER);
            }
        });

    }

    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getActivity().getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(color);
        }
    }
}