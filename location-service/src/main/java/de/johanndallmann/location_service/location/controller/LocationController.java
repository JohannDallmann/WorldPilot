package de.johanndallmann.location_service.location.controller;

import de.johanndallmann.location_service.common.mapper.LocationControllerMapper;
import de.johanndallmann.location_service.location.service.Location;
import de.johanndallmann.location_service.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @GetMapping("/all")
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<Location> locationList = this.locationService.getAllLocations();
        return ResponseEntity.ok(this.locationControllerMapper.toDtoList(locationList));
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getLocationPage(Pageable pageable) {
        Page<Location> locationPage = this.locationService.getLocationPage(pageable);
        return ResponseEntity.ok(locationPage.map(locationControllerMapper::toDto).getContent());
    }
}
