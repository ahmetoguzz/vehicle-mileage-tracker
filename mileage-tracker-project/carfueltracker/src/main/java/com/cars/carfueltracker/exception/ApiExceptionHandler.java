package com.cars.carfueltracker.exception;

import com.cars.carfueltracker.model.ApiResponse;
import com.cars.carfueltracker.model.FriendlyMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(GlobalException.class)
    public ApiResponse<String> handleGlobalException (GlobalException e){
        return ApiResponse.<String>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("Hata!")
                        .description(e.getMessage())
                        .build())
                .hasError(true)
                .payload(null)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ApiResponse<String> handleEmployeeNotFoundException(EmployeeNotFoundException e){
        return ApiResponse.<String>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("Hata!")
                        .description(e.getMessage())
                        .build())
                .hasError(true)
                .payload(null)
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(VehicleNotFoundException.class)
    public ApiResponse<String> handleVehicleNotFoundException(VehicleNotFoundException e){
        return ApiResponse.<String>builder()
                .friendlyMessage(FriendlyMessage.builder()
                        .title("Hata!")
                        .description(e.getMessage())
                        .build())
                .hasError(true)
                .payload(null)
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

}
