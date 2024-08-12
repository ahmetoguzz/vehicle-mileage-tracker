package com.cars.carfueltracker.model;

import lombok.Data;

@Data
public class UpdateKmRequest {
    private String token;
    private String currentKm;
}
