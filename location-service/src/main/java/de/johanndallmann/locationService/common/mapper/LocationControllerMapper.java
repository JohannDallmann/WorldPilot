package de.johanndallmann.locationService.common.mapper;

import de.johanndallmann.locationService.location.controller.LocationDto;
import de.johanndallmann.locationService.location.controller.NewLocationDto;
import de.johanndallmann.locationService.location.service.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationControllerMapper {
    LocationDto toDto(Location location);
    List<LocationDto> toDtoList(List<Location> locationList);

    Location newLocationToDomain(NewLocationDto newLocation);
}
