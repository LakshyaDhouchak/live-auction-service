package com.lakshya.auction.live_auction_service.ExceptionHandling;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // define the properties
    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataConflictException(DataConflictException ex){
        ErrorResponseDTO dto = new ErrorResponseDTO(ex.getMessage(), HttpStatus.CONFLICT.value(), "CONFLICT", Instant.now());
        return new ResponseEntity<>(dto,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex){
        ErrorResponseDTO dto = new ErrorResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Instant.now());
        return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BidValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleBidValidationException(BidValidationException ex){
        ErrorResponseDTO dto = new ErrorResponseDTO(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE.value(), "NOT_ACCEPTABLE", Instant.now());
        return new ResponseEntity<>(dto,HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex){
        ErrorResponseDTO dto =  new ErrorResponseDTO(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR", Instant.now());
        return new ResponseEntity<>(dto,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
