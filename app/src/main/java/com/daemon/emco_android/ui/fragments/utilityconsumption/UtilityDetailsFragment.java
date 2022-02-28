package com.daemon.emco_android.ui.fragments.utilityconsumption;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.daemon.emco_android.R;
import com.daemon.emco_android.databinding.FragmentUtilityDetailsBinding;
import com.daemon.emco_android.model.common.AssetInfo;
import com.daemon.emco_android.model.common.UtilityAssetDetail;
import com.daemon.emco_android.utils.AppUtils;

import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static com.daemon.emco_android.utils.AppUtils.ARGS_ASSETINFO;
import static com.daemon.emco_android.utils.AppUtils.ARGS_UTILITY_ASSET;
import static com.daemon.emco_android.utils.Utils.TAG_FRAGMENT_UTILITY_DETAIL;
import static com.daemon.emco_android.utils.Utils.TAG_FRAGMENT_UTILITY_READING;


public class UtilityDetailsFragment extends Fragment {

    FragmentUtilityDetailsBinding binding;
    private AppCompatActivity mActivity;
    UtilityAssetDetail utilityAssetDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            utilityAssetDetail = (UtilityAssetDetail) getArguments().getSerializable(ARGS_UTILITY_ASSET);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = (AppCompatActivity) getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_utility_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvJobNo.setText(utilityAssetDetail.getJobno());
        binding.tvCompanyCode.setText(utilityAssetDetail.getOpco());
        binding.tvAssetBarcode.setText(utilityAssetDetail.getAssetBarcode());
        binding.tvAssetCode.setText(utilityAssetDetail.getAssetCode());
        binding.tvAssetCondition.setText(utilityAssetDetail.getAssetCondition());
        binding.tvAssetDescription.setText(utilityAssetDetail.getAssetDescription());
        binding.tvAssetMake.setText(utilityAssetDetail.getAssetMake());
        binding.tvAssetModel.setText(utilityAssetDetail.getAssetModel());
        binding.tvEqpName.setText(utilityAssetDetail.getEquipmentName());
        binding.tvNatureDescription.setText(utilityAssetDetail.getNatureDescription());
        binding.tvUtilityDet.setText(utilityAssetDetail.getAssetBarcode()+" - "+utilityAssetDetail.getEquipmentName());

        binding.fabMenu.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                switch(menuItem.getItemId()){

                    case R.id.action_manual:

                        loadFragment(new MeterEntryFragment(),utilityAssetDetail,TAG_FRAGMENT_UTILITY_READING);

                        break;

                    case R.id.action_image:

                        loadFragment(new MeterReadingFragment(),utilityAssetDetail,TAG_FRAGMENT_UTILITY_READING);

                        break;
                }
                return false;
            }
        });

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

}