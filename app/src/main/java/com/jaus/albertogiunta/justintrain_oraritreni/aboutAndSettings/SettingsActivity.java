package com.jaus.albertogiunta.justintrain_oraritreni.aboutAndSettings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.SettingsPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_CHANGES;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_INSTANT_DELAY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_LIGHTNING;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_PREEMPTIVE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_DISABLE_VIBRATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_CHANGES;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_INSTANT_DELAY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_LIGHTNING;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_PREEMPTIVE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SETTINGS_ENABLE_VIBRATION;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_GET_PREEMPTIVE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_INCLUDE_CHANGES;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_INSTANT_DELAY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_LIGHTNING;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.SP_SETT_NOTIF_VIBRATION;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        View           view1;
        LinearLayout   linearLayout;
        LayoutInflater inflater = LayoutInflater.from(this);
        view1 = inflater.inflate(R.layout.activity_about_settings, null);
        linearLayout = (LinearLayout) view1.findViewById(R.id.ll_container);

        View aboutPage = new AboutPageBuilder(this)
                .addItem(new ItemBuilder(this, linearLayout).addItemGroupHeader("Preferiti").build())
                // FAV TRAIN VIBRATION
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Ritardo istantaneo", "Quando accedi alla schermata iniziale", SettingsPreferences.isInstantDelayEnabled(this), true, SP_SETT_INSTANT_DELAY, SETTINGS_ENABLE_INSTANT_DELAY, SETTINGS_DISABLE_INSTANT_DELAY)
                        .build())
                .addItem(new ItemBuilder(this, linearLayout).addItemGroupHeader("Notifica").build())
                // NOTIFICATION VIBRATION
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Vibrazione nella notifica", "Quando la attivi o la aggiorni", SettingsPreferences.isVibrationEnabled(this), true, SP_SETT_NOTIF_VIBRATION, SETTINGS_ENABLE_VIBRATION, SETTINGS_DISABLE_VIBRATION)
                        .build())
                .addItem(new ItemBuilder(this, linearLayout).addItemGroupHeader("Risultati").build())
                // SMART AUTOSCROLL (LIGHTNING)
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Autoscroll intelligente", "Sulla prima soluzione prendibile", SettingsPreferences.isLightningEnabled(this), true, SP_SETT_LIGHTNING, SETTINGS_ENABLE_LIGHTNING, SETTINGS_DISABLE_LIGHTNING)
                        .build())
                .addSeparator()
                // PREEMPTIVE SEARCH
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Cerca con precauzione", "Includendo una prima soluzione in più", SettingsPreferences.isPreemptiveEnabled(this), true, SP_SETT_GET_PREEMPTIVE, SETTINGS_ENABLE_PREEMPTIVE, SETTINGS_DISABLE_PREEMPTIVE)
                        .build())
                .addItem(new ItemBuilder(this, linearLayout).addItemGroupHeader("Filtri di ricerca").build())
                // INCLUDE CHANGES
                .addItem(new ItemBuilder(this, linearLayout).addItemPrefWithToggle(this, "Includi le soluzioni con cambi", "Se disattivata, e non appaiono soluzioni, potrebbe essere necessario cercare ad un orario differente", SettingsPreferences.isIncludeChangesEnabled(this), true, SP_SETT_INCLUDE_CHANGES, SETTINGS_ENABLE_CHANGES, SETTINGS_DISABLE_CHANGES)
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
}