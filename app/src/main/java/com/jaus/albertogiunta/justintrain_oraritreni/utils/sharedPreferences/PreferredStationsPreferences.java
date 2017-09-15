package com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.data.Journey;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.DateTimeAdapter;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.PostProcessingEnabler;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.PREFIX_PREF_JOURNEY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SEPARATOR;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SharedPreferencesHelper.getSharedPreferenceObject;

public class PreferredStationsPreferences {

    public static boolean isJourneyAlreadyPreferred(Context context, PreferredJourney preferredJourney) {
        return isJourneyAlreadyPreferred(context, preferredJourney.getStation1().getStationShortId(), preferredJourney.getStation2().getStationShortId());
    }

    public static boolean isJourneyAlreadyPreferred(Context context, PreferredStation departureStation, PreferredStation arrivalStation) {
        return isJourneyAlreadyPreferred(context, departureStation.getStationShortId(), arrivalStation.getStationShortId());
    }

    public static boolean isJourneyAlreadyPreferred(Context context, String id1, String id2) {
        return getSharedPreferenceObject(context, buildPreferredJourneyId(id1, id2)) != null;
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static void setPreferredJourney(Context context, PreferredJourney journey) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();
        SharedPreferencesHelper.setSharedPreferenceObject(context,
                buildPreferredJourneyId(journey.getStation1().getStationShortId(),
                        journey.getStation2().getStationShortId()),
                gson.toJson(journey));
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static boolean isPossibleToSaveMorePreferredJourneys(Context context) {
        return getAllPreferredJourneys(context).size() < 5;
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static boolean isPossibleToSaveMorePreferredSolutions(PreferredStation station) {
        return station.getPreferredSolutions().size() < 3;
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static boolean isSolutionAlreadyPreferred(Context context, PreferredJourney journey, Journey.Solution solution) {
        if (isJourneyAlreadyPreferred(context, journey)) {
            String           id      = buildPreferredSolutionId(solution);
            PreferredStation station;
            try {
                station = getRightStationInPreferredJourney(journey, solution);
            } catch (Exception e) {
                FirebaseCrash.report(new Exception("COULDNT SET SOLUTION AS PREFERRED CAUSE OF THE FUCKIN NULLPOINTER (WHEN COMING FROM NOTIFICATION OF NON-COMPLETE SOLUTION)"));
                return false;
            }
            return station.getPreferredSolutions().get(id) != null;
        }
        return false;
    }

    private static PreferredStation getRightStationInPreferredJourney(PreferredJourney journey, Journey.Solution solution) {
        if (solution.getDepartureStationId().equals(journey.getStation1().getStationLongId())) {
            return journey.getStation1();
        } else {
            return journey.getStation2();
        }
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static void setPreferredSolution(Context context, PreferredJourney journey, Journey.Solution solution) throws IndexOutOfBoundsException, NoSuchElementException {
        if (isJourneyAlreadyPreferred(context, journey)) {

            PreferredStation station;
            try {
                station = getRightStationInPreferredJourney(journey, solution);
            } catch (Exception e) {
                FirebaseCrash.report(new Exception("COULDNT SET SOLUTION AS PREFERRED CAUSE OF THE FUCKIN NULLPOINTER (WHEN COMING FROM NOTIFICATION OF NON-COMPLETE SOLUTION)"));
                return;
            }

            if (!isPossibleToSaveMorePreferredSolutions(station)) {
                throw new IndexOutOfBoundsException("Impossible to save more than 3 solutions for this journey station");
            }

            if (!isSolutionAlreadyPreferred(context, journey, solution)) {
                PreferredJourney newJourney = getPreferredJourney(context, journey);
                if (solution.getDepartureStationId().equals(journey.getStation1().getStationLongId())) {
                    newJourney.getStation1().addPreferredSolutions(buildPreferredSolutionId(solution), solution);
                } else {
                    newJourney.getStation2().addPreferredSolutions(buildPreferredSolutionId(solution), solution);
                }
                setPreferredJourney(context, newJourney);
            }

        } else {
            throw new NoSuchElementException("Impossible to save solution of a non-preferred journey");
        }
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static void removePreferredJourney(Context context, PreferredStation departureStation, PreferredStation arrivalStation) {
        removePreferredJourney(context, departureStation.getStationShortId(), arrivalStation.getStationShortId());
    }

    public static void removePreferredJourney(Context context, String id1, String id2) {
        SharedPreferencesHelper.removeSharedPreferenceObject(context, buildPreferredJourneyId(id1, id2));
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static void removePreferredSolution(Context context, PreferredJourney journey, Journey.Solution solution) {
        if (isSolutionAlreadyPreferred(context, journey, solution)) {
            PreferredJourney newJourney = getPreferredJourney(context, journey);
            if (solution.getDepartureStationId().equals(journey.getStation1().getStationLongId())) {
                newJourney.getStation1().removePreferredSolutions(buildPreferredSolutionId(solution));
            } else {
                newJourney.getStation2().removePreferredSolutions(buildPreferredSolutionId(solution));
            }
            setPreferredJourney(context, newJourney);
        }
    }

    //    ------------------------------------------------------------------------------------------------------------------------

    public static List<PreferredJourney> getAllPreferredJourneys(Context context) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();
        List<PreferredJourney> list = new LinkedList<>();
        Map<String, ?>         m    = SharedPreferencesHelper.getAllMatchingPrefix(context, PREFIX_PREF_JOURNEY);
        for (String el : m.keySet()) {
            PreferredJourney temp = gson.fromJson(((String) m.get(el)), PreferredJourney.class);
            if (temp.getStation1() != null && temp.getStation2() != null) {
                list.add(temp);
            }
        }
        Collections.sort(list, (o1, o2) -> {
            if (o1.getTimestamp() < o2.getTimestamp()) {
                return -1;
            } else if (o1.getTimestamp() > o2.getTimestamp()) {
                return 1;
            } else {
                return 0;
            }
        });
        return list;
    }

    public static PreferredJourney getPreferredJourney(Context context, PreferredJourney journey) {
        return new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create().fromJson(getSharedPreferenceObject(context, buildPreferredJourneyId(journey)), PreferredJourney.class);
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static String buildPreferredJourneyId(PreferredJourney preferredJourney) {
        return buildPreferredJourneyId(preferredJourney.getStation1().getStationShortId(), preferredJourney.getStation2().getStationShortId());
    }

    public static String buildPreferredJourneyId(PreferredStation departureStation, PreferredStation arrivalStation) {
        return buildPreferredJourneyId(departureStation.getStationShortId(), arrivalStation.getStationShortId());
    }

    public static String buildPreferredJourneyId(String id1, String id2) {
        int cod1 = Integer.parseInt(id1);
        int cod2 = Integer.parseInt(id2);
        return PREFIX_PREF_JOURNEY + Math.min(cod1, cod2) + SEPARATOR + Math.max(cod1, cod2);
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static String buildPreferredSolutionId(Journey.Solution solution) {
        String id = "";
        if (solution.hasChanges()) {
            String separator = "";
            for (Journey.Solution.Change c : solution.getChangesList()) {
                id += (separator + (c.getTrainId()));
                separator = SEPARATOR;
            }
        } else {
            id = solution.getTrainId();
        }
        return id;
    }

//    ------------------------------------------------------------------------------------------------------------------------
}
