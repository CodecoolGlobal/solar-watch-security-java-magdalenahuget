package com.company.solarwatch.repository;

import com.company.solarwatch.model.solarWatchData.City;
import com.company.solarwatch.model.solarWatchData.SunriseSunset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SunriseSunsetRepository extends JpaRepository<SunriseSunset, Long> {
    List<SunriseSunset> findByCityAndDate(City city, LocalDate date);
}