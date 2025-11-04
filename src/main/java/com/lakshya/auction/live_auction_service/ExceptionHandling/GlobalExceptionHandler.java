
package com.lakshya.auction.live_auction_service.ExceptionHandling;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataConflictException(DataConflictException ex) {
        ErrorResponseDTO dto = new ErrorResponseDTO(
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                "CONFLICT",
                Instant.now()
        );
        return new ResponseEntity<>(dto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponseDTO dto = new ErrorResponseDTO(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                Instant.now()
        );
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BidValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleBidValidationException(BidValidationException ex) {
        ErrorResponseDTO dto = new ErrorResponseDTO(
                ex.getMessage(),
                HttpStatus.NOT_ACCEPTABLE.value(),
                "NOT_ACCEPTABLE",
                Instant.now()
        );
        return new ResponseEntity<>(dto, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed";
        ErrorResponseDTO dto = new ErrorResponseDTO(
                message,
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                Instant.now()
        );
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex) {
        // Log the real exception for debugging
        log.error("Unhandled exception occurred", ex);

        // Hide sensitive message from API users
        ErrorResponseDTO dto = new ErrorResponseDTO(
                "An unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                Instant.now()
        );
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
