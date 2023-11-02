package com.company.solarwatch.controller;

import com.company.solarwatch.model.dto.CityRequest;
import com.company.solarwatch.service.SolarWatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final SolarWatchService solarWatchService;

    public AdminController(SolarWatchService solarWatchService) {
        this.solarWatchService = solarWatchService;
    }

    @PostMapping("/city")
    public ResponseEntity<?> addCity(@RequestBody CityRequest cityRequest) {


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/city/{cityId}")
    public ResponseEntity<?> updateCity(@PathVariable Long cityId, @RequestBody CityRequest cityRequest) {

    }

    @DeleteMapping("/city/{cityId}")
    public ResponseEntity<?> deleteCity(@PathVariable Long cityId) {


        return "City with id: " + cityId + " has been deleted";
    }
}
