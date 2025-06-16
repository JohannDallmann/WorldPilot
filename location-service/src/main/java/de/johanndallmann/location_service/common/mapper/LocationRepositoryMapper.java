package de.johanndallmann.location_service.common.mapper;

import de.johanndallmann.location_service.location.repository.LocationEntity;
import de.johanndallmann.location_service.location.service.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationRepositoryMapper {
    Location toDomain(LocationEntity locationEntity);
    List<Location> toDomainList(List<LocationEntity> locationEntityList);
}
