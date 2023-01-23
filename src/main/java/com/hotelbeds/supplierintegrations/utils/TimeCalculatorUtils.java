package com.hotelbeds.supplierintegrations.utils;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeCalculatorUtils {

    private static String TIMESTAMP_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";

    public static long getDifferenceInMinutes(String timeStampString1, String timeStampString2) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);

        ZonedDateTime timeStamp1 = ZonedDateTime.parse(timeStampString1, formatter);
        ZonedDateTime timeStamp2 = ZonedDateTime.parse(timeStampString2, formatter);

        return Math.abs(Duration.between(timeStamp1, timeStamp2).toMinutes());
    }
}
