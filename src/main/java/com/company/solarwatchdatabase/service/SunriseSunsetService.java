package com.company.solarwatchdatabase.service;

import com.company.solarwatchdatabase.model.City;
import com.company.solarwatchdatabase.model.SunriseSunset;
import com.company.solarwatchdatabase.repository.SunriseSunsetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SunriseSunsetService {


    private final SunriseSunsetRepository sunriseSunsetRepository;

    @Autowired
    public SunriseSunsetService(SunriseSunsetRepository sunriseSunsetRepository) {
        this.sunriseSunsetRepository = sunriseSunsetRepository;
    }

    public List<SunriseSunset> getAllSunriseSunsets() {
        return sunriseSunsetRepository.findAll();
    }

    public SunriseSunset getSunriseSunsetById(long id) {
        Optional<SunriseSunset> optionalSunriseSunset = sunriseSunsetRepository.findById(id);
        return optionalSunriseSunset.orElse(null);
    }

    public SunriseSunset createSunriseSunset(SunriseSunset sunriseSunset) {
        return sunriseSunsetRepository.save(sunriseSunset);
    }

    public SunriseSunset updateSunriseSunset(long id, SunriseSunset sunriseSunset) {
        if (sunriseSunsetRepository.existsById(id)) {
            sunriseSunset.setId(id);
            return sunriseSunsetRepository.save(sunriseSunset);
        }
        return null;
    }

    public void deleteSunriseSunset(long id) {
        sunriseSunsetRepository.deleteById(id);
    }

    public SunriseSunset getSunriseSunsetFromDb(LocalDate date, City cityFromDb) {
        SunriseSunset sunriseSunsetFromDb = sunriseSunsetRepository.findByCityAndDate(
                cityFromDb, date).stream().findFirst().orElse(null);
        return sunriseSunsetFromDb;
    }

    public void saveSunriseSunsetToDb(SunriseSunset sunriseSunset) {
        sunriseSunsetRepository.save(sunriseSunset);
    }
}
