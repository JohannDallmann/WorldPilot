package de.johanndallmann.location_service.location.controller;

import de.johanndallmann.location_service.common.mapper.LocationControllerMapper;
import de.johanndallmann.location_service.location.service.Location;
import de.johanndallmann.location_service.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final LocationControllerMapper locationControllerMapper;
    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<Location> locationList = this.locationService.getAllLocations();
        List<LocationDto> dtolist = this.locationControllerMapper.toDtoList(locationList);
        System.out.println(dtolist);
        return ResponseEntity.ok(this.locationControllerMapper.toDtoList(locationList));
    }
}
