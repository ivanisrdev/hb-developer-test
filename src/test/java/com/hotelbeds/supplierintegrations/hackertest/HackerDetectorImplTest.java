package com.hotelbeds.supplierintegrations.hackertest;

import com.hotelbeds.supplierintegrations.boot.config.AppConfiguration;
import com.hotelbeds.supplierintegrations.hackertest.parser.LogParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfiguration.class)
class HackerDetectorImplTest {

    private static long currentEpoch = 1542110400;

    @Autowired
    private HackerDetector testSubject;

    @BeforeEach
    void setup() {
        ((HackerDetectorImpl) testSubject).init();
    }

    @Test
    void nullLineThrowsException() {

        assertThrows(LogParserException.class, () -> testSubject.parseLine(null));
    }

    @Test
    void emptyLineThrowsException() {

        assertThrows(LogParserException.class, () -> testSubject.parseLine(""));
    }

    @Test
    void wrongLineLengthThrowsException() {

        assertThrows(LogParserException.class, () -> testSubject.parseLine("80.238.9.179,133612947,SIGNIN_FAILURE"));
    }

    @Test
    void invalidEpochFormatThrowsException() {

        assertThrows(LogParserException.class, () -> testSubject.parseLine("80.238.9.179,INVALIDEPOCH,SIGNIN_FAILURE,Will.Smith"));
    }

    @Test
    void invalidActionFormatThrowsException() {

        assertThrows(LogParserException.class, () -> testSubject.parseLine("80.238.9.179,133612947,INVALID_ACTION,Will.Smith"));
    }

    @Test
    void oneFailedLogEntryReturnNull() {
        String result = testSubject.parseLine("80.238.9.179,133612947,SIGNIN_FAILURE,Will.Smith");

        assertNull(result);
    }

    @Test
    void oneSuccessfulLogEntryReturnNull() {
        String result = testSubject.parseLine("80.238.9.179,133612947,SIGNIN_SUCCESS,Will.Smith");

        assertNull(result);
    }

    @Test
    void fourFailedLogEntriesFromSameIpWithinFiveMinutesReturnNull() {
        String result1 = testSubject.parseLine("80.238.9.179,1542110400,SIGNIN_FAILURE,Will.Smith");
        String result2 = testSubject.parseLine("80.238.9.179,1542110460,SIGNIN_FAILURE,Will.Smith");
        String result3 = testSubject.parseLine("80.238.9.179,1542110520,SIGNIN_FAILURE,Will.Smith");
        String result4 = testSubject.parseLine("80.238.9.179,1542110580,SIGNIN_SUCCESS,Will.Smith");
        String result5 = testSubject.parseLine("80.238.9.179,1542110699,SIGNIN_FAILURE,Will.Smith");

        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);
        assertNull(result5);
    }

    @Test
    void fiveFailedLogEntriesFromSameIpWithinFiveMinutesReturnIp() {
        String result1 = testSubject.parseLine("80.238.9.179,1542110400,SIGNIN_FAILURE,Will.Smith");
        String result2 = testSubject.parseLine("80.238.9.179,1542110460,SIGNIN_FAILURE,Will.Smith");
        String result3 = testSubject.parseLine("80.238.9.179,1542110520,SIGNIN_FAILURE,Will.Smith");
        String result4 = testSubject.parseLine("80.238.9.179,1542110580,SIGNIN_FAILURE,Will.Smith");
        String result5 = testSubject.parseLine("80.238.9.179,1542110699,SIGNIN_FAILURE,Will.Smith");

        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);
        assertEquals("80.238.9.179", result5);
    }

    @Test
    void fiveFailedLogEntriesFromSameIpWithinMoreThanFiveMinutesReturnNull() {

        String result1 = testSubject.parseLine("80.238.9.179,1542110400,SIGNIN_FAILURE,Will.Smith");
        String result2 = testSubject.parseLine("80.238.9.179,1542110460,SIGNIN_FAILURE,Will.Smith");
        String result3 = testSubject.parseLine("80.238.9.179,1542110520,SIGNIN_FAILURE,Will.Smith");
        String result4 = testSubject.parseLine("80.238.9.179,1542110580,SIGNIN_FAILURE,Will.Smith");
        String result5 = testSubject.parseLine("80.238.9.179,1542110700,SIGNIN_FAILURE,Will.Smith");

        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);
        assertNull(result5);
    }

    @Test
    void fiveFailedLogEntriesFromDifferentIpsWithinFiveMinutesReturnNull() {

        String result1 = testSubject.parseLine("80.238.9.179,1542110400,SIGNIN_FAILURE,Will.Smith");
        String result2 = testSubject.parseLine("80.238.9.179,1542110460,SIGNIN_FAILURE,Will.Smith");
        String result3 = testSubject.parseLine("80.238.9.179,1542110520,SIGNIN_FAILURE,Will.Smith");
        String result4 = testSubject.parseLine("80.238.9.179,1542110580,SIGNIN_SUCCESS,Will.Smith");
        String result5 = testSubject.parseLine("80.238.9.180,1542110699,SIGNIN_FAILURE,Will.Smith");

        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);
        assertNull(result5);
    }

    @Test
    @Disabled
    void fiveFailedLogEntriesFromSameIpWithinFiveMinutesByTwoThreadsReturnIp() throws InterruptedException {

        final List<String> results = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                results.add(testSubject.parseLine("80.238.9.179," + getNextEpoch() + ",SIGNIN_FAILURE,Will.Smith"));
            }

            countDownLatch.countDown();
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                results.add(testSubject.parseLine("80.238.9.179," + getNextEpoch() + ",SIGNIN_FAILURE,Will.Smith"));
            }

            countDownLatch.countDown();
        });

        t1.start();
        t2.start();

        countDownLatch.await();

        assertEquals("80.238.9.179", results.get(4));

    }

    @Test
    void memoryTestOldEntriesShouldeBeDropped() {
        String result1 = testSubject.parseLine("80.238.9.179,1542110400,SIGNIN_FAILURE,Will.Smith");
        String result2 = testSubject.parseLine("80.238.9.180,1542110701,SIGNIN_FAILURE,Will.Smith");

        assertNull(result1);
        assertNull(result2);
        assertEquals(1, ((HackerDetectorImpl) testSubject).getAttemptCounterChacheSize());
    }

    private static String getNextEpoch() {

        return String.valueOf(currentEpoch++);
    }

}
