package com.company.solarwatch.controller;

import com.company.solarwatch.exception.CityNotFoundException;
import com.company.solarwatch.exception.NoDataFoundInSunriseSunsetApi;
import com.company.solarwatch.model.solarWatchData.SolarWatchReport;
import com.company.solarwatch.service.SolarWatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class SolarWatchController {

    private final SolarWatchService solarWatchService;

    public SolarWatchController(SolarWatchService solarWatchService) {
        this.solarWatchService = solarWatchService;
    }

    @GetMapping("/solar-watch")
    public ResponseEntity<?> getSunriseTime(
            @RequestParam("city") String city,
            @RequestParam("date") LocalDate date) {
        try {
            SolarWatchReport solarWatchReport = solarWatchService.getSolarWatchReport(city, date);
            return new ResponseEntity<>(solarWatchReport, HttpStatus.OK);
        } catch (CityNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (NoDataFoundInSunriseSunsetApi exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}