package de.johanndallmann.locationService.location.service;

import de.johanndallmann.locationService.common.exceptionhandling.exceptions.InvalidFilterException;
import de.johanndallmann.locationService.location.controller.LocationFilterDto;
import de.johanndallmann.locationService.location.repository.LocationEntity;
import de.johanndallmann.locationService.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService{

    private final LocationRepository locationRepository;

    @Override
    public List<Location> getAllLocations() {
        return this.locationRepository.getAllLocations();
    }

    /**
     * Sets the specification based on the filter-attributes and returns a page of Locations
     */
    @Override
    public Page<Location> getLocationPage(LocationFilterDto filter, Pageable pageable, UUID ownerId) {
        Specification<LocationEntity> spec = LocationSpecifications.initialSpec()
                .and(LocationSpecifications.hasOwnerId(ownerId))
                .and(LocationSpecifications.hasLocationId(filter.locationId()))
                .and(LocationSpecifications.hasCountry(filter.country()))
                .and(LocationSpecifications.hasCity(filter.city()))
                .and(LocationSpecifications.hasType(filter.type()));

        return this.locationRepository.getLocationPage(spec, pageable);
    }

    @Override
    public Location createNewLocation(Location newLocation) {
        return this.locationRepository.createNewLocation(newLocation);
    }

    @Override
    public List<Location> transferLocationsToOtherUser(LocationFilterDto filter, UUID currentOwnerId, UUID newOwnerId) {
        this.checkForAtLeastOneValidFilterApplied(filter);
        Page<Location> locationPage = this.getLocationPage(filter, Pageable.unpaged(), currentOwnerId);

        List<Location> duplicatedLocationsList = new ArrayList<>();

        locationPage.getContent().forEach(locationToCopy -> {
            // TODO: check if location was already copied
            Location locationDuplicate = Location.builder()
                    .name(locationToCopy.getName())
                    .type(locationToCopy.getType())
                    .city(locationToCopy.getCity())
                    .country(locationToCopy.getCountry())
                    .description(locationToCopy.getDescription())
                    .creatorId(locationToCopy.getCreatorId())
                    .ownerId(newOwnerId)
                    .createdAt(locationToCopy.getCreatedAt())
                    .build();
            duplicatedLocationsList.add(this.locationRepository.duplicateLocation(locationDuplicate));
        });
        return duplicatedLocationsList;
    }

    private void checkForAtLeastOneValidFilterApplied(LocationFilterDto filter){
        if (filter.locationId() == null && filter.city() == null && filter.country() == null && filter.type() == null){
            throw new InvalidFilterException("No filter discovered: At least one valid filter needs to be applied for locationtransfer.");
        }
    }
}
