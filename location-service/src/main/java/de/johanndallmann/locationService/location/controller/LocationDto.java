package de.johanndallmann.locationService.location.controller;

import de.johanndallmann.locationService.common.enums.LocationType;

public record LocationDto(
    long id,
    String name,
    LocationType type,
    String city,
    String country,
    String description
) {
}
