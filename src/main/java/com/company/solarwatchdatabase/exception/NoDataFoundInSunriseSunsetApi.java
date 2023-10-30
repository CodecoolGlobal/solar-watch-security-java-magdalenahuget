package com.company.solarwatchdatabase.exception;

public class NoDataFoundInSunriseSunsetApi extends RuntimeException {
    public NoDataFoundInSunriseSunsetApi() {
        super("No data found in Open Geo API.");
    }
}
