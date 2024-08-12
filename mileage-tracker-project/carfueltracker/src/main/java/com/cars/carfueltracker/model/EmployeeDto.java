package com.cars.carfueltracker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDto {
    private long employeeId;
    private String name;
    private String surname;
    private String email;
    private String personalPhone;
    private String title;
    private String department;
    private Date employeeStartDate;
    private VehicleDto vehicle;
}
