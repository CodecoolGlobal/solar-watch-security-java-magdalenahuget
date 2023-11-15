package com.company.solarwatch.service;

import com.company.solarwatch.exception.CityNotFoundException;
import com.company.solarwatch.model.dto.CityRequestDto;
import com.company.solarwatch.model.dto.CityResponseDto;
import com.company.solarwatch.model.solarWatchData.City;
import com.company.solarwatch.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CityServiceAdmin {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Autowired
    public CityServiceAdmin(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    public CityResponseDto createCity(CityRequestDto cityRequestDto) {
        City city = new City();
        city.setName(cityRequestDto.getName());
        city.setLongitude(cityRequestDto.getLongitude());
        city.setLatitude(cityRequestDto.getLatitude());
        city.setState(cityRequestDto.getState());
        city.setCountry(cityRequestDto.getCountry());
        City savedCity = cityRepository.save(city);
        return cityMapper.mapCityToCityResponseDto(savedCity);
    }

    public CityResponseDto getCityById(Long cityId) {
        City city = cityRepository
                .findById(cityId)
                .orElseThrow(() -> {
                    log.error("There is no city with the given id: {}", cityId);
                    return new RuntimeException(
                            "City not found with id: {}" + cityId);
                });
        return cityMapper.mapCityToCityResponseDto(city);
    }

    public CityResponseDto updateCityById(Long cityId, CityRequestDto cityRequest) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> {
                    log.error("There is no city with the given id: {}", cityId);
                    return new RuntimeException(
                            "City not found with id: {}" + cityId);
                });
        city.setName(cityRequest.getName());
        city.setLongitude(cityRequest.getLongitude());
        city.setLatitude(cityRequest.getLatitude());
        city.setState(cityRequest.getState());
        city.setCountry(cityRequest.getCountry());
        cityRepository.save(city);
        return cityMapper.mapCityToCityResponseDto(city);
    }

    public void deleteCity(Long cityId) {
        cityRepository
                .findById(cityId)
                .ifPresentOrElse(city -> {
                            cityRepository.delete(city);
                            log.info("City with id: {} deleted successfully", cityId);
                        },
                        () -> {
                            log.error("No city found");
                            throw new CityNotFoundException("No city found with id: " + cityId);
                        });
    }
}
