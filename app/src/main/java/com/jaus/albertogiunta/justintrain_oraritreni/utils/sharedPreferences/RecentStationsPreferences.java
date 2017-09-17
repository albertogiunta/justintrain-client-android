package com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences;

import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;

import java.util.List;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_HOME_HEADER.RECENT;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.StationsPreferences.buildSavedJourneyId;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.StationsPreferences.getAllSavedJourneys;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.StationsPreferences.getSavedJourney;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.StationsPreferences.isJourneyAlreadySaved;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.StationsPreferences.removeSavedJourney;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.StationsPreferences.setSavedJourney;

public class RecentStationsPreferences {

    public static boolean isJourneyAlreadyRecent(Context context, PreferredJourney preferredJourney) {
        return isJourneyAlreadyRecent(context, preferredJourney.getStation1().getStationShortId(), preferredJourney.getStation2().getStationShortId());
    }

    public static boolean isJourneyAlreadyRecent(Context context, PreferredStation departureStation, PreferredStation arrivalStation) {
        return isJourneyAlreadyRecent(context, departureStation.getStationShortId(), arrivalStation.getStationShortId());
    }

    public static boolean isJourneyAlreadyRecent(Context context, String id1, String id2) {
        return isJourneyAlreadySaved(context, RECENT, id1, id2);
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static void setRecentJourney(Context context, PreferredJourney journey) {
        if (!isJourneyAlreadyRecent(context, journey)) {
            List<PreferredJourney> list = getAllRecentJourneys(context);
            if (!isPossibleToSaveMoreRecentJourneys(list.size())) {
                removeRecentJourney(context, list.get(list.size() - 1).getStation1(), list.get(list.size() - 1).getStation2());
            }
            setSavedJourney(context, RECENT, journey);
        }
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static boolean isPossibleToSaveMoreRecentJourneys(Context context) {
        return isPossibleToSaveMoreRecentJourneys(getAllRecentJourneys(context).size());
    }

    public static boolean isPossibleToSaveMoreRecentJourneys(int size) {
        return size < 3;
    }


//    ------------------------------------------------------------------------------------------------------------------------

    public static void removeRecentJourney(Context context, PreferredStation departureStation, PreferredStation arrivalStation) {
        removeRecentJourney(context, departureStation.getStationShortId(), arrivalStation.getStationShortId());
    }

    public static void removeRecentJourney(Context context, String id1, String id2) {
        removeSavedJourney(context, RECENT, id1, id2);
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static List<PreferredJourney> getAllRecentJourneys(Context context) {
        return getAllSavedJourneys(context, RECENT);
    }

    public static PreferredJourney getRecentJourney(Context context, PreferredJourney journey) {
        return getSavedJourney(context, RECENT, journey);
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static String buildRecentJourneyId(PreferredJourney preferredJourney) {
        return buildRecentJourneyId(preferredJourney.getStation1().getStationShortId(), preferredJourney.getStation2().getStationShortId());
    }

    public static String buildRecentJourneyId(PreferredStation departureStation, PreferredStation arrivalStation) {
        return buildRecentJourneyId(departureStation.getStationShortId(), arrivalStation.getStationShortId());
    }

    public static String buildRecentJourneyId(String id1, String id2) {
        return buildSavedJourneyId(RECENT, id1, id2);
    }

}
