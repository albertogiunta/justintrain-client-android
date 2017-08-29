package com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers;

import android.content.Context;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.jaus.albertogiunta.justintrain_oraritreni.MyApplication;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.db.Station;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.PreferredStationsPreferences;

import java.util.List;


public class DatabaseHelper {

    public static List<String> getElementByNameFancy(String stationName) {
        return Stream
                .of(MyApplication.database.stationDao().getAllByNameFancy(stationName + "%", "% " + stationName + "%"))
                .map(Station::getNameFancy).collect(Collectors.toList());
    }

    public static List<String> getElementByNameShort(String stationName) {
        return Stream
                .of(MyApplication.database.stationDao().getAllByNameShort(stationName))
                .map(Station::getNameLong).collect(Collectors.toList());
    }

    public static boolean isThisJourneyPreferred(PreferredStation departureStation, PreferredStation arrivalStation, Context context) {
        return departureStation != null &&
                arrivalStation != null &&
                PreferredStationsPreferences.isJourneyAlreadyPreferred(
                        context,
                        departureStation,
                        arrivalStation);
    }

    public static boolean isStationNameValid(String stationName) {
        return MyApplication.database.stationDao().isStationNameValid(stationName);
    }

    public static Station getStation4DatabaseObject(String stationName) {
        return MyApplication.database.stationDao().getByWhateverName(stationName);
    }
}
