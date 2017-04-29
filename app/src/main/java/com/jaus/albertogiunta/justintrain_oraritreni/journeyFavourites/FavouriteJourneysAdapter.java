package com.jaus.albertogiunta.justintrain_oraritreni.journeyFavourites;

import android.content.Context;
import android.support.annotation.Nullable;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

import java.util.List;

public class FavouriteJourneysAdapter extends FlexibleAdapter<AbstractFlexibleItem> {

    public FavouriteJourneysAdapter(@Nullable List<AbstractFlexibleItem> items, Context context) {
        super(items, context);
    }
}
