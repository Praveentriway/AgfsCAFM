package com.daemon.emco_android.ui.fragments.common.document;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.daemon.emco_android.ui.fragments.common.MainLandingUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.App;
import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.DocumentType;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.ReactiveSupportDoc;
import com.daemon.emco_android.model.request.DocumentTransaction;
import com.daemon.emco_android.model.response.CommonResponse;
import com.daemon.emco_android.model.response.DownloadDoc;
import com.daemon.emco_android.repository.remote.DocumentUploadRepository;
import com.daemon.emco_android.repository.remote.SupportDocumentRepository;
import com.daemon.emco_android.ui.components.FilterableListDialog;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.FileUtils;
import com.daemon.emco_android.utils.Font;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.daemon.emco_android.utils.AppUtils.ARGS_DOCCOUNT;
import static com.daemon.emco_android.utils.Utils.RSD;

public class DocumentUpload extends Fragment implements SupportDocumentRepository.SupportDocTypeI,View.OnClickListener,DocumentUploadRepository.ImageListner {
    // TODO: Rename parameter arguments, choose names that match

    private AppCompatActivity mActivity;
    private Bundle mSavedInstanceState;
    private Bundle mArgs;
    private Font font = App.getInstance().getFontInstance();
    private Toolbar mToolbar;
    private FragmentManager mManager;
    private CoordinatorLayout cl_main;
    private final int REQUEST_CHOOSE_PHOTO = 2;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 4;
    private final int REQUEST_TAKE_PHOTO = 1;
    private SharedPreferences mPreferences;
    private Login user;
    private String mStrEmpId = null;
    private String mLoginData = null;
    private SharedPreferences.Editor mEditor;
    private boolean isPermissionGranted = false;
    private TextView tv_toolbar_title,tv_img_name,tv_header;
    private ReactiveSupportDoc rxDoc;
    private DocumentTransaction dt;
    View rootView;
    private AppCompatTextView tv_lbl_doctype,tv_select_docType;
    private  List<DocumentType> docTypeList;
    private FloatingActionButton btn_upload_doc;
    private FrameLayout add_image_container;
    private AppCompatTextView tv_remarks;
    private AppCompatEditText tie_remarks;
    private CharSequence[] items;
    private String mImagePathToBeAttached;
    private final int THUMBNAIL_SIZE = 75;
    private ImageView img_thums;
    String  ret;
    String finalBase64=null;
    String docType=null;
    private File myFile;
    private Bitmap bitmap=null;
    private DocumentTransaction documentTrans;
    private DocumentType selectedDT=null;
    private String extension=null;
    private String SDCODE=RSD;
    private int count;
    private String type;

    Uri uri;
    byte[] fileBytes;
    public DocumentUpload() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){


            case R.id.tv_select_docType: {
                showDocTypeDialog();
            }
            break;

        }
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
        if(mArgs!=null){
            rxDoc= (ReactiveSupportDoc) mArgs.getParcelable(AppUtils.ARGS_SUPPORTDOC);

            dt= (DocumentTransaction) mArgs.getParcelable(AppUtils.ARGS_DOCTRANS);
            type=  mArgs.getString(AppUtils.ARGS_FILTERTYPE);
            count=mArgs.getInt(ARGS_DOCCOUNT);
        }

        new SupportDocumentRepository(mActivity,this).getDocType(type);
        AppUtils.showProgressDialog(mActivity,"",false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_document_upload, container, false);
        initView();
        setProps();
        setupActionBar();
        return  rootView;
    }

    public void initView(){
        tv_lbl_doctype=(AppCompatTextView) rootView.findViewById(R.id.tv_lbl_doctype);
        tv_select_docType=(AppCompatTextView) rootView.findViewById(R.id.tv_select_docType);
        btn_upload_doc=(FloatingActionButton) rootView.findViewById(R.id.btn_upload_doc);
        tv_remarks=(AppCompatTextView) rootView.findViewById(R.id.tv_remarks);
        tie_remarks=(AppCompatEditText) rootView.findViewById(R.id.tie_remarks);
        add_image_container=(FrameLayout) rootView.findViewById(R.id.add_image_container);
        img_thums=(ImageView) rootView.findViewById(R.id.img_thums);
        cl_main = (CoordinatorLayout) mActivity.findViewById(R.id.cl_main);
        tv_img_name=(TextView) rootView.findViewById(R.id.tv_img_name);
        tv_header = (TextView) rootView.findViewById(R.id.tv_header);

    }

    public void setProps(){

        AppUtils.closeInput(cl_main);
        tv_lbl_doctype.setText(Html.fromHtml(getString(R.string.rx_doc_type) + AppUtils.mandatory));
        tv_select_docType.setOnClickListener(this);
        tv_remarks.setText(Html.fromHtml("Remarks" + AppUtils.mandatory));
        tv_header.setText(rxDoc.getComplaintNo()+" - "+rxDoc.getBuildingName());

        add_image_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayAttachImageDialog();

            }
        });

        btn_upload_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadOnClick();

            }
        });


    }

    public void setupActionBar() {
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        tv_toolbar_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        LinearLayout linear_toolbar =(LinearLayout) mToolbar.findViewById(R.id.linear_profile) ;
        linear_toolbar.setVisibility(View.GONE);
        tv_toolbar_title.setText("Add Support Document");
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

   public void onDocTypeListSuccess(List<DocumentType> dtx, int from){
        AppUtils.hideProgressDialog();
        docTypeList=dtx;
       if(dt!=null){
           setValues(dt);
       }
   }

    public  void onDocTypeListFailure(String err, int from){
        AppUtils.hideProgressDialog();
        AppUtils.showToast(mActivity,err);
    }

    public  void showDocTypeDialog(){

        if(docTypeList!=null && docTypeList.size()>0){
            try {
                final ArrayList strArraySiteName = new ArrayList();
                for (DocumentType entity : docTypeList) {
                    strArraySiteName.add(entity.getCategoryName());
                }

                strArraySiteName.add("\n\n");
                FilterableListDialog.create(
                        mActivity,
                        ("Select Document Type"),
                        strArraySiteName,
                        new FilterableListDialog.OnListItemSelectedListener() {
                            @Override
                            public void onItemSelected(String text) {

                                if(!text.equals("\n\n")){
                                    tv_select_docType.setText(text);
                                    selectedDT=docTypeList.get((strArraySiteName.indexOf(text)));
                                    tv_img_name.setText(generateFilename());
                                }
                            }
                        })
                        .show();
            } catch (Exception ex) {
                ex.printStackTrace();
            } }
        else{
            AppUtils.showToast(mActivity,"No Data to show.");
        }
    }

    private void displayAttachImageDialog() {
        if (!isPermissionGranted) {
            getPermissionToReadExternalStorage();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        if(dt!=null) {
            items = new CharSequence[]{"View Document"};
            builder.setTitle("Document Viewer");
        }

        else{

            if (finalBase64 == null) {
                items = new CharSequence[]{"Take photo", "Choose Document"};
                builder.setTitle("Add  Document");
            } else {
                items = new CharSequence[]{"Take photo", "Choose Document", "View Document"};
            }
            builder.setTitle("Add  photo");
        }

        builder.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:

                                if (items[item].toString().equalsIgnoreCase("View Document")) {
                                    viewPhotoIntent(finalBase64);
                                } else if (items[item].toString().equalsIgnoreCase("Take photo")) {
                                    dispatchTakePhotoIntent();
                                }

                                    break;
                                case 1:
                                    dispatchChooseDocIntent();

                                    break;
                                case 2:

                                        viewPhotoIntent(finalBase64);

                                    break;

                            }
                        }
                    });
            builder.show();
            // }
        }


    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToReadExternalStorage() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }

                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }

            }
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            isPermissionGranted = true;
            displayAttachImageDialog();
        }
    }

    private void dispatchTakePhotoIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                 //   Log.d(TAG, "Cannot create a temp image file", e);
                }

                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    getActivity().startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg", storageDir);
        mImagePathToBeAttached = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            File file = new File(mImagePathToBeAttached);
            if (file.exists()) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mImagePathToBeAttached, options);
                options.inJustDecodeBounds = false;

                 bitmap = BitmapFactory.decodeFile(mImagePathToBeAttached, options);

                img_thums.setImageBitmap(
                        ThumbnailUtils.extractThumbnail(
                                bitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE));


                new AsyncTask<Void, String, String>() {
                    @Override
                    protected void onPreExecute() {

                        AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                        super.onPreExecute();
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        finalBase64=AppUtils.getEncodedString(bitmap);
                        extension=".jpg";

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String imageEntity) {

                        super.onPostExecute(imageEntity);
                        tv_img_name.setText(generateFilename());
                        AppUtils.hideProgressDialog();

                    }
                }.execute();
            }
        }
            else if (requestCode == REQUEST_CHOOSE_PHOTO) {


                try {

                    uri = data.getData();

                    if (getPath(uri) != null) {
                        myFile = new File(getPath(uri));

                    } else {
                        myFile = new File(FileUtils.getPath(mActivity, uri));
                    }


                    ret = myFile.getAbsolutePath();

                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        String path = ret;
                        FileInputStream fis = new FileInputStream(new File(path));

                        byte[] buf = new byte[1024];
                        int n;
                        while (-1 != (n = fis.read(buf)))
                            baos.write(buf, 0, n);

                        fileBytes = baos.toByteArray();

                        new AsyncTask<Void, String, String>() {
                            @Override
                            protected void onPreExecute() {

                                AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                                super.onPreExecute();
                            }

                            @Override
                            protected String doInBackground(Void... params) {
                                bitmap = BitmapFactory.decodeByteArray(fileBytes, 0, fileBytes.length);
                                finalBase64 = Base64.encodeToString(fileBytes, Base64.DEFAULT);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(String imageEntity) {

                                super.onPostExecute(imageEntity);

                                 extension = myFile.getAbsolutePath().substring(myFile.getAbsolutePath().lastIndexOf("."));

                                if (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png")) {

                                    img_thums.setImageBitmap(ThumbnailUtils.extractThumbnail(
                                            bitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE));

                                } else if(extension.equalsIgnoreCase(".pdf") ) {
                                    img_thums.setImageResource(R.drawable.pdf_icon);
                                }
                                else if(extension.equalsIgnoreCase(".doc") || extension.equalsIgnoreCase(".docx")) {
                                    img_thums.setImageResource(R.drawable.word_icon);
                                }
                                else if(extension.equalsIgnoreCase(".xls") || extension.equalsIgnoreCase(".xlsx") || extension.equalsIgnoreCase(".xlsm") ) {
                                    img_thums.setImageResource(R.drawable.excel_icon);
                                }

                                tv_img_name.setText(generateFilename());

                                AppUtils.hideProgressDialog();

                            }
                        }.execute();

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
    }

    public void dispatchChooseDocIntent(){

        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("*/*");
        String[] mimetypes = {"image/*", "application/pdf","application/doc","application/vnd.ms-excel",
        "application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select Document"), REQUEST_CHOOSE_PHOTO);

    }

    private String getPath(Uri uri) {
        String[]  data = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(mActivity, uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void uploadOnClick(){


     // checking condition whether the action is an add doc or update doc.

     if(dt==null){
         if(selectedDT==null){
             AppUtils.showSnackBar(R.id.coordinatorLayout, rootView, "Please select Document type");
         }

         else if(finalBase64==null){
             AppUtils.showSnackBar(R.id.coordinatorLayout, rootView, "No document has been added.");
         }
         else{
             pushDocToSever();
         }
     }
     else{

         pushDocToSever();

     }


    }

    public  void pushDocToSever()
    {
        documentTrans=new DocumentTransaction();

        if(dt!=null){
            documentTrans.setUniqueId(dt.getUniqueId());
            documentTrans.setCreatedBy(user.getEmployeeId());
            documentTrans.setOpco(rxDoc.getOpco());
            documentTrans.setReferenceId(rxDoc.getComplaintNo());
            documentTrans.setCategoryCode(selectedDT.getCategoryCode());
            documentTrans.setDocType(selectedDT.getCategoryName());
            documentTrans.setSdCode(selectedDT.getSdCode());
            documentTrans.setRefType(selectedDT.getSdCode());
            documentTrans.setRemarks(tie_remarks.getText().toString());
            documentTrans.setDocCount(count);
            documentTrans.setImage_name(dt.getImage_name());
            // sending null to get identified as update action on server side
            documentTrans.setBase64Image(null);
            documentTrans.setFileType(dt.getFileType());
            documentTrans.setModifiedBy(user.getEmployeeId());
            documentTrans.setFieldId(dt.getFieldId());
        }
        else{
            documentTrans.setUniqueId(generateUniqueID());
            documentTrans.setCreatedBy(user.getEmployeeId());
            documentTrans.setOpco(rxDoc.getOpco());
            documentTrans.setReferenceId(rxDoc.getComplaintNo());
            documentTrans.setCategoryCode(selectedDT.getCategoryCode());
            documentTrans.setDocType(selectedDT.getCategoryName());
            documentTrans.setSdCode(selectedDT.getSdCode());
            documentTrans.setRefType(selectedDT.getSdCode());
            documentTrans.setRemarks(tie_remarks.getText().toString());
            documentTrans.setBase64Image(finalBase64);
            documentTrans.setDocCount(count);
            documentTrans.setFileType(extension);
        }

        AppUtils.showProgressDialog(mActivity,"",true);
        new DocumentUploadRepository(mActivity,this).saveDocumentData(documentTrans,mActivity);
    }

    public void onDocSaveReceivedSuccess(CommonResponse resp, int mode){
        AppUtils.hideProgressDialog();
      //  AppUtils.showToast(mActivity,resp.getMessage());
        mActivity.onBackPressed();
    }
    public void onDocReceivedSuccess(DownloadDoc doc, int mode){}
    public void onDocSaveReceivedFailure(String strErr, int mode){
        AppUtils.hideProgressDialog();
        AppUtils.showToast(mActivity,strErr);
    }
    public void onDocReceivedFailure(String strErr, int mode){}

    public String generateUniqueID(){

    StringBuffer uniqueCode=new StringBuffer();

        if(SDCODE!=null){

            uniqueCode.append(SDCODE);

        }

        if(rxDoc.getOpco()!=null){

            uniqueCode.append("-"+rxDoc.getOpco());

        }

        if(rxDoc.getComplaintNo()!=null){

            uniqueCode.append("-"+rxDoc.getComplaintNo());

        }

       return uniqueCode.toString();
    }

    public String generateFilename(){

        StringBuffer fileName=new StringBuffer();

        if(SDCODE!=null){

            fileName.append(SDCODE);

        }

        if(rxDoc.getOpco()!=null){

            fileName.append("_"+rxDoc.getOpco());

        }

        if(rxDoc.getComplaintNo()!=null){

            fileName.append("_"+rxDoc.getComplaintNo());

        }

        if(selectedDT!=null){
            if(selectedDT.getCategoryCode()!=null){
                fileName.append("_"+selectedDT.getCategoryCode());
            }
        }

            if(count!=0){
                fileName.append("_"+count);
            }

        if(extension!=null){
            fileName.append(extension);
        }
      return fileName.toString();
    }

    private void viewPhotoIntent(final String base64) {


        if(extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") |extension.equalsIgnoreCase(".png")){
            Display display = mActivity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            loadPhoto(base64,width,height);
        }
        else{

            openFile();
        }

    }

    private void loadPhoto(String base64,int width, int height) {

        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup) rootView.findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageBitmap(AppUtils.getDecodedString(base64));
        image.getLayoutParams().height = height;
        image.getLayoutParams().width = width;
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);
        image.requestLayout();
        dialog.setContentView(layout);
        dialog.show();
    }


    private void openFile() {

        FileOutputStream fos = null;
        File file =null;
        Uri url =null;

                try {
                     file = new File(Environment.getExternalStorageDirectory()+"/com/AGFS/DOC/");

                    if(!file.exists()) {
                        file.mkdirs();
                    }
                        if (finalBase64 != null) {
                            byte[] byteArr = Base64.decode(finalBase64, Base64.DEFAULT);
                            File f = new File(file.getAbsolutePath(),"Document"+extension);

                            if(f.exists()){
                                f.delete();
                            }

                            f.createNewFile();
                            FileOutputStream fo = new FileOutputStream(f);
                            fo.write(byteArr);
                            url = Uri.fromFile(f);

                    }}catch (Exception e) {
                    e.printStackTrace();
                }

        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(url, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(url, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(url, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(url, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(url, "application/zip");
            } else if (url.toString().contains(".rar")){
                // RAR file
                intent.setDataAndType(url, "application/x-rar-compressed");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(url, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(url, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(url, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(url, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(url, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(url, "video/*");
            } else {
                intent.setDataAndType(url, "*/*");
            }
        //    intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(mActivity, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

    // setting value from args- this act as update document functionality.

    public  void setValues(DocumentTransaction dt){

         extension =dt.getFileType();
         finalBase64=dt.getBase64Image();

        if (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png")) {

            byte[] decodedString = Base64.decode(dt.getBase64Image(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            img_thums.setImageBitmap(ThumbnailUtils.extractThumbnail(
                    decodedByte, THUMBNAIL_SIZE, THUMBNAIL_SIZE));

        } else if(extension.equalsIgnoreCase(".pdf") ) {
            img_thums.setImageResource(R.drawable.pdf_icon);
        }
        else if(extension.equalsIgnoreCase(".doc") || extension.equalsIgnoreCase(".docx")) {
            img_thums.setImageResource(R.drawable.word_icon);
        }
        else if(extension.equalsIgnoreCase(".xls") || extension.equalsIgnoreCase(".xlsx") || extension.equalsIgnoreCase(".xlsm") ) {
            img_thums.setImageResource(R.drawable.excel_icon);
        }

        tv_img_name.setText(dt.getImage_name());
        tv_select_docType.setText(dt.getDocType());
        getDocType(dt.getDocType());
        tie_remarks.setText(dt.getRemarks());
   //     add_image_container.setEnabled(false);
        tv_select_docType.setEnabled(false);

    }

    // to get document type object from category name

    public void getDocType(String catName){

        if(docTypeList!=null){

            for (DocumentType dt:docTypeList){

                if(dt.getCategoryName().equalsIgnoreCase(catName)){
                    selectedDT=dt;
                }
            }
        }
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
                Fragment _fragment = new MainLandingUI();
                FragmentTransaction _transaction = getActivity().getSupportFragmentManager().beginTransaction();
                _transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                _transaction.replace(R.id.frame_container, _fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
