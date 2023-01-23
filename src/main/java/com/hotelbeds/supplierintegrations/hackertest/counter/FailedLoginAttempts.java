package com.hotelbeds.supplierintegrations.hackertest.counter;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FailedLoginAttempts {
    
    private int attempts = 1;
    private LocalDateTime firstAttempt;

    public FailedLoginAttempts(LocalDateTime firstAttempt) {
        this.firstAttempt = firstAttempt;
    }

    public synchronized int getFailedAttemptsCountWithinTimeWindow(LocalDateTime lastAttempt, int timeWindowInMinutes) {

        if (firstAttempt.isBefore(lastAttempt) && firstAttempt.plusMinutes(timeWindowInMinutes).isAfter(lastAttempt)) {
            attempts++;
        } else {
            attempts = 1;
            firstAttempt = lastAttempt;
        }

        return attempts;
    }

    public synchronized boolean firstAttemptIsOlderThan(LocalDateTime timeStamp, int timeWindowInMinutes) {
        return firstAttempt.plusMinutes(timeWindowInMinutes).isBefore(timeStamp);
    }
    
}
