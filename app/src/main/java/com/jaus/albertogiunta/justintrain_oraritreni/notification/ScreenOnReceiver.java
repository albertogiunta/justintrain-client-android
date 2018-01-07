package com.jaus.albertogiunta.justintrain_oraritreni.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

import static com.jaus.albertogiunta.justintrain_oraritreni.notification.NotificationService.ACTION_UPDATE_NOTIFICATION;


public class ScreenOnReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_ON)) {
            Intent i = new Intent(context, NotificationService.class);
            i.setAction(ACTION_UPDATE_NOTIFICATION);
            context.startService(i);
        }
    }
}
