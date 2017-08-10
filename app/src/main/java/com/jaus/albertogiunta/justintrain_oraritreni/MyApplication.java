package com.jaus.albertogiunta.justintrain_oraritreni;

import android.app.Application;

import com.jaus.albertogiunta.justintrain_oraritreni.db.AppDatabase;

import net.danlew.android.joda.JodaTimeAndroid;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

//import com.facebook.stetho.Stetho;

public class MyApplication extends Application {

    public static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);

//        MobileAds.initialize(getApplicationContext(), "ca-app-pub-8963908741443055~4285788324");

        String fontPath = "fonts/rooneysans-medium.ttf";
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(fontPath)
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        if (database == null) {
            database = AppDatabase.getAppDatabase(getBaseContext());
        }

        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .build());
//            SPTestingHelper.setupSPv0(this);
//            SPTestingHelper.setupSPv1(this);
//            Stetho.initializeWithDefaults(this);
        }
    }
}