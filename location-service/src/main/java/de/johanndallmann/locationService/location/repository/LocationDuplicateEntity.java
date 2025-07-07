package de.johanndallmann.locationService.location.repository;

import de.johanndallmann.locationService.common.enums.LocationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Shadow entity without Auditing for creatorId and createdAt, used only for duplication
public class LocationDuplicateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type")
    private LocationType type;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "description")
    private String description;

    @Column(name = "creator_id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID creatorId;

    @Column(name = "owner_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID ownerId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
