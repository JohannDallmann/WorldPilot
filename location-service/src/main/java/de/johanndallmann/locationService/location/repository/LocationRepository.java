package de.johanndallmann.locationService.location.repository;

import de.johanndallmann.locationService.location.service.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface LocationRepository {
    List<Location> getAllLocations();

    Page<Location> getLocationPage(Specification<LocationEntity> spec, Pageable pageable);

    Location createNewLocation(Location newLocation);

    Location duplicateLocation(Location locationToDuplicate);
}
