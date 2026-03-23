package com.resortmanagement.system.common.exception;

import java.util.UUID;

/**
 * Thrown when an inventory item does not have enough stock to fulfil a request.
 * Maps to HTTP 422 (Unprocessable Entity) via GlobalExceptionHandler.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(UUID inventoryItemId) {
        super("Insufficient stock for inventory item: " + inventoryItemId);
    }

    public InsufficientStockException(String message) {
        super(message);
    }
}
