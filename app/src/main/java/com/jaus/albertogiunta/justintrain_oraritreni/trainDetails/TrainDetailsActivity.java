package com.jaus.albertogiunta.justintrain_oraritreni.trainDetails;

import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.crash.FirebaseCrash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.data.News;
import com.jaus.albertogiunta.justintrain_oraritreni.journeyFavourites.FavouriteJourneysActivity;
import com.jaus.albertogiunta.justintrain_oraritreni.notification.NotificationService;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.Ads;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.WrapContentLinearLayoutManager;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.components.AnimationUtils;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.components.HideShowScrollListener;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_ERROR_BTN_STATUS;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_SNACKBAR_ACTIONS;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;

import org.joda.time.DateTime;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import trikita.log.Log;

import static butterknife.ButterKnife.apply;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.VISIBLE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_REFRESH_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_REMOVE_FAVOURITE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_SET_FAVOURITE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_SET_FAVOURITE_WITH_JOURNEY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_SHOW_NEWS_DIALOG;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ERROR_CONNECTIVITY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ERROR_NOT_FOUND_SOLUTION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ERROR_SERVER;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SCREEN_SOLUTION_DETAILS;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_SNACKBAR_ACTIONS.NONE;

public class TrainDetailsActivity extends AppCompatActivity implements TrainDetailsContract.View {

    TrainDetailsContract.Presenter presenter;
    AnalyticsHelper                analyticsHelper;
    BroadcastReceiver              messageReceiver;

    MenuItem menuItem;

    @BindView(R.id.loading_spinner)
    ProgressBar          progressBar;
    @BindView(R.id.rv_train_details)
    RecyclerView         rvTrainDetails;
    @BindView(R.id.btn_refresh)
    ImageButton          btnRefresh;
    @BindView(R.id.btn_share_single)
    FloatingActionButton btnShareSingle;
    @BindView(R.id.btn_share_multiple)
    FloatingActionMenu   btnShareMultiple;

    @BindView(R.id.rl_error)
    RelativeLayout rlEmptyJourneyBox;
    @BindView(R.id.tv_error_message)
    TextView       tvErrorMessage;
    @BindView(R.id.btn_error_button)
    Button         btnErrorMessage;

    @BindView(R.id.adView)
    NativeExpressAdView adView;

    TrainDetailsAdapter adapter;
    private long refreshBtnLastClickTime = SystemClock.elapsedRealtime();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_details);
        ButterKnife.bind(this);
        analyticsHelper = AnalyticsHelper.getInstance(getViewContext());
        Ads.initializeAds(getViewContext(), adView);

        presenter = new TrainDetailsPresenter(this);
        presenter.setState(getIntent().getExtras());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new TrainDetailsAdapter(getApplicationContext(), presenter);
        rvTrainDetails.setAdapter(adapter);
        rvTrainDetails.setHasFixedSize(true);
//        rvTrainDetails.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvTrainDetails.setLayoutManager(new WrapContentLinearLayoutManager(getViewContext(), LinearLayoutManager.VERTICAL, false));
        rvTrainDetails.addOnScrollListener(new HideShowScrollListener() {
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
                analyticsHelper.logScreenEvent(SCREEN_SOLUTION_DETAILS, ACTION_REFRESH_SOLUTION);
                presenter.refreshRequested();
            }
            refreshBtnLastClickTime = SystemClock.elapsedRealtime();
        });

        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showSnackbar(intent.getExtras().getString(NotificationService.NOTIFICATION_ERROR_MESSAGE), NONE, Snackbar.LENGTH_SHORT);
            }
        };

        presenter.updateRequested();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fav_train_menu, menu);
        this.menuItem = menu.findItem(R.id.action_fav_train);
        setFavouriteButtonStatus(presenter.isSolutionPreferred());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_fav_train:
                presenter.onFavouriteButtonClick();
                break;
            case android.R.id.home:
                if (isTaskRoot()) {
                    Intent i = new Intent(this, FavouriteJourneysActivity.class);
                    startActivity(i);
                }
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
    protected void onResume() {
        super.onResume();
//        adView.resume();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter(NotificationService.NOTIFICATION_ERROR_EVENT));
        presenter.setState(getIntent().getExtras());
    }

    @Override
    protected void onPause() {
//        adView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public Context getViewContext() {
        return TrainDetailsActivity.this;
    }

    @Override
    public void setFavouriteButtonStatus(boolean isPreferred) {
        if (isPreferred) {
            analyticsHelper.logScreenEvent(SCREEN_SOLUTION_DETAILS, ACTION_SET_FAVOURITE);
            this.menuItem.setIcon(R.drawable.ic_star_black);
        } else {
            analyticsHelper.logScreenEvent(SCREEN_SOLUTION_DETAILS, ACTION_REMOVE_FAVOURITE);
            this.menuItem.setIcon(R.drawable.ic_star_border);
        }
    }

    @Override
    public void showSnackbar(String message, ENUM_SNACKBAR_ACTIONS intent, int duration) {
        Log.w(message);
        Snackbar snackbar = Snackbar
                .make(rvTrainDetails, message, duration);
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(ContextCompat.getColor(this, R.color.txt_white));
        switch (intent) {
            case NONE:
                break;
            case REFRESH:
                snackbar.setAction("Aggiorna", view -> presenter.refreshRequested()).setActionTextColor(ContextCompat.getColor(this, R.color.btn_cyan));
                break;
            default:
                break;
        }
        snackbar.show();
    }

    @SuppressWarnings("RestrictedApi")
    @Override
    public void setShareButton(List<String> trainCatAndId) {
        if (trainCatAndId.size() == 1) {
            apply(btnShareSingle, VISIBLE);
            btnShareSingle.setImageDrawable(AppCompatDrawableManager.get().getDrawable(TrainDetailsActivity.this, R.drawable.ic_share));
            btnShareSingle.setOnClickListener(v -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, presenter.getShareableString());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Condividi l'andamento del Treno via..."));
            });
        } else {
            apply(btnShareMultiple, VISIBLE);
            btnShareMultiple.removeAllMenuButtons();
            btnShareMultiple.setClosedOnTouchOutside(true);
//            btnShareMultiple.setImageDrawable(AppCompatDrawableManager.get().getDrawable(TrainDetailsActivity.this, R.drawable.ic _share));
            for (String s : trainCatAndId) {
                FloatingActionButton menuItem = new FloatingActionButton(TrainDetailsActivity.this);
                menuItem.setLabelText(s);
                menuItem.setButtonSize(FloatingActionButton.SIZE_MINI);
                menuItem.setImageDrawable(AppCompatDrawableManager.get().getDrawable(TrainDetailsActivity.this, R.drawable.ic_share));
                menuItem.setColorNormalResId(R.color.btn_cyan);
                menuItem.setOnClickListener(v -> {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, presenter.getShareableString(trainCatAndId.indexOf(s)));
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Condividi l'andamento del Treno via..."));
                });
                btnShareMultiple.addMenuButton(menuItem);
            }
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        rvTrainDetails.setVisibility(View.GONE);
        rlEmptyJourneyBox.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        rvTrainDetails.setVisibility(View.VISIBLE);
        rlEmptyJourneyBox.setVisibility(View.GONE);
    }

    @Override
    public void updateTrainDetails() {
        rvTrainDetails.getRecycledViewPool().clear();
        try {
            adapter.notifyDataSetChanged(); //TODO possibile bug http://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in
        } catch (Exception e) {
            FirebaseCrash.report(new Exception("CATCHED Inconsistency detected. Invalid view holder adapter positionViewHolder"));
        }
    }

    @Override
    public void showNewsDialog(News news) {
        analyticsHelper.logScreenEvent(SCREEN_SOLUTION_DETAILS, ACTION_SHOW_NEWS_DIALOG);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Informazioni sul ritardo");
        alertDialog.setMessage(news.getMessage());
        alertDialog.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        alertDialog.create().show();
    }

    @Override
    public void showAddSolutionAndJourneyToFavouritesDialog() {
        analyticsHelper.logScreenEvent(SCREEN_SOLUTION_DETAILS, ACTION_SET_FAVOURITE_WITH_JOURNEY);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Attenzione!");
        alertDialog.setMessage("Verrà aggiunta anche la tratta cercata ai Preferiti.\n\nContinuare?\n");
        alertDialog.setPositiveButton("Aggiungi ai preferiti", (dialog, which) -> {
            presenter.onDoubleAdditionConfirmation();
            dialog.dismiss();
        });
        alertDialog.setNegativeButton("Cancella", (dialog, which) -> dialog.dismiss());
        alertDialog.create().show();
    }

    @Override
    public void showErrorMessage(String tvMessage, String btnMessage, ENUM_ERROR_BTN_STATUS intent) {
        progressBar.setVisibility(View.GONE);
        rvTrainDetails.setVisibility(View.GONE);
        rlEmptyJourneyBox.setVisibility(View.VISIBLE);
        btnErrorMessage.setText(btnMessage);
        tvErrorMessage.setText(tvMessage);
        btnErrorMessage.setOnClickListener(v -> {
            Intent i;
            switch (intent) {
                case CONN_SETTINGS:
                    analyticsHelper.logScreenEvent(SCREEN_SOLUTION_DETAILS, ERROR_CONNECTIVITY);
                    Log.d("intent a settings");
                    i = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(i);
                    break;
                case SEND_REPORT:
                    analyticsHelper.logScreenEvent(SCREEN_SOLUTION_DETAILS, ERROR_SERVER);
                    FirebaseCrash.report(new Exception(SCREEN_SOLUTION_DETAILS + ERROR_SERVER + ": " +
                            "" + presenter.getSolution().getDepartureStationId() +
                            " > " + presenter.getSolution().getArrivalStationId() +
                            " @ " + presenter.getSolution().getDepartureTimeReadable() +
                            " currTime " + DateTime.now().toString("HH:mm")));
                    showSnackbar("Messaggio ricevuto! Grazie per l'aiuto!", NONE, Snackbar.LENGTH_LONG);
                    Log.d("intent a report");
                    break;
                case NO_SOLUTIONS:
                    analyticsHelper.logScreenEvent(SCREEN_SOLUTION_DETAILS, ERROR_NOT_FOUND_SOLUTION);
                    Log.d("intent a ricerca");
                    finish();
                    break;
            }
        });
    }
}
