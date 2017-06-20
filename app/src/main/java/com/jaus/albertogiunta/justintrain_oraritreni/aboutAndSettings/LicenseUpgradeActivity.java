package com.jaus.albertogiunta.justintrain_oraritreni.aboutAndSettings;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LicenseUpgradeActivity extends AppCompatActivity {

    AnalyticsHelper analyticsHelper;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_upgrade);
        ButterKnife.bind(this);
        analyticsHelper = AnalyticsHelper.getInstance(LicenseUpgradeActivity.this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
