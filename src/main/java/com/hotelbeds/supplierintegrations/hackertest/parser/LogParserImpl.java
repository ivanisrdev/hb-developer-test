package com.hotelbeds.supplierintegrations.hackertest.parser;

import com.hotelbeds.supplierintegrations.hackertest.model.Log;
import com.hotelbeds.supplierintegrations.hackertest.model.LoginActionEnum;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.StringTokenizer;

@Component
public class LogParserImpl implements LogParser {
    @Override
    public Log parse(String line) {
        final StringTokenizer stringTokenizer = validateLine(line);

        final String ipAddress = stringTokenizer.nextToken();
        final String epochInSeconds = stringTokenizer.nextToken();
        final String actionString = stringTokenizer.nextToken();
        final String userName = stringTokenizer.nextToken();

        return new Log(ipAddress, parseEpoch(epochInSeconds), parseLoginActionEnum(actionString), userName);
        
    }

    private StringTokenizer validateLine(String line) {
        if (line == null) {
            throw new LogParserException("Input line is null!");
        }

        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");

        if (stringTokenizer.countTokens() != 4) {
            throw new LogParserException("Erroneous line format: " + line);
        }

        return stringTokenizer;
    }

    private LocalDateTime parseEpoch(String epochInSeconds) {

        try {
            long epochInMillis = Long.parseLong(epochInSeconds) * 1000;
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochInMillis), ZoneId.systemDefault());
        } catch (NumberFormatException e) {
            throw new LogParserException("Invalid epoch format: " + epochInSeconds, e);
        }
    }

    private LoginActionEnum parseLoginActionEnum(String actionString) {

        if (LoginActionEnum.SIGNIN_FAILURE.name().equalsIgnoreCase(actionString)) {
            return LoginActionEnum.SIGNIN_FAILURE;
        } else if (LoginActionEnum.SIGNIN_SUCCESS.name().equalsIgnoreCase(actionString)) {
            return LoginActionEnum.SIGNIN_SUCCESS;
        } else {
            throw new LogParserException("Invalid login action: " + actionString);
        }
    }
    
}
