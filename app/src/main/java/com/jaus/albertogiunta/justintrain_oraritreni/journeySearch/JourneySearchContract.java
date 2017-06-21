package com.jaus.albertogiunta.justintrain_oraritreni.journeySearch;

import com.jaus.albertogiunta.justintrain_oraritreni.BasePresenter;
import com.jaus.albertogiunta.justintrain_oraritreni.BaseView;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;

import org.joda.time.DateTime;

interface JourneySearchContract {

    interface Presenter extends BasePresenter {

        /**
         * It triggers an action when the user clicks on the "star" icon button.
         * The action will most likely be to toggle the icon and save/remove the current
         * journey from the favourite journeys.
         */
        void onFavouriteButtonClick();

        /**
         * It is called in order to automatically set the status of the favourite button.
         * When called it will check for the current journey if it's already favourite, and
         * it will set the button status depending on that.
         */
        void setFavouriteButtonStatus();

        /**
         * It triggers an action when the user clicks on the "search" button.
         * It will check for correspondence of the inserted stations, and if everything's alright
         * it will fire the search. Otherwise it will notify the user with an error message.
         * @param departureStationName name for the departure station
         * @param arrivalStationName name for the arrival station
         */
        void onSearchButtonClick(String departureStationName, String arrivalStationName);

        void onDepartureStationNameChanged(String name);

        void onArrivalStationNameChanged(String name);

        /**
         * Acts as a listener for time changes. It's triggered by the user clicks on the time buttons
         * and it will set the time in the view depending on the button that has been pressed.
         * @param delta the number of hours to increment or decrement from the previous value
         *              (The starting value is the current hour)
         */
        void onTimeChanged(int delta);

        /**
         * Should be called when the user inputs a custom datetime via the Time picker
         *
         * @param newHour representing an hour of day
         * @param newMinute representing a minute of hour
         */
        void onTimeChanged(int newHour, int newMinute);

        /**
         * Acts as a listener for time changes. It's triggered by the user clicks on the date buttons
         * and it will set the date in the view depending on the button that has been pressed.
         * @param delta the number of days to increment or decrement from the previous value
         *              (The starting value is the current day)
         */
        void onDateChanged(int delta);

        /**
         * Should be called when the user inputs a custom datetime via the Date picker
         * @param newYear representing an year
         * @param newMonth representing a month of year
         * @param newDay representing a day of month
         */
        void onDateChanged(int newYear, int newMonth, int newDay);

        /**
         * Getter for the DateTime. Should be used as default value in pickers and buttons
         * @return current or custom datetime
         */
        DateTime getSearchDateTime();

        /**
         * Called every time there's a change in the Autocomplete Text Views.
         * @param stationName name (can be also partial) to be searched (case-insensitive)
         * @return a list of matching station names
         */
//        List<String> searchStationName(String stationName);

        /**
         * Swaps the Preferred Station objects inside of the presenter. Called on the swap button
         * placed in the Search Panel in INACTIVE mode (only the header is visible).
         * It will swap the objects and the actual names (in both panel modes)
         * @param departure string from the textview
         * @param arrival string from the textview
         */
        void onSwapButtonClick(String departure, String arrival);

        PreferredStation getDepartureStation();

        PreferredStation getArrivalStation();
    }

    interface View extends BaseView {

        /**
         * Should be used when restoring the state of the activity or launching the search from
         * an activity where this values are already set (see Journey Results)
         * @param departureStationName departureStationName
         * @param arrivalStationName arrivalStationName
         */
        void setStationNames(PreferredStation departureStationName, PreferredStation arrivalStationName);

        /**
         * Called whenever there's the need to set the time (on start up of the activity or after
         * a change done by the user)
         * @param time the already formatted string (hh:mm) to be set
         */
        void setTime(String time);

        /**
         * Called whenever there's the need to set the date (on start up of the activity or after
         * a change done by the user)
         * @param date the already formatted string (hh:mm) to be set
         */
        void setDate(String date);

        /**
         * Called when everything goes right after the click on the search button.
         */
        void onValidSearchParameters();

        /**
         * Called when something bad happens after the click on the search button.
         * Errors can be:
         * - No results found
         * - Server error
         * - Client error
         * - Generic error
         */
        void onInvalidSearchParameters();

        void setFavouriteButtonStatus(boolean isPreferred);

        void setStationNames(String departureStationName, String arrivalStationName);

        void setDepartureStationNames(String departureStationName);

        void setArrivalStationNames(String arrivalStationName);
    }
}
