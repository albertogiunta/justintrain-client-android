package com.jaus.albertogiunta.justintrain_oraritreni.utils;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

import android.content.Context;
import android.view.View;

import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;

import trikita.log.Log;

import static butterknife.ButterKnife.apply;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.GONE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.VISIBLE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.AD_CLICKED;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.AD_FAILED_TO_LOAD;

public class Ads {

    public static void initializeAds(Context context, NativeExpressAdView adView) {
        MobileAds.initialize(context, "ca-app-pub-8963908741443055~4285788324");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("EF67DDDDDDD0896B1B02876D927AC309")  // An example device ID
                .build();
        Log.d("initializeAds: ", adView.getAdUnitId());
        adView.loadAd(adRequest);
    }

    public static void initializeAds(Context context, View bannerPlaceholder, NativeExpressAdView adView, AnalyticsHelper analyticsHelper, String screenName) {
        initializeAds(context, adView);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                apply(bannerPlaceholder, GONE);
                apply(adView, VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                analyticsHelper.logScreenEvent(screenName, AD_FAILED_TO_LOAD);
            }

            @Override
            public void onAdClicked() {
                analyticsHelper.logScreenEvent(screenName, AD_CLICKED);
            }
        });
    }
}
