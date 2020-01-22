package com.daemon.emco_android.ui.fragments.common;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.adapter.ImagePickerAdapter;
import com.daemon.emco_android.listeners.ImagePickListener;
import com.daemon.emco_android.model.common.CustomGallery;
import com.daemon.emco_android.utils.Font;

import java.util.ArrayList;
import java.util.Collections;


public class Fragment_ImagePicker extends DialogFragment {
    public static String MODULE = "Fragment_ImagePicker";
    public static String TAG = "";
    AppCompatActivity mActivity;
    FragmentManager mManager;
    CoordinatorLayout cl_main;
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Button btn_ok;
    ImagePickerAdapter adapter;
    ImagePickListener mCallBack;
    ArrayList<CustomGallery> mList = new ArrayList<CustomGallery>();
    Bundle mArgs;
    View rootView;
    String[] Str_Path_Array;
    int mSelectedCount = 0;

    private String ACTION_PICK = "luminous.ACTION_PICK";

    private Font font = App.getInstance().getFontInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            TAG = "onCreate";
            Log.d(MODULE, TAG);
            mActivity = (AppCompatActivity) getActivity();
            mManager = mActivity.getSupportFragmentManager();
            setHasOptionsMenu(true);
            setRetainInstance(false);
            mArgs = getArguments();

            mList = getGalleryPhotos();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The system calls this only when creating the layout in a dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TAG = "onCreateDialog";
        Log.d(MODULE, TAG);
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_image_picker, container, false);
        TAG = "onCreateView";
        Log.d(MODULE, TAG);
        initView(rootView);
        return rootView;
    }

    public void initView(View view) {
        TAG = "initView";
        Log.d(MODULE, TAG);
        try {
            toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
            cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            btn_ok = (Button) view.findViewById(R.id.btn_ok);
            setProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setActionBar() {
        TAG = "setActionBar";
        Log.d(MODULE, TAG);

        try {
            toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

            TextView tv_toolbar_title = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
            tv_toolbar_title.setText("Select Image");
            LinearLayout linear_profile=(LinearLayout) toolbar.findViewById(R.id.linear_profile);
            linear_profile.setVisibility(View.GONE);
            mActivity.setSupportActionBar(toolbar);
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.onBackPressed();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        TAG = "onStart";
        Log.d(MODULE, TAG);

        try {
            setActionBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setProperties() {
        TAG = "setProperties";
        Log.d(MODULE, TAG);
        try {
            setManager();
            setData();
            btn_ok.setTypeface(font.getHelveticaRegular());
            rootView.findViewById(R.id.ll_bottom_container).setVisibility(View.GONE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setManager() {
        TAG = "setManager";
        Log.d(MODULE, TAG);
        try {
            mLayoutManager = new GridLayoutManager(mActivity, 2);
            recyclerView.setLayoutManager(mLayoutManager);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<CustomGallery> getGalleryPhotos() {
        TAG = "getGalleryPhotos";
        Log.d(MODULE, TAG);

        ArrayList<CustomGallery> galleryList = new ArrayList<CustomGallery>();
        try {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;
            Cursor imagecursor = mActivity.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            Log.d(MODULE, TAG + " imagecursor : " + imagecursor.getCount());
            if (imagecursor != null && imagecursor.getCount() > 0) {
                while (imagecursor.moveToNext()) {
                    CustomGallery item = new CustomGallery();
                    int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    item.setSdcardPath(imagecursor.getString(dataColumnIndex));
                    galleryList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // show newest photo at beginning of the list
        Collections.reverse(galleryList);
        return galleryList;
    }

    public void setData() {
        TAG = "setData";
        Log.d(MODULE, TAG);

        try {
            Log.d(MODULE, TAG + " mList : " + mList.size());
            if (mList.size() > 0) {
                adapter = new ImagePickerAdapter(this, mList,mCallBack);
                recyclerView.setAdapter(adapter);
            } else {

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void SetImagePickListener(ImagePickListener mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void DismissDialog() {
        this.dismiss();
        if (mManager != null) mManager.popBackStack();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TAG = "onOptionsItemSelected";
        Log.d(MODULE, TAG);

        switch (item.getItemId()) {
            case android.R.id.home:
                mManager.popBackStack();
                return true;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
