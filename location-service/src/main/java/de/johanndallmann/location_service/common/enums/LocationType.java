package de.johanndallmann.location_service.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LocationType {
    BAR("Bar"),
    RESTAURANT("Restaurant"),
    NATURE("Nature"),
    OTHER("Other");

    private final String displayName;
}
