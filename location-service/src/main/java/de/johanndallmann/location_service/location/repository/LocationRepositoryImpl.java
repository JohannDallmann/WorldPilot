package de.johanndallmann.location_service.location.repository;

import de.johanndallmann.location_service.location.service.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository{
    //private final LocationJpaRepository locationJpaRepository;
    @Override
    public List<Location> getAllLocations() {
        return new ArrayList<>(List.of(
                new Location(1L, "Location1"),
                new Location(2L, "Location2")
        ));
    }
}
