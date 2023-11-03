package com.company.solarwatch.service;

import com.company.solarwatch.model.dto.CityRequestDto;
import com.company.solarwatch.model.dto.CityResponseDto;
import com.company.solarwatch.model.solarWatchData.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    City mapCityResponseDtoToCity(CityResponseDto city) {
        return City.builder()
                .name(city.getName())
                .longitude(city.getLongitude())
                .latitude(city.getLatitude())
                .state(city.getState())
                .country(city.getCountry())
                .build();
    }

    CityRequestDto mapCityToCityRequestDto(City city) {
        return CityRequestDto.builder()
                .name(city.getName())
                .longitude(city.getLongitude())
                .latitude(city.getLatitude())
                .state(city.getState())
                .country(city.getCountry())
                .build();
    }

    CityResponseDto mapCityToCityCityResponseDto(City city) {
        return CityResponseDto.builder()
                .name(city.getName())
                .longitude(city.getLongitude())
                .latitude(city.getLatitude())
                .state(city.getState())
                .country(city.getCountry())
                .build();
    }

    City mapCityRequestDtoToCity(CityRequestDto cityRequestDto) {
        return City.builder()
                .name(cityRequestDto.getName())
                .longitude(cityRequestDto.getLongitude())
                .latitude(cityRequestDto.getLatitude())
                .state(cityRequestDto.getState())
                .country(cityRequestDto.getCountry())
                .build();
    }
}
