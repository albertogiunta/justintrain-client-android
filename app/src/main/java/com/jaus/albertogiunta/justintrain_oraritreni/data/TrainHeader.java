package com.jaus.albertogiunta.justintrain_oraritreni.data;

import org.joda.time.DateTime;

@SuppressWarnings("unused")
public class TrainHeader {

    private String trainCategory;
    private String trainId;

    private String trainDepartureStationId;
    private String trainDeparturePlatform;
    private String trainDepartureStationName;

    private String arrivalStationId;
    private String trainArrivalStationName;

    private Integer timeDifference;
    private int progress;

    private String lastSeenStationName;
    private String lastSeenTimeReadable; // in HH:mm

    private String cancelledStopsInfo;

    private int firstClassOrientationCode;
    private int trainStatusCode;
    private boolean isDeparted;
    private boolean isArrivedToDestination;

    private String journeyDepartureStationId;
    private String journeyDepartureStationName;
    private DateTime journeyDepartureTime;
    private String journeyArrivalStationId;
    private String journeyArrivalStationName;
    private DateTime journeyArrivalTime;
    private String departurePlatform;
    private Boolean isJourneyDepartureStationVisited;
    private Boolean isJourneyArrivalStationVisited;
    private int ETAToNextJourneyStation;

    public String getTrainCategory() {
        return trainCategory;
    }

    public void setTrainCategory(String trainCategory) {
        this.trainCategory = trainCategory;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainDepartureStationId() {
        return trainDepartureStationId;
    }

    public void setTrainDepartureStationId(String trainDepartureStationId) {
        this.trainDepartureStationId = trainDepartureStationId;
    }

    public String getTrainDeparturePlatform() {
        return trainDeparturePlatform;
    }

    public void setTrainDeparturePlatform(String trainDeparturePlatform) {
        this.trainDeparturePlatform = trainDeparturePlatform;
    }

    public String getTrainDepartureStationName() {
        return trainDepartureStationName;
    }

    public void setTrainDepartureStationName(String trainDepartureStationName) {
        this.trainDepartureStationName = trainDepartureStationName;
    }

    public String getArrivalStationId() {
        return arrivalStationId;
    }

    public void setArrivalStationId(String arrivalStationId) {
        this.arrivalStationId = arrivalStationId;
    }

    public String getTrainArrivalStationName() {
        return trainArrivalStationName;
    }

    public void setTrainArrivalStationName(String trainArrivalStationName) {
        this.trainArrivalStationName = trainArrivalStationName;
    }

    public Integer getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(Integer timeDifference) {
        this.timeDifference = timeDifference;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getLastSeenStationName() {
        return lastSeenStationName;
    }

    public void setLastSeenStationName(String lastSeenStationName) {
        this.lastSeenStationName = lastSeenStationName;
    }

    public String getLastSeenTimeReadable() {
        return lastSeenTimeReadable;
    }

    public void setLastSeenTimeReadable(String lastSeenTimeReadable) {
        this.lastSeenTimeReadable = lastSeenTimeReadable;
    }

    public String getCancelledStopsInfo() {
        return cancelledStopsInfo;
    }

    public void setCancelledStopsInfo(String cancelledStopsInfo) {
        this.cancelledStopsInfo = cancelledStopsInfo;
    }

    public int getFirstClassOrientationCode() {
        return firstClassOrientationCode;
    }

    public void setFirstClassOrientationCode(int firstClassOrientationCode) {
        this.firstClassOrientationCode = firstClassOrientationCode;
    }

    public int getTrainStatusCode() {
        return trainStatusCode;
    }

    public void setTrainStatusCode(int trainStatusCode) {
        this.trainStatusCode = trainStatusCode;
    }

    public boolean isDeparted() {
        return isDeparted;
    }

    public void setDeparted(boolean departed) {
        isDeparted = departed;
    }

    public boolean isArrivedToDestination() {
        return isArrivedToDestination;
    }

    public void setArrivedToDestination(boolean arrivedToDestination) {
        isArrivedToDestination = arrivedToDestination;
    }

    public String getJourneyDepartureStationId() {
        return journeyDepartureStationId;
    }

    public void setJourneyDepartureStationId(String journeyDepartureStationId) {
        this.journeyDepartureStationId = journeyDepartureStationId;
    }

    public String getJourneyDepartureStationName() {
        return journeyDepartureStationName;
    }

    public void setJourneyDepartureStationName(String journeyDepartureStationName) {
        this.journeyDepartureStationName = journeyDepartureStationName;
    }

    public DateTime getJourneyDepartureTime() {
        return journeyDepartureTime;
    }

    public void setJourneyDepartureTime(DateTime journeyDepartureTime) {
        this.journeyDepartureTime = journeyDepartureTime;
    }

    public String getJourneyArrivalStationId() {
        return journeyArrivalStationId;
    }

    public void setJourneyArrivalStationId(String journeyArrivalStationId) {
        this.journeyArrivalStationId = journeyArrivalStationId;
    }

    public String getJourneyArrivalStationName() {
        return journeyArrivalStationName;
    }

    public void setJourneyArrivalStationName(String journeyArrivalStationName) {
        this.journeyArrivalStationName = journeyArrivalStationName;
    }

    public DateTime getJourneyArrivalTime() {
        return journeyArrivalTime;
    }

    public void setJourneyArrivalTime(DateTime journeyArrivalTime) {
        this.journeyArrivalTime = journeyArrivalTime;
    }

    public String getDeparturePlatform() {
        return departurePlatform;
    }

    public void setDeparturePlatform(String departurePlatform) {
        this.departurePlatform = departurePlatform;
    }

    public Boolean getJourneyDepartureStationVisited() {
        return isJourneyDepartureStationVisited;
    }

    public void setJourneyDepartureStationVisited(Boolean journeyDepartureStationVisited) {
        isJourneyDepartureStationVisited = journeyDepartureStationVisited;
    }

    public Boolean getJourneyArrivalStationVisited() {
        return isJourneyArrivalStationVisited;
    }

    public void setJourneyArrivalStationVisited(Boolean journeyArrivalStationVisited) {
        isJourneyArrivalStationVisited = journeyArrivalStationVisited;
    }

    public int getETAToNextJourneyStation() {
        return ETAToNextJourneyStation;
    }

    public void setETAToNextJourneyStation(int ETAToNextJourneyStation) {
        this.ETAToNextJourneyStation = ETAToNextJourneyStation;
    }

    @Override
    public String toString() {
        return "TrainHeader{" +
                "trainCategory='" + trainCategory + '\'' +
                ", trainId='" + trainId + '\'' +
                ", trainDepartureStationId='" + trainDepartureStationId + '\'' +
                ", trainDeparturePlatform='" + trainDeparturePlatform + '\'' +
                ", trainDepartureStationName='" + trainDepartureStationName + '\'' +
                ", arrivalStationId='" + arrivalStationId + '\'' +
                ", trainArrivalStationName='" + trainArrivalStationName + '\'' +
                ", timeDifference=" + timeDifference +
                ", progress=" + progress +
                ", lastSeenStationName='" + lastSeenStationName + '\'' +
                ", lastSeenTimeReadable='" + lastSeenTimeReadable + '\'' +
                ", cancelledStopsInfo='" + cancelledStopsInfo + '\'' +
                ", firstClassOrientationCode=" + firstClassOrientationCode +
                ", trainStatusCode=" + trainStatusCode +
                ", isDeparted=" + isDeparted +
                ", isArrivedToDestination=" + isArrivedToDestination +
                ", journeyDepartureStationId='" + journeyDepartureStationId + '\'' +
                ", journeyDepartureStationName='" + journeyDepartureStationName + '\'' +
                ", journeyDepartureTime=" + journeyDepartureTime +
                ", journeyArrivalStationId='" + journeyArrivalStationId + '\'' +
                ", journeyArrivalStationName='" + journeyArrivalStationName + '\'' +
                ", journeyArrivalTime=" + journeyArrivalTime +
                ", departurePlatform='" + departurePlatform + '\'' +
                ", isJourneyDepartureStationVisited=" + isJourneyDepartureStationVisited +
                ", isJourneyArrivalStationVisited=" + isJourneyArrivalStationVisited +
                ", ETAToNextJourneyStation=" + ETAToNextJourneyStation +
                '}';
    }
}
