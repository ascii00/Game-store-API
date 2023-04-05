package com.store.game.controllers;

import com.store.game.response.ErrorResponse;
import com.store.game.response.FailResponse;
import com.store.game.response.Response;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Response> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(ex.getBindingResult().toString()));
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response> handleConstraintViolation(ConstraintViolationException ex) {
        Set<String> violations = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        return ResponseEntity.status(BAD_REQUEST).body(new FailResponse<>(violations));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(new FailResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Response> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(new FailResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(UNAUTHORIZED).body(new FailResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Response> handleException(Exception e) {
        String message = e.getMessage() == null ? e.toString() : e + " " + e.getMessage();
        message = message + "\n" + Arrays.toString(e.getStackTrace());
        message = message.length() > 1000 ? message.substring(0, 1000) : message;
        System.out.println(6);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(message));
    }
}
