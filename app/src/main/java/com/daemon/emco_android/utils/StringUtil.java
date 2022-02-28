package com.daemon.emco_android.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {

    public static final String space ="\n\n\n\n\n\n\n\n";

    public static String MD5(String string) {
        if (string == null)
            return null;

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] inputBytes = string.getBytes();
            byte[] hashBytes = digest.digest(inputBytes);
            return byteArrayToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) { }

        return null;
    }

    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }


    public static String dateConvertion (String date){

        String[] splited = date.split("\\s+");

        return splited[0]+" "+splited[1]+" "+splited[2];
    }


    public static boolean IsNull(String val){
        if(val==null || val.equalsIgnoreCase("null")){
            return true;
        }
        else{
            return false;
        }

    }

    public static boolean isContainsArabic(String s) {
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }


}
