package com.jaus.albertogiunta.justintrain_oraritreni.notification;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.jaus.albertogiunta.justintrain_oraritreni.BuildConfig;
import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.journeyFavourites.FavouriteJourneysActivity;

import trikita.log.Log;

public class AppFCMService extends FirebaseMessagingService {
    private final static String TAG = "FCM Message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        showNotification(remoteMessage);
    }

    private void showNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String body  = remoteMessage.getNotification().getBody();

        Intent intent = new Intent(this, FavouriteJourneysActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        if ((remoteMessage.getData().get("isDebugNotification") == null) ||
                remoteMessage.getData().get("isDebugNotification").equals("false") ||
                (remoteMessage.getData().get("isDebugNotification").equals("true") && BuildConfig.DEBUG)) {
//        Bitmap notifyImage     = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification2);
//        Uri    defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.drawable.ic_notification2)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(body)
                            .setBigContentTitle(title)
                            .setSummaryText(body))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            if (remoteMessage.getData().get("isDiscountNotification").equals("true")) {
                ClickActionHelper.startActivity(remoteMessage.getData().get("on_notification_click_action"), null, this);
            }

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}