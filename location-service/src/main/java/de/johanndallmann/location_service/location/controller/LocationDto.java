package de.johanndallmann.location_service.location.controller;

import de.johanndallmann.location_service.common.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private long id;
    private String name;
    private LocationType type;
    private String city;
    private String country;
    private String description;
}
