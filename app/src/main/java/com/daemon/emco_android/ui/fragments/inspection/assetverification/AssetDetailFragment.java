package com.daemon.emco_android.ui.fragments.inspection.assetverification;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.AssetInfo;
import com.daemon.emco_android.repository.db.entity.SurveyTransaction;
import com.daemon.emco_android.ui.components.fab.Fab;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.utils.AppUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static com.daemon.emco_android.utils.AppUtils.ARGS_ASSETINFO;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYTRANS;
import static com.daemon.emco_android.utils.AppUtils.closeKeyboard;
import static com.daemon.emco_android.utils.Utils.ASSET_AUDIT;
import static com.daemon.emco_android.utils.Utils.ASSET_TRANSFER;
import static com.daemon.emco_android.utils.Utils.TAG_FRAGMENT_ASSET_AUDIT;


public class AssetDetailFragment extends Fragment {

    AssetInfo assetInfo;
    private Bundle mArgs;
    private AppCompatActivity mActivity;
    View view;
    private MaterialSheetFab materialSheetFab;
    private int statusBarColor;
    TextView txt_opco, txt_asset_tag, txt_asset_description, txt_po, txt_po_date, txt_sn, txt_price, txt_job_no, txt_asset_location,
            txt_custodian, txt_custodian_name, txt_remarks, txt_asset_status, txt_asset_condition, txt_warranty_type, txt_warranty_expiry, txt_warranty_remarks;

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
        view = inflater.inflate(R.layout.fragment_asset_detail, container, false);
        mActivity = (AppCompatActivity) getActivity();
        initView();
        setHasOptionsMenu(true);
        setupActionBar();
        return view;

    }

    public void initView() {

        txt_opco = (TextView) view.findViewById(R.id.txt_opco);
        txt_asset_tag = (TextView) view.findViewById(R.id.txt_asset_tag);
        txt_asset_description = (TextView) view.findViewById(R.id.txt_asset_description);
        txt_po = (TextView) view.findViewById(R.id.txt_po);
        txt_po_date = (TextView) view.findViewById(R.id.txt_po_date);
        txt_sn = (TextView) view.findViewById(R.id.txt_sn);
        txt_price = (TextView) view.findViewById(R.id.txt_price);
        txt_job_no = (TextView) view.findViewById(R.id.txt_job_no);
        txt_asset_location = (TextView) view.findViewById(R.id.txt_asset_location);
        txt_custodian = (TextView) view.findViewById(R.id.txt_custodian);
        txt_custodian_name = (TextView) view.findViewById(R.id.txt_custodian_name);
        txt_remarks = (TextView) view.findViewById(R.id.txt_remarks);
        txt_asset_status = (TextView) view.findViewById(R.id.txt_asset_status);
        txt_asset_condition = (TextView) view.findViewById(R.id.txt_asset_condition);
        txt_warranty_type = (TextView) view.findViewById(R.id.txt_warranty_type);
        txt_warranty_expiry = (TextView) view.findViewById(R.id.txt_warranty_expiry);
        txt_warranty_remarks = (TextView) view.findViewById(R.id.txt_warranty_remarks);

        setupFab();

        txt_opco.setText(assetInfo.getOpco());
        txt_asset_tag.setText(assetInfo.getAssetTag() + " - " + assetInfo.getEquipmentName());
        txt_asset_description.setText(assetInfo.getAssetDescription());
        txt_po.setText(assetInfo.getPoNo());
        txt_po_date.setText((assetInfo.getPurchaseDate()));
        txt_sn.setText(assetInfo.getSerialNo());
        txt_price.setText(assetInfo.getPrice());
        txt_job_no.setText(assetInfo.getJobNo());
        txt_asset_location.setText(assetInfo.getAssetLocation());
        txt_custodian.setText(assetInfo.getCustodian());
        txt_custodian_name.setText(assetInfo.getCustodianName());
        txt_remarks.setText(assetInfo.getRemark());
        txt_asset_status.setText(assetInfo.getStatus());
        txt_asset_condition.setText(assetInfo.getCondition());
        txt_warranty_type.setText(assetInfo.getWarrantyType());
        txt_warranty_expiry.setText((assetInfo.getWarrantyExpiry()));
        txt_warranty_remarks.setText(assetInfo.getWarrantyRemarks());


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

    public String changeDateFormat(String dateOrg) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
            return sdf2.format(sdf.parse(dateOrg));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void setupFab() {

        Fab fab = (Fab) view.findViewById(R.id.fab);
        View sheetView = view.findViewById(R.id.fab_sheet);
        View overlay = view.findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.colorPrimary);

        // Create material sheet FAB
         materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);
//        materialSheetFab.showFab();
//        materialSheetFab.hideSheet();

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