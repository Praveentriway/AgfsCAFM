package com.daemon.emco_android.ui.fragments.survey;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.R;
import com.daemon.emco_android.repository.remote.CustomerSurveyRepository;
import com.daemon.emco_android.repository.db.entity.SurveyTransaction;
import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.daemon.emco_android.utils.AppUtils;
import com.github.gcacace.signaturepad.views.SignaturePad;

import static com.daemon.emco_android.utils.AppUtils.ARGS_SUGESSTIONFLAG;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYTRANS;

public class SurveyFeedback extends Fragment implements CustomerSurveyRepository.SaveListener {

    private AppCompatActivity mActivity;
    Toolbar mToolbar;
    TextView tv_toolbar_title;
    private static final String TAG = SurveyFeedback.class.getSimpleName();
    FloatingActionButton fab_next;
    private SignaturePad signaturePad;
    Button btnClear;
    View view;
    private Bundle mArgs;
    AppCompatTextView tv_lbl_signature;
    AppCompatTextView tv_lbl_suggestion;
    private CardView cardviewSugesstion;
    EditText tie_suggestion;
    SurveyTransaction surveyTransaction;
    boolean suggestionFlag;

   public SurveyFeedback() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true);
        mArgs = getArguments();
        if(mArgs!=null){
            surveyTransaction = (SurveyTransaction) mArgs.getSerializable(ARGS_SURVEYTRANS);
            suggestionFlag=mArgs.getBoolean(ARGS_SUGESSTIONFLAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_customer_feedback_signature, container, false);
        signaturePad = (SignaturePad) view.findViewById(R.id.signaturePad);
        btnClear= (Button) view.findViewById(R.id.btnClear);
        setupActionBar();
        tv_lbl_signature = (AppCompatTextView) view.findViewById(R.id.tv_lbl_signature);
        tv_lbl_suggestion = (AppCompatTextView) view.findViewById(R.id.tv_lbl_suggestion);
        cardviewSugesstion= (CardView) view.findViewById(R.id.cardviewSugesstion);
        tie_suggestion= (EditText) view.findViewById(R.id.tie_suggestion);
        tv_lbl_signature.setText(Html.fromHtml("Customer Signature " + AppUtils.mandatory));
        tv_lbl_suggestion.setText(Html.fromHtml("Suggestion " + AppUtils.mandatory));
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
            }
        });
        fab_next=(FloatingActionButton) view.findViewById(R.id.fab_next);
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSurvey();
            }
        });
        setProps();
        return view;
    }

    public void setProps(){
       cardviewSugesstion.setVisibility(suggestionFlag?View.VISIBLE:View.GONE);
    }

    public void setupActionBar() {
        Log.d(TAG, "setupActionBar");
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Customer Signature");
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
        Log.d(TAG, "onPrepareOptionsMenu ");
        menu.findItem(R.id.action_logout).setVisible(false);
    }

    public  void saveSurvey(){
       if(tie_suggestion.getText().toString().equalsIgnoreCase("") && suggestionFlag ){
           Toast.makeText(mActivity,"Suggestion should not be empty.",Toast.LENGTH_SHORT).show();
       }
       else if(signaturePad.isEmpty()){
           Toast.makeText(mActivity,"Customer Signature should not be empty.",Toast.LENGTH_SHORT).show();
       }
       else{
           AppUtils.showProgressDialog(mActivity,"Loading...",true);
           call();
       }
    }

    public void call(){
        surveyTransaction.setSuggestion(tie_suggestion.getText().toString());
        surveyTransaction.setSignature(AppUtils.getEncodedString(signaturePad.getSignatureBitmap()));
        new CustomerSurveyRepository(mActivity,this).saveSurvey(surveyTransaction);
    }

   public void onSuccessSaveSurvey(String strMsg, int mode){
       AppUtils.hideProgressDialog();
       Toast.makeText(mActivity,"Survey saved successfully.",Toast.LENGTH_SHORT).show();
       FragmentManager fm = getActivity().getSupportFragmentManager();
       for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
           fm.popBackStack();
       }
       Fragment _fragment = new MainLandingUI();
       FragmentTransaction _transaction = mActivity.getSupportFragmentManager().beginTransaction();
       _transaction.replace(R.id.frame_container, _fragment);
   }

    public void onFailureSaveSurvey(String strErr, int mode){
       AppUtils.hideProgressDialog();
       Toast.makeText(mActivity,strErr,Toast.LENGTH_SHORT).show();
    }

}
