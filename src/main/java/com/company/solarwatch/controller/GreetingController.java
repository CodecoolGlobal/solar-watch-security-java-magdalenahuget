package com.company.solarwatch.controller;


import com.company.solarwatch.model.solarWatchData.SolarWatchReport;
import com.company.solarwatch.service.SolarWatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class GreetingController {

    @Autowired
    private SolarWatchService solarWatchService;

    public GreetingController(SolarWatchService solarWatchService) {
        this.solarWatchService = solarWatchService;
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "city", required = false, defaultValue = "World") String city,
                           @RequestParam(name = "date", required = false, defaultValue = "2023-10-02") LocalDate date,
                           Model model ) {
        SolarWatchReport solarWatchReport = solarWatchService.getSolarWatchReport(city, date);
        System.out.println(solarWatchService);
        model.addAttribute("cityInThymeleaf", solarWatchReport.city());
        model.addAttribute("dateInThymeleaf", solarWatchReport.date());
        model.addAttribute("sunsetInThymeleaf", solarWatchReport.sunset());
        model.addAttribute("sunriseInThymeleaf", solarWatchReport.sunrise());
        return "cssandjs/greeting";
    }

}