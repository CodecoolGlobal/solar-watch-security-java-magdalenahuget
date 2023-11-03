package com.company.solarwatch.controller;

import com.company.solarwatch.model.dto.SunriseSunsetRequestDto;
import com.company.solarwatch.model.dto.SunriseSunsetResponseDto;
import com.company.solarwatch.service.SunriseSunsetServiceAdmin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sunrise_sunset")
//@PreAuthorize("hasRole('ADMIN')")
public class SunriseSunsetController {

    private final SunriseSunsetServiceAdmin sunriseSunsetServiceAdmin;

    public SunriseSunsetController(SunriseSunsetServiceAdmin sunriseSunsetServiceAdmin) {
        this.sunriseSunsetServiceAdmin = sunriseSunsetServiceAdmin;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{sunrise_sunset_id}")
    public ResponseEntity<SunriseSunsetResponseDto> getSunriseSunset(@PathVariable Long sunrise_sunset_id) {
        SunriseSunsetResponseDto cityResponse = sunriseSunsetServiceAdmin.getSunriseSunsetById(sunrise_sunset_id);
        if (cityResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(cityResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SunriseSunsetResponseDto> addSunriseSunset(@RequestBody SunriseSunsetRequestDto sunriseSunsetRequestDto) {
        SunriseSunsetResponseDto sunriseSunsetResponseDto = sunriseSunsetServiceAdmin.createSunriseSunset(sunriseSunsetRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sunriseSunsetResponseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{sunrise_sunsetId}")
    public ResponseEntity<SunriseSunsetResponseDto> updateSunriseSunset(@PathVariable Long sunrise_sunsetId, @RequestBody SunriseSunsetRequestDto sunriseSunsetRequestDto) {
        SunriseSunsetResponseDto existingSunriseSunset = sunriseSunsetServiceAdmin.updateSunriseSunsetById(sunrise_sunsetId, sunriseSunsetRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(existingSunriseSunset);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{sunrise_sunsetId}")
    public ResponseEntity<?> deleteSunriseSunset(@PathVariable Long sunrise_sunsetId) {
        sunriseSunsetServiceAdmin.deleteSunriseSunset(sunrise_sunsetId);
        return ResponseEntity.status(HttpStatus.OK).body("City with id: " + sunrise_sunsetId + " has been deleted");
    }
}
