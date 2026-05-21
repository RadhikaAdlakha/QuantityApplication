package com.quantity.measurement.exception;
/**
 * Custom exception for database-related errors.
 * Wraps JDBC exceptions with meaningful messages.
 */
public class DatabaseException extends RuntimeException {

    /**
     * Constructor with message.
     *
     * @param message error message
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause.
     *
     * @param message error message
     * @param cause original exception
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}