package com.company.solarwatchdatabase.repository;

import com.company.solarwatchdatabase.model.City;
import com.company.solarwatchdatabase.model.SunriseSunset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SunriseSunsetRepository extends JpaRepository<SunriseSunset, Long> {
    List<SunriseSunset> findByCityAndDate(City city, LocalDate date);
}