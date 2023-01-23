package com.hotelbeds.supplierintegrations.hackertest;

import com.hotelbeds.supplierintegrations.hackertest.counter.AttemptCounter;
import com.hotelbeds.supplierintegrations.hackertest.model.Log;
import com.hotelbeds.supplierintegrations.hackertest.parser.LogParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HackerDetectorImpl implements HackerDetector {

    private final LogParser logParser;

    private final AttemptCounter attemptCounter;

    public HackerDetectorImpl(LogParser logParser, AttemptCounter attemptCounter) {
        this.logParser = logParser;
        this.attemptCounter = attemptCounter;
    }
    
    public String parseLine(final String line) {
        Log logFile = logParser.parse(line);

        if (attemptCounter.hasTooManyFailedAttempts(logFile)) {
            return logFile.getIpAddress();
        }
        return null;
    }


    void init() {
        attemptCounter.init();
    }

    int getAttemptCounterChacheSize() {
        return attemptCounter.getAttemptCacheSize();
    }
}
