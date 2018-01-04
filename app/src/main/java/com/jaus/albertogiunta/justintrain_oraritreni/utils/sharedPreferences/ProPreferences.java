package com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences;

import android.content.Context;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_INSTANT_DELAY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_NOTIF_AUTO_REMOVE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_NOTIF_COMPACT;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_NOTIF_LIVE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_IS_PRO;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SettingsPreferences.disableGenericSetting;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SettingsPreferences.enableGenericSetting;

public class ProPreferences {

    public static void enableAllPro(Context context) {
        ProPreferences.enablePro(context);
        ProPreferences.enableLiveNotification(context);
        ProPreferences.enableInstantDelay(context);
        ProPreferences.enableCompactNotification(context);
        ProPreferences.enableAutoRemoveNotification(context);
    }

    public static void disableAllPro(Context context) {
        ProPreferences.disablePro(context);
        ProPreferences.disableLiveNotification(context);
        ProPreferences.disableInstantDelay(context);
        ProPreferences.disableCompactNotification(context);
        ProPreferences.disableAutoRemoveNotification(context);
    }

    // instant delay
    public static boolean isPro(Context context) {
        return SharedPreferencesHelper.getSharedPreferenceBoolean(context, SP_SP_IS_PRO, false);
    }

    public static void enablePro(Context context) {
        enableGenericSetting(context, SP_SP_IS_PRO);
    }

    public static void disablePro(Context context) {
        disableGenericSetting(context, SP_SP_IS_PRO);
    }

    // instant delay
    public static boolean isInstantDelayEnabled(Context context) {
        return SharedPreferencesHelper.getSharedPreferenceBoolean(context, SP_SETT_INSTANT_DELAY, true);
    }

    public static void enableInstantDelay(Context context) {
        enableGenericSetting(context, SP_SETT_INSTANT_DELAY);
    }

    public static void disableInstantDelay(Context context) {
        disableGenericSetting(context, SP_SETT_INSTANT_DELAY);
    }

    // live notification
    public static boolean isLiveNotificationEnabled(Context context) {
        return SharedPreferencesHelper.getSharedPreferenceBoolean(context, SP_SETT_NOTIF_LIVE, true);
    }

    public static void enableLiveNotification(Context context) {
        enableGenericSetting(context, SP_SETT_NOTIF_LIVE);
    }

    public static void disableLiveNotification(Context context) {
        disableGenericSetting(context, SP_SETT_NOTIF_LIVE);
    }

    // live notification
    public static boolean isCompactNotificationEnabled(Context context) {
        return SharedPreferencesHelper.getSharedPreferenceBoolean(context, SP_SETT_NOTIF_COMPACT, true);
    }

    public static void enableCompactNotification(Context context) {
        enableGenericSetting(context, SP_SETT_NOTIF_COMPACT);
    }

    public static void disableCompactNotification(Context context) {
        disableGenericSetting(context, SP_SETT_NOTIF_COMPACT);
    }

    // auto removing notification
    public static boolean isAutoRemoveNotificationEnabled(Context context) {
        return SharedPreferencesHelper.getSharedPreferenceBoolean(context, SP_SETT_NOTIF_AUTO_REMOVE, true);
    }

    public static void enableAutoRemoveNotification(Context context) {
        enableGenericSetting(context, SP_SETT_NOTIF_AUTO_REMOVE);
    }

    public static void disableAutoRemoveNotification(Context context) {
        disableGenericSetting(context, SP_SETT_NOTIF_AUTO_REMOVE);
    }

}
