package com.company.solarwatch.service;

import com.company.solarwatch.model.solarWatchData.City;
import com.company.solarwatch.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCityById(long id) {
        Optional<City> optionalCity = cityRepository.findById(id);
        return optionalCity.orElse(null);
    }

    public City createCity(City city) {
        return cityRepository.save(city);
    }

    public City updateCity(long id, City city) {
        if (cityRepository.existsById(id)) {
            city.setId(id);
            return cityRepository.save(city);
        }
        return null;
    }

    public void deleteCity(long id) {
        cityRepository.deleteById(id);
    }

    public City getCityFromDb(String city) {
        City cityFromDb = cityRepository.findCitiesByName(city).stream().findFirst().orElse(null);
        return cityFromDb;
    }

    public void saveCityToDb(City city) {
        cityRepository.save(city);
    }
}
