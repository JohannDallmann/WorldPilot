package de.johanndallmann.location_service.location.service;

import de.johanndallmann.location_service.location.controller.LocationFilterDto;
import de.johanndallmann.location_service.location.repository.LocationEntity;
import de.johanndallmann.location_service.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService{

    private final LocationRepository locationRepository;

    @Override
    public List<Location> getAllLocations() {
        return this.locationRepository.getAllLocations();
    }

    @Override
    public Page<Location> getLocationPage(LocationFilterDto filter, Pageable pageable) {
        Specification<LocationEntity> spec = LocationSpecifications.initialSpec()
                        .and(LocationSpecifications.hasCountry(filter.country()))
                        .and(LocationSpecifications.hasCity(filter.city()));

        return this.locationRepository.getLocationPage(spec, pageable);
    }
}
