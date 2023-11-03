package com.company.solarwatch.repository;

import com.company.solarwatch.model.solarWatchData.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findCitiesByName(String name);

    City findByName(String name);
}
