package de.johanndallmann.locationService.location.service;

import de.johanndallmann.locationService.common.enums.LocationType;
import de.johanndallmann.locationService.common.exceptionhandling.exceptions.InvalidFilterException;
import de.johanndallmann.locationService.common.exceptionhandling.exceptions.InvalidUserException;
import de.johanndallmann.locationService.location.controller.LocationFilterDto;
import de.johanndallmann.locationService.location.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    @Test
    void transferLocationToOtherUser_withLocationIdFilter_duplicatesAndReturnsListWithOneLocation() {
        UUID oldOwnerId = UUID.fromString("a6b424e1-b86d-418d-a0f7-f0666920d047");
        UUID newOwnerId = UUID.fromString("a6b424e1-b86d-418d-a0f7-f0666920d048");
        Instant createdAt = Instant.parse("2025-01-01T12:00:00Z");
        Instant updatedAt = Instant.parse("2025-01-02T12:00:00Z");

        Location originalLocation = this.createTestLocation("Location1", LocationType.RESTAURANT, "City1", "country1", oldOwnerId, oldOwnerId, createdAt, createdAt);
        Location duplicatedBeforeSave = originalLocation.toBuilder()
                .ownerId(newOwnerId).updatedAt(null).build();
        Location expectedSavedDuplicate = originalLocation.toBuilder()
                .ownerId(newOwnerId).updatedAt(updatedAt).build();

        LocationFilterDto filter = LocationFilterDto.builder()
                .locationId(1L)
                .build();

        Page<Location> locationPage = new PageImpl<>(List.of(originalLocation));
        when(this.locationRepository.getLocationPage(any(), eq(Pageable.unpaged()))).thenReturn(locationPage);
        when(this.locationRepository.duplicateLocation(duplicatedBeforeSave)).thenReturn(expectedSavedDuplicate);

        List<Location> result = this.locationService.transferLocationsToOtherUser(filter, oldOwnerId, newOwnerId);

        assertEquals(1, result.size());
        this.assertLocationEquals(expectedSavedDuplicate, result.get(0));

        verify(this.locationRepository, times(1)).getLocationPage(any(),eq(Pageable.unpaged()));
        verify(this.locationRepository, times(1)).duplicateLocation(duplicatedBeforeSave);
    }

    @Test
    void transferLocationToOtherUser_withCityFilter_duplicatesAndReturnsListWithTwoLocations() {
        UUID oldOwnerId = UUID.fromString("a6b424e1-b86d-418d-a0f7-f0666920d047");
        UUID newOwnerId = UUID.fromString("a6b424e1-b86d-418d-a0f7-f0666920d048");
        Instant createdAt = Instant.parse("2025-01-01T12:00:00Z");
        Instant updatedAt = Instant.parse("2025-01-02T12:00:00Z");
        String cityToFilter = "City1";

        Location originalLocation1 = this.createTestLocation("Location1", LocationType.RESTAURANT, cityToFilter, "country1", oldOwnerId, oldOwnerId, createdAt, createdAt);
        Location duplicatedBeforeSave1 = originalLocation1.toBuilder()
                .ownerId(newOwnerId).updatedAt(null).build();
        Location expectedSavedDuplicate1 = originalLocation1.toBuilder()
                .ownerId(newOwnerId).updatedAt(updatedAt).build();

        Location originalLocation2 = originalLocation1.toBuilder()
                .name("Location2").build();
        Location duplicatedBeforeSave2 = originalLocation2.toBuilder()
                .ownerId(newOwnerId).updatedAt(null).build();
        Location expectedSavedDuplicate2 = originalLocation2.toBuilder()
                .ownerId(newOwnerId).updatedAt(updatedAt).build();

        LocationFilterDto filter = LocationFilterDto.builder()
                .city(cityToFilter)
                .build();

        Page<Location> locationPage = new PageImpl<>(List.of(originalLocation1, originalLocation2));
        when(this.locationRepository.getLocationPage(any(), eq(Pageable.unpaged()))).thenReturn(locationPage);
        when(this.locationRepository.duplicateLocation(duplicatedBeforeSave1)).thenReturn(expectedSavedDuplicate1);
        when(this.locationRepository.duplicateLocation(duplicatedBeforeSave2)).thenReturn(expectedSavedDuplicate2);

        List<Location> result = this.locationService.transferLocationsToOtherUser(filter, oldOwnerId, newOwnerId);

        assertEquals(2, result.size());
        this.assertLocationEquals(expectedSavedDuplicate1, result.get(0));
        this.assertLocationEquals(expectedSavedDuplicate2, result.get(1));

        verify(this.locationRepository, times(1)).getLocationPage(any(),eq(Pageable.unpaged()));
        verify(this.locationRepository, times(1)).duplicateLocation(duplicatedBeforeSave1);
        verify(this.locationRepository, times(1)).duplicateLocation(duplicatedBeforeSave2);
    }

    @Test
    void transferLocationToOtherUser_noFilterSet_throwsInvalidFilterException(){
        UUID oldOwnerId = UUID.fromString("a6b424e1-b86d-418d-a0f7-f0666920d047");
        UUID newOwnerId = UUID.fromString("a6b424e1-b86d-418d-a0f7-f0666920d048");

        LocationFilterDto filter = LocationFilterDto.builder().build();

        assertThrows(InvalidFilterException.class, () -> this.locationService.transferLocationsToOtherUser(filter, oldOwnerId, newOwnerId));
    }

    @Test
    void transferLocationToOtherUser_sameUserAsBefore_throwsInvalidUserException(){
        UUID oldOwnerId = UUID.fromString("a6b424e1-b86d-418d-a0f7-f0666920d047");

        LocationFilterDto filter = LocationFilterDto.builder()
                .locationId(1L).build();

        assertThrows(InvalidUserException.class, () -> this.locationService.transferLocationsToOtherUser(filter, oldOwnerId, oldOwnerId));
    }

    private Location createTestLocation(String name, LocationType type, String city, String country, UUID creatorId, UUID ownerId, Instant createdAt, Instant updatedAt) {
        return Location.builder()
                .name(name)
                .type(type)
                .city(city)
                .country(country)
                .creatorId(creatorId)
                .ownerId(ownerId)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    private void assertLocationEquals(Location expected, Location actual){
        assertEquals(expected.getName(),actual.getName());
        assertEquals(expected.getOwnerId(),actual.getOwnerId());
        assertEquals(expected.getCreatedAt(),actual.getCreatedAt());
        assertEquals(expected.getUpdatedAt(),actual.getUpdatedAt());
    }
}