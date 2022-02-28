package com.daemon.emco_android.ui.fragments.utilityconsumption;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.R;
import com.daemon.emco_android.databinding.FragmentUtilityBarcodeScanningBinding;
import com.daemon.emco_android.model.common.UtilityAssetDetail;
import com.daemon.emco_android.repository.remote.AssetVerificationRepository;
import com.daemon.emco_android.repository.remote.UtilityRepository;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.ui.fragments.inspection.assetverification.AssetDetailFragment;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.ZxingScannerActivity;
import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.List;

import static android.content.ContentValues.TAG;
import static com.daemon.emco_android.utils.AppUtils.ARGS_COMPANYCODE;
import static com.daemon.emco_android.utils.AppUtils.ARGS_JOBNO;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SUGESSTIONFLAG;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYMODE;
import static com.daemon.emco_android.utils.AppUtils.closeKeyboard;
import static com.daemon.emco_android.utils.Utils.TAG_FRAGMENT_ASSET_DETAIL;
import static com.daemon.emco_android.utils.Utils.TAG_FRAGMENT_UTILITY_DETAIL;
import static com.daemon.emco_android.utils.Utils.TAG_FRAGMENT_UTILITY_SCANNING;


public class UtilityBarcodeScanningFragment extends Fragment implements UtilityRepository.Listener {

    FragmentUtilityBarcodeScanningBinding binding;
    private AppCompatActivity mActivity;
    private boolean isPermissionGranted = false;
    private static final int AS_BARCODE_CAPTURE = 9001;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 4;
    //String opco="",jobno="";
    private Bundle mArgs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArgs=getArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_utility_barcode_scanning, container, false);
        mActivity = (AppCompatActivity) getActivity();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupActionBar();
        setHasOptionsMenu(true);

        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });
        binding.btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterManual();
            }
        });

        if (!isPermissionGranted) {
            getPermissionToReadExternalStorage();
        }

    }

    public void scan(){

        int requestId = AppUtils.getIdForRequestedCamera(AppUtils.CAMERA_FACING_BACK);
        if (requestId == -1)
            AppUtils.showDialog(getContext(), "Camera not available");
        else {
            Intent intent = new Intent(mActivity, ZxingScannerActivity.class);
            intent.putExtra(ZxingScannerActivity.AutoFocus, true);
            startActivityForResult(intent, AS_BARCODE_CAPTURE);
        }

    }

    public void enterManual() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Asset Tag");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        // input.setGravity(Gravity.CENTER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeKeyboard(getContext());
                String  m_Text = input.getText().toString();
                if(m_Text.equalsIgnoreCase("")){
                    AppUtils.showErrorToast(mActivity,"Asset Tag should not be empty.");
                }
                else{
                    fetchUtilityInfo(m_Text);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeKeyboard(getContext());
                dialog.cancel();
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AS_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String barcode = data.getStringExtra(ZxingScannerActivity.BarcodeObject);
                    showDialog(getContext(),barcode);
                } else {
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Log.d(TAG, "Error");
            }
        } else {

        }
    }


    public void setupActionBar() {
        Toolbar mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        TextView tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Utility Consumption");
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
        menu.findItem(R.id.action_home).setVisible(true);
        menu.findItem(R.id.action_logout).setVisible(false);

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

    public void showDialog(final Context context, final String StrMsg) {
        try {
            MaterialDialog.Builder builder =
                    new MaterialDialog.Builder(context)
                            .title("Please confirm the Asset Tag")
                            .content(StrMsg)
                            .positiveText("Confirm")
                            .negativeText("Cancel")
                            .stackingBehavior(StackingBehavior.ADAPTIVE)
                            .onPositive(
                                    new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(
                                                @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            fetchUtilityInfo(StrMsg);
                                        }
                                    }).onNegative(  new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(
                                @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    });

            MaterialDialog dialog = builder.build();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadFragment(final Fragment fragment, UtilityAssetDetail utilityAssetDetail,final String tag) {

        Bundle mdata = new Bundle();
        mdata.putSerializable(AppUtils.ARGS_UTILITY_ASSET,utilityAssetDetail);
        fragment.setArguments(mdata);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToReadExternalStorage() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }

                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }
            }
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            isPermissionGranted = true;
        }
    }

    public void fetchUtilityInfo(String assetTag) {

        AppUtils.showProgressDialog(mActivity,"",false);
        new UtilityRepository(mActivity,this).getUtilityDetail(assetTag);

    }

    @Override
    public void onReceiveUtilityDetails(List<UtilityAssetDetail> object) {
        AppUtils.hideProgressDialog();
        loadFragment(new UtilityDetailsFragment(),object.get(0),TAG_FRAGMENT_UTILITY_DETAIL);
    }

    @Override
    public void onReceiveFailureUtilityDetails(String err) {
        AppUtils.hideProgressDialog();
        AppUtils.showErrorToast(mActivity,err);
    }

    @Override
    public void onSuccessSaveUtilityReading(String strMsg, int mode) {
        AppUtils.hideProgressDialog();
    }

    @Override
    public void onFailureSaveUtilityReading(String strErr, int mode) {
        AppUtils.hideProgressDialog();

    }
}