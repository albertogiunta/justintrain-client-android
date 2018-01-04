package com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.DateTimeAdapter;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.PostProcessingEnabler;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_HOME_HEADER;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SEPARATOR;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SharedPreferencesHelper.getSharedPreferenceObject;

public class StationsPreferences {

    public static boolean isJourneyAlreadySaved(Context context, ENUM_HOME_HEADER type, String id1, String id2) {
        return getSharedPreferenceObject(context, buildSavedJourneyId(type, id1, id2)) != null;
    }

    public static void setSavedJourney(Context context, ENUM_HOME_HEADER type, PreferredJourney journey) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();
        SharedPreferencesHelper.setSharedPreferenceObject(context,
                buildSavedJourneyId(type, journey.getStation1().getStationShortId(),
                        journey.getStation2().getStationShortId()), gson.toJson(journey));
    }

    public static List<PreferredJourney> getAllSavedJourneys(Context context, ENUM_HOME_HEADER type) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();
        List<PreferredJourney> list = new LinkedList<>();
        Map<String, ?>         m    = SharedPreferencesHelper.getAllMatchingPrefix(context, type.getType());
        for (String el : m.keySet()) {
            PreferredJourney temp = gson.fromJson(((String) m.get(el)), PreferredJourney.class);
            if (temp.getStation1() != null && temp.getStation2() != null) {
                list.add(temp);
            }
        }

        if (type == ENUM_HOME_HEADER.FAVOURITES) {
            Collections.sort(list, (o1, o2) -> {
                if (o1.getTimestamp() < o2.getTimestamp()) {
                    return -1;
                } else if (o1.getTimestamp() > o2.getTimestamp()) {
                    return 1;
                } else {
                    return 0;
                }
            });
        } else if (type == ENUM_HOME_HEADER.RECENT) {
            Collections.sort(list, (o1, o2) -> {
                if (o1.getTimestamp() > o2.getTimestamp()) {
                    return -1;
                } else if (o1.getTimestamp() < o2.getTimestamp()) {
                    return 1;
                } else {
                    return 0;
                }
            });
        }
        return list;
    }

    public static PreferredJourney getSavedJourney(Context context, ENUM_HOME_HEADER type, PreferredJourney journey) {
        return new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create().fromJson(getSharedPreferenceObject(context, buildSavedJourneyId(type, journey)), PreferredJourney.class);
    }

    public static String buildSavedJourneyId(ENUM_HOME_HEADER type, PreferredJourney preferredJourney) {
        return buildSavedJourneyId(type, preferredJourney.getStation1().getStationShortId(), preferredJourney.getStation2().getStationShortId());
    }

    public static String buildSavedJourneyId(ENUM_HOME_HEADER type, PreferredStation departureStation, PreferredStation arrivalStation) {
        return buildSavedJourneyId(type, departureStation.getStationShortId(), arrivalStation.getStationShortId());
    }

    public static String buildSavedJourneyId(ENUM_HOME_HEADER type, String id1, String id2) {
        int cod1 = Integer.parseInt(id1);
        int cod2 = Integer.parseInt(id2);
        return type.getType() + Math.min(cod1, cod2) + SEPARATOR + Math.max(cod1, cod2);
    }

    public static void removeSavedJourney(Context context, ENUM_HOME_HEADER type, String id1, String id2) {
        SharedPreferencesHelper.removeSharedPreferenceObject(context, buildSavedJourneyId(type, id1, id2));
    }

}
