package de.johanndallmann.locationService.common.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String errorCode;
    private Instant timestamp;
}
