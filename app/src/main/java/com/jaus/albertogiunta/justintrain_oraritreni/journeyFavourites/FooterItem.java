package com.jaus.albertogiunta.justintrain_oraritreni.journeyFavourites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaus.albertogiunta.justintrain_oraritreni.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import java.util.List;

public class FooterItem extends AbstractFlexibleItem<FooterItem.SimpleViewHolder> {

    public FooterItem() {
        super();
        setDraggable(false);
        setSwipeable(false);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_home_element_footer_void;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public SimpleViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new SimpleViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void bindViewHolder(final FlexibleAdapter adapter, SimpleViewHolder holder, int position, List payloads) {
        holder.bind();
    }

    public class SimpleViewHolder extends FlexibleViewHolder {

        SimpleViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
        }

        void bind() {
        }

    }
}