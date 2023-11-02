package com.company.solarwatch.service;

import com.company.solarwatch.configuration.ConfigProperties;

import com.company.solarwatch.exception.CityNotFoundException;
import com.company.solarwatch.exception.NoDataFoundInSunriseSunsetApi;
import com.company.solarwatch.model.solarWatchData.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class SolarWatchService {
    private final RestTemplate restTemplate;
    private final ConfigProperties configProperties;
    private static final Logger logger = LoggerFactory.getLogger(SolarWatchService.class);
    private final CityService cityService;
    private final SunriseSunsetService sunriseSunsetService;

    @Autowired
    public SolarWatchService(RestTemplate restTemplate, ConfigProperties configProperties, CityService cityService, SunriseSunsetService sunriseSunsetService) {
        this.restTemplate = restTemplate;
        this.configProperties = configProperties;
        this.cityService = cityService;
        this.sunriseSunsetService = sunriseSunsetService;
    }

    public SolarWatchReport getSolarWatchReport(String city, LocalDate date) {
        city = city.toLowerCase();
        city = city.substring(0, 1).toUpperCase() + city.substring(1);
        City cityForSolarWatchReport;
        SunriseSunset sunriseSunsetForSolarWatchReport;
        City cityFromDb = cityService.getCityFromDb(city);
        if (cityFromDb != null) {
            cityForSolarWatchReport = cityFromDb;
            logger.info("City from DB");
        } else {
            cityForSolarWatchReport = getCityFromApi(city);
            if(cityForSolarWatchReport == null) {
                throw new CityNotFoundException(city);
            }
            logger.info("City from API");
            cityService.saveCityToDb(cityForSolarWatchReport);
        }
        SunriseSunset sunriseSunsetFromDb = sunriseSunsetService.getSunriseSunsetFromDb(date, cityFromDb);
        if (sunriseSunsetFromDb != null) {
            sunriseSunsetForSolarWatchReport = sunriseSunsetFromDb;
            logger.info("Sunrise and sunset from DB");
        } else {
            Double latitude = cityForSolarWatchReport.getLatitude();
            Double longitude = cityForSolarWatchReport.getLongitude();
            sunriseSunsetForSolarWatchReport = getSunriseSunsetFromApi(cityForSolarWatchReport, date, latitude, longitude);
            if(sunriseSunsetForSolarWatchReport == null) {
                throw new NoDataFoundInSunriseSunsetApi();
            }
            logger.info("Sunrise and sunset from Api");
            sunriseSunsetService.saveSunriseSunsetToDb(sunriseSunsetForSolarWatchReport);
        }
        SolarWatchReport solarWatchReport = new SolarWatchReport(city, date, sunriseSunsetForSolarWatchReport.getSunriseTime(), sunriseSunsetForSolarWatchReport.getSunsetTime());
        return solarWatchReport;
    }

    private SunriseSunset getSunriseSunsetFromApi(City city, LocalDate date, Double latitude,  Double longitude) {
        OpenSolarWatchReport responseSolar = getOpenSolarWatchReportFromAPI(date, latitude, longitude);
        SunriseSunset sunriseSunsetFromApi = null;
        if (responseSolar != null && responseSolar.results() != null) {
            logger.info("Raw response from Open Solar API: {}", responseSolar);
            String sunriseStr = responseSolar.results().sunrise();
            String sunsetStr = responseSolar.results().sunset();
            logger.info("Raw response from Open Solar API: {}", responseSolar);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm:ss a", Locale.ENGLISH);
            LocalTime sunrise = LocalTime.parse(sunriseStr, formatter);
            LocalTime sunset = LocalTime.parse(sunsetStr, formatter);
            sunriseSunsetFromApi = new SunriseSunset(city, date, sunrise, sunset);
        }
        return sunriseSunsetFromApi;
    }

    private City getCityFromApi(String city) {
        OpenGeoReport[] responseGeo = getOpenGeoReportsFromAPI(city);
        City cityFromApi = null;
        if (responseGeo != null && responseGeo.length > 0) {
            OpenGeoReport openGeoReport = responseGeo[0];
            cityFromApi = new City(openGeoReport.name(), openGeoReport.lon(), openGeoReport.lat(),
                    openGeoReport.state(), openGeoReport.country());
        }
        return cityFromApi;
    }

    private OpenSolarWatchReport getOpenSolarWatchReportFromAPI(LocalDate date, Double lat, Double lon) {
        String urlSunriseSunset = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s", lat, lon, date);
        logger.info("Requesting data from Open Solar API: {}", urlSunriseSunset);
        OpenSolarWatchReport responseSolar = restTemplate.getForObject(urlSunriseSunset, OpenSolarWatchReport.class);
        logger.info("Response from Open Solar API: {}", responseSolar);
        return responseSolar;
    }

    private OpenGeoReport[] getOpenGeoReportsFromAPI(String city) {
        String API_KEY = configProperties.getConfigValue("api.key");
        String urlGeo = String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s&appid=%s", city, API_KEY);
        OpenGeoReport[] responseGeo = restTemplate.getForObject(urlGeo, OpenGeoReport[].class);
        logger.info("Response from Open Geo API: {}", responseGeo);
        return responseGeo;
    }
}
