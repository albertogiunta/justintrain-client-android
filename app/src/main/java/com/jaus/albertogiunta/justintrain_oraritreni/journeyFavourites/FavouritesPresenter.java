package com.jaus.albertogiunta.justintrain_oraritreni.journeyFavourites;

import com.google.firebase.crash.FirebaseCrash;

import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_SNACKBAR_ACTIONS;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.PreferredStationsPreferences;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

import java.util.LinkedList;
import java.util.List;

class FavouritesPresenter implements FavouritesContract.Presenter {

    private FavouritesContract.View    view;
    private List<PreferredJourney>     preferredJourneys;
    private List<AbstractFlexibleItem> recyclerViewList;

    FavouritesPresenter(FavouritesContract.View view) {
        this.view = view;
        preferredJourneys = new LinkedList<>();
        recyclerViewList = new LinkedList<>();
        updatePreferredJourneys();
    }

    @Override
    public void unsubscribe() {
        view = null;
    }

    @Override
    public void setState(Bundle bundle) {
        // removeFavourite andthread checkintro pass ùa null bundle here from the view, watch out!
        updatePreferredJourneys();
        if (this.preferredJourneys != null && this.preferredJourneys.size() > 0) {
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
    public void removeFavourite(PreferredStation departureStation, PreferredStation arrivalStation) {
        PreferredStationsPreferences.removePreferredJourney(view.getViewContext(), departureStation, arrivalStation);
        view.showSnackbar("Tratta rimossa dai Preferiti", ENUM_SNACKBAR_ACTIONS.NONE, Snackbar.LENGTH_SHORT);
        setState(null);
    }

    private void updatePreferredJourneys() {
        if (view == null) {
            FirebaseCrash.report(new Exception("VIEW was null in favouritespresenter (method updatepreferredjourneys)"));
            return;
        }

        preferredJourneys.clear();
        recyclerViewList.clear();

        List<PreferredJourney> newPreferredJourneys = PreferredStationsPreferences.getAllPreferredJourneys(view.getViewContext());
        List<PreferredJourney> recentJourneys       = PreferredStationsPreferences.getAllPreferredJourneys(view.getViewContext());

        if (newPreferredJourneys != null) {
            this.preferredJourneys.addAll(newPreferredJourneys);
            for (PreferredJourney j : this.preferredJourneys) {
                recyclerViewList.add(new FavouriteJourneysItem(j, true));
                recyclerViewList.add(new FavouriteSolutionsItem(view.getViewContext(), j));
            }
        }
    }
}
