package de.johanndallmann.locationService.location.service;

import de.johanndallmann.locationService.common.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Location {
    private long id;
    private String name;
    private LocationType type;
    private String city;
    private String country;
    private String description;
    private UUID creatorId;
    private UUID ownerId;
    private Instant createdAt;
    private Instant updatedAt;

}
