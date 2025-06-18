package de.johanndallmann.location_service.location.service;

import de.johanndallmann.location_service.common.enums.LocationType;
import de.johanndallmann.location_service.common.exceptionhandling.exceptions.InvalidEnumValueException;
import de.johanndallmann.location_service.location.repository.LocationEntity;
import org.springframework.data.jpa.domain.Specification;

public class LocationSpecifications {

    public static Specification<LocationEntity> initialSpec(){
        return (root, query, cb) -> cb.conjunction();
    }

    public static Specification<LocationEntity> hasCountry(String country) {
        return (root, query, cb) ->
                (country == null || country.isBlank())
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get("country")), "%" + country.toLowerCase() + "%");
    }

    public static Specification<LocationEntity> hasCity(String city) {
        return (root, query, cb) ->
                (city == null || city.isBlank())
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%");
    }

    public static Specification<LocationEntity> hasType(String typeStr) {
        return (root, query, cb) -> {
            if (typeStr == null || typeStr.isBlank()) {
                return cb.conjunction();
            }

            try {
                LocationType type = LocationType.valueOf(typeStr.toUpperCase());
                return cb.equal(root.get("type"), type);
            } catch (IllegalArgumentException ex) {
                throw new InvalidEnumValueException("type", typeStr, LocationType.class);
            }
        };
    }

}
