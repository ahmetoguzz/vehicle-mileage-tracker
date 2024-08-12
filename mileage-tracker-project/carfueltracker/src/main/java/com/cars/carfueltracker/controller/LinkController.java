package com.cars.carfueltracker.controller;


import com.cars.carfueltracker.model.*;
import com.cars.carfueltracker.service.IEmployeeService;
import com.cars.carfueltracker.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController

public class LinkController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IEmployeeService employeeService;

    @GetMapping("/generate-link")
    public String generateLink(@RequestParam String email){

        Optional<Employee> employee = employeeService.findByEmail(email);
        if (employee.isPresent()) {
            String token = jwtUtil.generateToken(email);
            String link = "http://localhost:3000/?id=" + token;
            return link;
        } else {
            return "Employee not found";
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user-details")
    public ApiResponse<EmployeeDto> getUserDetails(@RequestParam String token) {
        EmployeeDto employeeDto = employeeService.getUserDetailsByToken(token);
        return ApiResponse.<EmployeeDto>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("User Details Fetched")
                        .description("User details fetched successfully.")
                        .build())
                .hasError(false)
                .payload(employeeDto)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PutMapping("/update-km")
    public ApiResponse<EmployeeDto> updateKm(@RequestBody UpdateKmRequest request) {
        EmployeeDto updatedEmployee = employeeService.updateKm(request.getToken(), request.getCurrentKm());
        return ApiResponse.<EmployeeDto>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("KM Updated")
                        .description("Last KM updated successfully.")
                        .build())
                .hasError(false)
                .payload(updatedEmployee)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    @GetMapping("/send-update-links")
    public String sendUpdateLinks() {
        employeeService.sendUpdateLinksToAllEmployees();
        return "Update links have been sent to all employees";
    }

    @GetMapping("/send-update-link/{id}")
    public String sendUpdateLink(@PathVariable Long id) {
        employeeService.sendUpdateLinkToEmployee(id);
        return "Update link has been sent to the employee.";
    }

}
