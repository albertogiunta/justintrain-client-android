package com.jaus.albertogiunta.justintrain_oraritreni.journeyFavourites;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;

public class FavouriteJourneysItem extends AbstractFlexibleItem<FavouriteJourneysItem.SimpleViewHolder> {

    PreferredJourney preferredJourney;

    public FavouriteJourneysItem(PreferredJourney journey) {
        super();
        this.preferredJourney = journey;
        setDraggable(false);
        setSwipeable(true);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_favourite_journey;
    }

    @Override
    public SimpleViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new SimpleViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void bindViewHolder(final FlexibleAdapter adapter, SimpleViewHolder holder, int position, List payloads) {
        holder.bind(preferredJourney);
    }

    public PreferredJourney getPreferredJourney() {
        return this.preferredJourney;
    }

    static final class SimpleViewHolder extends FlexibleViewHolder {

        @BindView(R.id.front_view)
        View            frontView;
        @BindView(R.id.rear_left_view)
        View            rearLeftView;
        @BindView(R.id.rear_right_view)
        View            rearRightView;
        @BindView(R.id.tv_favourite_station_left)
        AutofitTextView tvPreferredStation1;
        @BindView(R.id.tv_favourite_station_right)
        AutofitTextView tvPreferredStation2;

        boolean swiped = false;

        SimpleViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, itemView);
        }

        void bind(PreferredJourney preferredJourney) {
            tvPreferredStation1.setText(preferredJourney.getStation1().getNameShort());
            tvPreferredStation2.setText(preferredJourney.getStation2().getNameShort());
        }

        @Override
        public void onItemReleased(int position) {
            swiped = (mActionState == ItemTouchHelper.ACTION_STATE_SWIPE);
            super.onItemReleased(position);
        }

        @Override
        public View getFrontView() {
            return frontView;
        }

        @Override
        public View getRearLeftView() {
            return rearLeftView;
        }

        @Override
        public View getRearRightView() {
            return rearRightView;
        }
    }

    @Override
    public String toString() {
        return "FavouriteJourneysItem[" + super.toString() + "]";
    }

}