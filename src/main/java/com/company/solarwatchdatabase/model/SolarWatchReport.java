package com.company.solarwatchdatabase.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record SolarWatchReport(
        String city,
        LocalDate date,
        LocalTime sunrise,
        LocalTime sunset) {
}
