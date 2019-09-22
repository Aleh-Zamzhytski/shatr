package com.epam.shatr.entity;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Objects;
import java.util.StringJoiner;

public class Place {

    private String name;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private Coordinates coords;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Coordinates getCoords() {
        return coords;
    }

    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;
        Place place = (Place) o;
        return Objects.equals(name, place.name) &&
                Objects.equals(description, place.description) &&
                Objects.equals(startTime, place.startTime) &&
                Objects.equals(endTime, place.endTime) &&
                Objects.equals(startDate, place.startDate) &&
                Objects.equals(endDate, place.endDate) &&
                Objects.equals(coords, place.coords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, startTime, endTime, startDate, endDate, coords);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Place.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("startTime=" + startTime)
                .add("endTime=" + endTime)
                .add("startDate=" + startDate)
                .add("endDate=" + endDate)
                .add("coords='" + coords + "'")
                .toString();
    }
}
