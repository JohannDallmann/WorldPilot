package de.johanndallmann.locationService.location.controller;

public record LocationFilterDto(
        String city,
        String country,
        String type
) {
}
