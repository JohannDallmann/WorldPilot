package de.johanndallmann.location_service.location.controller;

public record LocationFilterDto(
        String city,
        String country,
        String type
) {
}
