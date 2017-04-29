package com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.journeyFavourites.FavouriteJourneysActivity;
import com.jaus.albertogiunta.justintrain_oraritreni.journeyResults.JourneyResultsActivity;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.DateTimeAdapter;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.PostProcessingEnabler;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.PreferredStationsPreferences;

import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_STATIONS;

public class ShortcutHelper {

    public static void updateShortcuts(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
                shortcutManager.removeAllDynamicShortcuts();
                List<PreferredJourney> l;
            if ((l = PreferredStationsPreferences.getAllPreferredJourneys(context)).size() > 0) {
                    shortcutManager.addDynamicShortcuts(setShortcuts(context, l.get(0)));
                }
        }
    }

    private static List<ShortcutInfo> setShortcuts(Context context, PreferredJourney j) {
        Intent i1 = buildIntent(context, j);
        Intent i2 = buildIntent(context, j.swapStations());
        j.swapStations();

        ShortcutInfo       shortcut1 = buildShortcut(context, i1, j.getStation1().getNameShort(), j.getStation2().getNameShort(), "shortcut_dynamic1");
        ShortcutInfo       shortcut2 = buildShortcut(context, i2, j.getStation2().getNameShort(), j.getStation1().getNameShort(), "shortcut_dynamic2");
        List<ShortcutInfo> l         = new LinkedList<>();
        l.add(shortcut2);
        l.add(shortcut1);
        return l;
    }

    private static Intent buildIntent(Context context, PreferredJourney journey) {
        Intent intent = new Intent(context, JourneyResultsActivity.class);
        intent.putExtras(bundleJourney(journey));
        intent.setAction(JourneyResultsActivity.ACTION);
        return intent;
    }

    private static ShortcutInfo buildShortcut(Context context, Intent i, String name1, String name2, String id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            return new ShortcutInfo.Builder(context, id)
                    .setShortLabel(shortenStationName(name1) + " > " + shortenStationName(name2))
                    .setLongLabel(name1 + " > " + name2)
                    .setIcon(Icon.createWithResource(context, R.drawable.ic_arrow_forward))
                    .setIntents(new Intent[]{
                            new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, FavouriteJourneysActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                            i,
                    })
                    .build();
        } else {
            return null;
        }
    }

    private static Bundle bundleJourney(PreferredJourney journey) {
        Bundle bundle = new Bundle();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();

        bundle.putString(I_STATIONS, gson.toJson(journey));
        return bundle;
    }

    private static String shortenStationName(String s) {
        return s.length() > 5 ? s.substring(0, 5).concat("...") : s;
    }
}
