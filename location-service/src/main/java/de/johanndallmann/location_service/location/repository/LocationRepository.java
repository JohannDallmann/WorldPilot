package de.johanndallmann.location_service.location.repository;

import de.johanndallmann.location_service.location.service.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface LocationRepository {
    List<Location> getAllLocations();

    Page<Location> getLocationPage(Specification<LocationEntity> spec, Pageable pageable);
}
