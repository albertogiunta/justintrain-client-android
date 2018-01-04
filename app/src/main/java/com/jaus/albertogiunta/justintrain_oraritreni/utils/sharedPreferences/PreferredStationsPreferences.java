package com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences;

import com.google.firebase.crash.FirebaseCrash;

import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.data.Journey;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;

import java.util.List;
import java.util.NoSuchElementException;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SEPARATOR;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_HOME_HEADER.FAVOURITES;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.StationsPreferences.buildSavedJourneyId;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.StationsPreferences.getAllSavedJourneys;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.StationsPreferences.getSavedJourney;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.StationsPreferences.isJourneyAlreadySaved;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.StationsPreferences.removeSavedJourney;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.StationsPreferences.setSavedJourney;

public class PreferredStationsPreferences {

    public static boolean isJourneyAlreadyPreferred(Context context, PreferredJourney preferredJourney) {
        return isJourneyAlreadyPreferred(context, preferredJourney.getStation1().getStationShortId(), preferredJourney.getStation2().getStationShortId());
    }

    public static boolean isJourneyAlreadyPreferred(Context context, PreferredStation departureStation, PreferredStation arrivalStation) {
        return isJourneyAlreadyPreferred(context, departureStation.getStationShortId(), arrivalStation.getStationShortId());
    }

    public static boolean isJourneyAlreadyPreferred(Context context, String id1, String id2) {
        return isJourneyAlreadySaved(context, FAVOURITES, id1, id2);
    }
//    ------------------------------------------------------------------------------------------------------------------------

    public static void setPreferredJourney(Context context, PreferredJourney journey) {
        setSavedJourney(context, FAVOURITES, journey);
        RecentStationsPreferences.removeRecentJourney(context, journey);
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
        removeSavedJourney(context, FAVOURITES, id1, id2);
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
        return getAllSavedJourneys(context, FAVOURITES);
    }

    public static PreferredJourney getPreferredJourney(Context context, PreferredJourney journey) {
        return getSavedJourney(context, FAVOURITES, journey);
    }

//    ------------------------------------------------------------------------------------------------------------------------

    public static String buildPreferredJourneyId(PreferredJourney preferredJourney) {
        return buildPreferredJourneyId(preferredJourney.getStation1().getStationShortId(), preferredJourney.getStation2().getStationShortId());
    }

    public static String buildPreferredJourneyId(PreferredStation departureStation, PreferredStation arrivalStation) {
        return buildPreferredJourneyId(departureStation.getStationShortId(), arrivalStation.getStationShortId());
    }

    public static String buildPreferredJourneyId(String id1, String id2) {
        return buildSavedJourneyId(FAVOURITES, id1, id2);
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
