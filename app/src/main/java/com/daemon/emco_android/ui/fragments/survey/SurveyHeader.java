package com.daemon.emco_android.ui.fragments.survey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.daemon.emco_android.model.response.SurveyFloorFlat;
import com.daemon.emco_android.repository.db.entity.SurveyEmployeeList;
import com.daemon.emco_android.utils.AnimateUtils;
import com.daemon.emco_android.utils.RightDrawableOnTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.CustomerSurveyRepository;
import com.daemon.emco_android.ui.components.FilterableListDialog;
import com.daemon.emco_android.repository.db.entity.ServeyQuestionnaire;
import com.daemon.emco_android.repository.db.entity.SurveyContract;
import com.daemon.emco_android.repository.db.entity.SurveyCustomer;
import com.daemon.emco_android.repository.db.entity.SurveyLocation;
import com.daemon.emco_android.repository.db.entity.SurveyMaster;
import com.daemon.emco_android.repository.db.entity.SurveyTransaction;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYTYPE;
import static com.daemon.emco_android.utils.AppUtils.checkInternet;
import static com.daemon.emco_android.utils.AppUtils.closeKeyboard;
import static com.daemon.emco_android.utils.AppUtils.showErrorToast;
import static com.daemon.emco_android.utils.StringUtil.space;


public class SurveyHeader extends Fragment implements CustomerSurveyRepository.Listener, View.OnClickListener {

    private static final String TAG = SurveyHeader.class.getSimpleName();
    private AppCompatActivity mActivity;
    Toolbar mToolbar;
    TextView tv_toolbar_title,tv_select_contract,tv_select_customer,tv_select_reference;
    FloatingActionButton fab_next;
    View view;
    List<SurveyCustomer> surveyCustomers;
    List<SurveyContract> surveyContracts;
    List<SurveyMaster> surveyReferences;
    String customerCode="",contract="",reference="";
    String customerName="",contractNo="",surveyReference="";
    private Font font = App.getInstance().getFontInstance();
    LinearLayout layout_main;
    CardView linear1,linear2,linear3;
    SurveyTransaction surveyTransaction;
    private SharedPreferences mPreferences;
    private String mStrEmpId = null;
    private String mLoginData = null;

    public String ALL_BUILDINGS="ALL-BUILDINGS";
    List<SurveyLocation> surveyLocations=new ArrayList<>();
    List<SurveyLocation> surveyReviwer;
    List<SurveyFloorFlat> surveyFloorFlats;
    public static String DETAILED="Detail";
    public static String SUMMARY="Summary";

    String surveyType;
    AppCompatTextView tv_lbl_custcode,tv_lbl_tenant_name, tv_lbl_contract_no, tv_lbl_reference, tv_lbl_location, tv_lbl_client_name, tv_lbl_designation, tv_lbl_email, tv_lbl_contact;
    String type;
    boolean suggestionFlag;
    AutoCompleteTextView tie_location_name,
            tie_floor_flat;

    public SurveyHeader() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_select_reference:{

                if(customerCode.equalsIgnoreCase("")){
                    showErrorToast(mActivity,"Customer should not be empty.");
                }
                else{
                    new CustomerSurveyRepository(mActivity,this).getSurveyRefernce(customerCode);
                }
            }
            break;

            case R.id.tv_select_contract: {

                if(type.equalsIgnoreCase("Tenant")){
                    showSurveyContractDialog();
                }
                else{
                    if(customerCode.equalsIgnoreCase("")){
                        showErrorToast(mActivity,"Customer should not be empty.");
                    }
                    else{
                        new CustomerSurveyRepository(mActivity,this).getSurveyContract(mStrEmpId,customerCode);
                    }
                }
            }
            break;

            case R.id.tv_select_customer :
                showSurveyCustomerDialog();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true);

        if(getArguments()!=null) {
            type = (String) getArguments().getSerializable(ARGS_SURVEYTYPE);
        }

        surveyTransaction=new SurveyTransaction();

        mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
        mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);

        if (mLoginData != null) {
            Gson gson = new Gson();
            Login login = gson.fromJson(mLoginData, Login.class);
            mStrEmpId = login.getEmployeeId();
        }

        surveyTransaction.setCreatedBy(mStrEmpId);
        surveyTransaction.setSurveyFrom(type);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_customer_feed_back_header, container, false);
        setupActionBar();
        setProps();
        setCacheVals();

        fab_next=(FloatingActionButton) view.findViewById(R.id.fab_next);
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed_questionnaire();
            }
        });
        new AnimateUtils().fabAnimate(fab_next);
        new AnimateUtils().filterLayoutAnimate(layout_main);
        return view;

    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbar.setTitle("my title");
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText( "Please fill the "+type+" Details");
        LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showbackPressAlert();
                    }
                });
    }

    public void showbackPressAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(getString(R.string.close_survey))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mActivity.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setProps() {

        layout_main=(LinearLayout) view.findViewById(R.id.layout_main);
        linear1=(CardView) layout_main.findViewById(R.id.linear1);
        linear2=(CardView) layout_main.findViewById(R.id.linear2);
        linear3=(CardView) layout_main.findViewById(R.id.linear3);
        tv_select_contract=(TextView) view.findViewById(R.id.tv_select_contract);
        tv_select_customer=(TextView) view.findViewById(R.id.tv_select_customer);
        tv_select_reference=(TextView) view.findViewById(R.id.tv_select_reference);
        tv_lbl_custcode=(AppCompatTextView) view.findViewById(R.id.tv_lbl_custcode);
        tv_lbl_contract_no=(AppCompatTextView) view.findViewById(R.id.tv_lbl_contract_no);
        tv_lbl_reference=(AppCompatTextView) view.findViewById(R.id.tv_lbl_reference);
        tv_lbl_location=(AppCompatTextView) view.findViewById(R.id.tv_lbl_location);
        tv_lbl_client_name=(AppCompatTextView) view.findViewById(R.id.tv_lbl_client_name);
        tv_lbl_designation=(AppCompatTextView) view.findViewById(R.id.tv_lbl_designation);
        tv_lbl_tenant_name=(AppCompatTextView) view.findViewById(R.id.tv_lbl_designation);
        tv_lbl_email=(AppCompatTextView) view.findViewById(R.id.tv_lbl_email);
        tv_lbl_contact=(AppCompatTextView) view.findViewById(R.id.tv_lbl_contact);
        tv_lbl_custcode.setText(Html.fromHtml("Customer" + AppUtils.mandatory));
        tv_lbl_reference.setText(Html.fromHtml("Survey Reference" + AppUtils.mandatory));
        tv_lbl_location.setText(Html.fromHtml("Location" + AppUtils.mandatory));

       ImageView right_btn_location_name=(ImageView) view.findViewById(R.id.right_btn_location_name) ;
        right_btn_location_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSearch(tie_location_name);
            }
        });

        ImageView right_btn_floor_flat_name=(ImageView) view.findViewById(R.id.right_btn_floor_flat_name) ;
        right_btn_floor_flat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSearch(tie_floor_flat);
            }
        });

        tie_location_name=(AutoCompleteTextView) view.findViewById(R.id.tie_location_name);
        tie_floor_flat=(AutoCompleteTextView) view.findViewById(R.id.tie_floor_flat);

        tv_select_contract.setOnClickListener(this);
        tv_select_customer.setOnClickListener(this);
        tv_select_reference.setOnClickListener(this);

        tie_location_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {

                if(type.equalsIgnoreCase("Customer")) {

                    if(surveyLocations.get(position).getReviewerName()!=null || !(surveyLocations.get(position).getReviewerName().equalsIgnoreCase(""))){

                        String title="You have other information to auto fill.";

                        String   StrMsg = "Reviewer Name :   "
                                + surveyLocations.get(position).getReviewerName()
                                + "\n"
                                +"Designation :   "
                                + surveyLocations.get(position).getDesignation()
                                + "\n"
                                +"Contact No :   "
                                + surveyLocations.get(position).getContactNo()
                                + "\n"
                                +"Email ID :   "
                                + surveyLocations.get(position).getEmailId()
                                + "\n"
                               ;
                    //    commonAlert(title,StrMsg,position);
                    }

                } }
        });


        if (checkInternet(getContext())) {

            if(type.equalsIgnoreCase("Tenant")){
                layout_main.removeView(linear1);
                layout_main.removeView(linear2);
                layout_main.addView(linear2,0);
                layout_main.addView(linear1,1);
                linear1.setVisibility(View.GONE);
                linear3.setVisibility(View.VISIBLE);
                tv_lbl_contract_no.setText(Html.fromHtml("Contract No" + AppUtils.mandatory));
                AppUtils.showProgressDialog2(mActivity,"Loading..",true);
                new CustomerSurveyRepository(mActivity,this).getSurveyContract(mStrEmpId,"");
            }

            else{
                AppUtils.showProgressDialog2(mActivity,"Loading..",true);
                new CustomerSurveyRepository(mActivity,this).getSurveyEmployeeList(mStrEmpId);
            }

        }

        final Handler handler = new Handler();
        Timer timer = new Timer(false);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(surveyTransaction.getSurveyReference()==null){
                            // showDialog();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 800);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_logout);
        item.setVisible(false);
        menu.findItem(R.id.action_home).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onClickSearch(final View view) {

        if(view==tie_location_name){
           if(surveyTransaction.getContractNo().isEmpty()){
               showErrorToast(getActivity(),"Contract should not be empty.");
           }
           else{
               fetchLocation(surveyTransaction.getContractNo());
           }
        }
        else{

            if(surveyTransaction.getBuildingCode()==null  ){
                showErrorToast(getActivity(),"Location should not be empty.");
            }
            else if(surveyTransaction.getBuildingCode().isEmpty()) {
                showErrorToast(getActivity(),"Location should not be empty.");
            }
            else{
               new CustomerSurveyRepository(mActivity,this).getSurveyFloorFlat(surveyTransaction.getContractNo(),surveyTransaction.getBuildingCode());
            }
        }
    }

    public void loadFragment(final Fragment fragment, final String tag) {

        surveyTransaction.setSurveyType(surveyType);
        Bundle mdata = new Bundle();

        ArrayList<SurveyLocation> all_loc = new ArrayList<>(surveyLocations.size());
        all_loc.addAll(surveyLocations);

        mdata.putSerializable(AppUtils.ARGS_SURVEYLOCATIONS,all_loc);

        mdata.putSerializable(AppUtils.ARGS_SURVEYTRANS,surveyTransaction);
        mdata.putBoolean(AppUtils.ARGS_SUGESSTIONFLAG,suggestionFlag);
        mdata.putString(AppUtils.ARGS_SURVEYMODE,surveyTransaction.getSurveyType());
        fragment.setArguments(mdata);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

    }

    public void onReceiveSurveyCustomer(List<SurveyCustomer> customers, int mode){
        AppUtils.hideProgressDialog();
        surveyCustomers=  customers;
    }

    public void onReceiveFailureSurveyCustomer(String strErr, int mode){
        AppUtils.hideProgressDialog();
        showErrorToast(mActivity,strErr);

    }

    public  void onReceiveSurveyContract(List<SurveyContract> contracts, int mode){

        surveyContracts=contracts;
        if(type.equalsIgnoreCase("Customer")){
            showSurveyContractDialog();
        }
        else{
            AppUtils.hideProgressDialog();
        }

    }

    public void onReceiveFailureSurveyContract(String strErr, int mode){
        AppUtils.hideProgressDialog();
        showErrorToast(mActivity,strErr);

    }

    public void onReceiveSurveyRefernce(List<SurveyMaster> refernces, int mode){
        surveyReferences=refernces;
        showSurveyReferenceDialog();
    }

    public void onReceiveFailureSurveyRefernce(String strErr, int mode){
        AppUtils.hideProgressDialog();
        showErrorToast(mActivity,strErr);
    }

    public void onReceiveSurveyQuestionnaire(List<ServeyQuestionnaire> questions, int mode){
        AppUtils.hideProgressDialog();
    }

    public void onReceiveFailureSurveyQuestionnaire(String strErr, int mode){
        AppUtils.hideProgressDialog();
        showErrorToast(mActivity,strErr);
    }

    public void onReceiveSurveyLocation(List<SurveyLocation> questions, int mode){
        surveyLocations=questions;
        showSurveyLocationDialog();
    }


    public void onReceiveSurveyFloorFlat(List<SurveyFloorFlat> floors, int mode){
        surveyFloorFlats=floors;
        showSurveyFloorDialog();
    }

    public void onReceiveFailureSurveyFloorFlat(String strErr, int mode){
        AppUtils.hideProgressDialog();
        showErrorToast(mActivity,strErr);
    }

    public  void onReceiveFailureSurveyLocation(String strErr, int mode){
        showErrorToast(mActivity,strErr);
    }

    public  void showSurveyCustomerDialog(){

        if(surveyCustomers!=null && surveyCustomers.size()>0){
            try {
                final ArrayList strArrayCustName = new ArrayList();
                for (SurveyCustomer entity : surveyCustomers) {
                    strArrayCustName.add(entity.getCustomerName());
                }
                strArrayCustName.add(space);
                FilterableListDialog.create(
                        mActivity,
                        ("Select the Customer"),
                        strArrayCustName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {

                                if(!text.equals(space)){
                                    customerName=text;
                                    tv_select_customer.setText(text);
                                    tv_select_customer.setTypeface(font.getHelveticaBold());
                                    customerCode=surveyCustomers.get(strArrayCustName.indexOf(text)).getCustomerCode();
                                    surveyTransaction.setCustomerName(text);
                                    surveyTransaction.setCustomerCode(customerCode);
                                    if(type.equalsIgnoreCase("Customer")){
                                        clearTextOnCustomerChange();
                                    } }

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

        closeKeyboard(getContext());
    }

    public  void showSurveyContractDialog(){

        if(surveyContracts!=null && surveyContracts.size()>0){
            try {
                final ArrayList strArrayCustName = new ArrayList();
                for (SurveyContract entity : surveyContracts) {
                    strArrayCustName.add(entity.getContractNo()+" - "+entity.getContractName());
                }
                strArrayCustName.add(space);
                FilterableListDialog.create(
                        mActivity,
                        ("Select the Contract"),
                        strArrayCustName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {

                                if(!text.equals(space)){
                                    contract=text;
                                    contractNo=text;
                                    tv_select_contract.setText(text);
                                    tv_select_contract.setTypeface(font.getHelveticaBold());
                                    surveyTransaction.setContractNo(surveyContracts.get(strArrayCustName.indexOf(text)).getContractNo());

                                    if(type.equalsIgnoreCase("Tenant")){

                                        customerName=surveyContracts.get(strArrayCustName.indexOf(text)).getCustomerName();
                                        tv_select_customer.setText(surveyContracts.get(strArrayCustName.indexOf(text)).getCustomerName());
                                        tv_select_customer.setTypeface(font.getHelveticaBold());
                                        customerCode=surveyContracts.get(strArrayCustName.indexOf(text)).getCustomerCode();
                                        surveyTransaction.setCustomerName(surveyContracts.get(strArrayCustName.indexOf(text)).getCustomerName());
                                        surveyTransaction.setCustomerCode(surveyContracts.get(strArrayCustName.indexOf(text)).getCustomerCode());

                                        tv_select_reference.setText("Select the Survey ref");
                                        reference="";
                                        surveyReference="";

                                    }
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

        closeKeyboard(getContext());
    }

    public  void showSurveyLocationDialog(){

        if(surveyLocations!=null && surveyLocations.size()>0){
            try {
                final ArrayList strArrayCustName = new ArrayList();
                for (SurveyLocation entity : surveyLocations) {
                    strArrayCustName.add(entity.getBuildingName());
                }
                strArrayCustName.add(space);
                FilterableListDialog.create(
                        mActivity,
                        ("Select the Building"),
                        strArrayCustName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {

                                if(!text.equals(space)) {
                                    tie_location_name.setText(text);
                                    surveyTransaction.setBuildingCode(surveyLocations.get(strArrayCustName.indexOf(text)).getBuildingCode());


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

        closeKeyboard(getContext());
    }


    public  void showSurveyFloorDialog(){

        if(surveyFloorFlats!=null && surveyFloorFlats.size()>0){
            try {
                final ArrayList strArrayCustName = new ArrayList();
                for (SurveyFloorFlat entity : surveyFloorFlats) {
                    strArrayCustName.add(entity.getFloorFlat());
                }
                strArrayCustName.add(space);
                FilterableListDialog.create(
                        mActivity,
                        ("Select the Floor/Flat"),
                        strArrayCustName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {

                                if(!text.equals(space)) {
                                    tie_floor_flat.setText(text);
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

        closeKeyboard(getContext());
    }



    public void fetchLocation(String contractNo){
        new CustomerSurveyRepository(mActivity,this).getSurveyLocation(contractNo);
    }

    public  void showSurveyReferenceDialog(){

        if(surveyReferences!=null && surveyReferences.size()>0){
            try {
                final ArrayList strArrayCustName = new ArrayList();
                for (SurveyMaster entity : surveyReferences) {
                    strArrayCustName.add(entity.getSurveyName());
                }
                strArrayCustName.add(space);

                FilterableListDialog.create(
                        mActivity,
                        ("Select the Reference"),
                        strArrayCustName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {

                                if(!text.equals(space)){
                                    reference=text;
                                    surveyReference=text;
                                    tv_select_reference.setText(text);
                                    tv_select_reference.setTypeface(font.getHelveticaBold());
                                    surveyTransaction.setSurveyReference(surveyReferences.get(strArrayCustName.indexOf(text)).getSurveyReference());
                                    surveyTransaction.setOpco(surveyReferences.get(strArrayCustName.indexOf(text)).getOpco());
                                    surveyTransaction.setScore(getSurveyScore(surveyReferences.get(strArrayCustName.indexOf(text))));
                                    surveyType=getSurveyType(surveyReferences.get(strArrayCustName.indexOf(text)));
                                    suggestionFlag=surveyReferences.get(strArrayCustName.indexOf(text)).getSuggestionFlag();
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

        closeKeyboard(getContext());
    }

    public String getSurveyType(SurveyMaster master){
        if(type.equalsIgnoreCase("Tenant")){
            return master.getTenantSurveyFlag();
        }
        else{
            return master.getCustomerSurveyFlag();
        }
    }

    public int getSurveyScore(SurveyMaster master){
        if(type.equalsIgnoreCase("Tenant")){
            return Integer.parseInt(master.getTenantScoreLimit());
        }
        else{
            return Integer.parseInt(master.getCustomerScoreLimit());
        }
    }


    public void proceed_questionnaire(){

        if(checkForEmpty()){
        }
        else{
            loadFragment(new SurveyHeaderSecondary(), Utils.TAG_FRAGMENT_HEADER_SECONDARY);
        }
    }

    public boolean checkForEmpty(){

        if(customerCode.equalsIgnoreCase("")){
            showErrorToast(mActivity,"Customer should not be empty");
            return true;
        }

        if(type.equalsIgnoreCase("Tenant")){

            if(contract.equalsIgnoreCase("")){
                showErrorToast(mActivity,"Contract No should not be empty");
                return true;
            }
        }

        if(reference.equalsIgnoreCase("")){
            showErrorToast(mActivity,"Survey Reference should not be empty");
            return true;
        }

        // If survey type is tenant then the condition will be checked


        if(AppUtils.checkEmptyEdt(tie_location_name)){
            showErrorToast(mActivity,"Location should not be empty");
            return true;
        }
        else{
            surveyTransaction.setLocation(tie_location_name.getText().toString());
        }

        // getting floor flat details

        if(type.equalsIgnoreCase("Tenant") ){

            surveyTransaction.setFloorFlat(tie_floor_flat.getText().toString());

        }

        return false;
    }


    public void setCacheVals(){
        if(!customerName.isEmpty()){
            tv_select_customer.setText(customerName);
            tv_select_customer.setTypeface(font.getHelveticaBold());
        }

        if(!contractNo.isEmpty()){
            tv_select_contract.setText(contractNo);
            tv_select_contract.setTypeface(font.getHelveticaBold());
        }

        if(!surveyReference.isEmpty()){
            tv_select_reference.setText(surveyReference);
            tv_select_reference.setTypeface(font.getHelveticaBold());
        }
    }

    public void  clearTextOnCustomerChange() {

        tv_select_contract.setText("Select the Contract No");
        tv_select_contract.setTypeface(font.getHelveticaBold());
        tv_select_reference.setText("Select the Survey ref");
        reference="";
        surveyReference="";
        contract="";
        contractNo="";
        tie_location_name.setText("");

    }

   public void onReceiveSurveyEmployeeList(List<SurveyEmployeeList> list, int mode){

       if(type.equalsIgnoreCase("Customer")) {
         List<SurveyCustomer> customers=new ArrayList<>();
         for(SurveyEmployeeList s : list){
             SurveyCustomer sc=new SurveyCustomer();
             sc.setCustomerCode(s.getCustomerCode());
             sc.setCustomerName(s.getCustomerName());
             customers.add(sc);
         }
         AppUtils.hideProgressDialog();
         surveyCustomers=  customers;
     }

     else{
           List<SurveyContract> contracts=new ArrayList<>();
           for(SurveyEmployeeList s : list){
               SurveyContract sc=new SurveyContract();
               sc.setContractNo(s.getContractNo());
               sc.setContractName(s.getContractName());
               sc.setCustomerCode(s.getCustomerCode());
               sc.setCustomerName(s.getCustomerName());
               contracts.add(sc);
           }
           surveyContracts=contracts;AppUtils.hideProgressDialog();
     }

   }

    public void onReceiveSurveyReviewer(List<SurveyLocation> questions, int mode){
        surveyReviwer=questions;
    }

    public  void onReceiveFailureSurveyReviewer(String strErr, int mode){
        showErrorToast(mActivity,strErr);
    }

    public void onReceiveFailureSurveyEmployeeList(String strErr, int mode) {
        AppUtils.hideProgressDialog();
        showErrorToast(mActivity,strErr);
    }

}
