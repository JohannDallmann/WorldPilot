package de.johanndallmann.locationService.location.controller;

import lombok.Builder;

/**
 * contains information about the user to which locations should be transferred
 * and filter-details
 */
@Builder
public record TransferLocationDto(
        String newOwnerId,
        Long locationId,
        String city,
        String country,
        String type
) {
}
