package com.jaus.albertogiunta.justintrain_oraritreni.journeyFavourites;

import com.google.firebase.crash.FirebaseCrash;

import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_HOME_HEADER;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_SNACKBAR_ACTIONS;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.PreferredStationsPreferences;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.RecentStationsPreferences;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SettingsPreferences;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

import java.util.LinkedList;
import java.util.List;

class FavouritesPresenter implements FavouritesContract.Presenter {

    private FavouritesContract.View    view;
    private List<PreferredJourney>     preferredJourneys;
    private List<PreferredJourney>     recentJourneys;
    private List<AbstractFlexibleItem> recyclerViewList;

    FavouritesPresenter(FavouritesContract.View view) {
        this.view = view;
        preferredJourneys = new LinkedList<>();
        recentJourneys = new LinkedList<>();
        recyclerViewList = new LinkedList<>();
        updatePreferredJourneys();
    }

    @Override
    public void unsubscribe() {
        view = null;
    }

    @Override
    public void setState(Bundle bundle) {
        // removeFavourite and thread checkintro pass ùa null bundle here from the view, watch out!
        updatePreferredJourneys();
        if ((this.preferredJourneys != null && this.preferredJourneys.size() > 0) ||
                (this.recentJourneys != null && this.recentJourneys.size() > 0)) {
            view.displayFavouriteJourneys();
            view.updateFavouritesList();
        } else {
            view.displayEntryButton();
        }
    }

    @Override
    public Bundle getState(Bundle bundle) {
        return bundle;
    }

    @Override
    public List<AbstractFlexibleItem> getRecyclerViewList() {
        return recyclerViewList;
    }

    @Override
    public List<PreferredJourney> getPreferredJourneys() {
        return this.preferredJourneys;
    }

    @Override
    public void addNewFavourite(PreferredStation departureStation, PreferredStation arrivalStation) {
        if (PreferredStationsPreferences.isPossibleToSaveMorePreferredJourneys(view.getViewContext())) {
            if (!PreferredStationsPreferences.isJourneyAlreadyPreferred(view.getViewContext(), departureStation, arrivalStation)) {
                PreferredStationsPreferences.setPreferredJourney(view.getViewContext(), new PreferredJourney(departureStation, arrivalStation));
                view.showSnackbar("Tratta aggiunta ai Preferiti", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_SHORT);
            } else {
                view.showSnackbar("La tratta è già tra i preferiti!", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_LONG);
            }
        } else {
            view.showSnackbar("Impossibile salvare più di 5 tratte preferite!", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_LONG);
        }
        setState(null);
    }

    @Override
    public void removeFavourite(PreferredStation departureStation, PreferredStation arrivalStation) {
        PreferredStationsPreferences.removePreferredJourney(view.getViewContext(), departureStation, arrivalStation);
        view.showSnackbar("Tratta rimossa dai Preferiti", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_SHORT);
        setState(null);
    }

    @Override
    public void removeRecent(PreferredStation departureStation, PreferredStation arrivalStation) {
        RecentStationsPreferences.removeRecentJourney(view.getViewContext(), departureStation, arrivalStation);
        view.showSnackbar("Tratta rimossa dai Recenti", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_SHORT);
        setState(null);
    }

    private void updatePreferredJourneys() {
        if (view == null) {
            FirebaseCrash.report(new Exception("VIEW was null in favouritespresenter (method updatepreferredjourneys)"));
            return;
        }

        preferredJourneys.clear();
        recentJourneys.clear();
        recyclerViewList.clear();

        List<PreferredJourney> newPreferredJourneys = PreferredStationsPreferences.getAllPreferredJourneys(view.getViewContext());
        List<PreferredJourney> newRecentJourneys    = RecentStationsPreferences.getAllRecentJourneys(view.getViewContext());

        if (newPreferredJourneys != null && !newPreferredJourneys.isEmpty()) {
            this.preferredJourneys.addAll(newPreferredJourneys);
            recyclerViewList.add(new FavouriteHeaderItem(view.getViewContext(), ENUM_HOME_HEADER.FAVOURITES, false));
            for (PreferredJourney j : this.preferredJourneys) {
                recyclerViewList.add(new FavouriteJourneysItem(j, true));
                recyclerViewList.add(new FavouriteSolutionsItem(view.getViewContext(), j));
            }
        }

        if (newRecentJourneys != null && !newRecentJourneys.isEmpty() && SettingsPreferences.isRecentEnabled(view.getViewContext())) {
            this.recentJourneys.addAll(newRecentJourneys);
            recyclerViewList.add(new FavouriteHeaderItem(view.getViewContext(), ENUM_HOME_HEADER.RECENT, false));
            for (PreferredJourney j : this.recentJourneys) {
                recyclerViewList.add(new FavouriteJourneysItem(j, false));
            }
            if (SettingsPreferences.isRecentHintEnabled(view.getViewContext())) {
                recyclerViewList.add(new FavouriteHeaderItem(view.getViewContext(), ENUM_HOME_HEADER.RECENT, true));
            }
        }

        if (!recyclerViewList.isEmpty()) recyclerViewList.add(new FooterItem());
    }
}
