package com.daemon.emco_android.ui.fragments.document;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.os.Handler;

import com.daemon.emco_android.ui.fragments.preventive.ppmschedule.fragment_ppmfilter;
import com.daemon.emco_android.utils.AnimateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.PpmFilterService;
import com.daemon.emco_android.ui.components.FilterableListDialog;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.ui.components.Fragments.DateRangePickerFragment;
import com.daemon.emco_android.listeners.DatePickerDialogListener;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.request.PPMFilterRequest;
import com.daemon.emco_android.model.response.ContractDetails;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.checkInternet;
import static com.daemon.emco_android.utils.StringUtil.space;


public class DocumentFilter extends Fragment implements View.OnClickListener , DatePickerDialogListener, PpmFilterService.Listener, DateRangePickerFragment.OnDateRangeSelectedListener  {


    private static final String TAG = fragment_ppmfilter.class.getSimpleName();
    final Handler handler = new Handler();
    private AppCompatActivity mActivity;
    private Toolbar mToolbar;
    private AppCompatTextView tv_select_nature,tv_select_from_date,tv_select_to_date,tv_select_completed_to_date,tv_select_completed_from_date;
    private TextView tv_toolbar_title;
    AppCompatTextView tv_select_contract,tv_select_search_type;
    FloatingActionButton btnSearchComplaintByFilter;
    private Font font = App.getInstance().getFontInstance();
    List<String> natureDesc;
    View rootView;
    CardView linear_ppmno,
            linear_location_name,
            linear_nature,  linear_assetcode,
            linear_fromdate,
            linear_todate,linear_completed_fromdate,linear_completed_todate;
    boolean startDateClicked;
    boolean toDateClicked;
    boolean startCDateClicked;
    boolean toCDateClicked;
LinearLayout layout_main;
    private SharedPreferences mPreferences;
    private String mStrEmpId = null;
    private String mLoginData = null;
    String fromDate="",toDate="";
    String completedfromDate="",completedtoDate="";
    String naturalDesc="",contractNo="",contractNo1="",opco="";
    EditText tie_rx_no,tie_location,tie_asset_barcode;

    TextView tv_lbl__rx_no,
            tv_lbl_location,
            tv_lbl_nature, tv_lbl_from_date,
            tv_lbl_to_date,tv_lbl_contract,tv_lbl_search,tv_completed_lbl_to_date,tv_lbl_completed_from_date,tv_lbl_asset_barcode;
    int dialog_pos=-1;

    String sContractNo="",sContractNo1="", sPPMno="",sNaturaldesc="",sStartdate="",sEnddate="",sCStartdate="",sCEnddate="",sLocation="",sAssetcode="";
    PPMFilterRequest ppmfilter ;


    private Bundle mSavedInstanceState;
    private Bundle mArgs;
    String type;


    public CharSequence[] searchOptions;

    public CharSequence[] searchOptionsR  = {"All",
            "Date range",
            "Reactive or Complaint no",
            "Location name or Zone building",
            "Nature description"
    };

    public CharSequence[] searchOptionsP  = {"All",
            "Planned Date",
            "PPM NO",
            "Location name or Zone building",
            "Nature description","Completed Date",
            "Asset Barcode or Client Barcode"
    };

    List<ContractDetails> contractDetails;

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnSearchComplaintForAll: {

            }
            break;
            case R.id.btnSearchComplaintByFilter:  searchByFilter();
                break;
            case R.id.tv_select_contract : showContractNoDialog();
                break;
            case R.id.tv_select_nature :   {

                if (checkContractNo()) {
                    AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                    new PpmFilterService(getContext(),this).getNatureDescription(opco,contractNo);
                }
            }
            break;
            case R.id.tv_select_search_type : showSearchType();
                break;
            case R.id.tv_select_from_date :
            {
                startDateClicked=true;
                showDateSelection();
            }
            break;

            case R.id.tv_select_completed_from_date :
            {
                startCDateClicked=true;
                showDateSelection();
            }
            break;

            case R.id.tv_select_completed_to_date :
            {
                toCDateClicked=true;
                showDateSelection();
            }
            break;


            case R.id.tv_select_to_date :
            {
                toDateClicked=true;
                showDateSelection();
            }
            break;
        } }


    public DocumentFilter() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();
        mArgs = getArguments();
        setHasOptionsMenu(true);
        if (mArgs != null) {

            type=  mArgs.getString(AppUtils.ARGS_FILTERTYPE);
            if(type.equalsIgnoreCase("P")){
                searchOptions =searchOptionsP;
            }
            else{
                searchOptions =searchOptionsR;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_fragment_document_filter, container, false);
        initView();
        setProps();
        setupActionBar();
        if(ppmfilter!=null){
            findFilterItem();
        }
        else{
            ppmfilter =new PPMFilterRequest();
        }
        mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
        mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
        if (mLoginData != null) {
            Gson gson = new Gson();
            Login login = gson.fromJson(mLoginData, Login.class);
            mStrEmpId = login.getEmployeeId();
        }



        if (checkInternet(getContext())) {

            AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
            new PpmFilterService(getContext(),this).getContractDetails(mStrEmpId,type);

        }


        return rootView;
    }

    public void initView(){

        layout_main=(LinearLayout) rootView.findViewById(R.id. layout_main);
        btnSearchComplaintByFilter=(FloatingActionButton) rootView.findViewById(R.id. btnSearchComplaintByFilter);
        tv_select_contract=(AppCompatTextView) rootView.findViewById(R.id. tv_select_contract);
        tv_select_nature=(AppCompatTextView) rootView.findViewById(R.id. tv_select_nature);
        tv_select_search_type=(AppCompatTextView) rootView.findViewById(R.id. tv_select_search_type);
        tv_select_from_date=(AppCompatTextView) rootView.findViewById(R.id. tv_select_from_date);
        tv_select_to_date=(AppCompatTextView) rootView.findViewById(R.id. tv_select_to_date);

        tv_select_completed_from_date=(AppCompatTextView) rootView.findViewById(R.id. tv_select_completed_from_date);
        tv_select_completed_to_date=(AppCompatTextView) rootView.findViewById(R.id. tv_select_completed_to_date);

        linear_ppmno=(CardView) rootView.findViewById(R.id. linear_ppmno);
        linear_location_name=(CardView) rootView.findViewById(R.id. linear_location_name);

        linear_nature=(CardView) rootView.findViewById(R.id. linear_nature);
        linear_assetcode=(CardView) rootView.findViewById(R.id. linear_assetcode);
        linear_fromdate=(CardView) rootView.findViewById(R.id. linear_fromdate);
        linear_todate=(CardView) rootView.findViewById(R.id. linear_todate);

        linear_completed_fromdate=(CardView) rootView.findViewById(R.id. linear_completed_fromdate);
        linear_completed_todate=(CardView) rootView.findViewById(R.id. linear_completed_todate);

        tie_rx_no=(EditText) rootView.findViewById(R.id. tie_rx_no);
        tie_location=(EditText) rootView.findViewById(R.id. tie_location);
        tv_lbl__rx_no=(TextView) rootView.findViewById(R.id. tv_lbl__rx_no);
        tv_lbl_location=(TextView) rootView.findViewById(R.id. tv_lbl_location);
        tv_lbl_nature=(TextView) rootView.findViewById(R.id. tv_lbl_nature);
        tv_lbl_from_date=(TextView) rootView.findViewById(R.id. tv_lbl_from_date);
        tv_lbl_to_date=(TextView) rootView.findViewById(R.id. tv_lbl_to_date);
        tv_lbl_asset_barcode=(TextView) rootView.findViewById(R.id. tv_lbl_asset_barcode);

        tv_completed_lbl_to_date=(TextView) rootView.findViewById(R.id. tv_completed_lbl_to_date);
        tv_lbl_completed_from_date=(TextView) rootView.findViewById(R.id. tv_lbl_completed_from_date);

        tv_lbl_nature=(TextView) rootView.findViewById(R.id. tv_lbl_to_date);
        //   til_location=(TextInputLayout) rootView.findViewById(R.id. til_location);
        tv_lbl_contract =(TextView) rootView.findViewById(R.id. tv_lbl_contract);
        tie_asset_barcode=(EditText) rootView.findViewById(R.id. tie_asset_barcode);
        tv_lbl_search=(TextView) rootView.findViewById(R.id. tv_lbl_search);

    }

    public void setProps(){



        if(type.equalsIgnoreCase("R")){
            tv_lbl__rx_no.setText(Html.fromHtml(getString(R.string.rx_no_workorder_no) + AppUtils.mandatory));
        }
        else{
            tv_lbl__rx_no.setText(Html.fromHtml("PPM NO" + AppUtils.mandatory));
        }

        tv_lbl_location.setText(Html.fromHtml(getString(R.string.location_name_zone_building) + AppUtils.mandatory));
        tv_lbl_nature.setText(Html.fromHtml(getString(R.string.select_nature) + AppUtils.mandatory));
        tv_lbl_from_date.setText(Html.fromHtml(getString(R.string.lbl_from_date) + AppUtils.mandatory));
        tv_lbl_to_date.setText(Html.fromHtml(getString(R.string.lbl_to_date) + AppUtils.mandatory));
        tv_lbl_contract.setText(Html.fromHtml(getString(R.string.contract_no) + AppUtils.mandatory));
        tv_lbl_search.setText(Html.fromHtml(getString(R.string.search_by) + AppUtils.mandatory));
        tv_lbl_asset_barcode.setText(Html.fromHtml("Asset Barcode or Client Barcode" + AppUtils.mandatory));

        tv_lbl_completed_from_date.setText(Html.fromHtml(getString(R.string.lbl_from_date) + AppUtils.mandatory));
        tv_completed_lbl_to_date.setText(Html.fromHtml(getString(R.string.lbl_to_date) + AppUtils.mandatory));

        btnSearchComplaintByFilter.setOnClickListener(this);
        tv_select_contract.setOnClickListener(this);
        tv_select_nature.setOnClickListener(this);
        tv_select_search_type.setOnClickListener(this);
        tv_select_from_date.setOnClickListener(this);
        tv_select_to_date.setOnClickListener(this);
        tv_select_completed_from_date.setOnClickListener(this);
        tv_select_completed_to_date.setOnClickListener(this);

        tv_select_nature.setTypeface(font.getHelveticaRegular());
        tv_select_search_type.setTypeface(font.getHelveticaRegular());
        tv_select_from_date.setTypeface(font.getHelveticaRegular());
        tv_select_to_date.setTypeface(font.getHelveticaRegular());

        new AnimateUtils().fabAnimate(btnSearchComplaintByFilter);
        new AnimateUtils().filterLayoutAnimate(layout_main);

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);
        tv_toolbar_title.setText("Document Upload Filter");
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

    public void loadFragment(final Fragment fragment, final String tag) {
        Log.d(TAG, "loadFragment");
        // update the main content by replacing fragments
        Bundle mdata = new Bundle();
        mdata.putSerializable(AppUtils.ARGS_PPMFILTER,ppmfilter);
        mdata.putString(AppUtils.ARGS_FILTERTYPE, type);
        fragment.setArguments(mdata);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        // fragmentTransaction.hide(getFragmentManager().findFragmentByTag(Utils.fragment_ppmfilter));
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(TAG, "onPrepareOptionsMenu ");
        MenuItem item = menu.findItem(R.id.action_logout);
        item.setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Log.d(TAG, "onOptionsItemSelected : home");
                // mActivity.onBackPressed();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                Fragment _fragment = new MainDashboard();
                FragmentTransaction _transaction = getActivity().getSupportFragmentManager().beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onContractDetailsReceived(List<ContractDetails> contractDetails) {
        AppUtils.hideProgressDialog();
        this.contractDetails=contractDetails;
    }

    @Override
    public void onContractDetailsReceivedError(String errMsg, int from) {

        AppUtils.hideProgressDialog();
    }

    @Override
    public void onNatureDescReceived(List<String> natureDesc) {
        // AppUtils.hideProgressDialog();
        AppUtils.hideProgressDialog();
        this.natureDesc=natureDesc;
        showNaturalDescDialog();
    }

    @Override
    public void onNatureDescReceivedError(String errMsg, int from) {
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(getContext(),"Error while fetching Natural description.");

    }

    public  void showContractNoDialog(){

        if(contractDetails!=null && contractDetails.size()>0){

            try {

                final ArrayList strArraySiteName = new ArrayList();
//                strArraySiteName.add("All");
                for (ContractDetails entity : contractDetails) {
                    strArraySiteName.add(entity.getContractNo()+ " - "+entity.getContractName());
                }

                strArraySiteName.add(space);

                FilterableListDialog.create(
                        mActivity,
                        ("Select Contract No."),
                        strArraySiteName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {

                                if(!text.equals(space)){

//                                   if(text.equalsIgnoreCase("ALL")){
//                                       contractNo="All";
//                                       opco="All";
//                                       tv_select_contract.setText(text);
//                                   }
//                                   else{
                                       contractNo=text.split("\\s+")[0];
                                       contractNo1=text;
                                       opco=contractDetails.get((strArraySiteName.indexOf(text))).getOpco();
                                       tv_select_contract.setText(text);
                                       tv_select_contract.setTypeface(font.getHelveticaBold());
//                                   }
                                }
                            }
                        })
                        .show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else{
         //   new PpmFilterService(getContext(),this).getContractDetails(mStrEmpId,type);

        }
    }
    public  void showNaturalDescDialog(){

        if(natureDesc!=null && natureDesc.size()>0){

            try {

                ArrayList strArraySiteName = new ArrayList(natureDesc);
                strArraySiteName.add("");
                strArraySiteName.add("");


                FilterableListDialog.create(
                        mActivity,
                        ("Select Nature Description"),
                        strArraySiteName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {

                                if(!text.isEmpty()){

                                    naturalDesc=text;
                                    tv_select_nature.setText(text);
                                    tv_select_nature.setTypeface(font.getHelveticaBold());
                                    //    AppUtils.setErrorBg(tv_select_nature, false);

                                } }
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

    public void showSearchType(){
        showDialog(getContext(), "Select your search option", new String[] { "Ok" },
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                        if(selectedPosition!=-1){
                            dialog_pos=selectedPosition;
                            tv_select_search_type.setText(searchOptions[selectedPosition]);
                            tv_select_search_type.setTypeface(font.getHelveticaBold());
                            // tv_select_search_type.setTypeface(font.getHelveticaBold());

                            //  AppUtils.setErrorBg(tv_select_search_type, false);
                            showSearchOptions(searchOptions[selectedPosition].toString());
                        }
                    }
                });

    }

    public void showDialog(Context context, String title, String[] btnText,
                           DialogInterface.OnClickListener listener) {
        if (listener == null)
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    paramDialogInterface.dismiss();
                }
            };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        builder.setSingleChoiceItems(searchOptions, dialog_pos,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                    }
                });
        builder.setPositiveButton(btnText[0], listener);
        if (btnText.length != 1) {
            builder.setNegativeButton(btnText[1], listener);
        }
        builder.show();
    }

    public void showSearchOptions(String val){

        clearVals();

        switch (val){

            case "Date range":
            {
                linear_ppmno.setVisibility(View.GONE);
                linear_location_name.setVisibility(View.GONE);
                linear_nature.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.VISIBLE);
                linear_todate .setVisibility(View.VISIBLE);
            }

            break;

            case "Planned Date":
            {
                linear_nature.setVisibility(View.GONE);
                linear_ppmno.setVisibility(View.GONE);
                linear_location_name.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.VISIBLE);
                linear_todate .setVisibility(View.VISIBLE);
            }

            break;

            case "Completed Date":
            {
                linear_nature.setVisibility(View.GONE);
                linear_ppmno.setVisibility(View.GONE);
                linear_location_name.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.VISIBLE);
                linear_completed_todate.setVisibility(View.VISIBLE);
                linear_fromdate.setVisibility(View.GONE);
                linear_todate .setVisibility(View.GONE);
            }

            break;

            case "Reactive or Complaint no":
            {
                linear_ppmno.setVisibility(View.VISIBLE);
                linear_location_name.setVisibility(View.GONE);
                linear_nature.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_todate .setVisibility(View.GONE);
            }

            break;

            case "PPM NO":
            {
                linear_location_name.setVisibility(View.GONE);
                linear_ppmno.setVisibility(View.VISIBLE);
                linear_assetcode.setVisibility(View.GONE);
                linear_nature.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_todate .setVisibility(View.GONE);
            }
            break;

            case "Asset Barcode or Client Barcode":
            {
                linear_ppmno.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.VISIBLE);
                linear_location_name.setVisibility(View.GONE);
                linear_nature.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_todate .setVisibility(View.GONE);
            }

            break;

            case "Location name or Zone building":
            {
                linear_ppmno.setVisibility(View.GONE);
                linear_location_name.setVisibility(View.VISIBLE);
                linear_nature.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_todate .setVisibility(View.GONE);
            }

            break;

            case "Nature description":
            {
                linear_ppmno.setVisibility(View.GONE);
                linear_location_name.setVisibility(View.GONE);
                linear_nature.setVisibility(View.VISIBLE);
                linear_completed_fromdate.setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_todate .setVisibility(View.GONE);
            }

            break;

            case "All": {
                linear_ppmno.setVisibility(View.GONE);
                linear_location_name.setVisibility(View.GONE);
                linear_nature.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_todate.setVisibility(View.GONE);
            }

                break;
        }


    }

    public void showDateSelection(){
        AppUtils.datePickerDialog(mActivity, this, null, null);
    }

    @Override
    public void onDateReceivedSuccess(String strDate) {
        Log.d(TAG, "onDateReceivedSuccess " + strDate);
        if(startDateClicked){
            fromDate=(strDate);
            tv_select_from_date.setText(strDate);
            tv_select_from_date.setTypeface(font.getHelveticaBold());

        }

        if(toDateClicked){
            toDate=strDate;
            tv_select_to_date.setText(strDate);
            tv_select_to_date.setTypeface(font.getHelveticaBold());

        }

        if(startCDateClicked){
            completedfromDate=strDate;
            tv_select_completed_from_date.setText(strDate);
            tv_select_completed_from_date.setTypeface(font.getHelveticaBold());

        }


        if(toCDateClicked){
            completedtoDate=strDate;
            tv_select_completed_to_date.setText(strDate);
            tv_select_completed_to_date.setTypeface(font.getHelveticaBold());

        }

        startDateClicked=false;
         toDateClicked=false;
         startCDateClicked=false;
         toCDateClicked=false;

    }


    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        Log.d("range : ","from: "+startDay+"-"+startMonth+"-"+startYear+" to : "+endDay+"-"+endMonth+"-"+endYear );
    }

    public void clearVals(){
        tv_select_nature.setText(getContext().getString(R.string.select_nature));
        tv_select_from_date.setText(getContext().getString(R.string.lbl_from_date));
        tie_asset_barcode.setText("");
        tv_select_to_date.setText(getContext().getString(R.string.lbl_to_date));
        tie_rx_no.setText("");
        tie_location.setText("");
        tv_select_completed_from_date.setText(getContext().getString(R.string.lbl_from_date));
        tv_select_completed_to_date.setText(getContext().getString(R.string.lbl_to_date));
        fromDate="";
        toDate="";
        completedfromDate="";
        completedtoDate="";

        sContractNo=""; sContractNo1=""; sPPMno=""; sNaturaldesc="" ;sStartdate="" ; sEnddate=""; sLocation=""; sCStartdate="";sCEnddate="";
        sAssetcode="";
    }


    public void searchByFilter(){

        ppmfilter =new PPMFilterRequest();
        sContractNo=contractNo;
        sContractNo1=contractNo1;

        switch (tv_select_search_type.getText().toString()) {

            case "Date range":
            {
                if(fromDate.trim().isEmpty() ){
                    AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "From Date field should not be empty.");
                }
                else if(toDate.trim().isEmpty()){
                    AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "To Date field should not be empty.");
                }
                else{
                    //  til_ppm_no.setErrorEnabled(false);

                    if(checkContractNo()){

                        ArrayList<String> sDate=new ArrayList<>();
                        sDate.add(fromDate);
                        sDate.add(toDate);
                        ArrayList<String> tDate=new ArrayList<>();
                        ppmfilter.setStartDate(sDate);
                        ppmfilter.setEndDate(tDate);
                        sStartdate=fromDate;
                        sEnddate=toDate;
                        loadFragment(new DocumentComplaintList(), Utils.TAG_DOC_COMPLAINT_LIST);
                    }
                }

                break;
            }

            case "Completed Date":
            {
                if(completedfromDate.trim().isEmpty() ){
                    AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "From Date field should not be empty");
                }
                else if(completedtoDate.trim().isEmpty()){
                    AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "To Date field should not be empty");
                }
                else{

                    if(checkContractNo()){

                        ArrayList<String> sDate=new ArrayList<>();
                        sDate.add(completedfromDate);
                        sDate.add(completedtoDate);
                        ArrayList<String> tDate=new ArrayList<>();
                        ppmfilter.setCompletedDate(sDate);
                        sCStartdate=completedfromDate;
                        sCEnddate=completedtoDate;
                        loadFragment(new DocumentComplaintList(), Utils.TAG_DOC_COMPLAINT_LIST);

                    }
                }

                break;

            }

            case "Planned Date":
            {
                if(fromDate.trim().isEmpty() ){
                    AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "From Date field should not be empty");
                }
                else if(toDate.trim().isEmpty()){
                    AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "To Date field should not be empty");
                }
                else{

                    if(checkContractNo()){
                        ArrayList<String> sDate=new ArrayList<>();
                        sDate.add(fromDate);
                        sDate.add(toDate);
                        ArrayList<String> tDate=new ArrayList<>();
                        ppmfilter.setStartDate(sDate);
                        ppmfilter.setEndDate(tDate);
                        sStartdate=fromDate;
                        sEnddate=toDate;
                        loadFragment(new DocumentComplaintList(), Utils.TAG_DOC_COMPLAINT_LIST);
                    }
                }

                break;

            }

            case "Reactive or Complaint no":
            {
                if(tie_rx_no.getText().toString().trim().isEmpty()){
                    // til_ppm_no.setError("PPM No. / WorkOrder No. field should not be empty.");

                       AppUtils.showSnackBar(R.id.coordinatorLayout,rootView,"Reactive/Complaint No. field should not be empty.");

                }
                else{


                    if(checkPPMValidation()){

                        if(checkContractNo()){
                            ppmfilter.setPpmNo(tie_rx_no.getText().toString());
                            sPPMno=tie_rx_no.getText().toString();
                            loadFragment(new DocumentComplaintList(), Utils.TAG_DOC_COMPLAINT_LIST);
                        } } }
                break;
            }


            case "PPM NO":
            {
                if(tie_rx_no.getText().toString().trim().isEmpty()){
                        AppUtils.showSnackBar(R.id.coordinatorLayout,rootView,"PPM No. field should not be empty.");
                }
                else{

                    if(checkPPMValidation()){

                        if(checkContractNo()){
                            ppmfilter.setPpmNo(tie_rx_no.getText().toString());
                            sPPMno=tie_rx_no.getText().toString();
                            loadFragment(new DocumentComplaintList(), Utils.TAG_DOC_COMPLAINT_LIST);
                        } } }
                break;
            }


            case "Asset Barcode or Client Barcode":
            {
                if(tie_asset_barcode.getText().toString().trim().isEmpty()){
                    //  til_asset_barcode.setError("Asset Code / Asset Barcode field should not be empty.");
                    AppUtils.showSnackBar(R.id.coordinatorLayout,rootView,getString(R.string.assest_msg));
                }
                else{
                    //   til_asset_barcode.setErrorEnabled(false);

                    if(checkAssetCodeValidation()){
                        if(checkContractNo()){
                            ppmfilter.setAssetBarcode(tie_asset_barcode.getText().toString());
                            sAssetcode=tie_asset_barcode.getText().toString();
                            loadFragment(new DocumentComplaintList(), Utils.TAG_DOC_COMPLAINT_LIST);
                        }

                    }
                }
                break;
            }


            case "Location name or Zone building":
            {
                if(tie_location.getText().toString().trim().isEmpty()){
                    //  til_location.setError("Location Name / Zone Building field should not be empty.");

                    AppUtils.showSnackBar(R.id.coordinatorLayout,rootView,"Location Name / Zone Building field should not be empty.");
                }
                else{
                    //     til_location.setErrorEnabled(false);

                    if(checkLocationValidation()){
                        if(checkContractNo()){
                            ppmfilter.setZoneBuilding(tie_location.getText().toString());
                            sLocation=tie_location.getText().toString();
                            loadFragment(new DocumentComplaintList(), Utils.TAG_DOC_COMPLAINT_LIST);
                        }
                    }
                }
                break;
            }

            case "Nature description":
            {
                if(tv_select_nature.getText().toString().trim().isEmpty() || tv_select_nature.getText().toString().equalsIgnoreCase(getContext().getString(R.string.select_nature))){

                    AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Nature description should be selected.");

                }
                else{

                    if(checkContractNo()){
                        ppmfilter.setNatureDescription(tv_select_nature.getText().toString());
                        sNaturaldesc=tv_select_nature.getText().toString();
                        loadFragment(new DocumentComplaintList(), Utils.TAG_DOC_COMPLAINT_LIST);
                    }
                }

                break;
            }

            case "All": {

                if(checkContractNo()){
                    loadFragment(new DocumentComplaintList(), Utils.TAG_DOC_COMPLAINT_LIST);
                }

            }
            break;

            default:
            {
                AppUtils.showSnackBar(R.id.coordinatorLayout,rootView,"Select any search option and proceed.");

                break;
            }
        }
    }

    public boolean checkContractNo(){
        if(tv_select_contract.getText().toString().trim().isEmpty() || tv_select_contract.getText().toString().equalsIgnoreCase(getContext().getString(R.string.select_contract))){
            AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Contract no. should not be empty.");
            return false;
        }
        else{
            ppmfilter.setContractNo(contractNo);
            ppmfilter.setCompCode(opco);
            return  true;
        }
    }

    public boolean checkPPMValidation(){
        if(tie_rx_no.getText().toString().trim().length()<=2 ){

            AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Complaint No. field should contain more than two characters.");
            return false;
        }
        else{
            //  til_ppm_no.setErrorEnabled(false);
            return  true;
        }
    }

    public boolean checkAssetCodeValidation(){

        if(tie_asset_barcode.getText().toString().trim().length()<=2 ){

            AppUtils.showSnackBar(R.id.coordinatorLayout,rootView, "Asset Barcode or Client Barcode field should contain more than two characters.");

             return false;
        }
        else{
            // til_asset_barcode.setErrorEnabled(false);
            return  true;
        }
    }


    public boolean checkLocationValidation(){
        if(tie_location.getText().toString().trim().length()<=2 ){
            //   til_location.setError("Location Name or Zone Building field should contain more than two characters.");
            AppUtils.showSnackBar(R.id.coordinatorLayout,rootView,"Location Name or Zone Building field should contain more than two characters.");
            return false;
        }
        else{
            //   til_location.setErrorEnabled(false);
            return  true;
        }
    }
    public void findFilterItem(){
        if(!sContractNo1.trim().isEmpty()){
            tv_select_contract.setText(sContractNo1);
        }

        if(type.equalsIgnoreCase("R")){
            if(!sStartdate.trim().isEmpty()){
                setFilterFromcache(1);
            }
            else  if(!sPPMno.trim().isEmpty()){
                setFilterFromcache(2);
            }
            else  if(!sLocation.trim().isEmpty()){
                setFilterFromcache(3);
            }
            else  if(!sNaturaldesc.trim().isEmpty()){
                setFilterFromcache(4);
            }
            else{
                setFilterFromcache(0);
            }
        }
        else{

            if(!sStartdate.trim().isEmpty()){
                setFilterFromcache(1);
            }
            else  if(!sPPMno.trim().isEmpty()){
                setFilterFromcache(2);
            }
            else  if(!sLocation.trim().isEmpty()){
                setFilterFromcache(3);
            }
            else  if(!sNaturaldesc.trim().isEmpty()){
                setFilterFromcache(4);
            }
            else  if(!sCStartdate.trim().isEmpty()){
                setFilterFromcache(5);
            }

            else  if(!sAssetcode.trim().isEmpty()){
                setFilterFromcache(6);
            }
            else{
                setFilterFromcache(0);
            }
        }

    }
    public void setFilterFromcache(int pos){
        dialog_pos=pos;
        tv_select_search_type.setText(searchOptions[pos].toString());
        //  tv_select_search_type.setTypeface(font.getHelveticaBold());
        //  AppUtils.setErrorBg(tv_select_search_type, false);

        switch (searchOptions[pos].toString()){

            case "All":
                linear_ppmno.setVisibility(View.GONE);
                linear_location_name.setVisibility(View.GONE);
                linear_nature.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                linear_todate.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                break;

            case "Date range":
            {
                linear_ppmno.setVisibility(View.GONE);
                linear_location_name.setVisibility(View.GONE);
                linear_nature.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.VISIBLE);
                linear_todate .setVisibility(View.VISIBLE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                tv_select_from_date.setText(sStartdate);
                tv_select_from_date.setTypeface(font.getHelveticaBold());
                tv_select_to_date.setText(sEnddate);
                tv_select_to_date.setTypeface(font.getHelveticaBold());

            }
            break;

            case "Planned Date":
            {
                linear_ppmno.setVisibility(View.GONE);
                linear_location_name.setVisibility(View.GONE);
                linear_nature.setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                linear_todate .setVisibility(View.VISIBLE);
                linear_fromdate.setVisibility(View.VISIBLE);
                tv_select_from_date.setText(sStartdate);
                tv_select_from_date.setTypeface(font.getHelveticaBold());
                tv_select_to_date.setText(sEnddate);
                tv_select_to_date.setTypeface(font.getHelveticaBold());

            }
            break;

            case "Completed Date":
            {
                linear_ppmno.setVisibility(View.GONE);
                linear_location_name.setVisibility(View.GONE);
                linear_nature.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_todate .setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.VISIBLE);
                linear_completed_fromdate.setVisibility(View.VISIBLE);

                tv_select_completed_from_date.setText(sCStartdate);
                tv_select_completed_from_date.setTypeface(font.getHelveticaBold());
                tv_select_completed_to_date.setText(sCEnddate);
                tv_select_completed_to_date.setTypeface(font.getHelveticaBold());

            }
            break;

            case "Asset Barcode or Client Barcode":
            {
                linear_ppmno.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.VISIBLE);
                linear_location_name.setVisibility(View.GONE);
                linear_nature.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_todate .setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                tie_asset_barcode.setText(sAssetcode);
                tie_asset_barcode.setTypeface(font.getHelveticaBold());
            }
            break;


            case "Reactive or Complaint no":
            {
                linear_ppmno.setVisibility(View.VISIBLE);
                linear_location_name.setVisibility(View.GONE);
                linear_nature.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                linear_todate .setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                tie_rx_no.setText(sPPMno);
                tie_rx_no.setTypeface(font.getHelveticaBold());
            }
            break;

            case "PPM NO":
            {
                linear_ppmno.setVisibility(View.VISIBLE);
                linear_location_name.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_nature.setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                linear_todate .setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                tie_rx_no.setText(sPPMno);
                tie_rx_no.setTypeface(font.getHelveticaBold());
            }
            break;

            case "Location name or Zone building":
            {
                linear_ppmno.setVisibility(View.GONE);
                linear_location_name.setVisibility(View.VISIBLE);
                linear_nature.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_todate .setVisibility(View.GONE);
                linear_assetcode.setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                tie_location.setText(sLocation);
                tie_location.setTypeface(font.getHelveticaBold());
            }
            break;

            case "Nature description":
            {
                linear_ppmno.setVisibility(View.GONE);
                linear_location_name.setVisibility(View.GONE);
                linear_nature.setVisibility(View.VISIBLE);
                linear_assetcode.setVisibility(View.GONE);
                linear_fromdate.setVisibility(View.GONE);
                linear_todate .setVisibility(View.GONE);
                linear_completed_todate.setVisibility(View.GONE);
                linear_completed_fromdate.setVisibility(View.GONE);
                tv_select_nature.setText(sNaturaldesc);
                tv_select_nature.setTypeface(font.getHelveticaBold());
            }
            break;

        }
        sContractNo="";sContractNo1=""; sPPMno=""; sNaturaldesc="" ;sStartdate="" ; sEnddate=""; sLocation=""; sCStartdate="";sCEnddate="";sCEnddate="";sAssetcode="";
    }

}
