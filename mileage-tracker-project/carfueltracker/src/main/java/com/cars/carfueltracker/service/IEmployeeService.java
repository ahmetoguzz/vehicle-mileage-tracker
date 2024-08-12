package com.cars.carfueltracker.service;

import com.cars.carfueltracker.model.Employee;
import com.cars.carfueltracker.model.EmployeeDto;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    Employee createEmployee(EmployeeDto employeeDto);

    Employee updateEmployee(Long id, EmployeeDto employeeDto);

    void deleteEmployee(Long id);

    EmployeeDto getEmployeeById(Long id);

    List<EmployeeDto> getAllEmployees();

    Optional<Employee> findByEmail(String email);

    EmployeeDto getUserDetailsByToken(String token);

    EmployeeDto updateKm(String token, String currentKm);

    void sendUpdateLinksToAllEmployees();

    void sendUpdateLinkToEmployee(Long id);
}
