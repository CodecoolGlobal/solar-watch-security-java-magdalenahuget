package com.company.solarwatchdatabase.repository;

import com.company.solarwatchdatabase.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findCitiesByName(String name);
}
