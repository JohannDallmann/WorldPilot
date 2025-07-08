package de.johanndallmann.locationService.common.exceptionhandling.exceptions;

public class InvalidUserException extends RuntimeException{
    public InvalidUserException(String message) {
        super(message);
    }
}
