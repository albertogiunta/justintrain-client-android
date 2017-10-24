package com.jaus.albertogiunta.justintrain_oraritreni.trainDetails;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.jaus.albertogiunta.justintrain_oraritreni.data.Journey;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.data.Train;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.APINetworkingFactory;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.DateTimeAdapter;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.PostProcessingEnabler;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.TrainService;
import com.jaus.albertogiunta.justintrain_oraritreni.notification.NotificationService;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_ERROR_BTN_STATUS;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_SNACKBAR_ACTIONS;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.NetworkingHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.NotificationPreferences;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.PreferredStationsPreferences;

import org.joda.time.DateTime;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.HttpException;
import trikita.log.Log;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_STATIONS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_TRAIN;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_SNACKBAR_ACTIONS.NONE;

class TrainDetailsPresenter implements TrainDetailsContract.Presenter {

    private TrainDetailsContract.View view;
    private List<Train>               trainList;
    private List<Object>              trainStopList;
    private List<String>              trainIdList;
    private List<Integer>             busesIndexList;
    private Journey.Solution          solution;
    private PreferredJourney          preferredJourney;
    private boolean isOnlyTrain = false;

    TrainDetailsPresenter(TrainDetailsContract.View view) {
        this.view = view;
        trainList = new ArrayList<>();
        trainStopList = new ArrayList<>();
        busesIndexList = new ArrayList<>();
        trainIdList = new ArrayList<>();
    }

    @Override
    public void setState(Bundle bundle) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();

        if (bundle != null) {
            // Restore value of members from saved state
            if (bundle.getString(I_TRAIN) != null) {
                trainIdList.clear();
                trainIdList.add(bundle.getString(I_TRAIN));
                isOnlyTrain = true;
                return;
            }
            List<String> shareableTrainList = new LinkedList<>();
            if (bundle.getString(I_SOLUTION) != null) {
                isOnlyTrain = false;
                solution = gson.fromJson(bundle.getString(I_SOLUTION), Journey.Solution.class);
                if (solution.hasChanges()) {
                    Arrays.copyOf(trainList.toArray(), solution.getChangesList().size());
                    for (Journey.Solution.Change c : solution.getChangesList()) {
                        shareableTrainList.add(c.getTrainCategory() + " " + c.getTrainId());
                    }
                } else {
                    shareableTrainList.add(solution.getTrainCategory() + " " + solution.getTrainId());
                }
                if (view != null) view.setShareButton(shareableTrainList);
            }
            if (bundle.getString(I_STATIONS) != null) {
                isOnlyTrain = false;
                preferredJourney = gson.fromJson(bundle.getString(I_STATIONS), PreferredJourney.class);
                updatePreferredJourneyFromFavouritesIfAvailable();
            }
        } else {
            Log.d("no bundle found");
        }
    }

    private void updatePreferredJourneyFromFavouritesIfAvailable() {
        if (PreferredStationsPreferences.isJourneyAlreadyPreferred(view.getViewContext(), preferredJourney)) {
            preferredJourney = PreferredStationsPreferences.getPreferredJourney(view.getViewContext(), this.preferredJourney);
        }
    }

    @Override
    public Bundle getState(Bundle bundle) {
        return bundle;
    }

    @Override
    public void unsubscribe() {
        this.view = null;
    }

    @Override
    public void updateRequested() {
        view.showProgress();

        if (trainIdList.size() == 0) {
            trainIdList = getTrainIdList();
        }

        if (trainIdList.size() == 0) {
            onComplete();
            return;
        }

        List<Train> newTrainList = new LinkedList<>();

        Observable.concatDelayError(Observable.fromIterable(searchTrainDetails(trainIdList))).subscribe(
                train -> {
                    newTrainList.add(train);
                }, throwable -> {
                    if (throwable == null) {
                        view.showErrorMessage("Si è verificato un errore", "Torna alle soluzioni", ENUM_ERROR_BTN_STATUS.NO_SOLUTIONS);
                        return;
                    }
                    Log.d(throwable.getMessage());
                    if (view != null) {
                        if (throwable.getMessage().equals("HTTP 404 ")) {
                            if (solution != null && solution.hasChanges()) {
                                FirebaseCrash.report(new Exception("TRAIN DETAIL ERROR solution is " + solution.toString()));
                                view.showSnackbar("Uno o più treni non sono ancora disponibili", NONE, Snackbar.LENGTH_LONG);
                                onComplete();
                            } else {
                                if (isOnlyTrain) {
                                    view.showErrorMessage("Le informazioni su questo treno purtroppo non sono ancora disponibili oppure potresti aver sbagliato ad inserire il numero del treno", "Torna indietro", ENUM_ERROR_BTN_STATUS.NO_SOLUTIONS);
                                } else {
                                    view.showErrorMessage("Le informazioni su questo treno purtroppo non sono ancora disponibili", "Torna alle soluzioni", ENUM_ERROR_BTN_STATUS.NO_SOLUTIONS);
                                }
                            }
                        } else {
                            Log.e("onServerError: ", throwable.toString());
                            if (throwable instanceof HttpException) {
                                Log.d(((HttpException) throwable).response().errorBody(), ((HttpException) throwable).response().code());
                                if (((HttpException) throwable).response().code() == 500) {
                                    view.showErrorMessage("Il server sta avendo dei problemi", "Segnala il problema", ENUM_ERROR_BTN_STATUS.SEND_REPORT);
                                }
                            } else if (throwable instanceof ConnectException) {
                                if (NetworkingHelper.isNetworkAvailable(view.getViewContext())) {
                                    view.showErrorMessage("Il server sta avendo dei problemi", "Segnala il problema", ENUM_ERROR_BTN_STATUS.SEND_REPORT);
                                } else {
                                    view.showErrorMessage("Assicurati di essere connesso a Internet", "Attiva connessione", ENUM_ERROR_BTN_STATUS.CONN_SETTINGS);
                                }
                            } else if (throwable instanceof SocketTimeoutException) {
                                view.showErrorMessage("Assicurati di essere connesso a Internet", "Attiva connessione", ENUM_ERROR_BTN_STATUS.CONN_SETTINGS);
                            }
                        }
                    }
                }, () -> {
                    trainList.clear();
                    trainList.addAll(newTrainList);
                    onComplete();
                }
        );
    }

    private void onComplete() {
        if (view == null) {
            return;
        }
        insertBuses();
        getFlatTrainList();
        view.hideProgress();
        view.updateTrainDetails();
    }

    @Override
    public void refreshRequested() {
        trainList.clear();
        trainStopList.clear();
        view.updateTrainDetails();
        updateRequested();
    }

    @Override
    public void onFavouriteButtonClick() {
        if (isSolutionPreferred()) {
            // remove from favourites
            PreferredStationsPreferences.removePreferredSolution(view.getViewContext(), this.preferredJourney, this.solution);
            view.setFavouriteButtonStatus(false);
        } else {
            // add to favourites
            try {
                PreferredStationsPreferences.setPreferredSolution(view.getViewContext(), this.preferredJourney, this.solution);
                view.setFavouriteButtonStatus(true);
            } catch (IndexOutOfBoundsException e) {
                view.showSnackbar("Impossibile salvare più di 3 soluzioni preferite per questa stazione!", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_LONG);
            } catch (NoSuchElementException e) {
                view.showAddSolutionAndJourneyToFavouritesDialog();
            }
        }
        updatePreferredJourneyFromFavouritesIfAvailable();
    }

    @Override
    public void onDoubleAdditionConfirmation() {
        PreferredStationsPreferences.setPreferredJourney(view.getViewContext(), this.preferredJourney);
        PreferredStationsPreferences.setPreferredSolution(view.getViewContext(), this.preferredJourney, this.solution);
        view.setFavouriteButtonStatus(true);
    }

    @Override
    public boolean isSolutionPreferred() {
        return PreferredStationsPreferences.isSolutionAlreadyPreferred(view.getViewContext(), this.preferredJourney, this.solution);
    }

    @Override
    public Journey.Solution getSolution() {
        return this.solution;
    }

    private Iterable<Observable<Train>> searchTrainDetails(List<String> trainIdList) {
        Log.d("searchTrainDetails: searching train with id:", trainIdList);
        List<Observable<Train>> o = new LinkedList<>();
        for (String s : trainIdList) {
            o.add(APINetworkingFactory.createRetrofitService(TrainService.class)
                    .getTrainDetails(s)
                    .observeOn(AndroidSchedulers.mainThread()));
        }
        return o;
    }

    @Override
    public List<Object> getFlatTrainList() {
        trainStopList.clear();
        view.updateTrainDetails();
        for (int i = 0; i < trainList.size(); i++) {
            Train            t  = new Train(trainList.get(i));
            List<Train.Stop> sl = t.getStops() != null ? t.getStops() : new LinkedList<>();
            t.setStops(null);
            trainStopList.add(t);
            trainStopList.addAll(sl);
        }
        return trainStopList;
    }

    @Override
    public Train getTrainForAdapterPosition(int position) {
        int past = 0;
        for (Train t : trainList) {
            if (position <= past + t.getStops().size()) {
                return t;
            }
            past += t.getStops().size() + 1;
        }
        return null;
    }

    @Override
    public Integer getTrainIndexForAdapterPosition(int position) {
        int past = 0;
        for (Train t : trainList) {
            if (position <= past + t.getStops().size()) {
                return trainList.indexOf(t);
            }
            past += t.getStops().size() + 1;
        }
        return null;
    }

    @Override
    public void onNotificationRequested(int position) {
        NotificationPreferences.setNotificationData(view.getViewContext(), preferredJourney, solution);

        PreferredStation departureStation;
        PreferredStation arrivalStation;

        if (preferredJourney.getStation1().getStationLongId() != null &&
                preferredJourney.getStation1().getStationLongId().equals(solution.getDepartureStationId())) {
            departureStation = preferredJourney.getStation1();
            arrivalStation = preferredJourney.getStation2();
        } else {
            departureStation = preferredJourney.getStation2();
            arrivalStation = preferredJourney.getStation1();
        }

        NotificationService.startActionStartNotification(view.getViewContext(),
                departureStation,
                arrivalStation,
                solution,
                getIndexOfTrainFromPosition(position), true, true);
    }

    @Override
    public String getShareableString() {
        updateRequested();
        String s = solution.getTrainCategory() + " " + solution.getTrainId() + "\n";
        if (solution.getTimeDifference() != null) {
            if (solution.getTimeDifference() > 0) {
                s += "Ritardo: " + solution.getTimeDifference() + " ";
                s += solution.getTimeDifference() == 1 ? "minuto\n" : "minuti\n";
            } else {
                s += "In orario" + "\n";
            }
        }

        s += "Partenza prevista alle " + solution.getDepartureTimeWithDelayReadable() + " da " + solution.getDepartureStationName() + "\n";
        s += "Arrivo previsto alle " + solution.getArrivalTimeWithDelayReadable() + " a " + solution.getArrivalStationName() + "\n";

        if (trainList != null && !trainList.isEmpty() && trainList.get(0) != null && trainList.get(0).getStops() != null) {
            for (Train.Stop stop : trainList.get(0).getStops()) {
                if (stop != null && stop.getStationId() != null && solution.getArrivalStationId() != null &&
                        stop.getStationId().equals(solution.getArrivalStationId()) && stop.getDeparturePlatform() != null) {
                    s += "Arrivo previsto al binario " + stop.getDeparturePlatform() + "\n";
                    break;
                }
            }
        }

        s += "\n - via JustInTrain - Orario Treni Trenitalia.\n";
        return s;
    }

    @Override
    public String getShareableString(int index) {
        updateRequested();
        Journey.Solution.Change change = solution.getChangesList().get(index);
        String                  s      = change.getTrainCategory() + " " + change.getTrainId() + "\n";
        if (change.getTimeDifference() != null) {
            if (change.getTimeDifference() > 0) {
                s += "Ritardo: " + change.getTimeDifference() + "\n";
            } else {
                s += "In orario" + "\n";
            }
        }
        s += "Partenza prevista alle " + change.getDepartureTimeWithDelayReadable() + " da " + change.getDepartureStationName() + "\n";
        s += "Arrivo previsto alle " + change.getArrivalTimeWithDelayReadable() + " da " + change.getArrivalStationName() + "\n";

        if (trainList != null && !trainList.isEmpty() && index < trainList.size() && trainList.get(index) != null && trainList.get(index).getStops() != null) {
            for (Train.Stop stop : trainList.get(index).getStops()) {
                if (stop != null && stop.getStationId() != null && solution.getArrivalStationId() != null &&
                        stop.getStationId().equals(solution.getArrivalStationId()) && stop.getDeparturePlatform() != null) {
                    s += "Arrivo previsto al binario " + stop.getDeparturePlatform() + "\n";
                    break;
                }
            }
        }

        s += "\n - via JustInTrain - Orario Treni Trenitalia.\n";
        return s;
    }

    @Override
    public void onNewsUpdateRequested(String trainId) {
        APINetworkingFactory.createRetrofitService(TrainService.class).getTrainNews(trainId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> {
                    view.showNewsDialog(news);
                }, throwable -> {
                    FirebaseCrash.report(new Exception("ERROR ON NEWS UPDATE for train" + trainId));
                });
    }

    @Override
    public boolean isOnlyTrainSearch() {
        return isOnlyTrain;
    }

    private void insertBuses() {
        // solution is only 1 train
        if (busesIndexList.size() == 1 && busesIndexList.get(0) == -1) {
            trainList.add(new Train(solution));
        } else {
            if (solution != null && solution.getChangesList() != null) {
                for (Integer busIndex : busesIndexList) {
                    trainList.add(busIndex, new Train(solution.getChangesList().get(busIndex)));
                }
            }
        }
    }

    private List<String> getTrainIdList() {
        List<String> trainIdList = new LinkedList<>();
        if (solution.hasChanges()) {
            for (Journey.Solution.Change c : solution.getChangesList()) {
                if (!c.getTrainCategory().equalsIgnoreCase("BUS") && !c.getTrainId().equalsIgnoreCase("Urb")) {
                    trainIdList.add(c.getTrainId());
                } else {
                    busesIndexList.add(solution.getChangesList().indexOf(c));
                }
            }
        } else {
            if (!solution.getTrainCategory().equalsIgnoreCase("BUS") && !solution.getTrainId().equalsIgnoreCase("Urb")) {
                trainIdList.add(solution.getTrainId());
            } else {
                busesIndexList.add(-1);
            }
        }
        return trainIdList;
    }

    private Integer getIndexOfTrainFromPosition(int position) {
        int pos = position;
        for (Train t : trainList) {
            if (position == 0) return 0;
            if (pos - t.getStops().size() < 0) {
                return pos;
            }
            pos -= t.getStops().size();
        }
        return null;
    }
}
