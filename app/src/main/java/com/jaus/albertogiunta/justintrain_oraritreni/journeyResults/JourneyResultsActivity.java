package com.jaus.albertogiunta.justintrain_oraritreni.journeyResults;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.journeySearch.JourneySearchActivity;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.DateTimeAdapter;
import com.jaus.albertogiunta.justintrain_oraritreni.networking.PostProcessingEnabler;
import com.jaus.albertogiunta.justintrain_oraritreni.notification.NotificationService;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.components.AnimationUtils;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.components.HideShowScrollListener;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_ERROR_BTN_STATUS;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_SNACKBAR_ACTIONS;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;

import org.joda.time.DateTime;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import trikita.log.Log;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_REFRESH_JOURNEY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_REMOVE_FAVOURITE_FROM_RESULTS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_SET_FAVOURITE_FROM_RESULTS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_SWAP_STATIONS_FROM_RESULTS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ERROR_CONNECTIVITY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ERROR_NOT_FOUND_JOURNEY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ERROR_SERVER;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ERROR_SERVICE_UNAVAILABLE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SCREEN_JOURNEY_RESULTS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_FROM_RESULTS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_STATIONS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_INTENT.I_TIME;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_SNACKBAR_ACTIONS.NONE;

public class JourneyResultsActivity extends AppCompatActivity implements JourneyResultsContract.View {

    public static final String ACTION = "com.jaus.albertogiunta.justintrain_oraritreni.OPEN_DYNAMIC_SHORTCUT";

    // The Native Express ad unit ID.
//    private static final String AD_UNIT_ID = "ca-app-pub-8963908741443055/6579349527";

    JourneyResultsContract.Presenter presenter;
    AnalyticsHelper                  analyticsHelper;
    BroadcastReceiver                messageReceiver;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rl_header_2)
    RelativeLayout rlHeader2;

    @BindView(R.id.tv_departure_station_name)
    TextView    tvHeaderDepartureStation;
    @BindView(R.id.tv_arrival_station_name)
    TextView    tvHeaderArrivalStation;
    @BindView(R.id.btn_header_swap_station_names)
    ImageButton btnHeaderSwapStationNames;
    @BindView(R.id.btn_toggle_favourite)
    ImageButton btnHeaderToggleFavorite;

    @BindView(R.id.loading_spinner)
    ProgressBar progressBar;

    // NO SOLUTION FOUND
    @BindView(R.id.rl_error)
    RelativeLayout rlEmptyJourneyBox;
    @BindView(R.id.tv_error_message)
    TextView       tvErrorMessage;
    @BindView(R.id.btn_error_button)
    Button         btnErrorMessage;

    //  RESULTS
    @BindView(R.id.rv_journey_solutions)
    RecyclerView rvJourneySolutions;
    @BindView(R.id.btn_refresh)
    ImageButton  btnRefresh;

    JourneyResultsAdapter journeyResultsAdapter;

    private long refreshBtnLastClickTime = SystemClock.elapsedRealtime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_results);
        ButterKnife.bind(this);
        analyticsHelper = AnalyticsHelper.getInstance(getViewContext());
        presenter = new JourneyResultsPresenter(this);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnHeaderSwapStationNames.setOnClickListener(v -> {
            AnimationUtils.animOnPress(v, AnimationUtils.ANIM_TYPE.MEDIUM);
            analyticsHelper.logScreenEvent(SCREEN_JOURNEY_RESULTS, ACTION_SWAP_STATIONS_FROM_RESULTS);
            presenter.onSwapButtonClick();
        });
        btnHeaderToggleFavorite.setOnClickListener(v -> presenter.onFavouriteButtonClick());

        rvJourneySolutions.setHasFixedSize(true);
        rvJourneySolutions.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        journeyResultsAdapter = new JourneyResultsAdapter(this, presenter);
        rvJourneySolutions.setAdapter(journeyResultsAdapter);

        rvJourneySolutions.addOnScrollListener(new HideShowScrollListener() {
            @Override
            public void onHide() {
                btnRefresh.animate().setInterpolator(new LinearInterpolator()).translationY(200).setDuration(100);
            }

            @Override
            public void onShow() {
                btnRefresh.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(100);
            }
        });

        btnRefresh.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - refreshBtnLastClickTime > 1000) {
                AnimationUtils.animOnPress(v, AnimationUtils.ANIM_TYPE.LIGHT);
                analyticsHelper.logScreenEvent(SCREEN_JOURNEY_RESULTS, ACTION_REFRESH_JOURNEY);
                presenter.searchFromSearch(true);
            }
            refreshBtnLastClickTime = SystemClock.elapsedRealtime();
        });
        presenter.setState(getIntent().getExtras());
        presenter.searchFromSearch(true);

        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showSnackbar(intent.getExtras().getString(NotificationService.NOTIFICATION_ERROR_MESSAGE), NONE, Snackbar.LENGTH_SHORT);
            }
        };
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                presenter.setState(data.getExtras());
                presenter.searchFromSearch(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(presenter.getState(outState));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        presenter.setState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter(NotificationService.NOTIFICATION_ERROR_EVENT));
//        presenter.setState(getIntent().getExtras());
    }

    @Override
    protected void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public Context getViewContext() {
        return JourneyResultsActivity.this;
    }

    @Override
    public void showSnackbar(String message, ENUM_SNACKBAR_ACTIONS intent, int duration) {
        journeyResultsAdapter.notifyItemChanged(0);
        journeyResultsAdapter.notifyItemChanged(journeyResultsAdapter.getItemCount() - 1);
        Log.w(android.R.id.message);
        Snackbar snackbar = Snackbar
                .make(rvJourneySolutions, message, duration);
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(ContextCompat.getColor(this, R.color.txt_white));
        switch (intent) {
            case NONE:
                break;
            case REFRESH:
                snackbar.setAction("Aggiorna", view -> presenter.searchFromSearch(true)).setActionTextColor(ContextCompat.getColor(this, R.color.btn_cyan));
                break;
            default:
                break;
        }
        snackbar.show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        rvJourneySolutions.setVisibility(View.GONE);
        rlEmptyJourneyBox.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        rvJourneySolutions.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage(String tvMessage, String btnMessage, ENUM_ERROR_BTN_STATUS intent) {
        progressBar.setVisibility(View.GONE);
        rvJourneySolutions.setVisibility(View.GONE);
        rlEmptyJourneyBox.setVisibility(View.VISIBLE);
        btnErrorMessage.setText(btnMessage);
        tvErrorMessage.setText(tvMessage);
        btnErrorMessage.setOnClickListener(v -> {
            Intent i;
            switch (intent) {
                case CONN_SETTINGS:
                    Log.d("intent a settings");
                    analyticsHelper.logScreenEvent(SCREEN_JOURNEY_RESULTS, ERROR_CONNECTIVITY);
                    i = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(i);
                    break;
                case SEND_REPORT:
                    analyticsHelper.logScreenEvent(SCREEN_JOURNEY_RESULTS, ERROR_SERVER);
                    FirebaseCrash.report(new Exception(SCREEN_JOURNEY_RESULTS + ERROR_SERVER + ": " +
                            "" + presenter.getPreferredJourney().getStation1().getStationLongId() +
                            " > " + presenter.getPreferredJourney().getStation2().getStationLongId() +
                            " @ " + presenter.getTimeOfSearch().toString("HH:mm")));
                    showSnackbar("Messaggio ricevuto! Grazie per l'aiuto!", NONE, Snackbar.LENGTH_LONG);
                    Log.d("intent a report");
                    break;
                case NO_SOLUTIONS:
                    analyticsHelper.logScreenEvent(SCREEN_JOURNEY_RESULTS, ERROR_NOT_FOUND_JOURNEY);
                    Log.d("intent a ricerca");
                    finish();
                    break;
                case SERVICE_UNAVAILABLE:
                    analyticsHelper.logScreenEvent(SCREEN_JOURNEY_RESULTS, ERROR_SERVICE_UNAVAILABLE);
                    Log.d("service unavailable");
                    analyticsHelper.logScreenEvent(SCREEN_JOURNEY_RESULTS, ACTION_REFRESH_JOURNEY);
                    presenter.searchFromSearch(true);
                    break;
            }
        });
    }

    @Override
    public void scrollToFirstFeasibleSolution(int position) {
        Log.d("scrolling to ", position);
        ((LinearLayoutManager) rvJourneySolutions.getLayoutManager()).scrollToPositionWithOffset(position, 14);
    }

    @Override
    public void updateSolutionsList() {
        rvJourneySolutions.getRecycledViewPool().clear();
        try {
            journeyResultsAdapter.notifyDataSetChanged(); //TODO possibile bug http://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in
        } catch (Exception e) {
            FirebaseCrash.report(new Exception("CATCHED Inconsistency detected. Invalid view holder adapter positionViewHolder"));
        }
    }

    @Override
    public void updateSolution(int elementIndex) {
        Log.d("updateSolution: ", elementIndex);
        journeyResultsAdapter.notifyItemChanged(elementIndex);
    }

    @Override
    public void setFavouriteButtonStatus(boolean isPreferred) {
        if (isPreferred) {
            analyticsHelper.logScreenEvent(SCREEN_JOURNEY_RESULTS, ACTION_SET_FAVOURITE_FROM_RESULTS);
            this.btnHeaderToggleFavorite.setImageResource(R.drawable.ic_star_black);
            AnimationUtils.animOnPress(this.btnHeaderToggleFavorite, AnimationUtils.ANIM_TYPE.MEDIUM);
        } else {
            analyticsHelper.logScreenEvent(SCREEN_JOURNEY_RESULTS, ACTION_REMOVE_FAVOURITE_FROM_RESULTS);
            this.btnHeaderToggleFavorite.setImageResource(R.drawable.ic_star_border);
        }
    }

    @Override
    public void setStationNames(String departure, String arrival) {
        this.tvHeaderDepartureStation.setText(departure);
        this.tvHeaderArrivalStation.setText(arrival);
    }

    @OnTouch({R.id.hsv_arrival_station_name, R.id.hsv_departure_station_name})
    public boolean openSearchActivity(MotionEvent event) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Intent intent = new Intent(JourneyResultsActivity.this, JourneySearchActivity.class);
            intent.putExtra(I_STATIONS, gson.toJson(presenter.getPreferredJourney()));
            intent.putExtra(I_TIME, presenter.getTimeOfSearch().getMillis());
            intent.putExtra(I_FROM_RESULTS, true);
            JourneyResultsActivity.this.startActivityForResult(intent, 1);
        }
        return false;
    }
}
