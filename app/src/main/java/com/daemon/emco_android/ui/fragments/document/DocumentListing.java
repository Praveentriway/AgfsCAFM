package com.daemon.emco_android.ui.fragments.document;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.ReactiveSupportDoc;
import com.daemon.emco_android.model.request.DocumentTransaction;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.DownloadDoc;
import com.daemon.emco_android.repository.remote.DocumentUploadRepository;
import com.daemon.emco_android.ui.adapter.DocumentListingAdapter;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.Font;
import com.daemon.emco_android.utils.Utils;
import com.google.gson.Gson;
import java.util.List;


public class DocumentListing extends Fragment implements DocumentUploadRepository.ImageListner,DocumentListingAdapter.ItemListner {

    FloatingActionButton fab_add;
    View rootView;
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
    private TextView tv_toolbar_title,tv_header;
    private RecyclerView recyclerView;
    private ReactiveSupportDoc rxDoc;
    private DocumentListingAdapter adapter;
    DocumentTransaction trans=new DocumentTransaction();
    private DocumentTransaction docTrans;
    private int docCount=0;
    private String type;

    public DocumentListing() {

        // Required empty public constructor

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
        if (getArguments() != null) {
            rxDoc= (ReactiveSupportDoc) mArgs.getParcelable(AppUtils.ARGS_SUPPORTDOC);
            type=  mArgs.getString(AppUtils.ARGS_FILTERTYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        rootView=inflater.inflate(R.layout.fragment_document_listing, container, false);
        initView();
        setProps();
        setupActionBar();

        return rootView;
    }

    public void initView(){

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        tv_header = (TextView) rootView.findViewById(R.id.tv_header);
        fab_add=(FloatingActionButton) rootView.findViewById(R.id.btn_add_img);

    }

    public void setProps(){

        setManager();
        trans.setOpco(rxDoc.getOpco());
        trans.setReferenceId(rxDoc.getComplaintNo());
        tv_header.setText(rxDoc.getComplaintNo()+" - "+rxDoc.getBuildingName());
        AppUtils.showProgressDialog(mActivity,"",true);
        new DocumentUploadRepository(mActivity,this).getDocumentData(trans,mActivity);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadFragment(new DocumentUpload(),rxDoc,docTrans,docCount, Utils.TAG_FRAGMENT_DOC_UPLOAD);

            }
        });

    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Support Documents");
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

    public void setManager() {
        try {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadFragment(final Fragment fragment,ReactiveSupportDoc rx,DocumentTransaction dt,int count, final String tag) {

        Bundle mdata = new Bundle();
        mdata.putParcelable(AppUtils.ARGS_SUPPORTDOC, rx);
        mdata.putParcelable(AppUtils.ARGS_DOCTRANS, dt);
        mdata.putString(AppUtils.ARGS_FILTERTYPE, type);
        mdata.putInt(AppUtils.ARGS_DOCCOUNT,count);

        fragment.setArguments(mdata);
        // update the main content by replacing fragments
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

    }

    public   void onDocSaveReceivedSuccess(CommonResponse resp, int mode){}
    public  void onDocReceivedSuccess(DownloadDoc doc, int mode){

        AppUtils.hideProgressDialog();

        // doc count will be count for next file naming
        docCount=doc.getBase64Docs().size();

        if(doc.getBase64Docs().size()<doc.getDocCount()){
            fab_add.setVisibility(View.VISIBLE);
        }

        adapter=new DocumentListingAdapter(doc.getBase64Docs(),0,this,mActivity);
        recyclerView.setAdapter(adapter);

    }
    public  void onDocSaveReceivedFailure(String strErr, int mode){}

    public void onDocReceivedFailure(String strErr, int mode){}

    public void onViewClick(int pos, DocumentTransaction trans, List<DocumentTransaction> transList){
        loadFragment(new DocumentUpload(),rxDoc,trans,transList.size()-(pos+1), Utils.TAG_FRAGMENT_DOC_UPLOAD);
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


    public void onAddNewDocument(){
        loadFragment(new DocumentUpload(),rxDoc,docTrans,docCount, Utils.TAG_FRAGMENT_DOC_UPLOAD);
    }

}
