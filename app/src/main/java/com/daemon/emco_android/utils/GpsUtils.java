package com.daemon.emco_android.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

public class GpsUtils {


    public static String getSerialNumber() {
        String serialNumber;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serialNumber = (String) get.invoke(c, "gsm.sn1");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ro.serialno");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "sys.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = Build.SERIAL;
            // If none of the methods above worked
            if (serialNumber.equals(""))
                serialNumber = null;
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }
        return serialNumber;
    }



    public static boolean isLocationEnabled(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) mContext.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }


    public static String getDeviceID(Context mContext) {
        TelephonyManager tManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getDeviceId();
    }



    public static boolean isNetworkConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    public static String getCompleteAddressString(double LATITUDE, double LONGITUDE,Context mContext) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }





}
