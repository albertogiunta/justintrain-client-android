package com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SharedPreferencesHelper;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_FIREBASE.FIREBASE_SERVER_CONFIG;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_PRO_USERS_NUMBER;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_SERVER_CONFIG;

public class ServerConfigsHelper {

    public static String getAPIEndpoint() {
        return FirebaseRemoteConfig.getInstance().getString(FIREBASE_SERVER_CONFIG);
    }

    public static void removeAPIEndpoint(Context context) {
        SharedPreferencesHelper.removeSharedPreferenceObject(context, SP_SP_SERVER_CONFIG);
    }

    public static int getProUsersNumberFromSharedPreferences(Context context) {
        return SharedPreferencesHelper.getSharedPreferenceInt(context, SP_SP_PRO_USERS_NUMBER, 2);
    }

    public static void setProUsersInSharedPreferences(Context context, int number) {
        SharedPreferencesHelper.setSharedPreferenceInt(context, SP_SP_PRO_USERS_NUMBER, number);
    }

}
