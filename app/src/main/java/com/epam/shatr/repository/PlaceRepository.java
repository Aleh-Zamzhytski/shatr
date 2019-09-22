package com.epam.shatr.repository;

import android.content.Context;

import com.epam.shatr.R;
import com.epam.shatr.entity.Coordinates;
import com.epam.shatr.entity.Place;
import com.epam.shatr.util.CoordinatesUtil;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class PlaceRepository {

    private static final List<Place> PLACES = new ArrayList<>();

    public PlaceRepository(Context context) {
        try (Reader in = new InputStreamReader(context.getResources().openRawResource(R.raw.data))) {
            Iterable<CSVRecord> records = CSVFormat.EXCEL
                    .withDelimiter(';')
                    .withFirstRecordAsHeader()
                    .parse(in);
            for (CSVRecord record : records) {
                PLACES.add(parse(record));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Place> getPlacesByLocation(final Coordinates userCoords, final double radius) {
        List<Place> places = new ArrayList<>();
        for (Place place: PLACES){
            if(CoordinatesUtil.getDistance(place.getCoords(), userCoords) < radius){
                places.add(place);
            }
        }
        return places;
    }

    private Place parse(CSVRecord record) {
        Place place = new Place();
        place.setName(record.get("name"));
        place.setDescription(record.get("description"));
        place.setStartTime(parseTime(record.get("startTime")));
        place.setEndTime(parseTime(record.get("endTime")));
        place.setStartDate(parseDate(record.get("startDate")));
        place.setEndDate(parseDate(record.get("endDate")));
        place.setCoords(parseCoords(record.get("coords")));
        return place;
    }

    private LocalTime parseTime(String time) {
        return LocalTime.parse(time, DateTimeFormat.forPattern("HH.mm"));
    }

    private LocalDate parseDate(String date) {
        if (date != null && !date.isEmpty()) {
            return LocalDate.parse(date, DateTimeFormat.forPattern("dd.MM.yyyy"));
        }
        return null;
    }

    private Coordinates parseCoords(String coordinates) {
        String[] strs = coordinates.split(",");
        return new Coordinates(Double.parseDouble(strs[0]), Double.parseDouble(strs[1]));
    }
}
