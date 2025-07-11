package de.johanndallmann.locationService.location.controller;

import lombok.Builder;

@Builder
public record LocationFilterDto(
        Long locationId,
        String city,
        String country,
        String type
) {
}
