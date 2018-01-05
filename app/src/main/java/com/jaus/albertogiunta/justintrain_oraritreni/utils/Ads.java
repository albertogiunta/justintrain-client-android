package com.jaus.albertogiunta.justintrain_oraritreni.utils;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.jaus.albertogiunta.justintrain_oraritreni.BuildConfig;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;

import trikita.log.Log;

import static butterknife.ButterKnife.apply;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.GONE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.VISIBLE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.AD_CLICKED;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.AD_FAILED_TO_LOAD;

public class Ads {

    public static int BOTTOM_MARGIN_WITH_ADS    = 60;
    public static int BOTTOM_MARGIN_WITHOUT_ADS = 16;
    public static int BOTTOM_MARGIN_ACTUAL      = 16;

    public static boolean ignoreBeingProAndShowAds = BuildConfig.DEBUG && true;

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

    public static void removeAds(View bannerPlaceholder, NativeExpressAdView adView) {
        apply(bannerPlaceholder, GONE);
        apply(adView, GONE);
        adView.setEnabled(false);

        ViewGroup parent = (ViewGroup) adView.getParent();
        if (parent != null) {
            parent.removeView(adView);
            parent.invalidate();

            parent = (ViewGroup) bannerPlaceholder.getParent();
            parent.removeView(bannerPlaceholder);
            parent.invalidate();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static void initializeAds(Context context, AdView adView) {
        MobileAds.initialize(context, "ca-app-pub-8963908741443055~4285788324");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("EF67DDDDDDD0896B1B02876D927AC309")  // An example device ID
                .build();
        Log.d("initializeAds: ", adView.getAdUnitId());
        adView.loadAd(adRequest);
    }

    public static void initializeAds(Context context, View bannerPlaceholder, AdView adView, AnalyticsHelper analyticsHelper, String screenName) {
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

    public static void removeAds(View bannerPlaceholder, AdView adView) {

        boolean ignoreBeingProAndShowAds = false;
        if (BuildConfig.DEBUG && ignoreBeingProAndShowAds) {
            return;
        }

        if (bannerPlaceholder != null) apply(bannerPlaceholder, GONE);
        BOTTOM_MARGIN_ACTUAL = BOTTOM_MARGIN_WITHOUT_ADS;
        apply(adView, GONE);
        adView.setEnabled(false);

        if (bannerPlaceholder != null) {
            ViewGroup parent = (ViewGroup) adView.getParent();
            if (parent != null) {
                parent.removeView(adView);
                parent.invalidate();

                parent = (ViewGroup) bannerPlaceholder.getParent();
                parent.removeView(bannerPlaceholder);
                parent.invalidate();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static CoordinatorLayout.LayoutParams createParamsForBottomView(Context context) {
        int defaultMargin = ViewsUtils.convertDPtoPX(context, 16);
        int bottomMargin  = ViewsUtils.convertDPtoPX(context, BOTTOM_MARGIN_ACTUAL);

        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, ViewsUtils.convertDPtoPX(context, 50));
        params.topMargin = defaultMargin;
        params.leftMargin = defaultMargin;
        params.rightMargin = defaultMargin;
        params.bottomMargin = bottomMargin;
        params.gravity = Gravity.BOTTOM;

        return params;
    }
}
