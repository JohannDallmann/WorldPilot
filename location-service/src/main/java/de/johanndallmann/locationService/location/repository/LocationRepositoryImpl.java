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
    private final LocationDuplicateJpaRepository locationDuplicateJpaRepository;
    private final LocationRepositoryMapper locationRepositoryMapper;

    @Override
    public List<Location> getAllLocations() {
        List<LocationEntity> locationEntityList = this.locationJpaRepository.findAll();
        return this.locationRepositoryMapper.toDomainList(locationEntityList);
    }

    /**
     * checks isPaged-status of Pageable and returns LocationPage
     * either for a fully defined PageRequest or without any Pagination-parameters
     */
    @Override
    public Page<Location> getLocationPage(Specification<LocationEntity> spec, Pageable pageable) {
        Pageable checkedPageable = pageable.isPaged() ? PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
        ) : pageable;

        Page<LocationEntity> locationEntityPage = this.locationJpaRepository.findAll(spec, checkedPageable);
        return locationEntityPage.map(locationRepositoryMapper::toDomain);
    }

    @Override
    public Location createNewLocation(Location newLocation) {
        LocationEntity savedEntity = this.locationJpaRepository.save(this.locationRepositoryMapper.toEntity(newLocation));
        return this.locationRepositoryMapper.toDomain(savedEntity);
    }

    /**
     * Uses Shadow Entity to bypass auditing (createdAt/createdBy) when duplicating entries
     */
    @Override
    public Location duplicateLocation(Location locationToDuplicate) {
        LocationDuplicateEntity savedEntity = this.locationDuplicateJpaRepository.save(this.locationRepositoryMapper.toDuplicationEntity(locationToDuplicate));
        return this.locationRepositoryMapper.toDomain(savedEntity);
    }
}
