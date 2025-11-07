package br.com.fiap.cinefinder.expection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        public record RestError(
                        List<String> messages,
                        int status,
                        LocalDateTime timestamp,
                        String path) {
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<RestError> handleValidationExceptions(
                        MethodArgumentNotValidException e,
                        HttpServletRequest request) {

                var httpError = HttpStatus.BAD_REQUEST;

                var errors = e.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                                .collect(Collectors.toList());

                RestError restError = new RestError(
                                errors,
                                httpError.value(),
                                LocalDateTime.now(),
                                request.getRequestURI());

                return ResponseEntity.status(httpError).body(restError);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<RestError> handleExceptionDefault(RuntimeException e, HttpServletRequest request) {

                var httpError = HttpStatus.NOT_FOUND;

                return ResponseEntity
                                .status(httpError)
                                .body(new RestError(
                                                List.of(e.getMessage()),
                                                httpError.value(),
                                                java.time.LocalDateTime.now(),
                                                request.getRequestURI()));
        }
}
