package de.johanndallmann.location_service.location.controller;

import de.johanndallmann.location_service.common.enums.LocationType;

public record LocationDto(
    long id,
    String name,
    LocationType type,
    String city,
    String country,
    String description
) {
}
