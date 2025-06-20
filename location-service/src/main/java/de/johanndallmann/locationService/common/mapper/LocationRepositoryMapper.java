package de.johanndallmann.locationService.common.mapper;

import de.johanndallmann.locationService.location.repository.LocationEntity;
import de.johanndallmann.locationService.location.service.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationRepositoryMapper {
    Location toDomain(LocationEntity locationEntity);
    List<Location> toDomainList(List<LocationEntity> locationEntityList);

    LocationEntity toEntity(Location location);
}
