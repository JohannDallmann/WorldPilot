package de.johanndallmann.locationService.common.exceptionhandling;

import de.johanndallmann.locationService.common.exceptionhandling.exceptions.InvalidEnumValueException;
import de.johanndallmann.locationService.common.exceptionhandling.exceptions.InvalidFilterException;
import de.johanndallmann.locationService.common.exceptionhandling.exceptions.InvalidUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Format response for specified exception-classes
     */
    @ExceptionHandler({InvalidEnumValueException.class, InvalidFilterException.class, InvalidUserException.class})
    public ResponseEntity<ErrorResponse> handleInvalidValues(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "INVALID_INPUT",
                Instant.now()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
