package com.epam.shatr.entity;

import org.joda.time.LocalDateTime;

import java.util.Objects;
import java.util.StringJoiner;

public class Visit {

    private Place place;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Visit)) return false;
        Visit visit = (Visit) o;
        return Objects.equals(place, visit.place) &&
                Objects.equals(startTime, visit.startTime) &&
                Objects.equals(endTime, visit.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(place, startTime, endTime);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Visit.class.getSimpleName() + "[", "]")
                .add("place=" + place)
                .add("startTime=" + startTime)
                .add("endTime=" + endTime)
                .toString();
    }
}
