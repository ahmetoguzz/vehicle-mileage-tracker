package com.cars.carfueltracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "employees")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PERSONAL_PHONE")
    private String personalPhone;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DEPARTMENT")
    private String department;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EMPLOYEE_START_DATE")
    private Date employeeStartDate;

    @OneToOne(mappedBy = "employee")
    @JsonBackReference
    private Vehicle vehicle;
}
