package com.hotelbeds.supplierintegrations.hackertest.parser;

public class LogParserException extends RuntimeException {
    LogParserException(String message) {
        super(message);
    }
    LogParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
