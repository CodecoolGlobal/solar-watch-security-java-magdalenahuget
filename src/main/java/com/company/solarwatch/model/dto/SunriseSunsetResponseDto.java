package com.company.solarwatch.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class SunriseSunsetResponseDto {
    private CityResponseDto city;
    private LocalDate date;
    private LocalTime sunriseTime;
    private LocalTime sunsetTime;
}
