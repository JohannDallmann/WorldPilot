package de.johanndallmann.locationService.location.service;

import de.johanndallmann.locationService.location.controller.LocationFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LocationService {
    List<Location> getAllLocations();

    Page<Location> getLocationPage(LocationFilterDto filter, Pageable pageable, UUID ownerId);

    Location createNewLocation(Location newLocation);

    List<Location> transferLocationsToOtherUser(LocationFilterDto filter, UUID currentOwnerId, UUID newOwnerId);
}
