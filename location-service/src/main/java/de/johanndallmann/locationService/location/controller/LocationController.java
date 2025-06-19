package de.johanndallmann.locationService.location.controller;

import de.johanndallmann.locationService.common.mapper.LocationControllerMapper;
import de.johanndallmann.locationService.location.service.Location;
import de.johanndallmann.locationService.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * Handles Get-request for locations and maps into LocationDto
     * @param filter contains all filter-attributes
     * @param pageable contains informations about pagenumber, pagesize and sorting
     *                 (e.g. "/locations?page=1&size=3&sort=id,desc")
     * @return page-content of all locations matching the filter-attributes
     */
    @GetMapping
    public ResponseEntity<List<LocationDto>> getLocationPage(@RequestBody LocationFilterDto filter, Pageable pageable) {
        Page<Location> locationPage = this.locationService.getLocationPage(filter, pageable);
        return ResponseEntity.ok(locationPage.map(locationControllerMapper::toDto).getContent());
    }
}
