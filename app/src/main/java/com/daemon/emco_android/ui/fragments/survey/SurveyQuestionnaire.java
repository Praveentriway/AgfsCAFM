package com.daemon.emco_android.ui.fragments.survey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.daemon.emco_android.utils.AppUtils;
import com.github.florent37.expectanim.ExpectAnim;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.CustomerFeedbackAdapter;
import com.daemon.emco_android.repository.db.entity.ServeyQuestionnaire;
import com.daemon.emco_android.repository.db.entity.SurveyTransaction;
import com.daemon.emco_android.utils.Utils;
import java.util.ArrayList;
import java.util.List;

import static com.daemon.emco_android.ui.fragments.survey.SurveyHeader.DETAILED;
import static com.daemon.emco_android.ui.fragments.survey.SurveyHeader.SUMMARY;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SUGESSTIONFLAG;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYQUES;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYTRANS;
import static com.daemon.emco_android.utils.AppUtils.showErrorToast;
import static com.github.florent37.expectanim.core.Expectations.atItsOriginalPosition;
import static com.github.florent37.expectanim.core.Expectations.invisible;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.visible;


public class SurveyQuestionnaire extends Fragment implements CustomerFeedbackAdapter.OptionTouchListner {

    View view;
    private AppCompatActivity mActivity;
    FloatingActionButton fab_next;
    Toolbar mToolbar;
    TextView tv_toolbar_title;
    private Bundle mArgs;
    private CustomerFeedbackAdapter adapter;
    private ArrayList<ServeyQuestionnaire> questionnaires;
    private SurveyTransaction surveyTransaction;
    boolean suggestionFlag;
    RecyclerView recycler_view;

    public SurveyQuestionnaire() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true);
        mArgs = getArguments();
        if(mArgs!=null){

            questionnaires = (ArrayList<ServeyQuestionnaire>) mArgs.getSerializable(ARGS_SURVEYQUES);
            surveyTransaction = (SurveyTransaction) mArgs.getSerializable(ARGS_SURVEYTRANS);
            suggestionFlag=mArgs.getBoolean(ARGS_SUGESSTIONFLAG);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_customer_feedback_form, container, false);
        setProprties(view);
        setupActionBar();
        return view;
    }


    public void setProprties(View view){

        fab_next=(FloatingActionButton) view.findViewById(R.id.fab_next);
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processQuest();
            }
        });
        new ExpectAnim()
                .expect(fab_next)
                .toBe(
                        outOfScreen(Gravity.BOTTOM),
                        invisible()
                )
                .toAnimation()
                .setNow();

        recycler_view=(RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        adapter=new CustomerFeedbackAdapter(mActivity,getQuest(surveyTransaction.getSurveyType()),surveyTransaction.getSurveyType(),this);
        recycler_view.setAdapter(adapter);

    }

    public void processQuest(){
        if(adapter.getQuestionnaire()!=null){

            if(surveyTransaction.getSurveyType().equalsIgnoreCase(SUMMARY)){

                if(adapter.getOptionScore(0)<surveyTransaction.getScore()){

                    showDialog("Please spend few minutes to give us detailed feedback about our service.");
                }
                else{
                    surveyTransaction.setQues(adapter.getQuestionnaire());
                    loadFragment(new SurveyFeedback(), Utils.TAG_FRAGMENT_CUST_FEEDBACK_SUGGESTION);
                }
            }
            else{

                if(surveyTransaction.getQues()!=null){

                    List<ServeyQuestionnaire> ques =surveyTransaction.getQues();
                    ques.addAll(adapter.getQuestionnaire());
                    surveyTransaction.setQues(ques);
                    loadFragment(new SurveyFeedback(), Utils.TAG_FRAGMENT_CUST_FEEDBACK_SUGGESTION);

                }

                else{
                    surveyTransaction.setQues(adapter.getQuestionnaire());
                    loadFragment(new SurveyFeedback(), Utils.TAG_FRAGMENT_CUST_FEEDBACK_SUGGESTION);
                }
            }
        }
        else{
            showErrorToast(mActivity,"Please fill all the questions.");
        }
    }

    public void loadFragment(final Fragment fragment, final String tag) {

        Bundle mdata = new Bundle();
        mdata.putSerializable(ARGS_SURVEYTRANS,surveyTransaction);
        mdata.putBoolean(AppUtils.ARGS_SUGESSTIONFLAG,suggestionFlag);
        fragment.setArguments(mdata);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

    }

    public void setupActionBar() {

        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Rate our service");
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showbackPressAlert();
                    }
                });
    }

    public void showbackPressAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Are you sure you want to close the Survey Questionnaire?")
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

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_logout).setVisible(false);
    }

    private void showDialog(final String msg) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mActivity);
        builder.setMessage(msg).setTitle("Detailed Feedback")
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();

                        surveyTransaction.setQues(adapter.getQuestionnaire());
                        surveyTransaction.setSurveyType(DETAILED);
                        adapter=new CustomerFeedbackAdapter(mActivity,getQuest(surveyTransaction.getSurveyType()),surveyTransaction.getSurveyType(),SurveyQuestionnaire.this);
                        recycler_view.setAdapter(adapter);

                        new ExpectAnim()
                                .expect(fab_next)
                                .toBe(
                                        atItsOriginalPosition(),
                                        visible()
                                )
                                .toAnimation()
                                .setDuration(800)
                                .start();

                    }
                }).setNegativeButton("Not now", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                dialog.cancel();

                surveyTransaction.setQues(adapter.getQuestionnaire());
                loadFragment(new SurveyFeedback(), Utils.TAG_FRAGMENT_CUST_FEEDBACK_SUGGESTION);

            }});

        final android.app.AlertDialog alert = builder.create();
        alert.show();

    }


public ArrayList<ServeyQuestionnaire> getQuest(String type){

    ArrayList<ServeyQuestionnaire> temp=new ArrayList<>();

      for(ServeyQuestionnaire ques:questionnaires){

          if(ques.getQuesType().equalsIgnoreCase(type)){
              temp.add(ques);
          }
      }
        return temp;
}


   public void onOptionSelected(){
    processQuest();
    }

    }
