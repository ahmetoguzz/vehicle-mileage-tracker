package com.cars.carfueltracker.repository;

import com.cars.carfueltracker.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVehicleRepository extends JpaRepository<Vehicle, Long> {
}
