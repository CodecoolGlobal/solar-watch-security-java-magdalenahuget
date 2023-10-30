package com.company.solarwatchdatabase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenSolarWatchReport(
        OpenSolarData results) {
}
