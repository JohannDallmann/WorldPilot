package de.johanndallmann.locationService.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationDuplicateJpaRepository extends JpaRepository<LocationDuplicateEntity, Long> {
}
