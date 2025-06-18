package de.johanndallmann.location_service.common.exceptionhandling.exceptions;

import java.util.Arrays;

public class InvalidEnumValueException extends RuntimeException{
    public InvalidEnumValueException(String fieldName, String value, Class<? extends Enum<?>> enumClass) {
        super(String.format("Invalid value '%s' for field '%s'. Allowed values: %s",
                value, fieldName, Arrays.toString(enumClass.getEnumConstants())));
    }
}
