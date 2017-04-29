package com.jaus.albertogiunta.justintrain_oraritreni.journeySearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaus.albertogiunta.justintrain_oraritreni.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class SearchedStationsAdapter extends RecyclerView.Adapter<SearchedStationsAdapter.ViewHolder> {

    private List<String> stationNames;
    private OnClickListener listener;

    SearchedStationsAdapter(List<String> stationNames, OnClickListener listener) {
        this.stationNames = stationNames;
        this.listener = listener;
    }

    @Override
    public SearchedStationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_autucomplete_station, parent ,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchedStationsAdapter.ViewHolder holder, int position) {
        holder.bind(stationNames.get(position));
        holder.tvStationName.setOnClickListener(view -> listener.onItemSelected(stationNames.get(position)));
    }

    @Override
    public int getItemCount() {
        return stationNames.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_station_name) TextView tvStationName;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String stationName) {
            tvStationName.setText(stationName);
        }
    }

    interface OnClickListener {
        void onItemSelected(String stationName);
    }
}
