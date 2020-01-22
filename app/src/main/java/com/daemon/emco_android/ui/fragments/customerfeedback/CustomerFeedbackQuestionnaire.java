package com.daemon.emco_android.ui.fragments.customerfeedback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.CustomerFeedbackAdapter;
import com.daemon.emco_android.ui.components.SimpleDividerItemDecoration;
import com.daemon.emco_android.repository.db.entity.ServeyQuestionnaire;
import com.daemon.emco_android.repository.db.entity.SurveyTransaction;
import com.daemon.emco_android.utils.Utils;

import java.util.ArrayList;

import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYQUES;
import static com.daemon.emco_android.utils.AppUtils.ARGS_SURVEYTRANS;


public class CustomerFeedbackQuestionnaire extends Fragment {

    View view;
    private AppCompatActivity mActivity;
    FloatingActionButton fab_next;
    Toolbar mToolbar;
    TextView tv_toolbar_title;
    private Bundle mArgs;
    private static final String TAG = CustomerFeedbackQuestionnaire.class.getSimpleName();
    private CustomerFeedbackAdapter adapter;
    private ArrayList<ServeyQuestionnaire> questionnaires;
    private SurveyTransaction surveyTransaction;

    public CustomerFeedbackQuestionnaire() {
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
                if(adapter.getQuestionnaire()!=null){
                    surveyTransaction.setQues(adapter.getQuestionnaire());
                    loadFragment(new CustomerFeedbackSignature(), Utils.TAG_FRAGMENT_CUST_FEEDBACK_SUGGESTION);
                }
                else{
                    Toast.makeText(mActivity,"Please fill all the questions.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        RecyclerView recycler_view=(RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager   mLayoutManager = new LinearLayoutManager(mActivity);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
//        recycler_view.addItemDecoration(new SimpleDividerItemDecoration(mActivity));
        adapter=new CustomerFeedbackAdapter(mActivity,questionnaires);
        recycler_view.setAdapter(adapter);
    }

    public void loadFragment(final Fragment fragment, final String tag) {
        Log.d(TAG, "loadFragment");
        // update the main content by replacing fragments

        Bundle mdata = new Bundle();
        mdata.putSerializable(ARGS_SURVEYTRANS,surveyTransaction);
        fragment.setArguments(mdata);

        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }


    public void setupActionBar() {
        Log.d(TAG, "setupActionBar");
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Customer Questionnaire");
        mActivity.setSupportActionBar(mToolbar);
     //   mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        Log.d(TAG, "onPrepareOptionsMenu ");
        menu.findItem(R.id.action_logout).setVisible(false);

    }


}
