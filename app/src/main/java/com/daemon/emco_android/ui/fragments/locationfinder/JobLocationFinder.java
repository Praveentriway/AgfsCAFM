package com.daemon.emco_android.ui.fragments.locationfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.OpcoDetail;
import com.daemon.emco_android.model.request.LocationDetail;
import com.daemon.emco_android.repository.remote.LocationFinderRepository;
import com.daemon.emco_android.ui.components.FilterableListDialog;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.utils.AppUtils;
import com.github.florent37.expectanim.ExpectAnim;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.StringUtil.space;
import static com.github.florent37.expectanim.core.Expectations.atItsOriginalPosition;
import static com.github.florent37.expectanim.core.Expectations.invisible;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.visible;

public class JobLocationFinder extends Fragment implements View.OnClickListener, LocationFinderRepository.LocationFinderInterface {

    private Toolbar mToolbar;
    private TextView tv_toolbar_title;
    private AppCompatActivity mActivity;
    private ExpectAnim expectAnimMove;
    private FragmentManager mManager;
    private Bundle mSavedInstanceState;
    private Bundle mArgs;
    private AppCompatTextView tv_lbl_opco,tv_lbl_contract_no,tv_lbl_zone,tv_lbl_building;
    AppCompatTextView tv_select_opco,tv_select_contract, tv_select_zone,tv_select_building;
    List<OpcoDetail> locationOpco;
    List<LocationDetail> locationJobNo;
    List<LocationDetail> locationBuilding;
    List<LocationDetail> locationZone;
    LocationDetail locationDetail=new LocationDetail();
    LinearLayout layout_main;
    View rootView;
    FloatingActionButton fab_fetch_location;

    public JobLocationFinder() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_opco: {
                showLocationOpco();
            }
            break;
            case R.id.tv_select_contract: {
                showLocationJobno();
            }
            break;
            case R.id.tv_select_zone: {
                showLocationZone();
            }
            break;
            case R.id.tv_select_building: {
                showLocationBuilding();
            }
            break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true);
        setRetainInstance(false);
        mManager = mActivity.getSupportFragmentManager();
        mSavedInstanceState = savedInstanceState;
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_job_location_finder, container, false);
        setupActionBar();
        initView(rootView);
        setProperties();
        return rootView;
    }

    private void initView(View v) {

        tv_lbl_opco=(AppCompatTextView) v.findViewById(R.id.tv_lbl_opco);
        layout_main=(LinearLayout) v.findViewById(R.id.layout_main);
        tv_lbl_contract_no=(AppCompatTextView) v.findViewById(R.id.tv_lbl_contract_no);
        tv_lbl_zone=(AppCompatTextView) v.findViewById(R.id.tv_lbl_zone);
        tv_lbl_building=(AppCompatTextView) v.findViewById(R.id.tv_lbl_building);
        tv_select_opco=(AppCompatTextView) v.findViewById(R.id.tv_select_opco);
        tv_select_contract=(AppCompatTextView) v.findViewById(R.id.tv_select_contract);
        tv_select_zone=(AppCompatTextView) v.findViewById(R.id.tv_select_zone);
        tv_select_building=(AppCompatTextView) v.findViewById(R.id.tv_select_building);

         fab_fetch_location=(FloatingActionButton) v.findViewById(R.id.fab_fetch_location);
        fab_fetch_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchLocationDetail();

            }
        });

        doAnimate();

    }
    private void  setProperties(){

        tv_lbl_opco.setText(Html.fromHtml("Opco"+ AppUtils.mandatory));
        tv_lbl_contract_no.setText(Html.fromHtml("Job Name"+ AppUtils.mandatory));
        tv_lbl_zone.setText(Html.fromHtml("Zone"+ AppUtils.mandatory));
        tv_lbl_building.setText(Html.fromHtml("Building Name"+ AppUtils.mandatory));
        tv_select_opco.setOnClickListener(this);
        tv_select_contract.setOnClickListener(this);
        tv_select_zone.setOnClickListener(this);
        tv_select_building.setOnClickListener(this);
        // Fetch OPCO List from server
        new LocationFinderRepository(mActivity,JobLocationFinder.this).getLocationOpco("");
    }
    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Job Location Finder");
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


    public void doAnimate(){
        new ExpectAnim()
                .expect(layout_main)
                .toBe(
                        outOfScreen(Gravity.TOP),
                        invisible()
                )
                .toAnimation()
                .setNow();

        this.expectAnimMove = new ExpectAnim()
                .expect(layout_main)
                .toBe(
                        atItsOriginalPosition(),
                        visible()
                )
                .toAnimation()
                .setDuration(800)
                .start();


        new ExpectAnim()
                .expect(fab_fetch_location)
                .toBe(
                        outOfScreen(Gravity.BOTTOM),
                        invisible()
                )
                .toAnimation()
                .setNow();

        this.expectAnimMove = new ExpectAnim()
                .expect(fab_fetch_location)
                .toBe(
                        atItsOriginalPosition(),
                        visible()
                )
                .toAnimation()
                .setDuration(800)
                .start();
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
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSuccessLocationOpco(List<OpcoDetail> opco, int mode){
        locationOpco=opco;
    }
    public void onFailureLocationOpco(String strMsg, int mode){
    }
    public void onSuccessLocationBuildingName(List<LocationDetail> location, int mode){
        locationBuilding=location;
    }
    public void onFailureLocationBuildingName(String strMsg, int mode){
    }
    public void onSuccessLocationZoneName(List<LocationDetail> location, int mode){
        locationZone=location;
    }
    public void onFailureLocationZoneName(String strMsg, int mode){

    }
    public void onSuccessLocationJobNo(List<LocationDetail> location, int mode){
        locationJobNo=location;
    }
    public void onFailureLocationJobNo(String strMsg, int mode){

    }
    public void onSuccessLocationDetail(LocationDetail location, int mode){

        AppUtils.hideProgressDialog();
        locationDetail=location;
        try {
                String strUri = "http://maps.google.com/maps?q=loc:" + location.getLat() + "," + location.getLng()+ " (" + locationDetail.getBuildingName() + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void onFailureLocationDetail(String strMsg, int mode){
        AppUtils.hideProgressDialog();
        AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Failed to fetch location.");
    }

    public  void showLocationOpco(){
        if(locationOpco!=null && locationOpco.size()>0){
            try {
                final ArrayList strArrayCustName = new ArrayList();
                for (OpcoDetail entity : locationOpco) {
                    strArrayCustName.add(entity.getOpcoCode()+" - "+entity.getOpcoName());
                }
                strArrayCustName.add(space);
                FilterableListDialog.create(
                        mActivity,
                        ("Select Opco"),
                        strArrayCustName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {
                                if(!text.equals(space)){

                                    locationDetail.setOpco(locationOpco.get(strArrayCustName.indexOf(text)).getOpcoCode());
                                    tv_select_opco.setText(text);
                                    tv_select_contract.setText("Select the Job Name");
                                    tv_select_zone.setText("Select the Zone");
                                    tv_select_building.setText("Select the Building Name");

                                    locationDetail.setJobno("");
                                    locationDetail.setJobName("");
                                    locationDetail.setZoneCode("");
                                    locationDetail.setZoneName("");
                                    locationDetail.setBuildingCode("");
                                    locationDetail.setBuildingName("");

                                    locationJobNo=null;
                                    locationZone=null;
                                    locationBuilding=null;

                                    new LocationFinderRepository(mActivity,JobLocationFinder.this).getLocationJobNo(locationDetail);

                                }
                            }
                        })
                        .show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else{
            Log.i("","no data to show");
        }
    }


    public  void showLocationJobno(){

        if(locationJobNo!=null && locationJobNo.size()>0){
            try {
                final ArrayList strArrayCustName = new ArrayList();
                for (LocationDetail entity : locationJobNo) {
                    strArrayCustName.add(entity.getJobno() +" - "+entity.getJobName());
                }
                strArrayCustName.add(space);
                FilterableListDialog.create(
                        mActivity,
                        ("Select Job number"),
                        strArrayCustName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {
                                if(!text.equals(space)){

                                    locationDetail.setJobno(locationJobNo.get(strArrayCustName.indexOf(text)).getJobno());
                                    locationDetail.setJobName(locationJobNo.get(strArrayCustName.indexOf(text)).getJobName());
                                    tv_select_contract.setText(text);

                                    tv_select_zone.setText("Select the Zone");
                                    tv_select_building.setText("Select the Building Name");

                                    locationDetail.setZoneCode("");
                                    locationDetail.setZoneName("");
                                    locationDetail.setBuildingCode("");
                                    locationDetail.setBuildingName("");

                                    locationZone=null;
                                    locationBuilding=null;

                                    new LocationFinderRepository(mActivity,JobLocationFinder.this).getLocationZone(locationDetail);
                                }
                            }
                        })
                        .show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else{
            Log.i("","no data to show");
        } }

        public  void showLocationZone(){

            if(locationZone!=null && locationZone.size()>0){
                try {
                    final ArrayList strArrayCustName = new ArrayList();
                    for (LocationDetail entity : locationZone) {
                        strArrayCustName.add(entity.getZoneCode() +" - "+entity.getZoneName());
                    }
                    strArrayCustName.add(space);
                    FilterableListDialog.create(
                            mActivity,
                            ("Select the Zone"),
                            strArrayCustName,
                            new FilterableListDialog.OnListItemSelectedListener() {
                                @Override
                                public void onItemSelected(String text) {
                                    if(!text.equals(space)){

                                        locationDetail.setZoneCode(locationZone.get(strArrayCustName.indexOf(text)).getZoneCode());
                                        locationDetail.setZoneName(locationZone.get(strArrayCustName.indexOf(text)).getZoneName());
                                        tv_select_zone.setText(text);
                                        tv_select_building.setText("Select the Building Name");
                                        locationDetail.setBuildingCode("");
                                        locationDetail.setBuildingName("");
                                        locationBuilding=null;
                                        new LocationFinderRepository(mActivity,JobLocationFinder.this).getLocationBuilding(locationDetail);
                                    }
                                }
                            })
                            .show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else{
                Log.i("","no data to show");
            }
    }


    public void showLocationBuilding(){
        if(locationBuilding!=null && locationBuilding.size()>0){
            try {
                final ArrayList strArrayCustName = new ArrayList();
                for (LocationDetail entity : locationBuilding) {
                    strArrayCustName.add(entity.getBuildingCode() +" - "+entity.getBuildingName());
                }
                strArrayCustName.add(space);
                FilterableListDialog.create(
                        mActivity,
                        ("Select the Building"),
                        strArrayCustName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {
                                if(!text.equals(space)){
                                    locationDetail.setBuildingCode(locationBuilding.get(strArrayCustName.indexOf(text)).getBuildingCode());
                                    locationDetail.setBuildingName(locationBuilding.get(strArrayCustName.indexOf(text)).getBuildingName());
                                    tv_select_building.setText(text);

                                }
                            }
                        })
                        .show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else{
            Log.i("","no data to show");
        }
    }


    public void fetchLocationDetail(){

        if(checkEmpty(locationDetail.getOpco())){
            AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Please select OPCO");
        }
        else if (checkEmpty(locationDetail.getJobName())) {
            AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Please select Job name");
        }
        else if (checkEmpty(locationDetail.getZoneName())) {
            AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Please select Zone");
        }
        else if (checkEmpty(locationDetail.getBuildingName())) {
            AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Please select Building name");
        }
        else{
            AppUtils.showProgressDialog(mActivity,"",true);
            new LocationFinderRepository(mActivity,JobLocationFinder.this).getLocationDetail(locationDetail);
        }
    }

    public boolean checkEmpty(String str){
        return((str==null || str.isEmpty()) ? true : false);
    }
}
