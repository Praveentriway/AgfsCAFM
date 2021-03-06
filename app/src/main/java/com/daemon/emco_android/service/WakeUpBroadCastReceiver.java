package com.daemon.emco_android.service;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import androidx.legacy.content.WakefulBroadcastReceiver;

/**
 * Created by vikram on 1/12/17.
 */

public class WakeUpBroadCastReceiver extends WakefulBroadcastReceiver
{

    private PowerManager.WakeLock screenWakeLock;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (screenWakeLock == null)
        {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            screenWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,"“ScreenLock tag from AlarmListener");
            screenWakeLock.acquire();
    }

        if (screenWakeLock != null){
            screenWakeLock.release();
            System.out.println("ffffffffffff");
        }else{
            System.out.println("vvvvvvvvvvvvvvv");
        }

    }
}
