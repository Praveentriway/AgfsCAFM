package com.daemon.emco_android.ui.fragments.customerfeedback;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.CustomerSurveyService;
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

import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYTYPE;


public class CustomerFeedBackHeader extends Fragment implements CustomerSurveyService.Listener, View.OnClickListener {

    private static final String TAG = CustomerFeedBackHeader.class.getSimpleName();
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
    CardView linear1,linear2;
    SurveyTransaction surveyTransaction;
    private SharedPreferences mPreferences;
    private String mStrEmpId = null;
    private String mLoginData = null;

    ArrayList<String> locationArr;

    AppCompatTextView tv_lbl_custcode, tv_lbl_contract_no, tv_lbl_reference, tv_lbl_location, tv_lbl_client_name, tv_lbl_designation, tv_lbl_email, tv_lbl_contact;
    String type;

    AppCompatEditText tie_client_name, tie_designation, tie_contact_no, tie_email;

    AutoCompleteTextView tie_location_name;

    public CustomerFeedBackHeader() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_select_reference:{

                if(customerCode.equalsIgnoreCase("")){
                    Toast.makeText(mActivity, "Customer should not be empty.", Toast.LENGTH_SHORT).show();
                }
                else{
                new CustomerSurveyService(mActivity,this).getSurveyRefernce(customerCode);
                }
            }
            break;

            case R.id.tv_select_contract: {

                if(type.equalsIgnoreCase("Tenant")){
                    showSurveyContractDialog();
                }
                else{
                    if(customerCode.equalsIgnoreCase("")){
                        Toast.makeText(mActivity, "Customer should not be empty.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        new CustomerSurveyService(mActivity,this).getSurveyContract(customerCode);
                    }
                }
             }
                break;

            case R.id.tv_select_customer : showSurveyCustomerDialog();
                break;
        } }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_customer_feed_back_header, container, false);
        setupActionBar();
        setCacheVals();

        fab_next=(FloatingActionButton) view.findViewById(R.id.fab_next);
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed_questionnaire();
            }
        });

        return view;
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbar.setTitle("my title");
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);

        tv_toolbar_title.setText(type+" Survey");
        LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);
    //  mToolbar.setTitle(getResources().getString(R.string.lbl_ppm_details));
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

        layout_main=(LinearLayout) view.findViewById(R.id.layout_main);
        linear1=(CardView) layout_main.findViewById(R.id.linear1);
        linear2=(CardView) layout_main.findViewById(R.id.linear2);
        tv_select_contract=(TextView) view.findViewById(R.id.tv_select_contract);
        tv_select_customer=(TextView) view.findViewById(R.id.tv_select_customer);
        tv_select_reference=(TextView) view.findViewById(R.id.tv_select_reference);
        tv_lbl_custcode=(AppCompatTextView) view.findViewById(R.id.tv_lbl_custcode);
        tv_lbl_contract_no=(AppCompatTextView) view.findViewById(R.id.tv_lbl_contract_no);
        tv_lbl_reference=(AppCompatTextView) view.findViewById(R.id.tv_lbl_reference);
        tv_lbl_location=(AppCompatTextView) view.findViewById(R.id.tv_lbl_location);
        tv_lbl_client_name=(AppCompatTextView) view.findViewById(R.id.tv_lbl_client_name);
        tv_lbl_designation=(AppCompatTextView) view.findViewById(R.id.tv_lbl_designation);
        tv_lbl_email=(AppCompatTextView) view.findViewById(R.id.tv_lbl_email);
        tv_lbl_contact=(AppCompatTextView) view.findViewById(R.id.tv_lbl_contact);

        tv_lbl_custcode.setText(Html.fromHtml("Customer" + AppUtils.mandatory));
       // tv_lbl_contract_no.setText(Html.fromHtml("Contract No" + AppUtils.mandatory));
        tv_lbl_reference.setText(Html.fromHtml("Survey Reference" + AppUtils.mandatory));
        tv_lbl_location.setText(Html.fromHtml("Location" + AppUtils.mandatory));
        tv_lbl_client_name.setText(Html.fromHtml("Reviewer Name" + AppUtils.mandatory));
        tv_lbl_designation.setText(Html.fromHtml("Designation" + AppUtils.mandatory));
        tv_lbl_email.setText(Html.fromHtml("Email Id" + AppUtils.mandatory));
        tv_lbl_contact.setText(Html.fromHtml("Contact No" + AppUtils.mandatory));

        tie_location_name=(AutoCompleteTextView) view.findViewById(R.id.tie_location_name);
        tie_client_name=(AppCompatEditText) view.findViewById(R.id.tie_client_name);
        tie_designation=(AppCompatEditText) view.findViewById(R.id.tie_designation);
        tie_contact_no=(AppCompatEditText) view.findViewById(R.id.tie_contact_no);
        tie_email=(AppCompatEditText) view.findViewById(R.id.tie_email);

        tv_select_contract.setOnClickListener(this);
        tv_select_customer.setOnClickListener(this);
        tv_select_reference.setOnClickListener(this);


        if(type.equalsIgnoreCase("Tenant")){
            layout_main.removeView(linear1);
            layout_main.removeView(linear2);
            layout_main.addView(linear2,0);
            layout_main.addView(linear1,1);
            tv_lbl_contract_no.setText(Html.fromHtml("Contract No" + AppUtils.mandatory));
            AppUtils.showProgressDialog2(mActivity,"Loading..",true);
            new CustomerSurveyService(mActivity,this).getSurveyContract("");
        }
        else{
            AppUtils.showProgressDialog2(mActivity,"Loading..",true);
            new CustomerSurveyService(getContext(),this).getSurveyCustomer();
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(TAG, "onPrepareOptionsMenu ");
        MenuItem item = menu.findItem(R.id.action_logout);
        item.setVisible(false);
    }

    public void loadFragment(final Fragment fragment, final String tag,List<ServeyQuestionnaire> questions) {
        Log.d(TAG, "loadFragment");
        // update the main content by replacing fragments

        ArrayList<ServeyQuestionnaire> al_ques = new ArrayList<>(questions.size());
        al_ques.addAll(questions);

        Bundle mdata = new Bundle();
        mdata.putSerializable(AppUtils.ARGS_SURVEYQUES,al_ques);
        mdata.putSerializable(AppUtils.ARGS_SURVEYTRANS,surveyTransaction);
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
        Toast.makeText(mActivity,strErr,Toast.LENGTH_SHORT).show();
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
        Toast.makeText(mActivity,strErr,Toast.LENGTH_SHORT).show();
    }

    public void onReceiveSurveyRefernce(List<SurveyMaster> refernces, int mode){
        surveyReferences=refernces;
        showSurveyReferenceDialog();
    }

    public void onReceiveFailureSurveyRefernce(String strErr, int mode){
        Toast.makeText(mActivity,strErr,Toast.LENGTH_SHORT).show();
    }

    public void onReceiveSurveyQuestionnaire(List<ServeyQuestionnaire> questions, int mode){
        AppUtils.hideProgressDialog();
        loadFragment(new CustomerFeedbackQuestionnaire(), Utils.TAG_FRAGMENT_CUST_FEEDBACK_QUES,questions);
    }

    public void onReceiveFailureSurveyQuestionnaire(String strErr, int mode){
        Toast.makeText(mActivity,strErr,Toast.LENGTH_SHORT).show();
    }

    public void onReceiveSurveyLocation(List<SurveyLocation> questions, int mode){

        locationArr=new ArrayList<>();

        for(SurveyLocation s:questions){
            locationArr.add(s.getBuildingName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (mActivity,android.R.layout.select_dialog_item, locationArr);

        tie_location_name.setThreshold(2);
        tie_location_name.setAdapter(adapter);

    }

    public  void onReceiveFailureSurveyLocation(String strErr, int mode){

    }


    public  void showSurveyCustomerDialog(){

        if(surveyCustomers!=null && surveyCustomers.size()>0){

            try {

                final ArrayList strArrayCustName = new ArrayList();
                for (SurveyCustomer entity : surveyCustomers) {
                  //  strArrayCustName.add(entity.getCustomerName()+ " - "+entity.getCustomerName());
                    strArrayCustName.add(entity.getCustomerName());
                }

                strArrayCustName.add("\n\n");
                FilterableListDialog.create(
                        mActivity,
                        ("Select the Customer"),
                        strArrayCustName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {

                                if(!text.equals("\n\n")){
                                     customerName=text;
                                      tv_select_customer.setText(text);
                                      tv_select_customer.setTypeface(font.getHelveticaBold());
                                      customerCode=surveyCustomers.get(strArrayCustName.indexOf(text)).getCustomerCode();
                                      surveyTransaction.setCustomerName(text);
                                      surveyTransaction.setCustomerCode(customerCode);

                                    if(type.equalsIgnoreCase("Customer")){
                                        clearTextOnCustomerChange();
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
    }

    public  void showSurveyContractDialog(){

        if(surveyContracts!=null && surveyContracts.size()>0){

            try {

                final ArrayList strArrayCustName = new ArrayList();
                for (SurveyContract entity : surveyContracts) {
                    //  strArrayCustName.add(entity.getCustomerName()+ " - "+entity.getCustomerName());
                    strArrayCustName.add(entity.getContractNo()+" - "+entity.getContractName());
                }


                strArrayCustName.add("\n\n\n");
                FilterableListDialog.create(
                        mActivity,
                        ("Select the Contract"),
                        strArrayCustName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {
                                if(!text.equals("\n\n\n")){
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

                              fetchLocation(surveyContracts.get(strArrayCustName.indexOf(text)).getContractNo());

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

    public void fetchLocation(String contractNo){
        new CustomerSurveyService(mActivity,this).getSurveyLocation(contractNo);
    }

    public  void showSurveyReferenceDialog(){

        if(surveyReferences!=null && surveyReferences.size()>0){

            try {

                final ArrayList strArrayCustName = new ArrayList();
                for (SurveyMaster entity : surveyReferences) {
                    //  strArrayCustName.add(entity.getCustomerName()+ " - "+entity.getCustomerName());

                    strArrayCustName.add(entity.getSurveyName());

                }

                strArrayCustName.add("\n\n");

                FilterableListDialog.create(
                        mActivity,
                        ("Select the Reference"),
                        strArrayCustName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {

                                if(!text.equals("\n\n")){
                                      reference=text;
                                      surveyReference=text;
                                      tv_select_reference.setText(text);
                                      tv_select_reference.setTypeface(font.getHelveticaBold());
                                      surveyTransaction.setSurveyReference(surveyReferences.get(strArrayCustName.indexOf(text)).getSurveyReference());
                                      surveyTransaction.setOpco(surveyReferences.get(strArrayCustName.indexOf(text)).getOpco());
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

    public  void loadQuestions(){
        new CustomerSurveyService(mActivity,this).getSurveyQuestionnaire(surveyTransaction.getOpco(),customerCode,surveyTransaction.getSurveyReference());
    }

    public void proceed_questionnaire(){

        new CustomerSurveyService(mActivity,this).getSurveyQuestionnaire("999","100046","CS000001");

//        if(checkForEmpty()){
//
//        }
//        else{
//          //  surveyTransaction.setContractNo(contract);
//            AppUtils.showProgressDialog2(mActivity,"Loading...",true);
//            loadQuestions();
//        }
    }

    public boolean checkForEmpty(){

        if(customerCode.equalsIgnoreCase("")){
            Toast.makeText(mActivity,"Customer should not be empty",Toast.LENGTH_SHORT).show();
            return true;
        }

        if(type.equalsIgnoreCase("Tenant")){

            if(contract.equalsIgnoreCase("")){
                Toast.makeText(mActivity,"Contract No should not be empty",Toast.LENGTH_SHORT).show();
                return true;
            }

        }

        if(reference.equalsIgnoreCase("")){
            Toast.makeText(mActivity,"Survey Reference should not be empty",Toast.LENGTH_SHORT).show();
            return true;
        }

        if(AppUtils.checkEmptyEdt(tie_location_name)){
            Toast.makeText(mActivity,"Location should not be empty",Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            surveyTransaction.setLocation(tie_location_name.getText().toString());
        }

        if(AppUtils.checkEmptyEdt(tie_client_name)){
            Toast.makeText(mActivity,"Reviewer name should not be empty",Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            surveyTransaction.setClientName(tie_client_name.getText().toString());
        }
        if(AppUtils.checkEmptyEdt(tie_designation)){
            Toast.makeText(mActivity,"Designation should not be empty",Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            surveyTransaction.setDesignation(tie_designation.getText().toString());
        }

        if(AppUtils.checkEmptyEdt(tie_contact_no) ){
            Toast.makeText(mActivity,"Contact should not be empty",Toast.LENGTH_SHORT).show();
            return true;
        }

        else if(tie_contact_no.getText().toString().length()<5){
            Toast.makeText(mActivity,"Please enter a valid Contact No.",Toast.LENGTH_SHORT).show();
            return true;
        }

        else{
            surveyTransaction.setContactNo(tie_contact_no.getText().toString());
        }

        if(AppUtils.checkEmptyEdt(tie_email)){
            Toast.makeText(mActivity,"Email id should not be empty",Toast.LENGTH_SHORT).show();
            return true;
        }

        else if(!AppUtils.validateEmail(tie_email.getText().toString())){
            Toast.makeText(mActivity,"Please enter a valid E-mail address.",Toast.LENGTH_SHORT).show();
            return true;
        }

        else{
            surveyTransaction.setEmail(tie_email.getText().toString());
        }

        return false;
    }


    public void setCacheVals(){

        if(!customerName.equalsIgnoreCase("")){
            tv_select_customer.setText(customerName);
        }

        if(!contractNo.equalsIgnoreCase("")){
            tv_select_contract.setText(contractNo);
        }

        if(!surveyReference.equalsIgnoreCase("")){
            tv_select_reference.setText(surveyReference);
        }
    }


    public void  clearTextOnCustomerChange(){

        tv_select_contract.setText("Select the Contract No");
        tv_select_reference.setText("Select the Survey ref");
        reference="";
        surveyReference="";

        contract="";
        contractNo="";


    }





}
