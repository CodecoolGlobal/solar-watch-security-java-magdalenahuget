package com.company.solarwatch.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityRequestDto {

    private String name;
    private double longitude;
    private double latitude;
    private String state;
    private String country;
}
