package com.company.solarwatch.model.solarWatchData;

import com.company.solarwatch.model.solarWatchData.OpenSolarData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenSolarWatchReport(
        OpenSolarData results) {
}
