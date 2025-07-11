package de.johanndallmann.locationService.location.service;

import de.johanndallmann.locationService.common.enums.LocationType;
import de.johanndallmann.locationService.common.exceptionhandling.exceptions.InvalidEnumValueException;
import de.johanndallmann.locationService.location.repository.LocationEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

/**
 * The static methods of this class are specifications
 * which are used by the JpaRepository to filter LocationEntities.
 * With the specifications boilerplate and sql-statements are avoided.
 */
public class LocationSpecifications {

    /**
     * Is used to base all specifications on a filter which is always true
     * and avoid NullPointerExceptions.
     */
    public static Specification<LocationEntity> initialSpec(){
        return (root, query, cb) -> cb.conjunction();
    }

    /**
     * Create Specification for ownerId, for that a user can only see his own locations.
     * Filter has to be always set.
     */
    public static Specification<LocationEntity> hasOwnerId(UUID ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("OwnerId must not be null");
        }

        return (root, query, cb) -> cb.equal(root.get("ownerId"), ownerId);
    }

    /**
     * Create Specification to filter for provided String with country-attribute.
     * Sets the filter true, if the attribute is not provided or null
     * to avoid NullPointerExceptions.
     */
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

    public static Specification<LocationEntity> hasLocationId(Long locationId) {
        return (root, query, cb) ->
                (locationId == null)
                        ? cb.conjunction()
                        : cb.equal(root.get("id"), locationId);
    }

    /**
     * Filter for provided String with the type-attribute.
     * @param typeStr String-value of the enum LocationType (e.g. "RESTAURANT, "BAR")
     * @return Specification for type-attribute
     * @throws InvalidEnumValueException if the input-String is not a enum-value.
     */
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
