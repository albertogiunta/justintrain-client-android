package com.jaus.albertogiunta.justintrain_oraritreni.journeyFavourites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_HOME_HEADER;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteHeaderItem extends AbstractFlexibleItem<FavouriteHeaderItem.SimpleViewHolder> {

    private String  titleText;
    private boolean isFooter;

    public FavouriteHeaderItem(ENUM_HOME_HEADER headerType, boolean isFooter) {
        super();
        this.titleText = headerType.getTitle();
        this.isFooter = isFooter;
        setDraggable(false);
        setSwipeable(false);
    }

    @Override
    public int getLayoutRes() {
        if (isFooter) {
            return R.layout.item_home_element_footer;
        } else {
            return R.layout.item_home_element_header;
        }
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
        if (!isFooter) holder.bind(titleText);
    }

    public class SimpleViewHolder extends FlexibleViewHolder {

        @BindView(R.id.tv_header)
        TextView title;

        SimpleViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, itemView);
        }

        void bind(String text) {
            title.setText(text);
        }

    }
}