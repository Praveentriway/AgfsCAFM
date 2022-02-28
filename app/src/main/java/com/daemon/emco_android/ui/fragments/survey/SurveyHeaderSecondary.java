package com.daemon.emco_android.ui.fragments.survey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.response.SurveyFloorFlat;
import com.daemon.emco_android.repository.db.entity.ServeyQuestionnaire;
import com.daemon.emco_android.repository.db.entity.SurveyContract;
import com.daemon.emco_android.repository.db.entity.SurveyCustomer;
import com.daemon.emco_android.repository.db.entity.SurveyEmployeeList;
import com.daemon.emco_android.repository.db.entity.SurveyLocation;
import com.daemon.emco_android.repository.db.entity.SurveyMaster;
import com.daemon.emco_android.repository.db.entity.SurveyTransaction;
import com.daemon.emco_android.repository.remote.CustomerSurveyRepository;
import com.daemon.emco_android.ui.components.FilterableListDialog;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.RightDrawableOnTouchListener;
import com.daemon.emco_android.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.utils.AppUtils.ARGS_SUGESSTIONFLAG;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYLOCATIONS;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYMODE;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYQUES;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYTRANS;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYTYPE;
import static com.daemon.emco_android.utils.AppUtils.closeKeyboard;
import static com.daemon.emco_android.utils.AppUtils.showErrorToast;
import static com.daemon.emco_android.utils.StringUtil.space;


public class SurveyHeaderSecondary extends Fragment implements CustomerSurveyRepository.Listener {

    private AppCompatActivity mActivity;
    private SharedPreferences mPreferences;
    private String mStrEmpId = null;
    SurveyTransaction surveyTransaction;
    private String mLoginData = null;
    private Bundle mArgs;
    private View view;
    Toolbar mToolbar;
    TextView tv_toolbar_title;
    FloatingActionButton fab_next;
    public String ALL_BUILDINGS="ALL-BUILDINGS";
    CardView layout_designation;
    AppCompatTextView tv_lbl_tenant_name, tv_lbl_client_name, tv_lbl_designation, tv_lbl_email, tv_lbl_contact;
    String type,surveyType;
    public static String TENANT="Tenant";
    boolean suggestionFlag;
    AppCompatEditText tie_contact_no, tie_email,tie_designation;
    List<SurveyLocation> surveyReviwer;
    AutoCompleteTextView tie_client_name;
    private ArrayList<SurveyLocation> locations=new ArrayList<>();

    public SurveyHeaderSecondary() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true);

        mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
        mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);

        if (mLoginData != null) {
            Gson gson = new Gson();
            Login login = gson.fromJson(mLoginData, Login.class);
            mStrEmpId = login.getEmployeeId();
        }
        mArgs = getArguments();
        if(mArgs!=null) {

            //temp is added to hold the state of survey type
            surveyTransaction = (SurveyTransaction) mArgs.getSerializable(ARGS_SURVEYTRANS);

            suggestionFlag=mArgs.getBoolean(ARGS_SUGESSTIONFLAG);
            surveyType=mArgs.getString(ARGS_SURVEYMODE);

            type=surveyTransaction.getSurveyFrom();
            locations = (ArrayList<SurveyLocation>) mArgs.getSerializable(ARGS_SURVEYLOCATIONS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_survey_header_secondary, container, false);
        setupActionBar();
        setProperty();

        return view;
    }

    private void setProperty(){

        layout_designation=(CardView) view.findViewById(R.id.layout_designation);
        tv_lbl_client_name=(AppCompatTextView) view.findViewById(R.id.tv_lbl_client_name);
        tv_lbl_designation=(AppCompatTextView) view.findViewById(R.id.tv_lbl_designation);
        tv_lbl_tenant_name=(AppCompatTextView) view.findViewById(R.id.tv_lbl_designation);
        tv_lbl_email=(AppCompatTextView) view.findViewById(R.id.tv_lbl_email);
        tv_lbl_contact=(AppCompatTextView) view.findViewById(R.id.tv_lbl_contact);
        tie_client_name=(AutoCompleteTextView) view.findViewById(R.id.tie_client_name);
        tie_contact_no=(AppCompatEditText) view.findViewById(R.id.tie_contact_no);
        tie_email=(AppCompatEditText) view.findViewById(R.id.tie_email);
        tie_designation=(AppCompatEditText) view.findViewById(R.id.tie_designation);
        fab_next=(FloatingActionButton) view.findViewById(R.id.fab_next);

        if(type.equalsIgnoreCase(TENANT)){
            tv_lbl_client_name.setText(Html.fromHtml("Tenant Name" + AppUtils.mandatory));
        }
        else{
            tv_lbl_client_name.setText(Html.fromHtml("Reviewer Name" + AppUtils.mandatory));
        }

        tv_lbl_tenant_name.setText(Html.fromHtml("Tenant Name" + AppUtils.mandatory));
        tv_lbl_designation.setText(Html.fromHtml("Designation"));
        tv_lbl_email.setText(Html.fromHtml("Email Id"));
        tv_lbl_contact.setText(Html.fromHtml("Contact No"));
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed_questionnaire();
            }
        });

        ImageView right_btn_client_name=(ImageView) view.findViewById(R.id.right_btn_client_name) ;
        right_btn_client_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSearch(tie_client_name);
            }
        });



        if((type.equalsIgnoreCase(TENANT))) {
            right_btn_client_name.setVisibility(View.GONE);
            layout_designation.setVisibility(View.GONE);
        }

        final ArrayList strArrayCustName = new ArrayList();
        for (SurveyLocation entity : locations) {
            strArrayCustName.add(entity.getBuildingName());
        }

        if(!(type.equalsIgnoreCase(TENANT))){

            if(surveyTransaction.getLocation().equalsIgnoreCase(ALL_BUILDINGS)){
                right_btn_client_name.setVisibility(View.VISIBLE);
                tie_client_name.setText("");
                tie_designation.setText("");
                tie_contact_no.setText("");
                tie_email.setText("");

            }
            else{
                right_btn_client_name.setVisibility(View.GONE);

                if(strArrayCustName.size()>0){
                    tie_client_name.setText(locations.get(strArrayCustName.indexOf(surveyTransaction.getLocation())).getReviewerName());
                    tie_designation.setText(locations.get(strArrayCustName.indexOf(surveyTransaction.getLocation())).getDesignation());
                    tie_contact_no.setText(locations.get(strArrayCustName.indexOf(surveyTransaction.getLocation())).getContactNo());
                    tie_email.setText(locations.get(strArrayCustName.indexOf(surveyTransaction.getLocation())).getEmailId());
                }
            }
        }
    }


    public void proceed_questionnaire(){

        if(checkForEmpty()){
        }
        else{
            AppUtils.showProgressDialog2(mActivity,"Loading...",true);
            loadQuestions();
        }
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
                        if (checkForEmptyData()) {
                            mActivity.onBackPressed();
                        }
                        else{
                            showBackPressAlert();
                        }
                    }
                });

    }

    public void showBackPressAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Are you sure your data will be cleared on navigating previous screen?")
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


    public boolean checkForEmpty(){

        if(type.equalsIgnoreCase(TENANT)){
            if(AppUtils.checkEmptyEdt(tie_client_name)){

                showErrorToast(mActivity,"Tenant name should not be empty");
                return true;
            }
            else{
                surveyTransaction.setTenantName(tie_client_name.getText().toString());
            }
        }
        else{

            if(AppUtils.checkEmptyEdt(tie_client_name)){

                showErrorToast(mActivity,"Reviewer name should not be empty");
                return true;
            }
            else{
                surveyTransaction.setClientName(tie_client_name.getText().toString());
            }

        }

        surveyTransaction.setDesignation(tie_designation.getText().toString());

        if(!AppUtils.checkEmptyEdt(tie_contact_no) ){
            if( tie_contact_no.getText().toString().length()<5){
                showErrorToast(mActivity,"Please enter a valid Contact No.");
                return true;
            }
            else{
                surveyTransaction.setContactNo(tie_contact_no.getText().toString());
            }
        }


        if(!AppUtils.checkEmptyEdt(tie_email)){

            if(!AppUtils.validateEmail(tie_email.getText().toString())){
                showErrorToast(mActivity,"Please enter a valid E-mail address.");
                return true;
            }
            else{
                surveyTransaction.setEmail(tie_email.getText().toString());
            }
        }

        return false;
    }

    public  void loadQuestions() {
        new CustomerSurveyRepository(mActivity,this).getSurveyQuestionnaire(surveyTransaction.getOpco(),surveyTransaction.getCustomerCode(),surveyTransaction.getSurveyReference(),surveyTransaction.getSurveyType());
    }

    public void onReceiveSurveyQuestionnaire(List<ServeyQuestionnaire> questions, int mode){
        AppUtils.hideProgressDialog();
        loadFragment(new SurveyQuestionnaire(), Utils.TAG_FRAGMENT_CUST_FEEDBACK_QUES,questions);
    }

    public void onReceiveFailureSurveyQuestionnaire(String strErr, int mode){
        AppUtils.hideProgressDialog();
        showErrorToast(mActivity,strErr);
    }

    private void loadFragment(final Fragment fragment, final String tag,List<ServeyQuestionnaire> questions) {
        ArrayList<ServeyQuestionnaire> all_ques = new ArrayList<>(questions.size());
        all_ques.addAll(questions);
        Bundle mdata = new Bundle();
        mdata.putSerializable(AppUtils.ARGS_SURVEYQUES,all_ques);
        surveyTransaction.setSurveyType(surveyType);
        mdata.putSerializable(AppUtils.ARGS_SURVEYTRANS,surveyTransaction);
        mdata.putBoolean(AppUtils.ARGS_SUGESSTIONFLAG,suggestionFlag);
        fragment.setArguments(mdata);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public  void onReceiveSurveyCustomer(List<SurveyCustomer> customers, int mode){}

    public void onReceiveFailureSurveyCustomer(String strErr, int mode){}

    public void onReceiveSurveyContract(List<SurveyContract> contracts, int mode){}

    public void onReceiveFailureSurveyContract(String strErr, int mode){}

    public  void onReceiveSurveyRefernce(List<SurveyMaster> refernces, int mode){}

    public void onReceiveFailureSurveyRefernce(String strErr, int mode){}

    public  void onReceiveSurveyLocation(List<SurveyLocation> questions, int mode){}

    public   void onReceiveFailureSurveyLocation(String strErr, int mode){}

    public  void onReceiveSurveyEmployeeList(List<SurveyEmployeeList> questions, int mode){}

    public  void onReceiveFailureSurveyEmployeeList(String strErr, int mode){}

    public  void onReceiveSurveyReviewer(List<SurveyLocation> questions, int mode){

        surveyReviwer=questions;
        showSurveyReviewerDialog();

    }

    public void onReceiveFailureSurveyReviewer(String strErr, int mode){}


    private void onClickSearch(final View view) {

        if(view==tie_client_name){
            new CustomerSurveyRepository(mActivity,this).getSurveyReviewer(surveyTransaction.getContractNo());
        }

    }


    public  void showSurveyReviewerDialog(){

        closeKeyboard(getContext());

        String msg="";
        if(type.equalsIgnoreCase(TENANT)){
            msg="Select the Reviewer/Tenant name";
        }
        else{
            msg="Select the Reviewer name";
        }

        if(surveyReviwer!=null && surveyReviwer.size()>0){
            try {
                final ArrayList strArrayCustName = new ArrayList();
                for (SurveyLocation entity : surveyReviwer) {
                    strArrayCustName.add(entity.getReviewerName());
                }
                strArrayCustName.add(space);
                FilterableListDialog.create(
                        mActivity,
                        (msg),
                        strArrayCustName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {
                                if(!text.equals(space)){

                                    tie_client_name.setText(surveyReviwer.get(strArrayCustName.indexOf(text)).getReviewerName());
                                    tie_designation.setText(surveyReviwer.get(strArrayCustName.indexOf(text)).getDesignation());
                                    tie_contact_no.setText(surveyReviwer.get(strArrayCustName.indexOf(text)).getContactNo());
                                    tie_email.setText(surveyReviwer.get(strArrayCustName.indexOf(text)).getEmailId());

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

    private boolean checkForEmptyData() {

        if(!(tie_client_name.equals(""))){
            return false;
        }
        if(!(tie_client_name.equals(""))){
            return false;
        }
        if(!(tie_client_name.equals(""))){
            return false;
        }
        if(!(tie_client_name.equals(""))){
            return false;
        }

        return true;

    }

    public void onReceiveSurveyFloorFlat(List<SurveyFloorFlat> floors, int mode){


    }

    public void onReceiveFailureSurveyFloorFlat(String strErr, int mode){


    }

}