package com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers;

import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SharedPreferencesHelper;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_INITIAL.SP0_FIRST_START;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_INITIAL.SP0_NOTIFICATION_PREF_JOURNEY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_INITIAL.SP0_NOTIFICATION_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_INITIAL.SP0_PREF_LIGHTNING;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_INITIAL.SP0_SAVED_VERSION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_INITIAL.SP0_SERVER_CONFIG;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_NOTIFICATION_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_LIGHTNING;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_FIRST_START;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_SAVED_VERSION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_VERSION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_VERSION;

public class SPTestingHelper {

    public static void setupSPv1(Context context) {
        SharedPreferencesHelper.removeAllSharedPreferences(context);
        SharedPreferencesHelper.setSharedPreferenceBoolean(context, SP_SP_FIRST_START, false);
        SharedPreferencesHelper.setSharedPreferenceInt(context, SP_SP_SAVED_VERSION, 13);
        SharedPreferencesHelper.setSharedPreferenceInt(context, SP_SP_VERSION, SP_VERSION);
        SharedPreferencesHelper.setSharedPreferenceBoolean(context, SP_SETT_LIGHTNING, true);
        SharedPreferencesHelper.setSharedPreferenceString(context, SP_NOTIFICATION_SOLUTION, "{\"arrivalStationName\":\"Roma Termini\",\"arrivalTime\":\"2017-01-11T21:34+0100\",\"arrivalTimeReadable\":\"21:34\",\"arrivalTimeWithDelayReadable\":\"21:34\",\"arrivesFirst\":false,\"departureStationName\":\"Aversa\",\"departureTime\":\"2017-01-11T19:49+0100\",\"departureTimeReadable\":\"19:49\",\"departureTimeWithDelayReadable\":\"19:49\",\"duration\":\"01:45\",\"hasChanges\":false,\"trainCategory\":\"IC\",\"trainId\":\"724\"}");
    }

    public static void setupSPv0(Context context) {
        SharedPreferencesHelper.removeAllSharedPreferences(context);
        SharedPreferencesHelper.setSharedPreferenceBoolean(context, SP0_FIRST_START, false);
        SharedPreferencesHelper.setSharedPreferenceBoolean(context, SP0_PREF_LIGHTNING, true);
        SharedPreferencesHelper.setSharedPreferenceInt(context, SP0_SAVED_VERSION, 9);
        SharedPreferencesHelper.setSharedPreferenceString(context, SP0_SERVER_CONFIG, "{\"address\":\"https://www.justintrain.it\",\"isAvailable\":true,\"weight\":0.0}");
        SharedPreferencesHelper.setSharedPreferenceString(context, SP0_NOTIFICATION_SOLUTION, "{\"arrivalStationName\":\"Roma Termini\",\"arrivalTime\":\"2017-01-11T21:34+0100\",\"arrivalTimeReadable\":\"21:34\",\"arrivalTimeWithDelayReadable\":\"21:34\",\"arrivesFirst\":false,\"departureStationName\":\"Aversa\",\"departureTime\":\"2017-01-11T19:49+0100\",\"departureTimeReadable\":\"19:49\",\"departureTimeWithDelayReadable\":\"19:49\",\"duration\":\"01:45\",\"hasChanges\":false,\"trainCategory\":\"IC\",\"trainId\":\"724\"}");
        SharedPreferencesHelper.setSharedPreferenceString(context, SP0_NOTIFICATION_PREF_JOURNEY, "{\"category\":\"\",\"priority\":-1,\"station1\":{\"nameLong\":\"Aversa\",\"nameShort\":\"Aversa\",\"stationLongId\":\"S09006\",\"stationShortId\":\"9006\"},\"station2\":{\"nameLong\":\"Roma Termini\",\"nameShort\":\"Roma Termini\",\"stationLongId\":\"S08409\",\"stationShortId\":\"8409\"},\"timestamp\":1484171507206,\"version\":\"\"}");
        SharedPreferencesHelper.setSharedPreferenceString(context, "5071-7104", "{\"category\":\"\",\"priority\":-1,\"station1\":{\"nameLong\":\"Pesaro\",\"nameShort\":\"Pesaro\",\"stationLongId\":\"S07104\",\"stationShortId\":\"7104\"},\"station2\":{\"nameLong\":\"Rimini\",\"nameShort\":\"Rimini\",\"stationLongId\":\"S05071\",\"stationShortId\":\"5071\"},\"timestamp\":1487413920236,\"version\":\"\"}");
        SharedPreferencesHelper.setSharedPreferenceString(context, "1301-1700", "{\"category\":\"\",\"priority\":-1,\"station1\":{\"nameLong\":\"Milano Centrale\",\"nameShort\":\"Milano Centrale\",\"stationLongId\":\"S01700\",\"stationShortId\":\"1700\"},\"station2\":{\"nameLong\":\"Chiasso\",\"nameShort\":\"Chiasso\",\"stationLongId\":\"S01301\",\"stationShortId\":\"1301\"},\"timestamp\":1483781450466,\"version\":\"\"}");
        SharedPreferencesHelper.setSharedPreferenceString(context, "5066-7104", "{\"category\":\"\",\"priority\":-1,\"station1\":{\"nameLong\":\"Pesaro\",\"nameShort\":\"Pesaro\",\"stationLongId\":\"S07104\",\"stationShortId\":\"7104\"},\"station2\":{\"nameLong\":\"Cesena\",\"nameShort\":\"Cesena\",\"stationLongId\":\"S05066\",\"stationShortId\":\"5066\"},\"timestamp\":1487621465099,\"version\":\"\"}");
    }

}
