package de.johanndallmann.location_service.location.repository;

import de.johanndallmann.location_service.location.service.Location;

import java.util.List;

public interface LocationRepository {
    List<Location> getAllLocations();
}
