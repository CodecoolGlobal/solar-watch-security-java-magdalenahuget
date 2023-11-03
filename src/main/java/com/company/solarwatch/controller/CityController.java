package com.company.solarwatch.controller;

import com.company.solarwatch.model.dto.CityRequestDto;
import com.company.solarwatch.model.dto.CityResponseDto;
import com.company.solarwatch.service.CityServiceAdmin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/city")
//@PreAuthorize("hasRole('ADMIN')")
public class CityController {

    private final CityServiceAdmin cityServiceAdmin;

    public CityController(CityServiceAdmin cityServiceAdmin) {
        this.cityServiceAdmin = cityServiceAdmin;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{cityId}")
    public ResponseEntity<CityResponseDto> getCity(@PathVariable Long cityId) {
        CityResponseDto cityResponse = cityServiceAdmin.getCityById(cityId);
        if (cityResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(cityResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CityResponseDto> addCity(@RequestBody CityRequestDto cityRequestDto) {
        CityResponseDto cityResponse = cityServiceAdmin.createCity(cityRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cityResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{cityId}")
    public ResponseEntity<CityResponseDto> updateCity(
            @PathVariable Long cityId,
            @RequestBody CityRequestDto cityRequest) {
        CityResponseDto existingCity = cityServiceAdmin.updateCityById(cityId, cityRequest);
        return ResponseEntity.status(HttpStatus.OK).body(existingCity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{cityId}")
    public ResponseEntity<?> deleteCity(@PathVariable Long cityId) {
        cityServiceAdmin.deleteCity(cityId);
        return ResponseEntity.status(HttpStatus.OK).body("City with id: " + cityId + " has been deleted");
    }
}
