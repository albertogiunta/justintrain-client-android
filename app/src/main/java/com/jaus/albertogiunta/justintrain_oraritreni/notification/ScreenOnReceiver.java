package com.jaus.albertogiunta.justintrain_oraritreni.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import trikita.log.Log;

import static com.jaus.albertogiunta.justintrain_oraritreni.notification.NotificationService.ACTION_UPDATE_NOTIFICATION;


public class ScreenOnReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, NotificationService.class);
        i.setAction(ACTION_UPDATE_NOTIFICATION);
        context.startService(i);
        Log.d("onReceive: inviato intent a intentservice");
//        PendingIntent.getService(context, 1000, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}