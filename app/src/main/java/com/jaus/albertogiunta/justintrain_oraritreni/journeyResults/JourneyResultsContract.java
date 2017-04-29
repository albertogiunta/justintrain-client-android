package com.jaus.albertogiunta.justintrain_oraritreni.journeyResults;

import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.BasePresenter;
import com.jaus.albertogiunta.justintrain_oraritreni.BaseView;
import com.jaus.albertogiunta.justintrain_oraritreni.data.Journey;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_ERROR_BTN_STATUS;

import org.joda.time.DateTime;

import java.util.List;

interface JourneyResultsContract {

    interface Presenter extends BasePresenter {

        void onSwapButtonClick();

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
         * Called when the Load More (Before) button is clicked. It will search for another set of
         * items (without delay, non preemptively) before the given ones (departing until 1 second
         * before the first know solution in order to avoid duplicates)
         */
        void onLoadMoreItemsBefore();

        /**
         * Called when the user swipes on a preferred station in the main activity and launches an
         * intent with the swiped data.
         * It's an INSTANT kind of search.
         */
//        void searchInstantaneously();

        /**
         * Called when the search input data is VALID, after the user clicked on the search button.
         * It will either search with the INSTANT search or the SearchAFTER kind of search.
         */
        void searchFromSearch(boolean isNewSearch);

        /**
         * Called when the Load More (After) button is clicked. It will search for another set of
         * items (without delay, non preemptively) after the given ones (departing 61 second after
         * the last known solution in order to avoid duplicates)
         */
        void onLoadMoreItemsAfter();

        /**
         * Sets up the notification with the selected solution
         *
         * @param elementIndex index of the solution inside of the journeyList
         */
        void onNotificationRequested(int elementIndex);

        /**
         * Refreshes the state of the selected solution
         * @param elementIndex index of the solution inside of the journeyList
         */
        void onJourneyRefreshRequested(int elementIndex);

        /**
         * Getter for the solution list (it's a static field)
         * @return the current solution list
         */
        List<Journey.Solution> getSolutionList();

        PreferredJourney getPreferredJourney();

        PreferredStation getDepartureStation();

        PreferredStation getArrivalStation();

        DateTime getTimeOfSearch();
    }

    interface View extends BaseView {

        /**
         * Hides everything and shows a loader
         */
        void showProgress();

        /**
         * Hides the loader and shows everything else
         */
        void hideProgress();

        /**
         * Hides everything and shows an error message with an action button
         *
         * @param tvMessage message to be shown
         * @param btnMessage text for the button
         * @param intent action to execute on button press
         */
        void showErrorMessage(String tvMessage, String btnMessage, ENUM_ERROR_BTN_STATUS intent);

        /**
         * Called when a new set of solutions is ready. It will then notify the adapter
         */
        void updateSolutionsList();

        /**
         * Called when only one solution has been updated
         * @param elementIndex index of the element that has been updated
         */
        void updateSolution(int elementIndex);

        void scrollToFirstFeasibleSolution(int position);

        /**
         * Sets the status of the favourite button depending on if the current journey is favourite
         * or not
         * @param isPreferred the status to be set
         */
        void setFavouriteButtonStatus(boolean isPreferred);

        /**
         * It Separate control over the station names.
         * @param departure departure station name
         * @param arrival arrival station name
         */
        void setStationNames(String departure, String arrival);

        interface JourneySearchStrategy {

            interface OnJourneySearchFinishedListener {

                /**
                 * No solution found for the requested journey
                 */
                void onJourneyNotFound();

                /**
                 * No solution found before the ones already gotten
                 */
                void onJourneyBeforeNotFound();

                /**
                 * No solution found after the ones already gotten
                 */
                void onJourneyAfterNotFound();

                /**
                 * The server is unreachable or network not available?
                 * @param exception describing the problem (could be 404, 500 or else)
                 */
                void onServerError(Throwable exception);

                /**
                 * The request has gone through. Everything's ok (but still it can be that there's
                 * no solution. Check for it)
                 */
                void onSuccess();

                void onScrollToFirstRequested(int position);

                Context getViewContext();

            }

            /**
             * Strategy used to search in the various kind of classes available (Search Instant,
             * Before or After).
             * @param departureStationId short departure Id
             * @param arrivalStationId short arrival Id
             * @param timestamp departure time in unix time (seconds)
             * @param isPreemptive if you want ONE journey before the next available one
             * @param withDelays if you want N delays in the returned set of solutions
             * @param listener the listener that will call onSuccess or the other methods
             */
            void searchJourney(boolean isNewSearch,
                               String departureStationId,
                               String arrivalStationId,
                               DateTime timestamp,
                               boolean isPreemptive,
                               boolean withDelays,
                               boolean includeTrainToBeTaken,
                               OnJourneySearchFinishedListener listener);
        }
    }
}
