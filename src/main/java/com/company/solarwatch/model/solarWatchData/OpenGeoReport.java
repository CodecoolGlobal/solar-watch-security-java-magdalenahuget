package com.company.solarwatch.model.solarWatchData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenGeoReport(String name,
                            @JsonProperty("local_names") Map<String, String> localNames,
                            Double lat,
                            Double lon,
                            String country,
                            String state) {
}
