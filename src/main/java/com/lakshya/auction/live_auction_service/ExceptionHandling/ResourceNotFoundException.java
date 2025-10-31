package com.lakshya.auction.live_auction_service.ExceptionHandling;

public class ResourceNotFoundException extends RuntimeException {
    // define the constructor
    public ResourceNotFoundException(String s){
        super(s);
    }
}
