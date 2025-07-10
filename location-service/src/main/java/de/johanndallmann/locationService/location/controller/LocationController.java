package de.johanndallmann.locationService.location.controller;

import de.johanndallmann.locationService.common.mapper.LocationControllerMapper;
import de.johanndallmann.locationService.location.service.Location;
import de.johanndallmann.locationService.location.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
@Tag(name = "Locations", description = "API for Location-operations")
public class LocationController {

    private final LocationService locationService;
    private final LocationControllerMapper locationControllerMapper;
    @GetMapping("/all")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<Location> locationList = this.locationService.getAllLocations();
        return ResponseEntity.ok(this.locationControllerMapper.toDtoList(locationList));
    }

    @Operation(
            summary = "Returns a selection of locations",
            description = "Expects a LocationFilterDto as RequestBody with a valid LocationType " +
                    "and a Pageable Object in the URL which contains pagenumber, pagesize and sorting " +
                    "(e.g. \"/locations?page=1&size=3&sort=id,desc\"). " +
                    "Returns page-content of all locations matching the filter-attributes."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Locations successfully returned"),
            @ApiResponse(responseCode = "400", description = "Invalid LocationFilterDto"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<List<LocationDto>> getLocationPage(@RequestBody LocationFilterDto filter, Pageable pageable, Principal principal) {
        Page<Location> locationPage = this.locationService.getLocationPage(filter, pageable, UUID.fromString(principal.getName()));
        return ResponseEntity.ok(locationPage.map(locationControllerMapper::toDto).getContent());
    }

    @Operation(
            summary = "Creates a new location",
            description = "Expects a NewLocationDto as RequestBody with a valid LocationType " +
                    "and returns the ID of the created Location."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Location successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid NewLocationDto"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Void> postNewLocation(@Valid @RequestBody NewLocationDto newLocation){
        Location savedLocation = this.locationService.createNewLocation(this.locationControllerMapper.newLocationToDomain(newLocation));
        return responseEntityWithLocation(savedLocation.getId());
    }

    @Operation(
            summary = "Transfers specified locations to another user",
            description = "Expects a TransferLocationDto as RequestBody which consists out of the ID of the new user " +
                    "the locations are transferred to and filter attributes to specify the locations (see LocationFilterDto). " +
                    "The specified locations are duplicated and a list of the created locations is returned."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Locations successfully duplicated"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/transfer")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<List<LocationDto>> transferLocationsToOtherUser(@RequestBody TransferLocationDto transferParams, Principal principal){
        LocationFilterDto filter = this.locationControllerMapper.transferLocationDtoToLocationFilterDto(transferParams);
        List<Location> duplicatedLocationsList = this.locationService.transferLocationsToOtherUser(filter, UUID.fromString(principal.getName()), UUID.fromString(transferParams.newOwnerId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.locationControllerMapper.toDtoList(duplicatedLocationsList));
    }

    /**
     * Creates the Uri based on the current request and the resourceId and wraps it with a ResponseEntity
     */
    private ResponseEntity<Void> responseEntityWithLocation(Object resourceId) {
        URI resourceLocation = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{childId}").buildAndExpand(resourceId)
                .toUri();

        return ResponseEntity.created(resourceLocation).build();
    }
}
