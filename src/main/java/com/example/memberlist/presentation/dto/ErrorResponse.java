package com.example.memberlist.presentation.dto;

import java.util.List;

/**
 * Error response DTO for validation and business logic errors.
 * Includes detailed error messages.
 */
public record ErrorResponse(
        String status,
        String message,
        List<String> errors
) {
    /**
     * Creates an error response with detailed error messages.
     *
     * @param message general error message
     * @param errors  list of specific error messages
     * @return error response
     */
    public static ErrorResponse of(String message, List<String> errors) {
        return new ErrorResponse("error", message, errors);
    }

    /**
     * Creates an error response with a single error message.
     *
     * @param message error message
     * @return error response
     */
    public static ErrorResponse of(String message) {
        return new ErrorResponse("error", message, null);
    }
}
