package de.johanndallmann.locationService.location.controller;

import de.johanndallmann.locationService.common.enums.LocationType;
import lombok.Builder;

@Builder
public record NewLocationDto(
        String name,
        LocationType type,
        String city,
        String country,
        String description
) {
}
