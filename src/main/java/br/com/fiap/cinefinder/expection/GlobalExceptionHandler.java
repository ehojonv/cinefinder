package br.com.fiap.cinefinder.expection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<RestError> handleInvalidPassword(InvalidPasswordException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new RestError(
                        e.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        java.time.LocalDateTime.now(),
                        "" // You can add the request path here if needed
                ));
    }
}
