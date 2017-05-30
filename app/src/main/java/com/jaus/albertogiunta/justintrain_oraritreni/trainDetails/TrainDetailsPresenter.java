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
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_SNACKBAR_ACTIONS.NONE;

class TrainDetailsPresenter implements TrainDetailsContract.Presenter {

    private TrainDetailsContract.View view;
    private List<Train>               trainList;
    private List<Object>              trainStopList;
    List<String> trainIdList;
    private Journey.Solution solution;
    private PreferredJourney preferredJourney;

    TrainDetailsPresenter(TrainDetailsContract.View view) {
        this.view = view;
        trainList = new ArrayList<>();
        trainStopList = new ArrayList<>();
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
            List<String> shareableTrainList = new LinkedList<>();
            if (bundle.getString(I_SOLUTION) != null) {
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
                Log.d("Current bundled solution is: ", solution.toString());
            }
            if (bundle.getString(I_STATIONS) != null) {
                preferredJourney = gson.fromJson(bundle.getString(I_STATIONS), PreferredJourney.class);
                updatePreferredJourneyFromFavouritesIfAvailable();
                Log.d("Current bundled preferred journey is: ", preferredJourney.toString());
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

        Observable.concatDelayError(Observable.fromIterable(searchTrainDetails(trainIdList))).subscribe(
                train -> {
                    trainList.add(train);
                }, throwable -> {
                    if (throwable == null) {
                    view.showErrorMessage("Si è verificato un errore", "Torna alle soluzioni", ENUM_ERROR_BTN_STATUS.NO_SOLUTIONS);
                    return;
                }
                    Log.d(throwable.getMessage());
                if (view != null) {
                    if (throwable.getMessage().equals("HTTP 404 ")) {
                        if (solution.hasChanges()) {
                            FirebaseCrash.report(new Exception("TRAIN DETAIL ERROR solution is " + solution.toString()));
                            view.showSnackbar("Uno o più treni non sono ancora disponibili", NONE, Snackbar.LENGTH_LONG);
                            onComplete();
                        } else {
                            view.showErrorMessage("Le informazioni su questo treno purtroppo non sono ancora disponibili", "Torna alle soluzioni", ENUM_ERROR_BTN_STATUS.NO_SOLUTIONS);
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
                    onComplete();
            }
        );
    }

    private void onComplete() {
        if (view == null) {
            return;
        }
        getFlatTrainList();
        view.hideProgress();
        view.updateTrainDetails();
    }

    @Override
    public void refreshRequested() {
        trainList.clear();
        trainStopList.clear();
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
            o.add(APINetworkingFactory.createRetrofitService(TrainService.class).getTrainDetails(s)
                    .observeOn(AndroidSchedulers.mainThread()));
        }
        return o;
    }

    @Override
    public List<Object> getFlatTrainList() {
        trainStopList.clear();
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
                getIndexOfTrainFromPosition(position));
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

        s += "\n Messaggio generato dall'app per pendolari -> JustInTrain Orari Trenitalia.\n";
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

    private List<String> getTrainIdList() {
        List<String> trainIdList = new LinkedList<>();
        if (solution.hasChanges()) {
            for (Journey.Solution.Change c : solution.getChangesList()) {
                if (!c.getTrainCategory().equalsIgnoreCase("BUS")) {
                    trainIdList.add(c.getTrainId());
                }
            }
        } else {
            trainIdList.add(solution.getTrainId());
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
