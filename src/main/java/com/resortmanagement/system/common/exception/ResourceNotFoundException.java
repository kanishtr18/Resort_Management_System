package com.resortmanagement.system.common.exception;

/**
 * Thrown when a requested resource (entity) cannot be found in the database.
 * Maps to HTTP 404 via GlobalExceptionHandler.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Object id) {
        super(resourceName + " not found with id: " + id);
    }
}
