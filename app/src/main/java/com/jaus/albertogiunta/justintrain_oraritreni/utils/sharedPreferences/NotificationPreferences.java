package com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.data.Journey;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.DateTimeAdapter;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.PostProcessingEnabler;

import org.joda.time.DateTime;

import trikita.log.Log;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_NOTIFICATION_PREF_JOURNEY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_NOTIFICATION_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SharedPreferencesHelper.getSharedPreferenceObject;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SharedPreferencesHelper.removeSharedPreferenceObject;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SharedPreferencesHelper.setSharedPreferenceObject;

public class NotificationPreferences {

    public static void setNotificationData(Context context, PreferredJourney preferredJourney, Journey.Solution solution) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();
        setSharedPreferenceObject(context,
                SP_NOTIFICATION_PREF_JOURNEY,
                gson.toJson(preferredJourney));

        setSharedPreferenceObject(context,
                SP_NOTIFICATION_SOLUTION,
                gson.toJson(solution));
    }

    public static void removeNotificationData(Context context) {
        removeSharedPreferenceObject(context, SP_NOTIFICATION_PREF_JOURNEY);
        removeSharedPreferenceObject(context, SP_NOTIFICATION_SOLUTION);
    }

    public static PreferredJourney getNotificationPreferredJourney(Context context) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();
        return gson.fromJson(getSharedPreferenceObject(context, SP_NOTIFICATION_PREF_JOURNEY), PreferredJourney.class);
    }

    public static Journey.Solution getNotificationSolution(Context context) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();
        return gson.fromJson(getSharedPreferenceObject(context, SP_NOTIFICATION_SOLUTION), Journey.Solution.class);
    }

    public static String getNotificationPreferredJourneyString(Context context) {
        return getSharedPreferenceObject(context, SP_NOTIFICATION_PREF_JOURNEY);
    }

    public static String getNotificationSolutionString(Context context) {
        return getSharedPreferenceObject(context, SP_NOTIFICATION_SOLUTION);
    }

    public static void printNotificationData(Context context) {
        Log.d("printNotificationData: ");
        Log.d("printNotificationData: ", getNotificationPreferredJourney(context).toString());
        Log.d("printNotificationData: ", getNotificationSolution(context).toString());
    }


}
