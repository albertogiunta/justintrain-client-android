package com.jaus.albertogiunta.justintrain_oraritreni.data;

import com.jaus.albertogiunta.justintrain_oraritreni.networking.PostProcessingEnabler;

import org.joda.time.DateTime;

import java.util.List;

import trikita.log.Log;

@SuppressWarnings("unused")
public class Journey implements PostProcessingEnabler.PostProcessable {

    private String journeyDepartureStationId;
    private String journeyArrivalStationId;
    private List<Solution> solutions;

    public String getJourneyDepartureStationId() {
        return journeyDepartureStationId;
    }

    public void setJourneyDepartureStationId(String journeyDepartureStationId) {
        this.journeyDepartureStationId = journeyDepartureStationId;
    }

    public String getJourneyArrivalStationId() {
        return journeyArrivalStationId;
    }

    public void setJourneyArrivalStationId(String journeyArrivalStationId) {
        this.journeyArrivalStationId = journeyArrivalStationId;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    @Override
    public String toString() {
        return "Journey{" +
                "journeyDepartureStationId='" + journeyDepartureStationId + '\'' +
                ", journeyArrivalStationId='" + journeyArrivalStationId + '\'' +
                ", solutions=" + solutions +
                '}';
    }

    @Override
    public void postProcess() {

    }

    public class Solution implements PostProcessingEnabler.PostProcessable {

        private String trainCategory;
        private String trainId;

        private String trainDepartureStationId;
        private String departureStationId;
        private String departureStationName;
        private DateTime departureTime;
        private String departureTimeReadable;
        private String departureTimeWithDelayReadable;
        private String departurePlatform;

        private String arrivalStationId;
        private String arrivalStationName;
        private DateTime arrivalTime;
        private String arrivalTimeWithDelayReadable;
        private String arrivalTimeReadable;

        private Integer timeDifference;
        private Integer progress;

        private String duration;
        private boolean arrivesFirst;
        private boolean hasChanges;
        private List<Change> changesList;

        private Integer trainStatusCode;

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

        public String getDepartureStationId() {
            return departureStationId;
        }

        public void setDepartureStationId(String departureStationId) {
            this.departureStationId = departureStationId;
        }

        public String getDepartureStationName() {
            return departureStationName;
        }

        public void setDepartureStationName(String departureStationName) {
            this.departureStationName = departureStationName;
        }

        public DateTime getDepartureTime() {
            return departureTime;
        }

        public void setDepartureTime(DateTime departureTime) {
            this.departureTime = departureTime;
        }

        public String getDepartureTimeReadable() {
            return departureTimeReadable;
        }

        public void setDepartureTimeReadable(String departureTimeReadable) {
            this.departureTimeReadable = departureTimeReadable;
        }

        public String getDepartureTimeWithDelayReadable() {
            return departureTimeWithDelayReadable;
        }

        public void setDepartureTimeWithDelayReadable(String departureTimeWithDelayReadable) {
            this.departureTimeWithDelayReadable = departureTimeWithDelayReadable;
        }

        public String getDeparturePlatform() {
            return departurePlatform;
        }

        public void setDeparturePlatform(String departurePlatform) {
            this.departurePlatform = departurePlatform;
        }

        public String getArrivalStationName() {
            return arrivalStationName;
        }

        public void setArrivalStationName(String arrivalStationName) {
            this.arrivalStationName = arrivalStationName;
        }

        public String getArrivalStationId() {
            return arrivalStationId;
        }

        public void setArrivalStationId(String arrivalStationId) {
            this.arrivalStationId = arrivalStationId;
        }

        public DateTime getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(DateTime arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

        public String getArrivalTimeReadable() {
            return arrivalTimeReadable;
        }

        public void setArrivalTimeReadable(String arrivalTimeReadable) {
            this.arrivalTimeReadable = arrivalTimeReadable;
        }

        public String getArrivalTimeWithDelayReadable() {
            return arrivalTimeWithDelayReadable;
        }

        public void setArrivalTimeWithDelayReadable(String arrivalTimeWithDelayReadable) {
            this.arrivalTimeWithDelayReadable = arrivalTimeWithDelayReadable;
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

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public boolean isArrivesFirst() {
            return arrivesFirst;
        }

        public void setArrivesFirst(boolean arrivesFirst) {
            this.arrivesFirst = arrivesFirst;
        }

        public boolean hasChanges() {
            return hasChanges;
        }

        public void setHasChanges(boolean hasChanges) {
            this.hasChanges = hasChanges;
        }

        public List<Change> getChangesList() {
            return changesList;
        }

        public void setChangesList(List<Change> changesList) {
            this.changesList = changesList;
        }

        public Integer getTrainStatusCode() {
            return trainStatusCode;
        }

        public void setTrainStatusCode(Integer trainStatusCode) {
            this.trainStatusCode = trainStatusCode;
        }

        private String makeDateTimeReadable(DateTime dt) {
            return dt != null ? dt.toString("HH:mm") : "";
        }

        public void refreshData() {
            if (hasChanges) {
                boolean foundAny = false;
                timeDifference = 0;
                Log.d("refreshData:", changesList.toString());
                for (int i = 0; i < changesList.size(); i++) {
                    if (changesList.get(i).getTimeDifference() != null) {
                        timeDifference += changesList.get(i).getTimeDifference();
                        foundAny = true;
                    }
                }
                if (!foundAny) timeDifference = null;
            }
            postProcess();
        }

        @Override
        public void postProcess() {
            departureTimeReadable = makeDateTimeReadable(departureTime);
            departureTimeWithDelayReadable = makeDateTimeReadable(departureTime.plusMinutes(timeDifference != null ? timeDifference : 0));
            arrivalTimeReadable = makeDateTimeReadable(arrivalTime);
            arrivalTimeWithDelayReadable = makeDateTimeReadable(arrivalTime.plusMinutes(timeDifference != null ? timeDifference : 0));
        }

        @Override
        public String toString() {
            return "Solution{" +
                    "trainCategory='" + trainCategory + '\'' +
                    ", trainId='" + trainId + '\'' +
                    ", trainDepartureStationId='" + trainDepartureStationId + '\'' +
                    ", departureStationName='" + departureStationName + '\'' +
                    ", departureTime=" + departureTime +
                    ", departureTimeReadable='" + departureTimeReadable + '\'' +
                    ", departurePlatform='" + departurePlatform + '\'' +
                    ", arrivalStationName='" + arrivalStationName + '\'' +
                    ", arrivalTime=" + arrivalTime +
                    ", arrivalTimeReadable='" + arrivalTimeReadable + '\'' +
                    ", timeDifference=" + timeDifference +
                    ", progress=" + progress +
                    ", duration='" + duration + '\'' +
                    ", arrivesFirst=" + arrivesFirst +
                    ", hasChanges=" + hasChanges +
                    ", changesList=" + changesList +
                    '}';
        }

        public class Change implements PostProcessingEnabler.PostProcessable {

            private String trainCategory;
            private String trainId;

            private String trainDepartureStationId;
            private String departureStationId;
            private String departureStationName;
            private DateTime departureTime;
            private String departureTimeReadable;
            private String departureTimeWithDelayReadable;
            private String departurePlatform;

            private String arrivalStationId;
            private String arrivalStationName;
            private DateTime arrivalTime;
            private String arrivalTimeReadable;
            private String arrivalTimeWithDelayReadable;

            private Integer timeDifference;
            private Integer progress;

            private String duration;

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

            public String getDepartureStationId() {
                return departureStationId;
            }

            public void setDepartureStationId(String departureStationId) {
                this.departureStationId = departureStationId;
            }

            public String getDepartureStationName() {
                return departureStationName;
            }

            public void setDepartureStationName(String departureStationName) {
                this.departureStationName = departureStationName;
            }

            public DateTime getDepartureTime() {
                return departureTime;
            }

            public void setDepartureTime(DateTime departureTime) {
                this.departureTime = departureTime;
            }

            public String getDepartureTimeReadable() {
                return departureTimeReadable;
            }

            public void setDepartureTimeReadable(String departureTimeReadable) {
                this.departureTimeReadable = departureTimeReadable;
            }

            public String getDepartureTimeWithDelayReadable() {
                return departureTimeWithDelayReadable;
            }

            public void setDepartureTimeWithDelayReadable(String departureTimeWithDelayReadable) {
                this.departureTimeWithDelayReadable = departureTimeWithDelayReadable;
            }

            public String getDeparturePlatform() {
                return departurePlatform;
            }

            public void setDeparturePlatform(String departurePlatform) {
                this.departurePlatform = departurePlatform;
            }

            public String getArrivalStationId() {
                return arrivalStationId;
            }

            public void setArrivalStationId(String arrivalStationId) {
                this.arrivalStationId = arrivalStationId;
            }

            public String getArrivalStationName() {
                return arrivalStationName;
            }

            public void setArrivalStationName(String arrivalStationName) {
                this.arrivalStationName = arrivalStationName;
            }

            public DateTime getArrivalTime() {
                return arrivalTime;
            }

            public void setArrivalTime(DateTime arrivalTime) {
                this.arrivalTime = arrivalTime;
            }

            public String getArrivalTimeReadable() {
                return arrivalTimeReadable;
            }

            public void setArrivalTimeReadable(String arrivalTimeReadable) {
                this.arrivalTimeReadable = arrivalTimeReadable;
            }

            public String getArrivalTimeWithDelayReadable() {
                return arrivalTimeWithDelayReadable;
            }

            public void setArrivalTimeWithDelayReadable(String arrivalTimeWithDelayReadable) {
                this.arrivalTimeWithDelayReadable = arrivalTimeWithDelayReadable;
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

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            private String makeDateTimeReadable(DateTime dt) {
                return dt != null ? dt.toString("HH:mm") : "";
            }

            @Override
            public void postProcess() {
                departureTimeReadable = makeDateTimeReadable(departureTime);
                departureTimeWithDelayReadable = makeDateTimeReadable(departureTime.plusMinutes(timeDifference != null ? timeDifference : 0));
                arrivalTimeReadable = makeDateTimeReadable(arrivalTime);
                arrivalTimeWithDelayReadable = makeDateTimeReadable(arrivalTime.plusMinutes(timeDifference != null ? timeDifference : 0));
            }

            @Override
            public String toString() {
                return "Change{" +
                        "trainCategory='" + trainCategory + '\'' +
                        ", trainId='" + trainId + '\'' +
                        ", trainDepartureStationId='" + trainDepartureStationId + '\'' +
                        ", departureStationName='" + departureStationName + '\'' +
                        ", departureTime=" + departureTime +
                        ", departurePlatform='" + departurePlatform + '\'' +
                        ", arrivalStationName='" + arrivalStationName + '\'' +
                        ", arrivalTime=" + arrivalTime +
                        ", timeDifference=" + timeDifference +
                        ", progress=" + progress +
                        ", duration='" + duration + '\'' +
                        '}';
            }

        }
    }
}


/*

public List<Solution> solutionList = new LinkedList<>();

    @Override
    public String toString() {
        return "Journey{" +
                "solutionList=" + solutionList +
                '}';
    }

    public class Solution {

        public String journeyDepartureStationId;
        public String journeyArrivalStationId;
        public Change solution;
        public boolean hasChanges;
        public ChangesInfo changesList;

        @Override
        public String toString() {
            return "Solution{" +
                    "journeyDepartureStationId='" + journeyDepartureStationId + '\'' +
                    ", journeyArrivalStationId='" + journeyArrivalStationId + '\'' +
                    ", solution=" + solution +
                    ", hasChanges=" + hasChanges +
                    ", changesList=" + changesList +
                    '}';
        }

        public class Change {
            public String trainCategory;
            public String trainId;

            public String trainDepartureStationId;
            public String departureStationName;
            public long departureTime;
            public String departureTimeReadable;
            public String departurePlatform;

            public String arrivalStationName;
            public long arrivalTime;
            public String arrivalTimeReadable;

            public Integer timeDifference;
            public Integer progress;

            public String duration;

            @Override
            public String toString() {
                return "Change{" +
                        "tvTrainCategory='" + trainCategory + '\'' +
                        ", trainId='" + trainId + '\'' +
                        ", trainDepartureStationId='" + trainDepartureStationId + '\'' +
                        ", departureStationName='" + departureStationName + '\'' +
                        ", departureTime=" + departureTime +
                        ", departureTimeReadable='" + departureTimeReadable + '\'' +
                        ", departurePlatform='" + departurePlatform + '\'' +
                        ", arrivalStationName='" + arrivalStationName + '\'' +
                        ", arrivalTime=" + arrivalTime +
                        ", arrivalTimeReadable='" + arrivalTimeReadable + '\'' +
                        ", timeDifference=" + timeDifference +
                        ", duration='" + duration + '\'' +
                        '}';
            }
        }

        public class ChangesInfo {
            public int changesNumber;
            public List<ShortTrainDetails> quickTrainDetails = new ArrayList<>();
            public List<Change> changesList = new ArrayList<>();

            @Override
            public String toString() {
                return "ChangesInfo{" +
                        "changesNumber=" + changesNumber +
                        ", quickTrainDetails=" + quickTrainDetails +
                        ", changesList=" + changesList +
                        '}';
            }
        }

        public class ShortTrainDetails {
            public String trainCategory;
            public String trainId;
            public String trainDepartureStationId;

            @Override
            public String toString() {
                return "ShortTrainDetails{" +
                        "tvTrainCategory='" + trainCategory + '\'' +
                        ", trainId='" + trainId + '\'' +
                        ", trainDepartureStationId='" + trainDepartureStationId + '\'' +
                        '}';
            }
        }
    }

 */