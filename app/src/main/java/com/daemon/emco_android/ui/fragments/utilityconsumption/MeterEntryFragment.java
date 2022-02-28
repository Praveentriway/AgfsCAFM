package com.daemon.emco_android.ui.fragments.utilityconsumption;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.daemon.emco_android.R;
import com.daemon.emco_android.databinding.FragmentMeterEntryBinding;
import com.daemon.emco_android.listeners.DefectDoneImage_Listener;
import com.daemon.emco_android.model.common.EmployeeTrackingDetail;
import com.daemon.emco_android.model.common.Login;
import com.daemon.emco_android.model.common.UtilityAssetDetail;
import com.daemon.emco_android.model.common.UtilityAssetTransaction;
import com.daemon.emco_android.model.response.DefectDoneImageUploaded;
import com.daemon.emco_android.model.response.RCDownloadImage;
import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;
import com.daemon.emco_android.repository.remote.ReceiveComplaintRespondService;
import com.daemon.emco_android.repository.remote.UtilityRepository;
import com.daemon.emco_android.service.GPSTracker;
import com.daemon.emco_android.ui.fragments.common.MainDashboard;
import com.daemon.emco_android.utils.AppUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;

import static android.content.Context.MODE_PRIVATE;
import static com.daemon.emco_android.utils.AppUtils.ARGS_UTILITY_ASSET;
import static com.daemon.emco_android.utils.AppUtils.METER_READING_EXTRA_FILEPATH;
import static com.daemon.emco_android.utils.AppUtils.checkInternet;


public class MeterEntryFragment extends Fragment implements UtilityRepository.Listener, DefectDoneImage_Listener {

    private Bitmap mSelectedImage;
    // Max width (portrait mode)
    private Integer mImageMaxWidth;
    // Max height (portrait mode)
    private Integer mImageMaxHeight;
    UtilityAssetDetail utilityAssetDetail;
    private ReceiveComplaintRespondService receiveComplaintRespond_service;
    String orginalText="";
    String filePath;
    String refNo;
    private AppCompatActivity mActivity;

    /**
     * Number of results to show in the UI.
     */
    private static final int RESULTS_TO_SHOW = 3;
    /**
     * Dimensions of inputs.
     */
    private static final int DIM_IMG_SIZE_X = 224;
    private static final int DIM_IMG_SIZE_Y = 224;

    private final PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float>
                                o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });

   FragmentMeterEntryBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();
        if (getArguments() != null) {
            if(getArguments().getString(METER_READING_EXTRA_FILEPATH)!=null){
                filePath = getArguments().getString(METER_READING_EXTRA_FILEPATH);
            }
            utilityAssetDetail = (UtilityAssetDetail) getArguments().getSerializable(ARGS_UTILITY_ASSET);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_meter_entry, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        receiveComplaintRespond_service = new ReceiveComplaintRespondService(mActivity);
        receiveComplaintRespond_service.setmCallbackImages(this);

        refNo=getReferenceNo();
        binding.tvUom.setText(utilityAssetDetail.getUom());
        binding.tvUtilityDet.setText(utilityAssetDetail.getAssetBarcode()+" - "+utilityAssetDetail.getEquipmentName());

       if(filePath!=null){

           // to show image view and text extraction layout

           Glide.with(getContext()).load(filePath).into(binding.imageView);

           Glide.with(getContext()).load(filePath).asBitmap()
                   .into(new SimpleTarget<Bitmap>() {
                       @Override
                       public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                           mSelectedImage = resource;
                       }
                   });
       }

       else{

           // to show manual entry layout

           binding.imageView.setVisibility(View.GONE);
           binding.viewButtons.setVisibility(View.GONE);
           binding.viewReadings.setVisibility(View.VISIBLE);
           binding.fabSave.setVisibility(View.VISIBLE);
       }

        binding.fabExtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTextRecognition();
            }
        });

        binding.fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        binding.fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTransaction();
            }
        });



    }


    private void runTextRecognition() {
        InputImage image = InputImage.fromBitmap(mSelectedImage, 0);
        TextRecognizer recognizer = TextRecognition.getClient();
        recognizer.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text texts) {
                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                e.printStackTrace();
                            }
                        });
    }

    private void processTextRecognitionResult(Text texts) {
        List<Text.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            showToast("No text found");
            return;
        }
        //  mGraphicOverlay.clear();
        for (int i = 0; i < blocks.size(); i++) {
            List<Text.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<Text.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {

                }


                orginalText=texts.getText();
         //       Log.d("rcgn txt", texts.getText());
                binding.edtReadings.setText(texts.getText().replaceAll("\\s",""));
                binding.viewButtons.setVisibility(View.GONE);
                binding.viewReadings.setVisibility(View.VISIBLE);
                binding.fabSave.setVisibility(View.VISIBLE);


            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Functions for loading images from app assets.

    // Returns max image width, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private Integer getImageMaxWidth() {
        if (mImageMaxWidth == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxWidth =  binding.imageView.getWidth();
        }

        return mImageMaxWidth;
    }

    // Returns max image height, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private Integer getImageMaxHeight() {
        if (mImageMaxHeight == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxHeight =
                    binding.imageView.getHeight();
        }

        return mImageMaxHeight;
    }

    // Gets the targeted width / height.
    private Pair<Integer, Integer> getTargetedWidthHeight() {
        int targetWidth;
        int targetHeight;
        int maxWidthForPortraitMode = getImageMaxWidth();
        int maxHeightForPortraitMode = getImageMaxHeight();
        targetWidth = maxWidthForPortraitMode;
        targetHeight = maxHeightForPortraitMode;
        return new Pair<>(targetWidth, targetHeight);
    }


    public void goBack(){

        FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
        Fragment _fragment = new MainDashboard();
        FragmentTransaction _transaction = fm.beginTransaction();
        _transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        _transaction.replace(R.id.frame_container, _fragment);

    }

    public void updateTransaction(){

        if(!(binding.edtReadings.getText().toString().matches(".*[a-zA-Z]+.*") )){

            GPSTracker gps=new GPSTracker(getContext());

            UtilityAssetTransaction transaction=new UtilityAssetTransaction();
            transaction.setMeterActual(orginalText);
            transaction.setMeterEntered(binding.edtReadings.getText().toString());
            transaction.setMultifactor(utilityAssetDetail.getMultipleFactor());
            transaction.setUom(utilityAssetDetail.getUom());
            transaction.setTagNumber(utilityAssetDetail.getAssetBarcode());
            transaction.setJobno(utilityAssetDetail.getJobno());
            transaction.setOpco(utilityAssetDetail.getOpco());
            transaction.setRefno(refNo);
            transaction.setCreatedBy(getUseID());
            transaction.setGpsLatitude(gps.getLatitude()+"");
            transaction.setGpsLongitude(gps.getLongitude()+"");

            AppUtils.showProgressDialog(mActivity,"",false);
            new UtilityRepository(mActivity,this).saveUtilityTransaction(transaction);

        }
        else{

            AppUtils.showErrorToast(mActivity,"Readings should not contain alphabetic characters");

        }

    }

    public String getReferenceNo(){

        return (utilityAssetDetail.getAssetBarcode()+"-"+new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()));

    }

    public String getUseID(){

        String mStrEmpId="";
        Gson gson = new Gson();
        SharedPreferences mPreferences = mActivity.getSharedPreferences(AppUtils.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor  mEditor = mPreferences.edit();
        String  mStringJson = mPreferences.getString(AppUtils.SHARED_LOGIN, null);
        if (mStringJson != null) {
            Login user = gson.fromJson(mStringJson, Login.class);
            mStrEmpId = user.getEmployeeId();
        }
        return mStrEmpId;

    }


    @Override
    public void onReceiveUtilityDetails(List<UtilityAssetDetail> object) {

    }

    @Override
    public void onReceiveFailureUtilityDetails(String err) {

    }

    @Override
    public void onSuccessSaveUtilityReading(String strMsg, int mode) {

        if(filePath!=null){
            submitImage("B");
        }
        else{
            AppUtils.hideProgressDialog();
            showDialog(mActivity,"Record saved successfully.","Reference Number : "+refNo);
        }

    }

    @Override
    public void onFailureSaveUtilityReading(String strErr, int mode) {
        AppUtils.hideProgressDialog();
        AppUtils.showErrorToast(mActivity,strErr);
    }


    public  void showDialog(Context context, String strTitle, String StrMsg) {
        try {

            MaterialDialog.Builder builder =
                    new MaterialDialog.Builder(context)
                            .content(StrMsg)
                            .title(strTitle)
                            .positiveText(R.string.lbl_okay)
                            .stackingBehavior(StackingBehavior.ADAPTIVE)
                            .onPositive(
                                    new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(
                                                @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();

                                            FragmentManager fm = getActivity().getSupportFragmentManager();
                                            for (int i = 0; i < 3; ++i) {
                                                fm.popBackStack();
                                            }

                                        }
                                    });

            MaterialDialog dialog = builder.build();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    @Override
    public void onImageSaveReceivedSuccess1(RCDownloadImage imageEntity, int mode) {
        AppUtils.hideProgressDialog();
        showDialog(mActivity,"Record saved successfully.","Reference Number : "+refNo);
    }

    @Override
    public void onImageSaveReceivedSuccess(DefectDoneImageUploaded mImageEntity, int mode) {

    }

    @Override
    public void onImageReceivedSuccess(RCDownloadImage imageEntity, int mode) {

    }

    @Override
    public void onImageSaveReceivedFailure(String strErr, int mode) {
        AppUtils.hideProgressDialog();
        AppUtils.showDialog(mActivity, strErr);
    }

    @Override
    public void onImageReceivedFailure(String strErr, int mode) {

    }

    @Override
    public void onAllImagesReceived(List<DFoundWDoneImageEntity> mImageEntities, int mode) {

    }

    private void submitImage( final String docType) {

        try {

            new AsyncTask<Void, DFoundWDoneImageEntity, DFoundWDoneImageEntity>() {
                @Override
                protected void onPreExecute() {

                  //  AppUtils.showProgressDialog(mActivity, getString(R.string.lbl_loading), false);
                    super.onPreExecute();
                }

                @Override
                protected DFoundWDoneImageEntity doInBackground(Void... params) {

                    DFoundWDoneImageEntity imageEntity = new DFoundWDoneImageEntity("" + docType);
                    imageEntity.setOpco(utilityAssetDetail.getOpco());
                    imageEntity.setFileType("jpg");
                    imageEntity.setTransactionType("U");
                    imageEntity.setDocType("UTILITY READING");
                    imageEntity.setRefDocNo(refNo);
                    imageEntity.setCreatedBy(getUseID());
                    imageEntity.setModifiedBy(getUseID());
                    imageEntity.setImageCount(0);
                    imageEntity.setGeneralRefNo(utilityAssetDetail.getAssetBarcode());
                    imageEntity.setBase64Image(AppUtils.getEncodedString(mSelectedImage));
                    return imageEntity;
                }

                @Override
                protected void onPostExecute(DFoundWDoneImageEntity imageEntity) {
                    postImageToServer(imageEntity);
                    super.onPostExecute(imageEntity);
                }
            }.execute();

        } catch (Exception e) {
            AppUtils.hideProgressDialog();
            e.printStackTrace();
        }
    }

    private void postImageToServer(DFoundWDoneImageEntity saveRequest) {

        if (checkInternet(getContext())) {
            receiveComplaintRespond_service.saveComplaintRespondImageData1(saveRequest, getActivity());
        }
    }

}
