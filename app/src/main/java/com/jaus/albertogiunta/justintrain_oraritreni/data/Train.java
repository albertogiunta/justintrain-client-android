package com.jaus.albertogiunta.justintrain_oraritreni.data;

import org.joda.time.DateTime;

import java.util.List;

public class Train {

    private String trainCategory;
    private String trainId;

    private String trainDepartureStationId;
    private String trainDepartureStationName;

    private String trainArrivalStationId;
    private String trainArrivalStationName;

    private Integer timeDifference;
    private Integer progress;

    private String lastSeenStationName;
    private String lastSeenTimeReadable;

    private List<Stop> stops;
    private String cancelledStopsInfo;

    private Integer firstClassOrientationCode;
    private Integer trainStatusCode;
    private Boolean isDeparted;
    private Boolean isArrivedToDestination;

    public Train(Train t) {
        this.trainCategory = t.getTrainCategory();
        this.trainId = t.getTrainId();
        this.trainDepartureStationId = t.getTrainDepartureStationId();
        this.trainDepartureStationName = t.getTrainDepartureStationName();
        this.trainArrivalStationId = t.getTrainArrivalStationId();
        this.trainArrivalStationName = t.getTrainArrivalStationName();
        this.timeDifference = t.getTimeDifference();
        this.progress = t.getProgress();
        this.lastSeenStationName = t.getLastSeenStationName();
        this.lastSeenTimeReadable = t.getLastSeenTimeReadable();
        this.stops = t.getStops();
        this.cancelledStopsInfo = t.getCancelledStopsInfo();
        this.firstClassOrientationCode = t.getFirstClassOrientationCode();
        this.trainStatusCode = t.getTrainStatusCode();
        this.isDeparted = t.isDeparted();
        this.isArrivedToDestination = t.isArrivedToDestination();
    }

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

    public String getTrainDepartureStationName() {
        return trainDepartureStationName;
    }

    public void setTrainDepartureStationName(String trainDepartureStationName) {
        this.trainDepartureStationName = trainDepartureStationName;
    }

    public String getTrainArrivalStationId() {
        return trainArrivalStationId;
    }

    public void setTrainArrivalStationId(String trainArrivalStationId) {
        this.trainArrivalStationId = trainArrivalStationId;
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

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
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

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public String getCancelledStopsInfo() {
        return cancelledStopsInfo;
    }

    public void setCancelledStopsInfo(String cancelledStopsInfo) {
        this.cancelledStopsInfo = cancelledStopsInfo;
    }

    public Integer getFirstClassOrientationCode() {
        return firstClassOrientationCode;
    }

    public void setFirstClassOrientationCode(Integer firstClassOrientationCode) {
        this.firstClassOrientationCode = firstClassOrientationCode;
    }

    public Integer getTrainStatusCode() {
        return trainStatusCode;
    }

    public void setTrainStatusCode(Integer trainStatusCode) {
        this.trainStatusCode = trainStatusCode;
    }

    public Boolean isDeparted() {
        return isDeparted;
    }

    public void setDeparted(Boolean departed) {
        isDeparted = departed;
    }

    public Boolean isArrivedToDestination() {
        return isArrivedToDestination;
    }

    public void setArrivedToDestination(Boolean arrivedToDestination) {
        isArrivedToDestination = arrivedToDestination;
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainCategory='" + trainCategory + '\'' +
                ", trainId='" + trainId + '\'' +
                ", trainDepartureStationId='" + trainDepartureStationId + '\'' +
                ", trainDepartureStationName='" + trainDepartureStationName + '\'' +
                ", trainArrivalStationId='" + trainArrivalStationId + '\'' +
                ", trainArrivalStationName='" + trainArrivalStationName + '\'' +
                ", timeDifference=" + timeDifference +
                ", progress=" + progress +
                ", lastSeenStationName='" + lastSeenStationName + '\'' +
                ", lastSeenTimeReadable='" + lastSeenTimeReadable + '\'' +
                ", stops=" + stops +
                ", cancelledStopsInfo='" + cancelledStopsInfo + '\'' +
                ", firstClassOrientationCode=" + firstClassOrientationCode +
                ", trainStatusCode=" + trainStatusCode +
                ", isDeparted=" + isDeparted +
                ", isArrivedToDestination=" + isArrivedToDestination +
                '}';
    }

    public class Stop {

        private String stationId;
        private String stationName;

        private Integer currentStopStatusCode;
        private Integer currentStopTypeCode;
        private Integer currentAndNextStopStatusCode;

        private DateTime plannedDepartureTime;
        private DateTime actualDepartureTime;
        private String departurePlatform;

        private Integer timeDifference;

        private Boolean isVisited;

        public String getStationId() {
            return stationId;
        }

        public void setStationId(String stationId) {
            this.stationId = stationId;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public Integer getCurrentStopStatusCode() {
            return currentStopStatusCode;
        }

        public void setCurrentStopStatusCode(Integer currentStopStatusCode) {
            this.currentStopStatusCode = currentStopStatusCode;
        }

        public Integer getCurrentStopTypeCode() {
            return currentStopTypeCode;
        }

        public void setCurrentStopTypeCode(Integer currentStopTypeCode) {
            this.currentStopTypeCode = currentStopTypeCode;
        }

        public Integer getCurrentAndNextStopStatusCode() {
            return currentAndNextStopStatusCode;
        }

        public void setCurrentAndNextStopStatusCode(Integer currentAndNextStopStatusCode) {
            this.currentAndNextStopStatusCode = currentAndNextStopStatusCode;
        }

        public DateTime getPlannedDepartureTime() {
            return plannedDepartureTime;
        }

        public void setPlannedDepartureTime(DateTime plannedDepartureTime) {
            this.plannedDepartureTime = plannedDepartureTime;
        }

        public DateTime getActualDepartureTime() {
            return actualDepartureTime;
        }

        public void setActualDepartureTime(DateTime actualDepartureTime) {
            this.actualDepartureTime = actualDepartureTime;
        }

        public String getDeparturePlatform() {
            return departurePlatform;
        }

        public void setDeparturePlatform(String departurePlatform) {
            this.departurePlatform = departurePlatform;
        }

        public Integer getTimeDifference() {
            return timeDifference;
        }

        public void setTimeDifference(Integer timeDifference) {
            this.timeDifference = timeDifference;
        }

        public Boolean isVisited() {
            return isVisited;
        }

        public void setVisited(Boolean visited) {
            isVisited = visited;
        }

        @Override
        public String toString() {
            return "Stop{" +
                    "stationId='" + stationId + '\'' +
                    ", stationName='" + stationName + '\'' +
                    ", currentStopStatusCode=" + currentStopStatusCode +
                    ", currentStopTypeCode=" + currentStopTypeCode +
                    ", currentAndNextStopStatusCode=" + currentAndNextStopStatusCode +
                    ", plannedDepartureTime=" + plannedDepartureTime +
                    ", actualDepartureTime=" + actualDepartureTime +
                    ", departurePlatform='" + departurePlatform + '\'' +
                    ", timeDifference=" + timeDifference +
                    ", isVisited=" + isVisited +
                    '}';
        }
    }
}
