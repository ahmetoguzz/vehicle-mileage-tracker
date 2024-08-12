package com.cars.carfueltracker.controller;

import com.cars.carfueltracker.model.*;
import com.cars.carfueltracker.service.IEmployeeService;
import com.cars.carfueltracker.service.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehicleController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IVehicleService vehicleService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/vehicle/create")
    public ApiResponse<Vehicle> createVehicle(@RequestBody VehicleDto vehicleDto) {
        Vehicle createdVehicle = vehicleService.createVehicle(vehicleDto);
        return ApiResponse.<Vehicle>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("Vehicle created.")
                        .description("Vehicle created successfully.")
                        .build())
                .hasError(false)
                .payload(createdVehicle)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/vehicle/update/{id}")
    public ApiResponse<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody VehicleDto vehicleDto) {
        Vehicle updatedVehicle = vehicleService.updateVehicle(id, vehicleDto);
        return ApiResponse.<Vehicle>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("Vehicle updated.")
                        .description("Vehicle updated successfully.")
                        .build())
                .hasError(false)
                .payload(updatedVehicle)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/vehicle/delete/{id}")
    public ApiResponse<Void> deleteVehicle(@PathVariable Long id) {
        System.out.println("Deleting vehicle with id: " + id);
        vehicleService.deleteVehicle(id);
        return ApiResponse.<Void>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("Vehicle deleted.")
                        .description("Vehicle deleted successfully.")
                        .build())
                .hasError(false)
                .payload(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/vehicle/{id}")
    public ApiResponse<VehicleDto> getVehicleById(@PathVariable Long id) {
        VehicleDto vehicleDto = vehicleService.getVehicleById(id);
        return ApiResponse.<VehicleDto>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("Vehicle fetched.")
                        .description("Vehicle fetched successfully.")
                        .build())
                .hasError(false)
                .payload(vehicleDto)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/vehicles")
    public ApiResponse<List<VehicleDto>> getAllVehicles() {
      List<VehicleDto> vehiclesDtos = vehicleService.getAllVehicles();
        return ApiResponse.<List<VehicleDto>>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("All vehicles fetched.")
                        .description("All vehicles fetched successfully.")
                        .build())
                .hasError(false)
                .payload(vehiclesDtos)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/employee/create")
    public ApiResponse<Employee> createEmployee(@RequestBody EmployeeDto employeeDto) {
        Employee createdEmployee = employeeService.createEmployee(employeeDto);
        return ApiResponse.<Employee>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("Employee created.")
                        .description("Employee created successfully.")
                        .build())
                .hasError(false)
                .payload(createdEmployee)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/employee/update/{id}")
    public ApiResponse<Employee> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDto);
        return ApiResponse.<Employee>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("Employee updated.")
                        .description("Employee updated successfully.")
                        .build())
                .hasError(false)
                .payload(updatedEmployee)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/employee/delete/{id}")
    public ApiResponse<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ApiResponse.<Void>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("Employee deleted.")
                        .description("Employee deleted successfully.")
                        .build())
                .hasError(false)
                .payload(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/employee/{id}")
    public ApiResponse<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);
        return ApiResponse.<EmployeeDto>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("Employee fetched.")
                        .description("Employee fetched successfully.")
                        .build())
                .hasError(false)
                .payload(employeeDto)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/employees")
    public ApiResponse<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employeeDtos = employeeService.getAllEmployees();
        return ApiResponse.<List<EmployeeDto>>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("Employees fetched.")
                        .description("All employees fetched successfully.")
                        .build())
                .hasError(false)
                .payload(employeeDtos)
                .httpStatus(HttpStatus.OK)
                .build();
    }



}
