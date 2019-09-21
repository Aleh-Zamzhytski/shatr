package com.epam.shatr.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Location {

    private LocalDateTime dateTime;
    private String coords;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(dateTime, location.dateTime) &&
                Objects.equals(coords, location.coords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, coords);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Location.class.getSimpleName() + "[", "]")
                .add("dateTime=" + dateTime)
                .add("coords='" + coords + "'")
                .toString();
    }
}
