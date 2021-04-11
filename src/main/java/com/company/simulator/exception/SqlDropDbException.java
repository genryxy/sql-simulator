package com.company.simulator.exception;

public class SqlDropDbException extends RuntimeException {
    public SqlDropDbException(String message) {
        super(message);
    }

    public SqlDropDbException(String message, Throwable cause) {
        super(message, cause);
    }
}

