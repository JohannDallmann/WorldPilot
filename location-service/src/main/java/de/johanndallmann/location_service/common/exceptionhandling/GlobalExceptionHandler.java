package de.johanndallmann.location_service.common.exceptionhandling;

import de.johanndallmann.location_service.common.exceptionhandling.exceptions.InvalidEnumValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEnumValue(InvalidEnumValueException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
