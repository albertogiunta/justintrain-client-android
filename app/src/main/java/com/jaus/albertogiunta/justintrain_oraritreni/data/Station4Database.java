package com.jaus.albertogiunta.justintrain_oraritreni.data;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Station4Database extends RealmObject {

    @Required
    private String stationShortId;
    @Required
    private String stationLongId;
    @Required
    private String nameLong;
    @Required
    private String nameShort;


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

    @Override
    public String toString() {
        return "Station4Database{" +
                "stationShortId='" + stationShortId + '\'' +
                ", stationLongId='" + stationLongId + '\'' +
                ", nameLong='" + nameLong + '\'' +
                ", nameShort='" + nameShort + '\'' +
                '}';
    }
}
