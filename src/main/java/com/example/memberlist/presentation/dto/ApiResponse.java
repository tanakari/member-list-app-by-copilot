package com.example.memberlist.presentation.dto;

import java.util.List;

/**
 * Generic API response wrapper.
 * Used to standardize all API responses with status and message.
 *
 * @param <T> the type of data in the response
 */
public record ApiResponse<T>(
        String status,
        String message,
        T data
) {
    /**
     * Creates a success response with data.
     *
     * @param message success message
     * @param data    response data
     * @param <T>     type of data
     * @return success API response
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }

    /**
     * Creates an error response.
     *
     * @param message error message
     * @param <T>     type of data
     * @return error API response
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }
}
