package com.jaus.albertogiunta.justintrain_oraritreni.journeyResults;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Pair;

import com.jaus.albertogiunta.justintrain_oraritreni.data.Journey;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.data.Station4Database;
import com.jaus.albertogiunta.justintrain_oraritreni.data.TrainHeader;
import com.jaus.albertogiunta.justintrain_oraritreni.journeyResults.JourneyResultsContract.View.JourneySearchStrategy.OnJourneySearchFinishedListener;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.APINetworkingFactory;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.DateTimeAdapter;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.JourneyService;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.PostProcessingEnabler;
import com.jaus.albertogiunta.justintrain_oraritreni.notification.NotificationService;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_ERROR_BTN_STATUS;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_SNACKBAR_ACTIONS;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.DatabaseHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.NetworkingHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.NotificationPreferences;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.PreferredStationsPreferences;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SettingsPreferences;

import org.joda.time.DateTime;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import trikita.log.Log;

import static com.jaus.albertogiunta.justintrain_oraritreni.journeyResults.JourneyResultsAdapter.INTERVAL;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ERROR_NOT_FOUND_JOURNEY_AFTER;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ERROR_NOT_FOUND_JOURNEY_BEFORE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ERROR_NOT_FOUND_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SCREEN_JOURNEY_RESULTS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_FROM_SWIPE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_STATIONS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_TIME;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.DatabaseHelper.isThisJourneyPreferred;

class JourneyResultsPresenter implements JourneyResultsContract.Presenter, OnJourneySearchFinishedListener {

    private JourneyResultsActivity view;

    private static List<Journey.Solution> journeySolutions;

    private PreferredStation departureStation;
    private PreferredStation arrivalStation;
    private DateTime         dateTime;
    private boolean          isSearchComingFromSwipe;
    private Gson             gson;

    JourneyResultsPresenter(JourneyResultsActivity view) {
        this.view = view;
        this.dateTime = DateTime.now();
        this.isSearchComingFromSwipe = false;
        journeySolutions = new LinkedList<>();
        gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();
    }

    @Override
    public void unsubscribe() {
        view = null;
    }

    @Override
    public void setState(Bundle bundle) {
        if (bundle != null) {
            // Restore value of members from saved state
            if (bundle.getString(I_STATIONS) != null) {
                PreferredJourney journey = gson.fromJson(bundle.getString(I_STATIONS), PreferredJourney.class);
                this.departureStation = journey.getStation1();
                this.arrivalStation = journey.getStation2();
                Log.d("Current bundled stations are: ", this.departureStation.toString(), this.arrivalStation.toString());
                view.setStationNames(departureStation.getNameLong(), arrivalStation.getNameLong());
                setFavouriteButtonStatus();
            }
            this.dateTime = new DateTime(bundle.getLong(I_TIME, DateTime.now().getMillis()));
            this.isSearchComingFromSwipe = bundle.getBoolean(I_FROM_SWIPE, false);
            Log.d("Current bundled DateTime is: ", dateTime);
        } else {
            Log.d("no bundle found");
        }
    }

    @Override
    public PreferredJourney getPreferredJourney() {
        return new PreferredJourney(departureStation, arrivalStation);
    }

    @Override
    public PreferredStation getDepartureStation() {
        return this.departureStation;
    }

    @Override
    public PreferredStation getArrivalStation() {
        return this.arrivalStation;
    }

    @Override
    public DateTime getTimeOfSearch() {
        return this.dateTime;
    }

    @Override
    public Bundle getState(Bundle bundle) {
        if (bundle == null) bundle = new Bundle();
        bundle.putString(I_STATIONS, gson.toJson(new PreferredJourney(departureStation, arrivalStation)));
        bundle.putLong(I_TIME, dateTime.getMillis());
        bundle.putBoolean(I_FROM_SWIPE, isSearchComingFromSwipe);
        return bundle;
    }

    @Override
    public void setFavouriteButtonStatus() {
        if (isThisJourneyPreferred(departureStation, arrivalStation, view.getViewContext())) {
            view.setFavouriteButtonStatus(true);
        } else {
            view.setFavouriteButtonStatus(false);
        }
    }

    @Override
    public void onLoadMoreItemsBefore() {
        new SearchBeforeTimeStrategy().searchJourney(false, departureStation.getStationShortId(),
                arrivalStation.getStationShortId(),
                journeySolutions.get(0).getDepartureTime().minusSeconds(1), false, false, false, this);
    }

//    @Override
//    public void searchInstantaneously() {
//        Log.d("searchInstantaneously: ");
//        view.showProgress();
//        new SearchInstantlyStrategy().searchJourney(true, departureStation.getStationShortId(),
//                arrivalStation.getStationShortId(),
//                null, true, true, this);
//    }

    @Override
    public void searchFromSearch(boolean isNewSearch) {
        view.showProgress();
        if (isSearchComingFromSwipe) {
            new SearchInstantlyStrategy().searchJourney(isNewSearch, departureStation.getStationShortId(),
                    arrivalStation.getStationShortId(),
                    null, SettingsPreferences.isPreemptiveEnabled(view.getViewContext()), true, true, this);
        } else {
            new SearchAfterTimeStrategy().searchJourney(isNewSearch, departureStation.getStationShortId(),
                    arrivalStation.getStationShortId(),
                    dateTime, false, true, false, this);
        }
    }

    @Override
    public void onFavouriteButtonClick() {
        if (isThisJourneyPreferred(departureStation, arrivalStation, view.getViewContext())) {
            PreferredStationsPreferences.removePreferredJourney(view.getViewContext(),
                    departureStation,
                    arrivalStation);
            setFavouriteButtonStatus();
            view.showSnackbar("Tratta rimossa dai Preferiti", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_SHORT);
        } else {
            if (PreferredStationsPreferences.isPossibleToSaveMoreJourneys(view.getViewContext())) {
                PreferredStationsPreferences.setPreferredJourney(view.getViewContext(),
                        new PreferredJourney(departureStation, arrivalStation));
                setFavouriteButtonStatus();
                view.showSnackbar("Tratta aggiunta ai Preferiti", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_SHORT);
            } else {
                view.showSnackbar("Impossibile salvare più di 5 tratte preferite!", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_LONG);
            }
        }
    }

    @Override
    public void onSwapButtonClick() {
        PreferredStation temp = departureStation;
        departureStation = arrivalStation;
        arrivalStation = temp;
        searchFromSearch(true);
        view.setStationNames(departureStation.getNameLong(), arrivalStation.getNameLong());
    }

    @Override
    public void onLoadMoreItemsAfter() {
        new SearchAfterTimeStrategy().searchJourney(false, departureStation.getStationShortId(),
                arrivalStation.getStationShortId(),
                journeySolutions.get(journeySolutions.size() - 1).getDepartureTime().plusMinutes(1), false, false, false, this);
    }

    @Override
    public void onNotificationRequested(int recyclerViewIndex) {
        int elementIndex = extractIndex(recyclerViewIndex);

        journeySolutions.get(elementIndex).setDepartureStationId(departureStation.getStationLongId());
        journeySolutions.get(elementIndex).setArrivalStationId(arrivalStation.getStationLongId());

        NotificationPreferences.setNotificationData(view.getViewContext(), new PreferredJourney(departureStation, arrivalStation), journeySolutions.get(elementIndex));
        NotificationService.startActionStartNotification(view.getViewContext(),
                departureStation,
                arrivalStation,
                journeySolutions.get(elementIndex),
                null);
        Log.d("onNotificationRequested: ", getSolutionList().get(elementIndex).toString());
    }

    @Override
    public void onJourneyRefreshRequested(int recyclerViewIndex) {
        int                               elementIndex = extractIndex(recyclerViewIndex);
        RealmResults<Station4Database>    stationList  = Realm.getDefaultInstance().where(Station4Database.class).findAll();
        Map<Pair<String, String>, String> m            = new HashMap<>();
        if (journeySolutions.get(elementIndex).hasChanges()) {
            for (int changeIndex = 0; changeIndex < journeySolutions.get(elementIndex).getChangesList().size(); changeIndex++) {
                try {
                    String           departureStationName = journeySolutions.get(elementIndex).getChangesList().get(changeIndex).getDepartureStationName();
                    String           arrivalStationName   = journeySolutions.get(elementIndex).getChangesList().get(changeIndex).getArrivalStationName();
                    Station4Database tempDepartureStation = DatabaseHelper.getStation4DatabaseObject(departureStationName, stationList);
                    Station4Database tempArrivalStation   = DatabaseHelper.getStation4DatabaseObject(arrivalStationName, stationList);

                    m.put(new Pair<>(tempDepartureStation.getStationShortId(), tempArrivalStation.getStationShortId()), journeySolutions.get(elementIndex).getChangesList().get(changeIndex).getTrainId());
                } catch (Exception e) {
                    AnalyticsHelper.getInstance(view.getViewContext()).logScreenEvent(SCREEN_JOURNEY_RESULTS, ERROR_NOT_FOUND_SOLUTION);
                    view.showSnackbar("Non riesco ad aggiornare questa soluzione", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_SHORT);
                }
            }
        } else {
            m.put(new Pair<>(departureStation.getStationShortId(), arrivalStation.getStationShortId()), journeySolutions.get(elementIndex).getTrainId());
        }
        Observable.concatDelayError(refreshChange(m)).subscribe(new Subscriber<TrainHeader>() {
            @Override
            public void onCompleted() {
                journeySolutions.get(elementIndex).refreshData();
                view.updateSolution(recyclerViewIndex);
                Log.d("onCompleted: ", getSolutionList().get(elementIndex).toString());
            }

            @Override
            public void onError(Throwable e) {
                if (e.getMessage().equals("HTTP 404 ")) {
                    AnalyticsHelper.getInstance(view.getViewContext()).logScreenEvent(SCREEN_JOURNEY_RESULTS, ERROR_NOT_FOUND_SOLUTION);
                    view.showSnackbar("Il treno potrebbe essere soppresso o avere cambiato codice", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_LONG);
                }
            }

            @Override
            public void onNext(TrainHeader trainHeader) {
                Integer          changeIndex = null;
                Journey.Solution sol         = journeySolutions.get(elementIndex);

                if (sol.hasChanges()) {
                    List<Journey.Solution.Change> changes = sol.getChangesList();
                    for (Journey.Solution.Change c : changes) {
                        if (trainHeader.getTrainId().equalsIgnoreCase(c.getTrainId()))
                            changeIndex = changes.indexOf(c);
                    }
                    if (changeIndex != null) {
                        changes.get(changeIndex).setDeparturePlatform(trainHeader.getDeparturePlatform());
                        if (trainHeader.isDeparted()) {
                            changes.get(changeIndex).setTimeDifference(trainHeader.getTimeDifference());
                            changes.get(changeIndex).setProgress(trainHeader.getProgress());
                            changes.get(changeIndex).postProcess();
                        } else if (sol.getTimeDifference() == null) {
                            view.showSnackbar("Il treno "
                                    + trainHeader.getTrainCategory()
                                    + " "
                                    + trainHeader.getTrainId()
                                    + " non è ancora partito", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_SHORT);
                        }
                    }
                } else {
                    sol.setDeparturePlatform(trainHeader.getDeparturePlatform());
                    if (trainHeader.isDeparted()) {
                        sol.setTimeDifference(trainHeader.getTimeDifference());
                        sol.setProgress(trainHeader.getProgress());
                    } else {
                        view.showSnackbar("Il treno non è ancora partito", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_SHORT);
                    }
                }

            }
        });
    }

    @Override
    public List<Journey.Solution> getSolutionList() {
        return journeySolutions;
    }

    @Override
    public void onJourneyNotFound() {
        FirebaseCrash.report(new Exception("JOURNEY RESULTS ERROR no journey found for " + departureStation.toString() + " " + arrivalStation.toString()));
        if (SettingsPreferences.isAnySearchFilterEnabled(view.getViewContext())) {
            view.showErrorMessage("La tratta non ha viaggi disponibili, oppure non ci sono soluzioni che rispettano i filtri di ricerca impostati", "Cambia stazioni", ENUM_ERROR_BTN_STATUS.NO_SOLUTIONS);
        } else {
            view.showErrorMessage("La tratta inserita non ha viaggi disponibili", "Cambia stazioni", ENUM_ERROR_BTN_STATUS.NO_SOLUTIONS);
        }
    }

    @Override
    public void onJourneyBeforeNotFound() {
        AnalyticsHelper.getInstance(view.getViewContext()).logScreenEvent(SCREEN_JOURNEY_RESULTS, ERROR_NOT_FOUND_JOURNEY_BEFORE);
        view.showSnackbar("Sembra non ci siano altre soluzioni in giornata", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onJourneyAfterNotFound() {
        AnalyticsHelper.getInstance(view.getViewContext()).logScreenEvent(SCREEN_JOURNEY_RESULTS, ERROR_NOT_FOUND_JOURNEY_AFTER);
        view.showSnackbar("Sembra non ci siano altre soluzioni in giornata", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onServerError(Throwable exception) {
        Log.d(exception.getMessage());
        Log.e(exception.toString());
        if (view != null) {
            if (exception.getMessage().equals("HTTP 404 ")) {
                onJourneyNotFound();
            } else if (exception instanceof HttpException) {
                Log.d(((HttpException) exception).response().errorBody(), ((HttpException) exception).response().code());
                if (((HttpException) exception).response().code() == 500) {
                    view.showErrorMessage("Il server sta avendo dei problemi", "Segnala il problema", ENUM_ERROR_BTN_STATUS.SEND_REPORT);
                } else if (((HttpException) exception).response().code() == 503) {
                    view.showErrorMessage("Il servizio Viaggiatreno di Trenitalia non è al momento disponibile.\nNon resta che aspettare...", "Aggiorna", ENUM_ERROR_BTN_STATUS.SERVICE_UNAVAILABLE);
                }
            } else if (exception instanceof ConnectException) {
                if (NetworkingHelper.isNetworkAvailable(view.getViewContext())) {
                    view.showErrorMessage("Si è verificato un problema", "Segnala il problema", ENUM_ERROR_BTN_STATUS.SEND_REPORT);
                } else {
                    view.showErrorMessage("Assicurati di essere connesso a Internet", "Attiva connessione", ENUM_ERROR_BTN_STATUS.CONN_SETTINGS);
                }
            } else if (exception.toString().equals("java.net.SocketTimeoutException: timeout")) {
                view.showErrorMessage("Assicurati di essere connesso a Internet", "Attiva connessione", ENUM_ERROR_BTN_STATUS.CONN_SETTINGS);
            }
        }
    }

    @Override
    public void onSuccess() {
        view.hideProgress();
        view.updateSolutionsList();
    }

    @Override
    public void onScrollToFirstRequested(int position) {
        if (position == -1) {
            position = 1;
            if (SettingsPreferences.isLightningEnabled(view)) {
                for (Journey.Solution s : journeySolutions) {
                    if (s.isArrivesFirst()) {
                        position = journeySolutions.indexOf(s) + 1;
                        break;
                    }
                }
            }
        }
        view.scrollToFirstFeasibleSolution(position);
    }

    @Override
    public Context getViewContext() {
        return view.getViewContext();
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    private List<Observable<TrainHeader>> refreshChange(Map<Pair<String, String>, String> info) {
        List<Observable<TrainHeader>> o = new LinkedList<>();
        for (Pair<String, String> p : info.keySet()) {
            try {
                Integer.parseInt(p.first);
                Integer.parseInt(p.second);
                Integer.parseInt(info.get(p));
            } catch (NumberFormatException e) {
                view.showSnackbar("Problema di aggiornamento", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_SHORT);
                Log.e("refreshChange: ", "There are problems with these parameters:", p.first, p.second, info.get(p));
                FirebaseCrash.report(new Exception(("REFRESH CHANGE ERROR There are problems with these parameters:" + p.first + " " + p.second + " " + info.get(p))));
                continue;
            }

            o.add(APINetworkingFactory.createRetrofitService(JourneyService.class)
                    .getDelay(p.first,
                            p.second,
                            info.get(p))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()));
        }
        return o;
    }

//    private boolean isComingFromSwipe(DateTime selectedHour) {
//        return selectedHour.getHourOfDay() == DateTime.now().getHourOfDay() &&
//                selectedHour.getMinuteOfHour() == DateTime.now().getMinuteOfHour();
//    }

    private int extractIndex(int i) {
        if (INTERVAL != 0) {
            return i - 1 - (int) Math.floor(i / INTERVAL);
        }
        return i - 1;
    }

    private static class SearchInstantlyStrategy implements JourneyResultsContract.View.JourneySearchStrategy {
        @Override
        public void searchJourney(boolean isNewSearch, String departureStationId, String arrivalStationId, DateTime timestamp, boolean isPreemptive, boolean withDelays, boolean includeTrainToBeTaken, OnJourneySearchFinishedListener listener) {
            Log.d(departureStationId, arrivalStationId, timestamp, isPreemptive, withDelays);
            APINetworkingFactory.createRetrofitService(JourneyService.class).getJourneyInstant(departureStationId, arrivalStationId, isPreemptive, SettingsPreferences.isIncludeChangesEnabled(listener.getViewContext()), SettingsPreferences.getEnabledCategoriesAsStringArray(listener.getViewContext()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Journey>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onServerError(e);
                        }

                        @Override
                        public void onNext(Journey solutionList) {
                            journeySolutions.clear();
                            journeySolutions.addAll(solutionList.getSolutions());
                            if (journeySolutions.size() > 0) {
                                listener.onSuccess();
                                listener.onScrollToFirstRequested(-1);
                            } else {
                                listener.onJourneyNotFound();
                            }
                        }
                    });
        }
    }

    private static class SearchAfterTimeStrategy implements JourneyResultsContract.View.JourneySearchStrategy {
        @Override
        public void searchJourney(boolean isNewSearch, String departureStationId, String arrivalStationId, DateTime timestamp, boolean isPreemptive, boolean withDelays, boolean includeTrainToBeTaken, OnJourneySearchFinishedListener listener) {
            APINetworkingFactory.createRetrofitService(JourneyService.class)
                    .getJourneyAfterTime(departureStationId, arrivalStationId, timestamp.toString("yyyy-MM-dd'T'HH:mmZ"), withDelays, isPreemptive, includeTrainToBeTaken, SettingsPreferences.isIncludeChangesEnabled(listener.getViewContext()), SettingsPreferences.getEnabledCategoriesAsStringArray(listener.getViewContext()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Journey>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onServerError(e);
                        }

                        @Override
                        public void onNext(Journey solutionList) {
                            Log.d(solutionList.getSolutions().size(), "new solutions found");
                            if (isNewSearch) {
                                journeySolutions.clear();
                            }
                            journeySolutions.addAll(solutionList.getSolutions());
                            if (journeySolutions.size() > 0) {
                                listener.onSuccess();
                                if (isNewSearch) listener.onScrollToFirstRequested(1);
                            } else {
                                listener.onJourneyAfterNotFound();
                            }
                        }
                    });
        }


    }

    private static class SearchBeforeTimeStrategy implements JourneyResultsContract.View.JourneySearchStrategy {
        @Override
        public void searchJourney(boolean isNewSearch, String departureStationId, String arrivalStationId, DateTime timestamp, boolean isPreemptive, boolean withDelays, boolean includeTrainToBeTaken, OnJourneySearchFinishedListener listener) {
            Log.d(timestamp);
            APINetworkingFactory.createRetrofitService(JourneyService.class).getJourneyBeforeTime(departureStationId, arrivalStationId, timestamp.toString("yyyy-MM-dd'T'HH:mmZ"), withDelays, isPreemptive, SettingsPreferences.isIncludeChangesEnabled(listener.getViewContext()), SettingsPreferences.getEnabledCategoriesAsStringArray(listener.getViewContext()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Journey>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onServerError(e);
                            Log.d(e.getMessage());
                        }

                        @Override
                        public void onNext(Journey solutionList) {
                            Log.d(solutionList.getSolutions().size(), "new solutions found");
                            if (solutionList.getSolutions().size() > 0) {
                                journeySolutions.addAll(0, solutionList.getSolutions());
                                listener.onSuccess();
                            } else {
                                listener.onJourneyBeforeNotFound();
                            }
                        }
                    });
        }
    }
}
