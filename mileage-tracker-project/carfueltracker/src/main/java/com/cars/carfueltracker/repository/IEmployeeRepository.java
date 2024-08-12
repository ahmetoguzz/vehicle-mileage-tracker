package com.cars.carfueltracker.repository;

import com.cars.carfueltracker.model.Employee;
import com.cars.carfueltracker.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
}
