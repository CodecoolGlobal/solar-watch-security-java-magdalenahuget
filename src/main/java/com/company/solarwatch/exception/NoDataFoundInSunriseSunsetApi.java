package com.company.solarwatch.exception;

public class NoDataFoundInSunriseSunsetApi extends RuntimeException {
    public NoDataFoundInSunriseSunsetApi() {
        super("No data found in Open Geo API.");
    }
}
