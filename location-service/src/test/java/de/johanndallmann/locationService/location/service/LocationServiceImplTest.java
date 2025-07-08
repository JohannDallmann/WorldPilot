package de.johanndallmann.locationService.location.service;

import de.johanndallmann.locationService.common.enums.LocationType;
import de.johanndallmann.locationService.location.controller.LocationFilterDto;
import de.johanndallmann.locationService.location.repository.LocationEntity;
import de.johanndallmann.locationService.location.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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