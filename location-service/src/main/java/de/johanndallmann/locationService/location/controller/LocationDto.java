package de.johanndallmann.locationService.location.controller;

import de.johanndallmann.locationService.common.enums.LocationType;

import java.time.Instant;
import java.util.UUID;

public record LocationDto(
    long id,
    String name,
    LocationType type,
    String city,
    String country,
    String description,
    UUID creatorId,
    UUID ownerId,
    Instant createdAt,
    Instant updatedAt
) {
}
