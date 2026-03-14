/*
ApplicationException.java
Purpose:
 - Base runtime exception for domain errors.
 - Provide constructors for message, cause, and error code.
 - Create specialized exceptions extending it (NoAvailabilityException, InsufficientInventoryException, PaymentFailedException, etc) in their respective packages if needed.

File: common/exception/ApplicationException.java
*/

package com.resortmanagement.system.common.exception;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);
    }
}