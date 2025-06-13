package de.johanndallmann.location_service.common.mapper;

import de.johanndallmann.location_service.location.controller.LocationDto;
import de.johanndallmann.location_service.location.service.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationControllerMapper {
    LocationDto toDto(Location location);
    List<LocationDto> toDtoList(List<Location> locationList);
}
