package com.hotelbeds.supplierintegrations.hackertest.counter;

import com.hotelbeds.supplierintegrations.hackertest.model.Log;

public interface AttemptCounter {
    boolean hasTooManyFailedAttempts(Log logFile);

    void init();

    int getAttemptCacheSize();
}
