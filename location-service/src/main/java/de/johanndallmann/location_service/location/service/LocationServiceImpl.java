package de.johanndallmann.location_service.location.service;

import de.johanndallmann.location_service.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Location> getLocationPage(Pageable pageable) {
        return this.locationRepository.getLocationPage(pageable);
    }
}
