package de.johanndallmann.location_service.location.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationService {
    List<Location> getAllLocations();

    Page<Location> getLocationPage(Pageable pageable);
}
