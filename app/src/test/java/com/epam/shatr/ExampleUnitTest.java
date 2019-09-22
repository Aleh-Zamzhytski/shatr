package com.epam.shatr;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        LocalDateTime localDateTime =
                LocalDateTime.parse("21.09.2019 08:00", DateTimeFormat.forPattern("dd.MM.yyyy HH:mm"));

        LocalTime time = LocalTime.parse("09:30", DateTimeFormat.forPattern("HH:mm"));
        assertTrue(time.toDateTimeToday().toLocalDateTime().isAfter(localDateTime));
    }
}