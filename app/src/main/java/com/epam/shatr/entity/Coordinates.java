package com.epam.shatr.entity;

import java.util.Objects;
import java.util.StringJoiner;

public class Coordinates {

    private double latitude;
    private double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getStringRepresentation() {
        return latitude + "," + longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Coordinates.class.getSimpleName() + "[", "]")
                .add("latitude=" + latitude)
                .add("longitude=" + longitude)
                .toString();
    }
}
