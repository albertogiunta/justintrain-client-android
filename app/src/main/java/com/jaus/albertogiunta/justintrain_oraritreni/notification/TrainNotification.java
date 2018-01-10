package com.jaus.albertogiunta.justintrain_oraritreni.notification;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.data.TrainHeader;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.DateTimeAdapter;
import com.jaus.albertogiunta.justintrain_oraritreni.trainDetails.TrainDetailsActivity;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.NotificationPreferences;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SettingsPreferences;

import org.joda.time.DateTime;

import java.util.Random;

import static com.jaus.albertogiunta.justintrain_oraritreni.notification.NotificationService.ACTION_STOP_NOTIFICATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.notification.NotificationService.ACTION_UPDATE_NOTIFICATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.notification.NotificationService.EXTRA_NOTIFICATION_DATA;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_STATIONS;

/**
 * Helper class for showing and canceling train
 * notifications.
 */
public class TrainNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String TRAIN_NOTIFICATION_TAG = "trainNotification";
    private static final int    NOTIFICATION_ID        = 0;
    private static final String CHANNEL_ID             = "trainNotification";

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     */

    static Notification notify(final Context context, TrainHeader trainHeader, boolean hasVibration, boolean shouldPriorityBeHigh, boolean isCompatNotificationEnabled) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .create();

        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
//        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.example_picture);

        final String title     = buildTitle(trainHeader);
        final String text      = buildBody(trainHeader, isCompatNotificationEnabled);
        final String smallText = buildSmallText(trainHeader);

        Intent iUpdate = new Intent(context, NotificationService.class);
        iUpdate.putExtra(EXTRA_NOTIFICATION_DATA, gson.toJson(trainHeader));
        iUpdate.setAction(ACTION_UPDATE_NOTIFICATION);

        Intent iStop = new Intent(context, NotificationService.class);
        iStop.setAction(ACTION_STOP_NOTIFICATION);

        Intent notificationIntent = new Intent(context, TrainDetailsActivity.class);
        notificationIntent.putExtra(I_SOLUTION, NotificationPreferences.getNotificationSolutionString(context));
        notificationIntent.putExtra(I_STATIONS, NotificationPreferences.getNotificationPreferredJourneyString(context));
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, new Random().nextInt(),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int refreshIc = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.ic_refresh : R.drawable.ic_refresh2;
        int closeIc   = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.ic_close : R.drawable.ic_close2;

        initChannels(context, shouldPriorityBeHigh);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_SOUND)
                // Set required fields, including the small icon, the notification title, and text.
                .setSmallIcon(R.drawable.ic_notification2)
                .setColor(ViewsUtils.getColor(context, R.color.btn_dark_cyan))
                .setContentTitle(title)
                .setContentText(text)
                .setTicker(buildTicker(trainHeader))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(title)
                        .setSummaryText(smallText))
                .addAction(
                        refreshIc,
                        res.getString(R.string.action_refresh),
                        PendingIntent.getService(context, 1000, iUpdate, PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(
                        closeIc,
                        res.getString(R.string.action_end),
                        PendingIntent.getService(context, 1001, iStop, PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(intent)
                .setChannelId(CHANNEL_ID);

        if (shouldPriorityBeHigh) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        } else {
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        if (!hasVibration || !SettingsPreferences.isVibrationEnabled(context)) {
            builder.setVibrate(new long[]{-1});
        }

//        notify(context, builder.build());
        return builder.build();
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(TRAIN_NOTIFICATION_TAG, NOTIFICATION_ID, notification);
    }

    public static void initChannels(Context context, boolean shouldPriorityBeHigh) {
        if (Build.VERSION.SDK_INT < 26) return;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if (shouldPriorityBeHigh) {
            channel = new NotificationChannel(CHANNEL_ID, "Train Notification", NotificationManager.IMPORTANCE_HIGH);
        } else {
            channel = new NotificationChannel(CHANNEL_ID, "Train Notification", NotificationManager.IMPORTANCE_DEFAULT);
        }
        channel.setDescription("Queste notifiche servono a fornire informazioni sul treno che stai seguendo.");
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * Cancels any notifications of this type previously shown using
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(TRAIN_NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(TRAIN_NOTIFICATION_TAG.hashCode());
        }
    }

    private static String buildTitle(TrainHeader data) {
        return new Builder()
                .withString(data.getTrainCategory())
                .withSpace()
                .withString(data.getTrainId())
                .withEndingSymbol("|")
                .withSeparatedWords(data.getJourneyDepartureStationName(), String.valueOf((char) 187), data.getJourneyArrivalStationName())
                .build();
    }

    private static String buildBody(TrainHeader data, boolean isCompatNotificationEnabled) {
        String delayPlusProgress;
        if (isCompatNotificationEnabled) {
            if (data.isDeparted()) {
                delayPlusProgress = new Builder()
                        .withString(buildTimeDifferenceStringPro(data.getTimeDifference()))
                        .withEndingSymbol("|")
                        .withString(buildPredictorPro(data))
                        .withEndingSymbol("|")
                        .withString(buildProgressStringPro(data.getProgress()))
                        .build();
            } else {
                delayPlusProgress = new Builder()
                        .withString("Non partito")
                        .withEndingSymbol("|")
                        .withString(buildPredictorPro(data))
                        .build();
            }
            return delayPlusProgress;
        } else {
            if (data.isDeparted()) {
                delayPlusProgress = new Builder()
                        .withString(buildTimeDifferenceStringNormal(data.getTimeDifference()))
                        .withEndingSymbol("|")
                        .withString(buildProgressStringNormal(data.getProgress()))
                        .build();
            } else {
                delayPlusProgress = new Builder()
                        .withString("Il treno non è ancora partito")
                        .build();
            }
            return delayPlusProgress + "\n" + buildPredictorNormal(data);
        }
    }

    private static String buildSmallText(TrainHeader data) {
        return buildLastSeenString(data.getLastSeenTimeReadable(), data.getLastSeenStationName());
    }

    private static String buildTicker(TrainHeader data) {
        return new Builder()
                .withString(buildTimeDifferenceStringNormal(data.getTimeDifference()))
                .withEndingSymbol("|")
                .withString(buildProgressStringNormal(data.getProgress()))
                .build();
    }

    private static String trimStationName(String stationName) {
        return (stationName.length() > 5 ? stationName.substring(0, 3) + "." : stationName).toUpperCase();
    }

    private static String buildTimeDifferenceStringNormal(int timeDifference) {
        String time   = Integer.toString(Math.abs(timeDifference)) + "'";
        String delay  = "Ritardo: ";
        String ontime = "Anticipo: ";
        if (timeDifference > 0) {
            time = delay + time;
        } else if (timeDifference < 0) {
            time = ontime + time;
        } else {
            time = "In orario";
        }
        return time;
    }

    private static String buildTimeDifferenceStringPro(int timeDifference) {
        String time = Integer.toString(Math.abs(timeDifference)) + "'";
        if (timeDifference > 0) {
            time = "+" + time;
        } else if (timeDifference < 0) {
            time = "-" + time;
        }
        return time;
    }

    private static String buildProgressStringNormal(int progress) {
        String progr = "Andamento: ";
        switch (progress) {
            case 0:
                return progr + "Costante";
            case 1:
                return progr + "Recuperando";
            case 2:
                return progr + "Rallentando";
            default:
                return "";
        }
    }

    private static String buildProgressStringPro(int progress) {
        String progr = "";
        switch (progress) {
            case 0:
                return progr + "Costante";
            case 1:
                return progr + "Recuperando";
            case 2:
                return progr + "Rallentando";
            default:
                return "";
        }
    }

    private static String buildPredictorNormal(TrainHeader data) {
        String prediction = "Probabile arrivo a ";
        String station;

        if (!data.getJourneyDepartureStationVisited()) {
            station = data.getJourneyDepartureStationName();
            if (data.getDeparturePlatform() != null && !data.getDeparturePlatform().equalsIgnoreCase(""))
                station += " (Bin. " + data.getDeparturePlatform() + ")";
        } else if (!data.getJourneyArrivalStationVisited()) {
            station = data.getJourneyArrivalStationName();
        } else {
            return "Treno arrivato a " + data.getJourneyArrivalStationName();
        }

        prediction += station;

        int eta = data.getETAToNextJourneyStation();
        if (eta == 0) {
            prediction += " adesso ";
        } else if (eta == 1) {
            prediction += (" tra " + eta + " minuto");
        } else if (eta > 1) {
            if (eta < 60) {
                prediction += (" tra " + eta + " minuti");
            } else if (eta / 60 < 2) {
                prediction += (" tra più di " + 1 + " ora");
            } else {
                int hours = eta / 60;
                prediction += (" tra più di " + hours + " ore");
            }
        }
        return prediction;
    }

    private static String buildPredictorPro(TrainHeader data) {
        String prediction = "";
        String station;

        if (!data.getJourneyDepartureStationVisited()) {
            station = data.getJourneyDepartureStationName();
            if (data.getDeparturePlatform() != null && !data.getDeparturePlatform().equalsIgnoreCase(""))
                station += " (Bin. " + data.getDeparturePlatform() + ")";
        } else if (!data.getJourneyArrivalStationVisited()) {
            station = data.getJourneyArrivalStationName();
        } else {
            return "Treno arrivato a " + data.getJourneyArrivalStationName();
        }


        int eta = data.getETAToNextJourneyStation();
        if (eta == 0) {
            prediction += "Adesso";
        } else if (eta == 1) {
            prediction += ("Tra " + eta + "'");
        } else if (eta > 1) {
            if (eta < 60) {
                prediction += ("Tra " + eta + "'");
            } else if (eta / 60 < 2) {
                prediction += ("Tra più di " + 1 + " ora");
            } else {
                int hours = eta / 60;
                prediction += ("Tra più di " + hours + " ore");
            }
        }
        prediction += " a " + station;
        return prediction;
    }

    private static String buildLastSeenString(String time, String station) {
        return time.length() > 0 && station.length() > 0 ? "Visto alle " + time + " a " + station : "";
    }

    private static class Builder {

        String string = "";

        Builder() {
        }

        Builder withString(String s) {
            string += s;
            return this;
        }

        Builder withSpace() {
            string += " ";
            return this;
        }

        Builder withEndingSymbol(String symbol) {
            string += string.length() > 0 ? " " + symbol + " " : "";
            return this;
        }

        Builder withSeparatedWords(String first, String separator, String second) {
            string += first.length() == 0 || second.length() == 0 ? first + second : first + " " + separator + " " + second;
            return this;
        }

        String build() {
            return string;
        }
    }
}
