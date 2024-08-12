package com.cars.carfueltracker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class FriendlyMessage {
    private String title;
    private String description;
}
