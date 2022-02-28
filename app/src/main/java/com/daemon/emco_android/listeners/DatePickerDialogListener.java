package com.daemon.emco_android.listeners;


import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by vikram on 17/7/17.
 */

public interface DatePickerDialogListener {
    void onDateReceivedSuccess(String strDate);
}
