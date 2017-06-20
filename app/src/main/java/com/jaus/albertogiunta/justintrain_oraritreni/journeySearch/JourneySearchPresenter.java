package com.jaus.albertogiunta.justintrain_oraritreni.journeySearch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.DateTimeAdapter;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.PostProcessingEnabler;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_SNACKBAR_ACTIONS;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.DatabaseHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.PreferredStationsPreferences;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

import trikita.log.Log;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_STATIONS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_TIME;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.DatabaseHelper.getStation4DatabaseObject;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.DatabaseHelper.isStationNameValid;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.DatabaseHelper.isThisJourneyPreferred;

class JourneySearchPresenter implements JourneySearchContract.Presenter {

    private JourneySearchActivity view;
    private PreferredStation      departureStation;
    private PreferredStation      arrivalStation;
    private DateTime              dateTime;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
            .registerTypeAdapterFactory(new PostProcessingEnabler())
            .create();

    JourneySearchPresenter(JourneySearchActivity view) {
        this.view = view;
        dateTime = DateTime.now().minusMinutes(10);
    }

    @Override
    public void unsubscribe() {
        view = null;
    }

    @Override
    public void setState(Bundle bundle) {
        if (bundle != null) {
            // Restore value of members from saved state
            PreferredJourney journey = gson.fromJson(bundle.getString(I_STATIONS), PreferredJourney.class);
            this.departureStation = journey.getStation1();
            this.arrivalStation = journey.getStation2();
            view.setStationNames(departureStation.getNameLong(), arrivalStation.getNameLong());
            setFavouriteButtonStatus();
            this.dateTime = new DateTime(bundle.getLong(I_TIME, DateTime.now().minusMinutes(10).getMillis()));
            setDateTime();
        } else {
            if (this.departureStation != null) {
                view.setDepartureStationNames(departureStation.getNameLong());
            }
            if (this.arrivalStation != null) {
                view.setArrivalStationNames(arrivalStation.getNameLong());
            }
            if (this.departureStation != null && this.arrivalStation != null) {
                setFavouriteButtonStatus();
            }
            Log.d("no bundle found");
        }
    }

    @Override
    public Bundle getState(Bundle bundle) {
        if (bundle == null) bundle = new Bundle();
        bundle.putString(I_STATIONS, gson.toJson(new PreferredJourney(departureStation, arrivalStation)));
        if (departureStation != null) {
            Log.d("Current bundled departure stations is: ", this.departureStation);
        }
        if (arrivalStation != null) {
            Log.d("Current bundled departure stations is: ", this.arrivalStation);
        }
        bundle.putLong(I_TIME, dateTime.getMillis());
        Log.d("Current bundled DateTime is: ", dateTime);
        return bundle;
    }

    @Override
    public void onDepartureStationNameChanged(String name) {
        departureStation = new PreferredStation(DatabaseHelper.getStation4DatabaseObject(name));
        setFavouriteButtonStatus();
    }

    @Override
    public void onArrivalStationNameChanged(String name) {
        arrivalStation = new PreferredStation(DatabaseHelper.getStation4DatabaseObject(name));
        setFavouriteButtonStatus();
    }

    @Override
    public void onTimeChanged(int delta) {
        dateTime = dateTime.plusHours(delta);
        setDateTime();
    }

    @Override
    public void onTimeChanged(int newHour, int newMinute) {
        dateTime = dateTime.withHourOfDay(newHour).withMinuteOfHour(newMinute);
        setDateTime();
    }

    @Override
    public void onDateChanged(int delta) {
        dateTime = dateTime.plusDays(delta);
        setDateTime();
    }

    @Override
    public void onDateChanged(int newYear, int newMonth, int newDay) {
        dateTime = dateTime.withYear(newYear).withMonthOfYear(newMonth).withDayOfMonth(newDay);
        setDateTime();
    }

    @Override
    public DateTime getSearchDateTime() {
        return this.dateTime;
    }

//    @Override
//    public List<String> searchStationName(String stationName) {
//        return Stream
//                .of(stationList.where().beginsWith("name", stationName, Case.INSENSITIVE).findAll())
//                .map(Station::getNameLong).collect(Collectors.toList());
//    }

    @Override
    public void onSwapButtonClick(String departure, String arrival) {
        if (!(departure.equals("Seleziona") && arrival.equals("Seleziona"))) {
            PreferredStation temp = departureStation;
            departureStation = arrivalStation;
            arrivalStation = temp;
            view.setStationNames(arrival, departure);
        }
    }

    @Override
    public void onFavouriteButtonClick() {
        if (isDataValid()) {
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
                    view.showSnackbar("Impossibile salvare più di 5 tratte nei Preferiti!", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_SHORT);
                }
            }
        }
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
    public void onSearchButtonClick(String departureStationName, String arrivalStationName) {
        if (isDataValid()) {
            view.onValidSearchParameters();
            Log.d("Searching for: " + departureStation.toString(), arrivalStation.toString());
        }
    }

    @Override
    public PreferredStation getDepartureStation() {
        return departureStation;
    }

    @Override
    public PreferredStation getArrivalStation() {
        return arrivalStation;
    }

    private void setDateTime() {
        view.setTime(dateTime.toString(DateTimeFormat.forPattern("HH:mm")));
        view.setDate(dateTime.toString(DateTimeFormat.forPattern("EE d MMM").withLocale(Locale.ITALY)));
    }

    private boolean isDataValid() {
        if (departureStation != null && isStationNameValid(departureStation.getNameShort())) {
            departureStation = new PreferredStation(getStation4DatabaseObject(departureStation.getNameShort()));
        } else {
            view.showSnackbar("Stazione di partenza mancante!", ENUM_SNACKBAR_ACTIONS.SELECT_DEPARTURE, Snackbar.LENGTH_LONG);
            if (departureStation != null) Log.d(departureStation.getNameShort() + " not found!");
            return false;
        }

        if (arrivalStation != null && isStationNameValid(arrivalStation.getNameShort())) {
            arrivalStation = new PreferredStation(getStation4DatabaseObject(arrivalStation.getNameShort()));
        } else {
            view.showSnackbar("Stazione di arrivo mancante!", ENUM_SNACKBAR_ACTIONS.SELECT_ARRIVAL, Snackbar.LENGTH_LONG);
            if (arrivalStation != null) Log.d(arrivalStation.getNameShort() + " not found!");
            return false;
        }

        return true;
    }
}
