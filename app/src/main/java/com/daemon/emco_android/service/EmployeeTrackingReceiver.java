package com.daemon.emco_android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import static com.daemon.emco_android.service.EmployeeTrackingService.TRACK_COUNT;

public class EmployeeTrackingReceiver extends BroadcastReceiver {

    int counter=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(EmployeeTrackingReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");

        if(intent.getExtras()!=null && intent.getExtras()!=null && intent.getExtras().getInt(TRACK_COUNT)!=0){
            counter=intent.getExtras().getInt(TRACK_COUNT);
        }
        Intent in=new Intent(context, EmployeeTrackingService.class);
        in.putExtra(TRACK_COUNT,counter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(in);
        } else {
            context.startService(in);
        }
    }
}
