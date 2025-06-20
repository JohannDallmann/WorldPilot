package de.johanndallmann.locationService.location.controller;

import de.johanndallmann.locationService.common.enums.LocationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record NewLocationDto(
        @NotBlank
        String name,

        @NotNull
        LocationType type,

        @NotBlank
        String city,

        @NotBlank
        String country,

        String description
) {
}
