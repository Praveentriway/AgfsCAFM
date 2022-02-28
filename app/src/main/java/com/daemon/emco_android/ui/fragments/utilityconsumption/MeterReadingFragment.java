package com.daemon.emco_android.ui.fragments.utilityconsumption;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.emco_android.R;
import com.daemon.emco_android.model.common.UtilityAssetDetail;
import com.daemon.emco_android.utils.AppUtils;
import com.daemon.emco_android.utils.meterReading.Helpers.BitmapWriter;
import com.daemon.emco_android.utils.meterReading.ImageProcessing.BitmapProcessor;
import com.daemon.emco_android.utils.meterReading.Objects.RectLocation;
import com.daemon.emco_android.utils.meterReading.Views.CameraPreview;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

import static com.daemon.emco_android.utils.AppUtils.ARGS_UTILITY_ASSET;
import static com.daemon.emco_android.utils.AppUtils.METER_READING_EXTRA_FILEPATH;
import static com.daemon.emco_android.utils.Utils.TAG_FRAGMENT_UTILITY_READING;


public class MeterReadingFragment extends Fragment {

    private CameraManager mCameraManager;
    private boolean flashOn=true;
    private String mCameraId;
    private Camera mCamera;
    private CameraPreview mPreview;
    private SurfaceView transparentView;
    private SurfaceHolder holderTransparent;
    private AppCompatActivity mActivity;
    RectLocation cropLocation = new RectLocation();
    UtilityAssetDetail utilityAssetDetail;
    FrameLayout preview;
    private static final String LOG_TAG = "MeterReadingFragment";
View view;
    ProgressDialog pd;
    FloatingActionButton fab_record;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();
     //   mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getArguments() != null) {
            utilityAssetDetail = (UtilityAssetDetail) getArguments().getSerializable(ARGS_UTILITY_ASSET);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view=inflater.inflate(R.layout.fragment_meter_reading, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /**
         * Removing title bar
         */

        /**
         * A transparent surface view for showing the crop rectangle
         */
        transparentView = (SurfaceView) view.findViewById(R.id.TransparentView);
        transparentView.setZOrderOnTop(true);
        holderTransparent = transparentView.getHolder();
        holderTransparent.setFormat(PixelFormat.TRANSPARENT);
        holderTransparent.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        fab_record=(FloatingActionButton) view.findViewById(R.id.fab_record);

        /**
         * Setting up the Camera preview
         */
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(getContext(), mCamera);
        preview = (FrameLayout) view.findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        preview.setOnTouchListener(onTouchListener);

        /**
         * Splash animation
         */
        TextView iv = (TextView) view.findViewById(R.id.iv_splash);
        AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(1500);
        animation1.setStartOffset(1000);
        animation1.setFillAfter(true);
        iv.startAnimation(animation1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DrawFocusRect();
            }
        }, 2800);


        fab_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

    }

    public void showNoFlashError() {
        AlertDialog alert = new AlertDialog.Builder(getContext())
                .create();
        alert.setTitle("Oops!");
        alert.setMessage("Flash not available in this device...");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }


    private void DrawFocusRect() {
        try {
            /**
             * Locking the canvas
             */
            Canvas canvas = holderTransparent.lockCanvas();
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            /**
             * Preparing the paint brush for the rectangle
             */
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(getResources().getColor(R.color.green));
            paint.setStrokeWidth(3);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            int canvasW = width;
            int canvasH = height;
            Point centerOfCanvas = new Point(canvasW / 2, canvasH / 2);
            int rectW = 500;
            int rectH = 150;
            int left = centerOfCanvas.x - (rectW / 2);
            int top = centerOfCanvas.y - (rectH / 2);
            int right = centerOfCanvas.x + (rectW / 2);
            int bottom = centerOfCanvas.y + (rectH / 2);

            cropLocation.setLeft_top_x(left);
            cropLocation.setLeft_top_y(top);
            cropLocation.setRight_bottom_x(right);
            cropLocation.setRight_bottom_y(bottom);

            Rect rect = new Rect(left, top, right, bottom);
            canvas.drawRect(rect, paint);

//            int x = 50;
//            int y = 50;
//            int sideLength = 200;
//
//            // create a rectangle that we'll draw later
//            Rect  rectangle = new Rect(x, y, sideLength, sideLength);
//            canvas.drawRect(rectangle, paint);

//            /**
//             * Drawing the rectangle
//             */
//            Log.d("Drawing rectangle","draw()");
//            canvas.drawRect(location.getLeft_top_x()
//                    , location.getLeft_top_y()
//                    , location.getRight_bottom_x()
//                    , location.getRight_bottom_y()
//                    , paint);

            /**
             * Unlocking the canvas
             */
            holderTransparent.unlockCanvasAndPost(canvas);
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Error in drawing");
        }
    }


    /**
     * On touch listener for the camera preview. This gives coordinates for drawing the rectangle.
     */
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {

//        float RectLeft = 0, RectTop = 0, RectRight = 0, RectBottom = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    /**
                     * Setting the coordinates of the rectangle on initial touch
                     */
//                    cropLocation.setLeft_top_x(event.getX());
//                    cropLocation.setLeft_top_y(event.getY());
//                    cropLocation.setRight_bottom_x(event.getX());
//                    cropLocation.setRight_bottom_y(event.getY());

                    /**
                     * Attempting to focus the camera on initial touch
                     */
                    try {
                        mCamera.autoFocus(null);
                    } catch (Throwable e) {
                        Log.e(LOG_TAG, "Auto focus failed");
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    /**
                     * Attempt to capture image when touch is released
                     */

                    break;
                case MotionEvent.ACTION_MOVE:
                    /**
                     * Set the new right-bottom-x and right-bottom-y locations when touch is dragged
                     */

//                    cropLocation.setRight_bottom_x(event.getX());
//                    cropLocation.setRight_bottom_y(event.getY());
                    break;
            }
            /**
             * Call the drawing method for the rectangle
             */

            //  DrawFocusRect(cropLocation);
            return true;
        }
    };


    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void loadFragment(final Fragment fragment, UtilityAssetDetail utilityAssetDetail, final String tag,String filePath) {

        Bundle mdata = new Bundle();
        mdata.putSerializable(AppUtils.ARGS_UTILITY_ASSET,utilityAssetDetail);
        mdata.putString(METER_READING_EXTRA_FILEPATH,filePath);
        fragment.setArguments(mdata);
        FragmentTransaction fragmentTransaction =
                mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public void takePicture(){
        try {
            preview.setOnTouchListener(null);
            mCamera.takePicture(null, null, mPicture);
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Capture failed");
        }
    }

    /**
     * Callback after the image capture
     */
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            try {
                pd = ProgressDialog.show(mActivity, "Processing image", "Please wait...", true);
            } catch (Throwable ignored) {
            }
            new AsyncTask<Void, Void, Boolean>() {

                private RectLocation normLoc;
                private File pictureFile;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    normLoc = cropLocation.normalize(transparentView.getWidth(), transparentView.getHeight());
                }

                @Override
                protected Boolean doInBackground(Void... params) {
                    /**
                     * Decoding byte array to bitmap
                     */
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

                    /**
                     * Processing bitmaps and writing to file
                     */
                    BitmapWriter.write(BitmapProcessor.process(bmp, normLoc, BitmapProcessor.TYPE_NONE), "NONE");    //TODO ONLY FOR COMPARISON SAKE
                    BitmapWriter.write(BitmapProcessor.process(bmp, normLoc, BitmapProcessor.TYPE_OTSU), "OTSU");    //TODO ONLY FOR COMPARISON SAKE
                    pictureFile = BitmapWriter.write(BitmapProcessor.process(bmp, normLoc, BitmapProcessor.TYPE_NONE), "ADAP");

                    bmp.recycle();

                    return pictureFile != null;
                }

                @Override
                protected void onPostExecute(Boolean success) {
                    super.onPostExecute(success);
                    try {
                        pd.dismiss();
                    } catch (Throwable ignored) {
                    }

                    if (success) {
                        /**
                         * Passing on the cropped image path to the Result Activity
                         */

                        loadFragment(new MeterEntryFragment(),utilityAssetDetail,TAG_FRAGMENT_UTILITY_READING,pictureFile.getAbsolutePath());
//                        Intent i = new Intent(MainActivity.this, ResultActivity.class);
//                        i.putExtra(ResultActivity.EXTRA_FILEPATH, pictureFile.getAbsolutePath());
//                        startActivity(i);
//                        finish();
                    } else {
                        Toast.makeText(getContext(), "Failed to capture. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    };


}



