package com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers;

import com.google.firebase.analytics.FirebaseAnalytics;

import android.content.Context;
import android.os.Bundle;

public class AnalyticsHelper {

    /*

    adb shell setprop log.tag.FA VERBOSE
    adb shell setprop log.tag.FA-SVC VERBOSE
    adb logcat -v time -s FA FA-SVC

     */

    private FirebaseAnalytics firebase = null;

    private static AnalyticsHelper analyticsHelper;

    public static AnalyticsHelper getInstance(Context context) {
        if (analyticsHelper == null) {
            analyticsHelper = new AnalyticsHelper(context);
        }
        return analyticsHelper;
    }

    private AnalyticsHelper(Context context) {
        firebase = FirebaseAnalytics.getInstance(context);
        firebase.setMinimumSessionDuration(3000);
//        firebase.setUserId();
//        firebase.setUserProperty();
    }

    public void logScreenEvent(String screen, String action) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, screen);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, action);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, action);
        firebase.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
