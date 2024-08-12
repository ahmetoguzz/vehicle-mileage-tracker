package com.cars.carfueltracker.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private Long id;
    private Long employeeId;
    private String plateNumber;
    private String brand;
    private String model;
    private String color;
    private String hgsNumber;
    private String lastKm;
    private Date createDate;
    private int modelYear;
    private EmployeeDto employee;

}
