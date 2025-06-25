package de.johanndallmann.locationService.location.repository;

import de.johanndallmann.locationService.common.enums.LocationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
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
public class LocationEntity {

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

    @CreatedBy
    @Column(name = "creator_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID creatorId;

    @Column(name = "owner_id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID ownerId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Sets ownerId initially to creatorId, but keeps it editable
     */
    @PrePersist
    public void prePersist() {
        if (ownerId == null) {
            this.ownerId = this.creatorId;
        }
    }
}
