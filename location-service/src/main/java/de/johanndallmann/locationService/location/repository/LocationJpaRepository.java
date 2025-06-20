package de.johanndallmann.locationService.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LocationJpaRepository extends JpaRepository<LocationEntity, Long>, JpaSpecificationExecutor<LocationEntity> {
    Optional<LocationEntity> findByName(String name);
}
