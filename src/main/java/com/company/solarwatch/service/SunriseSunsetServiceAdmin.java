package com.company.solarwatch.service;

import com.company.solarwatch.exception.CityNotFoundException;
import com.company.solarwatch.model.dto.SunriseSunsetRequestDto;
import com.company.solarwatch.model.dto.SunriseSunsetResponseDto;
import com.company.solarwatch.model.solarWatchData.City;
import com.company.solarwatch.model.solarWatchData.SunriseSunset;
import com.company.solarwatch.repository.CityRepository;
import com.company.solarwatch.repository.SunriseSunsetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SunriseSunsetServiceAdmin {

    private final SunriseSunsetRepository sunriseSunsetRepository;
    private final SunriseSunsetMapper sunriseSunsetMapper;
    private final CityMapper cityMapper;
    private final CityRepository cityRepository;

    @Autowired
    public SunriseSunsetServiceAdmin(SunriseSunsetRepository sunriseSunsetRepository, SunriseSunsetMapper sunriseSunsetMapper, CityMapper cityMapper, CityRepository cityRepository) {
        this.sunriseSunsetRepository = sunriseSunsetRepository;
        this.sunriseSunsetMapper = sunriseSunsetMapper;
        this.cityMapper = cityMapper;
        this.cityRepository = cityRepository;
    }

    public SunriseSunsetResponseDto getSunriseSunsetById(Long sunriseSunsetId) {
        SunriseSunset sunriseSunset = sunriseSunsetRepository
                .findById(sunriseSunsetId)
                .orElseThrow(() -> {
                    log.error("There is no sunriseSunset with the given id: {}", sunriseSunsetId);
                    return new RuntimeException(
                            "SunriseSunset not found with id: {}" + sunriseSunsetId);
                });
        return sunriseSunsetMapper.mapToSunriseSunsetResponseDto(sunriseSunset);
    }

    public SunriseSunsetResponseDto createSunriseSunset(SunriseSunsetRequestDto sunriseSunsetRequestDto) {

        City city = cityMapper.mapCityResponseDtoToCity(sunriseSunsetRequestDto.getCity());
        City existingCity = cityRepository.findByName(city.getName());
        if (existingCity == null) {
            cityRepository.save(city);
        } else {
            city = existingCity;
        }
        SunriseSunset sunriseSunset = new SunriseSunset();
        sunriseSunset.setCity(city);
        sunriseSunset.setDate(sunriseSunsetRequestDto.getDate());
        sunriseSunset.setSunriseTime(sunriseSunsetRequestDto.getSunriseTime());
        sunriseSunset.setSunsetTime(sunriseSunsetRequestDto.getSunsetTime());
        sunriseSunsetRepository.save(sunriseSunset);
        return sunriseSunsetMapper.mapToSunriseSunsetResponseDto(sunriseSunset);
    }

    public SunriseSunsetResponseDto updateSunriseSunsetById(Long sunriseSunsetId, SunriseSunsetRequestDto sunriseSunsetRequest) {
        SunriseSunset sunriseSunset = sunriseSunsetRepository.findById(sunriseSunsetId)
                .orElseThrow(() -> {
                    log.error("There is no sunriseSunset with the given id: {}", sunriseSunsetId);
                    return new RuntimeException(
                            "SunriseSunset not found with id: {}" + sunriseSunsetId);
                });
        City city = cityRepository.findByName(sunriseSunsetRequest.getCity().getName());
        if (city == null) {
            log.error("There is no city with the given name: {}", sunriseSunsetRequest.getCity().getName());
            throw new CityNotFoundException("City not found with name: " + sunriseSunsetRequest.getCity().getName());
        }
        cityRepository.save(city);
        sunriseSunset.setCity(city);
        sunriseSunset.setDate(sunriseSunsetRequest.getDate());
        sunriseSunset.setSunriseTime(sunriseSunsetRequest.getSunriseTime());
        sunriseSunset.setSunsetTime(sunriseSunsetRequest.getSunsetTime());
        sunriseSunsetRepository.save(sunriseSunset);
        return sunriseSunsetMapper.mapToSunriseSunsetResponseDto(sunriseSunset);
    }

    public void deleteSunriseSunset(Long sunriseSunsetId) {
        sunriseSunsetRepository
                .findById(sunriseSunsetId)
                .ifPresentOrElse(city -> {
                            sunriseSunsetRepository.delete(city);
                            log.info("SunriseSunset with id: {} deleted successfully", sunriseSunsetId);
                        },
                        () -> {
                            log.error("No sunriseSunset found");
                            throw new CityNotFoundException("No sunriseSunset found with id: " + sunriseSunsetId);
                        });
    }
}
