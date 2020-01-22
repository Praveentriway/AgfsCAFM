package com.daemon.emco_android.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.daemon.emco_android.ui.activities.BarcodeCaptureActivity.BarcodeObject;

/**
 * Created by Daemonsoft on 9/7/2017.
 */

public class ZxingScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static String TAG = ZxingScannerActivity.class.getSimpleName();
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        Intent data = new Intent();
        data.putExtra(BarcodeObject, rawResult.getText());
        setResult(CommonStatusCodes.SUCCESS, data);
        finish();
        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
    }
}