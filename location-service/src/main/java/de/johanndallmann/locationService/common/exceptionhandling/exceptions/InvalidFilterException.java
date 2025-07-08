package de.johanndallmann.locationService.common.exceptionhandling.exceptions;

public class InvalidFilterException extends RuntimeException{
    public InvalidFilterException(String message) {
        super(message);
    }
}
