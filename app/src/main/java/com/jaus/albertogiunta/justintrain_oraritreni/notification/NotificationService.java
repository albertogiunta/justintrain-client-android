package com.jaus.albertogiunta.justintrain_oraritreni.notification;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.jaus.albertogiunta.justintrain_oraritreni.data.Journey;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.data.TrainHeader;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.APINetworkingFactory;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.DateTimeAdapter;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.JourneyService;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.DatabaseHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.NotificationPreferences;

import org.joda.time.DateTime;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import trikita.log.Log;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_REFRESH_NOTIFICAITON;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_REMOVE_NOTIFICATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SCREEN_NOTIFICATION;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread
 */
public class NotificationService extends IntentService {
    public static final String ACTION_START_NOTIFICATION                    = "com.jaus.albertogiunta.justintrain_oraripendolaritrenitalia.action.START_NOTIFICATION";
    public static final String ACTION_STOP_NOTIFICATION                     = "com.jaus.albertogiunta.justintrain_oraripendolaritrenitalia.action.STOP_NOTIFICATION";
    public static final String ACTION_UPDATE_NOTIFICATION                   = "com.jaus.albertogiunta.justintrain_oraripendolaritrenitalia.action.UPDATE_NOTIFICATION";
    public static final String EXTRA_NOTIFICATION_DATA                      = "com.jaus.albertogiunta.justintrain_oraripendolaritrenitalia.extra.NOTIFICATION_DATA";
    public static final String NOTIFICATION_ERROR_EVENT                     = "EVENT_NOTIFICATION_ERROR";
    public static final String NOTIFICATION_ERROR_MESSAGE                   = "NOTIFICATION_ERROR_MESSAGE";
    public static final String NOTIFICATION_ERROR_MESSAGE_TRAIN_NOT_FOUND   = "Impossibile settare la notifica. \nIl treno potrebbe aver cambiato numero od orario.";
    public static final String NOTIFICATION_ERROR_MESSAGE_STATION_NOT_FOUND = "Impossibile settare la notifica a causa di un problema con le stazioni di partenza o arrivo.";

    AnalyticsHelper analyticsHelper;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
            .create();

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        analyticsHelper = AnalyticsHelper.getInstance(getBaseContext());

    }

    public static void startActionStartNotification(Context context, PreferredStation journeyDepartureStation, PreferredStation journeyArrivalStation, Journey.Solution sol, Integer indexOfJourneyToBeNotified) {
        Log.d("startActionStartNotification:", "start");
        String  trainId;
        String  journeyDepartureStationId = journeyDepartureStation.getStationShortId();
        String  journeyArrivalStationId   = journeyArrivalStation.getStationShortId();
        Intent  intent                    = new Intent(context, NotificationService.class);
        boolean justUpdate                = indexOfJourneyToBeNotified != null;

        intent.setAction(ACTION_START_NOTIFICATION);

        if (sol.hasChanges()) {
            if (indexOfJourneyToBeNotified == null) {
                indexOfJourneyToBeNotified = 0;
                for (Journey.Solution.Change c : sol.getChangesList()) {
                    if ((c.getTimeDifference() != null
                            && c.getTimeDifference() > 0 && c.getArrivalTime().plus(c.getTimeDifference()).isAfter(DateTime.now()))
                            || (c.getTimeDifference() == null && c.getArrivalTime().isAfter(DateTime.now()))) {
                        indexOfJourneyToBeNotified = sol.getChangesList().indexOf(c);
                        break;
                    }
                }
            }

            trainId = sol.getChangesList().get(indexOfJourneyToBeNotified).getTrainId();
            try {
                journeyDepartureStationId = DatabaseHelper.getStation4DatabaseObject(sol.getChangesList().get(indexOfJourneyToBeNotified).getDepartureStationName()).getStationShortId();
                journeyArrivalStationId = DatabaseHelper.getStation4DatabaseObject(sol.getChangesList().get(indexOfJourneyToBeNotified).getArrivalStationName()).getStationShortId();

                getData(journeyDepartureStationId,
                        journeyArrivalStationId,
                        trainId, context, justUpdate);

            } catch (Exception e) {
                FirebaseCrash.report(new Exception("Requested notification and Station was not found. SOLUTION IS " + sol.toString() + " AND REQUESTED STATIONSs ARE " + sol.getChangesList().get(indexOfJourneyToBeNotified).getDepartureStationName() + " " + sol.getChangesList().get(indexOfJourneyToBeNotified).getArrivalStationName()));
                Intent notificationErrorIntent = new Intent(NOTIFICATION_ERROR_EVENT);
                notificationErrorIntent.putExtra(NOTIFICATION_ERROR_MESSAGE, NOTIFICATION_ERROR_MESSAGE_STATION_NOT_FOUND);
                LocalBroadcastManager.getInstance(context).sendBroadcast(notificationErrorIntent);
            }
        } else {
            trainId = sol.getTrainId();

            getData(journeyDepartureStationId,
                    journeyArrivalStationId,
                    trainId, context, justUpdate);
        }
    }

    private static void getData(String journeyDepartureStationId, String journeyArrivalStationId, String trainId, Context context, boolean justUpdate) {
        Log.d("getData:", journeyDepartureStationId, journeyArrivalStationId, trainId);
        Intent notificationErrorIntent = new Intent(NOTIFICATION_ERROR_EVENT);

        APINetworkingFactory.createRetrofitService(JourneyService.class)
                .getDelay(journeyDepartureStationId, journeyArrivalStationId, trainId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TrainHeader>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(e.getMessage());
                        FirebaseCrash.report(new Exception("NOTIFICATION ERROR for departure " + journeyDepartureStationId + " arrival " + journeyArrivalStationId + " train " + trainId));
                        notificationErrorIntent.putExtra(NOTIFICATION_ERROR_MESSAGE, NOTIFICATION_ERROR_MESSAGE_TRAIN_NOT_FOUND);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(notificationErrorIntent);
                    }

                    @Override
                    public void onNext(TrainHeader trainHeader) {
                        if (justUpdate) {
                            Log.d("onNext: just updating");
                            TrainNotification.notify(context, trainHeader, true);
                            return;
                        }
                        if (trainHeader.getJourneyArrivalStationVisited()) {
                            Journey.Solution s = NotificationPreferences.getNotificationSolution(context);
                            if (s.hasChanges()) {
                                List<Journey.Solution.Change> l = s.getChangesList();
                                for (int i = 0; i < l.size(); i++) {
                                    if (trainHeader.getTrainId().equalsIgnoreCase(l.get(i).getTrainId())) {
                                        if (i < l.size() - 1) {
                                            Log.d("onNext: going to get the next one");
                                            String journeyDepartureStationId;
                                            String journeyArrivalStationId;
                                            try {
                                                journeyDepartureStationId = DatabaseHelper.getStation4DatabaseObject(l.get(i + 1).getDepartureStationName()).getStationShortId();
                                                journeyArrivalStationId = DatabaseHelper.getStation4DatabaseObject(l.get(i + 1).getArrivalStationName()).getStationShortId();
                                                getData(journeyDepartureStationId, journeyArrivalStationId, l.get(i + 1).getTrainId(), context, false);
                                            } catch (Exception e) {
                                                FirebaseCrash.report(new Exception("Requested new train in notification and Station was not found. SOLUTION IS " + s.toString() + " AND REQUESTED STATIONSs ARE " + l.get(i + 1).getDepartureStationName() + " " + l.get(i + 1).getArrivalStationName()));
                                                notificationErrorIntent.putExtra(NOTIFICATION_ERROR_MESSAGE, NOTIFICATION_ERROR_MESSAGE_STATION_NOT_FOUND);
                                                LocalBroadcastManager.getInstance(context).sendBroadcast(notificationErrorIntent);
                                            }
                                            return;
                                        } else {
                                            Log.d("onNext: displaying the last train of the solution (which has arrived)");
                                            TrainNotification.notify(context, trainHeader, true);
                                        }
                                    }
                                }
                            } else {
                                Log.d("onNext: displaying a single train solution which has arrived");
                                TrainNotification.notify(context, trainHeader, true);
                            }
                        } else {
                            Log.d("onNext: displaying a train of a solution which hasnt arrived yet");
                            TrainNotification.notify(context, trainHeader, true);
                        }
                    }
                });
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.d(action);
            if (ACTION_START_NOTIFICATION.equals(action)) {
//                analyticsHelper.logScreenEvent(SCREEN_NOTIFICATION, ACTION_START_NOTIFICATION);
                Log.d("onHandleIntent:", "start");
            } else if (ACTION_STOP_NOTIFICATION.equals(action)) {
                analyticsHelper.logScreenEvent(SCREEN_NOTIFICATION, ACTION_REMOVE_NOTIFICATION);
                Log.d("onHandleIntent:", "stop");
                NotificationPreferences.removeNotificationData(getBaseContext());
                TrainNotification.cancel(this);
            } else if (ACTION_UPDATE_NOTIFICATION.equals(action)) {
                analyticsHelper.logScreenEvent(SCREEN_NOTIFICATION, ACTION_REFRESH_NOTIFICAITON);
                Log.d("onHandleIntent:", "update");
                TrainHeader trainHeader = gson.fromJson(intent.getStringExtra(EXTRA_NOTIFICATION_DATA), TrainHeader.class);
                getData(trainHeader.getJourneyDepartureStationId(),
                        trainHeader.getJourneyArrivalStationId(),
                        trainHeader.getTrainId(), getApplicationContext(), false);
            }
        }
    }
}
