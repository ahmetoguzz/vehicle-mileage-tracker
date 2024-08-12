package com.cars.carfueltracker.service;

import com.cars.carfueltracker.model.Vehicle;
import com.cars.carfueltracker.model.VehicleDto;

import java.util.List;

public interface IVehicleService {
    Vehicle createVehicle(VehicleDto vehicleDto);

    Vehicle updateVehicle(Long id, VehicleDto vehicleDto);

    void deleteVehicle(Long id);

    VehicleDto getVehicleById(Long id);

    List<VehicleDto> getAllVehicles();
}
