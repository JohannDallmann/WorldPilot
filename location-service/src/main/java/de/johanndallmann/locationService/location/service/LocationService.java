package de.johanndallmann.locationService.location.service;

import de.johanndallmann.locationService.location.controller.LocationFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationService {
    List<Location> getAllLocations();

    Page<Location> getLocationPage(LocationFilterDto filter, Pageable pageable);

    Location createNewLocation(Location newLocation);
}
