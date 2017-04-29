package com.jaus.albertogiunta.justintrain_oraritreni.journeyFavourites;

import com.jaus.albertogiunta.justintrain_oraritreni.BasePresenter;
import com.jaus.albertogiunta.justintrain_oraritreni.BaseView;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

import java.util.List;

interface FavouritesContract {

    interface View extends BaseView {

        /**
         * It will notify the adapter after the favourite journeys list object has been updated
         */
        void updateFavouritesList();

        /**
         * Update the message board with the selected message (downloaded from the internet)
         * @param message to display
         * @param titleColor color f the title
         * @param isUpdateMessage
         */
        void updateDashboard(String message, ViewsUtils.COLORS titleColor, boolean isUpdateMessage);

        /**
         * Should be called after the preferred journey list has been updated
         */
        void displayFavouriteJourneys();

        /**
         * Should be called when no preferred journey is present, it displays an additional button
         */
        void displayEntryButton();

        /**
         * Hides the message view
         */
        void hideMessage();
    }

    interface Presenter extends BasePresenter {

        /**
         * Getter for the preferred journeys list object
         * @return the preferred journeys list object
         */
        List<PreferredJourney> getPreferredJourneys();

        List<AbstractFlexibleItem> getRecyclerViewList();

        /**
         * Removes the specified favourite journey
         *
         * @param departureStation departure station
         * @param arrivalStation arrival station
         */
        void removeFavourite(PreferredStation departureStation, PreferredStation arrivalStation);
    }
}
