package com.jaus.albertogiunta.justintrain_oraritreni.journeyFavourites;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.data.Journey;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredJourney;
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.DateTimeAdapter;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.PostProcessingEnabler;
import com.jaus.albertogiunta.justintrain_oraritreni.notification.NotificationService;
import com.jaus.albertogiunta.justintrain_oraritreni.trainDetails.TrainDetailsActivity;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.components.AnimationUtils;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.NotificationPreferences;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindViews;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.apply;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.GONE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.VISIBLE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_CLICK_ON_FAV_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_CLICK_ON_FAV_SOLUTION_NOTIF;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SCREEN_FAVOURITE_JOURNEYS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_STATIONS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SEPARATOR;

public class FavouriteSolutionsItem extends AbstractFlexibleItem<FavouriteSolutionsItem.SimpleViewHolder> {

    private Context context;
    PreferredJourney preferredJourney;
    public AnalyticsHelper analyticsHelper;


    public FavouriteSolutionsItem(Context context, PreferredJourney journey) {
        super();
        this.preferredJourney = journey;
        this.context = context;
        analyticsHelper = AnalyticsHelper.getInstance(context);
        setDraggable(false);
        setSwipeable(false);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_favourite_trains_journey;
    }

    @Override
    public SimpleViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new SimpleViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void bindViewHolder(final FlexibleAdapter adapter, SimpleViewHolder holder, int position, List payloads) {
        holder.bind(analyticsHelper, context, this.preferredJourney);
    }

    static final class SimpleViewHolder extends FlexibleViewHolder {

        @BindViews({R.id.left_fav_train_0, R.id.left_fav_train_1, R.id.left_fav_train_2})
        List<RelativeLayout> left  = new LinkedList<>();
        @BindViews({R.id.right_fav_train_0, R.id.right_fav_train_1, R.id.right_fav_train_2})
        List<RelativeLayout> right = new LinkedList<>();

        SimpleViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, itemView);
        }

        void bind(AnalyticsHelper analyticsHelper, Context context, PreferredJourney preferredJourney) {
            bindPreferredSolutionsButtons(analyticsHelper, context, preferredJourney.getStation1().getPreferredSolutions(), left, preferredJourney, preferredJourney.getStation1(), preferredJourney.getStation2());
            bindPreferredSolutionsButtons(analyticsHelper, context, preferredJourney.getStation2().getPreferredSolutions(), right, preferredJourney, preferredJourney.getStation2(), preferredJourney.getStation1());
        }

        private void bindPreferredSolutionsButtons(AnalyticsHelper analyticsHelper, Context context, Map<String, Journey.Solution> preferredSolutions, List<RelativeLayout> list, PreferredJourney preferredJourney, PreferredStation departureStation, PreferredStation arrivalStation) {

            Button    btn;
            ImageView ntf;
            int       i = 0;
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                    .registerTypeAdapterFactory(new PostProcessingEnabler())
                    .create();

            List<Journey.Solution> solutions = new ArrayList<>(preferredSolutions.values());
            Collections.sort(solutions, (o1, o2) -> {
                if (o1.getDepartureTime().isBefore(o2.getDepartureTime())) {
                    return -1;
                } else if (o1.getDepartureTime().isAfter(o2.getDepartureTime())) {
                    return 1;
                } else {
                    return 0;
                }
            });

            for (Journey.Solution solution : solutions) {
                if (i < list.size()) {
                    apply(list.get(i), VISIBLE);
                }

                btn = (Button) list.get(i).findViewById(R.id.btn_solution_time);
                ntf = (ImageView) list.get(i).findViewById(R.id.btn_pin);
                btn.setText(solution.getDepartureTimeReadable() + " " + SEPARATOR + " " + solution.getArrivalTimeReadable());
                btn.setOnClickListener(v -> {
                    analyticsHelper.logScreenEvent(SCREEN_FAVOURITE_JOURNEYS, ACTION_CLICK_ON_FAV_SOLUTION);
                    Intent intent = new Intent(context, TrainDetailsActivity.class);
                    intent.putExtra(I_SOLUTION, gson.toJson(solution));
                    intent.putExtra(I_STATIONS, gson.toJson(preferredJourney));
                    context.startActivity(intent);
                });
                i++;

                ntf.setOnClickListener(v -> {
                    analyticsHelper.logScreenEvent(SCREEN_FAVOURITE_JOURNEYS, ACTION_CLICK_ON_FAV_SOLUTION_NOTIF);
                    NotificationPreferences.setNotificationData(context, preferredJourney, solution);
                    NotificationService.startActionStartNotification(context,
                            departureStation,
                            arrivalStation,
                            solution,
                            null);
                    AnimationUtils.animOnPress(v, AnimationUtils.ANIM_TYPE.MEDIUM);
                });

                AnimationUtils.onCompare(ntf);
            }

            for (int j = preferredSolutions.size(); j < list.size(); j++) {
                apply(list.get(j), GONE);
            }
        }
    }

    @Override
    public String toString() {
        return "FavouriteJourneysItem[" + super.toString() + "]";
    }

}