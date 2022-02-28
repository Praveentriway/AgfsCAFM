package com.daemon.emco_android.ui.fragments.document;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.model.request.PPMFilterRequest;
import com.daemon.emco_android.repository.remote.SupportDocumentRepository;
import com.daemon.emco_android.ui.adapter.DocumentComplaintListAdapter;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.ReactiveSupportDoc;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class DocumentComplaintList extends Fragment implements SupportDocumentRepository.SupportDocI,DocumentComplaintListAdapter.DocumentListOnClick {

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextView tv_complaint_no,
            tv_site_location,
            tv_complaint_details,
            tv_time,
            tv_status,
            tv_work_type,
            tv_priority,
            tv_customer_ref;
    private AppCompatActivity mActivity;
    private Bundle mSavedInstanceState;
    private Bundle mArgs;
    private Font font = App.getInstance().getFontInstance();
    private Toolbar mToolbar;
    private FragmentManager mManager;
    private CoordinatorLayout cl_main;
    private SharedPreferences mPreferences;
    private Login user;
    private String mStrEmpId = null;
    private String mLoginData = null;
    private SharedPreferences.Editor mEditor;
    private TextView tv_toolbar_title;
    private List<ReactiveSupportDoc> data=new ArrayList<>();
    private PPMFilterRequest ppmfilter;
    private int totalnoOfRows = 0;
    private int mCurrentnoOfRows = 0;
    private List<ReactiveSupportDoc> mList = new ArrayList<>();
    ReactiveSupportDoc rx;
    DocumentComplaintListAdapter adapter;
    private String type;

    public DocumentComplaintList() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true);
        setRetainInstance(false);
        mManager = mActivity.getSupportFragmentManager();
        mSavedInstanceState = savedInstanceState;
        mArgs = getArguments();
        mActivity
                .getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mLoginData = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
        if (mLoginData != null) {
            Gson gson = new Gson();
            user = gson.fromJson(mLoginData, Login.class);
            mStrEmpId = user.getEmployeeId();
        }

        if(mArgs==null){

            String ppmfil = mPreferences.getString(AppUtils.SHARED_DOC_FILTER, null);
            Gson gson = new Gson();
            ppmfilter = gson.fromJson(ppmfil, PPMFilterRequest.class);

          //  type=  mArgs.getString(AppUtils.ARGS_FILTERTYPE);
        }
        else{
            ppmfilter= (PPMFilterRequest) mArgs.getSerializable(AppUtils.ARGS_PPMFILTER);
            type=  mArgs.getString(AppUtils.ARGS_FILTERTYPE);
            Gson gson = new Gson();
            String ppmfil = gson.toJson(ppmfilter);
            SharedPreferences.Editor mEditor = mPreferences.edit();
            mEditor.putString(AppUtils.SHARED_DOC_FILTER, ppmfil);
            mEditor.commit();
        }
        getDocComplaintList(mCurrentnoOfRows);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment_doc_complaint_list, container, false);
        initView(rootView);
        setProperties();
        setupActionBar();
        return rootView;
    }

    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        tv_complaint_no = (TextView) view.findViewById(R.id.tv_complaint_no);
        tv_time = (TextView) view.findViewById(R.id.tv_date);
        tv_work_type = (TextView) view.findViewById(R.id.tv_work_type);
        tv_priority = (TextView) view.findViewById(R.id.tv_priority);
        tv_site_location = (TextView) view.findViewById(R.id.tv_site_location);
        tv_complaint_details = (TextView) view.findViewById(R.id.tv_complaint_details);
        tv_status = (TextView) view.findViewById(R.id.tv_status);
        tv_customer_ref = (TextView) view.findViewById(R.id.tv_customer_ref);
    }

    public void loadFragment(final Fragment fragment, final String tag) {

        Bundle mdata = new Bundle();
        mdata.putParcelable(AppUtils.ARGS_SUPPORTDOC, rx);
        mdata.putString(AppUtils.ARGS_FILTERTYPE, type);

        fragment.setArguments(mdata);
        // update the main content by replacing fragments
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public void setProperties() {

        try {
            setManager();
            tv_complaint_no.setTypeface(font.getHelveticaRegular());
            tv_time.setTypeface(font.getHelveticaRegular());
            tv_work_type.setTypeface(font.getHelveticaRegular());
            tv_priority.setTypeface(font.getHelveticaRegular());
            tv_site_location.setTypeface(font.getHelveticaRegular());
            tv_complaint_details.setTypeface(font.getHelveticaRegular());
            tv_status.setTypeface(font.getHelveticaRegular());
            tv_customer_ref.setTypeface(font.getHelveticaRegular());

            if(type.equalsIgnoreCase("R")){
                tv_complaint_no.setText("Complaint No.");
                tv_time.setText("Complaint Date");
                tv_work_type.setText("Work Type");
                tv_priority.setText("Priority");
                tv_site_location.setText("Site/Location");
                tv_complaint_details.setText("Complaint Details");
                tv_status.setText("Status");
                tv_customer_ref.setText("Customer Ref.");
            }
            else{
                tv_complaint_no.setText("PPM No.");
                tv_time.setText("Planned Date");
                tv_work_type.setText("Work Type");
                tv_priority.setText("Completed Date");
                tv_site_location.setText("Building/Loc.");
                tv_complaint_details.setText("Complaint Details");
                tv_status.setText("Status");
                tv_customer_ref.setText("Barcode");

            }


            recyclerView.addOnScrollListener(
                    new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);


                            LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                            int total = lm.getItemCount();
                            int lastVisibleItemCount = lm.findLastVisibleItemPosition();

                            //to avoid multiple calls to loadMore() method
                            //maintain a boolean value (isLoading). if loadMore() task started set to true and completes set to false

                            if (total > 0)
                                if ((total - 1) == lastVisibleItemCount){
                                    getDocComplaintList(totalnoOfRows);
                                }
                                else{

                                }
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                        }
                    });
            adapter=new DocumentComplaintListAdapter(mActivity,data,type);
            adapter.setListener(this);
            recyclerView.setAdapter(adapter);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setManager() {
        try {
            mLayoutManager = new LinearLayoutManager(mActivity);
            recyclerView.setLayoutManager(mLayoutManager);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Complaint List");
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mManager.popBackStack();
                    }
                });
    }

    public void getDocComplaintList(int startIndex){
        if(startIndex==0){
            AppUtils.showProgressDialog2(mActivity,"Loading",true);
        }
         new SupportDocumentRepository(mActivity, this).getDocListData(mStrEmpId, startIndex,ppmfilter,type);
    }

    public void onListRcSuccess(List<ReactiveSupportDoc> rx, int from){

        AppUtils.hideProgressDialog();
        if(rx.size()>0){
            data.addAll(rx);
            totalnoOfRows=data.size();
            adapter.notifyDataSetChanged();
        }
    }
    public  void onListRcFailure(String err, int from){
        AppUtils.hideProgressDialog();
        AppUtils.showToast(mActivity,err);
    }

    public void onDocumentListItemClicked(List<ReactiveSupportDoc> temp, int position) {
         rx = temp.get(position);
        loadFragment(new DocumentListing(), Utils.FRAGMENT_DOC_LIST);
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


}
