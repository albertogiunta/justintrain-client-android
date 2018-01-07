package com.jaus.albertogiunta.justintrain_oraritreni.aboutAndSettings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.CustomIABHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.IAB.IabHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.IAB.IabResult;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.IAB.Inventory;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.ProPreferences;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SettingsPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.LinkedList;
import java.util.List;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_AUTOREMOVE_NOTIFICATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_CHANGES;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_COMPACT_NOTIFICATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_INSTANT_DELAY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_LIGHTNING;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_LIVE_NOTIFICATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_PREEMPTIVE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_RECENT_SEARCHES;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_VIBRATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_AUTOREMOVE_NOTIFICATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_CHANGES;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_COMPACT_NOTIFICATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_INSTANT_DELAY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_LIGHTNING;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_LIVE_NOTIFICATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_PREEMPTIVE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_RECENT_SEARCHES;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_VIBRATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_GET_PREEMPTIVE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_INCLUDE_CHANGES;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_INSTANT_DELAY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_LIGHTNING;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_NOTIF_AUTO_REMOVE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_NOTIF_COMPACT;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_NOTIF_LIVE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_NOTIF_VIBRATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_RECENT;

public class SettingsActivity extends AppCompatActivity implements IabHelper.QueryInventoryFinishedListener {


    private CustomIABHelper iabHelper;
    private boolean            isPro         = false;
    private List<CheckBox>     checkBoxes    = new LinkedList<>();
    private List<SwitchCompat> switchCompats = new LinkedList<>();


    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        View           view1;
        LayoutInflater inflater = LayoutInflater.from(this);
        view1 = inflater.inflate(R.layout.activity_about_settings, null);
        linearLayout = view1.findViewById(R.id.ll_container);

        this.iabHelper = CustomIABHelper.getInstance(SettingsActivity.this, this);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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

    private void buildView() {
        View aboutPage = new AboutPageBuilder(this)
                .addItem(new ItemBuilder(this, linearLayout).addItemGroupHeader("Preferiti").build())
                // RECENT SEARCHES
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Ricerche recenti", "Visualizza le tue ultime tratte ricercate", SettingsPreferences.isRecentEnabled(this), true, false, isPro, SP_SETT_RECENT, SETTINGS_ENABLE_RECENT_SEARCHES, SETTINGS_DISABLE_RECENT_SEARCHES)
                        .build())
                // FAV TRAIN VIBRATION
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Ritardo ISTANTANEO (PRO)", "Quando accedi alla schermata iniziale", ProPreferences.isInstantDelayEnabled(this), true, true, isPro, SP_SETT_INSTANT_DELAY, SETTINGS_ENABLE_INSTANT_DELAY, SETTINGS_DISABLE_INSTANT_DELAY)
                        .build())
                .addItem(new ItemBuilder(this, linearLayout).addItemGroupHeader("Notifica").build())
                // NOTIFICATION VIBRATION
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Vibrazione nella notifica", "Quando la attivi o la aggiorni", SettingsPreferences.isVibrationEnabled(this), true, false, isPro, SP_SETT_NOTIF_VIBRATION, SETTINGS_ENABLE_VIBRATION, SETTINGS_DISABLE_VIBRATION)
                        .build())
                // LIVE NOTIFICATION
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Notifiche LIVE (PRO)", "Sempre aggiornate all'ultimo secondo, ogni volta che accendi lo schermo", ProPreferences.isLiveNotificationEnabled(this), true, true, isPro, SP_SETT_NOTIF_LIVE, SETTINGS_ENABLE_LIVE_NOTIFICATION, SETTINGS_DISABLE_LIVE_NOTIFICATION)
                        .build())
                // LIVE NOTIFICATION
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Notifiche COMPATTE (PRO)", "Per vedere tutte le informazioni che ti servono in maniera sempre più veloce", ProPreferences.isCompactNotificationEnabled(this), true, true, isPro, SP_SETT_NOTIF_COMPACT, SETTINGS_ENABLE_COMPACT_NOTIFICATION, SETTINGS_DISABLE_COMPACT_NOTIFICATION)
                        .build())
                // AUTO REMOVE NOTIFICATION
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Notifiche SMART (PRO)", "La notifica saprà quando sei arrivato a destinazione e se ne andrà da sola, senza che tu debba far nulla", ProPreferences.isCompactNotificationEnabled(this), true, true, isPro, SP_SETT_NOTIF_AUTO_REMOVE, SETTINGS_ENABLE_AUTOREMOVE_NOTIFICATION, SETTINGS_DISABLE_AUTOREMOVE_NOTIFICATION)
                        .build())
                .addItem(new ItemBuilder(this, linearLayout).addItemGroupHeader("Risultati").build())
                // SMART AUTOSCROLL (LIGHTNING)
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Autoscroll intelligente", "Sulla prima soluzione prendibile", SettingsPreferences.isLightningEnabled(this), true, false, isPro, SP_SETT_LIGHTNING, SETTINGS_ENABLE_LIGHTNING, SETTINGS_DISABLE_LIGHTNING)
                        .build())
                .addSeparator()
                // PREEMPTIVE SEARCH
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Cerca con precauzione", "Includendo una prima soluzione in più", SettingsPreferences.isPreemptiveEnabled(this), true, false, isPro, SP_SETT_GET_PREEMPTIVE, SETTINGS_ENABLE_PREEMPTIVE, SETTINGS_DISABLE_PREEMPTIVE)
                        .build())
                .addItem(new ItemBuilder(this, linearLayout).addItemGroupHeader("Filtri di ricerca").build())
                // INCLUDE CHANGES
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Includi le soluzioni con cambi", "Se disattivata, e non appaiono soluzioni, potrebbe essere necessario cercare ad un orario differente", SettingsPreferences.isIncludeChangesEnabled(this), true, false, isPro, SP_SETT_INCLUDE_CHANGES, SETTINGS_ENABLE_CHANGES, SETTINGS_DISABLE_CHANGES)
                        .build())
                .addSeparator()
                // EXCLUDE CATEGORIES
                .addItem(new ItemBuilder(this, linearLayout).addItemTrainCategories(this)
                        .build())
                .addSeparator()
                .build();

        setContentView(aboutPage);
    }

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
        if (CustomIABHelper.isOrderOk(result, inv)) {
            isPro = true;
        }
        buildView();
    }
}