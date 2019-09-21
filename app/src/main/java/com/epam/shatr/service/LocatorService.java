package com.epam.shatr.service;

import android.content.Context;

import com.epam.shatr.entity.Coordinates;
import com.epam.shatr.entity.Place;
import com.epam.shatr.entity.Visit;
import com.epam.shatr.repository.PlaceRepository;
import com.epam.shatr.util.CoordinatesUtil;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class LocatorService {

    private PlaceRepository repository;

    public LocatorService(Context context) {
        repository = new PlaceRepository(context);
    }

    public List<Visit> getPlacesByLocationAndTime(Coordinates userCoords, LocalDateTime startDateTime,
                                                  List<Place> visited) {
        List<Place> places = repository.getPlacesByLocation(userCoords, 10);
        places.removeAll(visited);
        return createVisits(userCoords, startDateTime, places);
    }

    private List<Visit> createVisits(Coordinates userCoords, LocalDateTime startDateTime, List<Place> places) {
        List<Visit> toVisit = new ArrayList<>();
        for (Place place : places) {
            Visit visit = new Visit();
            visit.setPlace(place);
            LocalDateTime startVisitTime = getStartVisitTimeByDistance(
                    CoordinatesUtil.getDistance(userCoords, place.getCoords()), startDateTime);
            visit.setStartTime(startVisitTime);
            visit.setEndTime(startVisitTime.plusHours(1));
            if (isValid(visit)) {
                toVisit.add(visit);
            }
        }
        return toVisit;
    }

    private boolean isValid(Visit visit) {
        Place place = visit.getPlace();
        if (place.getStartDate() != null || place.getEndDate() != null) {
            if (visit.getStartTime().isBefore(place.getStartDate()) ||
                    visit.getEndTime().isAfter(place.getEndTime())) {
                return false;
            }
        }
        return visit.getStartTime().isAfter(place.getStartTime()) &&
                visit.getEndTime().isBefore(place.getEndTime());
    }

    public List<Visit> getPlacesByPath(Coordinates from, Coordinates to, LocalDateTime startDateTime,
                                       List<Place> visited) {
        List<Coordinates> sliced = CoordinatesUtil.slice(from, to);
        List<Visit> toVisit = new ArrayList<>();
        for (Coordinates coordinates : sliced) {
            List<Place> places = repository.getPlacesByLocation(coordinates, 10);
            places.removeAll(visited);
            toVisit.addAll(createVisits(from, startDateTime, visited));
        }
        return toVisit;
    }

    private LocalDateTime getStartVisitTimeByDistance(double distance, LocalDateTime currentTime) {
        int minutesInWay = (int) (distance / 80 * 60);
        return currentTime.plusMinutes(Math.max(20, minutesInWay));
    }
}
