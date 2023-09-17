package com.samseen.ecommerce.exceptions.handler;

import com.samseen.ecommerce.exceptions.UserNotFoundException;
import com.samseen.ecommerce.exceptions.model.RestError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<RestError> handler(UserNotFoundException unfe) {
        return handle(HttpStatus.NOT_FOUND, unfe);
    }

    public ResponseEntity<RestError> handle(HttpStatus status, Exception e) {
        return handle(status, e.getMessage());
    }

    public ResponseEntity<RestError> handle(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new RestError(message));
    }
}
