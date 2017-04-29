package com.jaus.albertogiunta.justintrain_oraritreni;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.internal.IOException;

//import com.facebook.stetho.Stetho;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        Realm.init(this);

        String fontPath = "fonts/rooneysans-medium.ttf";
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(fontPath)
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

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
        copyBundledRealmFile(this.getResources().openRawResource(R.raw.station), Realm.DEFAULT_REALM_NAME);
    }

    private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
        try {
            File             file         = new File(this.getFilesDir(), outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[]           buf          = new byte[1024];
            int              bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException | java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}