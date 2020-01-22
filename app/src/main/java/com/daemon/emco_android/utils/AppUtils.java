package com.daemon.emco_android.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import androidx.annotation.NonNull;

import com.daemon.emco_android.BuildConfig;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.daemon.emco_android.R;
import com.daemon.emco_android.ui.components.gifloadinglibrary.GifLoadingView;
import com.daemon.emco_android.listeners.DatePickerDialogListener;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Created by subbu on 7/7/17. */
public class AppUtils extends Dialog {

  public static final int TIMEOUT = 90000;

  public static final int TIMEOUT_NEW = 21600000;

  public static GifLoadingView mGifLoadingView;

  // list of Domain URL
   // public static CharSequence[] serverurls = { "my.agfacilities.com:8585","cafm.agfacilities.com","cafm.mbm.ae","10.111.111.41:8080", "10.111.111.37:8585","10.112.112.106:8080"};


  public static CharSequence[] serverurls= BuildConfig.URL_ARRAY;
  // public static CharSequence[] serverurls = { "my.agfacilities.com:8585","cafm.agfacilities.com","cafm.mbm.ae"};

    // 21600000pref

  // Room database variables
  public static final String EMCO_DATABASE = "emco_database";
  // Shared preference variables
  public static final String SHARED_PREFS = "shared_preference";
  public static final String SHARED_LOGIN = "shared_login_data";
  public static final String SHARED_BASEURL = "shared_base_url";
  public static final String SHARED_BASEURL_FILE = "shared_base_url_file";
  public static final String WORK_STATUS = "work_status_new";
  public static final String FILE_SIZE_DATA = "share_filezise_data";
  public static final String ARGS_PPM_FEEDBACK_CHECK = "arg_rc_feedback_ppm_check";
  public static final String SHARED_LOGIN_OFFLINE = "shared_login_data_offline";
  public static final String SHARED_CUSTOMER_REMARKS = "shared_customer_remarks_data";
  public static final String SHARED_CUSTOMER_LC = "shared_customer_logcomplaint_data";
  public static final String SHARED_TECH_REMARKS = "shared_tech_remarks_data";
  public static final int MODE_LOCAL = 111;
  public static final String SHARED_PPM_FILTER = "shared_ppm_filter";
  public static final String SHARED_DOC_FILTER = "shared_doc_filter";
  public static final int MODE_SERVER = 000;
  public static final int MODE_INSERT = 2000;
  public static final int MODE_INSERT_SINGLE = 20001;
  public static final int MODE_UPDATE = 2001;
  public static final int MODE_GET = 2002;
  public static final int MODE_GETALL = 2003;
  public static final int MODE_DELETE = 2004;
  public static final int MODE_FROMDATE = 2005;
  public static final int MODE_TODATE = 2006;
  public static final String MATERIALREQUIREDCODE = "MR";
  public static final String OTHERS = "OTH";
  public static final String PENDING = "P";
  public static final String COMPLETED = "C";
  public static final String FORWARDED = "F";
  public static final String RESPONDED = "R";
  public static final String REQUIRED = "Required";
  public static final String USED = "Used";
  public static final String mRESPONDED = "Responded";
  public static final String CLOSEWITHSIGN = "Close With Signature";
  public static final String CLOSEWITHOUTSIGN = "Close Without Signature";

  // Reactive maintenance dashboard pie data
  public static final String ARGS_REACTIVE_MAINTENANCE_DASHBOARD_DATA =
      "args_reactive_maintenance_dashboard_data";
  public static final String ARGS_REACTIVE_MAINTENANCE_DASHBOARD_REQUEST =
      "args_reactive_maintenance_dashboard_request";
  public static final String ARGS_BIECHART_ZONETITLE = "args_biechart_zonetitle";

  // Args variables
  public static final String ARGS_SEARCH_COMPLAINT_RESULT = "arg_search_complaint_result";
  public static final String ARGS_MULTISEARC_COMPLAINT_LIST = "arg multi search complaint list";
  public static final String ARGS_MULTISEARC_COMPLAINT_REQUEST =
      "arg multi search complaint request";
  public static final String ARGS_MULTISEARC_COMPLAINT_REQUEST_DASHBOARD =
      "arg complaint request dashboard";
  public static final String ARGS_MULTISEARC_COMPLAINT_TOTALPAGE =
      "arg multi search complaint total pages";
  public static final String ARGS_RECEIVECOMPLAINTUNSIGNED_PAGE = "arg_receivecomplaintunsigned";
  public static final String ARGS_RECEIVECOMPLAINT_PAGE = "arg_receivecomplaint";
  public static final String ARGS_RCLIST_F = "args_rclist_f";
  public static final String ARGS_RCLIST_C = "args_rclist_c";
  public static final String ARGS_RECEIVECOMPLAINT_PAGETYPE = "arg_receivecomplaint_type";
  public static final String ARGS_MATERIAL_PAGE_STRING = "arg_material_string";
  public static final String ARGS_MATERIAL_REQUIRED_STRING = "arg_material_required_string";

  public static final String ARGS_IM_PPE_Page = "arg_im_ppe_page_string";
  public static final String ARGS_IM_SERVICES_Page_TITLE = "arg_im_services_page_title";
  public static final String ARGS_IM_HARD_Page = "arg_im_hard_page_string";
  public static final String ARGS_IM_HARD_Page_TITLE = "arg_im_hard_page_title";
  public static final String ARGS_IM_RANDOM_PAGE = "arg_im_random_page_string";
  public static final String ARGS_IM_RANDOM_PAGE_TITLE = "arg_im_random_page_title";
  public static final String ARGS_PPM_FEEDBACK = "arg_rc_feedback_ppm";
  public static final String ARGS_MATERIAL_USED_STRING = "arg_material_used_string";
  public static final String ARGS_RECEIVEDCOMPLAINT_ITEM_DETAILS = "arg_rc_item_details";
  public static final String ARGS_RECEIVEDCOMPLAINT_ITEM_POSITION = "arg_rc_item_position";
  public static final String ARGS_RECEIVEDCOMPLAINT_VIEW_DETAILS = "arg_rc_view_details";
  public static final String ARGS_FEEDBACK_VIEW_DETAILS = "arg_pm_feedback";
  public static final String ARGS_RISKASSES_LIST = "arg_risk_asses_list";
  public static final String ARGS_TOOL_LIST = "arg_tool_list";
  public static final String ARG_PPE_PPM = "arg_ppe_ppm";
  public static final String ARGS_RCDOWNLOADIMAGE = "args_rcdownloadimage";
  public static final String ARGS_RECEIVEDCOMPLAINT_ASSET_DETAILS = "arg_rc_asset_details";
  public static final String ARGS_IMAGE_LIST = "ARGS_IMAGE_LIST";
  public static final String ARGS_IMAGE_LIST_DFECT_FOUND = "ARGS_IMAGE_LIST_DFECT_FOUND";
  public static final String ARGS_IMAGE_LIST_WORK_DONE = "ARGS_IMAGE_LIST_WORK_DONE";
  public static final String ARG_RC_LIST = "arg_rc_list_details";
  public static final String TAG_FRAGMNENT_VIEW_LANGUAGE = "tag_view_language";
  public static final String TAG_FRAGMNENT_MAIN = "tag_mainview";
  public static final String IS_NETWORK_AVAILABLE = "is_network_available";
  public static final String NETWORK_AVAILABLE = "network_available";
  public static final String NETWORK_NOT_AVAILABLE = "network_not_available";
  public static final String TECHNISION = "Technician";
  public static final String CUSTOMER = "Customer";
  public static final String SUPERVISOR = "Supervisor";
  public static final String ALLFORWARDEDCOMPLAINT = "AllForwardedComplaint";

  public static final String LABEL_BOOKMARKS_ADDED = "LABEL_BOOKMARKS_ADDED";
  public static final int CAMERA_FACING_BACK = Camera.CameraInfo.CAMERA_FACING_BACK;

  @SuppressLint("InlinedApi")
  public static final int CAMERA_FACING_FRONT = Camera.CameraInfo.CAMERA_FACING_FRONT;

  public static final int[] MATERIAL_COLORS = {
    rgb("#044092"), rgb("#3FB5A3"), rgb("#646569"), rgb("#FF0000"), rgb("#388E3C"),
    rgb("#49b6d6"), rgb("#ff4800"), rgb("#ffb300"), rgb("#00FFFF"), rgb("#00FF00"),
    rgb("#FF007F"), rgb("#FF7F00"), rgb("#FFFF00"), rgb("#7FFF00"), rgb("#007FFF"),
    rgb("#0000FF"), rgb("#7F00FF"), rgb("#ff00ff")
  };
  public static final String SHARED_CVIEW_REMARKS = "complaintviewR";
  private static final String MODULE = "AppUtils";
  private static final String EMAIL_PATTERN =
      "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
  public static int SHARED_INT_DIALOG_PICKER = 1400;
  public static String SHARED_DIALOG_PICKER = "Shared_Dialog_Picker";
  public static String ARG_SELECTED_POSITION = "arg_rc_list_selected_position";
  public static File root = android.os.Environment.getExternalStorageDirectory();
  public static String RootPath = "/Android/data/com.daemon.emco_android";
  // SD card image directory
  public static final String PHOTO_ALBUM = AppUtils.root.getAbsolutePath() + AppUtils.RootPath;
  public static String mandatory = "<font color='#D7164B'>*</font>";
  public static String ARGS_PPMDetails_List = "ARGS_PPMDetails_List";
    public static String ARGS_PPMFILTER = "ARGS_PPMFILTER";

  public static String ARGS_SUPPORTDOC = "ARGS_SUPPORTDOC";

  public static String ARGS_FILTERTYPE = "ARGS_FILTERTYPEARGS_FILTERTYPE";

  public static String ARGS_DOCTRANS = "ARGS_DOCTRANS";

  public static String ARGS_DOCCOUNT = "ARGS_DOCCOUNT";

  public static String ARGS_SURVEYQUES = "ARGS_SURVEYQUES";

  public static String ARGS_SURVEYTRANS = "ARGS_SURVEYTRANS";

  public static String ARGS_SURVEYTYPE = "ARGS_SURVEYTYPE";

  public static String ARGS_PPMSCHEDULEDOCBY = "ARGS_PPMSCHEDULEDOCBY";
  public static String ARGS_PPMSCHEDULEDOCBYREQUEST = "ppmScheduleDocByRequest";
  static Context mContext;
  static MaterialDialog progressDialog;
  static Calendar newCalendar = Calendar.getInstance();
  static String strDate = null;
  static DatePickerDialogListener mCallback = null;
  private static String TAG = AppUtils.class.getSimpleName();
  private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);

  public AppUtils(@NonNull Context context) {
    super(context);
    this.mContext = context;
  }

  public static void showDialog(Context context, String strTitle, String StrMsg) {
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
                    }
                  });

      MaterialDialog dialog = builder.build();
      dialog.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void showDialog(final Context context, String StrMsg) {
    try {
      MaterialDialog.Builder builder =
          new MaterialDialog.Builder(context)
              .content(StrMsg)
              .positiveText(R.string.lbl_okay)
              .stackingBehavior(StackingBehavior.ADAPTIVE)
              .onPositive(
                  new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(
                        @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                      dialog.dismiss();
                    }
                  });

      MaterialDialog dialog = builder.build();
      dialog.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static  void showSnackBar(int v,View rootView, String msg){

      Snackbar.make(rootView.findViewById(v), msg, Snackbar.LENGTH_LONG)
              .setAction("OK", new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                  }
              })
              .show();
  }

  public static  void showToast(Activity mact, String msg){

    Toast.makeText(mact,msg,Toast.LENGTH_SHORT).show();
  }


  public static void showProgressDialog(
      AppCompatActivity mActivity, String Str_Msg, boolean setCancelable) {
    try {

      mGifLoadingView = new GifLoadingView();
      mGifLoadingView.setBlurredActionBar(true);
      mGifLoadingView.setDimming(true);
      mGifLoadingView.setRadius(1);
      mGifLoadingView.setDownScaleFactor( 2.0f);
    //  mGifLoadingView.setImageResource(R.drawable.gear);
      mGifLoadingView.setImageResource(R.drawable.gear);
      mGifLoadingView.show(mActivity.getFragmentManager());

//      progressDialog =
//          new MaterialDialog.Builder(mActivity)
//              .title(Str_Msg)
//              .progress(true, 0)
//              .cancelable(setCancelable)
//              .progressIndeterminateStyle(true)
//              .show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public static void showProgressDialog2(
          AppCompatActivity mActivity, String Str_Msg, boolean setCancelable) {
    try {

      mGifLoadingView = new GifLoadingView();
      mGifLoadingView.setBlurredActionBar(true);
      mGifLoadingView.setDimming(true);
      mGifLoadingView.setRadius(1);
      mGifLoadingView.setDownScaleFactor( 2.0f);
      //  mGifLoadingView.setImageResource(R.drawable.gear);
      mGifLoadingView.setImageResource(R.drawable.searchgif);
      mGifLoadingView.show(mActivity.getFragmentManager());

//      progressDialog =
//          new MaterialDialog.Builder(mActivity)
//              .title(Str_Msg)
//              .progress(true, 0)
//              .cancelable(setCancelable)
//              .progressIndeterminateStyle(true)
//              .show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }



  public static void showProgressDialog(
      AppCompatActivity mActivity, String Str_Title, String Str_Msg, boolean setCancelable) {
    try {
//      progressDialog =
//          new MaterialDialog.Builder(mActivity)
//              .title(Str_Title)
//              .content(Str_Msg)
//              .progress(true, 0)
//              .cancelable(setCancelable)
//              .progressIndeterminateStyle(true)
//              .show();

      mGifLoadingView = new GifLoadingView();
      mGifLoadingView.setBlurredActionBar(true);
      mGifLoadingView.setDimming(true);
      mGifLoadingView.setRadius(1);

      mGifLoadingView.setDownScaleFactor( 2.0f);
      mGifLoadingView.setImageResource(R.drawable.gear);
      mGifLoadingView.show(mActivity.getFragmentManager());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void hideProgressDialog() {
    try {

      mGifLoadingView.dismiss();

//      if (progressDialog == null) {
//      } else {
//        progressDialog.hide();
//        progressDialog.dismiss();
//      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static int getIdForRequestedCamera(int facing) {
    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    for (int i = 0; i < Camera.getNumberOfCameras(); ++i) {
      Camera.getCameraInfo(i, cameraInfo);
      if (cameraInfo.facing == facing) {
        return i;
      }
    }
    return -1;
  }

  public static boolean validateEmail(String email) {
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public static boolean isURL(String url) {
    if (url == null) {
      return false;
    }
    // Assigning the url format regular expression
    String urlPattern =
        "^(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
    return url.matches(urlPattern);
  }

  // time zone formate "yyyy-MM-dd'T'HH:mm:ss.ZZZZ"
  public static String getDateTime() {
    try {
      SimpleDateFormat simpleDateFormat =
          new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
      Date date = new Date();
      Log.d(TAG, "getDateTime :" + simpleDateFormat.format(date));
      return simpleDateFormat.format(date);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  // time zone formate "yyyy-MM-dd'T'HH:mm:ss.ZZZZ"
  public static String getDateTimeChange(String inputString) {
    String outputString = inputString;
    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
    Date date = null;
    try {
      date = inputFormat.parse(inputString);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    // Format date into output format
    DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
    outputString = outputFormat.format(date);
    return outputString;
  }

  // time zone formate "yyyy-MM-dd'T'HH:mm:ss.ZZZZ"
  public static String getDateTime(String inputString) {
    String outputString = inputString;
    try {
      // Convert input string into a date
      /*DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
      Date date = inputFormat.parse(inputString);*/

      DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
      Date date = inputFormat.parse(inputString);



      // Format date into output format
      DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
      outputString = outputFormat.format(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Log.d(TAG, "getDateTime :" + outputString);
    return outputString;
  }
  // 2018-03-11 00:00:00.0
  // time zone formate "yyyy-MM-dd'T'HH:mm:ss.ZZZZ"
  public static String getDateTimeNew(String inputString) {
    String outputString = inputString;
    try {
      // Convert input string into a date
      DateFormat inputFormat = new SimpleDateFormat("yyyy-mmm-dd HH:mm:ss");
      Date date = inputFormat.parse(inputString);

      // Format date into output format
      DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
      outputString = outputFormat.format(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Log.d(TAG, "getDateTime :" + outputString);
    return outputString;
  }

  public static String getCurrentYear() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
    Date date = new Date();
    Log.d(TAG, "getDateTime :" + simpleDateFormat.format(date));
    return simpleDateFormat.format(date);
  }

  public static String getUniqueLogComplaintNo() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault());
    Date date = new Date();
    Log.d(TAG, "getUniqueLogComplaintNo :" + simpleDateFormat.format(date));
    return simpleDateFormat.format(date);
  }

  public static Object fromJson(String jsonString, Type type) {
    return new Gson().fromJson(jsonString, type);
  }

  public static boolean IsFileExist(File dir, File file) {
    TAG = "IsFileExist";
    Log.d(MODULE, TAG);

    boolean RetValue = false;
    File[] Files_Array = dir.listFiles();
    if (Files_Array != null) {
      if (Files_Array.length > 0) {
        for (int i = 0; i < Files_Array.length; i++) {
          if (file.equals(Files_Array[i])) {
            RetValue = true;
          }
        }
      }
    }
    return RetValue;
  }

  public static void saveImage(Bitmap photo, AppCompatActivity mActivity, String Str_Name) {
    File sdIconStorageDir = new File(getProfilePicturePath(mActivity));
    sdIconStorageDir.mkdirs();
    try {
      String filePath = sdIconStorageDir.toString() + "/" + Str_Name + ".png";
      FileOutputStream fileOutputStream = new FileOutputStream(filePath);
      BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
      photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
      bos.flush();
      bos.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      Log.d("TAG", "Error saving image file: " + e.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
      Log.d("TAG", "Error saving image file: " + e.getMessage());
    }
  }

  public static String getProfilePicturePath(Context context) {
    String profilePicturePath = AppUtils.PHOTO_ALBUM;
    return profilePicturePath;
  }

  public static Bitmap getScaledBitmap(Bitmap bitmap, int newWidth, int newHeight) {
    int width = bitmap.getWidth();
    int height = bitmap.getHeight();
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;

    // CREATE A MATRIX FOR THE MANIPULATION
    Matrix matrix = new Matrix();
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight);

    // RECREATE THE NEW BITMAP
    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    return resizedBitmap;
  }

  public static void datePickerDialog(
      AppCompatActivity mActivity, DatePickerDialogListener listener, Date minDate, Date maxDate) {
    mCallback = listener;
    /* final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",
    Locale.getDefault());*/
    final SimpleDateFormat simpleDateFormat =
        new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

    DatePickerDialog datePickerDialog =
        new DatePickerDialog(
            mActivity,
            new DatePickerDialog.OnDateSetListener() {

              public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                strDate = (simpleDateFormat.format(newDate.getTime()));
                Log.d(TAG, "datePickerDialog " + strDate);
                mCallback.onDateReceivedSuccess(strDate);
              }
            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH));
    if (minDate != null) datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
    if (maxDate != null) datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
    datePickerDialog.show();
  }

  public static void datePickerDialogNew(
      AppCompatActivity mActivity, DatePickerDialogListener listener, Date minDate, Date maxDate) {
    mCallback = listener;
    /*  final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",
    Locale.getDefault());*/
    final SimpleDateFormat simpleDateFormat =
        new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

    DatePickerDialog datePickerDialog =
        new DatePickerDialog(
            mActivity,
            new DatePickerDialog.OnDateSetListener() {

              public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                strDate = (simpleDateFormat.format(newDate.getTime()));
                Log.d(TAG, "datePickerDialog " + strDate);
                mCallback.onDateReceivedSuccess(strDate);
              }
            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH));
    if (minDate != null) datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
    if (maxDate != null) datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
    datePickerDialog.show();
  }

  public static DisplayImageOptions getOptions() {
    DisplayImageOptions options =
        new DisplayImageOptions.Builder()
            .displayer(new RoundedBitmapDisplayer(0))
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.NONE)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageOnLoading(R.drawable.ic_camera_24dp)
            .showImageForEmptyUri(R.drawable.ic_camera_24dp)
            .showImageOnFail(R.drawable.ic_camera_24dp)
            .resetViewBeforeLoading(true)
            .considerExifParams(true)
            .build();
    return options;
  }

  public static void closeInput(final View caller) {
    try {
      caller.postDelayed(
          new Runnable() {
            @Override
            public void run() {
              InputMethodManager imm =
                  (InputMethodManager)
                      caller.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
              imm.hideSoftInputFromWindow(
                  caller.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
          },
          100);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void setErrorBg(TextView view, boolean error) {
    Log.d(TAG, "setErrorBg " + error);
    if (error) {
      view.setBackgroundResource(R.drawable.bg_border_red_spinner);
    } else {
      view.setBackgroundResource(R.drawable.bg_border_spinner);
    }
  }

  /**
   * Converts the given hex-color-string to rgb.
   *
   * @param hex
   * @return
   */
  public static int rgb(String hex) {
    int color = (int) Long.parseLong(hex.replace("#", ""), 16);
    int r = (color >> 16) & 0xFF;
    int g = (color >> 8) & 0xFF;
    int b = (color >> 0) & 0xFF;
    return Color.rgb(r, g, b);
  }

  public static boolean checkImageFileSize(Bitmap bitmap) {
    TAG = "checkImageFileSize";
    Log.w(MODULE, TAG);
    try {
      if (bitmap != null) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        long length = byteArray.length;
        length = length / 1024;
        TAG = "image size " + length + " KB";
        Log.w(MODULE, TAG);
        return length <= 2000;
      } else {
        return false;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public static String getEncodedString(Bitmap bitmap) {
    TAG = "getEncodedString";
    Log.w(MODULE, TAG);
    try {

      int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
      Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      scaled.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
      byte[] byteArray = byteArrayOutputStream.toByteArray();
      String Str_Encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//      String resize = resizeBase64Image(Str_Encoded);
//      if (resize.length() > 13000) {
//        resize = resizeBase64ImageChange(Str_Encoded);
//      }
      // TAG = "getEncodedString size" + Str_Encoded.length();
      // System.out.println(resize.length()+"aaaaaaaaas"+Str_Encoded.length());
      Log.w(MODULE, TAG);
      return Str_Encoded;
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static String resizeBase64Image(String base64image) {
    byte[] encodeByte = Base64.decode(base64image.getBytes(), Base64.DEFAULT);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inPurgeable = true;
    Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);
    if (image.getHeight() <= 400 && image.getWidth() <= 400) {
      return base64image;
    }
    image = Bitmap.createScaledBitmap(image, 350, 250, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] b = baos.toByteArray();
    System.gc();
    return Base64.encodeToString(b, Base64.NO_WRAP);
  }

  public static String resizeBase64ImageChange(String base64image) {
    byte[] encodeByte = Base64.decode(base64image.getBytes(), Base64.DEFAULT);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inPurgeable = true;
    Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);
    if (image.getHeight() <= 400 && image.getWidth() <= 400) {
      return base64image;
    }
    image = Bitmap.createScaledBitmap(image, 300, 220, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] b = baos.toByteArray();
    System.gc();
    return Base64.encodeToString(b, Base64.NO_WRAP);
  }

  public static Bitmap getDecodedString(String strBase64) {
    try {
      byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
      Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
      return decodedByte;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  /*public static String convertToJson(List<Response> listLanguages)
  {
      Log.d(TAG,"convertToJson");
      Gson gson = new GsonBuilder().create();
      JsonArray languageArray = gson.toJsonTree(listLanguages).getAsJsonArray();

      Log.d(TAG,"response json array :"+ languageArray.toString());
      return languageArray.toString();
  }

  //conversion of json string to language list
  public static List<Response> convertJsonToResponseList(String jsonArray)
  {
      Log.d(TAG,"convertJsonToResponseList");

      Type listType = new TypeToken<List<Response>>() {}.getType();
      List<Response> listResponseLanguages = new Gson().fromJson(jsonArray.toString(), listType);

      //Log.d(TAG,"response collection array :"+ listResponseLanguages.get(0).getCategory());
      Log.d(TAG,"converting");
      return listResponseLanguages;
  }

  //conversion of json string to language list
  public static List<Languages> convertJsonToList(String jsonArray)
  {
      Log.d(TAG,"convertJsonToList");

      Type listType = new TypeToken<List<Languages>>() {}.getType();
      List<Languages> listLanguages = new Gson().fromJson(jsonArray.toString(), listType);

      return listLanguages;
  }*/

  public static boolean checkEmptyTv(TextView tv) {
    try {

      if(tv.getText().toString()!=null){

        if(!tv.getText().toString().equalsIgnoreCase("")) {
          return false;
        }
        else{
          return true;
        } }
      else{
      return true;
      }

    } catch (Exception e) {
      e.printStackTrace();
      return true;
    }
  }

  public static boolean checkEmptyEdt(EditText ed) {
    try {

      if(ed.getText().toString()!=null){

        if(!ed.getText().toString().equalsIgnoreCase("")) {
          return false;
        }
        else{
          return true;
        } }
      else{
        return true;
      }

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }


  public static String getGreeting(){

    Calendar c = Calendar.getInstance();
    int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

    if(timeOfDay >= 0 && timeOfDay < 12){
      return "Good Morning";
    }else if(timeOfDay >= 12 && timeOfDay < 16){
      return "Good Afternoon";
    }else if(timeOfDay >= 16 && timeOfDay < 21){
      return "Good Evening";
    }else if(timeOfDay >= 21 && timeOfDay < 24){
      return "Good Night";
    }
    else{
      return "";
    }

  }

}
