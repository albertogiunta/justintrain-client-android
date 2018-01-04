package com.jaus.albertogiunta.justintrain_oraritreni.data;

import com.jaus.albertogiunta.justintrain_oraritreni.db.Station;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class PreferredStation {

    private String stationShortId;
    private String stationLongId;
    private String nameShort;
    private String nameLong;

    private Map<String, Journey.Solution> preferredSolutions;

    public PreferredStation() {
        preferredSolutions = new HashMap<>();
    }

    public PreferredStation(Station station) {
        this.stationShortId = station.getStationShortId();
        this.stationLongId = station.getStationLongId();
        this.nameShort = station.getNameShort();
        this.nameLong = station.getNameLong();
        preferredSolutions = new HashMap<>();
    }

    public PreferredStation(String stationShortId, String stationLongId, String nameShort, String nameLong) {
        this.stationShortId = stationShortId;
        this.stationLongId = stationLongId;
        this.nameShort = nameShort;
        this.nameLong = nameLong;
        preferredSolutions = new HashMap<>();
    }

    public String getStationShortId() {
        return stationShortId;
    }

    public void setStationShortId(String stationShortId) {
        this.stationShortId = stationShortId;
    }

    public String getStationLongId() {
        return stationLongId;
    }

    public void setStationLongId(String stationLongId) {
        this.stationLongId = stationLongId;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getNameLong() {
        return nameLong;
    }

    public void setNameLong(String nameLong) {
        this.nameLong = nameLong;
    }

    public Map<String, Journey.Solution> getPreferredSolutions() {
        return preferredSolutions;
    }

    public void addPreferredSolutions(String id, Journey.Solution solution) {
        this.preferredSolutions.put(id, solution);
    }

    public void removePreferredSolutions(String id) {
        this.preferredSolutions.remove(id);
    }

    @Override
    public String toString() {
        return "PreferredStation{" +
                "stationShortId='" + stationShortId + '\'' +
                ", stationLongId='" + stationLongId + '\'' +
                ", nameShort='" + nameShort + '\'' +
                ", nameLong='" + nameLong + '\'' +
                ", preferredSolutions=" + preferredSolutions +
                '}';
    }
}
