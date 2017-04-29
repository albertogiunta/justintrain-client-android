package com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers;

import android.content.Context;
import android.content.res.Resources;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.data.Station4Database;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.PreferredStationsPreferences;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

@SuppressWarnings("unused")
public class DatabaseHelper {

    private static RealmResults<Station4Database> stationList = Realm.getDefaultInstance().where(Station4Database.class).findAll();

    public static List<String> getElementByNameLong(String stationName) {
        return Stream
                .of(stationList.where().beginsWith("nameLong", stationName, Case.INSENSITIVE).findAll())
                .map(Station4Database::getNameLong).collect(Collectors.toList());
    }

    public static List<String> getElementByNameShort(String stationName) {
        return Stream
                .of(stationList.where().beginsWith("nameShort", stationName, Case.INSENSITIVE).findAll())
                .map(Station4Database::getNameLong).collect(Collectors.toList());
    }

    public static boolean isThisJourneyPreferred(PreferredStation departureStation, PreferredStation arrivalStation, Context context) {
        return departureStation != null &&
                arrivalStation != null &&
                PreferredStationsPreferences.isJourneyAlreadyPreferred(
                        context,
                        departureStation,
                        arrivalStation);
    }

    public static boolean isStationNameValid(String stationName, RealmResults<Station4Database> stationList) {
        RealmResults<Station4Database> matchingStations = stationList.where().equalTo("nameLong", stationName, Case.INSENSITIVE).findAll();
        matchingStations = matchingStations.size() == 0 ? stationList.where().equalTo("nameShort", stationName, Case.INSENSITIVE).findAll() : matchingStations;
        return matchingStations.size() == 1 && !stationName.isEmpty();
    }

    public static Station4Database getStation4DatabaseObject(String stationNameBetterIfShort, RealmResults<Station4Database> stationList) {
        Station4Database temp;
        if (stationNameBetterIfShort == null) {
            return null;
        }
        try {
            temp = stationList.where().equalTo("nameShort", stationNameBetterIfShort, Case.INSENSITIVE).findAll().get(0);
        } catch (Exception e) {
            try {
                temp = stationList.where().equalTo("nameLong", stationNameBetterIfShort, Case.INSENSITIVE).findAll().get(0);
            } catch (Exception e1) {
                throw new Resources.NotFoundException();
            }
        }
        return temp;
    }

    public static Station4Database getStation4DatabaseObject(String stationName) {
        return getStation4DatabaseObject(stationName, Realm.getDefaultInstance().where(Station4Database.class).findAll());
    }
}
