package com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences;

import android.content.Context;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SP_IS_PRO;

public class ProPreferences {

    // instant delay
    public static boolean isPro(Context context) {
        return SharedPreferencesHelper.getSharedPreferenceBoolean(context, SP_SP_IS_PRO, false);
    }

    public static void enablePro(Context context) {
        SettingsPreferences.enableGenericSetting(context, SP_SP_IS_PRO);
    }

    public static void disablePro(Context context) {
        SettingsPreferences.disableGenericSetting(context, SP_SP_IS_PRO);
    }

}
