package com.jaus.albertogiunta.justintrain_oraritreni.journeyResults;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.data.Journey;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.DateTimeAdapter;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.PostProcessingEnabler;
import com.jaus.albertogiunta.justintrain_oraritreni.trainDetails.TrainDetailsActivity;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.components.AnimationUtils;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;

import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.apply;
import static butterknife.ButterKnife.bind;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.GONE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.INVISIBLE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.VISIBLE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_REFRESH_DELAY_ALREADY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_REFRESH_DELAY_FIRST;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_SEARCH_MORE_AFTER;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_SEARCH_MORE_BEFORE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_SET_NOTIFICATION_FROM_RESULTS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_SOLUTION_CLICK;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SCREEN_JOURNEY_RESULTS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_STATIONS;

class JourneyResultsAdapter extends RecyclerView.Adapter {

    private Context                          context;
    private List<Journey.Solution>           solutionList;
    private JourneyResultsContract.Presenter presenter;

    JourneyResultsAdapter(Context context, JourneyResultsContract.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        this.solutionList = presenter.getSolutionList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPES.Normal:
                return new JourneyHolder(layoutInflater.inflate(R.layout.item_journey, parent, false));
            case VIEW_TYPES.Header:
                return new LoadMoreBeforeHolder(layoutInflater.inflate(R.layout.item_journey_load_before, parent, false));
            case VIEW_TYPES.Footer:
                return new LoadMoreAfterHolder(layoutInflater.inflate(R.layout.item_journey_load_after, parent, false));
            default:
                throw new RuntimeException("there is no type that matches the type " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (solutionList.isEmpty()) return;
        if (holder instanceof JourneyHolder) {
            ((JourneyHolder) holder).bind(solutionList.get(position - 1));
        } else if (holder instanceof LoadMoreAfterHolder) {
            ((LoadMoreAfterHolder) holder).readyButton();
        } else if (holder instanceof LoadMoreBeforeHolder) {
            ((LoadMoreBeforeHolder) holder).readyButton();
        }
    }

    @Override
    public int getItemCount() {
        if (solutionList.isEmpty()) {
            return 0;
        } else {
            return solutionList.size() + 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPES.Header;
        } else if (position == solutionList.size() + 1) {
            return VIEW_TYPES.Footer;
        } else {
            return VIEW_TYPES.Normal;
        }
    }

    private class VIEW_TYPES {
        static final int Header   = 1;
        static final int Normal   = 2;
        static final int Footer   = 3;
    }

    class JourneyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_journey)
        CardView cvJourney; // used for click

        @BindView(R.id.tv_train_category)
        TextView tvTrainCategory;
        @BindView(R.id.tv_train_id)
        TextView tvTrainNumber;

        @BindView(R.id.tv_changes_number)
        TextView tvChangesNumber;
        @BindView(R.id.tv_changes_text)
        TextView tvChangesText;

        @BindView(R.id.tv_departure_time)
        TextView tvDepartureTime;
        @BindView(R.id.tv_departure_time_with_delay)
        TextView tvDepartureTimeWithDelay;

        @BindView(R.id.tv_arrival_time)
        TextView tvArrivalTime;
        @BindView(R.id.tv_arrival_time_with_delay)
        TextView tvArrivalTimeWithDelay;

        @BindView(R.id.tv_time_difference)
        TextView    tvTimeDifference;
        @BindView(R.id.btn_refresh_journey)
        ImageButton btnRefreshJourney;
        @BindView(R.id.iv_suppressed)
        ImageView   ivSuppressed;

        ///

        @BindView(R.id.tv_lasting_time)
        TextView tvLastingTime;

        @BindView(R.id.rl_platform)
        RelativeLayout rlPlatform;
        @BindView(R.id.tv_platform)
        TextView       tvPlatform;

        @BindView(R.id.tv_change)
        TextView tvChange;

        @BindView(R.id.ic_bolt)
        ImageView icBolt;
        @BindView(R.id.ic_warning)
        ImageView icWarning;
        @BindView(R.id.ic_coincidence_to_be_missed)
        ImageView icCoincidenceToBeMissed;

        @BindView(R.id.ll_notification)
        LinearLayout llNotification;

        @BindView(R.id.btn_pin)
        ImageView btnPin;

        @BindViews({R.id.ll_changes_number, R.id.rl_changes})
        List<View> viewsForSolutionWithChanges;

        @BindViews({R.id.ll_train_identification, R.id.rl_platform})
        List<View> viewsForDirectSolution;

        @BindViews({R.id.tv_time_difference, R.id.tv_arrival_time_with_delay, R.id.tv_departure_time_with_delay})
        List<View> viewsForTimeDifference;

        @BindViews({R.id.btn_refresh_journey})
        List<View> viewsForRefreshableSolution;

        @BindViews({R.id.tv_arrival_time_with_delay, R.id.tv_departure_time_with_delay})
        List<View> timesWithDelay;

        @BindViews({R.id.iv_suppressed, R.id.tv_suppressed})
        List<View> viewsForSuppressedSolution;

        @BindViews({R.id.tv_time_difference, R.id.tv_arrival_time_with_delay, R.id.tv_departure_time_with_delay})
        List<View> viewsForFilledNotSuppressedSolution;

        boolean isFilled, isSuppressed, hasAlert, isOnTime, hasEmptyRightSide, coincidenceToBeMissed;

        JourneyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Journey.Solution solution) {
            isFilled = solution.getTimeDifference() != null;
            isSuppressed = !solution.hasChanges() && solution.getTrainStatusCode() != null && solution.getTrainStatusCode() == 2;
            hasAlert = !solution.hasChanges() && solution.getTrainStatusCode() != null && solution.getTrainStatusCode() != 1 && solution.getTrainStatusCode() != 2;
            isOnTime = solution.getTimeDifference() != null && solution.getTimeDifference() <= 0;
            hasEmptyRightSide = false;
            coincidenceToBeMissed = false;

            if (solution.getDepartureTime() != null &&
                    (solution.getDepartureTime().isAfter(DateTime.now().withHourOfDay(23).withMinuteOfHour(59)) ||
                    solution.getArrivalTime().isBefore(DateTime.now().withHourOfDay(0).withMinuteOfHour(0)))) {
                if (solution.getDepartureTime().getHourOfDay() > 3 ||
                        DateTime.now().getHourOfDay() < 22) {
                    hasEmptyRightSide = true;
                }
            }

            if (solution.hasChanges()) {
                try {
                    for (int i = 0; i < solution.getChangesList().size() - 1; i++) {
                        Journey.Solution.Change first  = solution.getChangesList().get(i);
                        Journey.Solution.Change second = solution.getChangesList().get(i + 1);

                        int firstTimeDifference  = first.getTimeDifference() != null && first.getTimeDifference() > 0 ? first.getTimeDifference() : 0;
                        int secondTimeDifference = second.getTimeDifference() != null && first.getTimeDifference() > 0 ? second.getTimeDifference() : 0;

                        if (first.getArrivalTime().plusMinutes(firstTimeDifference).isAfter(second.getDepartureTime().plusMinutes(secondTimeDifference))) {
                            coincidenceToBeMissed = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    coincidenceToBeMissed = false;
                    FirebaseCrash.report(new Exception("Got exception while checking coincidences in JOURNEYRESULTSADAPTER. The exception message is " + e.getMessage()));
                }
            }

            cvJourney.setOnClickListener(v -> {
                AnalyticsHelper.getInstance(context).logScreenEvent(SCREEN_JOURNEY_RESULTS, ACTION_SOLUTION_CLICK);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                        .registerTypeAdapterFactory(new PostProcessingEnabler())
                        .create();
                Intent i = new Intent(context, TrainDetailsActivity.class);
                solution.setDepartureStationId(presenter.getDepartureStation().getStationLongId());
                solution.setArrivalStationId(presenter.getArrivalStation().getStationLongId());
                i.putExtra(I_SOLUTION, gson.toJson(solution));
                i.putExtra(I_STATIONS, gson.toJson(presenter.getPreferredJourney()));
                context.startActivity(i);
            });

            tvDepartureTime.setText(solution.getDepartureTimeReadable());
            tvArrivalTime.setText(solution.getArrivalTimeReadable());
            tvLastingTime.setText(solution.getDuration());
            llNotification.setOnClickListener(v -> {
                AnimationUtils.animOnPress(this.btnPin, AnimationUtils.ANIM_TYPE.MEDIUM);
                AnalyticsHelper.getInstance(context).logScreenEvent(SCREEN_JOURNEY_RESULTS, ACTION_SET_NOTIFICATION_FROM_RESULTS);
                presenter.onNotificationRequested(getAdapterPosition());
            });

            if (isSuppressed) {
                apply(viewsForSuppressedSolution, VISIBLE);
                apply(llNotification, INVISIBLE);
                apply(viewsForFilledNotSuppressedSolution, GONE);
                apply(viewsForRefreshableSolution, GONE);
            } else {
                if (isFilled) {
                    apply(viewsForSuppressedSolution, GONE);
                    apply(llNotification, VISIBLE);
                    apply(viewsForFilledNotSuppressedSolution, VISIBLE);
                    apply(viewsForRefreshableSolution, GONE);

                    tvTimeDifference.setText(Integer.toString(solution.getTimeDifference()) + "'");
                    tvTimeDifference.setTextColor(ViewsUtils.getTimeDifferenceColor(context, solution.getTimeDifference()));
                    tvTimeDifference.setOnLongClickListener(view -> {
                        AnalyticsHelper.getInstance(context).logScreenEvent(SCREEN_JOURNEY_RESULTS, ACTION_REFRESH_DELAY_ALREADY);
                        presenter.onJourneyRefreshRequested(getAdapterPosition());
                        return true;
                    });

                    if (isOnTime) {
                        apply(timesWithDelay, GONE);
                    } else {
                        tvDepartureTimeWithDelay.setText(solution.getDepartureTimeWithDelayReadable());
                        tvDepartureTimeWithDelay.setTextColor(ViewsUtils.getTimeDifferenceColor(context, solution.getTimeDifference()));
                        tvArrivalTimeWithDelay.setText(solution.getArrivalTimeWithDelayReadable());
                        tvArrivalTimeWithDelay.setTextColor(ViewsUtils.getTimeDifferenceColor(context, solution.getTimeDifference()));
                    }
                } else {
                    apply(viewsForSuppressedSolution, GONE);
                    apply(viewsForFilledNotSuppressedSolution, GONE);
                    apply(viewsForRefreshableSolution, VISIBLE);
                    btnRefreshJourney.setOnClickListener(view -> {
                        AnalyticsHelper.getInstance(context).logScreenEvent(SCREEN_JOURNEY_RESULTS, ACTION_REFRESH_DELAY_FIRST);
                        presenter.onJourneyRefreshRequested(getAdapterPosition());
                    });
                }

                if (hasAlert) {
                    apply(icWarning, VISIBLE);
                } else {
                    apply(icWarning, GONE);
                }

                if (hasEmptyRightSide) {
                    apply(viewsForSuppressedSolution, GONE);
                    apply(viewsForFilledNotSuppressedSolution, GONE);
                    apply(viewsForRefreshableSolution, GONE);
                }

            }

            // -------------------------------------------------------- //

            if (solution.hasChanges()) {
                apply(viewsForDirectSolution, GONE);
                apply(viewsForSolutionWithChanges, VISIBLE);
                int changesNumber = solution.getChangesList().size() - 1;
                tvChangesNumber.setText(Integer.toString(changesNumber));
                tvChangesText.setText(changesNumber == 1 ? "Cambio" : "Cambi");
                tvChange.setText(setChangesString(solution));
            } else {
                apply(viewsForSolutionWithChanges, GONE);
                apply(viewsForDirectSolution, VISIBLE);
                tvTrainCategory.setText(solution.getTrainCategory());
                tvTrainNumber.setText(solution.getTrainId());

                if (solution.getDeparturePlatform() != null) {
                    apply(rlPlatform, VISIBLE);
                    tvPlatform.setText(solution.getDeparturePlatform());
                } else {
                    apply(rlPlatform, GONE);
                }
            }

            if (solution.isArrivesFirst()) {
                apply(icBolt, VISIBLE);
            } else {
                apply(icBolt, GONE);
            }

            if (coincidenceToBeMissed) {
                apply(icCoincidenceToBeMissed, VISIBLE);
            } else {
                apply(icCoincidenceToBeMissed, GONE);
            }
        }

        private String setChangesString(Journey.Solution solution) {
            List<String> changesStrings = new LinkedList<>();
            //noinspection Convert2streamapi
            for (Journey.Solution.Change c : solution.getChangesList()) {
                changesStrings.add(c.getTrainCategory() + " " + c.getTrainId());
            }
            String string = "";
            for (String strings : changesStrings) {
                string += strings + " > ";
            }
            if (string.length() > 3) {
                return string.subSequence(0, string.length() - 3).toString();
            }
            return string;
        }
    }

    @SuppressWarnings("WeakerAccess")
    class LoadMoreBeforeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_before_btn)
        ImageButton    btn;
        @BindView(R.id.rl_search_before_loading_spinner)
        RelativeLayout relativeLayout;

        LoadMoreBeforeHolder(View itemView) {
            super(itemView);
            bind(this, itemView);
            btn.setOnClickListener(view -> {
                AnalyticsHelper.getInstance(context).logScreenEvent(SCREEN_JOURNEY_RESULTS, ACTION_SEARCH_MORE_BEFORE);
                presenter.onLoadMoreItemsBefore();
                busyButton();
            });
        }

        void busyButton() {
            apply(relativeLayout, VISIBLE);
            apply(btn, GONE);
        }

        void readyButton() {
            apply(relativeLayout, GONE);
            apply(btn, VISIBLE);
        }
    }

    class LoadMoreAfterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_search_after)
        ImageButton    btn;
        @BindView(R.id.rl_search_after_loading_spinner)
        RelativeLayout relativeLayout;

        LoadMoreAfterHolder(View itemView) {
            super(itemView);
            bind(this, itemView);
            btn.setOnClickListener(view -> {
                AnalyticsHelper.getInstance(context).logScreenEvent(SCREEN_JOURNEY_RESULTS, ACTION_SEARCH_MORE_AFTER);
                presenter.onLoadMoreItemsAfter();
                busyButton();
            });
        }

        void busyButton() {
            apply(relativeLayout, VISIBLE);
            apply(btn, GONE);
        }

        void readyButton() {
            apply(relativeLayout, GONE);
            apply(btn, VISIBLE);
        }
    }
}
