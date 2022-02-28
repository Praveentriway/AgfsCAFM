package com.daemon.emco_android.utils;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.daemon.emco_android.ui.activities.LoginActivity.IP_ADDRESS;

@SuppressWarnings("ALL")
public class SessionManager {

    private static String FILE_NAME = "LMS APP";

    public static SharedPreferences prefs;
    private Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public static void saveSession(String key, String value, Context context)  {

        try {
            Editor editor = context.getSharedPreferences("KEY",
                    Activity.MODE_PRIVATE).edit();
            editor.putString(key, new AESEncryption().encrypt(value,"KEY1"));
            editor.commit();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void saveSessionForURL(String key, String value, Context context)  {

        try {
            Editor editor = context.getSharedPreferences("KEY1",
                    Activity.MODE_PRIVATE).edit();
            editor.putString(key, new AESEncryption().encrypt(value,"KEY1"));
            editor.commit();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public static String getSessionForURL(String key, Context context)  {

        try {
            SharedPreferences pref = context.getSharedPreferences("KEY1",
                    Activity.MODE_PRIVATE);
            return  new AESEncryption().decrypt( pref.getString(key, ""),"KEY1");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getSession(String key, Context context)  {

        try {
            SharedPreferences pref = context.getSharedPreferences("KEY",
                    Activity.MODE_PRIVATE);
            return  new AESEncryption().decrypt( pref.getString(key, ""),"KEY1");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static void clearSession(Context context) {
        Editor editor = context.getSharedPreferences("KEY",
                Activity.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    /**
     * To store the session values
     *
     * @param key
     * @param value
     */
    public void setPreferences(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * To get the stored session values
     *
     * @param key
     * @return
     */
    public String getPrefString(String key) {
        return prefs.getString(key, "");
    }

    /**
     * To remove the particular value from preference
     *
     * @param key
     */

    public void removeKeyInPreference(String key) {
        editor.remove(key);
        editor.apply();
    }

    /**
     * To clear all the values from preference
     */
    public void clearPreference() {
        editor.clear();
        editor.apply();
    }
}
