package com.jaus.albertogiunta.justintrain_oraritreni.trainDetails;

import com.google.firebase.crash.FirebaseCrash;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
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
import com.jaus.albertogiunta.justintrain_oraritreni.data.PreferredStation;
import com.jaus.albertogiunta.justintrain_oraritreni.data.Station4Database;
import com.jaus.albertogiunta.justintrain_oraritreni.data.Train;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.components.AnimationUtils;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.COLORS;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.DatabaseHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import trikita.log.Log;

import static butterknife.ButterKnife.apply;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.GONE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.INVISIBLE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.VISIBLE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.getTimeDifferenceColor;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_SET_NOTIFICATION_FROM_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SCREEN_SOLUTION_DETAILS;

class TrainDetailsAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Object> trainList;
    private Journey.Solution solution;
    private TrainDetailsContract.Presenter presenter;

    TrainDetailsAdapter(Context context, TrainDetailsContract.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        trainList = presenter.getFlatTrainList();
        solution = presenter.getSolution();
        if (solution == null) {
            FirebaseCrash.report(new Exception("SOLUTION WAS NULL in Traindetilasadapter"));
        }
        if (solution.hasChanges()) {
            for (Journey.Solution.Change c : solution.getChangesList()) {
                Log.d(c.getDepartureStationName(), c.getArrivalStationName());
                try {
                    Station4Database station1 = DatabaseHelper.getStation4DatabaseObject(c.getDepartureStationName());
                    Station4Database station2 = DatabaseHelper.getStation4DatabaseObject(c.getArrivalStationName());
                    if (station1 != null && station2 != null) {
                        c.setDepartureStationId(new PreferredStation(station1).getStationLongId());
                        c.setArrivalStationId(new PreferredStation(station2).getStationLongId());
                    } else {
                        throw new Resources.NotFoundException();
                    }
                } catch (Resources.NotFoundException e) {
                    FirebaseCrash.report(new Exception("DEPARTURE or ARRIVAL not found" + c.getDepartureStationName() + "//" + c.getArrivalStationName() + "\n SOLUTION IS: " + solution.toString()));
                }
            }
        } else {
            try {
                solution.setDepartureStationId(new PreferredStation(DatabaseHelper.getStation4DatabaseObject(solution.getDepartureStationName())).getStationLongId());
                solution.setArrivalStationId(new PreferredStation(DatabaseHelper.getStation4DatabaseObject(solution.getArrivalStationName())).getStationLongId());
            } catch (Exception e) {
                FirebaseCrash.report(new Exception("DEPARTURE or ARRIVAL not found" + "\n SOLUTION IS: " + solution.toString()));
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater    = LayoutInflater.from(parent.getContext());
        int            currentapiVersion = android.os.Build.VERSION.SDK_INT;
        switch (viewType) {
            case VIEW_TYPES.Train:
                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                    return new TrainHolder(layoutInflater.inflate(R.layout.item_train_details, parent, false));
                } else {
                    return new TrainHolder(layoutInflater.inflate(R.layout.item_train_details_v19, parent, false));
                }
            case VIEW_TYPES.Stop:
                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                    return new StopHolder(layoutInflater.inflate(R.layout.item_station_detail, parent, false));
                } else {
                    return new StopHolder(layoutInflater.inflate(R.layout.item_station_detail_v19, parent, false));
                }
            case VIEW_TYPES.Footer:
                return new FooterHolder(layoutInflater.inflate(R.layout.item_journey_load_after, parent, false));
            default:
                throw new RuntimeException("there is no type that matches the type " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TrainHolder) {
            ((TrainHolder) holder).bind((Train) trainList.get(position));
        } else if (holder instanceof StopHolder) {
            ((StopHolder) holder).bind((Train.Stop) trainList.get(position), presenter.getTrainForAdapterPosition(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == trainList.size()) {
            return VIEW_TYPES.Footer;
        } else if (trainList.get(position) instanceof Train) {
            return VIEW_TYPES.Train;
        } else if (trainList.get(position) instanceof Train.Stop) {
            return VIEW_TYPES.Stop;
        } else {
            return -1;
        }
    }

    private class VIEW_TYPES {
        static final int Train = 1;
        static final int Stop = 2;
        static final int Footer = 3;
    }

    @Override
    public int getItemCount() {
        return trainList.isEmpty() ? 0 : trainList.size() + 1;
    }

    class TrainHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_train_number_category)
        TextView tvTrainNumberCategory;
        @BindView(R.id.tv_train_status)
        TextView tvTrainStatus;
        @BindView(R.id.tv_with_exception)
        TextView tvStatusException;
        @BindView(R.id.tv_departure_station_name)
        TextView tvDepartureStationName;
        @BindView(R.id.tv_arrival_station_name)
        TextView tvArrivalStationName;
        @BindView(R.id.ll_last_seen)
        LinearLayout llLastSeen;
        @BindView(R.id.tv_last_seen_station_time)
        TextView tvLastSeenStationTime;
        @BindView(R.id.ll_time_difference)
        LinearLayout llTimeDifference;
        @BindView(R.id.tv_time_difference)
        TextView tvTimeDifference;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.tv_why_delay)
        TextView tvWhyDelay;
        @BindView(R.id.btn_pin)
        ImageView btnPin;
        @BindView(R.id.tv_cancelled_stops_info)
        TextView tvCancelledStopsInfo;


        TrainHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Train train) {
            this.tvTrainNumberCategory.setText(train.getTrainCategory() + " " + train.getTrainId());
            tvDepartureStationName.setText(train.getTrainDepartureStationName());
            tvArrivalStationName.setText(train.getTrainArrivalStationName());

            this.btnPin.setOnClickListener(v -> {
                AnimationUtils.animOnPress(v, AnimationUtils.ANIM_TYPE.LIGHT);
                AnalyticsHelper.getInstance(context).logScreenEvent(SCREEN_SOLUTION_DETAILS, ACTION_SET_NOTIFICATION_FROM_SOLUTION);
                presenter.onNotificationRequested(getAdapterPosition());
            });

            apply(tvWhyDelay, GONE);

            if (train.getTrainStatusCode() == 2) {
                // soppresso
                apply(tvTrainStatus, GONE);
                apply(tvStatusException, VISIBLE);
                apply(tvWhyDelay, VISIBLE);
                apply(llLastSeen, GONE);
                apply(llTimeDifference, INVISIBLE);

                this.tvWhyDelay.setOnClickListener(v -> {
                    AnimationUtils.animOnPress(v, AnimationUtils.ANIM_TYPE.LIGHT);
                    presenter.onNewsUpdateRequested(train.getTrainId());
                });
                this.tvStatusException.setText("soppresso");
                this.tvStatusException.setTextColor(getTimeDifferenceColor(context, COLORS.RED));
            } else if (train.isDeparted() != null && (train.isDeparted() || (!train.isDeparted() && (train.getLastSeenStationName() != null && !train.getLastSeenStationName().equalsIgnoreCase(""))))) {
                if (train.isArrivedToDestination()) {
                    // arrivato a destinazione
                    apply(tvTrainStatus, VISIBLE);
                    apply(tvStatusException, GONE);
                    apply(llLastSeen, GONE);
                    apply(llTimeDifference, VISIBLE);
                    apply(tvProgress, GONE);

                    this.tvTrainStatus.setText("arrivato a destinazione");
                    this.tvTrainStatus.setTextColor(getTimeDifferenceColor(context, COLORS.BLACK));
                    this.tvTimeDifference.setText(train.getTimeDifference() + "'");
                    this.tvTimeDifference.setTextColor(getTimeDifferenceColor(context, train.getTimeDifference()));
                } else {
                    // in viaggio
                    apply(tvTrainStatus, VISIBLE);
                    apply(tvStatusException, VISIBLE);
                    apply(llLastSeen, VISIBLE);
                    apply(btnPin, VISIBLE);
                    apply(llTimeDifference, VISIBLE);

                    if (train.getTimeDifference() > 30) {
                        apply(tvWhyDelay, VISIBLE);
                        this.tvWhyDelay.setOnClickListener(v -> {
                            AnimationUtils.animOnPress(v, AnimationUtils.ANIM_TYPE.LIGHT);
                            presenter.onNewsUpdateRequested(train.getTrainId());
                        });
                    }

                    this.tvTrainStatus.setText("in viaggio");
                    if (train.getTrainStatusCode() == 1) {
                        this.tvTrainStatus.setTextColor(getTimeDifferenceColor(context, COLORS.GREEN));
                    } else {
                        this.tvTrainStatus.setTextColor(getTimeDifferenceColor(context, COLORS.BLACK));
                    }
                    this.tvStatusException.setText(parseTrainStatus(train.getTrainStatusCode(), tvStatusException));
                    if (train.getLastSeenStationName().equals("") || train.getLastSeenTimeReadable().equals("")) {
                        apply(this.llLastSeen, GONE);
                    } else {
                        apply(this.llLastSeen, VISIBLE);
                        this.tvLastSeenStationTime.setText(train.getLastSeenStationName() + " (" + train.getLastSeenTimeReadable() + ")");
                    }
                    this.tvTimeDifference.setText(train.getTimeDifference() + "'");
                    this.tvTimeDifference.setTextColor(getTimeDifferenceColor(context, train.getTimeDifference()));
                    this.tvProgress.setText(parseProgress(train.getProgress()));
                }
            } else {
                // non ancora partito
                apply(tvTrainStatus, VISIBLE);
                apply(tvStatusException, VISIBLE);
                apply(btnPin, VISIBLE);
                apply(llLastSeen, GONE);
                apply(tvTimeDifference, INVISIBLE);
                apply(tvProgress, INVISIBLE);
                this.tvTrainStatus.setText("non ancora partito");
                this.tvTrainStatus.setTextColor(getTimeDifferenceColor(context, COLORS.BLACK));
                this.tvStatusException.setText(parseTrainStatus(train.getTrainStatusCode(), tvStatusException));
                this.tvTimeDifference.setText(train.getTimeDifference() + "'");
                this.tvTimeDifference.setTextColor(getTimeDifferenceColor(context, train.getTimeDifference()));
            }

            if (train.getCancelledStopsInfo() != null && !train.getCancelledStopsInfo().equals("")) {
                apply(tvCancelledStopsInfo, VISIBLE);
                this.tvCancelledStopsInfo.setText(train.getCancelledStopsInfo());
                this.tvCancelledStopsInfo.setTextColor(getTimeDifferenceColor(context, COLORS.ORANGE));
            } else {
                apply(tvCancelledStopsInfo, GONE);
            }
        }

        private String parseProgress(int progress) {
            switch (progress) {
                case 0:
                    return "Costante";
                case 1:
                    return "Recuperando";
                case 2:
                    return "Rallentando";
                default:
                    return "";
            }
        }

        private String parseTrainStatus(int status, View v) {
            switch (status) {
                case 1:
                    this.tvStatusException.setTextColor(getTimeDifferenceColor(context, COLORS.GREEN));
                    return "";
                case 3:
                    this.tvStatusException.setTextColor(getTimeDifferenceColor(context, COLORS.ORANGE));
                    return "con fermate soppresse";
                case 4:
                    this.tvStatusException.setTextColor(getTimeDifferenceColor(context, COLORS.YELLOW));
                    return "con deviazioni";
                default:
                    return "";
            }
        }

        private String parseTrainHeadOrientation(int orientation) {
            switch (orientation) {
                case 0:
                    return "in coda";
                case 1:
                    return "in testa";
                default:
                    return "";
            }
        }
    }

    class StopHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_station)
        LinearLayout llStation;
        @BindView(R.id.tv_planned_departure_time)
        TextView tvPlannedDepartureTime;
        @BindView(R.id.tv_departure_time)
        TextView tvActualOrPlannedWithTimeDifference;
        @BindView(R.id.ll_node_first)
        LinearLayout llNodeFirst;
        @BindView(R.id.ll_node_in_between)
        LinearLayout llNodeInBetween;
        @BindView(R.id.ll_node_last)
        LinearLayout llNodeLast;
        @BindView(R.id.tv_station_name)
        TextView tvStationName;
        @BindView(R.id.ic_platform)
        ImageView icPlatform;
        @BindView(R.id.tv_platform)
        TextView tvPlatform;
        @BindView(R.id.tv_station_exception)
        TextView tvStationException;

        @BindView(R.id.iv_node_first)
        ImageView ivNodeFirst;
        @BindView(R.id.iv_node_in_between)
        ImageView ivNodeInBetween;
        @BindView(R.id.iv_node_last)
        ImageView ivNodeLast;

        @BindView(R.id.tv_mark_as_change_arrival)
        TextView tvMarkAsChangeArrival;
        @BindView(R.id.tv_mark_as_change_departure)
        TextView tvMarkAsChangeDeparture;
        @BindView(R.id.tv_mark_as_in_station)
        TextView tvMarkAsInStation;

        boolean isInStation;
        boolean trainIsOnTime;
        boolean hasPlatform;


        @BindViews({R.id.tv_planned_departure_time, R.id.tv_departure_time, R.id.tv_station_name, R.id.tv_platform})
        List<View> textViewsToBeColored;

        StopHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void bind(Train.Stop stop, Train train) {
            isInStation = stop.isVisited() && stop.getActualDepartureTime() == null;
            trainIsOnTime = train.getTimeDifference() != null && train.getTimeDifference() <= 0;
            hasPlatform = stop.getDeparturePlatform() == null || stop.getDeparturePlatform().equals("");

            tvPlannedDepartureTime.setText(stop.getPlannedDepartureTime().toString("HH:mm"));
            tvStationName.setText(stop.getStationName());
            parseCurrentStopTypeCode(stop.getCurrentStopTypeCode());

            switch (stop.getCurrentStopStatusCode()) {
                case 0:
                case 2:
                case 3:
                    // non visitata
                    // setto tutti i testi di colore nero
                    for (View v : textViewsToBeColored) {
                        ((TextView) v).setTextColor(getTimeDifferenceColor(context, COLORS.BLACK));
                    }

                    tvPlannedDepartureTime.setPaintFlags(0);
                    apply(tvMarkAsInStation, GONE);

                    if (train.isDeparted()) {
                        if (trainIsOnTime) {
                            tvPlannedDepartureTime.setTextColor(getTimeDifferenceColor(context, 0));
                            apply(tvActualOrPlannedWithTimeDifference, GONE);
                        } else {
                            apply(tvActualOrPlannedWithTimeDifference, VISIBLE);
                            tvActualOrPlannedWithTimeDifference.setText(stop.getPlannedDepartureTime().plusMinutes(train.getTimeDifference()).toString("HH:mm"));
                            tvActualOrPlannedWithTimeDifference.setTextColor(getTimeDifferenceColor(context, train.getTimeDifference()));
                        }
                    } else {
                        apply(tvActualOrPlannedWithTimeDifference, GONE);
                    }
                    break;
                case 1:
                    // visitata
                    for (View v : textViewsToBeColored) {
                        ((TextView) v).setTextColor(getTimeDifferenceColor(context, COLORS.GREY_LIGHTER));
                    }

                    tvPlannedDepartureTime.setPaintFlags(tvPlannedDepartureTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    if (isInStation) {
                        Log.d("bind: in station");
                        apply(tvActualOrPlannedWithTimeDifference, GONE);
                        apply(tvMarkAsInStation, VISIBLE);
                    } else if (stop.getActualDepartureTime() != null) {
                        apply(tvMarkAsInStation, GONE);
                        apply(tvActualOrPlannedWithTimeDifference, VISIBLE);
                        tvActualOrPlannedWithTimeDifference.setText(stop.getActualDepartureTime().toString("HH:mm"));
                    } else {
                        apply(tvMarkAsInStation, GONE);
                        apply(tvActualOrPlannedWithTimeDifference, GONE);
                    }
                    break;
                default:
                    break;
            }

            parseMarkingsAsChangeDepartureOrArrival(stop.getStationId());

            switch (stop.getCurrentStopStatusCode()) {
                case 2:
                    apply(tvStationException, VISIBLE);
                    tvStationException.setText("Straordinaria");
                    tvStationException.setTextColor(getTimeDifferenceColor(context, COLORS.ORANGE));
                    break;
                case 3:
                    apply(tvStationException, VISIBLE);
                    tvStationException.setText("Soppressa");
                    tvStationException.setTextColor(getTimeDifferenceColor(context, COLORS.RED));
                    parseCurrentStopTypeCode(4);
                    break;
                default:
                    apply(tvStationException, GONE);
                    tvStationException.setText("");
                    break;
            }

            ///////////////////////////////////////////////////////

            if (hasPlatform) {
                apply(icPlatform, GONE);
                apply(tvPlatform, GONE);
            } else {
                apply(icPlatform, VISIBLE);
                apply(tvPlatform, VISIBLE);
                tvPlatform.setText(stop.getDeparturePlatform());
            }
        }

        private void parseMarkingsAsChangeDepartureOrArrival(String id) {
            if (solution.hasChanges()) {
                Journey.Solution.Change c;
                try {
                    c = solution.getChangesList().get(presenter.getTrainIndexForAdapterPosition(getAdapterPosition()));
                    check(id, c.getDepartureStationId(), c.getArrivalStationId());
                } catch (IndexOutOfBoundsException e) {
                    FirebaseCrash.report(new Exception("Got arrayoutofboundexception in parsemarkingaschange... for solution: DEPID" + solution.getDepartureStationId() + ", DEPID " + solution.getArrivalStationId() + " DEP TIME " + solution.getDepartureTimeReadable()));
                }
            } else {
                check(id, solution.getDepartureStationId(), solution.getArrivalStationId());
            }
        }

        private void check(String idCurrent, String idDeparture, String idArrival) {
            if (idDeparture == null || idArrival == null) {
                return;
            }
            if (idCurrent.equals(idDeparture)) {
                apply(tvMarkAsChangeDeparture, VISIBLE);
                apply(tvMarkAsChangeArrival, GONE);
            } else if (idCurrent.equals(idArrival)) {
                apply(tvMarkAsChangeDeparture, GONE);
                apply(tvMarkAsChangeArrival, VISIBLE);
            } else {
                apply(tvMarkAsChangeDeparture, GONE);
                apply(tvMarkAsChangeArrival, GONE);
            }
        }


        private void parseCurrentStopTypeCode(int code) {
            switch (code) {
                case 1:
                    // first
                    apply(llNodeFirst, VISIBLE);
                    apply(llNodeInBetween, GONE);
                    apply(llNodeLast, GONE);
                    break;
                case 2:
                    // middle
                    apply(llNodeFirst, GONE);
                    apply(llNodeInBetween, VISIBLE);
                    apply(llNodeLast, GONE);
                    break;
                case 3:
                    // last
                    apply(llNodeFirst, GONE);
                    apply(llNodeInBetween, GONE);
                    apply(llNodeLast, VISIBLE);
                    break;
                case 4:
                    // suppressed
                    apply(llNodeFirst, GONE);
                    apply(llNodeInBetween, GONE);
                    apply(llNodeLast, GONE);
                    break;
                default:
                    break;
            }
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_search_after)
        ImageButton btn;
        @BindView(R.id.rl_search_after_loading_spinner)
        RelativeLayout relativeLayout;

        FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            apply(btn, GONE);
            apply(relativeLayout, GONE);
        }
    }
}
