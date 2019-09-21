package com.epam.shatr.util;

import com.epam.shatr.entity.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CoordinatesUtil {

    private static final double EARTH_RADIUS_KM = 6371;

    private CoordinatesUtil() {
    }

    public static double getDistance(Coordinates coords1, Coordinates coords2) {
        double dLat = degreesToRadians(coords2.getLatitude() - coords1.getLatitude());
        double dLon = degreesToRadians(coords2.getLongitude() - coords1.getLongitude());

        double lat1 = degreesToRadians(coords1.getLatitude());
        double lat2 = degreesToRadians(coords2.getLatitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    private static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    public static List<Coordinates> slice(Coordinates from, Coordinates to) {
        double distance = getDistance(from, to);
        int sliceCount = (int) (distance / 10);
        if (sliceCount <= 1) {
            return new ArrayList<>(Arrays.asList(from, to));
        }
        double latSlice = (from.getLatitude() - to.getLatitude()) / sliceCount;
        double longSlice = (from.getLongitude() - to.getLongitude()) / sliceCount;
        List<Coordinates> coordinates = new ArrayList<>();
        for (int i = 0; i < sliceCount; i++) {
            coordinates.add(new Coordinates(from.getLatitude() + (sliceCount + 1) * latSlice,
                    from.getLongitude() + (sliceCount + 1) * longSlice));
        }
        return coordinates;
    }
}
