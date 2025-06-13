package de.johanndallmann.location_service.location.service;

import de.johanndallmann.location_service.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
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
}
