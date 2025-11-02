package com.lakshya.auction.live_auction_service.ExceptionHandling;

import java.time.Instant;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    // define the properties
    private String message;
    private int status;
    private String error;
    private Instant timeStamp;

    // define the constructor
    public ErrorResponseDTO(String message, int status,String error ,Instant timeStamp){
        this.message =  message;
        this.status = status;
        this.error = error;
        this.timeStamp = timeStamp;
    }
}
