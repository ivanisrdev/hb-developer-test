package com.hotelbeds.supplierintegrations.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeCalculatorUtilsTest {

    @Test
    void sameTimeReturnZero() {
        String timeStamp1 = "Thu, 21 Dec 2000 16:01:00 +0200";
        String timeStamp2 = "Thu, 21 Dec 2000 16:01:00 +0200";

        final Long result = TimeCalculatorUtils.getDifferenceInMinutes(timeStamp1, timeStamp2);

        assertEquals(0, result);
    }

    @Test
    void oneMinuteDifferenceReturnOne() {
        String timeStamp1 = "Thu, 21 Dec 2000 16:01:00 +0200";
        String timeStamp2 = "Thu, 21 Dec 2000 16:02:00 +0200";

        final Long result = TimeCalculatorUtils.getDifferenceInMinutes(timeStamp1, timeStamp2);

        assertEquals(1, result);
    }

    @Test
    void firstTimestampIsOneMinuteLaterThanSecondReturnOne() {
        String timeStamp1 = "Thu, 21 Dec 2000 16:02:00 +0200";
        String timeStamp2 = "Thu, 21 Dec 2000 16:01:00 +0200";

        final Long result = TimeCalculatorUtils.getDifferenceInMinutes(timeStamp1, timeStamp2);
        
        assertEquals(1, result);
    }

    @Test
    void sameMinuteButOneHourTimeZoneDifferenceReturnSixty() {
        String timeStamp1 = "Thu, 21 Dec 2000 16:02:00 +0100";
        String timeStamp2 = "Thu, 21 Dec 2000 16:02:00 +0200";

        final Long result = TimeCalculatorUtils.getDifferenceInMinutes(timeStamp1, timeStamp2);

        assertEquals(60, result);
    }

    @Test
    void fiftyNineSecondsDifferenceReturnZero() {
        String timeStamp1 = "Thu, 21 Dec 2000 16:02:00 +0100";
        String timeStamp2 = "Thu, 21 Dec 2000 16:02:59 +0100";

        final Long result = TimeCalculatorUtils.getDifferenceInMinutes(timeStamp1, timeStamp2);

        assertEquals(0, result);
    }

}