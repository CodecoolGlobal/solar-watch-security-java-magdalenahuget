package com.company.solarwatch.service;

import com.company.solarwatch.model.dto.SunriseSunsetResponseDto;
import com.company.solarwatch.model.solarWatchData.SunriseSunset;
import org.springframework.stereotype.Component;

@Component
public class SunriseSunsetMapper {

    private CityMapper cityMapper;

    public SunriseSunsetMapper(CityMapper cityMapper) {
        this.cityMapper = cityMapper;
    }

    SunriseSunsetResponseDto mapToSunriseSunsetResponseDto(SunriseSunset sunriseSunset) {
        return SunriseSunsetResponseDto.builder()
                .city(cityMapper.mapCityToCityResponseDto(sunriseSunset.getCity()))
                .date(sunriseSunset.getDate())
                .sunriseTime(sunriseSunset.getSunriseTime())
                .sunsetTime(sunriseSunset.getSunsetTime())
                .build();
    }
}
