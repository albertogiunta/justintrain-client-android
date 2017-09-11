package com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences;

import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.BuildConfig;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_CATEGORIES;

import java.util.ArrayList;
import java.util.List;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.PREFIX_CATEGORIES;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_GET_PREEMPTIVE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_INCLUDE_CHANGES;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_LIGHTNING;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_NOTIF_VIBRATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_FIRST_START;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_SAVED_VERSION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_VERSION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_VERSION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.ProPreferences.disableCompactNotification;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.ProPreferences.disableInstantDelay;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.ProPreferences.disableLiveNotification;

public class SettingsPreferences {


    // vibration
    public static boolean isVibrationEnabled(Context context) {
        return SharedPreferencesHelper.getSharedPreferenceBoolean(context, SP_SETT_NOTIF_VIBRATION, true);
    }

    public static void enableVibration(Context context) {
        enableGenericSetting(context, SP_SETT_NOTIF_VIBRATION);
    }

    public static void disableVibration(Context context) {
        disableGenericSetting(context, SP_SETT_NOTIF_VIBRATION);
    }

    // lightning
    public static boolean isLightningEnabled(Context context) {
        return SharedPreferencesHelper.getSharedPreferenceBoolean(context, SP_SETT_LIGHTNING, false);
    }

    public static void enableLightning(Context context) {
        enableGenericSetting(context, SP_SETT_LIGHTNING);
    }

    public static void disableLightning(Context context) {
        disableGenericSetting(context, SP_SETT_LIGHTNING);
    }

    // preemptive
    public static boolean isPreemptiveEnabled(Context context) {
        return SharedPreferencesHelper.getSharedPreferenceBoolean(context, SP_SETT_GET_PREEMPTIVE, true);
    }

    public static void enablePreemptive(Context context) {
        enableGenericSetting(context, SP_SETT_GET_PREEMPTIVE);
    }

    public static void disablePreemptive(Context context) {
        disableGenericSetting(context, SP_SETT_GET_PREEMPTIVE);
    }

    // changes
    public static boolean isIncludeChangesEnabled(Context context) {
        return SharedPreferencesHelper.getSharedPreferenceBoolean(context, SP_SETT_INCLUDE_CHANGES, true);
    }

    public static void enableIncludeChanges(Context context) {
        enableGenericSetting(context, SP_SETT_INCLUDE_CHANGES);
    }

    public static void disableIncludeChanges(Context context) {
        disableGenericSetting(context, SP_SETT_INCLUDE_CHANGES);
    }

    // train categories
    public static boolean isCategoryEnabled(Context context, ENUM_CATEGORIES category) {
        return isGenericSettingEnabled(context, PREFIX_CATEGORIES + category.name(), true);
    }

    public static void enableAllCategories(Context context) {
        for (ENUM_CATEGORIES cat : ENUM_CATEGORIES.values()) {
            enableCategory(context, cat.name());
        }
    }

    public static void disableAllCategories(Context context) {
        for (ENUM_CATEGORIES cat : ENUM_CATEGORIES.values()) {
            disableCategory(context, cat.name());
        }
    }

    public static void enableCategory(Context context, String catName) {
        enableGenericSetting(context, PREFIX_CATEGORIES + catName);
    }

    public static void disableCategory(Context context, String catName) {
        disableGenericSetting(context, PREFIX_CATEGORIES + catName);
    }

    public static void setCategory(Context context, String catName, boolean isEnabled) {
        if (isEnabled) {
            enableCategory(context, catName);
        } else {
            disableCategory(context, catName);
        }
    }

    public static boolean isPossibleToDisableCheckbox(Context context) {
        return getEnabledCategoriesAsStringList(context).size() > 1;
    }

    public static String[] getEnabledCategoriesAsStringArray(Context context) {
        return getEnabledCategoriesAsStringList(context).toArray(new String[0]);
    }

    public static List<String> getEnabledCategoriesAsStringList(Context context) {
        List<String> categoriesArray = new ArrayList<>();
        for (ENUM_CATEGORIES cat : ENUM_CATEGORIES.values()) {
            if (isCategoryEnabled(context, cat)) {
                categoriesArray.add(cat.name());
            }
        }
        return categoriesArray;
    }

    public static String getCategoriesAsString(Context context, String separator) {
        String categoriesString = "";
        for (String s : getEnabledCategoriesAsStringList(context)) {
            categoriesString += s + separator;
        }
        if (categoriesString.length() > separator.length()) {
            categoriesString = categoriesString.substring(0, categoriesString.length() - separator.length());
        }
        return categoriesString;
    }


    // version code
    public static int getPreviouslySavedVersionCode(Context context) {
        return SharedPreferencesHelper.getSharedPreferenceInt(context, SP_SP_SAVED_VERSION, 0);
    }

    public static void setCurrentVersionCode(Context context) {
        SharedPreferencesHelper.setSharedPreferenceInt(context, SP_SP_SAVED_VERSION, BuildConfig.VERSION_CODE);
    }

    // generic
    public static boolean isGenericSettingEnabled(Context context, String CONST, boolean defaultValue) {
        return SharedPreferencesHelper.getSharedPreferenceBoolean(context, CONST, defaultValue);
    }

    public static void enableGenericSetting(Context context, String CONST) {
        SharedPreferencesHelper.setSharedPreferenceBoolean(context, CONST, true);
    }

    public static void disableGenericSetting(Context context, String CONST) {
        SharedPreferencesHelper.setSharedPreferenceBoolean(context, CONST, false);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static void setNewSettingsNotPreviouslyIncludedBefore6(Context context) {
        // 6 - 0.7.4
        disableLightning(context);
    }

    private static void setNewSettingsNotPreviouslyIncludedBefore12(Context context) {
        // 12 - 0.8.2
        enableVibration(context);
        enablePreemptive(context);
    }

    private static void setNewSettingsNotPreviouslyIncludedBefore13(Context context) {
        // 13 - 0.8.4
        enableIncludeChanges(context);
        enableAllCategories(context);
    }

    private static void setNewSettingsNotPreviouslyIncludedBefore30(Context context) {
        // 27 - 1.0.0
        disableInstantDelay(context);
        disableLiveNotification(context);
    }

    private static void setNewSettingsNotPreviouslyIncludedBefore47(Context context) {
        // 43 - 1.0.0
        disableCompactNotification(context);
    }

    public static void setDefaultSharedPreferencesOnFirstStart(Context context) {
        // preferences
        SharedPreferencesHelper.setSharedPreferenceBoolean(context, SP_SP_FIRST_START, false);
        SharedPreferencesHelper.setSharedPreferenceInt(context, SP_SP_SAVED_VERSION, BuildConfig.VERSION_CODE);
        SharedPreferencesHelper.setSharedPreferenceInt(context, SP_SP_VERSION, SP_VERSION);

        // settings screen
        setNewSettingsNotPreviouslyIncludedBefore6(context);
        setNewSettingsNotPreviouslyIncludedBefore12(context);
        setNewSettingsNotPreviouslyIncludedBefore13(context);
        setNewSettingsNotPreviouslyIncludedBefore30(context);
        setNewSettingsNotPreviouslyIncludedBefore47(context);
    }

    // inclusion of settings - update together with onFirstStart
    public static void setNewSettingsNotPreviouslyIncludedBefore(Context context) {
        if (getPreviouslySavedVersionCode(context) < 12) {
            setNewSettingsNotPreviouslyIncludedBefore12(context);
        }
        if (getPreviouslySavedVersionCode(context) < 13) {
            setNewSettingsNotPreviouslyIncludedBefore13(context);
        }
        if (getPreviouslySavedVersionCode(context) < 30) {
            setNewSettingsNotPreviouslyIncludedBefore30(context);
        }
        if (getPreviouslySavedVersionCode(context) < 47) {
            setNewSettingsNotPreviouslyIncludedBefore47(context);
        }
    }

    private static boolean areAllCategoriesEnabled(Context context) {
        return getEnabledCategoriesAsStringArray(context).length == ENUM_CATEGORIES.values().length;
    }

    public static boolean isAnySearchFilterEnabled(Context context) {
        return !isIncludeChangesEnabled(context) || !areAllCategoriesEnabled(context);
    }
}
