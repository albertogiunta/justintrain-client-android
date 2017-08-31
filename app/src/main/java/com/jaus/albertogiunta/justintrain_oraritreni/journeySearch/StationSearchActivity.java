package com.jaus.albertogiunta.justintrain_oraritreni.journeySearch;

import com.google.firebase.crash.FirebaseCrash;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.DatabaseHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationSearchActivity extends AppCompatActivity implements SearchedStationsAdapter.OnClickListener {

    @BindView(R.id.rv_searched_stations) RecyclerView rvSearchedStations;
    SearchedStationsAdapter adapter;
    List<String> stationNames = new LinkedList<>(DatabaseHelper.getAll());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_search);
        ButterKnife.bind(this);

        adapter = new SearchedStationsAdapter(stationNames, this);
        rvSearchedStations.setAdapter(adapter);
        rvSearchedStations.setHasFixedSize(true);
        rvSearchedStations.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Cerca stazione...");
        searchItem.expandActionView();
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                stationNames.clear();
                stationNames.addAll(DatabaseHelper.getElementByNameFancy(newText));
                if (stationNames.size() == 0) {
                    stationNames.addAll(DatabaseHelper.getElementByNameFancy(newText.trim()));
                }
                try {
                    adapter.notifyDataSetChanged(); //TODO possibile bug http://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in
                } catch (Exception e) {
                    FirebaseCrash.report(new Exception("CATCHED Inconsistency detected. Invalid view holder adapter positionViewHolder"));
                }
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return false;
            }
        });
        return true;
    }

    @Override
    public void onItemSelected(String stationName) {
        Intent output = new Intent();
        output.putExtra("stationName", stationName);
        setResult(RESULT_OK, output);
        finish();
    }
}
