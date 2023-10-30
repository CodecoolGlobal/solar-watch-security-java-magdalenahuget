package com.company.solarwatchdatabase.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sunrise_sunsets")
public class SunriseSunset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private City city;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "sunrise_time")
    private LocalTime sunriseTime;

    @Column(name = "sunset_time")
    private LocalTime sunsetTime;

    public SunriseSunset(City city, LocalDate date, LocalTime sunriseTime, LocalTime sunsetTime) {
        this.city = city;
        this.date = date;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
    }
}
