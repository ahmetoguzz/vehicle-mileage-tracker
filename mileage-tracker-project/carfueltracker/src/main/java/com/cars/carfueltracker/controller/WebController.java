package com.cars.carfueltracker.controller;

import com.cars.carfueltracker.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    private final EmailService mailService;

    public WebController(EmailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }

}
