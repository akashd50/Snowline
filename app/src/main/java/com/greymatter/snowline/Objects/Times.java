package com.greymatter.snowline.Objects;

public class Times {
    private String scheduledArrival, estimatedArrival, scheduledDeparture, estimatedDeparture;
    public Times(){}

    public String getScheduledArrival() {
        return scheduledArrival;
    }

    public Times setScheduledArrival(String scheduledArrival) {
        this.scheduledArrival = scheduledArrival;
        return this;
    }

    public String getEstimatedArrival() {
        return estimatedArrival;
    }

    public Times setEstimatedArrival(String estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
        return this;
    }

    public String getScheduledDeparture() {
        return scheduledDeparture;
    }

    public Times setScheduledDeparture(String scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
        return this;
    }

    public String getEstimatedDeparture() {
        return estimatedDeparture;
    }

    public Times setEstimatedDeparture(String estimatedDeparture) {
        this.estimatedDeparture = estimatedDeparture;
        return this;
    }
}
