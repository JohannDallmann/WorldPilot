package de.johanndallmann.location_service.location.controller;

import de.johanndallmann.location_service.common.enums.LocationType;

public record LocationFilterDto(
        String city,
        String country,
        LocationType type
) {
}
