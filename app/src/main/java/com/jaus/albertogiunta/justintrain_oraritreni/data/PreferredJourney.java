package com.jaus.albertogiunta.justintrain_oraritreni.data;

import org.joda.time.DateTime;

@SuppressWarnings("unused")
public class PreferredJourney {

    private PreferredStation station1;
    private PreferredStation station2;
    private long             timestamp;
    private int              priority;
    private String           category;
    private String           version;

    public PreferredJourney(PreferredStation station1, PreferredStation station2) {
        this.station1 = station1;
        this.station2 = station2;
        timestamp = DateTime.now().toInstant().getMillis();
        priority = -1;
        category = "";
        version = "";
    }

    public PreferredJourney swapStations() {
        PreferredStation temp = station1;
        station1 = station2;
        station2 = temp;
        return this;
    }

    public PreferredStation getStation1() {
        return station1;
    }

    public void setStation1(PreferredStation station1) {
        this.station1 = station1;
    }

    public PreferredStation getStation2() {
        return station2;
    }

    public void setStation2(PreferredStation station2) {
        this.station2 = station2;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "PreferredJourney{" +
                "station1=" + station1 +
                ", station2=" + station2 +
                ", timestamp=" + timestamp +
                ", priority=" + priority +
                ", category='" + category + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return timestamp == ((PreferredJourney) o).timestamp;
    }
}
