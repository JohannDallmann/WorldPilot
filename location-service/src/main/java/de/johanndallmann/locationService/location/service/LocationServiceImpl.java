package de.johanndallmann.locationService.location.service;

import de.johanndallmann.locationService.location.controller.LocationFilterDto;
import de.johanndallmann.locationService.location.repository.LocationEntity;
import de.johanndallmann.locationService.location.repository.LocationRepository;
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

    /**
     * Sets the specification based on the filter-attributes and returns
     */
    @Override
    public Page<Location> getLocationPage(LocationFilterDto filter, Pageable pageable) {
        Specification<LocationEntity> spec = LocationSpecifications.initialSpec()
                .and(LocationSpecifications.hasCountry(filter.country()))
                .and(LocationSpecifications.hasCity(filter.city()))
                .and(LocationSpecifications.hasType(filter.type()));

        return this.locationRepository.getLocationPage(spec, pageable);
    }
}
