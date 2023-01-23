package com.hotelbeds.supplierintegrations.hackertest.counter;

import com.hotelbeds.supplierintegrations.hackertest.model.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.hotelbeds.supplierintegrations.hackertest.model.LoginActionEnum.SIGNIN_SUCCESS;

@Component
public class AttemptCounterImpl implements AttemptCounter{

    private static Map<String, FailedLoginAttempts> failedLoginAttemptsByIp = new ConcurrentHashMap<>();

    @Value("${hacker-detector.time:5}")
    private int timeInMinutes;

    @Value("${hacker-detector.maxAttempts:5}")
    private int maxAttempts;

    @Override
    public boolean hasTooManyFailedAttempts(Log logFile) {

        if (SIGNIN_SUCCESS.equals(logFile.getLoginAction())) {
            return false;
        }

        synchronized (this) {
            removeOldAttempts(logFile);

            FailedLoginAttempts failedLoginAttempts = getFailedLoginAttemptsForIp(logFile);

            int failedAttemptsCount = failedLoginAttempts.getFailedAttemptsCountWithinTimeWindow(logFile.getLocalDateTime(), timeInMinutes);
            return failedAttemptsCount >= maxAttempts;
        }
    }

    @Override
    public void init() {
        this.failedLoginAttemptsByIp = new ConcurrentHashMap<>();
    }

    @Override
    public int getAttemptCacheSize() {
        return this.failedLoginAttemptsByIp.size();
    }

    private void removeOldAttempts(Log logFile) {

        LocalDateTime lastAttempt = logFile.getLocalDateTime();

        failedLoginAttemptsByIp.entrySet().forEach(attemptByIp -> {
            if (attemptByIp.getValue().firstAttemptIsOlderThan(lastAttempt, timeInMinutes)) {
                failedLoginAttemptsByIp.remove(attemptByIp.getKey());
            }
        });

    }

    private FailedLoginAttempts getFailedLoginAttemptsForIp(Log logFile) {

        FailedLoginAttempts failedLoginAttempts = failedLoginAttemptsByIp.get(logFile.getIpAddress());
        if (failedLoginAttempts == null) {
            failedLoginAttempts = new FailedLoginAttempts(logFile.getLocalDateTime());
            failedLoginAttemptsByIp.put(logFile.getIpAddress(), failedLoginAttempts);
        }

        return failedLoginAttempts;
    }
}
