package de.johanndallmann.location_service.location.repository;

import de.johanndallmann.location_service.common.mapper.LocationRepositoryMapper;
import de.johanndallmann.location_service.location.service.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository{

    private final LocationJpaRepository locationJpaRepository;
    private final LocationRepositoryMapper locationRepositoryMapper;

    @Override
    public List<Location> getAllLocations() {
        List<LocationEntity> locationEntityList = this.locationJpaRepository.findAll();
        return this.locationRepositoryMapper.toDomainList(locationEntityList);
    }
}
