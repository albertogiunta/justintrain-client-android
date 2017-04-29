package com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences;

import com.google.gson.Gson;

import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.BuildConfig;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import trikita.log.Log;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_INITIAL.SP0_FIRST_START;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_INITIAL.SP0_NOTIFICATION_PREF_JOURNEY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_INITIAL.SP0_NOTIFICATION_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_INITIAL.SP0_PREF_LIGHTNING;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_INITIAL.SP0_SAVED_VERSION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_INITIAL.SP0_SERVER_CONFIG;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_NOTIFICATION_PREF_JOURNEY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_NOTIFICATION_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_LIGHTNING;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_FIRST_START;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_SAVED_VERSION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_SERVER_CONFIG;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_VERSION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_VERSION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SharedPreferencesHelper.getAll;

public class MigrationHelper {

    /*
    nuova installaziione => fai default e sala sp_version a SP_VERSION
                            se il bomber elimina i settings ricomincia da zero e quindi risalva i settings

    vecchia installazione => o sp_version esiste ed è minore rispetto a quella dell'app, allora devi fare la migration
                             oppure sp_version non esiste e allora è il caso da -1 a 0 (perchè in tutti gli altri casi è alla prima installazione)
     */

    public static void migrateIfDue(Context context) {
//        SharedPreferencesHelper.setSharedPreferenceInt(context, SP_SP_VERSION, -1); // use for testing purposes
        int oldSPVersion = SharedPreferencesHelper.getSharedPreferenceInt(context, SP_SP_VERSION, -1);
        if (oldSPVersion == -1 && SP_VERSION == 0) {
            SharedPreferencesHelper.setSharedPreferenceInt(context, SP_SP_VERSION, SP_VERSION);
            migrationFromNothingToVersionZero(context);
        }
    }

    private static void migrationFromNothingToVersionZero(Context context) {

        // migro il firstStart
        SharedPreferencesHelper.setSharedPreferenceBoolean(context, SP_SP_FIRST_START, SharedPreferencesHelper.getSharedPreferenceBoolean(context, SP0_FIRST_START, true));
        SharedPreferencesHelper.removeSharedPreferenceObject(context, SP0_FIRST_START);

        // migro il savedVersion
        SharedPreferencesHelper.setSharedPreferenceInt(context, SP_SP_SAVED_VERSION, SharedPreferencesHelper.getSharedPreferenceInt(context, SP0_SAVED_VERSION, BuildConfig.VERSION_CODE));
        SharedPreferencesHelper.removeSharedPreferenceObject(context, SP0_SAVED_VERSION);

        // rimuovo e basta il server config perchè dovrebbe bastare firebase
        SharedPreferencesHelper.removeSharedPreferenceObject(context, SP0_SERVER_CONFIG);

        // migro il lightning
        SharedPreferencesHelper.setSharedPreferenceBoolean(context, SP_SETT_LIGHTNING, SharedPreferencesHelper.getSharedPreferenceBoolean(context, SP0_PREF_LIGHTNING, false));
        SharedPreferencesHelper.removeSharedPreferenceObject(context, SP0_PREF_LIGHTNING);

        // migro il notificationsolution
        String notificationSolution = SharedPreferencesHelper.getSharedPreferenceString(context, SP0_NOTIFICATION_SOLUTION, "");
        if (!notificationSolution.equalsIgnoreCase("")) {
            SharedPreferencesHelper.setSharedPreferenceString(context, SP_NOTIFICATION_SOLUTION, SharedPreferencesHelper.getSharedPreferenceString(context, SP0_NOTIFICATION_SOLUTION, ""));
            SharedPreferencesHelper.removeSharedPreferenceObject(context, SP0_NOTIFICATION_SOLUTION);
        }

        // migro il notificationprefjourney
        String notificationPrefJourney = SharedPreferencesHelper.getSharedPreferenceString(context, SP0_NOTIFICATION_PREF_JOURNEY, "");
        if (!notificationPrefJourney.equalsIgnoreCase("")) {
            SharedPreferencesHelper.setSharedPreferenceString(context, SP_NOTIFICATION_PREF_JOURNEY, SharedPreferencesHelper.getSharedPreferenceString(context, SP0_NOTIFICATION_PREF_JOURNEY, ""));
            SharedPreferencesHelper.removeSharedPreferenceObject(context, SP0_NOTIFICATION_PREF_JOURNEY);
        }

        // migro tutti le preferredJourneys
        List<PreferredJourney> preferredJourneysList = getAllOLDPreferredJourneys(context);
        for (PreferredJourney preferredJourney : preferredJourneysList) {
            PreferredStationsPreferences.setPreferredJourney(context, preferredJourney);
            SharedPreferencesHelper.removeSharedPreferenceObject(context, buildOLDPreferredJourneyId(preferredJourney));
        }
    }

    public static String buildOLDPreferredJourneyId(PreferredJourney preferredJourney) {
        return buildOLDPreferredJourneyId(preferredJourney.getStation1().getStationShortId(), preferredJourney.getStation2().getStationShortId());
    }


    public static String buildOLDPreferredJourneyId(PreferredStation departureStation, PreferredStation arrivalStation) {
        return buildOLDPreferredJourneyId(departureStation.getStationShortId(), arrivalStation.getStationShortId());
    }

    public static String buildOLDPreferredJourneyId(String id1, String id2) {
        int cod1 = Integer.parseInt(id1);
        int cod2 = Integer.parseInt(id2);
        return Math.min(cod1, cod2) + "-" + Math.max(cod1, cod2);
    }

    /**
     * SHOULD ONLY BE USED FOR MIGRATION STUFF
     *
     * @return a list of preferred journeys
     */
    public static List<PreferredJourney> getAllOLDPreferredJourneys(Context context) {
        Gson                   gson = new Gson();
        List<PreferredJourney> list = new LinkedList<>();
        Map<String, ?>         m    = getAll(context);
        for (String el : m.keySet()) {
            if (!el.equals(SP_SP_FIRST_START)
                    && !el.equals(SP_SP_SAVED_VERSION)
                    && !el.equals(SP_SP_SERVER_CONFIG)
                    && !el.equals(SP_NOTIFICATION_PREF_JOURNEY)
                    && !el.equals(SP_NOTIFICATION_SOLUTION)
                    && !el.equals(SP_SP_VERSION)
                    && !el.equals(SP_SETT_LIGHTNING)) {
                Log.d("getAllPreferredJourneys: ", el);
                PreferredJourney temp = gson.fromJson(((String) m.get(el)), PreferredJourney.class);
                if (temp.getStation1() != null && temp.getStation2() != null) list.add(temp);
            }
        }
        Collections.sort(list, (o1, o2) -> {
            if (o1.getTimestamp() < o2.getTimestamp()) {
                return -1;
            } else if (o1.getTimestamp() > o2.getTimestamp()) {
                return 1;
            } else {
                return 0;
            }
        });
        return list;
    }
}
