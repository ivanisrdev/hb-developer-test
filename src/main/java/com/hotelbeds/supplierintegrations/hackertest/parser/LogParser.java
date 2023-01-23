package com.hotelbeds.supplierintegrations.hackertest.parser;

import com.hotelbeds.supplierintegrations.hackertest.model.Log;

public interface LogParser {
    Log parse(String line);
}
