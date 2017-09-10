package com.jaus.albertogiunta.justintrain_oraritreni.aboutAndSettings;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.jaus.albertogiunta.justintrain_oraritreni.R;

import net.yslibrary.licenseadapter.GitHubLicenseEntry;
import net.yslibrary.licenseadapter.LicenseAdapter;
import net.yslibrary.licenseadapter.LicenseEntry;
import net.yslibrary.licenseadapter.Licenses;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.ArrayList;
import java.util.List;

public class LicencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenses);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        List<LicenseEntry> licenses = new ArrayList<>();

        licenses.add(Licenses.noContent("Android SDK", "Google Inc.",
                "https://developer.android.com/sdk/terms.html"));
        licenses.add(Licenses.fromGitHub("gabrielemariotti/changeloglib", Licenses.LICENSE_APACHE_V2));
        licenses.add(Licenses.fromGitHubApacheV2("jakewharton/butterknife"));
        licenses.add(Licenses.fromGitHubApacheV2("evant/gradle-retrolambda"));
        licenses.add(Licenses.fromGitHubApacheV2("google/gson", Licenses.FILE_NO_EXTENSION));
        licenses.add(Licenses.fromGitHubApacheV2("yshrsmz/LicenseAdapter", Licenses.FILE_NO_EXTENSION));
        licenses.add(Licenses.fromGitHubMIT("zserge/log", Licenses.FILE_NO_EXTENSION));
        licenses.add(Licenses.fromGitHubApacheV2("dlew/joda-time-android", Licenses.FILE_NO_EXTENSION));
        licenses.add(Licenses.fromGitHubApacheV2("apl-devs/AppIntro", Licenses.FILE_NO_EXTENSION));
        licenses.add(Licenses.fromGitHub("vcalvello/SwipeToAction", Licenses.LICENSE_APACHE_V2));
        licenses.add(Licenses.fromGitHubApacheV2("grantland/android-autofittextview"));
        licenses.add(Licenses.fromGitHubApacheV2("aNNiMON/Lightweight-Stream-API", Licenses.FILE_NO_EXTENSION));
        licenses.add(Licenses.fromGitHubApacheV2("square/retrofit"));

        licenses.add(
                new GitHubLicenseEntry(Licenses.NAME_APACHE_V2, "ReactiveX/RxAndroid", "1.x/", null,
                        Licenses.FILE_NO_EXTENSION));
        licenses.add(new GitHubLicenseEntry(Licenses.NAME_APACHE_V2, "ReactiveX/RxJava", "1.x/", null,
                Licenses.FILE_NO_EXTENSION));

        LicenseAdapter adapter = new LicenseAdapter(licenses);
        RecyclerView   list    = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapter);

        Licenses.load(licenses);
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
