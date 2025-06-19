package de.johanndallmann.locationService.location.repository;

import de.johanndallmann.locationService.common.mapper.LocationRepositoryMapper;
import de.johanndallmann.locationService.location.service.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public Page<Location> getLocationPage(Specification<LocationEntity> spec, Pageable pageable) {
        Page<LocationEntity> locationEntityPage = this.locationJpaRepository.findAll(
                spec,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
                )
        );
        return locationEntityPage.map(locationRepositoryMapper::toDomain);
    }
}
