package com.daemon.emco_android.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.daemon.emco_android.listeners.AppUpdateListener;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

public class AppUpdaterUtils {

    public void showDialog(final Context _context,final AppUpdateListener updateListener){

        new AppUpdater(_context)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setDisplay(Display.DIALOG)
                .setButtonDoNotShowAgain(null).setCancelable(false).setButtonDismiss(null)
                .showAppUpdated(false)
                .setButtonUpdateClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateListener.onUpdateClicked(false);

                    }
                }).start();

    }


}
