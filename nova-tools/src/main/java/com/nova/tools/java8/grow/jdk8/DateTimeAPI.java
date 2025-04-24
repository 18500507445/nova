package com.nova.tools.java8.grow.jdk8;

import java.time.*;

/**
 * 新的日期时间 API
 *
 * @author: wzh
 * @date: 2018/2/8
 */
class DateTimeAPI {

    public static void main(String[] args) {
        // Get the system clock as UTC offset
        final Clock clock = Clock.systemUTC();
        System.err.println(clock.instant());
        System.err.println(clock.millis());

        // Get the local date and local time
        final LocalDate date = LocalDate.now();
        final LocalDate dateFromClock = LocalDate.now(clock);

        System.err.println(date);
        System.err.println(dateFromClock);

        // Get the local date and local time
        final LocalTime time = LocalTime.now();
        final LocalTime timeFromClock = LocalTime.now(clock);

        System.err.println(time);
        System.err.println(timeFromClock);

        // Get the local date/time
        final LocalDateTime datetime = LocalDateTime.now();
        final LocalDateTime datetimeFromClock = LocalDateTime.now(clock);

        System.err.println(datetime);
        System.err.println(datetimeFromClock);

        // Get the zoned date/time
        final ZonedDateTime zonedDatetime = ZonedDateTime.now();
        final ZonedDateTime zonedDatetimeFromClock = ZonedDateTime.now(clock);
        final ZonedDateTime zonedDatetimeFromZone = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));

        System.err.println(zonedDatetime);
        System.err.println(zonedDatetimeFromClock);
        System.err.println(zonedDatetimeFromZone);

        // Get duration between two dates
        final LocalDateTime from = LocalDateTime.of(2014, Month.APRIL, 16, 0, 0, 0);
        final LocalDateTime to = LocalDateTime.of(2015, Month.APRIL, 16, 23, 59, 59);

        final Duration duration = Duration.between(from, to);
        System.err.println("Duration in days: " + duration.toDays());
        System.err.println("Duration in hours: " + duration.toHours());


    }

}
