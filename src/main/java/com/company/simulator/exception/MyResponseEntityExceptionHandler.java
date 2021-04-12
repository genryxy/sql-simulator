package com.company.simulator.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MyResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDenied(RuntimeException exc, WebRequest request) {
        return handleExceptionInternal(
            exc,
            String.format("Access is denied. \nReason: %s", exc.getMessage()),
            HttpHeaders.EMPTY,
            HttpStatus.FORBIDDEN,
            request
        );
    }

    @ExceptionHandler(value = SqlDropDbException.class)
    protected ResponseEntity<Object> handleAccessDeniedDropDb(RuntimeException exc, WebRequest request) {
        return handleExceptionInternal(
            exc,
            String.format("You are prohibited to remove database. \nReason: %s", exc.getMessage()),
            HttpHeaders.EMPTY,
            HttpStatus.FORBIDDEN,
            request
        );
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(RuntimeException exc, WebRequest request) {
        return handleExceptionInternal(
            exc,
            String.format("Not found. \nReason: %s", exc.getMessage()),
            HttpHeaders.EMPTY,
            HttpStatus.NOT_FOUND,
            request
        );
    }

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeExceptions(
        RuntimeException exc, WebRequest request
    ) {
        return handleExceptionInternal(
            exc,
            String.format(
                "Sorry, something goes wrong. Probably, try later. Reason: %s",
                exc.getMessage()
            ),
            HttpHeaders.EMPTY,
            HttpStatus.INTERNAL_SERVER_ERROR,
            request
        );
    }
}
