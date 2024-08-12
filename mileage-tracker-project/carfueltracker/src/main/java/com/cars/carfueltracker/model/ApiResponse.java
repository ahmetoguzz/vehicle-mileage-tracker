package com.cars.carfueltracker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Builder
@Getter @Setter
public class ApiResponse<T> {
    private FriendlyMessage friendlyMessage;
    private boolean hasError;
    private T payload;
    private HttpStatus httpStatus;
}
