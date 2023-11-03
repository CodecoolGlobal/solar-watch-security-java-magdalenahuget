package com.company.solarwatch.model.dto;

import com.company.solarwatch.model.solarWatchData.City;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class SunriseSunsetRequestDto {
    private CityResponseDto city;
    private LocalDate date;
    private LocalTime sunriseTime;
    private LocalTime sunsetTime;
}
